package com.camelot.payment.service.alipay;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayJournayStatusEnum;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.util.AlipayErrorCodeProperties;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.service.PayService;
import com.camelot.payment.service.alipay.util.pc.AlipayConfig;
import com.camelot.payment.service.alipay.util.pc.AlipayCore;
import com.camelot.payment.service.alipay.util.pc.AlipayNotify;
import com.camelot.payment.service.alipay.util.pc.AlipaySubmit;
import com.camelot.payment.service.alipay.util.pc.UtilDate;
import com.camelot.payment.service.alipay.util.pc.XmlUtils;

@Service("alipayGateService")
public class AlipayServiceImpl implements PayService {

	public ExecuteResult<Integer> buildPayForm(PayReqParam param) {
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		// 支付类型
		String payment_type = "1";
		// 必填，不能修改
		// 服务器异步通知页面路径
		String notify_url = SysProperties
				.getProperty(SysConstants.ALIPAY_NOTIFY_URL);
		if (param.getPlatformId() == PlatformEnum.GREEN.getId()) {
			notify_url = SysProperties
					.getProperty(SysConstants.GREEN_ALIPAY_NOTIFY_URL);
		}

		// 需http://格式的完整路径，不能加?id=123这类自定义参数 //页面跳转同步通知页面路径
		String return_url = SysProperties
				.getProperty(SysConstants.ALIPAY_RETURN_URL);
		if (param.getPlatformId() == PlatformEnum.GREEN.getId()) {
			return_url = SysProperties
					.getProperty(SysConstants.GREEN_ALIPAY_RETURN_URL);
		}
		// if(PayBankEnum.AP.getQrCode().toString().equals(param.getQrPayMode())){//页面嵌套二维码显示，支付成功后跳转中间页面，刷新父页面
		// return_url = SysProperties
		// .getProperty(SysConstants.ALIPAY_QR_RETURN_URL);
		// }else{
		// return_url = SysProperties
		// .getProperty(SysConstants.ALIPAY_RETURN_URL);
		// }

		// 需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/ //卖家支付宝帐户
		String seller_email = SysProperties
				.getProperty(SysConstants.ALIPAY_SELLER_EMAIL);
		// 必填 //商户订单号
		String out_trade_no = param.getOutTradeNo();
		// 商户网站订单系统中唯一订单号，必填 //订单名称
		String subject = param.getSubject();
		// 必填 //付款金额
		String total_fee = param.getTotalFee().toString();
		// 必填 //订单描述 String body = new
		// String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
		// 商品展示地址
		String show_url = SysProperties
				.getProperty(SysConstants.ALIPAY_SHOW_URL);
		// 需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html //防钓鱼时间戳
		String anti_phishing_key = "";
		// 若要使用请调用类文件submit中的query_timestamp函数 //客户端的IP地址
		String exter_invoke_ip = "";
		// 非局域网的外网IP地址，如：221.0.0.1

		Map<String, String> sParaTemp = new HashMap<String, String>();
		// //////////////////////////////////银行网关请求参数//////////////////////////////////////
		// 选择其他银行，调用支付宝网银接口
		if (PayBankEnum.OTHER.name().equals(param.getPayBank())) {
			sParaTemp.put("paymethod", "bankPay");
			sParaTemp.put("defaultbank", param.getDefaultBank());
		}
		// 必填，银行简码请参考接口技术文档
		// ////////////////////////////////////////////////////////////////////////////////
		// 把请求参数打包成数组
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("notify_url", notify_url);
		sParaTemp.put("return_url", return_url);
		sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("subject", subject);
		sParaTemp.put("total_fee", total_fee);
		// sParaTemp.put("body", body);
		// sParaTemp.put("show_url", show_url);
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		// sParaTemp.put("qr_pay_mode", "2");
		// 建立请求
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
		result.setResultMessage(sHtmlText);
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
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		sParaTemp.put("out_trade_no", out_trade_no);// "20141107002"
		sParaTemp.put("trade_no", trade_no);// "2014111500001000820035840385"
		// 建立请求
		String responseStr;
		try {
			responseStr = AlipaySubmit.buildRequest("", "", sParaTemp);
			ExecuteResult<Transations> result = this.parseStatusFromResponse(
					responseStr, trans);
			return result.getResult();
		} catch (Exception e) {
		}
		return trans;
	}

