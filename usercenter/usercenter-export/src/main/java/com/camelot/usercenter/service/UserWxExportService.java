package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.usercenter.dto.*;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserType;

import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Description: [微信用户的处理接口类]</p>
 * Created on 2015年7月16日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserWxExportService {

	/* 微信绑定 验证成功后写入   单独接口 便于后期修改
    *
    * @param userWxDTO
    * @author 鲁鹏
    * @return
    */
	public ExecuteResult<UserWxDTO> bindingWX(UserWxDTO userWxDTO);

	/*
	* 根据微信绑定ID获取用户信息    单独接口 便于后期修改
	* @param userWxDTO
	* @author 鲁鹏
	* @return
	*/
	public ExecuteResult<UserWxDTO> getUserInfoByOpenId(UserWxDTO userWxDTO);
	/*
	* 解除微信绑定
	* @param userWxDTO
	* @author 鲁鹏
	* @return
	*/
	public ExecuteResult<UserWxDTO> cancelBinding(UserWxDTO userWxDTO);
	/*
	* 协议用户查询
	* @param userWxDTO
	* @author 鲁鹏
	* @return
	*/
	public ExecuteResult<DataGrid<UserDTO>> queryUser(UserDTO userDTO,Pager page);
	/**
	 *
	 * <p>Discription:[根据用户 IDS 查询用户LIST]</p>
	 * Created on 2015-8-03
	 * @param idList
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<Map>> findUserListByUserIds(List<String> idList);
}
