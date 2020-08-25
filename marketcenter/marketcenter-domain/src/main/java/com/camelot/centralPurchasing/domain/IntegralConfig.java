package com.camelot.centralPurchasing.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IntegralConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	//主键
	private Long configId;
	//类型
	private Integer integralType;
	//起价
	private BigDecimal startPrice;
	//止价
	private BigDecimal endPrice;
	//获得积分数
	private BigDecimal getIntegral;
	//使用积分数
	private Long useIntegral;
	//一个积分兑换的钱数
	private BigDecimal exchangeRate;
	//状态
	private Integer state;
	//平台类型
	private Integer platformId;
	//插入时间
	private Date insertTime;
	//插入人
	private Integer insertBy;
	//更新时间
	private Date updateTime;
	//更新人
	private Integer updateBy;
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public Integer getIntegralType() {
		return integralType;
	}
	public void setIntegralType(Integer integralType) {
		this.integralType = integralType;
	}
	public BigDecimal getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(BigDecimal startPrice) {
		this.startPrice = startPrice;
	}
	public BigDecimal getEndPrice() {
		return endPrice;
	}
	public void setEndPrice(BigDecimal endPrice) {
		this.endPrice = endPrice;
	}
	public BigDecimal getGetIntegral() {
		return getIntegral;
	}
	public void setGetIntegral(BigDecimal getIntegral) {
		this.getIntegral = getIntegral;
	}
	public Long getUseIntegral() {
		return useIntegral;
	}
	public void setUseIntegral(Long useIntegral) {
		this.useIntegral = useIntegral;
	}
	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Integer getInsertBy() {
		return insertBy;
	}
	public void setInsertBy(Integer insertBy) {
		this.insertBy = insertBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Integer updateBy) {
		this.updateBy = updateBy;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	
}