	/**
	 * 解析返回数据
	 * 
	 * @param sHtmlText
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	private ExecuteResult<Transations> parseStatusFromResponse(
			String sHtmlText, Transations tran) throws Exception {
		ExecuteResult<Transations> result = new ExecuteResult<Transations>();
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
		String outTradeNo = (String) trade.get("out_trade_no");// 对外交易号
		String tradeNo = (String) trade.get("trade_no");// 支付宝交易号
		String totalFee = (String) trade.get("total_fee");
		// 判断签名是否正确,封装对象
		tran.setTransactionNo(tradeNo);
		tran.setRealAmount(new BigDecimal(totalFee));
		tran.setBuyer(buyerEmail);
		tran.setFromAccount(buyerId);
		if (StringUtils.isNotEmpty(successFlag) && "T".equals(successFlag)) {
			tran.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
			result.setResult(tran);
		} else if (StringUtils.isNotEmpty(successFlag)
				&& "F".equals(successFlag)) {
			tran.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
			result.setResult(tran);
		} else {
			result.addErrorMessage("失败");
			return result;
		}

		return result;
	}

	public ExecuteResult<Transations> buildTransatoins(
			Map<String, String> param, String type) {
		ExecuteResult<Transations> result = new ExecuteResult<Transations>();
		// 验证签名
		if (!AlipayNotify.verify(param)) {
			result.addErrorMessage("解析交易流程控制对象失败");
		} else {
			Transations reTran = new Transations();
			reTran.setOutTradeNo(param.get("out_trade_no"));// 对外交易号
			reTran.setTransactionNo(param.get("trade_no"));// 支付宝交易号
			reTran.setBuyer(param.get("buyer_email"));// 买家支付宝账号
			reTran.setFromAccount(param.get("buyer_id"));// 买家支付宝账户号
			if (PayJournayStatusEnum.SYNCHRONOUS_NOTICE.getCode().equals(type)) {
				reTran.setCompletedTime(new Date());// 交易付款时间
			} else if (PayJournayStatusEnum.ASYNCHRONOUS_NOTICE.getCode()
					.equals(type)) {
				try {
					reTran.setCompletedTime(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").parse(param
							.get("gmt_payment")));// 交易付款时间
				} catch (ParseException e) {
					reTran.setCompletedTime(new Date());
				}
			}
			reTran.setRealAmount(new BigDecimal(param.get("total_fee")));// 实际支付金额
			String tradeStatus = param.get("trade_status");// 交易状态
			if ("TRADE_FINISHED".equals(tradeStatus)
					|| "TRADE_SUCCESS".equals(tradeStatus)) {
				reTran.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
			} else {
				reTran.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
			}
			result.setResult(reTran);
		}
		return result;
	}

	public static void main(String[] args) {
		Integer a = 0;
		System.out.println(a.toString().equals("0"));
	}

	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		// TODO 退款申请
		ExecuteResult<HashMap<String,String>> result = new ExecuteResult<HashMap<String,String>>();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> sParaTemp = new HashMap<String, String>();
//		sParaTemp.put("service", "refund_fastpay_by_platform_nopwd");//无密退款
		
		sParaTemp.put("service", "refund_fastpay_by_platform_pwd");//有密退款
		
		sParaTemp.put("partner", AlipayConfig.PARTNER);
		
		sParaTemp.put("_input_charset", AlipayConfig.INPUT_CHARSET);
		
		sParaTemp.put("sign_type", AlipayConfig.SIGN_TYPE);//有密退款
		
		sParaTemp.put("seller_user_id", AlipayConfig.PARTNER);//有密退款 签名
		
		String seller_email = SysProperties.getProperty(SysConstants.ALIPAY_SELLER_EMAIL);
		sParaTemp.put("seller_email", seller_email);
		
		// TODO notify_url
		String notify_url = SysProperties.getProperty(SysConstants.ALIPAY_REFUND_NOTIFY_URL_PWD);//有密退款回调地址
		if(null == notify_url || "".equals(notify_url)){
			notify_url = SysProperties.getProperty(SysConstants.ALIPAY_REFUND_NOTIFY_URL).replace("refundResult", "apRefundResult");
		}
		sParaTemp.put("notify_url", notify_url);//服务器异步通知页面路径
		
		sParaTemp.put("refund_date", UtilDate.getDateFormatter());

		sParaTemp.put("batch_no", UtilDate.getDate() + refundReqParam.getOutRefundNo());
		sParaTemp.put("batch_num", "1");
		String amount = String.valueOf(refundReqParam.getRefundAmount().setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		String detail_data = refundReqParam.getTransactionNo() + "^"
				+ amount + "^"
				+ refundReqParam.getRefundReason();
		sParaTemp.put("detail_data", detail_data);
		
		//获取签名 组成字符串
		sParaTemp = AlipaySubmit.buildRequestPara(sParaTemp);//有密退款
		//返回结果为url
		String url = AlipayConfig.ALIPAY_GATEWAY_NEW + AlipayCore.createLinkString(sParaTemp);//有密退款
		resultMap.put("url", url);//有密退款
		resultMap.put("state", "3");//申请成功
		// 建立请求 无密退款	
//		String sHtmlText = null;
//		try {
//			sHtmlText = AlipaySubmit.buildRequest("", "", sParaTemp);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// 提交退款请求后，支付宝没有返回结果
//		if (StringUtils.isEmpty(sHtmlText)) {
//			String error = "支付宝服务器异常,请稍后再试！";
//			result.addErrorMessage(error);
//			return result;
//		}
//		Document dom = null;
//		try {
//			dom = DocumentHelper.parseText(sHtmlText);
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Element root = dom.getRootElement();
//		String isSuccess = root.element("is_success").getText();
//		// 退款成功
//		if ("T".equals(isSuccess)) {
//			result.setResultMessage("true");
//			resultMap.put("state", "3");//申请成功
//		} else {// 退款失败
//				// 获取失败原因
//			String error = root.element("error").getText();
//			result.setResultMessage("false");
//			result.addErrorMessage(error);
//			resultMap.put("state", "4");//申请失败
//			resultMap.put("resultCode", error);
//			resultMap.put("resultCode", AlipayErrorCodeProperties.getProperty(error));
//		}
		result.setResult(resultMap);
		return result;
	}
	
	//获取签名
	
	
	
}
