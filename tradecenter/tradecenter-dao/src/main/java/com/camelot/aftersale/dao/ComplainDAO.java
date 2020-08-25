package com.camelot.aftersale.dao;

import com.camelot.aftersale.domain.Complain;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface ComplainDAO  extends BaseDAO<Complain>{
	public List<Complain> getComplainByCondition(@Param("entity")Complain  t);
	
	public Long getCountEarlyComplainByCondition(@Param("entity")Complain  t);
	public List<Complain> findEarlyComplainListByCondition(@Param("entity")Complain  t,
			@SuppressWarnings("rawtypes")@Param("page") Pager page);
}