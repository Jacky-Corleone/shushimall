package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * @author
 * 
 */
public class ShopRenovationDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.String moduleName;//  模块名称
		private BigDecimal price;//  价格
		private java.lang.String pictureUrl;//  图片地址
		private Integer moduleGroup;//  1:基础模块,2:系统模块
		private Integer modultType;//  1：header，2：模板1广告位，4：推荐位，9：模板1广告位Outer，13：模板2轮播图，12：模板2轮播图Outer，11：模板2广告位，10：模板,12广告位Outer
		private Integer status;//  状态，1:有效 ,2:删除 3使用中
		private java.util.Date created;//  创建日期
		private java.util.Date modified;//  修改日期
		private java.lang.Long skuId;//  商品skuid
		private java.lang.String position;//  位置  （从a开始顺延  页面从上至下）
		private java.lang.Long templatesId;//  模板id (1模板一  ，2 模板二 ）
		private String chainUrl; //跳转地址
        private Integer hasPrice;//是否有报价
	
	
	public String getChainUrl() {
			return chainUrl;
		}

		public void setChainUrl(String chainUrl) {
			this.chainUrl = chainUrl;
		}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	public java.lang.String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(java.lang.String value) {
		this.moduleName = value;
	}
	
	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public java.lang.String getPictureUrl() {
		return this.pictureUrl;
	}

	public void setPictureUrl(java.lang.String value) {
		this.pictureUrl = value;
	}
	
	public Integer getModuleGroup() {
		return this.moduleGroup;
	}

	public void setModuleGroup(Integer value) {
		this.moduleGroup = value;
	}
	
	public Integer getModultType() {
		return this.modultType;
	}

	public void setModultType(Integer value) {
		this.modultType = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}
	
	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}
	
	public java.lang.Long getSkuId() {
		return this.skuId;
	}

	public void setSkuId(java.lang.Long value) {
		this.skuId = value;
	}
	
	public java.lang.String getPosition() {
		return this.position;
	}

	public void setPosition(java.lang.String value) {
		this.position = value;
	}

	public java.lang.Long getTemplatesId() {
		return templatesId;
	}

	public void setTemplatesId(java.lang.Long templatesId) {
		this.templatesId = templatesId;
	}

    public Integer getHasPrice() {
        return hasPrice;
    }

    public void setHasPrice(Integer hasPrice) {
        this.hasPrice = hasPrice;
    }
}

