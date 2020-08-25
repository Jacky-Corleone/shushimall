package com.camelot.maketcenter.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.dto.emums.CouponUsingRangeEnum;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.util.MessageTemplateFileUtil;
import com.camelot.openplatform.util.DateUtils;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/**
 * 
 * <p>Description: [优惠券即将过期提醒]</p>
 * Created on 2016年3月23日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class CouponExpireRemindJob {
	
	private static final Logger logger = LoggerFactory.getLogger(CouponExpireRemindJob.class);

	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	
	@Resource
	private CouponsExportService couponsExportService;
	
	@Resource
	private UserExportService userExportService;
	
	public void remindCouponExpire(){
		logger.info("=====优惠券即将过期定时任务开始=================");
		
		try {
			
			// 查询用户领取的优惠券
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			couponUserDTO.setDeleted(0);// 未删除
			couponUserDTO.setUserCouponType(0);// 未使用
			ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, null);
			if (couponUserResult.isSuccess() 
					&& couponUserResult.getResult() != null 
					&& couponUserResult.getResult().getRows() != null
					&& couponUserResult.getResult().getRows().size() > 0) {
				// 用户领取的优惠券
				List<CouponUserDTO> couponUserDTOs = couponUserResult.getResult().getRows();
				for(CouponUserDTO dto : couponUserDTOs){
					// 查询优惠券过期时间
					ExecuteResult<CouponsDTO> result = couponsExportService.queryById(dto.getCouponId());
					if(result.isSuccess() && result.getResult() != null){
						CouponsDTO couponsDTO = result.getResult();
						Date couponEndTime = couponsDTO.getCouponEndTime();
						// 计算当前时间距离优惠券过期时间相差的天数
						Date now = new Date();
						int dayDiff = DateUtils.dayDiff(couponEndTime, now);
						if (dayDiff == 2) {// 提前两天发送消息
							UserDTO userDTO = this.userExportService.queryUserById(dto.getUserId());
							// 获取短信模板信息
							MessageTemplateFileUtil messageTemplateFileUtil = MessageTemplateFileUtil.getInstance();
							String wsMessage = messageTemplateFileUtil.getValue("ws_coupon_will_expire_b");
							WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
							webSiteMessageDTO.setWmRead(1);
							webSiteMessageDTO.setWmCreated(new Date());
							webSiteMessageDTO.setModified(new Date());
							webSiteMessageDTO.setType(1);
							webSiteMessageDTO.setWmToUserid(dto.getUserId());
							if (userDTO != null) {
								webSiteMessageDTO.setWmToUsername(userDTO.getUname());
							}
							// 信息内容
							if(StringUtils.isNotBlank(wsMessage)){
								String couponName = null;
								if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.PLATFORM.getCode()) {
									couponName = CouponUsingRangeEnum.PLATFORM.getLabel();
								} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.SHOP.getCode()) {
									couponName = CouponUsingRangeEnum.SHOP.getLabel();
								} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.CATEGORY.getCode()) {
									couponName = CouponUsingRangeEnum.CATEGORY.getLabel();
								} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.SKU.getCode()) {
									couponName = CouponUsingRangeEnum.SKU.getLabel();
								}
								wsMessage = wsMessage.replaceAll("coupon_name", couponName);
								webSiteMessageDTO.setWmContext(wsMessage);
								baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("\n 方法[{}]，异常：[{}]", "RemindBuyerEvaluationJob-remindBuyerEvaluation", e);
		}
		logger.info("======优惠券即将过期定时任务结束=================");
	}
}
