package com.camelot.settlecenter.dto.combin;

import java.io.Serializable;
import java.util.List;

import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;

/**
 * 
 * <p>Description: [结算单集合类]</p>
 * Created on 2015-3-10
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SettlementCombinDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	/**
	 * 结算单类
	 */
	private SettlementDTO settlement;
	/**
	 * 结算单详情list
	 */
	private List<SettlementDetailDTO> settlementDetailList;
	
	
	
	public SettlementDTO getSettlement() {
		return settlement;
	}
	public void setSettlement(SettlementDTO settlement) {
		this.settlement = settlement;
	}
	public List<SettlementDetailDTO> getSettlementDetailList() {
		return settlementDetailList;
	}
	public void setSettlementDetailList(List<SettlementDetailDTO> settlementDetailList) {
		this.settlementDetailList = settlementDetailList;
	}

	
	
	

}
