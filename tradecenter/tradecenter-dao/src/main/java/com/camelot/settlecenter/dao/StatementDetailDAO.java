package com.camelot.settlecenter.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;

/**
 * 
 * <p>Description: [结算单DAO]</p>
 * Created on 2015-3-10
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface StatementDetailDAO extends BaseDAO<SettlementDetailDTO>{

	/**
	 * 根据结算单ID修改状态  可修改项 type/status
	 * 
	 * @param settlementDetailDTO
	 * @return
	 */
	int updateStatuBySettleId(SettlementDetailDTO settlementDetailDTO);
	
	/**
	 * 根据订单ID修改
	 * 
	 * @param settlementDetailDTO
	 * @return
	 */
	int updateByOrderId(SettlementDetailDTO settlementDetailDTO);
	
}
