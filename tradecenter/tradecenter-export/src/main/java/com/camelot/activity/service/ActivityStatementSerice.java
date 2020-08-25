package com.camelot.activity.service;

import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [活动结算单，平台活动平台需要给卖家结算]</p>
 * Created on 2015-12-10
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public  interface ActivityStatementSerice {

	/**
	 * 
	 * <p>Discription:[增加优惠活动结算单]</p>
	 * Created on 2015-12-10
	 * @param dto
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<String> addActivityStatement(ActivityStatementsDTO dto);
	
	/**
	 * 
	 * <p>Discription:[更新优惠活动结算单]</p>
	 * Created on 2015-12-10
	 * @param dto
	 * @return
	 * @author:[王鹏]
	 */
	public  ExecuteResult<String> updateActivityStatement(ActivityStatementsDTO dto);
	
	/**
	 * 
	 * <p>Discription:[根据订单编号查询优惠活动结算单]</p>
	 * Created on 2015-12-10
	 * @param orderId
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<ActivityStatementsDTO> queryActivityStatementsByOrderId(String orderId);
	
	/**
	 * 
	 * <p>Discription:[查询优惠活动结算单集合]</p>
	 * Created on 2015-12-10
	 * @param dto
	 * @param page
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<DataGrid<ActivityStatementsDTO>> queryActivityStatementsDTO(ActivityStatementsDTO dto,Pager page);
	
	/**
	 * 	
	 * <p>Discription:[增加优惠活动记录信息]</p>
	 * Created on 2015-12-10
	 * @param dto
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<String> addActivityRecord(ActivityRecordDTO dto);
	
	/**
	 * 
	 * <p>Discription:[查询优惠活动记录信息]</p>
	 * Created on 2015-12-10
	 * @param dto
	 * @param page
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<DataGrid<ActivityRecordDTO>> queryActivityRecordDTO(ActivityRecordDTO dto,Pager page);
}
