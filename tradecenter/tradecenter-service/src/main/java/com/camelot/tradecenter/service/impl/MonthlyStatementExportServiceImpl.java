package com.camelot.tradecenter.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.tradecenter.dao.MonthlyStatementDAO;
import com.camelot.tradecenter.dao.TradeOrderItemsDAO;
import com.camelot.tradecenter.dao.TradeOrdersDAO;
import com.camelot.tradecenter.dto.MonthlyStatementDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.MonthlyStatementExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/** 
 * <p>Description: [协议功能接口实现]</p>
 * Created on 2015年06月13日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("monthlyStatementExportService")
public class MonthlyStatementExportServiceImpl implements MonthlyStatementExportService {
	private static final Logger logger = LoggerFactory.getLogger(TradeOrderExportServiceImpl.class);
	@Resource
	private MonthlyStatementDAO monthlyStatementDAO;
	@Resource
	private TradeOrdersDAO tradeOrdersDAO;
	@Resource
	private TradeOrderItemsDAO tradeOrderItemsDAO;
	/**
	 * <p>Discription:[根据条件查询详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:[鲁鹏]
	 */
	  public ExecuteResult<MonthlyStatementDTO> queryByMonthlyStatement(MonthlyStatementDTO dto){
		  ExecuteResult<MonthlyStatementDTO> er=new  ExecuteResult<MonthlyStatementDTO>();
		  MonthlyStatementDTO inquiryInfoDTO=monthlyStatementDAO.findByMonthlyStatementDTO(dto);
		  try{
			  if(inquiryInfoDTO != null){
				  er.setResult(inquiryInfoDTO);
				  er.setResultMessage("success");
			  }else{
				 er.setResultMessage("您要查询的数据不存在"); 
			  }
		  }catch(Exception e){
			  er.setResultMessage(e.getMessage());
			  throw new RuntimeException(e);
		  }
		  return er;
	  }
	/**
	 * <p>Discription:[根据条件查询协议的列表接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<DataGrid<MonthlyStatementDTO>> queryMonthlyStatementList(MonthlyStatementDTO dto, Pager page){
		ExecuteResult<DataGrid<MonthlyStatementDTO>> er=new ExecuteResult<DataGrid<MonthlyStatementDTO>>();
		DataGrid<MonthlyStatementDTO> dg=new DataGrid<MonthlyStatementDTO>();
		List<MonthlyStatementDTO> list=monthlyStatementDAO.queryPage(page, dto);
		Long count=monthlyStatementDAO.queryPageCount(dto);
		try{
			if(list != null ){
				dg.setRows(list);
				dg.setTotal(count);
				er.setResult(dg);
			}else{
				er.setResultMessage("要查询的数据不存在");
			}

			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[生成协议详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> addMonthlyStatement(MonthlyStatementDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		try{
			if(dto.getOrderCode()==null || dto.getOrderCode().equals(0L)){
				er.setResultMessage("订单号不能为空");
				return er;
			}
			monthlyStatementDAO.insert(dto);
			er.setResultMessage("success");
		}catch(Exception e){
			er.setResultMessage("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[修改协议详情接口实现]</p>
	 * Created on 2015年06月13日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
	public ExecuteResult<String> modifyMonthlyStatement(MonthlyStatementDTO dto){
		ExecuteResult<String> er=new ExecuteResult<String>();
		MonthlyStatementDTO inquiryInfo=monthlyStatementDAO.findById(dto.getId());
		try{
			if(inquiryInfo != null){
				if(dto.getActiveFlag()!=null){
					inquiryInfo.setActiveFlag(dto.getActiveFlag());
				}
				if(monthlyStatementDAO.update(inquiryInfo) >0){
					er.setResult("修改成功");
				}
				else{
					er.setResult("修改失败");
				}
			}
		}catch(Exception e){
			er.setResult("error");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[根据用户查询所有的对账单号]</p>
	 * Created on 2015年06月13日
	 * @return
	 * @author:[马国平]
	 */
	public ExecuteResult<DataGrid<JSONObject>> queryPageGroupByUid(MonthlyStatementDTO queryParam,Pager pager){

		ExecuteResult<DataGrid<JSONObject>> er=new ExecuteResult<DataGrid<JSONObject>>();
		DataGrid<JSONObject> dataGrid = new DataGrid<JSONObject>();
		try{
			String uid=queryParam.getCreateBy();
			List<MonthlyStatementDTO> lm=monthlyStatementDAO.queryPageGroupByUid(pager,queryParam);
			if(lm != null){
				Long count=monthlyStatementDAO.queryPageGroupByUidCount(queryParam);
				List<JSONObject> joL=new ArrayList<JSONObject>();
				for(MonthlyStatementDTO msDto : lm){

					JSONObject joStatement=new JSONObject();
					joStatement.put("statementInfo",msDto);
					JSONArray jaOrders=new JSONArray();
					joStatement.put("orders",jaOrders);
					List<String> orderIds=queryOrderByStatementId(msDto.getStatementId());
					//封装订单信息
					for(String oi:orderIds){
						TradeOrdersDTO todto= tradeOrdersDAO.queryTradeOrderById(oi);
						jaOrders.add(JSON.toJSON(todto));
					}
					joL.add(joStatement);
				}
				dataGrid.setRows(joL);
				dataGrid.setTotal(count);
				er.setResult(dataGrid);
				er.setResultMessage("success");
			}else{
				er.addErrorMessage("您要查询的数据不存在");
			}
		}catch(Exception e){
			er.setResultMessage("exception");
			er.addErrorMessage(e.getMessage());
			e.printStackTrace();
			//throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * 根据对账单号查询所有的订单号，并封装订单信息
	 * @param statementId
	 * @return
	 */
	private List<String> queryOrderByStatementId(String statementId){

		return monthlyStatementDAO.queryOrderByStatementId(statementId);
	}



	@Override
	public ExecuteResult<DataGrid<TradeOrdersDTO>> queryOrders(TradeOrdersQueryInDTO inDTO,
															   Pager<TradeOrdersQueryInDTO> pager) {
		logger.info("==============开始订单查询=====================");
		ExecuteResult<DataGrid<TradeOrdersDTO>> result = new ExecuteResult<DataGrid<TradeOrdersDTO>>();
		try {
			DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
			//查询父级订单 暂时不用
//			List<TradeOrdersDTO> parentOrders = this.tradeOrdersDAO.queryParentOrders(inDTO,pager);
			List<TradeOrdersDTO> subOrders=new ArrayList<TradeOrdersDTO>();
			Long count=null;
			//查询子订单
			if(inDTO.isNotApproved()){//不走审核流程
				subOrders = this.monthlyStatementDAO.queryTradeOrders(inDTO, pager);
				count = this.monthlyStatementDAO.queryTradeOrdersCount(inDTO);
			}else{//走审核流程
				subOrders = this.monthlyStatementDAO.queryTradeOrders(inDTO, pager);
				count = this.monthlyStatementDAO.queryTradeOrdersCount(inDTO);
			}
			List<TradeOrderItemsDTO> items = null;
			for (TradeOrdersDTO order : subOrders) {
				//查询子订单行项目
				items = this.tradeOrderItemsDAO.queryItemsByOrderId(order.getOrderId());
				order.setItems(items);
			}
			dataGrid.setTotal(count);
			dataGrid.setRows(subOrders);
			result.setResult(dataGrid);
		} catch (Exception e) {
			logger.error("执行方法【queryOrders】报错：{}", e);
			result.addErrorMessage("执行方法【queryOrders】报错：" + e.getMessage());
			throw new RuntimeException(e);
		} finally {
			logger.info("==============结束订单查询=====================");
		}
		return result;
	}
}
