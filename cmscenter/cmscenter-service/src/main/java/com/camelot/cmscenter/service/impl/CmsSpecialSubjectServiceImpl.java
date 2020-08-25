package com.camelot.cmscenter.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.camelot.cmscenter.dao.CmsSpecialSubjectDAO;
import com.camelot.cmscenter.dto.CmsSpecialSubjectDTO;
import com.camelot.cmscenter.service.CmsSpecialSubjectService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("cmsSpecialSubjectService")
public class CmsSpecialSubjectServiceImpl implements CmsSpecialSubjectService {
	
	private static final Logger logger = LoggerFactory.getLogger(CmsSpecialSubjectService.class);
	
	@Autowired
	private CmsSpecialSubjectDAO cmsSpecialSubjectDAO;

	@Override
	public ExecuteResult<DataGrid<CmsSpecialSubjectDTO>> queryCmsSpecialSubjectList(Pager<?> page, CmsSpecialSubjectDTO specialSubjectDTO)throws Exception {
		ExecuteResult<DataGrid<CmsSpecialSubjectDTO>> result=new ExecuteResult<DataGrid<CmsSpecialSubjectDTO>>();
		try {
			DataGrid<CmsSpecialSubjectDTO> dataGrid=new DataGrid<CmsSpecialSubjectDTO>();
			List<CmsSpecialSubjectDTO> list = cmsSpecialSubjectDAO.queryDTOList(page, specialSubjectDTO);
			Long count = cmsSpecialSubjectDAO.getCount(specialSubjectDTO);
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
	public CmsSpecialSubjectDTO getCmsSpecialSubjectById(String id)throws Exception {
		return cmsSpecialSubjectDAO.queryById(id);
	}

	@Override
	public boolean deleteCmsSpecialSubjectById(CmsSpecialSubjectDTO specialSubjectDTO) throws Exception {
		boolean flag = false;
		cmsSpecialSubjectDAO.deleteCss(specialSubjectDTO);
		flag = true;
		return flag;
	}

	@Override
	public boolean saveCmsSpecialSubject(CmsSpecialSubjectDTO specialSubjectDTO) throws Exception {
		boolean flag = false;
		cmsSpecialSubjectDAO.insertCss(specialSubjectDTO);
		flag = true;
		return flag;
	}
	
	@Override
	public boolean updateCmsSpecialSubject(CmsSpecialSubjectDTO specialSubjectDTO) throws Exception {
		boolean flag = false;
		cmsSpecialSubjectDAO.updateCss(specialSubjectDTO);
		flag = true;
		return flag;
	}

	@Override
	public boolean release(CmsSpecialSubjectDTO specialSubjectDTO)
			throws Exception {
		boolean flag = false;
		cmsSpecialSubjectDAO.releaseCss(specialSubjectDTO);
		flag = true;
		return flag;
	}

}
