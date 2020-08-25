package com.camelot.settlecenter.service;


import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;
import com.camelot.settlecenter.dto.SettlementInfoOutDTO;
import com.camelot.settlecenter.dto.indto.SettlementInfoInDTO;


public interface SettleItemExpenseExportService {
	/**
	 * 
	 * <p>Discription:[商品返点费用查询]</p>
	 * Created on 2015-4-22
	 * @param sieDto
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<SettleItemExpenseDTO>> querySettleItemExpense(SettleItemExpenseDTO sieDto,Pager page);
	
	/**
	 * 
	 * <p>Discription:[根据ID查询商品返点费用信息]</p>
	 * Created on 2015-4-22
	 * @param id
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<SettleItemExpenseDTO> getSettleItemExpense(Long id);
	/**
	 * 
	 * <p>Discription:[添加商品返点费用]</p>
	 * Created on 2015-4-22
	 * @param sieDto
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> addSettleItemExpense(SettleItemExpenseDTO sieDto);
	/**
	 * 
	 * <p>Discription:[修改商品返点费用]</p>
	 * Created on 2015-4-22
	 * @param sieDto
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifySettleItemExpense(SettleItemExpenseDTO sieDto);
	
	/**
	 * 
	 * <p>Discription:[返点费用查询]</p>
	 * Created on 2015-4-23
	 * @param settlementInfoInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<SettlementInfoOutDTO> getSettlementInfo(SettlementInfoInDTO settlementInfoInDTO );
}
