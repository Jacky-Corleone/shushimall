package com.camelot.util;

import com.camelot.usercenter.dto.UserDTO;

public class SendMessageUser {
	private UserDTO buyer;
	private UserDTO seller;
	private String orderId;
	public UserDTO getBuyer() {
		return buyer;
	}
	public void setBuyer(UserDTO buyer) {
		this.buyer = buyer;
	}
	public UserDTO getSeller() {
		return seller;
	}
	public void setSeller(UserDTO seller) {
		this.seller = seller;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
