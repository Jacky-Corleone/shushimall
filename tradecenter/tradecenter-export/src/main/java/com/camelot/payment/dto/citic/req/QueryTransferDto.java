package com.camelot.payment.dto.citic.req;

import java.io.Serializable;
import java.util.List;

import com.camelot.payment.dto.citic.auxiliary.QueryTransfer;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账号间的交易结果查询回传
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("stream")
public class QueryTransferDto implements Serializable{

	private static final long serialVersionUID = 5327525527140427333L;
	private String status;// <!--交易状态 char(7)-->
	private String statusText;// <!--交易状态信息 varchar(254)-->
	private List<QueryTransfer> list;
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
	public List<QueryTransfer> getList() {
		return list;
	}
	public void setList(List<QueryTransfer> list) {
		this.list = list;
	}
	
}
