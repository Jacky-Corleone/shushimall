package com.camelot.mall.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.util.Signature;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.mall.service.ShopCartService;
import com.camelot.mall.shopcart.Product;
import com.camelot.mall.shopcart.ProductAttr;
import com.camelot.mall.shopcart.Promotion;
import com.camelot.mall.shopcart.Shop;
import com.camelot.mall.shopcart.ShopCart;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopFareDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFareExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {
	private Logger LOG = Logger.getLogger(this.getClass());
	private static Integer COOKIE_MAX_AGE = 1000 * 60 * 60 * 24 * 7;

	@Resource
	private RedisDB redisDB;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopService;
	@Resource
	private PromotionService promotionService; 
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private PaymentExportService paymentService;
	@Resource
	private ShopFareExportService shopFareService;
	@Resource
	private ShopCustomerServiceService shopCustomerServiceService;
	
	
	
	@Override
	public ExecuteResult<String> add(Product product) {
		boolean exists = false;
		Long skuid = product.getSkuId();
		String ctoken = product.getCtoken();
		String uid = product.getUid();
		List<Product> products = this.getAllProducts(ctoken,uid);


		for (Product p : products) {
			LOG.debug("SKUID:"+skuid +"    xxx:" + p.getSkuId());
			
			if ( skuid.compareTo(p.getSkuId()) == 0 ) {
				p.setRegionId(product.getRegionId());
				p.setQuantity(p.getQuantity() + product.getQuantity());
				p.setChecked(true);
				exists = true;
				break;
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

		ExecuteResult<String> er = new ExecuteResult<String>();
		er.setResult(product.getCtoken());
		return er;
		
	}

	@Override
	public ExecuteResult<String> edit(Product product) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		Long skuid = product.getSkuId();

		String ctoken = product.getCtoken();
		String uid = product.getUid();
		List<Product> products = this.getAllProducts(ctoken,uid);

		for (int i = 0; i < products.size(); i++) {
			Product p = products.get(i);
			if ( skuid.compareTo(p.getSkuId()) == 0 ) {
				p.setRegionId(product.getRegionId());
				p.setQuantity(product.getQuantity());
				p.setPromId(product.getPromId());
				p.setChecked(product.getChecked());
				break;
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
	public ExecuteResult<String> del(Product product) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		Long skuid = product.getSkuId();
		
		String ctoken = product.getCtoken();
		String uid = product.getUid();
		List<Product> products = this.getAllProducts(ctoken,uid);
		
		for (int i = 0; i < products.size(); i++) {
			Product p = products.get(i);
			if ( skuid.compareTo(p.getSkuId()) == 0 ) {
				products.remove(i);
				break;
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
	private List<Product> getAllProducts(String ctoken,String uid) {
		// 如果用户已登录，合并购物车商品
		String ckey = ctoken;
		if( uid != null && !"".equals(uid) ){
			this.mergeCart(ctoken, uid);
			ckey = uid;
		}
		String myCart = this.redisDB.get(ckey);
		return myCart == null || "".equals(myCart) ? new LinkedList<Product>() : JSON.parseArray(myCart, Product.class);
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
	 * <p>Discription:获取购物车所有商品，并按营销活动进行处理</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<Product> getProducts(String ctoken,String uid){
		List<Product> allProducts = this.getAllProducts(ctoken,uid);
		List<Product> products = new LinkedList<Product>();
		for (Product p : allProducts) {
			boolean success = this.setSku(p);
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
	 * <p>Discription: 获取商品可参加的营销活动</p>
	 * DESC: 现营销活动只有两种：满减、直降；如果买家为选取活动，将设置默认活动【优先直降】
	 * Created on 2015年3月31日
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private void setPromotions(Product product){
		List<Promotion> promotions = new LinkedList<Promotion>();
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
						Promotion promotion = new Promotion();
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
						Promotion promotion = new Promotion();
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
		
//		if( product.getPromId() == null ){
//			if( promotions.size() > 0 )
//				product.setPromId(promotions.get(0).getId());
//		}
	}
	
	/**
	 * <p>Discription: 计算商品所选营销活动后的优惠价格</p>
	 * Created on 2015年3月31日
	 * @param product
	 * @author:[Goma 郭茂茂]
	 */
	private void calculatePromotion(Product product){
		for( Promotion promotion: product.getPromotions() ){
			if( promotion.getId().compareTo(product.getPromId()) == 0 ){
				product.setPromType(promotion.getType());
				if( promotion.getType() == 1 ){
					product.setPayPrice(promotion.getPrice());
					BigDecimal total = product.isHasPrice() ? promotion.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())) : BigDecimal.ZERO;
					product.setPayTotal( total );
				}else if( promotion.getType() == 2 ){ // 满减活动
					BigDecimal total = product.getTotal();
					total = product.isHasPrice() ? total.subtract(promotion.getPrice()) : BigDecimal.ZERO;
					if( total.signum() == -1 )
						total = BigDecimal.ZERO;
					product.setPayTotal(total);
				}
			}
		}
	}
	
	/**
	 * 
	 * <p>Discription:设置商品SKU信息</p>
	 * Created on 2015年3月9日
	 * @param product
	 * @author:[Goma 郭茂茂]
	 */
	private boolean setSku(Product product){
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
			product.setSkuPrice(iscd.getSkuPrice());
			product.setPayPrice(iscd.getSkuPrice());
			product.setTitle(iscd.getItemName());
			product.setSrc(iscd.getSkuPicUrl());
			product.setCid(iscd.getCid());
			product.setStatus(iscd.getItemStatus());
			product.setHasPrice(iscd.isHasPrice());
			
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
			
			// 校验是否超库存
			if( product.getChecked() && iscd.getQty() < product.getQuantity() ){
//				product.setQuantity(iscd.getQty());
//				product.setChecked(false);
				
				product.setUnusualState(2);
				product.getUnusualMsg().add("库存不足，请重新设置购买数量!");
			}
			BigDecimal total = product.isHasPrice() ? BigDecimal.valueOf(product.getQuantity()).multiply(product.getSkuPrice()) : BigDecimal.ZERO;
			product.setTotal(total);
			product.setPayTotal(total);
			product.setQty(iscd.getQty());
			
			// 设置商品 销售属性
			List<ProductAttr> attrs = new LinkedList<ProductAttr>();
			for(ItemAttr ia : iscd.getAttrSales()){
				ProductAttr attr = new ProductAttr();
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
	 * <p>Discription:获取购物车所有店铺及商品</p>
	 * Created on 2015年3月7日
	 * @param ctoken
	 * @param uid
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private List<Shop> convertToShop( List<Product> products ){
		Map<Long,Shop> shopMap = new HashMap<Long, Shop>();
		for(Product p:products){
			Long shopid = p.getShopId();
			Shop shop = shopMap.get(shopid);
			
			if( shop == null ){
				shop = new Shop();
				shop.setShopId(shopid);
				shop.setSellerId(p.getSellerId());
				
				// 查询店铺名称
				ExecuteResult<ShopDTO> er = this.shopService.findShopInfoById(shopid);
				ShopDTO sd = er.getResult();
				String name = sd != null ? sd.getShopName() : "";
				shop.setShopTitle(name);
				
				shop.setProducts(new LinkedList<Product>());
				
				// 店铺运费设置
				ShopFareDTO fare = new ShopFareDTO();
				fare.setShopId(shopid);
				fare.setFareRegion(p.getRegionId());
				LOG.debug("SHOP FARE PARAMS:"+JSON.toJSONString(fare));
				ExecuteResult<ShopFareDTO> erShopFare = this.shopFareService.queryShopFareByFareRegion(fare);
				LOG.debug("SHOP FARE RESULT:"+JSON.toJSONString(erShopFare));
				if( erShopFare.isSuccess() && erShopFare.getResult() != null ){
					shop.setFare(erShopFare.getResult().getFirstWeightPrice());
				}else{
					shop.setFare(BigDecimal.ZERO);
				}
				
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
		
		// 
		List<Shop> sps = new LinkedList<Shop>();
		Iterator<Long> keys = shopMap.keySet().iterator();
		while( keys.hasNext() ){
			Long key = keys.next();
			Shop shop = shopMap.get(key);
			
			// 判断商品是否满足店铺起批、混批条件
			boolean canBuy = this.validShopRule(shop);
			if (!canBuy) {
				for (int i = 0; i < shop.getProducts().size(); i++){
					if(shop.getProducts().get(i).getChecked()){
//						shop.getProducts().get(i).setChecked(false);
						
						shop.getProducts().get(i).setUnusualState(2);
						shop.getProducts().get(i).getUnusualMsg().add("不满足店铺起批、混批规则!");
					}
				}
			}
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
	private boolean validShopRule(Shop shop) {
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
				
				shopRule.append(" ");
				
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
	public ShopCart getMyCart(String ctoken,String uid, Boolean carding){
		
		if(carding)
			this.cartCarding(ctoken, uid);
		
		List<Product> products = this.getProducts(ctoken, uid);
		List<Shop> shops = this.convertToShop(products);
		
		ShopCart cart = new ShopCart();
		cart.setShops(shops);
		return cart;
	}
	

	@Override
	public ExecuteResult<String> checkShop(String ctoken,String uid,Long shopid,Boolean checked){
		List<Product> products = this.getAllProducts(ctoken,uid);
		
		for(Product p: products){
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
		List<Product> products = this.getAllProducts(ctoken,uid);
		
		for(Product p: products){
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
	public ExecuteResult<String> changeRegion(String ctoken,String uid,String regionId){
		List<Product> products = this.getAllProducts(ctoken,uid);
		
		for(Product product:products){
			product.setRegionId(regionId);
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
	public ExecuteResult<String> subimtOrder(String ctoken,String uid, TradeOrdersDTO dto){
		LOG.debug(JSON.toJSONString(dto));
		ExecuteResult<String> er = new ExecuteResult<String>();
		ShopCart sc = this.getMyCart(ctoken, uid, false);
		if( sc.getUnusualCount() > 0 ){
			er.addErrorMessage("商品信息有变动，请返回购物车查看！");
		}else{

			// 首先，创建平台订单
			ExecuteResult<TradeOrderCreateDTO> erCreate = this.createOrder(sc, dto);
			if( erCreate.isSuccess() && erCreate.getResult() != null ){
				TradeOrderCreateDTO result = erCreate.getResult();
				String orderId = result.getParentOrder().getOrderId();
				er.setResult(orderId);
				// 其次，创建支付订单
				ExecuteResult<Integer> erPay = this.createPayOrder(result);
				LOG.debug(JSON.toJSON(erPay));
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
			
		}
		return er;
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
	private ExecuteResult<TradeOrderCreateDTO> createOrder(ShopCart cart, TradeOrdersDTO dto){
		LOG.debug(JSON.toJSONString(dto));
		TradeOrderCreateDTO createDTO = new TradeOrderCreateDTO();
		dto.setPaymentPrice(cart.getPayTotal().add(cart.getTotalFare()));
		dto.setTotalPrice(cart.getTotal());
		dto.setFreight(cart.getTotalFare());
		createDTO.setParentOrder(dto);
		
		
		for( Shop shop : cart.getShops() ){
			if( shop.getQuantity() > 0 ){
				TradeOrdersDTO tod = new TradeOrdersDTO();
				BeanUtils.copyProperties(dto, tod);
				tod.setShopId(shop.getShopId());
				tod.setSellerId(shop.getSellerId());
				tod.setFreight(shop.getFare());
				tod.setPaymentPrice(shop.getPayTotal().add(shop.getFare()));
				tod.setTotalPrice(shop.getTotal());
				
				tod.setPromoCode(dto.getPromoCode());
				tod.setMemo(dto.getMemo());
				
				tod.setTotalDiscount(shop.getTotal().subtract(shop.getPayTotal()));
				
				tod.setItems(new LinkedList<TradeOrderItemsDTO>());
				for(Product pro : shop.getProducts()){
					if(pro.getChecked()){
						TradeOrderItemsDTO itemDto = new TradeOrderItemsDTO();
						
						itemDto.setItemId(pro.getItemId());//  SKU所属品类ID
						itemDto.setSkuId(pro.getSkuId());//  SKU
						
						itemDto.setPrimitivePrice(pro.getSkuPrice());// SKU价格
						itemDto.setPayPrice(pro.getPayPrice());// 支付价格（优惠后的价格）
						
						itemDto.setAreaId(Long.valueOf(pro.getRegionId()));
						itemDto.setNum(pro.getQuantity());//  数量
						
						itemDto.setPayPriceTotal(pro.getPayTotal());
						
						itemDto.setPromotionType(pro.getPromType());
						itemDto.setPromotionId(pro.getPromId());//  促销ID
						
						itemDto.setCid(pro.getCid());
						
						tod.getItems().add(itemDto);
					}
				}
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
		param.setSystem(SysProperties.getProperty("transfer.system"));
		
		List<OrderItemPay> items = new LinkedList<OrderItemPay>();
		for( TradeOrdersDTO td: dto.getSubOrders() ){
			OrderItemPay item = new OrderItemPay();
			item.setSubOrderId(td.getOrderId().toString());
			item.setShopId(td.getShopId().toString());
			item.setSellerId(td.getSellerId());
			item.setSubOrderPrice(td.getPaymentPrice());
			items.add(item);
		}
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
		List<Product> nProducts = new LinkedList<Product>();
		List<Product> products = this.getAllProducts(ctoken, uid);
		
		for(Product product:products){
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
		
		try {
			er = this.paymentService.pay(param);
		} catch (Exception e) {
			LOG.error("支付订单失败！",e);
			er.addErrorMessage(e.getMessage());
		}
		return er;
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
		List<Product> products = this.getAllProducts(ctoken, uid);
		for( Product product:products ){
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
	 * <p>Discription:根据复选框内容进行批量删除购物</p>
	 * Created on 2015年11月13日15:27:57
	 * @param product 缓存商品信息
	 * @param objskuId 复选框选择商品
	 * @return
	 * @author:[訾瀚民]
	 */
	@Override
	public ExecuteResult<String> delAll(Product product,String objskuId) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		String ctoken = product.getCtoken();
		String uid = product.getUid();
		List<Product> products = this.getAllProducts(ctoken,uid);
		List<Product> productsTemp = new LinkedList<Product>();
		productsTemp.addAll(products);
		for (int i = 0; i < productsTemp.size(); i++) {
			Product p = productsTemp.get(i);
			if ( objskuId.contains(p.getSkuId().toString()) ) { 
				products.remove(p);
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
	public List<Product> AllProducts(String ctoken, String uid) {
		
		
		List<Product> products = this.getAllProducts(ctoken,uid);
		return products;
	}
	

	
	
}
