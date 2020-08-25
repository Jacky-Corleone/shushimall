package com.camelot.mall.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class VCode {
	private int w = 70;  //宽
	private int h = 30;  //高
	private Color bgColor = new Color(240, 240, 240);  //背景颜色
	
	private StringBuilder code = new StringBuilder();  //记录验证码，用于比较
	
	private Random random= new Random();
	
	private BufferedImage createImage() {
		/*
		 * 1. 创建图片
		 * 2. 设置背景色
		 */
		//创建图片
		BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		//设置画笔颜色
		img.getGraphics().setColor(bgColor);
		//填充一个与图片一样大小的矩形！即设置背景色！
		img.getGraphics().fillRect(0, 0, w, h);
		return img;
	}
	
	private Color randomColor(){
		int r = random.nextInt(256);
		int g = random.nextInt(256);
		int b = random.nextInt(256);
		return new Color(r, g, b);	
	}
	
	/*
	 * 字体号、样式、字号
	 */
	private String[] fontNames = {"宋体", "华文楷体", "黑体", "华文新魏", "华文隶书", "微软雅黑", "楷体_GB2312"};
	private int[] fontSizes = {24, 25, 26, 27, 28};
	private Font randomFont(){
		int index = random.nextInt(fontNames.length);
		String name = fontNames[index];
		
		int style = random.nextInt(4);

		index = random.nextInt(fontSizes.length);
		int size = fontSizes[index];
		return new Font(name, style, size);
	}
	
	//private String codes = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	private String codes = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789";//不包括i,o,l,1,0
	//随机生成字符
	private String randomChar(){
		//int index = random.nextInt(codes.length());
		//return codes.charAt(index) + "";
		int index = random.nextInt(10);
		return index + "";
	}
	
	//生成干扰线
	private void drawLine(BufferedImage img){
		Graphics g = img.getGraphics();
		//Graphics2D g2d = (Graphics2D) img.getGraphics();
		g.setColor(Color.BLACK);
		//g2d.setStroke(new BasicStroke(1.5F));  //调整画笔粗细
		for (int i = 0; i < 5; i++) {
			int x1 = random.nextInt(w);
			int y1 = random.nextInt(h);
			int x2 = random.nextInt(w);
			int y2 = random.nextInt(h);
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	//用户调用这个方法获取图片
	public BufferedImage getImage() {
		/*
		 * 写什么字符：随机生成，范围：0-9、A-Z、a-z
		 * 字体：随机
		 * 字符颜色：随机
		 */
		BufferedImage img = createImage();
		Graphics g = img.getGraphics();
		//画东西	
		for (int i = 0; i < 4; i++) {
			String ch = this.randomChar();  //获取随机字符
			code.append(ch);  //记录生成的字符
			g.setColor(this.randomColor());  //获取随机颜色
			g.setFont(this.randomFont());  //获取随机字体
			g.drawString(ch, w / 4 * i, h - 5);
		}
//		this.drawLine(img);  //添加干扰线
		return img;
	}
	
	//这个方法必须在getImage()方法后调用
	public String getCode(){
		return code.toString();
	}
	
	//保存图片
	public static void saveImage(BufferedImage img, OutputStream out) throws IOException {
		ImageIO.write(img, "JPEG", out);
	}
}

