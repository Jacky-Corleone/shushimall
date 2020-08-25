package com.camelot.goodscenter.service.utill;

import com.camelot.goodscenter.domain.Item;
import com.camelot.goodscenter.domain.ItemPrice;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.SellPrice;

/**
 * <p>Description: [ItemDTO转换domain类]</p>
 * Created on 2015年2月5日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemDTOToDomainUtil {
	
	/**
	 * <p>Discription:[ItemDTO转换ItemPrice]</p>
	 * Created on 2015年2月5日
	 * @return
	 * @author:[杨阳]
	 */
	public static ItemPrice itemDTO2SellPrice(ItemDTO itemDTO , SellPrice sellPrice){
		ItemPrice itemPrice = new ItemPrice();
		if(null != sellPrice.getSellPrice()){
			itemPrice.setSellPrice(sellPrice.getSellPrice());
		}
		if(null != sellPrice.getMinNum()){
			itemPrice.setMinNum(sellPrice.getMinNum());
		}
		if(null != sellPrice.getMaxNum()){
			itemPrice.setMaxNum(sellPrice.getMaxNum());
		}
		if(null != sellPrice.getAreaId()){
			itemPrice.setAreaId(sellPrice.getAreaId());
		}
		if(null != sellPrice.getAreaName()){
			itemPrice.setAreaName(sellPrice.getAreaName());
		}
		if(null != sellPrice.getStepIndex()){
			itemPrice.setStepIndex(sellPrice.getStepIndex());
		}
		if(null != itemDTO.getSellerId()){
			itemPrice.setSellerId(itemDTO.getSellerId());
		}
		if(null != itemDTO.getCreated()){
			itemPrice.setCreated(itemDTO.getCreated());
		}
		if(null != itemDTO.getShopId()){
			itemPrice.setShopId(itemDTO.getShopId());
		}
		return itemPrice;
	}
	
	/**
	 * <p>Discription:[ItemDTO转换Item]</p>
	 * Created on 2015年2月6日
	 * @param itemDTO
	 * @return
	 * @author:[杨阳]
	 */
	public static Item itemDTO2Item(ItemDTO itemDTO){
		Item item = new Item();
		if(null != itemDTO.getItemName()){
			item.setItemName(itemDTO.getItemName());
		}
		if(null != itemDTO.getCid()){
			item.setCid(itemDTO.getCid());
		}
		if(null != itemDTO.getSellerId()){
			item.setSellerId(itemDTO.getSellerId());
		}
		if(null != itemDTO.getItemStatus()){
			item.setItemStatus(itemDTO.getItemStatus());
		}
		if(null != itemDTO.getAttributesStr()){
			item.setAttributes(itemDTO.getAttributesStr());
		}
		if(null != itemDTO.getAttrSaleStr()){
			item.setAttrSale(itemDTO.getAttrSaleStr());
		}
		if(null != itemDTO.getCreated()){
			item.setCreated(itemDTO.getCreated());
		}
		if(null != itemDTO.getModified()){
			item.setModified(itemDTO.getModified());
		}
		if(null != itemDTO.getShopId()){
			item.setShopId(itemDTO.getShopId());
		}
		if(null != itemDTO.getBrand()){
			item.setBrand(itemDTO.getBrand());
		}
		if(null != itemDTO.getMarketPrice()){
			item.setMarketPrice(itemDTO.getMarketPrice());
		}
		if(null != itemDTO.getMarketPrice2()){
			item.setMarketPrice2(itemDTO.getMarketPrice2());
		}
		if(null != itemDTO.getInventory()){
			item.setInventory(itemDTO.getInventory());
		}
		if(null != itemDTO.getWeight()){
			item.setWeight(itemDTO.getWeight());
		}
		if(null != itemDTO.getProductId()){
			item.setProductId(itemDTO.getProductId());
		}
		if(null != itemDTO.getDescribeUrl()){
			item.setDescribeUrl(itemDTO.getDescribeUrl());
		}
		if(null != itemDTO.getPackingList()){
			item.setPackingList(itemDTO.getPackingList());
		}
		if(null != itemDTO.getAfterService()){
			item.setAfterService(itemDTO.getAfterService());
		}
		if(null != itemDTO.getAd()){
			item.setAd(itemDTO.getAd());
		}
		if(null != itemDTO.getTimingListing()){
			item.setTimingListing(itemDTO.getTimingListing());
		}
		if(null != itemDTO.getListtingTime()){
			item.setListtingTime(itemDTO.getListtingTime());
		}
		if(null != itemDTO.getDelistingTime()){
			item.setDelistingTime(itemDTO.getDelistingTime());
		}
		if(null != itemDTO.getOperator()){
			item.setOperator(itemDTO.getOperator());
		}
		if(null != itemDTO.getStatusChangeReason()){
			item.setStatusChangeReason(itemDTO.getStatusChangeReason());
		}
		if(null != itemDTO.getShopCid()){
			item.setShopCid(itemDTO.getShopCid());
		}
		if(null != itemDTO.getGuidePrice()){
			item.setGuidePrice(itemDTO.getGuidePrice());
		}
		if(null != itemDTO.getOrigin()){
			item.setOrigin(itemDTO.getOrigin());
		}
		if(null != itemDTO.getAddSource()){
			item.setAddSource(itemDTO.getAddSource());
		}
		if(null != itemDTO.getPlatLinkStatus()){
			item.setPlatLinkStatus(itemDTO.getPlatLinkStatus());
		}
		if(null != itemDTO.getHasPrice()){
			item.setHasPrice(itemDTO.getHasPrice());
		}
		if(null != itemDTO.getPlstItemId()){
			item.setPlstItemId(itemDTO.getPlstItemId());
		}
		if(null != itemDTO.getKeywords()){
			item.setKeywords(itemDTO.getKeywords());
		}
		if(null != itemDTO.getWeightUnit()){
			item.setWeightUnit(itemDTO.getWeightUnit());
		}
		if (null != itemDTO.getAuthentication()) {
			item.setAuthentication(itemDTO.getAuthentication());
		}
		if (null != itemDTO.getPlatformId()) {
			item.setPlatformId(itemDTO.getPlatformId());
		}
		if (null != itemDTO.getShopFreightTemplateId()) {
			item.setShopFreightTemplateId(itemDTO.getShopFreightTemplateId());
		}
		if (null != itemDTO.getVolume()) {
			item.setVolume(itemDTO.getVolume());
		}
		if (null != itemDTO.getSearched()) {
			item.setSearched(itemDTO.getSearched());
		}
		if (null != itemDTO.getPlacedTop()) {
			item.setPlacedTop(itemDTO.getPlacedTop());
		}
		if (null != itemDTO.getHousetype()) {
			item.setHousetype(itemDTO.getHousetype());
		}
		if (null != itemDTO.getSpecification()) {
			item.setSpecification(itemDTO.getSpecification());
		}
		return item;
	}
}
