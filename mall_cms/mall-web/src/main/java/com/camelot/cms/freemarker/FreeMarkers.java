/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.camelot.cms.freemarker;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.camelot.util.Exceptions;
import com.camelot.util.SpringContextHolder;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * FreeMarkers工具类
 * @author ThinkGem
 * @version 2013-01-15
 */
public class FreeMarkers {
	/**
	 * 加载过配置文件FreeMarkerConfigurer
	 */
	private final static FreeMarkerConfigurer freemarkerConfigurer = SpringContextHolder.getBean(FreeMarkerConfigurer.class);

	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			StringWriter result = new StringWriter();
			Template t = new Template("name", new StringReader(templateString), new Configuration());
			t.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}
	/**lj
	 * 自动自订设定全局变量带解析自定义标签
	 * @param template
	 * @param model
	 * @return
	 */
	public static String renderTagTemplate(String templatePath,String templateName, Object model) {
		try {
			Configuration cfg = freemarkerConfigurer.getConfiguration();
			cfg.setDirectoryForTemplateLoading(new File(templatePath));
			Template template = cfg.getTemplate(templateName);
			StringWriter result = new StringWriter();
			template.process(model, result);
			return result.toString();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**lj
	 * frammak 发布生成静态文件
	 * @param model 数据
	 * @param staticPath 输出的静态地址
	 * @param templatePath 模板位置
	 * @param templateName 模板名称
	 */
	public static void renderStaticTemplate(Map<String, Object> model,String staticPath,String templatePath,String templateName) {
		try {
			File f = new File(staticPath);
			File parent = f.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			Writer out = null;
			try {
				// FileWriter不能指定编码确实是个问题，只能用这个代替了。
				out = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
				Configuration cfg = freemarkerConfigurer.getConfiguration();
				cfg.setDirectoryForTemplateLoading(new File(templatePath));
				Template staticTemplate = cfg.getTemplate(templateName);
				staticTemplate.process(model, out);
			} finally {
				if (out != null) {
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Configuration buildConfiguration(String directory) throws IOException {
		Configuration cfg = new Configuration();
		Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(path.getFile());
		return cfg;
	}
	
	public static void main(String[] args) throws IOException {
//		// renderString
//		Map<String, String> model = com.google.common.collect.Maps.newHashMap();
//		model.put("userName", "calvin");
//		String result = FreeMarkers.renderString("hello ${userName}", model);
//		System.out.println(result);
//		// renderTemplate
//		Configuration cfg = FreeMarkers.buildConfiguration("classpath:/");
//		Template template = cfg.getTemplate("testTemplate.ftl");
//		String result2 = FreeMarkers.renderTemplate(template, model);
//		System.out.println(result2);
		
//		Map<String, String> model = com.google.common.collect.Maps.newHashMap();
//		model.put("userName", "calvin");
//		String result = FreeMarkers.renderString("hello ${userName} ${r'${userName}'}", model);
//		System.out.println(result);
	}
	
}
