package com.camelot.example.controller;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.example.service.DemoExportService;

@Controller
@RequestMapping("/demoController")
public class DemoController {
	private final static Logger logger = LoggerFactory
			.getLogger(DemoController.class);

	@Resource
	private DemoExportService demoExportService;
	
	/**
	 * 测试dubbo 服务
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getUserByIdTestNo")
	public String getUserByIdTestNo(Model model) {

		String username = demoExportService.getUserByIdTestNo(11111111).getName();

		logger.info("用户名:{}", username);

		model.addAttribute("username", username);// 往前台传参数
		return "/demo/user";
	}

	/**
	 * 测试redis
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getUserByIdTestRedis")
	public String getUserByIdTestRedis(Model model) {

		String username = demoExportService.getUserByIdTestRedis(11111111).getName();

		logger.info("用户名:{}", username);

		model.addAttribute("username", username);// 往前台传参数
		return "/demo/user";
	}

	/**
	 * 测试mybatis
	 * 
	 * @param model
	 * @return
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
