package com.camelot.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * <p>时间工具类</p>
 *  
 *  @author learrings
 *  @createDate 2015.03.04
 **/
public class DateUtil{

	public static final String DATEFORMAT = "yyyy-MM-dd";
	public static final String TIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 * 字符串转换为Date
	 * 
	 * @param dateStr - 时间字符串
	 * @param formatType - 该字符串格式
	 **/
	public static Date strToDate(String dateStr,String formatType) {
		try {
			if(StringUtils.isBlank(dateStr))return null;
			if (StringUtils.isBlank(formatType))formatType=DATEFORMAT;
			DateFormat sdf = new SimpleDateFormat(formatType);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Date转换为字符串
	 * 
	 * @param date - 时间
	 * @param formatType - 该字符串格式
	 **/
	public static String dateToStr(Date date,String formatType){
		if(date==null)return "";
		if (StringUtils.isBlank(formatType))formatType=DATEFORMAT;
		DateFormat sdf = new SimpleDateFormat(formatType);	
		return sdf.format(date);
	}
	
	/**
	 * 时间字符串增加天数
	 * 
	 * @param dateStr - 时间字符串
	 * @param addDateNum - 增加天数
	 * @param formatType - 该字符串格式
	 **/
	public static String addDateStr(String dateStr,Integer addDateNum,String formatType){
		
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(strToDate(dateStr, null)); 
		if(addDateNum==null)addDateNum=1;
		calendar.add(Calendar.DATE,addDateNum);	
		return dateToStr(calendar.getTime(),null);
	}
}
