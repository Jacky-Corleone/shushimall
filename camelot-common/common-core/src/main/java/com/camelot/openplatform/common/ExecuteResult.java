package com.camelot.openplatform.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行结果信息类，用于封装增删改方法的结果信息
 * 失败时必须将错误信息设置到errorMessages中
 */
public class ExecuteResult<T> implements Serializable {

	private static final long serialVersionUID = -1854616725284151074L;

	private T result;// 执行成功返回结果集
	private String resultMessage;// 执行成功结果信息
	private List<String> errorMessages = new ArrayList<String>();// 调用失败时，返回的错误集合
	
	/**
	 * <p>Discription:[存入错误信息]</p>
	 * Created on 2015年2月9日
	 * @param errorMsg
	 * @author:[周乐]
	 */
	public void addErrorMsg(String errorMsg){
		errorMessages.add(errorMsg);
	}
	
	public boolean isSuccess() {
		return errorMessages.isEmpty()?true:false;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public List<String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	public void addErrorMessage(String errorMessage) {
		errorMessages.add(errorMessage);
	}
}
