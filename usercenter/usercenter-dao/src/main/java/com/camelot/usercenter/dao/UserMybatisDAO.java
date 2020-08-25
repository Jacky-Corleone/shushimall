package com.camelot.usercenter.dao;

import java.util.Date;
import java.util.List;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.report.UserAndShopReportDTO;
import com.camelot.usercenter.dto.report.UserReportDTO;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;

/**
 * 
 * <p>Description: [用户处理的DAO类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">胡恒心</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserMybatisDAO extends BaseDAO<UserDTO> {
	
	/**
	 * 
	 * <p>Discription:[添加用户信息]</p>
	 * Created on 2015年2月3日
	 * @param dto
	 * @return
	 * @author:[胡恒心]
	 */
	public void registerUser(RegisterInfoDTO dto);
	
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
	public int modifyUserPw(@Param("username") String username, @Param("newPassword") String newPassword, @Param("oldPassword") String oldPassword);
	
	/**
	 * 
	 * <p>Discription:[通过邮箱修改用户密码]</p>
	 * Created on 2015年2月3日
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 * @author:[胡恒心]
	 */
	public int modifyUserPwdByEmail(@Param("email") String email, @Param("newPassword") String newPassword);
	
	/**
	 * 
	 * <p>Discription:[通过用户名修改用户密码]</p>
	 * Created on 2015年2月3日
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 * @author:[胡恒心]
	 */
	public int modifyUserPwdByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
	
	public int resetUserPassword(@Param("uid")Long uid,@Param("resetPassword")String resetPassword);
	/**
	 * 
	 * <p>Discription:[验证用户登录，成功则返回昵称]</p>
	 * Created on 2015年2月3日
	 * @param username
	 * @param password
	 * @return
	 * @author:[胡恒心]
	 */
	public RegisterDTO login(@Param("username") String username, @Param("password") String password);

	/**
	 * 
	 * <p>Discription: 更新 用户的基本信息</p>
	 * Created on 2015年2月5日
	 * @param uid
	 * @param email
	 * @return
	 * @author:胡恒心
	 */
	public int modifyUserInfo(UserDTO userDTO);

	/**
	 * <p>Discription: 更新 用户的基本信息</p>
	 * @param userDTO
	 * @return
	 */
	public int modifyUserInfoByMobile(UserDTO userDTO);

    /**
     *
     * <p>Discription:校验注册名是否重复</p>
     * Created on 2015年7月8日
     * @param uname
     * @return
     * @author:董其超
     */
    public int verifyRegisterName(@Param("uname") String uname);

	/**
	 * 
	 * <p>Discription:校验邮箱是否重复</p>
	 * Created on 2015年2月5日
	 * @param email
	 * @return
	 * @author:胡恒心
	 */
	public int verifyEmail(@Param("email") String email);

	/**
	 * 
	 * <p>Discription:校验手机号是否重复</p>
	 * Created on 2015年2月5日
	 * @param mobile
	 * @return
	 * @author:胡恒心
	 */
	public int verifyMobile(@Param("mobile") String mobile);
	
	/**
	 * 
	 * <p>Discription:用户ID查找用户信息</p>
	 * Created on 2015年2月6日
	 * @param uid
	 * @return
	 * @author:胡恒心
	 * @modifyUser learrings
	 * @modifyContent 增加用户类型展示
	 */
	public UserDTO queryUserById(long uid);
	
	/**
	 * 
	 * <p>Discription:用户父账号ID查找用户</p>
	 * Created on 2015年11月9日15:38:57
	 * @param uid
	 * @return
	 * @author:zhm
	 */
	public UserDTO queryUserByfId(long uid);

	/**
	 * <p>Discription:修改支付密码</p>
	 * Created on 2015年3月7日
	 * @param uid - 用户ID
	 * @param paypwd - 加密后的支付密码
	 * @return
	 * @author:胡恒心
	 */
	public int modifyPaypwdById(@Param("uid") long uid, @Param("paypwd") String paypwd, @Param("oldpwd") String oldpwd,@Param("securityLevel") String securityLevel);
	
	/**
	 * 
	 * <p>Discription:[根据条件查询用户基本信息]</p>
	 * Created on 2015-3-13
	 * @param userDTO
	 * @return
	 * @author:[liuqingshan]
	 */
	public UserDTO queryUserByCondtion(@Param("entity")UserDTO userDTO);
	
	/**
	 * 
	 * <p>Discription:[根据用户 IDS 查询用户LIST]</p>
	 * Created on 2015-3-17
	 * @param idList
	 * @return
	 * @author:[liuqingshan]
	 */
	public List<UserDTO> findUserListByUserIds(@Param("idList")List<String> idList);
	/**
	 * 
	 * <p>Discription:[添加子账号]</p>
	 * Created on 2015-3-17
	 * @param registerInfoDTO
	 * @author:yuht
	 */
	public void registerChildUser(RegisterInfoDTO registerInfoDTO);
	/**
	 * 
	 * <p>Discription:[根据父级id查询所有子账户]</p>
	 * Created on 2015-3-18
	 * @param parentId
	 * @return
	 * @author:yuht
	 */
	public List<UserDTO> queryChildUserList(@Param("parentId")Long parentId,@Param("pager") Pager pager);
	/**
	 * 
	 * <p>Discription:[根据父级id查询所有子账户数据条数]</p>
	 * Created on 2015-3-18
	 * @param parentId
	 * @return
	 * @author:yuht
	 */
	public Long queryChildUserCount(Long parentId);
	
	/**
	 * 
	 * <p>Discription:[校验 用户支付密码]</p>
	 * Created on 2015-3-24
	 * @param uid
	 * @param paypassword
	 * @return
	 * @author:[liuqingshan]
	 */
	public Long validatePayPassword(@Param("uid")Long uid,@Param("paypassword")String paypassword);
	
	/**
	 * 
	 * <p>Discription:[校验 用户是否设置了支付密码]</p>
	 * Created on 2016年3月8日
	 * @param uid
	 * @return
	 * @author:[宋文斌]
	 */
	public Long validateExistPaymentCode(@Param("uid")Long uid);
	
	/**
	 * <p>Description: [校验 用户登录密码]</p>
	 * Created on 2015年8月7日
	 * @param uid
	 * @param paypassword
	 * @return
	 * @author:[宋文斌]
	 */
	public Long validateLoginPassword(@Param("uid")Long uid,@Param("loginpassword")String loginpassword);

    public String getUserIdFun();

    /**
     * 查询 商家入驻报表
     * @return
     */
    public UserAndShopReportDTO getCustomerAndShopReportSum();

    public List<UserAndShopReportDTO> getCustomerAndShopReportByCondition(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);


    /**
     * 查询用户每日新增报表
     */
   public List<UserReportDTO> getUserCreateReportByCreateDt(@Param("beginDate")Date beginDate, @Param("endDate")Date endDate);
   

	/**
	 * <p>Discription:用户名查找用户信息</p>
	 * Created on 2015年9月7日
	 * @param UserDTO
	 * @return	UserDTO
	 * @author:李伟龙
	 */
	public UserDTO selectUserByUname(UserDTO userDTO);
	/**
	 * <p>Discription:用户邮箱查找用户信息</p>
	 * Created on 2015年9月7日
	 * @param UserDTO
	 * @return	UserDTO
	 * @author:李伟龙
	 */
	public UserDTO selectUserByUserEmail(UserDTO userDTO);
	/**
	 * <p>Discription:用户电话号码查找用户信息</p>
	 * Created on 2015年9月7日
	 * @param UserDTO
	 * @return	UserDTO
	 * @author:李伟龙
	 */
	public UserDTO selectUserByUmobile(UserDTO userDTO);
	/**
	 * 获取用户组IDS
	 * @param uid
	 * @return
	 * @author 周志军
	 */
	public List<Long> queryUserIds(@Param("uid")Long uid);
	/**
	 * 修改子账号类型
	 * @param userDto
	 */
	public int modifyChildUserInfo(UserDTO userDto);

	public long selectCountPid(@Param("entity")UserDTO userDTO);

	public List<UserDTO> selectparentId(@Param("entity")UserDTO userDTO, @Param("pager") Pager pager);
	/**
	 * 通过用户名查找用户
	 * @param userName
	 * @return
	 */
	public RegisterDTO findUserByLonginName(@Param("userName")String userName);
}
