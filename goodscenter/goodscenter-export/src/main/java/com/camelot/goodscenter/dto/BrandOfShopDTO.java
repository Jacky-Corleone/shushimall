package com.camelot.goodscenter.dto;

import java.io.Serializable;

/**
 * <p>Description: [根据类目查询品牌]</p>
 * Created on 2015年2月8日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class BrandOfShopDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long secondCid;	//二级类目ID
	private Long thirdCid;	//三级类目ID
	private Long shopId;	//店铺ID
	
	public Long getSecondCid() {
		return secondCid;
	}
	public void setSecondCid(Long secondCid) {
		this.secondCid = secondCid;
	}
	public Long getThirdCid() {
		return thirdCid;
	}
	public void setThirdCid(Long thirdCid) {
		this.thirdCid = thirdCid;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
}
