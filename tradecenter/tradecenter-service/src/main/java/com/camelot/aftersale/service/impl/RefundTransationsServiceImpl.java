package com.camelot.aftersale.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.aftersale.dto.RefundTransationsDTO;
import com.camelot.aftersale.service.RefundTransationsService;
import com.camelot.aftersale.service.util.AftersaleUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.dao.RefundTransationsDAO;
import com.camelot.payment.domain.RefundTransations;

@Service("refundTransationsService")
public class RefundTransationsServiceImpl extends AftersaleUtil implements RefundTransationsService{
	private final static Logger logger = LoggerFactory.getLogger(RefundTransationsServiceImpl.class);
	@Resource
	private RefundTransationsDAO refundTransationsDAO;

	@Override
	public ExecuteResult<RefundTransationsDTO> createRefundTransationsDTO(
			RefundTransationsDTO refundTransationsDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","RefundTransationsServiceImpl-createRefundTransationsDTO",JSONObject.toJSONString(refundTransationsDTO));
		ExecuteResult<RefundTransationsDTO> result =new ExecuteResult<RefundTransationsDTO>();
		try {
			RefundTransations refundTransations =this.buildRefundTransations(refundTransationsDTO);
			refundTransationsDAO.add(refundTransations);
			result.setResult(this.buildRefundTransationsDTO(refundTransations));
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]","RefundTransationsServiceImpl-createRefundTransationsDTO",e.getMessage());
		}
		logger.info("\n 方法[{}]，出参：[{}]","RefundTransationsServiceImpl-createRefundTransationsDTO",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<String> updateRefundTransationsDTO(
			RefundTransationsDTO refundTransationsDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","RefundTransationsServiceImpl-updateRefundTransationsDTO",JSONObject.toJSONString(refundTransationsDTO));
		ExecuteResult<String> result =new ExecuteResult<String>();
		try {
			RefundTransations refundTransations =this.buildRefundTransations(refundTransationsDTO);
			refundTransationsDAO.update(refundTransations);
			result.setResultMessage("修改成功");
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]","RefundTransationsServiceImpl-updateRefundTransationsDTO",e.getMessage());
		}
		logger.info("\n 方法[{}]，出参：[{}]","RefundTransationsServiceImpl-updateRefundTransationsDTO",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public DataGrid<RefundTransationsDTO> searchRefundTransationsDTO(
			Pager<RefundTransationsDTO> pager,
			RefundTransationsDTO refundTransationsDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","RefundTransationsServiceImpl-searchRefundTransationsDTO",JSONObject.toJSONString(refundTransationsDTO));
		DataGrid<RefundTransationsDTO> result =new DataGrid<RefundTransationsDTO>();
		RefundTransations refundTransations= this.buildRefundTransations(refundTransationsDTO);
		long count =refundTransationsDAO.queryCount(refundTransations);
		if(count>0){
			List<RefundTransationsDTO> listRefundTransationsDTO= new ArrayList<RefundTransationsDTO>();
			List<RefundTransations> listRefundTransations=refundTransationsDAO.queryList(refundTransations, pager);
			for (RefundTransations com : listRefundTransations) {
				listRefundTransationsDTO.add(this.buildRefundTransationsDTO(com));
			}
			result.setRows(listRefundTransationsDTO);
		}
		result.setTotal(count);
		logger.info("\n 方法[{}]，出参：[{}]","RefundTransationsServiceImpl-searchRefundTransationsDTO",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<RefundTransationsDTO> queryRefundTransationByRefundNo(String refundNo) {
		ExecuteResult<RefundTransationsDTO> result =new ExecuteResult<RefundTransationsDTO>();
		try{
			RefundTransationsDTO refundTransationsDTO=null;
			List<RefundTransations> refundTransationsList = refundTransationsDAO.queryRefundTransationByRefundNo(refundNo);
			if(null!=refundTransationsList&&refundTransationsList.size()>0){
				RefundTransations refundTransations = refundTransationsList.get(0);
				if(null!=refundTransations){
					refundTransationsDTO=this.buildRefundTransationsDTO(refundTransations);
				}
			}
			result.setResult(refundTransationsDTO);
			result.setResultMessage("查询成功");
		}catch(Exception e){
			result.addErrorMessage(e.getMessage());
			logger.info("\n 方法[{}]，异常：[{}]","RefundTransationsServiceImpl-queryRefundTransationByRefundNo",e.getMessage());
		}
		return result;
	}

}
