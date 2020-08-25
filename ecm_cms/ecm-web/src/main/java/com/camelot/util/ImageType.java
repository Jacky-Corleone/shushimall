package com.camelot.util;

/**
 * 图片类型：用于在图片服务器上创建对应的文件夹名称
 * @author Administrator
 */
public enum ImageType {
	GOODS("商品图片"),QUALIFICATIONS("资质图片");
	
	private String desc;

	ImageType(String desc){
		this.desc = desc;
	}
	
	public String getDesc(String desc){
		return desc;
	}
}
