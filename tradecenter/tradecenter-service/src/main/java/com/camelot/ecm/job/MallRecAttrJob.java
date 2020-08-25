package com.camelot.ecm.job;

import javax.annotation.Resource;

import com.camelot.sellercenter.mallrecattr.service.MallRecAttrExportService;

/**
 * 商城楼层定时上下架
 * 
 * @author wangdongxiao
 *
 */

public class MallRecAttrJob {

	@Resource
	private MallRecAttrExportService mallRecAttrExportService;
	
	public void execute(){
		mallRecAttrExportService.autoBatchPublishMallRec();
		mallRecAttrExportService.autoBatchCancelMallRec();
	}
}
