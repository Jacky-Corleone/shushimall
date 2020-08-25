package com.camelot.goodscenter.domain;

import java.io.Serializable;
import java.util.Date;

import com.camelot.openplatform.common.PropertyMapping;

/**
 * 平台类目管理实体类
 * @author 周立明
 *
 */
public class ItemCategory implements Serializable{
	 private static final long serialVersionUID = 1L;
	 private Long cid;        //类目id
	 private Long parentCid;  //父级ID
	 private String cName;    //类目名称
	 private Integer lev;     //类目级别
	 private Integer status;  //1:有效;0:删除
	 private Integer hasLeaf; //是否叶子节点
	 private Integer sortNumber;//排序号
	 private Integer homeShow; //是否前台首页展示,1:是;2:否
	 private Date created;     //创建日期
	 private Date modified;    //修改日期
	public Long getParentCid() {
		return parentCid;
	}
	public void setParentCid(Long parentCid) {
		this.parentCid = parentCid;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public Integer getLev() {
		return lev;
	}
	public void setLev(Integer lev) {
		this.lev = lev;
	}
	 public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getHasLeaf() {
		return hasLeaf;
	}
	public void setHasLeaf(Integer hasLeaf) {
		this.hasLeaf = hasLeaf;
	}
	public Integer getSortNumber() {
		return sortNumber;
	}
	public void setSortNumber(Integer sortNumber) {
		this.sortNumber = sortNumber;
	}
	public Integer getHomeShow() {
		return homeShow;
	}
	public void setHomeShow(Integer homeShow) {
		this.homeShow = homeShow;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
}
