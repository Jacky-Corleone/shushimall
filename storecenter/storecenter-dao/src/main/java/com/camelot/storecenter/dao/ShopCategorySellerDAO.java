package com.camelot.storecenter.dao;



import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;


/**
 * 
 * <p>Description: [店铺经营类目]</p>
 * Created on 2015-3-7
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopCategorySellerDAO  extends BaseDAO<ShopCategorySellerDTO>{
	   public int insertShopCategory(ShopCategorySellerDTO dto);
	   
	   public int deletes(@Param("cids")Long[] cids);
}