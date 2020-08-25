package com.camelot.tradecenter.dto.enums;

public enum TradeOrdersStateEnum {
	//订单状态 1:待付款，2：待配送，3：待确认收货，4：待评价，5：已完成
	PAYING(1, "待付款"), DELIVERING(2, "待配送"), CONFIRMING(3, "待确认收货"), EVALUATING(4, "待评价"), FINISHED(5, "已完成"),CANCELED(6,"已取消"),COLSE(7,"已关闭");

	private int code;
	private String label;

	TradeOrdersStateEnum(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public int getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}
}
