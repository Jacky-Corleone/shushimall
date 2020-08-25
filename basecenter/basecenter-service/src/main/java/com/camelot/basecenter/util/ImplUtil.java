package com.camelot.basecenter.util;

import org.apache.activemq.command.ActiveMQQueue;

import com.camelot.openplatform.common.enums.PlatformEnum;

/**
 * Impl中所要用到的封装
 * @author 王东晓
 *
 */
public class ImplUtil {
	/**
	 * 获取指定银行的service对象
	 * @param platformId  平台ID
	 * @param type 类型，2为短信，1为邮件
	 * @author 王东晓
	 * @return
	 */
	protected ActiveMQQueue getQueueDestination(Integer platformId,String type , Integer contextType) {
		return QueueDestinationFactory.getQueueDestinationIntance(platformId, type ,contextType);
	}
}
