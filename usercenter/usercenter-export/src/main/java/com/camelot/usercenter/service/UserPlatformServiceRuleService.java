package com.camelot.usercenter.service;


import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.dto.userrule.UserPlatformServiceRuleDTO;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [商家 与平台 规则中间表 增删改查]</p>
 * Created on 2015-3-11
 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserPlatformServiceRuleService {
	/**
	 * 
	 * <p>Discription:[根据ID查询中间表]</p>
	 * Created on 2015-3-11
	 * @param id
	 * @return
	 * @author:[liuqingshan]
	 */
	
	public  ExecuteResult<UserPlatformServiceRuleDTO> getUserPlatformServiceRuleById(long id);
	
	/**
	 * 
	 * <p>Discription:[用户新增 平台规则]</p>
	 * Created on 2015-3-11
	 * @param userPlatformServiceRuleDTO
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<UserPlatformServiceRuleDTO> saveUserPlatformServiceRule(UserPlatformServiceRuleDTO userPlatformServiceRuleDTO);
	/**
	 * 
	 * <p>Discription:[用户  退出 平台规则   成功返回1 失败返回0 ]</p>
	 * Created on 2015-3-11
	 * @param userPlatformServiceRuleDTO 需传入 USERID 和 platformRuleId
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<Integer> delUserPlatformService(UserPlatformServiceRuleDTO userPlatformServiceRuleDTO);
	
	/**
	 * 
	 * <p>Discription:[修改记录]</p>
	 * Created on 2015-3-11
	 * @param userPlatformServiceRuleDTO
	 * @return
	 * @author:[创建者中文名字]
	 */
	public ExecuteResult<UserPlatformServiceRuleDTO> modifyUserPlatformServiceRule(UserPlatformServiceRuleDTO userPlatformServiceRuleDTO);
	
	/**
	 * 
	 * <p>Discription:[根据用户USERID 查询用户 加入的平台服务规则]</p>
	 * Created on 2015-3-11
	 * @param userId
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<List<UserPlatformServiceRuleDTO>> getUserPlatformRuleList(String userId);

    /**
     *
     * <p>Discription:[根据用户USERID 查询用户 加入的平台服务规则]</p>
     * Created on 2015-3-11
     * @param userIds
     * @return
     * @author:[zhoule]
     */
    public ExecuteResult<List<UserPlatformServiceRuleDTO>> getUserPlatformRuleList(String[] userIds);

	/**
	 * 
	 * <p>Discription:[服务认证统计 返回RULEID  和 usrCount 用户数量]</p>
	 * Created on 2015-3-23
	 * @return
	 * @author:[liuqingshan]
	 */
	public DataGrid<UserPlatformServiceRuleDTO> getUserPlatformRuleStatistics(UserPlatformServiceRuleDTO dto,Pager pager);
	/**
	 * 
	 * <p>Discription:[根据平台 规则ID  认证时间查询 认证用户信息]</p>
	 * Created on 2015-3-23
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[liuqingshan]
	 */
	public DataGrid<UserPlatformServiceRuleDTO> getUserPlatformRuleDetail(UserPlatformServiceRuleDTO dto,Pager pager);
	
}
