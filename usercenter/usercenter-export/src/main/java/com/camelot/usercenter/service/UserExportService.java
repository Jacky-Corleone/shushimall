package com.camelot.usercenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserType;

/**
 * 
 * <p>Description: [用户的处理接口类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">胡恒心</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserExportService {

	/**
	 * 
	 * <p>Discription:[添加用户信息的接口]</p>
	 * Created on 2015年2月3日
	 * @param registerInfoDTO
	 * @return
	 * @author:[胡恒心]
	 */
	public Long registerUser(RegisterInfoDTO registerInfoDTO);
	
	/**
	 * 
	 * <p>Discription:[修改用户密码]</p>
	 * Created on 2015年2月3日
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> modifyUserPw(String username, String newPassword, String oldPassword);
	
	/**
	 * 
	 * <p>Discription:[通过邮箱修改用户密码]</p>
	 * Created on 2015年2月3日
	 * @param email
	 * @param newPassword
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> modifyUserPwdByEmail(String email, String newPassword);
	
	/**
	 * 
	 * <p>Discription:[通过用户名修改用户密码]</p>
	 * Created on 2015年2月3日
	 * @param username
	 * @param newPassword
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<String> modifyUserPwdByUsername(String username, String newPassword);
	
	/**
	 * 
	 * <p>Discription:[验证用户登录]</p>
	 * Created on 2015年2月3日
	 * @param username
	 * @param password
	 * @return
	 * @author:[胡恒心]
	 */
	public ExecuteResult<LoginResDTO> login(String username, String password, String key);
	
	/**
	 * 
	 * <p>Discription:[校验用户名是否存在]</p>
	 * Created on 2015年2月5日
	 * @param loginname
	 * @return
	 * @author:[胡恒心]
	 */
	public boolean verifyLoginName(String loginname);

    /**
     * <p>Discription:校验注册名是否重复</p>
     * Created on 2015年7月8日
     * @param registerName
     * @return
     * @author:[董其超]
     */
    public boolean verifyRegisterName(String registerName);

    /**
     * <p>Discription:校验邮箱或手机号是否重复</p>
     * Created on 2015年2月5日
     * @param attr
     * @return
     * @author:胡恒心
     */
    public boolean verifyEmailOrMobile(String attr);

	/**
	 * <p>Discription:校验邮箱重复</p>
	 * Created on 2015年2月5日
	 * @param email
	 * @return
	 * @author:胡恒心
	 */
	public boolean verifyEmail(String email);

    /**
     * <p>Discription:校验手机号重复</p>
     * Created on 2015年2月5日
     * @param mobile
     * @return
     * @author:胡恒心
     */
    public boolean verifyMobile(String mobile);
	
	/**
	 * 
	 * <p>Discription:通过token从redis中获取用户信息</p>
	 * Created on 2015年2月6日
	 * @param key
	 * @return
	 * @author:胡恒心
	 */
	public RegisterDTO getUserByRedis(String key);

	/**
	 * 
	 * <p>Discription:通过key从redis中获取value</p>
	 * Created on 2015年2月6日
	 * @param key
	 * @return
	 * @author:胡恒心
	 */
	public String getValueByRedis(String key);

	/**
	 * 
	 * <p>Discription:通过key从redis中删除记录</p>
	 * Created on 2015年2月6日
	 * @param key
	 * @return
	 * @author:胡恒心
	 */
	public void removeValueByRedis(String key);

	/**
	 * 
	 * <p>Discription:保存验证码到Redis</p>
	 * Created on 2015年2月8日
	 * @param key
	 * @param value
	 * @author:胡恒心
	 */
	public void saveVerifyCodeToRedis(String key, String value);
	
	/**
	 * 
	 * <p>Discription:通过用户名获取用户信息</p>
	 * Created on 2015年3月4日
	 * @param username
	 * @return
	 * @author:胡恒心
	 */
	public RegisterDTO getUserInfoByUsername(String username);
	
	/**
	 * 
	 * <p>Discription:通过用户ID修改用户信息，不为空则视为被修改条目</p>
	 * Created on 2015年2月5日
	 * @param userDTO
	 * @return
	 * @author:胡恒心
	 * @modifyUser - learrings
	 * @modifyContent : 方法定义范围扩大
	 */
	public boolean modifyUserInfo(UserDTO userDTO);

	/**
	 * 手机重新绑定之后，设置为空
	 * @param userDTO
	 * @return
	 */
	public boolean modifyUserInfoByMobile(UserDTO userDTO);
	
	/**
	 * 
	 * <p>Discription:[修改用户信息 以及用户审核信息]</p>
	 * Created on 2015-3-17
	 * @param userDTO
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<String> modifyUserInfoAndAuditStatus(UserDTO userDTO);
	/**
	 * 
	 * <p>Discription:用户ID查找用户</p>
	 * Created on 2015年2月6日
	 * @param uid
	 * @return
	 * @author:胡恒心
	 */
	public UserDTO queryUserById(long uid);
	
	
	/**
	 * 
	 * <p>Discription:用户父账号ID查找用户</p>
	 * Created on 2015年2月6日
	 * @param uid
	 * @return
	 * @author:胡恒心
	 */
	public UserDTO queryUserByfId(long uid);
	
	
	/**
	 * <p>根据条件查找用户</p>
	 * 
	 * @param userDTO - 查询条件,可空
	 * @param userType - 用户类型,可空
	 * @param pager - 分页,可空
	 */
	@SuppressWarnings("rawtypes") 
	public DataGrid<UserDTO> findUserListByCondition(UserDTO userDTO,UserType userType,Pager pager);
	
	
	/**
	 * <p>根据条件查找主账户用户</p>
	 * 
	 * @param userDTO - 查询条件,可空
	 * @param userType - 用户类型,可空
	 * @param pager - 分页,可空
	 */
	@SuppressWarnings("rawtypes") 
	public DataGrid<UserDTO> findUserListPid(UserDTO userDTO,UserType userType,Pager pager);
	
	
	
	/**
	 * <p>根据	用户名or邮箱or手机号码	查找用户</p>
	 * 
	 * @param userDTO - 查询条件,用户名or邮箱or手机号码	至少一个不为空
	 * @return	UserDTO
	 * @author:[李伟龙]
	 */
	public UserDTO findUserByUserNameOrEmailOrPhone(UserDTO userDTO);

    /** 根据条件查找用户
     *
     * @param userDTO
     * @param userType
     * @param idList
     * @param pager
     * @return
     */
    public DataGrid<UserDTO> queryUserListByCondition(UserDTO userDTO,UserType userType,List<String> idList, Pager pager);

	/**
	 * 
	 * <p>Discription:[根据用户IDLIST 查询用户]</p>
	 * Created on 2015-3-17
	 * @param idList
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<List<UserDTO>>findUserListByUserIds(List<String> idList);
	
	/**
	 * <p>根据用户ID修改缴费状态</p>
	 * 
	 * @param uid - 用户ID
	 * @param comStatus - 缴费状态
	 * @return 
	 */
	public ExecuteResult<String> modifyPayStatusByUId(long uid,ComStatus comStatus);
	
	/**
	 * <p>根据用户ID修改审核状态</p>
	 * 
	 * @param uid - 用户ID
	 * @param comStatus - 审核状态
	 * @return 
	 */
	public ExecuteResult<String> modifyAuditStatusByUId(long uid,ComStatus comStatus);
	/**
	 * 
	 * <p>Discription:[用户ID 用户类型 用户状态必传 用户审核状态 比传 返回用户的最终状态]</p>
	 * Created on 2015-3-12
	 * @param userDto
	 * @param auditUserId
	 * @param remark
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<UserDTO> modifyUserAuditStatusByUserIdAndAuditId(UserDTO userDto, String auditUserId, String remark) throws CommonCoreException;

    /**
     * 买家 快捷认证
     * @param userDto
     * @param auditUserId
     * @param remark
     * @return
     * @throws CommonCoreException
     */
    public ExecuteResult<UserDTO> quickAuditUser(UserDTO userDto,String auditUserId,String remark)throws  CommonCoreException;
	/**
	 * <p>Discription:修改支付密码</p>
	 * Created on 2015年3月7日
	 * @param uid - 用户ID
	 * @param paypwd - 加密后的支付密码
	 * @return
	 * @author:胡恒心
	 */
	public ExecuteResult<String> modifyPaypwdById(long uid, String paypwd, String oldpwd,String securityLevel);
	
	/**
	 * <p>Discription:更新redis里的用户信息</p>
	 * Created on 2015年3月12日
	 * @author:胡恒心
	 */
	public void updateUserInfoToRedis(String token, RegisterDTO registerDTO);
	
	/**
	 * 
	 * <p>Discription:[校验 支付密码 成功返回 1 失败返回0 ]</p>
	 * Created on 2015-3-24
	 * @param uid
	 * @param paypwd
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<String> validatePayPassword(long uid, String paypwd);
	
	/**
	 * <p>Description: [校验 登录密码 成功返回 1 失败返回0]</p>
	 * Created on 2015年8月7日
	 * @param uid
	 * @param loginpwd
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> validateLoginPassword(long uid, String loginpwd);
	
	/**
	 * 
	 * <p>Discription:[用户重置密码  传入用户ID 和加密后默认密码]</p>
	 * Created on 2015-4-2
	 * @param userId
	 * @param resetPassword
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<String> resetUserPassword(Long userId, String resetPassword);

    /**
     * 冻结账号 和 恢复账号
     * @param userId
     * @return
     */
    public ExecuteResult<String> frozenUser(String userId,CommonEnums.FrozenStatus status);

    /* 子账号申请 跟父账号互换
     *
     * @param sonUser
     * @return
     */
    public ExecuteResult<UserDTO> changeParentAndSonAccount(UserDTO sonUser);
    
    /**
     * 依据ID获取所有的父子账号ID集合
     * @param uid
     * @return
     */
    public ExecuteResult<List<Long>> queryUserIds(Long uid);
    
    /**
     * 
     * <p>Discription:[校验用户是否设置了支付密码]</p>
     * Created on 2016年3月8日
     * @param uid 用户ID
     * @return
     * @author:[宋文斌]
     */
    public ExecuteResult<String> validateExistPaymentCode(Long uid);
    
}
