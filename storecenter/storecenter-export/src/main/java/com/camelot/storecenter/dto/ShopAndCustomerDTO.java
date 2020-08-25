package com.camelot.storecenter.dto;

import java.io.Serializable;
import java.util.List;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class ShopAndCustomerDTO implements Serializable {
    private static final long serialVersionUID = -6998863775950772335L;
    private Long shopId;
    private String shopName;
    private List<ShopCustomerServiceDTO> customerList;

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

    public List<ShopCustomerServiceDTO> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<ShopCustomerServiceDTO> customerList) {
        this.customerList = customerList;
    }
}

