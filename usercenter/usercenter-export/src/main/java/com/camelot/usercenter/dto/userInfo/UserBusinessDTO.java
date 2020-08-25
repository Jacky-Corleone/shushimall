package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;
import java.util.Date;

import com.camelot.usercenter.enums.UserEnums;

/**
 * 用户营业执照
 *
 * @author - learrings
 * @Description -
 * @createDate - 2015-3-4
 */
public class UserBusinessDTO implements Serializable {

    private static final long serialVersionUID = 5600103525483427863L;

    private java.lang.String companyName;//  公司名称
    private java.lang.String artificialPersonName;//  法定代表人名称
    private java.lang.String businessLicenceId;//  注册号（营业执照号）
    private String idCardType; 	//证件类型（新增）
    private String idCardNum; 	//证件号码（原：身份证号）
    private String businessLicenceAddress; //营业执照所在地(省市县)
    private String businessLicencAddressDetail;// 营业执照所在地详细地址
    private Date businessLicenceDate;//营业执照成立日期
    private java.lang.String artificialPersonPicSrc;//  法人身份证电子版图片地址
    private java.lang.String artificialPersonPicBackSrc;//  法人身份证电子版图片地址(反面)
    private java.lang.String registeredCapital;//  注册资金
    private java.lang.String businessLicencePicSrc;//  营业执照副本电子版
    private java.lang.String businessScope;//  经营范围
    private java.util.Date businessLicenceIndate;//  营业执照有效期
    private java.lang.String linkman;//  联系人
    private java.lang.String linkmanPhone;//  联系人手机
    private java.lang.String companyAddress;//  公司地址
    private java.lang.String companyPhone;//  公司电话
    private java.lang.Integer businessStatus;//   营业执照审核状态 0驳回/1待确认/2确认
    private java.lang.String businessStatusLabel;//   营业执照审核状态 0驳回/1待确认/2确认
    private Integer neverExpires = 0;//   是否永久有效 2是/1否/0无操作
    private String auditRemark; // 审核备注 (驳回原因)

    private String companyDeclinedAddress; //公司详细地址
    private UserEnums.CompanyPeopleNum companyPeoNum; //公司人数
    private UserEnums.CompanyQualt companyQualt; //公司性质
    private UserEnums.BusinessScale businessScale; //经营规模
    private String isFinancing; //是否融资需求 1 是 0 否
    private String financingNum;//融资金额
    private Integer isSanzheng=0; //是否三证合一
    private String unifiedCreditCode;//统一信用代码
    //绿印需要添加的字段
    private String gpProductCertification;//绿色印刷环境标志产品认证证书
    private String gpPrintBusinessLicense;//印刷经营许可证
    private String gpPublicationLicense;//出版物印制许可证（书刊印刷定点企业证书）
    private String gpCommitmentBook;//承诺书
    private String gpQualityManagementCertification;//ISO9001质量管理体系认证证书
    private String gpEnvironmentalManagementCertification;//ISO14001环境管理体系认证证书

    
    public UserEnums.BusinessScale getBusinessScale() {
        return businessScale;
    }

    public String getIdCardNum() {
        return idCardNum;
    }

    public void setIdCardNum(String idCardNum) {
        this.idCardNum = idCardNum;
    }

    public String getBusinessLicenceAddress() {
        return businessLicenceAddress;
    }

    public void setBusinessLicenceAddress(String businessLicenceAddress) {
        this.businessLicenceAddress = businessLicenceAddress;
    }

    public String getBusinessLicencAddressDetail() {
        return businessLicencAddressDetail;
    }

    public void setBusinessLicencAddressDetail(String businessLicencAddressDetail) {
        this.businessLicencAddressDetail = businessLicencAddressDetail;
    }

    public Date getBusinessLicenceDate() {
        return businessLicenceDate;
    }

    public void setBusinessLicenceDate(Date businessLicenceDate) {
        this.businessLicenceDate = businessLicenceDate;
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone;
    }

    public void setBusinessScale(UserEnums.BusinessScale businessScale) {
        this.businessScale = businessScale;
    }

    public String getIsFinancing() {
        return isFinancing;
    }

    public void setIsFinancing(String isFinancing) {
        this.isFinancing = isFinancing;
    }

    public String getFinancingNum() {
        return financingNum;
    }

    public void setFinancingNum(String financingNum) {
        this.financingNum = financingNum;
    }

    public String getCompanyDeclinedAddress() {
        return companyDeclinedAddress;
    }

    public void setCompanyDeclinedAddress(String companyDeclinedAddress) {
        this.companyDeclinedAddress = companyDeclinedAddress;
    }

    public void setBusinessStatusLabel(String businessStatusLabel) {
        this.businessStatusLabel = businessStatusLabel;
    }


    public UserEnums.CompanyPeopleNum getCompanyPeoNum() {
        return companyPeoNum;
    }

    public void setCompanyPeoNum(UserEnums.CompanyPeopleNum companyPeoNum) {
        this.companyPeoNum = companyPeoNum;
    }


