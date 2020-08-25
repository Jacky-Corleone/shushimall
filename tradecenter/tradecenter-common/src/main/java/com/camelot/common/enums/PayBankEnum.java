package com.camelot.common.enums;

/**
 *  支付渠道
 *
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-17
 */
public enum PayBankEnum {
	AP("支付宝",0),
	AP_MOBILE("支付宝_手机端",100),
	CB("网银在线",1),
	CB_MOBILE("网银在线_手机端",101),
	CITIC("中信银行",2),
	OFFLINE("线下支付",3),
	OTHER("支付宝其他银行",4),
	WX("微信",5),
	WXPC("微信PC端",6),
	INTEGRAL("积分支付",7),
	CUP("银联",8);
	
	private String lable;
	private Integer qrCode;// 二维码支付

	private PayBankEnum(String lable,Integer qrCode) {
		this.lable = lable;
		this.qrCode = qrCode;
	}

	public static PayBankEnum getEnumByName(String name) {
		for (PayBankEnum payBankEnum:PayBankEnum.values()) {
			if(payBankEnum.name().equals(name)){
				return payBankEnum;
			}
		}
		return null;
	}

	public static PayBankEnum getEnumByQrCode(Integer qrCode) {
		for (PayBankEnum payBankEnum:PayBankEnum.values()) {
			if(payBankEnum.getQrCode()==qrCode){
				return payBankEnum;
			}
		}
		return null;
	}

	public Integer getQrCode() {
		return qrCode;
	}

	public String getLable() {
		return lable;
	}
}
