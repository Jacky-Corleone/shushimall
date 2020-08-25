package com.camelot.goodscenter.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品信息domain类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class Item implements Serializable {

	private static final long serialVersionUID = 2671677765195120413L;
	
	private java.lang.Long itemId;//  商品id                          
	private java.lang.String itemName;//  商品名称
	private java.lang.Long cid;//  类目ID
	private java.lang.Long sellerId;//  商家ID
	private java.lang.Integer itemStatus;//  商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁
	private java.lang.String attributes;//  商品类目属性keyId:valueId
	private java.lang.String attrSale;//  商品销售属性keyId:valueId
	private java.util.Date created;//  创建日期
	private java.util.Date modified;//  修改日期
	private java.lang.Long shopCid;//  商品所属店铺类目id
	private java.lang.Long brand;//  品牌
	private BigDecimal marketPrice;//  市场价
	private BigDecimal marketPrice2;//  成本价
	private java.lang.Integer inventory;//  库存量
	private BigDecimal weight;//  商品毛重
	private String weightUnit;//  商品毛重
	private java.lang.Long productId;//  商品货号
	private java.lang.String describeUrl;//  商品描述url，存在jfs中
	private java.lang.String packingList;//  包装清单
	private java.lang.String afterService;//  售后服务
	private java.lang.String ad;//  广告词
	private java.util.Date timingListing;//  定时上架，为空则表示未设置定时上架
	private java.util.Date listtingTime;//  上架时间
	private java.util.Date delistingTime;//  下架时间
	private java.lang.Integer operator;//  操作方，1：商家，2：平台
	private java.lang.String statusChangeReason;//  平台方下架或锁定或审核驳回时给出的理由
	private java.lang.Long shopId;//  店铺ID
	private BigDecimal guidePrice;//  商城指导价
	private java.lang.String origin;//  商品产地
	private java.lang.Integer addSource;//  来源：1自定义添加2：从平台商品库选择
	private java.lang.Integer platLinkStatus;//  与平台商品库关联状态：1：未符合待入库2：待入库3：已入库4：删除
	private java.lang.Integer hasPrice;//  是否有报价：1：有价格；2：暂无报价
	private java.lang.Long plstItemId;//  平台商品ID，只有add_source为2时值才有意义
	private String keywords;//关键字
	private Integer copied;//1 未加入商品库 2 已加入平台商品库
	private String authentication;//认证信息
	private Integer platformId;//平台id,区分绿印平台和科印平台id,科印此字段为空，绿印此字段为2；
	private Long shopFreightTemplateId;//运费模版id 平台:必填；商家:必填
	private BigDecimal volume;//体积 平台:非必填；商家:非必填
	private Integer placedTop;//商品是否置顶 1:是 2:否
	private Integer searched;//商品是否可以被搜索到。1或null：可以被搜索;2：不可以被搜索
	private String housetype; //户型11 一室一厅 21 两室一厅
	private java.lang.String specification; //规格参数
	public java.lang.Long getItemId() {
		return itemId;
	}

	public void setItemId(java.lang.Long itemId) {
		this.itemId = itemId;
	}

	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.lang.Long getCid() {
		return cid;
	}

	public void setCid(java.lang.Long cid) {
		this.cid = cid;
	}

	public java.lang.Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(java.lang.Integer itemStatus) {
		this.itemStatus = itemStatus;
	}

	public java.lang.String getAttributes() {
		return attributes;
	}

	public void setAttributes(java.lang.String attributes) {
		this.attributes = attributes;
	}

	public java.util.Date getCreated() {
		return created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}

	public java.util.Date getModified() {
		return modified;
	}

	public void setModified(java.util.Date modified) {
		this.modified = modified;
	}

	public java.lang.Long getShopCid() {
		return shopCid;
	}

	public void setShopCid(java.lang.Long shopCid) {
		this.shopCid = shopCid;
	}

	public java.lang.Long getBrand() {
		return brand;
	}

	public void setBrand(java.lang.Long brand) {
		this.brand = brand;
	}

	public BigDecimal getMarketPrice() {
		return marketPrice;
	}

	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}

	public BigDecimal getMarketPrice2() {
		return marketPrice2;
	}

	public void setMarketPrice2(BigDecimal marketPrice2) {
		this.marketPrice2 = marketPrice2;
	}

	public java.lang.Integer getInventory() {
		return inventory;
	}

	public void setInventory(java.lang.Integer inventory) {
		this.inventory = inventory;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public java.lang.Long getProductId() {
		return productId;
	}

	public void setProductId(java.lang.Long productId) {
		this.productId = productId;
	}

	public java.lang.String getDescribeUrl() {
		return describeUrl;
	}

	public void setDescribeUrl(java.lang.String describeUrl) {
		this.describeUrl = describeUrl;
	}

	public java.lang.String getPackingList() {
		return packingList;
	}

	public void setPackingList(java.lang.String packingList) {
		this.packingList = packingList;
	}

	public java.lang.String getAfterService() {
		return afterService;
	}

	public void setAfterService(java.lang.String afterService) {
		this.afterService = afterService;
	}

	public java.lang.String getAd() {
		return ad;
	}

	public void setAd(java.lang.String ad) {
		this.ad = ad;
	}

	public java.util.Date getTimingListing() {
		return timingListing;
	}

	public void setTimingListing(java.util.Date timingListing) {
		this.timingListing = timingListing;
	}

	public java.util.Date getListtingTime() {
		return listtingTime;
	}

	public void setListtingTime(java.util.Date listtingTime) {
		this.listtingTime = listtingTime;
	}

	public java.util.Date getDelistingTime() {
		return delistingTime;
	}

	public void setDelistingTime(java.util.Date delistingTime) {
		this.delistingTime = delistingTime;
	}

	public java.lang.Integer getOperator() {
		return operator;
	}

	public void setOperator(java.lang.Integer operator) {
		this.operator = operator;
	}

	public java.lang.String getStatusChangeReason() {
		return statusChangeReason;
	}

	public void setStatusChangeReason(java.lang.String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}

	public java.lang.Long getShopId() {
		return shopId;
	}

	public void setShopId(java.lang.Long shopId) {
		this.shopId = shopId;
	}

	public BigDecimal getGuidePrice() {
		return guidePrice;
	}

	public void setGuidePrice(BigDecimal guidePrice) {
		this.guidePrice = guidePrice;
	}

	public java.lang.String getOrigin() {
		return origin;
	}

	public void setOrigin(java.lang.String origin) {
		this.origin = origin;
	}

	public java.lang.Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(java.lang.Integer addSource) {
		this.addSource = addSource;
	}

	public java.lang.Integer getPlatLinkStatus() {
		return platLinkStatus;
	}

	public void setPlatLinkStatus(java.lang.Integer platLinkStatus) {
		this.platLinkStatus = platLinkStatus;
	}

	public java.lang.Integer getHasPrice() {
		return hasPrice;
	}

	public void setHasPrice(java.lang.Integer hasPrice) {
		this.hasPrice = hasPrice;
	}

	public java.lang.Long getPlstItemId() {
		return plstItemId;
	}

	public void setPlstItemId(java.lang.Long plstItemId) {
		this.plstItemId = plstItemId;
	}

	public java.lang.Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(java.lang.Long sellerId) {
		this.sellerId = sellerId;
	}

	public java.lang.String getAttrSale() {
		return attrSale;
	}

	public void setAttrSale(java.lang.String attrSale) {
		this.attrSale = attrSale;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getWeightUnit() {
		return weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public Integer getCopied() {
		return copied;
	}

	public void setCopied(Integer copied) {
		this.copied = copied;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Long getShopFreightTemplateId() {
		return shopFreightTemplateId;
	}

	public void setShopFreightTemplateId(Long shopFreightTemplateId) {
		this.shopFreightTemplateId = shopFreightTemplateId;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	public Integer getSearched() {
		return searched;
	}

	public void setSearched(Integer searched) {
		this.searched = searched;
	}

	public String getHousetype() {
		return housetype;
	}

	public void setHousetype(String housetype) {
		this.housetype = housetype;
	}

	public java.lang.String getSpecification() {
		return specification;
	}

	public void setSpecification(java.lang.String specification) {
		this.specification = specification;
	}

	public Integer getPlacedTop() {
		return placedTop;
	}

	public void setPlacedTop(Integer placedTop) {
		this.placedTop = placedTop;
	}
}
