package com.camelot.pricecenter.dto.outdto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopOutPriceDTO implements Serializable{
	private static final long serialVersionUID = -3449192420335972461L;
	private Long shopId; // 店铺ID
	private Long sellerId; // 卖家ID
	private BigDecimal fare = BigDecimal.ZERO; // 运费
	private String shopTitle; // 店铺名称
	private String shopRule; // 店铺营销规则 起批、混批说明
	private List<ProductInPriceDTO> products; // 店铺商品
	private Map<Long, List<ProductInPriceDTO>> groupProducts; // 商品组，使用同一运费模版的商品为一组。key=运费模版ID
	private Map<Long, BigDecimal> groupFreight; // 运费组，使用同一运费模版的商品为一组。key=运费模版ID
	private Map<Long, List<DeliveryTypeFreightDTO>> groupDeliveryTypes; // 运送方式组，运费模版对应的所有运送方式。key=运费模版ID
	private List<PromotionDTO> promotionList=new ArrayList<PromotionDTO>();	//促销活动
	private String stationId; // IM
	private List<ActivityRecordDTO> activityRecordDTOs=new ArrayList<ActivityRecordDTO>();//活动记录信息集合
	private Integer integral;//店铺使用的积分
	private BigDecimal exchangeRate;// 一个积分兑换的钱数
	private String shopActivity;//订单核对页是否显示店铺活动  
	
	private List<CouponsDTO> couponsList;

	public List<PromotionDTO> getPromotionList() {
		return promotionList;
	}

	public void setPromotionList(List<PromotionDTO> promotionList) {
		this.promotionList = promotionList;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public BigDecimal getFare() {
		return fare;
	}

	public void setFare(BigDecimal fare) {
		this.fare = fare;
	}

	public String getShopTitle() {
		return shopTitle;
	}

	public void setShopTitle(String shopTitle) {
		this.shopTitle = shopTitle;
	}

	public String getShopRule() {
		return shopRule;
	}

	public void setShopRule(String shopRule) {
		this.shopRule = shopRule;
	}
	
	public List<ProductInPriceDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductInPriceDTO> products) {
		this.products = products;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	/**
	 * <p>
	 * Discription: 获取店铺已选中商品数量
	 * </p>
	 * Created on 2015年3月23日
	 * 
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getQuantity() {
		Integer quantity = 0;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					quantity += p.getQuantity();
			}
		}
		return quantity;
	}
    /**
     * 
     * <p>Discription:[获取店铺商品优惠金额]</p>
     * Created on 2015-12-15
     * @return
     * @author:[王鹏]
     */
	public BigDecimal getDiscountAmountTotal(){
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					total = total.add(p.getDiscountAmount());
			}
		}
		return total;
	}
	
	/**
	 * <p>
	 * Discription: 获取店铺已选中商品总额
	 * </p>
	 * Created on 2015年3月23日
	 * 
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					total = total.add(p.getTotal());
			}
		}
		return total;
	}
	
	/**
	 * <p>
	 * Discription: 获取店铺已选中商品应付总额
	 * </p>
	 * Created on 2015年3月23日
	 * 
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getPayTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					total = total.add(p.getPayTotal());
			}
		}
		return total;
	}
	/**
	 * 
	 * <p>Discription:[商品的vip优惠活动]</p>
	 * Created on 2015-11-20
	 * @return
	 * @author:[创建者中文名字]
	 */
	public BigDecimal getDiscountTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					total = total.add(p.getDiscountTotal());
			}
		}
		return total;
	}
	/**
	 * 
	 * <p>Discription:[余额充足条件下商品折扣金额]</p>
	 * Created on 2015-11-20
	 * @return
	 * @author:[创建者中文名字]
	 */
	public BigDecimal getOriginalDiscount() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					total = total.add(p.getOriginalDiscount());
			}
		}
		return total;
	}
	/**
	 * <p>
	 * Discription: 获取店铺是否为选中状态
	 * </p>
	 * Created on 2015年3月23日
	 * 
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public boolean getChecked() {
		boolean checked = true;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (!p.getChecked() && p.getStatus() == 4 && p.getQty() > 0) {
					checked = false;
					break;
				}
			}
		}
		return checked;
	}

	/**
	 * <p>
	 * Discription: 获取店铺所有商品数量
	 * </p>
	 * Created on 2015年3月31日
	 * 
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getAllQuantity() {
		Integer quantity = 0;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products)
				quantity += p.getQuantity();
		}
		return quantity;
	}

	/**
	 * <p>
	 * Discription: 获取店铺所有商品应付款总额
	 * </p>
	 * Created on 2015年3月31日
	 * 
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getAllTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products)
				total = total.add(p.getPayTotal());
		}
		return total;
	}

	/**
	 * @desc 获得购物车店铺已选商品种类
	 * @return
	 */
	public Integer getTypeCount() {
		Integer quantity = 0;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked())
					++quantity;
			}
		}
		return quantity;
	}

	/**
	 * <P>
	 * Discription: 获取已选商品店铺异常商品数量
	 * </p>
	 * 
	 * @return
	 */
	public Integer getUnusualCount() {
		int count = 0;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked() && p.getUnusualState() != null)
					count++;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * <p>Discription:[获取店铺积分总优惠金额]</p>
	 * Created on 2015年12月17日
	 * @return
	 * @author:[宋文斌]
	 */
	public BigDecimal getIntegralDiscount() {
		BigDecimal integralDiscount = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (ProductInPriceDTO p : products) {
				if (p.getChecked()) {
					integralDiscount = integralDiscount.add(p.getIntegralDiscount());
				}
			}
		}
		return integralDiscount;
	}

	public List<ActivityRecordDTO> getActivityRecordDTOs() {
		return activityRecordDTOs;
	}

	public void setActivityRecordDTOs(List<ActivityRecordDTO> activityRecordDTOs) {
		this.activityRecordDTOs = activityRecordDTOs;
	}

	public Map<Long, List<ProductInPriceDTO>> getGroupProducts() {
		return groupProducts;
	}

	public void setGroupProducts(Map<Long, List<ProductInPriceDTO>> groupProducts) {
		this.groupProducts = groupProducts;
	}

	public Map<Long, BigDecimal> getGroupFreight() {
		return groupFreight;
	}

	public void setGroupFreight(Map<Long, BigDecimal> groupFreight) {
		this.groupFreight = groupFreight;
	}

	public Map<Long, List<DeliveryTypeFreightDTO>> getGroupDeliveryTypes() {
		return groupDeliveryTypes;
	}

	public void setGroupDeliveryTypes(
			Map<Long, List<DeliveryTypeFreightDTO>> groupDeliveryTypes) {
		this.groupDeliveryTypes = groupDeliveryTypes;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getShopActivity() {
		return shopActivity;
	}

	public void setShopActivity(String shopActivity) {
		this.shopActivity = shopActivity;
	}

	public List<CouponsDTO> getCouponsList() {
		return couponsList;
	}

	public void setCouponsList(List<CouponsDTO> couponsList) {
		this.couponsList = couponsList;
	}
	
}
