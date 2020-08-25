package com.thinkgem.jeesite.modules.cms.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.RankingList;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.RankingListService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;


@Controller
@RequestMapping(value = "${adminPath}/cms/rankingList")
public class RankingListController extends BaseController{
	
	@Autowired
	private RankingListService rankingListService;
	
	@Autowired
	private SiteService siteService;
	
	
	@ModelAttribute
	public RankingList get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return rankingListService.get(id);
		}else{
			return new RankingList();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(RankingList rankingList, HttpServletRequest request, HttpServletResponse response, Model model) {
		String keywords = request.getParameter("keywordsId");
		if(StringUtils.isNotEmpty(keywords)){
			rankingList.setKeywords(keywords);
		}else{
			rankingList.setKeywords(keywords);
		}
        Page<RankingList> page = rankingListService.find(new Page<RankingList>(request, response), rankingList); 
        model.addAttribute("page", page);
        model.addAttribute("keywords",rankingList.getKeywords());
		return "modules/cms/rankinglistList";
	}

	@RequestMapping(value = "form")
	public String form(@Param("id") String id, @Param("keywords") String keywords, Model model) {
		if(StringUtils.isNotEmpty(id)){
			model.addAttribute("rankingList", get(id));
		}
		if(StringUtils.isNotEmpty(keywords)){
			model.addAttribute("keywords", keywords);
		}
		return "modules/cms/rankinglistForm";
	}

	@RequestMapping(value = "save")
	public String save(RankingList rankingList, Model model,HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		if(null!=rankingList.getId() && !"".equals(rankingList.getId())){
			rankingList.setUpdateBy(UserUtils.getUser());
			rankingList.setUpdateDate(new Date());
			rankingListService.save(rankingList);
		}else{
			rankingList.setAddtime(new Date());
			rankingList.setAdminid(UserUtils.getUser().getId());
			rankingList.setCreateBy(UserUtils.getUser());
			rankingList.setCreateDate(new Date());
			rankingListService.save(rankingList);
		}
		try {
			String keywords = new String(request.getParameter("keywordsId").getBytes("iso-8859-1"), "utf-8");
			if(StringUtils.isNotEmpty(keywords)){
				rankingList.setKeywords(keywords);
			}else{
				rankingList.setKeywords(keywords);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
        Page<RankingList> page = rankingListService.find(new Page<RankingList>(request, response), rankingList); 
        model.addAttribute("page", page);
        model.addAttribute("keywords",rankingList.getKeywords());
		return "modules/cms/rankinglistList";
	}
	
	@RequestMapping(value = "delete")
	public String delete(@Param("id")String id,@Param("keywords")String keywords, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response, Model model) {
		RankingList rankngList=new RankingList();
		if(null!=id && !"".equals(id)){
			rankingListService.delete(id);
		}
		if(StringUtils.isNotEmpty(keywords)){
			rankngList.setKeywords(keywords);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/rankingList/list?keywords="+keywords;//list(rankngList,request, response, model);
		
	}
	
	/**
	 * 静态化发布
	 * @param id
	 * @param categoryId
	 * @param isRe
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "publish")
	public String publish(String id,RankingList rankingList,HttpServletRequest request,HttpServletResponse response, Model model) {
		boolean flag = false;
		rankingList = rankingListService.get(id);
		String sitePath=siteService.get(Site.getCurrentSiteId()).getPath();
		String path = siteService.get(Site.getCurrentSiteId()).getPath()+File.separator+"templateHTML"+File.separator+rankingList.getId()+".html";
		rankingList.setUpdateBy(UserUtils.getUser());
		rankingList.setRelease(1);
		rankingList.setLink(path);
		flag=rankingListService.save(rankingList);
		if (flag) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("rankingList", rankingList);
			String staticPath = sitePath + path;
			String templatePath = sitePath + File.separator + "template";
			FreeMarkers.renderStaticTemplate(map, staticPath, templatePath,
					"rankingList.ftl");
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/rankingList/list";//this.list(rankingList, request, response, model);
	}
	/**
	 * 预览
	 * @param id
	 * @param categoryId
	 * @param isRe
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	
	@RequestMapping(value = "preview")
	public String preview(String id,HttpServletRequest request,HttpServletResponse response, Model model) throws IOException {
		String rootPath="";
		RankingList rankingList = rankingListService.get(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("rankingList", rankingList);
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
		String result = FreeMarkers.renderTagTemplate(rootPath, "rankingList.ftl", model);
		model.addAttribute("content", result);
		return "modules/cms/front/preview";
	} 
}
