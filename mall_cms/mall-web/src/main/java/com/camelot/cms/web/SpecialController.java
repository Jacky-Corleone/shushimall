package com.camelot.cms.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.camelot.cms.freemarker.FreeMarkers;
import com.camelot.cmscenter.dto.CmsSpecialSubjectDTO;
import com.camelot.cmscenter.service.CmsSpecialSubjectService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;

import freemarker.template.Configuration;
import freemarker.template.Template;
@Controller
@RequestMapping("/zhuanti")
public class SpecialController {
	
	private static final String prefix = SysProperties.getProperty("cms.prefix"); 
	
	@Resource
	private CmsSpecialSubjectService cmsSpecialSubjectService;
	
	@RequestMapping("/special")
	public String infocenter(Pager<JSONObject> pager,CmsSpecialSubjectDTO cmsSpecialSubject,HttpServletRequest request,HttpServletResponse response,Model model){
		try {	
			String cityId = request.getParameter("cityId");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cityId", cityId);
			map.put("prefix", prefix);
			Configuration cfg = new Configuration(); 
			String path =this.getClass().getClassLoader().getResource("/").getPath();
			Template template = cfg.getTemplate(path+"cms"+File.separator+"template"+File.separator+"article.ftl");
			String result = FreeMarkers.renderTemplate(template, model);
			model.addAttribute("content", result);
			model.addAttribute("prefix", prefix);
			ExecuteResult<DataGrid<CmsSpecialSubjectDTO>> excuteResult = new ExecuteResult<DataGrid<CmsSpecialSubjectDTO>>();
			excuteResult =  cmsSpecialSubjectService.queryCmsSpecialSubjectList(pager,cmsSpecialSubject);
			//页面分页
//			page.setPageNo(pager.getPage());
//			page.setPageSize(pager.getRows());
//			page.setCount(excuteResult.getResult().getTotal());
//			page.setList(excuteResult.getResult().getRows());
//			model.addAttribute("page", page);
			model.addAttribute("pager", pager);
			model.addAttribute("excuteResult", excuteResult);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return "cms/preview";
	}
}
