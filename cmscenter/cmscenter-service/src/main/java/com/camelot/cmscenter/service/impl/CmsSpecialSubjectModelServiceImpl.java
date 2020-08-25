package com.camelot.cmscenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.cmscenter.dao.CmsSpecialSubjectModelDAO;
import com.camelot.cmscenter.dto.CmsSpecialSubjectModelDTO;
import com.camelot.cmscenter.service.CmsSpecialSubjectModelService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("cmsSpecialSubjectModelService")
public class CmsSpecialSubjectModelServiceImpl implements
		CmsSpecialSubjectModelService {
	
	private static final Logger logger = LoggerFactory.getLogger(CmsSpecialSubjectModelService.class);
	
	@Resource
	private CmsSpecialSubjectModelDAO cmsSpecialSubjectModelDAO;

	@Override
	public ExecuteResult<DataGrid<CmsSpecialSubjectModelDTO>> queryCmsSpecSubModelList(CmsSpecialSubjectModelDTO cmsSpecialSubjectModelDTO,Pager<?> page)
			throws Exception {
		ExecuteResult<DataGrid<CmsSpecialSubjectModelDTO>> result=new ExecuteResult<DataGrid<CmsSpecialSubjectModelDTO>>();
		try {
			DataGrid<CmsSpecialSubjectModelDTO> dataGrid=new DataGrid<CmsSpecialSubjectModelDTO>();
			List<CmsSpecialSubjectModelDTO> list = cmsSpecialSubjectModelDAO.queryModelList(cmsSpecialSubjectModelDTO, page);
			Long count = cmsSpecialSubjectModelDAO.getCount(cmsSpecialSubjectModelDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			throw new RuntimeException();
		}
		
		return result;
	}

	@Override
	public CmsSpecialSubjectModelDTO getCmsSpecialSubjectModel(String id)
			throws Exception {
		return cmsSpecialSubjectModelDAO.queryModelById(id);
	}

	@Override
	public boolean deleteCmsSpecialSubjectModel(String id) throws Exception {
		boolean flag = false;
		cmsSpecialSubjectModelDAO.deleteModel(id);
		flag = true;
		return flag;
	}

	@Override
	public boolean saveCmsSpecialSubjectModel(CmsSpecialSubjectModelDTO specialSubjectModelDTO) throws Exception {
		boolean flag = false;
		cmsSpecialSubjectModelDAO.insert(specialSubjectModelDTO);
		flag = true;
		return flag;
	}
	
	@Override
	public boolean updateCmsSpecialSubjectModel(CmsSpecialSubjectModelDTO specialSubjectModelDTO) throws Exception {
		boolean flag = false;
		cmsSpecialSubjectModelDAO.update(specialSubjectModelDTO);
		flag = true;
		return flag;
	}

	@Override
	public List<CmsSpecialSubjectModelDTO> queryModelList() throws Exception {
		List<CmsSpecialSubjectModelDTO> queryModelList = new ArrayList<CmsSpecialSubjectModelDTO>();
		queryModelList = cmsSpecialSubjectModelDAO.queryAllModelList();
		return queryModelList;
	}

}
