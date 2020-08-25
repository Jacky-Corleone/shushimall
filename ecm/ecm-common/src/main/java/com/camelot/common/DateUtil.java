package com.camelot.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	private static DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
	
	
	/**
	 * 格式化日期 格式为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		if(date != null)
			return formatDate.format(date);
		return "";
	}
	
	/**
	 * 日期转换
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date){
		if(date != null && date.trim().length() > 0){
			try {
				return formatDate.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
}
