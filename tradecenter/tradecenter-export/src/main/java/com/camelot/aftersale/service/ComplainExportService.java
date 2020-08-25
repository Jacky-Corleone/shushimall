package com.camelot.aftersale.service;

import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import java.util.List;

/**
 * 供客户端调用的投诉远程接口
 * 
 * @author - learrings
 * @createDate - 2015-4-09
 */
public interface ComplainExportService {

	/**
	 * 添加投诉信息
	 * 
	 * @param complainDTO
	 * @return
	 */
	ExecuteResult<ComplainDTO> addComplainInfo(ComplainDTO complainDTO) ;
	
	/**
	 * 根据ID更新投诉信息
	 * 
	 * @param complainDTO
	 * @return
	 */
	ExecuteResult<String> modifyComplainInfo(ComplainDTO complainDTO) ;
	
	/**
	 * 根据ID查询投诉信息
	 * 
	 * @param id
	 * @return
	 */
	ExecuteResult<ComplainDTO> findInfoById(Long id) ;
	
	/**
	 * 根据条件查询投诉信息
	 * 
	 * @param complainDTO - 可空
	 * @param pager - 可空
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	DataGrid<ComplainDTO> findInfoByCondition(ComplainDTO complainDTO,Pager pager) ;

    /**
     * 根据条件查询投诉信息
     * 
     * @param complainDTO
     * @return
     */
    List<ComplainDTO> getComplainByCondition(ComplainDTO complainDTO);
    
    /**
     * 根据条件查询投诉信息 (在ECM的投诉仲裁页面列表显示中用到)
     * 
     * @param complainDTO
     * @return
     */
    DataGrid<ComplainDTO> findEarlyComplainInfoByCondition(ComplainDTO complainDTO,Pager pager);
}
