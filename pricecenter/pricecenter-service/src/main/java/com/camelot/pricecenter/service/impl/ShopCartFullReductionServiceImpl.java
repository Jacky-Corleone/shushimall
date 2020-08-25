package com.camelot.pricecenter.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartFullReductionService;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.usercenter.dto.UserDTO;
/**
 * 
 * <p>Description: [计算满减后的支付金额]</p>
 * Created on 2015-12-15
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopCartFullReductionService")
public class ShopCartFullReductionServiceImpl implements ShopCartFullReductionService{
	@Resource
	private PromotionService promotionService;
	@Override
	public ExecuteResult<ShopCartDTO> calcFullReduction(ShopCartDTO  cart,String shopPromoId,UserDTO user) {
		 ExecuteResult<ShopCartDTO> res= new ExecuteResult<ShopCartDTO>();
		 int type=1; 
         List<PromotionDTO> PromotionList=new ArrayList<PromotionDTO>();
         BigDecimal addPrice = BigDecimal.ZERO;//最后一次之前的优惠价之和
         //满减活动不为空
         if(StringUtils.isNotBlank(shopPromoId)){
        	 String[] shopPromo = shopPromoId.split(",");
        	 String[] shopIds = new String[shopPromo.length];
        	 String[] promoIds = new String[shopPromo.length];
        	 for (int i = 0; i < shopPromo.length; i++) {
        		 shopIds[i] = shopPromo[i].split("&")[0];
        		 promoIds[i] = shopPromo[i].split("&")[1];
        	 }
         	for(ShopOutPriceDTO shop:cart.getShops()){
         		if(shop.getShopId()==0){
     				PromotionList=shop.getPromotionList();//平台活动
     			}
         		if( shop.getQuantity() > 0 ){
//         		Map<Long, List<Long>> proMap = new HashMap<Long, List<Long>>();
					List<Long> proList = new ArrayList<Long>();
					for (PromotionDTO pro : shop.getPromotionList()) {
						proList.add(pro.getId());
					}
//					proMap.put(shop.getShopId(), proList);
					BigDecimal discount =BigDecimal.ZERO;//一个优惠活动的优惠金额
                    for(int i=0;i<promoIds.length;i++){
                 	   if(!"0".equals(shopIds[i])){//店铺活动
                         if(shop.getShopId()==Long.parseLong(shopIds[i])) {
                        	 if(proList.contains(Long.parseLong(promoIds[i]))){
	                         	for (PromotionDTO pro : shop.getPromotionList()) {
	                                 if (pro.getId() == Long.parseLong(promoIds[i])) {
	                                     discount = pro.getPrice();
	                                 }
	                             }
	                         	 type=1;
	                             addPrice=BigDecimal.ZERO;
	                             Map map = countDscount(shop,null,Long.parseLong(promoIds[i]),discount,type,addPrice,user);
	                             shop=(ShopOutPriceDTO) map.get("shop");
                        	 }else{
                        		 res.addErrorMessage("满减活动不存在或已过期");
                        		 cart.getUnusualMsg().add("满减活动不存在或已过期");
                        	 }
                         }
                 	 }else{   //平台活动
                 		 boolean flag = true;
                 		 for (PromotionDTO pro : PromotionList) {
                              if (pro.getId() == Long.parseLong(promoIds[i])) {
                                  discount = pro.getPrice();
                                  Map map = countDscount(shop,cart,Long.parseLong(promoIds[i]),discount,type,addPrice,user);
                                  type=(Integer) map.get("type");
                                  shop=(ShopOutPriceDTO) map.get("shop");
                                  addPrice=(BigDecimal) map.get("addPrice");
                                  flag = false;
                              }
                          }
                 		 if(flag){
                 			res.addErrorMessage("满减活动不存在或已过期");
                 			cart.getUnusualMsg().add("满减活动不存在或已过期");
                 		 }
                 	 }
			      }       
         	   }
         	}
         }
         res.setResult(cart);
         return res;
	}
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015-12-15
	 * @param shop
	 * @param cart
	 * @param promoIds
	 * @param discount
	 * @param type
	 * @param addPrice
	 * @return
	 * @author:[王鹏]
	 */
    public Map countDscount(ShopOutPriceDTO shop,ShopCartDTO cart,Long promoIds,BigDecimal discount,int type,BigDecimal addPrice,UserDTO user){
        Map maps=new HashMap();
        List<ActivityRecordDTO> lists=new ArrayList<ActivityRecordDTO>();//计算促销活动的记录信息（包括平台促销和店铺促销活动）
        int state = 0;//状态
        BigDecimal itemPrices = BigDecimal.ZERO;;//商品优惠前总金额
        StringBuffer itemIds=new StringBuffer();
        //店铺活动
        if(cart==null){
	        	Map<String,Object> map=queryItemIdsAndPrice(shop,promoIds,state,itemPrices,itemIds,user);
	        	itemPrices=(BigDecimal) map.get("itemPrices");
	        	itemIds=(StringBuffer) map.get("itemIds");
	        	state=(Integer) map.get("state");
	        	if(discount.compareTo(new BigDecimal(0))>0){
	             	//活动记录信息
	        		ActivityRecordDTO dto=createActivityRecordDTO(ActivityTypeEnum.SHOPFULLREDUCTION.getStatus(),discount,promoIds,shop.getShopId());
	             	lists.add(dto);
	            }
        }else{//平台活动  查询所有符合平台活动的商品和价格的集合
		       for (ShopOutPriceDTO shop1 : cart.getShops()) {
		    	    if(shop1.getShopId()!=0){
		    	    	 Map<String,Object> map=queryItemIdsAndPrice(shop1,promoIds,state,itemPrices,itemIds,user);
				         itemPrices=(BigDecimal) map.get("itemPrices");
				         itemIds=(StringBuffer) map.get("itemIds");
				         state=(Integer) map.get("state");
		    	    }
		    	   
		        }
		       if(discount.compareTo(new BigDecimal(0))>0){
		       		//活动记录信息
			       ActivityRecordDTO dto=createActivityRecordDTO(ActivityTypeEnum.PLATFORMFULLREDUCTION.getStatus(),discount,promoIds,shop.getShopId());
		           lists.add(dto);   
	       	}
        }  
        BigDecimal price = BigDecimal.ZERO;//订单子表每个商品的实际支付金额（分摊之后的金额）
        for (ProductInPriceDTO pro : shop.getProducts()) {
            if (pro.getChecked()) {
                BigDecimal averagePrice=BigDecimal.ZERO;//分摊金额
                //计算单品优惠金额按比例平摊
                //判断是否是选中的活动以及活动和店铺对应
		        if(itemIds.indexOf(pro.getItemId().toString())!=-1){
	        		//优惠金额*（单个商品总金额/活动商品总金额）
                    averagePrice = (discount.multiply(pro.getPayTotal().divide(itemPrices, 10, RoundingMode.HALF_UP))).setScale(4,BigDecimal.ROUND_DOWN).setScale(2,BigDecimal.ROUND_UP);
                    if (state>1 && type == state) {//当店铺选中商品一件以上，最后一件优惠=总优惠-前面的优惠金额之和（为防止最后校验不通过）
                    	if(discount.compareTo(addPrice)>=0){
                        	price=pro.getPayTotal().subtract(discount.subtract(addPrice));//当前商品金额-（优惠金额-前面的金额之和）
                    	}else{
                        	price=pro.getPayTotal();
                        }
                    } else {
                    	if(discount.subtract(addPrice).compareTo(BigDecimal.valueOf(0))>0&discount.subtract(addPrice).compareTo(averagePrice)<0){
                    		price=pro.getPayTotal().subtract(discount.subtract(addPrice));
                    		addPrice = addPrice.add(discount.subtract(addPrice));
                    	}else{
                    		price=pro.getPayTotal().subtract(averagePrice);
                    		addPrice = addPrice.add(averagePrice);
                    	}
                        type=type+1;//默认为1，然后循环+1，判断是否是最后一件商品
                    }
                    //插入活动明细信息
                    pro=getTradeOrderItemsDiscountDTO(cart,pro,promoIds,price);
		        }else {
                    price=pro.getPayTotal();
                }
		        pro.setPayTotal(price);//实际支付金额
		        pro.setDiscountAmount(pro.getTotal().subtract(pro.getPayTotal()));//总优惠金额
            }
        }
        for(ActivityRecordDTO ac:shop.getActivityRecordDTOs()){
        	if(ac.getType()==ActivityTypeEnum.PLATFORMMARKDOWN.getStatus()||ac.getType()==ActivityTypeEnum.SHOPMARKDOWN.getStatus()){
        		lists.add(ac);
        	}
        }
        shop.setActivityRecordDTOs(lists);
        maps.put("type", type);
        maps.put("addPrice",addPrice);
        maps.put("shop", shop);
        return maps;
    }
    private Map<String,Object> queryItemIdsAndPrice(ShopOutPriceDTO shop,Long promoIds,int state,BigDecimal itemPrices,StringBuffer itemIds,UserDTO user){
		   Map<String,Object> map=new HashMap<String, Object>();
		   /*Integer userType=null;
		   if(user!=null){
   			if(user.getUsertype()==1){
   				userType=1;
   			}else{
   				userType=2;
   			}
   		}*/
	     PromotionInDTO pid = new PromotionInDTO();
         pid.setPromotionInfoId(promoIds);
         pid.setShopId(shop.getShopId());
         /*pid.setUserType(userType);//用户类型
		 if(user!=null){
        	// 获取成长值，判断用户会员等级
			BigDecimal growthValue = user.getGrowthValue();
			if(growthValue==null){
				growthValue=BigDecimal.ZERO;
			}
			VipLevelEnum vipLevel= VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
			pid.setMembershipLevel(vipLevel.getId().toString());
	      }*/
		 pid.setType(2);
         pid.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
         ExecuteResult<DataGrid<PromotionOutDTO>> er = this.promotionService.getPromotion(pid, null);
		//此循环是将店铺下符合活动的商品金额相加，以算每个单品优惠金额
		if(shop.getProducts()==null){
			return map;
		}
	     for (ProductInPriceDTO pro : shop.getProducts()) {
	         if (er.isSuccess()) {
	             for (PromotionOutDTO prom : er.getResult().getRows()) {
		             	if(prom.getPromotionInfo().getIsAllItem()==2){//部分商品
		             		PromotionFullReduction fr = prom.getPromotionFullReduction();
		                     if(fr.getItemId().longValue() == pro.getItemId().longValue()){
		                         itemIds.append(pro.getItemId()+",");
		                         itemPrices = itemPrices.add(pro.getPayTotal());
		                         state=state+1;
		                     }
		             	}else{//全部商品
		             		 itemIds.append(pro.getItemId()+",");
		                     itemPrices = itemPrices.add(pro.getPayTotal());
		                     state=state+1;
		             	}
	             }
	         }
	}  
     map.put("state", state);
     map.put("itemPrices", itemPrices);
     map.put("itemIds", itemIds);
     return map;
	}
    /**
	 * 
	 * <p>Discription:[获取最优的促销活动id]</p>
	 * Created on 2015-12-15
	 * @param myCart
	 * @author:[王鹏]
	 */
	public Map<String,ShopCartDTO> findShopPromotionId(ShopCartDTO myCart){
		List<PromotionDTO> lists=new ArrayList<PromotionDTO>();
		Map<Long,BigDecimal> shopMap =new HashMap<Long, BigDecimal>();
		Map<Long,BigDecimal> map =new HashMap<Long, BigDecimal>();
		Map<String,ShopCartDTO> hashMap=new HashMap<String, ShopCartDTO>();
		for(ShopOutPriceDTO shop:myCart.getShops()){
			if(shop.getShopId()!=0){
				if(shop.getPromotionList()!=null&&shop.getPromotionList().size()>0){
					PromotionDTO storePromotion=shop.getPromotionList().get(0);//店铺优惠活动
					BigDecimal price=storePromotion.getPrice();//店铺优惠活动
					shopMap.put(shop.getShopId(),price);
			    }
		     }
		}
		for(ShopOutPriceDTO shop:myCart.getShops()){
			if(shop.getShopId()==0){
				map=getAmount(shopMap,shop);
				lists=shop.getPromotionList();
			}
		}
		BigDecimal amount=BigDecimal.valueOf(0);
		for (Long shopId : shopMap.keySet()) {
			amount = amount.add(shopMap.get(shopId));
		   }
		       map.put(0L, amount);//所有店铺优惠总金额之和
		     //排序
		       map=sortByComparator(map);
		       
		      hashMap= this.findPromotionId(myCart,map,lists); 
      return hashMap;
	}
	
	private Map<String,ShopCartDTO> findPromotionId(ShopCartDTO myCart,Map<Long,BigDecimal> map,List<PromotionDTO> lists){
		Map<String,ShopCartDTO> hashMap=new HashMap<String, ShopCartDTO>();
		String shopPromotionId="";
		 outer:for(ShopOutPriceDTO shop:myCart.getShops()){
			if(!shop.getPromotionList().isEmpty()&&shop.getPromotionList().size()>0){
				Iterator it = map.entrySet().iterator();
				   while (it.hasNext()) {
				    Map.Entry entry = (Map.Entry)it.next();
				    Long id=(Long) entry.getKey();
				    BigDecimal money=(BigDecimal) entry.getValue();
				    if(id==0){//只显示店铺活动
				    	if(shop.getShopId()!=0){
				    		shop.setShopActivity("true");
					    	shopPromotionId+=shop.getShopId()+"&"+shop.getPromotionList().get(0).getId()+",";
				    	}
				    }else{
				    	for(PromotionDTO pro:lists){
				    		if(pro.getId()==id){
				    			if(pro.getShopIdList().contains(shop.getShopId())){
					    			continue outer;
					    		}
					    		shop.setShopActivity("true");
						    	if(shop.getShopId()==0){
						    		shopPromotionId=shop.getShopId()+"&"+id+",";
						    		myCart.setPlatPromotionId(id);//平台活动id
						    		myCart.setPlatformActivity("true");
						    	}else{
							    	shopPromotionId+=shop.getShopId()+"&"+shop.getPromotionList().get(0).getId()+",";
						    	}
				    		}
				    	}
				      }
		          }
			  }
		}
		hashMap.put(shopPromotionId, myCart);
		return hashMap;
	}
	//shopMap  <店铺id,最优金额>
	private Map getAmount(Map<Long,BigDecimal> shopMap,ShopOutPriceDTO shop){
		//<平台活动id,金额>
		Map<Long,BigDecimal> proBigDecimalMap=new HashMap<Long, BigDecimal>();
		//循环平台活动
		for (PromotionDTO pro : shop.getPromotionList()) {
			BigDecimal proAmount=pro.getPrice();
			List<Long> proShopList = pro.getShopIdList();
			for (Long shopId : shopMap.keySet()) {
				if(proShopList.contains(shopId)){
					continue;
				}
				proAmount = proAmount.add(shopMap.get(shopId));
			}
			proBigDecimalMap.put(pro.getId(), proAmount);
		}
		return proBigDecimalMap;
	}
    /**
     * 
     * <p>Discription:[对map集合的value值进行降序排列]</p>
     * Created on 2016-1-6
     * @param unsortMap
     * @return
     */
	private static Map sortByComparator(Map unsortMap) {
        List list = new LinkedList(unsortMap.entrySet());
        Collections.sort(list, new Comparator() 
        {
             public int compare(Object o1, Object o2) 
             {
               return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
             }
         });
          Map sortedMap = new LinkedHashMap();
          for (Iterator it = list.iterator(); it.hasNext();) {
			    Map.Entry entry = (Map.Entry)it.next();
			    sortedMap.put(entry.getKey(), entry.getValue());
			    break;
           }
             return sortedMap;
    }
	/**
	 * 
	 * <p>Discription:[创建活动记录信息]</p>
	 * Created on 2016-1-6
	 * @return
	 */
	private ActivityRecordDTO  createActivityRecordDTO(Integer type,BigDecimal discount,Long promoIds,Long shopId){
		//活动记录信息
     	ActivityRecordDTO dto=new ActivityRecordDTO();
     	dto.setType(type);
     	dto.setDiscountAmount(discount);
     	dto.setPromotionId(promoIds.toString());
     	dto.setShopId(shopId);
     	return dto;
	}
	/**
	 * 
	 * <p>Discription:[创建活动明细信息]</p>
	 * Created on 2016-1-6
	 * @param cart
	 * @param pro
	 * @param promoIds
	 * @return
	 */
	private ProductInPriceDTO getTradeOrderItemsDiscountDTO(ShopCartDTO cart,ProductInPriceDTO pro,Long promoIds,BigDecimal price){
		//活动明细存在（包括直降信息）
		if(pro.getTradeOrderItemsDiscountDTO()!=null){
			pro.getTradeOrderItemsDiscountDTO().setFullReductionId(promoIds);
	        if(cart==null){
	        	pro.getTradeOrderItemsDiscountDTO().setFullReductionType(ActivityTypeEnum.SHOPFULLREDUCTION.getStatus());
	        }else{
	        	pro.getTradeOrderItemsDiscountDTO().setFullReductionType(ActivityTypeEnum.PLATFORMFULLREDUCTION.getStatus());
	        }
	        if(pro.getTradeOrderItemsDiscountDTO().getMarkdownDiscount().compareTo(BigDecimal.valueOf(0))>0){
	        	pro.getTradeOrderItemsDiscountDTO().setFullReductionDiscount(pro.getTotal().subtract(price).subtract(pro.getTradeOrderItemsDiscountDTO().getMarkdownDiscount()));
	        }else{
	        	pro.getTradeOrderItemsDiscountDTO().setFullReductionDiscount(pro.getTotal().subtract(price));
	        }
		}else{
			TradeOrderItemsDiscountDTO tradeOrderItemsDiscountDTO=new TradeOrderItemsDiscountDTO();
			tradeOrderItemsDiscountDTO.setFullReductionId(promoIds);
	        if(cart==null){
	        	tradeOrderItemsDiscountDTO.setFullReductionType(ActivityTypeEnum.SHOPFULLREDUCTION.getStatus());
	        }else{
	        	tradeOrderItemsDiscountDTO.setFullReductionType(ActivityTypeEnum.PLATFORMFULLREDUCTION.getStatus());
	        }
	        tradeOrderItemsDiscountDTO.setFullReductionDiscount(pro.getTotal().subtract(price));
            pro.setTradeOrderItemsDiscountDTO(tradeOrderItemsDiscountDTO);
		}
		return pro;
	}
}