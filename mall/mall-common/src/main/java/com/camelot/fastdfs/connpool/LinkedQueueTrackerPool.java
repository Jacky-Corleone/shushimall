package com.camelot.fastdfs.connpool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.ProtoCommon;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

public class LinkedQueueTrackerPool implements ITrackerServerPool {
	
	
	 private LinkedBlockingQueue<TrackerServer> tspool = null;   
     private int poolSize;
	
     
     public LinkedQueueTrackerPool(String clientConfigPath, int poolSize)   
             throws FileNotFoundException, IOException, MyException, InterruptedException {  
    	 	super();  
    	 	this.poolSize = poolSize;  
    	 	ClientGlobal.init(clientConfigPath);  
    	 	init();  
     }  
	 
     
     /** 
      * @Description: TODO(使用远DFSAPI来保持长连接，轮询，开销小于创建一个新连接) 
      * @author LiuYi 
       */  
     private void keepingPool() {  
         Timer timer = new Timer();  
         timer.schedule(new TimerTask() {  
                 @Override  
                 public void run() {
                	 
                     for(TrackerServer ts : tspool){  
                             try {  
                                     ProtoCommon.activeTest(ts.getSocket());  
                             } catch (IOException e) {  
                                     e.printStackTrace();  
                             }  
                     }  
                 }  
         }, 30*1000, 30*1000);  
           
           
     }  
     
     
     /** 
      *  
     * @Description: TODO(初始化方法) 
     * @author LiuYi 
     *  @throws IOException 
     *  @throws InterruptedException  void 
      */  
     private void init() throws IOException, InterruptedException{  
             this.tspool = new LinkedBlockingQueue<>(this.poolSize);  
             for(int i=0;i<this.poolSize;i++){  
                     TrackerClient tc = new TrackerClient();  
                     TrackerServer ts = tc.getConnection();  
                     ProtoCommon.activeTest(ts.getSocket());  
                     this.tspool.put(ts);  
             }  
             keepingPool();  
     }  
     
     
	@Override
	public TrackerServer geTrackerServer() throws Exception {
		 return this.tspool.poll();  
		
	}

	@Override
	public TrackerServer geTrackerServer(long timeout) throws Exception {
		TrackerServer ts = this.geTrackerServer();  
        if(ts == null){  
                return this.tspool.poll(timeout, TimeUnit.SECONDS);  
        }  
        return ts;
	}

	@Override
	public boolean close(TrackerServer server) throws Exception {
		if(server != null){  
            this.tspool.put(server);  
            return true;  
		}  
		return false; 
	}

	@Override
	public void reset() throws Exception {
		 this.init(); 

	}

}
