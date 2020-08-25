package com.camelot.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface BankSettleDetailDAO  extends BaseDAO<BankSettleDetailDTO>{
	
	int adds(@Param("listBankSettleDetail")List<BankSettleDetailDTO> listBankSettleDetail);
	
	/**
	 * 根据对外交易号查询有效银行结算单据
	 * 
	 * @param outTradeNo
	 * @return
	 */
	BankSettleDetailDTO selectBankSettleByOutTradeNo(@Param("outTradeNo")String outTradeNo);
	
	/**
	 * 处理银行结算明细为已处理
	 * 
	 * @param outTradeNos
	 * @return
	 */
	int updateDealByOutTradeNo(@Param("outTradeNo")String outTradeNo);
	
}