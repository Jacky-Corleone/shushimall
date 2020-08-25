package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.domain.MallTheme;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;

public interface MallThemeDAO extends BaseDAO<MallTheme> {
	
	public List<MallThemeDTO> queryListDTO(@Param("entity")MallThemeDTO mallThemeDTO,@Param("publishFlag")String publishFlag, @Param("page")Pager page);
	
	public MallThemeDTO queryById(@Param("id")long id);
	
	public void add(@Param("entity")MallThemeDTO mallThemeDTO);
	
	public Integer delete(@Param("id")Long id);
	
	//public Integer updateq(@Param("entity")MallThemeDTO mallThemeDTO);
	
	public Integer updateStatusById(@Param("id")Long id,@Param("publishFlag")String status);
	
	public long queryListCountDTO(@Param("entity")MallThemeDTO mallThemeDTO,@Param("publishFlag")String publishFlag);
	
	public List<MallThemeDTO> queryGroupCityCode();
	
}
