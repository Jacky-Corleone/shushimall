package com.camelot.mall.shopcart;

import java.math.BigDecimal;
import java.util.List;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月7日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopCart implements java.io.Serializable {
	private static final long serialVersionUID = -1254351884148520722L;
	private List<Shop> shops;
    private boolean platformActivity;//订单核对页是否显示店铺活动  true:显示平台活动  false：显示店铺活动
	
	public List<Shop> getShops() {
		return shops;
	}
	
	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	/**
	 * 
	 * <p>Discription: 获取购物车中已选商品数量</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getQuantity() {
		Integer quantity = 0;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
				quantity += shop.getQuantity();
			}
		}
		return quantity;
	}
	/**
     * 
     * <p>Discription:[获取购物车中商品优惠金额]</p>
     * Created on 2015-12-15
     * @return
     * @author:[王鹏]
     */
	public BigDecimal getDiscountAmountTotal(){
		BigDecimal total = BigDecimal.ZERO;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
					total = total.add(shop.getDiscountAmountTotal());
			}
		}
		return total;
	}
	/**
	 * <p>Discription: 获取购物车中已选商品总额</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
				total = total.add(shop.getTotal());
			}
		}
		return total;
	}
	

	/**
	 * 
	 * <p>Discription: 获取购物车中已选商品应付总额</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getPayTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
				total = total.add(shop.getPayTotal());
			}
		}
		return total;
	}
	/**
	 * 
	 * <p>Discription:[获取所有订单的商品折扣金额]</p>
	 * Created on 2015-11-20
	 * @return
	 * @author:[王鹏]
	 */
	public BigDecimal getDiscountTotal() {
		BigDecimal total = BigDecimal.ZERO;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
				total = total.add(shop.getDiscountTotal());
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
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
					total = total.add(shop.getOriginalDiscount());
			}
		}
		return total;
	}
	/**
	 * <p>Discription: 购物车商品是否都已选中</p>
	 * Created on 2015年3月23日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public boolean getChecked() {
		boolean checked = false;
		if (shops != null && shops.size() > 0) {
			checked = true;
			for (Shop shop : shops) {
				if (!shop.getChecked()) {
					checked = false;
					break;
				}
			}
		}
		return checked;
	}

	/**
	 * 
	 * <p>Discription: 获取购物车中所选商品运费总额</p>
	 * Created on 2015年3月24日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getTotalFare() {
		BigDecimal total = BigDecimal.ZERO;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
				if (shop.getQuantity() > 0) {
					total = total.add(shop.getFare());
				}
			}
		}
		return total;
	}

	/**
	 * <p>Discription: 获取购物车中所有商品数量</p>
	 * Created on 2015年3月31日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getAllQuantity() {
		Integer quantity = 0;
		for (Shop shop : shops) 
			quantity += shop.getAllQuantity();
		return quantity;
	}


	/**
	 * <p>Discription: 获取购物车中所有商品应付款总额</p>
	 * Created on 2015年3月31日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public BigDecimal getAllTotal(){
		BigDecimal total = BigDecimal.ZERO;
		if (shops != null && shops.size() > 0) {
			for (Shop shop : shops) {
				total = total.add(shop.getAllTotal());
			}
		}
		return total;
	}

	/**
	 * <p>Discription: 获取购物车中已选商品中的异常商品数量</p>
	 * Created on 2015年3月31日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public Integer getUnusualCount(){
		int count = 0;
		for (Shop shop : shops){
			count += shop.getUnusualCount();
		}
		return count;
	}

	public boolean isPlatformActivity() {
		return platformActivity;
	}

	public void setPlatformActivity(boolean platformActivity) {
		this.platformActivity = platformActivity;
	}
	
}
