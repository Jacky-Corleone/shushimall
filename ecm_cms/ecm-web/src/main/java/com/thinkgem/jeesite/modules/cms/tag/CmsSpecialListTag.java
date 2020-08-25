package com.thinkgem.jeesite.modules.cms.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsCategoryDTO;
import com.camelot.cmscenter.dto.CmsSpecialSubjectDTO;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.cmscenter.service.CmsCategoryService;
import com.camelot.cmscenter.service.CmsSpecialSubjectService;
import com.camelot.openplatform.common.DataGrid;

import freemarker.core.Environment;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 栏目列表
 */
public class CmsSpecialListTag implements TemplateDirectiveModel {
	
	/**
	 * 文章服务
	 */
	@Resource
	private CmsSpecialSubjectService cmsSpecialSubjectService;
	/**
	 * 输入参数id
	 */
	public static final String id = "id";
	
	/**
	 * 状态
	 */
	public static final String delFlag = "delFlag";
	
	/**
	 * 
	 */
	public static final String specialSubjectName = "specialSubjectName";
	/**
	 * 
	 */
	public static final String productCategory = "productCategory";
	/**
	 * 
	 */
	public static final String specialSubjectTitle = "specialSubjectTitle";
	/**
	 * 
	 */
	public static final String specialSubjectModel = "specialSubjectModel";
	/**
	 * 
	 */
	public static final String categoryProfile = "categoryProfile";
	/**
	 * 
	 */
	public static final String keywords = "keywords";
	
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSpecialSubjectDTO special = new CmsSpecialSubjectDTO();
		if(params.get(id)!=null&&params.get(id)!=""){
			special.setId(params.get(id)+"");
		}
	
		if(params.get(delFlag)!=null&&params.get(delFlag)!=""){
			special.setDelFlag(params.get(delFlag)+"");
		}
		if(params.get(specialSubjectName)!=null&&params.get(specialSubjectName)!=""){
			special.setSpecialSubjectName(params.get(specialSubjectName)+"");
		}
		if(params.get(productCategory)!=null&&params.get(productCategory)!=""){
			special.setProductCategory(params.get(productCategory)+"");
		}
		if(params.get(specialSubjectTitle)!=null&&params.get(specialSubjectTitle)!=""){
			special.setSpecialSubjectTitle(params.get(specialSubjectTitle)+"");
		}
		if(params.get(specialSubjectModel)!=null&&params.get(specialSubjectModel)!=""){
			special.setSpecialSubjectModel(params.get(specialSubjectModel)+"");
		}
		if(params.get(categoryProfile)!=null&&params.get(categoryProfile)!=""){
			special.setCategoryProfile(params.get(categoryProfile)+"");
		}
		if(params.get(keywords)!=null&&params.get(keywords)!=""){
			special.setKeywords(params.get(keywords)+"");
		}
		
		DataGrid<CmsSpecialSubjectDTO> data = new DataGrid<CmsSpecialSubjectDTO>();
		try {
			data = cmsSpecialSubjectService.queryCmsSpecialSubjectList(null, special).getResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (body != null) { 
		 	env.setVariable("specialList", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(data.getRows()));
            body.render(env.getOut()); 
        } else { 
            throw new RuntimeException("missing body"); 
        } 
	}

}
