package com.camelot.openplatform.common.http;

import java.util.Vector;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2016年3月22日
 * @author  <a href="mailto: liuxiangping86@camelotchina.com">刘香平</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部 
 */
public class ResponseParam {

	public String urlString;
	
	public int defaultPort;
	
	public String host;
	
	public String path;
	
	public int port;
	
	public String protocol;
	
	public String query;
	
	public String ref;
	
	public String userInfo;
	
	public String content;
	
	public String contentEncoding;
	
	public int code;
	
	public String message;
	
	public String contentType;
	
	public String method;
	
	public int connectTimeout;
	
	public int readTimeout;
	
	public Vector<String> contentCollection;
	
	public String file;
}