    public UserEnums.CompanyQualt getCompanyQualt() {
        return companyQualt;
    }

    public void setCompanyQualt(UserEnums.CompanyQualt companyQualt) {
        this.companyQualt = companyQualt;
    }


    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    public java.lang.String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(java.lang.String companyName) {
        this.companyName = companyName;
    }

    public java.lang.String getArtificialPersonName() {
        return artificialPersonName;
    }

    public void setArtificialPersonName(java.lang.String artificialPersonName) {
        this.artificialPersonName = artificialPersonName;
    }

    public java.lang.String getBusinessLicenceId() {
        return businessLicenceId;
    }

    public void setBusinessLicenceId(java.lang.String businessLicenceId) {
        this.businessLicenceId = businessLicenceId;
    }

    public java.lang.String getArtificialPersonPicSrc() {
        return artificialPersonPicSrc;
    }

    public void setArtificialPersonPicSrc(java.lang.String artificialPersonPicSrc) {
        this.artificialPersonPicSrc = artificialPersonPicSrc;
    }

    public java.lang.String getArtificialPersonPicBackSrc() {
        return artificialPersonPicBackSrc;
    }

    public void setArtificialPersonPicBackSrc(
            java.lang.String artificialPersonPicBackSrc) {
        this.artificialPersonPicBackSrc = artificialPersonPicBackSrc;
    }

    public java.lang.String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(java.lang.String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public java.lang.String getBusinessLicencePicSrc() {
        return businessLicencePicSrc;
    }

    public void setBusinessLicencePicSrc(java.lang.String businessLicencePicSrc) {
        this.businessLicencePicSrc = businessLicencePicSrc;
    }

    public java.lang.String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(java.lang.String businessScope) {
        this.businessScope = businessScope;
    }

    public java.util.Date getBusinessLicenceIndate() {
        return businessLicenceIndate;
    }

    public void setBusinessLicenceIndate(java.util.Date businessLicenceIndate) {
        this.businessLicenceIndate = businessLicenceIndate;
    }

    public java.lang.String getLinkman() {
        return linkman;
    }

    public void setLinkman(java.lang.String linkman) {
        this.linkman = linkman;
    }

    public java.lang.String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(java.lang.String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public java.lang.String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(java.lang.String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public java.lang.Integer getBusinessStatus() {
        return businessStatus;
    }

    public String getBusinessStatusLabel() {
        return businessStatusLabel;
    }

    public void setBusinessStatus(java.lang.Integer businessStatus) {
        UserEnums.UserAuditStatus comStatus = UserEnums.UserAuditStatus.getEnumByOrdinal(businessStatus);
        if (comStatus != null) {
            this.businessStatusLabel = comStatus.getLabel().toString();
        }
        this.businessStatus = businessStatus;
    }

    public Integer getNeverExpires() {
        return neverExpires;
    }

    public void setNeverExpires(Integer neverExpires) {
        this.neverExpires = neverExpires;
    }

	public String getGpProductCertification() {
		return gpProductCertification;
	}

	public void setGpProductCertification(String gpProductCertification) {
		this.gpProductCertification = gpProductCertification;
	}

	public String getGpPrintBusinessLicense() {
		return gpPrintBusinessLicense;
	}

	public void setGpPrintBusinessLicense(String gpPrintBusinessLicense) {
		this.gpPrintBusinessLicense = gpPrintBusinessLicense;
	}

	public String getGpPublicationLicense() {
		return gpPublicationLicense;
	}

	public void setGpPublicationLicense(String gpPublicationLicense) {
		this.gpPublicationLicense = gpPublicationLicense;
	}

	public String getGpCommitmentBook() {
		return gpCommitmentBook;
	}

	public void setGpCommitmentBook(String gpCommitmentBook) {
		this.gpCommitmentBook = gpCommitmentBook;
	}

	public String getGpQualityManagementCertification() {
		return gpQualityManagementCertification;
	}

	public void setGpQualityManagementCertification(
			String gpQualityManagementCertification) {
		this.gpQualityManagementCertification = gpQualityManagementCertification;
	}

	public String getGpEnvironmentalManagementCertification() {
		return gpEnvironmentalManagementCertification;
	}

	public void setGpEnvironmentalManagementCertification(
			String gpEnvironmentalManagementCertification) {
		this.gpEnvironmentalManagementCertification = gpEnvironmentalManagementCertification;
	}

	public String getIdCardType() {
		return idCardType;
	}

	public void setIdCardType(String idCardType) {
		this.idCardType = idCardType;
	}

	public Integer getIsSanzheng() {
		return isSanzheng;
	}

	public void setIsSanzheng(Integer isSanzheng) {
		this.isSanzheng = isSanzheng;
	}

	public String getUnifiedCreditCode() {
		return unifiedCreditCode;
	}

	public void setUnifiedCreditCode(String unifiedCreditCode) {
		this.unifiedCreditCode = unifiedCreditCode;
	}



}
