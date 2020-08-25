package com.camelot.ecm.ueditor.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.camelot.ecm.ueditor.define.AppInfo;
import com.camelot.ecm.ueditor.define.BaseState;
import com.camelot.ecm.ueditor.define.State;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.openplatform.util.SysProperties;

public class BinaryUploader {

	/**
     * 使用ftp协议上传图片
     * @param uploadDir
     * @param is
     * @param suffix
     * @return
     */
    private static String uploadFileWithFtp(String uploadDir, InputStream is, String suffix) {
    	String FILE_SERVER_IP = SysProperties.getProperty("ftp.ip");
    	String LOGIN_NAME = SysProperties.getProperty("ftp.username");
    	String LOGIN_PASSWORD = SysProperties.getProperty("ftp.password");
        FTPUtils ftpUtils = new FTPUtils(FILE_SERVER_IP, LOGIN_NAME, LOGIN_PASSWORD);
        return ftpUtils.upload(uploadDir, is, suffix);
    }
	
	public static final State save(HttpServletRequest request,
			Map<String, Object> conf) {
		MultipartFile file = null;
		try {
			MultiValueMap<String, MultipartFile> multiValueMap = ((DefaultMultipartHttpServletRequest)request).getMultiFileMap();
			List<MultipartFile> list = multiValueMap.get("file");
			for(MultipartFile multipartFile:list){
				file = multipartFile;
			}
			if (file == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			String savePath = SysProperties.getProperty("ftp.packagef");
			String fileName = file.getOriginalFilename();
            String pix = fileName.substring( fileName.lastIndexOf("."), fileName.length());
			String returnUrl = uploadFileWithFtp(savePath,file.getInputStream(), pix);
			if(null==returnUrl){
				return new BaseState(false, AppInfo.IO_ERROR);
			}
			State storageState = new BaseState(true);
			storageState.putInfo("url", returnUrl);
			storageState.putInfo("type", pix);
			storageState.putInfo("original", fileName);

			return storageState;
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
