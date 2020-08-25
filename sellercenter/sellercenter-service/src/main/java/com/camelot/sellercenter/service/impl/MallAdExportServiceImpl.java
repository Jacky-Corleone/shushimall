package com.camelot.sellercenter.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.dao.MallAdCountDAO;
import com.camelot.sellercenter.dao.MallAdvertiseDAO;
import com.camelot.sellercenter.domain.MallAdvertise;
import com.camelot.sellercenter.malladvertise.dto.AdReportInDto;
import com.camelot.sellercenter.malladvertise.dto.AdReportOutDto;
import com.camelot.sellercenter.malladvertise.dto.MallAdCountDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.enums.AdTableTypeEnums;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.utils.DateDealUtils;

/** 
 * <p>Description: 商城广告服务实现</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("mallAdExportService")
public class MallAdExportServiceImpl implements MallAdExportService {
	
	private static final Logger logger = LoggerFactory.getLogger(MallAdExportServiceImpl.class);
	
	@Resource
	private MallAdvertiseDAO mallAvDao;
	@Resource
	private MallAdCountDAO mallAdCountDAO;
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月23日
	 * @param page
	 * @param mallAdQueryDTO
	 * @return
	 * @author:[创建者中文名字]
	 * @throws Exception 
	 */
	@Override
	public DataGrid<MallAdDTO> queryMallAdList(@SuppressWarnings("rawtypes") Pager page, MallAdQueryDTO mallAdQueryDTO){
		DataGrid<MallAdDTO> dg = new DataGrid<MallAdDTO>();
		try {
			MallAdvertise mallAdvertise = new MallAdvertise();
			BeanUtils.copyProperties(mallAdQueryDTO, mallAdvertise);
			
			dg.setTotal(this.mallAvDao.queryCount(mallAdvertise));
			dg.setRows(this.mallAvDao.queryPage(mallAdvertise, page));
		} catch (Exception e) {
			logger.error("执行方法【queryMallAdList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dg;
	}

	/**
	 * <p>Discription:广告详情查询</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @return
	 * @author:[Goma 郭茂茂]
	 * @throws Exception 
	 */
	
	@Override
	public MallAdDTO getMallAdById(Long id) {
		MallAdvertise mallAdvertise = this.mallAvDao.queryById(id);
		MallAdDTO dto = new MallAdDTO();
		try {
			BeanUtils.copyProperties(mallAdvertise, dto);
			dto.setTitle(mallAdvertise.getAdTitle());
			dto.setAdURL(mallAdvertise.getAdUrl());
			dto.setSortNumber(mallAdvertise.getSortNum());
		} catch (Exception e) {
			logger.error("执行方法【getMallAdById】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dto;
	}

	/**
	 * <p>Discription:广告添加</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@Override
	public ExecuteResult<String> addMallAd(MallAdInDTO mallAdInDTO) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			MallAdvertise ma = new MallAdvertise();
			BeanUtils.copyProperties(mallAdInDTO, ma);
			ma.setAdTitle(mallAdInDTO.getTitle());
			ma.setAdUrl(mallAdInDTO.getAdURL());
			ma.setSortNum(mallAdInDTO.getSortNumber());
//			ma.setStatus(1);
			if( "0".equals(mallAdInDTO.getPublishFlag()) ){
				ma.setStartTime(new Date());
			}
			this.mallAvDao.add(ma);
		} catch (Exception e) {
			logger.error("执行方法【addMallAd】报错！{}",e);
			er.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:广告修改</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@Override
	public ExecuteResult<String> modifyMallBanner(MallAdInDTO mallAdInDTO) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			MallAdvertise ma = new MallAdvertise();
			BeanUtils.copyProperties(mallAdInDTO, ma);
			ma.setAdTitle(mallAdInDTO.getTitle());
			ma.setAdUrl(mallAdInDTO.getAdURL());
			ma.setSortNum(mallAdInDTO.getSortNumber());
//			ma.setStatus(1);
			if( "0".equals(mallAdInDTO.getPublishFlag()) ){
				ma.setStartTime(new Date());
			}
			this.mallAvDao.update(ma);
		} catch (Exception e) {
			logger.error("执行方法【modifyMallBanner】报错！{}",e);
			er.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:广告上下架</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @param publishFlag
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@Override
	public ExecuteResult<String> modifyMallAdStatus(Long id, String publishFlag) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			this.mallAvDao.modifyMallAdStatus(id, publishFlag);
		} catch (Exception e) {
			logger.error("执行方法【modifyMallAdStatus】报错！{}",e);
			er.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public ExecuteResult<MallAdCountDTO> saveMallAdCount(Long mallAdId,Long adTableType) throws Exception{
		ExecuteResult<MallAdCountDTO> executeResult = new ExecuteResult<MallAdCountDTO>();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date=dateFormat.format(new Date().getTime());
		
		MallAdCountDTO mallAdCountDTO =new MallAdCountDTO();
		mallAdCountDTO.setMallAdId(mallAdId);
		mallAdCountDTO.setClickDateBegin(date);
		mallAdCountDTO.setClickDateEnd(date);
		mallAdCountDTO.setTableType(adTableType);
		List<MallAdCountDTO> listMallAdCount = mallAdCountDAO.queryList(mallAdCountDTO,null);
		
		if(listMallAdCount.size() ==0){
			mallAdCountDAO.add(mallAdCountDTO);
		}else{
			mallAdCountDTO =listMallAdCount.get(0);
			mallAdCountDAO.update(mallAdCountDTO);
		}
		executeResult.setResult(mallAdCountDTO);
		return executeResult;
	}
	
	@Override
	public MallAdCountDTO findMallAdCountById(long id){
		MallAdCountDTO mallAdCountDTO = mallAdCountDAO.queryById(id);
		if(mallAdCountDTO!=null){
			this.buildMallAdInfo(mallAdCountDTO);
		}
		return mallAdCountDTO;
	}
	
	@Override
	public DataGrid<MallAdCountDTO> findAdCountList(MallAdCountDTO mallAdCount,@SuppressWarnings("rawtypes") Pager pager){
		DataGrid<MallAdCountDTO> result = new DataGrid<MallAdCountDTO>();
		long count = mallAdCountDAO.queryCount(mallAdCount);
		if(count>0){
			List<MallAdCountDTO> listMallAdCount = mallAdCountDAO.queryList(mallAdCount,pager);
			for (MallAdCountDTO mallAdCountDTO:listMallAdCount) {
				this.buildMallAdInfo(mallAdCountDTO);
			}
			result.setRows(listMallAdCount);
		}
		result.setTotal(count);
		return result;
	}
	
	public MallAdCountDTO buildMallAdInfo(MallAdCountDTO mallAdCountDTO){
		if(AdTableTypeEnums.advertise.ordinal()==mallAdCountDTO.getTableType()){
			MallAdvertise  mallAdvertise = mallAvDao.queryById(mallAdCountDTO.getMallAdId());
			mallAdCountDTO.setMallAdName(mallAdvertise.getAdTitle());
			mallAdCountDTO.setMallAdType(mallAdvertise.getAdType()+"");
//		}else if(AdTableTypeEnums.advertise.ordinal()==mallAdCountDTO.getTableType()){
//			mallAdCountDTO.setMallAdName(mallAdName)
//			mallAdCountDTO.setMallAdType(mallAdType);
//		}else if(AdTableTypeEnums.advertise.ordinal()==mallAdCountDTO.getTableType()){
//			mallAdCountDTO.setMallAdName(mallAdName)
//			mallAdCountDTO.setMallAdType(mallAdType);
		}
		return mallAdCountDTO;
	}

	@Override
	public DataGrid<AdReportOutDto> queryReportList(AdReportInDto adReportInDto, Pager pager) {
		//去掉日期格式
		adReportInDto.setClickDate(DateDealUtils.dateWithoutFormat(adReportInDto.getClickDate()));
		adReportInDto.setClickDateBegin(DateDealUtils.dateWithoutFormat(adReportInDto.getClickDateBegin()));
		adReportInDto.setClickDateEnd(DateDealUtils.dateWithoutFormat(adReportInDto.getClickDateEnd()));
		
		DataGrid<AdReportOutDto> dtos=new DataGrid<AdReportOutDto>();
		List<AdReportOutDto> res=mallAdCountDAO.queryReportList(adReportInDto, pager);
		long count=mallAdCountDAO.queryReportCount(adReportInDto);
		if(adReportInDto.getDateFormat()!=null && adReportInDto.getDateFormat().length()>0){
			for (AdReportOutDto dto : res) {
				dto.setClickDate(DateDealUtils.getDateStrToStr(dto.getClickDate(), "yyyyMMdd", adReportInDto.getDateFormat()));
			}
		}
		dtos.setRows(res);
		dtos.setTotal(count);
		return dtos;
	}
	@Override
	public ExecuteResult<Integer> autoBatchCancelAD(){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer count = mallAvDao.autoBatchCancelAD();
		result.setResult(count);
		return result;
	}
	@Override
	public ExecuteResult<Integer> autoBatchPublishAD(){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer count = mallAvDao.autoBatchPublishAD();
		result.setResult(count);
		return result;
	}

	@Override
	public ExecuteResult<Boolean> deleteMallAd(Long id) {
		logger.info("\n 方法[{}]，出参：[{}]","MallAdExportServiceImpl-deleteMallAd",JSON.toJSONString(id));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			mallAvDao.deleteMallAd(id);
			er.setResult(true);
		} catch (Exception e) {
			er.setResult(false);
			er.addErrorMessage("广告删除失败");
			logger.error("广告删除失败",e);
		}
		return er;
	}

	@Override
	public Integer deleteById(Long id) {
		return mallAvDao.deleteById(id);
	}
}
