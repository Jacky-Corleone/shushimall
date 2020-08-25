package com.camelot.mall.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
*
* <p>Description: 信息发送模版读取工具类</p>
* Created on 2015年3月4日
* @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
* @version 1.0
* Copyright (c) 2015 北京柯莱特科技有限公司 交付部
*/
public class MessageTemplateFileUtil {
	private static MessageTemplateFileUtil messageTemplateFileUtil;
	private static String fileName = "message_template.properties";
	//模版文件对象
	private static Properties property ;
	
	private static class MessageTemplateFileHandler{
		private static MessageTemplateFileUtil messageTemplateFileUtil = new MessageTemplateFileUtil();
	}
	
	private MessageTemplateFileUtil(){}
	//获取单例对象
	public static MessageTemplateFileUtil getInstance(){
		return MessageTemplateFileHandler.messageTemplateFileUtil;
	}
	
	/**
	 * 获取模版配置文件
	 * @return
	 * @throws java.io.IOException
	 */
	private void getProperties() throws IOException{
		if(property == null){
			property = new Properties();
			InputStream in = MessageTemplateFileUtil.class.getClassLoader().getResourceAsStream(fileName);
			property.load(in);
		}
	}
	
	/**
	 * 通过模版文件key获取对应的value
	 * @param key
	 * @return
	 */
	public String getValue(String key){
		try {
			getProperties();
			return property.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
