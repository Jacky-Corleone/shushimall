package com.camelot.common.enums;

public class FactorageEnums {
	
	/**
	 *  手续费记录状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum FactorageStatus{
		Initial(0,"初始化"),
		Refund(1,"手续费退款中"),
		RefundSuccess(21,"退款处理完成");
		
		private Integer facCode;// 中信接口设定的编码
		private String label;
		FactorageStatus(Integer facCode,String label){
			this.facCode=facCode;
			this.label=label;
		}
		
		public static FactorageStatus getEnumByCiticCode(Integer facCode) {
			for (FactorageStatus facStatus:FactorageStatus.values()) {
				if(facStatus.getFacCode().equals(facCode)){
					return facStatus;
				}
			}
			return null;
		}
		
		public Integer getFacCode() {
			return facCode;
		}

		public String getLabel() {
			return label;
		}
	}
}
