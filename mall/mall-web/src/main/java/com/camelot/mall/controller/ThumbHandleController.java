package com.camelot.mall.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.fastdfs.PicReqInterceptor;



@Controller
@RequestMapping("/thumbHandle")
public class ThumbHandleController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@RequestMapping("/getThumb")
	@ResponseBody
	public JSON getThumb(HttpServletRequest request, String thumbUrlString, HttpServletResponse response){
		logger.info("--->> start handle the thumb request");
		//thumbUrlString = PicReqInterceptor.handlePicReq(thumbUrlString);
		thumbUrlString = PicReqInterceptor.handlePicReq2(thumbUrlString);
		JSONObject result = new JSONObject();
		result.put("thumbUrl", thumbUrlString);
		return result;
		
	}
	
	@RequestMapping("/testDemo")
	public String testDemo(HttpServletRequest request, String thumbUrlString){
		
		return "demo/testGetThunm";
		
	}
}
