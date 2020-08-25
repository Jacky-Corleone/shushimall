package com.camelot.ecm.job;

import java.util.Date;

import javax.annotation.Resource;

import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;

/**
 * ecm中轮播图定时上下架
 * 
 * Created on 2015年11月3日
 * @author wangdongxiao
 *
 */
public class MallBannerJob {

	@Resource
	private MallBannerExportService mallBannerExportService;
	/**
	 * ecm中轮播图定时上下架
	 */
	public void motifyMallBannerStatusAuto(){
		//获取当前时间
		//定时下架
		mallBannerExportService.autoBatchRelease();
		//定时上架
		mallBannerExportService.autoBatchOffShelves();
		
	}
}
