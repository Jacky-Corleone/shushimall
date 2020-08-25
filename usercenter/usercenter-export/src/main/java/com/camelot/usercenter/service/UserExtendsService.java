package com.camelot.usercenter.service;


import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;

/**
 *  用户扩展对外接口
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-4
 */
public interface UserExtendsService {

	/**
	 * 买家/卖家用户扩展信息的添加，审核状态变为待审核（业务去除）
	 * 
	 * @param userInfoDTO - 用户扩展信息，用户ID(userId)、用户类型(userType)不为空
	 * @param userExtends - 用户扩展类型，可空
	 * @return 用户扩展信息/ 卖家合同信息-初始化
	 * @throws 必填项为空
	 * @modify - 2015-03-07 去除待审核的限制，自由填写
	 */
	public ExecuteResult<UserInfoDTO> saveUserExtends(UserInfoDTO userInfoDTO,UserExtendsType userExtendsType);

    /**
     * 新增用户扩展信息 全部保存
     *
     * @param userInfoDTO - 用户扩展信息，用户ID(userId)、用户类型(userType)不为空
     * @param userExtends - 用户扩展类型，可空
     * @return 用户扩展信息/ 卖家合同信息-初始化
     * @throws 必填项为空
     * @modify - 2015-03-07 去除待审核的限制，自由填写
     */
    public ExecuteResult<UserInfoDTO> createUserExtends(UserInfoDTO userInfoDTO);
    /**
     * 修改用户扩展信息 全部保存
     *
     * @param userInfoDTO - 用户扩展信息，用户ID(userId)、用户类型(userType)不为空
     * @param userExtends - 用户扩展类型，可空
     * @return 用户扩展信息/ 卖家合同信息-初始化
     * @throws 必填项为空
     * @modify - 2015-03-07 去除待审核的限制，自由填写
     */
    public ExecuteResult<UserInfoDTO> updateUserExtends(UserInfoDTO userInfoDTO);
	/**
	 * 用户扩展信息的修改,相关扩展项审核状态变为待审核（业务去除）
	 * 
	 * @param userInfoDTO - 用户扩展信息
	 * @param userExtends - 用户扩展类型，可空
	 * @return
	 * @modify - 2015-03-07 去除待审核的限制，自由填写
	 */
	public ExecuteResult<UserInfoDTO> modifyUserExtends(UserInfoDTO userInfoDTO,UserExtendsType userExtendsType);
	
	/**
	 * 用户基本信息及扩展信息的查询
	 * 
	 * @param uid - 用户ID
	 * @return
	 */
	public ExecuteResult<UserInfoDTO> findUserInfo(Long uid);
	
	/**
	 * 用户扩展信息的列表查询
	 * 
	 * @param userInfoDTO - 用户信息条件，可空
	 * @param pager - 分页，可空
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataGrid<UserInfoDTO> findUserInfoList(UserInfoDTO userInfoDTO,Pager pager);
	
	/**
	 * 根据类项修改其审核状态
	 * 
	 * @param extendId - 主键
	 * @param status - 状态
	 * @param userExtends - 用户扩展类型，可空
	 */
	public  ExecuteResult<String> modifyStatusByType(Long extendId,ComStatus comStatus,UserExtendsType userExtendsType);

	/**
	 * 
	 * <p>Discription:[根据供应商名称查询]</p>
	 * Created on 2015-10-26
	 * @param supplyId
	 * @return
	 * @author:[zhaoyaqiang]
	 */
	public ExecuteResult<UserInfoDTO> queryUserExtendsByname(String companyName);
}
