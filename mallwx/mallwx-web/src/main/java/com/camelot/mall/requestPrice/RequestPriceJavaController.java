package com.camelot.mall.requestPrice;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.InquiryMatDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.mall.requestPriceWX.RequestPriceWXService;
import com.camelot.mall.shopcart.Product;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.SendWeiXinMessageUtil;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;



//询价类
@Controller
@RequestMapping("/requestPriceJavaController")
public class RequestPriceJavaController {
	@Resource
	private InquiryExportService inquiryService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private RequestPriceWXService requestPriceWXService;
	@Resource
	private RedisDB redisDB;
	
	private Logger LOG = Logger.getLogger(this.getClass());
    /**
    	 * 创建询价单
    	 * @author chengwt 创建时间：上午9:37:32
    	 * @param model
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/addInquiry")
	public String addInquiry(InquiryInfoDTO inquiryInfoDTO , String beginDate1,  String endDate1,  String deliveryDate1, String[] shopId,String[] itemIds, String[] nums, String[] deliveryDates ,String[] skuIds,HttpServletRequest request, Model model ){
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	Long userId = WebUtil.getInstance().getUserId(request);
    	String returnStr = "保存成功!";
    	//供发送提醒消息使用
    	request.setAttribute("result", result);
    	List<String> errs = new ArrayList<String>();
//    	String ctoken = LoginToken.getLoginToken(request);
//    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	/*if(register==null){
    		errs.add("未找到登录用户");
    		result.setErrorMessages(errs);
    		return result;
    	}*/
    	/*String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		return "保存失败，买家不是认证用户！";
    	}
    	Integer status = register.getuStatus();
    	if(status !=4 && status != 6 ){
    		return "保存失败，买家不是认证用户！";
    	}*/
    	//数据后台验证
    	if(inquiryInfoDTO.getPrinterId() == null || "".equals(inquiryInfoDTO.getPrinterId()) 
    			|| "null".equals(inquiryInfoDTO.getPrinterId())){
    		return "保存失败！印刷厂信息不全";
    	}
    	SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String beginDatec=beginDate1.substring(0, 4)+"-"+beginDate1.substring(5, 7)+"-"+beginDate1.substring(8, 10); 

    	try {
			Date beginDate = sm.parse(""+beginDatec);
			Date endDate = sm2.parse(""+endDate1+" 23:59:59");
			Date deliveryDate = sm.parse(""+deliveryDate1);
			inquiryInfoDTO.setBeginDate(beginDate);
			inquiryInfoDTO.setEndDate(endDate);
			inquiryInfoDTO.setDeliveryDate(deliveryDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return "保存失败，时间转换报错！";
		}
    	inquiryInfoDTO.setActiveFlag("1");
    	inquiryInfoDTO.setCreateBy(""+userId);
    	inquiryInfoDTO.setCreateDate(new Date());
    	inquiryInfoDTO.setStatus("1");
    	//李伟龙注释调
    	//Map<String,String> resultMap = requestPriceWXService.addInquiry(inquiryInfoDTO, shopId,itemIds, nums, deliveryDates);
    	//调用新增询价的新方法，多增加skuid
    	Map<String,String> resultMap = requestPriceWXService.addInquiryNew(inquiryInfoDTO, shopId,itemIds, nums, deliveryDates,skuIds);
    	
    	returnStr = resultMap.get("result");
		return returnStr;
	}
    
  /*  @InitBinder  
    public void initBinder(ServletRequestDataBinder binder) throws Exception {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        CustomDateEditor dateEditor = new CustomDateEditor(df, true);  
        binder.registerCustomEditor(Date.class, dateEditor);      
    }*/
    
