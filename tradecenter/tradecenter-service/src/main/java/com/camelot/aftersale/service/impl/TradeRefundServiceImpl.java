package com.camelot.aftersale.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.accountinfo.dao.FinanceAccountDAO;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.service.TradeRefundService;
import com.camelot.aftersale.service.TradeReturnGoodsDetailService;
import com.camelot.aftersale.service.TradeReturnGoodsService;
import com.camelot.aftersale.service.util.AftersaleUtil;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.common.enums.FactorageEnums.FactorageStatus;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.ReturnStatusEnums.TradeRefundStatusEnum;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.common.enums.SettleEnum.SettleStatusEnum;
import com.camelot.common.util.CommonUtil;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.DualAccountInfoService;
import com.camelot.payment.dao.FactorageJournalDAO;
import com.camelot.payment.dao.RefundOrderDAO;
import com.camelot.payment.dao.TransationsDAO;
import com.camelot.payment.domain.FactorageJournal;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.service.CiticService;
import com.camelot.settlecenter.dao.StatementDAO;
import com.camelot.settlecenter.dao.StatementDetailDAO;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.dto.SettlementInfoOutDTO;
import com.camelot.settlecenter.dto.indto.SettlementInfoInDTO;
import com.camelot.settlecenter.service.SettleItemExpenseExportService;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.tradecenter.dao.TradeOrderItemsDAO;
import com.camelot.tradecenter.domain.TradeOrderItems;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.userInfo.UserAccountDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExtendsService;


/**
 * <p>
 * 业务逻辑实现类
 * </p>
 * 
 * @author
 * @createDate 
 **/
@Service("tradeRefundService")
class TradeRefundServiceImpl extends AftersaleUtil implements TradeRefundService {
	
	private final static Logger logger = LoggerFactory.getLogger(TradeRefundServiceImpl.class);
	@Resource
	private RefundOrderDAO refundOrderDAO;
	@Resource
	private StatementDetailDAO statementDetailDAO;
	@Resource
	private StatementDAO statementDAO;
	@Resource
	private TransationsDAO transationsDAO;
	@Resource
    private TradeReturnGoodsService tradeReturnGoodsService;
	@Resource
    private TradeReturnGoodsDetailService tradeReturnGoodsDetailService;
	@Resource
	private FinanceAccountDAO financeAccountDAO;
	@Resource
	private FactorageJournalDAO factorageJournalDAO;
	@Resource
    private TradeOrderItemsDAO tradeOrderItemsDAO;
	
	@Resource(name="citicGateService")
	private CiticService citicService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private StatementExportService statementExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private SettleItemExpenseExportService settleItemExpenseExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private DualAccountInfoService dualAccountInfoService;
	
