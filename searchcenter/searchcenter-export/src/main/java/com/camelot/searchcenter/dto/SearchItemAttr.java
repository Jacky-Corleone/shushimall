package com.camelot.searchcenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>Description: [商品属性对象]</p>
 * Created on 2015-3-4
 * @author  wangcs
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SearchItemAttr implements Serializable,Comparable {

	private static final long serialVersionUID = -212851812667326995L;

	private Long id;
	private String name;
	private List<SearchItemAttrValue> values = new ArrayList<SearchItemAttrValue>();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<SearchItemAttrValue> getValues() {
		return values;
	}
	public void setValues(List<SearchItemAttrValue> values) {
		this.values = values;
	}

    @Override
    public int compareTo(Object o)
    {
        SearchItemAttr item = (SearchItemAttr)o;
        int subtract = this.values.size() - item.values.size();
        if(subtract == 0 )
            return 0;
        else return subtract > 0?-1:1;
    }
}
