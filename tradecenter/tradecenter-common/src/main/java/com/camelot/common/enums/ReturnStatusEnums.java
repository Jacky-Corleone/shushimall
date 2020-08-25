package com.camelot.common.enums;

public class ReturnStatusEnums {
	
	/**
	 *  退款单状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum TradeRefundStatusEnum {
		initialize(1,"初始化"),part(19,"卖家退款成功,未退佣金和手续费"),partFactorage(20,"卖家退款成功,未退手续费"),partSuccess(21,"卖家退款成功,未退佣金"),success(22,"退款完成"),offlineSuccess(23,"卖家线下退款"),fail(3,"退款失败");

		private Integer code;
		private String label;
		TradeRefundStatusEnum(Integer code, String label){
			this.code=code;
			this.label=label;
		}
		
		public static TradeRefundStatusEnum getEnumBycode(Integer code){
			for (TradeRefundStatusEnum refundStatus: TradeRefundStatusEnum.values()) {
				if(refundStatus.getCode()==code){
					return refundStatus;
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
}

