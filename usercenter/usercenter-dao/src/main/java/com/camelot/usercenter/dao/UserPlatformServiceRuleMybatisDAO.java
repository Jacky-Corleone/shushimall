package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.usercenter.domain.UserPlatformServiceRule;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 
 * <p>Description: [用户与平台服务规则关系表DAO]</p>
 * Created on 2015-3-10
 * @author  <a href="mailto: xxx@camelotchina.com">liuqsh</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserPlatformServiceRuleMybatisDAO  extends BaseDAO<UserPlatformServiceRule>{
	
	public List<UserPlatformServiceRule> getUserPlatformRuleListByUserId(@Param("userId")String uid);

    public List<UserPlatformServiceRule> getUserPlatformRuleListByUserIds(@Param("userIds")String[] uids);
	
	public Integer updateUserRuleByUserIdAndRuleId(UserPlatformServiceRule t);
	
	public List<UserPlatformServiceRule> selectRuleStatistics(@Param("entity") UserPlatformServiceRule t, @Param("pager") Pager pager);
	
	public Long selectRuleStatisticsCount(@Param("entity") UserPlatformServiceRule t);
	
	public List<UserPlatformServiceRule> selectRuleDetails(@Param("entity") UserPlatformServiceRule t, @Param("pager") Pager pager);
	
	public Long selectRuleDetailsCount(@Param("entity") UserPlatformServiceRule t);
}