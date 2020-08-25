package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dao.ShopTemplatesDAO;
import com.camelot.storecenter.dto.ShopTemplatesDTO;
import com.camelot.storecenter.service.ShopTemplatesExportService;

@Service("shopTemplatesExportService")
public class ShopTemplatesServiceImpl implements ShopTemplatesExportService{
	private final static Logger logger = LoggerFactory.getLogger(ShopTemplatesServiceImpl.class);

	@Resource
	private ShopTemplatesDAO shopTemplatesDAO;
	@Override
	public ExecuteResult<List<ShopTemplatesDTO>> createShopTemplatesList(ShopTemplatesDTO shopTemplatesDTO) {
		
		ExecuteResult<List<ShopTemplatesDTO>> result=new ExecuteResult<List<ShopTemplatesDTO>>();
		
		try {
			List<ShopTemplatesDTO> list = shopTemplatesDAO.selectListByCondition(shopTemplatesDTO, null);
			if(list.size()<=0){
				ShopTemplatesDTO d1=new ShopTemplatesDTO();
				ShopTemplatesDTO d2=new ShopTemplatesDTO();
				d1.setTemplatesName("shopTemplate1");
				d1.setTemplatesInfo("店铺装修模板1");
				d1.setColor("#ff0000");
				d1.setShopId(shopTemplatesDTO.getShopId());
				d2.setTemplatesName("shopTemplate2");
				d2.setTemplatesInfo("店铺装修模板2");
				d2.setColor("#ff0000");
				d2.setShopId(shopTemplatesDTO.getShopId());
				shopTemplatesDAO.insert(d1);
				shopTemplatesDAO.insert(d2);
				list=shopTemplatesDAO.selectListByCondition(shopTemplatesDTO, null);
			}
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
	@Override
	public ExecuteResult<String> modifyShopTemplatesStatus(Long templatesId,Long shopId) {
		ExecuteResult<String>  result=new ExecuteResult<String>();
		try {
			ShopTemplatesDTO shopTemplatesDTO=new ShopTemplatesDTO();
			shopTemplatesDTO.setShopId(shopId);
			
			List<ShopTemplatesDTO> list =shopTemplatesDAO.selectListByCondition(shopTemplatesDTO, null);
			for (ShopTemplatesDTO dt2 : list) {
				if(dt2.getStatus()==1){
					dt2.setStatus(2);
					shopTemplatesDAO.updateStatus(dt2);
				}
			}
			shopTemplatesDTO.setId(templatesId);
			shopTemplatesDTO.setStatus(1);
			shopTemplatesDAO.updateStatus(shopTemplatesDTO);
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
	public ExecuteResult<String> modifyShopTemplatesColor(Long templatesId, String color) {
		ExecuteResult<String>  result=new ExecuteResult<String>();
		try {
			ShopTemplatesDTO shopTemplatesDTO=new ShopTemplatesDTO();
			shopTemplatesDTO.setColor(color);
			shopTemplatesDTO.setId(templatesId);
			shopTemplatesDAO.updateColor(shopTemplatesDTO);
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
