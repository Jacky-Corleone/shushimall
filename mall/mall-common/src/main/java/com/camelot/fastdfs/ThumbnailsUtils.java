package com.camelot.fastdfs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

public class ThumbnailsUtils {
	
	
	/** 
     * 图片缩略图切割 --- 根据文件地址 生成图片缩略图 
     * @param fileOutPath 图片缩略图地址 
     * @param outWidth  图片缩略图宽度 
     * @param outHeight 图片缩略图高度 
     * @param scale 缩放比例 
     * @param rotate 旋转度数 
     * @return Builder<File>
     */  
   private static Builder<File> fileToBilder(String fileFromPath, Integer outWidth, Integer outHeight, Float scale, Integer rotate) {  
           Builder<File> builder = (Builder<File>) Thumbnails.of(fileFromPath);  
           if (null != outWidth && null == outHeight) {  
               builder.width(outWidth);  
           }  
           if (null == outWidth && null != outHeight) {  
               builder.height(outHeight);  
           }  
           if (null != outWidth && null != outHeight) {  
               builder.size(outWidth, outHeight);  
           }  
           if (null != scale) {  
               builder.scale(scale);  
           }  
           if (null != rotate) {  
               builder.rotate(rotate);  
           }  
           return builder;
    }  
   	
   /** 
    * 图片缩略图切割 --- 根据文件地址 生成图片缩略图 
    * @param fileOutPath 图片缩略图地址 
    * @param outWidth  图片缩略图宽度 
    * @param outHeight 图片缩略图高度 
    * @param scale 缩放比例 
    * @param rotate 旋转度数 
    * @return 
    */  
  public static BufferedImage getBufferedImage(String fileFromPath,String fileOutPath, Integer outWidth, Integer outHeight, Float scale, Integer rotate) {  
	      Builder<File> builder = fileToBilder(fileFromPath, outWidth, outHeight, scale, rotate);
          try {
			BufferedImage bufferedImage = builder.asBufferedImage();
			return bufferedImage;
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return null;  
   }  
  
  
  /** 
   * 图片缩略图切割 --- 根据文件地址 生成图片缩略图 
   * @param fileFromPath 图片源文件地址 
   * @param fileOutPath 图片缩略图地址 
   * @param outWidth  图片缩略图宽度 
   * @param outHeight 图片缩略图高度 
   * @param scale 缩放比例 
   * @param rotate 旋转度数 
   * @return 
   */  
	 public static void writeToLocal(String fileFromPath,String fileOutPath, Integer outWidth, Integer outHeight, Float scale, Integer rotate) {  
		      Builder<File> builder = fileToBilder(fileFromPath, outWidth, outHeight, scale, rotate);
	         try {
				builder.toFile(fileOutPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
	  }  
   
   
}
