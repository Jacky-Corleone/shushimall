package com.camelot.basecenter.ip;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import com.camelot.basecenter.dao.IptableDAO;
import com.camelot.basecenter.domain.Iptable;
import com.camelot.basecenter.dto.IptableDTO;
import com.camelot.basecenter.service.IptableService;

/** 
 * Description: [描述该类概要功能介绍]
 * Created on 2016年02月24日
 * @author  <a href="mailto: XXXXXX@camelotchina.com">中文名字</a>
 * @version 1.0 
 * Copyright (c) 2016年 北京柯莱特科技有限公司 交付部 
 */
@Service("iptableService")
public class IptableServiceImpl implements IptableService {
	private static final Logger LOGGER=LoggerFactory
			.getLogger(IptableServiceImpl.class);
	
	@Resource
	private IptableDAO iptableDAO;
	
	/**
	 * <p>Discription:[分页查询XXXX集合数据]</p>
	 * Created on 2016年02月24日
	 * @param pager 分页数据
	 * @param iptableDTO 条件
	 * @return XXXX集合数据
	 * @author:中文名字
	 */
	public List<IptableDTO> query(IptableDTO iptableDTO){
		List<IptableDTO> iptableDTOList = this.iptableDAO.queryList(iptableDTO);
		return iptableDTOList;
	}
}