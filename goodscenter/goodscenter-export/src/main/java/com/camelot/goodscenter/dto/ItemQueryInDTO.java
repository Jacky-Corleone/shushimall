package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:查询商品信息列表的的入参DTO]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemQueryInDTO implements Serializable {
	private static final long serialVersionUID = -6339767973191891159L;
	
	private Integer id;  //平台商品编码
	private java.lang.String itemName;//  商品名称
	private Integer itemId;//  商品名称
	private java.lang.Long cid;//  类目ID
	private Long[] cids;//  类目ID
	private java.lang.Integer itemStatus;//  商品状态,1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁
	private java.lang.Long productId;//  商品货号
	private java.lang.Long skuId;//sku编码（关联sku编码）
	private java.lang.Long[] shopIds;//店铺id组
	private java.lang.Integer operator;//操作方，1：商家，2：平台
    private Integer platLinkStatus;   //商品库状态    1：未符合待入库2：待入库3：已入库4：删除
	
    private Long shopCid;//店铺分类ID
    private Integer minInvetory;//最小库存
    private Integer maxInvetory;//最大库存
    private BigDecimal minPrice;//最小价格
    private BigDecimal maxPrice;//最大价格
    
    private Long platItemId;//平台商品ID 
    private Integer hasPrice;//  是否有报价：1：有价格；2：暂无报价  平台:必填；商家:必填
    
    private Date startTime;//开始时间
    private Date endTime;//结束时间
    private List<Integer> itemStatusList = new ArrayList<Integer>();//商品状态列表
    private List<Long> brandIdList = new ArrayList<Long>();//品牌ID组
    
    private Integer copied;//1 未加入商品库 2 已加入平台商品库
    private String xgjFlag;//微信小管家查询有效标志
    private Integer platformId;//平台id,区分绿印平台和科印平台id,科印此字段为空，绿印此字段为2；
    private Long shopFreightTemplateId;// 商品运费模版Id
    private Integer addSource;//  来源：1：自定义添加,3：套装商品
    private List<Long> itemIds; //item的id集合
    private Integer searched;//商品是否可以被搜索到。1或null：可以被搜索;2：不可以被搜索
    private java.lang.String specification;//商品的url（根据商品id关联 商品图片信息）
    private String housetype;//商品的url（根据商品id关联 商品图片信息）
    private Integer placedTop; // 是否置顶（1：是， 2和其他：不是）

	public String getXgjFlag() {
		return xgjFlag;
	}

	public void setXgjFlag(String xgjFlag) {
		this.xgjFlag = xgjFlag;
	}

	public List<Long> getBrandIdList() {
		return brandIdList;
	}

	public void setBrandIdList(List<Long> brandIdList) {
		this.brandIdList = brandIdList;
	}

	public Integer getPlatLinkStatus() {
		return platLinkStatus;
	}

	public Long[] getCids() {
		return cids;
	}

	public void setCids(Long[] cids) {
		this.cids = cids;
	}

	public void setPlatLinkStatus(Integer platLinkStatus) {
		this.platLinkStatus = platLinkStatus;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public java.lang.Long getProductId() {
		return productId;
	}

	public void setProductId(java.lang.Long productId) {
		this.productId = productId;
	}

	public java.lang.Long getSkuId() {
		return skuId;
	}

	public void setSkuId(java.lang.Long skuId) {
		this.skuId = skuId;
	}

	public java.lang.Long[] getShopIds() {
		return shopIds;
	}

	public void setShopIds(java.lang.Long[] shopIds) {
		this.shopIds = shopIds;
	}

	public java.lang.Integer getOperator() {
		return operator;
	}

	public void setOperator(java.lang.Integer operator) {
		this.operator = operator;
	}

	public Long getShopCid() {
		return shopCid;
	}

	public void setShopCid(Long shopCid) {
		this.shopCid = shopCid;
	}

	public Integer getMinInvetory() {
		return minInvetory;
	}

	public void setMinInvetory(Integer minInvetory) {
		this.minInvetory = minInvetory;
	}

	public Integer getMaxInvetory() {
		return maxInvetory;
	}

	public void setMaxInvetory(Integer maxInvetory) {
		this.maxInvetory = maxInvetory;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Long getPlatItemId() {
		return platItemId;
	}

	public void setPlatItemId(Long platItemId) {
		this.platItemId = platItemId;
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

	public List<Integer> getItemStatusList() {
		return itemStatusList;
	}

	public void setItemStatusList(List<Integer> itemStatusList) {
		this.itemStatusList = itemStatusList;
	}

	public Integer getCopied() {
		return copied;
	}

	public void setCopied(Integer copied) {
		this.copied = copied;
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

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAddSource() {
		return addSource;
	}

	public void setAddSource(Integer addSource) {
		this.addSource = addSource;
	}

	public List<Long> getItemIds() {
		return itemIds;
	}

	public void setItemIds(List<Long> itemIds) {
		this.itemIds = itemIds;
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

	public Integer getPlacedTop() {
		return placedTop;
	}

	public void setPlacedTop(Integer placedTop) {
		this.placedTop = placedTop;
	}

	public Integer getHasPrice() {
		return hasPrice;
	}

	public void setHasPrice(Integer hasPrice) {
		this.hasPrice = hasPrice;
	}
	
}
