package com.thinkgem.jeesite.modules.cms.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.service.CmsArticleService;

import freemarker.core.Environment;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 文章列表标签
 */
public class CmsRelaArticleListTag implements TemplateDirectiveModel {
	
	/**
	 * 文章服务
	 */
	@Resource
	private CmsArticleService cmsArticleService;
	/**
	 * 输入参数，相关文章id（数组）
	 */
	public static final String idList = "idList";

	
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		List<CmsArticleDTO> list = new ArrayList<CmsArticleDTO>();
		CmsArticleDTO article = new CmsArticleDTO();
		article.setDelFlag("0");
		if(params.get(idList)!=null && params.get(idList)!=""){
			List<String> idLists = Arrays.asList(String.valueOf(params.get(idList)).split(","));
			article.setIdList(idLists);
			list = cmsArticleService.queryCmsArticleList(null, article).getRows();
		}
		if (body != null) { 
		 	env.setVariable("articleList", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(list));
            body.render(env.getOut()); 
        } else { 
            throw new RuntimeException("missing body"); 
        } 
	}

}
