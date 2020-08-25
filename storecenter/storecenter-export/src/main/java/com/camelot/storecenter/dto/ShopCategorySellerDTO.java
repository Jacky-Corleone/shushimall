package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
 * <p>Description: [店铺分类]</p>
 * Created on 2015年3月5日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class ShopCategorySellerDTO implements Serializable{

	private static final long serialVersionUID = 4513249996368718783L;
	private long cid;   //类目id
	private long parentCid;  //类目父id
	private long sellerId;   //卖家id
	private long shopId;    //店铺id
	private String cname;    //类目名称
	private Integer status;   //状态，1、有效，2、删除
	private Integer lev;        //类目级别,1:一级类目;2:二级类目;3:三级类目,目前只支持二级类目
	private Integer hasLeaf;     //是否叶子节点 1叶子节点   0非叶子节点
	private Integer sortNumber;    //排序号,排序号越小越靠前
	private Integer homeShow;   //是否前台首页展示,1:是;2:否
	private Integer expand;    //标记分类是否在首页展开，1、是，2、否
	private Date created;    //创建时间
	private Date modified;   //修改时间

	//子集类目
	private List<ShopCategorySellerDTO> childShopCategory = new ArrayList<ShopCategorySellerDTO>();
	
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public long getParentCid() {
		return parentCid;
	}
	public void setParentCid(long parentCid) {
		this.parentCid = parentCid;
	}
	public long getSellerId() {
		return sellerId;
	}
	public void setSellerId(long sellerId) {
		this.sellerId = sellerId;
	}
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getLev() {
		return lev;
	}
	public void setLev(Integer lev) {
		this.lev = lev;
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
	public Integer getExpand() {
		return expand;
	}
	public void setExpand(Integer expand) {
		this.expand = expand;
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
	public List<ShopCategorySellerDTO> getChildShopCategory() {
		return childShopCategory;
	}
	public void setChildShopCategory(List<ShopCategorySellerDTO> childShopCategory) {
		this.childShopCategory = childShopCategory;
	}
}
