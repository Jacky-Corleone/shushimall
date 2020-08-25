package com.camelot.cms.tag;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.openplatform.common.DataGrid;

import freemarker.core.Environment;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 文章列表标签
 */
public class CmsArticleListTag implements TemplateDirectiveModel {
	
	/**
	 * 文章服务
	 */
	@Resource
	private CmsArticleService cmsArticleService;
	/**
	 * 输入参数，栏目ID。
	 */
	public static final String categoryid = "categoryid";
	/**
	 * 删除标记（0：正常；1：删除；2：审核）
	 */
	public static final String delFlag = "delFlag";
	
	/**
	 * 文章类型
	 */
	public static final String acateid = "acateid";
	
	/**
	 * 所属城市
	 */
	public static final String cityid = "cityid";
	
	/**
	 * 系统类型
	 */
	public static final String cateid = "cateid";
	
	/**
	 * 产品编号
	 */
	public static final String brandid = "brandid";
	/**
	 * 是否完工
	 */
	
	public static final String iswork = "iswork";
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsArticleDTO article = new CmsArticleDTO();
		if(params.get(categoryid)!=null&&params.get(categoryid)!=""){
			article.setCategoryid(params.get(categoryid)+"");
		}
		if(params.get(delFlag)!=null && params.get(delFlag)!=""){
			article.setDelFlag(params.get(delFlag)+"");
		}
		if(params.get(acateid)!=null && params.get(acateid)!=""){
			article.setAcateid(params.get(acateid)+"");
		}
		if(params.get(cateid)!=null && params.get(cateid)!=""){
			article.setCateid(params.get(cateid)+"");
		}
		if(params.get(brandid)!=null && params.get(brandid)!=""){
			article.setBrandid(params.get(brandid)+"");
		}
		if(params.get(iswork)!=null && params.get(iswork)!=""){
			article.setIswork(Integer.valueOf(params.get(iswork)+""));
		}
		DataGrid<CmsArticleDTO> dataGrid = cmsArticleService.queryCmsArticleList(null, article);
		
		if (body != null) { 
		 	env.setVariable("articleList", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(dataGrid.getRows()));
            body.render(env.getOut()); 
        } else { 
            throw new RuntimeException("missing body"); 
        } 
	}

}
