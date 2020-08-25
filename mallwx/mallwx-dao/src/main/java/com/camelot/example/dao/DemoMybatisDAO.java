package com.camelot.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.example.domain.Demo;
import com.camelot.example.dto.DemoDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * 数据库操作类
 */
public interface DemoMybatisDAO extends BaseDAO<Demo> {
	
	public List<DemoDTO> queryListDTO(@Param("entity") DemoDTO demoDTO, @Param("page") Pager page);

}
