package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopBrandDAO;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.service.ShopBrandExportService;

@Service("shopBrandExportService")
public class ShopBrandExportServiceImpl implements ShopBrandExportService{
	private final static Logger logger = LoggerFactory.getLogger(ShopBrandExportServiceImpl.class);

	@Resource
	private ShopBrandDAO shopBrandDAO;
	
	@Override
	public ExecuteResult<List<ShopBrandDTO>> queryShopBrandList(Long shopId,Integer status) {
		ExecuteResult<List<ShopBrandDTO>> result=new ExecuteResult<List<ShopBrandDTO>>();
		try {
			List<ShopBrandDTO> list=shopBrandDAO.selectByShopId(shopId,status);
			result.setResult(list);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public ExecuteResult<String> addShopBrand(ShopBrandDTO shopBrandDTO) {
		ExecuteResult<String>  result=new ExecuteResult<String> ();
		try {
			shopBrandDAO.insert(shopBrandDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ShopBrandDTO>> queryShopBrand(ShopBrandDTO shopBrandDTO, Pager page) {
		ExecuteResult<DataGrid<ShopBrandDTO>> result=new ExecuteResult<DataGrid<ShopBrandDTO>>();
		
		try {
			DataGrid<ShopBrandDTO> dataGrid=new DataGrid<ShopBrandDTO>();
			List<ShopBrandDTO> list = shopBrandDAO.selectListByCondition(shopBrandDTO, page);
			Long counbt = shopBrandDAO.selectCountByCondition(shopBrandDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(counbt);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		
		return result;
	}

	@Override
	public ExecuteResult<String> modifyShopBrandStatus(ShopBrandDTO shopBrandDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			Integer count=shopBrandDAO.modifyShopCategoryStatus(shopBrandDTO);
			result.setResult(count.toString());
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<DataGrid<ShopBrandDTO>> queryShopBrandAll(ShopBrandDTO shopBrandDTO, Pager page) {
			ExecuteResult<DataGrid<ShopBrandDTO>> result=new ExecuteResult<DataGrid<ShopBrandDTO>>();
			try {
				DataGrid<ShopBrandDTO> dataGrid=new DataGrid<ShopBrandDTO>();
				List<ShopBrandDTO> list = shopBrandDAO.selectListByConditionAll(shopBrandDTO, page);
				Long counbt = shopBrandDAO.selectCountByConditionAll(shopBrandDTO);
				dataGrid.setRows(list);
				dataGrid.setTotal(counbt);
				result.setResult(dataGrid);
				result.setResultMessage("success");
			} catch (Exception e) {
				result.setResultMessage("error");
				result.getErrorMessages().add(e.getMessage());
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			
			return result;
	}
	@Override
	public List<ShopBrandDTO> selectBrandIdById(Long id) {
		List<ShopBrandDTO> result=shopBrandDAO.selectBrandIdById(id);
		return result;
	}

	@Override
	public void updateStatusByIdAndBrandId(Long brandId) {
		shopBrandDAO.updateStatusByIdAndBrandId(brandId);
	}

	@Override
	public ExecuteResult<String> updateStatusByShopIdAndCid(Long shopId,
			Long cid) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			shopBrandDAO.updateStatusByShopIdAndCid(shopId, cid);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@Override
	public ExecuteResult<String> updateStatusByShopIdAndBrandId(Long shopId,
			Long brandId) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			shopBrandDAO.updateStatusByShopIdAndBrandId(shopId, brandId);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
}
