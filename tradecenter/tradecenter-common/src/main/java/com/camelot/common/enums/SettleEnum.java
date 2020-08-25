package com.camelot.common.enums;

public class SettleEnum{
	
	/**
	 *  结算单状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum SettleStatusEnum{
		UnBill(0,"结算待出账 "),UnSettle(1,"平台待结算 "),UnConfirmed(2,"卖家待确认"),Confirmed(3,"结算已完成");
		
		private Integer code;
		private String label;
		SettleStatusEnum(Integer code,String label){
			this.code=code;
			this.label=label;
		}
		
		public static SettleStatusEnum getEnumBycode(Integer code){
			for (SettleStatusEnum settleStatus:SettleStatusEnum.values()) {
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
	
	/**
	 *  结算详单状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum SettleDetailStatusEnum{
		RefundPaying(-3,"退款"),OffLinePaying(-2,"线下支付"),FreezePaying(-1,"已冻结"),UnPaying(0,"待支付"),PayAffirm(1,"确认银行已结算"),PaySuccess(2,"已支付待付佣金"),PayFinish(3,"支付完成");
		
		private Integer code;
		private String label;
		SettleDetailStatusEnum(Integer code,String label){
			this.code=code;
			this.label=label;
		}
		
		public static SettleDetailStatusEnum getEnumBycode(Integer code){
			for (SettleDetailStatusEnum settleDetailStatus:SettleDetailStatusEnum.values()) {
				if(settleDetailStatus.getCode()==code){
					return settleDetailStatus;
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
	
	/**
	 *  结算详单类型
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum SettleDetailTypeEnum{
		Settle(1,"第三方待结算"),Pay(2,"第三方已结算");
		
		private Integer code;
		private String label;
		SettleDetailTypeEnum(Integer code,String label){
			this.code=code;
			this.label=label;
		}
		
		public static SettleDetailTypeEnum getEnumBycode(Integer code){
			for (SettleDetailTypeEnum settleDetailType:SettleDetailTypeEnum.values()) {
				if(settleDetailType.getCode()==code){
					return settleDetailType;
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
