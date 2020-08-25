package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author
 */
public class ShopDTO implements Serializable {

	private static final long serialVersionUID = 8737941431210274666L;
	private java.lang.Long shopId;// 店铺id
	private java.lang.String shopName;// 店铺名称
	private java.lang.Long sellerId;// 卖家id
	private Integer status;// 店铺建新状态;1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
	private Integer[] statuss;// 店铺建新状态;1是申请，2是通过，3是驳回， 4是平台关闭，5是开通
	private java.lang.String shopUrl;// 店铺域名
	private java.lang.String logoUrl;// 店铺logourl
	private java.lang.String keyword;// 关键字
	private java.lang.String introduce;// 店铺介绍
	private java.lang.String mainSell;// 店铺主营
	private BigDecimal priceMin;// 混批金额要求
	private java.lang.Long mountMin;// 批混数量要求
	private java.lang.String provinceCode;// 省份代码
	private java.lang.String provinceName;// 省份名字
	private java.lang.String zcode;// 邮政编码
	private java.lang.String streetName;// 街道名字
	private java.lang.Long mobile;// 手机号码
	private java.lang.String areaCode;// 座机区号
	private java.lang.String landline;// 座机号码
	private java.lang.String extensionNumber;// 分机号码
	private java.util.Date created;// 申请时间
	private java.util.Date modified;// 更新时间
	private java.util.Date passTime;// 开通时间
	private java.util.Date endTime;// 终止时间
	private java.lang.String sellerBrand;// 运营商品品牌
	private java.lang.String comment;// 批注
	private java.lang.Long platformUserId;// 批注人id
	private Integer mutilPrice;// 是否混批（1为混批，2为不混批）
	private java.lang.String cityName;// 市的名字
	private java.lang.String cityCode;// 市的代码
	private java.lang.String districtName;// 区的名字
	private java.lang.String districtCode;// 区的代码
	private Integer mutilCondition;// 混批条件，1为或，2为且
	private BigDecimal initialPrice;// 起批价格
	private java.lang.Long initialMount;// 起批数量
	private Integer initialCondition;// 起批条件，1为或，2为且
	private Integer modifyStatus;// 店铺信息修改状态，1为待审核，2为驳回，3为修改通过
	private Integer runStatus;// 店铺运行状态（只能允许卖家操作，默认为不开启），1表示卖家开启铺店，2表示卖家关闭店铺

	
	private Date createdstr;// 申请时间开始
	private Date createdend;// 申请时间结束
	private java.util.Date modifiedstr;// 更新时间
	private java.util.Date modifiedend;// 更新时间
	private Date passTimeBegin; // 开通时间开始
	private Date passTimeEnd; // 开通时间开始

	private Long cid;// 类目id
	private Integer cStatus;// 类目状态
	private Integer collation = 1; // 1 修改时间升序 2 修改时间降序 3 评分升序 4评分降序 5销量升序 6销量降序

	private String scope;// 店铺评分
	private String salesVolume;// 店铺销量

	private Integer shopType;// 店铺类型 1 品牌商 2经销商
	private Integer brandType;// 品牌类型 1 国内品牌 2国际品牌
	private Integer businessType; // 经营类型 1自有品牌 2 代理品牌
	private String disclaimer;// 免责声明
	private String trademarkRegistCert;// 商标注册证/商品注册申请书扫描件
	private String inspectionReport;// 质检、检疫、检验报告/报关单类扫描件
	private String productionLicense;// 卫生/生产许可证扫描件
	private String marketingAuth;// 销售授权书扫描件
	private Integer financing;// 是否有融资需求 1是 2否
	private String financingAmt;// 融资金额 以万元为单位

	private Integer platformId;// 平台ID
	private String gpCommitmentBook;// 承诺书
	
	//Add By PC 2015-10-28 增加销售联系人、400电话、自营
	private String linkMan1; //销售联系人1
	private String linkPhoneNum1; //销售联系电话1
	private String linkMan2; //联系人2
	private String linkPhoneNum2; //联系电话2
	private Integer directSale; //0非自营、1自营
	private String remark; //客服电话

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public Date getPassTimeBegin() {
		return passTimeBegin;
	}

	public void setPassTimeBegin(Date passTimeBegin) {
		this.passTimeBegin = passTimeBegin;
	}

	public Date getPassTimeEnd() {
		return passTimeEnd;
	}

