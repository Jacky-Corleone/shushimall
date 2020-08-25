package com.camelot.settlecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;

/**
 * 
 * <p>Description: [类目费用service]</p>
 * Created on 2015-3-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface SattleCatExpenseExportService {

	/**
	 * <p>Discription:[类目费用列表查询]</p>
	 * Created on 2015年3月9日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<DataGrid<SettleCatExpenseDTO>> queryCategoryExpenseList(SettleCatExpenseDTO dto,@SuppressWarnings("rawtypes") Pager pager);
   
  /**
   * <p>Discription:[根据类目id或者类目id组查询]</p>
   * Created on 2015年3月9日
   * @param ids 可以是数组、也可以是long类型的参数
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<List<SettleCatExpenseDTO>> queryByIds(Long...ids);
  
  /**
   * <p>Discription:[类目费用的添加]</p>
   * Created on 2015年3月9日
   * @param dto
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<String> insertCategoryExpense(SettleCatExpenseDTO dto);
  
  /**
   * <p>Discription:[根据id删除]</p>
   * Created on 2015年3月9日
   * @param ids
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<String> deleteById(long id);
  
  /**
   * <p>Discription:[修改类目费用]</p>
   * Created on 2015年3月9日
   * @param dto
   * @return
   * @author:[杨芳]
   */
  public ExecuteResult<String> modifyCategoryExpense(SettleCatExpenseDTO dto);
}
