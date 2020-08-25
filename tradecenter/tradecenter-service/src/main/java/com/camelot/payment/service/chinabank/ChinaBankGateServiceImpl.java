package com.camelot.payment.service.chinabank;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.common.util.HttpUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.service.PayService;
import com.camelot.payment.service.chinabank.refund.util.BASE64;
import com.camelot.payment.service.chinabank.refund.util.DES;
import com.camelot.payment.service.chinabank.refund.util.HttpsClientUtil;
import com.camelot.payment.service.chinabank.refund.util.RSAUtil;

@Service("chinaBankGateService")
public class ChinaBankGateServiceImpl implements PayService {
	private final static Logger logger = LoggerFactory.getLogger(ChinaBankGateServiceImpl.class);

	public ExecuteResult<Integer> buildPayForm(PayReqParam payReqParam) {
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		
		Map<String, String> paramsInfo=ChinaBankUtil.packInfo();
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("v_md5info",ChinaBankUtil.Signature(payReqParam,paramsInfo));// 加密
		signParams.put("v_mid",paramsInfo.get(SysConstants.CB_MER_ID));// 商户号
		signParams.put("v_oid",payReqParam.getOutTradeNo());// 订单号
		signParams.put("v_amount",payReqParam.getTotalFee().toString());// 订单金额
		signParams.put("v_moneytype",SysConstants.CB_MONEY_TYPE);// 币种
		if(payReqParam.getPlatformId() == null){
			signParams.put("v_url",SysProperties.getProperty(SysConstants.CB_RETURN_URL));// 同步通知地址
			signParams.put("remark2","[url:="+SysProperties.getProperty(SysConstants.CB_NOTIFY_URL)+"]");// 异步通知地址
		}else if(payReqParam.getPlatformId() == PlatformEnum.GREEN.getId()){
			signParams.put("v_url",SysProperties.getProperty(SysConstants.GREEN_CB_RETURN_URL));// 同步通知地址
			signParams.put("remark2","[url:="+SysProperties.getProperty(SysConstants.GREEN_CB_NOTIFY_URL)+"]");// 异步通知地址
		}
		

//		//以下几项项为网上支付完成后，随支付反馈信息一同传给信息接收页 
//		signParams.put("remark1","");// 自定义值
//		signParams.put("pmode_id","");// 网银在线定义的银行编码
//
//		//以下几项只是用来记录客户信息，可以不用，不影响支付 
//		signParams.put("v_rcvname","");// 收货人姓名
//		signParams.put("v_rcvaddr","");// 收货人地址
//		signParams.put("v_rcvtel" ,"");// 收货人电话
//		signParams.put("v_rcvpost"  ,"");// 收货人邮编
//		signParams.put("v_rcvemail" ,"");// 收货人Email
//		signParams.put("v_rcvmobile" ,"");// 收货人手机号
//		signParams.put("v_ordername" ,"");// 订货人姓名
//		signParams.put("v_orderaddr","");// 订货人地址
//		signParams.put("v_ordertel" ,"");// 订货人电话
//		signParams.put("v_orderpost","");// 订货人邮编
//		signParams.put("v_orderemail","");// 订货人Email
//		signParams.put("v_ordermobile","");// 订货人手机号
		result.setResultMessage(ChinaBankUtil.buildRequest(signParams, SysProperties.getProperty(SysConstants.CB_PAY_URL), "submit"));
		return result;
	}

	@Override
	public ExecuteResult<Transations> buildTransatoins(Map<String, String> params,String type){
		ExecuteResult<Transations> result =new ExecuteResult<Transations>();
		Map<String, String> paramsInfo=ChinaBankUtil.packInfo();
		
		String v_oid = params.get("v_oid");		// 订单号
	    String v_pstatus = params.get("v_pstatus");	// 支付结果，20支付完成；30支付失败；
		String v_amount = params.get("v_amount");		// 订单实际支付金额
		String v_moneytype = params.get("v_moneytype");	// 币种
//		String remark1 = params.get("remark1");		// 备注1
//		String remark2 = params.get("remark2");		// 备注2
		String text = v_oid+v_pstatus+v_amount+v_moneytype+paramsInfo.get(SysConstants.CB_PRI_KEY);
		String v_md5text =ChinaBankUtil.getMD5ofStr(text);
		// 判断签名
		 if (params.get("v_md5str").equals(v_md5text)){
			Transations transations= new Transations();
			transations.setOutTradeNo(v_oid);
			transations.setRealAmount(new BigDecimal(v_amount));
			transations.setCompletedTime(new Date());
			if ("20".equals(v_pstatus)){ // 支付完成 回传ok代表异步停止通知
				transations.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
			}else{
				transations.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
			}
			result.setResult(transations);
		}else{
			result.addErrorMessage("解析交易流程控制对象失败");
		} 
		return result;
	}
	
	public Transations queryTradeInfo(Transations transations){
//		Map<String, String> paramsInfo=ChinaBankUtil.packInfo();
//		SortedMap<String, String> signParams = new TreeMap<String, String>();
//		signParams.put("v_oid",transations.getOutTradeNo());// 订单号
//		signParams.put("v_mid",paramsInfo.get(SysConstants.CB_MER_ID));// 商户号
//		signParams.put("billNo_md5",ChinaBankUtil.getMD5ofStr(transations.getOutTradeNo()+paramsInfo.get(SysConstants.CB_PRI_KEY)));// 加密
//		signParams.put("v_url",SysProperties.getProperty(SysConstants.CB_SEARCH_RETURN_URL));// 回传路径
//		HttpUtil.http(SysProperties.getProperty(SysConstants.CB_SEARCH_ORDER_URL), signParams);
		return transations;
	}

	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		logger.info("\n 方法[{}]，入参：[{}]","ChinaBankGateServiceImpl-buildRefundForm",JSON.toJSONString(refundReqParam));
		ExecuteResult<HashMap<String,String>> result = new ExecuteResult<HashMap<String,String>>();
		HashMap<String,String> resultMap = new HashMap<String,String>();
		//-------------------------组装退款数据--------------------------//
		String refundJson = this.createRefundData(refundReqParam);
        
