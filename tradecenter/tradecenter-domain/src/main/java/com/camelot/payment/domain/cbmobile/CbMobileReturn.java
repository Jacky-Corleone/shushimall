package com.camelot.payment.domain.cbmobile;

import java.io.Serializable;

/**
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
public class CbMobileReturn implements Serializable{
	private static final long serialVersionUID = 4381491109723127357L;
	private String CODE;// 交易返回码 0000 代表成功，错误种类，参考支付文档
	private String DESC;// 交易返回码信息
	public String getCODE() {
		return CODE;
	}
	public void setCODE(String cODE) {
		CODE = cODE;
	}
	public String getDESC() {
		return DESC;
	}
	public void setDESC(String dESC) {
		DESC = dESC;
	}
	
}
