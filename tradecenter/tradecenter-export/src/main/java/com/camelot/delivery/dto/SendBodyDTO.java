package com.camelot.delivery.dto;

import java.io.Serializable;

/**
 * <p>
 * Description: [物流信息发送基本信息]
 * </p>
 * Created on 2015年8月10日
 * 
 * @author <a href="mailto: liufangyi@camelotchina.com">刘芳义</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SendBodyDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8125782770013846308L;
	
	private String company;// 快递公司名
	private String number;// 快递编号
	private String from;// 发送地
	private String to;// 发送到那里
	private String key;// 发送密码
	private String mobiletelephone;//电话号码
	private String seller; //卖家名称
	private String commodity; //商品名称
	private ParametersDTO parameters;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public ParametersDTO getParameters() {
		return parameters;
	}

	public void setParameters(ParametersDTO parameters) {
		this.parameters = parameters;
	}

	public String getMobiletelephone() {
		return mobiletelephone;
	}

	public void setMobiletelephone(String mobiletelephone) {
		this.mobiletelephone = mobiletelephone;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	@Override
	public String toString() {
		return "SendBodyDTO [company=" + company + ", number=" + number + ", from=" + from + ", to=" + to + ", key="
				+ key + ", mobiletelephone=" + mobiletelephone + ", seller=" + seller + ", commodity=" + commodity
				+ ", parameters=" + parameters + "]";
	}
}
