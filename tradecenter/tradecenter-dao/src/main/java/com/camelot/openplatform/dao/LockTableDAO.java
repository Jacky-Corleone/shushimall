package com.camelot.openplatform.dao;

import org.apache.ibatis.annotations.Param;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface LockTableDAO{
	
	int updateByTable(@Param("tableNm")String tableNm,@Param("batch")int batch);
}