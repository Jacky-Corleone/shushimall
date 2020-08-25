package com.camelot.basecenter.domain;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * <p>Description: [短信、邮件配置、发送等基础服务]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: xxx@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class BaseSmsConfig {
	@PropertyMapping("id")
	private Long id;//平台id
	@PropertyMapping("sendName")
	private String sendName;//邮件发件人名称
	@PropertyMapping("sendAddress")
	private String sendAddress;//邮件发件人地址
	@PropertyMapping("emailType")
	private Short emailType;//邮件账户类型 1:POP3   2:SMTP  3:IMSP
	@PropertyMapping("sendServer")
	private String sendServer;//发送邮件服务器地址
	@PropertyMapping("sendServerPort")
	private Integer sendServerPort;//发送邮件服务器端口
	@PropertyMapping("receiveServer")
	private String receiveServer;//邮件接收服务器地址
	@PropertyMapping("receiveServerPort")
	private Integer receiveServerPort;//邮件接收服务器端口
	@PropertyMapping("isUseSmtpAuth")
	private Byte isUseSmtpAuth;//是否使用SMTP认证  1：使用  2：不使用
	@PropertyMapping("loginEmail")
	private String loginEmail;//登录邮箱地址
	@PropertyMapping("loginPassword")
	private String loginPassword;//邮件登录密码
	@PropertyMapping("msgUrl")
	private String msgUrl;//短信服务url
	@PropertyMapping("")
	private String msgHost;//短信服务host
	@PropertyMapping("msgHost")
	private String msgAccount;//短信服务账号
	@PropertyMapping("msgPassword")
	private String msgPassword;//短信服务密码
	@PropertyMapping("msgPszsubport")
	private String msgPszsubport;//短信服务子端口号码
	
	private String msgSa;//短信扩展号
	@PropertyMapping("msgSoapaddress")
	private String msgSoapaddress;//webservice接口访问地址
	
// setter and getter
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	public String getSendName(){
		return sendName;
	}
	
	public void setSendName(String sendName){
		this.sendName = sendName;
	}
	public String getSendAddress(){
		return sendAddress;
	}
	
	public void setSendAddress(String sendAddress){
		this.sendAddress = sendAddress;
	}
	public Short getEmailType(){
		return emailType;
	}
	
	public void setEmailType(Short emailType){
		this.emailType = emailType;
	}
	public String getSendServer(){
		return sendServer;
	}
	
	public void setSendServer(String sendServer){
		this.sendServer = sendServer;
	}
	public Integer getSendServerPort(){
		return sendServerPort;
	}
	
	public void setSendServerPort(Integer sendServerPort){
		this.sendServerPort = sendServerPort;
	}
	public String getReceiveServer(){
		return receiveServer;
	}
	
	public void setReceiveServer(String receiveServer){
		this.receiveServer = receiveServer;
	}
	public Integer getReceiveServerPort(){
		return receiveServerPort;
	}
	
	public void setReceiveServerPort(Integer receiveServerPort){
		this.receiveServerPort = receiveServerPort;
	}
	public Byte getIsUseSmtpAuth(){
		return isUseSmtpAuth;
	}
	
	public void setIsUseSmtpAuth(Byte isUseSmtpAuth){
		this.isUseSmtpAuth = isUseSmtpAuth;
	}
	public String getLoginEmail(){
		return loginEmail;
	}
	
	public void setLoginEmail(String loginEmail){
		this.loginEmail = loginEmail;
	}
	public String getLoginPassword(){
		return loginPassword;
	}
	
	public void setLoginPassword(String loginPassword){
		this.loginPassword = loginPassword;
	}
	public String getMsgUrl(){
		return msgUrl;
	}
	
	public void setMsgUrl(String msgUrl){
		this.msgUrl = msgUrl;
	}
	public String getMsgHost(){
		return msgHost;
	}
	
	public void setMsgHost(String msgHost){
		this.msgHost = msgHost;
	}
	public String getMsgAccount(){
		return msgAccount;
	}
	
	public void setMsgAccount(String msgAccount){
		this.msgAccount = msgAccount;
	}
	public String getMsgPassword(){
		return msgPassword;
	}
	
	public void setMsgPassword(String msgPassword){
		this.msgPassword = msgPassword;
	}
	public String getMsgPszsubport(){
		return msgPszsubport;
	}
	
	public void setMsgPszsubport(String msgPszsubport){
		this.msgPszsubport = msgPszsubport;
	}
	public String getMsgSa(){
		return msgSa;
	}
	
	public void setMsgSa(String msgSa){
		this.msgSa = msgSa;
	}
	public String getMsgSoapaddress(){
		return msgSoapaddress;
	}
	
	public void setMsgSoapaddress(String msgSoapaddress){
		this.msgSoapaddress = msgSoapaddress;
	}
}
