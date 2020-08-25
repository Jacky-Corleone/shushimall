package com.camelot.openplatform.common.sms;

import java.io.Serializable;

/** 
 * <p>Description: [短信发送结果类]</p>
 * Created on 2015年1月28日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class SmsSendResult implements Serializable{
	private static final long serialVersionUID = -4287370763204526727L;
	
	/**发送结果：true成功；false失败*/
	private Boolean result;
	/**发送失败的消息*/
	private String errorMsg;
	
	public Boolean getResult() {
		return result;
	}
	
	public void setResult(Boolean result) {
		this.result = result;
	}
	
	public String getErrorMsg() {
		return errorMsg;
	}
	
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
