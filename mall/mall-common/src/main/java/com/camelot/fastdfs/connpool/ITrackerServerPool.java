package com.camelot.fastdfs.connpool;

import org.csource.fastdfs.TrackerServer;

public interface ITrackerServerPool {
	
	 public TrackerServer geTrackerServer() throws Exception;  
     public TrackerServer geTrackerServer(long timeout) throws Exception;  
     public boolean close(TrackerServer server) throws Exception;  
     public void reset() throws Exception; 

}
