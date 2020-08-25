package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [tdk设置]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class BaseSendMessageDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String title;//标题
	private String address;//邮件/手机 地址
	private String content;//内容
	private String type;//类型 2:短信 1：邮件
	private Integer contentType;//1:验证码；2：通知
	private String isSend;//是否已经发送   0：否 1：是
	private String isPildash;//是否必达 0：否 1：是
	private Integer sendNum; //发送次数
	private Date createTime;//创建时间
	
	private Integer minSendNum;//最少发送次数 查询使用
	private Integer maxSendNum;//最大发送次数  查询使用
	private String beginTime; //起始日期 查询使用
	private String endTime;//结束日期  查询使用
	private Integer platformId;//平台ID  null为科印   2为绿印
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIsSend() {
		return isSend;
	}
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	public String getIsPildash() {
		return isPildash;
	}
	public void setIsPildash(String isPildash) {
		this.isPildash = isPildash;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getSendNum() {
		return sendNum;
	}
	public void setSendNum(Integer sendNum) {
		this.sendNum = sendNum;
	}
	public Integer getMinSendNum() {
		return minSendNum;
	}
	public void setMinSendNum(Integer minSendNum) {
		this.minSendNum = minSendNum;
	}
	public Integer getMaxSendNum() {
		return maxSendNum;
	}
	public void setMaxSendNum(Integer maxSendNum) {
		this.maxSendNum = maxSendNum;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public Integer getContentType() {
		return contentType;
	}
	public void setContentType(Integer contentType) {
		this.contentType = contentType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
