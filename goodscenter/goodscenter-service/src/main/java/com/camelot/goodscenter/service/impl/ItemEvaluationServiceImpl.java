package com.camelot.goodscenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.camelot.goodscenter.dao.ItemEvaluationDAO;
import com.camelot.goodscenter.dao.ItemEvaluationShowDAO;
import com.camelot.goodscenter.dao.ItemTagEvaluationDAO;
import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagCountDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationReplyDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDetailDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalQueryDTO;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Description: [商品评价服务实现类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("itemEvaluationService")
public class ItemEvaluationServiceImpl implements ItemEvaluationService{
	private static final Logger LOGGER=LoggerFactory
			.getLogger(ItemEvaluationServiceImpl.class);
	@Resource
	private ItemEvaluationDAO itemEvaluationDAO;
	
	@Resource
	private ItemTagEvaluationDAO itemTagEvaluationDAO;
	
	@Resource
	private ItemEvaluationShowDAO itemEvaluationShowDAO;
	/**
	 * 添加买家对商品的评价/卖家对买家的评价
	 * @param itemEvaluationDTO
	 * @return
	 */
	@Override
	public ExecuteResult<ItemEvaluationDTO> addItemEvaluation(
			ItemEvaluationDTO itemEvaluationDTO) {
		LOGGER.info("=============开始保存评价====================");
		ExecuteResult<ItemEvaluationDTO> result = new ExecuteResult<ItemEvaluationDTO>();
		try {
			if(itemEvaluationDTO == null){
				result.addErrorMessage("执行方法【addItemEvaluation】出错,错误:参数不能为空");
				return result;
			}
			itemEvaluationDAO.insertItemEvaluation(itemEvaluationDTO);
			result.setResult(itemEvaluationDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【addItemEvaluation】出错,错误:" + e);
			result.addErrorMessage("执行方法【addItemEvaluation】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束评价保存====================");
		}
		return result;
	}
	
	/**
	 * 添加卖家对评价的回复
	 * @param itemEvaluationDTO
	 * @return
	 */
	@Override
	public ExecuteResult<ItemEvaluationReplyDTO> addItemEvaluationReply(
			ItemEvaluationReplyDTO itemEvaluationReplyDTO) {
		LOGGER.info("=============开始保存评价回复====================");
		ExecuteResult<ItemEvaluationReplyDTO> result = new ExecuteResult<ItemEvaluationReplyDTO>();
		try {
			if(itemEvaluationReplyDTO == null){
				result.addErrorMessage("执行方法【addItemEvaluationReply】出错,错误:参数不能为空");
				return result;
			}
			itemEvaluationDAO.insertItemEvaluationReply(itemEvaluationReplyDTO);
			result.setResult(itemEvaluationReplyDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【addItemEvaluationReply】出错,错误:" + e);
			result.addErrorMessage("执行方法【addItemEvaluationReply】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束评价保存====================");
		}
		return result;
	}
	
	/**
	 * 查询商品评价
	 * @param itemEvaluationDTO
	 * @return
	 */
	@Override
	public DataGrid<ItemEvaluationQueryOutDTO> queryItemEvaluationList(
			ItemEvaluationQueryInDTO itemEvaluationQueryInDTO, Pager page) {
		LOGGER.info("=============开始查询商品评价====================");
		DataGrid<ItemEvaluationQueryOutDTO> dg = new DataGrid<ItemEvaluationQueryOutDTO>();
		try {
			if(itemEvaluationQueryInDTO != null && StringUtils.isNotEmpty(itemEvaluationQueryInDTO.getScopeLevel())){
				//好1|中2|差3
				if("2".equals(itemEvaluationQueryInDTO.getScopeLevel())){
					itemEvaluationQueryInDTO.setBeginScope(3L);
					itemEvaluationQueryInDTO.setEndScope(4L);
				}else if("1".equals(itemEvaluationQueryInDTO.getScopeLevel())){
					itemEvaluationQueryInDTO.setBeginScope(5L);
				}else if("3".equals(itemEvaluationQueryInDTO.getScopeLevel())){
					itemEvaluationQueryInDTO.setBeginScope(1L);
					itemEvaluationQueryInDTO.setEndScope(2L);
				}
			}
			LOGGER.info("=============开始查询商品评价数据列表====================");
			List<ItemEvaluationQueryOutDTO> rows = this.itemEvaluationDAO.queryItemEvaluationList(itemEvaluationQueryInDTO, page);
			LOGGER.info("=============完成查询商品评价数据列表====================");
			LOGGER.info("=============开始查询商品评价总条数====================");
			Long total = this.itemEvaluationDAO.queryCount(itemEvaluationQueryInDTO);
			LOGGER.info("=============完成查询商品评价总条数====================");
			dg.setRows(rows);
			dg.setTotal(total);
		} catch (Exception e) {
			LOGGER.error("调用方法【queryItemEvaluationList】出错,错误:" + e);
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束查询商品评价====================");
		}
		return dg;
	}
	
	/**
	 * 对商品评价/对买家评价统计
	 */
	@Override
	public ExecuteResult<ItemEvaluationTotalDTO> queryItemEvaluationTotal(
			ItemEvaluationQueryInDTO itemEvaluationQueryInDTO) {
		ExecuteResult<ItemEvaluationTotalDTO> result = new ExecuteResult<ItemEvaluationTotalDTO>();
		LOGGER.info("=============开始评价统计====================");
		try {
			ItemEvaluationTotalDTO itemEvaluationTotalDTO = null;
			LOGGER.info("=============开始查询评价统计列表,按照级别分组====================");
			List<ItemEvaluationTotalQueryDTO> totalList = this.itemEvaluationDAO.queryItemEvaluationTotal(itemEvaluationQueryInDTO);
			LOGGER.info("=============完成查询评价统计列表====================");
			if(totalList != null && totalList.size() > 0){
				LOGGER.info("=============开始组装列表数据====================");
				Map<Integer,ItemEvaluationTotalDetailDTO> scopeAvgDetails = 
						new HashMap<Integer,ItemEvaluationTotalDetailDTO>();
				for(ItemEvaluationTotalQueryDTO it : totalList){
					if(it == null)
						continue;
					//获取总体评分
					BigDecimal allCountBg = new BigDecimal(it.getAllCount());
					BigDecimal allSumBg = new BigDecimal(it.getAllSum());
					if(itemEvaluationTotalDTO == null){
						itemEvaluationTotalDTO = new ItemEvaluationTotalDTO();
						//获取总评价人数
						itemEvaluationTotalDTO.setEvaluationNum(it.getAllCount());
						if(allCountBg.intValue() == 0){
							itemEvaluationTotalDTO.setScopeAvg(0.0);
						}else{
							itemEvaluationTotalDTO.setScopeAvg(allSumBg.divide(allCountBg,1,BigDecimal.ROUND_HALF_UP).doubleValue());
						}
					}
					//组装评价
					ItemEvaluationTotalDetailDTO itemEvaluationTotalDetail = new ItemEvaluationTotalDetailDTO();
					itemEvaluationTotalDetail.setCount(it.getScopeCount().intValue());
					if(allCountBg.intValue() == 0){
						itemEvaluationTotalDetail.setPercent(0.0);
					}else{
						BigDecimal scopeCount = new BigDecimal(it.getScopeCount());
						itemEvaluationTotalDetail.setPercent(scopeCount.divide(allCountBg,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue());
					}
					scopeAvgDetails.put(it.getScope(), itemEvaluationTotalDetail);
				}
				if(itemEvaluationTotalDTO != null)
					itemEvaluationTotalDTO.setScopeAvgDetails(scopeAvgDetails);
				LOGGER.info("=============完成组装列表数据====================");
			}
			result.setResult(itemEvaluationTotalDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【queryItemEvaluationTotal】出错,错误:" + e);
			result.addErrorMessage("执行方法【queryItemEvaluationTotal】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束查询商品评价====================");
		}
		LOGGER.info("=============结束评价统计====================");
		return result;
	}
	
	/**
	 * 批量添加
	 * @param itemEvaluationDTOList
	 * @return
	 */
	public ExecuteResult<ItemEvaluationDTO> addItemEvaluations(List<ItemEvaluationDTO> itemEvaluationDTOList){
		ExecuteResult<ItemEvaluationDTO> result = new ExecuteResult<ItemEvaluationDTO>();
		try {
			if(itemEvaluationDTOList == null || itemEvaluationDTOList.size() == 0){
				result.addErrorMessage("参数不能为空");
				return result;
			}
			for(ItemEvaluationDTO itemEvaluationDTO : itemEvaluationDTOList){
				if(itemEvaluationDTO != null){
					this.itemEvaluationDAO.insertItemEvaluation(itemEvaluationDTO);
				}
			}
		} catch (Exception e) {
			LOGGER.error("调用方法【addItemEvaluations】出错,错误:" + e);
			result.addErrorMessage("执行方法【addItemEvaluations】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	
	/**
	 * 批量添加并且返回添加的实体
	 * @param itemEvaluationDTOList
	 * @return
	 */
	public ExecuteResult<List<ItemEvaluationDTO>> addItemEvaluationsReturnList(List<ItemEvaluationDTO> itemEvaluationDTOList){
		ExecuteResult<List<ItemEvaluationDTO>> result = new ExecuteResult<List<ItemEvaluationDTO>>();
		List<ItemEvaluationDTO> listResult = new ArrayList<ItemEvaluationDTO>();
		try {
			if(itemEvaluationDTOList == null || itemEvaluationDTOList.size() == 0){
				result.addErrorMessage("参数不能为空");
				return result;
			}
			for(ItemEvaluationDTO itemEvaluationDTO : itemEvaluationDTOList){
				if(itemEvaluationDTO != null){
					this.itemEvaluationDAO.insertItemEvaluation(itemEvaluationDTO);
					listResult.add(itemEvaluationDTO);
				}
			}
		} catch (Exception e) {
			LOGGER.error("调用方法【addItemEvaluations】出错,错误:" + e);
			result.addErrorMessage("执行方法【addItemEvaluations】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		result.setResult(listResult);
		return result;
	}

	@Override
	public ItemEvaluationDTO queryItemEvaluationById(
			ItemEvaluationDTO itemEvaluationDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataGrid<ItemEvaluationReplyDTO> queryItemEvaluationReplyList(
			ItemEvaluationReplyDTO itemEvaluationReplyDTO, Pager page) {
		LOGGER.info("=============开始查询卖家回复====================");
		DataGrid<ItemEvaluationReplyDTO> dg = new DataGrid<ItemEvaluationReplyDTO>();
		try {
			LOGGER.info("=============开始查询卖家回复列表====================");
			List<ItemEvaluationReplyDTO> rows = this.itemEvaluationDAO.queryItemEvaluationReplyList(itemEvaluationReplyDTO, page);
			LOGGER.info("=============完成查询卖家回复列表====================");
			LOGGER.info("=============开始查询卖家回复总条数====================");
			Long total = this.itemEvaluationDAO.queryTotal(itemEvaluationReplyDTO);
			LOGGER.info("=============完成查询卖家回复总条数====================");
			dg.setRows(rows);
			dg.setTotal(total);
		} catch (Exception e) {
			LOGGER.error("调用方法【queryItemEvaluationReplyList】出错,错误:" + e);
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束查询商品评价====================");
		}
		return dg;
	}
	
	
	
	public static void main(String[] args) {
		BigDecimal allCountBg = new BigDecimal(7);
		BigDecimal allSumBg = new BigDecimal(8);
		System.out.println(allSumBg.divide(allCountBg,2,BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	/**
	 * <p>Discription:[获取晒单图片]</p>
	 * Created on 2015年12月11日
	 * @param itemEvaluationShowDTO
	 * @param page
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<DataGrid<ItemEvaluationShowDTO>> queryItemEvaluationShowList(
			ItemEvaluationShowDTO itemEvaluationShowDTO, Pager page) {
		ExecuteResult<DataGrid<ItemEvaluationShowDTO>> result=new ExecuteResult<DataGrid<ItemEvaluationShowDTO>>();
		try {
			DataGrid<ItemEvaluationShowDTO> dataGrid=new DataGrid<ItemEvaluationShowDTO>();
			List<ItemEvaluationShowDTO> list = itemEvaluationShowDAO.queryList(itemEvaluationShowDTO, page);
			Long count = itemEvaluationShowDAO.queryCount(itemEvaluationShowDTO);
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			LOGGER.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * <p>Discription:[新增晒单图片]</p>
	 * Created on 2015年12月11日
	 * @param itemEvaluationShowDTO
	 * @return
	 * @author:[李伟龙]
	 */
	@Override
	public ExecuteResult<String> addItemEvaluationShow(ItemEvaluationShowDTO itemEvaluationShowDTO) {
        ExecuteResult<String> result=new ExecuteResult<String>();
        try {
        	itemEvaluationShowDAO.add(itemEvaluationShowDTO);
        	itemEvaluationDAO.setHasShow(itemEvaluationShowDTO);
            result.setResult("");
            result.setResultMessage("success");
        } catch (Exception e) {
            result.setResultMessage("error");
            result.getErrorMessages().add(e.getMessage());
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

	@Override
	public ExecuteResult<String> addItemTagsEvaluation(Long evalId, List<String> tagIds, Long skuId) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		if (evalId == null || CollectionUtils.isEmpty(tagIds)) {
			er.addErrorMessage("参数错误");
			return er;
		}
		try {
			itemTagEvaluationDAO.addTagsOfOneEvaluation(evalId, tagIds, skuId);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			er.addErrorMessage("保存数据出错");
		}
		return er;
	}

	@Override
	public ExecuteResult<List<EvalTag>> queryEvalTagsOfOneEvaluation(Long evalId) {
		ExecuteResult<List<EvalTag>> er = new ExecuteResult<List<EvalTag>>();
		List<EvalTag> list = null;
		try {
			list = itemTagEvaluationDAO.queryEvalTagsOfOneEvaluation(evalId);
			if (CollectionUtils.isEmpty(list)) {
				er.addErrorMessage("检索失败");
			} else {
				er.setResult(list);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			er.addErrorMessage("检索失败");
		}
		return er;
	}

	@Override
	public List<EvalTagCountDTO> queryTagEvalsCountOfSku(ItemEvaluationQueryInDTO entity) {
		return itemTagEvaluationDAO.queryTagEvalsCountOfSku(entity);
	}

	@Override
	public ExecuteResult<Integer> queryEvalCount(ItemEvaluationQueryInDTO entity) {
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		try {
			int count = itemEvaluationDAO.queryEvalCount(entity);
			er.setResult(count);
		} catch (Exception e) {
			er.addErrorMessage("检索出错");
			LOGGER.error(e.getMessage());
		}
		return er;
	}

	@Override
	public ExecuteResult<Integer> queryEvalShowCount(ItemEvaluationQueryInDTO entity) {
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		try {
			int count = itemEvaluationDAO.queryEvalShowCount(entity);
			er.setResult(count);
		} catch (Exception e) {
			er.addErrorMessage("检索出错");
			LOGGER.error(e.getMessage());
		}
		return er;
	}

	@Override
	public ExecuteResult<Float> queryAvgSkuScope(ItemEvaluationQueryInDTO entity) {
		ExecuteResult<Float> er = new ExecuteResult<Float>();
		try {
			Float count = itemEvaluationDAO.queryAvgSkuScope(entity);
			if (count == null) {
				count = 0f;
			}
			er.setResult(count);
		} catch (Exception e) {
			er.addErrorMessage("检索出错");
			LOGGER.error(e.getMessage());
		}
		return er;
	}
	

}
