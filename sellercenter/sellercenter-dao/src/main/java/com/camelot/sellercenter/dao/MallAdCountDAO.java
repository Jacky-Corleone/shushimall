package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.camelot.sellercenter.malladvertise.dto.MallAdCountDTO;

/**
 * <p>广告点击量数据交互接口</p>
 *  
 *  @author learrings
 *  @createDate 
 **/
public interface MallAdCountDAO  extends BaseDAO<MallAdCountDTO>{
	
	//广告报表 分页
	public List<AdReportOutDto> queryReportList(@Param("entity") AdReportInDto adReportInDto, @Param("page") Pager pager);

	
	public Long queryReportCount(@Param("entity") AdReportInDto adReportInDto);
	
	//轮播图报表 分页
	public List<AdReportOutDto> queryBannerReportList(@Param("entity") AdReportInDto adReportInDto, @Param("page") Pager pager);

    public Long queryBannerReportCount(@Param("entity") AdReportInDto adReportInDto);
}