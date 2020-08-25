package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopDomainDAO;
import com.camelot.storecenter.dao.ShopModifyDetailDAO;
import com.camelot.storecenter.dto.ShopDomainDTO;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;
import com.camelot.storecenter.service.ShopDomainExportService;
import com.camelot.storecenter.service.ShopModifyDetailExportService;

@Service("shopModifyDetailExportService")
public class ShopModifyDetailExportServiceImpl implements ShopModifyDetailExportService {
	private final static Logger logger = LoggerFactory.getLogger(ShopModifyDetailExportServiceImpl.class);

	@Resource
	private ShopModifyDetailDAO shopModifyDetailDAO;

	@Override
	public ExecuteResult<DataGrid<ShopModifyDetailDTO>> queryShopModifyDetail(ShopModifyDetailDTO shopModifyDetailDTO,Pager page) {
		
		ExecuteResult<DataGrid<ShopModifyDetailDTO>> result=new ExecuteResult<DataGrid<ShopModifyDetailDTO>>();
		
		try {
			DataGrid<ShopModifyDetailDTO> dataGrid=new DataGrid<ShopModifyDetailDTO>();
			List<ShopModifyDetailDTO> list=shopModifyDetailDAO.selectListByCondition(shopModifyDetailDTO, page);
			Long count = shopModifyDetailDAO.selectCountByCondition(shopModifyDetailDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}


}
