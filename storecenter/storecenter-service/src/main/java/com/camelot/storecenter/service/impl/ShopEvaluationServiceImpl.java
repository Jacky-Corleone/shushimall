package com.camelot.storecenter.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopEvaluationDAO;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalQueryDTO;
import com.camelot.storecenter.dto.emums.ShopEvaluation.ShopEvaluationTypeColumn;
import com.camelot.storecenter.service.ShopEvaluationService;

/**
 * <p>Description: [店铺评价服务实现类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopEvaluationService")
public class ShopEvaluationServiceImpl implements ShopEvaluationService{
	private static final Logger LOGGER=LoggerFactory
			.getLogger(ShopEvaluationServiceImpl.class);
	
	@Resource
	private ShopEvaluationDAO shopEvaluationDAO;
	
	/**
	 * 添加店铺评价
	 * @param shopEvaluationDTO
	 * @return
	 */
	@Override
	public ExecuteResult<ShopEvaluationDTO> addShopEvaluation(
			ShopEvaluationDTO shopEvaluationDTO) {
		LOGGER.info("=============开始保存店铺评价====================");
		ExecuteResult<ShopEvaluationDTO> result = new ExecuteResult<ShopEvaluationDTO>();
		try {
			if(shopEvaluationDTO == null){
				result.addErrorMessage("执行方法【addShopEvaluation】出错,错误:参数不能为空");
				return result;
			}
			shopEvaluationDAO.insertShopEvaluation(shopEvaluationDTO);
			result.setResult(shopEvaluationDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【addShopEvaluation】出错,错误:" + e);
			result.addErrorMessage("执行方法【addShopEvaluation】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束店铺评价保存====================");
		}
		return result;
	}
	
	
	/**
	 * 查询店铺评价统计
	 */
	@Override
	public ExecuteResult<ShopEvaluationTotalDTO> queryShopEvaluationTotal(
			ShopEvaluationQueryInDTO shopEvaluationQueryInDTO) {
		ExecuteResult<ShopEvaluationTotalDTO> result = new ExecuteResult<ShopEvaluationTotalDTO>();
		LOGGER.info("=============开始统计店铺评价====================");
		try {
			ShopEvaluationTotalDTO shopEvaluationTotalDTO = new ShopEvaluationTotalDTO();
			//查询出的统计结果为List<Map> 首先将List<Map>转换为Map供下面应用
			List<Map<String,List<ShopEvaluationTotalQueryDTO>>> shopTotalMapList = shopEvaluationDAO.queryShopEvaluationTotal(shopEvaluationQueryInDTO);
			Map<String,List<ShopEvaluationTotalQueryDTO>> shopTotalMap = convertListToMap(shopTotalMapList);
			//获取总数量、总评分
			List<ShopEvaluationTotalQueryDTO> shopEvaluationTotalList = shopTotalMap.get(ShopEvaluationTypeColumn.all.getLabel());
			if(shopEvaluationTotalList == null || shopEvaluationTotalList.size() == 0){
				LOGGER.info("=============店铺评价无数据====================");
				result.setResult(shopEvaluationTotalDTO);
				return result;
			}
			ShopEvaluationTotalQueryDTO shopEvaluationTotalQueryAll = shopEvaluationTotalList.get(0);
			BigDecimal allCount = new BigDecimal(shopEvaluationTotalQueryAll.getScopeCount());
			BigDecimal allSum = new BigDecimal("0");
			if(shopEvaluationTotalQueryAll.getScopeSum() != null){
				allSum = new BigDecimal(shopEvaluationTotalQueryAll.getScopeSum());
			}
			//如果评价总数量为0 
			if(allCount.intValue() == 0 || allSum.intValue() == 0){
				LOGGER.info("=============获取的店铺评价总数量为0====================");
				shopEvaluationTotalDTO.setShopReputation(0.0);
				shopEvaluationTotalDTO.setShopArrival(0.0);
				shopEvaluationTotalDTO.setShopDescription(0.0);
				shopEvaluationTotalDTO.setShopService(0.0);
				shopEvaluationTotalDTO.setAllCount(new BigInteger("0"));
				shopEvaluationTotalDTO.setAllSum(new BigInteger("0"));
				result.setResult(shopEvaluationTotalDTO);
				return result;
			}
			LOGGER.info("=============开始计算店铺信誉====================");
			//计算店铺信誉
			allSum = allSum.divide(new BigDecimal("3"),2,BigDecimal.ROUND_HALF_UP);
			shopEvaluationTotalDTO.setShopReputation(allSum.divide(allCount,1,BigDecimal.ROUND_HALF_UP).doubleValue());
			LOGGER.info("=============结束计算店铺信誉====================");
			LOGGER.info("=============开始计算店铺描述相符度====================");
			//计算描述相符
			BigDecimal allDescriptionSum = null;
			List<ShopEvaluationTotalQueryDTO> shopDescriptionList = shopTotalMap.get(ShopEvaluationTypeColumn.shop_description.getLabel());
			if(shopDescriptionList == null || shopDescriptionList.size() == 0){
				shopEvaluationTotalDTO.setShopDescription(0.0);
			}else{
				Map<Integer,Double> shopDescriptionDetails = new HashMap<Integer,Double>();
				for(ShopEvaluationTotalQueryDTO totalQuery : shopDescriptionList){
					allDescriptionSum = new BigDecimal(totalQuery.getScopeSum());
					//计算评分比例
					BigDecimal descriptionCount = new BigDecimal(totalQuery.getScopeCount());
					Double descriptionPercent = descriptionCount.divide(allCount,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue();
					shopDescriptionDetails.put(totalQuery.getScopeVal(),descriptionPercent);
				}
				//相符描述度评分 = 描述度总评分/总评价数量
				if(allDescriptionSum != null){
					shopEvaluationTotalDTO.setShopDescription(allDescriptionSum.divide(allCount,1,BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				shopEvaluationTotalDTO.setShopDescriptionDetails(shopDescriptionDetails);
			}
			LOGGER.info("=============结束计算店铺描述相符度====================");
			
			LOGGER.info("=============开始计算服务态度====================");
			BigDecimal allServiceSum = null;
			List<ShopEvaluationTotalQueryDTO> shopServiceList = shopTotalMap.get(ShopEvaluationTypeColumn.shop_service.getLabel());
			if(shopServiceList == null || shopServiceList.size() == 0){
				shopEvaluationTotalDTO.setShopService(0.0);
			}else{
				Map<Integer,Double> shopServiceDetails = new HashMap<Integer,Double>();
				for(ShopEvaluationTotalQueryDTO serviceTotalQuery : shopServiceList){
					allServiceSum = new BigDecimal(serviceTotalQuery.getScopeSum());
					//计算评分比例
					BigDecimal serviceCount = new BigDecimal(serviceTotalQuery.getScopeCount());
					Double servicePercent = serviceCount.divide(allCount,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue();
					shopServiceDetails.put(serviceTotalQuery.getScopeVal(),servicePercent);
				}
				if(allServiceSum != null){
					shopEvaluationTotalDTO.setShopService(allServiceSum.divide(allCount,1,BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				shopEvaluationTotalDTO.setShopServiceDetails(shopServiceDetails);
			}
			LOGGER.info("=============结束计算服务态度====================");
			
			LOGGER.info("=============开始计算发货速度====================");
			BigDecimal allArrivalSum = null;
			List<ShopEvaluationTotalQueryDTO> shopArrivalList = shopTotalMap.get(ShopEvaluationTypeColumn.shop_arrival.getLabel());
			if(shopArrivalList == null || shopArrivalList.size() == 0){
				shopEvaluationTotalDTO.setShopArrival(0.0);
			}else{
				Map<Integer,Double> shopArrivalDetails = new HashMap<Integer,Double>();
				for(ShopEvaluationTotalQueryDTO arrivalTotalQuery : shopArrivalList){
					allArrivalSum = new BigDecimal(arrivalTotalQuery.getScopeSum());
					//计算评分比例
					BigDecimal arrivalCount = new BigDecimal(arrivalTotalQuery.getScopeCount());
					Double arrivalPercent = arrivalCount.divide(allCount,4,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).doubleValue();
					shopArrivalDetails.put(arrivalTotalQuery.getScopeVal(),arrivalPercent);
				}
				if(allArrivalSum != null){
					shopEvaluationTotalDTO.setShopArrival(allArrivalSum.divide(allCount,1,BigDecimal.ROUND_HALF_UP).doubleValue());
				}
				shopEvaluationTotalDTO.setShopArrivalDetails(shopArrivalDetails);
			}
			result.setResult(shopEvaluationTotalDTO);
			LOGGER.info("=============结束计算发货速度====================");
		} catch (Exception e) {
			LOGGER.error("调用方法【queryShopEvaluationTotal】出错,错误:" + e);
			result.addErrorMessage("执行方法【queryShopEvaluationTotal】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束统计店铺评价====================");
		}
		return result;
	}
	
	/**
	 * 将List<Map> 转换为Map
	 * @param shopTotalMapList
	 * @return
	 */
	private Map<String,List<ShopEvaluationTotalQueryDTO>> convertListToMap(List<Map<String, List<ShopEvaluationTotalQueryDTO>>> shopTotalMapList){
		Map<String,List<ShopEvaluationTotalQueryDTO>> result = new HashMap<String,List<ShopEvaluationTotalQueryDTO>>();
		if(shopTotalMapList == null)
			return result;
		for(Map<String,List<ShopEvaluationTotalQueryDTO>> map_ : shopTotalMapList){
			Object key = map_.get("key");
			result.put(key.toString(),(List<ShopEvaluationTotalQueryDTO>)map_.get("value"));
		}
		return result;
	}
	
	/**
	 * 查询店铺评价
	 */
	@Override
	public DataGrid<ShopEvaluationDTO> queryShopEvaluationDTOList(
			ShopEvaluationQueryInDTO shopEvaluationQueryInDTO, Pager page) {
		DataGrid<ShopEvaluationDTO> dg = new DataGrid<ShopEvaluationDTO>();
		LOGGER.info("=============开始查询店铺评价====================");
		try {
			LOGGER.info("=============开始查询店铺评价数据列表====================");
			List<ShopEvaluationDTO> rows = this.shopEvaluationDAO.queryShopEvaluationList(shopEvaluationQueryInDTO, page);
			LOGGER.info("=============结束查询店铺评价数据列表====================");
			LOGGER.info("=============开始查询店铺评价总数量====================");
			Long total = this.shopEvaluationDAO.queryCount(shopEvaluationQueryInDTO);
			LOGGER.info("=============结束查询店铺评价总数量====================");
			dg.setRows(rows);
			dg.setTotal(total);
		} catch (Exception e) {
			LOGGER.error("调用方法【queryShopEvaluationDTOList】出错,错误:" + e);
			throw new RuntimeException(e);
		}finally{
			LOGGER.info("=============结束查询店铺评价====================");
		}
		return dg;
	}
	

}
