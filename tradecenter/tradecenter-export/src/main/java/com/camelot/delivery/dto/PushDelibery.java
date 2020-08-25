package com.camelot.delivery.dto;

import java.io.Serializable;

public class PushDelibery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2867930554598209534L;
	
	private String billstatus;
	private String message;
	private String polling;
	private LastResult lastResult;

	public String getBillstatus() {
		return billstatus;
	}

	public void setBillstatus(String billstatus) {
		this.billstatus = billstatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPolling() {
		return polling;
	}

	public void setPolling(String polling) {
		this.polling = polling;
	}

	public LastResult getLastResult() {
		return lastResult;
	}

	public void setLastResult(LastResult lastResult) {
		this.lastResult = lastResult;
	}

}
