package com.camelot.payment.service.chinabank;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.common.util.DateUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.dao.TransationsDAO;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.domain.cbmobile.CbMobileResult;
import com.camelot.payment.domain.cbmobile.CbMobileTrade;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.service.PayService;
import com.camelot.payment.service.chinabank.util.AsynNotificationReqDto;
import com.camelot.payment.service.chinabank.util.ByteUtil;
import com.camelot.payment.service.chinabank.util.CBMobileUtil;
import com.camelot.payment.service.chinabank.util.DESUtil;
import com.camelot.payment.service.chinabank.util.FormatUtil;
import com.camelot.payment.service.chinabank.util.HttpsClientUtil;
import com.camelot.payment.service.chinabank.util.PaySignEntity;
import com.camelot.payment.service.chinabank.util.RSACoder;
import com.camelot.payment.service.chinabank.util.SHAUtil;
import com.camelot.payment.service.chinabank.util.Sha256Util;
import com.camelot.payment.service.chinabank.util.TDESUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

@Service("cbMobileGateService")
public class CBMobileGateServiceImpl implements PayService {
	private final static Logger logger = LoggerFactory.getLogger(CBMobileGateServiceImpl.class);
	@Resource
	private TransationsDAO transationsDAO;
	/**
	 * TODO payReqParam.extraParam负责网银用户TOKEN传送
	 **/
	public ExecuteResult<Integer> buildPayForm(PayReqParam payReqParam) {

		ExecuteResult<Integer> result = new ExecuteResult<Integer>();

		SortedMap<String, String> signParams = new TreeMap<String, String>();
		PaySignEntity tradeInfo = CBMobileUtil.sign(payReqParam,
				SysProperties.getProperty(SysConstants.CB_M_RSA_PRI_KEY));
		signParams.put("version", tradeInfo.getVersion());// 版本号
		signParams.put("token", tradeInfo.getToken());// 网银用户TOKEN
		signParams.put("merchantSign", tradeInfo.getMerchantSign());// 商户签名
		signParams.put("merchantNum", tradeInfo.getMerchantNum());// 商户号
		signParams.put("merchantRemark", tradeInfo.getMerchantRemark());// 商户备注
		signParams.put("tradeNum", tradeInfo.getTradeNum());// 交易流水号
		signParams.put("tradeName", tradeInfo.getTradeName());// 交易名称
		signParams.put("tradeDescription", tradeInfo.getTradeDescription());// 交易描述
		signParams.put("tradeTime", tradeInfo.getTradeTime());// 交易时间
		signParams.put("tradeAmount", tradeInfo.getTradeAmount());// 交易金额
		signParams.put("currency", tradeInfo.getCurrency());// 货币种类
		signParams.put("notifyUrl", tradeInfo.getNotifyUrl());// 异步通知页面地址
		signParams.put("successCallbackUrl", tradeInfo.getSuccessCallbackUrl());// 支付成功跳转路径
		signParams.put("failCallbackUrl", tradeInfo.getFailCallbackUrl());// 支付失败跳转路径
		result.setResultMessage(ChinaBankUtil.buildRequest(signParams,
				SysProperties.getProperty(SysConstants.CB_M_PAY_URL), "submit"));
		return result;
	}

	@SuppressWarnings("restriction")
	@Override
	public ExecuteResult<Transations> buildTransatoins(Map<String, String> params, String type) {
		ExecuteResult<Transations> result = new ExecuteResult<Transations>();
		try {
			/*//判斷同步异步
			if(PayJournayStatusEnum.SYNCHRONOUS_NOTICE.getCode().equals(type)){
				//同步只回传外部订单号和token 可以区分成功失败 单独查询处理
				if(params.get("success").equals("true")){
					Transations tranQuery = transationsDAO.selectTransByOutTrade(params.get("tradeNum"));
					Transations transations = new Transations();
					transations.setOutTradeNo(tranQuery.getOutTradeNo());
					transations.setRealAmount(transations.getOrderAmount());
					transations.setCompletedTime(new Date());
					transations.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
					result.setResult(transations);
				}else {
					result.addErrorMessage("交易失败，请重新付款！");
				}
			}else{
				//异步会返回银行信息 所以解析判断*/
				String resp = params.get("resp"); // 获取通知原始信息
				if (StringUtils.isNotBlank(resp)) {
					ExecuteResult<AsynNotificationReqDto> vefity = CBMobileUtil.vefitySign(resp,SysProperties.getProperty(SysConstants.CB_M_MD5_PRI_KEY));
					if (vefity.isSuccess()) {
						// 对Data数据进行解密
						byte[] desPriKey = (new BASE64Decoder()).decodeBuffer(SysProperties.getProperty(SysConstants.CB_M_DES_PRI_KEY));
						String decryptArr = DESUtil.decrypt(vefity.getResult().getData(), desPriKey, "utf-8");
						XStream xstream = new XStream(new DomDriver());
						xstream.processAnnotations(CbMobileResult.class);
						CbMobileResult cbMobileResult = (CbMobileResult)xstream.fromXML(decryptArr);
						if(cbMobileResult!=null){
							if("0000".equals(cbMobileResult.getCbMobileReturn().getCODE())){
								CbMobileTrade cbMobileTrade = cbMobileResult.getCbMobileTrade();
								Transations transations = new Transations();
								transations.setOutTradeNo(cbMobileTrade.getID());
								BigDecimal b=BigDecimal.valueOf(Long.valueOf(cbMobileTrade.getAMOUNT())).divide(new BigDecimal(100));
								transations.setRealAmount(b);
								transations.setCompletedTime(new Date());
								transations.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
								result.setResult(transations);
							}else{
								result.addErrorMessage(cbMobileResult.getCbMobileReturn().getDESC());
							}
						}else{
							result.addErrorMessage("解析交易流程控制对象失败,空值");
						}
					} else {
						result.addErrorMessage(vefity.getErrorMessages().get(0));
					}
				} else {
					result.addErrorMessage("解析交易流程控制对象失败,空值");
				}
			/*}*/
		} catch (Exception e) {
			logger.debug(e.getMessage());
			result.addErrorMessage("解析交易流程控制对象异常");
		}
		return result;
	}

