package com.camelot.fastdfs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.csource.common.MyException;
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
            /*String classPath = new File(FileManager.class.getResource("/").getFile()).getCanonicalPath();  
            String fdfsClientConfigFilePath = classPath + File.separator + CLIENT_CONFIG_FILE; */ 
        	//String fdfsClientConfigFilePath = "src/main/resources/fdfs_client.conf";
           //private static final String CONF_FILENAME = "src/main/resources/fdfs/fdfs_client.conf";
        	
        	String fdfsClientConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + CLIENT_CONFIG_FILE;
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
    
    
    public static String generateThumbnail(String fileId, String sizeProduct, String extensionName) {  
    	try {
    		String[] size = sizeProduct.split(Constant.PIC_HEIGHT_WIDTH_DELIMITER);
			int width = Integer.parseInt(size[0]);
			int height = Integer.parseInt(size[1]);
			byte[] download_file1 = FileManager.getStorageClient1().download_file1(fileId);
			// 获取源图片字节
			BufferedImage bufferedImageTemp = Thumbnails.of(new ByteArrayInputStream(download_file1)).forceSize(width, height).asBufferedImage();
			ByteArrayOutputStream writeToBuf = writeToBuf(bufferedImageTemp, PictureUtils.getExtensionName(fileId));
			if(writeToBuf != null){
				byte[] byteArray = writeToBuf.toByteArray();
				//生成缩略图
				String upload_file = FileManager
						.getStorageClient1()
						.upload_file1(
								fileId,
								Constant.SUB_FILE_NAME_EXTEND + sizeProduct, byteArray, extensionName, null);
				//String fileAbsolutePath = FileManager.upload(file); 
				return getRealPath(upload_file);

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
    }
    
    //上传原始图片
    public static String uploadOriginal(InputStream inputStream , String local_filename) {
    	if(inputStream == null){
    		return null;
    	}
    	try {
    		if(PictureUtils.isPicType(local_filename)){
    			byte[] bytes = IOUtils.toByteArray(inputStream);
    			String upload_file1 = FileManager
    					.getStorageClient1().upload_file1(bytes, PictureUtils.getExtensionName(local_filename), null);
    			return getRealPath(upload_file1);
    		}else {
    			return null;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (MyException e) {
			e.printStackTrace();
			return null;
		}finally{
			IOUtils.closeQuietly(inputStream);
		}
    	
    }
    
    //上传指定尺寸大小的图片
    public static String uploadBySize(InputStream inputStream , String local_filename, Integer width , Integer height) {
    	if(StringUtils.isEmpty(local_filename)){
    		return null;
    	}
    	try {
    		
    		if(PictureUtils.isPicType(local_filename)){
    			BufferedImage bufferedImageTemp = Thumbnails.of(inputStream).forceSize(width, height).asBufferedImage();
    			ByteArrayOutputStream writeToBuf = writeToBuf(bufferedImageTemp, PictureUtils.getExtensionName(local_filename));
    			if(writeToBuf != null){
    				String upload_file1 = FileManager
        					.getStorageClient1().upload_file1(writeToBuf.toByteArray() , PictureUtils.getExtensionName(local_filename), null);
    				return getRealPath(upload_file1);
    			}else {
    				return null;
				}
    			
    			
    		}else {
    			return null;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (MyException e) {
			e.printStackTrace();
			return null;
		}
    	
    }
    
    
    
    /** 
     * 上传带图片水印的图片 --- 根据文件地址 生成图片缩略图 
     * @param local_filename 图片源文件地址 
     * @param targetIns 图片源文件流
     * @param waterMarkPath 水印图片流
     * @param  position  枚举值
     * @return 
     */  
    public static String uploadWithWaterMarkPath(InputStream targetIns, String local_filename, InputStream waterMarkIns, Position position) {
		if (StringUtils.isEmpty(local_filename)) {
			return null;
		}
		try {

			if (PictureUtils.isPicType(local_filename)) {
				BufferedImage bufferedImageTemp = Thumbnails
						.of(targetIns)
						.watermark(position,
								 ImageIO.read(waterMarkIns), 0.3f)
						.scale(1f).asBufferedImage();
				ByteArrayOutputStream writeToBuf = writeToBuf(
						bufferedImageTemp,
						PictureUtils.getExtensionName(local_filename));
				if (writeToBuf != null) {
					String upload_file1 = FileManager.getStorageClient1()
							.upload_file1(
									writeToBuf.toByteArray(),
									PictureUtils
											.getExtensionName(local_filename),
									null);
					return getRealPath(upload_file1);
				} else {
					return null;
				}

			} else {
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (MyException e) {
			e.printStackTrace();
			return null;
		}

	}
    
    
    
    /** 
     * 上传带图片水印的图片 --- 根据文件地址 生成图片缩略图   效果不佳（不建议使用）
     * @param local_filename 图片源文件地址 
     * @param waterMarkWord 图片缩略图水印文字
     * @param  position  枚举值
     * @return 
     */  
    	public static String uploadWithWaterMarkWord(InputStream inputStream, String local_filename, String waterMarkWord, Position position) {
		if (StringUtils.isEmpty(local_filename)) {
			return null;
		}
		try {

			if (PictureUtils.isPicType(local_filename)) {
				BufferedImage waterMark = PictureUtils.createWaterMark(
						waterMarkWord, null, null);
				BufferedImage bufferedImageTemp = Thumbnails.of(inputStream)
						.watermark(position, waterMark, 0.3f).scale(1f)
						.asBufferedImage();
				ByteArrayOutputStream writeToBuf = writeToBuf(
						bufferedImageTemp,
						PictureUtils.getExtensionName(local_filename));
				if (writeToBuf != null) {
					String upload_file1 = FileManager.getStorageClient1()
							.upload_file1(
									writeToBuf.toByteArray(),
									PictureUtils
											.getExtensionName(local_filename),
									null);
					return getRealPath(upload_file1);
				} else {
					return null;
				}

			} else {
				return null;
			}

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (MyException e) {
			e.printStackTrace();
			return null;
		}

	}

		private static String getRealPath(String upload_file1) {
			return PROTOCOL + TRACKER_NGNIX_HOST + PORT_SEPARATOR + TRACKER_NGNIX_PORT +SEPARATOR + upload_file1;
		}
		
		private static ByteArrayOutputStream writeToBuf(BufferedImage bufferedImageTemp , String stuffName) {
			try {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				boolean isWrited = ImageIO.write(bufferedImageTemp, stuffName, out);
				if(isWrited){
					return out;
				}else {
					return null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
    
    

	public static TrackerServer getTrackerServer() {
		return trackerServer;
	}



	public static StorageClient1 getStorageClient1() {
		return storageClient1;
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
    
    public static void main(String[] args) throws IOException {
    	//String uploadWithWaterMark = uploadWithWaterMarkPath("C:/Users/jacky/Downloads/wKgBvFcNPt-AcQggAABMaijauzw113.JPG", "D:/sofeware/pic/camelot.png", Positions.BOTTOM_CENTER);
    	FileInputStream fileInputStream = new FileInputStream("C:/Users/jacky/Downloads/wKgBvFcNPt-AcQggAABMaijauzw113.JPG");
    	
    	String uploadWithWaterMark = uploadWithWaterMarkWord(fileInputStream, "C:/Users/jacky/Downloads/wKgBvFcNPt-AcQggAABMaijauzw113.JPG","北京柯莱特集团", Positions.BOTTOM_CENTER);
    	//String uploadBySize = uploadBySize("D:/sofeware/pic/DSC_0655.JPG", 400, 400);
    	System.out.println(uploadWithWaterMark);
    	
    	/*Thumbnails.of(new File("C:/Users/jacky/Downloads/wKgBvFcNPt-AcQggAABMaijauzw113.JPG"))
        .size(480, 480)
        .watermark(Positions.CENTER,
                   ImageIO.read(new File("D:/sofeware/pic/camelot.png")), 0.5f)
        .toFile(new File("E:/1a_water_1.jpg"));*/
	}
}
