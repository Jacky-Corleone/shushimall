package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopBrandDAO;
import com.camelot.storecenter.dao.ShopCategorySellerDAO;
import com.camelot.storecenter.dao.ShopInfoDAO;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.combin.ShopAudiDTO;
import com.camelot.storecenter.service.ShopAudiExportService;

@Service("shopAudiExportService")
public class ShopAudiExportServiceImpl implements ShopAudiExportService {
   private final static Logger logger = LoggerFactory.getLogger(ShopAudiExportServiceImpl.class);

	@Resource
	private ShopInfoDAO shopInfoDAO;
	@Resource
	private ShopBrandDAO shopBrandDAO;
	@Resource
	private ShopCategorySellerDAO shopCategorySellerDAO;
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public ExecuteResult<ShopAudiDTO> queryShopAuditInfo(Long shopId) {
		ExecuteResult<ShopAudiDTO> result=new ExecuteResult<ShopAudiDTO>();
		ShopAudiDTO shopAudiDTO=new ShopAudiDTO();
		try {
			ShopDTO shopInfo = shopInfoDAO.selectById(shopId);
			shopAudiDTO.setShopInfo(shopInfo);
			
			ShopBrandDTO sbDTO=new ShopBrandDTO();
			sbDTO.setShopId(shopId);
			Pager page=new Pager();
			page.setRows(Integer.MAX_VALUE);
			List<ShopBrandDTO> sbList=shopBrandDAO.selectListByCondition(sbDTO, page);
			shopAudiDTO.setSbList(sbList);
			
			ShopCategorySellerDTO scsDTO=new  ShopCategorySellerDTO();
			scsDTO.setShopId(shopId);
			List<ShopCategorySellerDTO> scsList = shopCategorySellerDAO.selectListByCondition(scsDTO, page);
			shopAudiDTO.setScsList(scsList);
			result.setResult(shopAudiDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		
		return result;
	}
	



}
