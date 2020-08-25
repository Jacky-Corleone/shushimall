package com.camelot.storecenter.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopTemplatesDTO;

public interface ShopTemplatesDAO extends BaseDAO<ShopTemplatesDTO>{

	/**
	 * 
	 * <p>Discription:[修改模板状态]</p>
	 * Created on 2015-4-10
	 * @param templatesId
	 * @author:yuht
	 * @param status 
	 */
	void updateStatus(ShopTemplatesDTO shopTemplatesDTO);

	/**
	 * 
	 * <p>Discription:[根据店铺ID查询模板]</p>
	 * Created on 2015-4-14
	 * @param shopId
	 * @return
	 * @author:yuht
	 */
	ShopTemplatesDTO selectByShopId(Long shopId);
	
	/**
	 * 
	 * <p>Discription:[修改店铺模板颜色]</p>
	 * Created on 2015-4-14
	 * @param shopTemplatesDTO
	 * @author:yuht
	 */
	void updateColor(ShopTemplatesDTO shopTemplatesDTO);

}
