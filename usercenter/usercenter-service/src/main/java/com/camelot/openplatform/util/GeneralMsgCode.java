package com.camelot.openplatform.util;

public class GeneralMsgCode {

	public static final String ILLEGAL_ARGUMENT = "900001"; //非法参数
	public static final String NO_MATCHED_RECORD_FOUND = "900002";	//没有找到符合条件的记录
	public static final String INTERNAL_SERVER_ERROR = "900003";	//系统内部错误
	public static final String NULL_POINT_ERROR = "900004";			//空值错误
	public static final String DATA_EXPIRED_ERROR = "900005";		//数据过期
	public static final String MULTI_RECORD_FOUND = "900006";		//找到多于一条符合条件的记录
	public static final String RECORD_ALREADY_EXISTS = "900007";		//已经存在符合条件的记录
	
	public static final String WRONG_OPRA = "900205";   //错误操作
	
	public static final String FAIL_TO_UNMARSHAL = "900008"; 		//反序列化失败
	public static final String FAIL_TO_MARSHAL = "900009"; 			//序列化失败
	
	public static final String IOC_MGR_ERROR = "900100";			//MGR注入错误
	public static final String IOC_DAO_ERROR = "900102";			//DAO注入错误
	public static final String IOC_ACTION_SERVICE_ERROR = "900103";	//Action注入错误
	public static final String WEB_SERVICE_INVOKE_ERROR = "900104";	//网络服务调用失败
	
}
