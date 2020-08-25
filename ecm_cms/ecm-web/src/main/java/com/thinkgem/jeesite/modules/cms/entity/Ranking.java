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
 * 排行榜子信息
 * @author admin
 *
 */
@Entity
@Table(name = "cms_ranking")
@DynamicInsert @DynamicUpdate
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ranking extends IdEntity<Ranking>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3776161904692050217L;
	
	private String cid;//排行榜ID
	
    private Integer type;//类型  网络排行榜:0  线下排行榜:1
    
    private String brand;//品牌
    
    private String brandintro;//品牌描述
    
    private String position;//品牌位置方向
    
    private Integer trend;//趋势 无变化:0  上升:1  下降:2
    
    private Integer viewnum;//热度
    
    private Integer sort;//排序
    
    private String occupancy;//市场占有率
    
    private String brandCountry;//品牌国家
    
    private String hotType;//热卖型号
    
    private String score;//评分
    
    private Date addtime;//添加时间
    
    @Length(min=0, max=64)
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	@Length(min=0, max=100)
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Length(min=0, max=200)
	public String getBrandintro() {
		return brandintro;
	}
	public void setBrandintro(String brandintro) {
		this.brandintro = brandintro;
	}
	
	@Length(min=0, max=50)
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public Integer getTrend() {
		return trend;
	}
	public void setTrend(Integer trend) {
		this.trend = trend;
	}
	public Integer getViewnum() {
		return viewnum;
	}
	public void setViewnum(Integer viewnum) {
		this.viewnum = viewnum;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@Length(min=1, max=11)
	public String getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(String occupancy) {
		this.occupancy = occupancy;
	}
	@Length(min=0, max=100)
	public String getBrandCountry() {
		return brandCountry;
	}
	public void setBrandCountry(String brandCountry) {
		this.brandCountry = brandCountry;
	}
	@Length(min=0, max=200)
	public String getHotType() {
		return hotType;
	}
	public void setHotType(String hotType) {
		this.hotType = hotType;
	}
	@Length(min=0, max=100)
	public String getScore() {	
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	
	public static final Integer NETWORK_RANKINGLIST=0;
	public static final Integer OFFLINE_RANKINGLIST=1;
    
	  
}
