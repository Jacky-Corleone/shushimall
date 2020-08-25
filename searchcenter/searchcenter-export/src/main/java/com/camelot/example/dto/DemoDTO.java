package com.camelot.example.dto;

import java.io.Serializable;

/**
 * 
 * @author
 * 
 */
public class DemoDTO implements Serializable {
	private static final long serialVersionUID = 2990627565835351070L;
	private String name;
	private String password;
	private Integer status;
	private String email;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
