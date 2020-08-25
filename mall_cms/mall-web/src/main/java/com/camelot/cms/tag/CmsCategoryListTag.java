package com.camelot.cms.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsCategoryDTO;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.cmscenter.service.CmsCategoryService;
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
public class CmsCategoryListTag implements TemplateDirectiveModel {
	
	/**
	 * 文章服务
	 */
	@Resource
	private CmsCategoryService cmsCategoryService;
	/**
	 * 输入参数id
	 */
	public static final String id = "id";
	
	/**
	 * 输入参数，站点id
	 */
	public static final String siteId = "siteId";
	
	/**
	 * 上级栏目id，parentid
	 */
	public static final String parentId = "parentId";
	/**
	 * 状态
	 */
	public static final String delFlag = "delFlag";
	/**
	 * 名称
	 */
	public static final String name = "name";
	
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsCategoryDTO categroy = new CmsCategoryDTO();
		if(params.get(id)!=null&&params.get(id)!=""){
			categroy.setId(params.get(id)+"");
		}
		if(params.get(siteId)!=null&&params.get(siteId)!=""){
			categroy.setSiteId(params.get(siteId)+"");
		}
		if(params.get(parentId)!=null&&params.get(parentId)!=""){
			categroy.setParentId(params.get(parentId)+"");
		}
		if(params.get(delFlag)!=null&&params.get(delFlag)!=""){
			categroy.setDelFlag(params.get(delFlag)+"");
		}
		DataGrid<CmsCategoryDTO> data = cmsCategoryService.queryCmsCategoryList(null, categroy);
		if (body != null) { 
		 	env.setVariable("categoryList", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(data.getRows()));
            body.render(env.getOut()); 
        } else { 
            throw new RuntimeException("missing body"); 
        } 
	}

}
