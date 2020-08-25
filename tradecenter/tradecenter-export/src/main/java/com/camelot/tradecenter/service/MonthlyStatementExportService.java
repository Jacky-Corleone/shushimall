package com.camelot.tradecenter.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dto.MonthlyStatementDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;

public interface MonthlyStatementExportService {

	/**
	 * <p>Discription:[根据条件查询详情]</p>
	 * Created on 2015年07月16日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<MonthlyStatementDTO> queryByMonthlyStatement(MonthlyStatementDTO dto);
	/**
	 * <p>Discription:[根据条件查询列表]</p>
	 * Created on 2015年07月16日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<MonthlyStatementDTO>> queryMonthlyStatementList(MonthlyStatementDTO dto, Pager page);
	/**
	 * <p>Discription:[生成协议订单]</p>
	 * Created on 2015年07月16日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> addMonthlyStatement(MonthlyStatementDTO dto);

	/**
	 * <p>Discription:[修改协议详情]</p>
	 * Created on 2015年07月16日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> modifyMonthlyStatement(MonthlyStatementDTO dto);

	/**
	 * <p>Discription:[根据用户查询所有的对账单号]</p>
	 * Created on 2015年06月13日
	 * @return
	 * @author:[马国平]
	 */
	public ExecuteResult<DataGrid<JSONObject>> queryPageGroupByUid(MonthlyStatementDTO queryParam,Pager pager);

	public ExecuteResult<DataGrid<TradeOrdersDTO>> queryOrders(TradeOrdersQueryInDTO inDTO,
															   Pager<TradeOrdersQueryInDTO> pager) ;
}
