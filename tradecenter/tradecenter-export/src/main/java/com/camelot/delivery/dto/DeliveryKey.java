package com.camelot.delivery.dto;

public class DeliveryKey {
	
	public String key;
	public String url;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "deliveryKey [key=" + key + ", url=" + url + "]";
	}

}
