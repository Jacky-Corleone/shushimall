package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.dto.TypeDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [类型表]</p>
 * Created on 2015年8月11日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface TypeService {

	/**
	 * <p>Discription:[类型列表查询]</p>
	 * Created on 2015年8月11日
	 * @param id
	 * @return
	 * @author:[王灿]
	 */
	public List<TypeDTO> queryType();
	/**
	 * <p>Discription:[按id查询类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<TypeDTO> queryTypeById(Long id);
	/**
	 * <p>Discription:[按条件查询类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public DataGrid<TypeDTO> queryTypeByCondition(TypeDTO typeDTO,Pager<TypeDTO> pager);
	/**
	 * <p>Discription:[增加类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<String> addType(TypeDTO typeDTO);
	/**
	 * <p>Discription:[删除类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<String> delType(TypeDTO typeDTO);
	/**
	 * <p>Discription:[修改类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<String> updType(TypeDTO typeDTO);
	/**
	 * <p>Discription:[类型唯一性]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public boolean uniquenessType(TypeDTO typeDTO);
}
