package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>Description: [优惠方式DTO]</p>
 * Created on 2015年10月22日
 * @author  <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopDeliveryTypeDTO implements Serializable{
    private Long id;//运送方式id，主键

    private Long templateId;//模板id

    private BigDecimal firstPart;//首件/首重量/首体积

    private BigDecimal firstPrice;//首费

    private BigDecimal continues;//续件/续重量/续体积

    private BigDecimal continuePrice;//续费

    private String deliveryTo;//运送至

    private Integer deliveryType;//运送方式，1快递，2EMS，3平邮

    private Long shopId;//店铺id

    private Long sellerId;//卖家id

    private Date createTime;//运送方式创建时间

    private String deliveryAddress;//运送地址

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public BigDecimal getContinues() {
        return continues;
    }

    public void setContinues(BigDecimal continues) {
        this.continues = continues;
    }

    private Date updateTime;//运送方式时间

    private String delState;//是否删除（假删）1，未删，2已删

    public String getDelState() {
        return delState;
    }

    public void setDelState(String delState) {
        this.delState = delState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public BigDecimal getFirstPart() {
        return firstPart;
    }

    public void setFirstPart(BigDecimal firstPart) {
        this.firstPart = firstPart;
    }

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }


    public BigDecimal getContinuePrice() {
        return continuePrice;
    }

    public void setContinuePrice(BigDecimal continuePrice) {
        this.continuePrice = continuePrice;
    }

    public String getDeliveryTo() {
        return deliveryTo;
    }

    public void setDeliveryTo(String deliveryTo) {
        this.deliveryTo = deliveryTo;
    }

    public Integer getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(Integer deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}