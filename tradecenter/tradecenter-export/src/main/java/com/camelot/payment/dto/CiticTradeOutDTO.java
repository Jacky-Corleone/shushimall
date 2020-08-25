package com.camelot.payment.dto;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 非登录打印明细查询出参DTO
 * @author 王东晓
 *
 */
@XStreamAlias("stream")
public class CiticTradeOutDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status; //交易状态 char(7)
	private String statusText;//交易状态信息varchar(254)
	private List<CiticTradeInfoDTO> list;//账户明细信息列表
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
	public List<CiticTradeInfoDTO> getList() {
		return list;
	}
	public void setList(List<CiticTradeInfoDTO> list) {
		this.list = list;
	}
	
}
