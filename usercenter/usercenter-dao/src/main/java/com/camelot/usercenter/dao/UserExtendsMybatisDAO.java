package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserAuditStatus;

/**
 *  用户扩展信息查询
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-4
 */
public interface UserExtendsMybatisDAO extends BaseDAO<UserInfoDTO> {
	
	/**
	 * 根据条件修改，相关扩展项审核状态变为待审核
	 * 
	 * @param userInfoDTO - 条件查询
	 * @param userExtendsType - 用户扩展类型
	 */
	public Integer updateBySelect(@Param("userInfoDTO")UserInfoDTO userInfoDTO, @Param("userExtendsType")Integer userExtendsType);
	
	/**
	 * 根据类项修改其审核状态
	 * 
	 * @param extendId - 主键
	 * @param status - 审核状态
	 * @param userExtendsType - 用户扩展类型
	 */
	public Integer updateStatusByType(@Param("extendId")Long extendId,@Param("status")Integer status, @Param("userExtendsType")Integer userExtendsType);
	
	/**
	 * 
	 * <p>Discription:[根据用户id 扩展信息 相关属性]</p>
	 * Created on 2015-3-13
	 * @param userInfoDTO
	 * @return
	 * @author:[liuqingshan]
	 */
	
	public Integer updateSelective(@Param("userInfoDTO")UserInfoDTO userInfoDTO);

    /**
     *
     * <p>Discription:[根据主键修改各个扩展信息状态]</p>
     * Created on 2015-3-13
     * @param userInfoDTO
     * @return
     * @author:[liuqingshan]
     */
	public Integer updateAllStatusByUserExtendId(@Param("extendId")Long userExtendId,@Param("status")Integer status);
	
	/**
	 * 
	 * <p>Discription:[根据用户ID删除扩展信息]</p>
	 * Created on 2015-4-2
	 * @param userId
	 * @return
	 * @author:[liuqingshan]
	 */
	public Integer deleteUserExtendByUserId(@Param("userId")Long userId);
	/**
	 * 
	 * <p>Discription:[根据供应商名称查询]</p>
	 * Created on 2015-10-26
	 * @param supplyId
	 * @return
	 * @author:[zhaoyaqiang]
	 */
	public UserInfoDTO queryUserExtendsByname(@Param("companyName")String companyName);
}
