package com.camelot.payment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.common.enums.FactorageEnums.FactorageStatus;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.*;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.PaymentWxExportService;
import com.camelot.payment.dao.CiticPayJournalDAO;
import com.camelot.payment.dao.FactorageJournalDAO;
import com.camelot.payment.dao.PaymentJournalDAO;
import com.camelot.payment.dao.TransationsDAO;
import com.camelot.payment.domain.CiticPayJournal;
import com.camelot.payment.domain.FactorageJournal;
import com.camelot.payment.domain.PaymentJournal;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.FactorageJournalDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.service.ImplUtil;
import com.camelot.settlecenter.dao.StatementDetailDAO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("paymentWxExportService")
public class PaymentWxExportServiceImpl extends ImplUtil implements PaymentWxExportService {
	
	private final static Logger logger = LoggerFactory.getLogger(PaymentWxExportServiceImpl.class);
	@Resource
	private TransationsDAO transationsDAO;
	@Resource
	private PaymentJournalDAO paymentJournalDAO;
	@Resource
	private CiticPayJournalDAO citicPayJournalDAO;
	@Resource
	private FactorageJournalDAO factorageJournalDAO;
	@Resource
	private StatementDetailDAO statementDetailDAO;
	@Resource
	private UserExportService userExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private UserExtendsService userExtendsService;
	
	@Override
	public ExecuteResult<Integer> payIndex(PayReqParam payReq) throws Exception{
		logger.info("\n 方法[{}]，入参：[{}]","PaymentWxExportServiceImpl-payIndex",JSONObject.toJSONString(payReq));
		Integer resultStatus =null;
		
		// 1.校验必填参数是否为空
		ExecuteResult<Integer> result =  this.verifyPayIndex(payReq);
		if(!result.isSuccess()){
			return result;
		}
		
		// 2.查询订单是否存在，不存在则插入新订单交易记录
		Transations tranQuery = transationsDAO.selectTransByOrderNo(payReq.getOrderNo());
		if (tranQuery == null) {			
			/******* 创建支付订单交易对象   ********/
			List<Transations> listPayOrder=new ArrayList<Transations>();
			int compareResult = payReq.getTotalFee().compareTo(new BigDecimal(0));
			if(compareResult==0){										// 4.1.如果支付金额为0||0.0||0.00，插入支付成功
				resultStatus=TransationsStatusEnum.PAID_SUCCESS.getCode();
				listPayOrder =this.buildTransatoins(payReq, resultStatus);
			}else{   																	// 4.2.插入未支付
				resultStatus =TransationsStatusEnum.PAYING.getCode();
				listPayOrder =this. buildTransatoins(payReq, TransationsStatusEnum.NOT_PAID.getCode());
			}
			if(listPayOrder==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null, "子单中卖家冻结账户不存在"));
				return result;
			}
			
			/******* 添加支付订单交易对象   ********/
			transationsDAO.adds(listPayOrder);
			
