package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SkuInfo implements Serializable {

	private static final long serialVersionUID = -5919786885017397474L;

	private Long skuId;//SKU id
	private List<SkuPictureDTO> skuPics  = new ArrayList<SkuPictureDTO>();//SKU图片URL
	private String attributes;//销售属性组
	private Long skuType;//sku 类型  sku 类型 1:主sku,2:非主sku
	private List<SellPrice> sellPrices = new ArrayList<SellPrice>();//sku销售阶梯价格
	private Integer skuInventory;//Sku库存
	private String subSkuIds;//组合套装商品关联的skuId，以逗号隔开  例：100000021,10000034
	private Integer subNum;//shu数量
	private String skuProductId;//Sku商品Id
	
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public Long getSkuType() {
		return skuType;
	}

	public void setSkuType(Long skuType) {
		this.skuType = skuType;
	}

	public List<SellPrice> getSellPrices() {
		return sellPrices;
	}

	public void setSellPrices(List<SellPrice> sellPrices) {
		this.sellPrices = sellPrices;
	}

	public Integer getSkuInventory() {
		return skuInventory;
	}

	public void setSkuInventory(Integer skuInventory) {
		this.skuInventory = skuInventory;
	}

	public List<SkuPictureDTO> getSkuPics() {
		return skuPics;
	}

	public void setSkuPics(List<SkuPictureDTO> skuPics) {
		this.skuPics = skuPics;
	}

	public String getSkuProductId() {
		return skuProductId;
	}

	public void setSkuProductId(String skuProductId) {
		this.skuProductId = skuProductId;
	}

	public String getSubSkuIds() {
		return subSkuIds;
	}

	public void setSubSkuIds(String subSkuIds) {
		this.subSkuIds = subSkuIds;
	}

	public Integer getSubNum() {
		return subNum;
	}

	public void setSubNum(Integer subNum) {
		this.subNum = subNum;
	}
	
}
