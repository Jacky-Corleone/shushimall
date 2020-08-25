package com.camelot.openplatform.dao.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;

public interface BaseDAO<T> {

	public void insert(T t);

	public T selectById(@Param("id") Object id);

	public Integer update(T t);

	public Integer delete(Object id);

	public Long selectCount(@Param("entity") T t);

	@SuppressWarnings("rawtypes")
	public List<T> selectList(@Param("entity") T t, @Param("pager") Pager pager);

}
