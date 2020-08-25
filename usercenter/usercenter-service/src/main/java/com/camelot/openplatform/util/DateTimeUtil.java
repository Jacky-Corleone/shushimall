package com.camelot.openplatform.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.commons.lang3.StringUtils;

import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;

public class DateTimeUtil {

	
	public static 	int				HOUR_TO_MIN	= 60;
	public static 	int				SEC_TO_MILLISEC	= 1000; 	
	public static	int				MIN_TO_MILLISEC = 60000; 	// 60*1000;
	public static	int				HOUR_TO_MILLISEC = 3600000; // 60 * 60*1000;
	public static 	int				DAY_TO_MILLISEC	= 86400000; // 24 * 60 * 60 * 1000;
	public static int DAY_TO_MIN = 1440;
	
	public final static TimeZone	DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+8");
	
	//public static 	String		DEFAULT_DATE_TIME = "20140101000000";
	
	
	public final static String 	DATETIME_FORMAT_XWHP = "yyyy-MM-dd HH:mm:ss"; //2014-04-10 15:25:14
	public static final String	DATETIME_FORMAT_VEHICLE_PORTAL = "yyyyMMdd HH:mm:ss"; //资源门户日期格式
	
	
		
	public static final String 	TIMESTAMP_FORMAT_LOG = "yyyy-MM-dd_HH-mm-ss-SSS";
	public static final String 	TIMESTAMP_FORMAT_MSG = "yyyy-MM-dd HH:mm:ss.SSS Z";
	
	public static final String 	TIMESTAMP_FORMAT_SEC = "yyyyMMddHHmmss";
	public static final String 	TIMESTAMP_FORMAT_DAY_AND_MIN = "yyyyMMddHHmm";
	
	public static final String 	TIMESTAMP_FORMAT_MILLISEC = "yyMMddHHmmssSSS";
	public static final String 	TIMESTAMP_FORMAT_DAY = "yyyyMMdd";
	public static final String 	TIMESTAMP_FORMAT_TIME = "HHmmss";
	// 日期时间格式
	public final static String 	DATE_FORMAT = "yyyy-MM-dd";
	public final static String 	TIME_FORMAT = "HH:mm:ss";
	public final static String 	DATE_FORMAT_WITH_DOT = "yyyy.MM.dd";
	
	public final static String 	DAY_MONTH_YEAR_FORMAT = "dd/MM/yyyy";
	public final static String 	HOUR_FORMAT = "HH";
	public final static String 	MINUTE_FORMAT = "mm";
	public final static String 	DATETIME_MINUTE_FORMAT = "yyyy-MM-dd HH:mm";
	public final static String 	DATETIME_SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	
	public final static String 	DATE_FORMAT_YYYYMMDD = "yyyyMMdd";
	
	public final static String 	DATETIME_SECOND_FORMAT_YYYYMMDD_HH_MM_SS = "yyyyMMdd HH:mm:ss";
	public final static String 	DATETIME_SECOND_FORMAT_YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
	public final static String 	DATETIME_SECOND_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd_HH-mm-ss";
	
	public final static String DATETIME_DATE_FORMAT_FOR_USER_APP = "yyyy年MM月dd日";
	public final static String DATETIME_TIME_FORMAT_FOR_USER_APP = "HH:mm";
	public final static String DATETIME_FORMAT_CBS="yyyy-MM-ddHH:mm:ss";
	

