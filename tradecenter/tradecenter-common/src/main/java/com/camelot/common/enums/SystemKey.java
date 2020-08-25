package com.camelot.common.enums;
/**
 * 系统编码（秘钥）
 * @Description -
 */
public enum SystemKey{
	CiticPay("citic_pay_pri",false),// 不对外
	MainBalance("citic_main_balance",false),// 不对外
	CompanyJobPay("com_job_pay",true),
	MALL("citic_mall",true);
	
	private String keyPri;
	private boolean isOpen;
	
	SystemKey(String keyPri,boolean isOpen){
		this.keyPri=keyPri;
		this.isOpen=isOpen;
	}

	public static String getOpenKeyPri(String sysName) {
		for (SystemKey systemKey:SystemKey.values()) {
			if(systemKey.name().equals(sysName)&&systemKey.isOpen){
				return systemKey.keyPri;
			}
		}
		return "";
	}

	public String getKeyPri() {
		return keyPri;
	}
	
}