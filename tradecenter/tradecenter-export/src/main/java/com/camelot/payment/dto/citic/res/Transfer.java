package com.camelot.payment.dto.citic.res;

import java.io.Serializable;


/**
 *  附属账户强制转账对象（发送）
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-14
 */
public class Transfer implements Serializable{

	private static final long serialVersionUID = 8854734318225724290L;
	private String clientID;//<!--客户流水号varchar (20) -->
	private String payAccNo;//<!--付款账号varchar(19) -->
	private String tranType;//<!--转账类型varchar(2) "BF"：转账；"BG"：解冻；"BH"：解冻支付-->
	private String recvAccNo;//<!--收款账号varchar(19) -->
	private String recvAccNm;//<!--收款账户名称varchar(60) -->
	private String tranAmt;//<!--交易金额decimal(15,2) -->
	private String freezeNo;//<!--冻结编号varchar(22) 转账类型为“解冻”或“解冻支付”时，必输-->
	private String memo="";//<!--摘要varchar(22) 可空-->
	public String getClientID() {
		return clientID;
	}
	public void setClientID(String clientID) {
		this.clientID = clientID;
	}
	public String getPayAccNo() {
		return payAccNo;
	}
	public void setPayAccNo(String payAccNo) {
		this.payAccNo = payAccNo;
	}
	public String getTranType() {
		return tranType;
	}
	public void setTranType(String tranType) {
		this.tranType = tranType;
	}
	public String getRecvAccNo() {
		return recvAccNo;
	}
	public void setRecvAccNo(String recvAccNo) {
		this.recvAccNo = recvAccNo;
	}
	public String getRecvAccNm() {
		return recvAccNm;
	}
	public void setRecvAccNm(String recvAccNm) {
		this.recvAccNm = recvAccNm;
	}
	public String getTranAmt() {
		return tranAmt;
	}
	public void setTranAmt(String tranAmt) {
		this.tranAmt = tranAmt;
	}
	public String getFreezeNo() {
		return freezeNo;
	}
	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
