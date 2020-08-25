package com.camelot.mall.controller;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.basecenter.service.BaseConsultingSmsService;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.mall.Constants;
import com.camelot.mall.common.CommonService;
import com.camelot.mall.service.FavouriteService;
import com.camelot.mall.service.ItemInfoService;
import com.camelot.mall.service.ShopCartService;
import com.camelot.mall.shopcart.Product;
import com.camelot.mall.shopcart.ShopDeliveryType;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.service.ShopCartFreightService;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopDeliveryTypeDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import com.camelot.storecenter.dto.emums.ShopFreightTemplateEnum;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopDeliveryTypeService;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFavouriteExportService;
import com.camelot.storecenter.service.ShopFreightTemplateService;
import com.camelot.storecenter.service.ShopPreferentialWayService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.StationUtil;

/**
 *
 * <p>Description: [商品页面]</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/product")
public class ProductController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private ItemInfoService itemInfoService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private PromotionService promotionService; // maketcenter 营销活动
	@Resource
	private CommonService commonService;
	@Resource
	private ItemSkuInquiryPriceExportService itemSkuInquiryPriceExportService;
	@Resource
	private ShopExportService shopExportService;
	
	@Resource
	private ShopFavouriteExportService shopFavouriteService;
	@Resource
	private ShopCartService shopCartService;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemSkuInquiryPriceExportService inquiryService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private BaseConsultingSmsService consultService;
	@Resource
	private ItemFavouriteExportService itemFavouriteService;
	@Resource
	private ItemEvaluationService itemEvaluationService;
	@Resource
	private FavouriteService favouriteService;
	@Resource
	private ShopEvaluationService shopEvaluationService;
	@Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;
	
	//
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private ShopFreightTemplateService shopFreightTemplateService;
	@Resource
    private ShopPreferentialWayService shopPreferentialWayService;
	
	@Resource
	private ShopDeliveryTypeService shopDeliveryTypeService;
	@Resource
	private AddressInfoService addressInfoService;
	@Resource
    private ShopCartFreightService shopCartFreightService;


	
	@RequestMapping("toDetail")
	public String toDetail(HttpServletResponse response,HttpSession session, HttpServletRequest request,Model model,@RequestParam(value="id",required=false,defaultValue="")String id,
			@RequestParam(value="skuId",required=false,defaultValue="")String skuId
			){
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		List<Product> allProducts =shopCartService.AllProducts(ctoken, uid);
		Integer quantity=0;
		
		for(int i=0;i<allProducts.size();i++){
			Product product=allProducts.get(i);
			quantity+=product.getQuantity();
		}
		
		model.addAttribute("quantity", quantity);
		model.addAttribute("id", id);
		model.addAttribute("skuId", skuId);
		return "product/productDetail";
	}

	/**
	 *
	 * <p>Discription:[商品详情页]</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "/details")
	public String details(HttpServletRequest request, Model model) {
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		String id = request.getParameter("id");
		String skuId = request.getParameter("skuId");
		model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
		if(StringUtils.isNotEmpty(id)){
			ItemDTO item = itemInfoService.getItemInfoById(Long.parseLong(id));
			LOG.info("item======"+JSON.toJSON(item));
			if(item==null){
				model.addAttribute("item", null);
				model.addAttribute("shopInfo", null);
				model.addAttribute("catCascade", null);
				return "/product/productDetail";
			}
			if(StringUtils.isNotEmpty(skuId)){
				ItemShopCartDTO dto = new ItemShopCartDTO();
				dto.setAreaCode(region);//省市区编码
				dto.setSkuId(Long.valueOf(skuId));//SKU id
				dto.setQty(1);//数量
				dto.setShopId(item.getShopId());//店铺ID
				dto.setItemId(Long.valueOf(id));//商品ID
				ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(dto); //调商品接口查url
				LOG.info("skuItem======"+JSON.toJSON(skuItem));
				model.addAttribute("skuItem", skuItem.getResult());
			}
			ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(item.getShopId());
			List<ShopCategorySellerDTO> categorylist = buildCategoryLev(item.getShopId());//店铺产品分类
			ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascade = itemCategoryService.queryParentCategoryList(item.getCid());

			model.addAttribute("logging_status", "false");//添加判断用户是否登录的逻辑
			model.addAttribute("favouriteItem", "false");//是否收藏过该商品
			model.addAttribute("favouriteShop", "false");//是否收藏过该商品
			String userToken = LoginToken.getLoginToken(request);;
			if(userToken!=null){
				RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
				if(registerDto!=null){
					model.addAttribute("logging_status", "true");
					if(StringUtils.isNotEmpty(skuId)){
						ItemFavouriteDTO favourite = new ItemFavouriteDTO();
						favourite.setUserId(registerDto.getUid().intValue());
						favourite.setItemId(Integer.parseInt(id));
						favourite.setSkuId(Integer.parseInt(skuId));
						DataGrid<ItemFavouriteDTO> dg = itemFavouriteService.datagrid(new Pager(), favourite);
						if(dg!=null && dg.getTotal()>0){
							model.addAttribute("favouriteItem", "true");
						}
					}
					ShopFavouriteDTO shopfavourite = new ShopFavouriteDTO();
					shopfavourite.setUserId(registerDto.getUid().intValue());
					shopfavourite.setShopId(shopInfo.getResult().getShopId().intValue());
					DataGrid<JSONObject> dgShops = this.favouriteService.shops(new Pager(), shopfavourite);
					if(dgShops!=null && dgShops.getTotal()>0){
						model.addAttribute("favouriteShop", "true");
					}
				}
			}

			ShopEvaluationQueryInDTO shopEvaluationQueryInDTO = new ShopEvaluationQueryInDTO();
			shopEvaluationQueryInDTO.setByShopId(shopInfo.getResult().getShopId());
			ExecuteResult<ShopEvaluationTotalDTO> shopEvaluationTotal = shopEvaluationService.queryShopEvaluationTotal(shopEvaluationQueryInDTO);
			ShopEvaluationTotalDTO shopEvaluation = shopEvaluationTotal.getResult();
			model.addAttribute("shopEvaluationResult", shopEvaluation);

			model.addAttribute("item", item);
			model.addAttribute("skusVertx",JSON.toJSONString(item.getSkuInfos()) );
			model.addAttribute("shopInfo", shopInfo.getResult());
			model.addAttribute("categorylist", categorylist);
			model.addAttribute("catCascade", itemCatCascade.getResult());
			model.addAttribute("skuId", skuId);
			LOG.info("categorylist================"+categorylist);
			LOG.info("shopInfo======"+JSON.toJSON(shopInfo));
			LOG.info("ItemCatCascade======"+JSON.toJSON(itemCatCascade));
			//获得店铺站点
			String stationId = StationUtil.getStationIdByShopId(shopInfo.getResult().getShopId());
			model.addAttribute("stationId", stationId);
			// 查询商品发货地址（商品从哪发货）
						ExecuteResult<ShopFreightTemplateDTO> shopFreightTemplateExecuteResult = shopFreightTemplateService.queryById(item.getShopFreightTemplateId());
						// 省名称
						String proviceName = "";
						// 市名称
						String cityName = "";	
						if (shopFreightTemplateExecuteResult.isSuccess() && shopFreightTemplateExecuteResult.getResult() != null) {
							ShopFreightTemplateDTO shopFreightTemplateDTO = shopFreightTemplateExecuteResult.getResult();
							model.addAttribute("shopFreightTemplateDTO",shopFreightTemplateDTO);
							if (shopFreightTemplateDTO.getProvinceId() != null) {
								ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService.queryNameById(Integer.parseInt(shopFreightTemplateDTO.getProvinceId() + ""));
								if (erProvice.isSuccess() && erProvice.getResult() != null) {
									proviceName = erProvice.getResult().getName();
								}
							}
							if (shopFreightTemplateDTO.getCityId() != null) {
								ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService.queryNameById(Integer.parseInt(shopFreightTemplateDTO.getCityId() + ""));
								if (erCity.isSuccess() && erCity.getResult() != null) {
									cityName = erCity.getResult().getName();
								}
							}
							model.addAttribute("from", proviceName + cityName);
							// 运送方式
							List<ShopDeliveryType> deliveryTypes = new ArrayList<ShopDeliveryType>();
							if (shopFreightTemplateDTO.getPostageFree() == 1) {// 不包邮
								deliveryTypes = this.getDeliveryType(shopFreightTemplateDTO.getId(), region,
										shopFreightTemplateDTO.getValuationWay(), item);
							}
							model.addAttribute("deliveryTypes", deliveryTypes);
							// 优惠方式
							ShopPreferentialWayDTO inDTO = new ShopPreferentialWayDTO();
							inDTO.setTemplateId(shopFreightTemplateDTO.getId());
							inDTO.setDelState("1"); // 未删除
							ExecuteResult<List<ShopPreferentialWayDTO>> preferentialWayResult = shopPreferentialWayService
									.queryShopPreferentialWay(inDTO);
							if (preferentialWayResult.isSuccess() && preferentialWayResult.getResult() != null
									&& preferentialWayResult.getResult().size() > 0) {
								model.addAttribute("preferentialWay", preferentialWayResult.getResult().get(0));
							}
						}
			//判断用户是否登陆
//			long userId = WebUtil.getInstance().getUserId(request);
//			UserDTO userDTO = userExportService.queryUserById(userId);
//			model.addAttribute("userId", userId);
//			model.addAttribute("userName", userDTO.getUname());
		}else{
			model.addAttribute("item", null);
			model.addAttribute("skuItem", null);
			model.addAttribute("shopInfo", null);
			model.addAttribute("catCascade", null);
			model.addAttribute("skuId", null);
			model.addAttribute("logging_status", "false");
		}


		return "/product/detailContent";
	}
	
	
	/**
	 *
	 * <p>Discription:[商品详情页]</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "details2")
	@ResponseBody
	public String details2(HttpServletRequest request, Model model) {
		JSONObject jores = new JSONObject();
		try {
			String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
			
			// 下拉列表中省，例取辽宁省
			String province_select = request.getParameter("province_select");
			// 下拉列表中城市，例取铁西区
			String area_name = request.getParameter("area_name");
			// 下拉列表中城市，例沈阳
			String city_name = request.getParameter("city_name");
			//下拉列表不选择时 
			if ("请选择".equals(city_name)) {
				city_name = "";
			}
			//下拉列表不选择时 
			if ("请选择".equals(area_name)) {
				area_name = "";
			}
			// 下拉列表中省，例辽宁
			String province_name = request.getParameter("province_name");

			String id = request.getParameter("id");
			String skuId = request.getParameter("skuId");
			jores.put("gix", SysProperties.getProperty("ftp_server_dir"));
			if (StringUtils.isNotEmpty(id)) {
				ItemDTO item = itemInfoService.getItemInfoById(Long.parseLong(id));
				
				if(item==null){
					model.addAttribute("item", null);
					model.addAttribute("shopInfo", null);
					model.addAttribute("catCascade", null);
					return "/goodscenter/product/productDetails";
				}
				if(StringUtils.isNotEmpty(skuId)){
					ItemShopCartDTO dto = new ItemShopCartDTO();
					//判断用户是否登陆
					RegisterDTO registerDTO = userExportService.getUserByRedis(LoginToken.getLoginToken(request));
					if(registerDTO!=null){
						dto.setBuyerId(registerDTO.getUid());
					}
			    	ExecuteResult<ItemBaseDTO> res=itemService.getItemBaseInfoById(Long.parseLong(id));
			    	LOG.info(res.getResult().getSellerId());
					
					dto.setAreaCode(region);//省市区编码
					dto.setSkuId(Long.valueOf(skuId));//SKU id
					dto.setQty(1);//数量
					dto.setShopId(item.getShopId());//店铺ID
					dto.setItemId(Long.valueOf(id));//商品ID
					if(res.getResult().getSellerId()!=null){
						dto.setSellerId(res.getResult().getSellerId());
					}
					ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(dto); //调商品接口查url
					LOG.info("skuItem======"+JSON.toJSON(skuItem));
		
					jores.put("skuItem", skuItem.getResult());
				}
				ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(item.getShopId());
				List<ShopCategorySellerDTO> categorylist = buildCategoryLev(item.getShopId());// 店铺产品分类
				ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascade = itemCategoryService
						.queryParentCategoryList(item.getCid());
				// jores.put("logging_status", "false");//添加判断用户是否登录的逻辑
				// jores.put("favouriteItem", "false");//是否收藏过该商品
				// jores.put("favouriteShop", "false");//是否收藏过该商品
				String userToken = LoginToken.getLoginToken(request);
				if (userToken != null) {
					RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
					if (registerDto != null) {
						model.addAttribute("logging_status", "true");
						if (StringUtils.isNotEmpty(skuId)) {
							ItemFavouriteDTO favourite = new ItemFavouriteDTO();
							favourite.setUserId(registerDto.getUid().intValue());
							favourite.setItemId(Integer.parseInt(id));
							favourite.setSkuId(Integer.parseInt(skuId));
							DataGrid<ItemFavouriteDTO> dg = itemFavouriteService.datagrid(new Pager(), favourite);
							if (dg != null && dg.getTotal() > 0) {
								jores.put("favouriteItem", "true");
							}
						}
						ShopFavouriteDTO shopfavourite = new ShopFavouriteDTO();
						shopfavourite.setUserId(registerDto.getUid().intValue());
						shopfavourite.setShopId(shopInfo.getResult().getShopId().intValue());
						DataGrid<JSONObject> dgShops = this.favouriteService.shops(new Pager(), shopfavourite);
						if (dgShops != null && dgShops.getTotal() > 0) {
							jores.put("favouriteShop", "true");
						}
					}
				}
				ShopEvaluationQueryInDTO shopEvaluationQueryInDTO = new ShopEvaluationQueryInDTO();
				shopEvaluationQueryInDTO.setByShopId(shopInfo.getResult().getShopId());
				ExecuteResult<ShopEvaluationTotalDTO> shopEvaluationTotal = shopEvaluationService
						.queryShopEvaluationTotal(shopEvaluationQueryInDTO);
				ShopEvaluationTotalDTO shopEvaluation = shopEvaluationTotal.getResult();
				jores.put("shopEvaluationResult", JSON.toJSON(shopEvaluation));
				jores.put("item", item);
				jores.put("skusVertx", JSON.toJSON(item.getSkuInfos()));
				jores.put("shopInfo", shopInfo.getResult());
				jores.put("categorylist", categorylist);
				jores.put("catCascade", itemCatCascade.getResult());
				jores.put("skuId", skuId);
				// 获得店铺站点
				String stationId = StationUtil.getStationIdByShopId(shopInfo.getResult().getShopId());
				jores.put("stationId", stationId);
				// 查询商品发货地址（商品从哪发货）
				ExecuteResult<ShopFreightTemplateDTO> shopFreightTemplateExecuteResult = shopFreightTemplateService
						.queryById(item.getShopFreightTemplateId());
				String proviceName = "";
				String cityName = "";
				if (shopFreightTemplateExecuteResult.isSuccess()
						&& shopFreightTemplateExecuteResult.getResult() != null) {
					ShopFreightTemplateDTO shopFreightTemplateDTO = shopFreightTemplateExecuteResult.getResult();
					jores.put("shopFreightTemplateDTO", shopFreightTemplateDTO);
					if (shopFreightTemplateDTO.getProvinceId() != null) {
						ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService
								.queryNameById(Integer.parseInt(shopFreightTemplateDTO.getProvinceId() + ""));
						if (erProvice.isSuccess() && erProvice.getResult() != null) {
							proviceName = erProvice.getResult().getName();
						}
					}
					if (shopFreightTemplateDTO.getCityId() != null) {
						ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService
								.queryNameById(Integer.parseInt(shopFreightTemplateDTO.getCityId() + ""));
						if (erCity.isSuccess() && erCity.getResult() != null) {
							cityName = erCity.getResult().getName();
						}
					}
					// 配送地址截取
					String from = proviceName + cityName;
					if (from.length() < 8) {
						jores.put("from", from);
					} else {
						from = from.substring(0, 8);
						jores.put("from", from);
					}
					// 判断用户是否登入
					String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
					// 省
					String province = "";
					// 市
					String city = "";
					// 区
					String area = "";
					if (StringUtils.isEmpty(uid)) {
						if (StringUtils.isEmpty(province_select) || "请选择".equals(province_select)) {
							province = "北京市";
							jores.put("province", province);
							jores.put("city", city);
							jores.put("area", area);
						} else {
							jores.put("province", province_name);
							jores.put("city", city_name);
							jores.put("area", area_name);
						}
						// 运送方式
						List<DeliveryTypeFreightDTO> deliveryTypes = new ArrayList<DeliveryTypeFreightDTO>();
						if (shopFreightTemplateDTO.getPostageFree() == 1) {// 不包邮
							if (StringUtils.isEmpty(province_select)) {
								deliveryTypes = this.calcEveryDeliveryTypeFreight(Constants.DEFAULT_BEIJING, item);
							} else {
								deliveryTypes = this.calcEveryDeliveryTypeFreight(province_select, item);
							}
						}
						jores.put("deliveryTypes", deliveryTypes);
					} else {
						DataGrid<AddressInfoDTO> dgAddress = this.addressInfoService
								.queryAddressinfo(Long.valueOf(uid));
						if (dgAddress.getRows().size()>0) {
							for (int i = 0; i < dgAddress.getRows().size(); i++) {
								AddressInfoDTO addr = dgAddress.getRows().get(i);
								if (addr.getIsdefault() == 1 || addr.getIsdefault() == 2) {
									if (StringUtils.isEmpty(province_select) || "请选择".equals(province_select)) {
										ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService
												.queryNameById(Integer.valueOf(addr.getProvicecode()));
										jores.put("province", erProvice.getResult().getName());
										ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService
												.queryNameById(Integer.valueOf(addr.getCitycode()));
										jores.put("city", erCity.getResult().getName());
										ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService
												.queryNameById(Integer.valueOf(addr.getCountrycode()));
										jores.put("area", erCountry.getResult().getName());
									} else {
										jores.put("province", province_name);
										jores.put("city", city_name);
										jores.put("area", area_name);
									}
									// 运送方式
									List<DeliveryTypeFreightDTO> deliveryTypes = new ArrayList<DeliveryTypeFreightDTO>();
									if (shopFreightTemplateDTO.getPostageFree() == 1) {// 不包邮
										if (StringUtils.isEmpty(province_select) || "请选择".equals(province_select)) {
											deliveryTypes = this.calcEveryDeliveryTypeFreight(addr.getProvicecode(),
													item);
										} else {
											deliveryTypes = this.calcEveryDeliveryTypeFreight(province_select, item);
										}
									}
									jores.put("deliveryTypes", deliveryTypes);
								}
							}
						} else {
							if (StringUtils.isEmpty(province_select)||"请选择".equals(province_select)) {
								province = "北京市";
								jores.put("province", province);
								jores.put("city", city);
								jores.put("area", area);
							} else {
								jores.put("province", province_name);
								jores.put("city", city_name);
								jores.put("area", area_name);
							}
							// 运送方式
							List<DeliveryTypeFreightDTO> deliveryTypes = new ArrayList<DeliveryTypeFreightDTO>();
							if (shopFreightTemplateDTO.getPostageFree() == 1) {// 不包邮
								if (StringUtils.isEmpty(province_select)) {
									deliveryTypes = this.calcEveryDeliveryTypeFreight(Constants.DEFAULT_BEIJING, item);
								} else {
									deliveryTypes = this.calcEveryDeliveryTypeFreight(province_select, item);
								}
							}
							jores.put("deliveryTypes", deliveryTypes);
						}
					}
					// 优惠方式
					ShopPreferentialWayDTO inDTO = new ShopPreferentialWayDTO();
					inDTO.setTemplateId(shopFreightTemplateDTO.getId());
					inDTO.setDelState("1"); // 未删除
					ExecuteResult<List<ShopPreferentialWayDTO>> preferentialWayResult = shopPreferentialWayService
							.queryShopPreferentialWay(inDTO);
					if (preferentialWayResult.isSuccess() && preferentialWayResult.getResult() != null
							&& preferentialWayResult.getResult().size() > 0) {
						// model.addAttribute("preferentialWay",
						// preferentialWayResult.getResult().get(0));
						jores.put("preferentialWay", preferentialWayResult.getResult().get(0));
					}
				}
			} else {
				jores.put("item", null);
				jores.put("skuItem", null);
				jores.put("shopInfo", null);
				jores.put("catCascade", null);
				jores.put("skuId", null);
				jores.put("logging_status", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("加载商品详细信息出错了");
		}
		JSONObject jo = new JSONObject();
		jo.put("info", jores);
		return jores.toString();
	}
	
	private String Integer(int defaultBeijing) {
		// TODO Auto-generated method stub
		return null;
	}

	@RequestMapping(value = "/getItemStatus")
	@ResponseBody
	public String getItemStatus(HttpServletRequest request) {
		String id = request.getParameter("id");
		Integer itemStatus = 0;
		if(StringUtils.isNotEmpty(id)){
			ItemDTO item = itemInfoService.getItemInfoById(Long.parseLong(id));
			if(item!=null){
				itemStatus = item.getItemStatus();
			}
		}
		return itemStatus.toString();
	}

	//组装list：将子类目加入到对应的父类目中去
	private List<ShopCategorySellerDTO> buildCategoryLev(Long shopId) {
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setShopId(shopId);
		dto.setStatus(1);
		dto.setHomeShow(1);
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(dto, null);
		List<ShopCategorySellerDTO> list = result.getResult().getRows();

		List<ShopCategorySellerDTO> buildlist = new ArrayList<ShopCategorySellerDTO>();
		for(int i=0; null!=list && i<list.size(); i++){
			ShopCategorySellerDTO scParent = list.get(i);
			for(int j=0; j<list.size(); j++){
				//将一级类目的所有二级类目加入到一级类目的list中
				ShopCategorySellerDTO scChild = list.get(j);
				if(scParent.getCid() == scChild.getParentCid()){
					scParent.getChildShopCategory().add(scChild);
				}
			}
			//先将一级类目全部加入到buildlist中
			if(scParent.getLev()==1){
				buildlist.add(scParent);
			}
		}
		return buildlist;
	}
	/**
	 *
	 * <p>Discription:[购买咨询]</p>
	 * Created on 2015年3月25日
	 * @param request
	 * @param pager
	 * @param dto
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("/consult")
	public String consult(HttpServletRequest request, Model model) {
		Pager<BaseConsultingSmsDTO> pager = new Pager<BaseConsultingSmsDTO>();
		String itemId = request.getParameter("itemId");
		String page = request.getParameter("page");
		if(StringUtils.isNotEmpty(page) || StringUtils.isNumeric(page)){
			pager.setPage(Integer.parseInt(page));
		}
		if(StringUtils.isNotEmpty(itemId) || StringUtils.isNumeric(itemId)){
			BaseConsultingSmsDTO dto = new BaseConsultingSmsDTO();
			dto.setItemId(Long.valueOf(itemId));
			DataGrid<BaseConsultingSmsDTO> dg = this.consultService.queryList(dto, pager);
			pager.setTotalCount(dg.getTotal().intValue());
			pager.setRecords(dg.getRows());
			JSONArray consults = new JSONArray();
			for(BaseConsultingSmsDTO c : dg.getRows()){
				JSONObject consult = JSON.parseObject(JSON.toJSONString(c));
				Long buyerId = c.getBuyerId();
				if(buyerId!=null){
					UserDTO buyerUser = userExportService.queryUserById(buyerId);
					if(buyerUser!=null){
						consult.put("buyerId", c.getBuyerId());
						consult.put("buyerName", buyerUser.getUname());
					}
				}
				Long sellerId = c.getSellerId();
				if(sellerId!=null){
					UserDTO sellerUser = userExportService.queryUserById(sellerId);
					if(sellerUser!=null){
						consult.put("sellerId", c.getBuyerId());
						consult.put("sellerName", sellerUser.getUname());
					}
				}
				consults.add(consult);
			}
			model.addAttribute("consults", consults);
			model.addAttribute("pager", pager);
			LOG.info("-----------consults======="+JSON.toJSONString(consults) );
			LOG.info("-----------pager======="+JSON.toJSONString(pager) );
		}
		return "/goodscenter/product/productDetails_consult";
	}
	/**
	 *
	 * <p>Discription:[创建咨询]</p>
	 * Created on 2015年3月25日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping("/addConsult")
	public ExecuteResult<String> addConsult(HttpServletRequest request) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		//供站内消息提醒使用
		request.setAttribute("result",result);
		List<String> errs = new ArrayList<String>();
		String buyerId = CookieHelper.getCookieVal(request, com.camelot.mall.Constants.USER_ID);
		String itemId = request.getParameter("itemId");
		String consulting = request.getParameter("consulting");
		String sellerId = request.getParameter("sellerId");
		if(StringUtils.isEmpty(buyerId) && !StringUtils.isNumeric(buyerId)){
			//用户未登录
			errs.add("用户未登录");
			result.setErrorMessages(errs);
			return result;
		}
		if(StringUtils.isEmpty(itemId) || !StringUtils.isNumeric(itemId)){
			errs.add("商品Id不能为空，并且必须是数字。");
			result.setErrorMessages(errs);
			return result;
		}
		if(StringUtils.isEmpty(sellerId) || !StringUtils.isNumeric(sellerId)){
			errs.add("sellerId不能为空，并且必须是数字。");
			result.setErrorMessages(errs);
			return result;
		}
		if(StringUtils.isEmpty(consulting)){
			errs.add("咨询内容不能为空。");
			result.setErrorMessages(errs);
			return result;
		}
		BaseConsultingSmsDTO dto = new BaseConsultingSmsDTO();
		dto.setItemId(Long.valueOf(itemId));
		dto.setBuyerId(Long.valueOf(buyerId));
		dto.setConsulting(consulting);
		dto.setSellerId(Long.valueOf(sellerId));
		result = this.consultService.addBaseConsultingSms(dto);
		LOG.info("result======="+result);
		return result;
	}
	public static void run(List<List<String>> dimvalue, List<String> result, int layer, String curstring) {
		//大于一个集合时：
		if (layer < dimvalue.size() - 1) {
			//大于一个集合时，第一个集合为空
			if (dimvalue.get(layer).size() == 0)
				run(dimvalue, result, layer + 1, curstring);
			else {
				for (int i = 0; i < dimvalue.get(layer).size(); i++) {
					StringBuilder s1 = new StringBuilder();
					s1.append(curstring);
					s1.append(dimvalue.get(layer).get(i));
					s1.append(",");
					run(dimvalue, result, layer + 1, s1.toString());
				}
			}
		}
		//只有一个集合时：
		else if (layer == dimvalue.size() - 1) {
			//只有一个集合，且集合中没有元素
			if (dimvalue.get(layer).size() == 0)
				result.add(curstring);
			//只有一个集合，且集合中有元素时：其笛卡尔积就是这个集合元素本身
			else {
				for (int i = 0; i < dimvalue.get(layer).size(); i++) {
					result.add(curstring + dimvalue.get(layer).get(i));
				}
			}
		}
	}

	public static void main(String[] args) {
		//String jsonStr = "[[\"1:11\",\"1:12\"],[\"2:23\",\"2:24\",\"2:25\"],[\"3:33\",\"3:35\"]]";
		String jsonStr = "[[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"],[\"1\",\"2\",\"3\",\"4\",\"5\",\"6\",\"7\",\"8\",\"9\"]]";
		List dimvalue = JSON.parseArray(jsonStr);

		List<String> result = new ArrayList<String>();

		run(dimvalue, result, 0, "");

		for (String s : result) {
			//LOG.info(s);
		}
	}
    @ResponseBody
	@RequestMapping(value = "/skuDescartes")
	public List<String> skuDescartes(String jsonStr, Model model) {
    	List dimvalue = JSON.parseArray(jsonStr);
    	List<String> result = new ArrayList<String>();
    	run(dimvalue, result, 0, "");
    	return result;
	}
    /**
     *
     * <p>Discription:[读取商品sku价格]</p>
     * Created on 2015年3月12日
     * @param request
     * @param model
     * @return
     * @author:[马桂雷]
     */
    @ResponseBody
	@RequestMapping(value = "/getSkuPrice")
	public String getSkuPrice(HttpServletRequest request, Model model) {
    	String strAreaCode = request.getParameter("areaCode");
    	String strSkuId = request.getParameter("skuId");
    	String strQty = request.getParameter("qty");
    	String strShopId = request.getParameter("shopId");
    	String strItemId = request.getParameter("itemId");
    	String sellerId = request.getParameter("sellerId");
    	JSONObject jsonObj = new JSONObject();
    	if(StringUtils.isEmpty(strSkuId) || !StringUtils.isNumeric(strSkuId)){
    		jsonObj.put("skuId", "skuId不能为空，并且必须是数字");
    		return ((JSON) JSON.toJSON(jsonObj)).toString();
    	}
    	if(StringUtils.isEmpty(strQty) || !StringUtils.isNumeric(strQty)){
    		jsonObj.put("qty", "qty数量不能为空，并且必须是数字");
    		return ((JSON) JSON.toJSON(jsonObj)).toString();
    	}
    	if(StringUtils.isEmpty(strShopId) || !StringUtils.isNumeric(strShopId)){
    		jsonObj.put("shopId", "shopId不能为空，并且必须是数字");
    		return ((JSON) JSON.toJSON(jsonObj)).toString();
    	}
    	if(StringUtils.isEmpty(strItemId) || !StringUtils.isNumeric(strItemId)){
    		jsonObj.put("strItemId", "strItemId不能为空，并且必须是数字");
    		return ((JSON) JSON.toJSON(jsonObj)).toString();
    	}
    	if(StringUtils.isEmpty(sellerId) || !StringUtils.isNumeric(sellerId)){
    		jsonObj.put("sellerId", "sellerId不能为空，并且必须是数字");
    		return ((JSON) JSON.toJSON(jsonObj)).toString();
    	}
    	// 查询商品SKU信息
    	ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setAreaCode(strAreaCode);//省市区编码
		dto.setSkuId(Long.valueOf(strSkuId));//SKU id
		dto.setQty(Integer.parseInt(strQty));//数量
		dto.setShopId(Long.valueOf(strShopId));//店铺ID
		dto.setItemId(Long.valueOf(strItemId));//商品ID
		//判断用户是否登陆
		RegisterDTO registerDTO = userExportService.getUserByRedis(LoginToken.getLoginToken(request));
		if(registerDTO!=null){
			dto.setBuyerId(registerDTO.getUid());
		}
		dto.setSellerId(Long.valueOf(sellerId));
		ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto);
		return ((JSON) JSON.toJSON(er)).toString();
	}
   /**
    *
    * <p>Discription:[查询该商品sku是否收藏了]</p>
    * Created on 2015年4月29日
    * @param request
    * @param model
    * @return
    * @author:[马桂雷]
    */
    @ResponseBody
   	@RequestMapping(value = "/getFavouriteSku")
   	public JSON getFavouriteSku(HttpServletRequest request, Model model) {
    	String strSkuId = request.getParameter("skuId");
    	String strItemId = request.getParameter("itemId");
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("favouriteSku", "false");
    	if(StringUtils.isEmpty(strSkuId) || !StringUtils.isNumeric(strSkuId)){
    		jsonObj.put("skuId", "skuId不能为空，并且必须是数字");
    		return (JSON) JSON.toJSON(jsonObj);
    	}
    	if(StringUtils.isEmpty(strItemId) || !StringUtils.isNumeric(strItemId)){
    		jsonObj.put("itemId", "itemId不能为空，并且必须是数字");
    		return (JSON) JSON.toJSON(jsonObj);
    	}
    	String userToken = LoginToken.getLoginToken(request);
    	if(userToken!=null){
    		RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
    		if(registerDto!=null){
    			ItemFavouriteDTO favourite = new ItemFavouriteDTO();
    			favourite.setUserId(registerDto.getUid().intValue());
    			favourite.setItemId(Integer.parseInt(strItemId));
    			favourite.setSkuId(Integer.parseInt(strSkuId));
    			DataGrid<ItemFavouriteDTO> dg = itemFavouriteService.datagrid(new Pager(), favourite);
    			if(dg!=null && dg.getTotal()>0){
    				jsonObj.put("favouriteSku", "true");
    			}
    		}
    	}
    	return (JSON) JSON.toJSON(jsonObj);
    }

    /**
     *
     * <p>Discription:[读取商品活动价]</p>
     * Created on 2015年3月12日
     * @param request
     * @param model
     * @return
     * @author:[马桂雷]
     */
    @ResponseBody
   	@RequestMapping(value = "/getPromotion")
   	public String getPromotion(HttpServletRequest request, Model model) {
    	String strSkuId = request.getParameter("skuId");
    	String shopId = request.getParameter("shopId");
    	JSONObject jsonObj = new JSONObject();
    	if(StringUtils.isEmpty(strSkuId) || !StringUtils.isNumeric(strSkuId)){
    		jsonObj.put("skuId", "skuId不能为空，并且必须是数字");
    		return jsonObj.toJSONString();
    	}
    	if(StringUtils.isEmpty(shopId) || !StringUtils.isNumeric(shopId)){
    		jsonObj.put("shopId", "shopId不能为空，并且必须是数字");
    		return jsonObj.toJSONString();
    	}
    	// 获取商品营销活动
 		PromotionInDTO pid = new PromotionInDTO();
 		pid.setSkuId(Long.valueOf(strSkuId));
 		pid.setShopId(Long.valueOf(shopId));
 		pid.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));////不为空时 查询有效期内活动
 		pid.setOnlineState(1);//1 一上线的
 		ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(pid, null);
 		if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
 			List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
 			JSONArray markdownJsonArray = new JSONArray();
 			JSONArray fullJsonArray = new JSONArray();
 			for(PromotionOutDTO pro : listPromotionOutDTO){
 				JSONObject jo = new JSONObject();
 				Integer type = pro.getPromotionInfo().getType();
 				String activityName = pro.getPromotionInfo().getActivityName();
 				jo.put("activityName", activityName);
 				SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
 				Date startTimeDate = pro.getPromotionInfo().getStartTime();
 				String startTime = format.format(startTimeDate);
 				jo.put("startTime", startTime);
 				Date endTimeDate = pro.getPromotionInfo().getEndTime();
 				String endTime = format.format(endTimeDate);
 				jo.put("endTime", endTime);
 				//促销类型，1：直降，2：满减
 				if(type == 1){
 					PromotionMarkdown markdown = pro.getPromotionMarkdown();
 					if(markdown!=null){
 						Long minNum = markdown.getMinNum();//  最少订货量
 						jo.put("minNum", minNum);
 						Long maxNum = markdown.getMaxNum();//  最大订货量
 						jo.put("maxNum", maxNum);
 						BigDecimal promotionPrice = markdown.getPromotionPrice();
 						jo.put("promotionPrice", promotionPrice);
 						markdownJsonArray.add(jo);
 					}
 				}else if(type == 2){
 					PromotionFullReduction full = pro.getPromotionFullReduction();
 					if(full!=null){
 						BigDecimal meetPrice = full.getMeetPrice();
 						jo.put("meetPrice", meetPrice);
 						BigDecimal discountPrice = full.getDiscountPrice();
 						jo.put("discountPrice", discountPrice);
 						fullJsonArray.add(jo);
 					}
 				}
 			}
 			jsonObj.put("promotionMarkdown", markdownJsonArray);
 			jsonObj.put("promotionFullReduction", fullJsonArray);
 		}

 		return jsonObj.toJSONString();
    }
    /**
     *
     * <p>Discription:[促销活动详情,查询店铺参与这个活动的所有商品]</p>
     * <p>Discription:[这期先不做，放弃！！！]</p>
     * Created on 2015年4月2日
     * @param request
     * @param model
     * @return
     * @author:[马桂雷]
     */
    @RequestMapping(value = "/promotionInfo")
   	public String promotionInfo(HttpServletRequest request, Model model) {
    	String shopId = request.getParameter("shopId");
    	String promotionInfoId = request.getParameter("promotionInfoId");
    	String areaCode = request.getParameter("areaCode");
    	String page = request.getParameter("page");
    	boolean bl = true;
    	if(StringUtils.isEmpty(promotionInfoId) || !StringUtils.isNumeric(promotionInfoId)){
    		bl = false;
    	}
    	if(StringUtils.isEmpty(shopId) || !StringUtils.isNumeric(shopId)){
    		bl = false;
    	}
    	if(bl){
    		Pager<PromotionOutDTO> pager = new Pager<PromotionOutDTO>();
    		// 获取商品营销活动
     		PromotionInDTO pid = new PromotionInDTO();

     		pid.setPromotionInfoId(Long.valueOf(promotionInfoId));
     		pid.setShopId(Long.valueOf(shopId));
     		ExecuteResult<DataGrid<PromotionOutDTO>>  erdg = this.promotionService.getPromotion(pid, pager);
     		LOG.info("promotionItems====="+JSON.toJSONString(erdg));

     		if(erdg!=null && erdg.getResult()!=null && erdg.getResult().getRows()!=null && erdg.getResult().getRows().size()>0){
     			model.addAttribute("promotionItems", erdg.getResult().getRows().get(0));
     			pager.setTotalCount(erdg.getResult().getTotal().intValue());
    			pager.setRecords(erdg.getResult().getRows());
    			model.addAttribute("pager", pager);

     			List<PromotionOutDTO> proList = erdg.getResult().getRows();
     			JSONArray itemJsonArray = new JSONArray();
     			JSONArray skuItemJsonArray = new JSONArray();
     			for(PromotionOutDTO pro : proList){
     				Integer type = pro.getPromotionInfo().getType();
     				Long itemId = null;
     				Long skuId = null;
     				//促销类型，1：直降，2：满减
     				if(type == 1){
     					PromotionMarkdown markdown = pro.getPromotionMarkdown();
     					if(markdown!=null){
     						itemId = markdown.getItemId();
     						skuId = markdown.getSkuId();
     					}
     				}else if(type == 2){
     					PromotionFullReduction full = pro.getPromotionFullReduction();
     					if(full!=null){
     						itemId = full.getItemId();
     						skuId = full.getSkuId();
     					}
     				}
     				ItemDTO item = itemInfoService.getItemInfoById(itemId);
     				itemJsonArray.add(item);
 					ItemShopCartDTO dto = new ItemShopCartDTO();
 					dto.setAreaCode(areaCode);//省市区编码
 					dto.setSkuId(skuId);//SKU id
 					dto.setQty(1);//数量
 					dto.setShopId(Long.valueOf(shopId));//店铺ID
 					dto.setItemId(itemId);//商品ID
 					ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(dto); //调商品接口查url

 					skuItemJsonArray.add(skuItem.getResult());
     			}
     			LOG.info("item======"+itemJsonArray.toJSONString());
     			LOG.info("skuItem======"+skuItemJsonArray.toJSONString());
     			model.addAttribute("item", itemJsonArray);
     			model.addAttribute("skuItem", skuItemJsonArray);
     		}
    	}else{
    		model.addAttribute("promotionItems", null);
    		model.addAttribute("pager", null);
    		model.addAttribute("item", null);
 			model.addAttribute("skuItem", null);
    	}

    	return "/goodscenter/product/promotionInfo";
    }
    /**
     *
     * <p>Discription:[计算购买商品的金额]</p>
     * Created on 2015年3月27日
     * @param request
     * @return
     * @author:[马桂雷]
     */
    @ResponseBody
   	@RequestMapping(value = "/count")
   	public JSON count(HttpServletRequest request) {
    	String price = request.getParameter("price");
    	String num = request.getParameter("num");
    	String meetPrice = request.getParameter("meetPrice");
    	String discountPrice = request.getParameter("discountPrice");
    	JSONObject jsonObj = new JSONObject();
    	jsonObj.put("success", "false");
    	if(StringUtils.isEmpty(price)){
    		jsonObj.put("price", "price不能为空");
    		return (JSON) JSON.toJSON(jsonObj);
    	}
    	if(StringUtils.isEmpty(price)){
    		jsonObj.put("num", "num不能为空");
    		return (JSON) JSON.toJSON(jsonObj);
    	}
    	if(StringUtils.isEmpty(discountPrice)){
    		jsonObj.put("discountPrice", "discountPrice不能为空");
    		return (JSON) JSON.toJSON(jsonObj);
    	}

    	BigDecimal priceBig = new BigDecimal(price);
    	BigDecimal numBig = new BigDecimal(num);
    	BigDecimal needPay = priceBig.multiply(numBig);//乘法

    	BigDecimal meetPriceBig = new BigDecimal(meetPrice);//满足金额
    	BigDecimal discountPriceBig = new BigDecimal(discountPrice);//优惠金额
    	if(needPay.compareTo(meetPriceBig) == 1){
    		BigDecimal count = needPay.subtract(discountPriceBig);
    		jsonObj.put("needPay", needPay);
    		jsonObj.put("count", count);
        	jsonObj.put("success", "true");
    	}else{
    		jsonObj.put("needPay", needPay);
    		jsonObj.put("count", needPay);
        	jsonObj.put("success", "true");
    	}
 		return (JSON) JSON.toJSON(jsonObj);
    }
    /**
     *
     * <p>Discription: 产品大全列表页 </p>
     * Created on 2015年3月16日
     * @param request
     * @param model
     * @return
     * @author:[Goma 郭茂茂]
     */
    @RequestMapping("/categoryItems")
   	public String categoryItems( Pager<ItemQueryOutDTO> pager,String cid , Model model ) {
    	JSONArray categoryes = this.commonService.findCategory();
    	model.addAttribute("categoryes", categoryes);
    	LOG.debug("ALL CATEGORYES:" +  categoryes.toJSONString());

    	Long id = null;
    	if( cid != null && !"".equals(cid) ){
    		if( cid.indexOf(":") != -1 ){
        		String tmp = cid.substring(cid.lastIndexOf(":")+1, cid.length());
        		id = Long.valueOf(tmp);
    		}else{
    			id = Long.valueOf(cid);
    		}
    	}
    	LOG.debug("产品大全-商品列表查询参数："+id);
    	pager.setRows(30);
    	ExecuteResult<DataGrid<ItemQueryOutDTO>>  er = this.itemService.queryItemByCid(id, pager);

    	if(er.isSuccess()){
        	pager.setTotalCount(er.getResult().getTotal().intValue());
        	pager.setRecords(er.getResult().getRows());
    	}

    	model.addAttribute("pager", pager);
    	model.addAttribute("cid", cid);
    	return "/goodscenter/product/categoryItems";
    }

    /**
     *
     * <p>Discription: 产品大全详情页 </p>
     * Created on 2015年3月16日
     * @param request
     * @param model
     * @return
     * @author:[Goma 郭茂茂]
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/categoryItemDetail")
   	public String toProductCatalog(Pager pager, Long itemId, Model model ) {
    	model.addAttribute("itemId", itemId);
    	LOG.debug("商品大全详细ID："+itemId);
    	// 通过平台商品ID，查询商品信息
    	ExecuteResult<ItemDTO> er = this.itemService.getItemById(itemId);
    	model.addAttribute("item", er.getResult());
    	LOG.debug("平台商品属性："+JSON.toJSONString(er.getResult()));

    	// 通过平台商品ID查询右侧店铺列表
    	pager.setRows(3);
    	ExecuteResult<DataGrid<ItemQueryOutDTO>>  erItems = this.itemService.queryItemByPlatItemId(itemId, pager);
    	LOG.debug("平台店铺商品："+JSON.toJSONString(erItems));

    	List<JSONObject> items = new ArrayList<JSONObject>();
    	if(erItems.isSuccess() && erItems.getResult() != null ){
        	for(ItemQueryOutDTO dto:erItems.getResult().getRows()){
        		JSONObject item = JSON.parseObject(JSON.toJSONString(dto));
        		ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(dto.getShopId());
        		if( shop.isSuccess() && shop.getResult() != null ){
        			item.put("shopId", shop.getResult().getShopId());
        			item.put("shopName", shop.getResult().getShopName());
        		}
        		items.add(item);
        	}

        	pager.setTotalCount(erItems.getResult().getTotal().intValue());
        	pager.setRecords(items);
    	}
    	model.addAttribute("pager", pager);
    	return "/goodscenter/product/categoryItemDetail";
    }
    @ResponseBody
    @RequestMapping(value = "/itemSkuInquiryPriceDTO")
   	public ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> itemSkuInquiryPriceDTO(HttpServletRequest request, Model model ) {
    	ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> result = new ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>>();
    	List<String> errs = new ArrayList<String>();

    	String strSellerId = request.getParameter("sellerId");
    	if(StringUtils.isEmpty(strSellerId) || !StringUtils.isNumeric(strSellerId)){
    		errs.add("sellerId不能为空，并且必须是数字");
    	}
    	String strShopId = request.getParameter("shopId");
    	if(StringUtils.isEmpty(strShopId) || !StringUtils.isNumeric(strShopId)){
    		errs.add("strShopId不能为空，并且必须是数字");
    	}
    	String strItemId= request.getParameter("itemId");
    	if(StringUtils.isEmpty(strItemId) || !StringUtils.isNumeric(strItemId)){
    		errs.add("strItemId不能为空，并且必须是数字");
    	}
    	if(errs.size()>0){
    		result.setErrorMessages(errs);
    		return result;
    	}

    	ItemSkuInquiryPriceDTO dto = new ItemSkuInquiryPriceDTO();
    	dto.setSellerId(Long.valueOf(strSellerId));
    	dto.setShopId(Long.valueOf(strShopId));
    	dto.setItemId(Long.valueOf(strItemId));

    	result = itemSkuInquiryPriceExportService.queryList(dto, new Pager());

    	LOG.info("------------"+JSON.toJSONString(result));
    	return result;
    }
    /**
     *
     * <p>Discription:[发起询价，只有当买家认证通过后才能发起询价]</p>
     * Created on 2015年3月18日
     * @param request
     * @param dto
     * @param model
     * @return
     * @author:[马桂雷]
     */
    @ResponseBody
    @RequestMapping("/addInquiry")
	public ExecuteResult<String> addInquiry( HttpServletRequest request, Model model ){
    	ExecuteResult<String> result = new ExecuteResult<String>();
    	//供发送提醒消息使用
    	request.setAttribute("result", result);
    	List<String> errs = new ArrayList<String>();
    	String email = request.getParameter("email");
    	String cellphone = request.getParameter("cellphone");
    	String qty = request.getParameter("qty");
    	String comment = request.getParameter("comment");
    	String buyerId = request.getParameter("buyerId");
    	String sellerId = request.getParameter("sellerId");
    	String shopId = request.getParameter("shopId");
    	String itemId = request.getParameter("itemId");
    	String skuId = request.getParameter("skuId");
    	String ctoken = LoginToken.getLoginToken(request);;
    	//当前用户所处状态（1-普通用户待验证，2-普通用户验证通过，3-买家待审核，4-买家审核通过，5-卖家待审核，6-卖家审核通过）
    	RegisterDTO register = userExportService.getUserByRedis(ctoken);
    	if(register==null){
    		errs.add("未找到登录用户");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(buyerId) && !StringUtils.isNumeric(buyerId)){
    		errs.add("buyerId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(skuId) && !StringUtils.isNumeric(skuId)){
    		errs.add("skuId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(sellerId) && !StringUtils.isNumeric(sellerId)){
    		errs.add("sellerId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(shopId) && !StringUtils.isNumeric(shopId)){
    		errs.add("shopId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(itemId) && !StringUtils.isNumeric(itemId)){
    		errs.add("itemId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(qty) && !StringUtils.isNumeric(qty)){
    		errs.add("数量不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	Integer status = register.getuStatus();
    	if(status==4 || status==6 ){
    		//买家审核通过才能发起询价
    		ItemSkuInquiryPriceDTO dto = new ItemSkuInquiryPriceDTO();
    		dto.setEmail(email);
    		dto.setCellphone(cellphone);
    		dto.setInquiryQty(Integer.parseInt(qty));
    		dto.setComment(comment);
    		dto.setBuyerId(Long.valueOf(buyerId));
    		dto.setSellerId(Long.valueOf(sellerId));
    		dto.setItemId(Long.valueOf(itemId));
    		dto.setShopId(Long.valueOf(shopId));
    		dto.setSkuId(Long.valueOf(skuId));
    		result = inquiryService.addItemSkuInquiryPrice(dto);
    		return result;
    	}else{
    		errs.add("买家不是认证用户");
    		result.setErrorMessages(errs);
    		return result;
    	}
	}
    /**
     *
     * <p>Discription:[查询询价]</p>
     * Created on 2015年3月28日
     * @param request
     * @param model
     * @return
     * @author:[马桂雷]
     */
    @ResponseBody
    @RequestMapping("/queryInquiry")
	public ExecuteResult<ItemSkuInquiryPriceDTO> queryInquiry( HttpServletRequest request, Model model){
    	ExecuteResult<ItemSkuInquiryPriceDTO> result = new ExecuteResult<ItemSkuInquiryPriceDTO>();
    	List<String> errs = new ArrayList<String>();
    	String sellerId = request.getParameter("sellerId");
    	String buyerId = request.getParameter("buyerId");
    	String skuId = request.getParameter("skuId");
    	String itemId = request.getParameter("itemId");
    	if(StringUtils.isEmpty(itemId) && !StringUtils.isNumeric(itemId)){
    		errs.add("itemId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(buyerId) && !StringUtils.isNumeric(buyerId)){
    		errs.add("buyerId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(sellerId) && !StringUtils.isNumeric(sellerId)){
    		errs.add("sellerId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(StringUtils.isEmpty(skuId) && !StringUtils.isNumeric(skuId)){
    		errs.add("skuId不能为空，并且必须是数字");
    		result.setErrorMessages(errs);
    		return result;
    	}
    	if(errs.size()>0){
    		result.setErrorMessages(errs);
    		return result;
    	}
    	ItemDTO item = itemInfoService.getItemInfoById(Long.parseLong(itemId));
    	if(item!=null){
    		//读取询价价格
    		Integer hasPrice = item.getHasPrice();
    		if(hasPrice!=1){
    			ItemSkuInquiryPriceDTO skuInquiryDto = new ItemSkuInquiryPriceDTO();
            	skuInquiryDto.setBuyerId(Long.valueOf(buyerId));
            	skuInquiryDto.setSellerId(Long.valueOf(sellerId));
            	skuInquiryDto.setSkuId(Long.valueOf(skuId));
            	skuInquiryDto.setItemId(Long.valueOf(itemId));
            	result = itemSkuInquiryPriceExportService.queryByIdsAndNumber(skuInquiryDto);
    		}
    	}
    	LOG.info("询价查询结果===="+JSON.toJSONString(result));
    	return result;
    }

    @RequestMapping("/getItemEvaluation")
    public String getItemEvaluation(ItemEvaluationQueryInDTO dto,Pager<JSON> pager,Model model){
    	LOG.debug("商品评价查询参数"+JSON.toJSONString(dto));
    	dto.setType("1");
    	dto.setReply("1");
    	DataGrid<ItemEvaluationQueryOutDTO> dg = this.itemEvaluationService.queryItemEvaluationList(dto,pager);

    	List<JSON> rows = new ArrayList<JSON>();
    	for(ItemEvaluationQueryOutDTO ie:dg.getRows()){
    		JSONObject row = JSONObject.parseObject(JSON.toJSONString(ie));
    		UserDTO user = this.userExportService.queryUserById(ie.getUserId());
    		if( user != null )
    			row.put("userName", user.getUname());
    		rows.add(row);
    	}

    	pager.setTotalCount(dg.getTotal().intValue());
    	pager.setRecords(rows);
    	model.addAttribute("pager", pager);
    	return "/goodscenter/product/productEvaluation";
    }
    /**
     * 获得商品的评价，已json格式返回
     * @param dto
     * @param pager
     * @param model
     * @return
     */
    @RequestMapping("/getItemEvaluationJson")
    @ResponseBody
    public String getItemEvaluationJson(ItemEvaluationQueryInDTO dto,Pager<JSON> pager){
    	//LOG.debug("商品评价查询参数"+JSON.toJSONString(dto));
    	JSONObject modelMap=new JSONObject();
    	dto.setType("1");
    	dto.setReply("1");
    	//scopeLevel  评价类型 1:好评 2:中评 3:差评
    	pager.setRows(8);
    	DataGrid<ItemEvaluationQueryOutDTO> dg = this.itemEvaluationService.queryItemEvaluationList(dto,pager);

    	List<JSON> rows = new ArrayList<JSON>();
    	for(ItemEvaluationQueryOutDTO ie:dg.getRows()){
    		JSONObject row = JSONObject.parseObject(JSON.toJSONString(ie));
    		UserDTO user = this.userExportService.queryUserById(ie.getUserId());
    		if( user != null )
    			row.put("userName", user.getUname());
    		rows.add(row);
    	}

    	pager.setTotalCount(dg.getTotal().intValue());
    	pager.setRecords(rows);
    	modelMap.put("pager", JSON.toJSON(pager));
    	return modelMap.toJSONString();
    }
    /**
     * 获得商品的评价，已json格式返回
     * @param dto
     * @param pager
     * @param model
     * @return
     */
    @RequestMapping("/getItemEvaluationJson2")
    @ResponseBody
    public String getItemEvaluationJson2(ItemEvaluationQueryInDTO dto,Pager<JSON> pager){
    	//LOG.debug("商品评价查询参数"+JSON.toJSONString(dto));
    	JSONObject modelMap=new JSONObject();
    	dto.setType("1");
    	dto.setReply("1");
    	//scopeLevel  评价类型 1:好评 2:中评 3:差评
    	//pager.setRows(1);
    	DataGrid<ItemEvaluationQueryOutDTO> dg = this.itemEvaluationService.queryItemEvaluationList(dto,pager);
    	
    	List<JSON> rows = new ArrayList<JSON>();
    	for(ItemEvaluationQueryOutDTO ie:dg.getRows()){
    		JSONObject row = JSONObject.parseObject(JSON.toJSONString(ie));
    		UserDTO user = this.userExportService.queryUserById(ie.getUserId());
    		if( user != null )
    			row.put("userName", user.getUname());
    		rows.add(row);
    	}
    	
    	pager.setTotalCount(dg.getTotal().intValue());
    	pager.setRecords(rows);
    	modelMap.put("pager", JSON.toJSON(pager));
    	return modelMap.toJSONString();
    }

    
    
    
    /**
     * 以下部分是商品评论相关
     */
    @RequestMapping("toEval")
    public String toEval(@RequestParam(value="skuId",required=false,defaultValue="")String skuId,@RequestParam("itemId")String itemId,Model model){
    	model.addAttribute("itemId", itemId);
    	model.addAttribute("skuId", skuId);
    	return "product/productEvaluate";
    }
    
    /**
     * 删除商品收藏
     * @param dto 2015年11月4日10:27:38
     * @param pager
     * @param model
     * @return zhm
     */
    

	@ResponseBody
	@RequestMapping("/deladdr")
	public String deladdr(HttpServletRequest request,@RequestParam("ids[]")List<String> ids){
		ExecuteResult<String> result = new ExecuteResult<String>();
		String returnStr = "删除成功!";
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		for(String id:ids){
			result = itemFavouriteService.del(id, uid);
		}
		
		if(!result.isSuccess()){
			returnStr = "删除失败!"+result.getErrorMessages().get(0);
		}
		return returnStr;
	}
	
	
	@ResponseBody
	@RequestMapping("/deldp")
	public String deldp(HttpServletRequest request,@RequestParam("ids[]")List<String> ids){
		ExecuteResult<String> result = new ExecuteResult<String>();
		String returnStr = "删除成功!";
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		for(String id:ids){
			result = shopFavouriteService.del(id, uid);
		}
		
		if(!result.isSuccess()){
			returnStr = "删除失败!"+result.getErrorMessages().get(0);
		}
		return returnStr;
	}
	@ResponseBody
	@RequestMapping("/addItem")

		public String deldp(HttpServletRequest request,ItemFavouriteDTO favourite){
	
		ExecuteResult<String> er = new ExecuteResult<String>();
		String returnStr = "收藏成功!";
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		favourite.setUserId(Integer.valueOf(uid));
		er = this.itemFavouriteService.add(favourite);
		if(!er.isSuccess()){
			returnStr = "收藏失败!"+er.getErrorMessages().get(0);
		}
		return returnStr;
	}
	/**
	 * 
	 * <p>Description: [查询运送方式，同时计算每一种运送方式的运费]</p>
	 * Created on 2015年11月12日
	 * @param shopFreightTemplateId 运费模版ID
	 * @param regionId 区域ID
	 * @param valuationWay 计价方式：1件数，2重量，3体积
	 * @param itemDTO 商品信息
	 * @return
	 * @author:[宋文斌]
	 */
	private List<ShopDeliveryType> getDeliveryType(Long shopFreightTemplateId, String regionId, Integer valuationWay,
			ItemDTO itemDTO) {
		List<ShopDeliveryType> deliveryTypes = new ArrayList<ShopDeliveryType>();
		List<ShopDeliveryTypeDTO> deliveryTypeDTOs = new ArrayList<ShopDeliveryTypeDTO>();
		ExecuteResult<List<ShopDeliveryTypeDTO>> shopDeliveryTypeExecuteResult = shopDeliveryTypeService
				.queryByRegionIdAndTemplateId(regionId == null ? null : Long.parseLong(regionId), shopFreightTemplateId);
		if (shopDeliveryTypeExecuteResult.isSuccess()) {
			deliveryTypeDTOs = shopDeliveryTypeExecuteResult.getResult();
			// 计算每一种运送方式的运费
			for (ShopDeliveryTypeDTO deliveryTypeDTO : deliveryTypeDTOs) {
				ShopDeliveryType shopDeliveryType = new ShopDeliveryType();
				BeanUtils.copyProperties(deliveryTypeDTO, shopDeliveryType);
				BigDecimal freight = this.calcProductFreight(valuationWay, deliveryTypeDTO, itemDTO);
				shopDeliveryType.setGroupFreight(freight);
				deliveryTypes.add(shopDeliveryType);
			}
		}
		return deliveryTypes;
	}
    
    /**
     * 
     * <p>Description: [计算商品运费]</p>
     * Created on 2015年11月12日
     * @param valuationWay 计价方式：1件数，2重量，3体积
     * @param deliveryTypeDTO 运送方式
     * @return
     * @author:[宋文斌]
     */
	private BigDecimal calcProductFreight(Integer valuationWay, ShopDeliveryTypeDTO deliveryTypeDTO, ItemDTO itemDTO) {
		BigDecimal freight = BigDecimal.valueOf(0);
		// 总件数/总重量/总体积
		BigDecimal total = BigDecimal.ZERO;
		// 件数
		if (valuationWay == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.NUMBERS.getCode()) {
			total = BigDecimal.valueOf(1);
		} else if (valuationWay == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.WEIGHT.getCode()) { // 重量
			String weightUnit = itemDTO.getWeightUnit();
			BigDecimal unit = BigDecimal.valueOf(1);
			if ("g".equals(weightUnit)) {
				unit = BigDecimal.valueOf(1000);
			} else if ("mg".equals(weightUnit)) {
				unit = BigDecimal.valueOf(1000).pow(2);
			} else if ("μg".equals(weightUnit)) {
				unit = BigDecimal.valueOf(1000).pow(3);
			}
			total = itemDTO.getWeight().divide(unit);
		} else if (valuationWay == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.VOLUME.getCode()) { // 体积
			total = itemDTO.getVolume();
		}
		// 首价格
		freight = freight.add(deliveryTypeDTO.getFirstPrice());
		if (total.compareTo(deliveryTypeDTO.getFirstPart()) > 0) {
			if (deliveryTypeDTO.getContinues().compareTo(BigDecimal.ZERO) > 0) {
				// 剩余
				total = total.subtract(deliveryTypeDTO.getFirstPart());
				BigDecimal[] count = total.divideAndRemainder(deliveryTypeDTO.getContinues());
				// 首价格+续价格
				freight = freight.add(count[0].multiply(deliveryTypeDTO.getContinuePrice()));
				if (count[1].compareTo(BigDecimal.ZERO) != 0) {
					freight = freight.add(deliveryTypeDTO.getContinuePrice());
				}
			}
		}
		return freight;
	}

	/**
	 * 
	 * <p>Description: [计算每种运送方式的运费]</p>
	 * Created on 2015年11月12日
	 * @param regionId 区域ID
	 * @param itemDTO 商品信息
	 * @return
	 * @author:[宋文斌]
	 */
	private List<DeliveryTypeFreightDTO> calcEveryDeliveryTypeFreight(String regionId, ItemDTO itemDTO) {
		List<DeliveryTypeFreightDTO> deliveryTypeFreights = new ArrayList<DeliveryTypeFreightDTO>();
		List<ProductInPriceDTO> inPriceDTOs = new ArrayList<ProductInPriceDTO>();
		ProductInPriceDTO productInPriceDTO = new ProductInPriceDTO();
		productInPriceDTO.setShopFreightTemplateId(itemDTO.getShopFreightTemplateId());
		productInPriceDTO.setRegionId(regionId);
		productInPriceDTO.setPayTotal(itemDTO.getGuidePrice());
		productInPriceDTO.setQuantity(1);
		productInPriceDTO.setVolume(itemDTO.getVolume());
		productInPriceDTO.setWeightUnit(itemDTO.getWeightUnit());
		productInPriceDTO.setWeight(itemDTO.getWeight());
		inPriceDTOs.add(productInPriceDTO);
		ExecuteResult<List<DeliveryTypeFreightDTO>> result = shopCartFreightService.calcEveryDeliveryTypeFreight(inPriceDTOs,false);
		if(result.isSuccess() && result.getResult() != null && result.getResult().size() > 0){
			deliveryTypeFreights = result.getResult();
		}
		return deliveryTypeFreights;
	}
}
