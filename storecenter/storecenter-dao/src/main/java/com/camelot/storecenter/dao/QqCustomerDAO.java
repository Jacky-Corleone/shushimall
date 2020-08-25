package com.camelot.storecenter.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.QqCustomerDTO;

/**
 * 
 * <p>Description: [QQ客服DAO]</p>
 * Created on 2016年2月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface QqCustomerDAO extends BaseDAO<QqCustomerDTO>{
	
	/**
	 * 
	 * <p>Discription:[根据条件修改所有客服为非默认客服]</p>
	 * Created on 2016年2月17日
	 * @param qqCustomerDTO '客服'
	 * @author:[王宏伟]
	 */
	public void updateMRCustomer(QqCustomerDTO qqCustomerDTO);

}
