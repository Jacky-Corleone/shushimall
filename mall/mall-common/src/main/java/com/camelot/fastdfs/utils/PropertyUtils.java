package com.camelot.fastdfs.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils{
	
	
	
	private PropertyUtils(){}
	
	private static Map<String, Properties> configMap = new HashMap<String, Properties>();
	
	public static final PropertyUtils getInstance() {  
        return SingletonHolder.INSTANCE; 
    } 
	
	
	public Properties getProp(String propFileName){
		InputStream inputStream = null;
		Properties properties = null;
		properties = configMap.get(properties);
		if(properties == null){
			try {
				properties = new Properties();
				inputStream = PropertyUtils.class.getClassLoader().getResourceAsStream(propFileName);
				//inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
				if(inputStream != null){
					properties.load(inputStream);
					configMap.put(propFileName, properties);
				}else {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try {
					if(inputStream != null){
						inputStream.close();
					}
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			}
		}
		return properties;
		
		
	}	
	
	
	public static void main(String[] args) {
		Properties prop = PropertyUtils.getInstance().getProp("fdfs_client.conf");
		String secret_key = prop.getProperty("http.secret_key");
		String tracker_http_port = prop.getProperty("http.tracker_http_port");
		
		System.out.println(secret_key);
		System.out.println(tracker_http_port);
	}
	
	
	private static class SingletonHolder {  
        private static final PropertyUtils INSTANCE = new PropertyUtils();  
    } 
	
}
