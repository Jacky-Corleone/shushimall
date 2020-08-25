package com.camelot.goodscenter.domain;

import java.io.Serializable;

/**
 * 
 * <p>Description: [级联查询商品类目封装对象]</p>
 * Created on 2015-3-9
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemCategoryCascade implements Serializable {

	private static final long serialVersionUID = -1211254554706283746L;

	private Long cid;//类目ID
	private String cname;//类目名称
	private Long secondCatId;//二级类目ID
	private String secondCatName;//二级类目名称
	private Long thirdCatId;//三级类目ID
	private String thirdCatName;//三级类目名称
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Long getSecondCatId() {
		return secondCatId;
	}
	public void setSecondCatId(Long secondCatId) {
		this.secondCatId = secondCatId;
	}
	public String getSecondCatName() {
		return secondCatName;
	}
	public void setSecondCatName(String secondCatName) {
		this.secondCatName = secondCatName;
	}
	public Long getThirdCatId() {
		return thirdCatId;
	}
	public void setThirdCatId(Long thirdCatId) {
		this.thirdCatId = thirdCatId;
	}
	public String getThirdCatName() {
		return thirdCatName;
	}
	public void setThirdCatName(String thirdCatName) {
		this.thirdCatName = thirdCatName;
	}
	
}
