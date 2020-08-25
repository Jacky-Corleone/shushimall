package com.camelot.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.mall.Constants;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopCustomerServiceService;

public class StationUtil {

	private static ShopCustomerServiceService shopCustomerServiceService;
	private static QqCustomerService qqCustomerService;
	static{
		shopCustomerServiceService = SpringApplicationContextHolder.getBean("shopCustomerServiceService");
		qqCustomerService = SpringApplicationContextHolder.getBean("qqCustomerService");
			
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
	public static List<String> getStationIdListByShopList(List<ShopOutPriceDTO> shopList) {
		List<String> stationIdList = new ArrayList<String>();
		for(int i=0;i<shopList.size();i++){
			Long shopId = shopList.get(i).getShopId();
			String stationId = getStationIdByShopId(shopId);
			stationIdList.add(stationId);
		}
		return stationIdList;
	}
	
	/**
	 * 获得多个店铺的QQ客服
	 */
	public static List<ShopOutPriceDTO> getQQListByShopList(List<ShopOutPriceDTO> shopList) {
		for(int i=0;i<shopList.size();i++){
			List<Long> idlist = new ArrayList<Long>();
			idlist.add(shopList.get(i).getSellerId());
			String stationId = qqCustomerService.getQqCustomerByIds(idlist, Constants.TYPE_SHOP);
			shopList.get(i).setStationId(stationId);
		}
		return shopList;
	}

}
