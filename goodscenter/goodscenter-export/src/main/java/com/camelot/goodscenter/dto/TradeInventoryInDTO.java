package com.camelot.goodscenter.dto;

import java.io.Serializable;
import java.util.List;

public class TradeInventoryInDTO implements Serializable{

	/**
	 * <p>Discription:[库存]</p>
	 */
	private static final long serialVersionUID = -3589602010036381361L;
	private Long sellerId;  //商家id
	private Long shopId;//店铺id
	private List<Long> shopIds;//店铺id
	private String itemName;//  商品名称(item)
	private Long itemId;  //商品编码、spu编码
	private Long skuId;//sku编码
	private Integer minInvetory;//最小库存
	private Integer maxInvetory;//最大库存
	private Long productId; //商品货号
	private Integer itemStatus;//  商品状态(item),1:未发布，2：待审核，20：审核驳回，3：待上架，4：在售，5：已下架，6：锁定， 7： 申请解锁
	private Long cid;//三级类目id
	private Long shopCid;//店铺类目
	private Integer hasPrice;//1：有价格 2：暂无报价
	
	private Integer platformId;
	
	
	public Long getShopCid() {
		return shopCid;
	}
	public void setShopCid(Long shopCid) {
		this.shopCid = shopCid;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getCid() {
		return cid;
	}
	public void setCid(Long cid) {
		this.cid = cid;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Integer getMinInvetory() {
		return minInvetory;
	}
	public void setMinInvetory(Integer minInvetory) {
		this.minInvetory = minInvetory;
	}
	public Integer getMaxInvetory() {
		return maxInvetory;
	}
	public void setMaxInvetory(Integer maxInvetory) {
		this.maxInvetory = maxInvetory;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Integer getItemStatus() {
		return itemStatus;
	}
	public void setItemStatus(Integer itemStatus) {
		this.itemStatus = itemStatus;
	}
	public List<Long> getShopIds() {
		return shopIds;
	}
	public void setShopIds(List<Long> shopIds) {
		this.shopIds = shopIds;
	}
	public Integer getPlatformId() {
		return platformId;
	}
	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}
	public Integer getHasPrice() {
		return hasPrice;
	}
	public void setHasPrice(Integer hasPrice) {
		this.hasPrice = hasPrice;
	}
	
}
