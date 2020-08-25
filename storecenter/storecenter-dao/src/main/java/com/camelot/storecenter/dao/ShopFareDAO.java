package com.camelot.storecenter.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopFareDTO;

/** 
 * <p>Description: [店铺运费dao]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ShopFareDAO extends BaseDAO<ShopFareDTO> {
	/**
	 * <p>Discription:[根据卖家id、店铺id查询运送范围]</p>
	 * Created on 2015年3月23日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
   public String queryBysellerIdAndShopId(ShopFareDTO dto);
   /**
    * <p>Discription:[根据运送范围和店铺id查询]</p>
    * Created on 2015年3月25日
    * @param dto
    * @return
    * @author:[杨芳]
    */
   public ShopFareDTO selectByFareRegion(ShopFareDTO dto);
   /**
    * <p>Discription:[店铺id查询运送范围是全国的]</p>
    * Created on 2015年3月25日
    * @param dto
    * @return
    * @author:[杨芳]
    */
   public ShopFareDTO selectByFareRegions(ShopFareDTO dto);
}
