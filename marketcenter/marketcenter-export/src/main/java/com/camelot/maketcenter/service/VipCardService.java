package com.camelot.maketcenter.service;

import java.util.List;

import com.camelot.maketcenter.dto.VipCardDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [vip卡服务]</p>
 * Created on 2015-4-15
 * @author  zhangzq
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface VipCardService {
	/**
	 * 查询vipCard
	 * @param vipCardDTO vip卡实体
	 * @param page 分页
	 * @return vip卡集合
	 */
	public ExecuteResult<DataGrid<VipCardDTO>> queryVipCardList(VipCardDTO vipCardDTO,Pager page);
	
	/**
	 * 查询vipCard
	 * @param vipCardDTO vip卡实体
	 * @return VipCardDTO vip卡实体
	 */
	public ExecuteResult<VipCardDTO> queryVipCard(VipCardDTO vipCardDTO);
	
	/**
	 * 查询vipCard表中数据总条数
	 * @param vipCardDTO vip卡实体
	 * @return int 总条数
	 */
	public ExecuteResult<Long> queryCountVipCard(VipCardDTO vipCardDTO);
	
	/**
	 * 
	 * <p>Discription:[更新vip卡信息]</p>
	 * Created on 2015-11-18
	 * @param vipCardDTO
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<VipCardDTO> updateVipCard(VipCardDTO vipCardDTO );
	
}
