package com.camelot.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper{
	//存放url中不需要过滤的字段名
	private Map<String,String> filedMap = new HashMap<String,String>();
	
	public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
	public XssHttpServletRequestWrapper(HttpServletRequest servletRequest ,String filed){
		super(servletRequest);
		String[] filedArr = filed.split(",");
		for(int i = 0 ; i < filedArr.length ; i++){
			filedMap.put(filedArr[i].trim(), "true");
		}
	}
    public String[] getParameterValues(String parameter) {
      String[] values = super.getParameterValues(parameter);
      if (values==null)  {
                  return null;
          }
      int count = values.length;
      String[] encodedValues = new String[count];
      for (int i = 0; i < count; i++) {
    	  //如果filedMap中不存在某个字段，则过滤这个字段
    	  if(filedMap.get(parameter)==null){
             encodedValues[i] = cleanXSS(values[i]);
    	  }else{
    		  encodedValues[i] = values[i];
    	  }
       }
      return encodedValues;
    }
    public String getParameter(String parameter) {
          String value = super.getParameter(parameter);
          if (value == null) {
             return null;
          }
          //如果filedMap中不存在某个字段，则过滤这个字段
          //如果存在，则不过滤这个字段
    	  if(filedMap.get(parameter)==null){
             return cleanXSS(value);
    	  }
          return value;
    }
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null){
            return null;
        }
        return cleanXSS(value);
    }
    private String cleanXSS(String value) {
    	//过滤特殊字符
    	String result = WebUtil.getInstance().htmlToText(value);
        return result;
    }
}
