package com.camelot.payment.service.chinabank.refund.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.util.HttpUtil;

public class HttpsClientUtil {

	/**
	 * 发送json格式请求到指定地址
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	private static final String CONTENT_CHARSET = "UTF-8";
	private static final Logger logger = LoggerFactory
			.getLogger(HttpUtil.class);

	public static Map<String, String> getParam() {
		return new HashMap<String, String>();
	}

	public static String sendPost(String url, Map<String, String> params)
			throws HttpException, IOException {
		String response = null;
		PostMethod httpPost = null;
		HttpClient httpClient = null;
		httpClient = new HttpClient();
		httpPost = new PostMethod(url);
		httpPost.getParams().setContentCharset(CONTENT_CHARSET);
		httpClient.getHttpConnectionManager().getParams().setTcpNoDelay(true);
		httpClient.getHttpConnectionManager().getParams()
				.setConnectionTimeout(30000);
		for (Map.Entry entry : params.entrySet()) {
			httpPost.addParameter((String) entry.getKey(),
					(String) entry.getValue());
		}
		if (200 == httpClient.executeMethod(httpPost)) {
			response = httpPost.getResponseBodyAsString();
		}
		if (httpPost != null) {
			httpPost.releaseConnection();
		}
		if (httpClient != null) {
			httpClient.getHttpConnectionManager().closeIdleConnections(0L);
		}
		return response;
	}
}
