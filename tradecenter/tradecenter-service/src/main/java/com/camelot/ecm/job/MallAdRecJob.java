package com.camelot.ecm.job;

import javax.annotation.Resource;

import com.camelot.sellercenter.malladvertise.service.MallAdExportService;

/**
 * 商城广告定时上、下架
 * @author wangdongxiao
 *
 */
public class MallAdRecJob {

	@Resource
	private MallAdExportService mallAdExportService;
	
	public void execute(){
		mallAdExportService.autoBatchPublishAD();
		mallAdExportService.autoBatchCancelAD();
	}
}
