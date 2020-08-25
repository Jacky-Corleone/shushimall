package com.camelot.maketcenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.centralPurchasing.domain.CentralPurchasingActivites;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 集采活动
 * 
 * @author 周志军
 * 
 */
public interface CentralPurchasingActivitesDAO extends BaseDAO<CentralPurchasingActivites> {

	/**
	 * 
	 * <p>Description: [查询活动]</p>
	 * Created on 2015年11月27日
	 * @param queryCentralPurchasingDTO
	 * @param page
	 * @return
	 * @author:[宋文斌]
	 */
	List<QueryCentralPurchasingDTO> queryCentralPurchasingActivites(
			@Param("entity") QueryCentralPurchasingDTO queryCentralPurchasingDTO, @Param("page") Pager page);
	
	/**
	 * 
	 * <p>Description: [查询数量]</p>
	 * Created on 2015年11月27日
	 * @param queryCentralPurchasingDTO
	 * @return
	 * @author:[宋文斌]
	 */
	Long queryCentralPurchasingActivitesCount(@Param("entity") QueryCentralPurchasingDTO queryCentralPurchasingDTO);
	
	/**
	 * 
	 * <p>Description: [根据集采活动详情ID查询集采活动]</p>
	 * Created on 2015年12月3日
	 * @param activitesDetailsId
	 * @return
	 * @author:[宋文斌]
	 */
	QueryCentralPurchasingDTO queryByDetailId(@Param("activitesDetailsId") Long activitesDetailsId);
	/**
	 * 
	 * <p>Description: [根据集采活动详情ID查询集采活动]</p>
	 * Created on 2015年12月15日
	 * @param activity
	 * @return
	 * @author:[周志军]
	 */
	int checkUniqueSku(@Param("entity") QueryCentralPurchasingDTO queryCentralPurchasingDTO);
	/**
	 * 
	 * <p>Description: [批量删除活动]</p>
	 * Created on 2015年12月15日
	 * @param ids
	 * @return
	 * @author:[周志军]
	 */
	int deleteBatch(@Param("ids") String ids);
}
