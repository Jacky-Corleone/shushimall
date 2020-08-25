package com.camelot.maketcenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface PromotionInfoDAO extends BaseDAO<PromotionInfo> {

	public List<PromotionInfo> queryMdTimeFrameByItemId(@Param("entity")PromotionInfo promotionInfo);

	public long queryActivityCheck(@Param("shopId")Long shopId);
}
