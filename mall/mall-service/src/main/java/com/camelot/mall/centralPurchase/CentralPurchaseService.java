package com.camelot.mall.centralPurchase;

import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [集采活动]</p>
 * Created on 2015-12-9
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface CentralPurchaseService {
	/** 
	 * <p>Description: [获取集采活动]</p>
	 * Created on 2015-12-18
	 * @author  周志军
	 * @param page
	 * @param queryCentralPurchasingDTO,
	 * @return
	 */
	public Pager<QueryCentralPurchasingDTO> getCentralPurchase(QueryCentralPurchasingDTO queryCentralPurchasingDTO,Pager page);
}
