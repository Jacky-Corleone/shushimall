package com.camelot.goodscenter.dao;

import com.camelot.goodscenter.dto.TranslationOrderDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranslationOrderDAO extends BaseDAO<TranslationOrderDTO> {

	public List<TranslationOrderDTO> queryPage(@Param("pager") Pager pager, @Param("translationOrder") TranslationOrderDTO translationOrder);
	
	public Long queryPageCount(@Param("translationOrder") TranslationOrderDTO translationOrder);
	
	public TranslationOrderDTO findById(Long id);
	
	public Integer insert(TranslationOrderDTO translationOrder);
	
	public Integer update(TranslationOrderDTO translationOrder);

	public void delete(@Param("codes") List<String> codes);
	
	public List<Map<String,Object>> findAll();

	public TranslationOrderDTO findByTranslationOrderDTO(@Param("translationOrder") TranslationOrderDTO translationOrder);
}
