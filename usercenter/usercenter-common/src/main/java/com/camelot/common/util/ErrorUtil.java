package com.camelot.common.util;

/**
 * <p>错误信息整合</p>
 *  
 *  @author learrings
 *  @createDate 2015.03.04
 **/
public class ErrorUtil{

	/**
	 * 提供result.errorMsg数据库级别的错误信息展示给前台（后期需删除数据库级别的展示）
	 * @param resultMsg - 提示错误
	 * @param errorMsg - 数据库级别错误
	 * @return
	 */
	public static String buildErrorMsg(String resultMsg,String errorMsg) {
			return new StringBuffer().append(resultMsg).append("  错误原因：").append(errorMsg).toString();
	}
}
