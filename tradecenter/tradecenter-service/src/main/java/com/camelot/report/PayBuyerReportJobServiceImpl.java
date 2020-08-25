package com.camelot.report;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.report.dao.PayBuyerReportJobDao;
import com.camelot.report.dto.PayBuyerInfo;
import com.camelot.report.service.PayBuyerReportJobService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

@Service("payBuyerReportJobService")
public class PayBuyerReportJobServiceImpl implements PayBuyerReportJobService {

	private final static Logger logger = LoggerFactory.getLogger(PayBuyerReportJobServiceImpl.class);
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private PayBuyerReportJobDao payBuyerReportJobDao;
	
	@Override
	public void insertReportPayBuyerByUser(String createTimeStart, String createTimeEnd) {
		logger.info("\n 方法[{}]，入参：[{}]","PayBuyerReportJobServiceImpl-insertReportPayBuyerByUser",JSONObject.toJSONString(createTimeStart),
				JSONObject.toJSONString(createTimeEnd));
		UserDTO userDTO=new UserDTO();
		userDTO.setCreateTimeBegin(createTimeStart);	//昨天
		userDTO.setCreateTimeEnd(createTimeEnd);	//今天
		
		DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(userDTO, null, null);
		logger.info("=====把用户相关字段插入到tradecenter.report_pay_buyer表====开始===");
		for (UserDTO user : dataGrid.getRows()) {
			PayBuyerInfo payBuyerInfo= new PayBuyerInfo();
			payBuyerInfo.setId(user.getUid());
			payBuyerInfo.setUserName(user.getUname());
			payBuyerInfo.setUmobile(user.getUmobile());
			payBuyerInfo.setCreateTime(user.getCreated());
			payBuyerInfo.setUserEmail(user.getUserEmail());
			payBuyerInfo.setUserType(user.getUsertype());
			payBuyerInfo.setParentId(user.getParentId());
			payBuyerInfo.setDeleted(user.getDeleted());
			payBuyerInfo.setOrderNum(new BigDecimal(0));
			payBuyerInfo.setShopNum(new BigDecimal(0));
			payBuyerInfo.setPayPriceTotal(new BigDecimal(0));
			payBuyerReportJobDao.insertReportPayBuyerByUser(payBuyerInfo);
		}
		logger.info("=====把用户相关字段插入到tradecenter.report_pay_buyer表====结束===");
	}

	@Override
	public void updateReportPayBuyerByOrder(String yesterday) {
		logger.info("\n 方法[{}]，入参：[{}]","PayBuyerReportJobServiceImpl-insertReportPayBuyerByUser",JSONObject.toJSONString(yesterday));
		logger.info("=====更新tradecenter.report_pay_buyer表的统计字段====开始===");
		payBuyerReportJobDao.updateReportPayBuyerByOrder(yesterday);
		logger.info("=====更新tradecenter.report_pay_buyer表的统计字段====开始===");
	}
	
}
