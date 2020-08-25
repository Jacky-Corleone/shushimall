package com.camelot.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * @author yoUng
 * 
 * @description 发送http请求
 * 
 * @filename HttpUtil.java
 * 
 * @time 2011-6-15 下午05:26:36
 * 
 * @version 1.0
 */

public class HttpUtil {

	public static String http(String url, Map<String, String> params)
			throws Exception {
		URL u = null;
		HttpURLConnection con = null;
		// 构建请求参数
		StringBuffer sb = new StringBuffer();
		if (params != null && !params.isEmpty()) {
			for (Entry<String, String> e : params.entrySet()) {
				if(e.getValue()!=null){
					sb.append(URLEncoder.encode(e.getKey(), "UTF-8"));
					sb.append("=");
					sb.append(URLEncoder.encode(e.getValue(), "UTF-8"));
					sb.append("&");
				}
			}
		}
		// 尝试发送请求
		try {
			u = new URL(url);
			con = (HttpURLConnection) u.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded; text/html; charset=utf-8");
			OutputStreamWriter osw = new OutputStreamWriter(
					con.getOutputStream(), "UTF-8");
			osw.write(sb.toString());
			osw.flush();
			osw.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		// 读取返回内容
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
			}
		} catch (Exception e) {
			throw e;
		}
		return buffer.toString();
	}
	/**
	 * Https POST
	 *
	 * @throws
	 **/
	public static String httpsPostMethod(String postURL, String postData) {
		OutputStreamWriter out = null;
		HttpsURLConnection conn = null;
		InputStream is = null;
		StringBuffer buffer = new StringBuffer();
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[]{new TrustAnyTrustManager()},
					new java.security.SecureRandom());
			// 更换服务如何获取凭证
			URL console = new URL(postURL);
			conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setDoOutput(true);

			out = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
			out.write(postData);
			out.flush();
			conn.connect();

			// 读取返回内容
			is = conn.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));
			String temp;
			while ((temp = br.readLine()) != null) {
				buffer.append(temp);
				buffer.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					out.close();
					is.close();
					conn.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return buffer.toString();
	}

	// https证书验证
	private static class TrustAnyTrustManager implements X509TrustManager {
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[]{};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}


}
