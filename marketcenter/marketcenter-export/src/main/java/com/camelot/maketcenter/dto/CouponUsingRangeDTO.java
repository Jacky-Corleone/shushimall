package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.util.List;

/**
 * <p>Description: [优惠券使用范围表]</p>
 * Created on 2015年12月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CouponUsingRangeDTO implements Serializable {
	private static final long serialVersionUID = 1l;
	private Long id;// 主键
	private String couponId;// 优惠券编号
	// 优惠券使用范围id,根据优惠券类型不同，此字段含义不同，
	// 全平台优惠券：没有记录，店铺优惠券：存店铺ID，类目优惠券：类目id，sku优惠券：skuId
	private Long couponUsingId;
	private String remark;//备注信息

	private String itemName;//商品名称
	private String categoryNme;//类目名称
	private String skuAttr;//销售属性
	private String itemId;//产品编号
	private String price;//价格
	
	private List<Long> skuIdList;//skuid 集合
	private List<Long> cIdList;//类名id集合
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}
	
	public Long getCouponUsingId() {
		return couponUsingId;
	}

	public void setCouponUsingId(Long couponUsingId) {
		this.couponUsingId = couponUsingId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getCategoryNme() {
		return categoryNme;
	}

	public void setCategoryNme(String categoryNme) {
		this.categoryNme = categoryNme;
	}

	public String getSkuAttr() {
		return skuAttr;
	}

	public void setSkuAttr(String skuAttr) {
		this.skuAttr = skuAttr;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<Long> getSkuIdList() {
		return skuIdList;
	}

	public void setSkuIdList(List<Long> skuIdList) {
		this.skuIdList = skuIdList;
	}

	public List<Long> getcIdList() {
		return cIdList;
	}

	public void setcIdList(List<Long> cIdList) {
		this.cIdList = cIdList;
	}

}
