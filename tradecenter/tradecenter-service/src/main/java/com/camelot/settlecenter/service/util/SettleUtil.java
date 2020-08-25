package com.camelot.settlecenter.service.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.common.enums.SettleEnum.SettleStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.domain.RefundOrder;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.dto.SettlementInfoOutDTO;
import com.camelot.settlecenter.dto.indto.SettlementInfoInDTO;
import com.camelot.settlecenter.service.SettleItemExpenseExportService;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;

public class SettleUtil {

	
	@Resource
	protected SettleItemExpenseExportService settleItemExpenseExportService;
	@Resource
	private StatementExportService statementExportService;
	
	private enum SettlePayNoEnums{
		CbToFreeze,// 网银在线账户-->卖家冻结账户
		FreezeToCash,//卖家冻结账户-->卖家收款账户
		FreezeToCommission;//卖家冻结账户-->佣金账户
	}
	/**
	 * 校验订单项，生成结算详情单
	 * 
	 * @param payReq
	 * @return
	 */
	protected ExecuteResult<String> verifyCreateDetail(TradeOrdersDTO tradeOrder){
		ExecuteResult<String> result= new ExecuteResult<String>();
		
		if(tradeOrder!=null){
			
			// 参数验证
			if(StringUtils.isBlank(tradeOrder.getOrderId())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "orderId"));
			}else if(tradeOrder.getSellerId()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "sellerId"));
			}else if(tradeOrder.getShopId()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "shopId"));
			}else if(tradeOrder.getItems()==null||tradeOrder.getItems().size()==0){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "Items不能为空"));
			}else{
				for (TradeOrderItemsDTO tradeOrderItemsDTO:tradeOrder.getItems()) {
					if(tradeOrderItemsDTO.getCid()==null){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "Items.cid存在空值"));
						break;
					}else if(tradeOrderItemsDTO.getPayPriceTotal()==null){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "Items.payPriceTotal存在空值"));
						break;
					}
				}
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	/**
	 *  构建结算单，初始化金额
	 * 
	 * @param tradeOrder
	 * @return
	 */
	protected SettlementDTO buildSettlement(TradeOrdersDTO tradeOrder,String sellFreeze, 
			String sellReceipt,String companyName,FinanceAccountInfoDto financeAccountInfoDto,List<RefundOrder> refundOrderList) {
		SettlementDTO settlement = new SettlementDTO();
		settlement.setSellerId(tradeOrder.getSellerId());
		settlement.setShopId(tradeOrder.getShopId());

		//如果是企业支付，则结算单的状体直接设置为“已结算待确认”,并且需要直接计算出各部分金额
		if(tradeOrder.getPaymentType()==PayMethodEnum.PAY_ENTERPRISE.getCode()){
			
			String paymentId = statementExportService.buildPaymentId(PayBankEnum.CITIC.name());
			settlement.setPaymentId(paymentId);
			settlement.setBeginDate(new Date());
			settlement.setEndDate(new Date());
			
			BigDecimal platFormIncome = new BigDecimal("0.00");//平台总收入
			BigDecimal totlePayMoney = new BigDecimal("0.00");// 订单总结算金额
			BigDecimal totalRefundAmount = new BigDecimal("0.00");//退款金额
			//遍历一个订单下的所有不同的商品,获取不同商品的佣金，用来计算平台总收入
			for(TradeOrderItemsDTO tradeOrderItemsDTO:tradeOrder.getItems()){
				BigDecimal itemTotlePrice = tradeOrderItemsDTO.getPayPriceTotal();//每一种商品的总价格
				BigDecimal integralDiscount = tradeOrderItemsDTO.getIntegralDiscount();
				//如果这个商品使用了积分兑换，则需要使用总商品计算佣金
				if(integralDiscount != null){
					itemTotlePrice = itemTotlePrice.add(integralDiscount);
				}
				//如果该商品存在退款，则计算佣金时应该使用退款后的金额计算佣金
				for(RefundOrder refundOrder : refundOrderList){
					if(tradeOrderItemsDTO.getSkuId().equals(refundOrder.getSkuId())){
						//退款金额
						totalRefundAmount = totalRefundAmount.add(refundOrder.getRefundAmount());
						//退款后的商品金额,如果退款中包含运费，则默认商品金额为0
						if(itemTotlePrice.compareTo(refundOrder.getRefundAmount()) <= 0){
							itemTotlePrice = new BigDecimal("0.00");
						}else{
							itemTotlePrice = itemTotlePrice.subtract(refundOrder.getRefundAmount());
						}
					}
				}
				// 获取单个商品的费率
				SettlementInfoInDTO settlementInfoInDTO=new SettlementInfoInDTO();
			    settlementInfoInDTO.setCid(tradeOrderItemsDTO.getCid());
			    settlementInfoInDTO.setItemId(tradeOrderItemsDTO.getItemId());
				ExecuteResult<SettlementInfoOutDTO> resultSettleCats = settleItemExpenseExportService.getSettlementInfo(settlementInfoInDTO);
			    BigDecimal rebateRate=resultSettleCats.getResult().getRebateRate();// 费率
				//佣金
				BigDecimal commission = itemTotlePrice.multiply(rebateRate).setScale(2, BigDecimal.ROUND_UP);
				platFormIncome = platFormIncome.add(commission);
			}
			totlePayMoney = tradeOrder.getPaymentPrice().subtract(totalRefundAmount).subtract(platFormIncome);
			settlement.setPlatformIncome(platFormIncome);//  平台总收入=佣金
			settlement.setSellerIncome(totlePayMoney);//  卖家总收入=订单金额-佣金
			settlement.setOrderTotalMoney(tradeOrder.getPaymentPrice());//  订单总金额 = 实际支付金额-运费
			settlement.setCommissionTotalMoney(platFormIncome);//  佣金总金额
			settlement.setSettlementTotalMoney(totlePayMoney);//  结算总金额 = 订单总结算金额+运费
			settlement.setPlatformNeedPay(totlePayMoney);//  平台需要支付给卖家的金额
			
			settlement.setStatus(SettleStatusEnum.UnConfirmed.getCode());//  状态：2->已结算待确认
			settlement.setSettlementDate(new Date());//  应结算日期
			settlement.setBillDate(new Date());//出账日
			
			settlement.setPlatformAccount(financeAccountInfoDto.getSettlementAccountNumber());//  平台网银在线结算账户
			settlement.setPlatformCommissionAccount(financeAccountInfoDto.getCommissionAccountNumber());//  平台佣金账号
			//settlement.setSellerCashAccount(sellReceipt);
			settlement.setSellerFrozenAccount(sellFreeze);
			settlement.setCompanyName(companyName);
		}else{//个人支付
			settlement.setBeginDate(getIntervalDate("start"));
			settlement.setEndDate(getIntervalDate("end"));
			settlement.setPlatformIncome(new BigDecimal("0"));//  平台总收入
			settlement.setSellerIncome(new BigDecimal("0"));//  卖家总收入
			settlement.setOrderTotalMoney(new BigDecimal("0"));//  订单总金额
			settlement.setCommissionTotalMoney(new BigDecimal("0"));//  佣金总金额
			settlement.setSettlementTotalMoney(new BigDecimal("0"));//  结算总金额
			
			settlement.setStatus(SettleStatusEnum.UnBill.getCode());//  状态：0->待出账
			
			//结算周期
			/*Integer interval=Integer.parseInt(SysProperties.getProperty(SysConstants.CITIC_INTERVAL));
			Calendar calendar = new GregorianCalendar();
		    calendar.setTime(new Date()); 
		    calendar.add(calendar.DATE, interval);*/
			
		    Calendar calendar =Calendar.getInstance();
		    calendar.add(calendar.DATE, 1);
		    
			settlement.setSettlementDate(calendar.getTime());//  应结算日期 = 当前日期+结算周期
			/*if(PayBankEnum.CB.getQrCode() == tradeOrder.getPaymentType() || PayBankEnum.CB_MOBILE.getQrCode() == tradeOrder.getPaymentType()){
				settlement.setPlatformAccount(financeAccountInfoDto.getSettlementAccountNumber());//  平台网银在线结算账户
			}else if(PayBankEnum.AP.getQrCode() == tradeOrder.getPaymentType() || PayBankEnum.AP_MOBILE.getQrCode() == tradeOrder.getPaymentType()){
				settlement.setPlatformAccount(financeAccountInfoDto.getSettlementAlipayAccountNumber());//  支付宝在线结算账户
			}else if(PayBankEnum.WX.getQrCode() == tradeOrder.getPaymentType() || PayBankEnum.WXPC.getQrCode() == tradeOrder.getPaymentType()){
				settlement.setPlatformAccount(financeAccountInfoDto.getSettlementWXAccountNumber());// 微信在线结算账户
			}*/
			
			//settlement.setPlatformCommissionAccount(financeAccountInfoDto.getCommissionAccountNumber());//  平台佣金账号
			//settlement.setSellerCashAccount(sellReceipt);
			//settlement.setSellerFrozenAccount(sellFreeze);
			settlement.setCompanyName(companyName);
		}
		
		settlement.setPlatformExpenditure(new BigDecimal("0"));//  平台总支出
		settlement.setSellerExpenditure(new BigDecimal("0"));//  卖家总支出=退款金额
		settlement.setPlatformHavePaid(new BigDecimal("0"));//  平台已付金额
		settlement.setPlatformNeedPay(new BigDecimal("0"));//  平台需要支付给卖家的金额
		//计算退款总金额
		BigDecimal totalRefundAmount = new BigDecimal(0.00);
		for(RefundOrder refundOrder : refundOrderList){
			totalRefundAmount = totalRefundAmount.add(refundOrder.getRefundAmount());
		}
		settlement.setRefundTotalMoney(totalRefundAmount);//  退款总金额
		return settlement;
	}
	
	/**
	 * 构建中信支付（网银在线-->卖家冻结账户）
	 * 
	 * @param listCBDetail - 结算详情单列表 - 针对网银在线结算到冻结账户， 此处可以只传递一个SettlementDetatilDTO，但是为了兼容以前的数据，还是传入的list
	 * @param settlementDTO - 结算单信息
	 * @return
	 */
	protected PayReqParam buildCbPayReqParam(List<SettlementDetailDTO> listCBDetail,SettlementDTO settlementDTO,FinanceAccountInfoDto financeAccountInfoDto) {
		PayReqParam payReqParam =new PayReqParam();
		payReqParam.setOutTradeNo(settlementDTO.getPaymentId()+SettlePayNoEnums.CbToFreeze.ordinal());
		
		// 收款账户 
		payReqParam.setToAccount(settlementDTO.getSellerFrozenAccount());
		payReqParam.setSeller(settlementDTO.getCompanyName()); // 卖家公司名称
		// 交易金额
		BigDecimal totalFee =new BigDecimal("0");
		for (SettlementDetailDTO cbDetail:listCBDetail) {
			
			if(cbDetail.getPaymentMethod() == PayBankEnum.AP.getQrCode() || cbDetail.getPaymentMethod() == PayBankEnum.AP_MOBILE.getQrCode() ){//支付宝支付
				// 付款账户
				payReqParam.setFromAccount(financeAccountInfoDto.getSettlementAlipayAccountNumber());// 支付宝中信结算附属账户
			}else if(cbDetail.getPaymentMethod() == PayBankEnum.CB.getQrCode() || cbDetail.getPaymentMethod() == PayBankEnum.CB_MOBILE.getQrCode()){//网银在线支付
				// 付款账户
				payReqParam.setFromAccount(financeAccountInfoDto.getSettlementAccountNumber());// 网银在线中信结算附属账户
			}else if(cbDetail.getPaymentMethod() == PayBankEnum.WX.getQrCode() || cbDetail.getPaymentMethod() == PayBankEnum.WXPC.getQrCode()){//微信支付
				// 付款账户
				payReqParam.setFromAccount(financeAccountInfoDto.getSettlementWXAccountNumber());// 微信中信结算附属账户
			}
			if(cbDetail.getStatus()>=SettleDetailStatusEnum.UnPaying.getCode()){ // 有效的结算明细，此处的else不做处理，是为了线下支付和已冻结状态的处理
				if(cbDetail.getStatus()!=SettleDetailStatusEnum.PayAffirm.getCode()){
					return null;
				}
				totalFee=totalFee.add(cbDetail.getOrderPrice().subtract(cbDetail.getFactorage()).subtract(cbDetail.getRefundMoney()));// 网银在线部分的结算金额=订单总金额-第三方手续费金额-退款金额
			}
		}
		payReqParam.setTotalFee(totalFee);
		payReqParam.setExtraParam("网银在线账户-->卖家冻结账户");
		return payReqParam;
	}
	
	/**
	 * 构建中信支付（卖家冻结账户-->卖家收款账户）,可结算佣金
	 * 
	 * @param settlementDTO - 结算单信息
	 * @param isCommission - 是否是结算佣金
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	protected PayReqParam buildPayReqParam(SettlementDTO settlementDTO,boolean isCommission,
			FinanceAccountInfoDto financeAccountInfoDto){
		PayReqParam payReqParam =new PayReqParam();
		
		// 付款账户
		payReqParam.setFromAccount(settlementDTO.getSellerFrozenAccount()); // 卖家冻结账户
		// 收款账户 
		BigDecimal totalFee =new BigDecimal("0");
		if(isCommission){ // 佣金
			payReqParam.setOutTradeNo(settlementDTO.getPaymentId()+SettlePayNoEnums.FreezeToCommission.ordinal());
			payReqParam.setToAccount(financeAccountInfoDto.getCommissionAccountNumber());// 中信佣金账户
			try{
				payReqParam.setSeller(financeAccountInfoDto.getCommissionAccountName()); 
			}catch (Exception e) {
				return null;
			}
			totalFee=settlementDTO.getCommissionTotalMoney();
			payReqParam.setExtraParam("卖家冻结账户-->佣金账户");
		}else{// 卖家收款账户
			payReqParam.setOutTradeNo(settlementDTO.getPaymentId()+SettlePayNoEnums.FreezeToCash.ordinal());
			payReqParam.setToAccount(settlementDTO.getSellerCashAccount());
			payReqParam.setSeller(settlementDTO.getCompanyName()); // 卖家公司名称
			totalFee=settlementDTO.getSettlementTotalMoney();
			payReqParam.setExtraParam("卖家冻结账户-->卖家收款账户");
		}
		// 交易金额
		payReqParam.setTotalFee(totalFee);
		return payReqParam;
	}
	
	private static Date getIntervalDate(String location){
		Integer interval=Integer.parseInt(SysProperties.getProperty(SysConstants.CITIC_INTERVAL));//2
		String intervalStartDate= SysProperties.getProperty(SysConstants.CITIC_INTERVAL_START_DATE);//2015-03-01
		int dayDiff=DateUtils.dayDiff(new Date(), 
				DateUtils.parseReturnTime(intervalStartDate, DateUtils.YMD_DASH))%interval;
		if("start".equals(location)){
			return DateUtils.dayOffset(new Date(), -dayDiff);
		}else if("end".equals(location)){
			return DateUtils.dayOffset(new Date(), interval-dayDiff-1);
		}else if("settle".equals(location)){
			return DateUtils.dayOffset(new Date(), interval-dayDiff);
		}else{
			return null;
		}
	}
	public  static void main(String[] arg){
		String endTime = DateUtils.getDateStr(DateUtils.dayOffset(new Date(), +1), DateUtils.YMD_DASH);
		Date endDate = DateUtils.parseReturnTime(endTime, DateUtils.YMD_DASH);
		System.out.println(endDate);
		BigDecimal t = new BigDecimal("0.03");
		BigDecimal g = new BigDecimal("0.01");
		System.out.println(t.multiply(g).setScale(2, BigDecimal.ROUND_UP));
	}
}
