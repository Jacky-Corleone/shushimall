package com.camelot.activity.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

/** 
 * <p>Description: [活动结算dao]</p>
 * Created on 2015-12-10
 * @author  <a href="mailto: wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ActivityStatementsDao extends BaseDAO<ActivityStatementsDTO>{
	
	/**
	 * 
	 * <p>Discription:[根据订单id查询优惠结算单]</p>
	 * Created on 2015-12-10
	 * @param orderId
	 * @return
	 * @author:[王鹏]
	 */
	ActivityStatementsDTO queryByOrderId(@Param("orderId")String orderId);
	

}
