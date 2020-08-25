package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface DictionaryDAO extends BaseDAO<DictionaryDTO> {

	public List<DictionaryDTO> queryDictionaryNameList();
	
	public DictionaryDTO queryDictionaryById(Long id);
	
	public List<DictionaryDTO> queryDictionaryNoParent(@Param("entity") DictionaryDTO dictionaryDTO, @Param("page") Pager pager);
	public long queryCountNoParent(@Param("entity") DictionaryDTO dictionaryDTO);
	
	public List<DictionaryDTO> queryDictionaryByCode(@Param("code") String code,@Param("type") String type);
	public List<DictionaryDTO> queryDictionaryByCondition(@Param("entity") DictionaryDTO dictionaryDTO,@Param("page") Pager pager);
	public long queryDictionaryByConditionCount(@Param("entity") DictionaryDTO dictionaryDTO);
}
