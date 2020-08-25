package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:商品评价查询结果输出DTO类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluationQueryOutDTO implements Serializable {

	private static final long serialVersionUID = -2729081174251794580L;
	private java.lang.Long id;//  id
	private java.lang.Long userId;//  评价人
	private java.lang.Long userShopId;//  评价人店铺id
	private java.lang.Long byUserId;//  被评价人
	private java.lang.Long byShopId;//  被评价店铺id
	private java.lang.String orderId;//  订单ID
	private java.lang.Long itemId;//  商品id
	private java.lang.Long skuId;//  商品sku
	private java.lang.Integer skuScope;// 评分
	private java.lang.String content;//  评价内容
	private java.lang.String type;//1:买家对卖家评价 2:卖家对买家评价  3:售后评价
	private java.util.Date createTime;//  创建时间
	private java.util.Date modifyTime;//  编辑时间
	private String itemName;//商品名称
	private String skuName; //sku名称
	private String resource; //1:默认回复 2:手动回复
	private List<ItemEvaluationReplyDTO> itemEvaluationReplyList; //针对评价的回复
	private List<EvalTag> evalTags; // 评价标签
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.Long getUserId() {
		return userId;
	}
	public void setUserId(java.lang.Long userId) {
		this.userId = userId;
	}
	public java.lang.Long getUserShopId() {
		return userShopId;
	}
	public void setUserShopId(java.lang.Long userShopId) {
		this.userShopId = userShopId;
	}
	public java.lang.Long getByUserId() {
		return byUserId;
	}
	public void setByUserId(java.lang.Long byUserId) {
		this.byUserId = byUserId;
	}
	public java.lang.Long getByShopId() {
		return byShopId;
	}
	public void setByShopId(java.lang.Long byShopId) {
		this.byShopId = byShopId;
	}
	public java.lang.Integer getSkuScope() {
		return skuScope;
	}
	public void setSkuScope(java.lang.Integer skuScope) {
		this.skuScope = skuScope;
	}
	public java.lang.String getType() {
		return type;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.util.Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public List<ItemEvaluationReplyDTO> getItemEvaluationReplyList() {
		return itemEvaluationReplyList;
	}
	public void setItemEvaluationReplyList(
			List<ItemEvaluationReplyDTO> itemEvaluationReplyList) {
		this.itemEvaluationReplyList = itemEvaluationReplyList;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public List<EvalTag> getEvalTags() {
		return evalTags;
	}
	public void setEvalTags(List<EvalTag> evalTags) {
		this.evalTags = evalTags;
	}
}
