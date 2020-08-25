package com.camelot.tradecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.TradeOrdersEnclosureDTO;
/**
 * 
 * <p>Description: [订单附近表接口]</p>
 * Created on 2016年2月17日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface TradeOrdersEnclosureService {
	/**
	 * 
	 * <p>Discription:[增加订单附件]</p>
	 * Created on 2016年2月17日
	 * @param tradeOrdersEnclosureDTO
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<TradeOrdersEnclosureDTO> addEnclosure(TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO);

	/**
	 * 
	 * <p>Discription:[分页查询订单附件]</p>
	 * Created on 2016年2月17日
	 * @param tradeOrdersEnclosureDTO
	 * @param pager
	 * @return
	 * @author:[李伟龙]
	 */
	public DataGrid<TradeOrdersEnclosureDTO> queryEnclosures(TradeOrdersEnclosureDTO tradeOrdersEnclosureDTO, Pager<TradeOrdersEnclosureDTO> pager);

	
	public ExecuteResult<String> delEnclosure(long id);
	
	public ExecuteResult<TradeOrdersEnclosureDTO> queryById(long id);
}
