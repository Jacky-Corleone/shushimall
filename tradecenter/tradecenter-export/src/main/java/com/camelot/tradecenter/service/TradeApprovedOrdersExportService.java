package com.camelot.tradecenter.service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

public interface TradeApprovedOrdersExportService {
	/**
	 * 
	 * <p>Discription:[订单审核通过]</p>
	 * Created on 2015-8-26
	 * @param tradeOrdersDTO
	 * @return
	 * @author:[王鹏]
	 */
	 public ExecuteResult<String> approveSubmit(TradeOrdersDTO tradeOrdersDTO);
	 /**
	  * 
	  * <p>Discription:[订单审核驳回]</p>
	  * Created on 2015-8-26
	  * @param tradeOrdersDTO
	  * @return
	  * @author:[王鹏]
	  */
	 public ExecuteResult<String> approveReject(TradeOrdersDTO tradeOrdersDTO);

}
