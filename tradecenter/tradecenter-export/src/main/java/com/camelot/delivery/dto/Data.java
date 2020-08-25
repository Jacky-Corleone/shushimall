package com.camelot.delivery.dto;

import java.io.Serializable;
import java.util.Date;

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4041052825011444912L;
	
	private String areaCode;
	private String areaName;
	private String context;
	private Date ftime;
	private String status;
	private Date time;

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Date getFtime() {
		return ftime;
	}

	public void setFtime(Date ftime) {
		this.ftime = ftime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
