package com.camelot.settlecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.settlecenter.dto.combin.SettlementCombinDTO;
import com.camelot.settlecenter.dto.indto.SettlementInDTO;

/**
 * 
 * <p>Description: [结算单DAO]</p>
 * Created on 2015-3-10
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface StatementCombinDAO extends BaseDAO<SettlementCombinDTO>{

	/**
	 * 
	 * <p>Discription:[结算单查询带分页]</p>
	 * Created on 2015-3-10
	 * @param settlementInDTO
	 * @param page 
	 * @return
	 * @author:yuht
	 */
	List<SettlementCombinDTO> querySettlementList(@Param("entity")SettlementInDTO settlementInDTO, @SuppressWarnings("rawtypes") @Param("page") Pager page);
	
	/**
	 * 
	 * <p>Discription:[结算单查询总数量]</p>
	 * Created on 2015-3-10
	 * @param settlementInDTO
	 * @return
	 * @author:yuht
	 */
	Long querySettlementListCount(@Param("entity")SettlementInDTO settlementInDTO);

}
