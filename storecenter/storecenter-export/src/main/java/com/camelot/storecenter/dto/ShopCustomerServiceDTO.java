package com.camelot.storecenter.dto;

import java.io.Serializable;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class ShopCustomerServiceDTO implements Serializable {
    private static final long serialVersionUID = -525088031235925336L;
    private Long id;//  id
    private Long shopId;//  店铺id
    private String shopName; //店铺名称
    private String stationId;// 站点ID
    private String number;//  客服号码
    private String operator;//  添加号码的人
    private java.util.Date createdDt;//  创建时间
    private java.util.Date lastUpdDt;//  更新时间
    private Integer deletedFlag;//  是否有效，1为有效，0为无效
    private String add1;//  add1
    private String add2;//  add2


    public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Long getShopId() {
        return this.shopId;
    }

    public void setShopId(Long value) {
        this.shopId = value;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String value) {
        this.number = value;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String value) {
        this.operator = value;
    }

    public java.util.Date getCreatedDt() {
        return this.createdDt;
    }

    public void setCreatedDt(java.util.Date value) {
        this.createdDt = value;
    }

    public java.util.Date getLastUpdDt() {
        return this.lastUpdDt;
    }

    public void setLastUpdDt(java.util.Date value) {
        this.lastUpdDt = value;
    }

    public Integer getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(Integer value) {
        this.deletedFlag = value;
    }

    public String getAdd1() {
        return this.add1;
    }

    public void setAdd1(String value) {
        this.add1 = value;
    }

    public String getAdd2() {
        return this.add2;
    }

    public void setAdd2(String value) {
        this.add2 = value;
    }
}

