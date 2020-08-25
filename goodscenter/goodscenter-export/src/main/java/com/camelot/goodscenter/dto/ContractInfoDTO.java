package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ContractInfoDTO implements Serializable {

    /**
     * <p>Discription:[商品SKU协议主信息]</p>
     * Created on 2015年6月08日
     *
     * @author:鲁鹏
     */
    private static final long serialVersionUID = -6251858618435829940L;
    private Long id;//主键
    private String contractNo;//合同编号
    private String contractOrderNo;//协议序号
    private String contractName;//合同名称
    private Integer printerId;//印刷厂用户主键
    private Integer supplierId;//供货方主键
    private Date beginDate;//合同有效期--开始
    private Date endDate;//合同有效期--结束
    private String payType;//支付约定 支付类型
    private String invoiceFlag;//发票状态 0-开票 1-不开票
    private String invoiceType;//发票类型
    private String invoiceName;//发票抬头
    private String deliverMethod;//配送方式
    private String remark;//备注
    private String confirmBy;//确认人
    private Date confirmDate;//确认时间
    private String approveBy;//审批人
    private Date approveDate;//审批时间
    private String status;//合同状态 0：未提交 1：待审核  2：审核驳回3 ：待确认 4：确认驳回 5：待生效  6：协议生效 7：需要审批  8：协议生效（正在修改） 9：协议过期 10：协议终止
    private Integer createRole;//创建者角色（卖0、买1）
    private String createBy;//
    private Date createDate;//
    private String updateBy;//
    private Date updateDate;//
    private String activeFlag;//有效标记 0-有效 1-无效
    private String loginId;
    private String itemName;
    private List<String> printerIdList;
    private List<String> supplierIdList;
    private List<Map> contractMatDTOs;
    private String annex;//附件
    private String updatedContractNo;
    private String refusalReason;
    private List<Long> userList;//主子帐号集合
    private String protocolType;//协议类型

    public String getRefusalReason() {
        return refusalReason;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public String getAnnex() {
        return annex;
    }

    public void setAnnex(String annex) {
        this.annex = annex;
    }

    public List<Map> getContractMatDTOs() {
        return contractMatDTOs;
    }

    public void setContractMatDTOs(List<Map> contractMatDTOs) {
        this.contractMatDTOs = contractMatDTOs;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<String> getPrinterIdList() {
        return printerIdList;
    }

    public void setPrinterIdList(List<String> printerIdList) {
        this.printerIdList = printerIdList;
    }

    public List<String> getSupplierIdList() {
        return supplierIdList;
    }

    public void setSupplierIdList(List<String> supplierIdList) {
        this.supplierIdList = supplierIdList;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getInvoiceFlag() {
        return invoiceFlag;
    }

    public void setInvoiceFlag(String invoiceFlag) {
        this.invoiceFlag = invoiceFlag;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getDeliverMethod() {
        return deliverMethod;
    }

    public void setDeliverMethod(String deliverMethod) {
        this.deliverMethod = deliverMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public void setConfirmBy(String confirmBy) {
        this.confirmBy = confirmBy;
    }

    public Date getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(Date confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(String approveBy) {
        this.approveBy = approveBy;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreateRole() {
        return createRole;
    }

    public void setCreateRole(Integer createRole) {
        this.createRole = createRole;
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

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }


    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getContractOrderNo() {
        return contractOrderNo;
    }

    public void setContractOrderNo(String contractOrderNo) {
        this.contractOrderNo = contractOrderNo;
    }

    public String getUpdatedContractNo() {
        return updatedContractNo;
    }

    public void setUpdatedContractNo(String updatedContractNo) {
        this.updatedContractNo = updatedContractNo;
    }

	public List<Long> getUserList() {
		return userList;
	}

	public void setUserList(List<Long> userList) {
		this.userList = userList;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}
	
	
    
}
