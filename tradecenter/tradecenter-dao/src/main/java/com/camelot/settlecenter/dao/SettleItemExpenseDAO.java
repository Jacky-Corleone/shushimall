package com.camelot.settlecenter.dao;

import java.util.List;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.settlecenter.dto.SettleItemExpenseDTO;

public interface SettleItemExpenseDAO extends BaseDAO<SettleItemExpenseDTO>{
	
	
	/**
	 * 
	 * <p>Discription:[根据ID组查询类目信息]</p>
	 * Created on 2015-4-22
	 * @param id
	 * @return
	 * @author:yuht
	 */
	List<SettleItemExpenseDTO> queryByIds(Long[] id);
	/**
	 * 
	 * <p>Discription:[根据itemId查询费用信息]</p>
	 * Created on 2015-4-23
	 * @param itemId
	 * @return
	 * @author:yuht
	 */
	SettleItemExpenseDTO queryByItemId(Long itemId);

}
