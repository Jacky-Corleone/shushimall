package com.camelot.payment.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.constants.SysConstants;
import com.camelot.openplatform.util.SysProperties;

/**
 * User: rizenguo Date: 2014/10/29 Time: 14:36
 */
public class HttpsRequest {

	// public interface ResultListener {
	//
	//
	// public void onConnectionPoolTimeoutError();
	//
	// }

	private final static Logger log = LoggerFactory.getLogger(HttpsRequest.class);

	// 表示请求器是否已经做了初始化工作
	private boolean hasInit = false;

	// 连接超时时间，默认10秒
	private int socketTimeout = 10000;

	// 传输超时时间，默认30秒
	private int connectTimeout = 30000;

	// 请求器的配置
	private RequestConfig requestConfig;

	// HTTP请求器
	private CloseableHttpClient httpClient;

	public HttpsRequest() throws UnrecoverableKeyException,
			KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, IOException, CertificateException {
		init();
	}

	private void init() throws IOException, KeyStoreException,
			UnrecoverableKeyException, NoSuchAlgorithmException,
			KeyManagementException, CertificateException {
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		InputStream inputStream = HttpsRequest.class.getResourceAsStream("/cert/apiclient_cert.p12");
		
		keyStore.load(inputStream,
				SysProperties.getProperty(SysConstants.WX_MCH_ID).toCharArray());// 设置证书密码

		// 指定PKCS12的密码(商户ID)
		SSLContext sslcontext = SSLContexts
				.custom()
				.loadKeyMaterial(
						keyStore,
						SysProperties.getProperty(SysConstants.WX_MCH_ID)
								.toCharArray()).build();
		// 指定TLS版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// 设置httpclient的SSLSocketFactory
		httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		// 根据默认超时限制初始化requestConfig
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();

		hasInit = true;
	}

	/**
	 * 通过Https往API post xml数据
	 * 
	 * @param url
	 *            API地址
	 * @param xmlObj
	 *            要提交的XML数据对象
	 * @return API回包的实际数据
	 * @throws IOException
	 * @throws KeyStoreException
	 * @throws UnrecoverableKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws CertificateException
	 */

	public String sendPost(String url, Object xmlObj) throws IOException,
			KeyStoreException, UnrecoverableKeyException,
			NoSuchAlgorithmException, KeyManagementException,
			CertificateException {
		log.info("\n 方法[{}]，入参：[{}][{}]","HttpsRequest-sendPost",url,xmlObj);
		if (!hasInit) {
			init();
		}

		String result = null;

		HttpPost httpPost = new HttpPost(url);

		String postDataXML = xmlObj.toString();

		log.info("API，POST过去的数据是：");
		log.info(postDataXML);

		// 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
		StringEntity postEntity = new StringEntity(postDataXML, "UTF-8");
		httpPost.addHeader("Content-Type", "text/xml");
		httpPost.setEntity(postEntity);

		// 设置请求器的配置
		httpPost.setConfig(requestConfig);

		log.info("executing request" + httpPost.getRequestLine());

		HttpResponse response = httpClient.execute(httpPost);

		HttpEntity entity = response.getEntity();

		result = EntityUtils.toString(entity, "UTF-8");

		return result;
	}

	/**
	 * 设置连接超时时间
	 * 
	 * @param socketTimeout
	 *            连接时长，默认10秒
	 */
	public void setSocketTimeout(int socketTimeout) {
		socketTimeout = socketTimeout;
		resetRequestConfig();
	}

	/**
	 * 设置传输超时时间
	 * 
	 * @param connectTimeout
	 *            传输时长，默认30秒
	 */
	public void setConnectTimeout(int connectTimeout) {
		connectTimeout = connectTimeout;
		resetRequestConfig();
	}

	private void resetRequestConfig() {
		requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout)
				.setConnectTimeout(connectTimeout).build();
	}

	/**
	 * 允许商户自己做更高级更复杂的请求器配置
	 * 
	 * @param requestConfig
	 *            设置HttpsRequest的请求器配置
	 */
	public void setRequestConfig(RequestConfig requestConfig) {
		requestConfig = requestConfig;
	}
}
