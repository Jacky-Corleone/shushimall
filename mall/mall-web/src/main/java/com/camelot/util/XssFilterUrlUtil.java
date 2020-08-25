package com.camelot.util;

import java.util.HashMap;
import java.util.Map;

public class XssFilterUrlUtil {
	//key:不需要过滤的Url 
	//value:这个url中不需要过滤的字段,如果有多个字段，则用","分开，如：a,b,c
	public static Map<String , String> unXssFilterUrlMap = new HashMap<String,String>();
	
	static{
		//商品添加页面，因为其中含有富文本，所以不做过滤
		unXssFilterUrlMap.put("/sellcenter/sellProduct/save", "describeUrl,specification,afterService");
		unXssFilterUrlMap.put("/sellcenter/sellProduct/edit", "describeUrl,specification,afterService");
		//添加公告连接，也含有富文本框
		unXssFilterUrlMap.put("/sellcenter/sellNotice/edit", "noticeContent");
		unXssFilterUrlMap.put("/sellcenter/sellNotice/save", "noticeContent");
		//二手商品发布页面，富文本框
		unXssFilterUrlMap.put("/secondGoods/storageSecondGoods", "describeUr,describeDetail");
	}
	
}
