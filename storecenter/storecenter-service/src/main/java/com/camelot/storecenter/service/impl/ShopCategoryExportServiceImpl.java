package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopBrandDAO;
import com.camelot.storecenter.dao.ShopCategoryDAO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.service.ShopCategoryExportService;

@Service("shopCategoryExportService")
public class ShopCategoryExportServiceImpl implements ShopCategoryExportService{
	private final static Logger logger = LoggerFactory.getLogger(ShopCategoryExportServiceImpl.class);

	@Resource
	private ShopCategoryDAO shopCategoryDAO;
	@Resource
	private ShopBrandDAO shopBrandDAO;
	@Override
	public ExecuteResult<List<ShopCategoryDTO>> queryShopCategoryList(Long shopId,Integer status) {
		ExecuteResult<List<ShopCategoryDTO>> result=new ExecuteResult<List<ShopCategoryDTO>>();
		try {
			List<ShopCategoryDTO> list=shopCategoryDAO.selectByShopId(shopId,status);
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
	public ExecuteResult<String> addShopCategory(ShopCategoryDTO shopCategoryDTO) {
		ExecuteResult<String>  result=new ExecuteResult<String> ();
		try {
			shopCategoryDTO.setStatus(1);
			shopCategoryDAO.insert(shopCategoryDTO);
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
	public ExecuteResult<DataGrid<ShopCategoryDTO>> queryShopCategory(ShopCategoryDTO shopCategoryDTO,Pager page) {
		ExecuteResult<DataGrid<ShopCategoryDTO>> result=new ExecuteResult<DataGrid<ShopCategoryDTO>>();
		
		try {
			DataGrid<ShopCategoryDTO> dataGrid=new DataGrid<ShopCategoryDTO>();
			List<ShopCategoryDTO> list = shopCategoryDAO.selectListByCondition(shopCategoryDTO, page);
			Long counbt = shopCategoryDAO.selectCountByCondition(shopCategoryDTO);
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
	public ExecuteResult<DataGrid<ShopCategoryDTO>> queryShopCategoryAll(ShopCategoryDTO shopCategoryDTO,Pager page) {
		ExecuteResult<DataGrid<ShopCategoryDTO>> result=new ExecuteResult<DataGrid<ShopCategoryDTO>>();
		
		try {
			DataGrid<ShopCategoryDTO> dataGrid=new DataGrid<ShopCategoryDTO>();
			List<ShopCategoryDTO> list = shopCategoryDAO.selectListByConditionAll(shopCategoryDTO, page);
			Long counbt = shopCategoryDAO.selectCountByConditionAll(shopCategoryDTO);
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
	public ExecuteResult<String> modifyShopCategoryStatus(ShopCategoryDTO shopCategoryDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			Integer count=shopCategoryDAO.modifyShopCategoryStatus(shopCategoryDTO);
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
	public List<ShopCategoryDTO> selectShopIdById(Long id) {
		List<ShopCategoryDTO> result=shopCategoryDAO.selectShopIdById(id);
		return result;
	}
	@Override
	public void updateStatusByIdAndShopId(Long shopId) {
		shopCategoryDAO.updateStatusByIdAndShopId(shopId);
		
	}
	@Override
	public ExecuteResult<String> updateStatusByShopIdAndCid(Long shopId,Long cid) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		
		try {
			// 删除该店铺经营的该类目下的品牌
			shopBrandDAO.updateStatusByShopIdAndCid(shopId, cid);
			// 删除该店铺经营的类目
			shopCategoryDAO.updateStatusByShopIdAndCid(shopId, cid);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}


}