	/**
	 * getCurrentSystemDateTime
	 * @return
	 */
	public static Timestamp getCurrentSystemDateTime() {
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 获取两个日期中间的工作日
	 * @param starttime
	 * @param endtime
	 * @return
	 */
	public static long getDays_yyyyMMdd(String starttime, String endtime){
		long quot = 0;
		SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
		try {
			Date date1 = ft.parse(starttime);
			Date date2 = ft.parse(endtime);
			quot = date1.getTime()-date2.getTime();
			quot =quot/1000/60/60/24;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return quot;
	}
	
	/**
	 * 将时间转换为les系统中的日期格式yyyy.MM.dd
	 * @param date
	 * @return
	 */
	public static String getLesDate(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.DATE_FORMAT_WITH_DOT);
		return sdf.format(date);
	}
	
	/**
	 * 将时间转换为les系统中的时间格式HH:mm:ss
	 * @param date
	 * @return
	 */
	public static String getLesTime(Date date){
		if(date==null)return null;
		SimpleDateFormat sdf = new SimpleDateFormat(DateTimeUtil.TIME_FORMAT);
		return sdf.format(date);
	}

	/**
	 * 获取两个日期中间的工作日
	 * @param time1
	 * @param time2
	 * @return -1:参数有null
	 */
	public static Long getDays(Timestamp time1, Timestamp time2) {
		
		if(time1 == null || time2 == null)
			return null;
		
		long quot = time1.getTime()- time2.getTime();
		quot = quot / 1000 / 60 / 60 / 24;

		return quot;
	}
	
	
	/**
	 * 时间转换成格式字符串
	 * 
	 * @param timestamp
	 * @param formatStr 时间字符串格式默认为：yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getTimestampToStr_yyyyMMddHHmmss(Timestamp ts) {
		return getTimestampToStr(ts, DateTimeUtil.DATETIME_SECOND_FORMAT);
	}
	
	public static String getTimestampToStr(Timestamp ts, String formatStr) {

		if (ts == null)
			return "";
		return (new SimpleDateFormat(formatStr)).format(ts);

	}
	/**
	 * getCurrentSystemDateTime
	 * @return
	 */
	/*public static Date getCurrentDate() {
		return new Date();
	}*/

	public static Date getCurrentDateByTimeZone() {
		Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
		Date date = calendar.getTime();
		return date;
	}
	
	
	/**
	 * 取得较小时间（若其中丄1�7个为空，则返回不为空的一个）
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static Timestamp	getMinTimestamp(Timestamp  time1, Timestamp time2) {
		
		if (time1 != null && time2 != null) {
			return 	time1.before(time2)? time1: time2;
		} else if (time1 == null) {
			return	time2;
		} else {
			return	time1;
		}
	}
	
	public static Timestamp	getMaxTimestamp(Timestamp  time1, Timestamp time2) {
		
		if (time1 != null && time2 != null) {
			return 	time1.before(time2)? time2: time1;
		} else if (time1 == null) {
			return	time2;
		} else {
			return	time1;
		}
	}
	
	
	/**
	 * 取得时间列表中较小时闄1�7
	 * @param timeList
	 * @return
	 */
	public static Timestamp	getMinTimestampFromList(List<Timestamp>  timeList) {
		
		Timestamp	resTS = null;
		if (timeList != null)
			for (Timestamp tmp: timeList) {
				resTS = getMinTimestamp(resTS, tmp);
			}
		return	resTS;
	}
	
	/**
	 * 取得Timestamp之后任意毫秒的时闄1�7
	 * @param startTime
	 * @param milliSec
	 * @return
	 */
	public static Timestamp getTimestampAfterMilliSec(Timestamp startTime, long milliSec) {
		Timestamp endTime = startTime;
		if (startTime != null && milliSec>0 ) {
			long	startTimeLong = startTime.getTime();
			endTime = new Timestamp(startTimeLong + milliSec);
		}
		return	endTime;
	}
	
	/**
	 * 取得Timestamp之前任意毫秒的时闄1�7
	 * @param startTime
	 * @param milliSec
	 * @return
	 */
	public static Timestamp getTimestampBeforeMilliSec(Timestamp startTime, long milliSec) {
		Timestamp endTime = startTime;
		if (startTime != null && milliSec>0 ) {
			long	startTimeLong = startTime.getTime();
			endTime = new Timestamp(startTimeLong - milliSec);
		}
		return	endTime;
	}
	
	/**
	 * 取得当前Timestamp第二天零点时闄1�7
	 * @param dateTime
	 * @return
	 */
	public static Timestamp getTimestampWithoutTimeForNextDay(Timestamp dateTime) {
		Timestamp	resTime = null;
		resTime = getTimestampWithoutTime(dateTime);
		resTime = getTimestampAfterMilliSec(resTime, DAY_TO_MILLISEC);
		return	resTime;
	}
	
	/**
	 * 取得当前Timestamp头天零点时间
	 * @param dateTime
	 * @return
	 */
	public static Timestamp getTimestampWithoutTimeForPrevDay(Timestamp dateTime) {
		Timestamp	resTime = null;
		resTime = getTimestampWithoutTime(dateTime);
		resTime = getTimestampBeforeMilliSec(resTime, DAY_TO_MILLISEC);
		return	resTime;
	}
	
	public static Timestamp getTimestampWithoutTimeForPrevDay(Timestamp dateTime, int dayNum) {
		Timestamp	resTime = null;
		resTime = getTimestampWithoutTime(dateTime);
		resTime = getTimestampBeforeMilliSec(resTime, DAY_TO_MILLISEC * dayNum);
		return	resTime;
	}
	
	/**
	 * 取得当前Timestamp零点时间
	 * @param dateTime
	 * @return
	 */
	public static Timestamp getTimestampWithoutTime(Timestamp dateTime) {
		Timestamp	resTime = null;
		if (dateTime != null) {
			
			Calendar	cal = Calendar.getInstance();
			cal.setTimeInMillis(dateTime.getTime());
			Calendar	resCal = Calendar.getInstance();
			resCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			resTime = new Timestamp(resCal.getTimeInMillis());
			
			//生成的时间不可带微秒
			resTime.setNanos(0);
		}
		return	resTime;
	}
	
	/**
	 * 取得当前Timestamp零点时间
	 * @param dateTime
	 * @return
	 */
	public static Timestamp getTimestampWithMinOnly(Timestamp dateTime) {
		Timestamp	resTime = null;
		if (dateTime != null) {
			
			Calendar	cal = Calendar.getInstance();
			cal.setTimeInMillis(dateTime.getTime());
			Calendar	resCal = Calendar.getInstance();
			resCal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), 
					cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), 0);
			resTime = new Timestamp(resCal.getTimeInMillis());
			
