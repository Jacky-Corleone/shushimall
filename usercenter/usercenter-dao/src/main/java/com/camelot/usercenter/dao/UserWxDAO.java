package com.camelot.usercenter.dao;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserWxDTO;
import com.camelot.usercenter.dto.report.UserAndShopReportDTO;
import com.camelot.usercenter.dto.report.UserReportDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Description: [微信用户处理的DAO类]</p>
 * Created on 2015年7月16日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserWxDAO extends BaseDAO<UserWxDTO> {
	/**
	 *
	 * <p>Discription:[根据微信绑定ID获取用户信息    单独接口 便于后期修改]</p>
	 * Created on 2015-7-16
	 * @param wxopenid
	 * @return
	 * @author:鲁鹏
	 */
	public int bindingWX(@Param("uid") Long uid, @Param("wxopenid") String wxopenid);
	/**
	 *
	 * <p>Discription:[根据微信绑定ID获取用户信息    单独接口 便于后期修改]</p>
	 * Created on 2015-7-16
	 * @param userWxDTO
	 * @return
	 * @author:鲁鹏
	 */
	public UserWxDTO getUserInfoByOpenId(@Param("userWxDTO") UserWxDTO userWxDTO);

	/**
	 *
	 * <p>Discription:[根据用户Id解除绑定]</p>
	 * Created on 2015-7-16
	 * @param uid
	 * @return
	 * @author:鲁鹏
	 */
	public int cancelBinding(@Param("uid") Long uid);

	public List<UserDTO> queryPage(@Param("pager") Pager pager, @Param("userDTO") UserDTO userDTO);

	public Long queryPageCount(@Param("userDTO") UserDTO userDTO);
	/**
	 *
	 * <p>Discription:[根据用户 IDS 查询用户LIST]</p>
	 * Created on 2015-8-03
	 * @param idList
	 * @return
	 * @author:鲁鹏
	 */
	public List<Map> findUserListByUserIds(@Param("idList")List<String> idList);

	public Long findUserListByUserIdsCount(@Param("idList")List<String> idList);

}
