package com.camelot.mall.userIntegral;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;

/** 
 * <p>Description: [用户积分]</p>
 * Created on 2015-12-9
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class UserIntegralTrajectoryServiceImpl implements
		UserIntegralTrajectoryService {
	
	private final static Logger log = LoggerFactory.getLogger(UserIntegralTrajectoryServiceImpl.class);
	@Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	@Resource
	private IntegralConfigExportService integralConfigService;
	
	@Override
	public void saveUserIntegral(
			UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,BigDecimal amount) {
		log.info("\n 方法[{}]，入参：[{}]","UserIntegralTrajectoryServiceImpl-saveUserIntegral",JSON.toJSONString(userIntegralTrajectoryDTO));
		userIntegralTrajectoryDTO = getIntegralValue(userIntegralTrajectoryDTO,amount);
		if(null == userIntegralTrajectoryDTO){
			return ;
		}else{
			userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
			log.info("获取积分成功");
		}
	}
	
	private UserIntegralTrajectoryDTO getIntegralValue(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,BigDecimal amount){
		log.info("\n 方法[{}]，入参：[{},{}]","UserIntegralTrajectoryServiceImpl-getIntegralValue",JSON.toJSONString(userIntegralTrajectoryDTO),amount);
		// 校验是否已获取积分
		ExecuteResult<DataGrid<UserIntegralTrajectoryDTO>> userIntegral = userIntegralTrajectoryService.queryUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO, null);
		if(!userIntegral.isSuccess() || 0 < userIntegral.getResult().getTotal()){
			log.info("获取积分失败，已获取积分");
			return null;
		}
		// 评论获取积分
		if(null == amount){
			IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
			integralConfigDTO.setPlatformId(0);
			integralConfigDTO.setIntegralType(userIntegralTrajectoryDTO.getIntegralType());
			ExecuteResult<DataGrid<IntegralConfigDTO>> integralEr = integralConfigService.queryIntegralConfigDTO(integralConfigDTO, null);
			if(integralEr.isSuccess() && integralEr.getResult().getRows().size() == 1){
				IntegralConfigDTO backIntegral = integralEr.getResult().getRows().get(0);
				userIntegralTrajectoryDTO.setIntegralValue(backIntegral.getGetIntegral().longValue());
				return userIntegralTrajectoryDTO;
			}else{
				log.info("评论获取积分失败，配置信息不存在或不唯一");
				return null;
			}
		// 订单获取积分
		}else{
			ExecuteResult<List<IntegralConfigDTO>> integralEr = integralConfigService.queryIntegralConfigDTOByMoney(amount,0L);
			if(integralEr.isSuccess() && integralEr.getResult().size() == 1){
				IntegralConfigDTO backIntegral = integralEr.getResult().get(0);
				Long val = backIntegral.getGetIntegral().multiply(amount).longValue();
				if(val > 0){
					userIntegralTrajectoryDTO.setIntegralValue(val);
				}else{
					log.info("订单获取积分失败，积分值为0");
					return null;
				}
				return userIntegralTrajectoryDTO;
			}else{
				log.info("订单获取积分失败，配置信息不存在或不唯一");
				return null;
			}
		}
	}

}
