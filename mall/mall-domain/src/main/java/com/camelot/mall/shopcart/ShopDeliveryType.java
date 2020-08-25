package com.camelot.mall.shopcart;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShopDeliveryType implements Serializable {
	private static final long serialVersionUID = 4839202847216483632L;

	private Long id;// 运送方式id，主键

	private Long templateId;// 模板id

	private BigDecimal firstPart;// 首件/首重量/首体积

	private BigDecimal firstPrice;// 首费

	private BigDecimal continues;// 续件/续重量/续体积

	private BigDecimal continuePrice;// 续费

	private Integer deliveryType;// 运送方式，1快递，2EMS，3平邮

	private BigDecimal groupFreight; // 店铺中某组商品的运费

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

	public BigDecimal getContinues() {
		return continues;
	}

	public void setContinues(BigDecimal continues) {
		this.continues = continues;
	}

	public BigDecimal getContinuePrice() {
		return continuePrice;
	}

	public void setContinuePrice(BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

	public Integer getDeliveryType() {
		return deliveryType;
	}

	public void setDeliveryType(Integer deliveryType) {
		this.deliveryType = deliveryType;
	}

	public BigDecimal getGroupFreight() {
		return groupFreight;
	}

	public void setGroupFreight(BigDecimal groupFreight) {
		this.groupFreight = groupFreight;
	}

}
