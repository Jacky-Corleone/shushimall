package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [商品SKU询价Service]</p>
 * Created on 2015年3月11日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ItemSkuInquiryPriceExportService {
	/**
	 * <p>Discription:[根据id查询详情]</p>
	 * Created on 2015年3月28日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
    public ExecuteResult<ItemSkuInquiryPriceDTO> queryById(Long id);
	/**
	 * <p>Discription:[商品SKU询价的列表查询]</p>
	 * Created on 2015年3月11日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
   public ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> queryList(ItemSkuInquiryPriceDTO dto,Pager page);
   
   /**
    * <p>Discription:[发起询价]</p>
    * Created on 2015年3月12日
    * @param dto
    * @return
    * @author:[杨芳]
    */
   public ExecuteResult<String> addItemSkuInquiryPrice(ItemSkuInquiryPriceDTO dto);
   
   /**
    * <p>Discription:[询价修改]</p>
    * Created on 2015年3月12日
    * @param dto
    * @return
    * @author:[杨芳]
    */
   public ExecuteResult<String> modifyItemSkuInquiryPrice(ItemSkuInquiryPriceDTO dto);
   /**
    * <p>Discription:[根据买家id、卖家id、商品id、sku号]</p>
    * Created on 2015年3月18日
    * @param dto
    * @return
    * @author:[杨芳]
    */
   public ExecuteResult<ItemSkuInquiryPriceDTO> queryByIdsAndNumber(ItemSkuInquiryPriceDTO dto);
}
