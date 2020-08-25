package com.camelot.sellercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.dao.MallWordDAO;
import com.camelot.sellercenter.mallword.dto.MallWordDTO;
import com.camelot.sellercenter.mallword.service.MallWordExportService;

@Service("mallWordExportService")
public class MallWordExportServiceImpl implements MallWordExportService {
	@Resource
	private MallWordDAO mallWordDao;
	
	@Override
	public ExecuteResult<MallWordDTO> add(MallWordDTO dto) {
		ExecuteResult<MallWordDTO> er = new ExecuteResult<MallWordDTO>();
		this.mallWordDao.add(dto);
		er.setResult(dto);
		return er;
	}

	@Override
	public ExecuteResult<String> delete(Long id) {
		this.mallWordDao.delete(id);
		return new ExecuteResult<String>();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<MallWordDTO> datagrid(MallWordDTO dto, Pager page) {
		DataGrid<MallWordDTO> dg = new DataGrid<MallWordDTO>();
		
		List<MallWordDTO> list = this.mallWordDao.queryList(dto, page);
		Long total = this.mallWordDao.queryCount(dto);
		
		dg.setTotal(total);
		dg.setRows(list);
		return dg;
	}

}
