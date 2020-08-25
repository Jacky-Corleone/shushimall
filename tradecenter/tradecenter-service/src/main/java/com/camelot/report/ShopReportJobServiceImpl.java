package com.camelot.report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.report.dao.ShopReportJobDao;
import com.camelot.report.dto.ShopReportDTO;
import com.camelot.report.service.ShopReportJobService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;

@Service("shopReportJobService")
public class ShopReportJobServiceImpl implements ShopReportJobService {

	private final static Logger logger = LoggerFactory.getLogger(ShopReportJobServiceImpl.class);
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private ShopReportJobDao shopReportJobDao;

	@Override
	public void insertReportShopReportByShop(Date yesterdayDate,Date todayDate) {
		logger.info("\n 方法[{}]，入参：[{}]","ShopReportJobServiceImpl-insertReportShopReportByShop",JSONObject.toJSONString(yesterdayDate),
				JSONObject.toJSONString(todayDate));
		
		ShopDTO shop=new ShopDTO();
		shop.setStatus(5);
		shop.setPassTimeBegin(yesterdayDate);
		shop.setPassTimeEnd(todayDate);
		ExecuteResult<DataGrid<ShopDTO>> result = shopExportService.findShopInfoByCondition(shop, null);
		DataGrid<ShopDTO> dataGrid = result.getResult();
		List<ShopDTO> list = dataGrid.getRows();
		
		logger.info("=====把店铺相关字段插入到tradecenter.report_sell_shop表====开始===");
		for (ShopDTO shopDTO : list) {
			ShopReportDTO shopReportDTO=new ShopReportDTO();
			shopReportDTO.setShopId(shopDTO.getShopId());
			shopReportDTO.setShopName(shopDTO.getShopName());
			shopReportDTO.setSellerId(shopDTO.getSellerId());
			shopReportDTO.setPassTime(shopDTO.getPassTime());
			shopReportDTO.setProvinceCode(shopDTO.getProvinceCode());
			shopReportDTO.setProvinceName(shopDTO.getProvinceName());
			shopReportDTO.setZcode(shopDTO.getZcode());
			shopReportDTO.setStreetName(shopDTO.getStreetName());
			shopReportDTO.setMobile(shopDTO.getMobile());
			shopReportDTO.setOrderNum(new BigDecimal(0));
			shopReportDTO.setSaleNum(new BigDecimal(0));
			shopReportDTO.setCustomerNum(new BigDecimal(0));
			shopReportJobDao.insertReportShopReportByShop(shopReportDTO);
		}
		logger.info("=====把店铺相关字段插入到tradecenter.report_sell_shop表====结束===");
	}

	@Override
	public void updateReportShopReportByOrder(String yesterday) {
		logger.info("\n 方法[{}]，入参：[{}]","ShopReportJobServiceImpl-updateReportShopReportByOrder",JSONObject.toJSONString(yesterday));
		logger.info("=====更新tradecenter.report_sell_shop表的统计字段====开始===");
		shopReportJobDao.updateReportShopReportByOrder(yesterday);
		logger.info("=====更新tradecenter.report_sell_shop表的统计字段====结束===");
	}
	
}
