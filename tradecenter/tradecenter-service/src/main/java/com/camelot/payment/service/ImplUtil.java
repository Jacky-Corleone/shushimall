package com.camelot.payment.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.common.enums.SystemKey;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CompanyPayJobDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.OrderItemPay;
import com.camelot.payment.dto.PayReqParam;

/**
 *  Impl中所要用到的封装，验证等方法
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-27
 */
public class ImplUtil {
	
	/**
	 * 获取指定银行的service对象
	 * @param payBank
	 * @return
	 */
	protected PayService getPayService(String payBank) {
		return PayServiceFactory.getPayServiceInstance(payBank);
	}
	/**
	 * 验证payIndex,初始化添加，没有父单的子单不存在
	 * 
	 * @param payReq
	 * @return
	 */
	protected ExecuteResult<Integer> verifyPayIndex(PayReqParam payReq){
		ExecuteResult<Integer> result= new ExecuteResult<Integer>();
		
		if(payReq!=null){
			
			// 系统编码验证
			if(StringUtils.isBlank(payReq.getSystem())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "系统编码"));
				return result;
			}
			
			// 签名验证
			Map<String, String> params =new HashMap<String, String>();
			params.put("system",payReq.getSystem());
			params.put("orderNo", payReq.getOrderNo());
			params.put("totalFee", payReq.getTotalFee().toString());
			if(StringUtils.isBlank(payReq.getSign())||
					(!Signature.verifySign(payReq.getSign(),params, SysProperties.getProperty(SystemKey.getOpenKeyPri(payReq.getSystem()))))){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Signature_Error, ""));
				return result;
			}
			
			// 参数验证
			if(StringUtils.isBlank(payReq.getOrderNo())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "orderNo"));
			}else if(payReq.getTotalFee()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "totalFee"));
			}else{
				int compareResult = payReq.getTotalFee().compareTo(new BigDecimal(0));
				if(compareResult<0){			
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "totalFee不能小于0"));
				}
			}
			if(payReq.getOrderItemPays()==null||payReq.getOrderItemPays().size()==0){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "childList"));
			}else{
				// 校验父单金额与子单金额和的一致性
				BigDecimal totle = new BigDecimal("0");
				for (OrderItemPay orderItemPay:payReq.getOrderItemPays()) {
					if(orderItemPay.getSubOrderPrice()==null){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "[parent]:子单subOrderPrice"));
						return result;
					}else if(orderItemPay.getSubOrderId()==null){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "[parent]:子单subOrderId"));
						return result;
					}else if(orderItemPay.getSellerId()==null){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "[parent]:子单sellerId"));
						return result;
					}
					totle=totle.add(orderItemPay.getSubOrderPrice());
				}
				if(payReq.getTotalFee().compareTo(totle)!=0){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "订单总金额和子订单金额总和不一致"));
				}
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	/**
	 * 验证pay
	 * 
	 * @param payReq
	 * @return
	 */
	protected ExecuteResult<Integer> verifyPay(PayReqParam payReq){
		ExecuteResult<Integer> result= new ExecuteResult<Integer>();
		if(payReq!=null){
			if(StringUtils.isBlank(payReq.getOrderNo())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "orderNo"));
			}else if(payReq.getPayBank()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "payBank"));
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	/**
	 * 验证outTransfer
	 * 
	 * @param accountInfoDto
	 * @return
	 */
	protected ExecuteResult<String> verifyOutTransfer(AccountInfoDto accountInfoDto){
		ExecuteResult<String> result= new ExecuteResult<String>();
		if(accountInfoDto!=null){
			// 系统编码验证
			if(StringUtils.isBlank(accountInfoDto.getSystem())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "系统编码"));
				return result;
			}
			
			// 签名验证
			Map<String, String> params =new HashMap<String, String>();
			params.put("system",accountInfoDto.getSystem());
			params.put("uid", accountInfoDto.getUserId().toString());
			params.put("accType", accountInfoDto.getAccType().name());
			params.put("withdrawPrice",accountInfoDto.getWithdrawPrice().toString());
			if(StringUtils.isBlank(accountInfoDto.getSign())||
					(!Signature.verifySign(accountInfoDto.getSign(),params, SysProperties.getProperty(SystemKey.getOpenKeyPri(accountInfoDto.getSystem()))))){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Signature_Error, ""));
				return result;
			}
						
			if(accountInfoDto.getUserId()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "userId"));
			}else if(accountInfoDto.getWithdrawPrice()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "withdrawPrice"));
			}else if(accountInfoDto.getAccType()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "accType"));
			}else if(StringUtils.isNotBlank(accountInfoDto.getBindingAccNo())){
				if(StringUtils.isBlank(accountInfoDto.getBindingAccNm())||accountInfoDto.getSameBank()==null
						||(StringUtils.isBlank(accountInfoDto.getBankName())&&StringUtils.isBlank(accountInfoDto.getBankBranchJointLine()))){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "当BindingAccNo存在：bindingAccNm,sameBank必须并存;bankName/bankBranchJointLine至少填一项."));
				}
				if(StringUtils.isNotBlank(accountInfoDto.getBankBranchJointLine())){
					if(accountInfoDto.getBankBranchJointLine().length()>12){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "bankBranchJointLine不得超过12位"));
					}
					if(StringUtils.isNumeric(accountInfoDto.getBankBranchJointLine())){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "bankBranchJointLine非法账户"));
					}
				}
				if(StringUtils.isNotBlank(accountInfoDto.getBankName())&&accountInfoDto.getBankName().length()>50){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "bankName不得超过50位"));
				}
			}else{
				int compareResult = new BigDecimal("0").compareTo(accountInfoDto.getWithdrawPrice());
				if(compareResult>=0){			
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "withdrawPrice必须大于0"));
				}
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	/**
	 * 验证pay
	 * 
	 * @param payReq
	 * @return
	 */
	protected ExecuteResult<String> verifySaveComPayJob(CompanyPayJobDTO companyPayJobDTO){
		ExecuteResult<String> result= new ExecuteResult<String>();
		if(companyPayJobDTO!=null){
			if(StringUtils.isBlank(companyPayJobDTO.getOutTradeNo())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "outTradeNo"));
			}else if(companyPayJobDTO.getUserId()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "userId"));
			}else if(companyPayJobDTO.getAccType()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "accType"));
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	/**
	 * 方法：【pay】-从交易记录取得必要信息 重新构建出参
	 * 
	 * @param dbTran
	 * @return
	 */
	protected PayReqParam buildPayReqParam(PayReqParam param, Transations dbTran) {
		
		param.setOutTradeNo(dbTran.getOutTradeNo());
		param.setOrderNo(dbTran.getOrderNo());
		param.setSubject(dbTran.getSubject());
		param.setTotalFee(dbTran.getOrderAmount());
		
		param.setSeller(dbTran.getSeller());
		param.setBuyer(dbTran.getBuyer());
		param.setOrderDetails(dbTran.getOrderDetails());
		param.setPlatformId(dbTran.getPlatformId());
		
		return param;
	}
	
	/**
	 * 方法：【payResult】【findTransListByOutTrades】-构建回传支付结果出参
	 * 
	 * @param transations
	 * @param isIgnore - 是否忽略业务处理结果
	 * @param status - isIgnore为true时有效，代表银行通知结果
	 * @return
	 */
	protected OrderInfoPay buildOrderInfo(Transations tranQuery,boolean isIgnore,Integer status) {
		
		OrderInfoPay orderInfo = new OrderInfoPay();
		orderInfo.setOutTradeNo(tranQuery.getOutTradeNo());
		orderInfo.setOrderNo(tranQuery.getOrderNo());
		orderInfo.setSubject(tranQuery.getSubject());
		orderInfo.setPayBank(tranQuery.getPayBank());
		orderInfo.setOrderAmount(tranQuery.getOrderAmount());
		orderInfo.setSeller(tranQuery.getSeller());
		orderInfo.setBuyer(tranQuery.getBuyer());
		if(isIgnore){  // 针对忽略对银行结果处理的对象处理方式
			orderInfo.setStatus(status);
		}else{
			orderInfo.setStatus(tranQuery.getStatus());
		}
		orderInfo.setCompletedTime(tranQuery.getCompletedTime());
		orderInfo.setCreatedTime(tranQuery.getCreatedTime());
		
		orderInfo.setDeliveryType(tranQuery.getDeliveryType());
		orderInfo.setOrderDetails(tranQuery.getOrderDetails());
		
		return orderInfo;
	}
	
	
}
