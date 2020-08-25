package com.camelot.payment.dto;

import java.io.Serializable;
import java.util.Date;

import com.camelot.common.enums.WithdrawEnums;

/**
 *  提现申请对象
 * 
 * @Description -
 * @createDate - 2015-3-24
 */
public class FinanceWithdrawApplyDTO implements Serializable{
	
	private static final long serialVersionUID = -5494590135686139672L;
	private Long id;//
	private String tradeNo;//申请单号
	private Long userId;//用户id
	private String userName;//用户名
	private java.math.BigDecimal amount;//申请金额
	private String content;//审批理由
	private Integer status;//申请状态, 1：待审核 2不通过 10：提现处理中  20：提现失败 30 提现成功  
	private String statusText;
	private Date createdTime;// 创建时间
	private Date modifiedTime;// 修改时间
	
	private String createdBegin;	//创建开始时间
	private String createdEnd;	//创建结束时间
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public java.math.BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		WithdrawEnums withdrawEnum=WithdrawEnums.getEnumByCode(status);
		if(withdrawEnum!=null){
			this.statusText=withdrawEnum.getLabel();
		}
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public String getCreatedBegin() {
		return createdBegin;
	}
	public void setCreatedBegin(String createdBegin) {
		this.createdBegin = createdBegin;
	}
	public String getCreatedEnd() {
		return createdEnd;
	}
	public void setCreatedEnd(String createdEnd) {
		this.createdEnd = createdEnd;
	}
	
}