package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.usercenter.domain.UserPlatformServiceRule;
import com.camelot.usercenter.dto.UserCreditDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 
 * <p>Description: [用户积分]</p>
 * Created on 2015-4-14
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserCreditDAO  extends BaseDAO<UserCreditDTO>{
	
}