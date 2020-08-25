package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.shop.domain.Area;
import com.camelot.storecenter.dao.ShopDomainDAO;
import com.camelot.storecenter.dao.ShopInfoDAO;
import com.camelot.storecenter.dao.ShopModifyDetailDAO;
import com.camelot.storecenter.dao.ShopModifyInfoDAO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopDomainDTO;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;
import com.camelot.storecenter.service.ShopDomainExportService;
import com.camelot.storecenter.service.ShopModifyInfoExportService;

@Service("shopModifyInfoExportService")
public class ShopModifyInfoExportServiceImpl implements ShopModifyInfoExportService {
	private final static Logger logger = LoggerFactory.getLogger(ShopModifyInfoExportServiceImpl.class);

	@Resource
	private ShopModifyInfoDAO shopModifyInfoDAO;
	@Resource
	private ShopModifyDetailDAO shopModifyDetailDAO;
	@Resource
	private ShopInfoDAO shopInfoDAO;

	@Override
	public ExecuteResult<DataGrid<ShopModifyInfoDTO>> queryShopModifyInfo(ShopModifyInfoDTO shopModifyInfoDTO,Pager page) {
		ExecuteResult<DataGrid<ShopModifyInfoDTO>> result=new ExecuteResult<DataGrid<ShopModifyInfoDTO>>();
		
		
		try {
			DataGrid<ShopModifyInfoDTO> dataGrid=new DataGrid<ShopModifyInfoDTO>();
			List<ShopModifyInfoDTO> list=shopModifyInfoDAO.selectListGroupShopId(shopModifyInfoDTO, page);
			Long count = shopModifyInfoDAO.selectCountGroupShopId(shopModifyInfoDTO);
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

	@Override
	public ExecuteResult<String> modifyShopModifyStatus(ShopModifyInfoDTO shopModifyInfoDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try {
			List<ShopModifyDetailDTO> smdList = shopModifyDetailDAO.selectListById(shopModifyInfoDTO.getId());
			if(shopModifyInfoDTO.getApplyStatus()==1){
				for (ShopModifyDetailDTO shopModifyDetailDTO : smdList) {
					//pcdCombip 未修改省市县 特殊处理
					if("pcdCombin".equals(shopModifyDetailDTO.getPropertiesColumn())){
						String area=shopModifyDetailDTO.getAfterChange();
						Area areaOb=JSON.parseObject(area, Area.class);
						ShopDTO shopDTO=new ShopDTO();
						shopDTO.setProvinceCode(areaOb.getProvinceCode());
						shopDTO.setProvinceName(areaOb.getProvinceName());
						shopDTO.setCityCode(areaOb.getCityCode());
						shopDTO.setCityName(areaOb.getCityName());
						shopDTO.setDistrictCode(areaOb.getDistrictCode());
						shopDTO.setDistrictName(areaOb.getDistrictName());
						shopDTO.setShopId(shopModifyInfoDTO.getShopId());
						shopInfoDAO.update(shopDTO);
					}else{
						shopModifyDetailDTO.setShopId(shopModifyInfoDTO.getShopId());
						shopInfoDAO.updateShopInfo(shopModifyDetailDTO);
					}
				}
				//修改完成后删除修改详情表
				shopModifyDetailDAO.delete(shopModifyInfoDTO.getId());
				//将修改总表 修改为通过
				shopModifyInfoDAO.update(shopModifyInfoDTO);
				result.setResultMessage("success");
			}else if(shopModifyInfoDTO.getApplyStatus()==2){
				shopModifyDetailDAO.delete(shopModifyInfoDTO.getId());
				shopModifyInfoDAO.update(shopModifyInfoDTO);
				result.setResultMessage("success");
			}else{
				result.setResultMessage("applyStatus error，must 1 or 2");
			}
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<List<ShopModifyInfoDTO>> queryShopModifyInfoById(Long... shopIds) {
		ExecuteResult<List<ShopModifyInfoDTO>> result=new ExecuteResult<List<ShopModifyInfoDTO>>();
		try {
			List<ShopModifyInfoDTO> list=shopModifyInfoDAO.selectByIds(shopIds);
			result.setResult(list);
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
