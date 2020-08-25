package com.camelot.activeMQ.service;
/**
 * Description: activeMQ消息生产者接口
 *  
 * @author 王东晓
 * 
 * create on 2015-06-22
 * 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 *
 */
public interface MessagePublisherService {
	/**
	 * 发送验证码消息
	 * @param obj  消息对象
	 * @return 发送成功返回true,发送失败返回false
	 */
	public boolean sendMessage(final Object obj);
}
