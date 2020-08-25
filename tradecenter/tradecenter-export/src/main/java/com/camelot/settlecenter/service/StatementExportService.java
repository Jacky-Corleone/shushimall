package com.camelot.settlecenter.service;

import java.math.BigDecimal;
import java.util.List;

import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.common.enums.SettleEnum.SettleStatusEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.dto.combin.SettlementCombinDTO;
import com.camelot.settlecenter.dto.indto.SettlementInDTO;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

/**
 * 
 * <p>Description: [结算单service]</p>
 * Created on 2015-3-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface StatementExportService {
	
	/**
	 * 生成结算明细单，如果当前周期内结算单不存在则生成结算单，否则明细添加到该结算单下,不做数据校验
	 * 
	 * 
	 * @param tradeOrder -orderId[订单号]/sellerId[卖家ID]/shopId[店铺ID]/PaymentPrice[订单金额] -- 必传参数
	 * @return
	 */
	ExecuteResult<String> createSettleDetail(TradeOrdersDTO tradeOrder,List<RefundOrder> refundOrderList) throws Exception;
	
	/**
	 * 结算单进行结算(卖家确认，进行正真转账)
	 * 
	 * @param settlementId
	 * @return
	 */
	ExecuteResult<String> proceedSettle(Long settlementId) throws Exception;
	
	/**
	 * 结算详情单根据子订单进行冻结（冻结之后的订单不参与结算单生成）
	 * 
	 * @param orderId
	 * @return
	 */
	ExecuteResult<String> freezeSettleDetail(String orderId) throws Exception;
	
	/**
	 * 
	 * <p>Discription:[结算单查询]</p>
	 * Created on 2015-3-10
	 * @param settlementInDTO
	 * @return
	 * @author:yuht
	 */
	ExecuteResult<DataGrid<SettlementCombinDTO>> querySettlementList(SettlementInDTO settlementInDTO,@SuppressWarnings("rawtypes") Pager page);
	
	/**
	 * 
	 * <p> [根据ID或ID组修改结算状态]</p>
	 * Created on 2015-3-10
	 * @param id
	 * @return
	 * @author:yuht
	 */
	ExecuteResult<String>  modifySettlementStates(Long... id);
	
	
	/**
	 * 
	 * <p> [根据ID或ID组修改结算状态--线下结算]</p>
	 * Created on 2015-3-10
	 * @param id
	 * @return
	 * @author:yuht
	 */
	ExecuteResult<String>  modifySettlementStatesNoPay(Long... id);
	/**
	 * 保存导入数据并确认结算详情单已在网银在线上结算完成
	 * 
	 * @param listBankSettleDetailDTO orderNo[订单号]/ orderAmount[订单金额]/bankType[导入银行] 必填
	 * @return
	 */
	ExecuteResult<String> saveBankSettle(List<BankSettleDetailDTO> listBankSettleDetailDTO) throws Exception;
	
	/**
	 * 根据条件查询银行导入结算数据
	 * 
	 * @param bankSettleDetailDTO 
	 * @param pager
	 * @return
	 */
	DataGrid<BankSettleDetailDTO> findBankSettle(BankSettleDetailDTO bankSettleDetailDTO,@SuppressWarnings("rawtypes") Pager pager);
	
	/**
	 * 根据状态查询结算单
	 * 
	 * @param status
	 * @return
	 */
	List<SettlementDTO> findSettleByStatus(SettleStatusEnum status);
	/**
	 * 根据状态查询结算单
	 * 
	 * @param status
	 * @return
	 */
	List<SettlementDTO> selectSettleForSellerSettlement(SettleStatusEnum status);

	/**
	 * 修改结算单
	 * 
	 * @param settlement
	 * @return
	 */
	int modifyStatement(SettlementDTO settlement);
	
	/**
	 * 根据订单号修改结算明细单
	 * 
	 * @param settlementDetailDTO
	 * @return
	 */
	int modifySettleDetailByOrderId(String orderId,SettleDetailStatusEnum settleDetailStatusEnum,BigDecimal factorage);
	
	/**
	 * 根据条件查询结算详情单
	 * 
	 * @param settleDetailCondition
	 * @return
	 */
	List<SettlementDetailDTO> findSettlementDetail(SettlementDetailDTO settleDetailCondition);
	
	/**
	 * 根据条件查询银行结算明细
	 * 
	 * @param bankSettleDetailDTO
	 * @return
	 */
	List<BankSettleDetailDTO> findBankSettleDetail(BankSettleDetailDTO bankSettleDetailDTO);
	
	/**
	 * 处理银行结算明细为已处理
	 * 
	 * @param outTradeNo
	 * @return
	 */
	int dealBankSettleDetail(String outTradeNo);
	
	/**
	 * 根据ID修改结算单明细
	 * @param settlementDetailDTO
	 * @return
	 */
	int modifySettleDetailById(SettlementDetailDTO settlementDetailDTO);
	
	/**
	 * 获取当前店铺今天所在的周期内的待出账状态的结算单
	 * 
	 * @return
	 */
	SettlementDTO findSettleByPeriod(Long shopId);
	
	/**
	 * 为结算单生成一个支付对外交易号
	 * 
	 * @return
	 */
	String buildPaymentId(String payType);
	/**
	 * 查询金融账户
	 * 
	 * @return
	 */
	public FinanceAccountInfoDto findFinanceAcc();
	/**
	 * 新增结算单
	 * @param settlementDTO
	 */
	public void addSettlement(SettlementDTO settlementDTO);
}
