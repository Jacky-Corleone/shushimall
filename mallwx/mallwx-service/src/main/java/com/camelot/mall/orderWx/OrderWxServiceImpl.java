package com.camelot.mall.orderWx;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.util.Signature;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.mall.shopcart.Product;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.ProductAttrDTO;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartCouponService;
import com.camelot.pricecenter.service.ShopCartFreightService;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFareExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

@Service(value="orderWxService")
public class OrderWxServiceImpl implements OrderWxService {
	private static final Logger LOG = LoggerFactory.getLogger(OrderWxServiceImpl.class);
	private static Integer COOKIE_MAX_AGE = 1000 * 60 * 60 * 24 * 7;
	@Resource
	private RedisDB redisDB;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopService;
	@Resource
	private ShopFareExportService shopFareService;
	@Resource
	private ShopCustomerServiceService shopCustomerServiceService;
	@Resource
	private PromotionService promotionService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private PaymentExportService paymentService;
	@Resource
	private ProtocolExportService protocolExportService;
	@Resource
	private InquiryExportService inquiryExportService;
	@Resource
	private ShopCartFreightService shopCartFreightService;
	
	@Resource
	private ShopCartCouponService shopCartCouponService;

	@Override
	public void changeRegion(String ctoken, String uid, String regionId) {
		List<ProductInPriceDTO> products = this.getAllProducts(ctoken,uid);
		
		for(ProductInPriceDTO product:products){
			product.setRegionId(regionId);
		}

		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set(uid, JSON.toJSONString(products));
		} else {
			this.redisDB.setAndExpire(ctoken, JSON.toJSONString(products), COOKIE_MAX_AGE);
		}
	}
	
	/**
	 * 
	 * <p>Discription:获取当前用户购物车商品</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<ProductInPriceDTO> getAllProducts(String ctoken,String uid) {
		// 如果用户已登录，合并购物车商品
		String ckey = ctoken;
		if( uid != null && !"".equals(uid) ){
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
	@Override
	public ShopCartDTO getMyCart(String ctoken, String uid, boolean carding, Map<Long,Integer> freightDeliveryType,String promoCode,String couponId,Integer paymentMethod,Integer platformId) {
		if(carding)
			this.cartCarding(ctoken, uid);
		
		List<ProductInPriceDTO> products = this.getProducts(ctoken, uid,null);
		List<ShopOutPriceDTO> shops = this.convertToShop(products, freightDeliveryType);
		ShopCartDTO cart = new ShopCartDTO();
		cart.setShops(shops);
		// 优惠券计算
		//优惠劵couponId，付款方法 paymentMethod, platformId平台
		if (StringUtils.isNotBlank(couponId)) {
			ExecuteResult<ShopCartDTO> newCart = shopCartCouponService.calcCoupon(cart, Long.parseLong(uid), couponId, paymentMethod, platformId);
			cart = newCart.getResult();
		}
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
			ExecuteResult<ItemShopCartDTO> er = this.itemService.getSkuShopCart(dto);
			
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
	 * <p>Discription:获取购物车所有商品，并按营销活动进行处理</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<ProductInPriceDTO> getProducts(String ctoken,String uid,String orderType){
		List<ProductInPriceDTO> allProducts = this.getAllProducts(ctoken,uid);
		List<ProductInPriceDTO> products = new LinkedList<ProductInPriceDTO>();
		for (ProductInPriceDTO p : allProducts) {
			boolean success = this.setSku(p,orderType);
			LOG.debug("设置SKUOK");
			if (success) {
				// 查询商品营销活动
				this.setPromotions(p);
				// 计算商品营销活动
				this.calculatePromotion(p);
				products.add(p);
			}
		}
		return products;
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
	public List<ShopOutPriceDTO> convertToShop(List<ProductInPriceDTO> products, Map<Long,Integer> deliveryTypeFreightDTO){
		Map<Long,ShopOutPriceDTO> shopMap = new HashMap<Long, ShopOutPriceDTO>();
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
	 * 
	 * <p>Discription:设置商品SKU信息</p>
	 * Created on 2015年3月9日
	 * @param product
	 * @author:[Goma 郭茂茂]
	 */
	private boolean setSku(ProductInPriceDTO product,String orderType){
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
		
		LOG.debug("通过SKUID查询商品SKU信息："+JSON.toJSONString(er));
		
		if( er.isSuccess() && er.getResult() != null ){
			ItemShopCartDTO iscd = er.getResult();
			if(StringUtils.isBlank(orderType)){//普通订单价格
				product.setSkuPrice(iscd.getSkuPrice());
				product.setPayPrice(iscd.getSkuPrice());
			}else{//协议或询价订单价格
				product.setPayPrice(product.getSkuPrice());
			}
			product.setTitle(iscd.getItemName());
			product.setSrc(iscd.getSkuPicUrl());
			product.setCid(iscd.getCid());
			product.setStatus(iscd.getItemStatus());
			

			
			// 校验订单状态是否有效
			if( product.getChecked() && product.getStatus() != 4 ){
//				product.setChecked( false );
				product.setUnusualState(1);
				
				product.getUnusualMsg().add("商品已下架！");
			}
			
			// 校验是否超库存
			if( product.getChecked() && iscd.getQty() < product.getQuantity() ){
//				product.setQuantity(iscd.getQty());
//				product.setChecked(false);
				
				product.setUnusualState(2);
				product.getUnusualMsg().add("库存不足，请刷新页面重新设置购买数量!");
			}
			BigDecimal total = BigDecimal.valueOf(product.getQuantity()).multiply(product.getSkuPrice());
			product.setTotal(total);
			product.setPayTotal(total);
			product.setQty(iscd.getQty());
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
	 * <p>Discription: 获取商品可参加的营销活动</p>
	 * DESC: 现营销活动只有两种：满减、直降；如果买家为选取活动，将设置默认活动【优先直降】
	 * Created on 2015年3月31日
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private void setPromotions(ProductInPriceDTO product){
		List<PromotionDTO> promotions = new LinkedList<PromotionDTO>();
		PromotionInDTO pid = new PromotionInDTO();
		pid.setShopId(product.getShopId());
//		pid.setSellerId(product.getSellerId());
		pid.setItemId(product.getItemId());
		pid.setSkuId( product.getSkuId() );
		pid.setIsEffective("1");
//		pid.setAreaId(Long.valueOf(product.getRegionId()));
		
		@SuppressWarnings("rawtypes")
		Pager pager = new Pager();
		pager.setRows(20);
		LOG.debug("PROMOTION PARAMS:"+JSON.toJSONString(pid));
		ExecuteResult<DataGrid<PromotionOutDTO>> er = this.promotionService.getPromotion(pid,pager);
		LOG.debug("PROMOTION RESULT:"+JSON.toJSONString(er));
		if( er.isSuccess() ){
			
			LOG.debug("----->" + JSON.toJSONString(er.getResult()));
			
			for( PromotionOutDTO prom : er.getResult().getRows() ){
				PromotionInfo promotionInfo = prom.getPromotionInfo();
				Integer type = promotionInfo.getType();
				if( type == 2 ){ // 判断活动是否满足
					PromotionFullReduction fr = prom.getPromotionFullReduction();
					if( product.getTotal().compareTo( fr.getMeetPrice() ) >= 0 ){
						PromotionDTO promotion = new PromotionDTO();
						promotion.setType( type );
						promotion.setId(promotionInfo.getId());
						promotion.setName(promotionInfo.getActivityName());
						promotion.setPrice(fr.getDiscountPrice());
						promotions.add(promotion);
					}
				}else if( type == 1 ){
					PromotionMarkdown pmd = prom.getPromotionMarkdown();
					Integer quantity = product.getQuantity();
					if( pmd.getMinNum().compareTo(Long.valueOf(quantity)) <= 0 && pmd.getMaxNum().compareTo(Long.valueOf(quantity)) >= 0 ){
						PromotionDTO promotion = new PromotionDTO();
						promotion.setType( type );
						promotion.setId(promotionInfo.getId());
						promotion.setName(promotionInfo.getActivityName());
						promotion.setPrice(pmd.getPromotionPrice());
						promotions.add(promotion);
						
						if( product.getPromId() == null )
							product.setPromId(promotionInfo.getId());
					}
				}
			}
		}
		if( product.getPromId() == null && promotions.size() > 0 )
			product.setPromId(promotions.get(0).getId());
		    product.setPromotions(promotions);
		
		if( product.getPromId() == null ){
			if( promotions.size() > 0 )
				product.setPromId(promotions.get(0).getId());
		}
	}
	
	/**
	 * <p>Discription: 计算商品所选营销活动后的优惠价格</p>
	 * Created on 2015年3月31日
	 * @param product
	 * @author:[Goma 郭茂茂]
	 */
	private void calculatePromotion(ProductInPriceDTO product){
		for( PromotionDTO promotion: product.getPromotions() ){
			if( promotion.getId().compareTo(product.getPromId()) == 0 ){
				product.setPromType(promotion.getType());
				if( promotion.getType() == 1 ){
					product.setPayPrice(promotion.getPrice());
					BigDecimal total = promotion.getPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
					product.setPayTotal( total );
				}else if( promotion.getType() == 2 ){ // 满减活动
					BigDecimal total = product.getTotal();
					total = total.subtract(promotion.getPrice());
					if( total.signum() == -1 )
						total = BigDecimal.ZERO;
					product.setPayTotal(total);
				}
			}
		}
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
				BigDecimal initTotal = sd.getInitialPrice() == null ? BigDecimal.valueOf(0) : sd.getInitialPrice();
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

	/**
	 * 订单提交
	 */
	@Override
	public Map<String, Object> subimtOrder(String ctoken, String uid, TradeOrdersDTO dto,
			String contractNo, String needApprove, Map<Long, Integer> freightDeliveryType, String promoCode,String couponId) {
		LOG.debug(JSON.toJSONString(dto));
		ExecuteResult<String> er = new ExecuteResult<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		ShopCartDTO sc = null;
		if (dto.getOrderType() == null) {
			sc = this.getMyCart(ctoken, uid, false, freightDeliveryType,dto.getPromoCode(), couponId, dto.getPaymentMethod(),  com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
			dto.setOrderType(2);
		} else {
			sc = this.getMyOrder(uid, freightDeliveryType,contractNo);
		}
		if( sc.getUnusualCount() > 0 ){
			er.addErrorMessage("商品信息有变动，请返回购物车查看！");
		}else{
			// 首先，创建平台订单
			dto.setPaid(1);
			ExecuteResult<TradeOrderCreateDTO> erCreate = this.createOrder(sc, dto);
			if( erCreate.isSuccess() && erCreate.getResult() != null ){
				TradeOrderCreateDTO result = erCreate.getResult();
				String orderId = result.getParentOrder().getOrderId();
				er.setResult(orderId);
				// 其次，创建支付订单
				ExecuteResult<Integer> erPay = this.createPayOrder(result);
				LOG.debug(JSON.toJSONString(erPay));
				if( erPay.isSuccess() ){
					// 最后，更新购物车
					this.submitCallBack(ctoken, uid);
				}else{
					LOG.error("支付订单创建失败");
					er.setErrorMessages(erPay.getErrorMessages());
					this.tradeOrderExportService.deleteTradeOrders(orderId);
				 }
			}else{
				er.setErrorMessages(erCreate.getErrorMessages());
			}
			map.put("er", er);
		}
		return map;
	}

	/**
	 * 
	 * <p>Discription: 创建平台订单</p>
	 * Created on 2015年3月17日
	 * @param ctoken
	 * @param uid
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private ExecuteResult<TradeOrderCreateDTO> createOrder(ShopCartDTO sc, TradeOrdersDTO dto){
		LOG.debug(JSON.toJSONString(dto));
		TradeOrderCreateDTO createDTO = new TradeOrderCreateDTO();
		dto.setPaymentPrice(sc.getPayTotal().add(sc.getTotalFare()));
		dto.setTotalPrice(sc.getTotal());
		dto.setFreight(sc.getTotalFare());
		dto.setCouponId(sc.getCouponId());
		createDTO.setParentOrder(dto);
		
		for( ShopOutPriceDTO shop : sc.getShops() ){
			if( shop.getQuantity() > 0 ){
				TradeOrdersDTO tod = new TradeOrdersDTO();
				BeanUtils.copyProperties(dto, tod);
				tod.setShopId(shop.getShopId());
				tod.setSellerId(shop.getSellerId());
				tod.setFreight(shop.getFare());
				tod.setPaymentPrice(shop.getPayTotal().add(shop.getFare()));
				tod.setTotalPrice(shop.getTotal());
				
				tod.setTotalDiscount(shop.getTotal().subtract(shop.getPayTotal()));
				
				tod.setItems(new LinkedList<TradeOrderItemsDTO>());
				tod.setCentralPurchasing(dto.getCentralPurchasing());
				for(ProductInPriceDTO pro : shop.getProducts()){
					if(pro.getChecked()){
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
						itemDto.setContractNo(dto.getContractNo());// 增加协议订单号
						itemDto.setActivitesDetailsId(pro.getActivitesDetailsId()); // 设置集采活动详情ID
						tod.getItems().add(itemDto);
						if (pro.getTradeOrderItemsDiscountDTO() != null) {
							pro.getTradeOrderItemsDiscountDTO().setSkuId(pro.getSkuId());
							tod.getTradeOrderItemsDiscountDTO().add(pro.getTradeOrderItemsDiscountDTO());
						}
					}
				}
				tod.setActivityRecordDTOs(shop.getActivityRecordDTOs());// 活动记录信息
				createDTO.getSubOrders().add(tod);
			}
		}
		LOG.debug("CREATE ORDER PARAMS:"+JSON.toJSONString(createDTO));
		return this.tradeOrderExportService.createOrder(createDTO);
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
		param.setOrderNo(String.valueOf(pOrder.getOrderId()));
		param.setTotalFee(pOrder.getPaymentPrice());
		param.setPayOrderType(PayOrderTypeEnum.Parent);
		param.setSystem("MALL");
		String orderIds = "";
		List<OrderItemPay> items = new LinkedList<OrderItemPay>();
		for( TradeOrdersDTO td: dto.getSubOrders() ){
//			OrderItemPay item = new OrderItemPay();
//			item.setSubOrderId(td.getOrderId().toString());
//			item.setShopId(td.getShopId().toString());
//			item.setSellerId(td.getSellerId());
//			item.setSubOrderPrice(td.getPaymentPrice());
//			items.add(item);
			//李伟龙修改，改成与pc端一致
			OrderItemPay item = new OrderItemPay();
			item.setSubOrderId(td.getOrderId());
			item.setShopId(td.getShopId().toString());
			item.setSellerId(td.getSellerId());
			item.setSubOrderPrice(td.getPaymentPrice());
			item.setSubOrderSubject("印刷家订单："+td.getOrderId());
			items.add(item);
			orderIds = orderIds+td.getOrderId()+"|";
		
		}
		orderIds = orderIds.substring(0, orderIds.length()-1);
		param.setSubject("印刷家订单："+orderIds);
		param.setOrderItemPays(items);
		
		Map<String, String> params =new HashMap<String, String>();
		params.put("system", param.getSystem());
		params.put("orderNo", param.getOrderNo());
		params.put("totalFee", param.getTotalFee().toString());
		
		param.setSign(Signature.createSign(params, "123456"));
		
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
		
		for(ProductInPriceDTO product:products){
			if(!product.getChecked()){
				nProducts.add(product);
			}
		}

		// 判断用户是否登录进行区别保存购物车数据
		if ( uid != null && !"".equals(uid) ) {
			this.redisDB.set(uid, JSON.toJSONString(nProducts));
		} else {
			this.redisDB.setAndExpire(ctoken, JSON.toJSONString(nProducts), COOKIE_MAX_AGE);
		}
	}

	/**
	 * 获取放在redis中的协议或是询价中的商品
	 */
	@Override
	public String getRedisCar(String uid) {
		return this.redisDB.get(uid+"Redis");
	}
	/**
	 * 设置放在redis中的协议或是询价中的商品
	 */
	@Override
	public void setRedisCar(String uid,String jsonProducts) {
		this.redisDB.set(uid+"Redis", jsonProducts);
	}
	/**
	 * 删除协议或是询价订单的购物车
	 */
	@Override
	public void delRedisCart(String uid) {
		this.redisDB.del(uid+"Redis");
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
	@Override
	public ExecuteResult<BigDecimal> calcShopFreight(ShopOutPriceDTO shop, Map<Long, Integer> shopDeliveryType) {
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
	
	@Override
	public ShopCartDTO getMyOrder(String uid, Map<Long, Integer> freightDeliveryType, String contractNo) {
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

	


}
