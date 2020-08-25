package com.camelot.mall.home;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.service.ShopFavouriteExportService;
import com.camelot.storecenter.service.ShopFreightTemplateService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月14日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service
public class FavouriteServiceImpl implements FavouriteService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private ShopFavouriteExportService shopFavouriteService;
	@Resource
	private ItemFavouriteExportService itemFavouriteService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private UserExportService userService;
	@Resource
    private PromotionService promotionService;
    @Resource 
    private ShopFreightTemplateService shopFreightTemplateService;
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年3月14日
	 * @param pager
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<JSONObject> shops(Pager pager, ShopFavouriteDTO favourite) {
		DataGrid<JSONObject> dg = new DataGrid<JSONObject>();
		DataGrid<ShopFavouriteDTO> dgShops = this.shopFavouriteService.datagrid(pager, favourite);
		
		LOG.debug("DUBBO 查询收藏店铺："+JSON.toJSONString(dgShops));
		
		List<JSONObject> shops = new ArrayList<JSONObject>();
		
		for( ShopFavouriteDTO sf: dgShops.getRows() ){
			
			Integer sellerId = sf.getSellerId();
			UserDTO user = this.userService.queryUserById(sellerId);
			sf.setSellerName(user.getUname());
			
			JSONObject shop = JSON.parseObject(JSON.toJSONString(sf));
			
			ItemQueryInDTO item = new ItemQueryInDTO();
			item.setShopIds(new Long[]{Long.valueOf(sf.getShopId())});
			item.setItemStatus(4);
			
			Pager page = new Pager(1, 5);
			DataGrid<ItemQueryOutDTO> items = this.itemService.queryItemList(item, page);
			shop.put("items", items.getRows());
			
			shops.add(shop);
		}
		
		dg.setTotal(dgShops.getTotal());
		dg.setRows(shops);
		
		return dg;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<JSONObject> products(Pager pager, ItemFavouriteDTO favourite,String regionId) {
		DataGrid<JSONObject> rlt = new DataGrid<JSONObject>();
		
		DataGrid<ItemFavouriteDTO> dg = this.itemFavouriteService.datagrid(pager, favourite);
		List<JSONObject> rows = new ArrayList<JSONObject>();
		ItemShopCartDTO skuDto = new ItemShopCartDTO();
		for( ItemFavouriteDTO dto: dg.getRows() ){
			
			JSONObject row = JSONObject.parseObject(JSON.toJSONString(dto));
			
			skuDto.setAreaCode(regionId);
			skuDto.setShopId(Long.valueOf(dto.getShopId()));
			skuDto.setItemId(Long.valueOf(dto.getItemId()));
			skuDto.setSkuId(Long.valueOf(dto.getSkuId()));
			
			ExecuteResult<ItemShopCartDTO> er = this.itemService.getSkuShopCart(skuDto);
			ItemShopCartDTO iscd = er.getResult();
			
			if( er.isSuccess() && iscd != null ){
				row.put("picUrl", iscd.getSkuPicUrl());
				row.put("itemName", iscd.getItemName());
				row.put("hasPrice", iscd.isHasPrice());
				row.put("price", iscd.getSkuPrice());
			}
			
			rows.add(row);
		}
		
		rlt.setTotal(dg.getTotal());
		rlt.setRows(rows);
		
		return rlt;
	}
	
	@Override
	public Map<String,Object> productsMarketing(Pager pager,
			ItemFavouriteDTO favourite, String regionId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		DataGrid<JSONObject> rlt = new DataGrid<JSONObject>();
		
		DataGrid<ItemFavouriteDTO> dg = this.itemFavouriteService.datagrid(pager, favourite);
		List<JSONObject> rows = new ArrayList<JSONObject>();
		ItemShopCartDTO skuDto = new ItemShopCartDTO();
		//满减
		Map<Long, Long> fullMap = new HashMap<Long, Long>();
		//促销 直降
		Map<String, BigDecimal> downMap = new HashMap<String, BigDecimal>();

		for( ItemFavouriteDTO dto: dg.getRows() ){
			//满减
			fullMap.put(Long.valueOf(dto.getItemId()),Long.valueOf(dto.getShopId()));
			
			JSONObject row = JSONObject.parseObject(JSON.toJSONString(dto));
			//查询商品信息开始
			ExecuteResult<ItemDTO> itemResult = itemService.getItemById(Long.valueOf(dto.getItemId()));
			
			if(null != itemResult && null != itemResult.getResult()){
				//添加该商品的状态
				row.put("statue", itemResult.getResult().getItemStatus());
			}
			//查询商品信息结束
			
			skuDto.setAreaCode(regionId);
			skuDto.setShopId(Long.valueOf(dto.getShopId()));
			skuDto.setItemId(Long.valueOf(dto.getItemId()));
			skuDto.setSkuId(Long.valueOf(dto.getSkuId()));
			
			ExecuteResult<ItemShopCartDTO> er = this.itemService.getSkuShopCart(skuDto);
			ItemShopCartDTO iscd = er.getResult();
			
			//促销
			PromotionInDTO promotInDTO = new PromotionInDTO();
			promotInDTO.setItemId(iscd.getItemId());
			promotInDTO.setShopId(Long.valueOf(iscd.getShopId()));
			promotInDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
			promotInDTO.setType(1);//查询促销
			ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(promotInDTO, null);
			if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
				List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
				for (PromotionOutDTO pro : listPromotionOutDTO) {
					PromotionMarkdown markdown = pro.getPromotionMarkdown();
					//计算促销价格
//					BigDecimal disPrice = iscd.getSkuPrice().multiply(markdown.getDiscountPercent()).setScale(2, BigDecimal.ROUND_CEILING);
					BigDecimal disPrice = new BigDecimal(0);
	 				//计算促销价格
	 				if (markdown.getMarkdownType() == 1) {//百分比满减
	 					disPrice = iscd.getSkuPrice().multiply(markdown.getDiscountPercent()).setScale(2, BigDecimal.ROUND_CEILING);
					}else {//直降固定金额
						disPrice = iscd.getSkuPrice().subtract(markdown.getPromotionPrice());
					}
					//设置SKU价格 为促销价
					downMap.put(String.valueOf(iscd.getSkuId()), disPrice);
				}
			}
			
			if( er.isSuccess() && iscd != null ){
				row.put("picUrl", iscd.getSkuPicUrl());
				row.put("itemName", iscd.getItemName());
				row.put("hasPrice", iscd.isHasPrice());
				row.put("price", iscd.getSkuPrice());
			}
			
			rows.add(row);
		}
		//商品满减信息
		JSONArray fullReduction = fullReduction(fullMap);
		
		rlt.setTotal(dg.getTotal());
		rlt.setRows(rows);
		
		map.put("rlt", rlt);
		map.put("fullReduction", fullReduction);
		map.put("downMap", downMap);
		return map;
	}
	
	
	/**
	 * 
	 * <p>Discription:[查询商品满减信息]</p>
	 * Created on 2015年12月4日
	 * @param fullMap
	 * @return
	 * @author: 尹归晋
	 */
	public JSONArray fullReduction(Map<Long, Long> fullMap){
		//满减
		JSONArray fulljsonArray = new JSONArray();
		for (Map.Entry<Long, Long> entry : fullMap.entrySet()) {
			//满减数据json
			JSONObject jsonObj = new JSONObject();
			//查询商品满减
			PromotionInDTO promotInDTO = new PromotionInDTO();
			promotInDTO.setItemId(entry.getKey());
			promotInDTO.setShopId(entry.getValue());
			promotInDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
			promotInDTO.setType(2);//查询满减
			ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(promotInDTO, null);
			if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
				JSONArray fullJsonArray = new JSONArray();
				List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
	 			for (PromotionOutDTO pro : listPromotionOutDTO) {
	 				JSONObject jo = new JSONObject();
	 				//名称
	 				String activityName = pro.getPromotionInfo().getActivityName();
	 				jo.put("activityName", activityName);
	 				SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
	 				Date startTimeDate = pro.getPromotionInfo().getStartTime();
	 				String startTime = format.format(startTimeDate);
	 				jo.put("startTime", startTime);
	 				Date endTimeDate = pro.getPromotionInfo().getEndTime();
	 				String endTime = format.format(endTimeDate);
	 				jo.put("endTime", endTime);
	 				
	 				PromotionFullReduction full = pro.getPromotionFullReduction();
	 				if(full!=null){
 						BigDecimal meetPrice = full.getMeetPrice();//满值
 						jo.put("meetPrice", meetPrice);
 						BigDecimal discountPrice = full.getDiscountPrice();//减值
 						jo.put("discountPrice", discountPrice);
 						fullJsonArray.add(jo);
 					}
				}
	 			jsonObj.put("shopId", entry.getValue());
	 			jsonObj.put("itemId", entry.getKey());
	 			jsonObj.put("promotionFullReduction", fullJsonArray);
			}
			//载入满减信息
			fulljsonArray.add(jsonObj);
		}
		return fulljsonArray;
	}
}
