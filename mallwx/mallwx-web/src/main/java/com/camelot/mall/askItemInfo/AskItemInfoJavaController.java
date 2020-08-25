package com.camelot.mall.askItemInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.mall.askItemInfoWX.AskItemInfoWXService;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.SendWeiXinMessageUtil;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;
/**
 * 
 * <p>Description: [描述该类概要功能介绍:求购管理，对求购信息进行创建、查询、待提交、待接收]</p>
 * Created on 2016年1月27日
 * @author  <a href="mailto: zihanmin@camelotchina.com">訾瀚民</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/askItemInfoJavaController")
public class AskItemInfoJavaController {
	@Resource
	private TranslationExportService translationExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private AskItemInfoWXService askItemInfoWXService;
	@Resource
	private ShopExportService shopExportService;
	
	private Logger LOG = Logger.getLogger(this.getClass());
	
	 /**
	  * 
	  * <p>Discription:[方法功能中文描述：创建求购单信息]</p>
	  * Created on 2016年1月27日
	  * @param translationDTO
	  * @param beginDate1
	  * @param endDate1
	  * @param deliveryDate1
	  * @param category_ids
	  * @param itemNames
	  * @param nums
	  * @param matAttributes
	  * @param request
	  * @param model
	  * @return returnStr
	  * @author:[chengwt]
	  */
    @ResponseBody
    @RequestMapping("/addTranslation")
	public String addTranslation(TranslationInfoDTO translationDTO , String beginDate1,  String endDate1,  String deliveryDate1,String[] category_ids, String[] itemNames, String[] nums, String[] matAttributes, HttpServletRequest request, Model model ){
    	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
    	
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	String returnStr = "保存成功!";
    	//供发送提醒消息使用
    	request.setAttribute("result", result);
    	
    	//数据后台验证
    	if(translationDTO.getPrinterId()== null){
    		return "保存失败！印刷厂信息不全";
    	}
    	SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
    	try {
			Date beginDate = sm.parse(""+beginDate1);
			Date endDate = sm.parse(""+endDate1);
			Date deliveryDate = sm.parse(""+deliveryDate1);
			translationDTO.setBeginDate(beginDate);
			translationDTO.setEndDate(endDate);
			translationDTO.setDeliveryDate(deliveryDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return "保存失败，时间转换报错！";
		}
    	translationDTO.setActiveFlag("1");
    	translationDTO.setCreateBy(""+userId);
    	translationDTO.setCreateDate(new Date());
    	translationDTO.setStatus("0");
    	Map<String,String> resultMap = askItemInfoWXService.addTranslation(translationDTO, category_ids, itemNames, nums, matAttributes);
    	returnStr = resultMap.get("result");
		return returnStr;
	} 
    /**
     * 
     * <p>Discription:[方法功能中文描述：求购单删除，将active字段置为0]</p>
     * Created on 2016年1月27日
     * @param request
     * @param model
     * @return
     * @author:[chengwt]
     */
    
    @ResponseBody
    @RequestMapping("/deleteTranslation")
	public String deleteTranslation(HttpServletRequest request, Model model ){
    	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	String translationId = request.getParameter("translationNo");
    	String returnStr = "删除成功!";
    	//供发送提醒消息使用
    	request.setAttribute("result", result);
    	TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
    	translationInfoDTO.setTranslationNo(translationId);
    	Map<String,String> resultMap = askItemInfoWXService.deleteTranslation(translationInfoDTO, ""+userId);
    	returnStr = resultMap.get("result");
		return returnStr;
	}
    
    
    /**
     * 
     * <p>Discription:[方法功能中文描述：更新求购主单信息，deleteIds根据逗号分隔需要删除的求购单，ids数据记录的是需要修改的求购单的id]</p>
     * Created on 2016年1月27日
     * @param dto
     * @param beginDate1
     * @param endDate1
     * @param deliveryDate1
     * @param deleteIds
     * @param ids
     * @param category_ids
     * @param itemNames
     * @param nums
     * @param statusDetail
     * @param matAttributes
     * @param request
     * @param model
     * @return
     * @author:[chengwt]
     */
    
	@ResponseBody
	@RequestMapping("/updateTranslation")
	public String updateTranslation(TranslationInfoDTO dto , String beginDate1 , String endDate1 , String deliveryDate1 , String deleteIds , String[] ids, String[] category_ids,String[] itemNames, String[] nums, String[] statusDetail,  String[] matAttributes ,HttpServletRequest request, Model model ){
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if(register==null){
			return "redirect:/";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		ExecuteResult<String> result = new ExecuteResult<String>();
		String returnStr = "保存成功!";
		//供发送提醒消息使用
		request.setAttribute("result", result);
		String[] deleteId = deleteIds.split(",");
		SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dto.setBeginDate(sp.parse(beginDate1));
			dto.setEndDate(sp.parse(endDate1));
			dto.setDeliveryDate(sp.parse(deliveryDate1));
		} catch (ParseException e) {
			e.printStackTrace();
			return "保存失败！时间转换错误";
		}
		
		Map<String,String> resultMap = askItemInfoWXService.updateTranslation(""+userId, dto, deleteId, ids, category_ids, itemNames, nums, statusDetail, matAttributes);
		returnStr = resultMap.get("result");
		return returnStr;
	}

	
    /**
     * 
     * <p>Discription:[方法功能中文描述：求购单提交，status字段置为1]</p>
     * Created on 2016年1月27日
     * @param request
     * @param model
     * @return
     * @author:[chengwt]
     */
	
    @ResponseBody
    @RequestMapping("/commitTranslation")
	public String commitTranslation(HttpServletRequest request, Model model ){
    	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
    	String translationNo = request.getParameter("translationNo");
    	String returnStr = "求购成功!";
    	//供发送提醒消息使用
    	TranslationInfoDTO dto = new TranslationInfoDTO();
    	dto.setStatus("1");
		dto.setActiveFlag("1");
		dto.setTranslationNo(translationNo);
		dto.setUpdateBy("" + userId);
		dto.setUpdateDate(new Date());
		dto.setTranslationNo(translationNo);
		Map<String, String > result = new HashMap<String, String>();
		Pager pager = new Pager();
		pager.setRows(Integer.MAX_VALUE);
		pager.setPage(1);
		result = askItemInfoWXService.commitTranslation(""+userId, dto,  pager);
		return returnStr;
	}
    
    
 
    /**
     * 
     * <p>Discription:[方法功能中文描述：求购审核（暂时不用）]</p>
     * Created on 2016年1月27日
     * @param request
     * @param model
     * @return
     * @author:[chengwt]
     */
	@ResponseBody
	@RequestMapping("/auditTranslation")
	public String auditTranslation(HttpServletRequest request, Model model ){
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if(register==null){
			return "redirect:/";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		String translationNo = request.getParameter("translationNo");
		
		//供发送提醒消息使用
		TranslationInfoDTO dto = new TranslationInfoDTO();
		dto.setStatus("2");
		dto.setActiveFlag("1");
		dto.setTranslationNo(translationNo);
		dto.setUpdateBy(""+userId);
		dto.setUpdateDate(new Date());
		dto.setTranslationNo(translationNo);
		Map<String, String > result = new HashMap<String, String>();
		result = askItemInfoWXService.auditTranslation(dto, ""+userId);
		return result.get("result");
	}
    
    

	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：卖家保存求购信息，生成求购单明细]</p>
	 * Created on 2016年1月27日
	 * @param translationInfoDTO
	 * @param flag_status
	 * @param ids
	 * @param itemNames
	 * @param quantitys
	 * @param price
	 * @param detailstartDate
	 * @param detailendDate
	 * @param matAttributes
	 * @param category_ids
	 * @param request
	 * @param response
	 * @return
	 * @author:[chengwt]
	 */
    @ResponseBody 
    @RequestMapping("/repModifyTranslation")
	public String repModifyTranslation(	TranslationInfoDTO translationInfoDTO  ,String flag_status[], String[] ids, String[] itemNames,String[] quantitys, String[] price, String[] detailstartDate , String[] detailendDate ,String[] matAttributes, String[] category_ids, HttpServletRequest request,HttpServletResponse response){
    	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
    	String message = "success";
    	
    	//将获取到的前台的
    	//translationInfoDTO.setStatus("3");
    	message = "保存成功";
    	Map<String,String> resultMap = askItemInfoWXService.resopnseModifyTranslation(translationInfoDTO, ""+userId, flag_status, ids, itemNames, quantitys, price, detailstartDate, detailendDate,matAttributes, category_ids);
    	message = resultMap.get("result");
    	try {
    		LOG.info("----------------------您有一条求购报价提醒-----------------------------");
        	SendWeiXinMessage weiXinMessage = new SendWeiXinMessage();
        	weiXinMessage.setModeId(WeiXinMessageModeId.ASK_ITEM);
        	weiXinMessage.setFirst("您有一条求购报价提醒。");
        	weiXinMessage.setKeyword1(translationInfoDTO.getTranslationNo());
        	weiXinMessage.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        	ExecuteResult<TranslationInfoDTO> queryByTranslationInfo = translationExportService.queryByTranslationInfo(translationInfoDTO);
    		SendWeiXinMessageUtil.sendWeiXinMessage(queryByTranslationInfo.getResult().getCreateBy(), weiXinMessage, request, response);
		} catch (Exception e) {
			LOG.info("微信推送短信失败",e);
		}
    	return message;
	}
    
    /**
     * 
     * <p>Discription:[方法功能中文描述:卖家确认求购信息（暂时不用）]</p>
     * Created on 2016年1月27日
     * @param request
     * @param model
     * @return
     * @author:[chengwt]
     */
    
    
	@ResponseBody 
	@RequestMapping("/commitRepTranslation")
	public String commitRepTranslation(	HttpServletRequest request, Model model ){
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if(register==null){
			return "redirect:/";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		String translationNo = request.getParameter("translationNo");
		String returnStr = "求购确认成功!";
		//供发送提醒消息使用
		TranslationInfoDTO dto = new TranslationInfoDTO();
		dto.setStatus("4");
		dto.setActiveFlag("1");
		dto.setTranslationNo(translationNo);
		dto.setUpdateBy(""+userId);
		dto.setUpdateDate(new Date());
		//根据编号查询其下的物品id
		Map<String,String> resultMap = askItemInfoWXService.commitRepTranslation(dto, "" + userId);
		returnStr = resultMap.get("result");
		return returnStr;
	}
    
    

	 /**
	  * 
	  * <p>Discription:[方法功能中文描述:买家接受报价信息，status字段置为3]</p>
	  * Created on 2016年1月27日
	  * @param request
	  * @param model
	  * @return
	  * @author:[chengwt]
	  */
	
	
    @ResponseBody 
    @RequestMapping("/commitRequestTranslation")
	public String commitRequestTranslation(HttpServletRequest request, Model model ){
    	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
    	String translationNo = request.getParameter("translationNo");
    	String detailIds = request.getParameter("detailIds");
    	String[] detailId = detailIds.split(",");
    	String returnStr = "求购确认成功!";
    	//供发送提醒消息使用
    	TranslationInfoDTO dto = new TranslationInfoDTO();
		dto.setActiveFlag("1");
		dto.setTranslationNo(translationNo);
		Map<String,String> resultMap = askItemInfoWXService.commitRequestTranslation(dto, ""+userId, detailId);
		returnStr = resultMap.get("result");
		return returnStr;
	}
    
    
    
    /**
     * 
     * <p>Discription:[方法功能中文描述:买家重新求购，主单status字段置为2，明细表status字段滞空]</p>
     * Created on 2016年1月27日
     * @param request
     * @param model
     * @return
     * @author:[chengwt]
     */
    
    
    @ResponseBody
    @RequestMapping("/commitTranslationRe")
	public String commitTranslationRe(HttpServletRequest request, Model model ){
    	String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "redirect:/";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
    	String translationNo = request.getParameter("translationNo");
    	String returnStr = "操作成功!";
    	//供发送提醒消息使用
    	TranslationInfoDTO dto = new TranslationInfoDTO();
		dto.setActiveFlag("1");
		dto.setTranslationNo(translationNo);
		Map<String,String> resultMap = askItemInfoWXService.commitTranslationRe(dto, ""+userId);
		returnStr = resultMap.get("result");
		return returnStr;
	}
    
}
