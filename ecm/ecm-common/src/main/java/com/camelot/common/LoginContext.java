package com.camelot.common;

public class LoginContext {
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 用户名
	 */
	private String userName;
	/**
	 * 访问IP地址
	 */
	private String ip;
	/*员工号*/
	private String empCode;
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
}
