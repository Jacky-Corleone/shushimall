package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

public interface TranslationMatDAO extends BaseDAO<TranslationMatDTO>{

	public List<Map> queryPage(@Param("pager") Pager pager, @Param("translationMat") TranslationMatDTO translation);

	public Long queryPageCount(@Param("translationMat") TranslationMatDTO translation);

	public TranslationMatDTO findById(Long id);

	public Integer insert(TranslationMatDTO translation);

	public Integer update(TranslationMatDTO translation);

	public void delete(@Param("codes") List<String> codes);

	public List<Map<String,Object>> findAll();

	public TranslationMatDTO findByTranslationMatDTO(@Param("translationMat")TranslationMatDTO translation);
}
