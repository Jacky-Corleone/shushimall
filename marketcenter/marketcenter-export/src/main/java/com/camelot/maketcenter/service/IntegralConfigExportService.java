package com.camelot.maketcenter.service;

import java.math.BigDecimal;
import java.util.List;

import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [积分设置]</p>
 * Created on 2015-11-25
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface IntegralConfigExportService {
	/**
	 * 
	 * <p>Description: [新增积分配置]</p>
	 * Created on 2015-12-4
	 * @author 周志军
	 * @param IntegralConfigDTO
	 * @return 
	 */
	public ExecuteResult<Boolean> addIntegralConfigDTO(List<IntegralConfigDTO> integralConfigDTOs);
	/**
	 * 
	 * <p>Description: [更新积分配置]</p>
	 * Created on 2015-12-4
	 * @author 周志军
	 * @param IntegralConfigDTO
	 * @return 
	 */
	public ExecuteResult<Boolean> updateIntegralConfigDTO(List<IntegralConfigDTO> integralConfigDTO);
	/**
	 * 
	 * <p>Description: [查询积分配置]</p>
	 * Created on 2015-12-4
	 * @author 周志军
	 * @param IntegralConfigDTO
	 * @param page
	 * @return 
	 */
	public ExecuteResult<DataGrid<IntegralConfigDTO>> queryIntegralConfigDTO(IntegralConfigDTO integralConfigDTO,Pager page);
	/**
	 * 
	 * <p>Description: [依据ID查询积分配置]</p>
	 * Created on 2015-12-7
	 * @author 周志军
	 * @return 
	 */
	public IntegralConfigDTO queryIntegralConfigDTOById(Long id);
	/**
	 * 
	 * <p>Description: [依据金额查询积分配置]</p>
	 * Created on 2015-12-8
	 * @author 周志军
	 * @return 
	 */
	public ExecuteResult<List<IntegralConfigDTO>> queryIntegralConfigDTOByMoney(BigDecimal money,Long platformId);
	/**
	 * 
	 * <p>Discription:[]</p>
	 * Created on 2015-12-15
	 * @param integralConfigDTO
	 * @param page
	 * @return
	 * @author:[范东藏]
	 */
	public ExecuteResult<DataGrid<IntegralConfigDTO>> queryOneType(IntegralConfigDTO integralConfigDTO, Pager page);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015-12-15
	 * @param integralConfigDTO
	 * @return
	 * @author:[范东藏]
	 */
	public ExecuteResult<Integer> deleteByType(IntegralConfigDTO integralConfigDTO);

}
