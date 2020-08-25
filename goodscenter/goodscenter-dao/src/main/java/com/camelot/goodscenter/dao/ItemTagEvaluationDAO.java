package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagCountDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;

/**
 * <p>DAO for goodscenter.item_evaluation_tag</p>
 * Created on 2016年2月24日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public interface ItemTagEvaluationDAO {
	
	/**
	 * <p>插入商品的标签评价</p>
	 * Created on 2016年2月24日
	 * @param evalId
	 * @param tagIds
	 * @return
	 * @author: 顾雨
	 */
	int addTagsOfOneEvaluation(@Param("evalId")Long evalId, @Param("tagIds")List<String> tagIds,
			@Param("skuId")Long skuId);
	
	/**
	 * <p>检索一条评价的所有标签</p>
	 * Created on 2016年2月24日
	 * @param evalId
	 * @return
	 * @author: 顾雨
	 */
	List<EvalTag> queryEvalTagsOfOneEvaluation(@Param("evalId")Long evalId);
	
	/**
	 * <p>检索sku标签评价和其计数</p>
	 * Created on 2016年2月24日
	 * @param entity
	 * @return
	 * @author: 顾雨
	 */
	List<EvalTagCountDTO> queryTagEvalsCountOfSku(@Param("entity")ItemEvaluationQueryInDTO entity);
}
