package com.camelot.tradecenter.dto;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;

public class MonthlyStatementDTO  implements Serializable {

	private Long id;//
	private String statementId;
	private String tradeName;//交易名称
	private String orderCode;//订单号
	private Integer orderStates;//订单状态
	private Double paidAmount;//已付金额
	private Double npaidAmount;//未付金额
	private Double amount;//总金额
	private String createBy;//创建人
	private Date createDate;//创建时间
	private String updateBy;//修改人
	private Date updateDate;//修改时间
	private Integer activeFlag;//有效标记
	private String alternateField1;//买家创建 与 卖家创建 的对账单标识  2-买家创建 3-卖家创建
// setter and getter

	public Long getId() {
		return id;
	}

	public String getStatementId() {
		return statementId;
	}

	public void setStatementId(String statementId) {
		this.statementId = statementId;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getTradeName() {
		return tradeName;
	}

	public void setTradeName(String tradeName) {
		this.tradeName = tradeName;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getOrderStates() {
		return orderStates;
	}

	public void setOrderStates(Integer orderStates) {
		this.orderStates = orderStates;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Double getNpaidAmount() {
		return npaidAmount;
	}

	public void setNpaidAmount(Double npaidAmount) {
		this.npaidAmount = npaidAmount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(Integer activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String getAlternateField1() {
		return alternateField1;
	}

	public void setAlternateField1(String alternateField1) {
		this.alternateField1 = alternateField1;
	}
	
}
