package com.camelot.mall.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.sellercenter.malladvertise.service.MallAdExportService;

@Controller
@RequestMapping("/adVisit")
public class AdVisitController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private MallAdExportService mallAdService;
	
	@RequestMapping("")
	public String visit(String id, String type, String url, HttpServletRequest request){
		// 1: ad  2： bunner  3： recattr
		try {
			this.mallAdService.saveMallAdCount(Long.valueOf(id),Long.valueOf(type));
		} catch (Exception e) {
			logger.error("广告访问量新增:",e);
		}
        if(StringUtils.isBlank(url)){
            request.setAttribute("indexUrl", "true");//跳转到首页
        }else{
            request.setAttribute("siteUrl", url);//跳转到指定页面
            request.setAttribute("runType", "login");//跳转到指定页面
        }
        return "/message";
    }
}
