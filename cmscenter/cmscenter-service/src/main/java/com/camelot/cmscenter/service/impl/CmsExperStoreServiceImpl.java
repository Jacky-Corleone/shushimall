package com.camelot.cmscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.cmscenter.dao.CMSExperStoreDAO;
import com.camelot.cmscenter.dto.CmsExperStoreDTO;
import com.camelot.cmscenter.service.CmsExperStoreService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

@Service("cmsExperStoreService")
public class CmsExperStoreServiceImpl implements CmsExperStoreService {
	
	@Resource
	private CMSExperStoreDAO cMSExperStoreDAO;
	@Override
	public boolean add(CmsExperStoreDTO cmsExperStoreDTO) {
		boolean flag = false;
		cMSExperStoreDAO.add(cmsExperStoreDTO);
		flag = true;
		return flag;
	}

	@Override
	public boolean delete(CmsExperStoreDTO cmsExperStoreDTO) {
		boolean flag = false;
		cMSExperStoreDAO.delete(cmsExperStoreDTO);
		flag = true;
		return flag;
	}

	@Override
	public boolean update(CmsExperStoreDTO cmsExperStoreDTO) {
		boolean flag = false;
		cMSExperStoreDAO.update(cmsExperStoreDTO);
		flag = true;
		return flag;
	}

	@Override
	public CmsExperStoreDTO queryById(String id) {
		CmsExperStoreDTO dto = new CmsExperStoreDTO();
		dto = cMSExperStoreDAO.queryObjByid(id);
		return dto;
	}

	@Override
	public ExecuteResult<DataGrid<CmsExperStoreDTO>> queryExperStoreList(
			CmsExperStoreDTO cmsExperStoreDTO, Pager<?> page) {
			ExecuteResult<DataGrid<CmsExperStoreDTO>> result=new ExecuteResult<DataGrid<CmsExperStoreDTO>>();
			DataGrid<CmsExperStoreDTO> dataGrid=new DataGrid<CmsExperStoreDTO>();
			List<CmsExperStoreDTO> list = cMSExperStoreDAO.queryByPageList(page, cmsExperStoreDTO);
			Long count = cMSExperStoreDAO.getCount(cmsExperStoreDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
			return result;
		}
}
