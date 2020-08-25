package com.camelot.settlecenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.accountinfo.dao.FinanceAccountDAO;
import com.camelot.aftersale.service.TradeRefundService;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.common.enums.SettleEnum.SettleDetailTypeEnum;
import com.camelot.common.enums.SettleEnum.SettleStatusEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.LockTableDAO;
import com.camelot.payment.dao.BankSettleDetailDAO;
import com.camelot.payment.dao.RefundOrderDAO;
import com.camelot.payment.dao.TransationsDAO;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.service.CiticService;
import com.camelot.settlecenter.dao.StatementCombinDAO;
import com.camelot.settlecenter.dao.StatementDAO;
import com.camelot.settlecenter.dao.StatementDetailDAO;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.dto.SettlementInfoOutDTO;
import com.camelot.settlecenter.dto.combin.SettlementCombinDTO;
import com.camelot.settlecenter.dto.indto.SettlementInDTO;
import com.camelot.settlecenter.dto.indto.SettlementInfoInDTO;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.settlecenter.service.util.SettleUtil;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExtendsService;

@Service("statementExportService")
public class StatementExportServiceImpl extends SettleUtil implements StatementExportService{
	private final static Logger logger = LoggerFactory.getLogger(StatementExportServiceImpl.class);

	@Resource
	private StatementCombinDAO statementCombinDAO;
	@Resource
	private StatementDetailDAO statementDetailDAO;
	@Resource
	private StatementDAO statementDAO;
	@Resource
	private LockTableDAO lockTableDAO;
	@Resource
	private TransationsDAO  transationsDAO;
	@Resource
	private BankSettleDetailDAO bankSettleDetailDAO;
	@Resource
	private FinanceAccountDAO financeAccountDAO;
	
	@Resource(name="citicGateService")
	private CiticService citicService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private TradeRefundService tradeRefundService;
	@Resource
	private RefundOrderDAO refundOrderDAO;
	
