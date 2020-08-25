package com.camelot.fastdfs;

import java.io.Serializable;

public interface  FileManagerConfig extends Serializable{

	
	public static final String FILE_DEFAULT_WIDTH   = "120"; 
	
    public static final String FILE_DEFAULT_HEIGHT  = "120";
    
    public static final String FILE_DEFAULT_AUTHOR  = "jacky";  
      
    public static final String PROTOCOL = "http://";  
    public static final String SEPARATOR = "/";  
      
    public static final String TRACKER_NGNIX_PORT   = "8000";  
    
    public static final String PORT_SEPARATOR   = ":";  
    
    public static final String TRACKER_NGNIX_HOST = "192.168.1.186"; 
    
    public static final String CLIENT_CONFIG_FILE   = "fdfs_client.conf"; 

}
