package com.camelot.openplatform.common;

import java.util.Date;

public class DateCommon {

	/*
		判断2个日期区间是否符合要求（没有交集）
	*/
	public static Boolean isHaveNoIntersection(Date startTime,Date endTime,Date usedStartTime,Date usedEndTime){
		if(startTime.getTime()>=usedEndTime.getTime()){
			return true;
		}else if(endTime.getTime()<=usedStartTime.getTime()){
			return true;
		}else{
			return false;
		}
	}
}
