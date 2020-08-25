package com.camelot.aftersale.dto.returngoods;

import java.io.Serializable;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class TradeReturnPicDTO implements Serializable {
    private static final long serialVersionUID = 513738302947859885L;
    private Long id;//  id
    private String picUrl;//  picUrl
    private Long returnGoodsId;//  returnGoodsId
    private java.util.Date createdDt;//  createdDt
    private String createdBy;//  createdBy
    private java.util.Date lastUpdDt;//  lastUpdDt
    private String lastUpdBy;//  lastUpdBy
    private String deletedFlag;//  deletedFlag


    public Long getId() {
        return this.id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String value) {
        this.picUrl = value;
    }

    public Long getReturnGoodsId() {
        return this.returnGoodsId;
    }

    public void setReturnGoodsId(Long value) {
        this.returnGoodsId = value;
    }

    public java.util.Date getCreatedDt() {
        return this.createdDt;
    }

    public void setCreatedDt(java.util.Date value) {
        this.createdDt = value;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String value) {
        this.createdBy = value;
    }

    public java.util.Date getLastUpdDt() {
        return this.lastUpdDt;
    }

    public void setLastUpdDt(java.util.Date value) {
        this.lastUpdDt = value;
    }

    public String getLastUpdBy() {
        return this.lastUpdBy;
    }

    public void setLastUpdBy(String value) {
        this.lastUpdBy = value;
    }

    public String getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(String value) {
        this.deletedFlag = value;
    }
}

