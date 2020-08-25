package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * <p>Description: [运费模板DTO]</p>
 * Created on 2015年10月22日
 * @author  <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ShopFreightTemplateDTO implements Serializable {
    private Long id;//运费模板id，主键

    private String templateName;//模板名称

    private Long provinceId;//省

    private Long cityId;//市

    private Long countyId;//县

    private String addressDetails;//详细地址

    private String deliveryTime;//发货时间

    private Integer postageFree;//是否包邮，1自定义运费，2卖家承担运费

    private String deliveryType;//运送方式，1快递，2EMS，3平邮

    private Integer valuationWay;//计价方式，1件数，2重量，3体积

    private Long shopId;//店铺id

    private Long sellerId;//卖家id

    private Date createTime;//模板创建时间

    private Date updateTime;//模板修改时间

    private String deliveryName;//运送方式文字

    private String valuationWayName;//计价方式文字

    private List<ShopPreferentialWayDTO> shopPreferentialWayList;//优惠方式子表

    private List<ShopDeliveryTypeDTO> shopDeliveryTypeList;//运送方式子表

    private String delState;//是否删除（假删）1，未删，2已删

    public String getDelState() {
        return delState;
    }

    public void setDelState(String delState) {
        this.delState = delState;
    }

    public String getValuationWayName() {
        return valuationWayName;
    }

    public void setValuationWayName(String valuationWayName) {
        this.valuationWayName = valuationWayName;
    }

    public String getDeliveryName() {
        return deliveryName;
    }

    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getPostageFree() {
        return postageFree;
    }

    public void setPostageFree(Integer postageFree) {
        this.postageFree = postageFree;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public Integer getValuationWay() {
        return valuationWay;
    }

    public void setValuationWay(Integer valuationWay) {
        this.valuationWay = valuationWay;
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

    public List<ShopDeliveryTypeDTO> getShopDeliveryTypeList() {
        return shopDeliveryTypeList;
    }

    public void setShopDeliveryTypeList(List<ShopDeliveryTypeDTO> shopDeliveryTypeList) {
        this.shopDeliveryTypeList = shopDeliveryTypeList;
    }

    public List<ShopPreferentialWayDTO> getShopPreferentialWayList() {
        return shopPreferentialWayList;
    }

    public void setShopPreferentialWayList(List<ShopPreferentialWayDTO> shopPreferentialWayList) {
        this.shopPreferentialWayList = shopPreferentialWayList;
    }
}