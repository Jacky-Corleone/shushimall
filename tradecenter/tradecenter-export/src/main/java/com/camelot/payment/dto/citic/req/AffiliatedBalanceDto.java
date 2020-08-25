package com.camelot.payment.dto.citic.req;

import java.io.Serializable;
import java.util.List;

import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账户余额信息回传对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("stream")
public class AffiliatedBalanceDto implements Serializable {

	private static final long serialVersionUID = -2122603911435223933L;
	private String status;//<!--账户状态 char(7)-->
	private String statusText;// <!--账户状态信息 varchar(254)-->

	private List<AffiliatedBalance> list;
	
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
	public List<AffiliatedBalance> getList() {
		return list;
	}
	public void setList(List<AffiliatedBalance> list) {
		this.list = list;
	}
}