	public ExecuteResult<String> createSettleDetail(TradeOrdersDTO tradeOrder,List<RefundOrder> refundOrderList) throws Exception{
		
		// 1.校验必填参数是否为空
		ExecuteResult<String> result= this.verifyCreateDetail(tradeOrder);
		if(!result.isSuccess()){
			return result;
		}
		//校验是否符合生成结算单的条件
		//线下支付不生成结算单、退款冲抵账号下的订单（平台的特殊账号，订单号以“R”开头）不生成结算单
		//判断当前订单是否确认收货以及是否付款，针对于延期付款的订单，如果买家确认收货的时候还未付款，那么不生成结算单
		if (tradeOrder.getPaid() == 1 || tradeOrder.getState() < 2 || tradeOrder.getPaymentType() == PayBankEnum.OFFLINE.getQrCode() || tradeOrder.getOrderId().startsWith("R")) {
			logger.error("没有生成结算单；原因：付款状态：" + tradeOrder.getPaid() + "；订单状态：" + tradeOrder.getState() + "；订单编号：" + tradeOrder.getOrderId() + "；付款方式：" + tradeOrder.getPaymentType());
			return result;
		}
		ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(tradeOrder.getSellerId());
		if(userInfo!=null&&userInfo.getResult()!=null&&userInfo.getResult().getUserCiticDTO()!=null&&
				userInfo.getResult().getUserBusinessDTO()!=null){
			String sellerFrozenAcc =userInfo.getResult().getUserCiticDTO().getSellerFrozenAccount();
			String sellerWithdrawsCashAcc =userInfo.getResult().getUserCiticDTO().getSellerWithdrawsCashAccount();
			String companyName= userInfo.getResult().getUserBusinessDTO().getCompanyName();
//			if(StringUtils.isNotBlank(sellerFrozenAcc)&&StringUtils.isNotBlank(sellerWithdrawsCashAcc)&&StringUtils.isNotBlank(companyName)){
				
				//获取退货单
//				Integer[] states = {TradeReturnStatusEnum.SUCCESS.getCode(),TradeReturnStatusEnum.REFUNDING.getCode(),TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),TradeReturnStatusEnum.PLATFORMDEALING.getCode(),};
//				List<RefundOrder> refundOrderList = refundOrderDAO.selectRefundOrderByOrderIdAndRefundGoodsStatus(tradeOrder.getOrderId(), states);//.searchByOrderIdAndState(tradeOrder.getOrderId(),states);
				//每一个订单都生成一个结算单
				FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
				SettlementDTO settlementDTO=this.buildSettlement(tradeOrder,sellerFrozenAcc,sellerWithdrawsCashAcc,companyName,financeAccountInfoDto,refundOrderList);
				statementDAO.add(settlementDTO);
				statementDetailDAO.add(this.buildSettlementDetailDTO(tradeOrder, settlementDTO.getId(),refundOrderList));
			/*}else{
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"当前用户不存在冻结账户/收款账户/公司名称"));
			}*/
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"当前用户不存在"));
		}
		return result;
	}
	
	public synchronized ExecuteResult<String> proceedSettle(Long settlementId){
		ExecuteResult<String> result= new ExecuteResult<String>();
		SettlementDTO settlementDTO=statementDAO.queryById(settlementId);
		if(settlementDTO==null){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"结算单不存在"));
		}else if(SettleStatusEnum.UnSettle.getCode()==settlementDTO.getStatus()){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"该结算单尚未结算，待平台运维人员结算"));
		}else if(SettleStatusEnum.UnBill.getCode()==settlementDTO.getStatus()){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"该结算单尚未出账，不能进行结算"));
		}else if(SettleStatusEnum.Confirmed.getCode()==settlementDTO.getStatus()){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"该结算单已结算并卖家确认完成"));
		}
		
		if(result.isSuccess()){
			//暂时先用代码块实现代码同步
//			this.lock();// 锁表
			try {
				FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
				//根据结算单ID获取结算明细的状态
				SettlementDetailDTO settlementDetailDTO = new SettlementDetailDTO ();
				settlementDetailDTO.setSettlementId(settlementId);
				List<SettlementDetailDTO> settlementDetailList = statementDetailDAO.queryList(settlementDetailDTO, null);
				//一个结算单对应一个结算明细
				settlementDetailDTO = settlementDetailList.get(0);
				long status = settlementDetailDTO.getStatus();
				//结算明细的状态为 -1 ：冻结，0：未支付，1：确认银行已结算，2：已支付待付佣金，3支付完成
				//正常情况此处不会出新-1，0，3这三种情况
				//如果状态为1，则需要结算卖家结算金额和佣金金额
				//如果状态为2，则只需要结算佣金金额
				if(status == 1){
					// 卖家中信冻结账户转入卖家中信收款账户
					PayReqParam payDetail = this.buildPayReqParam(settlementDTO,false,financeAccountInfoDto);
					ExecuteResult<String>  resultDetail = citicService.transferAffiliated(payDetail,settlementDTO.getPaymentId(),CiticPayTypeCode.settle);
					if(!resultDetail.isSuccess()){
						logger.error(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"结算失败-"+resultDetail.getErrorMessages().get(0)));
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"结算失败-"+resultDetail.getErrorMessages().get(0)));
						return result;
					}
				}
				// 佣金扣除
				PayReqParam payCommission = this.buildPayReqParam(settlementDTO,true,financeAccountInfoDto);
				ExecuteResult<String>  resultCommission = citicService.transferAffiliated(payCommission,settlementDTO.getPaymentId(),CiticPayTypeCode.settle);
				
				SettlementDetailDTO settleDetailUpdate = new SettlementDetailDTO();
				settleDetailUpdate.setSettlementId(settlementId);
				if(resultCommission.isSuccess()){
					settleDetailUpdate.setStatus(SettleDetailStatusEnum.PayFinish.getCode());// 佣金已抽取   
					settlementDTO.setStatus(SettleStatusEnum.Confirmed.getCode());
					result.setResultMessage("处理成功");
				}else{
					settleDetailUpdate.setStatus(SettleDetailStatusEnum.PaySuccess.getCode());// 佣金未抽取         
					//现在结算单的状态就是卖家待确认，所以不需要修改结算单的状态，等待卖家再次确认结算，抽取佣金
					result.setResultMessage("处理成功，但佣金未抽取，请再次结算");
				}
				statementDetailDAO.updateStatuBySettleId(settleDetailUpdate);
				statementDAO.update(settlementDTO);
				
			} catch (Exception e) {
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Custom,e.getMessage()));
				logger.info("\n 方法[{}]，异常：[{}]","StatementExportServiceImpl-proceedSettlement",e.getMessage());
				throw new RuntimeException(e);
			} 
		}
		return result;
	}
	
	@Override
	public ExecuteResult<String> freezeSettleDetail(String orderId) throws Exception{
		ExecuteResult<String> result= new ExecuteResult<String>();
		if(orderId==null){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"无效订单号"));
			return result;
		}
		
		// 查询当前子订单下的所有结算明细
		SettlementDetailDTO settleDetailOrderId =new SettlementDetailDTO();
		settleDetailOrderId.setOrderId(orderId);
		List<SettlementDetailDTO> listSettleDetail = statementDetailDAO.queryList(settleDetailOrderId, null);
		if(listSettleDetail.size()==0){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"结算详情单中未查询到该订单信息"));
			return result;
		}
		
		// 当前子订单号拥有唯一一个结算单号
		SettlementDTO settlement=statementDAO.queryById(listSettleDetail.get(0).getSettlementId());
		if(settlement.getStatus()>SettleStatusEnum.UnSettle.getCode()){ 
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"该订单结算中，无法冻结"));
		}else{
			SettlementDetailDTO settlementDetailDTO = new SettlementDetailDTO();
			settlementDetailDTO.setOrderId(orderId);
			settlementDetailDTO.setStatus(SettleDetailStatusEnum.FreezePaying.getCode());
			statementDetailDAO.updateByOrderId(settlementDetailDTO);
			// 结算单已出账，重新结算处理，否则不做处理。
			if(settlement.getStatus()==SettleStatusEnum.UnSettle.getCode()){
				BigDecimal platformIncome=new BigDecimal("0");// 平台总收入
				BigDecimal platformExpenditure=new BigDecimal("0"); // 平台总支出
				BigDecimal sellerIncome=new BigDecimal("0");// 卖家总收入
				BigDecimal sellerExpenditure=new BigDecimal("0");// 卖家总支出
				BigDecimal orderTotalMoney=new BigDecimal("0");// 卖家总支出
				BigDecimal commissionTotalMoney=new BigDecimal("0");// 佣金总额
				BigDecimal settlementTotalMoney=new BigDecimal("0"); // 结算总金额
				BigDecimal refundTotalMoney=new BigDecimal("0"); // 退款总金额
				BigDecimal platformNeedPay=new BigDecimal("0"); // 平台需要支付给卖家的金额
				
				// 获取当前结算单下所有结算子单
				SettlementDetailDTO detailSettlementId =new SettlementDetailDTO();
				detailSettlementId.setSettlementId(settlement.getId());
				List<SettlementDetailDTO> listReSettle = statementDetailDAO.queryList(detailSettlementId, null);
				
				for (SettlementDetailDTO settleDetail2 :listReSettle) {
					// 校验订单是否已支付，可能存在货到付款但未支付的订单
					ExecuteResult<TradeOrdersDTO> tradeOrderResult = tradeOrderExportService.getOrderById(settleDetail2.getOrderId());
					if(tradeOrderResult.isSuccess()&&tradeOrderResult.getResult().getPaid()==2&&// 订单是否支付标记  1未支付 2已支付
							settleDetail2.getStatus() == SettleDetailStatusEnum.PayAffirm.getCode()){ 
						platformIncome=platformIncome.add(settleDetail2.getPlatformIncome());
						platformExpenditure=platformExpenditure.add(settleDetail2.getPlatformExpenditure());
						sellerIncome=sellerIncome.add(settleDetail2.getSellerIncome());
						sellerExpenditure=sellerExpenditure.add(settleDetail2.getSellerExpenditure());
						orderTotalMoney=orderTotalMoney.add(settleDetail2.getOrderPrice());
						commissionTotalMoney=commissionTotalMoney.add(settleDetail2.getCommission());
						refundTotalMoney=refundTotalMoney.add(settleDetail2.getRefundMoney());
						platformNeedPay=platformNeedPay.add(settleDetail2.getSellerCashAccountIncome());
					}else if(settleDetail2.getStatus() == SettleDetailStatusEnum.UnPaying.getCode()){
						settleDetail2.setRemark("原结算单号："+settleDetail2.getSettlementId()+"；变更记录：");
						settleDetail2.setSettlementId(0l);
						this.modifySettleDetailById(settleDetail2);
					}
				}
				
				settlementTotalMoney=platformNeedPay;
				
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
				statementDAO.update(settlement);
			}
			result.setResultMessage("冻结成功");
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public ExecuteResult<DataGrid<SettlementCombinDTO>> querySettlementList(SettlementInDTO settlementInDTO,Pager page) {
		
		ExecuteResult<DataGrid<SettlementCombinDTO>> result=new ExecuteResult<DataGrid<SettlementCombinDTO>>();
		
		try {
			DataGrid<SettlementCombinDTO> dataGrid=new DataGrid<SettlementCombinDTO>();
			List<SettlementCombinDTO> list=statementCombinDAO.querySettlementList(settlementInDTO,page);
			Long count=statementCombinDAO.querySettlementListCount(settlementInDTO);
			//循环 查询结算单详情 塞入 SettlementCombinDTO中
			SettlementDetailDTO sdDTO=new SettlementDetailDTO();
			Pager pager=new Pager();
			pager.setPage(1);
			pager.setRows(Integer.MAX_VALUE);
			for (SettlementCombinDTO scDTO:list) {
				sdDTO.setSettlementId(scDTO.getSettlement().getId());
				List<SettlementDetailDTO>  sdList=statementDetailDAO.queryList(sdDTO, pager);
				BigDecimal platformIncome =new BigDecimal("0") ;
				BigDecimal platformExpenditure=new BigDecimal("0");
				BigDecimal sellerIncome=new BigDecimal("0");
				BigDecimal sellerExpenditure=new BigDecimal("0");
				for (SettlementDetailDTO settlementDetailDTO : sdList) {
					platformIncome=platformIncome.add(settlementDetailDTO.getPlatformIncome());
					platformExpenditure=platformExpenditure.add(settlementDetailDTO.getPlatformExpenditure());
					sellerIncome=sellerIncome.add(settlementDetailDTO.getSellerIncome());
					sellerExpenditure=sellerExpenditure.add(settlementDetailDTO.getSellerExpenditure());
				}
				
				scDTO.getSettlement().setPlatformIncome(platformIncome);
				scDTO.getSettlement().setPlatformExpenditure(platformExpenditure);
				scDTO.getSettlement().setSellerIncome(sellerIncome);
				scDTO.getSettlement().setSellerExpenditure(sellerExpenditure);
				scDTO.getSettlement().setOrderTotalMoney(platformIncome.add(sellerIncome));
				scDTO.getSettlement().setCommissionTotalMoney(platformIncome);
				scDTO.getSettlement().setSettlementTotalMoney(sellerIncome);
				scDTO.setSettlementDetailList(sdList);
			}
			dataGrid.setRows(list);
			dataGrid.setTotal(count);
			result.setResult(dataGrid);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifySettlementStates(Long... settlementIds) {
		
		ExecuteResult<String> result=new ExecuteResult<String>();
		int successCount=0; 
		StringBuffer resultMsgSuccess=new StringBuffer("已结算单号：");
		StringBuffer resultMsgError=new StringBuffer("未结算单号：");
		for (int i = 0; i < settlementIds.length; i++) {
			 SettlementDTO settlementDTO=statementDAO.queryById(settlementIds[i]);
			 if(SettleStatusEnum.UnSettle.getCode()==settlementDTO.getStatus()){ // 待结算的进行结算，将企业支付的结算单过滤掉
				 boolean flag=false;
				// 查询所有网银在线结算的结算详情单--->卖家冻结账户
				SettlementDetailDTO settleDetailCondition= new SettlementDetailDTO();
				settleDetailCondition.setSettlementId(settlementIds[i]);
				settleDetailCondition.setType(SettleDetailTypeEnum.Settle.getCode());// 1->第三方待结算,2->第三方已结算      
				//需要结算的结算单只有个人支付的订单，企业支付的订单生成的结算单都是“已结算待确认”状态，在上一个if判断中就已经过滤掉了
				//settleDetailCondition.setPaymentMethod(PayBankEnum.CB.getQrCode());
				//每一个结算单下面只有一个结算明细，所以获取到的结算明细列表的size=1
				List<SettlementDetailDTO> listSettleDetail = statementDetailDAO.queryList(settleDetailCondition, null);
				
				if(listSettleDetail!=null&&listSettleDetail.size()>0){
					try {
						FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
						PayReqParam payReqParam = this.buildCbPayReqParam(listSettleDetail, settlementDTO,financeAccountInfoDto);
						if(payReqParam!=null){
							//判断账户余额，如果余额不足，则返回
							boolean isEnough = citicService.querySubBalanceForPay(payReqParam.getTotalFee(), payReqParam.getFromAccount());
							if(!isEnough){
								logger.error("账户余额不足，平台结算失败。:账号："+payReqParam.getFromAccount());
								resultMsgError.append(settlementIds[i]+"[异常结算,账户余额不足，平台结算失败!],");
								flag = true;
							}
							ExecuteResult<String> resultCb = citicService.transferAffiliated(payReqParam,settlementDTO.getPaymentId(),CiticPayTypeCode.settle);
							if(resultCb.isSuccess()){
								flag=true;
							}else{
								resultMsgError.append(settlementIds[i]+"[异常结算,"+resultCb.getErrorMessages().get(0)+"],");
							}
						}else{
							resultMsgError.append(settlementIds[i]+"[异常结算,存在未确认结算明细],");
						}
					} catch (Exception e) {
						resultMsgError.append(settlementIds[i]+"[异常结算,支付异常],");
					}
				}
				 
				if(flag){
					SettlementDTO settle = new SettlementDTO();
					settle.setId(settlementIds[i]);
					settle.setStatus(SettleStatusEnum.UnConfirmed.getCode());
					settle.setHavePaidDate(new Date());
					statementDAO.update(settle);
					// 结算完成进入确认支付环节
					SettlementDetailDTO settleDetailUpdate= new SettlementDetailDTO();
					settleDetailUpdate.setSettlementId(settlementIds[i]);
					settleDetailUpdate.setType(SettleDetailTypeEnum.Pay.getCode());
					statementDetailDAO.updateStatuBySettleId(settleDetailUpdate);
					successCount++;
					resultMsgSuccess.append(settlementIds[i]+",");
				}
			 }else{
				 resultMsgError.append(settlementIds[i]+"[不可结算],");
			 }
		}
		
		if(successCount>0){
			result.setResultMessage(resultMsgSuccess.substring(0,resultMsgSuccess.length()-1)+"；"+resultMsgError.substring(0,resultMsgError.length()-1));
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"结算失败:"+resultMsgError.substring(0,resultMsgError.length()-1)));
		}
		return result;
	}

	@Override
    public ExecuteResult<String> saveBankSettle(List<BankSettleDetailDTO> listBankSettleDetailDTO) {
        ExecuteResult<String> result = new ExecuteResult<String>();
    	List<String> verifyRepetition = new ArrayList<String>();
    	
    	List<BankSettleDetailDTO> listBankSettleDetailResult =new ArrayList<BankSettleDetailDTO>();
    	for (BankSettleDetailDTO bankSettleDetailDTO:listBankSettleDetailDTO) {
    		// 校验单号重复性
    		if(!verifyRepetition.contains(bankSettleDetailDTO.getOutTradeNo())){
    			 verifyRepetition.add(bankSettleDetailDTO.getOutTradeNo());
			 }else{
//				 throw new CommonCoreException("存在重复单号："+bankSettleDetailDTO.getOutTradeNo());
				 result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error,"订单号："+bankSettleDetailDTO.getOutTradeNo()+"已存在"));
				 return result;
			 }
    		 
    		// 校验异常
			if(bankSettleDetailDTO.getOrderStatus()==2&&bankSettleDetailDTO.getLiquidateStatus()==2){
				Transations transations =transationsDAO.selectTransByOutTrade(bankSettleDetailDTO.getOutTradeNo());
				if(transations==null){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"订单号："+bankSettleDetailDTO.getOutTradeNo()+"不存在"));
					return result;
				}
				int compare=transations.getOrderAmount().compareTo(bankSettleDetailDTO.getOrderAmount());
				if(compare!=0){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"订单号："+bankSettleDetailDTO.getOutTradeNo()+"金额不一致"));
					return result;
				}
			}else{
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Custom,"订单号："+bankSettleDetailDTO.getOutTradeNo()+"状态异常"));
				return result;
			}
			
			// 刨除重复导入数据
			BankSettleDetailDTO detail=new BankSettleDetailDTO();
			detail.setOutTradeNo(bankSettleDetailDTO.getOutTradeNo());
			long count=bankSettleDetailDAO.queryCount(detail);
			if(count==0){
				listBankSettleDetailResult.add(bankSettleDetailDTO);
			}
		}
        if(listBankSettleDetailResult.size()>0){
        	bankSettleDetailDAO.adds(listBankSettleDetailResult);
        }	
        
        logger.info("\n 方法[{}]，出参：[{}]","PaymentExportServiceImpl-saveBankSettle",JSONObject.toJSONString(result));
        return result;
    } 
	 
	@Override
	public DataGrid<BankSettleDetailDTO> findBankSettle(BankSettleDetailDTO bankSettleDetailDTO, @SuppressWarnings("rawtypes") Pager pager) {
		DataGrid<BankSettleDetailDTO> result =new DataGrid<BankSettleDetailDTO>();
		long count=bankSettleDetailDAO.queryCount(bankSettleDetailDTO);
		if(count>0){
			List<BankSettleDetailDTO> list = bankSettleDetailDAO.queryList(bankSettleDetailDTO, pager);
			result.setRows(list);
		}
		result.setTotal(count);
		return result;
	}
	
	@Override
	public int modifySettleDetailByOrderId(String orderId,SettleDetailStatusEnum settleDetailStatusEnum,BigDecimal factorage){
		int successCount=0;
		SettlementDetailDTO settleDetailOrderId =new SettlementDetailDTO();
		settleDetailOrderId.setOrderId(orderId);
		List<SettlementDetailDTO> listSettleDetail = statementDetailDAO.queryList(settleDetailOrderId, null);
		for (SettlementDetailDTO settleDetailDTO:listSettleDetail) {
			SettlementDetailDTO settleDetail = new SettlementDetailDTO();
			settleDetail.setOrderId(orderId);
			settleDetail.setFactorage(factorage);
			settleDetail.setSellerCashAccountIncome(settleDetailDTO.getOrderPrice().subtract(settleDetailDTO.getCommission()).subtract(settleDetailDTO.getRefundMoney()).subtract(factorage));
			settleDetail.setSellerIncome(settleDetail.getSellerCashAccountIncome());
			settleDetail.setSellerExpenditure(settleDetailDTO.getCommission().add(factorage));
			settleDetail.setStatus(settleDetailStatusEnum.getCode());
			int count =statementDetailDAO.updateByOrderId(settleDetail);
			
			if(count>0){
				successCount++;
			}
		}
		return successCount;
	}
	
	@Override
	public List<SettlementDTO> findSettleByStatus(SettleStatusEnum status) {
		List<SettlementDTO> listSettle=statementDAO.selectSettleByStatus(status.getCode());
		return listSettle;
	}
	
	@Override
	public List<SettlementDTO> selectSettleForSellerSettlement(SettleStatusEnum status) {
		List<SettlementDTO> listSettle=statementDAO.selectSettleForSellerSettlement(status.getCode());
		return listSettle;
	}

	@Override
	public int modifyStatement(SettlementDTO settlement) {
		return statementDAO.update(settlement);
	}

	@Override
	public List<SettlementDetailDTO> findSettlementDetail(SettlementDetailDTO settleDetailCondition) {
		return statementDetailDAO.queryList(settleDetailCondition, null);
	}

	@Override
	public int modifySettleDetailById(SettlementDetailDTO settlementDetailDTO) {
		return statementDetailDAO.update(settlementDetailDTO);
	}
	
	@Override
	public SettlementDTO findSettleByPeriod(Long shopId) {
		return statementDAO.selectSettleByPeriod(shopId);
	}
	
	@Override
	public List<BankSettleDetailDTO> findBankSettleDetail(BankSettleDetailDTO bankSettleDetailDTO) {
		return bankSettleDetailDAO.queryList(bankSettleDetailDTO,null);
	}
	
	@Override
	public int dealBankSettleDetail(String outTradeNos) {
		return bankSettleDetailDAO.updateDealByOutTradeNo(outTradeNos);
	}
	
	@Override
	public String buildPaymentId(String payType){
		return transationsDAO.selectOutTranNo(payType);
	}
	@Override
	public void addSettlement(SettlementDTO settlementDTO){
		statementDAO.add(settlementDTO);
	}
	/**
	 * 构建结算单详情
	 * 
	 * @param tradeOrder - 订单信息
	 * @param settlementId - 结算单编号
	 * @return
	 */
	private SettlementDetailDTO buildSettlementDetailDTO(TradeOrdersDTO tradeOrder,Long settlementId,List<RefundOrder> refundOrderList) {
		BigDecimal totleCommission = new BigDecimal("0.00"); // 订单总佣金
		BigDecimal totlePayMoney = new BigDecimal("0.00");// 订单总结算金额
		//计算退款总金额
		BigDecimal totalRefundAmount = new BigDecimal(0.00);
		
		for (TradeOrderItemsDTO tradeOrderItemsDTO:tradeOrder.getItems()) {
			BigDecimal itemTotlePrice= tradeOrderItemsDTO.getPayPriceTotal(); // 每一个商品单价*数量
			BigDecimal integralDiscount = tradeOrderItemsDTO.getIntegralDiscount();
			//如果这个商品使用了积分兑换，则需要使用总商品计算佣金
			if(integralDiscount != null){
				itemTotlePrice = itemTotlePrice.add(integralDiscount);
			}
			// 获取单个商品的费率
			SettlementInfoInDTO settlementInfoInDTO=new SettlementInfoInDTO();
		    settlementInfoInDTO.setCid(tradeOrderItemsDTO.getCid());
		    settlementInfoInDTO.setItemId(tradeOrderItemsDTO.getItemId());
		    ExecuteResult<SettlementInfoOutDTO> resultSettleCats = settleItemExpenseExportService.getSettlementInfo(settlementInfoInDTO);
		    BigDecimal rebateRate=resultSettleCats.getResult().getRebateRate();// 费率
			//如果该商品存在退款，需要计算退款金额后的金额
			for(RefundOrder refundOrder : refundOrderList){
				
				if(tradeOrderItemsDTO.getSkuId().equals(refundOrder.getSkuId())){
					//退款金额的手续费
					totalRefundAmount = totalRefundAmount.add(refundOrder.getRefundAmount());
					//退款后的商品金额,如果退款中包含运费，则默认商品金额为0
					if(itemTotlePrice.compareTo(refundOrder.getRefundAmount()) <= 0){
						itemTotlePrice = new BigDecimal("0.00");
					}else{
						itemTotlePrice = itemTotlePrice.subtract(refundOrder.getRefundAmount());
					}
				}
			}
			//计算退款后的佣金
			totleCommission=totleCommission.add(itemTotlePrice.multiply(rebateRate).setScale(2,BigDecimal.ROUND_UP));
		}
		totlePayMoney = tradeOrder.getPaymentPrice().subtract(totalRefundAmount).subtract(totleCommission);//卖家应结算金额应减去退款金额,此处不考虑手续费，分摊手续费的时候再考虑手续费
		
		SettlementDetailDTO settlementDetail = new SettlementDetailDTO();
		settlementDetail.setSellerId(tradeOrder.getSellerId());
		settlementDetail.setShopId(tradeOrder.getShopId());
		settlementDetail.setSettlementId(settlementId);
		settlementDetail.setOrderId(tradeOrder.getOrderId());
		settlementDetail.setOrderPrice(tradeOrder.getPaymentPrice());// 订单金额
		
		settlementDetail.setFactorage(new BigDecimal("0"));// 第三方手续费 初始化不计算	，在分摊手续费是查看手续费记录表，看有没有退款的手续费
		
		settlementDetail.setRefundMoney(totalRefundAmount);// 退款金额
		settlementDetail.setPlatformIncome(totleCommission);//  平台收入
		settlementDetail.setPlatformExpenditure(new BigDecimal("0"));//  平台支出
		settlementDetail.setSellerIncome(totlePayMoney);//  卖家收入=订单总金额-退款金额 - 佣金金额-第三方手续费=卖家提现账号应收  (此时不知道手续费，所以需要在分摊手续时在除去手续费)
		settlementDetail.setSellerExpenditure(totleCommission.add(settlementDetail.getRefundMoney()));//  卖家支出
		settlementDetail.setBuyerIncome(new BigDecimal("0"));//  买家收入
		settlementDetail.setBuyerExpenditure(tradeOrder.getPaymentPrice().subtract(totalRefundAmount));//  买家支出
		settlementDetail.setCommission(totleCommission);//  佣金
			
		settlementDetail.setBuyerPaymentAccount(null);// 买家账户
		settlementDetail.setPaymentMethod(tradeOrder.getPaymentType()); //  支付银行，需要知道具体的付款方式
		if(tradeOrder.getPaymentMethod() == PayMethodEnum.PAY_ENTERPRISE.getCode()){
			//如果是企业支付
			settlementDetail.setStatus(SettleDetailStatusEnum.PayAffirm.getCode());//  中信设置状态为银行已确认
			settlementDetail.setType(SettleDetailTypeEnum.Pay.getCode());//  类型：1->待结算,2->已支付
			settlementDetail.setSellerCashAccountIncome(totlePayMoney);//  卖家提现账号应收金额
		}else if(tradeOrder.getPaymentMethod() == PayMethodEnum.PAY_PERSONAL.getCode()){
			//如果是个人支付
			settlementDetail.setStatus(SettleDetailStatusEnum.UnPaying.getCode());//  状态
			settlementDetail.setType(SettleDetailTypeEnum.Settle.getCode());//  类型：1->待结算,2->已支付
			//  针对延期付款可能遇到的情况
			settlementDetail.setSellerCashAccountIncome(totlePayMoney);//  卖家提现账号应收初始化，如有调整，进行修改
		}
		
		return settlementDetail;
	}
	public static void main(String[] args){
		BigDecimal itemTotlePrice = new BigDecimal(0.01);
		BigDecimal rebateRate = new BigDecimal(0.01);
		BigDecimal commission =itemTotlePrice.multiply(rebateRate).setScale(2, BigDecimal.ROUND_UP);
		BigDecimal b =itemTotlePrice.multiply(rebateRate).setScale(2, BigDecimal.ROUND_UP);
		System.out.println(commission.subtract(b));
	}
	/**
	 * 查询金融账户
	 * 
	 * @return
	 */
	public FinanceAccountInfoDto findFinanceAcc() {
		return financeAccountDAO.queryById(1);
	}
	
	/**
	 * 加锁
	 */
	private void lock() {
		lockTableDAO.updateByTable("settle_transfer", 10);
	}

	@Override
	public ExecuteResult<String> modifySettlementStatesNoPay(Long... settlementIds) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		int successCount=0;
		StringBuffer resultMsgSuccess=new StringBuffer("已结算单号：");
		StringBuffer resultMsgError=new StringBuffer("未结算单号：");
		for (int i = 0; i < settlementIds.length; i++) {
			 SettlementDTO settlementDTO=statementDAO.queryById(settlementIds[i]);
//			 if(SettleStatusEnum.UnSettle.getCode()==settlementDTO.getStatus()){
				 	SettlementDTO settle = new SettlementDTO();
					settle.setId(settlementIds[i]);
					settle.setStatus(SettleStatusEnum.UnConfirmed.getCode());
					settle.setHavePaidDate(new Date());
					statementDAO.update(settle);
					// 结算完成进入确认支付环节
					SettlementDetailDTO settleDetailUpdate= new SettlementDetailDTO();
					settleDetailUpdate.setSettlementId(settlementIds[i]);
					settleDetailUpdate.setType(SettleDetailTypeEnum.Pay.getCode());
					statementDetailDAO.updateStatuBySettleId(settleDetailUpdate);
					successCount++;
					resultMsgSuccess.append(settlementIds[i]+",");
//			 }else{
//				 resultMsgError.append(settlementIds[i]+"[不可结算],");
//			 }
		}
		
		if(successCount>0){
			result.setResultMessage(resultMsgSuccess.substring(0,resultMsgSuccess.length()-1)+"；"+resultMsgError.substring(0,resultMsgError.length()-1));
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"结算失败:"+resultMsgError.substring(0,resultMsgError.length()-1)));
		}
		return result;
	}

}
