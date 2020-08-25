package com.camelot.tradecenter.job;

import javax.annotation.Resource;

import com.camelot.tradecenter.service.TradeOrderExportService;

public class ConfirmOrderJob {

	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
	public void confirmOrder(){
		this.tradeOrderExportService.confirmOrderAuto();
	}
	
}
