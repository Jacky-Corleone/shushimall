/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.FileStaticTplService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.cms.utils.TplUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 文章Controller
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private CategoryService categoryService;
    @Autowired
   	private FileStaticTplService fileStaticTplService;
    @Autowired
   	private SiteService siteService;
    @Autowired
   	private ItemCategoryService itemCategoryService;
    @Autowired
   	private ItemBrandExportService itemBrandExportService;
	//地区
	@Autowired 
	private AddressBaseService addressBaseService;
	
	@ModelAttribute
	public Article get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return articleService.get(id);
		}else{
			return new Article();
		}
	}
	
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = {"list", ""})
	public String list(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
//		for (int i=0; i<10000000; i++){
//			Article a = new Article();
//			a.setCategory(new Category(article.getCategory().getId()));
//			a.setTitle("测试测试测试测试测试测试测试测试"+a.getCategory().getId());
//			a.setArticleData(new ArticleData());
//			a.getArticleData().setContent(a.getTitle());
//			articleService.save(a);
//		}
        Page<Article> page = articleService.find(new Page<Article>(request, response), article, true); 
        model.addAttribute("page", page);
		return "modules/cms/articleList";
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "form")
	public String form(Article article, Model model) {
		// 如果当前传参有子节点，则选择取消传参选择
		if (article.getCategory()!=null && StringUtils.isNotBlank(article.getCategory().getId())){
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(), Site.getCurrentSiteId());
			if (list.size() > 0){
				article.setCategory(null);
			}
		}
	    DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
        Pager brandPager = new Pager();
		brandPager.setPage(1);
		brandPager.setRows(Integer.MAX_VALUE);
        List<ItemBrandDTO> brandList = itemBrandExportService.queryItemBrandAllList(brandPager).getRows();
      	//查询地区省直辖市
        List<AddressBaseDTO> level = addressBaseService.getAddressesByLevel(2);

        model.addAttribute("brandAllList",brandList);
        model.addAttribute("addresList",level);
        model.addAttribute("contentViewList",getTplContent());
        model.addAttribute("article_DEFAULT_TEMPLATE",Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		return "modules/cms/articleForm";
	}

	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "save")
	public String save(Article article, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {
		if (!beanValidator(model, article)){
			return form(article, model);
		}
		articleService.save(article);
		addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(),50) + "'成功");
		String categoryId = article.getCategory()!=null?article.getCategory().getId():null;
		if("0".equals(article.getDelFlag())){
			return "redirect:"+SysProperties.getProperty("adminPath")+"/cms/article/publish?id="+article.getId();
		}else{
			return this.list(newArticle(categoryId), request, response, model);
		}
	}
	
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, String categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response, Model model) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!SecurityUtils.getSubject().isPermitted("cms:article:audit")){
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}
		articleService.delete(id, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"发布":"删除")+"文章成功");
		return this.list(newArticle(categoryId), request, response, model);
	}

	/**
	 * 文章选择列表
	 */
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "selectList")
	public String selectList(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
        list(article, request, response, model);
		return "modules/cms/articleSelectList";
	}
	
	/**
	 * 通过编号获取文章标题
	 */
	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = articleService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
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
	public String publish(String id,HttpServletRequest request,HttpServletResponse response, Model model) {
		Article article = articleService.get(id);
		String sitePath=article.getCategory().getSite().getPath();
		String path = File.separator+article.getCategory().getStaticpath()+article.getId()+".html";
		article.setDelFlag(Article.DEL_FLAG_NORMAL);
		article.setCreateBy(UserUtils.getUser());
		article.setCreateDate(new Date());
		article.setLink(path);
		articleService.save(article);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("article", article);
		String staticPath =sitePath+path;
		String templatePath = sitePath+File.separator+"templates";
		FreeMarkers.renderStaticTemplate(map, staticPath, templatePath, getTpl(article)+".ftl");
		return this.list(newArticle(article.getCategory().getId()), request, response, model);
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
	
	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "preview")
	public String preview(String id,HttpServletRequest request,HttpServletResponse response, Model model) throws IOException {
		Article article = articleService.get(id);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("aritlce", article);
		String templatePath = article.getCategory().getSite().getPath()+File.separator+"templates";
		String result = FreeMarkers.renderTagTemplate(templatePath,getTpl(article)+".ftl",model);
		model.addAttribute("content", result);
		return "modules/cms/front/preview";
	} 
    private String getTpl(Article article){
        if(StringUtils.isBlank(article.getCustomContentView())){
            String view = null;
            Category c = article.getCategory();
            boolean goon = true;
            do{
                if(StringUtils.isNotBlank(c.getCustomContentView())){
                    view = c.getCustomContentView();
                    goon = false;
                }else if(c.getParent() == null || c.getParent().isRoot()){
                    goon = false;
                }else{
                    c = c.getParent();
                }
            }while(goon);
            return StringUtils.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
        }else{
            return article.getCustomContentView();
        }
    }
    private List<String> getTplContent() {
   		List<String> tplList = fileStaticTplService.getNameListByPrefix(siteService.get(Site.getCurrentSiteId()).getPath()+File.separator+"templates");
   		tplList = TplUtils.tplTrim(tplList, Article.DEFAULT_TEMPLATE, "");
   		return tplList;
   	}
    
    
    private Article newArticle(String catetoryId){
    	Article article=new Article();
		if(article.getCategory()!=null){
		article.getCategory().setId(catetoryId);
		}else {
			Category category=new Category();
			category.setId(catetoryId);
			article.setCategory(category);
		}
		return article;
    	
    }
}
