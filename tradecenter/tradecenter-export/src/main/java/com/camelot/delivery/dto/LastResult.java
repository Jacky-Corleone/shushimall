package com.camelot.delivery.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LastResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9206720534096178491L;
	
	private String com;
	private String condition;
	private String ischeck;
	private String message;
	private String nu;
	private String status;
	private String state;
	private List<Data> data = new ArrayList<Data>();

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Data> getData() {
		return data;
	}

	public void setData(List<Data> data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
