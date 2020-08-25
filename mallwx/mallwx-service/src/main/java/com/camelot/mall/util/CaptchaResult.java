package com.camelot.mall.util;

import java.awt.image.BufferedImage;

/**
 * 
 * <p>Description: [生成验证码结果集]</p>
 * Created on 2015年2月5日
 * @author  <a href="mailto: xxx@camelotchina.com">胡恒心</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class CaptchaResult {
	private BufferedImage bufferedImage;
	private String key;
	private String value;

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
