package com.camelot.payment.dto.citic.req;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账号出金回传结果
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("stream")
public class OutTransferDto implements Serializable{

	private static final long serialVersionUID = 931556860172252163L;
	private String status;// <!--交易状态 char(7)-->
	private String statusText;// <!--交易状态信息 varchar(254)-->
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStatusText() {
		return statusText;
	}
	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}
}
