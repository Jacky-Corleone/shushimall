package com.camelot.common.enums;

/**
 *  支付订单状态
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-2-27
 */
public enum TransationsStatusEnum {

	USER_LESS(-1),		/*无效的(父单)*/
	NOT_PAID(0),		/*未支付*/
	PAYING(1),	/*正在支付*/
	PAID_SUCCESS(2),	/*支付成功*/
	PAID_FAIL(3),	/*支付失败*/
	PAID_EXCEPTION(4);	/*支付金额不一致*/
	
	private Integer code;

	TransationsStatusEnum(Integer code) {
		this.code = code;
	}
	
	public static TransationsStatusEnum getEnumByCode(Integer code) {
		for (TransationsStatusEnum transationsStatusEnum:TransationsStatusEnum.values()) {
			if(transationsStatusEnum.getCode().equals(code)){
				return transationsStatusEnum;
			}
		}
		return null;
	}
	
	public Integer getCode() {
		return code;
	}
	
}
