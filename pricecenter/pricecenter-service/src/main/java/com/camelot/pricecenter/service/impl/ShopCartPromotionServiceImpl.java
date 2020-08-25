package com.camelot.pricecenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionFrExportService;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.pricecenter.dto.ProductAttrDTO;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ProductOutPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.service.ShopCartFreightService;
import com.camelot.pricecenter.service.ShopCartPromotionService;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFareExportService;
import com.camelot.usercenter.dto.UserDTO;

/**
 * 
 * <p>Description: [计算商品营销活动业务实现]</p>
 * Created on 2015年11月24日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopCartPromotionService")
public class ShopCartPromotionServiceImpl implements ShopCartPromotionService {

	private Logger LOG = Logger.getLogger(this.getClass());

	@Resource
	private ItemExportService itemService;

	@Resource
	private PromotionService promotionService;
	
	@Resource
	private PromotionFrExportService promotionFrExportService;
	
	@Resource
	private ShopExportService shopService;
	
	@Resource
	private ShopFareExportService shopFareService;
	
	@Resource
	private ShopCartFreightService shopCartFreightService;
	
	@Resource
	private ShopCustomerServiceService shopCustomerServiceService;
	
	@Resource
	private ItemSkuPackageService itemSkuPackageService;
	
	@Override
	public ExecuteResult<List<ProductOutPriceDTO>> calcAllPromotion(List<ProductInPriceDTO> productInPriceDTOs,UserDTO user) {
		ExecuteResult<List<ProductOutPriceDTO>> result = new ExecuteResult<List<ProductOutPriceDTO>>();
		List<ProductOutPriceDTO> productOutPriceDTOs = new ArrayList<ProductOutPriceDTO>();
		try {
			if (productInPriceDTOs != null && productInPriceDTOs.size() > 0) {
				for (ProductInPriceDTO p : productInPriceDTOs) {
					// 计算商品营销活动
					ExecuteResult<ProductOutPriceDTO> executeResult = this.calcPromotion(p,user);
					if (executeResult.isSuccess() && executeResult.getResult() != null) {
						productOutPriceDTOs.add(executeResult.getResult());
					}
				}
			}
			result.setResult(productOutPriceDTOs);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			LOG.error("\n 方法[ShopCartPromotionServiceImpl-calcAllPromotion]异常：", e);
		}
		return result;
	}
	
	@Override
	public ExecuteResult<ProductOutPriceDTO> calcPromotion(ProductInPriceDTO productInPriceDTO,UserDTO user) {
		ExecuteResult<ProductOutPriceDTO> result = new ExecuteResult<ProductOutPriceDTO>();
		ProductOutPriceDTO productOutPriceDTO = null;
		try {
			if (productInPriceDTO != null) {
				// 查询商品营销活动
				this.setPromotions(productInPriceDTO,user);
				// 计算商品营销活动
				this.calcSinglePromotion(productInPriceDTO);
				productOutPriceDTO = new ProductOutPriceDTO();
				BeanUtils.copyProperties(productInPriceDTO, productOutPriceDTO);
				result.setResult(productOutPriceDTO);
			}
			result.setResult(productOutPriceDTO);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			LOG.error("\n 方法[ShopCartPromotionServiceImpl-calcPromotion]异常：", e);
		}
		return result;
	}

	/**
	 * 
	 * <p>Discription: 获取商品可参加的营销活动</p>
	 * DESC: 现营销活动只有两种：满减、直降；如果买家未选取活动，将设置默认活动【优先直降】
	 * Created on 2015年3月31日
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	private void setPromotions(ProductInPriceDTO product,UserDTO user) {
		PromotionDTO  promotion=null;
		Map<Long,Long> map=new HashMap<Long,Long>();
		Integer userType=null;
		if(user!=null){
			if(user.getUsertype()==1){
				userType=1;
			}else{
				userType=2;
			}
		}
		ExecuteResult<ItemBaseDTO> res=itemService.getItemBaseInfoById(product.getItemId());
		List<PromotionDTO> promotions = new LinkedList<PromotionDTO>();
		PromotionInDTO pid = new PromotionInDTO();
		pid.setShopId(product.getShopId());
		pid.setItemId(product.getItemId());
		pid.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
//        pid.setUserType(userType);//用户类型
        if(res.getResult().getPlatformId()==null){//该商品为科印商品
			pid.setPlatformId(1);
		}else if(res.getResult().getPlatformId()==2){//该商品为绿印商品
			pid.setPlatformId(res.getResult().getPlatformId());
		}
       /* if(user!=null){
        	// 获取成长值，判断用户会员等级
			BigDecimal growthValue = user.getGrowthValue();
			if(growthValue==null){
				growthValue=BigDecimal.ZERO;
			}
			VipLevelEnum vipLevel= VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
			pid.setMembershipLevel(vipLevel.getId().toString());
        }*/
		@SuppressWarnings("rawtypes")
		Pager pager = new Pager();
		pager.setRows(20);
		LOG.debug("PROMOTION PARAMS:" + JSON.toJSONString(pid));
		ExecuteResult<DataGrid<PromotionOutDTO>> er = this.promotionService.getPromotion(pid, pager);
		LOG.debug("PROMOTION RESULT:" + JSON.toJSONString(er));
		if( er.isSuccess() ){
			
			LOG.debug("----->" + JSON.toJSONString(er.getResult()));
			Set<Long> proSet=new HashSet<Long>();
            for( PromotionOutDTO prom : er.getResult().getRows() ){
				PromotionInfo promotionInfo = prom.getPromotionInfo();
                if(proSet.contains(promotionInfo.getId())){
                    continue;
                }
                proSet.add(promotionInfo.getId());

				Integer onLineState=promotionInfo.getOnlineState();
				Integer type = promotionInfo.getType();
				if(onLineState==1){
				if( type == 2 ){ // 判断活动是否满足
					PromotionFullReduction fr = prom.getPromotionFullReduction();
					    promotion=setPromotionDTO(type,promotionInfo,fr.getDiscountPrice());
						promotion.setMeetPrice(fr.getMeetPrice());
						promotion.setDiscountPrice(fr.getDiscountPrice());
						promotions.add(promotion);
				}else if( type == 1 ){
					if(map.get(product.getItemId())!=null){//同一个商品同时参加店铺和平台的直降活动取店铺直降活动
					  if(map.get(product.getItemId())<promotionInfo.getShopId()){
					    PromotionMarkdown pmd = prom.getPromotionMarkdown();
					    if(1==pmd.getMarkdownType()){
					       promotion=setPromotionDTO(type,promotionInfo,pmd.getDiscountPercent());
					    }else if(2==pmd.getMarkdownType()){
					       promotion=setPromotionDTO(type,promotionInfo,pmd.getPromotionPrice());
					    }
					    promotion.setDownType(pmd.getMarkdownType());
						promotions.add(promotion);
						product.setPromId(promotionInfo.getId());
					  }
					}else{//同一个商品同时参加店铺和平台的直降活动取店铺直降活动
						map.put(product.getItemId(), promotionInfo.getShopId());
						PromotionMarkdown pmd = prom.getPromotionMarkdown();
						 if(1==pmd.getMarkdownType()){
						    promotion=setPromotionDTO(type,promotionInfo,pmd.getDiscountPercent());
						 }else if(2==pmd.getMarkdownType()){
						    promotion=setPromotionDTO(type,promotionInfo,pmd.getPromotionPrice());
						 }
						promotion.setDownType(pmd.getMarkdownType());
						promotions.add(promotion);
						product.setPromId(promotionInfo.getId());
					}
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
	private void calcSinglePromotion(ProductInPriceDTO product) {
		Map<String,Long> map=new HashMap<String,Long>();
		//DecimalFormat df = new DecimalFormat("0.00");
		BigDecimal payPrice=BigDecimal.ZERO;
		for( PromotionDTO promotion: product.getPromotions() ){
			if( promotion.getId().compareTo(product.getPromId()) == 0 ){
				product.setPromType(promotion.getType());
				if( promotion.getType() == 1 ){
						if(promotion.getPrice()==null){
							promotion.setPrice(BigDecimal.ZERO);
						}
					    if(1==promotion.getDownType()){//直降百分比
					    	payPrice=promotion.getPrice().multiply(product.getSkuPrice()).setScale(2, BigDecimal.ROUND_DOWN);
					    }else if(2==promotion.getDownType()){//直降促销价
					    	payPrice=product.getSkuPrice().subtract(promotion.getPrice());
					    	if(payPrice.compareTo(new BigDecimal(0))<=0){
					    		payPrice=BigDecimal.ZERO;
					    	}
					    }else{
					    	payPrice=product.getSkuPrice();
					    }
						product.setPayPrice(payPrice);
						BigDecimal total = product.isHasPrice() ? product.getPayPrice().multiply(BigDecimal.valueOf(product.getQuantity())) : BigDecimal.ZERO;
						product.setPayTotal( total );
						//平台直降活动
						if(promotion.getShopId()==0){
							map.put("platformActivity",promotion.getId());
						}else{//店铺直降活动
							map.put("shopActivity", promotion.getId());
						}
					 product.setMap(map);
				   }	
				}
			}
	}
	@Override
	public ExecuteResult<Map<Long, ShopOutPriceDTO>> getShopByProducts(List<ProductInPriceDTO> allProducts,UserDTO user) {
//		Integer userType=null;
		ExecuteResult<Map<Long, ShopOutPriceDTO>> result=new ExecuteResult<Map<Long, ShopOutPriceDTO>>();
		/*if(user!=null){
			if(user.getUsertype()==1){
				userType=1;
			}else{
				userType=2;
			}
		}*/
		//amountMap存放 key=商品id   value=商品总金额
		Map<Long,BigDecimal> amountMap=new HashMap<Long,BigDecimal>();	
		Map<Long,BigDecimal> shopAmountMap=new HashMap<Long,BigDecimal>();	
		//shopAmountMap.put(0L, new BigDecimal("0.00"));	//所有商品的金额
		
		BigDecimal keyinAmount=new BigDecimal("0");
		BigDecimal lvyinAmount=new BigDecimal("0");
		
		// 存放  <店铺id,店铺信息>
		Map<Long, ShopOutPriceDTO> shopMap=new HashMap<Long, ShopOutPriceDTO>();
		ShopOutPriceDTO platOutPriceDTO = new ShopOutPriceDTO();
		platOutPriceDTO.setShopId(0L);
		shopMap.put(0L, platOutPriceDTO);
		
		try {
			//对商品循环 进行打折计算总价
			for (ProductInPriceDTO product : allProducts) {
				if(!product.getChecked()){
					continue;
				}
				boolean flag=this.setSku(product);
				if(!flag){
					result.addErrorMessage("查询商品信息失败,skuId:"+product.getSkuId());
				}
				//根据商品获取店铺信息  （店铺名称、运费、店铺IM等）
				getShopInfoByProduct(shopMap, product);
				
				/*map.put(product.getShopId(), new ArrayList<Promotion>());*/
				PromotionInDTO pid = new PromotionInDTO();
				pid.setShopId(product.getShopId());	//店铺id
				pid.setItemId(product.getItemId());	//商品id
				pid.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));	//查询进行中
				pid.setType(1);//查询限时折扣
//				pid.setUserType(userType);
				pid.setPlatformId(product.getPlatFormId());//平台id
				/*if(user!=null){
		        	// 获取成长值，判断用户会员等级
					BigDecimal growthValue = user.getGrowthValue();
					if(growthValue==null){
						growthValue=BigDecimal.ZERO;
					}
					VipLevelEnum vipLevel= VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
					pid.setMembershipLevel(vipLevel.getId().toString());
			      }*/
				ExecuteResult<DataGrid<PromotionOutDTO>> er = this.promotionService.getPromotion(pid,null);
				if(!er.isSuccess()){
					result.addErrorMessage("查询促销活动失败");
				}
				Map<Long,Long> map=new HashMap<Long,Long>();
				//默认折扣为100% 
				BigDecimal discountPercent =new BigDecimal("1");
				BigDecimal promotionPrice=BigDecimal.valueOf(0);//促销价
				BigDecimal price=product.getSkuPrice();//直降价格
				Integer type=null;
				if(er.getResult().getTotal()>0){
					for(PromotionOutDTO promotionOutDTO:er.getResult().getRows()){
						PromotionInfo promotionInfo=promotionOutDTO.getPromotionInfo();
						PromotionMarkdown markdown =promotionOutDTO.getPromotionMarkdown();
						if(map.get(product.getItemId())!=null){//同一个商品同时存在平台和店铺的直降活动，则取店铺的直降
							if(map.get(product.getItemId())<promotionInfo.getShopId()){
								type=markdown.getMarkdownType();
								promotionPrice=markdown.getPromotionPrice();
								if(markdown.getDiscountPercent()!=null){
									discountPercent=markdown.getDiscountPercent();
								}
							}
						}else{
							map.put(product.getItemId(),promotionInfo.getShopId());
							type=markdown.getMarkdownType();
							promotionPrice=markdown.getPromotionPrice();
							if(markdown.getDiscountPercent()!=null){
								discountPercent=markdown.getDiscountPercent();
							}
						}
					}
				}
				//先对单价进行折扣  再保留2位小数位 多余小数位向下舍去
				if(type!=null){
					if(product.getSkuPrice()==null){
						product.setSkuPrice(BigDecimal.ZERO);
					}
					if(1==type){
						 price= product.getSkuPrice().multiply(discountPercent).setScale(2, BigDecimal.ROUND_DOWN);
					}else if(2==type){ 
						 price=product.getSkuPrice().subtract(promotionPrice);
						 if(price.compareTo(new BigDecimal(0))<=0){
							 price=BigDecimal.ZERO;
						}
					}
				}
				BigDecimal totalPrice=price.multiply(new BigDecimal(product.getQuantity()));
				product.setPayPrice(price);	//折扣价
				product.setPayTotal(totalPrice);	//折扣支付总价
				
				Long shopItem= product.getItemId();
				Long shopId= product.getShopId();
				//同一个商品下的金额计算到一起
				if(amountMap.containsKey(shopItem)){
					amountMap.put(shopItem, amountMap.get(shopItem).add(totalPrice));
				}else{
					amountMap.put(shopItem, totalPrice);
				}
				//店铺的金额
				if(shopAmountMap.containsKey(shopId)){
					shopAmountMap.put(shopId,shopAmountMap.get(shopId).add(totalPrice));
				}else{
					shopAmountMap.put(shopId,totalPrice);
				}
				
				//所有商品的金额
				//shopAmountMap.put(0L,shopAmountMap.get(0L).add(totalPrice));
				
				if(product.getPlatFormId()!=null && product.getPlatFormId()==1){
					keyinAmount=keyinAmount.add(totalPrice);
				}else if(product.getPlatFormId()!=null && product.getPlatFormId()==2){
					lvyinAmount=lvyinAmount.add(totalPrice);
				}
				
			}
			
			Iterator<Long> keys = shopMap.keySet().iterator();
			while( keys.hasNext() ){
				Long key = keys.next();
				if(key!=0){
					ShopOutPriceDTO shop = shopMap.get(key);
					// 判断商品是否满足店铺起批、混批条件
					boolean canBuy = this.validShopRule(shop);
					if (!canBuy) {
						for (int i = 0; i < shop.getProducts().size(); i++){
							if(shop.getProducts().get(i).getChecked()){
//								shop.getProducts().get(i).setChecked(false);

								shop.getProducts().get(i).setUnusualState(2);
								shop.getProducts().get(i).getUnusualMsg().add("不满足店铺起批、混批规则!");
							}
						}
					}
					if(user!=null && user.getShopId()!=null){
						if(user.getShopId().equals(shop.getShopId())){
							for (int i = 0; i < shop.getProducts().size(); i++){
								if(shop.getProducts().get(i).getChecked()){
									shop.getProducts().get(i).setUnusualState(10);
									shop.getProducts().get(i).getUnusualMsg().add("买家不允许购买自己店铺的商品!");
								}
							}
						}
					}
				}
			}
			Iterator<Entry<Long, ShopOutPriceDTO>> iterator = shopMap.entrySet().iterator();
			Set<Long> platPromotionSet=new HashSet<Long>();
			//for(Long shopId :shopMap.keySet()) {
			while(iterator.hasNext()){
				Entry<Long, ShopOutPriceDTO> entry = iterator.next();
				Long shopId=entry.getKey();
				//Long shopId = iterator.next().getKey();
				ShopOutPriceDTO shop =entry.getValue();
				
				List<PromotionDTO> list=new ArrayList<PromotionDTO>();
				PromotionInDTO pid = new PromotionInDTO();
				pid.setShopId(shopId);	//店铺id
				pid.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));	//查询有效活动 
				pid.setType(2);//查询满减
//				pid.setUserType(userType);
				//pid.setPlatformId(platformId);
				/*if(user!=null){
		        	// 获取成长值，判断用户会员等级
					BigDecimal growthValue = user.getGrowthValue();
					if(growthValue==null){
						growthValue=BigDecimal.ZERO;
					}
					VipLevelEnum vipLevel= VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
					pid.setMembershipLevel(vipLevel.getId().toString());
			      }*/
				ExecuteResult<DataGrid<PromotionOutDTO>> er = this.promotionService.getPromotion(pid,null);
				//shop set的是list的引用 
				
				//shop.setPromotionList(list);
				if(!er.isSuccess()||er.getResult().getTotal()<=0){
					continue ;
				}
				//平台促销商品idset
				Set<Long> promotionSet=new HashSet<Long>();
				for (PromotionOutDTO outDTO : er.getResult().getRows()) {
					PromotionFullReduction promotionFullReduction=new PromotionFullReduction();
					PromotionInfo promotionInfo = outDTO.getPromotionInfo();
					//查出来的结果集是促销活动一对多满减活动 所以要去重
					Long promotionId=promotionInfo.getId();
					if(promotionId!=null && promotionSet.contains(promotionId)){
						continue;
					}else{
						promotionSet.add(promotionId);
					}
					promotionFullReduction.setPromotionInfoId(promotionId);
					//根据促销活动id查询对应的满减列表
					ExecuteResult<DataGrid<PromotionFullReduction>> queryPromotionFrList 
							= promotionFrExportService.queryPromotionFrList(promotionFullReduction,null);
					if(queryPromotionFrList.isSuccess() && queryPromotionFrList.getResult().getTotal()>0){
						//判断是否达到 满减价格
						BigDecimal meetPrice=queryPromotionFrList.getResult().getRows().get(0).getMeetPrice();
						
						//同一个商品id只计算一次
						Set<Long> itemSet=new HashSet<Long>();
						for (PromotionFullReduction pf : queryPromotionFrList.getResult().getRows()) {
							
							if(pf.getItemId()!=null && pf.getItemId()==0){
								//平台全部商品或店铺全部商品
								if(promotionInfo.getShopId()!=null && promotionInfo.getShopId()==0){
									//针对平台所有商品  （科印或绿印）
									if(promotionInfo.getPlatformId()!=null && promotionInfo.getPlatformId()==1){
										meetPrice=meetPrice.subtract(keyinAmount);	//科印商品总金额
									}else if(promotionInfo.getPlatformId()!=null && promotionInfo.getPlatformId()==2){
										meetPrice=meetPrice.subtract(lvyinAmount);	//绿印商品总金额
									}/*else{
										meetPrice=meetPrice.subtract(shopAmountMap.get(0L));
									}*/
								}else{
									meetPrice=meetPrice.subtract(shopAmountMap.get(shopId));
								}
								break;
							}
							
							//itemSet若包含商品id，则跳出；    amountMap即商品list里如果没有该商品 ，则跳出
							if(itemSet.contains(pf.getItemId()) || !amountMap.containsKey(pf.getItemId()) ){
								continue;
							}
							itemSet.add(pf.getItemId());
							meetPrice=meetPrice.subtract(amountMap.get(pf.getItemId()));
						}
						//如果小于0，则符合该活动
						if(meetPrice.compareTo(BigDecimal.ZERO)<=0){
							//平台活动
							if(outDTO.getPromotionInfo().getShopId()!=null && outDTO.getPromotionInfo().getShopId()==0  ){
								if(!platPromotionSet.contains(promotionId)){
									platPromotionSet.add(promotionId);
									PromotionInfo pro = outDTO.getPromotionInfo();
									PromotionDTO dto=promotionBeanCopy(outDTO);
									List<Long> shopList=new ArrayList<Long>();
                                    if(pro.getIsAllItem()==1){//查询哪些店铺参加平台活动
                                    	ExecuteResult<List<ShopDTO>> ext=shopService.findAllKeyinShopInfo();
                                    	for(ShopDTO shopDto:ext.getResult()){
                                    		shopList.add(shopDto.getShopId());
                                    	}
                                    	dto.setShopIdList(shopList);
                                    	shopMap.get(0L).getPromotionList().add(dto);
                                    }else{
                                    	for(PromotionFullReduction full:queryPromotionFrList.getResult().getRows()){
                                    		ExecuteResult<ItemBaseDTO> dg=itemService.getItemBaseInfoById(full.getItemId());
                                        	shopList.add(dg.getResult().getShopId());
                                    	}
                                    	dto.setShopIdList(shopList);
                                    	shopMap.get(0L).getPromotionList().add(dto);
                                    }
								}
							}else{
								//店铺活动
								list.add(promotionBeanCopy(outDTO));
							}
							
						}
					}
				}
				list.addAll(shop.getPromotionList());
				//先对list按照金额进行升序排列
				Collections.sort(list);
				// 对list首尾反转
				Collections.reverse(list);
				shop.setPromotionList(list);
			}
			//对平台的活动进行降序排序
			Collections.sort(shopMap.get(0L).getPromotionList());
			Collections.reverse(shopMap.get(0L).getPromotionList());
			result.setResult(shopMap);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	
	/**
	 * 
	 * <p>Discription:设置商品SKU信息</p>
	 * Created on 2015年3月9日
	 * @param product
	 */
	private boolean setSku(ProductInPriceDTO product){
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
		ExecuteResult<ItemBaseDTO>  ext=itemService.getItemBaseInfoById(product.getItemId());
		if (product.getSellerId() != null )
			dto.setSellerId(Long.valueOf(product.getSellerId()));
		if (product.getUid() != null && !"".equals(product.getUid()))
			dto.setBuyerId(Long.valueOf(product.getUid()));
		
		ExecuteResult<ItemShopCartDTO> er = null;
		if (product.getAddSource() != null && product.getAddSource().intValue() == ItemAddSourceEnum.COMBINATION.getCode()) {
			er = itemService.getCombinationSkuShopCart(dto);
		} else {
			er = itemService.getSkuShopCart(dto);
		}
		
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
						if (itemResult.isSuccess() && itemResult.getResult() != null) {
							ItemDTO itemDTO = itemResult.getResult();
							if(itemDTO.getHasPrice() == null || itemDTO.getHasPrice() != 1){
								product.setUnusualState(11);
								product.getUnusualMsg().add("抱歉！该套装中存在非销售的增值服务，暂时不能购买。");
							}
							// 查询增值服务的sku价格
							ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
							itemShopCartDTO.setAreaCode(product.getRegionId());//省市区编码
							itemShopCartDTO.setSkuId(skuId);//SKU id
							itemShopCartDTO.setQty(1);//数量
							itemShopCartDTO.setShopId(itemDTO.getShopId());//店铺ID
							itemShopCartDTO.setItemId(itemId);//商品ID
							ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO);
							if (skuItem.isSuccess() && skuItem.getResult() != null) {
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
								if (product.isHasPrice() && product.getSkuPrice() != null
										&& valueAddedProduct.isHasPrice() && valueAddedProduct.getSkuPrice() != null) {
									// 重新设置该商品的sku价格
									product.setSkuPrice(product.getSkuPrice().add(valueAddedProduct.getSkuPrice().multiply(BigDecimal.valueOf(itemSkuPackageDTO.getSubNum()))));
									product.setPayPrice(product.getPayPrice().add(valueAddedProduct.getSkuPrice().multiply(BigDecimal.valueOf(itemSkuPackageDTO.getSubNum()))));
								}
								if(valueAddedProduct.isHasPrice() && valueAddedProduct.getSkuPrice() != null){
									// 付款总额
									valueAddedProduct.setPayTotal(valueAddedProduct.getSkuPrice().multiply(BigDecimal.valueOf(valueAddedProduct.getQuantity())).multiply(BigDecimal.valueOf(product.getQuantity())));
								}
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
						ItemDTO subItemDTO = subItemResult.getResult();
						if(subItemDTO.getHasPrice() == null || subItemDTO.getHasPrice() != 1){
							product.setUnusualState(11);
							product.getUnusualMsg().add("抱歉！该套装中存在非销售的组合单品，暂时不能购买。");
						}
						if(subItemDTO.getItemStatus() == null || subItemDTO.getItemStatus() != 4){
							product.setUnusualState(12);
							product.getUnusualMsg().add("抱歉！该套装中存在非在售的组合单品，暂时不能购买。");
						}
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(product.getRegionId());// 省市区编码
						itemShopCartDTO.setSkuId(subSkuId);// skuId
						itemShopCartDTO.setQty(1);// 数量
						itemShopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(subItemId);// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查sku
						if (skuItem.isSuccess() && skuItem.getResult() != null) {
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
							if(subProduct.isHasPrice() && subProduct.getSkuPrice() != null){
								// 付款总额
								subProduct.setPayTotal(subProduct.getSkuPrice().multiply(BigDecimal.valueOf(subProduct.getQuantity()).multiply(BigDecimal.valueOf(product.getQuantity()))));
							}
							subProduct.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
							subProducts.add(subProduct);
						}
					}
				}
				if(subProducts != null && subProducts.size() > 0){
					product.setSubProducts(subProducts);
				}
			}
			//--------------------辅料类型----------------------
			// 辅料商品的ID集合
			Set<Long> auxiliaryItemIds = new HashSet<Long>();
			// 查询组成该套装的辅料类型
			ItemSkuPackageDTO packageDTO = new ItemSkuPackageDTO();
			packageDTO.setPackageItemId(product.getItemId());
			packageDTO.setAddSource(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());
			List<ItemSkuPackageDTO> packageDTOs = itemSkuPackageService.getPackages(packageDTO);
			if(packageDTOs != null && packageDTOs.size() > 0){
				List<ProductInPriceDTO> auxiliaryProducts  = new ArrayList<ProductInPriceDTO>();
				List<ProductInPriceDTO> showAuxiliaryProducts  = new ArrayList<ProductInPriceDTO>();
				for (ItemSkuPackageDTO skuPackageDTO : packageDTOs) {
					Long subItemId = skuPackageDTO.getSubItemId();
					Long subSkuId = skuPackageDTO.getSubSkuId();
					ExecuteResult<ItemDTO> subItemResult = itemService.getItemById(subItemId);
					if (subItemResult.isSuccess() && subItemResult.getResult() != null) {
						ItemDTO subItemDTO = subItemResult.getResult();
						if(subItemDTO.getHasPrice() == null || subItemDTO.getHasPrice() != 1){
							product.setUnusualState(11);
							product.getUnusualMsg().add("抱歉！该套装中存在非销售的辅料类型，暂时不能购买。");
						}
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(product.getRegionId());// 省市区编码
						itemShopCartDTO.setSkuId(subSkuId);// skuId
						itemShopCartDTO.setQty(1);// 数量
						itemShopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(subItemId);// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查sku
						if (skuItem.isSuccess() && skuItem.getResult() != null) {
							// 辅料商品
							ProductInPriceDTO auxiliaryProduct = new ProductInPriceDTO();
							// 商品Id
							auxiliaryProduct.setItemId(subItemId);
							// SkuId
							auxiliaryProduct.setSkuId(subSkuId);
							// 商品状态
							auxiliaryProduct.setStatus(subItemDTO.getItemStatus());
							// 图片
							auxiliaryProduct.setSrc(skuItem.getResult().getSkuPicUrl());
							// 商品名称
							auxiliaryProduct.setTitle(subItemDTO.getItemName());
							// 销售属性
							List<ProductAttrDTO> attrs = new ArrayList<ProductAttrDTO>();
							for(ItemAttr itemAttr : skuItem.getResult().getAttrSales()){
								ProductAttrDTO productAttrDTO = new ProductAttrDTO();
								productAttrDTO.setName(itemAttr.getName());
								productAttrDTO.setValue(itemAttr.getValues().get(0).getName());
								attrs.add(productAttrDTO);
							}
							auxiliaryProduct.setAttrs(attrs);
							// 是否显示价格
							auxiliaryProduct.setHasPrice(skuItem.getResult().isHasPrice());
							// sku价格
							auxiliaryProduct.setSkuPrice(skuItem.getResult().getSkuPrice());
							// 付款单价
							auxiliaryProduct.setPayPrice(skuItem.getResult().getSkuPrice());
							// 数量
							auxiliaryProduct.setQuantity(skuPackageDTO.getSubNum());
							if(auxiliaryProduct.isHasPrice() && auxiliaryProduct.getSkuPrice() != null){
								// 付款总额
								auxiliaryProduct.setPayTotal(auxiliaryProduct.getSkuPrice().multiply(BigDecimal.valueOf(auxiliaryProduct.getQuantity()).multiply(BigDecimal.valueOf(product.getQuantity()))));
							}
							auxiliaryProduct.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
							auxiliaryProducts.add(auxiliaryProduct);
							if(!auxiliaryItemIds.contains(subItemId)){
								showAuxiliaryProducts.add(auxiliaryProduct);
							}
							auxiliaryItemIds.add(subItemId);
						}
					}
				}
				if(auxiliaryProducts != null && auxiliaryProducts.size() > 0){
					product.setAuxiliaryProducts(auxiliaryProducts);
					product.setShowAuxiliaryProducts(showAuxiliaryProducts);
				}
			}
			//--------------------基础服务----------------------
			// 基础服务商品的ID集合
			Set<Long> basicItemIds = new HashSet<Long>();
			// 查询组成该套装的基础服务
			ItemSkuPackageDTO skuPackageDTO = new ItemSkuPackageDTO();
			skuPackageDTO.setPackageItemId(product.getItemId());
			skuPackageDTO.setAddSource(ItemAddSourceEnum.BASICSERVICE.getCode());
			List<ItemSkuPackageDTO> skuPackageDTOs = itemSkuPackageService.getPackages(skuPackageDTO);
			if(skuPackageDTOs != null && skuPackageDTOs.size() > 0){
				List<ProductInPriceDTO> basicProducts  = new ArrayList<ProductInPriceDTO>();
				List<ProductInPriceDTO> showBasicProducts  = new ArrayList<ProductInPriceDTO>();
				for (ItemSkuPackageDTO skuPackage : skuPackageDTOs) {
					Long subItemId = skuPackage.getSubItemId();
					Long subSkuId = skuPackage.getSubSkuId();
					ExecuteResult<ItemDTO> subItemResult = itemService.getItemById(subItemId);
					if (subItemResult.isSuccess() && subItemResult.getResult() != null) {
						ItemDTO subItemDTO = subItemResult.getResult();
						if(subItemDTO.getHasPrice() == null || subItemDTO.getHasPrice() != 1){
							product.setUnusualState(11);
							product.getUnusualMsg().add("抱歉！该套装中存在非销售的基础服务，暂时不能购买。");
						}
						ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
						itemShopCartDTO.setAreaCode(product.getRegionId());// 省市区编码
						itemShopCartDTO.setSkuId(subSkuId);// skuId
						itemShopCartDTO.setQty(1);// 数量
						itemShopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
						itemShopCartDTO.setItemId(subItemId);// 商品ID
						ExecuteResult<ItemShopCartDTO> skuItem = itemService.getSkuShopCart(itemShopCartDTO); // 调商品接口查sku
						if (skuItem.isSuccess() && skuItem.getResult() != null) {
							// 基础服务
							ProductInPriceDTO basicProduct = new ProductInPriceDTO();
							// 商品Id
							basicProduct.setItemId(subItemId);
							// SkuId
							basicProduct.setSkuId(subSkuId);
							// 商品状态
							basicProduct.setStatus(subItemDTO.getItemStatus());
							// 图片
							basicProduct.setSrc(skuItem.getResult().getSkuPicUrl());
							// 商品名称
							basicProduct.setTitle(subItemDTO.getItemName());
							// 销售属性
							List<ProductAttrDTO> attrs = new ArrayList<ProductAttrDTO>();
							for(ItemAttr itemAttr : skuItem.getResult().getAttrSales()){
								ProductAttrDTO productAttrDTO = new ProductAttrDTO();
								productAttrDTO.setName(itemAttr.getName());
								productAttrDTO.setValue(itemAttr.getValues().get(0).getName());
								attrs.add(productAttrDTO);
							}
							basicProduct.setAttrs(attrs);
							// 是否显示价格
							basicProduct.setHasPrice(skuItem.getResult().isHasPrice());
							// sku价格
							basicProduct.setSkuPrice(skuItem.getResult().getSkuPrice());
							// 付款单价
							basicProduct.setPayPrice(skuItem.getResult().getSkuPrice());
							// 数量
							basicProduct.setQuantity(skuPackage.getSubNum());
							if(basicProduct.isHasPrice() && basicProduct.getSkuPrice() != null){
								// 付款总额
								basicProduct.setPayTotal(basicProduct.getSkuPrice().multiply(BigDecimal.valueOf(basicProduct.getQuantity()).multiply(BigDecimal.valueOf(product.getQuantity()))));
							}
							basicProduct.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
							basicProducts.add(basicProduct);
							if(!basicItemIds.contains(subItemId)){
								showBasicProducts.add(basicProduct);
							}
							basicItemIds.add(subItemId);
						}
					}
				}
				if(basicProducts != null && basicProducts.size() > 0){
					product.setBasicProducts(basicProducts);
					product.setShowBasicProducts(showBasicProducts);
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
	
	/*
		对应的类进行copy
	*/
	public static PromotionDTO promotionBeanCopy(PromotionOutDTO outDTO){
		PromotionDTO promotion=new PromotionDTO();
		PromotionInfo promotionInfo = outDTO.getPromotionInfo();
		promotion.setId(promotionInfo.getId());
		promotion.setName(promotionInfo.getActivityName());
		promotion.setType(2);	//满减
		promotion.setPrice(outDTO.getPromotionFullReduction().getDiscountPrice());
		promotion.setMeetPrice(outDTO.getPromotionFullReduction().getMeetPrice());
		promotion.setDiscountPrice(outDTO.getPromotionFullReduction().getDiscountPrice());
		return promotion;
	}
	
	private void getShopInfoByProduct(Map<Long,ShopOutPriceDTO> shopMap,ProductInPriceDTO p){
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
		
		if(p!=null&&p.getPromotions()!=null){
			for (PromotionDTO promotion : p.getPromotions()) {
	            if(promotion.getType() == 1){
	                continue;
	            }
//	            promotionMap.get(shopid).add(promotion.getId());
	            promotionList.add(promotion);
	        }
		}
        p.setPromotions(promotionList);
		shop.getProducts().add(p);
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
	private boolean validShopRule(ShopOutPriceDTO shop){
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
			
	//		if(!canBuy){
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
	//		}
		}
		return canBuy;
	}
	private PromotionDTO setPromotionDTO(Integer type,PromotionInfo promotionInfo,BigDecimal discountPercent){
		PromotionDTO  promotion = new PromotionDTO();
		promotion.setType( type );
		promotion.setId(promotionInfo.getId());
		promotion.setName(promotionInfo.getActivityName());
		promotion.setShopId(promotionInfo.getShopId());
		promotion.setPrice(discountPercent);//折扣价
		return promotion;
	}
}
