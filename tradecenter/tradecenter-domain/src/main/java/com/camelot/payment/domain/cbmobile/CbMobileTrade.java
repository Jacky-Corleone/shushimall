package com.camelot.payment.domain.cbmobile;

import java.io.Serializable;
import java.util.Date;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-13
 */
@XStreamAlias("TRADE")
public class CbMobileTrade implements Serializable{
	private static final long serialVersionUID = -8607541451206463054L;
	private String TYPE;// 返回原交易类型 V[签约]/S[消费]/Q[查询]/R[退款]
	private String ID;// 交易号
	private String AMOUNT;// 交易金额	单位：分
	private String CURRENCY;// 交易币种	人民币：CNY
	private String DATE;//
	private String TIME;
	private String NOTE;
	private String STATUS;

	public String getTIME() {
		return TIME;
	}

	public void setTIME(String TIME) {
		this.TIME = TIME;
	}

	public String getNOTE() {
		return NOTE;
	}

	public void setNOTE(String NOTE) {
		this.NOTE = NOTE;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String STATUS) {
		this.STATUS = STATUS;
	}

	public String getDATE() {
		return DATE;
	}

	public void setDATE(String DATE) {
		this.DATE = DATE;
	}

	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAMOUNT() {
		return AMOUNT;
	}
	public void setAMOUNT(String aMOUNT) {
		AMOUNT = aMOUNT;
	}
	public String getCURRENCY() {
		return CURRENCY;
	}
	public void setCURRENCY(String cURRENCY) {
		CURRENCY = cURRENCY;
	}
	
}
