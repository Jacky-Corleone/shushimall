package com.camelot.payment.service.alipay;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayJournayStatusEnum;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.service.PayService;
import com.camelot.payment.service.PayServiceFactory;
import com.camelot.payment.service.alipay.util.mobile.AlipayNotify;
import com.camelot.payment.service.alipay.util.mobile.AlipaySubmit;
import com.camelot.payment.service.alipay.util.mobile.UtilDate;
import com.camelot.payment.service.alipay.util.pc.AlipayConfig;
import com.camelot.payment.service.alipay.util.pc.XmlUtils;

@Service("apMobileGateService")
public class AlipayMobileServiceImpl implements PayService {

	private static final Logger logger = LoggerFactory
			.getLogger(AlipayMobileServiceImpl.class);

	public ExecuteResult<Integer> buildPayForm(PayReqParam param){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		// 支付宝网关地址
		try{
			// //////////////////////////////////调用授权接口alipay.wap.trade.create.direct获取授权码token//////////////////////////////////////
			// 返回格式
			String format = "xml";
			// 必填，不需要修改

			// 返回格式
			String v = "2.0";
			// 必填，不需要修改

			// 请求号
			String reqId = UtilDate.getOrderNum();
			// 必填，须保证每次请求都是唯一

			// req_data详细信息

			// 服务器异步通知页面路径
			String notifyUrl = SysProperties
					.getProperty(SysConstants.ALIPAY_M_NOTIFY_URL);
			// 需http://格式的完整路径，不能加?id=123这类自定义参数

			// 页面跳转同步通知页面路径
			String callBackUrl = SysProperties
					.getProperty(SysConstants.ALIPAY_M_RETURN_URL);
			// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

			// 操作中断返回地址
			String merchantUrl = SysProperties
					.getProperty(SysConstants.ALIPAY_M_NOTIFY_URL);
			// 用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数

			// 卖家支付宝帐户
			String sellerEmail = SysProperties
					.getProperty(SysConstants.ALIPAY_M_SELLER_EMAIL);
			// 必填

			// 商户订单号
			String outTradeNo = param.getOutTradeNo();
			// 商户网站订单系统中唯一订单号，必填

			// 订单名称
			String subject = param.getSubject();
			// 必填

			// 付款金额
			String totalFee = param.getTotalFee().toString();
			// 必填

			// 请求业务参数详细
			String reqDataToken = "<direct_trade_create_req><notify_url>"
					+ notifyUrl + "</notify_url><call_back_url>" + callBackUrl
					+ "</call_back_url><seller_account_name>" + sellerEmail
					+ "</seller_account_name><out_trade_no>" + outTradeNo
					+ "</out_trade_no><subject>" + subject
					+ "</subject><total_fee>" + totalFee
					+ "</total_fee><merchant_url>" + merchantUrl
					+ "</merchant_url></direct_trade_create_req>";
			
			logger.error("1reqDataToken:"+reqDataToken);

			// 把请求参数打包成数组
			Map<String, String> sParaTempToken = new HashMap<String, String>();
			sParaTempToken.put("service", "alipay.wap.trade.create.direct");
			sParaTempToken.put("partner", AlipayConfig.PARTNER_MOBILE);
			sParaTempToken.put("_input_charset", AlipayConfig.INPUT_CHARSET);
			sParaTempToken.put("sec_id", AlipayConfig.SIGN_TYPE);
			sParaTempToken.put("format", format);
			sParaTempToken.put("v", v);
			sParaTempToken.put("req_id", reqId);
			sParaTempToken.put("req_data", reqDataToken);
			// 建立请求
			String sHtmlTextToken = AlipaySubmit.buildRequest(
					"http://wappaygw.alipay.com/service/rest.htm?", "","",
					sParaTempToken);
			// URLDECODE返回的信息
			sHtmlTextToken = URLDecoder.decode(sHtmlTextToken,
					AlipayConfig.INPUT_CHARSET);
			logger.error("3支付宝手机支付：请求token：sHtmlTextToken=======  " + sHtmlTextToken);
			// 获取token
			String requestToken = AlipaySubmit.getRequestToken(sHtmlTextToken);

			logger.error("4支付宝手机支付：请求token：requestToken=======  "+requestToken);
			// out.println(request_token);

			// //////////////////////////////////根据授权码token调用交易接口alipay.wap.auth.authAndExecute//////////////////////////////////////

			// 业务详细
			String reqData = "<auth_and_execute_req><request_token>" + requestToken
					+ "</request_token></auth_and_execute_req>";
			// 必填

			// 把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
			sParaTemp.put("service", "alipay.wap.auth.authAndExecute");
			sParaTemp.put("partner", AlipayConfig.PARTNER_MOBILE);
			sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
			sParaTemp.put("sec_id", AlipayConfig.SIGN_TYPE);
			sParaTemp.put("format", format);
			sParaTemp.put("v", v);
			sParaTemp.put("req_data", reqData);
			// 建立请求
			String sHtmlText = AlipaySubmit.buildRequest(
					"http://wappaygw.alipay.com/service/rest.htm?", sParaTemp,
					"get", "");
			logger.error("6支付宝手机支付：sHtmlText ========   "+sHtmlText);
			result.setResultMessage(sHtmlText);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			//result.addErrorMessage("生成支付链接失败");
		}
		return result;
	}

	

