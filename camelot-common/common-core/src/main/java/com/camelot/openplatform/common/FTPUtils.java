package com.camelot.openplatform.common;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.UUID;

/**
 * 
 * <p>Description: [FTP文件上传帮助类]</p>
 * Created on 2015年3月2日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class FTPUtils {
    private Logger LOG = LoggerFactory.getLogger(FTPUtils.class);

	private FTPClient ftpClient = null;
	private String url;
	private int port = 21;
	private String username;
	private String password;

	public static void main(String[] args) throws Exception {
		File file = new File("F:\\qingdaoimages\\IMG_3490.JPG");
		FileInputStream fis = new FileInputStream(file);
		FTPUtils fu = new FTPUtils("211.151.14.251", "printhome_ftp", "test");
		String retVal = fu.upload("/album", fis, ".jpeg");
		System.out.println(retVal);
		//   /2015/4/9/78183491-a9d0-455e-8ada-d14923d3d2f7.jpeg
	}

	/**
	 * 
	 * <p>Discription:[构造器方法描述]</p>
	 * @coustructor 方法.
	 */
	public FTPUtils(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 
	 * <p>Discription:[构造器方法描述]</p>
	 * @coustructor 方法.
	 */
	public FTPUtils(String url, int port, String username, String password) {
		this.url = url;
		this.port = port;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年3月2日
	 * @param path FTP服务器保存目录,如果是根目录则为"/"；如果目录不存在，会自动创建
	 * @param is 上传文件文件流
	 * @param contentType 上传文件文件类型
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	public String upload(String path, InputStream is, String contentType) {
        LOG.info("FTP上传文件开始...");
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		//图片存储分年月日文件夹存储
		String dir = "/" + year + "/" + month + "/" + day + "/";
		path += dir;
		String remoteName = UUID.randomUUID().toString() + contentType;
        boolean isFinish = false;
		try {

			this.ftpClient = new FTPClient();
            LOG.info("开始连接FTP服务器："+this.url+":"+this.port);
			this.ftpClient.connect(this.url, this.port);
            LOG.info("FTP服务器连接成功，开始登录FTP服务器...");
			this.ftpClient.login(this.username, this.password);
            LOG.info("FTP服务器登录成功，开始更改操作的目录...");
			// 转移工作目录至指定目录下 
			this.changeMakeWorkingDir(path);
			this.ftpClient.setBufferSize(1024);
			this.ftpClient.setControlEncoding("GBK");
            ftpClient.enterLocalPassiveMode();
			//设置文件类型（二进制）
			this.ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            LOG.info("开始往FTP服务器上传文件...");
            isFinish = this.ftpClient.storeFile(new String(remoteName.getBytes("GBK"),"iso-8859-1"), is);
            if (isFinish){
                LOG.info("FTP服务器文件上传成功");
            }else {
//                while (!isFinish){
//                    isFinish = this.ftpClient.storeFile(new String(remoteName.getBytes("UTF-8"),"iso-8859-1"), is);
//                }
                LOG.info("FTP服务器文件上传失败");
                return null;
            }
		} catch (Exception e) {
            LOG.info("往FTP服务器上传文件失败："+e);
			return null;
		} finally {
			try {
                if (isFinish){
                    is.close();
                }
			} catch (IOException e1) {
				LOG.error("关闭文件流出错",e1);
			}
			if (this.ftpClient.isConnected()) {
				try {
					this.ftpClient.disconnect();
				} catch (IOException e) {
                    LOG.error("关闭FTP客户端出错");
				}
			}
		}
		return dir + remoteName;
	}

	/**
	 * 
	 * <p>Discription:[切换目录，如果目录不存在会自动创建]</p>
	 * Created on 2015年3月2日
	 * @param path 要切换的工作区路径
	 * @throws IOException
	 * @author:[Goma 郭茂茂]
	 */
	private void changeMakeWorkingDir(String path) throws IOException {
		String[] dirs = path.split("/");
		for (String dir : dirs) {
			dir = new String(dir.getBytes("GBK"), "iso-8859-1");
			if (dir != null && !"".equals(dir)) {
				this.ftpClient.makeDirectory(dir);
			}
			this.ftpClient.changeWorkingDirectory(dir);
		}
	}

	public String getUrl() {
		return url;
	}

	public int getPort() {
		return port;
	}
	
	
}
