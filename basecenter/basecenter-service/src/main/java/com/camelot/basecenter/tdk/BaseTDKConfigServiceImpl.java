package com.camelot.basecenter.tdk;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.BaseTDKConfigDAO;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.service.BaseTDKConfigService;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * <p>Description: [tdk设置实现类]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("baseTDKConfigService")
public class BaseTDKConfigServiceImpl implements BaseTDKConfigService {
	private static final Logger LOGGER=LoggerFactory
			.getLogger(BaseTDKConfigServiceImpl.class);
	@Resource
	private BaseTDKConfigDAO baseTDKConfigDAO;
	
	/**
	 * 保存tdk设置
	 * @param baseTDKConfigDTO
	 * BaseTDKConfigService@return
	 */
	@Override
	public ExecuteResult<BaseTDKConfigDTO> saveBaseTDKConfig(
			BaseTDKConfigDTO baseTDKConfigDTO) {
		LOGGER.info("=========保存tdk设置开始================");
		ExecuteResult<BaseTDKConfigDTO> result = new ExecuteResult<BaseTDKConfigDTO>();
		try {
			this.baseTDKConfigDAO.add(baseTDKConfigDTO);
			result.setResult(baseTDKConfigDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【saveBaseTDKConfig】出错,错误:" + e);
			result.addErrorMessage("执行方法【saveBaseTDKConfig】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束tdk设置保存====================");
		}
		return result;
	}
	/**
	 * 查询tdk设置
	 * @param baseTDKConfigDTO
	 * @return
	 */
	@Override
	public ExecuteResult<BaseTDKConfigDTO> queryBaseTDKConfig(
			BaseTDKConfigDTO baseTDKConfigDTO) {
		LOGGER.info("========查询tdk设置开始================");
		ExecuteResult<BaseTDKConfigDTO> result = new ExecuteResult<BaseTDKConfigDTO>();
		try {
			List<BaseTDKConfigDTO> baseTDKConfigDTOList = this.baseTDKConfigDAO.queryBaseTDKConfigList();
			result.setResult(null);
			if(baseTDKConfigDTOList != null && baseTDKConfigDTOList.size() > 0){
				result.setResult(baseTDKConfigDTOList.get(0));
			}
		} catch (Exception e) {
			LOGGER.error("调用方法【queryBaseTDKConfig】出错,错误:" + e);
			result.addErrorMessage("执行方法【queryBaseTDKConfig】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============查询tdk设置编辑====================");
		}
		return result;
	}
	
	/**
	 * 编辑tdk设置
	 * @param baseTDKConfigDTO
	 * @return
	 */
	@Override
	public ExecuteResult<BaseTDKConfigDTO> modifyBaseTDKConfig(
			BaseTDKConfigDTO baseTDKConfigDTO) {
		LOGGER.info("========编辑tdk设置开始================");
		ExecuteResult<BaseTDKConfigDTO> result = new ExecuteResult<BaseTDKConfigDTO>();
		try {
			this.baseTDKConfigDAO.update(baseTDKConfigDTO);
			result.setResult(baseTDKConfigDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【modifyBaseTDKConfig】出错,错误:" + e);
			result.addErrorMessage("执行方法【modifyBaseTDKConfig】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束tdk设置编辑====================");
		}
		return result;
	}

}
