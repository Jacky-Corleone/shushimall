package com.camelot.maketcenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface PromotionMarkdownDAO extends BaseDAO<PromotionMarkdown>{

	public Integer deleteByPromotionId(Object id);

	public long queryActivityCheck(@Param("shopId")Long shopId,@Param("itemIds")Long[] itemIds);
}
