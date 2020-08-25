package com.camelot.basecenter.service;

import java.util.List;
import java.util.Map;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.basecenter.dto.IptableDTO;
import com.camelot.openplatform.common.DataGrid;

/** 
 * Description: [描述该类概要功能介绍]
 * Created on 2016年02月24日
 * @author  <a href="mailto: XXXXXX@camelotchina.com">中文名字</a>
 * @version 1.0 
 * Copyright (c) 2016年 北京柯莱特科技有限公司 交付部 
 */
public interface IptableService {
	
	/**
	 * <p>Discription:[查询IP段集合数据]</p>
	 * Created on 2016年02月24日
	 * @param iptableDTO 条件
	 * @return IP段集合数据
	 * @author:中文名字
	 */
	public List<IptableDTO> query(IptableDTO iptableDTO);
	
}
