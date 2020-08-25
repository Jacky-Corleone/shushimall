package com.camelot;

import javax.annotation.Resource;

import org.springframework.test.context.ContextConfiguration;

import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;

/**
 * 
 * <p>Description: [描述该类概要功能介绍 :单元测试的配置文件  不需要zookeeper]</p>
 * Created on 2015年2月5日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@ContextConfiguration(locations = {"classpath:/test.xml"})
public class Base {
	@Resource
	protected  ItemExportService itemExportService;
	
	@Resource
	protected  ItemEvaluationService itemEvaluationService;
	
}
