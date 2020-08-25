package com.camelot.storecenter.dao;


import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopRenovationDTO;

public interface ShopRenovationDAO extends BaseDAO<ShopRenovationDTO>{

	void deleteTid(ShopRenovationDTO shopRenovationDTO);

}
