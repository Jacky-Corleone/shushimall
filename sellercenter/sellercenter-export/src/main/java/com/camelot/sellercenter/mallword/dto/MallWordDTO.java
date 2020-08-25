package com.camelot.sellercenter.mallword.dto;

import java.io.Serializable;
import java.util.Date;

public class MallWordDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id; // ID
	private String word; // 推广词
	private Date created; // 创建时间
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
