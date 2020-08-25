package com.camelot.settlecenter.job;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.camelot.common.enums.BankSettleTypeEnum;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.common.enums.FactorageEnums.FactorageStatus;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.common.enums.SettleEnum.SettleStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.DualAccountInfoService;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dao.FactorageJournalDAO;
import com.camelot.payment.domain.FactorageJournal;
import com.camelot.payment.dto.FactorageJournalDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.settlecenter.service.util.SettleUtil;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

public class ConfirmSettleJob extends SettleUtil{

	private static final Logger logger = LoggerFactory.getLogger(ConfirmSettleJob.class);

	@Resource
	private StatementExportService statementExportService;
	@Resource
	private PaymentExportService paymentExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private CiticExportService citicExportService;
	@Resource
	private DualAccountInfoService dualAccountInfoService;
	@Resource
	private FactorageJournalDAO factorageJournalDAO;
	
	/**
	 * 结算单周期性结算，并拾起被遗弃的结算单明细加入本轮结算周期
	 * 
	 * @Title: createBill
	 */
	public void createBill() {
		logger.info("\n 定时调用结算单周期性进行处理：启动时间：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
		
		// 结算
		List<SettlementDTO> listSettle=statementExportService.findSettleByStatus(SettleStatusEnum.UnBill);
		for (SettlementDTO settlement:listSettle) {
		
			BigDecimal platformIncome=new BigDecimal("0");// 平台总收入
			BigDecimal platformExpenditure=new BigDecimal("0"); // 平台总支出
			BigDecimal sellerIncome=new BigDecimal("0");// 卖家总收入
			BigDecimal sellerExpenditure=new BigDecimal("0");// 卖家总支出
			BigDecimal orderTotalMoney=new BigDecimal("0");// 卖家总支出
			BigDecimal commissionTotalMoney=new BigDecimal("0");// 佣金总额
			BigDecimal settlementTotalMoney=new BigDecimal("0"); // 结算总金额
			BigDecimal refundTotalMoney=new BigDecimal("0"); // 退款总金额
			BigDecimal platformNeedPay=new BigDecimal("0"); // 平台需要支付给卖家的金额
			//String paymentId = statementExportService.buildPaymentId(PayBankEnum.CITIC.name());
			
			SettlementDetailDTO settleDetailCondition2 =new SettlementDetailDTO();
			settleDetailCondition2.setSettlementId(settlement.getId());
			List<SettlementDetailDTO> listSettleDetail2 = statementExportService.findSettlementDetail(settleDetailCondition2);
			logger.info("\n 定时调用结算单周期性进行处理：listSettleDetail：{}",listSettleDetail2.size());
			if(listSettleDetail2.size() == 0){
				continue ;
			}
			//一个结算单对应一个结算周期
			SettlementDetailDTO settleDetail2 = listSettleDetail2.get(0);
//			for (SettlementDetailDTO settleDetail2 :listSettleDetail2) {
			// 校验订单是否已支付，可能存在货到付款但未支付的订单
			ExecuteResult<TradeOrdersDTO> tradeOrderResult = tradeOrderExportService.getOrderById(settleDetail2.getOrderId());
			if(tradeOrderResult.isSuccess()&&tradeOrderResult.getResult().getPaid()==2&&
					settleDetail2.getStatus() == SettleDetailStatusEnum.PayAffirm.getCode()){ // 订单是否支付标记  1未支付 2已支付
				platformIncome=platformIncome.add(settleDetail2.getPlatformIncome());
				platformExpenditure=platformExpenditure.add(settleDetail2.getPlatformExpenditure());
				sellerIncome=sellerIncome.add(settleDetail2.getSellerIncome());
				sellerExpenditure=sellerExpenditure.add(settleDetail2.getSellerExpenditure());
				orderTotalMoney=orderTotalMoney.add(settleDetail2.getOrderPrice());
				commissionTotalMoney=commissionTotalMoney.add(settleDetail2.getCommission());
				refundTotalMoney=refundTotalMoney.add(settleDetail2.getRefundMoney());
				platformNeedPay=platformNeedPay.add(settleDetail2.getSellerCashAccountIncome());
				
				settlementTotalMoney=platformNeedPay;
				
				//20160302 修改结算 
				//settlement.setPaymentId(paymentId);
				settlement.setPlatformIncome(platformIncome);//  平台总收入
				settlement.setPlatformExpenditure(platformExpenditure);//  平台总支出
				settlement.setSellerIncome(sellerIncome);//  卖家总收入
				settlement.setSellerExpenditure(sellerExpenditure);//  卖家总支出
				settlement.setOrderTotalMoney(orderTotalMoney);//  订单总金额
				settlement.setCommissionTotalMoney(commissionTotalMoney);//  佣金总金额
				settlement.setSettlementTotalMoney(settlementTotalMoney);//  结算总金额
				settlement.setRefundTotalMoney(refundTotalMoney);//  退款总金额
				settlement.setPlatformNeedPay(platformNeedPay);//  平台需要支付给卖家的金额
				settlement.setPlatformHavePaid(new BigDecimal("0"));//  平台已付金额
				settlement.setBillDate(new Date());
				settlement.setStatus(SettleStatusEnum.UnSettle.getCode());
				
			}else if(settleDetail2.getStatus() == SettleDetailStatusEnum.UnPaying.getCode()){//如果没有导入银行的结算单，则抛出此结算明细
				//不需要修改结算明细
				//settleDetail2.setRemark("原结算单号："+settleDetail2.getSettlementId()+"；变更记录：");
				//settleDetail2.setSettlementId(0l);
				//statementExportService.modifySettleDetailById(settleDetail2);
				//需要修改结算单的应结算日期，将应结算日期修改为第二天
				Date settlementDate = settlement.getSettlementDate();
				Calendar calendar = new GregorianCalendar();
			    calendar.setTime(settlementDate); 
			    calendar.add(calendar.DATE, 1);
				settlement.setSettlementDate(calendar.getTime());
			}
//			}
			
			logger.info("\n 定时调用结算单周期性进行处理：settlement：{}",JSON.toJSONString(settlement));
			statementExportService.modifyStatement(settlement);
		}
		
		logger.info("\n 定时调用结算单周期性进行处理：结束时间：{}，处理数量：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS),listSettle.size());
	}
	
	/**
	 * 周期性核对第三方结算详情，根据银行结算结果修改结算明细单状态为银行确认结算,并标记为已处理
	 * 
	 * @Title: collateSettleDetail
	 */
	public void collateSettleDetail() {
		logger.info("\n 定时调用周期性核对第三方结算详情进行处理：启动时间：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
		int successCount=0;
		// 查询未处理的银行结算数据
		BankSettleDetailDTO bankSettleDetailCondition = new BankSettleDetailDTO();
		bankSettleDetailCondition.setOrderStatus(2);
		bankSettleDetailCondition.setLiquidateStatus(2);
		bankSettleDetailCondition.setDealFlag("1");
		List<BankSettleDetailDTO> listBankSettleDetailDTO=statementExportService.findBankSettleDetail(bankSettleDetailCondition);
		for(BankSettleDetailDTO bankSettleDetailDTO:listBankSettleDetailDTO){
			try{
				BigDecimal factorage = bankSettleDetailDTO.getFactorage();
				BigDecimal banlance=new BigDecimal("0.00");
				boolean flag=true;// 是否全部订单都结算
				// 根据对外交易号查询对外交易号下所有的子订单号
				List<OrderInfoPay>  listOrderInfoPay = paymentExportService.findChildTransByOutTrades(bankSettleDetailDTO.getOutTradeNo());
				for (OrderInfoPay childOrderPay:listOrderInfoPay) {
					// 银行手续费进行权重分摊 当前子单手续费=（子订单金额*手续费)/支付总金额
					BigDecimal childOrderFactorage =childOrderPay.getOrderAmount().multiply(bankSettleDetailDTO.getFactorage())
							.divide(bankSettleDetailDTO.getOrderAmount(),2, BigDecimal.ROUND_UP); //小数大于三位，自动+1分
					//查询该订单已经退款成功的手续费
					List<FactorageJournal> factorageJournalList = factorageJournalDAO.selectByOrderNoAndStatus(childOrderPay.getOrderNo(),FactorageStatus.RefundSuccess.getFacCode());
					for(FactorageJournal factorageJournal : factorageJournalList){
						childOrderFactorage = childOrderFactorage.subtract(factorageJournal.getFactorage());
						factorage = factorage.subtract(factorageJournal.getFactorage());
						if(factorage.compareTo(new BigDecimal("0.00")) <= 0){
							factorage = new BigDecimal("0.00");
							break;
						}
					}
					// 结算明细单中对应子订单更改为银行已结算,并分摊手续费
					int count =statementExportService.modifySettleDetailByOrderId(childOrderPay.getOrderNo(),SettleDetailStatusEnum.PayAffirm,childOrderFactorage);
					if(count>0){
						
						// 初始化记录手续费
						FactorageJournalDTO factorageJournalDTO =new FactorageJournalDTO();
						factorageJournalDTO.setOrderNo(childOrderPay.getOrderNo());
						factorageJournalDTO.setFactorage(childOrderFactorage);
						factorageJournalDTO.setStatus(FactorageStatus.Initial);
						paymentExportService.saveFactorageJournal(factorageJournalDTO);
						
						banlance=banlance.add(childOrderFactorage);
					}else{
						flag=false;
					}
				}
				
				// 校验分摊后的手续费是否有差异，如果有差异，则代表从卖家手里多获取到了手续余额，存入平台佣金账户。
				int difference=banlance.compareTo(factorage);
				if(flag&&difference>=0){
					ExecuteResult<String> result=new ExecuteResult<String>();
					if(difference>0){
						ExecuteResult<FinanceAccountInfoDto> financeAccResult = dualAccountInfoService.getFinanceAccountInfoDtoById("1");
						FinanceAccountInfoDto financeAccDto =financeAccResult.getResult();
						PayReqParam payReqParam =new PayReqParam();
						payReqParam.setOutTradeNo(statementExportService.buildPaymentId(PayBankEnum.CITIC.name()));
						// 付款账户 网银在线中信结算附属账户
						//如果是网银在线导入的结算单
						if(bankSettleDetailDTO.getBankType().equals(BankSettleTypeEnum.CB.getLable())){
							payReqParam.setFromAccount(financeAccDto.getSettlementAccountNumber());
							payReqParam.setExtraParam("网银在线中信结算附属账户-->中信佣金附属账户");
						}else if(bankSettleDetailDTO.getBankType().equals(BankSettleTypeEnum.AP.getLable())){//支付宝导入的结算单
							payReqParam.setFromAccount(financeAccDto.getSettlementAlipayAccountNumber());
							payReqParam.setExtraParam("支付宝中信结算附属账户-->中信佣金附属账户");
						}else if(bankSettleDetailDTO.getBankType().equals(BankSettleTypeEnum.WX.getLable())){//微信导入的结算单
							payReqParam.setFromAccount(financeAccDto.getSettlementWXAccountNumber());
							payReqParam.setExtraParam("微信中信结算附属账户-->中信佣金附属账户");
						}
						// 收款账户 网银在线中信佣金附属账户
						payReqParam.setToAccount(financeAccDto.getCommissionAccountNumber());
						payReqParam.setSeller(financeAccDto.getCommissionAccountName());
						payReqParam.setTotalFee(banlance.subtract(factorage));
						
						result=citicExportService.transferAffiliated(payReqParam, "0", CiticPayTypeCode.factorage.getCode());
					}
					
					if(result.isSuccess()){
						statementExportService.dealBankSettleDetail(bankSettleDetailDTO.getOutTradeNo());
						successCount++;
					}
				}
			}catch (Exception e) {
				logger.info("\n 定时调用周期性核对第三方结算详情进行处理：异常：{}",e.getMessage());
			}
	    }
		logger.info("\n 定时调用周期性核对第三方结算详情进行处理：结束时间：{}，处理数量：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS),successCount);
	}
	/**
	 * 卖家每日0：00对结算单进行批量处理（每小时100单）
	 * @author zhouzj
	 * @since 2015-8-24
	 */
	public void proceedSettlement(){
		logger.info("\n 卖家每日结算单自动批量处理开始：proceedSettlement");
		// 结算
		List<SettlementDTO> listSettle=statementExportService.selectSettleForSellerSettlement(SettleStatusEnum.UnConfirmed);
		for(SettlementDTO settlement:listSettle){
			try {
				ExecuteResult<String> eRequest = statementExportService.proceedSettle(settlement.getId());
				
				if(eRequest.isSuccess()){
					logger.info("卖家结算自动处理成功");
				}else{
					logger.error("卖家结算自动处理失败，\n " + eRequest.getResultMessage());
				}
			} catch (Exception e) {
				logger.error("卖家结算自动处理失败，\n " + e.getMessage());
			}
		}
	}
}
