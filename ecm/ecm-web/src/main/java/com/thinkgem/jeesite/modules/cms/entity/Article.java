/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.thinkgem.jeesite.modules.cms.utils.CmsUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.IdEntity;

/**
 * 文章Entity
 * @author ThinkGem
 * @version 2013-05-15
 */
@Entity
@Table(name = "cms_article")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Indexed @Analyzer(impl = IKAnalyzer.class)
public class Article extends IdEntity<Article> {

    public static final String DEFAULT_TEMPLATE = "defaultFrontView";
	
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;	// 标题
    private String link;	// 外部链接
	private String color;	// 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image;	// 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer weight;	// 权重，越大越靠前
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits;	// 点击数
	private String posid;	// 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
    private String customContentView;	// 自定义内容视图
   	private String viewConfig;	// 视图参数

	private ArticleData articleData;	//文章副表
	private String title2;//副标题
	private String cityid;//所属分站
	private String acateid;//文章类型
	private String cateid;//系统类型
	private String brandid;//品牌编号
	private Integer expcate;
	private String imglist;//文章图片集合
	private String theme;
	private Integer viewnum;//浏览次数
	private Integer addview;//添加浏览次数
	private Integer extend;
	private Integer hotnum;//热门数
	private Integer praisenum;//点赞数
	private Integer addpraise;//添加点赞数点赞数
	private Integer template;//是否启用新模板
	private Integer cstate;
	private Integer isybj;//是否作为样板房文章
	private Integer isset;//是否为图片集
	private Integer isrss;//是否为rss推送
	private Integer ishtml;//是否生成静态页面
	private Integer ishide;//是否隐藏
	private Integer iscomment;//是否推荐
	private Integer iswork;//是否完工
	private Integer outid;//外包文章编号
	public Article() {
		super();
		this.weight = 0;
		this.hits = 0;
		this.posid = "";
	}

	public Article(String id){
		this();
		this.id = id;
	}
	
	public Article(Category category){
		this();
		this.category = category;
	}

	@PrePersist
	public void prePersist(){
		super.prePersist();
		articleData.setId(this.id);
	}
	
	@ManyToOne
	@JoinColumn(name="category_id")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	@IndexedEmbedded
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Length(min=1, max=255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    @Length(min=0, max=255)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

	@Length(min=0, max=50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min=0, max=255)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
        this.image = CmsUtils.formatImageSrcToDb(image);
	}

	@Length(min=0, max=255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min=0, max=255)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	@Length(min=0, max=10)
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

    public String getCustomContentView() {
        return customContentView;
    }

    public void setCustomContentView(String customContentView) {
        this.customContentView = customContentView;
    }

    public String getViewConfig() {
        return viewConfig;
    }

    public void setViewConfig(String viewConfig) {
        this.viewConfig = viewConfig;
    }

	@OneToOne(mappedBy="article",cascade=CascadeType.ALL,optional=false)
	@Valid
	@IndexedEmbedded
	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}

	@Transient
	public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null){
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	@Transient
	public void setPosidList(List<Long> list) {
		posid = ","+StringUtils.join(list, ",")+",";
	}

    @Transient
   	public String getUrl() {
        return CmsUtils.getUrlDynamic(this);
   	}

    @Transient
   	public String getImageSrc() {
        return CmsUtils.formatImageSrcToWeb(this.image);
   	}

	@Length(min=0, max=64)
	public String getTitle2() {
		return title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	@Length(min=0, max=64)
	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	@Length(min=0, max=64)
	public String getAcateid() {
		return acateid;
	}

	public void setAcateid(String acateid) {
		this.acateid = acateid;
	}
	@Length(min=0, max=64)
	public String getCateid() {
		return cateid;
	}

	public void setCateid(String cateid) {
		this.cateid = cateid;
	}

	@Length(min=0, max=64)
	public String getBrandid() {
		return brandid;
	}

	public void setBrandid(String brandid) {
		this.brandid = brandid;
	}

	public Integer getExpcate() {
		return expcate;
	}

	public void setExpcate(Integer expcate) {
		this.expcate = expcate;
	}
	@Length(min=0, max=2000)
	public String getImglist() {
		return imglist;
	}

	public void setImglist(String imglist) {
		this.imglist = imglist;
	}
	@Length(min=0, max=200)
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Integer getViewnum() {
		return viewnum;
	}

	public void setViewnum(Integer viewnum) {
		this.viewnum = viewnum;
	}

	public Integer getAddview() {
		return addview;
	}

	public void setAddview(Integer addview) {
		this.addview = addview;
	}

	public Integer getExtend() {
		return extend;
	}

	public void setExtend(Integer extend) {
		this.extend = extend;
	}

	public Integer getHotnum() {
		return hotnum;
	}

	public void setHotnum(Integer hotnum) {
		this.hotnum = hotnum;
	}

	public Integer getPraisenum() {
		return praisenum;
	}

	public void setPraisenum(Integer praisenum) {
		this.praisenum = praisenum;
	}

	public Integer getAddpraise() {
		return addpraise;
	}

	public void setAddpraise(Integer addpraise) {
		this.addpraise = addpraise;
	}

	public Integer getTemplate() {
		return template;
	}

	public void setTemplate(Integer template) {
		this.template = template;
	}

	public Integer getCstate() {
		return cstate;
	}

	public void setCstate(Integer cstate) {
		this.cstate = cstate;
	}

	public Integer getIsybj() {
		return isybj;
	}

	public void setIsybj(Integer isybj) {
		this.isybj = isybj;
	}

	public Integer getIsset() {
		return isset;
	}

	public void setIsset(Integer isset) {
		this.isset = isset;
	}

	public Integer getIsrss() {
		return isrss;
	}

	public void setIsrss(Integer isrss) {
		this.isrss = isrss;
	}

	public Integer getIshtml() {
		return ishtml;
	}

	public void setIshtml(Integer ishtml) {
		this.ishtml = ishtml;
	}

	public Integer getIshide() {
		return ishide;
	}

	public void setIshide(Integer ishide) {
		this.ishide = ishide;
	}

	public Integer getIscomment() {
		return iscomment;
	}

	public void setIscomment(Integer iscomment) {
		this.iscomment = iscomment;
	}

	public Integer getIswork() {
		return iswork;
	}

	public void setIswork(Integer iswork) {
		this.iswork = iswork;
	}

	public Integer getOutid() {
		return outid;
	}

	public void setOutid(Integer outid) {
		this.outid = outid;
	}

}


