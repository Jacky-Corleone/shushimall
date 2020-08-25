package com.thinkgem.jeesite.modules.cms.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Ranking;
import com.thinkgem.jeesite.modules.cms.entity.RankingList;
import com.thinkgem.jeesite.modules.cms.service.FileTplService;
import com.thinkgem.jeesite.modules.cms.service.RankingListService;
import com.thinkgem.jeesite.modules.cms.service.RankingService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;


@Controller
@RequestMapping(value = "${adminPath}/cms/ranking")
public class RankingController extends BaseController{
	
	@Autowired
	private RankingListService rankingListService;
	
	@Autowired
	private RankingService rankingService;
	
	@Autowired
	private SiteService siteService;
	
	@Autowired
	private FileTplService fileTplService;
	
	
	@ModelAttribute
	public Ranking get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return rankingService.get(id);
		}else{
			return new Ranking();
		}
	}
	
	@RequestMapping(value = {"list", ""})
	public String list(@Param("cid") String cid,Ranking ranking, RankingList rankingList, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(cid)){
			cid=request.getParameter("cid");
		}
		rankingList = rankingListService.get(cid);
		List<RankingList> rankinglist = new ArrayList<RankingList>();
		List<Ranking> offlineList = new ArrayList<Ranking>();
		List<Ranking> networkList = new ArrayList<Ranking>();
		rankinglist=rankingListService.getRankingList(rankingList);
		
		if (StringUtils.isNotEmpty(cid)) {
			networkList = rankingService.findRankingList(cid,ranking.NETWORK_RANKINGLIST);//type: 类型  网络排行榜:0  线下排行榜:1
			offlineList = rankingService.findRankingList(cid,ranking.OFFLINE_RANKINGLIST);
		}
		model.addAttribute("rankingListBean",rankingList);
		model.addAttribute("rankingList", rankinglist);
		model.addAttribute("networkList", networkList);
		model.addAttribute("offlineList", offlineList);
		return "modules/cms/rankingList";
	}

	@RequestMapping(value = "form")
	public String form(@Param("cid") String cid, @Param("id")String id,HttpServletRequest request, HttpServletResponse response,Model model) {
		Ranking ranking = new Ranking();
		if(StringUtils.isNotEmpty(id)){
			ranking = get(id);
			model.addAttribute("ranking", ranking);
			return "modules/cms/rankingForm";
		}else{
			ranking.setType(0);
			ranking.setTrend(0);
			ranking.setCid(cid);
			model.addAttribute("ranking", ranking);
			return "modules/cms/rankingForm";
		}
	}

	@RequestMapping(value = "save")
	public String save(@Param("ranking")Ranking ranking,@Param("rankingList")RankingList rankingList, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response) {
		if(StringUtils.isNotEmpty(ranking.getId())){
			ranking.setUpdateBy(UserUtils.getUser());
			ranking.setUpdateDate(new Date());
			rankingService.save(ranking);
		}else{
			ranking.setAddtime(new Date());
			ranking.setCreateBy(UserUtils.getUser());
			ranking.setCreateDate(new Date());
			rankingService.save(ranking);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/ranking/list?cid="+ranking.getCid();
	}
	
	@RequestMapping(value = "delete")
	public String delete(@Param("id")String id,@Param("cid")String cid,RedirectAttributes redirectAttributes,RankingList rankingList, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isNotEmpty(id)){
			rankingService.delete(id);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/ranking/list?cid="+cid;
	}
	/**
	 * 静态化发布文章
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
		Ranking ranking = rankingService.get(id);
		String sitePath=siteService.get(ranking.getId()).getPath();
		String path = File.separator+siteService.get(ranking.getId()).getPath()+".html";
		ranking.setDelFlag(Article.DEL_FLAG_NORMAL);
		ranking.setCreateBy(UserUtils.getUser());
		ranking.setCreateDate(new Date());
		rankingService.save(ranking);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ranking", ranking);
		String staticPath =sitePath+path;
		String templatePath = sitePath+File.separator+"template";
		FreeMarkers.renderStaticTemplate(map, staticPath, templatePath,"ranking.ftl");
		return "redirect:" + SysProperties.getAdminPath() + "/cms/ranking/list?cid="+ranking.getCid();
	}
	/**
	 * 静态化发布文章
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
		Ranking ranking = rankingService.get(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("ranking", ranking);
		String templatePath = siteService.get(ranking.getId()).getPath()+File.separator+"template";
		Configuration cfg = new Configuration(); 
		cfg.setDirectoryForTemplateLoading(new File(templatePath));
		Template template = cfg.getTemplate("ranking.ftl");
		String result = FreeMarkers.renderTemplate(template, model);
		model.addAttribute("content", result);
		return "modules/cms/front/preview";
	} 
	
	
	
}
