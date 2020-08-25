package com.camelot.payment.service.unionpay;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.common.enums.PayJournayStatusEnum;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.AlipayErrorCodeProperties;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.service.PayService;
import com.camelot.payment.service.alipay.util.pc.AlipayNotify;
import com.camelot.payment.service.unionpay.util.SDKConfig;
import com.camelot.payment.service.unionpay.util.SDKUtil;


@Service("unionpayService")
public class UnionpayServiceImpl implements PayService {
	
	private final static Logger logger = LoggerFactory.getLogger(UnionpayServiceImpl.class);

	@Override
	public ExecuteResult<Integer> buildPayForm(PayReqParam param) {
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		String merId = SDKConfig.getConfig().getMerId();
		
		String txnAmt = new BigDecimal(String.valueOf(param.getTotalFee())).multiply(new BigDecimal(100)).setScale(0).toString();
		
		Map<String, String> requestData = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		requestData.put("version", SDKUtil.version);   			  //版本号，全渠道默认值
		requestData.put("encoding", SDKUtil.encoding_UTF8); 			  //字符集编码，可以使用UTF-8,GBK两种方式
		requestData.put("signMethod", "01");            			  //签名方法，只支持 01：RSA方式证书加密
		requestData.put("txnType", "01");               			  //交易类型 ，01：消费
		requestData.put("txnSubType", "01");            			  //交易子类型， 01：自助消费
		requestData.put("bizType", "000201");           			  //业务类型，B2C网关支付，手机wap支付
		requestData.put("channelType", "07");           			  //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板  08：手机
		
		/***商户接入参数***/
		requestData.put("merId", merId);    	          			  //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号
		requestData.put("accessType", "0");             			  //接入类型，0：直连商户 
		requestData.put("orderId",param.getOutTradeNo());             //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则		
		requestData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效
		requestData.put("currencyCode", "156");         			  //交易币种（境内商户一般是156 人民币）		
		requestData.put("txnAmt", txnAmt);             			      //交易金额，单位分，不要带小数点
		requestData.put("reqReserved", "shushi100");        		      //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		
		//前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址
		//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限
		//异步通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		requestData.put("frontUrl", SDKConfig.getConfig().getReturnFrontUrl());
		
		//后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知
		//后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 消费交易 商户通知
		//注意:1.需设置为外网能访问，否则收不到通知    2.http https均可  3.收单后台通知后需要10秒内返回http200或302状态码 
		//    4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。
		//    5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败
		requestData.put("backUrl", SDKConfig.getConfig().getReturnBackUrl());
		
		//////////////////////////////////////////////////
		//
		//       报文中特殊用法请查看 PCwap网关跳转支付特殊用法.txt
		//
		//////////////////////////////////////////////////
		 
		/**请求参数设置完毕，以下对请求参数进行签名并生成html表单，将表单写入浏览器跳转打开银联页面**/
		Map<String, String> submitFromData = SDKUtil.signData(requestData,SDKUtil.encoding_UTF8);  //报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。
		
		String requestFrontUrl = SDKConfig.getConfig().getFrontRequestUrl();  //获取请求银联的前台地址：对应属性文件acp_sdk.properties文件中的acpsdk.frontTransUrl
		String html = SDKUtil.createAutoFormHtml(requestFrontUrl, submitFromData,SDKUtil.encoding_UTF8);   //生成自动跳转的Html表单
		result.setResultMessage(html);
		return result;
	}

	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		ExecuteResult<HashMap<String,String>> result = new ExecuteResult<HashMap<String,String>>();
		HashMap<String, String> resultMap = new HashMap<String, String>();
		String origQryId = refundReqParam.getTransactionNo();
		String txnAmt = new BigDecimal(String.valueOf(refundReqParam.getRefundAmount())).multiply(new BigDecimal(100)).setScale(0).toString();
		
		Map<String, String> data = new HashMap<String, String>();
		
		/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/
		data.put("version", SDKUtil.version);               //版本号
		data.put("encoding", SDKUtil.encoding_UTF8);             //字符集编码 可以使用UTF-8,GBK两种方式
		data.put("signMethod", "01");                        //签名方法 目前只支持01-RSA方式证书加密
		data.put("txnType", "04");                           //交易类型 04-退货		
		data.put("txnSubType", "00");                        //交易子类型  默认00		
		data.put("bizType", "000201");                       //业务类型 B2C网关支付，手机wap支付	
		data.put("channelType", "07");                       //渠道类型，07-PC，08-手机		
		
