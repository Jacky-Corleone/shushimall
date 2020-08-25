package com.camelot.goodscenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

/** 
 * <p>Description: [询价dao]</p>
 * Created on 2015年3月12日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ItemSkuInquiryPriceDAO extends BaseDAO<ItemSkuInquiryPriceDTO> {

	  /**
	    * <p>Discription:[根据买家id、卖家id、商品id、sku号、数量查询]</p>
	    * Created on 2015年3月18日
	    * @param dto
	    * @return
	    * @author:[杨芳]
	    */
  public  ItemSkuInquiryPriceDTO selectByIdsAndNumber(@Param("dto")ItemSkuInquiryPriceDTO dto);
}
