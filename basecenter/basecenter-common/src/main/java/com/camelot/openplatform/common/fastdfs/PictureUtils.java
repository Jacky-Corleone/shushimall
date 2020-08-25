package com.camelot.openplatform.common.fastdfs;

import java.util.ArrayList;
import java.util.List;

public class PictureUtils {
	
	public static List<String> PIC_TYPE = null;
	
	static{
		PIC_TYPE = new ArrayList<String>();
		PIC_TYPE.add("jpg");
		PIC_TYPE.add("gif");
		PIC_TYPE.add("png");
		PIC_TYPE.add("bmp");
	}
	//不带扩展名
	public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }  
	//不带 .
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }   
	
	public static boolean isPicType(String filename) {  
		
		return PIC_TYPE.contains(filename.toLowerCase());
    }   
	
	
	public static void main(String[] args) {
		System.out.println(PictureUtils.getExtensionName("/fdsfefeqferergqre/fdsfdgfdg.jpg"));
		System.out.println(PictureUtils.getFileNameNoEx("/fdsfefeqferergqre/fdsfdgfdg.jpg"));
		System.out.println(PictureUtils.isPicType("/fdsfefeqferergqre/fdsfdgfdg.jpg"));
	}
}