	@Override
	public ExecuteResult<RefundPayParam> refundApply(RefundPayParam refundPayParam) {
		logger.info("\n 方法[{}]，入参：[{}]","TradeRefundServiceImpl-refundApply",JSONObject.toJSONString(refundPayParam));
		ExecuteResult<RefundPayParam> result =new ExecuteResult<RefundPayParam>();
		// 1.校验必填参数是否为空
		result= this.verifyRefundApply(refundPayParam);
		if(!result.isSuccess()){
			return result;
		}
		try {
			RefundOrder refundOrder = this.buildRefundOrder(refundPayParam);
			
			if(PayBankEnum.CITIC.name().equals(refundOrder.getOrderPayBank())){
				// 根据买家用户ID查询 买家账户信息
				ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(refundPayParam.getBuyerId());
				UserCiticDTO userCiticDTO=userInfo.getResult().getUserCiticDTO();
				UserAccountDTO userAccountDTO=userInfo.getResult().getUserAccountDTO();
				// 买家账户可能是“印刷家_xx”，与公司名称不符，造成退款不成功。调用银行真实账户名称
				AffiliatedBalanceDto  affiliatedBalanceDto  = citicService.querySubBalance(userCiticDTO.getBuyerPaysAccount());
				AffiliatedBalance affiliatedBalance=affiliatedBalanceDto.getList().get(0);
				
				refundOrder.setBuyAccountNo(userCiticDTO.getBuyerPaysAccount());
				refundOrder.setBuyAccountName(affiliatedBalance.getSUBACCNM());
				refundOrder.setBuyBankName(userAccountDTO.getBankName());
				refundOrder.setBankBranchJointLine(userAccountDTO.getBankBranchJointLine());
				refundOrder.setSameBank(userAccountDTO.getIsCiticBank());
				
			}else if(PayBankEnum.CB.name().equals(refundOrder.getOrderPayBank()) || PayBankEnum.CB_MOBILE.name().equals(refundOrder.getOrderPayBank())){
				// 退款到中信网银在线退款账户
				FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
				refundOrder.setBuyAccountNo(financeAccountInfoDto.getRefundAccountNumber());
				refundOrder.setBuyAccountName(financeAccountInfoDto.getRefundAccountName());
			}else if(PayBankEnum.AP.name().equals(refundOrder.getOrderPayBank()) || PayBankEnum.AP_MOBILE.name().equals(refundOrder.getOrderPayBank())){
				// 退款到支付宝在线退款账户
				FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
				refundOrder.setBuyAccountNo(financeAccountInfoDto.getRefundAlipayAccountNumber());
				refundOrder.setBuyAccountName(financeAccountInfoDto.getRefundAlipayAccountName());
			}else if(PayBankEnum.WX.name().equals(refundOrder.getOrderPayBank()) || PayBankEnum.WXPC.name().equals(refundOrder.getOrderPayBank())){
				// 退款到微信在线退款账户
				FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
				refundOrder.setBuyAccountNo(financeAccountInfoDto.getRefundWXAccountNumber());
				refundOrder.setBuyAccountName(financeAccountInfoDto.getRefundWXAccountName());
			}
			
			refundOrder.setOutTradeNo(transationsDAO.selectOutTranNo(refundOrder.getOrderPayBank()));
			refundOrder.setStatus(TradeRefundStatusEnum.initialize.getCode());
			
			refundOrderDAO.add(refundOrder);
			result.setResult(this.buildRefundPayParam(refundOrder));
			result.setResultMessage("添加成功");
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			logger.error("\n 方法[{}]，异常：[{}]","TradeRefundServiceImpl-refundApply",e);
		}
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-refundApply",JSONObject.toJSONString(result));
		return result;
	}
	@Override
	public ExecuteResult<String> refundDeal(RefundPayParam refundPayParam,boolean isBuildSettleDetail,boolean isReturnFactorage,boolean isReturnCommission) throws Exception{
		logger.info("\n 方法[{}]，入参：[{}][{}][{}][{}]","TradeRefundServiceImpl-refundDeal",JSONObject.toJSONString(refundPayParam),isBuildSettleDetail,isReturnFactorage,isReturnCommission);
		ExecuteResult<String> result =new ExecuteResult<String>();
		//根据订单编号，获取订单信息，查询订单金额
		RefundOrder refundOrder = refundOrderDAO.selectRefundByRePro(refundPayParam.getReproNo());
		if(refundOrder == null){
			result.addErrorMessage("退款单不存在");
			return result;
		}
		SettlementDetailDTO settleDetail = null;
		SettlementDTO settlement = null;
		// 根据订单号获取结算单明细
		if(isBuildSettleDetail){
			SettlementDetailDTO settleDetailCon =new SettlementDetailDTO();
			settleDetailCon.setOrderId(refundOrder.getOrderNo());	
			List<SettlementDetailDTO> listSettleDetail = statementDetailDAO.queryList(settleDetailCon, null);
			//一个结算单对应一个结算明细
			if(listSettleDetail.size()>0){
				settleDetail = listSettleDetail.get(0);
				settlement = statementDAO.queryById(settleDetail.getSettlementId());
			}else {
				result.addErrorMessage("结算单不存在");
				return result;
			}
		}
		//执行退款后，修改结算单相应的数据
		ExecuteResult<String> resultSel = this.reBuildSettlement(refundOrder , settlement , settleDetail,new BigDecimal(0.00));
		if(resultSel.getErrorMessages().size() != 0){
			result.addErrorMessage(resultSel.getErrorMessages().get(0));;						
		}
		
		//平台应退手续费默认为0.00，即不退手续费
		//BigDecimal platformFactorage = new BigDecimal(0.00);
		//20160302 修改结算
		/*if(isReturnFactorage && CommonUtil.getPaymentMethodByPayBank(refundOrder.getOrderPayBank()) != 2){
			
			Transations transations = transationsDAO.selectTransByOrderNo(refundOrder.getOrderNo());
			ExecuteResult<BankSettleDetailDTO> bankSettleDetailResult = this.getBankSettleDetailDTOByRefundOrder(transations);
			if(!bankSettleDetailResult.isSuccess()){
				result.setErrorMessages(bankSettleDetailResult.getErrorMessages());
				return result;
			}
			BankSettleDetailDTO bankSettleDetail = bankSettleDetailResult.getResult();
			BigDecimal totalFactorage = bankSettleDetail.getFactorage();
			
			//查询应退手续费
			platformFactorage = this.getPlatformFactorage(refundOrder.getRefundAmount(), refundOrder.getOrderPrice(), totalFactorage);
			//查询是否还有手续费可退
			if(settleDetail != null && SettleDetailStatusEnum.PayAffirm.getCode().equals(settleDetail.getStatus())){
				BigDecimal canPayFactorage = settleDetail.getFactorage();
				if(canPayFactorage.compareTo(platformFactorage) <= 0){
					platformFactorage = canPayFactorage;
				}
			}else{
				//计算分摊的手续费
				BigDecimal orderFactorage =transations.getOrderAmount().multiply(bankSettleDetail.getFactorage())
						.divide(bankSettleDetail.getOrderAmount(),2, BigDecimal.ROUND_UP); //小数大于三位，自动+1分
				//查询已经扣除的手续费
				List<FactorageJournal> factorageJournalList = factorageJournalDAO.selectByOrderNoAndStatus(refundOrder.getOrderNo(),FactorageStatus.RefundSuccess.getFacCode());
				for(FactorageJournal factorageJournal : factorageJournalList){
					orderFactorage = orderFactorage.subtract(factorageJournal.getFactorage());
				}
				if(orderFactorage.compareTo(platformFactorage) <= 0){
					platformFactorage = orderFactorage;
				}
			}
		}
		if(TradeRefundStatusEnum.initialize.getCode().equals(refundOrder.getStatus()) || TradeRefundStatusEnum.fail.getCode().equals(refundOrder.getStatus())){
			//完全退款

			if(isBuildSettleDetail){
				result =this.buildSettleRefDeal(refundOrder,settlement,settleDetail,platformFactorage,isReturnCommission);
			}else{
				result =this.unBuildSettleRefDeal(refundOrder,platformFactorage);
			}
		}else if(TradeRefundStatusEnum.part.getCode().equals(refundOrder.getStatus())){
			//只需要退手续费和佣金
			
			//退手续费
			result = this.refundFactorage(refundOrder,platformFactorage);
			if(!result.isSuccess()){
				return result;
			}
			//退佣金
			result = this.refundCommission(refundOrder,settlement,settleDetail,isReturnCommission);
			
		}else if(TradeRefundStatusEnum.partFactorage.getCode().equals(refundOrder.getStatus())){
			//只需要退手续费
			result = this.refundFactorage(refundOrder,platformFactorage);
			
		}else if(TradeRefundStatusEnum.partSuccess.getCode().equals(refundOrder.getStatus())){
			//只需要退佣金
			result = this.refundCommission(refundOrder,settlement,settleDetail,isReturnCommission);
			
		}else{
			result.setResultMessage("退款已完成，无需重复操作");
		}
		//修改退货单状态
		refundOrder.setRefundTime(new Date());
		refundOrderDAO.update(refundOrder);
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-refundDeal",JSONObject.toJSONString(result));
		*/
		return result;
	}
	
