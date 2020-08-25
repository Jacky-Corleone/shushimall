package com.camelot.sellercenter.malladvertise.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.camelot.sellercenter.malladvertise.dto.MallAdCountDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;

/**
 * 
 * <p>Description: 商城广告服务接口</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface MallAdExportService {
	/**
	 * 
	 * <p>Discription:商城广告查询</p>
	 * Created on 2015年1月23日
	 * @param page
	 * @param mallAdQueryDTO
	 * @return
	 * @author:Goma guomaomao@camelotchina.com
	 */
	public DataGrid<MallAdDTO> queryMallAdList(Pager page, MallAdQueryDTO mallAdQueryDTO);
	
	/**
	 * 
	 * <p>Discription:广告详情查询]</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public MallAdDTO getMallAdById(Long id);
	

	/**
	 * <p>Discription:广告添加</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> addMallAd(MallAdInDTO mallAdInDTO);


	/**
	 * <p>Discription:广告修改</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> modifyMallBanner(MallAdInDTO mallAdInDTO);


	/**
	 * <p>Discription:广告上下架</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @param publishFlag
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public ExecuteResult<String> modifyMallAdStatus(Long id,String publishFlag);
	
	/**
	 * 广告增加点击量，链接存在则+1，不存在则生成新记录
	 *
	 * @param mallAdCountDTO - 广告ID
	 * @param adTableType - AdTableTypeEnums 类型
	 * @return 
	 */
	ExecuteResult<MallAdCountDTO> saveMallAdCount(Long mallAdId,Long adTableType) throws Exception;
	
	/**
	 * 根据ID查询点击量
	 * 
	 * @param id
	 * @return
	 */
	MallAdCountDTO findMallAdCountById(long id);
	
	/**
	 * 根据条件查询广告点击量信息
	 *
	 * @param mallAdCount - 条件，可空
	 * @param pager - 分页，可空
	 * @return 
	 */
	DataGrid<MallAdCountDTO> findAdCountList(MallAdCountDTO mallAdCountDTO,@SuppressWarnings("rawtypes") Pager pager);
	
	/**
	 * 查询广告点击量报表列表
	 * @param adReportInDto
	 * @param pager
	 * @return
	 */
	public DataGrid<AdReportOutDto> queryReportList(AdReportInDto adReportInDto,Pager pager);
	/**
	 * 将定时下架时间小于当前时间的广告下架
	 * @return
	 */
	public ExecuteResult<Integer> autoBatchCancelAD();
	/**
	 * 将定时上架时间小于当前时间的广告上架
	 * @return
	 */
	public ExecuteResult<Integer> autoBatchPublishAD();
	
	/**
	 * 删除方法
	 * @param id
	 * @return
	 */
	public Integer deleteById(Long id);
	/**
	 * 
	 * <p>Description: [删除广告]</p>
	 * Created on 2015-12-23
	 * @param id
	 * @author 周志军
	 * @return 
	 */
	public ExecuteResult<Boolean> deleteMallAd(Long id);
}
