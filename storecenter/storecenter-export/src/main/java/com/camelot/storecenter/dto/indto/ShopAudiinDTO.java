package com.camelot.storecenter.dto.indto;

import java.io.Serializable;
import java.util.Date;

public class ShopAudiinDTO implements Serializable{

	/**
	 * <p>Discription:[店铺审核信息查询入参]</p>
	 */
	private static final long serialVersionUID = -2672785162993421505L;
	
	
	/**
	 * 店铺申请 开始时间
	 */
	private Date createdstr;
	/**
	 * 店铺申请  结束时间
	 */
	private Date createdend;
	/**
	 * 店铺开通开始时间
	 */
	private Date passTimestr;
	/**
	 * 店铺开通结束时间
	 */
	private Date passTimeend;
	
	/**
	 * 卖家ID  商家ID
	 */
	private Long sellerId;
	
	/**
	 * 店铺经营类目ID
	 */
	private Long cid;
	
	/**
	 * 店铺名称
	 */
	private String shopName;
	/**
	 * 店铺建新状态;1是申请，2是通过，3是驳回，4是平台关闭，5是开通
	 */
	private Integer status ;
	
	/**
	 * 店铺ID组
	 */
	private Long[] shopIds;
	
	private String childCategorys; //子集的集合 以"，"连接
	
	
	public Long[] getShopIds() {
		return shopIds;
	}
	public void setShopIds(Long[] shopIds) {
		this.shopIds = shopIds;
	}
	public Date getPassTimestr() {
		return passTimestr;
	}
	public void setPassTimestr(Date passTimestr) {
		this.passTimestr = passTimestr;
	}
	public Date getPassTimeend() {
		return passTimeend;
	}
	public void setPassTimeend(Date passTimeend) {
		this.passTimeend = passTimeend;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreatedstr() {
		return createdstr;
	}
	public void setCreatedstr(Date createdstr) {
		this.createdstr = createdstr;
	}
	public Date getCreatedend() {
		return createdend;
	}
	public void setCreatedend(Date createdend) {
		this.createdend = createdend;
	}
	public String getChildCategorys() {
		return childCategorys;
	}
	public void setChildCategorys(String childCategorys) {
		this.childCategorys = childCategorys;
	}
	
	
	
}