	@Override
	public ExecuteResult<RefundPayParam> findRefInfoByRefNo(String refundNo) {
		logger.info("\n 方法[{}]，入参：[{}]","TradeRefundServiceImpl-findRefInfoById",refundNo);
		ExecuteResult<RefundPayParam> result = new ExecuteResult<RefundPayParam>();
		RefundOrder refundOrder = refundOrderDAO.selectRefInfoByRefNo(refundNo);
		result.setResult(this.buildRefundPayParam(refundOrder));
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-findRefInfoById",JSONObject.toJSONString(result));
		return result;
	}
	
	@Override
	public DataGrid<RefundPayParam> findRefInfoByCondition(RefundPayParam refundPayParam,@SuppressWarnings("rawtypes") Pager pager) {
		logger.info("\n 方法[{}]，入参：[{}]","TradeRefundServiceImpl-findRefInfoByCondition",JSON.toJSONString(refundPayParam));
		
		DataGrid<RefundPayParam> result = new DataGrid<RefundPayParam>();
		RefundOrder refundOrder = this.buildRefundOrder(refundPayParam);
		long count= refundOrderDAO.queryCount(refundOrder);
		if(count>0){
			List<RefundOrder> listQuery = refundOrderDAO.queryList(refundOrder, pager);
			List<RefundPayParam> listResult = new ArrayList<RefundPayParam>();
			for (RefundOrder refOrder:listQuery) {
				listResult.add(this.buildRefundPayParam(refOrder));
			}
			result.setRows(listResult);
		}
		result.setTotal(count);
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-findRefInfoByCondition",JSONObject.toJSONString(result));
		return result;
	}
	// TODO 废弃代码
    public ExecuteResult<RefundPayParam> findRefInfoByReturnGoodsCode(String returnGoodsCodeNo) {
        ExecuteResult<RefundPayParam> result = new ExecuteResult<RefundPayParam>();
        return result;
    }
    /**
     *  买家没有确认收货，没有生成结算单
     * 
     * 	a 平台没有下载网银在线结算明细excel；无法计算第三方手续费，卖家无法同意退款（系统会提示等待平台导入结算明细excel）
	 *	b 平台已经下载网银在线结算明细excel，并导入系统，此时数据库表（factorage_journal）中保存有订单手续费信息。
	 *  c 生成一个新的手续费流水，保存手续费的变化，手续费表（factorage_journal）添加一条记录：orderId为退款的订单ID，factorage为需要退的手续费，用-x表示，remark中存放手续费变化说明
     *  d 结算第三方手续费时，只需要将相同订单ID的记录factorage相加，就是当前时间需要扣除的手续费
     * @param refundOrder
     * @param tradeOrdersDTO
     * @param isReturnFactorage  是否退手续费
     * @param factorage  应退手续费
     * @return
     * @author 王东晓
     */
	private ExecuteResult<String> unBuildSettleRefDeal(RefundOrder refundOrder ,BigDecimal factorage){
		ExecuteResult<String> result =new ExecuteResult<String>();
		PayReqParam payReqParam =new PayReqParam();
		payReqParam.setOutTradeNo(refundOrder.getOutTradeNo());
		
		// 1.付款账户及实际转入退款账户的退款金额
		// 个人支付
		Integer paymentMethod = CommonUtil.getPaymentMethodByPayBank(refundOrder.getOrderPayBank());
		if(paymentMethod == null){
			result.addErrorMessage("请查看订单的支付方式，重新退款");
			return result;
		}
		if(PayMethodEnum.PAY_PERSONAL.getCode().equals(paymentMethod)){
			
			ExecuteResult<String> accountResult = dualAccountInfoService.getSettlementAccountByPayBank(refundOrder.getOrderPayBank());
			if(!accountResult.isSuccess()){
				result.setErrorMessages(accountResult.getErrorMessages());
				return result;
			}
			payReqParam.setFromAccount(accountResult.getResult());
			payReqParam.setTotalFee(refundOrder.getRefundAmount().subtract(factorage));
		}else{
			Transations  transations= transationsDAO.selectTransByOrderNo(refundOrder.getOrderNo());
			payReqParam.setFromAccount(transations.getToAccount());// 卖家冻结账户
			// 全额退款金额
			payReqParam.setTotalFee(refundOrder.getRefundAmount());
		}
		
		// 2.校验付款账户余额是否足以支付退款
		try {
			boolean isEnough = citicService.querySubBalanceForPay(payReqParam.getTotalFee(), payReqParam.getFromAccount());
			
			if(!isEnough){
				logger.error("付款账户余额不足，退款失败，如有疑问，请联系平台。客服电话：4006 770 878 按3");
				result.addErrorMessage("付款账户余额不足，退款失败，如有疑问请联系平台。客服电话：4006 770 878 按3");
				return result;
			}
		} catch (Exception e) {
			result.addErrorMessage("付款账户异常，如有疑问，请联系平台。客服电话：4006 770 878 按3");
			return result;
		}
		// 3.收款账户
		payReqParam.setToAccount(refundOrder.getBuyAccountNo());
		payReqParam.setSeller(refundOrder.getBuyAccountName()); 	
		
		// 4.根据退款结果处理
		ExecuteResult<String> resultPay=citicService.transferAffiliated(payReqParam, "0", CiticPayTypeCode.refund);		
		if(resultPay.isSuccess()){
			refundOrder.setStatus(TradeRefundStatusEnum.part.getCode());// 成功
		}else{
			result.addErrorMessage("退款失败-"+resultPay.getErrorMessages().get(0));
			return result;
		}
		
		// 4.根据手续费退款结果处理
		if(factorage.compareTo(new BigDecimal(0.00)) != 0){
			result = this.refundFactorage(refundOrder,factorage);
		}
		//根据手续费退款结果，退佣金，没有生成结算单的时候，没有佣金可退，只需要修改状态即可
		if(result.isSuccess()){
			refundOrder.setStatus(TradeRefundStatusEnum.success.getCode());
		}
		
		result.setResultMessage(TradeRefundStatusEnum.getEnumBycode(refundOrder.getStatus()).getLabel());
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-refundDeal",JSONObject.toJSONString(result));
		return result;
	}
	/**
	 * 生成结算单时退款方法
	 * 
	 * @param refundOrder
	 * @param tradeOrdersDTO
	 * @param settlement  结算单
	 * @param listSettleDetail 结算明细
	 * @param factorage 应退手续费，不可为空
	 * @param isReturnCommission  是否退佣金
	 * @return
	 */
	private ExecuteResult<String> buildSettleRefDeal(RefundOrder refundOrder ,SettlementDTO settlement,SettlementDetailDTO listSettleDetail,BigDecimal factorage,boolean isReturnCommission){
		ExecuteResult<String> result =new ExecuteResult<String>();
		
		
		//修改结算单和结算明细失败
		if(!result.isSuccess()){
			return result;
		}
		
		// 3.生成退款对象 TODO 
		ExecuteResult<PayReqParam> resultPayReqParam = this.buildPayReqParam(settlement,refundOrder,factorage);
		if(!resultPayReqParam.isSuccess()){
			result.setErrorMessages(resultPayReqParam.getErrorMessages());
			return result;
		}
		PayReqParam payReqParam = resultPayReqParam.getResult();
		// 4.校验付款账户余额是否足以支付
		boolean isEnough = citicService.querySubBalanceForPay(payReqParam.getTotalFee(), payReqParam.getFromAccount());
		if(!isEnough){
			logger.error("账户余额不足，退款失败。:账号："+payReqParam.getFromAccount());
			result.addErrorMessage("您的结算账户余额不足，退款失败，请先充值，再确认收货完成退款！");
			return result;
		}
		// 4.执行退款
		ExecuteResult<String> resultPay=citicService.transferAffiliated(payReqParam, "0", CiticPayTypeCode.refund);
		if(resultPay.isSuccess()){
			refundOrder.setStatus(TradeRefundStatusEnum.part.getCode());// 成功
			//执行退款后，修改结算单相应的数据
			result = this.reBuildSettlement(refundOrder , settlement , listSettleDetail,factorage);
			//退手续费
			if(factorage.compareTo(new BigDecimal(0.00)) != 0){
				//如果应退手续费为0，则不执行手续费退款，直接通过
				result = this.refundFactorage(refundOrder,factorage);
				if(!result.isSuccess()){
					//执行退款后，修改结算单相应的数据
					ExecuteResult<String> resultSel = this.reBuildSettlement(refundOrder , settlement , listSettleDetail,new BigDecimal(0.00));
					if(resultSel.getErrorMessages().size() != 0){
						result.addErrorMessage(resultSel.getErrorMessages().get(0));;						
					}
					return result;
				}
				
			}else{
				refundOrder.setStatus(TradeRefundStatusEnum.partSuccess.getCode());// 成功
			}
			//退佣金
			if(SettleStatusEnum.Confirmed.getCode().equals(settlement.getStatus())){
				result = this.refundCommission(refundOrder,settlement,listSettleDetail,isReturnCommission);
			}else{
				refundOrder.setStatus(TradeRefundStatusEnum.success.getCode());// 成功
			}
			
			result.setResultMessage(TradeRefundStatusEnum.getEnumBycode(refundOrder.getStatus()).getLabel());
		}else{
			result.addErrorMessage("退款失败");
		}
			
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-refundDeal",JSONObject.toJSONString(result));
		return result;
	}
	/**
	 * 构建中信支付参数，手续费退款，只有个人支付的订单有手续费，在外层校验
	 * 
	 * @param refundOrder 退款单  ：对外交易号必填
	 * @param fromAccount 退款账号：此处为手续费补足账号   必填
	 * @param paymentPrice  订单实际支付金额
	 * @param factorage  应退手续费
	 * @return
	 */
	private ExecuteResult<PayReqParam> buildFactoragePayReqParam(RefundOrder refundOrder,String fromAccount,BigDecimal factorage){
		
		ExecuteResult<PayReqParam> result = new ExecuteResult<PayReqParam>();
		
		PayReqParam payReqParam =new PayReqParam();
		payReqParam.setOutTradeNo(refundOrder.getOutTradeNo());
		
		//手续费
		payReqParam.setOutTradeNo(refundOrder.getOutTradeNo() + "3");// 重新定义对外交易号
		payReqParam.setFromAccount(fromAccount);// 平台手续费补足账户
		// 收款账户
		payReqParam.setToAccount(refundOrder.getBuyAccountNo());
		payReqParam.setSeller(refundOrder.getBuyAccountName()); 
		
//		factorage = this.getPlatformFactorage(refundOrder.getRefundAmount(), paymentPrice, factorage);	
		if(factorage.compareTo(new BigDecimal(0.00)) != 1){
			return null;
		}
		payReqParam.setTotalFee(factorage);
		result.setResult(payReqParam);
		return result;
	}
	/**
	 * 根据支付订单查询银行到如的excel
	 * @param refundOrder
	 * @return
	 */
	private ExecuteResult<BankSettleDetailDTO> getBankSettleDetailDTOByRefundOrder(Transations transations){
		ExecuteResult<BankSettleDetailDTO> result = new ExecuteResult<BankSettleDetailDTO>();
		//根据订单编号，获取订单支付时的对外交易号
		String outTradeNo = transations.getOutTradeNo();
		//如果子订单的对外交易号为0则此交易是通过父订单支付的，需要根据父订单获取对外交易号
		if("0".equals(transations.getOutTradeNo())){
			//根据父级订单号，获取对外交易号
			String orderParentNo = transations.getOrderParentNo();
			Transations parentTransations = transationsDAO.selectTransByOrderNo(orderParentNo);
			outTradeNo = parentTransations.getOutTradeNo();
		}
		//根据对外交易号查询第三方结算单导入信息
		BankSettleDetailDTO bankSettleDetailDTO = new BankSettleDetailDTO();
		bankSettleDetailDTO.setOutTradeNo(outTradeNo);
		List<BankSettleDetailDTO> bankSettleDetailList = statementExportService.findBankSettleDetail(bankSettleDetailDTO);
		if(bankSettleDetailList == null || bankSettleDetailList.size() == 0){
			result.addErrorMessage("平台尚未导入银行结算明细，暂缓退款，如有疑问，请联系平台。客服电话：4006 770 878 按3");
			return result;
		}
		result.setResult(bankSettleDetailList.get(0));
		return result ;
	}
	/**
	 *  根据退款单查询应退的佣金
	 *  1.不使用返点重新计算佣金，有可能返点发生变化，导致支付时的佣金和退款时的佣金不同
	 *  
	 * @param refundOrder
	 * @return
	 */
	private ExecuteResult<BigDecimal> getRefundCommissionByRefundOrder(RefundOrder refundOrder){
		ExecuteResult<BigDecimal> result = new ExecuteResult<BigDecimal>();
		//如果settlement为空则表示没有生成结算单，此时也需要退手续费
		BigDecimal totalCommission = new BigDecimal("0");// 佣金
		//根据退款商品ID获取商品返点
		Long itemId = refundOrder.getItemId();
		SettlementInfoInDTO settlementInfoInDTO = new SettlementInfoInDTO();
		settlementInfoInDTO.setItemId(itemId);
		ExecuteResult<ItemDTO> itemDTO = itemExportService.getItemById(itemId);
		if(itemDTO == null || itemDTO.getResult() == null){
			logger.error("无法获取商品信息：itemId："+itemId);
			return null;
		}
		settlementInfoInDTO.setCid(itemDTO.getResult().getCid());
		
		ExecuteResult<SettlementInfoOutDTO> settlementInfo = settleItemExpenseExportService.getSettlementInfo(settlementInfoInDTO);
		if(!settlementInfo.isSuccess()){
			logger.error("无法获取返点信息：itemId:"+itemId+";cid:"+itemDTO.getResult().getCid());
			return null;
		}
		//获取商品返点，如果商品没有返点则查询类目返点
		BigDecimal rebateRate = settlementInfo.getResult().getRebateRate();
		//需要精确计算需要计算佣金的商品价格
		//1、如果退款金额  小于退款商品数量*商品单价    则根据退款金额计算佣金  （如果存在满减或优惠，则退款金额可能会小）
		//2、如果退款金额大于退款商品数量*单价，则按商品数量*单价计算佣金（存在退运费的情况）
		//计算应退佣金，不能按退款金额计算，因为退款金额中可能含有运费，如果退款金额大于订单商品总金额，则按订单商品总金额金额计算佣金，否则按退款金额计算佣金
		TradeReturnGoodsDTO tradeReturnGoods = new TradeReturnGoodsDTO();
		tradeReturnGoods.setCodeNo(refundOrder.getReproNo());
		//退款商品的数量
//		int itemReturnCount =0;
		//退款金额中所包含的运费
		BigDecimal returnFreight = new BigDecimal("0.00");
		//查询退款商品个数，根据商品个数计算佣金
		List<TradeReturnGoodsDTO> tradeReturnGoodsList = tradeReturnGoodsService.searchByCondition(tradeReturnGoods);
		if(tradeReturnGoodsList != null && tradeReturnGoodsList.size() != 0){
//			TradeReturnGoodsDetailDTO tradeReturnGoodsDetailDTO = new TradeReturnGoodsDetailDTO();
			//获取退款运费
			if(tradeReturnGoodsList.get(0).getRefundFreight() != null){
				returnFreight = tradeReturnGoodsList.get(0).getRefundFreight();
			}
//			tradeReturnGoodsDetailDTO.setReturnGoodsId(tradeReturnGoodsList.get(0).getId());
//			List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList = tradeReturnGoodsDetailService.searchByCondition(tradeReturnGoodsDetailDTO);
//			if(tradeReturnGoodsDetailList != null && tradeReturnGoodsDetailList.size() != 0){
//				itemReturnCount = tradeReturnGoodsDetailList.get(0).getRerurnCount();
//			}
		}
		//查询订单明细表，查看此商品一共支付多少金额，用来计算佣金（查看商品实际支付金额）
		String orderId = refundOrder.getOrderNo();
		Long skuId = refundOrder.getSkuId();
		TradeOrderItems tradeOrderItems = new TradeOrderItems();
		tradeOrderItems.setOrderId(orderId);
		tradeOrderItems.setSkuId(skuId);
		//订单总支付金额，该商品总共支付了多少钱
		BigDecimal payPriceTotal = new BigDecimal("0.00");
		BigDecimal totalIntegralDiscount = new BigDecimal("0.00");
//		int num = 0;
		List<TradeOrderItems> tradeOrderItemsList = tradeOrderItemsDAO.queryList(tradeOrderItems, null);
		if(tradeOrderItemsList != null && tradeOrderItemsList.size() != 0){
			TradeOrderItems tradeOrderItem = tradeOrderItemsList.get(0);
			//获取该商品支付总金额,此金额不包含运费
			payPriceTotal = tradeOrderItem.getPayPriceTotal();//.add(tradeOrderItem.getIntegralDiscount()== null ? new BigDecimal("0.00") : tradeOrderItem.getIntegralDiscount());
			//获取商品支付时使用的积分兑换金额总额
			totalIntegralDiscount = totalIntegralDiscount.add(tradeOrderItem.getIntegralDiscount()== null ? new BigDecimal("0.00") : tradeOrderItem.getIntegralDiscount());
//			num = tradeOrderItem.getNum();
		}
		//获取商品应退款金额  = 申请退款金额 - 退款运费
		BigDecimal returnAmount = refundOrder.getRefundAmount().subtract(returnFreight);
		//通过比例计算退款金额应退的积分金额,退积分是按 （退款金额[不包含运费]） 和 （支付金额[不包含运费]） 的比例计算积分金额
		BigDecimal returnIntegralDiscount = returnAmount.multiply(totalIntegralDiscount).divide(payPriceTotal,5,BigDecimal.ROUND_HALF_UP);
//		BigDecimal returnIntegralDiscount = totalIntegralDiscount.multiply(new BigDecimal(itemReturnCount+"")).divide(new BigDecimal(num+""),5,BigDecimal.ROUND_HALF_UP);
		
//		BigDecimal itemTotalPrice = new BigDecimal("0.00");
//		itemTotalPrice = itemTotalPrice.add(payPriceTotal.multiply(new BigDecimal(itemReturnCount+"")).divide(new BigDecimal(num+""),5,BigDecimal.ROUND_HALF_UP));
		//平台应该退款金额=平台应退佣金金额=（买家要求退款金额-运费+应退积分金额）*商品返点
		totalCommission = totalCommission.add(refundOrder.getRefundAmount().subtract(returnFreight).add(returnIntegralDiscount).multiply(rebateRate).setScale(2, BigDecimal.ROUND_UP));
		
		result.setResult(totalCommission);
		return result;
	}
	/**
	 * 构建中信支付参数，佣金退款
	 * @param refundOrder  退款单  ：对外交易号和商品ID必填
	 * @param fromAccount  退款账号：此处为平台佣金账号   必填
	 * @return
	 */
	private PayReqParam buildCommissionPayReqParam(RefundOrder refundOrder,String fromAccount){
		PayReqParam payReqParam =new PayReqParam();
		payReqParam.setOutTradeNo(refundOrder.getOutTradeNo());
		
		ExecuteResult<BigDecimal> commissionResult = this.getRefundCommissionByRefundOrder(refundOrder);
		if(commissionResult == null){
			return null;
		}
		
		payReqParam.setTotalFee(commissionResult.getResult());
		
		payReqParam.setOutTradeNo(refundOrder.getOutTradeNo() + "2");// 重新定义对外交易号
		payReqParam.setFromAccount(fromAccount);// 平台佣金账户
		
		// 收款账户
		payReqParam.setToAccount(refundOrder.getBuyAccountNo());
		payReqParam.setSeller(refundOrder.getBuyAccountName()); 
		
		return payReqParam;
	}
	/**
	 * 构建中信退款支付
	 * 
	 * @param listSettleDetail - 结算详情单列表
	 * @param settlement - 结算单
	 * @param refundOrder - 退款信息
	 * @param factorage 应退手续费  
	 * @return 
	 */
	private ExecuteResult<PayReqParam> buildPayReqParam(SettlementDTO settlement,RefundOrder refundOrder,BigDecimal factorage) {
		
		ExecuteResult<PayReqParam> result = new ExecuteResult<PayReqParam>();
		
		PayReqParam payReqParam =new PayReqParam();
		payReqParam.setOutTradeNo(refundOrder.getOutTradeNo());
		// 交易金额
		BigDecimal totalFee =new BigDecimal("0");
		//卖家已确认结算，则卖家应退金额需要去除手续费和佣金
		if(SettleStatusEnum.Confirmed.getCode().equals(settlement.getStatus())){
			
			ExecuteResult<BigDecimal> commissionResult = this.getRefundCommissionByRefundOrder(refundOrder);
			payReqParam.setOutTradeNo(refundOrder.getOutTradeNo() + "1");// 重新定义对外交易号
			payReqParam.setFromAccount(settlement.getSellerCashAccount());// 卖家提现账户
			payReqParam.setExtraParam("卖家已结算资金卖家结算账户退款部分");
			// 实际转入退款账户的退款金额=退款总金额-佣金金额-手续费
			totalFee = refundOrder.getRefundAmount().subtract(commissionResult.getResult()).subtract(factorage);
		}else if(settlement.getStatus()<SettleStatusEnum.UnConfirmed.getCode()){
			if(PayMethodEnum.PAY_PERSONAL.getCode().equals(CommonUtil.getPaymentMethodByPayBank(refundOrder.getOrderPayBank()))){
				payReqParam.setFromAccount(settlement.getPlatformAccount());// 个人支付在线结算账户
				payReqParam.setExtraParam("未结算资金平台个人支付结算账户退款部分");
				totalFee=refundOrder.getRefundAmount().subtract(factorage);
			}else{
				payReqParam.setFromAccount(settlement.getSellerFrozenAccount());//  卖家冻结账户
				payReqParam.setExtraParam("平台已结算资金卖家冻结账户退款部分");
				totalFee=refundOrder.getRefundAmount();
			}
		}else{
			logger.error("当前结算单状态为：平台已结算卖家待确认，无法退款。结算单号："+settlement.getId());
			result.addErrorMessage("当前结算单状态为：平台已结算卖家待确认，无法退款");
			return result;
		}
		
		// 收款账户
		payReqParam.setToAccount(refundOrder.getBuyAccountNo());
		payReqParam.setSeller(refundOrder.getBuyAccountName()); 
		
		payReqParam.setTotalFee(totalFee);
		result.setResult(payReqParam);
		return result;
	}
	
	
	/**
	 * 申请退款是，处理退款业务，对结算单的影响
	 * 1、没有生成结算单，不需要考虑结算的影响，直接计算第三方手续费即可(手续费可以根据导入的excel计算，即便是没有结算单也是可以导入excel的)
	 * 2、如果已经生成结算单，则需要修改结算明细中的退款金额字段的值，表示此结算明细中需要退款的金额，用来计算应结算金额。应结算金额=订单金额-退款金额。
	 * 
	 * 首先修改结算明细中的退款金额字段，修改时应给结算明细添加备注信息，并且不修改结算单的状态
	 * 分析结算单的状态：
	 * 1、结算待出账：
	 * 	1.1  如果结算明细状态为：待支付   此时不做处理，当结算周期到达时，会将结算明细中的退款金额统计到结算单中的退款总金额字段
	 *  1.2 如果结算明细状态为：银行已确认，则此时需要修改结算明细中的退款字段
	 * 2、平台待结算：因为结算周期已经执行，所以需要修改结算单中的总退款金额字段，同时需要修改结算明细中的退款金额字段
	 * 3、卖家待结算：此状态不允许退款，不需要考虑
	 * 4、卖家已确认：因为结算已完成，所以不再修改结算单的内容
	 * 
	 * 
	 * @param factorage  应退手续费，不可为空
	 */
	private ExecuteResult<String> reBuildSettlement(RefundOrder refundOrder,SettlementDTO settlement,SettlementDetailDTO settlementDetailDTO,BigDecimal factorage){
		
		ExecuteResult<String> result = new ExecuteResult<String>();
		
		//卖家待确认状态不支持退款
		if(null!=settlement && SettleStatusEnum.UnConfirmed.getCode().equals(settlement.getStatus())){
			result.addErrorMessage("卖家尚未确认结算单，不支持退款，如有疑问，请联系平台。客服电话：4006 770 878 按3");
			return result;
		}
		//获取应退的佣金佣金
		ExecuteResult<BigDecimal> commissionResult = this.getRefundCommissionByRefundOrder(refundOrder);
		//计算手续费
		BigDecimal oldFactorage = new BigDecimal(0.00);
		if( null != settlementDetailDTO){
			oldFactorage = settlementDetailDTO.getFactorage() == null ? new BigDecimal(0.00) : settlementDetailDTO.getFactorage();
		}
		
		if(null!=settlement && SettleStatusEnum.UnBill.getCode().equals(settlement.getStatus())){
			//修改结算明细中的相应的字段
			//如果结算明细为银行已确认状态
			if(SettleDetailStatusEnum.PayAffirm.getCode().equals(settlementDetailDTO.getStatus())){
			
				//手续费
				settlementDetailDTO.setFactorage(oldFactorage.subtract(factorage));
				
			}
			if(commissionResult != null){
				//平台收入 =佣金   :为之前的佣金-应退的佣金
				BigDecimal commission = new BigDecimal("0.00");
				if(settlementDetailDTO.getCommission().compareTo(commissionResult.getResult()) > 0){
					commission = settlementDetailDTO.getCommission().subtract(commissionResult.getResult());
				}
				settlementDetailDTO.setPlatformIncome(commission);
				//佣金
				settlementDetailDTO.setCommission(commission);
			}
			
			//退款金额
			settlementDetailDTO.setRefundMoney(settlementDetailDTO.getRefundMoney().add(refundOrder.getRefundAmount()));
			
			//卖家收入 = 买家应结算金额 = 买家支付金额（订单金额）-退款金额-佣金-手续费
			settlementDetailDTO.setSellerIncome(settlementDetailDTO.getOrderPrice().subtract(settlementDetailDTO.getRefundMoney()).subtract(settlementDetailDTO.getCommission()).subtract(settlementDetailDTO.getFactorage()));
			//卖家支出 = 佣金+手续费
			settlementDetailDTO.setSellerExpenditure(settlementDetailDTO.getCommission().add(settlementDetailDTO.getFactorage()));
			//买家支出 = 订单金额-退款金额
			settlementDetailDTO.setBuyerExpenditure(settlementDetailDTO.getOrderPrice().subtract(refundOrder.getRefundAmount()));
			//卖家收款账户应收金额
			settlementDetailDTO.setSellerCashAccountIncome(settlementDetailDTO.getSellerIncome());
//			}else
		}else if(null!=settlement && SettleStatusEnum.UnSettle.getCode().equals(settlement.getStatus())){
			//修改结算明细中的字段
			//手续费
			settlementDetailDTO.setFactorage(oldFactorage.subtract(factorage));
			//获取应退的佣金佣金
			if(commissionResult != null){
				//平台收入 =佣金   :为之前的佣金-应退的佣金
				BigDecimal commission = new BigDecimal("0.00");
				if(settlementDetailDTO.getCommission().compareTo(commissionResult.getResult()) > 0){
					commission = settlementDetailDTO.getCommission().subtract(commissionResult.getResult());
				}
				settlementDetailDTO.setPlatformIncome(commission);
				//佣金
				settlementDetailDTO.setCommission(commission);
			}
			
			//退款金额
			settlementDetailDTO.setRefundMoney(settlementDetailDTO.getRefundMoney().add(refundOrder.getRefundAmount()));
			
			//卖家收入 = 卖家应结算金额 = 买家支付金额（订单金额）-退款金额-佣金-手续费
			settlementDetailDTO.setSellerIncome(settlementDetailDTO.getOrderPrice().subtract(refundOrder.getRefundAmount()).subtract(settlementDetailDTO.getCommission()).subtract(settlementDetailDTO.getFactorage()));
			//卖家支出 = 佣金+手续费
			settlementDetailDTO.setSellerExpenditure(settlementDetailDTO.getCommission().add(settlementDetailDTO.getFactorage()));
			//买家支出 = 订单金额-退款金额
			settlementDetailDTO.setBuyerExpenditure(settlementDetailDTO.getOrderPrice().subtract(refundOrder.getRefundAmount()));
			//卖家收款账户应收金额
			settlementDetailDTO.setSellerCashAccountIncome(settlementDetailDTO.getSellerIncome());
			
			
			//修改结算单中字段
			//佣金
			if(null!=settlement && settlement.getCommissionTotalMoney().compareTo(commissionResult.getResult()) <= 0){
				settlement.setCommissionTotalMoney(new BigDecimal("0.00"));
			}else{
				settlement.setCommissionTotalMoney(settlement.getCommissionTotalMoney().subtract(commissionResult.getResult()));
			}
			//应退金额
			settlement.setRefundTotalMoney(settlement.getRefundTotalMoney().add(refundOrder.getRefundAmount()));
			//平台总收入 = 佣金
			settlement.setPlatformIncome(settlement.getCommissionTotalMoney());
			//卖家总收入 = 订单金额-退款金额-佣金-手续费
			settlement.setSellerIncome(settlement.getOrderTotalMoney().subtract(refundOrder.getRefundAmount()).subtract(settlement.getCommissionTotalMoney()).subtract(settlementDetailDTO.getFactorage()));
			//卖家总支出 = 佣金+手续费
			settlement.setSellerExpenditure(settlement.getCommissionTotalMoney().add(settlementDetailDTO.getFactorage()));
			//结算总金额
			settlement.setSettlementTotalMoney(settlement.getSellerIncome());
			//平台需要支付给卖家的金额
			settlement.setPlatformNeedPay(settlement.getSellerIncome());
			int scount = statementDAO.update(settlement);
			if(scount == 0){
				result.addErrorMessage("修改结算单中的退款金额失败，结算单状态："+ SettleStatusEnum.UnSettle.getLabel());
				return result;
			}
		}
		
		if(null != settlementDetailDTO){
			int count = statementDetailDAO.update(settlementDetailDTO);
			if(count == 0){
				result.addErrorMessage("修改结算明细中的退款金额失败，结算单状态："+ SettleStatusEnum.UnBill.getLabel());
				return result;
			}
		}
		
		return result;
	}
	/**
	 * 退佣金方法
	 * 
	 * @param refundOrder  退货单
	 */
	@SuppressWarnings("unused")
	private ExecuteResult<String> refundCommission(RefundOrder refundOrder,SettlementDTO settlement,SettlementDetailDTO settlementDetailDTO ,boolean isReturnCommission){
		
		ExecuteResult<String> result = new ExecuteResult<String>();
		
		//不需要退佣金，默认佣金退款成功
		if(!isReturnCommission){
			refundOrder.setStatus(TradeRefundStatusEnum.success.getCode());// 成功
			return result;
		}
		//如果需要退佣金，判断当前结算单的状态是否已经产生了佣金
		if(!SettleStatusEnum.Confirmed.getCode().equals(settlement.getStatus())){
			//不需要退佣金，默认佣金退款成功
			refundOrder.setStatus(TradeRefundStatusEnum.success.getCode());// 成功
			return result;
		}
		
		FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
		
		PayReqParam payComParam = this.buildCommissionPayReqParam(refundOrder, financeAccountInfoDto.getCommissionAccountNumber());
		payComParam.setExtraParam("已结算资金平台佣金账户退款佣金部分");
		
		// 校验佣金账户余额是否足以支付
		boolean isFactEnough = citicService.querySubBalanceForPay(payComParam.getTotalFee(), payComParam.getFromAccount());
		if(!isFactEnough){
			logger.error("佣金账户余额不足，退款失败。:账号："+payComParam.getFromAccount());
			result.addErrorMessage("佣金账户余额不足，手续费退款失败，如有疑问，请联系平台。客服电话：4006 770 878 按3");
			return result;
		}
		
		result = citicService.transferAffiliated(payComParam, "0", CiticPayTypeCode.refund);
		if(result.isSuccess()){
			refundOrder.setStatus(TradeRefundStatusEnum.success.getCode());// 成功
		}else{
			refundOrder.setStatus(TradeRefundStatusEnum.partSuccess.getCode());// 不成功
			logger.error("佣金退款失败:账号"+payComParam.getFromAccount());
			result.addErrorMessage("佣金退款失败，请稍后再试。如有疑问，请联系平台。客服电话：4006 770 878 按3");
		}
		return result;
	}
	
