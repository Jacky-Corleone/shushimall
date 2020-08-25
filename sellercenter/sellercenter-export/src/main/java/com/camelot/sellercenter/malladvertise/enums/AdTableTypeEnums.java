package com.camelot.sellercenter.malladvertise.enums;

/**
 *  广告位点击量对应的表
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-5-11
 */
public enum AdTableTypeEnums{
	advertise(""),recommendAttr("");
	
	private String label;
	AdTableTypeEnums(String label){
		this.label=label;
	}
	
	public String getLabel() {
		return label;
	}
}
