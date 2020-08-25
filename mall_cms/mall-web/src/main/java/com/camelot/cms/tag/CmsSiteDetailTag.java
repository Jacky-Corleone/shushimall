package com.camelot.cms.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsCategoryDTO;
import com.camelot.cmscenter.dto.CmsSiteDTO;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.cmscenter.service.CmsCategoryService;
import com.camelot.cmscenter.service.CmsSiteService;
import com.camelot.openplatform.common.DataGrid;

import freemarker.core.Environment;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 站点详情
 */
public class CmsSiteDetailTag implements TemplateDirectiveModel {
	
	/**
	 * 文章服务
	 */
	@Resource
	private CmsSiteService cmsSiteService;
	/**
	 * 输入参数id
	 */
	public static final String id = "id";
	
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsSiteDTO site = new CmsSiteDTO();
		if(params.get(id)!=null&&params.get(id)!=""){
			site = cmsSiteService.getCmsSiteById(params.get(id)+"");
		}
		if (body != null) { 
		 	env.setVariable("cmsSiteDetail", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(site));
            body.render(env.getOut()); 
        } else { 
            throw new RuntimeException("missing body"); 
        } 
	}

}
