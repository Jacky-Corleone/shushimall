package com.camelot.fastdfs;

import java.io.ByteArrayInputStream;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.log4j.Logger;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.junit.Test;

public class FastDFSUtils {
	
	private static Logger logger = Logger.getLogger(FastDFSUtils.class);
	private static TrackerClient tracker = null;
	private static TrackerServer trackerServer = null;
    private static StorageServer storageServer = null;
    private static StorageClient1 client = null;
		static{
		    try {
		        ClientGlobal.init("src/main/resources/fdfs_client.conf");
		        tracker = new TrackerClient();
		        trackerServer = tracker.getConnection();
		        client = new StorageClient1(trackerServer, storageServer); 
		    } catch (Exception e) {
		        throw new RuntimeException(e);
		    }
		}
		
		public static String uploadFile(String filePath) throws Exception{        
		    String fileId = ""; 
		    String fileExtName = ""; 
		    if (filePath.contains(".")) { 
		        fileExtName = filePath.substring(filePath.lastIndexOf(".") + 1); 
		    } else { 
		        logger.warn("Fail to upload file, because the format of filename is illegal."); 
		        return fileId; 
		    } 
		
		    //建立连接 
		    /*.......*/
		
		    //上传文件 
		    try { 
		        fileId = client.upload_file1(filePath, fileExtName, null); 
		    } catch (Exception e) { 
		        logger.warn("Upload file \"" + filePath + "\"fails"); 
		    }finally{
		        trackerServer.close();
		    }        
		    return fileId; 
		}
		
		
		public static String uploadSlaveFile(String masterFileId, String prefixName, String slaveFilePath) throws Exception{
		    String slaveFileId = ""; 
		    String slaveFileExtName = ""; 
		    if (slaveFilePath.contains(".")) { 
		        slaveFileExtName = slaveFilePath.substring(slaveFilePath.lastIndexOf(".") + 1); 
		    } else { 
		        logger.warn("Fail to upload file, because the format of filename is illegal."); 
		        return slaveFileId; 
		    } 
		
		    //建立连接 
		    /*.......*/
		
		    //上传文件 
		    try { 
		        slaveFileId = client.upload_file1(masterFileId, prefixName, slaveFilePath, slaveFileExtName, null); 
		    } catch (Exception e) { 
		        logger.warn("Upload file \"" + slaveFilePath + "\"fails"); 
		    }finally{
		        trackerServer.close();
		    }
		
		    return slaveFileId;
		}
		
		public static String uploadSlaveFile2(String masterFileId, String prefixName, byte[] content, String slaveFileExtName) throws Exception{
		    String slaveFileId = ""; 
		    //String slaveFileExtName = ""; 
		    /*if (slaveFilePath.contains(".")) { 
		        slaveFileExtName = slaveFilePath.substring(slaveFilePath.lastIndexOf(".") + 1); 
		    } else { 
		        logger.warn("Fail to upload file, because the format of filename is illegal."); 
		        return slaveFileId; 
		    } */
		
		    //建立连接 
		    /*.......*/
		
		    //上传文件 
		    try { 
		        slaveFileId = client.upload_file1(masterFileId, prefixName, content, slaveFileExtName, null); 
		    } catch (Exception e) { 
		        logger.warn("Upload file \"" + content + "\"fails"); 
		    }finally{
		        trackerServer.close();
		    }
		
		    return slaveFileId;
		}
		
		public static int download(String fileId, String localFile) throws Exception{   
		    int result = 0;
		    //建立连接 
		  /*  TrackerClient tracker = new TrackerClient();
		    TrackerServer trackerServer = tracker.getConnection();
		    StorageServer storageServer = null;
		    StorageClient1 client = new StorageClient1(trackerServer, storageServer); */
		
		    //上传文件 
		    try { 
		        result = client.download_file1(fileId, localFile); 
		    } catch (Exception e) { 
		        logger.warn("Download file \"" + localFile + "\"fails"); 
		    }finally{
		        trackerServer.close();
		    }
		
		    return result;
		}
		
		@Test 
	    public void testDownload() {
	        try {
	        	byte[] download_file1 = client.download_file1("group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.jpg");
	        	//byte[] b = FileManager.downloadFile("group1", "M00/00/00/wKgBu1cGP2mADUUYAErASNF-ON0393.jpg");
	            //storageClient.download_file("group1", "M00/00/00/wKgBu1cGP2mADUUYAErASNF-ON0393.jpg"); 
	        	System.out.println(download_file1.length);
	        	Thumbnails.of(new ByteArrayInputStream(download_file1)).size(200, 200).toFile("D:/kkk.jpg");
	        	/*ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	        	boolean write = ImageIO.write(asBufferedImage, "JPG", out);
	        	if(write){
	        		byte[] byteArray = out.toByteArray();
	        		//FastDFSFile file = new FastDFSFile("200*200", byteArray, "jpg");  
	        		//String upload_file = client.upload_file1("group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.jpg", "_200*200", byteArray, "JPG", null);
	    	        //String fileAbsolutePath = FileManager.upload(file); 
	        		String uploadSlaveFile2 = uploadSlaveFile2("group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.jpg", "_200*200", byteArray, "JPG");
	        		System.out.println(uploadSlaveFile2);
	    	        
	        	}*/
	        	
	        	String uploadSlaveFile2 = client.upload_file1("group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.jpg", "_200*200", "D:/kkk.jpg", "JPG",null);
	           System.out.println(uploadSlaveFile2);
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	        }
	    }
		
		
		public static void main(String[] args) {
		    try {
		            String masterFileId = uploadFile("D:/sofeware/pic/DSC_0680.JPG");
		            System.out.println(masterFileId);
		            download(masterFileId, "D:/master.png");
		
		            String slaveFileId = uploadSlaveFile(masterFileId, "_120x120", "D:/sofeware/pic/DSC_0680.JPG");
		            System.out.println(slaveFileId);
		
		            download(slaveFileId, "D:/slave.png");
		        } catch (Exception e) {
		            logger.error("upload file to FastDFS failed.", e);
		        }
		    }
}

