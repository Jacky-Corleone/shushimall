package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [商品搜索服务]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface SearchItemExportService {
	
	/**
	 * 
	 * <p>Discription:[商品搜索服务]</p>
	 * Created on 2015-3-6
	 * @return
	 * @author:wangcs
	 */
	@SuppressWarnings("rawtypes")
	public SearchOutDTO searchItem(SearchInDTO inDTO,Pager pager);
	
}
