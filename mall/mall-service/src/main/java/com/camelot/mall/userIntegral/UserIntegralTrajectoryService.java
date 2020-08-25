package com.camelot.mall.userIntegral;

import java.math.BigDecimal;

import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;

/** 
 * <p>Description: [用户积分]</p>
 * Created on 2015-12-9
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface UserIntegralTrajectoryService {
	/** 
	 * <p>Description: [保存用户积分]</p>
	 * Created on 2015-12-9
	 * @author  周志军
	 * @param userIntegralTrajectoryDTO
	 * @param amount
	 */
	public void saveUserIntegral(UserIntegralTrajectoryDTO userIntegralTrajectoryDTO,BigDecimal amount);
}
