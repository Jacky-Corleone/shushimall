package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallword.dto.MallWordDTO;

public interface MallWordDAO {

	public void add(MallWordDTO dto);

	public void delete(@Param("id") Long id);

	public Long queryCount(@Param("entity") MallWordDTO dto);

	@SuppressWarnings("rawtypes")
	public List<MallWordDTO> queryList(@Param("entity") MallWordDTO dto,
			@Param("page") Pager page);
}
