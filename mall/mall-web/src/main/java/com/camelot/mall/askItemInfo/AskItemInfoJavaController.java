package com.camelot.mall.askItemInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;























import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.InquiryOrderDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.dto.TranslationOrderDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.askItemInfoService.AskItemInfoService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.searchcenter.dto.SearchBrand;
import com.camelot.searchcenter.dto.SearchCategory;
import com.camelot.searchcenter.dto.SearchItemAttr;
import com.camelot.searchcenter.dto.SearchItemSku;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;


//求购类
@Controller
@RequestMapping("/askItemInfoJavaController")
public class AskItemInfoJavaController {
	@Resource
	private TranslationExportService translationExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private AskItemInfoService askItemInfoService;
		/**
		 * 新增求购单信息
		 * @author chengwt 创建时间：下午2:26:12
		 * @param request
		 */
	    @ResponseBody
	    @RequestMapping("/addTranslationInfo")
		public String addTranslationInfo(TranslationInfoDTO translationInfoDTO , String beginDate1, String endDate1 , String deliveryDate1,String deleteId, String[] category_ids, String[] ids,  String[] itemNames , String[] nums , String[] flag , String[] matAttributes ,HttpServletRequest request ){
	    	Map<String, String> resultMap = new HashMap<String, String>();
	    	String ctoken = LoginToken.getLoginToken(request);
	    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
	    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
			if (register == null) {
				return "redirect:/user/login";
			}
			//查询用户信息
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
	    	//将获取到的前台的
	    	translationInfoDTO.setStatus("0");
	    	translationInfoDTO.setActiveFlag("1");
	    	SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
	    	try {
	    		translationInfoDTO.setBeginDate(sp.parse(beginDate1));
	    		translationInfoDTO.setEndDate(sp.parse(endDate1));
	    		translationInfoDTO.setDeliveryDate(sp.parse(deliveryDate1));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "保存失败！时间转换错误";
			}
	    	resultMap = askItemInfoService.addTranslationInfo(translationInfoDTO, ""+userId, deleteId, ids, category_ids, itemNames, nums, flag, matAttributes);
    		return resultMap.get("result");
		}
	    
	    
	    /**
	    	 * 获取求购明细信息,订单明细信息
	    	 * @author chengwt 创建时间：下午5:42:50
	    	 * @param request
	    	 */
	    @RequestMapping(value = "/getDetaiInfo")
	    @ResponseBody
		public String getDetaiInfo( HttpServletRequest request) {
			String translationNo = request.getParameter("translationNo");
			String flag = request.getParameter("flag");
			TranslationMatDTO dto = new TranslationMatDTO();
			TranslationOrderDTO orderDTO = new TranslationOrderDTO();
			TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
			translationInfoDTO.setTranslationNo(translationNo);
			translationInfoDTO.setActiveFlag("1");
			dto.setTranslationNo(translationNo);
			dto.setActiveFlag("1");
			orderDTO.setTranslationNo(translationNo);
			orderDTO.setActiveFlag("1");
			dto.setActiveFlag("1");
			//各自的卖家只能看到自己的报价信息
			if("response".equals(flag)){
				String ctoken = LoginToken.getLoginToken(request);
		    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
				if (register == null) {
					return "redirect:/user/login";
				}
				//查询用户信息
				Long userId = WebUtil.getInstance().getUserId(request);
				UserDTO userDTO = userExportService.queryUserById(userId);
				dto.setShopId(userDTO.getShopId().intValue());
			}
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(5);
			//查询求购明细
			ExecuteResult<DataGrid<Map>> translationDTOList =  translationExportService.queryTranslationInfoPager(translationInfoDTO, pager);
			ExecuteResult<DataGrid<Map>> detailDTOList =  translationExportService.queryTranslationMatList(dto, pager);			
			//查询订单明细
			ExecuteResult<DataGrid<TranslationOrderDTO>>  resultOrder = translationExportService.queryTranslationOrderList(orderDTO, pager);
			List<TranslationMatDTO> matDTOs = new ArrayList<TranslationMatDTO>();
			String result1 = "";
			String result2 = "";
			List<Map> mapList = new ArrayList<Map>();
			if(detailDTOList.getResult().getTotal() > 0){
				mapList = detailDTOList.getResult().getRows();
			}else{
				mapList = translationDTOList.getResult().getRows();
			}
			//取出map放入list中，前台展示用
			if(mapList.size() > 0){
				for(Map dtoItem : mapList){
					result1 = result1 + "<tr class='font_cen bg_05'><td class='border-1 wid_50'><input type='checkbox' name='itemDetail' value='"+dtoItem.get("id")+"' checked/><input type='hidden' name='detailPrice' value='"+dtoItem.get("matPrice")+"' />"
							+ "<input type='hidden' name='detailName' value='"+dtoItem.get("matDesc")+"'/></td>";
					//查询供应商名称
					if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))){
						ExecuteResult<ShopDTO> shopRe = shopExportService.findShopInfoById(Long.parseLong(""+dtoItem.get("shopId")));
						result1 = result1 + "<td class='border-1 wid_120' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+shopRe.getResult().getShopName()+"'>"+shopRe.getResult().getShopName()+"</td>";
						result1 = result1 + "<td class='border-1 wid_120' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+dtoItem.get("cName")+"'>"+dtoItem.get("cName")+"</td>";
						result1 = result1 + "<td class='border-1 wid_120' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+dtoItem.get("matDesc")+"'>"+dtoItem.get("matDesc")+"</td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_120'> </td>";
						result1 = result1 + "<td class='border-1 wid_120' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+dtoItem.get("cName")+"'>"+dtoItem.get("cName")+"</td>";
						result1 = result1 + "<td class='border-1 wid_120' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+dtoItem.get("matCd")+"'>"+dtoItem.get("matCd")+"</td>";
					}
					result1 = result1 + "<td class='border-1 wid_120' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;' title='"+dtoItem.get("matAttribute")+"'>"+dtoItem.get("matAttribute")+"</td>";
					if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId")) && "1".equals(""+dtoItem.get("status"))){
						result1 = result1 + "<td class='border-1 wid_80'>已接受</td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_80'>未接受</td>";
					}
					result1 = result1 + "<td class='border-1 wid_80'>"+dtoItem.get("quantity")+"</td>";
					//拼接前台的html
					if(dtoItem.get("matPrice") != null && !"".equals(dtoItem.get("matPrice")) && !"null".equals(dtoItem.get("matPrice"))){
						result1 = result1 + "<td class='border-1 wid_80'>"+dtoItem.get("matPrice")+"</td>";
					}else{
						result1 = result1 + "<td class='border-1 wid_80'> </td>";
					}
					result1 = result1 + "</tr>";
				}
			}
			//取出map放入list中，前台展示用
			if(resultOrder.isSuccess()){
			
				for(TranslationOrderDTO orderList : resultOrder.getResult().getRows()){
					//拼接前台的html
					result2 = result2 + "<tr class='font_cen bg_05'><td class='border-1 wid_180'>"+translationNo+"</td>";
					result2 = result2 + "<td class='border-1 wid_200'>"+orderList.getOrderNo()+"</td>";
					result2 = result2 + "</tr>";
				}
			}
			return result1+"%"+result2;
		}
	    
	    /**
		 *求购单删除，将active字段置为0
		 * @author chengwt 创建时间：下午7:33:36
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/deleteTranslation")
	@ResponseBody
	public String deleteTranslation(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
    		return "redirect:/user/login";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		TranslationInfoDTO dto = new TranslationInfoDTO();
		TranslationMatDTO dtoDetail = new TranslationMatDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = askItemInfoService.deleteTranslation(""+userId, ids, dto);
		return resultMap.get("result");
	}
	
	/**
		 * 求购单提交，将status字段置为1
		 * @author chengwt 创建时间：上午9:45:34
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/commitTranslation")
	@ResponseBody
	public String commitTranslation(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
    		return "redirect:/user/login";
    	}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids");
		TranslationInfoDTO dto = new TranslationInfoDTO();
		TranslationMatDTO dtoDetail = new TranslationMatDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = askItemInfoService.commitTranslation(""+userId, dto, ids);
		request.setAttribute("addResult", resultMap);
		request.setAttribute("ids", ids);
    	request.setAttribute("userId", userId);
		return resultMap.get("result");
	}
	
	/**
	 * 暂未使用
	 * @author chengwt 创建时间：上午9:45:34
	 * @param model
	 * @param request
	 * @param response
	 */
