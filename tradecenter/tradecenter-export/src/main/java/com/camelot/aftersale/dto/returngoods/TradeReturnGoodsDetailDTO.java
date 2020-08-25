package com.camelot.aftersale.dto.returngoods;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>信息VO（展示）</p>
 */
public class TradeReturnGoodsDetailDTO implements Serializable {
    private static final long serialVersionUID = 6064513922976779240L;
    private Long id;//  id
    private Long returnGoodsId;//  退货单ID
    private java.lang.Long skuId;//  SKU
    private java.lang.String skuName;//  SKU名称
    private Long goodsId;//  商品ID
    private String goodsName;//  商品名称
    private String goodsPicUrl; //商品图片URL
    private BigDecimal payPrice; //单价
    private BigDecimal returnAmount;//  退款金额
    private Integer rerurnCount;//  退货数量
    private Integer returnIntegral;// 退还积分数量
    private String remark;//  备注
    private java.util.Date createdDt;//  创建时间
    private String createdBy;//  createdBy
    private String add1;//  add1
    private String add2;//  add2
    private String add3;//  add3
    private Long[] returnGoodsIds;
    public Long[] getReturnGoodsIds() {
        return returnGoodsIds;
    }

    public void setReturnGoodsIds(Long[] returnGoodsIds) {
        this.returnGoodsIds = returnGoodsIds;
    }



    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getGoodsPicUrl() {
        return goodsPicUrl;
    }

    public void setGoodsPicUrl(String goodsPicUrl) {
        this.goodsPicUrl = goodsPicUrl;
    }

    public BigDecimal getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(BigDecimal payPrice) {
        this.payPrice = payPrice;
    }

    public void setReturnAmount(BigDecimal returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getReturnAmount() {
        return returnAmount;
    }

    public Long getSkuId() {
        return skuId;

    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Long getReturnGoodsId() {
        return this.returnGoodsId;
    }

    public void setReturnGoodsId(Long value) {
        this.returnGoodsId = value;
    }

    public Long getGoodsId() {
        return this.goodsId;
    }

    public void setGoodsId(Long value) {
        this.goodsId = value;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public void setGoodsName(String value) {
        this.goodsName = value;
    }

    public Integer getRerurnCount() {
        return this.rerurnCount;
    }

    public void setRerurnCount(Integer value) {
        this.rerurnCount = value;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String value) {
        this.remark = value;
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

    public String getAdd3() {
        return this.add3;
    }

    public void setAdd3(String value) {
        this.add3 = value;
    }

	public Integer getReturnIntegral() {
		return returnIntegral;
	}

	public void setReturnIntegral(Integer returnIntegral) {
		this.returnIntegral = returnIntegral;
	}
    
}

