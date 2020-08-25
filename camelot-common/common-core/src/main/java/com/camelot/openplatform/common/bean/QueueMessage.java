package com.camelot.openplatform.common.bean;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Description: 消息实体
 *  
 * @author zhangzhiqiang
 * 
 * create on 2016-03-30
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 */
public class QueueMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1462547136225747672L;
	//消息类型
	private String key;
	//【选填】容错url，业务使用。使用事例场景：业务处理的时候如果发现数据有问题需要重新加工的地址。
	private String errorUrl;
	//【必填】数据的处理业务地址，采用http接口。消息队列会将消息主动推送到此地址，可多个
	private List<String> callBackUrls;
	//【必填】数据体，可用json格式，如果多条数据可采用json集合的形式，具体取决于业务场景。注:为了跨系统、跨语言考虑，暂不支持对象的形式
	private String data;
	//数据唯一性标识,调用者不需要关心
	private String flag;
	//数据推送次数，系统内使用，调用者不需要关心
	private int num = 1;
	
	public QueueMessage(){
		if(flag == null || flag.trim().length() == 0){
			UUID uuid = UUID.randomUUID();
			this.flag = uuid.toString();
		}
	}
	
	public String getErrorUrl() {
		return errorUrl;
	}
	public void setErrorUrl(String errorUrl) {
		this.errorUrl = errorUrl;
	}
	public List<String> getCallBackUrls() {
		return callBackUrls;
	}
	public void setCallBackUrls(List<String> callBackUrls) {
		this.callBackUrls = callBackUrls;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getFlag() {
		return flag;
	}
	
}
