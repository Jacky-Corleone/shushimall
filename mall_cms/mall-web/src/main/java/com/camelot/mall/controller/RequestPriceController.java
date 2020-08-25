package com.camelot.mall.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.lang.model.element.PackageElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.goodscenter.dto.*;
import com.camelot.mall.requestPrice.RequestPriceJavaController;

import com.camelot.openplatform.util.SysProperties;
import org.apache.log4j.Logger;
import org.apache.poi.util.SystemOutLogger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.common.CommonService;
import com.camelot.mall.common.CommonServiceImpl;
import com.camelot.mall.shopcart.Shop;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

//询价类
@Controller
@RequestMapping("/requestPriceController")
public class RequestPriceController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private CommonServiceImpl commonServiceImpl;
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private MallRecExportService mallRecService;
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private CommonService commonService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopExportService;

	private RequestPriceJavaController requestPriceJavaController;
	/**
	 * 跳转到买家询价单主页
	 * @author 成文涛 创建时间：2015-5-28 下午3:21:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/requestPrice")
	public String goRequestPrice(Pager pager,Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	// 当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	
		if (register == null) {
			return "redirect:/user/login";
		}
		//没有认证通过的用户，不支持此功能
		if(register.getUserType() == 1){
			return "/no_authentication";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("uid", userDTO.getUid());
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(userDTO.getUid());
		List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setActiveFlag("1");
//		inquiryInfoDTO.setCreateBy(""+userDTO.getUid());
		inquiryInfoDTO.setCreateBys(idsEr.getResult());
//		pager.setRows(5);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		//查询询价单主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
		
		if( er.isSuccess() && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){
				
			//	JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
				UserDTO seller = this.userExportService.queryUserById(Long.parseLong(price.getCreateBy()));
				if(seller == null || seller.getCompanyName()== null || "null".equals(seller.getCompanyName())){
					price.setAlternate3("公司名称为空");
				}else{
					price.setAlternate3(seller.getCompanyName());
				}
				priceArr.add(price);
			}
			
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( priceArr );
		}
		model.addAttribute("pager", pager);
		return "/requestPrice/requestPricePage";
	}

	/**
	 * 选择跳转到询价单创建页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/requestPriceCreate")
	public String goRequestPriceCreate(	Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		//调用dubbo生成询价单编号
		ExecuteResult<String> inquiryNo =  inquiryExportService.createInquiryNo();
		//李伟龙注释掉
//		UserDTO userDTO = userExportService.queryUserById(userId);
		//李伟龙新增   判断是否为子账号，并且查询信息
		 UserDTO  buyercx = userExportService.queryUserById(userId);//查询是否是父账号
	     UserDTO userDTO=null;
		  if(buyercx.getParentId()==null){
			  userDTO = userExportService.queryUserById(userId);//是父账号查询内容
		  }else{
			  userDTO = userExportService.queryUserByfId(userId);//子账号查询内容
		  }
		
		
		model.addAttribute("uName", userDTO.getCompanyName());
		model.addAttribute("uid", userDTO.getUid());
		if(inquiryNo.isSuccess()){
			model.addAttribute("inquiryNo", inquiryNo.getResult());
		}
		model.addAttribute("annex", "null");
		return "/requestPrice/requestPriceCreatePage";
	}
	
	/**
	 * 跳转到货品选择页面
	 * @author 成文涛 创建时间：2015-6-17 下午5:40:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/ggoodsListPrice")
	public String goGoodsListPage(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String number = request.getParameter("number");
		model.addAttribute("number", number);
		
		
		return "/requestPrice/goodslistPage";
	}

	/**
	 * 选择产品大全界面
	 * @param request
	 * @param model
	 * @return
	 */
	   @RequestMapping("/productsListPrice")
	   	public String categoryItems( Pager<ItemQueryOutDTO> pager,String cid , HttpServletRequest request,Model model ) {
		    String itemName = request.getParameter("itemName");
	    	JSONArray categoryes = this.commonService.findCategory();
//	    	try {
//	    		if(itemName!=null && !"".equals(itemName)){
//	    			itemName = new String(itemName.getBytes("ISO-8859-1"),"utf8");
//	    		}
//				
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
	    	model.addAttribute("categoryes", categoryes);
	    	logger.debug("ALL CATEGORYES:" +  categoryes.toJSONString());

	    	Long id = null;
	    	if( cid != null && !"".equals(cid) ){
	    		if( cid.indexOf(":") != -1 ){
	        		String tmp = cid.substring(cid.lastIndexOf(":")+1, cid.length());
	        		id = Long.valueOf(tmp);
	    		}else{
	    			id = Long.valueOf(cid);
	    		}
	    	}
	    	logger.debug("产品大全-商品列表查询参数："+id);
	    	pager.setRows(30);
	    	ExecuteResult<DataGrid<ItemQueryOutDTO>>  er = this.itemService.queryItemByCidAndName(id, itemName ,pager);
	    	String itemNames = "";
	    	if(er.isSuccess()){
	        	pager.setTotalCount(er.getResult().getTotal().intValue());
	        	List<ItemQueryOutDTO> itemQueryOutDTO = er.getResult().getRows();
	        	pager.setRecords(itemQueryOutDTO);
//	        	for(ItemQueryOutDTO dto : itemQueryOutDTO){
//	        		itemNames += dto.getItemName() + ",";
//	        	}
	    	}
//	    	model.addAttribute("itemNames",itemNames);
	    	model.addAttribute("pager", pager);
	    	model.addAttribute("cid", cid);
	    	String itemId = request.getParameter("itemId");
	    	model.addAttribute("itemId", itemId);
	    	model.addAttribute("itemName", itemName);
	    	return "/requestPrice/productsListPage";
	    }
	
	
	/**
		 * 跳往询价修改界面
		 * @author chengwt 创建时间：下午4:07:38
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/requestPriceUpdate")
	public String goRequestPriceUpdate(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		String dtoNo = request.getParameter("inquiryNo");
		String result = "/requestPrice/requestPricePage";
		InquiryInfoDTO dto = new InquiryInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setInquiryNo(dtoNo);
		dto.setActiveFlag("1");
		//查询询价单主单信息
		ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryDTOS =  inquiryExportService.queryInquiryInfoList(dto, pager);
		pager.setRows(Integer.MAX_VALUE);
		//查询询价单主单信息(未去重)，当做明细在页面展示产品大全名称信息
		ExecuteResult<DataGrid<Map>> inquiryDTOList =  inquiryExportService.queryInquiryInfoPager(dto, pager);
		if(inquiryDTOS.isSuccess()){
			InquiryInfoDTO inquiryInfoDTO = inquiryDTOS.getResult().getRows().get(0);
			model.addAttribute("inquiryNo", inquiryInfoDTO.getInquiryNo());
			model.addAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("printerId", inquiryInfoDTO.getPrinterId());
			UserDTO userDTO = userExportService.queryUserById(Long.valueOf(inquiryInfoDTO.getCreateBy()));
			model.addAttribute("uName", userDTO.getCompanyName());
			model.addAttribute("uid", userDTO.getUid());
			model.addAttribute("printerName", userDTO.getCompanyName());
			model.addAttribute("supplierId", inquiryInfoDTO.getSupplierId());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(inquiryInfoDTO.getAnnex() == null ||  "".equals(inquiryInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != inquiryInfoDTO.getAnnex()) {
					fileName = inquiryInfoDTO.getAnnex().substring(inquiryInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", inquiryInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", inquiryInfoDTO.getBeginDate());
			model.addAttribute("endDate", inquiryInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", inquiryInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", inquiryInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			//取出map放入list中，前台展示用
			int i = 1;
			for(Map mapp : inquiryDTOList.getResult().getRows()){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("id", ""+mapp.get("id"));
				map.put("matCd", ""+mapp.get("matCd"));
				map.put("matDesc", ""+mapp.get("itemName"));
				map.put("deliveryDates", ""+mapp.get("deliveryDate2"));
				map.put("quantity", ""+mapp.get("quantity"));
				map.put("shopId",  ""+mapp.get("supplierId"));
				
				//获取销售属性
				Map<String, String> mapAttr = new HashMap<String, String>();
				mapAttr.put("shopId","" + mapp.get("supplierId"));
				mapAttr.put("skuId","" + mapp.get("skuId"));
				mapAttr.put("itemId","" + mapp.get("matCd"));
				mapAttr = putSalerAttr(mapAttr);
				map.put("attrsDesc",mapAttr.get("salerAttr"));
				//根据店铺id查询名称
				ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong(""+mapp.get("supplierId")));
				map.put("shopName",  shop.getResult().getShopName());
				map.put("flag", "u");
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
			result = "/requestPrice/requestPriceCreatePage";
		}
		return result;
	}
	
	/**
		 * 跳往卖家询价报价界面
		 * @author chengwt 创建时间：上午10:06:20
		 * @param model
		 * @param request
		 * @param response
		 */
	@RequestMapping("/responsePrice")
	public String goresponsePrice(Pager pager,Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
		UserDTO seller = this.userExportService.queryUserById(userId);
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		inquiryInfoDTO.setActiveFlag("1");
		inquiryInfoDTO.setSupplierId(seller.getShopId().intValue());
		List<String> list = new ArrayList<String>();
		list.add("2");
		list.add("3");
		inquiryInfoDTO.setStatusList(list);
		pager.setRows(5);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		//查询询价单主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
		if( er.isSuccess() && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){
				
				JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
				UserDTO buyer = this.userExportService.queryUserById(Long.parseLong(price.getCreateBy()));
				if(buyer == null || buyer.getCompanyName()== null || "null".equals(buyer.getCompanyName())){
					price.setAlternate3("公司名称为空");
				}else{
					price.setAlternate3(buyer.getCompanyName());
				}
				price.setAlternate2(seller.getCompanyName());
				priceArr.add(price);
			}
			
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( priceArr );
		}
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("shopId", userDTO.getShopId());
		model.addAttribute("companyName", userDTO.getCompanyName());
		model.addAttribute("pager", pager);
		
		
		return "/requestPrice/responsePricePage";
	}



	/**
	 * 跳往询价单卖家报价页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/responsePriceUpdate")
	public String goResponsePriceUpdate(Model model, HttpServletRequest request,
			HttpServletResponse response){
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		String dtoNo = request.getParameter("inquiryNo");
		String result = "/requestPrice/responsePricePage";
		InquiryInfoDTO dto = new InquiryInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setInquiryNo(dtoNo);
		dto.setActiveFlag("1");
		//查询询价主单信息
		ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryDTOS =  inquiryExportService.queryInquiryInfoList(dto,pager);
		//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
		InquiryMatDTO detailDTO = new InquiryMatDTO();
		detailDTO.setActiveFlag("1");
		detailDTO.setInquiryNo(dtoNo);
		UserDTO seller = this.userExportService.queryUserById(userId);
		detailDTO.setShopId(seller.getShopId().intValue());
		//暂时把人员公司关系注释
		//detailDTO.setSupplierId(11);
		pager.setRows(Integer.MAX_VALUE);
		//查询询价单明细信息
		ExecuteResult<DataGrid<Map>> inquiryDTOList =  inquiryExportService.queryInquiryMatList(detailDTO, pager);
		if(inquiryDTOS.isSuccess()){
			InquiryInfoDTO inquiryInfoDTO = inquiryDTOS.getResult().getRows().get(0);
			model.addAttribute("inquiryId", inquiryInfoDTO.getId());
			model.addAttribute("inquiryNo", inquiryInfoDTO.getInquiryNo());
			model.addAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("printerName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("printerId", inquiryInfoDTO.getPrinterId());
			model.addAttribute("supplierId", inquiryInfoDTO.getSupplierId());
			UserDTO userDTO = userExportService.queryUserById(Long.valueOf(inquiryInfoDTO.getCreateBy()));
			model.addAttribute("uName", userDTO.getCompanyName());
			model.addAttribute("uid", userDTO.getUid());
			model.addAttribute("printerName", userDTO.getCompanyName());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(inquiryInfoDTO.getAnnex() == null ||  "".equals(inquiryInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != inquiryInfoDTO.getAnnex()) {
					fileName = inquiryInfoDTO.getAnnex().substring(inquiryInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", inquiryInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", inquiryInfoDTO.getBeginDate());
			model.addAttribute("endDate", inquiryInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", inquiryInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", inquiryInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			int i = 1;
			//取出map放入list中，前台展示用
			for(Map dtoItem : inquiryDTOList.getResult().getRows()){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("matCd", ""+dtoItem.get("matCd"));
				map.put("matDesc", ""+dtoItem.get("itemName"));
				map.put("cName", ""+dtoItem.get("cName"));
				map.put("quantity",  ""+dtoItem.get("quantity"));
				if("null".equals(""+dtoItem.get("matPrice"))){
					map.put("matPrice",  "");
				}else{
					map.put("matPrice",  ""+dtoItem.get("matPrice"));
				}
				//获取销售属性
				Map<String, String> mapAttr = new HashMap<String, String>();
				mapAttr.put("shopId","" + dtoItem.get("shopId"));
				mapAttr.put("skuId","" + dtoItem.get("matCd"));
				mapAttr.put("itemId","" + dtoItem.get("itemId"));
				Map<String, String> saleAttr = this.putSalerAttr(mapAttr);
				map.put("salerAttr", saleAttr.get("salerAttr"));
				map.put("detailStartDate", ""+dtoItem.get("beginDate"));
				map.put("detailEndDate", ""+dtoItem.get("endDate"));
				map.put("id", ""+dtoItem.get("id"));
				map.put("deliveryDates", ""+dtoItem.get("deliveryDate2"));
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
			result = "/requestPrice/responsePriceUpdatePage";
		}
		return result;
	}

	/**获取商品属性
	 * @param contractMats
	 * @return
	 */
	public Map<String, String> putSalerAttr(Map contractMats) {
		ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setAreaCode("0");
		dto.setShopId(Long.valueOf(contractMats.get("shopId").toString()));
		dto.setSkuId(Long.valueOf(contractMats.get("skuId").toString()));
		dto.setItemId(Long.valueOf(contractMats.get("itemId").toString()));
		ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
		ItemShopCartDTO itemShopCartDTO = er.getResult();
		if (null != itemShopCartDTO) {
			contractMats.put("skuPicUrl", itemShopCartDTO.getSkuPicUrl());
			//商品属性
			String skuName = "";
			for (ItemAttr itemAttr : itemShopCartDTO.getAttrSales()) {
				skuName += itemAttr.getName();
				for (ItemAttrValue itemAttrValue : itemAttr.getValues()) {
					skuName += ":" + itemAttrValue.getName() + ";";
				}
			}
			contractMats.put("salerAttr", skuName);
		}
		return contractMats;
	}

	/**
	 * 暂未使用
	 * @param model
	 * @return
	 */
	@RequestMapping("/getSellerBuyerDetail")
	public String getPartBDetail(
			Pager<UserDTO> page, Model model, Integer uType) {
		UserType userType = UserType.getEnumBycode(uType);
		DataGrid<UserDTO> users=new DataGrid<UserDTO>();
		
		if (userType != null) {
			 users = userExportService
					.findUserListByCondition(null, userType, page);
		}
		page.setTotalCount(users.getTotal().intValue());
		page.setRecords(users.getRows());
		model.addAttribute("userList", users.getRows());
		model.addAttribute("page", page);
		
		return "/requestPrice/requestPartBDetail";

	}
	
	/**
	 * 查询询价单信息
	 * @author chengwt 创建时间：下午2:26:12
	 * @param model
	 * @param request
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping("/queryRequestPrice")
	public String queryRequestPrice(Pager pager,Model model, HttpServletRequest request,
			HttpServletResponse response) throws ParseException{
		String ctoken = LoginToken.getLoginToken(request);
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		String inquiryName = request.getParameter("inquiryName");
		if(inquiryName != null && !"null".equals(inquiryName) && !"".equals(inquiryName)) {
			try {
				inquiryName = URLDecoder.decode(inquiryName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				inquiryName = "";
			}
		}
		String supplierId = request.getParameter("supplierId");
		String supplierName = request.getParameter("supplierName");
		if(supplierName != null && !"null".equals(supplierName) && !"".equals(supplierName)) {
			try {
				supplierName = URLDecoder.decode(supplierName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				supplierName = "";
			}
		}
		String itemName = request.getParameter("itemName");
		if(itemName != null && !"null".equals(itemName) && !"".equals(itemName)) {
			try {
				itemName = URLDecoder.decode(itemName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				itemName = "";
			}
		}
		
		String contractStatus = request.getParameter("contractStatus");
		if(contractStatus != null && !"null".equals(contractStatus) && !"".equals(contractStatus)) {
			try {
				contractStatus = URLDecoder.decode(contractStatus, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				contractStatus = "";
			}
		}
		
		String startTime = request.getParameter("startTime");
	
		if(startTime != null && !"null".equals(startTime) && !"".equals(startTime)) {
			try {
				startTime = URLDecoder.decode(startTime, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				startTime = "";
			}
		}
		
		String endTime = request.getParameter("endTime");
		if(endTime != null && !"null".equals(endTime) && !"".equals(endTime)) {
			try {
				endTime = URLDecoder.decode(endTime, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				endTime = "";
			}
		}
		
//		
		
		String createBy = request.getParameter("createBy");
		List<InquiryInfoDTO> dtos = new ArrayList<InquiryInfoDTO>();
		InquiryInfoDTO inquiryInfoDTO = new InquiryInfoDTO();
		//是否卖家查询标志
		String sellerFlag = request.getParameter("sellerFlag");
		if("1".equals(sellerFlag)){
			createBy = null;
			//卖家查询则只能查询卖家自己的报价信息
			inquiryInfoDTO.setSupplierId(Integer.parseInt(supplierId));
		}else{
			if(!"".equals(supplierName)){
				ShopDTO shopDTO = new ShopDTO();
				shopDTO.setShopName(supplierName);
				ExecuteResult<DataGrid<ShopDTO>> shopList = shopExportService.findShopInfoByCondition(shopDTO, pager);
				List<String> supplierIdList = new ArrayList<String>();
				if(shopList.getResult().getTotal() == 0){
					supplierIdList.add("1");
				}else{
					for(ShopDTO shop : shopList.getResult().getRows()){
						supplierIdList.add(""+shop.getShopId());
					}
				}
				inquiryInfoDTO.setSupplierIdList(supplierIdList);
			}
			inquiryInfoDTO.setCreateBy(createBy);
		}
		inquiryInfoDTO.setInquiryName(inquiryName);
		inquiryInfoDTO.setActiveFlag("1");
		//
		inquiryInfoDTO.setItemName(itemName);
		inquiryInfoDTO.setStatus(contractStatus);



		inquiryInfoDTO.setStartTime(startTime);
		inquiryInfoDTO.setEndTime(endTime);
//		pager.setRows(5);
		ExecuteResult<DataGrid<InquiryInfoDTO>> er= new ExecuteResult<DataGrid<InquiryInfoDTO>>();
		//查询询价单主单信息
		er = inquiryExportService.queryInquiryInfoList(inquiryInfoDTO, pager);
		if( er.isSuccess() && er.getResult() != null ){
			JSONArray priceArr = new JSONArray();
			for( InquiryInfoDTO price: er.getResult().getRows() ){
				
				JSONObject sp = JSON.parseObject(JSON.toJSONString(price));
				UserDTO seller = this.userExportService.queryUserById(Long.parseLong(price.getCreateBy()));
				if(seller == null || seller.getCompanyName()== null || "null".equals(seller.getCompanyName())){
					price.setAlternate3("公司名称为空");
				}else{
					price.setAlternate3(seller.getCompanyName());
				}
				priceArr.add(price);
			}
			
			pager.setTotalCount(er.getResult().getTotal().intValue());
			pager.setRecords( priceArr );
		}
		
		model.addAttribute("pager", pager);
		model.addAttribute("sellerFlag", sellerFlag);
		model.addAttribute("contractStatus", contractStatus);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		
		
		return "/requestPrice/requestPriceModelPage";
	}

	/**
	 * 暂未使用
	 * @author chengwt 创建时间：下午2:26:12
	 * @param model
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getShopList")
	public String getShopList(Pager pager,Model model, HttpServletRequest request,
			HttpServletResponse response){
		String itemId = request.getParameter("itemId");
		String itemName = request.getParameter("itemName");
		ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
		itemInDTO.setPlatItemId(Long.parseLong(itemId));
		//只查询在售商品
		itemInDTO.setItemStatus(Constants.ITEM_IS_SELL_STATUS);
		//选择商品供应商时候不分页
		pager.setRows(Integer.MAX_VALUE);
		DataGrid<ItemQueryOutDTO> shopList = itemService.queryItemList(itemInDTO, pager);
		//查询店铺名字
		List<ShopDTO> shops = new ArrayList<ShopDTO>();
		Long l = 1L;
		for(ItemQueryOutDTO out : shopList.getRows()){
			if(out.getShopId() != null ){
    			ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(out.getShopId().longValue());
    			ShopDTO shopDto = new ShopDTO();
    			shopDto.setShopId(out.getShopId());
    			shopDto.setShopName(shop.getResult().getShopName());
    			shopDto.setLinkMan1(out.getItemName());
    			shopDto.setLinkMan2(out.getItemId() + "");
    			shopDto.setCid(l);
    			shops.add(shopDto);
			}
			l++;
		}
		pager.setRecords(shops);
		pager.setTotalCount(shops.size());
		model.addAttribute("pager", pager);
		model.addAttribute("itemId", itemId);
		model.addAttribute("itemName", itemName);
		
		return "/requestPrice/shopListPage";
	}

	/**
	 * 跳往询价单查看页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/requestPriceLook")
	public String requestPriceLook(Model model, HttpServletRequest request,
									   HttpServletResponse response,String userType){
		String ctoken = LoginToken.getLoginToken(request);
		//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		if (register == null) {
			return "redirect:/user/login";
		}
		//查询用户信息
		Long userId = WebUtil.getInstance().getUserId(request);
		String dtoNo = request.getParameter("inquiryNo");
		String flag = request.getParameter("flag");
		String result = "/requestPrice/responsePricePage";
		InquiryInfoDTO dto = new InquiryInfoDTO();
		Pager pager = new Pager();
		pager.setRows(1);
		dto.setInquiryNo(dtoNo);
		dto.setActiveFlag("1");
		//查询主单信息
		ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryDTOS =  inquiryExportService.queryInquiryInfoList(dto,pager);
		//查询明细中的supplyID为登陆人所属的supplyId相同的才展示
		InquiryMatDTO detailDTO = new InquiryMatDTO();
		detailDTO.setActiveFlag("1");
		detailDTO.setInquiryNo(dtoNo);
		//如果为卖家进入查看界面则只能查看自己的报价信息
		if("response".equals(flag)) {
			UserDTO seller = this.userExportService.queryUserById(userId);
			detailDTO.setShopId(seller.getShopId().intValue());
		}
		//暂时把人员公司关系注释
		//detailDTO.setSupplierId(11);
		pager.setRows(Integer.MAX_VALUE);
		//查询询价单明细信息
		ExecuteResult<DataGrid<Map>> inquiryDTOList =  inquiryExportService.queryInquiryMatList(detailDTO, pager);
		if(inquiryDTOS.isSuccess()){
			InquiryInfoDTO inquiryInfoDTO = inquiryDTOS.getResult().getRows().get(0);
			model.addAttribute("inquiryId", inquiryInfoDTO.getId());
			model.addAttribute("inquiryNo", inquiryInfoDTO.getInquiryNo());
			model.addAttribute("inquiryName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("printerName", inquiryInfoDTO.getInquiryName());
			model.addAttribute("printerId", inquiryInfoDTO.getPrinterId());
			model.addAttribute("supplierId", inquiryInfoDTO.getSupplierId());
			UserDTO userDTO = userExportService.queryUserById(Long.valueOf(inquiryInfoDTO.getCreateBy()));
			model.addAttribute("uName", userDTO.getCompanyName());
			model.addAttribute("uid", userDTO.getUid());
			model.addAttribute("printerName", userDTO.getCompanyName());
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			if(inquiryInfoDTO.getAnnex() == null ||  "".equals(inquiryInfoDTO.getAnnex())){
				model.addAttribute("annex", "null");
			}else{
				String fileName = null;
				if (null != inquiryInfoDTO.getAnnex()) {
					fileName = inquiryInfoDTO.getAnnex().substring(inquiryInfoDTO.getAnnex().lastIndexOf("/") + 1);
				}
				model.addAttribute("fileName", fileName);
				model.addAttribute("annex", inquiryInfoDTO.getAnnex());
			}
			model.addAttribute("beginDate", inquiryInfoDTO.getBeginDate());
			model.addAttribute("endDate", inquiryInfoDTO.getEndDate());
			model.addAttribute("deliveryDate", inquiryInfoDTO.getDeliveryDate());
			model.addAttribute("remarks", inquiryInfoDTO.getRemarks());
			List<Map> inquiryMatDTOs = new ArrayList<Map>();
			Map<String, String> map = new HashMap<String, String>();
			int i = 1;
			//取出map放入list中，前台展示用
			if(inquiryDTOList.getResult().getTotal() < 1){
				inquiryDTOList =  inquiryExportService.queryInquiryInfoPager(dto, pager);
			}
			for(Map dtoItem : inquiryDTOList.getResult().getRows()){
				map = new HashMap<String, String>();
				map.put("no", ""+i);
				map.put("matCd", ""+dtoItem.get("matCd"));
				map.put("matDesc", ""+dtoItem.get("itemName"));
				map.put("cName", ""+dtoItem.get("cName"));
				map.put("quantity",  ""+dtoItem.get("quantity"));
				if("null".equals(""+dtoItem.get("matPrice"))){
					map.put("matPrice",  "");
				}else{
					map.put("matPrice",  ""+dtoItem.get("matPrice"));
				}
				//获取销售属性
				Map<String, String> mapAttr = new HashMap<String, String>();
				if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))) {
					mapAttr.put("shopId", "" + dtoItem.get("shopId"));
				}else{
					mapAttr.put("shopId", "" + dtoItem.get("supplierId"));
					dtoItem.put("shopId", dtoItem.get("supplierId"));
				}
				mapAttr.put("skuId","" + dtoItem.get("skuId"));
				mapAttr.put("itemId","" + dtoItem.get("itemId"));
				Map<String, String> saleAttr = this.putSalerAttr(mapAttr);
				map.put("salerAttr", saleAttr.get("salerAttr"));
				//获取shop名称
				if(dtoItem.get("shopId") != null && !"".equals(dtoItem.get("shopId")) && !"null".equals(dtoItem.get("shopId"))) {
					ExecuteResult<ShopDTO> shop = shopExportService.findShopInfoById(Long.parseLong("" + dtoItem.get("shopId")));
					map.put("shopName", shop.getResult().getShopName());
				}
				
				map.put("detailStartDate", ""+dtoItem.get("beginDate"));
				map.put("detailEndDate", ""+dtoItem.get("endDate"));
				map.put("id", "" + dtoItem.get("id"));
				map.put("deliveryDates", "" + dtoItem.get("deliveryDate2"));
				inquiryMatDTOs.add(map);
				i += 1;
			}
			model.addAttribute("details", inquiryMatDTOs);
			model.addAttribute("update_flag", "1");
			result = "/requestPrice/requestPriceLookPage";
		}
		model.addAttribute("flag",flag);
		model.addAttribute("userType",userType);
		return result;
	}
	/**
	 * 查询商品sku
	 * @param request
	 * @param itemIds		商品ids
	 * @param shopIds		店铺id
	 * @param ulLength		排列序号
	 * @param shopNames		店铺名称
	 * @param itemName		商品名称
	 * @param tempDate		
	 * @return
	 */
	@RequestMapping(value = "/getSkuByItemIds")
	@ResponseBody
	public String getSkuByItemIds(HttpServletRequest request,String itemIds , String shopIds,String ulLength ,String shopNames, String itemName,String tempDate) {
		int xh =Integer.parseInt(ulLength);
		String itemIds_arry[] = itemIds.split(",");
		String shopIds_arry[] = shopIds.split(",");
		String shopNames_arry[] = shopNames.split(",");
		String result = "";
		String dateSlect = "WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});";
		//数组游标
		int index = 0;
		String skuId ="";
		for(String itemId : itemIds_arry){
			//获取商品信息
			ExecuteResult<ItemDTO> er= itemService.getItemById(Long.valueOf(itemId));
			if(er != null){
				List<SkuInfo> list = er.getResult().getSkuInfos();
				if(list != null){
					for(int i = 0;i< list.size();i++){
						skuId = list.get(i).getSkuId().toString();
						//获取销售属性
						Map<String, String> mapAttr = new HashMap<String, String>();
						mapAttr.put("shopId", shopIds_arry[index]);
						mapAttr.put("skuId", skuId);
						mapAttr.put("itemId", itemId);
						mapAttr = putSalerAttr(mapAttr);
						//拼接返回的页面代码
						result +="<ul class='list_top hei_40 font_cen' name='ul'>"+
								"<li class='wid_40'>" + (xh +i) + "<input type='hidden' style='display:none' name= 'skuId' value='"+skuId+"'/>"+"</li>"+
		        				"<li class='wid_140' title='"+er.getResult().getItemName()+"' style='white-space: nowrap;text-overflow:ellipsis; overflow:hidden;'>"+ 
		        				"<input type='hidden' name= 'flag' value=''/>"+
		        				"<input type='hidden' name= 'matDesc' value='"+er.getResult().getItemName()+"'/> "+er.getResult().getItemName()+"</li>"+
		        				"<li class='wid_140 over_ell' title='"+mapAttr.get("salerAttr")+"'><input type='hidden' value='"+mapAttr+"'/> "+mapAttr.get("salerAttr")+"</li>"+
		                        "<li class='wid_190 over_ell' title='"+shopNames_arry[index]+"'> "+
		        				"<input type='hidden' name = 'shopId' value='"+shopIds_arry[index]+"'/>"+shopNames_arry[index]+"</li>"+
		                   		"<li class='wid_190 font_cen'> <input type='text' name = 'deliveryDates' class='input_Style2' value='"+tempDate+"'  onclick="+dateSlect+"  readOnly/> </li>"+
		                  		"<li class='wid_180 font_cen'> <input type='text'  maxlength='11' class='input_Style2' onkeypress='number()' onkeyup='filterInput()' onchange='filterInput()' onbeforepaste='filterPaste()' onpaste='return false' name = 'nums' value=''/> </li>"+
		        				"<li class='wid_60 font_cen font_7a delete' style='cursor: pointer;'><input type='hidden' name = 'detailId'  value='"+itemId+"'/> <input type='hidden' name = 'matCd' value='"+itemId+"'/>删除</li></ul>";
						 //记录最后一个序号
						if(i == (list.size() -1)){
							xh = xh +i;
						}
					}
					xh ++;
				}
			}
			index ++;
		}
		if(!"".equals(result)){
			result = "&_&" + result + "^_^";
		}
		return result;
	}
	
}
