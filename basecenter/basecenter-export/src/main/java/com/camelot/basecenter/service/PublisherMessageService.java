package com.camelot.basecenter.service;

public interface PublisherMessageService {
	/**
	 * 发送验证码消息
	 * @param obj  消息对象
	 * @return 发送成功返回true,发送失败返回false
	 */
	public boolean sendMessage(final Object obj);
}
