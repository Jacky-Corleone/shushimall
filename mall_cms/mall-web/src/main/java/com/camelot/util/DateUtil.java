package com.camelot.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


public class DateUtil {
	private static Logger log = Logger.getLogger(DateUtil.class);
	
	/**
	 * 获取日期的时间差
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @return 时间差
	 */
	public static String getBetweenTime(Date startDate,Date endDate){
		String time = "";
		try {
			if(startDate == null || endDate == null){
				return "";
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        Date smdate = sdf.parse(sdf.format(startDate));  
	        Date bdate = sdf.parse(sdf.format(endDate));  
	        Calendar cal = Calendar.getInstance();    
	        cal.setTime(smdate);    
	        long time1 = cal.getTimeInMillis();                 
	        cal.setTime(bdate);    
	        long time2 = cal.getTimeInMillis();         
	        long between_days=(time2-time1)/(1000*3600*24); 
	        time = String.valueOf(between_days);
		} catch (Exception e) {
			log.error("计算时间差", e);
		}
		return time;
	}
	/**
	 * 
	 * <p>Discription:[获取时间的字符串形态]</p>
	 * Created on 2015-12-11
	 * @param date
	 * @param str
	 * @return
	 * @author:[创建者中文名字]
	 */
	public static String getNewDate(Date date,String str){
		SimpleDateFormat sdf = new SimpleDateFormat(str); 
		str = sdf.format(date);
		return str;
	}
}
