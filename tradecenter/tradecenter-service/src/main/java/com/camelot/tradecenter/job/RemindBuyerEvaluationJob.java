package com.camelot.tradecenter.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.util.MessageTemplateFileUtil;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/**
 * 
 * <p>Description: [买家商品未评价,提醒买家评价]</p>
 * Created on 2016年3月23日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class RemindBuyerEvaluationJob {
	
	private static final Logger logger = LoggerFactory.getLogger(RemindBuyerEvaluationJob.class);

	@Resource
	private UserExportService userExportService;
	
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	
	public void remindBuyerEvaluation(){
		logger.info("=====提醒买家评价定时任务开始=================");
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		Date date = cal.getTime();
		
		this.remindBuyerEvaluation(date);
		
		logger.info("======提醒买家评价定时任务结束=================");
	}
	
	private void remindBuyerEvaluation(Date date) {
		logger.info("\n 方法[{}]，入参：[{}]", "RemindBuyerEvaluationJob-remindBuyerEvaluation", JSONObject.toJSONString(date));
		try {
			TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
			inDTO.setDeleted(1); // 未删除
			inDTO.setState(4); // 待评价
			ExecuteResult<DataGrid<TradeOrdersDTO>> orders = tradeOrderExportService.queryOrders(inDTO, null);
			if (orders.isSuccess() && orders.getResult() != null) {

				List<TradeOrdersDTO> list = orders.getResult().getRows();
				for (TradeOrdersDTO tradeOrdersDTO : list) {
					if (tradeOrdersDTO.getConfirmReceiptTime() == null) {
						continue;
					}
					if (DateUtils.dayDiff(date, tradeOrdersDTO.getConfirmReceiptTime()) == 0) {
						UserDTO userDTO = this.userExportService.queryUserById(tradeOrdersDTO.getBuyerId());
						// 获取短信模板信息
						MessageTemplateFileUtil messageTemplateFileUtil = MessageTemplateFileUtil.getInstance();
						String wsMessage = messageTemplateFileUtil.getValue("ws_remind_evaluation_b");
						Integer platformId = tradeOrdersDTO.getPlatformId();
						WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
						webSiteMessageDTO.setWmRead(1);
						webSiteMessageDTO.setWmCreated(new Date());
						webSiteMessageDTO.setModified(new Date());
						webSiteMessageDTO.setType(1);
						webSiteMessageDTO.setWmToUserid(tradeOrdersDTO.getBuyerId());
						webSiteMessageDTO.setPlatformId(platformId);
						if (userDTO != null) {
							webSiteMessageDTO.setWmToUsername(userDTO.getUname());
						}
						// 信息内容
						if(StringUtils.isNotBlank(wsMessage)){
							wsMessage = wsMessage.replaceAll("orderId", tradeOrdersDTO.getOrderId());
							webSiteMessageDTO.setWmContext(wsMessage);
							baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("\n 方法[{}]，异常：[{}]", "RemindBuyerEvaluationJob-remindBuyerEvaluation", e);
		}
	}
	
}
