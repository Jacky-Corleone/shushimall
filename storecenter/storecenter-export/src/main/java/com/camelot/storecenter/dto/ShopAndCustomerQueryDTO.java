package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.util.List;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class ShopAndCustomerQueryDTO implements Serializable {
    private static final long serialVersionUID = -6998863775950772335L;
    private Long shopId;
    private String shopName;
    private String customerNum; //客服号码
    private String stationId;//商铺站点
    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }


    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

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
    
}

