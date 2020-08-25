package com.camelot.example.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.example.service.DemoExportService;

/**
 * 
 * <p>Description: [描述该类概要功能介绍：测试服务]</p>
 * Created on 2016年1月28日
 * @author  <a href="mailto: zihanmin@camelotchina.com">訾瀚民</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */


@Controller
@RequestMapping("/demoController")
public class DemoController {
	private final static Logger logger = LoggerFactory
			.getLogger(DemoController.class);

	@Resource
	private DemoExportService demoExportService;
	

	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：测试dubbo 服务]</p>
	 * Created on 2016年1月28日
	 * @param model
	 * @return
	 * @author:[訾瀚民]
	 */
	
	@RequestMapping(value = "/getUserByIdTestNo")
	public String getUserByIdTestNo(Model model) {

		String username = demoExportService.getUserByIdTestNo(11111111).getName();

		logger.info("用户名:{}", username);

		model.addAttribute("username", username);// 往前台传参数
		return "/demo/user";
	}


	/**
	 * 
	 * <p>Discription:[方法功能中文描述：测试redis]</p>
	 * Created on 2016年1月28日
	 * @param model
	 * @return
	 * @author:[訾瀚民]
	 */
	@RequestMapping(value = "/getUserByIdTestRedis")
	public String getUserByIdTestRedis(Model model) {

		String username = demoExportService.getUserByIdTestRedis(11111111).getName();

		logger.info("用户名:{}", username);

		model.addAttribute("username", username);// 往前台传参数
		return "/demo/user";
	}


	/**
	 * 
	 * <p>Discription:[方法功能中文描述：测试mybatis]</p>
	 * Created on 2016年1月28日
	 * @param model
	 * @return
	 * @author:[訾瀚民]
	 */
	
	@RequestMapping(value = "/getUserByIdTestMybatis")
	public String getUserByIdTestMybatis(Model model) {

		// 获取数据id为1 的用户
		String username = demoExportService.getUserByIdTestMybatis(1).getName();

		logger.info("用户名:{}", username);

		model.addAttribute("username", username);// 往前台传参数
		return "/demo/user";
	}
}
