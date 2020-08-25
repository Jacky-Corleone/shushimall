package com.camelot.basecenter.dictionary;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.TypeDAO;
import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.basecenter.dto.TypeDTO;
import com.camelot.basecenter.service.TypeService;
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
@Service("typeService")
public class TypeServiceImpl implements TypeService {
	private final static Logger LOGGER = LoggerFactory.getLogger(TypeServiceImpl.class);
	@Resource
	private TypeDAO typeDAO;

	/**
	 * <p>Discription:[类型列表查询]</p>
	 * Created on 2015年8月11日
	 * @param id
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public List<TypeDTO> queryType() {
		return typeDAO.queryType();
	}

	/**
	 * <p>Discription:[按条件查询类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<TypeDTO> queryTypeById(Long id) {
		ExecuteResult<TypeDTO> result = new ExecuteResult<TypeDTO>();
		try {
			result.setResult(typeDAO.queryTypeById(id));
			result.setResultMessage("按条件查询类型成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("按条件查询类型失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[增加类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<String> addType(TypeDTO typeDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			typeDAO.add(typeDTO);
			result.setResultMessage("添加成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("添加失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[删除类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<String> delType(TypeDTO typeDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			typeDTO.setStatus(0);
			typeDAO.update(typeDTO);
			result.setResultMessage("删除成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("删除失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[修改类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public ExecuteResult<String> updType(TypeDTO typeDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			typeDAO.update(typeDTO);
			result.setResultMessage("修改成功！");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			result.setResultMessage("修改失败！");
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * <p>Discription:[按条件查询类型]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public DataGrid<TypeDTO> queryTypeByCondition(TypeDTO typeDTO,Pager pager) {
		DataGrid<TypeDTO> rpager = new DataGrid<TypeDTO>();
		rpager.setRows(typeDAO.queryTypeByCondition(typeDTO,pager));
		rpager.setTotal(typeDAO.queryTypeByConditionCount(typeDTO));
		return rpager;
	}

	/**
	 * <p>Discription:[类型唯一性]</p>
	 * Created on 2015年8月11日
	 * @param 
	 * @return
	 * @author:[王灿]
	 */
	@Override
	public boolean uniquenessType(TypeDTO typeDTO) {
		Long count = typeDAO.uniquenessType(typeDTO);
		if(count > 0){
			return false;
		}
		return true;
	}

}
