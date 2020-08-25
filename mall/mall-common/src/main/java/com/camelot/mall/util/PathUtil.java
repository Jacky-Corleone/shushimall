package com.camelot.mall.util;

import java.io.File;
import java.net.URISyntaxException;

/**
 * 获取项目根目录的绝对路径工具类,windows于linux兼容
 * @author 卓家进
 * create on 2016年1月27日
 */
public class PathUtil {
	
	/** 
	 * @Description: 取得项目根路径，并根据不同的操作系统返回合适路径
	 * @param path 水印图片路径
	 * @return     水印图片完整绝对路径
	 * @author 卓家进
	 * @version 1.0
	 * Create on 2016年1月29日
	 * Copyright (c) 2015 大家智合网络科技有限公司
	 */
	public static String getRootPath(String path) {
		
		String classPath = "";
		try {
			classPath = PathUtil.class.getClassLoader().getResource("/").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		};
		  String rootPath  = "";
		  
		  //windows系统下
		  if("\\".equals(File.separator)){   
		   rootPath  = classPath.substring(1,classPath.indexOf("/WEB-INF/classes")) + path;
		   rootPath = rootPath.replace("/", "\\");
		  }
		  
		  //linux系统下
		  if("/".equals(File.separator)){   
		   rootPath  = classPath.substring(0,classPath.indexOf("/WEB-INF/classes")) + path;
		   rootPath = rootPath.replace("\\", "/");
		  }
		  
		  return rootPath;
		 }
}
