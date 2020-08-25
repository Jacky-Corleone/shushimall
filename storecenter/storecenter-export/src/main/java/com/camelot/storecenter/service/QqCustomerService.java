package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.QqCustomerDTO;

/**
 * 
 * <p>Description: [QQ客服接口]</p>
 * Created on 2016年2月2日
 * @author  <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface QqCustomerService {
	
	/**
	 * 
	 * <p>Discription:[查询客服列表]</p>
	 * Created on 2016年2月2日
	 * @param pager 分页
	 * @param qqCustomerDTO 过滤条件
	 * @return 
	 * @author:[李伟龙]
	 */
	public ExecuteResult<DataGrid<QqCustomerDTO>> selectListByConditionAll(Pager<QqCustomerDTO> pager, QqCustomerDTO qqCustomerDTO);
	
	/**
	 * 
	 * <p>Discription:[更新客服信息，包含逻辑删除方法]</p>
	 * Created on 2016年2月2日
	 * @param qqCustomerDTO 更新的内容
	 * @return 影响的条数
	 * @author:[李伟龙]
	 */
	public int updateQqCustomer(QqCustomerDTO qqCustomerDTO);
	
	/**
	 * 
	 * <p>Discription:[新增客服]</p>
	 * Created on 2016年2月2日
	 * @param qqCustomerDTO 新增的内容
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> addQqCustomer(QqCustomerDTO qqCustomerDTO);
	
	/**
	 * 
	 * <p>Discription:[根据idlist与客服类型获取QQ号码]</p>
	 * Created on 2016年2月3日
	 * @param idlst
	 * @param customerType '客服类型：0平台客服，1店铺客服'
	 * @return	QQ号码
	 * @author:[李伟龙]
	 */
	public String getQqCustomerByIds(List<Long> idlst,Integer customerType);
	
	/**
	 * 
	 * <p>Discription:[根据条件修改所有客服为非默认客服]</p>
	 * Created on 2016年2月17日
	 * @param qqCustomerDTO '客服'
	 * @author:[王宏伟]
	 */
	public void updateMRCustomer(QqCustomerDTO qqCustomerDTO);
}
