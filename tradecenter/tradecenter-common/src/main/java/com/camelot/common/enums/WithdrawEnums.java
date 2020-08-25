package com.camelot.common.enums;

public enum WithdrawEnums {
	WithdrawQueryFail(1,"查询失败"),
	WithdrawDispose(10,"申请处理中"),
	WithdrawalApplicationFail(20,"提现申请失败"),
	WithdrawalApplicationSuccess(30,"提现申请成功"),
	WithdrawalFail(40,"提现失败"),
	WithdrawalSuccess(50,"提现已完成，请查验"), 
	WithdrawalUnknow(60,"未知"),
	WithdrawalUnVerify(70,"审核拒绝"),
	WithdrawalDel(80,"用户撤销");
	private Integer code;
	private String label;
	
	WithdrawEnums(Integer code,String label){
		this.code=code;
		this.label=label;
	}
	
	public static WithdrawEnums getEnumByCode(Integer code){
		for (WithdrawEnums withdrawEnum:WithdrawEnums.values()) {
			if(withdrawEnum.getCode()==code){
				return withdrawEnum;
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
