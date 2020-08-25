package com.camelot.maketcenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 集采详情
 * @author 周志军
 *
 */
public class SignUpRefPurchasingDetail implements Serializable {

	private static final long serialVersionUID = 1L;

	//主键
    private Long purchaseRefEnterpriseId;
	//参与集采单位ID
    private Long enterpriseId;
	//集采商品ID
    private Long activitesDetailsId;
	//预期采购数量
    private Integer enterpriseEstimateNum;
	//预期采购价格
    private BigDecimal enterpriseEstimatePrice;
	//插入时间
    private Date insertTime;
	//插入人
    private Long insertBy;
	//更新时间
    private Date updateTime;
	//更新人
    private Long updateBy;
	public Long getPurchaseRefEnterpriseId() {
		return purchaseRefEnterpriseId;
	}
	public void setPurchaseRefEnterpriseId(Long purchaseRefEnterpriseId) {
		this.purchaseRefEnterpriseId = purchaseRefEnterpriseId;
	}
	public Long getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public Long getActivitesDetailsId() {
		return activitesDetailsId;
	}
	public void setActivitesDetailsId(Long activitesDetailsId) {
		this.activitesDetailsId = activitesDetailsId;
	}
	public Integer getEnterpriseEstimateNum() {
		return enterpriseEstimateNum;
	}
	public void setEnterpriseEstimateNum(Integer enterpriseEstimateNum) {
		this.enterpriseEstimateNum = enterpriseEstimateNum;
	}
	public BigDecimal getEnterpriseEstimatePrice() {
		return enterpriseEstimatePrice;
	}
	public void setEnterpriseEstimatePrice(BigDecimal enterpriseEstimatePrice) {
		this.enterpriseEstimatePrice = enterpriseEstimatePrice;
	}
	public Date getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}
	public Long getInsertBy() {
		return insertBy;
	}
	public void setInsertBy(Long insertBy) {
		this.insertBy = insertBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
}
