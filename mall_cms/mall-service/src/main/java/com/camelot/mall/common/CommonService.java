package com.camelot.mall.common;

import com.alibaba.fastjson.JSONArray;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月16日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface CommonService {

	/**
	 * 
	 * <p>Discription:获取商品类目、轮播广告及公告</p>
	 * Created on 2015年2月5日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public JSONArray findCategory();
	
	/**
	 * 
	 * <p>Discription:[查询二级类目子站的左侧类目信息]</p>
	 * Created on 2015年12月23日
	 * @param cid
	 * @return
	 * @author:[王鹏]
	 */
	public JSONArray findSonCategory(Long cid,Integer themId);
	
}