	/**
	 * 退手续费方法
	 * @param tradeOrdersDTO  订单
	 * @param refundOrder  退货单
	 * @param factorage  应退手续费
	 * @return
	 */
	private ExecuteResult<String> refundFactorage(RefundOrder refundOrder,BigDecimal factorage){
		
		ExecuteResult<String> result = new ExecuteResult<String>();
		
		FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
		ExecuteResult<PayReqParam> payReqParamResult = this.buildFactoragePayReqParam(refundOrder, financeAccountInfoDto.getPoundageFillAccountNumber(),factorage);
		if(!payReqParamResult.isSuccess()){
			result.setErrorMessages(payReqParamResult.getErrorMessages());
			return result;
		}
		PayReqParam payFactParam = payReqParamResult.getResult();
		payFactParam.setExtraParam("已结算资金平台手续费补足账户退款手续费部分");
		// 校验手续费补足账户余额是否足以支付
		boolean isFactEnough = citicService.querySubBalanceForPay(payFactParam.getTotalFee(), payFactParam.getFromAccount());
		if(!isFactEnough){
			logger.error("手续费补足账户余额不足，退款失败。:账号："+payFactParam.getFromAccount());
			result.addErrorMessage("手续费补足账户余额不足，手续费退款失败，如有疑问，请联系平台。客服电话：4006 770 878 按3");
			return result;
		}
		result=citicService.transferAffiliated(payFactParam, "0", CiticPayTypeCode.refund);
		if(result.isSuccess()){
			refundOrder.setStatus(TradeRefundStatusEnum.partSuccess.getCode());// 成功
			
			//添加手续费流水
			FactorageJournal factorageJournal = new FactorageJournal();
			factorageJournal.setOrderNo(refundOrder.getOrderNo());
			factorageJournal.setReproNo(refundOrder.getReproNo());
			factorageJournal.setFactorage(factorage);
			factorageJournal.setStatus(FactorageStatus.RefundSuccess.getFacCode());
			factorageJournal.setRemark1("应退手续费为："+factorage);
			factorageJournal.setCreated(new Date());
			factorageJournalDAO.add(factorageJournal);
			
		}else{
			refundOrder.setStatus(TradeRefundStatusEnum.part.getCode());// 不成功
			logger.error("手续费退款失败:账号"+payFactParam.getFromAccount());
			result.addErrorMessage("手续费退款失败，请稍后再试。如有疑问，请联系平台。客服电话：4006 770 878 按3");
		}
		return result;
	}
	@Override
	public ExecuteResult<RefundPayParam> selectRefundByRePro(String rePro) {
		logger.info("\n 方法[{}]，入参：[{}]","TradeRefundServiceImpl-selectRefundByRePro",rePro);
		ExecuteResult<RefundPayParam> result = new ExecuteResult<RefundPayParam>();
		RefundOrder refundOrder = refundOrderDAO.selectRefundByRePro(rePro);
		result.setResult(this.buildRefundPayParam(refundOrder));
		logger.info("\n 方法[{}]，出参：[{}]","TradeRefundServiceImpl-selectRefundByRePro",JSONObject.toJSONString(result));
		return result;
	}
	@Override
	public BigDecimal getTotalRefundAmountByOrderNo(String orderNo) {
		RefundOrder refundOrderCon = new RefundOrder();
		refundOrderCon.setOrderNo(orderNo);
		List<RefundOrder> listQuery = refundOrderDAO.queryList(refundOrderCon, null);
		BigDecimal totalRefundAmount = new BigDecimal(0.00);
		for(RefundOrder refundOrder : listQuery){
			totalRefundAmount = totalRefundAmount.add(refundOrder.getRefundAmount());
		}
		return totalRefundAmount;
	}
	public static void main(String[] args) {
		BigDecimal a = new  BigDecimal("1.00");
		BigDecimal b = new  BigDecimal("2.00");
		System.out.println(a.compareTo(b));
	}
}

