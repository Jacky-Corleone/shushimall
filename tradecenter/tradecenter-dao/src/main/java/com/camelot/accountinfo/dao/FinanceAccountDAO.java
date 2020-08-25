package com.camelot.accountinfo.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
/**
 * 
 * @author yangsaibei
 *
 * @version 2015年4月17日
 *
 */
public interface FinanceAccountDAO extends BaseDAO<FinanceAccountInfoDto> {
	/**
	 * 
	 * @param financeAccountInfoDto
	 * @return int
	 */
	public int updateFinanceAccountInfoDto(@Param("financeAccountInfoDto") FinanceAccountInfoDto financeAccountInfoDto);
	
	public FinanceAccountInfoDto queryById(@Param("financialCloudPaymentAccountId")Long id);

}
