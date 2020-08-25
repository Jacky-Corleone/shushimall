package com.camelot.openplatform.common.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <p>Description: [HTTP协议上传文件信息]</p>
 * Created on 2016年3月22日
 * 
 * @author <a href="mailto: liuxiangping86@camelotchina.com">刘香平</a>
 * @version 1.0 Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
public class FormFile {
	
	/** 上传文件的数据 */
	private byte[] data;
	
	/** 上传文件的流 */
	private InputStream inStream;
	
	/** 上传的文件 */
	private File file;
	
	/** 文件名称 */
	private String filname;
	
	/** 请求参数名称 */
	private String parameterName;
	
	/** 内容类型 */
	private String contentType = "application/octet-stream";

	/**
	 * <p>Discription:[此函数用来传输小文件]</p>
	 * @coustructor 方法.
	 */
	public FormFile(String filname, byte[] data, String parameterName,
			String contentType) {
		
		this.data = data;
		this.filname = filname;
		this.parameterName = parameterName;
		if (contentType != null)
			this.contentType = contentType;
	}

	/**
	 * <p>Discription:[此函数用来传输大文件]</p>
	 * @coustructor 方法.
	 */
	public FormFile(String filname, File file, String parameterName,
			String contentType) {
		
		this.filname = filname;
		this.parameterName = parameterName;
		this.file = file;
		try {
			this.inStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if (contentType != null)
			this.contentType = contentType;
	}

	public File getFile() {
		return file;
	}

	public InputStream getInStream() {
		return inStream;
	}

	public byte[] getData() {
		return data;
	}

	public String getFilname() {
		return filname;
	}

	public void setFilname(String filname) {
		this.filname = filname;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}