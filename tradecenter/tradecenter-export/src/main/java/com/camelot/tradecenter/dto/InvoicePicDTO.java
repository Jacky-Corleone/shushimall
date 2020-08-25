package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <p>
 * Description: [增值税发票对应的图片]
 * </p>
 * Created on 2015年9月10日
 * 
 * @author <a href="mailto: xxx@camelotchina.com">宋文斌</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class InvoicePicDTO implements Serializable {

	private static final long serialVersionUID = -825510315914028295L;

	private Long id;// 主键
	private Long invoiceId;// 发票ID
	private Integer picType;// 图片类型：1营业执照，2税务登记证，3一般纳税人证明
	private String picUrl;// 图片地址
	private Date createTime;// 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getPicType() {
		return picType;
	}

	public void setPicType(Integer picType) {
		this.picType = picType;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
