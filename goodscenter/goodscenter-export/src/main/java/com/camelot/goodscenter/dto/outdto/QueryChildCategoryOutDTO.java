package com.camelot.goodscenter.dto.outdto;

import java.io.Serializable;

/**
 * 
 * <p>Description: [查询子集类目 出参dto]</p>
 * Created on 2015-10-26
 * @author  武超强
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

public class QueryChildCategoryOutDTO implements Serializable{
	private static final long serialVersionUID = -2729081174251794580L;
	private String childCategorys;

	public String getChildCategorys() {
		return childCategorys;
	}

	public void setChildCategorys(String childCategorys) {
		this.childCategorys = childCategorys;
	}
	
}