        try{
        	//-------------------------加密退款数据--------------------------//
			String agreementKey=SysProperties.getProperty(SysConstants.CB_M_MD5_PRI_KEY);//协议秘钥
//			String base64AgreementKey = BASE64.encode(agreementKey.getBytes("UTF-8"));
//			String refundCiphertext = DESUtil.encrypt(refundJson, base64AgreementKey, "UTF-8");
			String base64AgreementKey = BASE64.encode(agreementKey.getBytes("UTF-8"));
			String refundCiphertext = DES.encrypt(refundJson, base64AgreementKey, "UTF-8");
			//-------------------------加密退款数据--------------------------//
			
			logger.info("\n 组装退款报文");
			Map<String,String> refundParams = this.createRefundParams(refundCiphertext, refundJson, base64AgreementKey);
			logger.info("\n 发送的数据是："+JSON.toJSONString(refundParams));
			String refundUrl = SysProperties.getProperty(SysConstants.CB_REFUND_INTERFACE_URL);
			String returnHttp = HttpsClientUtil.sendPost(refundUrl,refundParams);
			
			logger.info("\n 响应的数据是："+returnHttp);
			HashMap<String,String> refundResult = JSON.parseObject(returnHttp, HashMap.class);
			String kcipher = refundResult.get("code");
			if(kcipher.equals("00000")){
				result.setResultMessage("SUCCESS");
				resultMap.put("state", "1");
			}else{
				resultMap.put("state", "2");
				resultMap.put("resultCode", refundResult.get("code"));
				resultMap.put("resultMessage", refundResult.get("message"));
				logger.info("\n 网银在线退款失败,code："+refundResult.get("code")+",message:"+refundResult.get("message"));
				result.addErrorMessage("网银在线退款失败,code："+refundResult.get("code")+",message:"+refundResult.get("message"));
			}
		}catch(Exception e){
			resultMap.put("state", "2");
			resultMap.put("resultCode", "900");
			resultMap.put("resultMessage", "系统内部异常");
			logger.error("\n 网银在线退款失败,失败原因：",e);
			result.addErrorMessage("\n 网银在线退款失败");
		}
        result.setResult(resultMap);
		return result;
	}
	/**
	 * 组装退款数据
	 * @return
	 * @author zhouzhijun
	 * @since 2015-10-08
	 */
	private String createRefundData(RefundReqParam refundReqParam){
		logger.info("\n 方法[{}]，入参：[{}]","ChinaBankGateServiceImpl-createRefundData",JSON.toJSONString(refundReqParam));
		Map<String,String> refundData = new HashMap<String,String>();
        refundData.put("order",    refundReqParam.getOutTradeNo());   //商户订单号
        refundData.put("password", SysProperties.getProperty(SysConstants.CB_TRANS_PWD));     //交易密码
        refundData.put("username", SysProperties.getProperty(SysConstants.CB_USER_NAME));   //用户名
        refundData.put("merchant", SysProperties.getProperty(SysConstants.CB_MER_ID));   //商户号
        String amount = String.valueOf(refundReqParam.getRefundAmount().setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        refundData.put("amount",   amount);        //退款金额
        refundData.put("desc",     refundReqParam.getDesc());       //退款描述
        Map<String,Object> refundDataWraper = new HashMap<String,Object>();
        refundDataWraper.put("serviceParam", refundData);
		String refundJson = JSON.toJSONString(refundDataWraper);
		logger.info("\n 方法[{}]，出参：[{}]","ChinaBankGateServiceImpl-createRefundData",JSON.toJSONString(refundJson));
		return refundJson;
	}
	/**
	 * 组装退款报文
	 * @param refundCiphertext
	 * @param refundJson
	 * @param base64AgreementKey
	 * @return
	 * @throws Exception
	 * @since 2015-10-10
	 */
	private Map<String,String> createRefundParams(String refundCiphertext,String refundJson,String base64AgreementKey) throws Exception{
		logger.info("\n 方法[{}]，入参：[{},{},{}]","ChinaBankGateServiceImpl-createRefundParams",refundCiphertext,refundJson,base64AgreementKey);
		// 获取公钥
//		String  privateKey = SysProperties.getProperty(SysConstants.CB_RSA_PRI_KEY);
//		String  publicKey = SysProperties.getProperty(SysConstants.CB_RSA_PUB_KEY);
		Map<String,String> refundParams = new HashMap<String,String>();
		refundParams.put("code", SysProperties.getProperty(SysConstants.CB_REFUND_INTERFACE_CODE));
		refundParams.put("owner", SysProperties.getProperty(SysConstants.CB_OWNER));
		refundParams.put("data", refundCiphertext);
//		refundParams.put("sign", RSACoder.sign(refundJson.getBytes("UTF-8"), privateKey));
//		refundParams.put("key", BASE64.encode(RSACoder.encryptByPublicKey(base64AgreementKey.getBytes("UTF-8"), publicKey)));
		refundParams.put("sign", RSAUtil.sign(refundJson));
		refundParams.put("key", RSAUtil.encrypt(base64AgreementKey));
		logger.info("\n 方法[{}]，出参：[{}]","ChinaBankGateServiceImpl-createRefundParams",JSON.toJSONString(refundParams));
		return refundParams;
	}
	
}
