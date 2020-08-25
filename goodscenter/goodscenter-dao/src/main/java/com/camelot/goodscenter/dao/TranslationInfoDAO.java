package com.camelot.goodscenter.dao;

import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranslationInfoDAO extends BaseDAO<TranslationInfoDTO> {
	
	public Integer updateByTranslationNo(TranslationInfoDTO translationInfo);

	public List<TranslationInfoDTO> queryPage(@Param("pager") Pager pager, @Param("translationInfo") TranslationInfoDTO translationInfo);
	
	public Long queryPageCount(@Param("translationInfo") TranslationInfoDTO translationInfo);
	
	public TranslationInfoDTO findById(Long id);
	
	public Integer insert(TranslationInfoDTO translationInfo);
	
	public Integer update(TranslationInfoDTO translationInfo);

	public void delete(@Param("codes") List<String> codes);
	
	public List<TranslationInfoDTO> findAll(@Param("translationInfo") TranslationInfoDTO translationInfo);

	public TranslationInfoDTO findByTranslationInfoDTO(@Param("translationInfo") TranslationInfoDTO translationInfo);

	public List<Map> queryTranslationInfoPager(@Param("pager") Pager pager, @Param("translationInfo") TranslationInfoDTO translationInfo);

	public Long queryTranslationInfoPagerCount(@Param("translationInfo") TranslationInfoDTO translationInfo);
}
