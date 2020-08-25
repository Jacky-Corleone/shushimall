package com.camelot.tradecenter.dto.combin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.camelot.tradecenter.dto.TradeOrdersDTO;

public class TradeOrderCreateDTO implements Serializable {

	private static final long serialVersionUID = -5781206828844373396L;

	private TradeOrdersDTO parentOrder;//父级订单
	private List<TradeOrdersDTO> subOrders = new ArrayList<TradeOrdersDTO>();//子订单
	public TradeOrdersDTO getParentOrder() {
		return parentOrder;
	}
	public void setParentOrder(TradeOrdersDTO parentOrder) {
		this.parentOrder = parentOrder;
	}
	public List<TradeOrdersDTO> getSubOrders() {
		return subOrders;
	}
	public void setSubOrders(List<TradeOrdersDTO> subOrders) {
		this.subOrders = subOrders;
	}
	
	
	
}
