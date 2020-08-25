package com.camelot.payment.dto.citic.req;

import java.io.Serializable;
import java.util.List;

import com.camelot.payment.dto.citic.auxiliary.AffiliatedQuery;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *  附属账户签约状态回传对象
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
@XStreamAlias("stream")
public class AffiliatedQueryDto implements Serializable{

	private static final long serialVersionUID = -1797090654337730568L;
	private String status;// 状态
	private String statusText;// 状态文本
	private List<AffiliatedQuery> list;
	
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
	public List<AffiliatedQuery> getList() {
		return list;
	}
	public void setList(List<AffiliatedQuery> list) {
		this.list = list;
	}
}