	public void setPassTimeEnd(Date passTimeEnd) {
		this.passTimeEnd = passTimeEnd;
	}

	public Integer[] getStatuss() {
		return statuss;
	}

	public void setStatuss(Integer[] statuss) {
		this.statuss = statuss;
	}

	public Integer getCollation() {
		return collation;
	}

	public void setCollation(Integer collation) {
		this.collation = collation;
	}

	public Integer getcStatus() {
		return cStatus;
	}

	public void setcStatus(Integer cStatus) {
		this.cStatus = cStatus;
	}

	public Date getCreatedstr() {
		return createdstr;
	}

	public void setCreatedstr(Date createdstr) {
		this.createdstr = createdstr;
	}

	public Date getCreatedend() {
		return createdend;
	}

	public void setCreatedend(Date createdend) {
		this.createdend = createdend;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public java.lang.Long getShopId() {
		return this.shopId;
	}

	public void setShopId(java.lang.Long value) {
		this.shopId = value;
	}

	public java.util.Date getModifiedstr() {
		return modifiedstr;
	}

	public void setModifiedstr(java.util.Date modifiedstr) {
		this.modifiedstr = modifiedstr;
	}

	public java.util.Date getModifiedend() {
		return modifiedend;
	}

	public void setModifiedend(java.util.Date modifiedend) {
		this.modifiedend = modifiedend;
	}

	public java.lang.String getShopName() {
		return this.shopName;
	}

	public void setShopName(java.lang.String value) {
		this.shopName = value;
	}

	public java.lang.Long getSellerId() {
		return this.sellerId;
	}

	public void setSellerId(java.lang.Long value) {
		this.sellerId = value;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public java.lang.String getShopUrl() {
		return this.shopUrl;
	}

	public void setShopUrl(java.lang.String value) {
		this.shopUrl = value;
	}

	public java.lang.String getLogoUrl() {
		return this.logoUrl;
	}

	public void setLogoUrl(java.lang.String value) {
		this.logoUrl = value;
	}

	public java.lang.String getKeyword() {
		return this.keyword;
	}

	public void setKeyword(java.lang.String value) {
		this.keyword = value;
	}

	public java.lang.String getIntroduce() {
		return this.introduce;
	}

	public void setIntroduce(java.lang.String value) {
		this.introduce = value;
	}

	public java.lang.String getMainSell() {
		return this.mainSell;
	}

	public void setMainSell(java.lang.String value) {
		this.mainSell = value;
	}

	public java.lang.Long getMountMin() {
		return this.mountMin;
	}

	public void setMountMin(java.lang.Long value) {
		this.mountMin = value;
	}

	public java.lang.String getProvinceCode() {
		return this.provinceCode;
	}

	public void setProvinceCode(java.lang.String value) {
		this.provinceCode = value;
	}

	public java.lang.String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(java.lang.String value) {
		this.provinceName = value;
	}

	public java.lang.String getZcode() {
		return this.zcode;
	}

	public void setZcode(java.lang.String value) {
		this.zcode = value;
	}

	public java.lang.String getStreetName() {
		return this.streetName;
	}

	public void setStreetName(java.lang.String value) {
		this.streetName = value;
	}

	public java.lang.Long getMobile() {
		return this.mobile;
	}

	public void setMobile(java.lang.Long value) {
		this.mobile = value;
	}

	public java.lang.String getAreaCode() {
		return this.areaCode;
	}

	public void setAreaCode(java.lang.String value) {
		this.areaCode = value;
	}

	public java.lang.String getLandline() {
		return this.landline;
	}

	public void setLandline(java.lang.String value) {
		this.landline = value;
	}

	public java.lang.String getExtensionNumber() {
		return this.extensionNumber;
	}

	public void setExtensionNumber(java.lang.String value) {
		this.extensionNumber = value;
	}

	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}

	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

	public java.util.Date getPassTime() {
		return this.passTime;
	}

	public void setPassTime(java.util.Date value) {
		this.passTime = value;
	}

	public java.util.Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(java.util.Date value) {
		this.endTime = value;
	}

	public java.lang.String getSellerBrand() {
		return this.sellerBrand;
	}

	public void setSellerBrand(java.lang.String value) {
		this.sellerBrand = value;
	}

	public java.lang.String getComment() {
		return this.comment;
	}

	public void setComment(java.lang.String value) {
		this.comment = value;
	}

	public java.lang.Long getPlatformUserId() {
		return this.platformUserId;
	}

	public void setPlatformUserId(java.lang.Long value) {
		this.platformUserId = value;
	}

	public Integer getMutilPrice() {
		return this.mutilPrice;
	}

	public void setMutilPrice(Integer value) {
		this.mutilPrice = value;
	}

	public java.lang.String getCityName() {
		return this.cityName;
	}

	public void setCityName(java.lang.String value) {
		this.cityName = value;
	}

	public java.lang.String getCityCode() {
		return this.cityCode;
	}

	public void setCityCode(java.lang.String value) {
		this.cityCode = value;
	}

	public java.lang.String getDistrictName() {
		return this.districtName;
	}

	public void setDistrictName(java.lang.String value) {
		this.districtName = value;
	}

	public java.lang.String getDistrictCode() {
		return this.districtCode;
	}

	public void setDistrictCode(java.lang.String value) {
		this.districtCode = value;
	}

	public Integer getMutilCondition() {
		return this.mutilCondition;
	}

	public void setMutilCondition(Integer value) {
		this.mutilCondition = value;
	}

	public BigDecimal getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(BigDecimal priceMin) {
		this.priceMin = priceMin;
	}

	public BigDecimal getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(BigDecimal initialPrice) {
		this.initialPrice = initialPrice;
	}

	public java.lang.Long getInitialMount() {
		return this.initialMount;
	}

	public void setInitialMount(java.lang.Long value) {
		this.initialMount = value;
	}

	public Integer getInitialCondition() {
		return this.initialCondition;
	}

	public void setInitialCondition(Integer value) {
		this.initialCondition = value;
	}

	public Integer getModifyStatus() {
		return this.modifyStatus;
	}

	public void setModifyStatus(Integer value) {
		this.modifyStatus = value;
	}

	public Integer getRunStatus() {
		return this.runStatus;
	}

	public void setRunStatus(Integer value) {
		this.runStatus = value;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(String salesVolume) {
		this.salesVolume = salesVolume;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}

	public Integer getBrandType() {
		return brandType;
	}

	public void setBrandType(Integer brandType) {
		this.brandType = brandType;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getTrademarkRegistCert() {
		return trademarkRegistCert;
	}

	public void setTrademarkRegistCert(String trademarkRegistCert) {
		this.trademarkRegistCert = trademarkRegistCert;
	}

	public String getInspectionReport() {
		return inspectionReport;
	}

	public void setInspectionReport(String inspectionReport) {
		this.inspectionReport = inspectionReport;
	}

	public String getProductionLicense() {
		return productionLicense;
	}

	public void setProductionLicense(String productionLicense) {
		this.productionLicense = productionLicense;
	}

	public String getMarketingAuth() {
		return marketingAuth;
	}

	public void setMarketingAuth(String marketingAuth) {
		this.marketingAuth = marketingAuth;
	}

	public Integer getFinancing() {
		return financing;
	}

	public void setFinancing(Integer financing) {
		this.financing = financing;
	}

	public String getFinancingAmt() {
		return financingAmt;
	}

	public void setFinancingAmt(String financingAmt) {
		this.financingAmt = financingAmt;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public String getGpCommitmentBook() {
		return gpCommitmentBook;
	}

	public void setGpCommitmentBook(String gpCommitmentBook) {
		this.gpCommitmentBook = gpCommitmentBook;
	}

	public String getLinkPhoneNum1() {
		return linkPhoneNum1;
	}

	public void setLinkPhoneNum1(String linkPhoneNum1) {
		this.linkPhoneNum1 = linkPhoneNum1;
	}

	public String getLinkMan2() {
		return linkMan2;
	}

	public void setLinkMan2(String linkMan2) {
		this.linkMan2 = linkMan2;
	}

	public String getLinkPhoneNum2() {
		return linkPhoneNum2;
	}

	public void setLinkPhoneNum2(String linkPhoneNum2) {
		this.linkPhoneNum2 = linkPhoneNum2;
	}

	public String getLinkMan1() {
		return linkMan1;
	}

	public void setLinkMan1(String linkMan1) {
		this.linkMan1 = linkMan1;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDirectSale() {
		return directSale;
	}

	public void setDirectSale(Integer directSale) {
		this.directSale = directSale;
	}

}
