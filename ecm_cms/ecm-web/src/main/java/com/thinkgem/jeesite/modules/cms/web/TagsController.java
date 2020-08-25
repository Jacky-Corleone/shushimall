/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.thinkgem.jeesite.modules.cms.web;

import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListJoinDto;
import com.camelot.cmscenter.dto.CmsImageDto;
import com.camelot.cmscenter.service.CmsArtTagsListService;
import com.camelot.cmscenter.service.CmsArtTagsService;
import com.camelot.cmscenter.service.CmsImageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.FileStaticTplService;
import com.thinkgem.jeesite.modules.cms.utils.TplUtils;
import com.thinkgem.jeesite.modules.sys.utils.DictUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * ImageController图集Controller
 * @author mengbo
 * @version 2016-2-16
 */

@Controller
@RequestMapping(value = "admin/cms/tagsMange")
public class TagsController extends BaseController {
	@Resource
	private CmsArtTagsService cmsArtTagsService;
	@Resource
	private CmsArtTagsListService cmsArtTagsListService;
	/**
	 * 标签选择列表
	 */
	@RequestMapping(value = "selectList")
	public String selectList(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
		String aid=request.getParameter("aid");
		list(aid, request, response, model);
		return "modules/cms/tagsSelectList";
	}
	@RequestMapping(value = {"list", ""})
	public String list(String id, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<CmsArticleTagsListJoinDto> page = new Page<CmsArticleTagsListJoinDto>(request, response);
		/*List<CmsArticleTagsListDto> cmsArticleTagsListDtos = cmsArtTagsListService.getCmsArtTagsListByArtId(id);*/
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		CmsArticleTagsListDto cmsArticleTagsListDto=new CmsArticleTagsListDto();
		cmsArticleTagsListDto.setAid(id);
		DataGrid<CmsArticleTagsListJoinDto> pageGrid = cmsArtTagsListService.queryCmsArtTagsListListJion(pager, cmsArticleTagsListDto);
		page.setCount(pageGrid.getTotal());
		page.setList(pageGrid.getRows());
		model.addAttribute("page", page);
		return "modules/cms/tagsList";
	}
	@RequestMapping(value = {"modifytagsList", ""})
	/*通过文章id会查出多条关系，先删除所有的关联然后再把这次选择的关联加进去*/
	public String modifytagsList(Map params){
		String aid=null;//查询出文章id
		if(params.get("checkIds")!=null&&params.get("checkIds")!=""){
			List<String> idList= Arrays.asList(String.valueOf(params.get("checkIds")).split(","));
			List<String> allIdList= Arrays.asList(String.valueOf(params.get("checkIds")).split(","));
			for(int i=0;i<idList.size();i++){
				String strId=idList.get(i);
				CmsArticleTagsListDto cmsArticleTagsListDto=cmsArtTagsListService.getCmsArtTagsListById(strId);
				aid=cmsArticleTagsListDto.getAid();
				cmsArtTagsListService.insertTagList(cmsArticleTagsListDto);//向关系表中插入选中的记录
			}
			for(int i=0;i<allIdList.size();i++){
				String strId=allIdList.get(i);
				cmsArtTagsListService.deleteArtTagsList(strId);//删除本页关系表中的数据
			}
		}
		//删除掉该文章的所以标签
		return null;
	}
}