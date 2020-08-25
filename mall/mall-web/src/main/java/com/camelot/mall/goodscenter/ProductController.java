package com.camelot.mall.goodscenter;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.BaseConsultingSmsService;
import com.camelot.common.util.DateUtils;
import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagCountDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationReplyDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.SellPrice;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.SkuPictureDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.goodscenter.service.ItemPriceExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.maketcenter.dto.CentralPurchasingRefEnterpriseDTO;
import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.maketcenter.service.CentralPurchasingRefOrderExportService;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.mall.Constants;
import com.camelot.mall.common.CommonService;
import com.camelot.mall.home.FavouriteService;
import com.camelot.mall.service.ItemInfoService;
import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.service.ShopCartFreightService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopDeliveryTypeService;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFreightTemplateService;
import com.camelot.storecenter.service.ShopPreferentialWayService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;

/**
 *
 * <p>Description: [商品页面]</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/productController")
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
	@Resource
	private ShopFreightTemplateService shopFreightTemplateService;
	@Resource
	private ShopDeliveryTypeService shopDeliveryTypeService;
	@Resource
    private ShopPreferentialWayService shopPreferentialWayService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
    private ShopCartFreightService shopCartFreightService;
	@Resource
	private CentralPurchasingExportService centralPurchasingExportService;
	@Resource
	private CentralPurchasingRefOrderExportService centralPurchasingRefOrderExportService;
	@Resource
	private ItemPriceExportService itemPriceExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private  QqCustomerService qqCustomerService;
	@Resource
	private ItemSkuPackageService itemSkuPackageService;
	@Resource
	private MallThemeService mallThemeService;
	@Resource
	private CouponsExportService couponsExportService;
	/**
	 *
	 * <p>Discription:[商品详情页]</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "/details")
	public String details(HttpServletRequest request, Model model) {
		String url = "/goodscenter/product/productDetails";
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		String id = request.getParameter("id");
		String skuId = request.getParameter("skuId");
		if(StringUtils.isNotEmpty(id)){
			ItemDTO item = itemInfoService.getItemInfoById(id);
			System.out.println("item======"+JSON.toJSON(item));
			if (item == null) {
				model.addAttribute("item", null);
				model.addAttribute("shopInfo", null);
				model.addAttribute("catCascade", null);
				return url;
			}
			if(StringUtils.isNotEmpty(skuId)){
				ItemShopCartDTO dto = new ItemShopCartDTO();
				dto.setAreaCode(region);//省市区编码
				dto.setSkuId(Long.valueOf(skuId));//SKU id
				dto.setQty(1);//数量
				dto.setShopId(item.getShopId());//店铺ID
				dto.setItemId(Long.valueOf(id));//商品ID
				ExecuteResult<ItemShopCartDTO> skuItem = null;
				if (item.getAddSource() != null && item.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()) {
					skuItem = itemService.getCombinationSkuShopCart(dto); // 调商品接口查url
				} else {
					skuItem = itemService.getSkuShopCart(dto); // 调商品接口查url
				}
				System.out.println("skuItem======"+JSON.toJSON(skuItem));
				model.addAttribute("skuItem", skuItem.getResult());
				if (skuItem.isSuccess() && skuItem.getResult() != null && skuItem.getResult().getSkuPics() != null
						&& skuItem.getResult().getSkuPics().size() > 0) {
					String[] skuPics = new String[skuItem.getResult().getSkuPics().size()];
					for(int i = 0; i < skuPics.length; i++){
						SkuPictureDTO pictureDTO = skuItem.getResult().getSkuPics().get(i);
						skuPics[i] = pictureDTO.getPicUrl();
					}
					item.setPicUrls(skuPics);
				}
			}
			// 推荐套装
			if(StringUtils.isNotBlank(id)){
				this.getPackageSku(Long.valueOf(id), StringUtils.isBlank(skuId) ? null : Long.valueOf(skuId), region, null, model);
			}
			// 套装商品
			if (item.getAddSource() != null && item.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()) {
				// 商品详情页地址
				//url = "/goodscenter/product/packageProductDetails";
				// 辅料类型
				List<ItemDTO> auxiliaryItems = new ArrayList<ItemDTO>();
				ItemSkuPackageDTO auxiliaryItemSkuPackageInDTO = new ItemSkuPackageDTO();
				auxiliaryItemSkuPackageInDTO.setPackageItemId(item.getItemId());
				auxiliaryItemSkuPackageInDTO.setAddSource(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());
				List<ItemSkuPackageDTO> auxiliaryItemSkuPackageDTOs = itemSkuPackageService.getPackages(auxiliaryItemSkuPackageInDTO);
				if(auxiliaryItemSkuPackageDTOs != null && auxiliaryItemSkuPackageDTOs.size() > 0){
					for(ItemSkuPackageDTO itemSkuPackageDTO : auxiliaryItemSkuPackageDTOs){
						boolean exist = false;
						for(ItemDTO auxiliaryItem : auxiliaryItems){
							if(auxiliaryItem.getItemId().equals(itemSkuPackageDTO.getSubItemId())){
								exist = true;
							}
						}
						if(!exist){
							ItemDTO auxiliaryItem = itemInfoService.getItemInfoById(String.valueOf(itemSkuPackageDTO.getSubItemId()));
//							if(auxiliaryItem.getItemStatus() == 4){
//								auxiliaryItems.add(auxiliaryItem);
//							}
							auxiliaryItems.add(auxiliaryItem);
						}
					}
					model.addAttribute("auxiliaryItems", auxiliaryItems);
				}
				// 基础服务
				List<ItemDTO> basicItems = new ArrayList<ItemDTO>();
				ItemSkuPackageDTO basicItemSkuPackageInDTO = new ItemSkuPackageDTO();
				basicItemSkuPackageInDTO.setPackageItemId(item.getItemId());
				basicItemSkuPackageInDTO.setAddSource(ItemAddSourceEnum.BASICSERVICE.getCode());
				List<ItemSkuPackageDTO> basicItemSkuPackageDTOs = itemSkuPackageService.getPackages(basicItemSkuPackageInDTO);
				if(basicItemSkuPackageDTOs != null && basicItemSkuPackageDTOs.size() > 0){
					for(ItemSkuPackageDTO itemSkuPackageDTO : basicItemSkuPackageDTOs){
						boolean exist = false;
						for(ItemDTO basicItem : basicItems){
							if(basicItem.getItemId().equals(itemSkuPackageDTO.getSubItemId())){
								exist = true;
							}
						}
						if(!exist){
							ItemDTO basicItem = itemInfoService.getItemInfoById(String.valueOf(itemSkuPackageDTO.getSubItemId()));
//							if(basicItem.getItemStatus() == 4){
//								basicItems.add(basicItem);
//							}
							basicItems.add(basicItem);
						}
					}
					model.addAttribute("basicItems", basicItems);
				}
				// 增值服务
				this.getValueAddedService(item.getItemId(), region, request, model);
			}
			ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(item.getShopId());
			List<ShopCategorySellerDTO> categorylist = buildCategoryLev(item.getShopId());//店铺产品分类
			ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascade = itemCategoryService.queryParentCategoryList(item.getCid());

			//用来显示公司名称
			UserDTO userDTO = userExportService.queryUserById(shopInfo.getResult().getSellerId());
			model.addAttribute("userDTO",userDTO);
			model.addAttribute("logging_status", "false");//添加判断用户是否登录的逻辑
			model.addAttribute("userId", "");
			model.addAttribute("favouriteItem", "false");//是否收藏过该商品
			model.addAttribute("favouriteShop", "false");//是否收藏过该商品
			String userToken = LoginToken.getLoginToken(request);
			RegisterDTO registerDto = null;
			if(userToken!=null){
				registerDto = userExportService.getUserByRedis(userToken);
				if(registerDto!=null){
					model.addAttribute("logging_status", "true");
                    model.addAttribute("userId", String.valueOf(registerDto.getUid()));
                    model.addAttribute("userEmail", registerDto.getUserEmail());
                    model.addAttribute("userMobile", registerDto.getUserMobile());
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
			//配置商品详情页的SEO
			BaseTDKConfigDTO  baseTDKConfigDTO=new BaseTDKConfigDTO();
			baseTDKConfigDTO.setTitle(item.getItemName()+"【图片 价格 品牌 评价】- "+shopInfo.getResult().getShopName()+" - 舒适100");
			baseTDKConfigDTO.setDescription(item.getItemName()+" 图片、价格、品牌、评论 【舒适100，全国配送，优惠多多】");
			baseTDKConfigDTO.setKeywords(item.getKeywords()+","+item.getBrandName()+","+shopInfo.getResult().getShopName()+",舒适100");
			model.addAttribute("baseTDKConfigDTO2", baseTDKConfigDTO);
			model.addAttribute("shopEvaluationResult", shopEvaluation);

			model.addAttribute("item", item);
			model.addAttribute("shopInfo", shopInfo.getResult());
			model.addAttribute("categorylist", categorylist);
			System.out.println("categorylist================"+categorylist);
			model.addAttribute("catCascade", itemCatCascade.getResult());
			model.addAttribute("skuId", skuId);
			System.out.println("shopInfo======"+JSON.toJSON(shopInfo));
			System.out.println("ItemCatCascade======"+JSON.toJSON(itemCatCascade));

			//获得店铺站点
			String stationId = StationUtil.getStationIdByShopId(shopInfo.getResult().getShopId());
			model.addAttribute("stationId", stationId);
			List<Long> skuIds = new ArrayList<Long>();
			skuIds.add(Long.valueOf(skuId));
			List<Long> cids = new ArrayList<Long>();
			cids.add(item.getCid());
			List<CouponsDTO> couponsDTOs = couponsExportService.getCouponsByShopId(Long.valueOf(shopInfo.getResult().getShopId()),WebUtil.getInstance().getUserId(request),skuIds,cids);
			model.addAttribute("couponsList", couponsDTOs);
			//获取QQ客服
			List<Long> idlist = new ArrayList<Long>();
			idlist.add(shopInfo.getResult().getSellerId());
			String qqId = qqCustomerService.getQqCustomerByIds(idlist, Constants.TYPE_SHOP);
			model.addAttribute("qqId", qqId);
			// 查询商品发货地址（商品从哪发货）
			ExecuteResult<ShopFreightTemplateDTO> shopFreightTemplateExecuteResult = shopFreightTemplateService.queryById(item.getShopFreightTemplateId());
			String proviceName = "";
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
				// 每种运送方式的运费
				List<DeliveryTypeFreightDTO> deliveryTypeFreights = new ArrayList<DeliveryTypeFreightDTO>();
				if (shopFreightTemplateDTO.getPostageFree() == 1) {// 不包邮
					deliveryTypeFreights = this.calcEveryDeliveryTypeFreight(region, item);
				}
				model.addAttribute("deliveryTypes", deliveryTypeFreights);
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
			String centralPurchasingId = request.getParameter("centralPurchasingId");
			if (StringUtils.isNotBlank(centralPurchasingId) && StringUtils.isNumeric(centralPurchasingId)) {
				ExecuteResult<List<SkuPictureDTO>> skuPicResult = itemService.querySkuPics(Long.parseLong(skuId));
				if (skuPicResult.isSuccess() && skuPicResult.getResult() != null && skuPicResult.getResult().size() > 0) {
					String[] picUrls = new String[skuPicResult.getResult().size()];
					int i =0;
					for(SkuPictureDTO u : skuPicResult.getResult()){
						picUrls[i] = u.getPicUrl();
						i++;
					}
					item.setPicUrls(picUrls);
				}
				// 查询商品有没有参加集采活动
				QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
				queryCentralPurchasingDTO.setItemId(Long.parseLong(id));
				if(StringUtils.isNotBlank(skuId)){
					queryCentralPurchasingDTO.setSkuId(Long.parseLong(skuId));
				}
				queryCentralPurchasingDTO.setCentralPurchasingId(Long.parseLong(centralPurchasingId));
				ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> result = centralPurchasingExportService.queryCentralPurchasingActivity(queryCentralPurchasingDTO, null);
				if(result.isSuccess()
						&& result.getResult() != null
						&& result.getResult().getRows() != null
						&& result.getResult().getRows().size() > 0){
					queryCentralPurchasingDTO = result.getResult().getRows().get(0);
					model.addAttribute("centralPurchasingDTO", queryCentralPurchasingDTO);
					// 该集采商品已经付款的数量,用来计算库存
					int orderNum = 0;
					CentralPurchasingRefOrderDTO CentralPurchasingRefOrderInDTO = new CentralPurchasingRefOrderDTO();
					CentralPurchasingRefOrderInDTO.setActivitesDetailsId(queryCentralPurchasingDTO.getActivitesDetailsId());
					CentralPurchasingRefOrderInDTO.setItemId(queryCentralPurchasingDTO.getItemId());
					CentralPurchasingRefOrderInDTO.setSkuId(queryCentralPurchasingDTO.getSkuId());
					ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>> executeResult = centralPurchasingRefOrderExportService.queryCentralPurchasingRefOrder(CentralPurchasingRefOrderInDTO, null);
					if(executeResult.isSuccess()
							&& executeResult.getResult() != null
							&& executeResult.getResult().getRows() != null
							&& executeResult.getResult().getRows().size() > 0){
						List<CentralPurchasingRefOrderDTO> centralPurchasingRefOrderDTOs = executeResult.getResult().getRows();
						for(CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO : centralPurchasingRefOrderDTOs){
							orderNum += centralPurchasingRefOrderDTO.getPurchaseNum();
						}
					}
					if(orderNum > queryCentralPurchasingDTO.getReleaseGoodsMaxNum().intValue()){
						queryCentralPurchasingDTO.setReleaseGoodsMaxNum(0);
					} else{
						queryCentralPurchasingDTO.setReleaseGoodsMaxNum(queryCentralPurchasingDTO.getReleaseGoodsMaxNum().intValue() - orderNum);
					}
					if(registerDto != null){
						// 该用户已下单的该集采商品的数量
						orderNum = 0;
						// 查询集采活动的订单
						TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
						inDTO.setBuyerId(registerDto.getUid());
						inDTO.setCentralPurchasing(1);
						ExecuteResult<DataGrid<TradeOrdersDTO>> orderResult = tradeOrderExportService.queryOrders(inDTO, null);
						if (orderResult.isSuccess() && orderResult.getResult() != null
								&& orderResult.getResult().getRows() != null
								&& orderResult.getResult().getRows().size() > 0) {
							List<TradeOrdersDTO> tradeOrdersDTOs =  orderResult.getResult().getRows();
							for(TradeOrdersDTO tradeOrdersDTO : tradeOrdersDTOs){
								for(TradeOrderItemsDTO itemDTO : tradeOrdersDTO.getItems()){
									if (itemDTO.getItemId().longValue() == item.getItemId().longValue()
											&& itemDTO.getActivitesDetailsId().longValue() == queryCentralPurchasingDTO.getActivitesDetailsId().longValue()
											&& itemDTO.getSkuId().longValue() == queryCentralPurchasingDTO.getSkuId().longValue()) {
										orderNum += itemDTO.getNum();
									}
								}
							}
						}
						model.addAttribute("orderNum", orderNum);
						// 查询集采详情
						CentralPurchasingRefEnterpriseDTO centralPurchasingRefEnterpriseDTO = new CentralPurchasingRefEnterpriseDTO();
						centralPurchasingRefEnterpriseDTO.setActivitesDetailsId(queryCentralPurchasingDTO.getActivitesDetailsId());
						centralPurchasingRefEnterpriseDTO.setInsertBy(registerDto.getUid());
						ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> er = centralPurchasingExportService.queryCentralPurchasingRefEnterprise(centralPurchasingRefEnterpriseDTO);
						if (er.isSuccess() && er.getResult() != null && er.getResult().size() > 0) {
							model.addAttribute("centralPurchasingRefEnterpriseDTO", er.getResult().get(0));
						}
					}
					// 返回服务器当前时间用于倒计时
					model.addAttribute("serverTime", DateUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"));
				}
			}
		}else{
			model.addAttribute("item", null);
			model.addAttribute("skuItem", null);
			model.addAttribute("shopInfo", null);
			model.addAttribute("catCascade", null);
			model.addAttribute("skuId", null);
			model.addAttribute("logging_status", "false");
			model.addAttribute("userId", "");
		}
		return url;
	}
	
	/**
	 * 
	 * <p>Discription:[获取套装商品的增值服务]</p>
	 * Created on 2016年2月29日
	 * @param packageItemId 套装商品Id
	 * @param areaCode 区域Id
	 * @param request 请求对象
	 * @param model 数据模型
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping(value = "/getValueAddedService")
	public String getValueAddedService(Long packageItemId, String areaCode, HttpServletRequest request, Model model){
		// 选中的增值服务
		String[] selectValueAddedSkuIds = request.getParameterValues("valueAddedSkuIds[]");
		// 选中的增值服务的skuId
		List<Long> selectedSkuIds = new ArrayList<Long>();
		if(selectValueAddedSkuIds != null && selectValueAddedSkuIds.length > 0){
			for(String strValueAddedSkuId : selectValueAddedSkuIds){
				selectedSkuIds.add(Long.valueOf(strValueAddedSkuId.split("-")[1]));
			}
		}
		model.addAttribute("selectedSkuIds", selectedSkuIds);
		List<ItemDTO> valueAddedItems = new ArrayList<ItemDTO>();
		ItemSkuPackageDTO valueAddedItemSkuPackageInDTO = new ItemSkuPackageDTO();
		valueAddedItemSkuPackageInDTO.setPackageItemId(packageItemId);
		valueAddedItemSkuPackageInDTO.setAddSource(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
		List<ItemSkuPackageDTO> valueAddedItemSkuPackageDTOs = itemSkuPackageService.getPackages(valueAddedItemSkuPackageInDTO);
		// key=增值服务商品Id，value=增值服务sku
		Map<Long, List<Long>> valueAddedMap = new HashMap<Long, List<Long>>();
		if(valueAddedItemSkuPackageDTOs != null && valueAddedItemSkuPackageDTOs.size() > 0){
			for(ItemSkuPackageDTO itemSkuPackageDTO : valueAddedItemSkuPackageDTOs){
				if(valueAddedMap.containsKey(itemSkuPackageDTO.getSubItemId())){
					valueAddedMap.get(itemSkuPackageDTO.getSubItemId()).add(itemSkuPackageDTO.getSubSkuId());
				} else{
					List<Long> valueAddedSkuIds = new ArrayList<Long>();
					valueAddedSkuIds.add(itemSkuPackageDTO.getSubSkuId());
					valueAddedMap.put(itemSkuPackageDTO.getSubItemId(), valueAddedSkuIds);
				}
			}
			for (Entry<Long, List<Long>> entry : valueAddedMap.entrySet()) {
				Long subItemId = entry.getKey();
				List<Long> subSkuIds = entry.getValue();
				ItemDTO valueAddedItem = itemInfoService.getItemInfoById(String.valueOf(subItemId));
				// 增值服务的sku信息
				LinkedList<ItemShopCartDTO> itemShopCartDTOs = new LinkedList<ItemShopCartDTO>();
				if (valueAddedItem != null 
						&& valueAddedItem.getItemStatus() == 4 
						&& valueAddedItem.getHasPrice() == 1
						&& subSkuIds != null && subSkuIds.size() > 0) {
					for(Long valueAddedSkuId : subSkuIds){
						ItemShopCartDTO dto = new ItemShopCartDTO();
						dto.setAreaCode(areaCode);//省市区编码
						dto.setSkuId(valueAddedSkuId);//SKU id
						dto.setQty(1);//数量
						dto.setShopId(valueAddedItem.getShopId());//店铺ID
						dto.setItemId(valueAddedItem.getItemId());//商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(dto);
						if(skuItem.isSuccess() && skuItem.getResult() != null){
							if(selectedSkuIds.contains(valueAddedSkuId)){
								// 把选中的放在第一个位置，用于数据回显
								itemShopCartDTOs.addFirst(skuItem.getResult());
							} else{
								itemShopCartDTOs.add(skuItem.getResult());
							}
						}
					}
				}
				if(itemShopCartDTOs.size() > 0){
					valueAddedItem.setItemShopCartDTOs(itemShopCartDTOs);
					valueAddedItems.add(valueAddedItem);
				}
			}
			model.addAttribute("valueAddedItems", valueAddedItems);
		}
		return "/goodscenter/product/valueAddedService";
	}
	
	/**
	 * 
	 * <p>Discription:[获取套装信息]</p>
	 * Created on 2016年2月22日
	 * @param itemId 商品ID
	 * @param skuId 商品SKU ID
	 * @param region 区域ID
	 * @param packageSkuId 套装商品SKU ID
	 * @param model 数据模型
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping(value = "/getPackageSku")
	public String getPackageSku(Long itemId, Long skuId, String region, Long packageSkuId, Model model) {
		// 缓存ItemDTO，key=itemId，value=item
		Map<Long, ItemDTO> cacheItems = new HashMap<Long, ItemDTO>();
		ItemDTO item = itemInfoService.getItemInfoById(String.valueOf(itemId));
		// 缓存商品信息
		cacheItems.put(item.getItemId(), item);
		// key=组装成的新skuId，value=组成该sku的sku具体信息
		Map<Long, List<ItemShopCartDTO>> combinationSkuMap = new HashMap<Long, List<ItemShopCartDTO>>();
		// key=组装成的新skuId，value=组装成的新sku具体信息
		Map<Long, ItemShopCartDTO> packageSkuMap = new LinkedHashMap<Long, ItemShopCartDTO>();
		// 记录套装商品出现的次数，同一间套装商品显示套装1，套装2....
		Map<Long,Integer> countMap = new HashMap<Long,Integer>();
		// 套装商品
		if(item.getAddSource() != null && item.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()){
			// 查询该套装商品的sku
			ItemSkuPackageDTO itemSkuPackageDTO = new ItemSkuPackageDTO();
			itemSkuPackageDTO.setPackageItemId(itemId);
			itemSkuPackageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
			List<ItemSkuPackageDTO> itemSkuPackageDTOs = itemSkuPackageService.getPackages(itemSkuPackageDTO);
			// 初始化推荐套装下拉框
			for(ItemSkuPackageDTO dto : itemSkuPackageDTOs){
				if(packageSkuMap.get(dto.getPackageSkuId()) == null){
					// 用户没有指定sku
					if(skuId == null || (skuId != null && dto.getPackageSkuId().longValue() == skuId)){
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(region);// 省市区编码
						itemShopCartDTO.setSkuId(dto.getPackageSkuId());// skuId
						itemShopCartDTO.setQty(1);// 数量
						itemShopCartDTO.setShopId(item.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(dto.getPackageItemId());// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查url
						if (skuItem.isSuccess() && skuItem.getResult() != null) {
							packageSkuMap.put(dto.getPackageSkuId(), skuItem.getResult());
							// 如果没有指定组合套装的sku，默认查询第一个组合套装的信息
							if(packageSkuId == null){
								packageSkuId = dto.getPackageSkuId();
							}
						}
						// 组装成的新的sku
						model.addAttribute("packageSkuMap", packageSkuMap);
						// 用户指定了sku就只显示该sku信息
						if(skuId != null){
							break;
						}
					}
				}
			}
		} else {
			// 根据该商品SKU查询该商品的SKU组合成了哪些新的SKU
			List<Long> skuIds = new ArrayList<Long>();
			if (skuId == null) {
				for (SkuInfo skuInfo : item.getSkuInfos()) {
					skuIds.add(skuInfo.getSkuId());
				}
			} else {
				skuIds.add(Long.valueOf(skuId));
			}
			ItemSkuPackageDTO itemSkuPackageDTO = new ItemSkuPackageDTO();
			itemSkuPackageDTO.setSubSkuIds(skuIds);
			itemSkuPackageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
			// 组装成的新的sku集合
			List<ItemSkuPackageDTO> itemSkuPackageDTOs = itemSkuPackageService.getPackages(itemSkuPackageDTO);
			if (itemSkuPackageDTOs != null && itemSkuPackageDTOs.size() > 0) {
				// 初始化推荐套装下拉框
				for (ItemSkuPackageDTO dto : itemSkuPackageDTOs) {
					ItemDTO packageItemDTO = null;
					if(cacheItems.get(dto.getPackageItemId()) == null){
						packageItemDTO = itemInfoService.getItemInfoById(String.valueOf(dto.getPackageItemId()));
					} else{
						packageItemDTO = cacheItems.get(dto.getPackageItemId());
					}
					// 不显示非在售的套装商品
					if(packageItemDTO.getItemStatus() != null && packageItemDTO.getItemStatus() == 4){
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(region);// 省市区编码
						itemShopCartDTO.setSkuId(dto.getPackageSkuId());// skuId
						itemShopCartDTO.setQty(1);// 数量
						itemShopCartDTO.setShopId(packageItemDTO.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(dto.getPackageItemId());// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查url
						if (skuItem.isSuccess() && skuItem.getResult() != null) {
							packageSkuMap.put(dto.getPackageSkuId(), skuItem.getResult());
							// 如果没有指定组合套装的sku，默认查询第一个组合套装的信息
							if (packageSkuId == null) {
								packageSkuId = dto.getPackageSkuId();
							}
						}
						// 组装成的新的sku
						model.addAttribute("packageSkuMap", packageSkuMap);
					}
				}
			}
		}
		if(packageSkuId != null){
			// 判断packageSkuId是否在查询到的套装商品中
			boolean exist = false;
			for(Long pSkuId : packageSkuMap.keySet()){
				if(pSkuId.equals(packageSkuId)){
					exist = true;
					break;
				}
			}
			if(!exist){
				for(Long pSkuId : packageSkuMap.keySet()){
					packageSkuId = pSkuId;
					break;
				}
			}
			// 根据组装成的新的sku查询是哪几个item组装成这个新sku的
			ItemSkuPackageDTO packageDTO = new ItemSkuPackageDTO();
			packageDTO.setPackageSkuId(packageSkuId);
			packageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
			// 组装成该sku的商品
			List<ItemSkuPackageDTO> packageDTOs = itemSkuPackageService.getPackages(packageDTO);
			if (packageDTOs != null && packageDTOs.size() > 0) {
				List<ItemShopCartDTO> itemShopCartDTOs = new ArrayList<ItemShopCartDTO>();
				for (ItemSkuPackageDTO itemPackage : packageDTOs) {
					ItemDTO combinationItem = null;
					if(cacheItems.get(itemPackage.getSubItemId()) == null){
						combinationItem = itemInfoService.getItemInfoById(String.valueOf(itemPackage.getSubItemId()));
					} else{
						combinationItem = cacheItems.get(itemPackage.getSubItemId());
					}
					ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
					itemShopCartDTO.setAreaCode(region);// 省市区编码
					itemShopCartDTO.setSkuId(itemPackage.getSubSkuId());// skuId
					itemShopCartDTO.setQty(1);// 数量
					itemShopCartDTO.setShopId(combinationItem.getShopId());// 店铺ID
					itemShopCartDTO.setItemId(itemPackage.getSubItemId());// 商品ID
					ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查url
					if (skuItem.isSuccess() && skuItem.getResult() != null) {
						itemShopCartDTOs.add(skuItem.getResult());
					}
				}
				if (itemShopCartDTOs != null && itemShopCartDTOs.size() > 0) {
					combinationSkuMap.put(packageSkuId, itemShopCartDTOs);
					// 用于组装成新sku的sku
					model.addAttribute("combinationSkuMap", combinationSkuMap);
				}
			}
		}
		// 记录套装商品出现的次数
		for (Entry<Long, ItemShopCartDTO> packageEntry : packageSkuMap.entrySet()) {
			ItemShopCartDTO itemShopCartDTO = packageEntry.getValue();
			if(countMap.containsKey(itemShopCartDTO.getItemId())){
				countMap.put(itemShopCartDTO.getItemId(), countMap.get(itemShopCartDTO.getItemId()) + 1);
			} else{
				countMap.put(itemShopCartDTO.getItemId(), 1);
			}
		}
		// 记录同一个商品的套装名称顺序
		Map<Long,Integer> itemSortMap = new HashMap<Long, Integer>();
		for (Entry<Long, ItemShopCartDTO> packageEntry : packageSkuMap.entrySet()) {
			ItemShopCartDTO itemShopCartDTO = packageEntry.getValue();
			if (countMap.get(itemShopCartDTO.getItemId()) != null && countMap.get(itemShopCartDTO.getItemId()) > 1) {
				if(itemSortMap.get(itemShopCartDTO.getItemId()) == null){
					itemSortMap.put(itemShopCartDTO.getItemId(), 1);
				} else{
					itemSortMap.put(itemShopCartDTO.getItemId(), itemSortMap.get(itemShopCartDTO.getItemId()) + 1);
				}
				itemShopCartDTO.setItemName(itemShopCartDTO.getItemName() + "（套装" + itemSortMap.get(itemShopCartDTO.getItemId()) + "）");
			}
		}
		model.addAttribute("itemAddSource", item.getAddSource());
		return "/goodscenter/product/packageSku";
	}
	
	public BaseTDKConfigDTO getProductTDK(ItemDTO item, String shopname){
		//根据店铺Id查询店铺名称 商品名称,所属分类,品牌名称,店铺名称,舒适100
		BaseTDKConfigDTO  baseTDKConfigDTO2=new BaseTDKConfigDTO();
		baseTDKConfigDTO2.setTitle(item.getItemName()+"【图片 价格 品牌 评价】- "+shopname+" - 舒适100");
		baseTDKConfigDTO2.setDescription(item.getItemName()+" 图片、价格、品牌、评论 【舒适100，全国配送，优惠多多】");
		baseTDKConfigDTO2.setKeywords(item.getItemName()+","+item.getBrandName()+","+shopname+",舒适100");

		return baseTDKConfigDTO2;
	}
	
	/**
	 * 切换不同的sku获取不同的商品评分
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/getItemScope")
	@ResponseBody
	public ItemShopCartDTO getItemScope(HttpServletRequest request){
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		String id = request.getParameter("id");
		String skuId = request.getParameter("skuId");
		String shopId=request.getParameter("shopId");
		ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setAreaCode(region);//省市区编码
		if(StringUtils.isNotEmpty(skuId)){
		    dto.setSkuId(Long.valueOf(skuId));//SKU id
	    }
		dto.setQty(1);//数量
		if(StringUtils.isNoneBlank(shopId)){
			dto.setShopId(Long.parseLong(shopId));//店铺ID
		}
		if(StringUtils.isNoneEmpty(id)){
			dto.setItemId(Long.valueOf(id));//商品ID
		}
		ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(dto); //调商品接口查url
		if(skuItem.isSuccess()){
			return skuItem.getResult();
		}else{
			return dto;
		}
		
	}
	
	
	
	
	@RequestMapping(value = "/getItemStatus")
	@ResponseBody
	public Integer getItemStatus(HttpServletRequest request) {
		String id = request.getParameter("id");
		Integer itemStatus = 0;
		if(StringUtils.isNotEmpty(id)){
			ItemDTO item = itemInfoService.getItemInfoById(id);
			if(item!=null){
				itemStatus = item.getItemStatus();
			}
		}
		return itemStatus;
	}
	
	
	@RequestMapping(value = "/checkServiceItemCity")
	@ResponseBody
	public ExecuteResult<Boolean> getItemStatus(String itemId,String cityCode) {
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setCityCode(Long.parseLong(cityCode));
		mallThemeDTO.setStatus(1);
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		if(StringUtils.isNotEmpty(itemId)){
			ItemDTO item = itemInfoService.getItemInfoById(itemId);
			if(item!=null){
				int addSource = item.getAddSource();
				//服务商品
				if(addSource > 3 ){
					if(null != cityCode && !"".equals(cityCode)){
						DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO,null, new Pager());
						List<MallThemeDTO> list  = mallThemeList.getRows();
						if(list != null && !list.isEmpty() && list.size() > 0){
							result.setResult(true);
						}else{
							result.setResult(false);
							result.setResultMessage("所选地区无服务商，不允许购买");
						}
					}else{
						result.setResult(false);
						result.setResultMessage("未获取到地域信息，无法进行子站校验");
					}
				}else if(addSource == 3 ){
					//套装商品，判断套装里面是否包含服务
					ItemSkuPackageDTO itemSkuPackage = new ItemSkuPackageDTO();
					itemSkuPackage.setPackageItemId(item.getItemId());
					List<Integer> addSources = new ArrayList<Integer>();
					addSources.add(ItemAddSourceEnum.BASICSERVICE.getCode());
					addSources.add(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
					addSources.add(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());
					itemSkuPackage.setAddSources(addSources);
					List<ItemSkuPackageDTO> pakelist= itemSkuPackageService.getPackages(itemSkuPackage);
					if(pakelist != null && !pakelist.isEmpty() && pakelist.size() > 0){
						DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO,null, new Pager());
						List<MallThemeDTO> list  = mallThemeList.getRows();
						if(list != null && !list.isEmpty() && list.size() > 0){
							result.setResult(true);
						}else{
							result.setResult(false);
							result.setResultMessage("所选地区无服务商，不允许购买");
						}
					}else{
						result.setResult(true);
					}
				}else{
					result.setResult(true);
				}
			}else{
				result.setResult(false);
				result.setResultMessage("商品不存在");
			}
		}
		return result;
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
			System.out.println("-----------consults======="+JSON.toJSONString(consults) );
			System.out.println("-----------pager======="+JSON.toJSONString(pager) );
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
		Long buyerId = WebUtil.getInstance().getUserId(request);
		String itemId = request.getParameter("itemId");
		String consulting = request.getParameter("consulting");
		String sellerId = request.getParameter("sellerId");
		if(buyerId == null){
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
		System.out.println("result======="+result);
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
			System.out.println(s);
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
	public JSONObject getSkuPrice(HttpServletRequest request, Model model) {
    	Map<String,Long> map=new HashMap<String,Long>();
    	String strAreaCode = request.getParameter("areaCode");
    	String strSkuId = request.getParameter("skuId");
    	String strQty = request.getParameter("qty");
    	String strShopId = request.getParameter("shopId");
    	String strItemId = request.getParameter("itemId");
    	String sellerId = request.getParameter("sellerId");
    	String[] valueAddedSkuIds = request.getParameterValues("valueAddedSkuIds[]");
    	JSONObject jsonObj = new JSONObject();
    	if(StringUtils.isEmpty(strSkuId) || !StringUtils.isNumeric(strSkuId)){
    		jsonObj.put("skuId", "skuId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	if(StringUtils.isEmpty(strQty) || !StringUtils.isNumeric(strQty)){
    		jsonObj.put("qty", "qty数量不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	if(StringUtils.isEmpty(strShopId) || !StringUtils.isNumeric(strShopId)){
    		jsonObj.put("shopId", "shopId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	if(StringUtils.isEmpty(strItemId) || !StringUtils.isNumeric(strItemId)){
    		jsonObj.put("strItemId", "strItemId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	if(StringUtils.isEmpty(sellerId) || !StringUtils.isNumeric(sellerId)){
    		jsonObj.put("sellerId", "sellerId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	ExecuteResult<ItemBaseDTO> res=itemService.getItemBaseInfoById(Long.parseLong(strItemId));
    	Integer userType=null;
    	UserDTO user =null;
    	Long uid = WebUtil.getInstance().getUserId(request);
    	if(uid != null){
    		user = this.userExportService.queryUserById(uid);
    		if(user!=null){
    			if(user.getUsertype()==1){
    				userType=1;
    			}else{
    				userType=2;
    			}
    		}
    	}
    	// 查询商品SKU信息
    	ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setAreaCode(strAreaCode);//省市区编码
		dto.setSkuId(Long.valueOf(strSkuId));//SKU id
		dto.setQty(Integer.parseInt(strQty));//数量
		dto.setShopId(Long.valueOf(strShopId));//店铺ID
		dto.setItemId(Long.valueOf(strItemId));//商品ID
		//判断用户是否登录
		RegisterDTO registerDTO = userExportService.getUserByRedis(LoginToken.getLoginToken(request));
		if(registerDTO!=null){
			dto.setBuyerId(registerDTO.getUid());
		}
		dto.setSellerId(Long.valueOf(sellerId));
		ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto);
		jsonObj.put("priceObject", er);
		if (er.isSuccess() && er.getResult() != null) {
			// 计算增值服务价格
			BigDecimal valueAddedPrice = this.getValueAddedPrice(valueAddedSkuIds, strAreaCode);
			if(er.getResult().isHasPrice() && er.getResult().getSkuPrice() != null){
				er.getResult().setSkuPrice(er.getResult().getSkuPrice().add(valueAddedPrice));
			}
		}
		//计算促销价
		if (er.getResult() != null) {
			//促销
			PromotionInDTO promotInDTO = new PromotionInDTO();
			promotInDTO.setItemId(Long.valueOf(strItemId));
			promotInDTO.setShopId(Long.valueOf(strShopId));
			promotInDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
			/*promotInDTO.setUserType(userType);//用户类型
				if(user!=null){
		        	// 获取成长值，判断用户会员等级
					BigDecimal growthValue = user.getGrowthValue();
					if(growthValue==null){
						growthValue=BigDecimal.ZERO;
					}
					VipLevelEnum vipLevel= VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
					promotInDTO.setMembershipLevel(vipLevel.getId().toString());
			      }
			 */
			promotInDTO.setType(1);//查询促销
			if(res!=null&&res.getResult()!=null){
				if(res.getResult().getPlatformId()==null){//该商品为科印商品
					promotInDTO.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_KY_ID);
				}else if(res.getResult().getPlatformId()==2){//该商品为绿印商品
					promotInDTO.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_ID);
				}
			}
			BigDecimal disPrice=BigDecimal.ZERO;
			ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(promotInDTO, null);
			if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
				List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
	 			for (PromotionOutDTO pro : listPromotionOutDTO) {
	 				PromotionInfo promotionInfo=pro.getPromotionInfo(); 
	 				PromotionMarkdown markdown = pro.getPromotionMarkdown();
	 				if(map.get(strItemId)!=null){
	 					if(map.get(strItemId)<promotionInfo.getShopId()){
	 						if (er.getResult().getSkuPrice() != null && er.getResult().getSkuPrice().compareTo(new BigDecimal(0))>0) {
	 							disPrice=calDownPrice(er.getResult().getSkuPrice(),markdown,disPrice);
			 					//设置SKU价格 为促销价
			 					jsonObj.put("disPrice", disPrice);
							}else {
								jsonObj.put("disPrice", new BigDecimal(0));
							}
	 					}
	 				}else{
	 					map.put(strItemId,promotionInfo.getShopId());
	 					if (er.getResult().getSkuPrice() != null && er.getResult().getSkuPrice().compareTo(new BigDecimal(0))>0) {
		 					//计算促销价格
	 						disPrice=calDownPrice(er.getResult().getSkuPrice(),markdown,disPrice);
		 					//设置SKU价格 为促销价
		 					jsonObj.put("disPrice", disPrice);
						}else {
							jsonObj.put("disPrice", new BigDecimal(0));
						}
	 				}
		 				
	 			}
			}
		}
		return jsonObj;
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
   	public JSONObject getPromotion(HttpServletRequest request, Model model) {
    	Map<String,Long> map=new HashMap<String,Long>();
    	String areaCode = request.getParameter("areaCode");
    	String itemId = request.getParameter("itemId");
    	String shopId = request.getParameter("shopId");
    	String skuId = request.getParameter("skuId");
    	String sellerId = request.getParameter("sellerId");
    	String[] valueAddedSkuIds = request.getParameterValues("valueAddedSkuIds[]");
    	JSONObject jsonObj = new JSONObject();
    	if(StringUtils.isEmpty(itemId) || !StringUtils.isNumeric(itemId)){
    		jsonObj.put("ItemId", "ItemId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	if(StringUtils.isEmpty(shopId) || !StringUtils.isNumeric(shopId)){
    		jsonObj.put("shopId", "shopId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	if(StringUtils.isEmpty(sellerId) || !StringUtils.isNumeric(sellerId)){
    		jsonObj.put("sellerId", "sellerId不能为空，并且必须是数字");
    		return jsonObj;
    	}
    	ExecuteResult<ItemBaseDTO> res=itemService.getItemBaseInfoById(Long.parseLong(itemId));
    	UserDTO user=null;
    	Integer userType=null;
    	Long uid = WebUtil.getInstance().getUserId(request);
    	if(uid != null){
    		user = this.userExportService.queryUserById(uid);
    		if(user!=null){
    			if(user.getUsertype()==1){
    				userType=1;
    			}else{
    				userType=2;
    			}
    		}
    	}
    	//促销价信息
	    BigDecimal disPrice = null;
	    //sku价
	    BigDecimal skuShowPrice = null;
    	//查询该商品
    	ItemDTO item = itemInfoService.getItemInfoById(itemId);
    	//查询SKU价格
    	if(!StringUtils.isEmpty(skuId) && StringUtils.isNumeric(skuId)){
    		
    		ItemShopCartDTO inDTO = new ItemShopCartDTO();
    		inDTO.setAreaCode(areaCode);
    		inDTO.setItemId(Long.valueOf(itemId));
    		inDTO.setSkuId(Long.valueOf(skuId));
    		inDTO.setShopId(Long.valueOf(shopId));
    		//判断用户是否登录
    		RegisterDTO registerDTO = userExportService.getUserByRedis(LoginToken.getLoginToken(request));
    		if(registerDTO!=null){
    			inDTO.setBuyerId(registerDTO.getUid());
    		}
    		inDTO.setSellerId(Long.valueOf(sellerId));
    		skuShowPrice = itemPriceExportService.getSkuShowPrice(inDTO);
    		// 组装增值服务参数
			Map<Long, Long> valueAddedMap = new HashMap<Long, Long>();
			if (valueAddedSkuIds != null && valueAddedSkuIds.length > 0) {
				for (String valueAddedSkuIdStr : valueAddedSkuIds) {
					String[] kv = valueAddedSkuIdStr.split("-");
					Long valueAddedItemId = Long.valueOf(kv[0]);
					Long valueAddedSkuId = Long.valueOf(kv[1]);
					valueAddedMap.put(valueAddedItemId, valueAddedSkuId);
				}
			}
    		// 计算增值服务价格
			BigDecimal valueAddedPrice = this.getValueAddedPrice(valueAddedSkuIds, areaCode);
			if(skuShowPrice != null){
				skuShowPrice = skuShowPrice.add(valueAddedPrice);
			}
    	}
    	// 获取商品营销活动
 		PromotionInDTO pid = new PromotionInDTO();
 		pid.setItemId(Long.valueOf(itemId));
 		pid.setShopId(Long.valueOf(shopId));
 		pid.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));////不为空时 查询有效期内活动
 		pid.setOnlineState(1);//1 一上线的
// 		pid.setUserType(userType);//用户类型
 		if(res!=null&&res.getResult()!=null){
			if(res.getResult().getPlatformId()==null){//该商品为科印商品
				pid.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_KY_ID);
			}else if(res.getResult().getPlatformId()==2){//该商品为绿印商品
				pid.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_ID);
			}
		}
		/*if(user!=null){
        	// 获取成长值，判断用户会员等级
			BigDecimal growthValue = user.getGrowthValue();
			if(growthValue==null){
				growthValue=BigDecimal.ZERO;
			}
			VipLevelEnum vipLevel= VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
			pid.setMembershipLevel(vipLevel.getId().toString());
	      }*/
 		ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(pid, null);
 		if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
 			List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
 			JSONArray markdownJsonArray = new JSONArray();
 			JSONArray fullJsonArray = new JSONArray();
 			for(PromotionOutDTO pro : listPromotionOutDTO){
 				JSONObject jo = new JSONObject();
 				//类型
 				Integer type = pro.getPromotionInfo().getType();
 				//名称
 				String activityName = pro.getPromotionInfo().getActivityName();
 				jo.put("activityName", activityName);
 				SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
 				Date startTimeDate = pro.getPromotionInfo().getStartTime();
 				String startTime = format.format(startTimeDate);
 				jo.put("startTime", startTime);
 				Date endTimeDate = pro.getPromotionInfo().getEndTime();
 				String endTime = format.format(endTimeDate);
 				jo.put("endTime", endTime);
 				// 服务器时间
 				jo.put("serverTime", DateUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"));
 				PromotionInfo promotionInfo=pro.getPromotionInfo();
 				//促销类型，1：直降，2：满减
 				if(type == 1){//同一件商品如果平台和店铺都有直降活动，则取店铺直降活动
 					PromotionMarkdown markdown = pro.getPromotionMarkdown();
					if (markdown != null) {
						if (map.get(itemId) != null) {
							if (map.get(itemId) < promotionInfo.getShopId()) {
								BigDecimal promotionPrice = markdown.getPromotionPrice();
								jo.put("promotionPrice", promotionPrice);
								Integer markdownType = markdown.getMarkdownType();
								jo.put("markdownType", markdownType);
								BigDecimal discountPercent = markdown.getDiscountPercent();
								jo.put("discountPercent", discountPercent);
								if(markdownType == 1 && discountPercent != null){
									jo.put("discountPercent", discountPercent.multiply(BigDecimal.valueOf(10)));
								}
								// 得到促销活动折扣值
								List<SellPrice> sellPrice = item.getSellPrices();
								if (skuShowPrice != null && skuShowPrice.compareTo(new BigDecimal(0)) > 0) {
									disPrice = calDownPrice(skuShowPrice, markdown, disPrice);
								} else {
									if (sellPrice != null && sellPrice.size() > 0) {
										BigDecimal price = sellPrice.get(0).getSellPrice();
										disPrice = calDownPrice(price, markdown, disPrice);
									}
								}
								jo.put("disPrice", disPrice);
								jo.put("skuShowPrice", skuShowPrice);// sku售价
								markdownJsonArray.clear();
								markdownJsonArray.add(jo);
							}
						} else {
							map.put(itemId, promotionInfo.getShopId());
							BigDecimal promotionPrice = markdown.getPromotionPrice();
							jo.put("promotionPrice", promotionPrice);
							Integer markdownType = markdown.getMarkdownType();
							jo.put("markdownType", markdownType);
							BigDecimal discountPercent = markdown.getDiscountPercent();
							jo.put("discountPercent", discountPercent);
							if(markdownType == 1 && discountPercent != null){
								jo.put("discountPercent", discountPercent.multiply(BigDecimal.valueOf(10)));
							}
							// 得到促销活动折扣值
							List<SellPrice> sellPrice = item.getSellPrices();
							if (skuShowPrice != null && skuShowPrice.compareTo(new BigDecimal(0)) > 0) {
								disPrice = calDownPrice(skuShowPrice, markdown, disPrice);
							} else {
								if (sellPrice != null && sellPrice.size() > 0) {
									BigDecimal price = sellPrice.get(0).getSellPrice();
									disPrice = calDownPrice(price, markdown, disPrice);
								}
							}
							jo.put("disPrice", disPrice);
							jo.put("skuShowPrice", skuShowPrice);// sku售价
							markdownJsonArray.add(jo);
						}

					}
				} else if (type == 2) {
					PromotionFullReduction full = pro.getPromotionFullReduction();
					if (full != null) {
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
 		return jsonObj;
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
     		System.out.println("promotionItems====="+JSON.toJSONString(erdg));

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
     				ItemDTO item = itemInfoService.getItemInfoById(itemId.toString());
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
     			System.out.println("item======"+itemJsonArray.toJSONString());
     			System.out.println("skuItem======"+skuItemJsonArray.toJSONString());
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
   	public String toProductCatalog(Pager pager, Long itemId, Model model,HttpServletRequest request ) {
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
    	ItemDTO item1 = itemInfoService.getItemInfoById(itemId.toString());

    	List<JSONObject> items = new ArrayList<JSONObject>();
    	if(erItems.isSuccess() && erItems.getResult() != null ){
        	for(ItemQueryOutDTO dto:erItems.getResult().getRows()){
        		JSONObject item = JSON.parseObject(JSON.toJSONString(dto));
        		ExecuteResult<ShopDTO> shop = this.shopExportService.findShopInfoById(dto.getShopId());
        		if( shop.isSuccess() && shop.getResult() != null ){
        			item.put("shopId", shop.getResult().getShopId());
        			item.put("shopName", shop.getResult().getShopName());
        			item.put("scope", shop.getResult().getScope());
        		}
        		items.add(item);
        	}

        	pager.setTotalCount(erItems.getResult().getTotal().intValue());
        	pager.setRecords(items);
    	}
    	String userToken = LoginToken.getLoginToken(request);
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				model.addAttribute("logging_status", "true");
			}else{
				model.addAttribute("logging_status", "false");
			}
		}
		//配置商品大全详情页的SEO
		BaseTDKConfigDTO  baseTDKConfigDTO=new BaseTDKConfigDTO();
		baseTDKConfigDTO.setTitle(item1.getItemName()+"【图片  价格   品牌 评价】 "+" - 舒适100");
		baseTDKConfigDTO.setDescription(item1.getItemName()+" 图片、价格、品牌、评论 【舒适100，全国配送，优惠多多】");
		baseTDKConfigDTO.setKeywords(item1.getKeywords()+","+item1.getBrandName()+","+er.getResult().getItemName()+",舒适100");
		model.addAttribute("baseTDKConfigDTO5", baseTDKConfigDTO);
		model.addAttribute("shopEvaluationResult", er);
				
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

    	System.out.println("------------"+JSON.toJSONString(result));
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
    	ItemDTO item = itemInfoService.getItemInfoById(itemId);
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
    	System.out.println("询价查询结果===="+JSON.toJSONString(result));
    	return result;
    }
    
    /**
     * 
     * <p>Description: [根据skuId查询sku图片]</p>
     * Created on 2015年9月29日
     * @param skuId
     * @param request
     * @param model
     * @return
     * @author:[宋文斌]
     */
	@ResponseBody
	@RequestMapping("/querySkuPics")
	public Object querySkuPics(Long skuId, HttpServletRequest request, Model model) {
		JSONObject jsonObject = new JSONObject();
		ExecuteResult<List<SkuPictureDTO>> result = itemService.querySkuPics(skuId);
		if (result.isSuccess() && result.getResult() != null && result.getResult().size() > 0) {
			jsonObject.put("skuPicUrl", result.getResult().get(0).getPicUrl());
		}
		jsonObject.put("skuPics", result.getResult());
		return jsonObject;
	}
	
	/**
	 * <p>获取商品评价总数</p>
	 * Created on 2016年2月29日
	 * @param queryIn
	 * @return
	 * @author: 顾雨
	 */
	@ResponseBody
	@RequestMapping("/getItemEvaluationCount")
	public Object getItemEvaluationCount(ItemEvaluationQueryInDTO queryIn) {
		Map<String, Object> response = new HashMap<String, Object>();
		ExecuteResult<Integer> richCount = itemEvaluationService.queryEvalCount(queryIn);
		response.put("success", richCount.isSuccess());
		response.put("count", richCount.getResult());
		
		return response;
	}
	
	/**
	 * <p>获取商品评价区块头部内容</p>
	 * Created on 2016年2月29日
	 * @param queryIn 检索参数
	 * @return
	 * @author: 顾雨
	 */
	@RequestMapping("/getItemEvaluationHead")
	public String getItemEvaluationHead(ItemEvaluationQueryInDTO queryIn, Model model) {
		// 商品综合评价
		ExecuteResult<? extends Number> er_avgSkuScope = itemEvaluationService.queryAvgSkuScope(queryIn);
		model.addAttribute("avgSkuScope", er_avgSkuScope.isSuccess() ? er_avgSkuScope.getResult() : 0);
		
		// 买家印象,获取sku的评价标签
		List<EvalTagCountDTO> evalTagCounts = itemEvaluationService.queryTagEvalsCountOfSku(queryIn);
		if (!evalTagCounts.isEmpty()) {
			model.addAttribute("evalTagCounts", evalTagCounts);
		}
		// 有图评论数
		ExecuteResult<Integer> er_evalShowCount = itemEvaluationService.queryEvalShowCount(queryIn);
		model.addAttribute("evalShowCount", er_evalShowCount.isSuccess() ? er_evalShowCount.getResult() : 0);
		
		model.addAttribute("queryIn", queryIn);
		return "/goodscenter/product/evaluationHeader";
	}

	/**
	 * <p>评价内容分页</p>
	 * Created on 2016年2月29日
	 * @param dto
	 * @param pager
	 * @param model
	 * @return
	 * @author: 顾雨
	 */
    @RequestMapping("/getItemEvaluation")
    public String getItemEvaluation(ItemEvaluationQueryInDTO dto,Pager<JSON> pager,Model model){
		LOG.debug("商品评价查询参数" + JSON.toJSONString(dto));
		dto.setType("1");
		dto.setReply("0");
		
		// 所有的评价
		DataGrid<ItemEvaluationQueryOutDTO> dg = this.itemEvaluationService.queryItemEvaluationList(dto, pager);
		List<ItemEvaluationQueryOutDTO> lists = dg.getRows();
		
		Map<String, List<ItemAttr>> evalMapToItemAttrs = new HashMap<String, List<ItemAttr>>();
		model.addAttribute("evalMapToItemAttrs", evalMapToItemAttrs);
		Map<String, Date> evalMapToOrderTime = new HashMap<String, Date>();
		model.addAttribute("evalMapToOrderTime", evalMapToOrderTime);
		if (null != lists && lists.size() > 0) {
			// 每条评价的所有回复
			for (ItemEvaluationQueryOutDTO list : lists) {
				ItemEvaluationReplyDTO itemEvaluationReplyDTO = new ItemEvaluationReplyDTO();
				itemEvaluationReplyDTO.setEvaluationId(list.getId());
				DataGrid<ItemEvaluationReplyDTO> itemEvaluationReplyDTOs = itemEvaluationService
						.queryItemEvaluationReplyList(itemEvaluationReplyDTO, null);
				if (null != itemEvaluationReplyDTOs && itemEvaluationReplyDTOs.getTotal() > 0) {
					List<ItemEvaluationReplyDTO> rows = itemEvaluationReplyDTOs.getRows();
					list.setItemEvaluationReplyList(rows);
				}
				// 每条评价的评价标签
				ExecuteResult<List<EvalTag>> er_tags = itemEvaluationService.queryEvalTagsOfOneEvaluation(list.getId());
				if (er_tags.isSuccess()) {
					list.setEvalTags(er_tags.getResult());
				}
				
				// 每条评价对应有sku，获取该sku的销售属性
				ExecuteResult<String> er_attrStr = itemService.queryAttrBySkuId(list.getSkuId());
				if (er_attrStr.isSuccess()) {
					ExecuteResult<List<ItemAttr>> er_itemAttrs= itemCategoryService.queryCatAttrByKeyVals(er_attrStr.getResult());
					if (er_itemAttrs.isSuccess()) {
						evalMapToItemAttrs.put(list.getId()+"", er_itemAttrs.getResult());
					}
				}
				
				// 每条评价对应一笔订单，获取订单下单事件
				ExecuteResult<TradeOrdersDTO> er_order = tradeOrderExportService.getOrderById(list.getOrderId());
				if (er_order.isSuccess()) {
					evalMapToOrderTime.put(list.getId()+"", er_order.getResult().getCreateTime());
				}

			}

		}
		// 晒单图片实体类
		ItemEvaluationShowDTO itemEvaluationShowDTO;
		// 晒单图片返回结果
		ExecuteResult<DataGrid<ItemEvaluationShowDTO>> showPicResult;
		List<ItemEvaluationShowDTO> picList;
		List<JSON> rows = new ArrayList<JSON>();
		for (ItemEvaluationQueryOutDTO ie : dg.getRows()) {
			JSONObject row = JSONObject.parseObject(JSON.toJSONString(ie));
			UserDTO user = this.userExportService.queryUserById(ie.getUserId());
			if (user != null) {
				row.put("userName", user.getUname());
			}
			// 查询晒单图片根据评价表主键id
			itemEvaluationShowDTO = new ItemEvaluationShowDTO();
			itemEvaluationShowDTO.setEvaluationId(ie.getId());
			showPicResult = itemEvaluationService.queryItemEvaluationShowList(itemEvaluationShowDTO, new Pager());
			if (null != showPicResult.getResult()) {
				picList = showPicResult.getResult().getRows();
				if (null != picList && !picList.isEmpty()) {
					row.put("picList", picList);
					row.put("isHavePic", true);
				} else {
					row.put("picList", "");
					row.put("isHavePic", false);
				}
			} else {
				row.put("picList", "");
				row.put("isHavePic", false);
			}
			rows.add(row);
		}

		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(rows);
		model.addAttribute("pager", pager);
		model.addAttribute("urlFtp", SysProperties.getProperty("ftp_server_dir"));
		return "/goodscenter/product/productEvaluation";
    }
    
    /**
     * 
     * <p>Description: [根据商品ID和地区ID获取运送方式]</p>
     * Created on 2015年11月5日
     * @param itemId
     * @param regionId
     * @param request
     * @return
     * @author:[宋文斌]
     */
	@ResponseBody
	@RequestMapping("/getDeliveryType")
	public List<DeliveryTypeFreightDTO> getDeliveryType(Long itemId, Long regionId, HttpServletRequest request) {
		List<DeliveryTypeFreightDTO> deliveryTypeFreights = new ArrayList<DeliveryTypeFreightDTO>();
		// 查询商品
		ExecuteResult<ItemDTO> itemExecuteResult = itemService.getItemById(itemId);
		if (itemExecuteResult.isSuccess() && itemExecuteResult.getResult() != null) {
			ItemDTO itemDTO = itemExecuteResult.getResult();
			deliveryTypeFreights = this.calcEveryDeliveryTypeFreight(regionId + "", itemDTO);
		}
		return deliveryTypeFreights;
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
		ExecuteResult<List<DeliveryTypeFreightDTO>> result = shopCartFreightService.calcEveryDeliveryTypeFreight(inPriceDTOs, false);
		if(result.isSuccess() && result.getResult() != null && result.getResult().size() > 0){
			deliveryTypeFreights = result.getResult();
		}
		return deliveryTypeFreights;
	}
	/**
	 * 校验购买商品是否是买家自己店铺的商品
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/checkItem")
	@ResponseBody
	public Json checkItem(HttpServletRequest request) {
		Json json = new Json();
		String id = request.getParameter("id");
		try {
			Long uid = WebUtil.getInstance().getUserId(request);
			UserDTO user = null;
			if (uid != null) {
				user = this.userExportService.queryUserById(uid);
			}
			if (StringUtils.isNotEmpty(id)) {
				ExecuteResult<ItemBaseDTO> dg = itemService.getItemBaseInfoById(Long.parseLong(id));
				if (user != null && user.getShopId() != null) {
					if (user.getShopId().equals(dg.getResult().getShopId())) {
						json.setMsg("买家不允许购买自己店铺的商品");
						json.setSuccess(true);
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			json.setMsg("系统出现意外错误，请联系管理员");
			json.setSuccess(false);
		}
		return json;
	}
	
	//计算直降价格
	private BigDecimal calDownPrice(BigDecimal skuShowPrice, PromotionMarkdown markdown, BigDecimal disPrice) {
		if (markdown.getMarkdownType() != null) {
			if(skuShowPrice==null){//sku价格为空，默认为0
				skuShowPrice=BigDecimal.ZERO;
			}
			if (1 == markdown.getMarkdownType()) {// 百分比
				// 计算促销价格
				disPrice = skuShowPrice.multiply(markdown.getDiscountPercent()).setScale(2, BigDecimal.ROUND_DOWN);
			} else if (2 == markdown.getMarkdownType()) {// 促销价格
				disPrice = skuShowPrice.subtract(markdown.getPromotionPrice());
				if (disPrice.compareTo(new BigDecimal(0)) <= 0) {
					disPrice = BigDecimal.ZERO;
				}
			}
		}
		return disPrice;
	}
	
	/**
	 * 
	 * <p>Discription:[校验子站是否存在]</p>
	 * Created on 2016年3月1日
	 * @param cityCode
	 * @return
	 * @author:[李伟龙]
	 */
	@RequestMapping("checkMallTheme")
	@ResponseBody
	public ExecuteResult<Boolean> checkMallTheme(String cityCode) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		if(null != cityCode && !"".equals(cityCode)){
			MallThemeDTO mallThemeDTO = new MallThemeDTO();
			mallThemeDTO.setCityCode(Long.parseLong(cityCode));
			mallThemeDTO.setStatus(1);
			DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO,null, new Pager());
			List<MallThemeDTO> list  = mallThemeList.getRows();
			if(list != null && !list.isEmpty() && list.size() > 0){
				result.setResult(true);
			}else{
				result.setResult(false);
				result.setResultMessage("所选地区无服务商，不允许购买");
			}
		}else{
			result.setResult(false);
			result.setResultMessage("未获取到地域信息，无法进行子站校验");
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[计算所选增值服务的价格]</p>
	 * Created on 2016年3月4日
	 * @param valueAddedSkuIds 增值服务的数组形式，数组中的每个元素的形式：增值服务商品ID-增值服务SkuId
	 * @param areaCode 区域编码
	 * @return 增值服务的价格
	 * @author:[宋文斌]
	 */
	private BigDecimal getValueAddedPrice(String[] valueAddedSkuIds, String areaCode){
		// 组装增值服务参数
		Map<Long, Long> valueAddedMap = new HashMap<Long, Long>();
		if (valueAddedSkuIds != null && valueAddedSkuIds.length > 0) {
			for (String valueAddedSkuIdStr : valueAddedSkuIds) {
				String[] kv = valueAddedSkuIdStr.split("-");
				Long itemId = Long.valueOf(kv[0]);
				Long skuId = Long.valueOf(kv[1]);
				valueAddedMap.put(itemId, skuId);
			}
		}
		// 计算增值服务价格
		BigDecimal valueAddedPrice = BigDecimal.ZERO;
		for (Entry<Long, Long> entry : valueAddedMap.entrySet()) {
			Long itemId = entry.getKey();
			Long skuId = entry.getValue();
			ItemDTO itemDTO = itemInfoService.getItemInfoById(String.valueOf(itemId));
			if (itemDTO != null && itemDTO.getHasPrice() == 1) {
				ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
				itemShopCartDTO.setAreaCode(areaCode);// 省市区编码
				itemShopCartDTO.setSkuId(skuId);// SKU id
				itemShopCartDTO.setShopId(itemDTO.getShopId());// 店铺ID
				itemShopCartDTO.setItemId(itemId);// 商品ID
				ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO);
				if (skuItem.isSuccess() && skuItem.getResult() != null && skuItem.getResult().isHasPrice()
						&& skuItem.getResult().getSkuPrice() != null) {
					valueAddedPrice = valueAddedPrice.add(skuItem.getResult().getSkuPrice());
				}
			}
		}
		return valueAddedPrice;
	}
}
