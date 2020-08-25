package com.camelot.example.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.example.domain.Demo;

@Controller
@RequestMapping("/classname")
// 一定要带上类名
public class ExampleContorller {

	/**
	 * 参数少可以使用rest风格 第三个user为对象传参 比如有id name两个字段，页面直接传id=1&name=a就行
	 * 
	 * @return
	 */
	@RequestMapping("/methodname/{id}/{name}")
	public String test(@PathVariable String id, Model model, Demo demo) {
		// 说明
		// 需要用到request，response单独传入参数，不是必传

		model.addAttribute("key", "value");// 往前台传参数
		return "目录/vm文件名";
	}

	@RequestMapping("/methodname/{id}")
	public @ResponseBody
	String testAjax() {
		return "根据@ResponseBody 后面是对象就返回对象，是字符串就返回字符串";
	}

	@RequestMapping("/methodname/{参数}")
	public String redirect() {
		return "redirect:/classname/methodname/{参数}";
	}

	@RequestMapping("/loginAndCart")
	public String loginAndCart(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		return "cart/loginAndCart";
	}

	@RequestMapping("/doLoginAndCart")
	public @ResponseBody
	String doLoginAndCart(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ckey", "null".equals(request.getParameter("ckey")) ? ""
				: request.getParameter("ckey"));
		map.put("uid", "null".equals(request.getParameter("uid")) ? ""
				: request.getParameter("uid"));
		map.put("counterSKU", request.getParameter("counterSKU"));
		map.put("quantity", request.getParameter("quantity"));
		map.put("counterCode", request.getParameter("countercode"));
		String send = "";
		try {
			System.out.println("请求map----" + map);
			/*
			 * send = HttpClient.doPost(SysProperties.getProperty("serviceUrl")
			 * + "shopCar/addToCar", JSON.toJSONString(map));
			 */
			System.out.println("请求返回----" + send);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return send;
	}

}
