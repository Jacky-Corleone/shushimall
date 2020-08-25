package com.camelot.tradecenter.dto;

import java.io.Serializable;

/**
 * 
 * <p>
 * Description: [订单附件表]
 * </p>
 * Created on 2016年2月17日
 * 
 * @author <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class TradeOrdersEnclosureDTO implements Serializable {

	private static final long serialVersionUID = -825510315914024355L;
	private long id;// 附件id
	private String orderId;// 订单id
	private String enclosureName;// 附件名称
	private String enclosureUrl;// 附件地址
	private long isImg;
	private long type;//附件类型 1:普通房型图 2:房型设计图
	private String remark;//备注
	private long isDelete;// 是否删除 0 未删除 1 已删除

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIsImg() {
		return isImg;
	}

	public void setIsImg(long isImg) {
		this.isImg = isImg;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getEnclosureName() {
		return enclosureName;
	}

	public void setEnclosureName(String enclosureName) {
		this.enclosureName = enclosureName;
	}

	public String getEnclosureUrl() {
		return enclosureUrl;
	}

	public void setEnclosureUrl(String enclosureUrl) {
		this.enclosureUrl = enclosureUrl;
	}

	public long getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(long isDelete) {
		this.isDelete = isDelete;
	}

}
