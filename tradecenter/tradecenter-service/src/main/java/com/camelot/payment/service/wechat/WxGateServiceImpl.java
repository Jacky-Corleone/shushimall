package com.camelot.payment.service.wechat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.common.util.HttpUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.service.HttpsRequest;
import com.camelot.payment.service.PayService;
import com.camelot.payment.service.wechat.util.RequestHandler;
import com.camelot.payment.service.wechat.util.ResponseHandler;
import com.camelot.payment.service.wechat.util.WeixinPayUtil;
import com.camelot.payment.service.wechat.util.XMLUtil;

@Service("wxGateService")
public class WxGateServiceImpl implements PayService {
	private final static Logger logger = LoggerFactory.getLogger(WxGateServiceImpl.class);
	@Override
	public ExecuteResult<Integer> buildPayForm(PayReqParam payReqParam) {
		logger.info("\n 方法[{}]，入参：[{}]","WxGateServiceImpl-buildPayForm",JSONObject.toJSONString(payReqParam));
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();

		Map<String, String> params = WeixinPayUtil.packInfo();
		String postURL = SysProperties
				.getProperty(SysConstants.WX_UNIFIED_ORDER_URL);
		String noncestr = WeixinPayUtil.getNonceStr();
		String timeStart = WeixinPayUtil.getCurrTime();
		// --sign--
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid", params.get("appid"));// *微信分配的公众账号 ID
		signParams.put("attach", payReqParam.getExtraParam());// 附加数据
		signParams.put("device_info", null);// 微信支付分配的终端设备号
		signParams.put("goods_tag", null);// 商品标记
		signParams.put("mch_id", params.get("mch_id"));// *微信支付分配的商户号
		signParams.put("nonce_str", noncestr);// * 随机字符
		if (PayBankEnum.WX.name().equals(payReqParam.getPayBank().name())) {
			// 小管家微信支付
			signParams.put("notify_url",
					SysProperties.getProperty(SysConstants.WX_NOTIFY_URL));// *接收微信支付成功通知
			signParams.put("trade_type", "JSAPI");// *交易类型 JSAPI、NATIVE、APP													// 通知地址
			signParams.put("body", "网上支付商品");// *描述payReqParam.getSubject()
			signParams.put("openid", payReqParam.getOpenid());// 用户在商户 appid 下的唯一 trade_type 为 JSAPI
			// 时，此参数必传
		} else if (PayBankEnum.WXPC.name().equals(
				payReqParam.getPayBank().name())) {
			// PC端微信支付
			String notify_url = SysConstants.WX_PC_NOTIFY_URL;
			if (payReqParam.getPlatformId() == PlatformEnum.GREEN.getId()) {
				notify_url = SysConstants.GREEN_WX_PC_NOTIFY_URL;
			}
			signParams.put("trade_type", "NATIVE");// *交易类型 JSAPI、NATIVE、APP
			signParams.put("notify_url",SysProperties.getProperty(notify_url));	// *接收微信支付成功通知																// 通知地址
			signParams.put("body", payReqParam.getSubject());// *描述payReqParam.getSubject()
			signParams.put("openid", null);// 用户在商户 appid 下的唯一 trade_type 为 JSAPI
			// 时，此参数必传
		}

		signParams.put("out_trade_no", payReqParam.getOutTradeNo());// *商户订单号
		signParams.put("product_id", payReqParam.getOrderNo());// 只在 trade_type
																// 为
																// NATIVE时需要填写。此
																// id 为二维码中包含的商品
																// ID，商户自行 维护。
		signParams.put("spbill_create_ip", payReqParam.getCustomIp());// *订单生成的机器
																		// IP
		signParams.put("time_expire", null);// 交易结束时间
		signParams.put("time_start", timeStart);// 交易起始时间
		// 换算成分为单位
		Integer amountByFen = payReqParam.getTotalFee()
				.multiply(new BigDecimal("100")).intValue();
		signParams.put("total_fee", amountByFen.toString());// *订单总金额，单位为分
		

		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(params.get("appid"), params.get("partnerKey"));
		String sign = reqHandler.createSign(signParams);

		String xml = WeixinPayUtil.buildTranDataXml(signParams, noncestr,
				timeStart, sign);
		String prepayXml = HttpUtil.httpsPostMethod(postURL, xml);

		try {
			Map<String, String> resultMap = XMLUtil.doXMLParse(prepayXml);
			String return_code = resultMap.get("return_code");
			String result_code = resultMap.get("result_code");
			if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
				if (PayBankEnum.WX.name().equals(payReqParam.getPayBank().name())) {
					result = this.createWXPay(resultMap,result);
				}else{
					result.setResultMessage(resultMap.get("code_url"));
				}
			} else {
				result.addErrorMessage("系统信息：" + resultMap.get("return_msg")
						+ ";error_code：" + resultMap.get("error_code"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		ExecuteResult<HashMap<String,String>> result = new ExecuteResult<HashMap<String,String>>();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		Map<String, String> params = WeixinPayUtil.packInfo();
		String postURL = SysProperties.getProperty(SysConstants.WX_REFUND_URL);
		String noncestr = WeixinPayUtil.getNonceStr();
		// --sign--
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid", params.get("appid"));// *微信分配的公众账号 ID
		signParams.put("device_info", null);// 微信支付分配的终端设备号
		signParams.put("mch_id", params.get("mch_id"));// *微信支付分配的商户号
		signParams.put("nonce_str", noncestr);// * 随机字符
		signParams.put("out_trade_no", refundReqParam.getOutTradeNo());// *商户订单号
		signParams.put("out_refund_no", refundReqParam.getOutRefundNo());// *商户退款单号
		// 换算成分为单位
		Integer total_fee = refundReqParam.getTotalAmount()
				.multiply(new BigDecimal("100")).intValue();
		signParams.put("total_fee", total_fee.toString());// *订单总金额，单位为分
		Integer refund_fee = refundReqParam.getRefundAmount()
				.multiply(new BigDecimal("100")).intValue();
		signParams.put("refund_fee", refund_fee.toString());// *退款金额，单位为分
		signParams.put("refund_fee_type", SysConstants.CB_MONEY_TYPE);// *货币种类
		signParams.put("op_user_id", params.get("mch_id"));// *操作员帐号, 默认为商户号

		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(params.get("appid"), params.get("partnerKey"));
		String sign = reqHandler.createSign(signParams);

		String xml = WeixinPayUtil.buildTranDataXml(signParams, noncestr,
				null, sign);
		String refundXml = null;
		try {
			refundXml = new HttpsRequest().sendPost(postURL, xml);
			logger.info("微信退款响应："+refundXml);
		} catch(Exception e){
			logger.error("微信退款失败，失败原因：",e);
			resultMap.put("state", "2");
			resultMap.put("resultCode", "900");
			resultMap.put("resultMessage", "系统内部异常");
		}
		try {
			Map<String, String> refundResult = XMLUtil.doXMLParse(refundXml);
			String return_code = refundResult.get("return_code");
			String result_code = refundResult.get("result_code");
			if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
				resultMap.put("state", "1");
				result.setResultMessage("SUCCESS");
				logger.info("微信退款成功");
			} else if("FAIL".equals(result_code)){
				resultMap.put("state", "2");
				resultMap.put("resultCode", refundResult.get("err_code"));
				resultMap.put("resultMessage", refundResult.get("err_code_des"));
				logger.error("微信退款失败，失败代码："+refundResult.get("err_code")+",失败原因："+refundResult.get("err_code_des"));
			} else {
				resultMap.put("state", "2");
				resultMap.put("resultCode",return_code);
				resultMap.put("resultMessage", refundResult.get("return_msg"));
				logger.error("微信退款失败，失败代码："+return_code+",失败原因："+refundResult.get("return_msg"));
			}
		} catch (Exception e) {
			logger.error("微信退款失败，失败原因：",e);
			resultMap.put("state", "2");
			resultMap.put("resultCode", "900");
			resultMap.put("resultMessage", "系统内部异常");
		}
		result.setResult(resultMap);
		return result;
	}

	public ExecuteResult<Transations> buildTransatoins(
			Map<String, String> params, String type) {
		ExecuteResult<Transations> result = new ExecuteResult<Transations>();

		Map<String, String> paramsInfo = WeixinPayUtil.packInfo();
		// 创建支付应答对象
		ResponseHandler resHandler = new ResponseHandler(params);
		resHandler.setKey(paramsInfo.get("partnerKey"));
		// 判断签名
		if (resHandler.isTenpaySign()) {
			try {
				String return_code = resHandler.getParameter("return_code");
				String result_code = resHandler.getParameter("result_code");

				if ("SUCCESS".equals(return_code)) { // 系统正确
					Transations transations = new Transations();
					transations.setOutTradeNo(resHandler
							.getParameter("out_trade_no"));
					transations.setTransactionNo(resHandler
							.getParameter("transaction_id"));
					transations.setFromAccount(resHandler
							.getParameter("openid"));
					BigDecimal amountByFen = new BigDecimal(
							resHandler.getParameter("total_fee"));
					BigDecimal amount = amountByFen.multiply(new BigDecimal(
							"0.01"));
					transations.setRealAmount(amount);
					transations.setCompletedTime(DateUtils.parse(
							resHandler.getParameter("time_end"),
							DateUtils.YMDHMS));

					if ("SUCCESS".equals(result_code)) {
						transations
								.setStatus(TransationsStatusEnum.PAID_SUCCESS
										.getCode());
					} else {
						transations.setStatus(TransationsStatusEnum.PAID_FAIL
								.getCode());
					}

					result.setResult(transations);
					return result;
				}
			} catch (Exception e) {
				logger.error("\n 方法[{}]，异常：[{}]", "WxGateServiceImpl-buildTransatoins", e);
			}
		} else{
			logger.error("签名不正确");
		}
		return null;
	}

	public Transations queryTradeInfo(Transations transations) {

		Map<String, String> params = WeixinPayUtil.packInfo();
		String noncestr = WeixinPayUtil.getNonceStr();
		String postURL = SysProperties
				.getProperty(SysConstants.WX_SEARCH_ORDER_URL);

		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appid", params.get("appid"));// *微信分配的公众账号 ID
		signParams.put("mch_id", params.get("mch_id"));// *微信支付分配的商户号
		signParams.put("nonce_str", noncestr);// * 随机字符
		signParams.put("out_trade_no", transations.getOutTradeNo());// *商户订单号

		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(params.get("appid"), params.get("partnerKey"));
		String sign = reqHandler.createSign(signParams);

		StringBuffer xml = new StringBuffer();
		xml.append("<xml>");
		xml.append("<appid>" + params.get("appid") + "</appid>");
		xml.append("<mch_id>" + params.get("mch_id") + "</mch_id>");
		xml.append("<out_trade_no>" + transations.getOutTradeNo()
				+ "</out_trade_no>");
		xml.append("<nonce_str>" + noncestr + "</nonce_str>");
		xml.append("<sign>" + sign + "</sign>");
		xml.append("</xml>");
		String prepayXml = HttpUtil.httpsPostMethod(postURL, xml.toString());

		try {
			Map<String, String> resultMap = XMLUtil.doXMLParse(prepayXml);

			// 创建支付应答对象
			ResponseHandler resHandler = new ResponseHandler(resultMap);
			resHandler.setKey(params.get("partnerKey"));
			// 判断签名
			if (resHandler.isTenpaySign()) {
				String return_code = resHandler.getParameter("return_code");
				String result_code = resHandler.getParameter("result_code");

				if ("SUCCESS".equals(return_code)) { // 系统正确
					Transations tran = new Transations();
					tran.setOutTradeNo(resHandler.getParameter("out_trade_no"));
					BigDecimal amountByFen = new BigDecimal(
							resHandler.getParameter("total_fee"));
					BigDecimal amount = amountByFen.multiply(new BigDecimal(
							"0.01"));
					tran.setRealAmount(amount);
					if ("SUCCESS".equals(result_code)) {
						tran.setStatus(TransationsStatusEnum.PAID_SUCCESS
								.getCode());
					} else {
						tran.setStatus(TransationsStatusEnum.PAID_FAIL
								.getCode());
					}
					return tran;
				} else {
					// result.addErrorMessage("系统信息："+resultMap.get("return_msg")+"；error_code："+resultMap.get("error_code"));
				}
			} else {
				// result.addErrorMessage("系统信息：订单号："+transations.getOutTradeNo()+"-验证签名失败");
			}
		} catch (Exception e1) {
		}
		return null;
	}

	private ExecuteResult<Integer> createWXPay(Map<String, String> resultMap,ExecuteResult<Integer> result){
		String noncestr = WeixinPayUtil.getNonceStr();
		String timeStart = WeixinPayUtil.getCurrTime();
		Map<String, String> params = WeixinPayUtil.packInfo();
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("appId", params.get("appid"));// *微信分配的公众账号 ID
		signParams.put("timeStamp", timeStart);// 时间戳
		signParams.put("nonceStr", noncestr);// 随机数
		signParams.put("signType", "MD5");
		signParams.put("package", "prepay_id="+resultMap.get("prepay_id"));// 订单详情扩展字符串
		RequestHandler reqHandler = new RequestHandler();
		reqHandler.init(params.get("appid"), params.get("partnerKey"));
		String sign = reqHandler.createSign(signParams);
		signParams.put("paySign", sign);
		logger.error("sign:"+sign);
		result.setResultMessage(JSON.toJSONString(signParams));
		return result;
	}
	
}
