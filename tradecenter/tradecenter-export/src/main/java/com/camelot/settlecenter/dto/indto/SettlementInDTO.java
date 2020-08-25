package com.camelot.settlecenter.dto.indto;

import java.io.Serializable;
import java.util.Date;

public class SettlementInDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long cid ;//类目Id
	private Long shopId;//店铺id
	private Long[] shopIds;//店铺ID组
	private Long settlement_id;//结算单号
	
	private Date settlementDatestr;//应结算日期开始
	private Date settlementDateend;//应结算日期结束
	
	private Date havePaidDatestr;//结算日期开始
	private Date havePaidDateend;//结算日期结束	
	
	private Date  billDatestr; //出账日期开始
	private Date  billDateend;//出账日期结束
	private Integer status; //结算状态

	
	
	public Date getBillDatestr() {
		return billDatestr;
	}

	public void setBillDatestr(Date billDatestr) {
		this.billDatestr = billDatestr;
	}

	public Date getBillDateend() {
		return billDateend;
	}

	public void setBillDateend(Date billDateend) {
		this.billDateend = billDateend;
	}

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public Long getSettlement_id() {
		return settlement_id;
	}

	public void setSettlement_id(Long settlement_id) {
		this.settlement_id = settlement_id;
	}

	public Date getSettlementDatestr() {
		return settlementDatestr;
	}

	public void setSettlementDatestr(Date settlementDatestr) {
		this.settlementDatestr = settlementDatestr;
	}

	public Date getSettlementDateend() {
		return settlementDateend;
	}

	public void setSettlementDateend(Date settlementDateend) {
		this.settlementDateend = settlementDateend;
	}

	public Date getHavePaidDatestr() {
		return havePaidDatestr;
	}

	public void setHavePaidDatestr(Date havePaidDatestr) {
		this.havePaidDatestr = havePaidDatestr;
	}

	public Date getHavePaidDateend() {
		return havePaidDateend;
	}

	public void setHavePaidDateend(Date havePaidDateend) {
		this.havePaidDateend = havePaidDateend;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long[] getShopIds() {
		return shopIds;
	}

	public void setShopIds(Long[] shopIds) {
		this.shopIds = shopIds;
	}
	
	
	
	
}
