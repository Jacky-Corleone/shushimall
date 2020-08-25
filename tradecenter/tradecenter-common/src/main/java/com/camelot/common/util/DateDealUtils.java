package com.camelot.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 业务中用到日期的一些处理方法
 */

public class DateDealUtils {

	public static void main(String[] args) throws Exception {
		List<String> list = getListDateReduce("20160102","20160102",20);
		System.out.println(list);
		
		System.out.println(dateWithoutFormat("2015-./\\03.01"));
	}
	
	/**
	 * 从截止日期开始递减，返回dayInterval天的记录，如果递减到开始日期，则停止 
	 * 
	 * startDate：开始日期
	 * endDate：截止日期  默认不为空
	 * 日期格式默认yyyyMMdd
	 * dayInterval不为空
	 */
	public static List<String> getListDateReduce(String startDate,String endDate,Integer dayInterval) throws Exception{
		
		List<String> dateList=new ArrayList<String>();
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		Calendar startCalendar = Calendar.getInstance();
		Calendar endCalendar = Calendar.getInstance();
		
		endCalendar.setTime(df.parse(endDate));		//截止日期转化为Calendar格式
		
		if(startDate!=null && startDate.length()>0){
			startCalendar.setTime(df.parse(startDate));	//开始日期转化为Calendar格式
		}
		
		//循环天数，从截止日期开始-1，如果截止日期与开始日期之间大于7天，则返回7天；否则返回之间的天数（包括首尾）
	    for (int i = 0; i < dayInterval; i++) {
		   if (startDate!=null && startDate.length()>0 && endCalendar.compareTo(startCalendar)< 0) {  
			   break;  
		   }else{
			   dateList.add(df.format(endCalendar.getTime()));
			   endCalendar.add(Calendar.DATE, -1);  //截止日期减1
		   }
	    }
		
		return dateList;
	}
	
	// convertedFormat 待转换的格式， toFormat 要转换成的格式
	public static String getDateStrToStr (String str,String convertedFormat,String toFormat) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat(convertedFormat).parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		str=new SimpleDateFormat(toFormat).format(date);
		return str;
	}
	
	public static String dateWithoutFormat(String date){
		if(date!=null && date.length()>0){
			return date.replace("-", "").replace("/", "").replace(".", "").replace("\\", "");
		}else{
			return null;
		}
	}
	
	
}
