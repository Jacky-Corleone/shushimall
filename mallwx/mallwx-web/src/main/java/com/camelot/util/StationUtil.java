package com.camelot.util;

import java.util.ArrayList;
import java.util.List;

import com.camelot.mall.shopcart.Shop;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;

public class StationUtil {

	private static ShopCustomerServiceService shopCustomerServiceService;

	static{
		shopCustomerServiceService = SpringApplicationContextHolder.getBean("shopCustomerServiceService");
	}

    /**
	 * 获得单个店铺站点
	 */
	public static String getStationIdByShopId(Long shopId) {
		ShopCustomerServiceDTO shopCustomerServiceDTO = new ShopCustomerServiceDTO();
		shopCustomerServiceDTO.setShopId(shopId);
		List<ShopCustomerServiceDTO> shopCustomerServiceDTOList = shopCustomerServiceService.searchByCondition(shopCustomerServiceDTO);
		if(shopCustomerServiceDTOList!=null && shopCustomerServiceDTOList.size()>0){
			return shopCustomerServiceDTOList.get(0).getStationId();
		}else{
			return "";
		}
	}

	/**
	 * 获得多个店铺站点
	 */
	public static List<String> getStationIdListByShopList(List<Shop> shopList) {
		List<String> stationIdList = new ArrayList<String>();
		for(int i=0;i<shopList.size();i++){
			Long shopId = shopList.get(i).getShopId();
			String stationId = getStationIdByShopId(shopId);
			stationIdList.add(stationId);
		}
		return stationIdList;
	}

}
