package com.camelot.mall.shopcart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.util.Signature;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.VipCardDTO;
import com.camelot.maketcenter.dto.emums.CentralPurchasingActivityRealStatusEnum;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.maketcenter.service.CentralPurchasingRefOrderExportService;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.maketcenter.service.VipCardService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.ProductAttrDTO;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ProductOutPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartCouponService;
import com.camelot.pricecenter.service.ShopCartFreightService;
import com.camelot.pricecenter.service.ShopCartFullReductionService;
import com.camelot.pricecenter.service.ShopCartIntegralService;
import com.camelot.pricecenter.service.ShopCartPromotionService;
import com.camelot.pricecenter.service.ShopCartVIPCardService;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsPackageDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.dto.enums.OrderTypeEnum;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {
	private static final Logger LOG = LoggerFactory.getLogger(ShopCartServiceImpl.class);
	private static Integer COOKIE_MAX_AGE = 1000 * 60 * 60 * 24 * 7;

	@Resource
	private RedisDB redisDB;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private PaymentExportService paymentService;
	@Resource
	private ShopCustomerServiceService shopCustomerServiceService;
	@Resource
	private VipCardService vipCardService;
	@Resource
    private ShopCartFreightService shopCartFreightService;
	@Resource
	private ShopCartPromotionService shopCartPromotionService;
	@Resource
	private ShopCartVIPCardService shopCartVIPCardService;
	@Resource
	private PromotionService promotionService; 
	@Resource
	private CentralPurchasingExportService centralPurchasingExportService;
	@Resource
	private CentralPurchasingRefOrderExportService centralPurchasingRefOrderExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopCartFullReductionService shopCartFullReductionService;
	@Resource
	private ShopCartCouponService shopCartCouponService;
	@Resource
	private ShopCartIntegralService shopCartIntegralService;
	@Resource
	private ItemSkuPackageService itemSkuPackageService;
	@Resource
    private CouponsExportService couponsExportService;
	
	@Override
	public ExecuteResult<String> add(ProductInPriceDTO product) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		ExecuteResult<ItemDTO> itemResult = itemService.getItemById(product.getItemId());
		if(itemResult.isSuccess() && itemResult.getResult() != null){
			boolean exists = false;
			Long skuid = product.getSkuId();
			String ctoken = product.getCtoken();
			String uid = product.getUid();
			List<ProductInPriceDTO> products = this.getAllProducts(ctoken,uid);
			ItemDTO itemDTO = itemResult.getResult();
			product.setAddSource(itemDTO.getAddSource());
			// 设置每个用户可购买的最大数量
			if(product.getActivitesDetailsId() != null){
				ExecuteResult<QueryCentralPurchasingDTO> result = centralPurchasingExportService.queryByDetailId(product.getActivitesDetailsId());
				if(result.isSuccess() && result.getResult() != null){
					product.setPerPerchaseMaxNum(result.getResult().getPerPerchaseMaxNum());
				}
			}
			
			for (ProductInPriceDTO p : products) {
				LOG.debug("SKUID:"+skuid +"    xxx:" + p.getSkuId());
				if ( skuid.compareTo(p.getSkuId()) == 0 ) {
					// 集采活动详情ID相同
					if ((product.getActivitesDetailsId() == null && p.getActivitesDetailsId() == null)
							|| (product.getActivitesDetailsId() != null && p.getActivitesDetailsId() != null 
								&& product.getActivitesDetailsId().longValue() == p.getActivitesDetailsId().longValue())) {
						p.setRegionId(product.getRegionId());
						p.setQuantity(p.getQuantity() + product.getQuantity());
						p.setChecked(true);
						p.setValueAddedMap(product.getValueAddedMap());
						exists = true;
						break;
					}
				}
			}

			if (!exists) {
				product.setChecked(true);
				products.add(product);
			}

			// 判断用户是否登录进行区别保存购物车数据
			if ( uid != null && !"".equals(uid)) {
				this.redisDB.set(uid, JSON.toJSONString(products));
			} else {
				this.redisDB.setAndExpire(ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE);
			}
			er.setResult(product.getCtoken());
		}
		return er;
		
	}

	@Override
	public ExecuteResult<String> edit(ProductInPriceDTO product) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		Long skuid = product.getSkuId();

		String ctoken = product.getCtoken();
		String uid = product.getUid();
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken,uid);

		for (ProductInPriceDTO p : products) {
			if (skuid.compareTo(p.getSkuId()) == 0) {
				if ((product.getActivitesDetailsId() == null && p.getActivitesDetailsId() == null)
						|| (product.getActivitesDetailsId() != null && p.getActivitesDetailsId() != null && product
								.getActivitesDetailsId().longValue() == p.getActivitesDetailsId().longValue())) {
					p.setRegionId(product.getRegionId());
					p.setQuantity(product.getQuantity());
					p.setPromId(product.getPromId());
					p.setChecked(product.getChecked());
					break;
				}
			}
		}

		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set( uid, JSON.toJSONString(products) );
		} else {
			this.redisDB.setAndExpire( ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE );
		}
		return er;
	}

	@Override
	public ExecuteResult<String> del(ProductInPriceDTO product) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		Long skuid = product.getSkuId();
		
		String ctoken = product.getCtoken();
		String uid = product.getUid();
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken,uid);
		
		for (int i = 0; i < products.size(); i++) {
			ProductInPriceDTO p = products.get(i);
			if ( skuid.compareTo(p.getSkuId()) == 0 ) {
				if ((product.getActivitesDetailsId() == null && p.getActivitesDetailsId() == null)
						|| (product.getActivitesDetailsId() != null && p.getActivitesDetailsId() != null && product
								.getActivitesDetailsId().longValue() == p.getActivitesDetailsId().longValue())) {
					products.remove(i);
					break;
				}
			}
		}

		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set( uid, JSON.toJSONString(products) );
		} else {
			this.redisDB.setAndExpire( ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE );
		}
		return er;
	}

	/**
	 * 
	 * <p>Discription:获取当前用户购物车商品</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@Override
	public List<ProductInPriceDTO> getAllProducts(String ctoken, String uid) {
		// 如果用户已登录，合并购物车商品
		String ckey = ctoken;
		if( StringUtils.isNotBlank(uid) ){
			this.mergeCart(ctoken, uid);
			ckey = uid;
		}
		String myCart = this.redisDB.get(ckey);
		return myCart == null || "".equals(myCart) ? new LinkedList<ProductInPriceDTO>() : JSON.parseArray(myCart, ProductInPriceDTO.class);
	}
	
	/**
	 * 
	 * <p>Discription:合并购物车，临时购物车及登录购物车合并</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @author:[Goma 郭茂茂]
	 */
	private void mergeCart(String ctoken, String uid) {
		String sTempCart = this.redisDB.get(ctoken);
		String sCart = this.redisDB.get(uid);

		List<Product> tProducts = JSON.parseArray(sTempCart, Product.class);
		List<Product> products = JSON.parseArray(sCart, Product.class);

		List<Product> nProducts = new LinkedList<Product>();

		if ( tProducts != null && tProducts.size() > 0 && products != null && products.size() > 0 ) {
			//将登录购物车存在的物品进行数量相加，登录购物车不存在的商品添加到临时变量
			for (Product tp : tProducts) {
				Long skuid = tp.getSkuId();
				boolean exists = false;
				
				for (Product p : products) {
					// 将登录购物车中存在的商品进行数量相加
					if ( skuid.compareTo(p.getSkuId()) == 0 ) {
						p.setQuantity(p.getQuantity() + tp.getQuantity());
						exists = true;
						break;
					}
				}
				// 将登录购物车中不存在的商品添加到临时变量中
				if (!exists) {
					nProducts.add(tp);
				}
			}

			//将登录购物车不存在的商品与登录购物车中商品合并
			nProducts.addAll(products);
		} else if ( tProducts != null && tProducts.size() > 0 ) {
			nProducts = tProducts;
		} else if( products != null && products.size() > 0 ){
			nProducts = products;
		}
		LOG.debug("MERGE CART:" + JSON.toJSONString(nProducts));
		this.redisDB.set(uid, JSON.toJSONString(nProducts));
		this.redisDB.del(ctoken);
	}
	
	/**
	 * 
	 * <p>Discription:[获取购物车/立即购买的所有商品，并按营销活动进行处理]</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @param areaCode
	 * @param pros 如果是立即购买，该值不为空，为购买的商品
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<ProductInPriceDTO> getProducts(String ctoken, String uid, String areaCode, List<ProductInPriceDTO> pros) {
		List<ProductInPriceDTO> allProducts = this.getAllProducts(ctoken, uid);
		List<ProductInPriceDTO> products = new LinkedList<ProductInPriceDTO>();
		UserDTO user = null;
		if (StringUtils.isNotBlank(uid)) {
			user = this.userExportService.queryUserById(Long.parseLong(uid));
		}
		if (pros != null && pros.size() > 0) {
			allProducts = pros;
		}
		for (ProductInPriceDTO p : allProducts) {
			boolean success = false;
			// 非集采活动的商品
			if (p.getActivitesDetailsId() == null) {
				success = this.setSku(p, areaCode);
			} else {
				success = this.setCentralPurchasingSku(p, StringUtils.isNotBlank(uid) ? Long.parseLong(uid) : null);
			}
			LOG.debug("设置SKUOK");
			if (success) {
				// 集采活动商品不参与任何活动
				if (p.getActivitesDetailsId() == null) {
					List<PromotionDTO> lists = new ArrayList<PromotionDTO>();
					// 计算商品营销活动
					ExecuteResult<ProductOutPriceDTO> result = shopCartPromotionService.calcPromotion(p, user);
					if (result.isSuccess() && result.getResult() != null) {
						ProductOutPriceDTO productOutPriceDTO = result.getResult();
						BeanUtils.copyProperties(productOutPriceDTO, p);
						products.add(p);
						for (PromotionDTO promotionDTO : result.getResult().getPromotions()) {
							lists.add(promotionDTO);
						}
						p.setPromotions(lists);
					}
				} else {
					products.add(p);
				}
			}
		}
		return products;
	}
	
	/**
	 * 
	 * <p>Discription:设置商品SKU信息</p>
	 * Created on 2015年3月9日
	 * @param product
	 * @author:[Goma 郭茂茂]
	 */
	private boolean setSku(ProductInPriceDTO product, String areaCode) {
		ItemShopCartDTO dto = new ItemShopCartDTO();
		// 查询商品SKU信息
		if (StringUtils.isBlank(areaCode)) {
			areaCode = product.getRegionId();
		}
		dto.setAreaCode(areaCode);
		dto.setSkuId(product.getSkuId());
		if(product.getQuantity() == null || product.getQuantity().intValue() == 0){
			dto.setQty(1);
		} else{
			dto.setQty(product.getQuantity());
		}
		dto.setShopId(product.getShopId());
		dto.setItemId(product.getItemId());
		if (product.getSellerId() != null && !"".equals(product.getSellerId())){
			dto.setSellerId(Long.valueOf(product.getSellerId()));
		}
		if (product.getUid() != null && !"".equals(product.getUid())){
			dto.setBuyerId(Long.valueOf(product.getUid()));
		}
		LOG.debug("获取SKU参数：" + JSON.toJSONString(dto));
		
		ExecuteResult<ItemShopCartDTO> er = null;
		if (product.getAddSource() != null && product.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()) {
			er = itemService.getCombinationSkuShopCart(dto);
		} else {
			er = itemService.getSkuShopCart(dto);
		}
		
		LOG.debug("通过SKUID查询商品SKU信息："+JSON.toJSONString(er));
		
		if( er.isSuccess() && er.getResult() != null ){
			ItemShopCartDTO iscd = er.getResult();
			product.setSkuPrice(iscd.getSkuPrice());
			product.setPayPrice(iscd.getSkuPrice());
			product.setTitle(iscd.getItemName());
			product.setSrc(iscd.getSkuPicUrl());
			product.setCid(iscd.getCid());
			product.setStatus(iscd.getItemStatus());
			product.setHasPrice(iscd.isHasPrice());
			// 增值服务
			Map<Long, Long> valueAddedMap = product.getValueAddedMap();
			if(valueAddedMap != null){
				List<ProductInPriceDTO> valueAddedProducts = new ArrayList<ProductInPriceDTO>();
				for(Entry<Long,Long> entry : valueAddedMap.entrySet()){
					// 增值服务的itemId
					Long itemId = entry.getKey();
					// 增值服务的skuId
					Long skuId = entry.getValue();
					// 判断该商品还有没有这个增值服务
					ItemSkuPackageDTO valueAddedItemSkuPackageInDTO = new ItemSkuPackageDTO();
					valueAddedItemSkuPackageInDTO.setPackageItemId(product.getItemId());
					valueAddedItemSkuPackageInDTO.setSubItemId(itemId);
					valueAddedItemSkuPackageInDTO.setSubSkuId(skuId);
					valueAddedItemSkuPackageInDTO.setAddSource(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
					List<ItemSkuPackageDTO> valueAddedItemSkuPackageDTOs = itemSkuPackageService.getPackages(valueAddedItemSkuPackageInDTO);
					if(valueAddedItemSkuPackageDTOs != null && valueAddedItemSkuPackageDTOs.size() > 0){
						// 增值服务
						ItemSkuPackageDTO itemSkuPackageDTO = valueAddedItemSkuPackageDTOs.get(0);
						ExecuteResult<ItemDTO> itemResult = itemService.getItemById(itemId);
						if (itemResult.isSuccess() && itemResult.getResult() != null
								&& itemResult.getResult().getItemStatus() == 4
								&& itemResult.getResult().getHasPrice() == 1) {
							ItemDTO itemDTO = itemResult.getResult();
							// 查询增值服务的sku价格
							ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
							itemShopCartDTO.setAreaCode(areaCode);//省市区编码
							itemShopCartDTO.setSkuId(skuId);//SKU id
							itemShopCartDTO.setQty(1);//数量
							itemShopCartDTO.setShopId(itemDTO.getShopId());//店铺ID
							itemShopCartDTO.setItemId(itemId);//商品ID
							ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO);
							if (skuItem.isSuccess() && skuItem.getResult() != null && product.getSkuPrice() != null
									&& skuItem.getResult().getSkuPrice() != null) {
								product.setSkuPrice(product.getSkuPrice().add(skuItem.getResult().getSkuPrice().multiply(BigDecimal.valueOf(itemSkuPackageDTO.getSubNum()))));
								product.setPayPrice(product.getPayPrice().add(skuItem.getResult().getSkuPrice().multiply(BigDecimal.valueOf(itemSkuPackageDTO.getSubNum()))));
								
								// 增值服务商品
								ProductInPriceDTO valueAddedProduct = new ProductInPriceDTO();
								// 商品Id
								valueAddedProduct.setItemId(itemId);
								// SkuId
								valueAddedProduct.setSkuId(skuId);
								// 商品状态
								valueAddedProduct.setStatus(itemDTO.getItemStatus());
								// 图片
								valueAddedProduct.setSrc(skuItem.getResult().getSkuPicUrl());
								// 商品名称
								valueAddedProduct.setTitle(itemDTO.getItemName());
								// 销售属性
								List<ProductAttrDTO> attrs = new ArrayList<ProductAttrDTO>();
								for(ItemAttr itemAttr : skuItem.getResult().getAttrSales()){
									ProductAttrDTO productAttrDTO = new ProductAttrDTO();
									productAttrDTO.setName(itemAttr.getName());
									productAttrDTO.setValue(itemAttr.getValues().get(0).getName());
									attrs.add(productAttrDTO);
								}
								valueAddedProduct.setAttrs(attrs);
								// 是否显示价格
								valueAddedProduct.setHasPrice(skuItem.getResult().isHasPrice());
								// sku价格
								valueAddedProduct.setSkuPrice(skuItem.getResult().getSkuPrice());
								// 付款单价
								valueAddedProduct.setPayPrice(skuItem.getResult().getSkuPrice());
								// 数量
								valueAddedProduct.setQuantity(itemSkuPackageDTO.getSubNum());
								// 付款总额
								valueAddedProduct.setPayTotal(valueAddedProduct.getSkuPrice().multiply(BigDecimal.valueOf(valueAddedProduct.getQuantity())).multiply(BigDecimal.valueOf(product.getQuantity())));
								valueAddedProduct.setAddSource(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
								valueAddedProducts.add(valueAddedProduct);
							}
						}
					}
				}
				if(valueAddedProducts != null && valueAddedProducts.size() > 0){
					product.setValueAddedProducts(valueAddedProducts);
				}
			}
			//--------------------套装单品----------------------
			// 查询组成该套装的单品信息
			ItemSkuPackageDTO itemSkuPackageDTO = new ItemSkuPackageDTO();
			itemSkuPackageDTO.setPackageItemId(product.getItemId());
			itemSkuPackageDTO.setPackageSkuId(product.getSkuId());
			itemSkuPackageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
			List<ItemSkuPackageDTO> itemSkuPackageDTOs = itemSkuPackageService.getPackages(itemSkuPackageDTO);
			if(itemSkuPackageDTOs != null && itemSkuPackageDTOs.size() > 0){
				List<ProductInPriceDTO> subProducts  = new ArrayList<ProductInPriceDTO>();
				for (ItemSkuPackageDTO skuPackageDTO : itemSkuPackageDTOs) {
					Long subItemId = skuPackageDTO.getSubItemId();
					Long subSkuId = skuPackageDTO.getSubSkuId();
					ExecuteResult<ItemDTO> subItemResult = itemService.getItemById(subItemId);
					if (subItemResult.isSuccess() && subItemResult.getResult() != null) {
						if(subItemResult.getResult().getItemStatus() != 4){
							product.setUnusualState(11);
							product.getUnusualMsg().add("抱歉！该套装中存在非在售的组合单品，暂时不能购买。");
						}
						if(subItemResult.getResult().getHasPrice() != 1){
							product.setUnusualState(12);
							product.getUnusualMsg().add("抱歉！该套装中存在非销售的组合单品，暂时不能购买。");
						}
						ItemDTO subItemDTO = subItemResult.getResult();
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(areaCode);// 省市区编码
						itemShopCartDTO.setSkuId(subSkuId);// skuId
						itemShopCartDTO.setQty(1);// 数量
						itemShopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(subItemId);// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查sku
						if (skuItem.isSuccess() && skuItem.getResult() != null && skuItem.getResult().getSkuPrice() != null) {
							// 单品
							ProductInPriceDTO subProduct = new ProductInPriceDTO();
							// 商品Id
							subProduct.setItemId(subItemId);
							// SkuId
							subProduct.setSkuId(subSkuId);
							// 商品状态
							subProduct.setStatus(subItemDTO.getItemStatus());
							// 图片
							subProduct.setSrc(skuItem.getResult().getSkuPicUrl());
							// 商品名称
							subProduct.setTitle(subItemDTO.getItemName());
							// 销售属性
							List<ProductAttrDTO> attrs = new ArrayList<ProductAttrDTO>();
							for(ItemAttr itemAttr : skuItem.getResult().getAttrSales()){
								ProductAttrDTO productAttrDTO = new ProductAttrDTO();
								productAttrDTO.setName(itemAttr.getName());
								productAttrDTO.setValue(itemAttr.getValues().get(0).getName());
								attrs.add(productAttrDTO);
							}
							subProduct.setAttrs(attrs);
							// 是否显示价格
							subProduct.setHasPrice(skuItem.getResult().isHasPrice());
							// sku价格
							subProduct.setSkuPrice(skuItem.getResult().getSkuPrice());
							// 付款单价
							subProduct.setPayPrice(skuItem.getResult().getSkuPrice());
							// 数量
							subProduct.setQuantity(skuPackageDTO.getSubNum());
							// 付款总额
							subProduct.setPayTotal(subProduct.getSkuPrice().multiply(BigDecimal.valueOf(subProduct.getQuantity()).multiply(BigDecimal.valueOf(product.getQuantity()))));
							subProduct.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
							subProducts.add(subProduct);
						}
					}
				}
				if(subProducts != null && subProducts.size() > 0){
					product.setSubProducts(subProducts);
				}
			}
			// 询价商品处理。。。 Modifyed by Goma 2015年6月16日 14:57:44
			if( !iscd.isHasPrice() ){
				product.setUnusualState(4);
				product.getUnusualMsg().add("暂无报价!");
			}
			
			// 校验订单状态是否有效
			if( product.getChecked() && product.getStatus() != 4 ){
//				product.setChecked( false );
				product.setUnusualState(1);
				
				product.getUnusualMsg().add("商品已下架,请重新选择！");
			}
			
			// 库存为0时不选中
//			if(iscd.getQty() <= 0){
//				product.setChecked(false);
//			}
			
			// 校验是否超库存
//			if( product.getChecked() && iscd.getQty() < product.getQuantity() ){
//				product.setUnusualState(2);
//				product.getUnusualMsg().add("库存不足，请重新设置购买数量!");
//			}
			BigDecimal total = product.isHasPrice() ? BigDecimal.valueOf(product.getQuantity()).multiply(product.getSkuPrice()) : BigDecimal.ZERO;
			product.setTotal(total);
			product.setPayTotal(total);
			product.setQty(iscd.getQty());
			
			// 设置商品 销售属性
			List<ProductAttrDTO> attrs = new LinkedList<ProductAttrDTO>();
			for(ItemAttr ia : iscd.getAttrSales()){
				ProductAttrDTO attr = new ProductAttrDTO();
				if(product.getAddSource() != null && product.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()){
					attr.setName("通用");
				} else{
					attr.setName(ia.getName());
				}
				
				StringBuffer val = new StringBuffer();
				for( ItemAttrValue iav:ia.getValues() ){
					if(product.getAddSource() != null && product.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()){
						val.append(iav.getItemSkuName()).append("&nbsp;");
					} else{
						val.append(iav.getName()).append("&nbsp;");
					}
				}
				attr.setValue(val.toString());
				attrs.add(attr);
			}
			product.setAttrs(attrs);
		}
		
		return er.isSuccess();
	}


	/**
	 * 
	 * <p>Discription:[设置集采商品的sku]</p>
	 * Created on 2015年12月18日
	 * @param product
	 * @param uid
	 * @return
	 * @author:[宋文斌]
	 */
	private boolean setCentralPurchasingSku(ProductInPriceDTO product, Long uid){
		ItemShopCartDTO dto = new ItemShopCartDTO();
		// 查询商品SKU信息
		dto.setAreaCode(product.getRegionId());
		dto.setSkuId(product.getSkuId());
		if(product.getQuantity() == null || product.getQuantity().intValue() == 0){
			dto.setQty(1);
		} else{
			dto.setQty(product.getQuantity());
		}
		dto.setShopId(product.getShopId());
		dto.setItemId(product.getItemId());
		if (product.getSellerId() != null && !"".equals(product.getSellerId())){
			dto.setSellerId(Long.valueOf(product.getSellerId()));
		}
		if (product.getUid() != null && !"".equals(product.getUid())){
			dto.setBuyerId(Long.valueOf(product.getUid()));
		}
		
		LOG.debug("获取SKU参数：" + JSON.toJSONString(dto));
		
		ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto);
		
		LOG.debug("通过SKUID查询商品SKU信息："+JSON.toJSONString(er));
		
		if( er.isSuccess() && er.getResult() != null ){
			ItemShopCartDTO iscd = er.getResult();
			// 查询集采价
			ExecuteResult<QueryCentralPurchasingDTO> result = centralPurchasingExportService.queryByDetailId(product.getActivitesDetailsId());
			if(result.isSuccess() && result.getResult() != null){
				QueryCentralPurchasingDTO queryCentralPurchasingDTO = result.getResult();
				
				if (product.getChecked() && queryCentralPurchasingDTO.getCentralPurchasingPrice() == null) {
					product.setUnusualState(4);
					product.getUnusualMsg().add("暂无报价!");
				}
				if(product.getChecked() && product.getUnusualMsg().size() == 0){
					// 判断购物车中加入的集采商品在没在活动有效期之内
					if (queryCentralPurchasingDTO.getDetailedStatus() == null
							|| queryCentralPurchasingDTO.getDetailedStatus().intValue() != CentralPurchasingActivityRealStatusEnum.INACTIVITY.getCode()) {
						product.setUnusualState(7);
						if (queryCentralPurchasingDTO.getDetailedStatus() != null
								&& queryCentralPurchasingDTO.getDetailedStatus().intValue() == CentralPurchasingActivityRealStatusEnum.ALREADY_SOLD_OUT.getCode()) {
							product.getUnusualMsg().add("该商品已售罄");
						} else {
							product.getUnusualMsg().add("该集采商品不在活动有效期之内");
						}
					}
				}
				if(product.getChecked() && product.getUnusualMsg().size() == 0){
					// 该用户已下单的该集采商品的数量
					int myOrderNum = 0;
					if(uid != null){
						// 查询集采活动的订单
						TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
						inDTO.setBuyerId(uid);
						inDTO.setCentralPurchasing(1);
						ExecuteResult<DataGrid<TradeOrdersDTO>> orderResult = tradeOrderExportService.queryOrders(inDTO, null);
						if (orderResult.isSuccess() && orderResult.getResult() != null
								&& orderResult.getResult().getRows() != null
								&& orderResult.getResult().getRows().size() > 0) {
							List<TradeOrdersDTO> tradeOrdersDTOs =  orderResult.getResult().getRows();
							for(TradeOrdersDTO tradeOrdersDTO : tradeOrdersDTOs){
								for(TradeOrderItemsDTO itemDTO : tradeOrdersDTO.getItems()){
									if (itemDTO.getItemId().longValue() == product.getItemId().longValue()
											&& itemDTO.getActivitesDetailsId().longValue() == queryCentralPurchasingDTO.getActivitesDetailsId().longValue()
											&& itemDTO.getSkuId().longValue() == queryCentralPurchasingDTO.getSkuId().longValue()) {
										myOrderNum += itemDTO.getNum();
									}
								}
							}
						}
					}
					
					if (queryCentralPurchasingDTO.getPerPerchaseMaxNum() != null) {
						int realNum = queryCentralPurchasingDTO.getPerPerchaseMaxNum().intValue() - myOrderNum - product.getQuantity();
						if(realNum < 0){
							int maxNum = queryCentralPurchasingDTO.getPerPerchaseMaxNum().intValue() - myOrderNum;
							product.setUnusualState(9);
							product.getUnusualMsg().add("您已经购买" + myOrderNum + "件，最多还可购买" + maxNum + "件");
						}
					} else if (queryCentralPurchasingDTO.getPerPerchaseMaxNum() == null
							|| product.getQuantity() > queryCentralPurchasingDTO.getPerPerchaseMaxNum()) {
						product.setUnusualState(8);
						product.getUnusualMsg().add("集采数量超过了每个用户可购买的最大数量");
					}
				}
				// 该集采商品已经付款的数量
				int orderNum = 0;
				CentralPurchasingRefOrderDTO CentralPurchasingRefOrderInDTO = new CentralPurchasingRefOrderDTO();
				CentralPurchasingRefOrderInDTO.setActivitesDetailsId(product.getActivitesDetailsId());
				CentralPurchasingRefOrderInDTO.setItemId(product.getItemId());
				CentralPurchasingRefOrderInDTO.setSkuId(product.getSkuId());
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
				// 库存
				int qty = 0;
				if(orderNum < queryCentralPurchasingDTO.getReleaseGoodsMaxNum().intValue()){
					qty = queryCentralPurchasingDTO.getReleaseGoodsMaxNum().intValue() - orderNum;
				}
				if(product.getChecked() && product.getUnusualMsg().size() == 0 && product.getQuantity() > qty){
					product.setUnusualState(3);
					product.getUnusualMsg().add("库存不足");
				}
				// 设置集采价
				product.setSkuPrice(queryCentralPurchasingDTO.getCentralPurchasingPrice());
				product.setPayPrice(queryCentralPurchasingDTO.getCentralPurchasingPrice());
				product.setTitle(iscd.getItemName());
				product.setSrc(iscd.getSkuPicUrl());
				product.setCid(iscd.getCid());
				product.setStatus(iscd.getItemStatus());
				product.setHasPrice(iscd.isHasPrice());
				BigDecimal total = queryCentralPurchasingDTO.getCentralPurchasingPrice() != null ? BigDecimal.valueOf(product.getQuantity()).multiply(product.getSkuPrice()) : BigDecimal.ZERO;
				product.setTotal(total);
				product.setPayTotal(total);
				product.setQty(qty);
			} else {
				product.setUnusualState(6);
				product.getUnusualMsg().add("该集采商品不存在!");
			}
			
			// 设置商品 销售属性
			List<ProductAttrDTO> attrs = new LinkedList<ProductAttrDTO>();
			for(ItemAttr ia : iscd.getAttrSales()){
				ProductAttrDTO attr = new ProductAttrDTO();
				attr.setName(ia.getName());
				
				StringBuffer val = new StringBuffer();
				for( ItemAttrValue iav:ia.getValues() )
					val.append(iav.getName()).append("&nbsp;");
				
				attr.setValue(val.toString());
				attrs.add(attr);
			}
			product.setAttrs(attrs);
		}
		
		return er.isSuccess();
	}
	/**
	 * 
	 * <p>Description: [计算店铺运费]</p>
	 * Created on 2015年10月29日
	 * @param shop
	 * @param shopDeliveryType key=运费模版ID，value=运送方式（1快递，2EMS，3平邮）
	 * @return
	 * @author:[宋文斌]
	 */
	private ExecuteResult<BigDecimal> calcShopFreight(ShopOutPriceDTO shop, Map<Long, Integer> shopDeliveryType) {
		LOG.info("\n 方法[{}]，入参：[{}][{}]", "ShopCartServiceImpl-calcShopFreight", JSONObject.toJSONString(shop), JSONObject.toJSONString(shopDeliveryType));
		ExecuteResult<BigDecimal> result = new ExecuteResult<BigDecimal>();
		try {
			// 店铺总运费
			BigDecimal totalFreight = BigDecimal.ZERO;
			// 按运费模版分组店铺中的商品。key=模版ID
			Map<Long, List<ProductInPriceDTO>> groupProducts = new HashMap<Long, List<ProductInPriceDTO>>();
			// 按运费模版分组商品运费。key=模版ID
			Map<Long, BigDecimal> groupFreight = new HashMap<Long, BigDecimal>();
			// 运费模版对应的运送方式
			Map<Long, List<DeliveryTypeFreightDTO>> groupDeliveryTypes = new HashMap<Long, List<DeliveryTypeFreightDTO>>();
			Map<Long, ItemDTO> itemDTOMap = new HashMap<Long, ItemDTO>();
			// 初始化
			if(shop.getProducts()!=null){
				for (int i = 0; i < shop.getProducts().size(); i++) {
					ProductInPriceDTO product = shop.getProducts().get(i);
					if (product.getChecked()) {
						// 查询商品
						ItemDTO itemDTO = null;
						if (itemDTOMap.get(product.getItemId()) == null) {
							ExecuteResult<ItemDTO> itemExecuteResult = itemService.getItemById(product.getItemId());
							if (itemExecuteResult.isSuccess() && itemExecuteResult.getResult() != null) {
								itemDTO = itemExecuteResult.getResult();
								itemDTOMap.put(product.getItemId(), itemDTO);
							}
						} else {
							itemDTO = itemDTOMap.get(product.getItemId());
						}
						if(itemDTO != null){
							product.setWeight(itemDTO.getWeight());
							product.setWeightUnit(itemDTO.getWeightUnit());
							product.setVolume(itemDTO.getVolume());
							product.setShopFreightTemplateId(itemDTO.getShopFreightTemplateId());
							if (groupProducts.get(product.getShopFreightTemplateId()) == null) {
								groupProducts.put(product.getShopFreightTemplateId(), new ArrayList<ProductInPriceDTO>());
								groupDeliveryTypes.put(product.getShopFreightTemplateId(), new ArrayList<DeliveryTypeFreightDTO>());
								groupFreight.put(product.getShopFreightTemplateId(), BigDecimal.ZERO);
							}
							groupProducts.get(product.getShopFreightTemplateId()).add(product);
						}
					}
				}
			}
			for (Entry<Long, List<ProductInPriceDTO>> entryProduct : groupProducts.entrySet()) {
				// 计算每种运送方式（快递，EMS，平邮）的运费
				ExecuteResult<List<DeliveryTypeFreightDTO>> er = shopCartFreightService.calcEveryDeliveryTypeFreight(entryProduct.getValue(), true);
				if(er.isSuccess()){
					if(er.getResult() != null && er.getResult().size() > 0){
						// 如果用户指定了运送方式，校验指定的运送方式是否正确，防止用户在前端乱改
						if (shopDeliveryType != null && shopDeliveryType.get(entryProduct.getKey()) != null) {
							List<Integer> deliveryTypes = new ArrayList<Integer>();
							for (DeliveryTypeFreightDTO deliveryTypeFreight : er.getResult()) {
								deliveryTypes.add(deliveryTypeFreight.getDeliveryType());
							}
							// 用户指定的运送方式
							Integer userDeliveryType = shopDeliveryType.get(entryProduct.getKey());
							// 指定的运送方式不存在
							if(!deliveryTypes.contains(userDeliveryType)){
								// result.addErrorMessage("指定的运送方式不存在");
								shopDeliveryType.put(entryProduct.getKey(), er.getResult().get(0).getDeliveryType());
							}
						}
						groupDeliveryTypes.put(entryProduct.getKey(), er.getResult());
						// 商品总运费
						BigDecimal freight = BigDecimal.ZERO;
						// 用户指定了运送方式
						if (shopDeliveryType != null && shopDeliveryType.get(entryProduct.getKey()) != null) {
							for(ProductInPriceDTO product : entryProduct.getValue()){
								// 设置商品的运送方式，用来在订单商品表存入商品的运送方式
								product.setDeliveryType(shopDeliveryType.get(entryProduct.getKey()));
							}
							for (DeliveryTypeFreightDTO deliveryTypeFreight : er.getResult()) {
								if (shopDeliveryType.get(entryProduct.getKey()) == deliveryTypeFreight.getDeliveryType()) {
									freight = deliveryTypeFreight.getGroupFreight();
									break;
								}
							}
						} else {
							for(ProductInPriceDTO product : entryProduct.getValue()){
								// 设置商品的运送方式，用来在订单商品表存入商品的运送方式
								product.setDeliveryType(er.getResult().get(0).getDeliveryType());
							}
							freight = er.getResult().get(0).getGroupFreight();
						}
						groupFreight.put(entryProduct.getKey(), freight);
						totalFreight = totalFreight.add(freight);
					}
				} else{
					result.getErrorMessages().addAll(er.getErrorMessages());
				}
			}
			shop.setGroupProducts(groupProducts);
			shop.setGroupFreight(groupFreight);
			shop.setGroupDeliveryTypes(groupDeliveryTypes);
			result.setResult(totalFreight);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			LOG.info("\n 方法[{}]，异常：[{}]", "ShopCartServiceImpl-calcShopFreight", e);
		}
		LOG.info("\n 方法[{}]，出参：[{}]", "ShopCartServiceImpl-calcShopFreight", JSON.toJSONString(result));
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:获取购物车所有店铺及商品</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<ShopOutPriceDTO> convertToShop(List<ProductInPriceDTO> products, Map<Long,Integer> deliveryTypeFreightDTO,String uid){
		Map<Long,ShopOutPriceDTO> shopMap = new HashMap<Long, ShopOutPriceDTO>();
		UserDTO user=null;
    	if(StringUtils.isNotEmpty(uid)){
    		user = this.userExportService.queryUserById(Long.parseLong(uid));
    	}
		// 是否存在普通商品（非集采商品）
		boolean isExistNormalProduct = false;
		for (ProductInPriceDTO p : products) {
			Long shopid = p.getShopId();
			ShopOutPriceDTO shop = shopMap.get(shopid);

			if (shop == null) {
				shop = new ShopOutPriceDTO();
				shop.setShopId(shopid);
				shop.setSellerId(p.getSellerId());

				// 查询店铺名称
				ExecuteResult<ShopDTO> er = this.shopService.findShopInfoById(shopid);
				ShopDTO sd = er.getResult();
				String name = sd != null ? sd.getShopName() : "";
				shop.setShopTitle(name);

				shop.setProducts(new LinkedList<ProductInPriceDTO>());
				
				// 店铺IM
				ShopCustomerServiceDTO shopCustomerServiceDTO = new ShopCustomerServiceDTO();
				shopCustomerServiceDTO.setShopId(shopid);
				List<ShopCustomerServiceDTO> shopCustomerServiceDTOList = this.shopCustomerServiceService.searchByCondition(shopCustomerServiceDTO);
				if(shopCustomerServiceDTOList!=null && shopCustomerServiceDTOList.size()>0)
					shop.setStationId(shopCustomerServiceDTOList.get(0).getStationId());
				
				shopMap.put(shopid,shop);
			}
			List<PromotionDTO> promotionList=new ArrayList<PromotionDTO>();
			if (p.getPromotions() != null && p.getPromotions().size() > 0) {
				for (PromotionDTO promotion : p.getPromotions()) {
					if (promotion.getType() == 1) {
						continue;
					}
					// promotionMap.get(shopid).add(promotion.getId());
					promotionList.add(promotion);
				}
			}
            p.setPromotions(promotionList);
			shop.getProducts().add(p);
			if (p.getChecked() && p.getActivitesDetailsId() == null) {
				isExistNormalProduct = true;
			}
		}
		
		List<ShopOutPriceDTO> shops = new LinkedList<ShopOutPriceDTO>();
		Iterator<Long> keys = shopMap.keySet().iterator();
		while( keys.hasNext() ){
			Long key = keys.next();
			ShopOutPriceDTO shop = shopMap.get(key);

			// 判断商品是否满足店铺起批、混批条件
			boolean canBuy = this.validShopRule(shop);
			if (!canBuy) {
				for (int i = 0; i < shop.getProducts().size(); i++){
					if (shop.getProducts().get(i).getChecked()) {
						// 非集采商品
						if (shop.getProducts().get(i).getActivitesDetailsId() == null) {
							// shop.getProducts().get(i).setChecked(false);

							shop.getProducts().get(i).setUnusualState(2);
							shop.getProducts().get(i).getUnusualMsg().add("不满足店铺起批、混批规则!");
						}
					}
				}
			}
			// 集采商品不能与普通商品一起结算
			for (int i = 0; i < shop.getProducts().size(); i++) {
				if (shop.getProducts().get(i).getChecked()) {
					if (isExistNormalProduct && shop.getProducts().get(i).getActivitesDetailsId() != null) {
						shop.getProducts().get(i).setUnusualState(5);
						shop.getProducts().get(i).getUnusualMsg().add("集采商品不能与普通商品一起结算!");
					}
					//买家不能购买自己店铺的商品
					if(user!=null && user.getShopId()!=null){
						if(user.getShopId().equals(shop.getShopId())){
							shop.getProducts().get(i).setUnusualState(10);
							shop.getProducts().get(i).getUnusualMsg().add("买家不允许购买自己店铺的商品!");
						}
					}
				}
			}
			// 店铺运费设置
			ExecuteResult<BigDecimal> freightResult = this.calcShopFreight(shop, deliveryTypeFreightDTO);
			shop.setFare(freightResult.getResult());
			shops.add(shop);
		}
		
		List<ShopOutPriceDTO> sps = new LinkedList<ShopOutPriceDTO>();
		Iterator<Entry<Long, ShopOutPriceDTO>> iterator = shopMap.entrySet().iterator();
		while( iterator.hasNext() ){
			Entry<Long, ShopOutPriceDTO> entry = iterator.next();
			ShopOutPriceDTO shop = entry.getValue();
			sps.add(shop);
		}
		
		return sps;
	}
	
	/**
	 * @desc 判断店铺商品是否购买
	 * 逻辑：当店铺商品只有一种是走起批逻辑判断；当店铺商品多于一种时，店铺商品若满足起批或混批限定都可以购买
	 * 起批：分数量、总额，根据设定或与且做逻辑判断
	 * 混批：分数量、总额，根据设定或与且做逻辑判断
	 * @param id
	 * @param typeCount
	 * @param quantity
	 * @param total
	 * @return
	 */
	private boolean validShopRule(ShopOutPriceDTO shop) {
		Long shopId = shop.getShopId();
		Integer typeCount = shop.getTypeCount();
		Integer quantity = shop.getQuantity();
		BigDecimal total = shop.getTotal();
		ExecuteResult<ShopDTO> er = this.shopService.findShopInfoById(shopId);
		boolean canBuy = true;
		if (er.isSuccess() && er.getResult() != null) {
			ShopDTO sd = er.getResult();

			if (sd.getInitialCondition() != null) {
				Integer initQuantity = sd.getInitialMount().intValue();
				BigDecimal initTotal = sd.getInitialPrice();
				canBuy = sd.getInitialCondition() == 1 ? (quantity
						.compareTo(initQuantity) >= 0 || total
						.compareTo(initTotal) >= 0) : (quantity
						.compareTo(initQuantity) >= 0 && total
						.compareTo(initTotal) >= 0);
			}

			if ((typeCount > 1 && sd.getMutilCondition() != null)
					&& (sd.getInitialCondition() == null || !canBuy)) {
				Integer mountMin = sd.getMountMin().intValue();
				BigDecimal priceMin = sd.getPriceMin();

				canBuy = sd.getMutilCondition() == 1 ? (quantity
						.compareTo(mountMin) >= 0 || total.compareTo(priceMin) >= 0)
						: (quantity.compareTo(mountMin) >= 0 && total
								.compareTo(priceMin) >= 0);
			}
			
//			if(!canBuy){
				StringBuffer shopRule = new StringBuffer();
				if( sd.getInitialMount() != null ){
					shopRule.append("起批规则：购买数量≥").append(sd.getInitialMount()).append("件 ");
				}
				if( sd.getInitialMount() != null && sd.getInitialPrice() != null ){
					if(sd.getInitialCondition()==null){
						shopRule.append("且");
					}else{
						shopRule.append(sd.getInitialCondition() == 1 ? "或" : "且");
					}
				}
				if( sd.getInitialPrice() != null ){
					shopRule.append("购买金额≥").append(sd.getInitialPrice()).append("元 ");
				}

				if( sd.getMountMin() != null ){
					shopRule.append("混批规则：购买数量≥").append(sd.getMountMin()).append("件 ");
				}
				if( sd.getMountMin() != null && sd.getPriceMin() != null ){
					shopRule.append(sd.getInitialCondition() == 1 ? "或" : "且");
				}
				if( sd.getPriceMin() != null ){
					shopRule.append("购买金额≥").append(sd.getPriceMin()).append("元  ");
				}
				
				shop.setShopRule(shopRule.toString());
//			}
		}
		return canBuy;
	}
	
	@Override
	public ShopCartDTO getMyCart(String ctoken,String uid, Boolean carding, Map<Long,Integer> freightDeliveryType,String areaCode){
		
		if(carding)
			this.cartCarding(ctoken, uid);
		
		List<ProductInPriceDTO> products = this.getProducts(ctoken, uid, areaCode, null);
		List<ShopOutPriceDTO> shops = this.convertToShop(products, freightDeliveryType, uid);
		//加入优惠券
		if(null == uid || "".equals(uid)){
			shops = this.getCouponsByShopId(shops,null);
		}else{
			shops = this.getCouponsByShopId(shops,Long.parseLong(uid));
		}
		ShopCartDTO cart = new ShopCartDTO();
		cart.setShops(shops);
		return cart;
	}
	

	@Override
	public ExecuteResult<String> checkShop(String ctoken,String uid,Long shopid,Boolean checked){
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken,uid);
		
		for(ProductInPriceDTO p: products){
			if( shopid.compareTo(p.getShopId()) == 0 )
				p.setChecked( checked );
		}
		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set(uid, JSON.toJSONString(products));
		} else {
			this.redisDB.setAndExpire(ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE);
		}
		return new ExecuteResult<String>();
	}
	

	@Override
	public ExecuteResult<String> checkAll(String ctoken,String uid,Boolean checked){
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken,uid);
		
		for(ProductInPriceDTO p: products){
			p.setChecked( checked );
		}

		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set(uid, JSON.toJSONString(products));
		} else {
			this.redisDB.setAndExpire(ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE);
		}
		return new ExecuteResult<String>();
	}

	@Override
	public ExecuteResult<String> changeRegion(String ctoken, String uid, String regionId, Integer orderType,
			String buyType) {
		if (StringUtils.isNotBlank(buyType) && "NOW".equals(buyType)) {
			String buyNowProducts = redisDB.get("buyNowProducts");
			if(StringUtils.isNotBlank(buyNowProducts)){
				List<ProductInPriceDTO> pros = JSON.parseArray(buyNowProducts, ProductInPriceDTO.class);
				if (pros != null && pros.size() > 0) {
					for(ProductInPriceDTO product : pros){
						product.setRegionId(regionId);
					}
					redisDB.addObject("buyNowProducts", JSON.toJSONString(pros), 60 * 60 * 24);
				}
			}
		}
		if (OrderTypeEnum.NORMAL.getCode() == orderType) {
			List<ProductInPriceDTO> products = this.getAllProducts(ctoken, uid);

			for (ProductInPriceDTO product : products) {
				product.setRegionId(regionId);
			}

			// 判断用户是否登录进行区别保存购物车数据
			if (uid != null && !"".equals(uid)) {
				this.redisDB.set(uid, JSON.toJSONString(products));
			} else {
				this.redisDB.setAndExpire(ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE);
			}
		} else if (OrderTypeEnum.INQUIRY.getCode() == orderType || OrderTypeEnum.AGREEMENT.getCode() == orderType) {
			List<ProductInPriceDTO> products = this.getRedisProducts(uid);
			for (ProductInPriceDTO product : products) {
				product.setRegionId(regionId);
			}
			String jsonProduct = JSONArray.toJSONString(products);
			this.redisDB.set(uid + "Redis", jsonProduct);
		}
		return new ExecuteResult<String>();
	}

	@Override
	public ExecuteResult<String> subimtOrder(OrderParameter parameter) {
		LOG.debug(JSON.toJSONString(parameter.getDto()));
		ExecuteResult<String> er = new ExecuteResult<String>();
		ShopCartDTO sc = null;
		// 获取当前用户购物车商品
		List<ProductInPriceDTO> allProducts = getAllProducts(parameter.getCtoken(),parameter.getUid());
		// 购物车中是否存在集采的商品
		boolean isExistCentralPurchasing = false;
		if(allProducts != null && allProducts.size() > 0){
			for(ProductInPriceDTO p : allProducts){
				if(p.getChecked() && p.getActivitesDetailsId() != null){
					isExistCentralPurchasing = true;
					// 设置父订单为集采订单
					parameter.getDto().setCentralPurchasing(1);
					break;
				}
			}
			if(parameter.getDto().getCentralPurchasing() == null){
				// 设置父订单为非集采订单
				parameter.getDto().setCentralPurchasing(2);
			}
		}
		if (parameter.getDto().getOrderType() == OrderTypeEnum.NORMAL.getCode()) {
			if(isExistCentralPurchasing){
				sc = this.getMyCart(parameter.getCtoken(),parameter.getUid(), false ,parameter.getFreightDeliveryType(),null);
			} else{
				sc = this.getMyCartToOrderView(parameter.getProducts(), parameter.getCtoken(),parameter.getUid(), false ,parameter.getPromptly(),parameter.getCouponId(), parameter.getDto().getPaymentMethod(), parameter.getIntegral(), parameter.getFreightDeliveryType(), parameter.getPlatformId(), parameter.getPromotion(), parameter.getMarkdown());
			}
		} else {
			sc = this.getMyOrder(parameter.getUid(), parameter.getFreightDeliveryType());
		}
		if( sc.getUnusualCount() > 0 ){
			er.addErrorMessage("商品信息有变动，请返回购物车查看！");
		} else if(sc.getPayTotal().add(sc.getTotalFare()).compareTo(BigDecimal.ZERO) <= 0){
			er.addErrorMessage("支付金额为0！");
		} else{
			// 首先，创建平台订单
			parameter.getDto().setPaid(1);
			ExecuteResult<TradeOrderCreateDTO> erCreate = this.createOrder(parameter.getShopPromoId(), sc, parameter.getDto());
			if( erCreate.isSuccess() && erCreate.getResult() != null ){
				TradeOrderCreateDTO result = erCreate.getResult();
				String orderId = result.getParentOrder().getOrderId();
				er.setResult(orderId);
				// 其次，创建支付订单
				ExecuteResult<Integer> erPay = this.createPayOrder(result);
				LOG.debug(JSON.toJSONString(erPay));
				if( erPay.isSuccess() ){
					// 最后，更新购物车
					this.submitCallBack(parameter.getCtoken(),parameter.getUid());
				}else{
					LOG.error("支付订单创建失败");
					er.setErrorMessages(erPay.getErrorMessages());
					this.tradeOrderExportService.deleteTradeOrders(orderId);
				 }
			}else{
				er.setErrorMessages(erCreate.getErrorMessages());
			}
		}
		return er;
	}
	
	
	/**
	 * 
	 * <p>Discription: 创建平台订单</p>
	 * Created on 2015年3月17日
     * Update on 2015年11月10日 author 郭建宁
	 * @param shopPromoId
	 * @param couponId 优惠券编号
	 * @param cart
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private ExecuteResult<TradeOrderCreateDTO> createOrder(String shopPromoId, ShopCartDTO cart, TradeOrdersDTO dto) {
		LOG.debug(JSON.toJSONString(dto));
		TradeOrderCreateDTO createDTO = new TradeOrderCreateDTO();
		dto.setTotalPrice(cart.getTotal());
		dto.setFreight(cart.getTotalFare());
		dto.setCouponId(cart.getCouponId());
		dto.setExchangeRate(cart.getExchangeRate());
		dto.setIntegralDiscount(cart.getIntegralDiscount());
		for (ShopOutPriceDTO shop : cart.getShops()) {
			if (shop.getQuantity() > 0) {
				TradeOrdersDTO tod = new TradeOrdersDTO();
				BeanUtils.copyProperties(dto, tod);
				tod.setShopId(shop.getShopId());
				tod.setSellerId(shop.getSellerId());
				tod.setFreight(shop.getFare());
				tod.setTotalPrice(shop.getTotal());

				tod.setPromoCode(dto.getPromoCode());
				tod.setMemo(dto.getMemo());
				tod.setItems(new LinkedList<TradeOrderItemsDTO>());
				tod.setCentralPurchasing(dto.getCentralPurchasing());
				for (ProductInPriceDTO pro : shop.getProducts()) {
					if (pro.getChecked()) {
						TradeOrderItemsDTO itemDto = new TradeOrderItemsDTO();

						itemDto.setItemId(pro.getItemId());// SKU所属品类ID
						itemDto.setSkuId(pro.getSkuId());// SKU

						itemDto.setPrimitivePrice(pro.getSkuPrice().multiply(new BigDecimal(pro.getQuantity())));// SKU价格
						itemDto.setPayPrice(pro.getPayPrice());// 支付价格（优惠后的价格）

						itemDto.setAreaId(Long.valueOf(pro.getRegionId()));
						itemDto.setNum(pro.getQuantity());// 数量

						itemDto.setPromotionType(pro.getPromType());
						itemDto.setPromotionId(pro.getPromId());// 促销ID

						itemDto.setCid(pro.getCid());
						itemDto.setShopFreightTemplateId(pro.getShopFreightTemplateId()); // 运费模版ID
						itemDto.setDeliveryType(pro.getDeliveryType()); // 运送方式
						itemDto.setPayPriceTotal(pro.getPayTotal());
						itemDto.setPromotionDiscount(pro.getDiscountAmount());
						itemDto.setCouponDiscount(pro.getCouponDiscount()); // 优惠券优惠金额
						itemDto.setIntegralDiscount(pro.getIntegralDiscount()); // 积分使用金额
						itemDto.setContractNo(dto.getContractNo());// 增加协议编号
						itemDto.setActivitesDetailsId(pro.getActivitesDetailsId()); // 设置集采活动详情ID
						// 设置套装商品订单记录
						List<TradeOrderItemsPackageDTO> getTradeOrderItemsPackages = new ArrayList<TradeOrderItemsPackageDTO>();
						// 套装单品记录
						if(pro.getSubProducts() != null && pro.getSubProducts().size() > 0){
							for(ProductInPriceDTO subProduct : pro.getSubProducts()){
								TradeOrderItemsPackageDTO itemsPackageDTO = new TradeOrderItemsPackageDTO();
								itemsPackageDTO.setItemId(pro.getItemId());
								itemsPackageDTO.setSkuId(pro.getSkuId());
								itemsPackageDTO.setSubItemId(subProduct.getItemId());
								itemsPackageDTO.setSubSkuId(subProduct.getSkuId());
								itemsPackageDTO.setSubSkuPrice(subProduct.getSkuPrice());
								itemsPackageDTO.setSubNum(subProduct.getQuantity());
								itemsPackageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
								getTradeOrderItemsPackages.add(itemsPackageDTO);
							}
						}
						// 套装增值服务
						if(pro.getValueAddedProducts() != null && pro.getValueAddedProducts().size() > 0){
							for(ProductInPriceDTO valueAddedProduct : pro.getValueAddedProducts()){
								TradeOrderItemsPackageDTO itemsPackageDTO = new TradeOrderItemsPackageDTO();
								itemsPackageDTO.setItemId(pro.getItemId());
								itemsPackageDTO.setSkuId(pro.getSkuId());
								itemsPackageDTO.setSubItemId(valueAddedProduct.getItemId());
								itemsPackageDTO.setSubSkuId(valueAddedProduct.getSkuId());
								itemsPackageDTO.setAddSource(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
								itemsPackageDTO.setSubSkuPrice(valueAddedProduct.getSkuPrice());
								itemsPackageDTO.setSubNum(valueAddedProduct.getQuantity());
								getTradeOrderItemsPackages.add(itemsPackageDTO);
							}
						}
						itemDto.setTradeOrderItemsPackages(getTradeOrderItemsPackages);
						tod.getItems().add(itemDto);
						if(pro.getTradeOrderItemsDiscountDTO()!=null){
							pro.getTradeOrderItemsDiscountDTO().setSkuId(pro.getSkuId());
							tod.getTradeOrderItemsDiscountDTO().add(pro.getTradeOrderItemsDiscountDTO());
						}
					}
				}
				tod.setIntegral(shop.getIntegral()); // 每个订单使用的积分（注：积分分摊到订单；积分抵扣的金额分摊到商品）
				tod.setExchangeRate(shop.getExchangeRate());
				tod.setIntegralDiscount(shop.getIntegralDiscount());
				tod.setPaymentPrice(shop.getPayTotal().add(shop.getFare()));// 实际支付金额=付款金额+运费金额-折扣金额
				tod.setTotalDiscount(shop.getTotal().subtract(shop.getPayTotal()).subtract(shop.getIntegralDiscount()));// 积分不算优惠金额
				tod.setActivityRecordDTOs(shop.getActivityRecordDTOs());// 活动记录信息
				if (new BigDecimal(0).compareTo(shop.getDiscountTotal()) != 0) {
					tod.setDiscountAmount(shop.getDiscountTotal());// 订单的折扣金额
				}
				createDTO.getSubOrders().add(tod);
			}
		}
		dto.setPaymentPrice(cart.getPayTotal().add(cart.getTotalFare()));
		if (new BigDecimal(0).compareTo(cart.getDiscountTotal()) != 0) {
			dto.setDiscountAmount(cart.getDiscountTotal());// 父单的折扣总金额
		}
		if (StringUtils.isNotBlank(dto.getPromoCode())) {
			VipCardDTO vipCardDTO = new VipCardDTO();
			vipCardDTO.setVip_id(Integer.parseInt(dto.getPromoCode()));
			// 根据vip卡号查询vip卡信息
			ExecuteResult<VipCardDTO> res = vipCardService.queryVipCard(vipCardDTO);
			if (res != null) {
				res.getResult().setResidual_amount(
						res.getResult().getResidual_amount().subtract(cart.getDiscountTotal()));
				vipCardService.updateVipCard(res.getResult());
			}
		}
		createDTO.setParentOrder(dto);
		LOG.debug("CREATE ORDER PARAMS:" + JSON.toJSONString(createDTO));
		ExecuteResult<TradeOrderCreateDTO> result = this.tradeOrderExportService.createOrder(createDTO);
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:创建支付订单</p>
	 * Created on 2015年3月17日
	 * @author:[Goma 郭茂茂]
	 */
	private ExecuteResult<Integer> createPayOrder(TradeOrderCreateDTO dto){
		LOG.debug("CREATE ORDER RETURN: "+JSON.toJSONString(dto));
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		PayReqParam param = new PayReqParam();
		
		// 必填信息设置
		TradeOrdersDTO pOrder = dto.getParentOrder();
		param.setOrderNo(pOrder.getOrderId());
		param.setTotalFee(pOrder.getPaymentPrice());
		param.setPayOrderType(PayOrderTypeEnum.Parent);
		param.setSystem(SysProperties.getProperty("transfer.system"));
		String orderIds = "";
		List<OrderItemPay> items = new LinkedList<OrderItemPay>();
		for( TradeOrdersDTO td: dto.getSubOrders() ){
			OrderItemPay item = new OrderItemPay();
			item.setSubOrderId(td.getOrderId());
			item.setShopId(td.getShopId().toString());
			item.setSellerId(td.getSellerId());
			item.setSubOrderPrice(td.getPaymentPrice());
			item.setSubOrderSubject("舒适100订单："+td.getOrderId());
			items.add(item);
			orderIds = orderIds+td.getOrderId()+"|";
		}
		orderIds = orderIds.substring(0, orderIds.length()-1);
		param.setSubject("舒适100订单："+orderIds);
		param.setOrderItemPays(items);
		
		Map<String, String> params =new HashMap<String, String>();
		params.put("system", param.getSystem());
		params.put("orderNo", param.getOrderNo());
		params.put("totalFee", param.getTotalFee().toString());
		
		param.setSign(Signature.createSign(params, SysProperties.getProperty("transfer.prikey")));
		
		try {
			LOG.debug("CREATE PAY ORDER PARAMS:" + JSON.toJSONString(param));
			er = this.paymentService.payIndex(param);
		} catch (Exception e) {
			e.printStackTrace();
			er.addErrorMessage(e.getMessage());
		}
		
		return er;
	}
	
	/**
	 * 
	 * <p>Discription: 订单提交成功后的回调方法，主要用于清空购物车已提交订单商品</p>
	 * Created on 2015年3月17日
	 * @param ctoken
	 * @param uid
	 * @author:[Goma 郭茂茂]
	 */
	private void submitCallBack(String ctoken,String uid){
		List<ProductInPriceDTO> nProducts = new LinkedList<ProductInPriceDTO>();
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken, uid);
		
		for (ProductInPriceDTO product : products) {
			if (!product.getChecked()) {
				nProducts.add(product);
			} else if (product.getActivitesDetailsId() != null) {
				// 更新参加集采活动的商品的已下单人数
				centralPurchasingExportService.plusPlaceOrderNum(product.getActivitesDetailsId(), 1);
			}
		}

		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set(uid, JSON.toJSONString(nProducts));
		} else {
			this.redisDB.setAndExpire(ctoken, JSON.toJSONString(nProducts), COOKIE_MAX_AGE);
		}
	}

	@Override
	public ExecuteResult<Integer> payOrder(String orderNo,String type,String paymentMethod){
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		PayReqParam param = new PayReqParam();
		param.setOrderNo(orderNo);
		
		if( "1".equals(type) ){
			param.setPayOrderType(PayOrderTypeEnum.child);
		}else{
			param.setPayOrderType(PayOrderTypeEnum.Parent);
		}
		param.setPayBank(PayBankEnum.getEnumByName(paymentMethod));
		param.setQrPayMode(PayBankEnum.getEnumByName(paymentMethod).getQrCode()+"");
		
		try {
			er = this.paymentService.pay(param);
		} catch (Exception e) {
			LOG.error("支付订单失败！",e);
			er.addErrorMessage(e.getMessage());
		}
		return er;
	}

	@Override
	public ShopCartDTO getMyOrder(String uid, Map<Long, Integer> freightDeliveryType) {
		List<ProductInPriceDTO> products = this.getRedisProducts(uid);
		Map<Long,ShopOutPriceDTO> shopMap = new HashMap<Long, ShopOutPriceDTO>();
		for (ProductInPriceDTO p : products) {
			Long shopid = p.getShopId();
			ShopOutPriceDTO shop = shopMap.get(shopid);

			if( shop == null ){
				shop = new ShopOutPriceDTO();
				shop.setShopId(shopid);
				shop.setSellerId(p.getSellerId());

				// 查询店铺名称
				ExecuteResult<ShopDTO> er = this.shopService.findShopInfoById(shopid);
				ShopDTO sd = er.getResult();
				String name = sd != null ? sd.getShopName() : "";
				shop.setShopTitle(name);

				shop.setProducts(new LinkedList<ProductInPriceDTO>());

				// 店铺IM
				ShopCustomerServiceDTO shopCustomerServiceDTO = new ShopCustomerServiceDTO();
				shopCustomerServiceDTO.setShopId(shopid);
				List<ShopCustomerServiceDTO> shopCustomerServiceDTOList = this.shopCustomerServiceService.searchByCondition(shopCustomerServiceDTO);
				if(shopCustomerServiceDTOList!=null && shopCustomerServiceDTOList.size()>0)
					shop.setStationId(shopCustomerServiceDTOList.get(0).getStationId());

				shopMap.put(shopid,shop);
			}
			shop.getProducts().add(p);
		}

		List<ShopOutPriceDTO> shops = new LinkedList<ShopOutPriceDTO>();
		Iterator<Long> keys = shopMap.keySet().iterator();
		while (keys.hasNext()) {
			Long key = keys.next();
			ShopOutPriceDTO shop = shopMap.get(key);
			// 店铺运费设置
			ExecuteResult<BigDecimal> freightResult = this.calcShopFreight(shop, freightDeliveryType);
			shop.setFare(freightResult.getResult());
			shops.add(shop);
		}
		ShopCartDTO cart = new ShopCartDTO();
		cart.setShops(shops);
		return cart;
	}

	/**
	 * 
	 * <p>Discription: 购物车梳理，将下架商品清除选中</p>
	 * Created on 2015年3月17日
	 * @param ctoken
	 * @param uid
	 * @author:[Goma 郭茂茂]
	 */
	private void cartCarding(String ctoken,String uid){
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken, uid);
		for( ProductInPriceDTO product:products ){
			ItemShopCartDTO dto = new ItemShopCartDTO();
			// 查询商品SKU信息
			dto.setAreaCode(product.getRegionId());
			dto.setSkuId(product.getSkuId());
			dto.setQty(product.getQuantity());
			dto.setShopId(product.getShopId());
			dto.setItemId(product.getItemId());
			if (product.getSellerId() != null && !"".equals(product.getSellerId()))
				dto.setSellerId(Long.valueOf(product.getSellerId()));
			if (product.getUid() != null && !"".equals(product.getUid()))
				dto.setBuyerId(Long.valueOf(product.getUid()));
			LOG.debug("获取SKU参数：" + JSON.toJSONString(dto));
			ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto);
			
			if( er.isSuccess() && er.getResult() != null && er.getResult().getItemStatus() != 4 ){
				product.setChecked(false);
			}
		}
		
		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid)) {
			this.redisDB.set(uid, JSON.toJSONString(products));
		} else {
			this.redisDB.setAndExpire(ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE);
		}
		
	}
	
	/**
	 * 
	 * <p>Description: [获取协议订单和询价订单商品]</p>
	 * Created on 2015年11月12日
	 * @param uid
	 * @return
	 * @author:[宋文斌]
	 */
	private List<ProductInPriceDTO> getRedisProducts(String uid) {
		String productsJsonStr = this.redisDB.get(uid + "Redis");
		LOG.debug("=====>" + productsJsonStr);
		List<ProductInPriceDTO> productList = JSON.parseArray(productsJsonStr, ProductInPriceDTO.class);
		List<ProductInPriceDTO> products = new LinkedList<ProductInPriceDTO>();
		if (productList != null && productList.size() > 0) {
			for (int i = 0; i < productList.size(); i++) {
				ProductInPriceDTO product = productList.get(i);
				ExecuteResult<ItemDTO> itemDTO = this.itemService.getItemById(product.getItemId());
				if (itemDTO != null) {
					product.setTitle(itemDTO.getResult().getItemName());
					product.setSrc(itemDTO.getResult().getPicUrls()[0]);
					BigDecimal quantity = BigDecimal.valueOf(product.getQuantity());
					BigDecimal skuPrice = product.getSkuPrice();
					product.setTotal(quantity.multiply(skuPrice)); // 商品总价
					product.setPayPrice(product.getSkuPrice()); // 支付价格
					product.setPayTotal(quantity.multiply(skuPrice)); // 支付总价格
				}
				products.add(product);
			}
		}
		return products;
	}
	
	@Override
	public ShopCartDTO getPromptly(List<ProductInPriceDTO> pros,String uid, Boolean carding) {
		List<ProductInPriceDTO> products = pros;


        List<ProductInPriceDTO> list=new ArrayList<ProductInPriceDTO>();
        for (ProductInPriceDTO product : products) {
            ProductInPriceDTO p=new ProductInPriceDTO();
            BeanUtils.copyProperties(product, p);
            list.add(p);
        }
        UserDTO user=null;
        if(StringUtils.isNotBlank(uid)){
        	user = this.userExportService.queryUserById(Long.parseLong(uid));
        }
        ExecuteResult<Map<Long, ShopOutPriceDTO>> result = shopCartPromotionService.getShopByProducts(list,user);
        ShopCartDTO cart = new ShopCartDTO();
        if(result.isSuccess()){
            Iterator<Entry<Long, ShopOutPriceDTO>> iterator = result.getResult().entrySet().iterator();
            List<ShopOutPriceDTO> shops=new ArrayList<ShopOutPriceDTO>();
            while (iterator.hasNext()) {
                Entry<Long, ShopOutPriceDTO> entry = iterator.next();
                ShopOutPriceDTO shopOutPriceDTO = entry.getValue();
                ShopOutPriceDTO shop=new ShopOutPriceDTO();

                List<ProductInPriceDTO> proList=new ArrayList<ProductInPriceDTO>();
                for (ProductInPriceDTO product : shopOutPriceDTO.getProducts()) {
                	ProductInPriceDTO p=new ProductInPriceDTO();
                    BeanUtils.copyProperties(product, p);
                    proList.add(p);
                }
                List<PromotionDTO> promotionList=new ArrayList<PromotionDTO>();
                for (PromotionDTO promotion : shopOutPriceDTO.getPromotionList()) {
                    promotionList.add(promotion);
                }
                BeanUtils.copyProperties(shopOutPriceDTO, shop);
                shop.setProducts(proList);
                shop.setPromotionList(promotionList);
                shops.add(shop);
            }
            cart.setShops(shops);
        }
//		List<Shop> shops = this.convertToShop(products);
//		ShopCart cart = new ShopCart();
//		cart.setShops(shops);
		return cart;
	}
	
	@Override
	public ShopCartDTO getMyCartToOrderView(List<ProductInPriceDTO> pros, String ctoken, String uid, Boolean carding,
			String promptly, String couponId, Integer paymentMethod, Integer integral,
			Map<Long, Integer> freightDeliveryType, Integer platformId, String promotion, String markdown) {
		List<ProductInPriceDTO> products = new ArrayList<ProductInPriceDTO>();
		// 区别是否是加入购物车还是立即购买
		if (StringUtils.isNotBlank(promptly)) {
			products = this.getProducts(ctoken, uid, null, pros);
		} else {
			if (carding) {
				this.cartCarding(ctoken, uid);
			}
			products = this.getProducts(ctoken, uid, null, null);
		}
		List<ProductInPriceDTO> list = new ArrayList<ProductInPriceDTO>();
		for (ProductInPriceDTO product : products) {
			list.add(product);
		}
		UserDTO user = null;
		if (StringUtils.isNotBlank(uid)) {
			user = this.userExportService.queryUserById(Long.parseLong(uid));
		}
		ExecuteResult<Map<Long, ShopOutPriceDTO>> result = shopCartPromotionService.getShopByProducts(list, user);
		ShopCartDTO cart = new ShopCartDTO();
		if (result.isSuccess()) {
			Iterator<Entry<Long, ShopOutPriceDTO>> iterator = result.getResult().entrySet().iterator();
			List<ShopOutPriceDTO> shops = new ArrayList<ShopOutPriceDTO>();
			while (iterator.hasNext()) {
				Entry<Long, ShopOutPriceDTO> entry = iterator.next();
				ShopOutPriceDTO shopOutPriceDTO = entry.getValue();
				shops.add(shopOutPriceDTO);
			}
			cart.setShops(shops);
			//计算直降记录信息
			if(StringUtils.isNotBlank(markdown)){
				cart=this.getMarkDownActivityRecord(cart);
			}
			//获取最优满减活动id
			String shopPromoId="";
			Map<String,ShopCartDTO> map = shopCartFullReductionService.findShopPromotionId(cart);
			   Iterator it = map.entrySet().iterator();
			   while (it.hasNext()) {
				    Map.Entry entry = (Map.Entry)it.next();
				    shopPromoId=(String) entry.getKey();
				    cart=(ShopCartDTO) entry.getValue();
			    }
			// 计算满减活动
			if (StringUtils.isBlank(promotion)) {
				promotion = shopPromoId;
			}
			if(StringUtils.isNotBlank(promotion)){
				ExecuteResult<ShopCartDTO> newCart = shopCartFullReductionService.calcFullReduction(cart, promotion, user);
				cart = newCart.getResult();
			}
			// 校验直降
			if(StringUtils.isNotBlank(markdown)){
				this.checkMarkdown(cart, user, markdown);
			}
			// 优惠券计算
			if (StringUtils.isNotBlank(couponId)) {
				ExecuteResult<ShopCartDTO> newCart = shopCartCouponService.calcCoupon(cart, Long.parseLong(uid), couponId, paymentMethod, platformId);
				if(newCart.isSuccess() && newCart.getResult() != null){
					cart = newCart.getResult();
				}
			}
			// 计算店铺运费(这段逻辑必须在优惠券计算完成之后，积分计算之前)
			for(ShopOutPriceDTO shopOutPriceDTO : cart.getShops()){
				if(shopOutPriceDTO.getShopId() != null && shopOutPriceDTO.getShopId().longValue() != 0){
					ExecuteResult<BigDecimal> freightResult = this.calcShopFreight(shopOutPriceDTO, freightDeliveryType);
					shopOutPriceDTO.setFare(freightResult.getResult());
				}
			}
			// 积分优惠计算
//			if (integral != null) {
//				cart.setIntegral(integral);
//				ExecuteResult<ShopCartDTO> newCart = shopCartIntegralService.calcIntegral(cart, Long.parseLong(uid), platformId);
//				if(newCart.isSuccess() && newCart.getResult() != null){
//					cart = newCart.getResult();
//				}
//			}
		} else {
			cart.setShops(new ArrayList<ShopOutPriceDTO>());
			cart.getUnusualMsg().addAll(result.getErrorMessages());
		}
		return cart;
	}
	//计算直降记录信息
	private ShopCartDTO getMarkDownActivityRecord(ShopCartDTO cart){
		for(ShopOutPriceDTO myCart:cart.getShops()){
			List<ActivityRecordDTO> lists=new ArrayList<ActivityRecordDTO>();
			if(myCart.getShopId()!=0){
			  for(ProductInPriceDTO pro:myCart.getProducts()){
				  if(pro.getChecked()){
					  Iterator iter = pro.getMap().entrySet().iterator(); 
		                while (iter.hasNext()) { 
			            	 Map.Entry<String,Long> entry = (Map.Entry) iter.next(); 
			                 String key = entry.getKey(); 
			                 Long val = entry.getValue(); 
			                 //if(markDownId.indexOf(val.toString())==-1){//直降活动集合中不包含活动id
			                 //	 markDownId.append(pro.getPromId()+",");
			                	//活动记录信息
			                	 ActivityRecordDTO dto=new ActivityRecordDTO();
			     	             dto.setShopId(myCart.getShopId());
			                     if("platformActivity".equals(key)){
			                     	dto.setType(ActivityTypeEnum.PLATFORMMARKDOWN.getStatus());//平台直降
			                     }else if("shopActivity".equals(key)){
			                     	dto.setType(ActivityTypeEnum.SHOPMARKDOWN.getStatus());//店铺直降
			                     }
			                     dto.setPromotionId(val.toString());
			                     dto.setDiscountAmount(pro.getTotal().subtract(pro.getPayTotal()));//直降优惠金额
			                     pro.setDiscountAmount(pro.getTotal().subtract(pro.getPayTotal()));//每个商品的直降优惠金额
			                     lists.add(dto);
			                     TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO=new TradeOrderItemsDiscountDTO();
			                     tradeOrderItemsDiscountDTO.setMarkdownDiscount(pro.getTotal().subtract(pro.getPayTotal()));
			                     if(val!=null){
			                    	 tradeOrderItemsDiscountDTO.setMarkdownId(val);
			                     }
			                     if("platformActivity".equals(key)){
			                    	 tradeOrderItemsDiscountDTO.setMarkdownType(ActivityTypeEnum.PLATFORMMARKDOWN.getStatus());
			                     }else if("shopActivity".equals(key)){
			                    	 tradeOrderItemsDiscountDTO.setMarkdownType(ActivityTypeEnum.SHOPMARKDOWN.getStatus());//店铺直降
				                 }
			                     pro.setTradeOrderItemsDiscountDTO(tradeOrderItemsDiscountDTO);
		                }
				  }
			  }
			  myCart.setActivityRecordDTOs(lists);
			}
		}
		 return cart;
	}
	
	/**
	 * <p>Discripption:[校验直降活动是否已结束]</p>
	 * Created on 2015-12-28
	 * @param markdown 待校验直降活动，shopId&itemId&markdownId，多个以逗号分隔
	 * @return 
	 * @author 周志军
	 * 
	 */
	private void checkMarkdown(ShopCartDTO shopCartDTO, UserDTO user, String markdown){
		LOG.info("\n 方法[{}]，入参：[{},\n{},\n{}]", "ShopCartServiceImpl-checkMarkdown", JSON.toJSONString(shopCartDTO),JSON.toJSONString(user),markdown);
		String[] markdowns = markdown.split(",");
		String[] shopIds = new String[markdowns.length];
		String[] itemIds = new String[markdowns.length];
		String[] promoIds = new String[markdowns.length];
		ProductInPriceDTO productInPriceDTO = new ProductInPriceDTO();
		for(int i = 0 ; i < markdowns.length ; i++ ){
			shopIds[i] = markdowns[i].split("&")[0];
			itemIds[i] = markdowns[i].split("&")[1];
   		 	promoIds[i] = markdowns[i].split("&")[2];
			productInPriceDTO.setShopId(Long.parseLong(shopIds[i]));
			productInPriceDTO.setItemId(Long.parseLong(itemIds[i]));
			ExecuteResult<ProductOutPriceDTO> proEr = shopCartPromotionService.calcPromotion(productInPriceDTO, user);
			if(proEr.isSuccess() && null != proEr.getResult()){
				ProductOutPriceDTO outPriceDTO = proEr.getResult();
				if(outPriceDTO.getPromId() != Long.parseLong(promoIds[i])){
					shopCartDTO.getUnusualMsg().add("直降活动不存在或已过期");
					return;
				}
			}
		}
	}
	
	/**
	 * 
	 * <p>Discription:[获取可以领取的优惠券]</p>
	 * Created on 2016年2月27日
	 * @param shopList
	 * @return
	 * @author:[李伟龙]
	 */
	public List<ShopOutPriceDTO> getCouponsByShopId(List<ShopOutPriceDTO> shopList ,Long userId) {
		for(int i=0;i<shopList.size();i++){
			List<ProductInPriceDTO> products = shopList.get(i).getProducts();
			//取到skuId  类目Id
			List<Long> skuIdList = new ArrayList<Long>();
			List<Long> cIdList = new ArrayList<Long>();
			if(null != products && !products.isEmpty() && products.size() > 0){
				for(int p = 0 ;p< products.size() ; p++){
					skuIdList.add(products.get(p).getSkuId());
					cIdList.add(products.get(p).getCid());
				}
			}
			shopList.get(i).setCouponsList(couponsExportService.getCouponsByShopId(shopList.get(i).getShopId(), userId ,skuIdList,cIdList));
		}
		return shopList;
	}
	
}
