package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagsOfCatDTO;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>DAO——>goodscenter.item_category_tag</p>
 * Created on 2016年2月22日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface EvalTagDao {
	/**
	 * <p>插入一个或多个评价标签</p>
	 * Created on 2016年2月22日
	 * @param tags 插入数据
	 * @return
	 * @author: 顾雨
	 */
	int add(@Param("param")EvalTagsOfCatDTO tags);
	
	/**
	 * <p>检索2级类目分页</p>
	 * Created on 2016年2月23日
	 * @param pager
	 * @param param
	 * @return
	 * @author: 顾雨
	 */
	List<Integer> queryCatsOfPage(@Param("param")EvalTagsOfCatDTO param, @Param("pager")@SuppressWarnings("rawtypes") Pager pager);
	
	/**
	 * <p>检索列表</p>
	 * Created on 2016年2月22日
	 * @param param 检索条件
	 * @return
	 * @author: 顾雨
	 */
	List<EvalTagsOfCatDTO> queryList(@Param("param")EvalTagsOfCatDTO param, @Param("catsL2")List<Integer> cats);
	
	/**
	 * <p>检索列表计数</p>
	 * Created on 2016年2月22日
	 * @param param
	 * @return
	 * @author: 顾雨
	 */
	long countUnderCondition(@Param("param")EvalTagsOfCatDTO param);
	
	/**
	 * <p>更新某个标签的名字</p>
	 * Created on 2016年2月23日
	 * @param tag
	 * @return
	 * @author: 顾雨
	 */
	int updateNameOfOneTag(EvalTag tag);

	/**
	 * <p>删除一堆评价标签</p>
	 * Created on 2016年2月23日
	 * @param cidL2
	 * @param tagIds
	 * @return
	 * @author: 顾雨
	 */
	int deleteTags(@Param("cidL2")Integer cidL2, @Param("tagIds")List<String> tagIds);
	
	/**
	 * <p>检索itemId的评价标签</p>
	 * Created on 2016年2月24日
	 * @param itemId
	 * @return
	 * @author: 顾雨
	 */
	List<EvalTag> queryByItemId(Long itemId);
}
