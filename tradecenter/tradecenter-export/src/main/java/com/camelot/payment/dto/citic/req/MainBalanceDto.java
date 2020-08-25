package com.camelot.payment.dto.citic.req;

import java.io.Serializable;
import java.util.List;

import com.camelot.payment.dto.citic.auxiliary.MainBalance;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  主体账户余额回传对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("stream")
public class MainBalanceDto  implements Serializable{

	private static final long serialVersionUID = 3537205236647004138L;
	private String status;// 状态
	private String statusText;// 状态文本
	private List<MainBalance> list;
	
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
	public List<MainBalance> getList() {
		return list;
	}
	public void setList(List<MainBalance> list) {
		this.list = list;
	}
}
