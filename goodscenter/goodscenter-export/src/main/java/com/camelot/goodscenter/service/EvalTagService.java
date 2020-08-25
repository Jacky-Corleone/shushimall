package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagsOfCatDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Service for 评价标签</p>
 * Created on 2016年2月22日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface EvalTagService {
	/**
	 * <p>插入一个或多个评价标签</p>
	 * Created on 2016年2月22日
	 * @param tags 插入数据
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<String> add(EvalTagsOfCatDTO tags);
	
	/**
	 * <p>分页检索</p>
	 * Created on 2016年2月22日
	 * @param param 检索条件
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<DataGrid<EvalTagsOfCatDTO>> queryList(EvalTagsOfCatDTO param, Pager pager);
	
	/**
	 * <p>检索一组同类目的标签</p>
	 * Created on 2016年2月23日
	 * @param param
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<EvalTagsOfCatDTO> queryOneGroupTags(EvalTagsOfCatDTO param);
	
	/**
	 * <p>更新某个标签的名字</p>
	 * Created on 2016年2月23日
	 * @param tag
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<String> updateNameOfOneTag(EvalTag tag);

	/**
	 * <p>删除一堆评价标签</p>
	 * Created on 2016年2月23日
	 * @param cidL2
	 * @param tagIds
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<String> deleteTags(Integer cidL2, List<String> tagIds);
	
	/**
	 * <p>检索itemId的评价标签</p>
	 * Created on 2016年2月24日
	 * @param itemId
	 * @return
	 * @author: 顾雨
	 */
	List<EvalTag> queryByItemId(Long itemId);
}
