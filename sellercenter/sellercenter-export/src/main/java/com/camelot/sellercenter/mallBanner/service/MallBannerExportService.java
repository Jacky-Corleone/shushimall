package com.camelot.sellercenter.mallBanner.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.dto.MallBannerInDTO;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
/**
 * 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月27日
 * @author  <a href="mailto: maguilei59@camelotchina.com">马桂雷</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface MallBannerExportService {
	/**
	 * 
	 * <p>Discription:[前台轮播列表查询]</p>
	 * Created on 2015年1月27日
	 * @param publishFlag  展示标记,传入参数为1时查询当前可用于前台展示的轮播 0为未发布轮播 不传为全部
	 * @param page
	 * @return
	 * @author:[马桂雷]
	 */
	public DataGrid<MallBannerDTO> queryMallBannerList(String publishFlag, Integer bannerType, Pager page);
	/**
	 * 
	 * <p>Discription:[后台轮播列表查询]</p>
	 * Created on 2015年1月27日
	 * @param MallBannerDTO  
	 * @param publishFlag  展示标记,传入参数为1时查询当前可用于前台展示的轮播 0为未发布轮播 不传为全部
	 * @param page
	 * @return
	 * @author:[马桂雷]
	 */
	public DataGrid<MallBannerDTO> queryMallBannerList(MallBannerDTO mallBannerDTO, String publishFlag, Pager page);
	/**
	 * 
	 * <p>Discription:[轮播详情查询]</p>
	 * Created on 2015年1月27日
	 * @param id 轮播图ID
	 * @return
	 * @author:[马桂雷]
	 */
	public MallBannerDTO getMallBannerById(long id);
	/**
	 * 
	 * <p>Discription:[轮播添加.即时发布时，直接添加为上架轮播；定时发布时，添加的轮播是非上架状态  添加时候 请在 mallBannerInDTO 传入STATUS 状态之 1 上架 0 下架]</p>
	 * Created on 2015年1月27日
	 * @param mallBannerDTO
	 * @return
	 * @author:[马桂雷]
	 */
	public ExecuteResult<String> addMallBanner(MallBannerInDTO mallBannerInDTO );
	/**
	 * 
	 * <p>Discription:[轮播修改.即时发布时，直接添加为上架轮播；定时发布时，添加的轮播是非上架状态]</p>
	 * Created on 2015年1月27日
	 * @param mallBannerInDTO
	 * @return
	 * @author:[马桂雷]
	 */
	public ExecuteResult<String> modifyMallBanner(MallBannerInDTO mallBannerInDTO );
	
	/**
	 * 
	 * <p>Discription:[轮播上下架]</p>
	 * Created on 2015年1月27日
	 * @param id
	 * @param publishFlag 上下架标记,0：下架  1：上架
	 * @return
	 * @author:[马桂雷]
	 */
	public ExecuteResult<String> motifyMallBannerStatus(Long id, String publishFlag);
	/**
	 * 
	 * <p>Discription:[批量轮播上下架]</p>
	 * Created on 2015年11月3日
	 * @param id
	 * @param publishFlag 上下架标记,0：下架  1：上架
	 * @return
	 * @author:[wangdongxiao]
	 */
	public ExecuteResult<String> motifyMallBannerStatusBatch(String publishFlag,Long... ids);
	
	/**
	 * 
	 * <p>Discription:[轮播顺序修改]</p>
	 * Created on 2015年1月27日
	 * @param id
	 * @param sortNum 重新设置后的排序号
	 * @return
	 * @author:[马桂雷]
	 */
	public ExecuteResult<String> modifyMallBannerSort(Long id, Integer sortNum);
	
	/**
	 * 查询轮播图点击量报表列表
	 * @param mallAdCountDTO
	 * @param pager
	 * @return
	 */
	public DataGrid<AdReportOutDto> queryReportList(AdReportInDto adReportInDto,Pager pager);
	/**
	 * 自动批量上架，定时上架时间小于当前时间
	 * 
	 * @return
	 */
	public ExecuteResult<Integer> autoBatchOffShelves();
	
	/**
	 * 自动批量下架，定时下架时间小于当前时间
	 * 
	 * @return
	 */
	public ExecuteResult<Integer> autoBatchRelease();
	
	/**
	 * 
	 * <p>Discription:[删除轮播图]</p>
	 * Created on 2015年12月29日
	 * @param id
	 * @return
	 * @author:[化亚会]
	 */
	public boolean delete(MallBannerInDTO mallBannerInDTO);
}
