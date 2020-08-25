package com.camelot.common.enums;

/**
 * 确认收货时间起始时间点 true 启用
 * 
 * @author zhouzj
 * @since 2015-8-24
 * 
 */
public enum ConfirmTimeEnum {

	orderedTime("order_time", "订单下达时间 ", false), 
	confirmReceiptTime("confirm_receipt_time", "确认收货时间 ", false),
	deliverTime("deliver_time", "订单发货时间 ", true);

	private String code;
	private String label;
	private boolean active;

	ConfirmTimeEnum(String code, String label, boolean active) {
		this.code = code;
		this.label = label;
		this.active = active;
	}

	public static ConfirmTimeEnum getConfirmTime() {
		for (ConfirmTimeEnum settleStatus : ConfirmTimeEnum.values()) {
			if (settleStatus.getActive()) {
				return settleStatus;
			}
		}
		return null;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public boolean getActive() {
		return active;
	}
}
