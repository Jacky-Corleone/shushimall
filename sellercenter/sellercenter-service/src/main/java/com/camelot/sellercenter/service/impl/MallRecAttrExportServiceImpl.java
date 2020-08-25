package com.camelot.sellercenter.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.dao.MallRecommendAttrDAO;
import com.camelot.sellercenter.domain.MallRecommendAttr;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrInDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrQueryDTO;
import com.camelot.sellercenter.mallrecattr.service.MallRecAttrExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月28日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("mallRecAttrExportService")
public class MallRecAttrExportServiceImpl implements MallRecAttrExportService {
	
	private static final Logger logger = LoggerFactory.getLogger(MallRecAttrExportServiceImpl.class);
	
	@Resource
	private MallRecommendAttrDAO mallRecAttrDAO;
	
	@Override
	public DataGrid<MallRecAttrDTO> queryMallRecAttrList(Pager page, MallRecAttrQueryDTO mallRecAttrQueryDTO) {
		DataGrid<MallRecAttrDTO> dg = new DataGrid<MallRecAttrDTO>();
		try {
			MallRecommendAttr mra = new MallRecommendAttr();
			BeanUtils.copyProperties(mallRecAttrQueryDTO, mra);
			dg.setTotal(this.mallRecAttrDAO.queryCount(mra));
			dg.setRows(this.mallRecAttrDAO.queryPage(page, mra));
		} catch (Exception e) {
			logger.error("执行方法【queryMallRecAttrList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dg;
	}

	@Override
	public MallRecAttrDTO getMallRecAttrById(Long id) {
		MallRecAttrDTO mrad = new MallRecAttrDTO();
		try {
			MallRecommendAttr mra = this.mallRecAttrDAO.queryById(id);
			BeanUtils.copyProperties(mra, mrad);
			mrad.setSortNum(mra.getSortNumber());
		} catch (Exception e) {
			logger.error("执行方法【queryMallRecAttrList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return mrad;
	}

	@Override
	public ExecuteResult<String> addMallRecAttr(MallRecAttrInDTO mallRecAttrInDTO) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			MallRecommendAttr mra = new MallRecommendAttr();
			BeanUtils.copyProperties(mallRecAttrInDTO, mra);
			mra.setSortNumber(mallRecAttrInDTO.getSortNum());
			if(mallRecAttrInDTO.getTimeFlag() != null && mallRecAttrInDTO.getTimeFlag() == 0 ){
				mra.setStartTime(new Date());
			}
//			mra.setStatus(1);
			this.mallRecAttrDAO.add(mra);
		} catch (Exception e) {
			logger.error("执行方法【addMallRecAttr】报错！{}",e);
			er.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public ExecuteResult<String> modifyMallRecAttr(MallRecAttrInDTO mallRecAttrInDTO) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			MallRecommendAttr mra = new MallRecommendAttr();
			BeanUtils.copyProperties(mallRecAttrInDTO, mra);
			mra.setSortNumber(mallRecAttrInDTO.getSortNum());
			if(mallRecAttrInDTO.getTimeFlag() != null && mallRecAttrInDTO.getTimeFlag() == 0 ){
				mra.setStartTime(new Date());
			}
//			mra.setStatus(1);
			this.mallRecAttrDAO.update(mra);
		} catch (Exception e) {
			logger.error("执行方法【modifyMallRecAttr】报错！{}",e);
			er.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public ExecuteResult<String> modifyMallRecAttrStatus(Long id, String publishFlag) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			this.mallRecAttrDAO.modifyMallRecAttrStatus(id, publishFlag);
		} catch (Exception e) {
			logger.error("执行方法【modifyMallRecAttrStatus】报错！{}",e);
			er.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public ExecuteResult<String> deleteMallRecAttrById(Long id) {
		ExecuteResult<String> res=new ExecuteResult<String>();
		long count=mallRecAttrDAO.deleteMallRecAttrById(id);
		if(count>0){
			res.setResult("1");
		}else{
			res.setResult("0");
		}
		return  res;
	}
	
	@Override
	public ExecuteResult<Integer> autoBatchPublishMallRec(){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer count = mallRecAttrDAO.autoBatchPublishMallRec();
		result.setResult(count);
		return result;
	}
	@Override
	public ExecuteResult<Integer> autoBatchCancelMallRec(){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer count = mallRecAttrDAO.autoBatchCancelMallRec();
		result.setResult(count);
		return result;
	}
}
