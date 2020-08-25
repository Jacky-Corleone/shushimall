package com.camelot.payment.dto.citic.auxiliary;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  主体账户余额回传对象(辅)
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("row")
public class QueryTransfer implements Serializable{

	private static final long serialVersionUID = 293592017759460432L;
	private String stt;//<!--状态标志  char(1) 0 成功 1 失败 2未知 3审核拒绝 4 用户撤销-->
	private String status;//<!--账户状态 char(7)-->
	private String statusText;// <!--账户状态信息 varchar(254)-->
	public String getStt() {
		return stt;
	}
	public void setStt(String stt) {
		this.stt = stt;
	}
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
