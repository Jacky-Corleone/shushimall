package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.TypeDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface TypeDAO extends BaseDAO<TypeDTO> {
	public List<TypeDTO> queryType();
	public long queryTypeCount();
	public TypeDTO queryTypeById(Long id);
	public List<TypeDTO> queryTypeByCondition(@Param("entity") TypeDTO typeDTO,@Param("page") Pager pager);
	public long queryTypeByConditionCount(TypeDTO typeDTO);
	public long uniquenessType(TypeDTO typeDTO);
}