@RequestMapping("/auditTranslation")
@ResponseBody
public String auditTranslation(Model model, HttpServletRequest request,
		HttpServletResponse response){
	String ctoken = LoginToken.getLoginToken(request);
	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
	RegisterDTO register = userExportService.getUserByRedis(ctoken);
	if (register == null) {
		return "redirect:/user/login";
	}
	//查询用户信息
	Long userId = WebUtil.getInstance().getUserId(request);
	UserDTO userDTO = userExportService.queryUserById(userId);
	String ids = request.getParameter("ids");
	TranslationInfoDTO dto = new TranslationInfoDTO();
	Map<String, String> resultMap = new HashMap<String, String>();
	resultMap = askItemInfoService.auditTranslation(""+userId, dto, ids);
	return resultMap.get("result");
}

	
	
	/**
		 * 卖家保存报价信息，此时生成求购单明细
		 * @author chengwt 创建时间：上午10:35:35
		 * @param request
		 */
	@ResponseBody
    @RequestMapping("/saveRepTranslationInfo")
	public String saveRepTranslationInfo(TranslationInfoDTO translationInfoDTO  , String[] ids, String[] price, String[] addFlag, String[] matCd1, String[] nums,String[] detailstartDate , String[] detailendDate , String[] matAttributes, String[] category_ids, HttpServletRequest request ){
		Map<String, String> resultMap = new HashMap<String, String>();
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		resultMap = askItemInfoService.saveRepTranslationInfo(""+userId, translationInfoDTO, ids, price, addFlag, matCd1, nums, detailstartDate, detailendDate, matAttributes, category_ids);
		TranslationInfoDTO infoDTO = new TranslationInfoDTO();
		infoDTO.setTranslationNo(translationInfoDTO.getTranslationNo());
		ExecuteResult<TranslationInfoDTO> executeResult = translationExportService.queryByTranslationInfo(infoDTO);
		if (executeResult.getResult() != null) {
			request.setAttribute("userId", executeResult.getResult().getCreateBy());
		}
		request.setAttribute("sellerId", userId);
		request.setAttribute("addResult", resultMap);
		return resultMap.get("result");
	}
	
	
	/**
		 * 卖家确认价格，暂未使用
		 * @author chengwt 创建时间：下午3:17:23
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/commitRepTranslationInfo")
	@ResponseBody
	public String commitRepTranslationInfo(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ids = request.getParameter("ids");
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		TranslationInfoDTO dto = new TranslationInfoDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = askItemInfoService.commitRepTranslationInfo(""+userId, dto, ids);
		return resultMap.get("result");
	}
	
	
	/**
		 * 重新求购，将求购单status字段置为2
		 * @author chengwt 创建时间：下午3:07:38
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/commitTranslationRe")
	@ResponseBody
	public String commitTranslationRe(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ids = request.getParameter("ids");
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		TranslationInfoDTO dto = new TranslationInfoDTO();
		TranslationMatDTO dtoDetail = new TranslationMatDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = askItemInfoService.commitTranslationRe(""+userId, dto, ids);
		return resultMap.get("result");
	}
	
	
	/**
		 * 买家确认求购单信息，将status字段置为3
		 * @author chengwt 创建时间：上午10:31:38
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/acceptTranslationInfo")
	@ResponseBody
	public String acceptTranslationInfo(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		String ids = request.getParameter("ids"); 
		String detailId = request.getParameter("detailId"); 
		TranslationInfoDTO dto = new TranslationInfoDTO();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = askItemInfoService.acceptTranslationInfo(""+userId, dto, ids, detailId);
		request.setAttribute("detailId", detailId);
		request.setAttribute("userId", userId);
		request.setAttribute("addResult", resultMap);
		return resultMap.get("result");
	}
}
