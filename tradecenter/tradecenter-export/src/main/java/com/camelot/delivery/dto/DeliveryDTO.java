package com.camelot.delivery.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Description: [物流信息表]
 * </p>
 * Created on 2015年8月10日
 * 
 * @author <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class DeliveryDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7545622323653430674L;
	
	private Integer id;		 //主键ID
	private Long orderItemId;//订单详情ID
	private Long itemId;	 //itemId
	private String orderId;	 //订单id
	private String deliveryNumber;		// 快递号
	private String deliveryCompanyCode; // 快递公司编号
	private String deliveryCompanyName; // 快递公司编号
	private String deliveryRemark;		//备注
	private String isSametemplate;		//是否使用同一运费模板
	private Integer status;	// 订阅快递100订阅状态
	private String message;	// json串
	private Date createTime;// 创建时间
	private Date updateTime;// 更新时间
	private Long shopFreightTemplateId;//模板ID
	private String mobile; //买家电话
	
	private DeliveryCompanyDTO deliveryCompanyDTO;// 快递上信息

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getDeliveryNumber() {
		return deliveryNumber;
	}

	public void setDeliveryNumber(String deliveryNumber) {
		this.deliveryNumber = deliveryNumber;
	}

	public String getDeliveryCompanyCode() {
		return deliveryCompanyCode;
	}

	public void setDeliveryCompanyCode(String deliveryCompanyCode) {
		this.deliveryCompanyCode = deliveryCompanyCode;
	}

	public String getDeliveryRemark() {
		return deliveryRemark;
	}

	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}

	public String getIsSametemplate() {
		return isSametemplate;
	}

	public void setIsSametemplate(String isSametemplate) {
		this.isSametemplate = isSametemplate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getShopFreightTemplateId() {
		return shopFreightTemplateId;
	}

	public void setShopFreightTemplateId(Long shopFreightTemplateId) {
		this.shopFreightTemplateId = shopFreightTemplateId;
	}

	public DeliveryCompanyDTO getDeliveryCompanyDTO() {
		return deliveryCompanyDTO;
	}

	public void setDeliveryCompanyDTO(DeliveryCompanyDTO deliveryCompanyDTO) {
		this.deliveryCompanyDTO = deliveryCompanyDTO;
	}

	public String getDeliveryCompanyName() {
		return deliveryCompanyName;
	}

	public void setDeliveryCompanyName(String deliveryCompanyName) {
		this.deliveryCompanyName = deliveryCompanyName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
