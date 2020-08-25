package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品信息的DTO]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemBaseDTO implements Serializable {
	private static final long serialVersionUID = 2464215588527124822L;
	private Long itemId;//  商品id              
	private Long sellerId;//  商家ID  平台:非必填；商家:必填      
	private Long shopCid;//  商品所属店铺类目id  平台:非必填；商家:必填      
	private String itemName;//  商品名称   平台:必填；商家:必填     
	private String ad;//  广告词   平台:非必填；商家:非必填      
	private Long cid;//  类目ID  平台:必填；商家:必填      
	private String cName;//类目名称
	private Long brand;//  品牌  平台:必填；商家:必填      
	private String brandName;//品牌名称  查询用  
	private Integer hasPrice;//  是否有报价：1：有价格；2：暂无报价  平台:必填；商家:必填      
	private Long productId;//  商品货号  平台:必填；商家:必填      
	private BigDecimal marketPrice;//  市场价  平台:非必填；商家:必填      
	private BigDecimal marketPrice2;//  成本价  平台:非必填；商家:必填      
	private Integer inventory;//  库存量  平台:非必填；商家:必填      
	private BigDecimal weight;//  商品毛重  平台:非必填；商家:必填      
	private String weightUnit;//重量单位 
	
	private String describeUrl;//  商品描述url，存在jfs中  平台:必填；商家:必填      
	private String packingList;//  包装清单  平台:非必填；商家:必填      
	private String afterService;//  售后服务  平台:非必填；商家:必填      
	private Date timingListing;//  定时上架，为空则表示未设置定时上架   平台:非必填；商家:非必填      
	private Integer addSource;//  来源：1自定义添加2：从平台商品库选择  平台:非必填；商家:必填      
	private Long plstItemId;//  平台商品ID，只有add_source为2时值才有意义 平台:非必填；商家:非必填 , add_source为2时必填    
	private Integer itemStatus;// 平台:必填；商家:必填       商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁
	
	private Date created;//  创建日期     
	private Date modified;//  修改日期  
	
	private Long shopId;//  店铺ID  平台:非必填；商家:必填      
	
	
	private String attributesStr;//  非销售属性  平台:非必填；商家:必填      
	private String attrSaleStr;//销售属性   平台:非必填；商家:必填      
	
	
	private java.util.Date listtingTime;//  上架时间         
	private java.util.Date delistingTime;//  下架时间        
	private java.lang.Integer operator;//  操作方，1：商家，2：平台 平台:必填；商家:必填      
	private java.lang.String statusChangeReason;//  平台方下架或锁定或审核驳回时给出的理由  
	private BigDecimal guidePrice;//  报价
	private java.lang.String origin;//  商品产地  平台:非必填；商家:必填      
	private java.lang.Integer platLinkStatus;// 平台:必填；商家:非必填  与平台商品库关联状态：1：未符合待入库2：待入库3：已入库4：删除       
	private String keywords;//关键字
	private Integer copied;//1 未加入商品库 2 已加入平台商品库
	private String authentication;//认证信息
	private Integer platformId;//平台id,区分绿印平台和科印平台id,科印此字段为空，绿印此字段为2；
	private String timingList;//前台接收定时上架，精确到时分秒
	private Long shopFreightTemplateId;//运费模版id 平台:必填；商家:必填
	private BigDecimal volume;//体积 平台:非必填；商家:非必填
	
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

	public java.lang.Integer getPlatLinkStatus() {
		return platLinkStatus;
	}

	public void setPlatLinkStatus(java.lang.Integer platLinkStatus) {
		this.platLinkStatus = platLinkStatus;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getSellerId() {
		return sellerId;
	}

	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}

	public Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
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

	public Long getShopCid() {
		return shopCid;
	}

	public void setShopCid(Long shopCid) {
		this.shopCid = shopCid;
	}

	public Long getBrand() {
		return brand;
	}

	public void setBrand(Long brand) {
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

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getDescribeUrl() {
		return describeUrl;
	}

	public void setDescribeUrl(String describeUrl) {
		this.describeUrl = describeUrl;
	}

	public String getPackingList() {
		return packingList;
	}

	public void setPackingList(String packingList) {
		this.packingList = packingList;
	}

	public String getAfterService() {
		return afterService;
	}

	public void setAfterService(String afterService) {
		this.afterService = afterService;
	}

	public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public Date getTimingListing() {
		return timingListing;
	}

	public void setTimingListing(Date timingListing) {
		this.timingListing = timingListing;
	}


	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(Integer addSource) {
		this.addSource = addSource;
	}

	public Integer getHasPrice() {
		return hasPrice;
	}

	public void setHasPrice(Integer hasPrice) {
		this.hasPrice = hasPrice;
	}

	public Long getPlstItemId() {
		return plstItemId;
	}

	public void setPlstItemId(Long plstItemId) {
		this.plstItemId = plstItemId;
	}

	public String getAttributesStr() {
		return attributesStr;
	}

	public void setAttributesStr(String attributesStr) {
		this.attributesStr = attributesStr;
	}

	public String getAttrSaleStr() {
		return attrSaleStr;
	}

	public void setAttrSaleStr(String attrSaleStr) {
		this.attrSaleStr = attrSaleStr;
	}
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getcName() {
		return cName;
	}

	public void setcName(String cName) {
		this.cName = cName;
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
	public String getTimingList() {
		return timingList;
	}

	public void setTimingList(String timingList) {
		this.timingList = timingList;
	}
	
	
}
