package com.camelot.mall.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * 图片处理工具类，水印、缩放
 * @author 卓家进
 * @version v1.0
 * create on 2016年1月25日
 */
public class ImageUtil {
 
	/**
	* @Description: 为图片添加水印
	* @param iconPath 水印图片路径
	* @param is     源图片文件取得的输入流
	* @return  返回处理后的输入流
	* @author 卓家进
	* @version 1.0
	* Create on 2016年1月25日
	* Copyright (c) 2015 大家智合网络科技有限公司
	*/
    public static InputStream markImageByIcon(String iconPath, InputStream is) {   
    	ByteArrayOutputStream os = null;   
        try {   
            Image srcImg = ImageIO.read(is);   
  
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null),   
                    srcImg.getHeight(null), BufferedImage.TYPE_INT_RGB);  
  
            // 得到画笔对象   
            Graphics2D g = buffImg.createGraphics();   
  
            // 设置对线段的锯齿状边缘处理   
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,   
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);   
  
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg   
                    .getHeight(null), Image.SCALE_SMOOTH), 0, 0, null);   
    
            // 水印图象的路径 水印一般为gif或者png的，这样可设置透明度   
            ImageIcon imgIcon = new ImageIcon(iconPath);   
  
            // 得到Image对象。   
            Image img = imgIcon.getImage();   
  
            float alpha = 0.5f; // 透明度   
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,   
                    alpha));   
  
            // 表示水印图片的位置   
            g.drawImage(img,srcImg.getWidth(null) - img.getWidth(null),
            		srcImg.getHeight(null) - img.getHeight(null), null);   
  
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));   
  
            g.dispose();   
            
            os = new ByteArrayOutputStream();  
            // 将加水印后的图片输出   
            ImageIO.write(buffImg, "JPG", os);
            //返回输入流
            return new ByteArrayInputStream(os.toByteArray());                         
        } catch (Exception e) {       	
            e.printStackTrace(); 
            return null;
        } finally {   
        	//释放占有的ＩＯ资源
            try {   
                if (null != os)   
                    os.close();   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }   
    }   

    /**
     * @Description: 图片缩放 
     * @param is 图片文件读取的输入流
     * @param normalLen 图片尺寸标准宽度/长度
     * @return 缩放后的输入流
     * @throws IOException 
     * @author 卓家进
     * @version 1.0
     * Create on 2016年1月25日
     * Copyright (c) 2015 大家智合网络科技有限公司
     */
    public static InputStream resize(InputStream is, Integer normalLen) throws IOException {  
    	ByteArrayOutputStream os = null;
        try {   
	        Image iOriginal = ImageIO.read(is);    
	        Image resizedImage = null;  
	  
	        int iWidth = iOriginal.getWidth(null);  
	        int iHeight = iOriginal.getHeight(null);  
	  
	        if (iWidth > iHeight) {  
	        	if(iWidth >= normalLen){
	        		resizedImage = iOriginal.getScaledInstance(normalLen, (normalLen *iHeight)  
	                        /iWidth , Image.SCALE_SMOOTH);  
	        	}       
	        } else { 
	        	if(iHeight >= normalLen){
	                resizedImage = iOriginal.getScaledInstance((normalLen *  iWidth)/iHeight ,  
	                		normalLen, Image.SCALE_SMOOTH);  
	        	}
	        }  
	        Image temp = null;
	        // 保证加载所有像素	
	        if(null == resizedImage){
	        	temp = iOriginal;
	        }else{
	        	temp = new ImageIcon(resizedImage).getImage();  
	        }	        
	  
	        // 创建buffered image.  
	        BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null),  
	                temp.getHeight(null), BufferedImage.TYPE_INT_RGB);  
	  
	        // 复制image到bufferedimage中.  
	        Graphics g = bufferedImage.createGraphics();  
	  
	        // 清除背景色.  
	        g.setColor(Color.white);  
	        g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));  
	        g.drawImage(temp, 0, 0, null);  
	        g.dispose();  
	  
	        // Soften.  
	        float softenFactor = 0.05f;  
	        float[] softenArray = { 0, softenFactor, 0, softenFactor,  
	                1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0 };  
	        Kernel kernel = new Kernel(3, 3, softenArray);  
	        ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);  
	        bufferedImage = cOp.filter(bufferedImage, null);  
	  
	        os = new ByteArrayOutputStream(); 
	        ImageIO.write(bufferedImage, "JPG", os);
	        return new ByteArrayInputStream(os.toByteArray()); 
        }catch( Exception e){
        	e.printStackTrace(); 
            return null;
        }finally{
        	//释放占有的ＩＯ资源
            try {   
                if (null != os)   
                    os.close();   
            } catch (Exception e) {   
                e.printStackTrace();   
            }   
        }
    }   
}
