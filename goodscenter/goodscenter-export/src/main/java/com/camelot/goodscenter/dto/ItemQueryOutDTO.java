package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品信息列表查询的出参DTO]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemQueryOutDTO implements Serializable {
	private static final long serialVersionUID = 5105703741539775929L;
	private java.lang.Long itemId;//  商品id                          
	private java.lang.String itemName;//  商品名称
	private java.lang.String cName;//  所属类目（根据cid获取cname）
	private String keywords; //关键字
	private java.lang.Integer itemStatus;//  商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁
	private BigDecimal marketPrice;// 销售价
	private BigDecimal marketPrice2;//  成本价
	private java.lang.Long productId;//  商品货号
	private java.lang.Integer hasPrice;//  是否有报价：1：有价格；2：暂无报价
	private java.lang.String pictureUrl;//商品的url（根据商品id关联 商品图片信息）
    private java.lang.Long shopId;//店铺id  用来关联店铺信息
    private Integer platLinkStatus;   //商品库状态    1：未符合待入库2：待入库3：已入库4：删除
	private Integer inventory;//库存
	private BigDecimal guidePrice;//商城知道价
    private Integer cid;//类目ID
    private String ad;//广告词
    private Long shopCid; //店铺类目ID
    private Integer addSource;//来源
    private Date created;//创建时间
    private Date modified;// 修改时间
    private Date timingListing;//定时上架时间
    private List<SellPrice> areaPrices;
    
    private String statusChangeReason;//驳回原因
    
    private Integer copied;//1 未加入商品库 2 已加入平台商品库

    private Integer platformId;//平台id,区分绿印平台和科印平台id,科印此字段为空，绿印此字段为2；
    
    private Integer operator;//1商家  2平台
    
    private Long plstItemId;//  平台商品ID，只有add_source为2时值才有意义 平台:非必填；商家:非必填 , add_source为2时必填
    
    private Long shopFreightTemplateId;//运费模版id 平台:必填；商家:必填    
    private BigDecimal volume;//体积 平台:非必填；商家:非必填    
    
    private String passKey;//对itemId加密后的结构
    
    private Integer searched;//商品是否可以被搜索到。1或null：可以被搜索;2：不可以被搜索
    private java.lang.String specification;//商品的url（根据商品id关联 商品图片信息）
    private String housetype;//商品的url（根据商品id关联 商品图片信息）
    private String attrSaleStr;//销售属性   平台:非必填；商家:必填      
    private List<ItemAttr> attrSale;//销售属性 平台:非必填；商家:必填  也可以查询作为查询条件用
    private List<SkuInfo> skuInfos;//SKU列表 平台:非必填；商家:必填
    private String attrSales;
    private String placedTop;
    public String getAd() {
		return ad;
	}

	public void setAd(String ad) {
		this.ad = ad;
	}

	public Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(Integer addSource) {
		this.addSource = addSource;
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

	public Date getTimingListing() {
		return timingListing;
	}

	public void setTimingListing(Date timingListing) {
		this.timingListing = timingListing;
	}

	public Long getShopCid() {
		return shopCid;
	}

	public void setShopCid(Long shopCid) {
		this.shopCid = shopCid;
	}

	public Integer getPlatLinkStatus() {
		return platLinkStatus;
	}

	public void setPlatLinkStatus(Integer platLinkStatus) {
		this.platLinkStatus = platLinkStatus;
	}

	public BigDecimal getGuidePrice() {
		return guidePrice;
	}

	public void setGuidePrice(BigDecimal guidePrice) {
		this.guidePrice = guidePrice;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

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

	public java.lang.String getcName() {
		return cName;
	}

	public void setcName(java.lang.String cName) {
		this.cName = cName;
	}

	public java.lang.Integer getItemStatus() {
		return itemStatus;
	}

	public void setItemStatus(java.lang.Integer itemStatus) {
		this.itemStatus = itemStatus;
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

	public java.lang.Long getProductId() {
		return productId;
	}

	public void setProductId(java.lang.Long productId) {
		this.productId = productId;
	}

	public java.lang.Integer getHasPrice() {
		return hasPrice;
	}

	public void setHasPrice(java.lang.Integer hasPrice) {
		this.hasPrice = hasPrice;
	}

	public java.lang.String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(java.lang.String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public java.lang.Long getShopId() {
		return shopId;
	}

	public void setShopId(java.lang.Long shopId) {
		this.shopId = shopId;
	}

	public List<SellPrice> getAreaPrices() {
		return areaPrices;
	}

	public void setAreaPrices(List<SellPrice> areaPrices) {
		this.areaPrices = areaPrices;
	}

	public Integer getInventory() {
		return inventory;
	}

	public void setInventory(Integer inventory) {
		this.inventory = inventory;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public Integer getCopied() {
		return copied;
	}

	public void setCopied(Integer copied) {
		this.copied = copied;
	}

	public String getStatusChangeReason() {
		return statusChangeReason;
	}

	public void setStatusChangeReason(String statusChangeReason) {
		this.statusChangeReason = statusChangeReason;
	}
	
	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public Integer getOperator() {
		return operator;
	}

	public void setOperator(Integer operator) {
		this.operator = operator;
	}

	public Long getPlstItemId() {
		return plstItemId;
	}

	public void setPlstItemId(Long plstItemId) {
		this.plstItemId = plstItemId;
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

	public String getPassKey() {
		return passKey;
	}

	public void setPassKey(String passKey) {
		this.passKey = passKey;
	}

	public Integer getSearched() {
		return searched;
	}

	public void setSearched(Integer searched) {
		this.searched = searched;
	}

	public java.lang.String getSpecification() {
		return specification;
	}

	public void setSpecification(java.lang.String specification) {
		this.specification = specification;
	}

	public String getHousetype() {
		return housetype;
	}

	public void setHousetype(String housetype) {
		this.housetype = housetype;
	}

	public String getAttrSaleStr() {
		return attrSaleStr;
	}

	public void setAttrSaleStr(String attrSaleStr) {
		this.attrSaleStr = attrSaleStr;
	}

	public List<ItemAttr> getAttrSale() {
		return attrSale;
	}

	public void setAttrSale(List<ItemAttr> attrSale) {
		this.attrSale = attrSale;
	}

	public List<SkuInfo> getSkuInfos() {
		return skuInfos;
	}

	public void setSkuInfos(List<SkuInfo> skuInfos) {
		this.skuInfos = skuInfos;
	}

	public String getAttrSales() {
		return attrSales;
	}

	public void setAttrSales(String attrSales) {
		this.attrSales = attrSales;
	}

	public String getPlacedTop() {
		return placedTop;
	}

	public void setPlacedTop(String placedTop) {
		this.placedTop = placedTop;
	}
}
