package com.camelot.cmscenter.domain;

import java.io.Serializable;
import java.util.Date;

public class CmsArticleInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String categoryid;// 分类编号
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
	//articleData 
	private String content;	// 内容
	private String copyfrom;// 来源
	private String relation;// 相关文章
	private String allowComment;// 是否允许评论
	
	private String remarks;	// 备注
	private String createBy;	// 创建者
	private Date createDate;// 创建日期
	private String updateBy;	// 更新者
	private Date updateDate;// 更新日期
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCategoryid() {
		return categoryid;
	}
	public void setCategoryid(String categoryid) {
		this.categoryid = categoryid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
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
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getAcateid() {
		return acateid;
	}
	public void setAcateid(String acateid) {
		this.acateid = acateid;
	}
	public String getCateid() {
		return cateid;
	}
	public void setCateid(String cateid) {
		this.cateid = cateid;
	}
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
	public String getImglist() {
		return imglist;
	}
	public void setImglist(String imglist) {
		this.imglist = imglist;
	}
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCopyfrom() {
		return copyfrom;
	}
	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getAllowComment() {
		return allowComment;
	}
	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
	

}
