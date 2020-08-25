package com.camelot.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.thinkgem.jeesite.common.utils.StringUtils;

public class ShopInfoTag extends TagSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Integer COOKIE_MAX_AGE = 1000 * 60 * 60 * 24;
	// 封装random标签的两个属性的JavaBean属性
	private Long shopId;
	private ShopExportService shopExportService = null;
	private RedisDB redisDB;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Override
	public int doStartTag() throws JspException {
		redisDB = SpringApplicationContextHolder.getBean("redisDB");
		String shopName = "";
		shopName = redisDB.get(shopId+"shopName");
		if(StringUtils.isEmpty(shopName)){
			shopExportService = SpringApplicationContextHolder.getBean("shopExportService");
			ExecuteResult<ShopDTO> shopDTO = shopExportService.findShopInfoById(shopId);
			shopName = shopDTO.getResult().getShopName();
			redisDB.setAndExpire(shopId+"shopName", shopName,COOKIE_MAX_AGE);
		}
		try {
			pageContext.getOut().write(shopName);
		} catch (IOException e) {
			
		}
		return super.doStartTag();
	}
}
