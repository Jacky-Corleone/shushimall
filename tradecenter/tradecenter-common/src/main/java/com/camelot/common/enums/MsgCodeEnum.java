package com.camelot.common.enums;

/**
 *  信息处理编号
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-17
 */
public enum MsgCodeEnum {

	Citic_Service_Error("101","中信服务异常"), 
	Citic_Business_Error("102","中信业务失败"),
	
	Success("200","处理成功"),
	
	Req_Param_Null("301","请求参数缺失"),
	Req_Param_Error("302","请求参数错误"),
	
	Signature_Error("401","签名错误"),
	
	Business_Param_Null("501","业务参数缺失"),
	Business_Logic_Error("502","业务处理失败"),
	
	Custom("900","自定义");
	private String code;
	private String label;
	
	private MsgCodeEnum(String code,String label) {
		this.code = code;
		this.label = label;
	}

	public static String info(MsgCodeEnum msgCodeEnum,String msg) {
		return new StringBuffer("[").append(msgCodeEnum.code).append("]-").
				append(msgCodeEnum.label).append("：").append(msg).toString();
	}
	
	
}
