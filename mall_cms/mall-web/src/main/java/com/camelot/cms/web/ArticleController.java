package com.camelot.cms.web;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.cms.freemarker.FreeMarkers;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.openplatform.util.SysProperties;


/**
 * 学知识
 * @author jh
 *
 */
@Controller
@RequestMapping("/article")
public class ArticleController {
	
	@Resource
	private CmsArticleService cmsArticleService;
	
	private final static String prefix = SysProperties.getProperty("cms_prefix");
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("/study")
	public ModelAndView studyKnowledge(HttpServletRequest request,HttpServletResponse response,Model model){
		String rootPath  = "";
		try {
			String cityId = request.getParameter("cityId");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cityId", cityId);
			String path =this.getClass().getClassLoader().getResource("/").getPath();
			//windows系统下
			  if("\\".equals(File.separator)){   
			   rootPath  = path.substring(1,path.indexOf("/classes"));
			  }
			  //linux系统下
			  if("/".equals(File.separator)){   
			   rootPath  = path.substring(0,path.indexOf("/classes"));
			  }
			  rootPath=rootPath+File.separator+"views/cms/template"+File.separator;
			  rootPath = rootPath.replace("/", "\\");
			String result = FreeMarkers.renderTagTemplate(rootPath, "article.ftl", model);
			model.addAttribute("content", result);
			model.addAttribute("prefix", prefix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/cms/preview");
	}
	@RequestMapping("/info")
	public ModelAndView infocenter(HttpServletRequest request,HttpServletResponse response,Model model){
		String rootPath  = "";
		try {
			String cityId = request.getParameter("cityId");
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cityId", cityId);
			String path =this.getClass().getClassLoader().getResource("/").getPath();
			//windows系统下
			  if("\\".equals(File.separator)){   
			   rootPath  = path.substring(1,path.indexOf("/classes"));
			  }
			  //linux系统下
			  if("/".equals(File.separator)){   
			   rootPath  = path.substring(0,path.indexOf("/classes"));
			  }
			  rootPath=rootPath+File.separator+"views/cms/template"+File.separator;
			  rootPath = rootPath.replace("/", "\\");
			String result = FreeMarkers.renderTagTemplate(rootPath, "article.ftl", model);
			model.addAttribute("content", result);
			model.addAttribute("prefix", prefix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView("/cms/preview");
	}

	

}
