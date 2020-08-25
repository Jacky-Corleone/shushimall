package com.camelot.settlecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class SettlementInfoOutDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	

	private BigDecimal rebateRate;//  返点率


	public BigDecimal getRebateRate() {
		return rebateRate;
	}
	public void setRebateRate(BigDecimal rebateRate) {
		this.rebateRate = rebateRate;
	}

	
	

}
