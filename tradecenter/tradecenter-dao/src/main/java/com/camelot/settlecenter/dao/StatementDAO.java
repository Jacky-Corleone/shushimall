package com.camelot.settlecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.settlecenter.dto.SettlementDTO;

/**
 * 
 * <p>Description: [结算单DAO]</p>
 * Created on 2015-3-10
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface StatementDAO extends BaseDAO<SettlementDTO>{
	
	/**
	 * 
	 * <p>Discription:[批量修改结算单状态]</p>
	 * Created on 2015-3-10
	 * @param long1
	 * @param status - 状态：0[待出账] 1[待结算] 2[待确认] 3[已完成]
	 * @return
	 * @author:yuht
	 */
	Integer modifySettlementStates(@Param("id")Long id,@Param("status")Integer status);
	
	/**
	 * 获取当前店铺今天所在的周期内的结算单
	 * 
	 * @return
	 */
	SettlementDTO selectSettleByPeriod(Long shopId);

	/**
	 * 获取应结算日期小于当前日期的结算单
	 * 
	 * @param status - 状态：0[待出账] 1[待结算] 2[待确认] 3[已完成]
	 * @return
	 */
	List<SettlementDTO> selectSettleByStatus(Integer status);
	
	/**
	 * 获取应结算日期小于当前日期的结算单
	 * 
	 * @param status - 状态：0[待出账] 1[待结算] 2[待确认] 3[已完成]
	 * @return
	 */
	List<SettlementDTO> selectSettleForSellerSettlement(Integer status);
}
