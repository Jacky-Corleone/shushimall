/*package com.thinkgem.jeesite.modules.cms.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.cmscenter.dto.CmsSpecialSubjectDTO;
import com.camelot.cmscenter.dto.CmsSpecialSubjectModelDTO;
import com.camelot.cmscenter.service.CmsSpecialSubjectModelService;
import com.camelot.cmscenter.service.CmsSpecialSubjectService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value = "${adminPath}/cms/specsub")
public class SpecialSubjectController {
	
	@Resource
	private CmsSpecialSubjectService cmsSpecialSubjectService;
	
	@Resource
	private CmsSpecialSubjectModelService cmsSpecialSubjectModelService;
	
	@Resource
   	private ItemCategoryService itemCategoryService;
	
	@Resource
	private SiteService siteService;
	
	*//**
	 * 专题分页
	 * @param article
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	
	@RequestMapping(value = "/list")
	public String list(@ModelAttribute("pager") Pager<?> pager,CmsSpecialSubjectDTO cmsSpecialSubject, HttpServletRequest request, HttpServletResponse response, Model model){
		
		ExecuteResult<DataGrid<CmsSpecialSubjectDTO>> excuteResult = new ExecuteResult<DataGrid<CmsSpecialSubjectDTO>>();
	try {
			
			excuteResult =  cmsSpecialSubjectService.queryCmsSpecialSubjectList(pager,cmsSpecialSubject);
			//页面分页
			Page<CmsSpecialSubjectDTO> page = new Page<CmsSpecialSubjectDTO>();
			page.setPageNo(pager.getPage());
			page.setPageSize(pager.getRows());
			page.setCount(excuteResult.getResult().getTotal());
			page.setList(excuteResult.getResult().getRows());
			model.addAttribute("page", page);
			model.addAttribute("specialSubjectBean", cmsSpecialSubject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/cms/specialsubjectList";
	}
	
	*//**
	 * 删除专题
	 * @param article
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "/delete")
	public String deleteSpecialSubject(@Param("id")String id, HttpServletRequest request, HttpServletResponse response, Model model){
		CmsSpecialSubjectDTO cmsSpecialSubjectDTO = new CmsSpecialSubjectDTO();
		if(StringUtils.isNotEmpty(id)){
			try {
				cmsSpecialSubjectDTO = cmsSpecialSubjectService.getCmsSpecialSubjectById(id);
				cmsSpecialSubjectDTO.setUpdateBy(UserUtils.getUser().getId());
				boolean flag = cmsSpecialSubjectService.deleteCmsSpecialSubjectById(cmsSpecialSubjectDTO);
				if (flag) {
					File fd = new File(siteService.get(Site.getCurrentSiteId()).getPath()+ File.separator+ "templates"+ File.separator + "specialPic"+File.separator+cmsSpecialSubjectDTO.getId()+cmsSpecialSubjectDTO.getSpecialSubjectPic());
					if (fd.exists()) {
						fd.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/specsub/list";
	}
	
	*//**
	 * 保存专题
	 * @param article
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "/save")
	public String saveSpecialSubject(@Param("cmsSpecialSubjectDTO")CmsSpecialSubjectDTO specialSubjectDTO, HttpServletRequest request, HttpServletResponse response, Model model){
		boolean flag = false;
		if(null!=specialSubjectDTO){
			try {
				if(StringUtils.isNotEmpty(specialSubjectDTO.getId())){
					specialSubjectDTO.setUpdateBy(UserUtils.getUser().getId());
					flag=cmsSpecialSubjectService.updateCmsSpecialSubject(specialSubjectDTO);
				}else{
					specialSubjectDTO.setId(String.valueOf(UUID.randomUUID()));
					specialSubjectDTO.setCreateBy(UserUtils.getUser().getId());
					specialSubjectDTO.setDelFlag("0");
					flag=cmsSpecialSubjectService.saveCmsSpecialSubject(specialSubjectDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/specsub/list";
	}
	
	*//**
	 * 添加专题跳转form页
	 * @param article
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/add")
	public String addSpecialSubjectForm(CmsSpecialSubjectDTO cmsSpecialSubjectDTO, HttpServletRequest request, HttpServletResponse response, Model model){
		String specId = request.getParameter("id");
		@SuppressWarnings("rawtypes")
		DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
		List<CmsSpecialSubjectModelDTO> queryList = new ArrayList<CmsSpecialSubjectModelDTO>();
		try {
			if(StringUtils.isNotEmpty(specId)){
				cmsSpecialSubjectDTO = cmsSpecialSubjectService.getCmsSpecialSubjectById(specId);
			}
			queryList=cmsSpecialSubjectModelService.queryModelList();
			cmsSpecialSubjectDTO.setSpecialList(queryList);
			cmsSpecialSubjectDTO.setProductList(dataGrid.getRows());
			model.addAttribute("cmsSpecialSubjectDTO",cmsSpecialSubjectDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "modules/cms/specialsubjectForm";
	}
	
	*//**
	 * 静态化发布
	 * @param id
	 * @param categoryId
	 * @param isRe
	 * @param redirectAttributes
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "publish")
	public String publish(String id,CmsSpecialSubjectDTO specialSubjectDTO,HttpServletRequest request,HttpServletResponse response, Model model) {
		try {
			specialSubjectDTO = cmsSpecialSubjectService.getCmsSpecialSubjectById(id);
			String sitePath=siteService.get(Site.getCurrentSiteId()).getPath();
			String path = File.separator+"special"+specialSubjectDTO.getId()+".html";
			specialSubjectDTO.setLink(path);
			specialSubjectDTO.setUpdateBy(UserUtils.getUser().getId());
			cmsSpecialSubjectService.release(specialSubjectDTO);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("specialSubjectDTO", specialSubjectDTO);
			String staticPath =sitePath+path;
			String templatePath = sitePath+File.separator+"template";
			FreeMarkers.renderStaticTemplate(map, staticPath, templatePath,specialSubjectDTO.getSpecialSubjectModel()+".ftl");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/specsub/list";
	}
}
*/