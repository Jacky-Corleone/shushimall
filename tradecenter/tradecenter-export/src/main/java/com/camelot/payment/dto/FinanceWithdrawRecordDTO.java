package com.camelot.payment.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.camelot.common.enums.WithdrawEnums;

/**
 * 提现记录日志DTO
 * @author 周乐
 */
public class FinanceWithdrawRecordDTO implements Serializable{
	private static final long serialVersionUID = -8186800578803943820L;
	
	private Long id;			//日志id
	private String tradeNo;		//交易号
	private Long userId;		//用户id
	private BigDecimal amount;	//申请提现的金额
	private Integer type;		//记录类型  1：买家付款账户 2：买家金融账户 3：卖家提现账户
	private Integer status;		//10：提现处理中  20：提现申请失败 30 提现申请成功   40 提现失败  50 提现成功  60 未知 70 审核拒绝 80 用户撤销
	private String statusText;		//10：提现处理中  20：提现失败 30 提现成功  
	private Date createdTime;	//创建时间
	private Date modifiedTime;	//更新时间
	
	private String createdBegin;	//创建开始时间
	private String createdEnd;	//创建结束时间
	
	private List<Long> userIds;
	private String userName;
	private String failReason;//失败原因
	private Integer[] recordStatus;//提现状态数组
	
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
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public Integer[] getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Integer[] recordStatus) {
		this.recordStatus = recordStatus;
	}
	
}
