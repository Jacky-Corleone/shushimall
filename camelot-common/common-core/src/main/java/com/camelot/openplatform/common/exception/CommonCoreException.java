package com.camelot.openplatform.common.exception;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月9日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class CommonCoreException extends RuntimeException{
	private static final long serialVersionUID = -573866319646766671L;

	public CommonCoreException() {
		super();
	}

	public CommonCoreException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonCoreException(String message) {
		super(message);
	}

	public CommonCoreException(Throwable cause) {
		super(cause);
	}
}
