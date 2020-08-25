package com.thinkgem.jeesite.modules.cms.tag;

import java.io.IOException;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 文章列表标签
 */
public class CmsArticleListTag implements TemplateDirectiveModel {
	/**
	 * 模板名称
	 */
	public static final String tpl_name = "article_list";

	/**
	 * 输入参数，栏目ID。
	 */
	public static final String categoryid = "categoryid";
	
	/**
	 * 
	 */



	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		System.out.println("333333333333333333333333333333");
	}

}
