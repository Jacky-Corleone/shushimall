package com.camelot.payment.service.alipay.util.mobile;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 *  附属账户预签约回传对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("direct_trade_create_res")
public class AlipayMRes implements Serializable {

	private static final long serialVersionUID = 5461794212590485075L;

	private String request_token;

	public String getRequest_token() {
		return request_token;
	}

	public void setRequest_token(String request_token) {
		this.request_token = request_token;
	}

	 
}
