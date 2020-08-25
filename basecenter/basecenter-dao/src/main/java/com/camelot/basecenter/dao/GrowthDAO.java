package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.GrowthDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface GrowthDAO extends BaseDAO<GrowthDTO>{

	
	/**
	 * 
	 * <p>Discription:[查询实体]</p>
	 * Created on 2015-12-4
	 * @param growthDto
	 * @return
	 * @author:[范东藏]
	 */
	public GrowthDTO getEntityByEntity(@Param("entity") GrowthDTO growthDTO);
}
