package com.camelot.fastdfs.connpool;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.fastdfs.FileManager;

public class TrackerServerPoolManager {
	
	
	public TrackerServerPoolManager() {
		super();
	}

	private static ITrackerServerPool pool = null;  
    private static int waitTime = 3;
    private static Logger logger  = LoggerFactory.getLogger(TrackerServerPoolManager.class); 
    public static final String CLIENT_CONFIG_FILE   = "fdfs_client.conf"; 
    static{
    	try {
    		String fdfsClientConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + CLIENT_CONFIG_FILE;
    		//String fdfsClientConfigFilePath = "src/main/resources/fdfs_client.conf";
            logger.info("Fast DFS configuration file path:" + fdfsClientConfigFilePath);
        	pool = new LinkedQueueTrackerPool(fdfsClientConfigFilePath, 10);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }
    
    
     public TrackerServer getTrackerServer(){
    	 
    	 TrackerServer ts = null;
		try {
			ts = pool.geTrackerServer(waitTime);
			 return ts;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ts;  
    	
     }
     
     public void closeTrackerServer(TrackerServer trackerServer) {
 		try {
			this.pool.close(trackerServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
 	}
    
    public static void main(String[] args) throws IOException, MyException {
    	TrackerServerPoolManager trackerServerPoolManager = new TrackerServerPoolManager();
    	TrackerServer trackerServer = trackerServerPoolManager.getTrackerServer();
    	StorageClient1 storageClient1 = new StorageClient1(trackerServer, null);
    	FileInputStream fileInputStream = new FileInputStream("C:/Users/jacky/Downloads/wKgBvFcNPt-AcQggAABMaijauzw113.JPG");
    	String upload_file1 = storageClient1.upload_file1(IOUtils.toByteArray(fileInputStream), "JPG", null);
    	trackerServerPoolManager.closeTrackerServer(trackerServer);
    	System.out.println(upload_file1);
	}



}
