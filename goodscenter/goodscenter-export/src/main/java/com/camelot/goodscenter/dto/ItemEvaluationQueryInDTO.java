package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>
 * Description: [描述该类概要功能介绍:商品评价查询条件DTO类]
 * </p>
 * Created on 2015年2月3日
 * 
 * @author <a href="mailto: zhangzhiqiang34@camelotchina.com">chenx</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemEvaluationQueryInDTO implements Serializable {

	private static final long serialVersionUID = 7431348243222270865L;
	private String contentEmpty;// 有无评价内容 0:无 1:有
	private String itemName;// 商品名称
	private Long itemId;// 商品编码
	private Long skuId;// sku编码
	private Long[] userIds;// 评价人
	private Date beginTime; // 开始评价时间
	private Date endTime;// 结束评价时间
	private String orderId;// 订单编号
	private String scopeLevel;// 评价级别 1:好评 2:中评 3:差评
	private String type;// 类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
	private Long byUserId;// 被评价人
	private Long byShopId;// 被评价店铺
	private Integer scope;// 商品评分
	private Long beginScope;// 起始评分 包含
	private Long endScope;// 结束评分 包含
	private String reply = "0"; // 是否包含回复信息 0:不包含 1：包含 默认为不包含
	private String resource; // 1:默认回复 2:手动回复
	private Long shopCid;// 店铺分类
	private Integer hasShow; // 是否有晒图

	public String getContentEmpty() {
		return contentEmpty;
	}

	public void setContentEmpty(String contentEmpty) {
		this.contentEmpty = contentEmpty;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public Long[] getUserIds() {
		return userIds;
	}

	public void setUserIds(Long[] userIds) {
		this.userIds = userIds;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getScopeLevel() {
		return scopeLevel;
	}

	public void setScopeLevel(String scopeLevel) {
		this.scopeLevel = scopeLevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getByUserId() {
		return byUserId;
	}

	public void setByUserId(Long byUserId) {
		this.byUserId = byUserId;
	}

	public Long getByShopId() {
		return byShopId;
	}

	public void setByShopId(Long byShopId) {
		this.byShopId = byShopId;
	}

	public Integer getScope() {
		return scope;
	}

	public void setScope(Integer scope) {
		this.scope = scope;
	}

	public Long getBeginScope() {
		return beginScope;
	}

	public void setBeginScope(Long beginScope) {
		this.beginScope = beginScope;
	}

	public Long getEndScope() {
		return endScope;
	}

	public void setEndScope(Long endScope) {
		this.endScope = endScope;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public Long getShopCid() {
		return shopCid;
	}

	public void setShopCid(Long shopCid) {
		this.shopCid = shopCid;
	}

	public Integer getHasShow() {
		return hasShow;
	}

	public void setHasShow(Integer hasShow) {
		this.hasShow = hasShow;
	}

}
