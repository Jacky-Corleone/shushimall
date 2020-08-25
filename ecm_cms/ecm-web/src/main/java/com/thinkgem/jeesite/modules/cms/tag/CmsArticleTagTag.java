package com.thinkgem.jeesite.modules.cms.tag;

import javax.annotation.Resource;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.cmscenter.service.CmsArtTagsService;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.Map;

/**
 * 文章标签的标签
 */
public class CmsArticleTagTag implements TemplateDirectiveModel {

	@Resource
	private CmsArtTagsService cmsArtTagsService;
	/**
	 * 文章标签id
	 */
	public static final String tagid = "tagid";

	/**
	 * 父级id。
	 */
	public static final String parentid = "parentid";
	
	/**
	 * 标签名
	 */
	public static final String tagname="tagname";

	/**
	 * 文章seo描述
	 */
	public static final String art_seo="art_seo";
	/**
	 * 文章seo描述
	 */
	public static final String pro_seo="pro_seo";
	/**
	 * 图片seo描述
	 */
	public static final String img_seo="img_seo";
	/**
	 * 类型
	 */
	public static final String type="type";
	/**
	 * 是否为文章标签
	 */
	public static final String art_tag="art_tag";
	/**
	 * 是否为图片标签
	 */
	public static final String img_tag="img_tag";
	/**
	 * 是否为工程筛选项
	 */
	public static final String pro_nav="pro_nav";
	/**
	 * 是否为图片筛选项
	 */
	public static final String img_nav="img_nav";

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
						TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsArticleTagsDto cmsArticleTagsDto = new CmsArticleTagsDto();
		if(params.get(tagid)!=null && params.get(tagid)!=""){
			cmsArticleTagsDto.setTagid(params.get(tagid) + "");
		}
		if(params.get(parentid)!=null && params.get(parentid)!=""){
			cmsArticleTagsDto.setParentid(params.get(parentid) + "");
		}
		if(params.get(tagname)!=null && params.get(tagname)!=""){
			cmsArticleTagsDto.setTagname(params.get(tagname) + "");
		}
		if(params.get(art_seo)!=null && params.get(art_seo)!=""){
			cmsArticleTagsDto.setArt_seo(params.get(art_seo) + "");
		}
		if(params.get(pro_seo)!=null && params.get(pro_seo)!=""){
			cmsArticleTagsDto.setPro_seo(params.get(pro_seo) + "");
		}
		if(params.get(img_seo)!=null && params.get(img_seo)!=""){
			cmsArticleTagsDto.setImg_seo(params.get(img_seo) + "");
		}
		if(params.get(type)!=null && params.get(type)!=""){
			cmsArticleTagsDto.setType(Integer.valueOf(params.get(type) + ""));
		}
		if(params.get(art_tag)!=null && params.get(art_tag)!=""){
			cmsArticleTagsDto.setArt_tag(Integer.valueOf(params.get(art_tag) + ""));
		}
		if(params.get(img_tag)!=null && params.get(img_tag)!=""){
			cmsArticleTagsDto.setImg_tag(Integer.valueOf(params.get(img_tag) + ""));
		}
		if(params.get(pro_nav)!=null && params.get(pro_nav)!=""){
			cmsArticleTagsDto.setPro_nav(Integer.valueOf(params.get(pro_nav) + ""));
		}
		if(params.get(img_nav)!=null && params.get(img_nav)!=""){
			cmsArticleTagsDto.setImg_nav(Integer.valueOf(params.get(img_nav) + ""));
		}
		DataGrid<CmsArticleTagsDto> dataGrid = cmsArtTagsService.queryCmsArtTagsList(null, cmsArticleTagsDto);

		if (body != null) {
			env.setVariable("articleTagList", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(dataGrid.getRows()));
			body.render(env.getOut());
		} else {
			throw new RuntimeException("missing body");
		}
	}

}
