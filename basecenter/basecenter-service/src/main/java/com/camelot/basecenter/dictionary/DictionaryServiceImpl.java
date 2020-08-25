package com.camelot.basecenter.dictionary;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dao.DictionaryDAO;
import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.basecenter.service.DictionaryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("dictionaryService")
public class DictionaryServiceImpl implements DictionaryService {

	private final static Logger LOGGER = LoggerFactory.getLogger(DictionaryServiceImpl.class);
    @Resource
	private DictionaryDAO dictionaryDAO;
    
    /**
	 * <p>Discription:[类型列表字典]</p>
	 * Created on 2015年8月11日
	 * @param id
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public List<DictionaryDTO> queryDictionaryNameList() {
		return dictionaryDAO.queryDictionaryNameList();
		
	}

	/**
	 * <p>Discription:[按id查询字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<DictionaryDTO> queryDictionaryById(Long id) {
		ExecuteResult<DictionaryDTO> result = new ExecuteResult<DictionaryDTO>();
		try {
			result.setResult(dictionaryDAO.queryDictionaryById(id));
			result.setResultMessage("按条件查询字典成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("按条件查询字典失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[按编码查询名称]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<List<DictionaryDTO>> queryDictionaryByCode(String code , String type) {
		ExecuteResult<List<DictionaryDTO>> result = new ExecuteResult<List<DictionaryDTO>>();
		try {
			result.setResult(dictionaryDAO.queryDictionaryByCode(code,type));
			result.setResultMessage("按编码查询名称成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("按编码查询名称失败！");
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 获取物流公司列表信息
	 * @param dictionaryDTO
	 * @param type
	 * @param pager
	 * @return
	 */
	@Override
	public DataGrid<DictionaryDTO> queryDictionaryByType(DictionaryDTO dictionaryDTO,Pager pager){
		LOGGER.info("方法[{}]，入参：[{}][{}][{}]", "DictionaryServiceImpl-queryDictionaryByType", pager);
        DataGrid<DictionaryDTO> dataGrid = new DataGrid<DictionaryDTO>();

        long size = dictionaryDAO.queryCountNoParent(dictionaryDTO);
        if (size > 0) {
            List<DictionaryDTO> listDictionaryDTO = dictionaryDAO.queryDictionaryNoParent(dictionaryDTO, pager);
            dataGrid.setRows(listDictionaryDTO);
            dataGrid.setTotal(size);
        }
        LOGGER.info("方法[{}]，出参：[{}]", "DictionaryServiceImpl-queryDictionaryByType", JSONObject.toJSON(dataGrid));
        return dataGrid;
    }
	
	/**
	 * <p>Discription:[增加字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<String> addDictionary(DictionaryDTO dictionaryDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			dictionaryDAO.add(dictionaryDTO);
			result.setResultMessage("增加字典成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("增加字典失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[删除字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<String> delDictionary(DictionaryDTO dictionaryDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			dictionaryDTO.setStatus(0);
			dictionaryDAO.update(dictionaryDTO);
			result.setResultMessage("删除字典成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("删除字典失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[修改字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<String> updDictionary(DictionaryDTO dictionaryDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			dictionaryDAO.update(dictionaryDTO);
			result.setResultMessage("修改字典成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("修改字典失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[按条件查询字典]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public DataGrid<DictionaryDTO> queryDictionaryByCondition(
			DictionaryDTO dictionaryDTO,Pager pager) {
		DataGrid<DictionaryDTO> rpager = new DataGrid<DictionaryDTO>();
		rpager.setRows(dictionaryDAO.queryDictionaryByCondition(dictionaryDTO,pager));
		rpager.setTotal(dictionaryDAO.queryDictionaryByConditionCount(dictionaryDTO));
		return rpager;
	}

}