			//生成的时间不可带微秒
			resTime.setNanos(0);
		}
		return	resTime;
	}
	

	/**
	 * Get the year of the given date
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 取得Date间隔日期（取整）
	 * Returns elapsed date
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public static int getElapsedDate(Date dateFrom, Date dateTo) {
		long result = dateTo.getTime() - dateFrom.getTime();
		return (int) result / DAY_TO_MILLISEC;
	}
	
	/**
	 * 取得Date间隔日期（取整）
	 * Returns elapsed date
	 * 
	 * @param dateFrom
	 * @param dateTo
	 * @return
	 */
	public static int getElapsedDate(Timestamp dateFrom, Timestamp dateTo, int intervalTime) {
		if (dateFrom != null && dateTo != null && dateFrom.before(dateTo) && intervalTime > 0) {
			long result = dateTo.getTime() - dateFrom.getTime();
			return (int) result / intervalTime;
		} else {
			return	0;
		}
	}

	/**
	 * 通过年月日构建Date，并返回毫秒敄1�7
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static long constructDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DATE, day);
		return calendar.getTime().getTime();
	}

	/**
	 * 设置Date的时间为零点
	 * Function : stripTime Description : Strip the time from a date Parameters : Return Values :
	 * long
	 */
	public static long stripTime(Date date) {
		java.util.Calendar calendar = java.util.Calendar.getInstance(DEFAULT_TIMEZONE);
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime().getTime();
	}

	/**
	 * @param bool
	 * @return
	 */
	/*
	 * public static String changeBool(boolean bool){ String charBool = null; if(bool){ charBool =
	 * "1"; }else{ charBool = "0"; }
	 * 
	 * return charBool; }
	 */

	/**
	 * 比较时间，按毫秒
	 * @param time1
	 * @param time2
	 * @return
	 */
	public static boolean isEquals(Timestamp time1, Timestamp time2) {

		if (time1 == null && time2 == null) {
			return true;
		} else if (time1 != null && time2 != null && time1.equals(time2)) {
			return true;
		} else
			return false;
	}

	/**
	 * 比较时间，按微秒
	 * @param dateTime
	 * @param timeStamp
	 * @return
	 */
	public static boolean isEqualsWithNanoSec(Date dateTime, Timestamp timeStamp) {

		if (dateTime == null && timeStamp == null) {
			return true;
		} else if (dateTime != null && timeStamp != null) {

			long time1 = dateTime.getTime();
			long time2 = timeStamp.getTime() + timeStamp.getNanos() / 1000000;

			if (time1 == time2)
				return true;
			else
				return false;
		} else
			return false;
	}

	/**
	 * 按小时增加时闄1�7
	 * 
	 * @param time 参�1�7�时闄1�7
	 * @param hour 要增加的小时
	 * @return 增加后的时间
	 * @create:zhengliexin Dec 24, 2009
	 * @update:zhengliexin Dec 24, 2009
	 */
	public static Timestamp addTimesWithHours(Timestamp time, long hour) {

		Timestamp resTime = new Timestamp(time.getTime() + hour * HOUR_TO_MILLISEC);

		return resTime;
	}

	/**
	 * 按天数增加时闄1�7
	 * 
	 * @param time 参�1�7�时闄1�7
	 * @param day 要增加的天数
	 * @return 增加后的时间
	 * @create:zhengliexin Dec 24, 2009
	 * @update:zhengliexin Dec 24, 2009
	 */
	public static Timestamp addTimesWithDays(Timestamp time, long day) {

		Timestamp resTime = new Timestamp(time.getTime() + day * DAY_TO_MILLISEC);

		return resTime;
	}

	/**
	 * 将时间转化成分钟（不考虑日期＄1�7
	 */
	public static int dateToMin(Timestamp time) {
		int resTime = -1;

		if (time != null) {
			int _hour = time.getHours();
			int _minutes = time.getMinutes();

			resTime = _hour * 60 + _minutes;
		}
		return resTime;
	}

	/**
	 * 将时间转化成毫秒（不考虑日期＄1�7
	 */
	public static int dateToMilliSec(Timestamp time) {
		int resTime = -1;

		if (time != null) {
			int _hour = time.getHours();
			int _minutes = time.getMinutes();

			resTime = _hour * 60 + _minutes;
		}
		return resTime;
	}
	
	
	/**
	 * 取得两个时间段的时间间隔(保证第二个时间一定大于第丄1�7个时闄1�7)
	 * 
	 * @author color
	 * @param t1 时间1
	 * @param t2 时间2
	 * @return t2 与t1的间隔天敄1�7
	 * @throws ParseException 如果输入的日期格式不昄1�700-00-00 格式抛出异常
	 */
	public static int _getBetweenDays(String t1, String t2) throws ParseException {
		DateFormat format = new SimpleDateFormat(DATE_FORMAT);
		int betweenDays = 0;
		Date d1 = format.parse(t1);
		Date d2 = format.parse(t2);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		// // 保证第二个时间一定大于第丄1�7个时闄1�7
		// if (c1.after(c2)) {
		// c1 = c2;
		// c2.setTime(d1);
		// }
		int betweenYears = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		betweenDays = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR);
		for (int i = 0; i < betweenYears; i++) {
			c1.set(Calendar.YEAR, (c1.get(Calendar.YEAR) + 1));
			betweenDays += c1.getMaximum(Calendar.DAY_OF_YEAR);
		}
		return betweenDays;
	}

	/**
	 * 将Timestamp的时，分，秒转化为Integer
	 * 
	 * @param time
	 * @return
	 * @create:zhengliexin Dec 15, 2009
	 * @update:zhengliexin Dec 15, 2009
	 */
	public static Integer dateToInteger(Timestamp time) {

		SimpleDateFormat format = new SimpleDateFormat(DATETIME_SECOND_FORMAT); // "yyyy-MM-dd HH:mm:ss"
		String str = format.format(time);

		Date date = null;

		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat sh = new SimpleDateFormat("HH");
		SimpleDateFormat sm = new SimpleDateFormat("mm");
		SimpleDateFormat ss = new SimpleDateFormat("ss");

		String shour = sh.format(date);
		String sminute = sm.format(date);
		String ssecond = ss.format(date);

		StringBuffer sba = new StringBuffer();

		sba.append(shour);
		sba.append(sminute);
		sba.append(ssecond);
		String dstartTime = sba.toString();
		// 将系统时间转换为Int垄1�7
		int estartTime = Integer.parseInt(dstartTime);

		return estartTime;
	}

	/**
	 * 在Q-DTO中构建时间时使用，true为当天零点，false为第二天零点
	 * 
	 * @param str_date
	 * @param flag
	 * @return
	 * @create:zhengliexin Mar 11, 2010
	 * @update:zhengliexin Mar 11, 2010
	 */
	public static String appendTimestamp_yyyyMMdd_000000(String str_date, boolean flag) {
		StringBuilder sbuilder = new StringBuilder();

		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		Timestamp ts = null;
		Date date = null;
		try {
			date = format.parse(str_date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (flag != true) {
			ts = new Timestamp(date.getTime() + 24 * 60 * 60 * 1000);
		} else {
			ts = new Timestamp(date.getTime());
		}
		str_date = format.format(ts);
		sbuilder.append(str_date).append("-00.00.00.000000");
		return sbuilder.toString();
	}
	
	public static String getDateStrForNextDay(String date) {
		
		String	res = null;
		if (StringUtils.isNotBlank(date)) {
			Timestamp	curDT = DateTimeUtil.stringToTimestamp_yyyyMMdd(date);
			Timestamp	nextDT = DateTimeUtil.getTimestampWithoutTimeForNextDay(curDT);
			res = DateTimeUtil.getTimestampToStr(nextDT, DATE_FORMAT);
		}
		return	res;
	}
	
	
	public static String getDateStrForPrevDay(String date) {
		
		String	res = null;
		if (StringUtils.isNotBlank(date)) {
			Timestamp	curDT = DateTimeUtil.stringToTimestamp_yyyyMMdd(date);
			Timestamp	nextDT = DateTimeUtil.getTimestampWithoutTimeForPrevDay(curDT);
			res = DateTimeUtil.getTimestampToStr(nextDT, DATE_FORMAT);
		}
		return	res;
	}
	
	
	
	public static Date stringToDate(String dtStr, SimpleDateFormat format) {
		Date date = null;
		if (StringUtils.isNotBlank(dtStr)) {
			
			try {
				date = format.parse(dtStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return	date;
	}

	/**
	 * String类型的时间转为Timestamp类型(zhi)
	 * 
	 * @param strDate
	 * @return
	 * @create:zhengliexin Mar 13, 2010
	 * @update:zhengliexin Mar 13, 2010
	 */
	public static Timestamp stringToTimestamp_yyyyMMdd(String strDate) {

		SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
		Timestamp ts = null;

		Date date = null;

		try {

			date = format.parse(strDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		ts = new Timestamp(date.getTime());

		return ts;

	}
	
	
	/**
	 * String类型的时间转为Timestamp类型(zhi)
	 * 
	 * @param strDate
	 * @return
	 * @create:zhengliexin Mar 13, 2010
	 * @update:zhengliexin Mar 13, 2010
	 */
	public static Timestamp stringToTimestamp_yyyyMMddHHmmss(String strDateTime) {

		SimpleDateFormat format = new SimpleDateFormat(DATETIME_SECOND_FORMAT);
		Timestamp ts = null;

		Date date = null;
		try {
			date = format.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		ts = new Timestamp(date.getTime());
		return ts;
	}

	/**
	 * 比较两个时间大小
	 * 
	 * @param time1 第一个时闄1�7
	 * @param time2 第二个时闄1�7
	 * @return 如果丄1�7�表示第丄1�7个时间在第二个时间后靄1�7br>
	 *         如果丄1�7�表示两个时间相各1�7br>
	 *         如果丄1�7-1，表示第丄1�7个时间在第二个时间前靄1�7br>
	 *         如果丄1�79,表示有�1�7�为穄1�7
	 * @create:zhengliexin Mar 24, 2010
	 * @update:zhengliexin Mar 24, 2010
	 */
	public static int compareTimestamp(Timestamp time1, Timestamp time2) {

		int i = 1;
		if (time1 != null && time2 != null) {

			try {

				long t1 = time1.getTime();
				long t2 = time2.getTime();
				long t = t1 - t2;

				if (t > 0) {
					i = 1;
				} else if (t == 0) {
					i = 0;
				} else {
					i = -1;
				} 

			} catch (Exception e) {

				e.printStackTrace();
			}
		} else if (time1 != null && time2 == null) {
			i = 1;
		} else if (time1 == null && time2 != null) {
			i = -1;
		} else if (time1 == null && time2 == null) {
			i = 0;
		}

		return i;
	}
	
	
	public static int compareDate(Date time1, Date time2) {

		int i = 1;
		if (time1 != null && time2 != null) {

			try {

				long t1 = time1.getTime();
				long t2 = time2.getTime();
				long t = t1 - t2;

				if (t > 0) {
					i = 1;
				} else if (t == 0) {
					i = 0;
				} else {
					i = -1;
				} 
			} catch (Exception e) {

				e.printStackTrace();
			}
		} else if (time1 != null && time2 == null) {
			i = 1;
		} else if (time1 == null && time2 != null) {
			i = -1;
		} else if (time1 == null && time2 == null) {
			i = 0;
		}
		return i;
	}
	
	
	public static Date fomate2DateFromTimestamp(Timestamp stamp) {
		Date date = null;
		if (stamp != null) {
			date = new Date(stamp.getTime());
		} else {
			date = new Date();
		}
		return date;
	}
	

	
	public static String	toString(List<Timestamp> timeList) {
		String	res = "";
		if (timeList != null) {
			for (Timestamp dt: timeList) {
				res = res + " " + dt.toString();
			}
		}
		return	res;
	}
	
    
    
    public static boolean isSameDate(Timestamp date1, Timestamp date2) {
    	boolean res = false;
    	if (date1 != null && date2 != null) {
    		Timestamp date1_day = DateTimeUtil.getTimestampWithoutTime(date1);
    		Timestamp date2_day = DateTimeUtil.getTimestampWithoutTime(date2);
    		if (date1_day.equals(date2_day)) {
    			res = true;
    		}
    	}
    	return	res;
    }
    
    
   
    /**
	 * 获得偏移后的时间
	 * @param baseTime 基准时间
	 * @param offsetUnit 偏移单位 Calendar类的时间单位常量
	 * @param offsetValue 偏移值
	 * @return
	 */
	public static Timestamp getOffsetTimestamp(Timestamp baseTime, int offsetUnit, int offsetValue) {
		if (baseTime==null) return null;

		Calendar c = Calendar.getInstance(DEFAULT_TIMEZONE);
		c.setTime(baseTime);
		Timestamp newTime=null;

		c.set(offsetUnit, c.get(offsetUnit) + offsetValue);
		newTime=new Timestamp(c.getTimeInMillis());
		
		return  newTime;
	}
	
	
	public static Date getCurrentDateWithoutTimeByTimeZone() {
		Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE); 
		calendar = clearCalendarTime(calendar);
	    return calendar.getTime();
	}
	
	/**    
	 * 得到本月的第一天    
	 * @return    
	 */     
	public static Date getCurrentMonthFirstDay() {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);      
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMinimum(Calendar.DAY_OF_MONTH));      
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	/*******************
	 * 获取本周第一天：周日
	 * @return
	 */
	public static Date getCurrentWeekFirstDay() {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);      
	    calendar.set(Calendar.DAY_OF_WEEK, calendar      
	            .getActualMinimum(Calendar.DAY_OF_WEEK));
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	/*******************
	 * 获取本周最后一天：周六
	 * @return
	 */
	public static Date getCurrentWeekLastDay() {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);      
	    calendar.set(Calendar.DAY_OF_WEEK, calendar      
	            .getActualMaximum(Calendar.DAY_OF_WEEK));
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	public static Date getWeekFirstDay(Date oldDate) {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);

	    calendar.set(Calendar.DAY_OF_WEEK, calendar      
	            .getActualMinimum(Calendar.DAY_OF_WEEK));      
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	public static Date getDateBeforeWithoutTime(Date oldDate, int beforeDays) {
		Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar.add(Calendar.DATE, - beforeDays);  
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();
	}
	
	public static Date getDateAfterWithoutTime(Date oldDate, int afterDays) {
		Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar.add(Calendar.DATE, afterDays);  
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();
	}
	
	public static Date getDateWithoutTime(Date oldDate) {
		Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();
	}
	
	public static Date getWeekLastDay(Date oldDate) {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar.set(Calendar.DAY_OF_WEEK, calendar      
	            .getActualMaximum(Calendar.DAY_OF_WEEK));      
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	public static Date getMonthFirstDay(Date oldDate) {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMinimum(Calendar.DAY_OF_MONTH));      
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	public static Date getMonthLastDay(Date oldDate) {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMaximum(Calendar.DAY_OF_MONTH));      
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	}
	     
	/**    
	 * 得到本月的最后一天    
	 *     
	 * @return    
	 */     
	public static Date getCurrentMonthLastDay() {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);      
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMaximum(Calendar.DAY_OF_MONTH));
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	}
	
	/**    
	 * 得到上月的第一天    
	 * @return    
	 */     
	public static Date getLastMonthFirstDay() {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.add(Calendar.MONTH, -1);
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMinimum(Calendar.DAY_OF_MONTH));
	    
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	/**
	 * 
	* @Title: getDateSevPre 
	* @Description: 取day天前时间 
	* @param @return    设定文件 
	* @return Date    返回类型 
	* @throws
	 */
	  public static Date getDatePre(int day){
		   Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
		   calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)-day);
		   calendar = clearCalendarTime(calendar);
		   return calendar.getTime();
	    }
	/**
	 * 
	* @Title: getLastFourWeekFirstDay 
	* @Description: 取week周前第一天的时间 
	* @param @return    设定文件 
	* @return Date    返回类型 
	* @throws
	 */
	public static Date getLastWeekFirstDay(int week) {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.add(Calendar.WEDNESDAY, -week);
	    calendar.set(Calendar.DAY_OF_WEEK, calendar      
	            .getActualMinimum(Calendar.DAY_OF_WEEK));
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	/**    
	 * 得到本年的第一天    
	 * @return    
	 */     
	public static Date getCurrentYearFirstDay() {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMinimum(Calendar.DAY_OF_MONTH));      
	     
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	public static Date getYearFirstDay(Date oldDate) {      
	    Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
	    calendar.setTime(oldDate);
	    
	    calendar.set(Calendar.MONTH, calendar.getActualMinimum(Calendar.MONTH));
	    calendar.set(Calendar.DAY_OF_MONTH, calendar      
	            .getActualMinimum(Calendar.DAY_OF_MONTH));      
	     
	    calendar = clearCalendarTime(calendar);
	    return calendar.getTime();      
	} 
	
	public static Calendar clearCalendarTime(Calendar calendar) {
		if (calendar!= null) {
			 calendar.set(Calendar.HOUR_OF_DAY, 0);
			 calendar.set(Calendar.MINUTE, 0);
			 calendar.set(Calendar.SECOND, 0);
			 calendar.set(Calendar.MILLISECOND, 0);
		}
		return calendar;
	}
	public static String stringFormatToString(String strDate,String sformat) {

		SimpleDateFormat format = new SimpleDateFormat(sformat);
		SimpleDateFormat newFormat = new SimpleDateFormat(DATETIME_SECOND_FORMAT);	
		Date date = null;

		try {

			date = format.parse(strDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return newFormat.format(date);

	}
	public static String stringDateTime(Date date,String sformat) {
		if (date == null)
			return null;
		return new SimpleDateFormat(sformat).format(date.getTime());
	}
	public static String dateFormatToString(Date date,String formatStr){
		String dateStr = "";
		try
		{	
			
			if(date != null && formatStr != null ){
				SimpleDateFormat format = new SimpleDateFormat(formatStr);
				dateStr = format.format(date);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return dateStr;
	}
	public static Date stringFormatToDate(String strDate,String sformat) {
		if(strDate!=null && !"".equals(strDate)){
			SimpleDateFormat format = new SimpleDateFormat(sformat);
			Date date = null;
	
			try {
	
				date = format.parse(strDate);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
	
			return date;
		}else{
			return null;
		}

	}
	
	public static String StringMinuteTime(Date date) {
		if (date == null)
			return null;
		return new SimpleDateFormat(DATETIME_SECOND_FORMAT_YYYY_MM_DD_HH_MM_SS).format(date.getTime());
	}
	//得到n分钟前(后)的时间
	public static Date getMinuteAferDate(int min) {
		Calendar calendar = Calendar.getInstance(DEFAULT_TIMEZONE);
		calendar.add(Calendar.MINUTE, min);
	    return calendar.getTime();
	}
	public static Date stringformatAmPmToDate(String date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd K:m a",Locale.ENGLISH);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static Long	getCurrentDateNumber() {
		String	dateStr = new SimpleDateFormat(TIMESTAMP_FORMAT_DAY).format(getCurrentDateByTimeZone());
		Long	dateLong = Long.valueOf(dateStr);
		return	dateLong;
	}
	
	public static Long	getDateNumber(Date date) {
		if(date==null) return null;
		String	dateStr = new SimpleDateFormat(TIMESTAMP_FORMAT_DAY).format(date);
		Long	dateLong = Long.valueOf(dateStr);
		return	dateLong;
	}
	
	public static Long	getDateNumberWithTimeNumber(Date date) {
		if(date==null) return null;
		String	dateStr = new SimpleDateFormat(TIMESTAMP_FORMAT_SEC).format(date);
		Long	dateLong = Long.valueOf(dateStr);
		return	dateLong;
	}
	
	/**
	 * 将java.util.Date转为javax.xml.datatype.XMLGregorianCalendar
	 * @param date
	 * @return
	 */
	public static javax.xml.datatype.XMLGregorianCalendar getXMLGregorianCalendar(java.util.Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		javax.xml.datatype.DatatypeFactory dtf=null;
		try {
			dtf = javax.xml.datatype.DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return dtf.newXMLGregorianCalendar(
		calendar.get(Calendar.YEAR),
		calendar.get(Calendar.MONTH)+1,
		calendar.get(Calendar.DAY_OF_MONTH),
		calendar.get(Calendar.HOUR),
		calendar.get(Calendar.MINUTE),
		calendar.get(Calendar.SECOND),
		calendar.get(Calendar.MILLISECOND),
		calendar.get(Calendar.ZONE_OFFSET)/(1000*60));
	}
	public static String getCustomDate(String dateStr){
		String res="";
		
		String deliveryDate=dateStr.replace("-", "");
		if(deliveryDate.length()>10){
			String dd=deliveryDate.substring(0, 11);
			StringBuffer s=new StringBuffer();
			s.append(dd);
			s.append("0000");
			res=s.toString();
		}
		return res;
		
	}
	 public static Date getAfterDate(Date d){
		  Date date = null;
		  Calendar calendar = Calendar.getInstance();  
		  calendar.setTime(d);  
		  calendar.add(Calendar.DAY_OF_MONTH,1);  
		  date = calendar.getTime();  
		  return date;
	}
	 public static String stringDateFormat(String strDate,String sourceFormat,String endFormat) {
		 	
			SimpleDateFormat format = new SimpleDateFormat(sourceFormat);
			SimpleDateFormat newFormat = new SimpleDateFormat(endFormat);	
			Date date = null;

			try {

				date = format.parse(strDate);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}

			return newFormat.format(date);

		}
	 
	 public static void main(String args[]){
		 UserModifyDetailDTO insertDto=new UserModifyDetailDTO();
		 insertDto.setAfterChange(null);
		 UserModifyDetailDTO insertDto2=new UserModifyDetailDTO();
		 insertDto2.setAfterChange(null);
		 System.out.println(!insertDto.getAfterChange().equals(insertDto2.getAfterChange()));
	 }
	 
}
