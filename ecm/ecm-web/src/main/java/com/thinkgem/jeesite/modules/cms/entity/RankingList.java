package com.thinkgem.jeesite.modules.cms.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;
import org.hibernate.validator.constraints.Length;

import com.drew.lang.annotations.NotNull;
import com.thinkgem.jeesite.common.persistence.IdEntity;

/**
 * 排行榜列表
 * @author admin
 *
 */
@Entity
@Table(name = "cms_ranking_list")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RankingList extends IdEntity<RankingList>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9118965063161943035L;
	
	
	
	private String title;//标题
	  
	  private String keywords;//关键字
	  
	  private String intro;//描述
	  
	  private String cname;//排行榜名称
	  
	  private Integer recommend;//是否推荐  是：1 否：0
	  
	  private String cateid;//系统类型
	  
	  private String adminid;//添加人
	  
	  private Integer ishtml;//是否生成静态页面
	  
	  private Integer sort;//排序
	  
	  private Date addtime;//添加时间
	  
	  private Integer release;//是否发布  是：1 否：0
	  
	  private Date releasetime;//发布时间
	  
	  private String link;//静态化请求地址
	  
	  public RankingList() {
			super();
		}
		
		public RankingList(String id){
			this();
			this.id = id;
		}
		
	@Length(min=0, max=100)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=100)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	@Length(min=0, max=100)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	@Length(min=0, max=50)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}
	public String getCateid() {
		return cateid;
	}

	public void setCateid(String cateid) {
		this.cateid = cateid;
	}
	
	@Length(min=0, max=64)
	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	public String getAdminid() {
		return adminid;
	}
	
	public void setAdminid(String adminid) {
		this.adminid = adminid;
	}

	public Integer getIshtml() {
		return ishtml;
	}

	public void setIshtml(Integer ishtml) {
		this.ishtml = ishtml;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Integer getRelease() {
		return release;
	}

	public void setRelease(Integer release) {
		this.release = release;
	}

	public Date getReleasetime() {
		return releasetime;
	}

	public void setReleasetime(Date releasetime) {
		this.releasetime = releasetime;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
}
