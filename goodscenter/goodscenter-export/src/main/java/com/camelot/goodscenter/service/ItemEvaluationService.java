package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagCountDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationReplyDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDTO;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
/**
 * <p>Description: [商品评价服务类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemEvaluationService {
	
	/**
	 * 添加买家对商品的评价/卖家对买家的评价
	 * @param itemEvaluationDTO
	 * @return
	 */
	public ExecuteResult<ItemEvaluationDTO> addItemEvaluation(ItemEvaluationDTO itemEvaluationDTO);
	
	/**
	 * 批量添加
	 * @param itemEvaluationDTOList
	 * @return
	 */
	public ExecuteResult<ItemEvaluationDTO> addItemEvaluations(List<ItemEvaluationDTO> itemEvaluationDTOList);
	
	/**
	 * 添加针对评价的回复信息
	 * @param itemEvaluationReplyDTO
	 * @return
	 */
	public ExecuteResult<ItemEvaluationReplyDTO> addItemEvaluationReply(ItemEvaluationReplyDTO itemEvaluationReplyDTO);
	
	
	/**
	 * 查询买家对商品的评价记录
	 * @param itemEvaluationQueryInDTO
	 * @return
	 */
	public DataGrid<ItemEvaluationQueryOutDTO> queryItemEvaluationList(ItemEvaluationQueryInDTO itemEvaluationQueryInDTO,Pager page);
	
	
	/**
	 * 对商品评价统计/对买家评价统计
	 * @param itemEvaluationQueryInDTO
	 * @return
	 */
	public ExecuteResult<ItemEvaluationTotalDTO> queryItemEvaluationTotal(ItemEvaluationQueryInDTO itemEvaluationQueryInDTO);
	
	
	
	/**
	 * 查询评价详情
	 * @param itemEvaluationDTO
	 * @return
	 */
	public ItemEvaluationDTO queryItemEvaluationById(ItemEvaluationDTO itemEvaluationDTO);
	
	/**
	 * 查询评价的回复数据集合
	 * @param itemEvaluationReplyDTO
	 * @return
	 */
	public DataGrid<ItemEvaluationReplyDTO> queryItemEvaluationReplyList(ItemEvaluationReplyDTO itemEvaluationReplyDTO,Pager page);
	
	/**
	 * <p>Discription:[获取晒单图片列表]</p>
	 * Created on 2015年12月11日
	 * @param itemEvaluationShowDTO
	 * @param page
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<DataGrid<ItemEvaluationShowDTO>> queryItemEvaluationShowList(ItemEvaluationShowDTO itemEvaluationShowDTO, Pager page);
	
	/**
	 * <p>Discription:[add晒单图片]</p>
	 * Created on 2015年12月11日
	 * @param itemEvaluationShowDTO
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<String> addItemEvaluationShow(ItemEvaluationShowDTO itemEvaluationShowDTO);
	
	/**
	 * <p>Discription:[批量添加，并且返回实体]</p>
	 * Created on 2015年12月14日
	 * @param itemEvaluationDTOList
	 * @return
	 * @author:[李伟龙]
	 */
	public ExecuteResult<List<ItemEvaluationDTO>> addItemEvaluationsReturnList(List<ItemEvaluationDTO> itemEvaluationDTOList);

	/**
	 * <p>插入商品的标签评价</p>
	 * Created on 2016年2月24日
	 * @param evalId 评价记录id
	 * @param tagIds 多个标签id
	 * @param skuId skuId
	 * @author: 顾雨
	 */
	public ExecuteResult<String> addItemTagsEvaluation(Long evalId, List<String> tagIds, Long skuId);
	
	/**
	 * <p>检索一条评价的所有标签</p>
	 * Created on 2016年2月24日
	 * @param evalId
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<List<EvalTag>> queryEvalTagsOfOneEvaluation(Long evalId);
	
	/**
	 * 
	 * <p>检索sku标签评价和其计数</p>
	 * Created on 2016年2月24日
	 * @param entity
	 * @return
	 * @author: 顾雨
	 */
	List<EvalTagCountDTO> queryTagEvalsCountOfSku(ItemEvaluationQueryInDTO entity);
	
	/**
	 * <p>查询item或sku的评价总数</p>
	 * Created on 2016年2月29日
	 * @param entity
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<Integer> queryEvalCount(ItemEvaluationQueryInDTO entity);
	
	/**
	 * <p>查询item或sku的评价总数（有图）</p>
	 * Created on 2016年2月29日
	 * @param entity
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<Integer> queryEvalShowCount(ItemEvaluationQueryInDTO entity);
	
	/**
	 * <p>查询item或sku的平均评价星级</p>
	 * Created on 2016年2月29日
	 * @param entity
	 * @return
	 * @author: 顾雨
	 */
	ExecuteResult<Float> queryAvgSkuScope(ItemEvaluationQueryInDTO entity);
}
