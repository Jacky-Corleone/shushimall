package com.camelot.fastdfs;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
	
	//不带 .
	public static String getExtensionName(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length() - 1))) {   
                return filename.substring(dot + 1);   
            }   
        }   
        return filename;   
    }  
	//不带扩展名
	public static String getFileNameNoEx(String filename) {   
        if ((filename != null) && (filename.length() > 0)) {   
            int dot = filename.lastIndexOf('.');   
            if ((dot >-1) && (dot < (filename.length()))) {   
                return filename.substring(0, dot);   
            }   
        }   
        return filename;   
    }   
	
	//尺寸参数
	public static String getSizeArgs(String filename) {   
		 if ((filename != null) && (filename.length() > 0)) {   
	            int dot = filename.lastIndexOf('_');
	            if(dot == -1){
	            	return null;
	            }
	            if ((dot >-1) && (dot < (filename.length() - 1))) {   
	                return filename.substring(dot + 1);   
	            }   
	        }   
	        return filename;      
    } 
	
	 //不带扩展名 _
	 public static String getFileNameNoArgs(String filename) {   
	        if ((filename != null) && (filename.length() > 0)) {   
	            int dot = filename.lastIndexOf('_');   
	            if ((dot >-1) && (dot < (filename.length()))) {   
	                return filename.substring(0, dot);   
	            }   
	        }   
	        return filename;   
	    }   

	public static boolean isPicType(String fileName) {  
		
		return PIC_TYPE.contains(PictureUtils.getExtensionName(fileName).toLowerCase());
    }   
	
	
	//根据文字自定义出一张水印图片
	
	public static BufferedImage  createWaterMark(String waterMarkName, Integer width , Integer height) {
		if(waterMarkName == null){
			return null;
		}else {
			BufferedImage bi = new BufferedImage(width==null?64:width, height==null?64:height, BufferedImage.TYPE_INT_RGB);
		    Graphics2D g = bi.createGraphics();
		    g.setColor(Color.LIGHT_GRAY);
		    g.drawRect(0, 0, width==null?64:width, height==null?64:height);
		    char[] data = waterMarkName.toCharArray();
		    g.drawChars(data, 0, data.length, 5, 32);
		    return bi;
		}
		  
		
    }   
	
	public static void main(String[] args) {
		System.out.println(PictureUtils.getExtensionName("/fdsfefeqferergqre/fdsfdgfdg.jpg"));
		System.out.println(PictureUtils.getFileNameNoEx("/fdsfefeqferergqre/fdsfdgfdg.jpg"));
		System.out.println(PictureUtils.isPicType("/fdsfefeqferergqre/fdsfdgfdg.jpg"));
		
		System.out.println(PictureUtils.getSizeArgs("/group2/M00/00/00/wKgBvFcGi3CATUkwAErASNF-ON0066_80x80.JPG"));
	}
}
