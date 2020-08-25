package com.camelot.tradecenter.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dao.TradeApprovedOrdersDao;
import com.camelot.tradecenter.dao.TradeOrdersDAO;
import com.camelot.tradecenter.domain.TradeApprovedOrders;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeApprovedOrdersExportService;

@Service("tradeApprovedOrdersExportService")
public class TradeApprovedOrdersExportServiceImpl implements TradeApprovedOrdersExportService{
	@Resource
    private TradeApprovedOrdersDao tradeApprovedOrdersDAO;
	@Resource
	private TradeOrdersDAO tradeOrdersDAO;
	@Override
	public ExecuteResult<String> approveSubmit(TradeOrdersDTO tradeOrdersDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		if(tradeOrdersDTO==null){
			result.addErrorMessage("参数为空");
			return result;
		}
		if(tradeOrdersDTO.getOrderId()==null){
			result.addErrorMessage("订单id不能为空");
			return result;
		}
		TradeApprovedOrders dtos=new TradeApprovedOrders();
		dtos.setOrderId(tradeOrdersDTO.getOrderId());//订单id
		dtos.setApproveStatus(tradeOrdersDTO.getApproveStatus());//审核状态
		dtos.setRejectReason(tradeOrdersDTO.getRejectReason());//驳回原因
		dtos.setParentOrderId(tradeOrdersDTO.getParentOrderId());//父订单id
		this.tradeApprovedOrdersDAO.update(dtos);//修改订单状态为审核通过
		TradeOrdersDTO tradeOrders=tradeOrdersDAO.queryTradeOrderById(dtos.getOrderId());//查询订单表中是否已经有子订单
		if(tradeOrders==null){
			tradeApprovedOrdersDAO.insertOrdersFromApprovedOrdersByoid(dtos.getOrderId());//同事审核订单表中向订单表中插入该订单数据
		}
		TradeOrdersDTO pOrders=tradeOrdersDAO.queryTradeOrderById(dtos.getParentOrderId());//查询订单表中是否已经有父订单
		if(pOrders==null){
			tradeApprovedOrdersDAO.insertOrdersFromApprovedOrdersBypid(dtos.getParentOrderId());
		}
		return result;
	}

	@Override
	public ExecuteResult<String> approveReject(TradeOrdersDTO tradeOrdersDTO) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		if(tradeOrdersDTO==null){
			result.addErrorMessage("参数为空");
			return result;
		}
		if(tradeOrdersDTO.getOrderId()==null){
			result.addErrorMessage("订单id不能为空");
			return result;
		}
		TradeApprovedOrders dtos=new TradeApprovedOrders();
		dtos.setOrderId(tradeOrdersDTO.getOrderId());//订单id
		dtos.setApproveStatus(tradeOrdersDTO.getApproveStatus());//审核状态
		dtos.setRejectReason(tradeOrdersDTO.getRejectReason());//驳回原因
		this.tradeApprovedOrdersDAO.update(dtos);//修改订单状态为审核驳回
		return result;
	}

}