		/***商户接入参数***/
		data.put("merId", SDKConfig.getConfig().getMerId());                //商户号码，请改成自己申请的商户号或者open上注册得来的777商户号测试
		data.put("accessType", "0");                         //接入类型，商户接入固定填0，不需修改		
		data.put("orderId", refundReqParam.getOutRefundNo());          //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则，重新产生，不同于原消费		
		data.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));      //订单发送时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效		
		data.put("currencyCode", "156");                     //交易币种（境内商户一般是156 人民币）		
		data.put("txnAmt", txnAmt);                          //****退货金额，单位分，不要带小数点。退货金额小于等于原消费金额，当小于的时候可以多次退货至退货累计金额等于原消费金额		
		data.put("reqReserved", "shushi100");                    //请求方保留域，透传字段（可以实现商户自定义参数的追踪）本交易的后台通知,对本交易的交易状态查询交易、对账文件中均会原样返回，商户可以按需上传，长度为1-1024个字节		
		data.put("backUrl", SDKConfig.getConfig().getRefundNotifyUrl());               //后台通知地址，后台通知参数详见open.unionpay.com帮助中心 下载  产品接口规范  网关支付产品接口规范 退货交易 商户通知,其他说明同消费交易的后台通知
		
		/***要调通交易以下字段必须修改***/
		data.put("origQryId", origQryId);      //****原消费交易返回的的queryId，可以从消费交易后台通知接口中或者交易状态查询接口中获取
		
		/**请求参数设置完毕，以下对请求参数进行签名并发送http post请求，接收同步应答报文------------->**/
		
		Map<String, String> submitFromData  = SDKUtil.signData(data,SDKUtil.encoding_UTF8);//报文中certId,signature的值是在signData方法中获取并自动赋值的，只要证书配置正确即可。

		String url = SDKConfig.getConfig().getBackRequestUrl();//交易请求url从配置文件读取对应属性文件acp_sdk.properties中的 acpsdk.backTransUrl
		
		//如果这里通讯读超时（30秒），需发起交易状态查询交易
		Map<String, String> resmap = SDKUtil.submitUrl(submitFromData, url,SDKUtil.encoding_UTF8);//这里调用signData之后，调用submitUrl之前不能对submitFromData中的键值对做任何修改，如果修改会导致验签不通过
		
		/**对应答码的处理，请根据您的业务逻辑来编写程序,以下应答码处理逻辑仅供参考------------->**/
		
		//应答码规范参考open.unionpay.com帮助中心 下载  产品接口规范  《平台接入接口规范-第5部分-附录》
		logger.info(JSON.toJSONString(resmap));
		if(resmap.get("respCode").equals("00")){
			//交易已受理，等待接收后台通知更新订单状态,也可以主动发起 查询交易确定交易状态。
			result.setResultMessage("true");
			resultMap.put("state", "3");//申请成功
		}else if(resmap.get("respCode").equals("03") || 
				 resmap.get("respCode").equals("04") ||
				 resmap.get("respCode").equals("05")){
			result.setResultMessage("true");
			resultMap.put("state", "3");//申请成功
		}else{
			//其他应答码为失败请排查原因
			String error = resmap.get("respMsg");
			result.setResultMessage("false");
			result.addErrorMessage(error);
			resultMap.put("state", "4");//申请失败
			resultMap.put("resultCode", resmap.get("respCode"));
			resultMap.put("resultMessage", error);
		}
		result.setResult(resultMap);
		return result;
	}

	@Override
	public ExecuteResult<Transations> buildTransatoins(Map<String, String> resData, String type) {
		ExecuteResult<Transations> result = new ExecuteResult<Transations>();
		Map<String, String> valideData = null;
		if (null != resData && !resData.isEmpty()) {
			Iterator<Entry<String, String>> it = resData.entrySet()
					.iterator();
			valideData = new HashMap<String, String>(resData.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				try {
					value = new String(value.getBytes("ISO-8859-1"), SDKUtil.encoding_UTF8);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				valideData.put(key, value);
			}
		}
		
		// 验证签名
		if (!SDKUtil.validate(valideData, SDKUtil.encoding_UTF8)) {
			result.addErrorMessage("解析交易流程控制对象失败");
		} else {
			Transations reTran = new Transations();
			reTran.setOutTradeNo(resData.get("orderId"));//对外交易号
			reTran.setTransactionNo(resData.get("queryId")) ;//银联交易查询流水号
			reTran.setBuyer(resData.get("accNo"));//买家支付宝账号
			reTran.setFromAccount(resData.get("accNo"));//买家支付宝账户号
			reTran.setToAccount(resData.get("merId"));//平台银联号
			if(PayJournayStatusEnum.SYNCHRONOUS_NOTICE.getCode().equals(type)){
				reTran.setCompletedTime(new Date());//交易付款时间
			}else if(PayJournayStatusEnum.ASYNCHRONOUS_NOTICE.getCode().equals(type)){
				try {
					reTran.setCompletedTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(resData.get("txnTime")));//交易付款时间
				} catch (ParseException e) {
					reTran.setCompletedTime(new Date());
				}
			}
			reTran.setRealAmount(new BigDecimal(resData.get("txnAmt")).divide(new BigDecimal(100)).setScale(2));//实际支付金额
			String tradeStatus = resData.get("respCode");//交易状态
			if("00".equals(tradeStatus.trim())){
				reTran.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
			}else{
				reTran.setStatus(TransationsStatusEnum.PAID_FAIL.getCode());
			}
			result.setResult(reTran);
		}
		return result;
	}

	@Override
	public Transations queryTradeInfo(Transations transations) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
