package com.camelot.payment.dto.citic.req;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账号间的强制转账回传结果
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("stream")
public class TenantTransferDTO implements Serializable{

	private static final long serialVersionUID = -1166785663846250983L;
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
