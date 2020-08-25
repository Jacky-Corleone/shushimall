package com.camelot.openplatform.common.fastdfs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager implements FileManagerConfig{

	/**
	 * 
	 */
	private static final long serialVersionUID = 736123137449356840L;
	
	private static Logger logger  = LoggerFactory.getLogger(FileManager.class);  
    
    private static TrackerClient  trackerClient;  
    private static TrackerServer  trackerServer;  
    private static StorageServer  storageServer;  
    private static StorageClient1  storageClient1;  
  
    static { // Initialize Fast DFS Client configurations  
          
        try {  
            String classPath = new File(FileManager.class.getResource("/").getFile()).getCanonicalPath();  
              
            String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;  
            fdfsClientConfigFilePath = "src/main/resources/fdfs_client.conf";
            //private static final String CONF_FILENAME = "src/main/resources/fdfs/fdfs_client.conf";
            logger.info("Fast DFS configuration file path:" + fdfsClientConfigFilePath);  
            ClientGlobal.init(fdfsClientConfigFilePath);  
              
            trackerClient = new TrackerClient();  
            trackerServer = trackerClient.getConnection();  
              
            storageClient1 = new StorageClient1(trackerServer, storageServer);  
              
        } catch (Exception e) {  
        	//logger.error(logger,  e);  
        	logger.error(e.getMessage());
              
        }  
    }  
      
      
      
    public static String upload(FastDFSFile file) {  
    	
    	
    	logger.info("File Name: {}" + file.getName() + "     File Length: {}" + file.getContent().length);  
          
        NameValuePair[] meta_list = new NameValuePair[3];  
        meta_list[0] = new NameValuePair("width", "120");  
        meta_list[1] = new NameValuePair("heigth", "120");  
        meta_list[2] = new NameValuePair("author", "Diandi");  
          
        long startTime = System.currentTimeMillis();  
        String[] uploadResults = null;  
        try {  
            uploadResults = storageClient1.upload_file(file.getContent(), file.getExt(), meta_list); 
            String s = "";
            for (int i = 0; i < uploadResults.length; i++) {
				s+=uploadResults[i];
			}
            System.out.println(s);
        } catch (IOException e) {  
            logger.error("IO Exception when uploadind the file: {}" + file.getName(), e);  
        } catch (Exception e) {  
            logger.error("Non IO Exception when uploadind the file: {}" + file.getName(), e);  
        }  
        logger.info("upload_file time used:{} " + (System.currentTimeMillis() - startTime) + " ms");  
          
        if (uploadResults == null) {  
        	logger.error("upload file fail, error code: {} " + storageClient1.getErrorCode());  
        }  
          
        String groupName        = uploadResults[0];  
        String remoteFileName   = uploadResults[1];  
          
        String fileAbsolutePath = PROTOCOL + trackerServer.getInetSocketAddress().getHostName()   
                + SEPARATOR  
                + TRACKER_NGNIX_PORT  
                + SEPARATOR   
                + groupName   
                + SEPARATOR   
                + remoteFileName;  
          
          
        logger.info("upload file successfully!!!  " +"group_name: {}" + groupName + ", remoteFileName: {}"  
                + " " + remoteFileName);  
          
        return fileAbsolutePath;  
          
    }  
      
    public static FileInfo getFile(String groupName, String remoteFileName) {  
        try {  
            return storageClient1.get_file_info(groupName, remoteFileName);  
        } catch (IOException e) {  
            logger.error("IO Exception: Get File from Fast DFS failed {}", e);  
        } catch (Exception e) {  
            logger.error("Non IO Exception: Get File from Fast DFS failed {}", e);  
        }  
        return null;  
    }  
    
    
    public static byte[] downloadFile(String groupName, String remoteFileName) {  
        try {  
        	return storageClient1.download_file(groupName, remoteFileName);
            //return storageClient.get_file_info(groupName, remoteFileName);  
        } catch (IOException e) {  
            logger.error("IO Exception: Get File from Fast DFS failed {}", e);  
        } catch (Exception e) {  
            logger.error("Non IO Exception: Get File from Fast DFS failed {}", e);  
        }  
        return null;  
    }  
    
    
    
    public static boolean fileIsExist(String fileId) {  
        try {  
        	 byte[] fileByte = FileManager.getStorageClient1().download_file1(fileId);
        	 if(fileByte != null && fileByte.length >0){
        		 return true;
        	 }
        } catch (IOException e) {  
            logger.error("IO Exception: Get File from Fast DFS failed {}", e);  
        } catch (Exception e) {  
            logger.error("Non IO Exception: Get File from Fast DFS failed {}", e);  
        }  
        return false;  
    }  
    
    
    public static boolean generateThumbnail(String fileId, String sizeProduct, String extensionName) {  
    	try {
    		String[] size = sizeProduct.split(Constant.PIC_HEIGHT_WIDTH_DELIMITER);
			int width = Integer.parseInt(size[0]);
			int height = Integer.parseInt(size[1]);
			byte[] download_file1 = FileManager.getStorageClient1().download_file1(fileId);
			
			BufferedImage asBufferedImage = Thumbnails.of(new ByteArrayInputStream(download_file1)).size(width, height).asBufferedImage();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			boolean write = ImageIO.write(asBufferedImage, extensionName, out);
			if (write) {
				byte[] byteArray = out.toByteArray();
				String upload_file = FileManager
						.getStorageClient1()
						.upload_file1(
								fileId,
								Constant.SUB_FILE_NAME_EXTEND+sizeProduct, byteArray, extensionName, null);
				//String fileAbsolutePath = FileManager.upload(file); 
				System.out.println(upload_file);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
    }  
    
    
      
    public static TrackerClient getTrackerClient() {
		return trackerClient;
	}

	public static void setTrackerClient(TrackerClient trackerClient) {
		FileManager.trackerClient = trackerClient;
	}

	public static TrackerServer getTrackerServer() {
		return trackerServer;
	}

	public static void setTrackerServer(TrackerServer trackerServer) {
		FileManager.trackerServer = trackerServer;
	}

	public static StorageServer getStorageServer() {
		return storageServer;
	}

	public static void setStorageServer(StorageServer storageServer) {
		FileManager.storageServer = storageServer;
	}

	public static StorageClient1 getStorageClient1() {
		return storageClient1;
	}

	public static void setStorageClient1(StorageClient1 storageClient1) {
		FileManager.storageClient1 = storageClient1;
	}

	public static void deleteFile(String groupName, String remoteFileName) throws Exception {  
        storageClient1.delete_file(groupName, remoteFileName);  
    }  
      
    public static StorageServer[] getStoreStorages(String groupName) throws IOException {  
        return trackerClient.getStoreStorages(trackerServer, groupName);  
    }  
      
    public static ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws IOException {  
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);  
    }  

}
