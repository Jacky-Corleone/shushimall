package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [商品搜索业务service]</p>
 * Created on 2015-3-26
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface SearchService {

	/**
	 * 
	 * <p>Discription:[根据条件查询SKU相关信息]</p>
	 * Created on 2015-3-26
	 * @param inDTO
	 * @param pager  null时查询全部
	 * @return
	 * @author:wangcs
	 */
	@SuppressWarnings("rawtypes")
	DataGrid<ItemSkuDTO> queryItemSkus(SearchInDTO inDTO, Pager pager) throws Exception ;

	/**
	 * 
	 * <p>Discription:[根据输入的条件，反推出应该返回的条件<br>
	 * 查询反推的所有类目<br>
		查询反推的所有品牌<br>
		查询反推的所有非销售属性<br>]</p>
	 * Created on 2015-3-26
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	SearchOutDTO getSearchConditions(SearchInDTO inDTO);

}
