package com.camelot.goodscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.camelot.goodscenter.dao.EvalTagDao;
import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagsOfCatDTO;
import com.camelot.goodscenter.service.EvalTagService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("evalTagService")
public class EvalTagServiceImpl implements EvalTagService {

	private static final Logger LOG = LoggerFactory.getLogger(EvalTagServiceImpl.class);
	
	@Resource
	private EvalTagDao evalTagDao;
	
	@Override
	public ExecuteResult<String> add(EvalTagsOfCatDTO tags) {
		ExecuteResult<String> addResult = new ExecuteResult<String>();
		try {
			int successRows = evalTagDao.add(tags);
			if (successRows > 0) {
				addResult.setResult("增加成功！");
			} else {
				addResult.addErrorMsg("增加失败！");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			addResult.addErrorMessage("操作异常，增加失败！");
		}
		
		return addResult;
	}

	@Override
	public ExecuteResult<DataGrid<EvalTagsOfCatDTO>> queryList(EvalTagsOfCatDTO param, Pager pager) {
		ExecuteResult<DataGrid<EvalTagsOfCatDTO>> queryResult = new ExecuteResult<DataGrid<EvalTagsOfCatDTO>>();
		DataGrid<EvalTagsOfCatDTO> evalTagsOfCatDTOGroups = new DataGrid<EvalTagsOfCatDTO>();
		try {
			List<Integer> cats = evalTagDao.queryCatsOfPage(param, pager);
			if (!CollectionUtils.isEmpty(cats)) {
				evalTagsOfCatDTOGroups.setRows(evalTagDao.queryList(param, cats));
			}
			evalTagsOfCatDTOGroups.setTotal(evalTagDao.countUnderCondition(param));
			queryResult.setResult(evalTagsOfCatDTOGroups);
		} catch (Exception e) {
			LOG.error(e.getMessage());
			queryResult.addErrorMessage(e.getMessage());
		}
		return queryResult;
	}

	@Override
	public ExecuteResult<EvalTagsOfCatDTO> queryOneGroupTags(EvalTagsOfCatDTO param) {
		ExecuteResult<EvalTagsOfCatDTO> queryResult = new ExecuteResult<EvalTagsOfCatDTO>();
		try {
			queryResult.setResult(evalTagDao.queryList(param, null).get(0));
		} catch(Exception e) {
			LOG.error(e.getMessage());
			queryResult.addErrorMessage(e.getMessage());
		}
		return queryResult;
	}

	@Override
	public ExecuteResult<String> updateNameOfOneTag(EvalTag tag) {
		ExecuteResult<String> updateResult = new ExecuteResult<String>();
		try {
			int updateCount = evalTagDao.updateNameOfOneTag(tag);
			if (updateCount <= 0) {
				updateResult.addErrorMessage("更新失败");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			updateResult.addErrorMessage("更新失败");
		}
		return updateResult;
	}

	@Override
	public ExecuteResult<String> deleteTags(Integer cidL2, List<String> tagIds) {
		ExecuteResult<String> updateResult = new ExecuteResult<String>();
		try {
			int updateCount = evalTagDao.deleteTags(cidL2, tagIds);
			if (updateCount <= 0) {
				updateResult.addErrorMessage("删除失败");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
			updateResult.addErrorMessage("删除失败");
		}
		return updateResult;
	}

	@Override
	public List<EvalTag> queryByItemId(Long itemId) {
		List<EvalTag> queriedTags = null;
		try {
			queriedTags = evalTagDao.queryByItemId(itemId);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return queriedTags;
	}

}
