package com.camelot.storecenter.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dao.ShopDomainDAO;
import com.camelot.storecenter.service.ShopDomainExportService;

@Service("shopDomainExportService")
public class ShopDomainExportServiceImpl implements ShopDomainExportService {
	private final static Logger logger = LoggerFactory.getLogger(ShopDomainExportServiceImpl.class);

	@Resource
	private ShopDomainDAO shopDomainDAO;
	
	@Override
	public ExecuteResult<Boolean> existShopUrl(String shopUrl,Long shopId) {
		
		ExecuteResult<Boolean> result=new ExecuteResult<Boolean>();
		try {

			Long count = shopDomainDAO.existShopUrl(shopUrl,shopId);
			if(count>0){
				result.setResult(false);
			}else{
				result.setResult(true);
			}
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
	public ExecuteResult<Boolean> existShopUrlByPlatformId(String shopUrl, Integer platformId,Long shopId) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		try {
			Long count = shopDomainDAO.existShopUrlByPlatformId(shopUrl, platformId,shopId);
			if (count > 0) {
				result.setResult(false);
			} else {
				result.setResult(true);
			}
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
