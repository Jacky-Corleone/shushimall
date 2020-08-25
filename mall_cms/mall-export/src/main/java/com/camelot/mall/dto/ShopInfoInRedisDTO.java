package com.camelot.mall.dto;

import java.io.Serializable;

import com.camelot.storecenter.dto.ShopDTO;

/**
 * 卖家认证--》卖家店铺运营申请数据dto，用于存储到redis中
 */
public class ShopInfoInRedisDTO implements Serializable{
	private static final long serialVersionUID = -3330897391484443490L;

	//品类信息id字符串
	private String cids;
	//已选经营品牌id字符串
	private String brandIds;
	
	private ShopDTO shopDTO;

	public String getCids() {
		return cids;
	}

	public void setCids(String cids) {
		this.cids = cids;
	}

	public String getBrandIds() {
		return brandIds;
	}

	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
	}

	public ShopDTO getShopDTO() {
		return shopDTO;
	}

	public void setShopDTO(ShopDTO shopDTO) {
		this.shopDTO = shopDTO;
	}
}