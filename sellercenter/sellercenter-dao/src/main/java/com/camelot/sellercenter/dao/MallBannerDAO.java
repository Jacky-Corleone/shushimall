package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.domain.MallBanner;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;

public interface MallBannerDAO extends BaseDAO<MallBanner>{
	
	public List<MallBannerDTO> queryListDTO(@Param("nowTime") String nowTime, @Param("publishFlag") String publishFlag, @Param("bannerType") Integer bannerType, @Param("page") Pager page);
	public Long queryCountDTO(@Param("nowTime") String nowTime,@Param("publishFlag") String publishFlag, @Param("bannerType") Integer bannerType);
	
	public List<MallBannerDTO> queryListDTOToAdmin(@Param("entity") MallBannerDTO mallBannerDTO, @Param("nowTime") String nowTime, @Param("publishFlag") String publishFlag, @Param("page") Pager page);
	public Long queryCountDTOToAdmin(@Param("entity") MallBannerDTO mallBannerDTO, @Param("nowTime") String nowTime,@Param("publishFlag") String publishFlag);
	
	public Integer updateStatusById(@Param("id")Long id,@Param("publishFlag") String publishFlag);
	public Integer updateBatchStatusById( @Param("publishFlag") String publishFlag,@Param("ids") Long... ids);
	public Integer updateSortNumberById(@Param("id")Long id,@Param("sortNum") Integer sortNum);
	
	/**
	 * 批量上架
	 * @return
	 */
	public Integer autoBatchOffShelves();
	/**
	 * 批量下架
	 * @return
	 */
	public Integer autoBatchRelease();

}
