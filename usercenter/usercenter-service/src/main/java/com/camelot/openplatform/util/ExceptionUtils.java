package com.camelot.openplatform.util;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.camelot.openplatform.common.exception.CommonCoreException;

public class ExceptionUtils {

	
	public static void throwThrowable(Throwable t, Logger log) throws CommonCoreException {
		
		if (log != null)
			log.error(t.getMessage(), t);
		
		throw new CommonCoreException(t.getMessage(), t);
		
	}
	
	public static void throwSysException(RuntimeException t, Logger log) throws CommonCoreException {
		
		if (log != null)
			log.error(t.getMessage(), t);
		
		throw new CommonCoreException(t.getMessage(), t);
		
	}
	
	
	public static void throwCoreException(String expCode, String suffixMsg, Throwable t, Logger log) throws CommonCoreException {
		
		//Locale myLocale = GlobalConstant.DEFAULT_LOCALE_ZH_CN;
		
		String  expMsg = expCode;
		try {
			expMsg = expCode;
		} catch (Exception e) {
		}
		

		String	errorMsg = StringUtils.stripToEmpty(expMsg);
		if (StringUtils.isNotBlank(suffixMsg)) {
			if (StringUtils.isBlank(errorMsg)) 
				errorMsg = StringUtils.stripToEmpty(suffixMsg);
			else 
				errorMsg = errorMsg + " [ " + StringUtils.stripToEmpty(suffixMsg) + " ] ";
		}
		if (log != null)
			log.error(errorMsg, t);
		
		throw new CommonCoreException( errorMsg, t);
		
	}
}
