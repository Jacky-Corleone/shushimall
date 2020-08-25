package com.camelot.openplatform.dao.orm;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;

public interface CmsDAO<T> {

	public void add(T t);

	public T queryById(Object id);

	public void update(T t);

	public void updateBySelect(T t);

	public void delete(Object id);

	public Long queryCount(@Param("entity") T t);

	public List<T> queryList(@Param("entity") T t, @Param("page") Pager page);

}
