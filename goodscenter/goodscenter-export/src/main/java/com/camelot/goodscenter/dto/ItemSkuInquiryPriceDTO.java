package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemSkuInquiryPriceDTO implements Serializable {

	/**
	 * <p>
	 * Discription:[商品SKU询价]
	 * </p>
	 */
	private static final long serialVersionUID = 5431858618435829940L;
	private Long id;// 主键
	private Long buyerId;// 买家ID
	private Long sellerId;// 卖家ID
	private Long shopId;// 店铺ID
	private Long itemId;// 商品ID
	private String itemName; // 商品名称
	private String pictureUrl; // 商品的图片
	private Long skuId;// SKU id
	private Integer inquiryQty;// 询价数量
	private BigDecimal inquiryPrice;// 价格
	private Date startTime;// 价格执行开始时间
	private Date endTime;// 价格执行结束时间
	private String comment;// 备注
	private String inquiryRemark;// 卖家中心备注
	private Date created;// 创建时间
	private Date modified;// 结束时间
	private String email;// 邮箱
	private String cellphone;// 手机
	private List<SkuPictureDTO> skuPics = new ArrayList<SkuPictureDTO>();// SKU图片

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCellphone() {
		return cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getInquiryQty() {
		return inquiryQty;
	}

	public void setInquiryQty(Integer inquiryQty) {
		this.inquiryQty = inquiryQty;
	}

	public BigDecimal getInquiryPrice() {
		return inquiryPrice;
	}

	public void setInquiryPrice(BigDecimal inquiryPrice) {
		this.inquiryPrice = inquiryPrice;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public List<SkuPictureDTO> getSkuPics() {
		return skuPics;
	}

	public void setSkuPics(List<SkuPictureDTO> skuPics) {
		this.skuPics = skuPics;
	}

	public String getInquiryRemark() {
		return inquiryRemark;
	}

	public void setInquiryRemark(String inquiryRemark) {
		this.inquiryRemark = inquiryRemark;
	}

}
