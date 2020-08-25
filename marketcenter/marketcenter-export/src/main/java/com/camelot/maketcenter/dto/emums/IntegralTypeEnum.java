package com.camelot.maketcenter.dto.emums;

/**
 *  支付方式
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-12
 */
public enum IntegralTypeEnum {

	INTEGRAL_MONEY("金额返还积分",1), 	
	INTEGRAL_EVALUATE("评价获取积分",2), 	
	INTEGRAL_USING("订单使用积分",3), 	
	INTEGRAL_EXCHANGE("积分兑换金额",4), 	
	INTEGRAL_REFUND("退款返还积分",5),
	INTEGRAL_CANCEL("取消订单返还积分",6),
	INTEGRAL_BASKORDER("评价+晒单可获得积分",7);

	private String lable;
	private Integer code;

	private IntegralTypeEnum(String lable,Integer code) {
		this.lable = lable;
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}
	public String getLable(){
		return lable;
	}
	
}
