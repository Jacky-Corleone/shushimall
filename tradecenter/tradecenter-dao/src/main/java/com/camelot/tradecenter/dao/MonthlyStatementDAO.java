package com.camelot.tradecenter.dao;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.tradecenter.dto.MonthlyStatementDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface MonthlyStatementDAO extends BaseDAO<MonthlyStatementDTO> {

	public List<MonthlyStatementDTO> queryPage(@Param("pager") Pager pager, @Param("monthlyStatement") MonthlyStatementDTO monthlyStatement);
	
	public Long queryPageCount(@Param("monthlyStatement") MonthlyStatementDTO monthlyStatement);
	
	public MonthlyStatementDTO findById(Long id);
	
	public Integer insert(MonthlyStatementDTO monthlyStatement);
	
	public Integer update(MonthlyStatementDTO monthlyStatement);

	public void delete(@Param("codes") List<String> codes);
	
	public List<Map<String,Object>> findAll();

	public MonthlyStatementDTO findByMonthlyStatementDTO(@Param("monthlyStatement") MonthlyStatementDTO monthlyStatement);

	public List<MonthlyStatementDTO> queryPageGroupByUid(@Param("pager") Pager pager,@Param("monthlyStatement") MonthlyStatementDTO queryParam);

	public Long queryPageGroupByUidCount(@Param("monthlyStatement") MonthlyStatementDTO queryParam);

	public List<String> queryOrderByStatementId(@Param("statementId")String statementId);

	List<TradeOrdersDTO> queryTradeOrders(@Param("entity") TradeOrdersQueryInDTO inDTO, @Param("page") Pager<TradeOrdersQueryInDTO> pager);

	Long queryTradeOrdersCount(@Param("entity") TradeOrdersQueryInDTO inDTO);
}
