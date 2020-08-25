/*package com.thinkgem.jeesite.modules.cms.web;

import java.io.File;
import java.util.Iterator;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.camelot.cmscenter.dto.CmsSpecialSubjectModelDTO;
import com.camelot.cmscenter.service.CmsSpecialSubjectModelService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value = "${adminPath}/cms/specsubmodel")
public class SpecialSubjectModelController {
	
	@Resource
	private CmsSpecialSubjectModelService cmsSpecialSubjectModelService;
	
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
	public String list(@ModelAttribute("pager") Pager<?> pager,CmsSpecialSubjectModelDTO cmsSpecialSubjectModel, HttpServletRequest request, HttpServletResponse response, Model model){
		try {
			String name = request.getParameter("name");
			cmsSpecialSubjectModel.setName(name);
			ExecuteResult<DataGrid<CmsSpecialSubjectModelDTO>> excuteResult =  cmsSpecialSubjectModelService.queryCmsSpecSubModelList(cmsSpecialSubjectModel,pager);
			//页面分页
			Page<CmsSpecialSubjectModelDTO> page = new Page<CmsSpecialSubjectModelDTO>();
			page.setPageNo(pager.getPage());
			page.setPageSize(pager.getRows());
			page.setCount(excuteResult.getResult().getTotal());
			page.setList(excuteResult.getResult().getRows());
			request.setAttribute("page", page);
			request.setAttribute("name", cmsSpecialSubjectModel.getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/cms/specialsubjectmodelList";
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
		if(StringUtils.isNotEmpty(id)){
			try {
				boolean flag=cmsSpecialSubjectModelService.deleteCmsSpecialSubjectModel(id);
				if (flag) {
					File fd = new File(siteService.get(Site.getCurrentSiteId()).getPath()+ File.separator+ "templates"+ File.separator + "specialFile"+File.separator+id+".ftl");
					if (fd.exists()) {
						fd.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/specsubmodel/list";
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
	public String saveSpecialSubject(@Param("cmsSpecialSubjectModel")CmsSpecialSubjectModelDTO cmsSpecialSubjectModel, HttpServletRequest request, HttpServletResponse response, Model model){
		boolean flag = false;
		if(null!=cmsSpecialSubjectModel){
			try {
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
				if(StringUtils.isNotEmpty(cmsSpecialSubjectModel.getId())){
					cmsSpecialSubjectModel.setUpdateBy(UserUtils.getUser().getId());
					flag=cmsSpecialSubjectModelService.updateCmsSpecialSubjectModel(cmsSpecialSubjectModel);
				}else{
					cmsSpecialSubjectModel.setId(String.valueOf(UUID.randomUUID()));
					cmsSpecialSubjectModel.setCreateBy(UserUtils.getUser().getId());
					flag=cmsSpecialSubjectModelService.saveCmsSpecialSubjectModel(cmsSpecialSubjectModel);
				}
				if (flag) {
					if (multipartResolver.isMultipart(request)) {
						File fd = new File(siteService.get(Site.getCurrentSiteId()).getPath()+ File.separator+ "templates"+ File.separator+ "specialFile");
						if (!fd.exists()) {
							fd.mkdirs();
						}
						//转换成多部分request    
						MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
						//取得request中的所有文件名  
						Iterator<String> iter = multiRequest.getFileNames();
						while (iter.hasNext()) {
							//取得上传文件  
							MultipartFile file = multiRequest.getFile(iter.next());
							if (file != null) {
								File localFile = new File(fd.getPath()+ File.separator + cmsSpecialSubjectModel.getId()+".ftl");
								file.transferTo(localFile);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/specsubmodel/list";
	}
	
	*//**
	 * 添加专题跳转form页
	 * @param article
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	@RequestMapping(value = "/add")
	public String addSpecialSubjectForm(CmsSpecialSubjectModelDTO cmsSpecialSubjectModel, HttpServletRequest request, HttpServletResponse response, Model model){
		String modelId = request.getParameter("id");
		try {
			if(StringUtils.isNotEmpty(modelId)){
				cmsSpecialSubjectModel = cmsSpecialSubjectModelService.getCmsSpecialSubjectModel(modelId);
				model.addAttribute("cmsSpecialSubjectModel", cmsSpecialSubjectModel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/cms/specialsubjectmodelForm";
	}
}
*/