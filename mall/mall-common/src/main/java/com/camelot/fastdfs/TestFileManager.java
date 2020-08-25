package com.camelot.fastdfs;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;
import org.junit.Assert;
import org.junit.Test;

public class TestFileManager {
	
	 	@Test  
	    public void upload() throws Exception {  
	        File content = new File("D:/sofeware/pic/DSC_0680.JPG");  
	          
	        FileInputStream fis = new FileInputStream(content);  
	        byte[] file_buff = null;  
	        if (fis != null) {  
	            int len = fis.available();  
	            file_buff = new byte[len];  
	            fis.read(file_buff);  
	        }  
	          
	        //FastDFSFile file = new FastDFSFile("520", file_buff, "jpg");  
	        try {
	        	String upload_file1 = FileManager.getStorageClient1().upload_file1(file_buff, "jpg", null);
	        	System.out.println(upload_file1);  
	        	fis.close();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				FileManager.getTrackerServer().close();
			}
	        
	       // String fileAbsolutePath = FileManager.upload(file);  
	        
	          
	    }  
	      
	    @Test  
	    public void getFile() throws Exception {  
	        FileInfo file = FileManager.getFile("group1", "M00/00/00/wKgBu1cGP2mADUUYAErASNF-ON0393.jpg");  
	        Assert.assertNotNull(file);  
	        String sourceIpAddr = file.getSourceIpAddr();  
	        long size = file.getFileSize();  
	        System.out.println(file.getFileSize());
	        System.out.println(file.getCreateTimestamp());
	        System.out.println(file.getCrc32());
	        System.out.println("ip:" + sourceIpAddr + ",size:" + size);  
	    }  
	      
	    @Test  
	    public void getStorageServer() throws Exception {  
	        StorageServer[] ss = FileManager.getStoreStorages("group1");  
	        Assert.assertNotNull(ss);  
	          
	        for (int k = 0; k < ss.length; k++){  
	            System.err.println(k + 1 + ". " + ss[k].getInetSocketAddress().getAddress().getHostAddress() + ":" + ss[k].getInetSocketAddress().getPort());  
	        }  
	    }  
	      
	    @Test  
	    public void getFetchStorages() throws Exception {  
	        ServerInfo[] servers = FileManager.getFetchStorages("group1", "M00/00/00/wKgBu1cGP2mADUUYAErASNF-ON0393.jpg");  
	        Assert.assertNotNull(servers);  
	          
	        for (int k = 0; k < servers.length; k++) {  
	            System.err.println(k + 1 + ". " + servers[k].getIpAddr() + ":" + servers[k].getPort());  
	        }  
	    }  
	    
	    @Test 
	    public void testDownload() {
	        try {
	        	byte[] download_file1 = FileManager.getStorageClient1().download_file1("group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.jpg");
	        	//byte[] b = FileManager.downloadFile("group1", "M00/00/00/wKgBu1cGP2mADUUYAErASNF-ON0393.jpg");
	            //storageClient.download_file("group1", "M00/00/00/wKgBu1cGP2mADUUYAErASNF-ON0393.jpg"); 
	        	System.out.println(download_file1.length);
	        	BufferedImage asBufferedImage = Thumbnails.of(new ByteArrayInputStream(download_file1)).size(200, 200).asBufferedImage();
	        	 ByteArrayOutputStream out = new ByteArrayOutputStream(); 
	        	boolean write = ImageIO.write(asBufferedImage, "JPG", out);
	        	if(write){
	        		byte[] byteArray = out.toByteArray();
	        		//FastDFSFile file = new FastDFSFile("200*200", byteArray, "jpg");  
	        		String upload_file = FileManager.getStorageClient1().upload_file1("group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066.jpg", "_200_200", byteArray, "JPG", null);
	    	        //String fileAbsolutePath = FileManager.upload(file); 
	        		System.out.println(upload_file);
	    	        
	        	}
	        	
	        	
	           
	        } catch (Exception e) { 
	            e.printStackTrace(); 
	        }finally{
	        	try {
					FileManager.getTrackerServer().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } 
	    }
	     
	    public static void main(String[] args) throws Exception{
	    	
	    	File content = new File("D:/sofeware/pic/DSC_0620.JPG");  
	          
	        FileInputStream fis = new FileInputStream(content);  
	        byte[] file_buff = null;  
	        if (fis != null) {  
	            int len = fis.available();  
	            file_buff = new byte[len];  
	            fis.read(file_buff);  
	        }  
	          
	        //FastDFSFile file = new FastDFSFile("520", file_buff, "jpg");  
	        try {
	        	String upload_file1 = FileManager.getStorageClient1().upload_file1(file_buff, "jpg", null);
	        	System.out.println(upload_file1);  
	        	fis.close();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				FileManager.getTrackerServer().close();
			}
			
		}
}
