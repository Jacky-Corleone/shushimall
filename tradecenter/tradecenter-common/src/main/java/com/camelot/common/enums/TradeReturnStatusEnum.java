package com.camelot.common.enums;

/**
 *  退货单状态
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
public enum TradeReturnStatusEnum {
	AUTH(1,"退款申请等待卖家确认中"),DISAGRESS(2,"卖家不同意协议,等待买家修改"),PASS(3,"退款申请达成,等待买家发货"),ORDERRERURN(4,"买家已退货,等待卖家确认收货")
    ,CLOSE(5,"退款关闭 "),SUCCESS(6,"退款成功"),REFUNDING(7,"退款中"),PLATFORMTOREFUND(8,"待平台处理退款"),PLATFORMDEALING (9,"平台处理中")
    ,REFUNDFAIL(10,"退款失败"),REFUNDAPPLICATION(11,"退款申请成功,支付宝处理中"),REFUNDAPPLICATION_CUP(12,"退款申请成功,银联处理中")
    ,WAITBUYERSUBMIT(13,"待买家确认收款");

	private Integer code;
	private String label;
	TradeReturnStatusEnum(Integer code, String label){
		this.code=code;
		this.label=label;
	}
	
	public static TradeReturnStatusEnum getEnumBycode(Integer code){
	 	for (TradeReturnStatusEnum settleStatus: TradeReturnStatusEnum.values()) {
			if(settleStatus.getCode()==code){
				return settleStatus;
			}
		}
		return null;
	}
	
	public Integer getCode() {
		return code;
	}
	public String getLabel() {
		return label;
	}
}
