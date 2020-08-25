package com.camelot.example.domain;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author
 * 
 */
public class Demo implements Serializable {
	private static final long serialVersionUID = 2990627565835351070L;
	private String id;
	private String name;
	private String password;
	private Integer status;
	private String email;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
