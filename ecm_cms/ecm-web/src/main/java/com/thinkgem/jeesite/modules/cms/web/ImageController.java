package com.thinkgem.jeesite.modules.cms.web;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListJoinDto;
import com.camelot.cmscenter.service.CmsArtTagsListService;
import com.camelot.cmscenter.service.CmsArtTagsService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.thinkgem.jeesite.common.config.Global;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.FreeMarkers;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.cms.entity.Article;
import com.thinkgem.jeesite.modules.cms.entity.ArticleImage;
import com.thinkgem.jeesite.modules.cms.entity.Category;
import com.thinkgem.jeesite.modules.cms.entity.Site;
import com.thinkgem.jeesite.modules.cms.service.ArticleImageService;
import com.thinkgem.jeesite.modules.cms.service.ArticleService;
import com.thinkgem.jeesite.modules.cms.service.CategoryService;
import com.thinkgem.jeesite.modules.cms.service.FileStaticTplService;
import com.thinkgem.jeesite.modules.cms.service.SiteService;
import com.thinkgem.jeesite.modules.cms.utils.TplUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * ImageController
 * @author mengbo
 * @version 2016-2-16
 */


@Controller
@RequestMapping(value = "${adminPath}/cms/image")
public class ImageController extends BaseController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private CategoryService categoryService;
    @Autowired
   	private FileStaticTplService fileStaticTplService;
    @Autowired
   	private SiteService siteService;
	@Autowired
	private ArticleImageService articleImageService;
	@Resource
	private CmsArtTagsListService cmsArtTagsListService;
	@Resource
	private CmsArtTagsService cmsArtTagsService;
	@ModelAttribute
	public Article get(@RequestParam(required=false) String id) {
		if (StringUtils.isNotBlank(id)){
			return articleService.get(id);
		}else{
			return new Article();
		}
	}

	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = {"list", ""})
	public String list(Article article, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Article> page = articleService.find(new Page<Article>(request, response), article, true);
        model.addAttribute("page", page);
		return "modules/cms/imageList";
	}
	@RequestMapping(value = "step")
	public String step(){
		return "modules/cms/step";
	}
	@RequestMapping(value = "form")
	public String form(Article article, Model model,HttpServletRequest request,HttpServletResponse response) {
		// 如果当前传参有子节点，则选择取消传参选择
 		if (article.getCategory()!=null && StringUtils.isNotBlank(article.getCategory().getId())){
			String name=article.getCategory().getName();
			try {
				name=URLDecoder.decode(name, "UTF-8");//重新用utf-8解码
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			article.getCategory().setName("工程案例");
			List<Category> list = categoryService.findByParentId(article.getCategory().getId(), Site.getCurrentSiteId());
			if (list.size() > 0){
				article.setCategory(null);
			}
		}
		CmsArticleTagsListDto cmsArticleTagsListDto =new CmsArticleTagsListDto();
		cmsArticleTagsListDto.setAid(article.getId());
		DataGrid<CmsArticleTagsListJoinDto> cmsArticleTagsListDtoList =cmsArtTagsListService.queryCmsArtTagsListListJion(null, cmsArticleTagsListDto);//查询标签
		model.addAttribute("cmsArticleTagsListDtoList",cmsArticleTagsListDtoList);
        model.addAttribute("contentViewList", getTplContent());
		model.addAttribute("article_DEFAULT_TEMPLATE",Article.DEFAULT_TEMPLATE);
		model.addAttribute("article", article);
		return "modules/cms/imageForm";
	}
	/**
	 * 子页面保存
	 * */
	@RequestMapping(value = "tagsSave")
	@ResponseBody
	public void tagsSave(HttpServletRequest request) {
		String str = request.getParameter("checkIds");
		String artId= request.getParameter("artId");
		String[] checkId=str.split(",");
		if(checkId.length>0) {
			for (int i = 0; i < checkId.length; i++) {
				CmsArticleTagsListDto cmsArticleTagsListDto = new CmsArticleTagsListDto();
				cmsArticleTagsListDto.setId(String.valueOf(UUID.randomUUID()));
				cmsArticleTagsListDto.setAid(artId);
				cmsArticleTagsListDto.setTagid(checkId[i]);
				/*cmsArticleTagsListDto.setImgid("");*/
				cmsArticleTagsListDto.setAddtime(new Date());
				cmsArtTagsListService.insertTagList(cmsArticleTagsListDto);
			}
		}
	}
	@RequestMapping(value = "imgForm")
	public String imgForm(String id, Model model) {
		List<ArticleImage> imgList=articleImageService.findByIds(id);
		model.addAttribute("article_DEFAULT_TEMPLATE",Article.DEFAULT_TEMPLATE);
		model.addAttribute("imgList", imgList);
		return "modules/cms/addImageForm";
	}
	@RequestMapping(value = "saveImg")
	public String saveImg(Article article,ArticleImage articleImage,String id, Model model, RedirectAttributes redirectAttributes,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		String[] imgName=httpServletRequest.getParameterValues("imgname");
		String[] remark=httpServletRequest.getParameterValues("remark");
		String[] paths=httpServletRequest.getParameterValues("imgPath");
		List<ArticleImage> articleImages=new ArrayList<ArticleImage>();
		articleImageService.deletebyId(id);
		if(imgName!=null) {
			for (int i = 0; i < imgName.length; i++) {
				ArticleImage articleImage1 = new ArticleImage();
				articleImage1.setImgname(imgName[i]);
				articleImage1.setRemark(remark[i]);
				articleImage1.setAid(id);
				String uuid = UUID.randomUUID().toString();
				articleImage1.setId(uuid);
				articleImage1.setPath(paths[i]);
				articleImages.add(articleImage1);
			}
			articleImageService.save(articleImages);
		}
		addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
		String categoryId = article.getCategory()!=null?article.getCategory().getId():null;
		Article article1=new Article();
		if(article1.getCategory()!=null){
			article1.getCategory().setId(categoryId);
		}else {
			Category category=new Category();
			category.setId(categoryId);
			article1.setCategory(category);
		}
		article=null;
		return this.list(article1, httpServletRequest, httpServletResponse, model);
	}
	@RequestMapping(value = "save")
	public String save(Article article, Model model, RedirectAttributes redirectAttributes,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) {
		String acateid=httpServletRequest.getParameter("acateid");
		String[] tagid=httpServletRequest.getParameterValues("tagId");//获得标签id
		article.setAcateid(acateid);
		CmsArticleTagsListDto cmsArticleTagsListDto = new CmsArticleTagsListDto();
		cmsArtTagsListService.deleteArtTagsList(article.getId());//删除本文章下的所有关联
		if(tagid!=null&&tagid.length>0){//保存taglist表
			for (int i=0;i<tagid.length;i++){
				cmsArticleTagsListDto.setId(tagid[i]);
				cmsArticleTagsListDto.setAid(article.getId());
				cmsArticleTagsListDto.setAddtime(new Date());
				cmsArticleTagsListDto.setTagid(tagid[i]);
				cmsArtTagsListService.insertTagList(cmsArticleTagsListDto);//save
			}
		}
		articleService.save(article);
		addMessage(redirectAttributes, "保存文章'" + StringUtils.abbr(article.getTitle(), 50) + "'成功");
		String categoryId = article.getCategory()!=null?article.getCategory().getId():null;
		if("0".equals(article.getDelFlag())){
			return "redirect:"+ SysProperties.getProperty("adminPath")+"/cms/image/publish?id="+article.getId();
		}else{
			return this.list(newArticle(categoryId), httpServletRequest, httpServletResponse, model);
		}
	}
	@RequiresPermissions("cms:article:edit")
	@RequestMapping(value = "delete")
	public String delete(String id, String categoryId, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,Model model) {
		// 如果没有审核权限，则不允许删除或发布。
		if (!SecurityUtils.getSubject().isPermitted("cms:article:audit")){
			addMessage(redirectAttributes, "你没有删除或发布权限");
		}
		articleService.delete(id, isRe);
		addMessage(redirectAttributes, (isRe != null && isRe ? "发布" : "删除") + "文章成功");
		Article article1=new Article();
		if(article1.getCategory()!=null){
			article1.getCategory().setId(categoryId);
		}else {
			Category category=new Category();
			category.setId(categoryId);
			article1.setCategory(category);
		}
		return this.list(article1,httpServletRequest,httpServletResponse,model);
	}

/**
	 * 文章选择列表
	 */


	@RequiresPermissions("cms:article:view")
	@RequestMapping(value = "selectList")
	public String selectList(HttpServletRequest request, HttpServletResponse response, Model model) {
       /*子页面信息显示*/

		Page<CmsArticleTagsDto> page = new Page<CmsArticleTagsDto>(request, response);
		CmsArticleTagsDto cmsArticleTagsDto=new CmsArticleTagsDto();
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		DataGrid<CmsArticleTagsDto> pageGrid=cmsArtTagsService.queryCmsArtTagsList(pager, cmsArticleTagsDto);
		page.setCount(pageGrid.getTotal());
		page.setList(pageGrid.getRows());
		model.addAttribute("page", page);
		return "modules/cms/tagsSelectList";
	}
/**
	 * 通过编号获取文章标题
	 */


	@RequiresPermissions("cms:article:view")
	@ResponseBody
	@RequestMapping(value = "findByIds")
	public String findByIds(String ids) {
		List<Object[]> list = articleService.findByIds(ids);
		return JsonMapper.nonDefaultMapper().toJson(list);
	}
    private List<String> getTplContent() {
   		List<String> tplList = fileStaticTplService.getNameListByPrefix(siteService.get(Site.getCurrentSiteId()).getPath()+File.separator+"templates");
   		tplList = TplUtils.tplTrim(tplList, Article.DEFAULT_TEMPLATE, "");
   		return tplList;
   	}
	private String getTpl(Article article){
		if(StringUtils.isBlank(article.getCustomContentView())){
			String view = null;
			Category c = article.getCategory();
			boolean goon = true;
			do{
				if(StringUtils.isNotBlank(c.getCustomContentView())){
					view = c.getCustomContentView();
					goon = false;
				}else if(c.getParent() == null || c.getParent().isRoot()){
					goon = false;
				}else{
					c = c.getParent();
				}
			}while(goon);
			return StringUtils.isBlank(view) ? Article.DEFAULT_TEMPLATE : view;
		}else{
			return article.getCustomContentView();
		}
	}
	@RequestMapping(value = "publish")
	public String publish(String id,HttpServletRequest request,HttpServletResponse response, Model model) {
		Article article = articleService.get(id);
		String sitePath=article.getCategory().getSite().getPath();
		String path = File.separator+article.getCategory().getStaticpath()+article.getId()+".html";
		article.setDelFlag(Article.DEL_FLAG_NORMAL);
		article.setCreateBy(UserUtils.getUser());
		article.setCreateDate(new Date());
		article.setLink(path);
		articleService.save(article);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("article", article);
		String staticPath =sitePath+path;
		String templatePath = sitePath+File.separator+"templates";
		FreeMarkers.renderStaticTemplate(map, staticPath, templatePath, getTpl(article)+".ftl");
		return this.list(newArticle(article.getCategory().getId()), request, response, model);
	}
	private Article newArticle(String catetoryId){
		Article article=new Article();
		if(article.getCategory()!=null){
			article.getCategory().setId(catetoryId);
		}else {
			Category category=new Category();
			category.setId(catetoryId);
			article.setCategory(category);
		}
		return article;

	}
}