			/*******  特殊处理   ********/
			if(TransationsStatusEnum.PAID_SUCCESS.getCode()==resultStatus){
				this.callTradeOrders(tranQuery); // 0元直接成功
			}
		} else{ 		 // 3.2.存在则返回查询结果
			
			if (TransationsStatusEnum.PAID_SUCCESS.getCode().equals(tranQuery.getStatus())) {
				resultStatus=TransationsStatusEnum.PAID_SUCCESS.getCode();// 支付成功
			}else{
				resultStatus =TransationsStatusEnum.PAYING.getCode();
			}
		}
		result.setResult(resultStatus);
		
		this.insertPaymentJournal("null", payReq.getOrderNo(),PayJournayStatusEnum.INDEX_REQ.getCode(),
				JSONObject.toJSONString(payReq));
		logger.info("\n 方法[{}]，出参：[{}]","PaymentWxExportServiceImpl-payIndex",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<Integer> pay(PayReqParam payReq) throws Exception{
		logger.info("\n 方法[{}]，入参：[{}]","PaymentWxExportServiceImpl-pay",JSONObject.toJSONString(payReq));
		// 1.必填参数是否为空
		 ExecuteResult<Integer> result = this.verifyPay(payReq);
		 if(!result.isSuccess()){
				return result;
		}
        
		 // 获取二维码时 需要自动添加支付方式
		if (PayBankEnum.AP.getQrCode().toString().equals(payReq.getQrPayMode())) {// 支付宝二维码
			payReq.setPayBank(PayBankEnum.AP);
		} 
			
		/**
		 * 2.校验
		 * (1)判断支付订单记录的存在性,不存在--->返回交易记录不存在
		 * (2)判断支付订单记录是否无效性(父单),无效--->返回该订单下子订单正在处理，请以子订单支付
		 * (2)判断支付订单记录是否已支付成功，支付成功--->返回交易记录成功
		 * (3)校验支付请求数据和支付查询数据，获取最后支付结果，生成支付链接--->返回支付链接
		 **/ 
		Transations tranQuery =transationsDAO.selectTransByOrderNo(payReq.getOrderNo());
		if (tranQuery == null) {// 交易记录不存在 报错
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null, "交易记录不存在！"));
		} else if (TransationsStatusEnum.USER_LESS.getCode().equals(tranQuery.getStatus())) {// 子单进行交易中，父级订单无效化
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error, "该订单下子订单正在处理，请以子订单支付！"));
		} else if (TransationsStatusEnum.PAID_EXCEPTION.getCode().equals(tranQuery.getStatus())) {// 支付成功，金额异常
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Custom, "该订单交易异常，请与管理员联系！"));
		} else if (TransationsStatusEnum.PAID_SUCCESS.getCode().equals(tranQuery.getStatus())) {// 支付成功
			result.setResult(TransationsStatusEnum.PAID_SUCCESS.getCode());
		} else if (TransationsStatusEnum.NOT_PAID.getCode().equals(tranQuery.getStatus())) {// 未支付过的 添加银行信息
			tranQuery.setPayBank(payReq.getPayBank().name());
			tranQuery.setStatus(TransationsStatusEnum.PAYING.getCode());
			tranQuery.setLaunchPayTimes(1);	//第一次支付，发起支付次数为1
			transationsDAO.updatesByParentId(tranQuery);
			
			/** 子单交易，则默认为父级订单无效化 **/
			if(PayOrderTypeEnum.child.ordinal()==tranQuery.getPayOrderType()){
				Transations tranParent=transationsDAO.selectTransByOrderNo(tranQuery.getOrderParentNo());
				if(tranParent!=null&&TransationsStatusEnum.USER_LESS.getCode()!=tranParent.getStatus()){
					tranParent.setStatus(TransationsStatusEnum.USER_LESS.getCode());
					transationsDAO.update(tranParent);
				}
			}
		} else{
			/**
			 * 1.父级订单批量处理
			 * 2.子级订单单体处理并把父级订单置为无效化 
			 **/
			
			// 校验是否为正在支付的原来银行.支付次数+1
			if(tranQuery.getPayBank().equals(payReq.getPayBank().name())&& TransationsStatusEnum.PAYING.getCode().equals(tranQuery.getStatus())){ 
				tranQuery.setLaunchPayTimes(tranQuery.getLaunchPayTimes()+1);
				transationsDAO.updatesByParentId(tranQuery);
			}else{
				// 未支付成功的 原支付订单及子单记录作废 插入新支付订单记录
				List<Transations> listTran =this.getItemByOrderId(tranQuery.getOrderNo());
				
				tranQuery.setEnableFlag(false);
				transationsDAO.updatesByParentId(tranQuery);
				
				// 重新封装后批量添加
				for (Transations reTrans: listTran) {
					reTrans.setStatus(TransationsStatusEnum.PAYING.getCode());
					reTrans.setPayBank(payReq.getPayBank().name());
					reTrans.setLaunchPayTimes(1);
				}
				tranQuery.setStatus(TransationsStatusEnum.PAYING.getCode());
				tranQuery.setPayBank(payReq.getPayBank().name());
				tranQuery.setLaunchPayTimes(1);
				listTran.add(tranQuery); // 添加父单
				transationsDAO.adds(listTran);
			}
			
			/** 子单交易，则默认为父级订单无效化 **/
			if(PayOrderTypeEnum.child.ordinal()==tranQuery.getPayOrderType()){
				Transations tranParent=transationsDAO.selectTransByOrderNo(tranQuery.getOrderParentNo());
				if(tranParent!=null&&TransationsStatusEnum.USER_LESS.getCode()!=tranParent.getStatus()){
					tranParent.setStatus(TransationsStatusEnum.USER_LESS.getCode());
					transationsDAO.update(tranParent);
				}
			}
		}
		
		// 生成支付链接，支付宝二维码和普通支付一个方法
		if(result.isSuccess()&&TransationsStatusEnum.PAYING.getCode().equals(tranQuery.getStatus())){
//			// 校验金额一致性，订单修改价格，则该次支付无效化
			ExecuteResult<TradeOrdersDTO> orderQuery=tradeOrderExportService.getOrderById(tranQuery.getOrderNo());
			if(orderQuery.isSuccess()&&orderQuery.getResult()!=null){
				TradeOrdersDTO tradeOrdersDTO = orderQuery.getResult();
				int verifyAmount= tranQuery.getOrderAmount().compareTo(tradeOrdersDTO.getPaymentPrice());
				if(verifyAmount==0){
					
					//根据支付类型。更新支付订单的对外交易号 ，默认为901，微信101，支付宝201，网银在线301，中信401
					Transations trans = transationsDAO.selectTransByOrderNo(payReq.getOrderNo());
					String outTranNo = null;
					if (null != trans && trans.getPayBank().equals(payReq.getPayBank()) && !trans.getOutTradeNo().equals("0")) {
						outTranNo = trans.getOutTradeNo();
					} else {
						// 买家信息
						UserDTO userDTO = null;
						// 退款冲抵用户名
						String refund_offset_account = SysProperties.getProperty(SysConstants.REFUND_OFFSET_ACCOUNT);
						// 根据订单编号查buyerId再查user
						ExecuteResult<TradeOrdersDTO> er = tradeOrderExportService.getOrderById(payReq.getOrderNo());
						if (result.isSuccess() && result.getResult() != null) {
							userDTO = userExportService.queryUserById(er.getResult().getBuyerId());
						}
						// 如果买家是退款冲抵账号，那么对外交易号以“R”开头
						if (userDTO != null && refund_offset_account.equals(userDTO.getUname())) {
							outTranNo = transationsDAO.selectOutTranNo("R");
						} else {
							outTranNo = transationsDAO.selectOutTranNo(payReq.getPayBank().name());
						}
						tranQuery.setOutTradeNo(outTranNo);
					}
					// 更新对外交易号
					Integer updateResult = transationsDAO.updateOutTradeNoByOrderId(outTranNo, payReq.getOrderNo());
					if(updateResult == null || updateResult == 0){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Custom, "生成对外交易号失败"));
						return result;
					}
					
					if(PayBankEnum.AP.getQrCode().toString().equals(payReq.getQrPayMode())){// 支付宝二维码
						result = this.getPayService(PayBankEnum.AP.name()).buildPayForm(this.buildPayReqParam(payReq, tranQuery));
					}else{
						result = this.getPayService(payReq.getPayBank().name()).buildPayForm(this.buildPayReqParam(payReq, tranQuery));
					}
					result.setResult(TransationsStatusEnum.PAYING.getCode());
				}else{
					tranQuery.setEnableFlag(false);
					transationsDAO.update(tranQuery);
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"订单价格已变更，请重新支付"));
				}
			}else{
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Custom,"订单异常"));
			}
		}
		this.insertPaymentJournal(tranQuery==null?"null":tranQuery.getOutTradeNo(),payReq.getOrderNo(), PayJournayStatusEnum.PAY_REQ.getCode(),
				JSONObject.toJSONString(payReq));// 插入流水
		logger.info("\n 方法[{}]，出参：[{}]","PaymentWxExportServiceImpl-pay",JSONObject.toJSONString(result));
		return result;
	}

	@Override
	public ExecuteResult<OrderInfoPay> payResult(Map<String, String> params,String payBank) {
		logger.info("\n 方法[{}]，入参：[{}][{}]","PaymentWxExportService-payResult",params,payBank);
		ExecuteResult<OrderInfoPay> result = new ExecuteResult<OrderInfoPay>();
		String resultMsg="";
		boolean isSuccess =false;
		
		// 获取回传方式 同步/异步
		String notifyType = ("false".equals(params.get("isNotify")) ? PayJournayStatusEnum.SYNCHRONOUS_NOTICE
				.getCode() : PayJournayStatusEnum.ASYNCHRONOUS_NOTICE.getCode());
		params.remove("isNotify"); // 删除影响验证的key
		
		/** 1.根据传入方式解析回传数据为流程控制对象 **/
		ExecuteResult<Transations> buildResut = this.getPayService(payBank).buildTransatoins(params, notifyType);
		if (buildResut.isSuccess()) {
			Transations tranBank=buildResut.getResult();
			/** 2.根据对外订单号查询交易记录是否存在 **/
			Transations tranQuery = transationsDAO.selectTransByOutTrade(tranBank.getOutTradeNo());
			if (tranQuery != null&&!TransationsStatusEnum.USER_LESS.getCode().equals(tranQuery.getStatus())) { // 获取有效的交易记录
				if (TransationsStatusEnum.PAYING.getCode().equals(tranQuery.getStatus())) {
					/** 3.流程控制对象中正在支付的根据通知结果做处理 **/
					ExecuteResult<List<Transations>> handleResult = handleBankResult(tranQuery,tranBank,payBank); // 对银行结果进行处理
					if(handleResult.isSuccess()){
						ExecuteResult<Integer> systemResult = confirmSystemBack(handleResult.getResult());
						resultMsg = "消息-银行结果成功处理条数："+handleResult.getResult().size()+";调用系统结果成功处理条数："+systemResult.getResult();
					}else{
						resultMsg = "消息-银行结果处理："+JSONObject.toJSONString(handleResult.getErrorMessages());
					}
				} else {
					resultMsg = "消息-流程控制对象状态处理已完成，不再进行重复处理";
				}
				// 仅代表对银行结果处理完成的。
				isSuccess=true;
				result.setResult(this.buildOrderInfo(tranQuery,tranBank.isIgnore(),tranBank.getStatus()));		
				this.insertPaymentJournal(tranQuery.getOutTradeNo(),tranQuery.getOrderNo(), notifyType,JSONObject.toJSONString(params)+"["+payBank+"]"); // 插入流水
			} else {
				resultMsg = "消息-对外订单号不存在或无效，请尝试用子单进行支付";
				this.insertPaymentJournal(tranBank.getOutTradeNo(), "null",notifyType, JSONObject.toJSONString(params)+"["+payBank+"]"); // 插入流水
			}
		} else {
			resultMsg = "消息-"+buildResut.getErrorMessages().get(0);
			this.insertPaymentJournal("null", "null", notifyType,JSONObject.toJSONString(params)+"["+payBank+"]"); // 插入流水
		}
		if(isSuccess){
			if(PayJournayStatusEnum.SYNCHRONOUS_NOTICE.getCode().equals(notifyType)){
				result.setResultMessage("银行-"+payBank+";通知方式-" + notifyType + ";"+resultMsg);
			}else{
				result.setResultMessage("ok");
			}
		}else{
			result.setResultMessage(buildResut.getResultMessage());// 针对中信金额不足的标记补充。
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"银行-"+payBank+";通知方式-" + notifyType + ";"+resultMsg));
		}
		logger.info("\n 方法[{}]，出参：[{}][{}]","PaymentWxExportService-payResult",JSON.toJSON(result));
		return result;
	}

	/** 
	  * 银行结果的处理
	  *
	  * @param tranResult - 原交易流程控制对象
	  * @param tranBank - 银行返回结果解析
	  * @throws -修改失败
	  **/ 
	private ExecuteResult<List<Transations>> handleBankResult(Transations tranQuery,Transations tranBank,String payBank){
		ExecuteResult<List<Transations>> result = new ExecuteResult<List<Transations>>();
		
		// 1.更新对象初始化准备
		tranQuery.setTransactionNo(tranBank.getTransactionNo()) ;//返回的银行交易号
		tranQuery.setBuyer(tranBank.getBuyer());//买家名称
		tranQuery.setFromAccount(tranBank.getFromAccount());//付款账号
		tranQuery.setCompletedTime(tranBank.getCompletedTime());
		tranQuery.setLaunchPayTimes(null);
		tranQuery.setPayBank(payBank);
		// 2.支付金额是否一致
		int compareResult= tranQuery.getOrderAmount().compareTo(tranBank.getRealAmount());
		if (compareResult==0) { 
			tranQuery.setStatus(tranBank.getStatus());
			tranQuery.setStatusText("success");
			result.setResultMessage("支付结果处理完成");
		} else {
			tranQuery.setRealAmount(tranBank.getRealAmount());
			tranQuery.setStatus(TransationsStatusEnum.PAID_EXCEPTION.getCode());
			tranQuery.setStatusText("支付金额不一致");
			result.addErrorMessage("支付金额不一致");
		}
		List<Transations> listTran = new ArrayList<Transations>();
		if(PayBankEnum.CB_MOBILE.name().equals(tranQuery.getPayBank())){
			//网银在线手机端
			transationsDAO.updatesByParentId(tranQuery);
			listTran.add(tranQuery);
		}else if(PayBankEnum.AP_MOBILE.name().equals(tranQuery.getPayBank())){
			//支付宝支付手机端
			transationsDAO.updatesByParentId(tranQuery);
			listTran.add(tranQuery);
		}else if(PayBankEnum.WX.name().equals(tranQuery.getPayBank())){
			//网微信支付手机端
			transationsDAO.updatesByParentId(tranQuery);
			listTran.add(tranQuery);
		}else{
			if(PayOrderTypeEnum.Parent.ordinal()==tranQuery.getPayOrderType()){ // 父单
				CiticPayJournal journalCondition = new CiticPayJournal();
				journalCondition.setOrderParentTradeNo(tranQuery.getOutTradeNo());
				journalCondition.setStatus(2);//  交易状态 0初始化/1交易失败/2交易成功
				journalCondition.setPayType(0); //  交易类型  0支付/1结算
				List<CiticPayJournal> listPayResult =citicPayJournalDAO.queryList(journalCondition, null);
				// 父单中存在支付成功的子单，则父单默认为无效化
				tranQuery.setStatus(TransationsStatusEnum.USER_LESS.getCode());
				transationsDAO.update(tranQuery);
				
				for (CiticPayJournal item:listPayResult) {
					Transations tranItem =transationsDAO.selectTransByOutTrade(item.getOutTradeNo());
					tranItem.setFromAccount(tranBank.getFromAccount());//付款账号
					tranItem.setCompletedTime(tranBank.getCompletedTime());
					tranItem.setLaunchPayTimes(null);
					tranItem.setStatus(tranBank.getStatus());
					tranItem.setStatusText(tranBank.getStatusText());
					transationsDAO.update(tranItem);
					listTran.add(tranItem);
				}
			}else{ // 子单支付，则父单已为无效订单
				transationsDAO.update(tranQuery);
				listTran.add(tranQuery);
			}
		}
		result.setResult(listTran);
		return result;
	}
	
	/**
	 * 向指定系统回传结果
	 * 
	 * @param handleBankResult
	 * @return
	 */
	public ExecuteResult<Integer> confirmSystemBack(List<Transations> handleBankResult){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		Integer successCount=0;
		for (Transations transations : handleBankResult) {
			if(transations.getDealFlag()!=null){
				if(TransationsStatusEnum.PAID_SUCCESS.getCode().equals(transations.getStatus())){
					ExecuteResult<String> resultOrder=this.callTradeOrders(transations);
					if(resultOrder.isSuccess()){
						transations.setDealFlag("");// 修改流程控制对象处理方式为已处理
						transationsDAO.update(transations);
						result.setResultMessage("支付结果回传到"+transations.getPayType()+"系统成功");
						successCount+=1;
						
					}else{
						result.setResultMessage("支付结果回传到"+transations.getPayType()+"系统失败");
					}
					
					SettlementDetailDTO settleDetail = new SettlementDetailDTO();
					settleDetail.setOrderId(transations.getOrderNo());
					if(PayBankEnum.CITIC.name().equals(transations.getPayBank())){
						// 针对中信延期付款修改结算明细的状态为确认支付
						settleDetail.setStatus(SettleDetailStatusEnum.PayAffirm.getCode());
					}
					settleDetail.setPaymentMethod(PayBankEnum.getEnumByName(transations.getPayBank()).getQrCode());
					statementDetailDAO.updateByOrderId(settleDetail);
				}
			}else{
				result.setResultMessage("支付结果已做过回传处理");
			}
		}
		
		result.setResult(successCount);
		return result;
	}
	
	@Override
	public ExecuteResult<OrderInfoPay> paySearch(String orderNo) {
		ExecuteResult<OrderInfoPay> result =new ExecuteResult<OrderInfoPay>();
		Transations  tranQuery =transationsDAO.selectTransByOrderNo(orderNo);
		if(tranQuery!=null){
			tranQuery = this.getPayService(tranQuery.getPayBank()).queryTradeInfo(tranQuery);
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"支付订单不存在"));
		}
		result.setResult(this.buildOrderInfo(tranQuery,false,null));		
		return result;
	}
	
	@Override
	public List<OrderInfoPay> findChildTransByOutTrades(String outTradeNo) {
		List<Transations> listChildTrans = new ArrayList<Transations>();
		Transations tranQuery =transationsDAO.selectTransByOutTrade(outTradeNo);
		if(tranQuery!=null){
			if(tranQuery.getPayOrderType()==PayOrderTypeEnum.Parent.ordinal()){
				listChildTrans = transationsDAO.selectChildTransByParentNo(tranQuery.getOrderNo());
			}else{
				listChildTrans.add(tranQuery);
			}
		}
		List<OrderInfoPay> listOrderInfoPay = new ArrayList<OrderInfoPay>();
		for (Transations childTran: listChildTrans) {
			listOrderInfoPay.add(this.buildOrderInfo(childTran, false, null));
		}
		return listOrderInfoPay;
	}
	
	@Override
	public ExecuteResult<String> modifyOrderPrice(String orderNo) throws Exception{
		ExecuteResult<String> result =new ExecuteResult<String>();
		Transations transations =transationsDAO.selectTransByOrderNo(orderNo);
	    // 父级订单支付记录无效化
		Transations tranParent =new Transations();
		tranParent.setOrderNo(transations.getOrderParentNo());
		tranParent.setEnableFlag(false);
		transationsDAO.updateByOrderId(tranParent);
		
		// 该支付记录价格修改为订单改价后价格
		ExecuteResult<TradeOrdersDTO> orderQuery=tradeOrderExportService.getOrderById(orderNo);
		transations.setOrderAmount(orderQuery.getResult().getPaymentPrice());
		transationsDAO.updateAmount(transations);
		return result;
	}
	
	@Override
	public void paySearchResult(Map<String, String> parameterMap) {
		this.payResult(parameterMap, PayBankEnum.CB.name());
	}
	
	@Override
	public void saveFactorageJournal(FactorageJournalDTO factorageJournalDTO) throws Exception{
		List<FactorageJournal>  factorageJournal  = factorageJournalDAO.selectByOrderNoAndStatus(factorageJournalDTO.getOrderNo(),FactorageStatus.Initial.getFacCode());
		if(factorageJournal.size()==0&&factorageJournalDTO!=null&&factorageJournalDTO.getStatus()!=null){
			FactorageJournal  fac= EntityTranslator.transObj(factorageJournalDTO, FactorageJournal.class, false);
			fac.setStatus(factorageJournalDTO.getStatus().getFacCode());
			factorageJournalDAO.add(fac);
		}
	}

	@Override
	public DataGrid<FactorageJournalDTO> findFactorageJournal(FactorageJournalDTO factorageJournalDTO,@SuppressWarnings("rawtypes") Pager pager) {
		DataGrid<FactorageJournalDTO> result = new DataGrid<FactorageJournalDTO>();
		FactorageJournal factorageJournal =null;
		if(factorageJournalDTO!=null){
			factorageJournal = EntityTranslator.transObj(factorageJournalDTO, FactorageJournal.class, false);
			factorageJournal.setCreatedBegin(factorageJournalDTO.getCreatedBegin());
			if(StringUtils.isNotBlank(factorageJournalDTO.getCreatedEnd())){
				Date createdEnd=DateUtils.dayOffset(DateUtils.parseReturnTime(factorageJournalDTO.getCreatedEnd(),DateUtils.YMD_DASH), 1); // 查询结束时间=创建时间+1天
				factorageJournal.setCreatedEnd(DateUtils.format(createdEnd, DateUtils.YMD_DASH));
			}
			if(factorageJournalDTO.getStatus()!=null){
				factorageJournal.setStatus(factorageJournalDTO.getStatus().getFacCode());	
			}
		}
		
		long count  = factorageJournalDAO.queryCount(factorageJournal);
		if(count>0){
			List<FactorageJournal> list  = factorageJournalDAO.queryList(factorageJournal, pager);
			
			List<FactorageJournalDTO> listResult=new ArrayList<FactorageJournalDTO>();
			for (FactorageJournal fac: list) {
				FactorageJournalDTO facDTO = EntityTranslator.transObj(fac, FactorageJournalDTO.class, true);
				facDTO.setStatus(FactorageStatus.getEnumByCiticCode(fac.getStatus()));
				listResult.add(facDTO);
			}
			result.setRows(listResult);
		}
		result.setTotal(count);
		return result;
	}
	
	// TODO 废弃代码
	public ExecuteResult<RefundPayParam> refundApply(RefundPayParam refundPayParam) {
		logger.info("\n 方法[{}]，入参：[{}]","PaymentWxExportServiceImpl-refundApply",JSONObject.toJSONString(refundPayParam));
		ExecuteResult<RefundPayParam> result =new ExecuteResult<RefundPayParam>();
		return result;
	}
	// TODO 废弃代码
	public ExecuteResult<RefundPayParam> refundDeal(RefundPayParam refundPayParam) {
		ExecuteResult<RefundPayParam> result =new ExecuteResult<RefundPayParam>();
		return result;
	}
	// TODO 废弃代码
	public ExecuteResult<RefundPayParam> findRefInfoByRefNo(String refundNo) {
		ExecuteResult<RefundPayParam> result = new ExecuteResult<RefundPayParam>();
		return result;
	}
	// TODO 废弃代码
	public DataGrid<RefundPayParam> findRefInfoByCondition(RefundPayParam refundPayParam,@SuppressWarnings("rawtypes") Pager pager) {
		DataGrid<RefundPayParam> result = new DataGrid<RefundPayParam>();
		return result;
	}
	// TODO 废弃代码
    public ExecuteResult<RefundPayParam> findRefInfoByReturnGoodsCode(String returnGoodsCodeNo) {
        ExecuteResult<RefundPayParam> result = new ExecuteResult<RefundPayParam>();
        return result;
    }

    /**
	 * 方法：【payIndex】-构建支付订单对象
	 * 
	 * @param payReq -支付接收参数
	 * @param status - 状态
	 * @return 封装支付订单对象
	 */
	protected List<Transations> buildTransatoins(PayReqParam payReq, Integer status) {
		List<Transations> listPayOrder=new ArrayList<Transations>();
		// 父级订单
		Transations transParent = new Transations();
		transParent.setOrderNo(payReq.getOrderNo());
		transParent.setSubject(payReq.getSubject());
		transParent.setOrderParentNo("0"); // 父单
		transParent.setPayOrderType(PayOrderTypeEnum.Parent.ordinal());
		transParent.setOrderAmount(payReq.getTotalFee());
		transParent.setStatus(status);
		transParent.setOrderDetails(payReq.getOrderDetails());
		transParent.setPayType(payReq.getPayType().name()); // 支付方式 默认在线支付
		listPayOrder.add(transParent);
		
		// 子级订单
		List<OrderItemPay> listItems=payReq.getOrderItemPays();
		for (OrderItemPay orderItem:listItems) {
			// 校验卖家冻结账号的存在性
			ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(orderItem.getSellerId());
			if(userInfo!=null&&userInfo.getResult()!=null&&userInfo.getResult().getUserCiticDTO()!=null
					&&userInfo.getResult().getUserBusinessDTO()!=null){
				String subAccNo = userInfo.getResult().getUserCiticDTO().getSellerFrozenAccount();
				String SubAccNm = userInfo.getResult().getUserBusinessDTO().getCompanyName();
				Integer accountState=userInfo.getResult().getUserCiticDTO().getAccountState();
				
				if(StringUtils.isNotBlank(subAccNo)&&StringUtils.isNotBlank(SubAccNm)
						&&CommonEnums.ComStatus.PASS.getCode()==accountState){ // 卖家只有审核通过状态
					Transations tranItem = new Transations();
					tranItem.setOrderNo(orderItem.getSubOrderId());
					tranItem.setSubject(orderItem.getSubOrderSubject());
					tranItem.setOrderParentNo(transParent.getOrderNo());
					tranItem.setPayOrderType(PayOrderTypeEnum.child.ordinal());
					tranItem.setOrderAmount(orderItem.getSubOrderPrice());
					tranItem.setToAccount(subAccNo); // 卖家冻结账号
					tranItem.setSeller(SubAccNm);// 卖家冻结账号名称
					tranItem.setStatus(transParent.getStatus());
					tranItem.setPayType(transParent.getPayType());
					
					listPayOrder.add(tranItem);
				}else{
					return null;
				}
			}else{
				return null;
			}
		}
		return listPayOrder;
	}
	
	/**
	 * 构建并插入流水记录对象
	 *
	 * @return 封装支付订单对象
	 */
	private void insertPaymentJournal(String outTradeNo, String orderNo,String type, String paramStr) {
		PaymentJournal payment = new PaymentJournal();
		payment.setOutTradeNo(outTradeNo);
		payment.setOrderNo(orderNo);
		payment.setType(type);
		payment.setDetails(paramStr);
		paymentJournalDAO.add(payment);
	}

	/**
	 * 通知订单
	 * 
	 * @param transations
	 */
	private ExecuteResult<String> callTradeOrders(Transations transations) {
		TradeOrdersDTO inDTO =new TradeOrdersDTO();
		inDTO.setOrderId(transations.getOrderNo());
		inDTO.setPaymentType(PayBankEnum.getEnumByName(transations.getPayBank()).getQrCode());
		return tradeOrderExportService.modifyOrderPayStatus(inDTO);
	}
	
	/**
	 * 搜索当前交易表中该父级订单号下子单数量
	 * 
	 * @param orderParentId
	 * @return
	 */
	private List<Transations> getItemByOrderId(String orderParentId) {
		List<Transations> list=new ArrayList<Transations>();
		Transations transations=new Transations();
		transations.setOrderParentNo(orderParentId);
		list=transationsDAO.queryList(transations,null);
		return list;
	}

}
