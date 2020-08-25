package com.thinkgem.jeesite.modules.cms.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsCategoryDTO;
import com.camelot.cmscenter.dto.CmsImageDto;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.cmscenter.service.CmsCategoryService;
import com.camelot.cmscenter.service.CmsImageService;
import com.camelot.openplatform.common.DataGrid;

import freemarker.core.Environment;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 图片列表
 */
public class CmsImageListTag implements TemplateDirectiveModel {
	
	/**
	 * 图片服务
	 */
	@Resource
	private CmsImageService cmsImageService;
	/**
	 * 输入参数id
	 */
	public static final String id = "id";
	
	/**
	 * 输入参数，所述内容
	 */
	public static final String aid = "aid";
	
	/**
	 * 类型
	 */
	public static final String imgtype = "imgtype";
	/**
	 * 名称
	 */
	public static final String imgname = "imgname";
	
	

	@SuppressWarnings("unchecked")
	public void execute(Environment env, Map params, TemplateModel[] loopVars,
			TemplateDirectiveBody body) throws TemplateException, IOException {
		CmsImageDto image = new CmsImageDto();
		if(params.get(id)!=null&&params.get(id)!=""){
			image.setId(params.get(id)+"");
		}
		if(params.get(aid)!=null&&params.get(aid)!=""){
			image.setAid(params.get(aid)+"");
		}
		if(params.get(imgtype)!=null&&params.get(imgtype)!=""){
			image.setImgtype(params.get(imgtype)+"");
		}
		if(params.get(imgname)!=null&&params.get(imgname)!=""){
			image.setImgname(params.get(imgname)+"");
		}
		DataGrid<CmsImageDto> data = cmsImageService.queryCmsImageList(null, image);
		if (body != null) { 
		 	env.setVariable("imageList", DefaultObjectWrapper.DEFAULT_WRAPPER.wrap(data.getRows()));
            body.render(env.getOut()); 
        } else { 
            throw new RuntimeException("missing body"); 
        } 
	}

}
