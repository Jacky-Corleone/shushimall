package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [字典表]</p>
 * Created on 2015年8月11日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface DictionaryService {

	/**
	 * <p>Discription:[字典名称列表查询]</p>
	 * Created on 2015年8月11日
	 * @param id
	 * @return
	 * @author:[王灿]
	 */
	public List<DictionaryDTO> queryDictionaryNameList();
	/**
	 * <p>Discription:[按id查询字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<DictionaryDTO> queryDictionaryById(Long id);
	/**
	 * <p>Discription:[按条件查询字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public DataGrid<DictionaryDTO> queryDictionaryByCondition(DictionaryDTO dictionaryDTO,Pager pager);
	/**
	 * <p>Discription:[按编码查询名称]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<List<DictionaryDTO>> queryDictionaryByCode(String code ,String type);
	
	/**
	 * 获取物流公司列表信息
	 * @param dictionaryDTO
	 * @param type
	 * @param pager
	 * @return
	 */
	public DataGrid<DictionaryDTO> queryDictionaryByType(DictionaryDTO dictionaryDTO,Pager pager);

	/**
	 * <p>Discription:[增加字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<String> addDictionary(DictionaryDTO dictionaryDTO);
	/**
	 * <p>Discription:[删除字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<String> delDictionary(DictionaryDTO dictionaryDTO);
	/**
	 * <p>Discription:[修改字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	public ExecuteResult<String> updDictionary(DictionaryDTO dictionaryDTO);
}