    /**
    	 * 询价单删除，将数据的active_flag字段置为0
    	 * @author chengwt 创建时间：上午9:49:30
    	 * @param model
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/deleteInquiry")
	public String deleteInquiry(
			HttpServletRequest request, Model model ){
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	String inquiryId = request.getParameter("inquiryNo");
    	String returnStr = "删除成功!";
    	//供发送提醒消息使用
    	request.setAttribute("result", result);
    	List<String> errs = new ArrayList<String>();
    	InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
    	inquiryInfoDTO.setActiveFlag("0");
    	inquiryInfoDTO.setUpdateBy(""+userId);
    	inquiryInfoDTO.setUpdateDate(new Date());
    	inquiryInfoDTO.setInquiryNo(inquiryId);
    	result = inquiryService.modifyInquiryInfo(inquiryInfoDTO);
    	if(!result.isSuccess()){
    		returnStr = "删除失败!"+result.getErrorMessages().get(0);
    	}
		return returnStr;
	}
    
    /**
    	 * 询价提交，status字段改为2，生成询价明细信息（根据用户勾选的店铺生成）
    	 * @author chengwt 创建时间：上午9:49:49
    	 * @param model
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/commitInquiry")
	public String commitInquiry(HttpServletRequest request,HttpServletResponse response, Model model ){
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	String inquiryId = request.getParameter("inquiryNo");
    	String returnStr = "询价成功!";
    	//供发送提醒消息使用
    	List<String> errs = new ArrayList<String>();
    	InquiryInfoDTO dto = new InquiryInfoDTO();
    	dto.setStatus("2");
		dto.setActiveFlag("1");
		dto.setInquiryNo(inquiryId);
		dto.setUpdateBy(""+userId);
		dto.setUpdateDate(new Date());
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		Map<String,String> resultMap = requestPriceWXService.commitInquiry(dto, pager, ""+userId);
		
		InquiryInfoDTO infoDto = new InquiryInfoDTO();
		infoDto.setInquiryNo(inquiryId);
		infoDto.setActiveFlag("1");
		Pager<InquiryInfoDTO> page = new Pager<InquiryInfoDTO>();
		page.setPage(1);
		page.setRows(Integer.MAX_VALUE);
		ExecuteResult<DataGrid<Map>> er = inquiryExportService.queryInquiryInfoPager(infoDto, page);
		List<Long> ids = new ArrayList<Long>();
		if(null!=er && er.isSuccess()){
			DataGrid<Map> result = er.getResult();
			List<Map> rows = result.getRows();
			for(Map map : rows){
				Integer supplierId = (Integer) map.get("supplierId");//获取店铺id
				ids.add(Long.valueOf(supplierId));
			}
			//根据店铺id获取账号
			UserDTO udto = new UserDTO();
			udto.setShopIds(ids.toArray(new Long[ids.size()]));
			Pager<UserDTO> p = new Pager<UserDTO>();
			p.setPage(1);
			p.setRows(Integer.MAX_VALUE);
			DataGrid<UserDTO> dg = userExportService.queryUserListByCondition(udto, null, null, p);
			if(dg!=null){
				List<UserDTO> rows2 = dg.getRows();
				Long uid = rows2.get(0).getUid();
				LOG.info("-------------您有一个新询价单------------------");
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.REQUEST_PRICE_NOTICE);
				message.setFirst("【印刷家】尊敬的用户，您有一个新询价单，印刷家提醒您及时查看。");
				message.setKeyword1(userDTO.getUname());
				message.setKeyword2(null);
				message.setKeyword3(null);
				SendWeiXinMessageUtil.sendWeiXinMessage(uid.toString(), message, request, response);
			}
		}
		
		returnStr = resultMap.get("result");
		return returnStr;
	}
    
    /**
    	 * 更新询价信息，此时修改的信息实际都是主单的信息（还未生成询价单明细）
    	 * @author chengwt 创建时间：下午6:35:27
    	 * @param model
    	 * @param request
	     * @param deleteIds 用逗号分隔的需要删除的询价单，此时由于没有生成明细信息，实际操作是将主单信息删除，即active字段置为0
	     * @param detailId 记录的是需要修改信息的询价单信息，
    	 */
    @ResponseBody
    @RequestMapping("/updateInquiry")
	public String updateInquiry(InquiryInfoDTO inquiryInfoDTO , String beginDate1 , String endDate1 , String deliveryDate1 , String deleteIds , String[] detailId, String[] itemIds, String[] shopId, String[] nums, String[] statusDetail, String[] deliveryDates,String[] skuIds ,HttpServletRequest request, Model model ){
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	String returnStr = "保存成功!";
    	//供发送提醒消息使用
    	request.setAttribute("result", result);
    	List<String> errs = new ArrayList<String>();
    	String[] deleteId = deleteIds.split(",");
    	SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sp2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String beginDatec=beginDate1.substring(0, 4)+"-"+beginDate1.substring(5, 7)+"-"+beginDate1.substring(8, 10); 

    	try {
			inquiryInfoDTO.setBeginDate(sp.parse(beginDatec));
			inquiryInfoDTO.setEndDate(sp2.parse(endDate1+" 23:59:59"));
			inquiryInfoDTO.setDeliveryDate(sp.parse(deliveryDate1));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "保存失败！时间转换错误";
		}
//    	String ctoken = LoginToken.getLoginToken(request);
//    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	/*if(register==null){
    		errs.add("未找到登录用户");
    		result.setErrorMessages(errs);
    		return result;
    	}*/
    	inquiryInfoDTO.setStatus("1");
//    	Map<String,String> resultMap = requestPriceWXService.updateInquiry(""+userId, inquiryInfoDTO, deleteId, detailId, itemIds, shopId, nums, statusDetail, deliveryDates);
    	Map<String,String> resultMap = requestPriceWXService.updateInquiryNew(""+userId, inquiryInfoDTO, deleteId, detailId, itemIds, shopId, nums, statusDetail, deliveryDates,skuIds);
    	returnStr = resultMap.get("result");
    	return returnStr;
	}
    
    
    /**
    	 * 卖家确认询价信息
    	 * @author chengwt 创建时间：上午10:11:11
    	 * @param model
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/commitResponseInquiry")
	public String commitResponseInquiry(HttpServletRequest request, Model model ){
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	String inquiryId = request.getParameter("inquiryNo");
    	String returnStr = "询价确认成功!";
    	//供发送提醒消息使用
    	List<String> errs = new ArrayList<String>();
    	InquiryInfoDTO dto = new InquiryInfoDTO();
    	dto.setStatus("4");
		dto.setActiveFlag("1");
		dto.setInquiryNo(inquiryId);
		dto.setUpdateBy(""+userId);
		dto.setUpdateDate(new Date());
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		//根据编号查询其下的物品id
		ExecuteResult<String>  result = inquiryExportService.modifyInquiryInfo(dto);
		if(!result.isSuccess()){
			returnStr = "询价确认失败!"+result.getErrorMessages();
		}
		return returnStr;
	}
    
    /**
    	 * 卖家保存报价信息
    	 * @author chengwt 创建时间：上午10:36:26
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/resopnseModifyInquiry")
	public String resopnseModifyInquiry(InquiryInfoDTO inquiryInfoDTO  , String[] detailId, String[] price, String[] nums ,String[] detailstartDate , String[] detailendDate , HttpServletRequest request,HttpServletResponse response ){
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	String message = "success";
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	ExecuteResult<String> resultDetail = new ExecuteResult<String>();
    	//将获取到的前台的
    	//inquiryInfoDTO.setStatus("3");
    	inquiryInfoDTO.setSupplierId(userDTO.getShopId().intValue());
    	message = "保存成功";
    	Map<String,String> resultMap = requestPriceWXService.resopnseModifyInquiry(inquiryInfoDTO, ""+userId, detailId, price, nums, detailstartDate, detailendDate);
    	message = resultMap.get("result");
    	
    	InquiryInfoDTO dto = new InquiryInfoDTO();
    	dto.setActiveFlag("1");
    	dto.setSupplierId(userDTO.getShopId().intValue());
    	dto.setInquiryNo(inquiryInfoDTO.getInquiryNo());
    	Pager<InquiryInfoDTO> page = new Pager<InquiryInfoDTO>();
    	page.setPage(1);
    	page.setRows(Integer.MAX_VALUE);
    	ExecuteResult<DataGrid<Map>> er = inquiryExportService.queryInquiryInfoPager(inquiryInfoDTO, page);
    	if(er!=null && er.isSuccess()){
    		DataGrid<Map> result2 = er.getResult();
    		List<Map> rows = result2.getRows();
    		for(Map map : rows){
    			String itemName = (String) map.get("itemName");
    			LOG.info("--------------------卖家对您发布的询价单进行了报价----------------------------------");
    			SendWeiXinMessage weixinMessage = new SendWeiXinMessage();
    			weixinMessage.setModeId(WeiXinMessageModeId.REQUEST_PRICE_REC);
    			weixinMessage.setFirst("【印刷家】尊敬的用户，有卖家对您发布的询价单进行了报价，印刷家提醒您及时查看。");
    			weixinMessage.setKeyword1(itemName);
    			weixinMessage.setKeyword2(null);
    			weixinMessage.setKeyword3(null);
    			weixinMessage.setKeyword4(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    			InquiryInfoDTO dto1 = new InquiryInfoDTO();
    			dto.setInquiryNo(inquiryInfoDTO.getInquiryNo());
    			ExecuteResult<DataGrid<InquiryInfoDTO>> queryInquiryInfoList = inquiryExportService.queryInquiryInfoList(dto1, page);
    			SendWeiXinMessageUtil.sendWeiXinMessage(queryInquiryInfoList.getResult().getRows().get(0).getCreateBy(), weixinMessage, request, response);
    		}
    	}
    	
    	return message;
	}
    
    
    /**
    	 * 买家接收卖家报价，将status字段置为3
    	 * @author chengwt 创建时间：下午4:59:49
    	 * @param model
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/commitRequestInquiry")
	public String commitRequestInquiry(HttpServletRequest request, Model model ){
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	String inquiryId = request.getParameter("inquiryNo");
    	String detailIds = request.getParameter("detailIds");
    	String[] detailId = detailIds.split(",");
    	String returnStr = "询价接收成功!";
    	List<String> errs = new ArrayList<String>();
    	InquiryInfoDTO dto = new InquiryInfoDTO();
    	dto.setStatus("3");
		dto.setActiveFlag("1");
		dto.setInquiryNo(inquiryId);
		dto.setUpdateBy(""+userId);
		dto.setUpdateDate(new Date());
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		Map<String,String> resultMap = requestPriceWXService.commitRequestInquiry(dto, ""+userId, detailId);
		returnStr = resultMap.get("result");
		return returnStr;
	}


    /**
    	 * 买家重新询价，将status字段置为2
    	 * @author chengwt 创建时间：下午5:15:15
    	 * @param model
    	 * @param request
    	 */
    @ResponseBody
    @RequestMapping("/commitnquiryRe")
	public String commitnquiryRe(HttpServletRequest request, Model model ){
    	Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
    	String inquiryId = request.getParameter("inquiryNo");
    	String returnStr = "操作成功!";
    	//供发送提醒消息使用
    	List<String> errs = new ArrayList<String>();
    	InquiryInfoDTO dto = new InquiryInfoDTO();
    	dto.setStatus("2");
		dto.setActiveFlag("1");
		dto.setInquiryNo(inquiryId);
		dto.setUpdateBy("" + userId);
		dto.setUpdateDate(new Date());
		Map<String,String> resultMap =  requestPriceWXService.commitnquiryRe(dto, ""+userId);
		returnStr = resultMap.get("result");
		return returnStr;
	}

	/**
	 * 买家下单
	 * @author chengwt 创建时间：下午5:15:15
	 * @param model
	 * @param request
	 */
	@ResponseBody
	@RequestMapping("/createOrderInfo")
	public String createOrderInfo(HttpServletRequest request, Model model ){
		//如果单据状态为2，则将状态改为3
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String inquiryId = request.getParameter("inquiryNo");
		String detailIds = request.getParameter("detailIds");

		String status = request.getParameter("status");
		String ids = request.getParameter("ids");
		String nums = request.getParameter("nums");
		String[] detailId = detailIds.split(",");
		String returnStr = "success";
		List<String> errs = new ArrayList<String>();
		if("2".equals(status)) {
			InquiryInfoDTO dto = new InquiryInfoDTO();
			dto.setStatus("3");
			dto.setActiveFlag("1");
			dto.setInquiryNo(inquiryId);
			dto.setUpdateBy("" + userId);
			dto.setUpdateDate(new Date());
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			//如果买家未接收直接下单，需要将询价单状态修改为已接收，选中的明细的status字段置为1
			Map<String, String> resultMap = requestPriceWXService.commitRequestInquiry(dto, "" + userId, detailId);
			if(!"操作成功!".equals(resultMap.get("result"))){
				return "下单失败!"+resultMap.get("result");
			}
		}
		//查询物品相关信息，将信息存入redis中
		String[] idList = ids.split(",");
		String[] numList = nums.split(",");
		List<Product> productList = new ArrayList<Product>();
		for(int i=0 ; i < idList.length; i++) {
			InquiryMatDTO inquiryMatDTO = new InquiryMatDTO();
			inquiryMatDTO.setId(Long.parseLong(idList[i]));
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<Map>> inquiryDTOList = inquiryExportService.queryInquiryMatList(inquiryMatDTO, pager);
			for(Map mapp : inquiryDTOList.getResult().getRows()){
				Product product = new Product();
				product.setCid(Long.parseLong("" + mapp.get("cid")));
				product.setQuantity(Integer.parseInt(numList[i]));
				product.setSkuPrice(new BigDecimal("" + mapp.get("matPrice")));
				product.setSkuId(Long.parseLong("" + mapp.get("matCd")));
				product.setItemId(Long.parseLong("" + mapp.get("itemId")));
				product.setShopId(Long.parseLong("" + mapp.get("shopId")));
				product.setSellerId(Long.parseLong("" + mapp.get("supplierId")));
				product.setRegionId("0");
				productList.add(product);
			}
		}
		String jsonProducts = JSONArray.toJSONString(productList);
		this.redisDB.set(userId+"Redis", jsonProducts);
		return returnStr;
	}

}
