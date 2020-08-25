package com.camelot.mall.dto;

import com.camelot.storecenter.dto.ShopRenovationDTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/6/26.
 */
public class ShopRenovationListDTO implements Serializable{

    private List<ShopRenovationDTO> shopRenovationList;

    public List<ShopRenovationDTO> getShopRenovationList() {
        return shopRenovationList;
    }

    public void setShopRenovationList(List<ShopRenovationDTO> shopRenovationList) {
        this.shopRenovationList = shopRenovationList;
    }
}
