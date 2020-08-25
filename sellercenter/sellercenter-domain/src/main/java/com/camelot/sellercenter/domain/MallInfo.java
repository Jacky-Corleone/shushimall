package com.camelot.sellercenter.domain;
/**
 * 
 * <p>Description: [买场中心_商城信息]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class MallInfo {

	private Double id;//
	private String title;//网站名称
	private String logo;//商户logo图片地址
	private String created;//创建时间
	private String modified;//修改时间
	private String domain;//
	private Integer needAuth;//平台买家购买时必须通过认证 1是 2不是
	private String shortName;//商铺的简称，用于卖场首页右边楼层显示
	private Integer status;//备用状态字段
	private String platformQq;//平台qq
	private String jfcpAccount;//平台在金融云账号
	private String fullName;//平台在金融云中全名
	private Integer paymentChannel;//所属银行
	private String bankuserName;//平台在银行网银登录账号
	private String bankmasterAccountNo;//银行分配给平台主账号
	private String bankmasterAccountName;//平台在银行主账号名称
	private String commissionAccountNo;//平台佣金账号
	private String gatherAccountNo;//平台收款账号
	private String bankWithdrawDelegate;//代理出金账户
	private String bankInterest;//公共计息收费
	private String bankAdjustment;//公共调账
	private String bankInitializer;//资金初始化
	private Double clusterId;//集群ID
	private Integer platformId;// 平台ID
	
// setter and getter
	public Double getId(){
		return id;
	}
	
	public void setId(Double id){
		this.id = id;
	}
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}

	public String getLogo(){
		return logo;
	}
	
	public void setLogo(String logo){
		this.logo = logo;
	}
	public String getCreated(){
		return created;
	}
	
	public void setCreated(String created){
		this.created = created;
	}
	public String getModified(){
		return modified;
	}
	
	public void setModified(String modified){
		this.modified = modified;
	}
	public String getDomain(){
		return domain;
	}
	
	public void setDomain(String domain){
		this.domain = domain;
	}
	public Integer getNeedAuth(){
		return needAuth;
	}
	
	public void setNeedAuth(Integer needAuth){
		this.needAuth = needAuth;
	}
	public String getShortName(){
		return shortName;
	}
	
	public void setShortName(String shortName){
		this.shortName = shortName;
	}
	public Integer getStatus(){
		return status;
	}
	
	public void setStatus(Integer status){
		this.status = status;
	}
	public String getPlatformQq(){
		return platformQq;
	}
	
	public void setPlatformQq(String platformQq){
		this.platformQq = platformQq;
	}
	public String getJfcpAccount(){
		return jfcpAccount;
	}
	
	public void setJfcpAccount(String jfcpAccount){
		this.jfcpAccount = jfcpAccount;
	}
	public String getFullName(){
		return fullName;
	}
	
	public void setFullName(String fullName){
		this.fullName = fullName;
	}
	public Integer getPaymentChannel(){
		return paymentChannel;
	}
	
	public void setPaymentChannel(Integer paymentChannel){
		this.paymentChannel = paymentChannel;
	}
	public String getBankuserName(){
		return bankuserName;
	}
	
	public void setBankuserName(String bankuserName){
		this.bankuserName = bankuserName;
	}
	public String getBankmasterAccountNo(){
		return bankmasterAccountNo;
	}
	
	public void setBankmasterAccountNo(String bankmasterAccountNo){
		this.bankmasterAccountNo = bankmasterAccountNo;
	}
	public String getBankmasterAccountName(){
		return bankmasterAccountName;
	}
	
	public void setBankmasterAccountName(String bankmasterAccountName){
		this.bankmasterAccountName = bankmasterAccountName;
	}
	public String getCommissionAccountNo(){
		return commissionAccountNo;
	}
	
	public void setCommissionAccountNo(String commissionAccountNo){
		this.commissionAccountNo = commissionAccountNo;
	}
	public String getGatherAccountNo(){
		return gatherAccountNo;
	}
	
	public void setGatherAccountNo(String gatherAccountNo){
		this.gatherAccountNo = gatherAccountNo;
	}
	public String getBankWithdrawDelegate(){
		return bankWithdrawDelegate;
	}
	
	public void setBankWithdrawDelegate(String bankWithdrawDelegate){
		this.bankWithdrawDelegate = bankWithdrawDelegate;
	}
	public String getBankInterest(){
		return bankInterest;
	}
	
	public void setBankInterest(String bankInterest){
		this.bankInterest = bankInterest;
	}
	public String getBankAdjustment(){
		return bankAdjustment;
	}
	
	public void setBankAdjustment(String bankAdjustment){
		this.bankAdjustment = bankAdjustment;
	}
	public String getBankInitializer(){
		return bankInitializer;
	}
	
	public void setBankInitializer(String bankInitializer){
		this.bankInitializer = bankInitializer;
	}
	public Double getClusterId(){
		return clusterId;
	}
	
	public void setClusterId(Double clusterId){
		this.clusterId = clusterId;
	}
}
