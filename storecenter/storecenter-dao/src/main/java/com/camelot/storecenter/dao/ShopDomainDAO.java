package com.camelot.storecenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopDomainDTO;

public interface ShopDomainDAO extends BaseDAO<ShopDomainDTO> {

	public Long existShopUrl(@Param("shopUrl") String shopUrl,@Param("shopId") Long shopId);

	public Long existShopUrlByPlatformId(@Param("shopUrl") String shopUrl, @Param("platformId") Integer platformId,@Param("shopId") Long shopId);

}