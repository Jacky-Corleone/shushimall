package com.camelot.mall.shopcart;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class Shop implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 239720720234441284L;
	private Long shopId; // 店铺ID
	private Long sellerId; // 卖家ID
	private BigDecimal fare; // 运费
	private String shopTitle; // 店铺名称
	private String shopRule; // 店铺营销规则 起批、混批说明
	private List<Product> products; // 店铺商品
	private Map<Long, List<Product>> groupProducts; // 商品组，使用同一运费模版的商品为一组。key=运费模版ID
	private Map<Long, BigDecimal> groupFreight; // 运费组，使用同一运费模版的商品为一组。key=运费模版ID
	private Map<Long, List<ShopDeliveryType>> groupDeliveryTypes; // 运送方式组，运费模版对应的所有运送方式。key=运费模版ID

	private String stationId; // IM
	
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
	
	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * <p>Discription: 获取店铺已选中商品数量</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getQuantity() {
		Integer quantity = 0;
		if (products != null && products.size() > 0) {
			for (Product p : products) {
				if (p.getChecked())
					quantity += p.getQuantity();
			}
		}
		return quantity;
	}

	/**
	 * <p>Discription: 获取店铺已选中商品总额</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (Product p : products) {
				if (p.getChecked())
					total = total.add(p.getTotal());
			}
		}
		return total;
	}

	/**
	 * <p>Discription: 获取店铺已选中商品应付总额</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getPayTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (products != null && products.size() > 0) {
			for (Product p : products) {
				if (p.getChecked())
					total = total.add(p.getPayTotal());
			}
		}
		return total;
	}

	/**
	 * <p>Discription: 获取店铺是否为选中状态</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public boolean getChecked() {
		boolean checked = true;
		if (products != null && products.size() > 0) {
			for (Product p : products) {
				if (!p.getChecked()) {
					checked = false;
					break;
				}
			}
		}
		return checked;
	}

	/**
	 * <p>Discription: 获取店铺所有商品数量</p>
	 * Created on 2015年3月31日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getAllQuantity() {
		Integer quantity = 0;
		if (products != null && products.size() > 0) {
			for (Product p : products) 
				quantity += p.getQuantity();
		}
		return quantity;
	}
	
	/**
	 * @desc 获得购物车店铺已选商品种类
	 * @return
	 */
	public Integer getTypeCount(){
		Integer quantity = 0;
		if (products != null && products.size() > 0) {
			for (Product p : products){
				if( p.getChecked() )
					++quantity;
			}
		}
		return quantity;
	}
	
	/**
	 * <P>Discription: 获取已选商品店铺异常商品数量</p>
	 * @return
	 */
	public Integer getUnusualCount(){
		int count = 0;
		if (products != null && products.size() > 0) {
			for (Product p : products){
				if( p.getUnusualState() != null )
					count++;
			}
		}
		return count;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public Map<Long, List<Product>> getGroupProducts() {
		return groupProducts;
	}

	public void setGroupProducts(Map<Long, List<Product>> groupProducts) {
		this.groupProducts = groupProducts;
	}

	public Map<Long, BigDecimal> getGroupFreight() {
		return groupFreight;
	}

	public void setGroupFreight(Map<Long, BigDecimal> groupFreight) {
		this.groupFreight = groupFreight;
	}

	public Map<Long, List<ShopDeliveryType>> getGroupDeliveryTypes() {
		return groupDeliveryTypes;
	}

	public void setGroupDeliveryTypes(Map<Long, List<ShopDeliveryType>> groupDeliveryTypes) {
		this.groupDeliveryTypes = groupDeliveryTypes;
	}
	
	
	
}