	public Transations queryTradeInfo(Transations transations) {
		// Map<String, String> paramsInfo=ChinaBankUtil.packInfo();
		// SortedMap<String, String> signParams = new TreeMap<String, String>();
		// signParams.put("v_oid",transations.getOutTradeNo());// 订单号
		// signParams.put("v_mid",paramsInfo.get(SysConstants.CB_MER_ID));// 商户号
		// signParams.put("billNo_md5",ChinaBankUtil.getMD5ofStr(transations.getOutTradeNo()+paramsInfo.get(SysConstants.CB_PRI_KEY)));//
		// 加密
		// signParams.put("v_url",SysProperties.getProperty(SysConstants.CB_SEARCH_RETURN_URL));//
		// 回传路径
		// HttpUtil.http(SysProperties.getProperty(SysConstants.CB_SEARCH_ORDER_URL),
		// signParams);
		return transations;
	}

	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		logger.info("\n 方法[{}]，入参：[{}]","CBMobileGateServiceImpl-buildRefundForm",JSON.toJSONString(refundReqParam));
		ExecuteResult<HashMap<String,String>> result = new ExecuteResult<HashMap<String,String>>();
		HashMap<String,String> resultMap = new HashMap<String,String>();
		try {
			// 1.组织退款申请数据
			String data = this.createRefundData(refundReqParam);
			// 2.加密退款申请数据
			String threeDesData = this.encryptData(data);
			// 3.对3DES加密的数据进行签名
			String sha256Data = SHAUtil.Encrypt(threeDesData, null);
            String priKey=SysProperties.getProperty(SysConstants.CB_M_RSA_PRI_KEY);
            byte[] rsaResult = RSACoder.encryptByPrivateKey(sha256Data.getBytes(), priKey);
            String merchantSign = RSACoder.encryptBASE64(rsaResult);
            // 4.组织退款申请报文
            String refundParams = this.createRefundParams(merchantSign, threeDesData);
            // 5.发送请求
            String refundUrl=SysProperties.getProperty(SysConstants.CB_M_REFUND_INTERFACE_URL);
            String refundResult = HttpsClientUtil.sendRequest(refundUrl, refundParams);
            // 6.解析响应结果
            resultMap = this.parseRefundResult(refundResult);
            
		} catch (Exception e) {
			logger.error("退款失败",e);
			resultMap.put("state", "2");
			resultMap.put("resultCode", "900");
			resultMap.put("resultMessage", "系统内部异常");
			result.addErrorMessage("\n 网银在线退款失败");
		}
		logger.info("\n 方法[{}]，出参：[{}]","CBMobileGateServiceImpl-buildRefundForm",JSON.toJSONString(refundReqParam));
		result.setResult(resultMap);
		return result;
	}
	/**
	 * 组装退款申请数据
	 * @param refundReqParam
	 * @return
	 */
	private String createRefundData(RefundReqParam refundReqParam){
		logger.info("\n 方法[{}]，入参：[{}]","CBMobileGateServiceImpl-createRefundData",JSON.toJSONString(refundReqParam));
		Map<String,String> refundData = new HashMap<String,String>();
        refundData.put("tradeNum",    refundReqParam.getOutRefundNo());   //对外交易号（退款）
        refundData.put("oTradeNum",    refundReqParam.getOutTradeNo());   //对外交易号（支付）
        String amount = String.valueOf(refundReqParam.getRefundAmount().multiply(new BigDecimal(100)).intValue());
        refundData.put("tradeAmount",   amount);        //退款金额
        refundData.put("tradeCurrency",     SysConstants.CB_MONEY_TYPE);       //币种
        refundData.put("tradeDate",     DateUtils.format(new Date(), "yyyyMMdd")); //交易日期
        refundData.put("tradeTime",     DateUtils.format(new Date(), "HHmmss")); //交易时间
//        refundData.put("tradeNotice",     null); //交易通知地址
        refundData.put("tradeNote", refundReqParam.getDesc()); //交易备注
		String refundJson = JSON.toJSONString(refundData);
		logger.info("\n 方法[{}]，出参：[{}]","CBMobileGateServiceImpl-createRefundData",JSON.toJSONString(refundJson));
		return refundJson;
	}
	/**
	 * 加密退款申请数据
	 * @param data
	 * @return
	 * @throws Exception 
	 */
	private String encryptData(String data) throws Exception{
		logger.info("\n 方法[{}]，入参：[{}]","CBMobileGateServiceImpl-encodeData",data);
		//对退款信息进行3DES加密
    	String deskey=SysProperties.getProperty(SysConstants.CB_M_DES_PRI_KEY);
        String threeDesData = TDESUtil.encrypt2HexStr(RSACoder.decryptBASE64(deskey), data);
        logger.info("\n 方法[{}]，出参：[{}]","CBMobileGateServiceImpl-encodeData",threeDesData);
		return threeDesData;
	}
	
	private String createRefundParams(String merchantSign,String threeDesData){
		logger.info("\n 方法[{}]，入参：[{},{}]","CBMobileGateServiceImpl-createRefundParams",merchantSign,threeDesData);
		//构造最终退款请求json
		Map<String,String> refundParams = new HashMap<String,String>();
		refundParams.put("version","1.0");
		refundParams.put("merchantNum",SysProperties.getProperty(SysConstants.CB_M_MER_ID));
		refundParams.put("merchantSign",FormatUtil.stringBlank(merchantSign));
		refundParams.put("data",threeDesData);
		
		logger.info("\n 方法[{}]，出参：[{}]","CBMobileGateServiceImpl-createRefundParams",JSON.toJSONString(refundParams));
		return JSON.toJSONString(refundParams);
	}
	
	private HashMap<String,String> parseRefundResult(String refundResult) throws Exception{
		logger.info("\n 方法[{}]，入参：[{}]","CBMobileGateServiceImpl-parseRefundResult",JSON.toJSONString(refundResult));
		HashMap<String,String> resultMap = new HashMap<String,String>();
		JSONObject result = JSONObject.parseObject(refundResult);
		String resultCode = result.getString("resultCode");
        String resultMessage = result.getString("resultMsg");
        if(null!=resultCode&&resultCode.equals("0")){
        	
	        JSONObject resultData = result.getJSONObject("resultData");
	        String data = resultData.getString("data");
	        String sign = resultData.getString("sign");
	        
	        //1.解密签名内容
	        byte[] decryptBASE64Arr = new BASE64Decoder().decodeBuffer(sign);
	        String wyPubKey=SysProperties.getProperty(SysConstants.CB_JDPAY_RSA_PUB_KEY);
	        byte[] decryptArr = RSACoder.decryptByPublicKey(decryptBASE64Arr, wyPubKey);
	        String decryptStr = ByteUtil.byte2HexString(decryptArr);
	
	        //2.对data进行sha256摘要加密
	        String sha256SourceSignString = ByteUtil.byte2HexLowerCase(Sha256Util.encrypt(data.getBytes("UTF-8")));
	        
	        //3.比对结果
	        if (decryptStr.equals(sha256SourceSignString)) {
	            /**
	             * 验签通过
	             */
	            //解密data
	        	String deskey=SysProperties.getProperty(SysConstants.CB_M_DES_PRI_KEY);
	            String decrypData = TDESUtil.decrypt4HexStr(RSACoder.decryptBASE64(deskey), data);
	
	            //退款结果实体
	            JSONObject refundResultData = JSONObject.parseObject(decrypData);
	            String tradeStatus = refundResultData.getString("tradeStatus");
	            
	            if(null!=tradeStatus&&tradeStatus.equals("0")){
	            	resultMap.put("state", "1");
	            }else{
	            	resultMap.put("state", "2");
	            	resultMap.put("resultCode", resultCode);
					resultMap.put("resultMessage", resultMessage);
	            }
	        } else {
	            /**
	             * 验签失败  不受信任的响应数据
	             * 终止
	             */
	        	resultMap.put("state", "2");
	        	resultMap.put("resultCode", "900");
	        	resultMap.put("resultMessage", "不受信任的响应数据");
	        }
        }else{
        	resultMap.put("state", "2");
        	resultMap.put("resultCode", resultCode);
        	resultMap.put("resultMessage", resultMessage);
        }
        logger.info("\n 方法[{}]，出参：[{}]","CBMobileGateServiceImpl-parseRefundResult",JSON.toJSONString(resultMap));
        return resultMap;
	}
}
