package com.camelot.openplatform.common;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * 文件工具类
 * @author Administrator
 */
public class FileUtil {
	private Image img;
    private int width;
	private int height;
	private long size;
	private String fileName;

	public FileUtil(){}
	
    /**
     * 构造函数
     */
    public FileUtil(String fileName) throws IOException {
        File file = new File(fileName);// 读入文件
        this.img = ImageIO.read(file);      // 构造Image对象
        this.width = img.getWidth(null);    // 得到源图宽
        this.height = img.getHeight(null);  // 得到源图长
        this.size = file.length();
        this.fileName = fileName;
    }
    
    public FileUtil(File file) throws IOException {
        this.img = ImageIO.read(file);      // 构造Image对象
        this.width = img.getWidth(null);    // 得到源图宽
        this.height = img.getHeight(null);  // 得到源图长
        this.size = file.length();
        this.fileName = file.getName();
    }
    
    /**
	 * 获取文件宽度
     * @return
     */
    public int getWidth() {
		return width;
	}

	/**
	 * 获取文件高度
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	public long getSize() {
		return size;
	}
	
	/**
     * 获取文件后缀名
     * @param filer
     * @return
     */
	public String getSuffix() {
		String suffix = this.fileName.substring(this.fileName.lastIndexOf("."), this.fileName.length());
		return suffix;
	}
	
	/**
	 * 验证上传文件类型是否属于图片格式
	 * @return 验证结果成功或失败：true/false
	 */
    public static final boolean isImage(File file){  
        boolean flag = false;  
        try  
        {  
            BufferedImage bufreader = ImageIO.read(file);  
            int width = bufreader.getWidth();  
            int height = bufreader.getHeight();  
            if(width==0 || height==0){  
                flag = false;  
            }else {  
                flag = true;  
            }  
        }  
        catch (IOException e)  
        {  
            flag = false;  
        }catch (Exception e) {  
            flag = false;  
        }  
        return flag;  
    }
    
    /**
     * 判断图片后缀名是否在指定的后缀名数组中
     * @param file
     * @param exts 后缀名
     * @return
     */
    public static final boolean rightSuffix(File file, String[] exts){
    	boolean flag = false;
    	if(null == file){
    		return flag;
    	}
    	String fileName = file.getName();
    	String suffix = fileName.substring(fileName.lastIndexOf(".")+1, fileName.length());
    	List<String> extList = Arrays.asList(exts);
    	
    	return extList.contains(suffix.toLowerCase());
    }
}