	public Transations queryTradeInfo(Transations trans) {
		// 必填 //商户订单号
		String out_trade_no = trans.getOutTradeNo();
		// 必填 //支付宝交易号
		String trade_no = trans.getTransactionNo();
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "single_trade_query");
		sParaTemp.put("partner", AlipayConfig.PARTNER_MOBILE);
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("out_trade_no",out_trade_no );//"20141107002"
		sParaTemp.put("trade_no",trade_no );//"2014111500001000820035840385"
		// 建立请求
		String responseStr;
		try {
			responseStr = com.camelot.payment.service.alipay.util.pc.AlipaySubmit.buildRequest("", "", sParaTemp);
			return this.parseStatusFromResponse(responseStr,trans);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解析返回数据
	 * 
	 * @param sHtmlText
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private Transations parseStatusFromResponse(String sHtmlText,Transations tran) throws Exception {
		Map<String, Object> map = XmlUtils.xmlStr2Map(sHtmlText, "utf-8");
		String successFlag = (String) map.get("is_success");
		String sign = (String) map.get("sign");
		String error = (String) map.get("error");
		Map<String, Object> response = (Map<String, Object>) map
				.get("response");
		Map<String, Object> trade = (Map<String, Object>) response.get("trade");
		String buyerEmail = (String) trade.get("buyer_email");
		String buyerId = (String) trade.get("buyer_id");
		String isTotalFeeAdjust = (String) trade.get("is_total_fee_adjust");
		String outTradeNo = (String) trade.get("out_trade_no");//对外交易号
		String tradeNo = (String) trade.get("trade_no");//支付宝交易号
		String totalFee = (String) trade.get("total_fee");
		// TODO 判断签名是否正确,封装对象
		tran.setTransactionNo(tradeNo);
		tran.setRealAmount(new BigDecimal(totalFee));
		tran.setBuyer(buyerEmail);
		tran.setFromAccount(buyerId);
		if(StringUtils.isNotEmpty(successFlag) && "success".equals(successFlag)){
			tran.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
		}else if(StringUtils.isNotEmpty(successFlag) && "fail".equals(successFlag)){
			tran.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
		}else {
			return null;
		}
		return tran;
	}

	public ExecuteResult<Transations> buildTransatoins(Map<String, String> param, String type) {
		ExecuteResult<Transations> result =new ExecuteResult<Transations>();
		try {
			if(PayJournayStatusEnum.SYNCHRONOUS_NOTICE.getCode().equals(type)){
				result.setResult(this.buildReturnTransatoins(param));
			}else if(PayJournayStatusEnum.ASYNCHRONOUS_NOTICE.getCode().equals(type)){
				result.setResult(this.buildNotifyTransatoins(param));
	 		}else{
	 			result.addErrorMessage("类型错误，不能解析");
	 		}
		} catch (Exception e) {
			result.addErrorMessage("解析失败");
		}
		return result;
	}



	/**
	 * 构建异步通知返回参数
	 * @param param
	 * @return
	 */
	private Transations buildNotifyTransatoins(Map<String, String> param) throws Exception{
		//验证签名
		/*if (!AlipayNotify.verifyNotify(param)) {
			return null;
		}*/
		Transations reTran = new Transations();

		//解密（如果是RSA签名需要解密，如果是MD5签名则下面一行清注释掉）
//		Map<String,String> decrypt_params = AlipayNotify.decrypt(param);
		//XML解析notify_data数据
		Document doc = DocumentHelper.parseText(param.get("notify_data"));
		reTran.setCompletedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(doc.selectSingleNode( "//notify/gmt_payment" ).getText()));//交易付款时间
		//商户订单号
		reTran.setOutTradeNo(doc.selectSingleNode( "//notify/out_trade_no" ).getText());
 		//支付宝交易号
		reTran.setTransactionNo(doc.selectSingleNode( "//notify/trade_no" ).getText());
		//交易状态
		String tradeStatus = doc.selectSingleNode( "//notify/trade_status" ).getText();
		if (tradeStatus.equals("TRADE_FINISHED")
				|| tradeStatus.equals("TRADE_SUCCESS")) {// 返回信息 订单支付成功
			reTran.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
		} else {// 订单支付失败
			reTran.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
		}
		//卖家支付宝用户号
		reTran.setToAccount(doc.selectSingleNode( "//notify/seller_id" ).getText());
		//交易金额
		reTran.setRealAmount(new BigDecimal(doc.selectSingleNode( "//notify/total_fee" ).getText()));
		//卖家支付宝账号
		reTran.setSeller(doc.selectSingleNode( "//notify/seller_email" ).getText());
		//买家支付宝账号
		reTran.setBuyer(doc.selectSingleNode( "//notify/buyer_email" ).getText());
		//买家支付宝用户号 
		reTran.setFromAccount(doc.selectSingleNode( "//notify/buyer_id" ).getText());
		return reTran;
	}
	
	/**
	 * 构建同步通知返回参数
	 * @param param
	 * @return
	 */
	private Transations buildReturnTransatoins(Map<String, String> param)  throws Exception{
		//验证签名
		if (!AlipayNotify.verifyReturn(param)) {
			return null;
		}
		Transations reTran = new Transations();
		reTran.setOutTradeNo(param.get("out_trade_no"));//对外交易号
		reTran.setTransactionNo(param.get("trade_no")) ;//支付宝交易号
		if("success".equals(param.get("result"))){
			reTran.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
		}else{
			reTran.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
		}
		reTran.setCompletedTime(new Date());//交易付款时间
		reTran.setIgnore(true);// 忽略对银行结果的数据库处理
		return reTran;
	}



	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		// 基于手机端与PC端支付宝账号信息一致
		return PayServiceFactory.getPayServiceInstance(PayBankEnum.AP.name()).buildRefundForm(refundReqParam);
	}
	
}
