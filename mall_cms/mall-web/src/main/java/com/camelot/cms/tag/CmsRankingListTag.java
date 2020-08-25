package com.camelot.cms.tag;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.formula.functions.Rank;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class CmsRankingListTag implements TemplateDirectiveModel{
	
	public static final String  title="title";
	/**
	 * 删除标记（0：正常；1：删除；2：审核）
	 */
	public static final String delFlag = "delFlag";
	
	/**
	 * 系统类型
	 */
	public static final String cateid = "cateid";
	/**
	 * 关键字
	 */
	public static final String keywords = "keywords";
	/**
	 * 排行榜名称
	 */
	public static final String cname = "cname";
	/**
	 * 是否推荐  是：1 否：0
	 */
	public static final String recommend = "recommend";
	/**
	 * 是否发布  是：1 否：0
	 */
	public static final String release = "release";
	
	
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
//		CmsRankingListDTO rankList = new CmsRankingListDTO();
		
	}

}
