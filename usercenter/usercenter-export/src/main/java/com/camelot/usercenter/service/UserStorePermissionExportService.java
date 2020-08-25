package com.camelot.usercenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.dto.UserPermissionDTO;
import com.camelot.usercenter.dto.indto.StoreUserResourceInDTO;

/**
 * 
 * <p>Description: [店铺用户权限]</p>
 * Created on 2015-3-16
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserStorePermissionExportService {

	
	/**
	 * 
	 * <p>Discription:[查询所有父级资源]</p>
	 * Created on 2015-3-16
	 * @param type 用户类型：1-买家/2-卖家/3-平台
	 * @param modularType 模块类型 ：1买家中心 2卖家中心  
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<List<UserMallResourceDTO>>  queryParentResourceList(Integer type,Integer modularType);
	/**
	 * 
	 * <p>Discription:[添加子账号]</p>
	 * Created on 2015-3-17
	 * @param storeUserResourceInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  addStoreUserResource(StoreUserResourceInDTO storeUserResourceInDTO);
	
	/**
	 * 
	 * <p>Discription:[查询用户下子账户信息]</p>
	 * Created on 2015-3-17
	 * @param parentId 当前用户id
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ChildUserDTO>> queryChildUserList(Long parentId,Integer modularType,Pager page);
	
	/**
	 * 
	 * <p>Discription:[根据子账户ID查询子账户信息]</p>
	 * Created on 2015-3-18
	 * @param userId 
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<ChildUserDTO>  getChildUser(Long userId,Integer modularType);
	
	
	/**
	 * 
	 * <p>Discription:[根据用户ID查询资源]</p>
	 * Created on 2015-3-18
	 * @param userId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<List<UserMallResourceDTO>> queryUserMallResourceById(Long userId,Integer modularType);
	
	/**
	 * 
	 * <p>Discription:[用户资源修改]</p>
	 * Created on 2015-3-20
	 * @param serPermissionDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyUserResourceById(UserPermissionDTO userPermissionDTO);
	/**
	 * 
	 * <p>Discription:[删除子账户]</p>
	 * Created on 2015-3-24
	 * @param userId
	 * @param modularType 模块类型 ：1买家中心 2卖家中心  
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> deleteUserById(Integer modularType,Long... userId);
	
}
