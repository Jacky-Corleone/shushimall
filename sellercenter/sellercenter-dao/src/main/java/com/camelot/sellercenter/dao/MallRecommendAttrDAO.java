package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.sellercenter.domain.MallRecommendAttr;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface MallRecommendAttrDAO extends BaseDAO<MallRecommendAttr>{
	
	public List<MallRecAttrDTO> queryPage(@Param("page")Pager page,@Param("entity")MallRecommendAttr mra);
	
	public void modifyMallRecAttrStatus(@Param("id")Long id,@Param("status")String status);

	public Integer deleteMallRecAttrById(@Param("id")Long id);
	
	/**
	 * 定时上架时间小于当前时间的楼曾自动上架
	 * @return
	 */
	public Integer autoBatchPublishMallRec();
	/**
	 * 定时下架时间小于当前时间的楼曾自动下架
	 * @return
	 */
	public Integer autoBatchCancelMallRec();
}
