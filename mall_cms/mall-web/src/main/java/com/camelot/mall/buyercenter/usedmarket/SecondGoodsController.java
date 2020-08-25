package com.camelot.mall.buyercenter.usedmarket;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.ItemPictureDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.indto.ItemOldInDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemOldExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/secondGoods")
public class SecondGoodsController {

	@Resource
	private ItemOldExportService itemOldExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private TradeReturnExportService tradeReturnExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private UserExportService userExportService;

	/**
	 * 初始化：二手商品发布页面
	 */
	@RequestMapping("/initRelease")
	public String initRelease(Long id, String orderId, Long itemId, String releaseState, Model model, HttpServletRequest request) {
		//未被审核用户无法发布闲置
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		Integer uStatus = userDTO.getUserstatus();
		if( uStatus < 3 ){
			return "forward:/usedMarket?ifRelease=0";
		}
		//平台一级类目
		DataGrid<ItemCategoryDTO> itemCategoryGrid = itemCategoryService.queryItemCategoryList(0L);
		model.addAttribute("firstAssortmentList",itemCategoryGrid.getRows());
		//获取省份
		model.addAttribute("addressList", addressBaseService.queryAddressBase("0"));
		//类目ID
		Long cid = 0L;
		ItemOldDTO itemOldDTO = new ItemOldDTO();
		/* 一键转卖 */
		if(!StringUtils.isBlank(orderId)){
			//订单信息
			ExecuteResult<TradeOrdersDTO> tradeOrdersResult = tradeOrderExportService.getOrderById(orderId);
			TradeOrdersDTO tradeOrdersDTO = tradeOrdersResult.getResult();
			model.addAttribute("tradeOrdersDTO", tradeOrdersDTO);

			//选中商品信息:商品属性/商品名称/商品图片
			for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrdersDTO.getItems()){
				if(itemId.longValue() == tradeOrderItemsDTO.getItemId().longValue()){
					//商品原价
					itemOldDTO.setPriceOld(tradeOrderItemsDTO.getPayPrice());
					//商品名称
					ExecuteResult<ItemDTO> itemResult = itemExportService.getItemById(tradeOrderItemsDTO.getItemId());
					ItemDTO itemDTO = itemResult.getResult();
					itemOldDTO.setItemName(itemDTO.getItemName());
					//商品图片
					String[] picUrls = itemDTO.getPicUrls();
					for(int i=0; i < picUrls.length; i++){
						model.addAttribute("pictureUrl0"+(i+1), picUrls[i]);
					}
					break;
				}
			}
		}
		/* 发布：新增、编辑 */
		if(id!=null && id.longValue()>0){
			ExecuteResult<ItemOldOutDTO> itemOldOutResult = itemOldExportService.getItemOld(id);
			ItemOldOutDTO itemOldOutDTO = itemOldOutResult.getResult();
			itemOldDTO = itemOldOutDTO.getItemOldDTO();
			if(itemOldDTO != null){
				//商品图片
				for(ItemPictureDTO itemPictureDTO : itemOldOutDTO.getItemPictureDTO()){
					String sortNumber = itemPictureDTO.getSortNumber();
					String pictureUrl = itemPictureDTO.getPictureUrl();
					if("1".equals(sortNumber)){
						model.addAttribute("pictureUrl01", pictureUrl);
					}else if("2".equals(sortNumber)){
						model.addAttribute("pictureUrl02", pictureUrl);
					}else if("3".equals(sortNumber)){
						model.addAttribute("pictureUrl03", pictureUrl);
					}else if("4".equals(sortNumber)){
						model.addAttribute("pictureUrl04", pictureUrl);
					}else if("5".equals(sortNumber)){
						model.addAttribute("pictureUrl05", pictureUrl);
					} else {
						model.addAttribute("pictureUrl01", pictureUrl);
					}
				}
			}
			if(itemOldDTO!=null && itemOldDTO.getCid()!=null){
				cid = itemOldDTO.getCid();
			}
		}
		model.addAttribute("itemOldDTO", itemOldDTO);
		//商品类目
		ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascadeResult = itemCategoryService.queryParentCategoryList(cid);
		List<ItemCatCascadeDTO> itemCatCascadeList = itemCatCascadeResult.getResult();
		if(itemCatCascadeList!=null && itemCatCascadeList.size()>0){
			ItemCatCascadeDTO firstAssortment = itemCatCascadeList.get(0);
			model.addAttribute("firstAssortment", firstAssortment.getCid());
			model.addAttribute("firstAssortmentName", firstAssortment.getCname());
			List<ItemCatCascadeDTO> secondAssortmentList = firstAssortment.getChildCats();
			if(secondAssortmentList!=null && secondAssortmentList.size()>0){
				ItemCatCascadeDTO secondAssortment = secondAssortmentList.get(0);
				model.addAttribute("secondAssortment", secondAssortment.getCid());
				model.addAttribute("secondAssortmentName", secondAssortment.getCname());
			}
		}
		model.addAttribute("releaseState", releaseState);
		model.addAttribute("pageState", "release");
		return "/buyercenter/usedmarket/secondGoodsRelease";
	}

	/**
	 * 初始化：一键转卖页面
	 */
	@RequestMapping("/initResell")
	public String initResell(TradeOrdersQueryInDTO inDTO, Integer page, Model model, HttpServletRequest request) {
		//未被审核用户无法一键转卖
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		Integer uStatus = userDTO.getUserstatus();
		if( uStatus < 3 ){
			return "forward:/usedMarket?ifResell=0";
		}
		//查询条件对象:买家标记/订单删除(1:无,2:是)
		inDTO.setBuyerId(userId);
		inDTO.setState(5);
		inDTO.setUserType(1);
		inDTO.setDeleted(1);
		model.addAttribute("inDTO", inDTO);
		//分页对象
		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		if(null != page){
			pager.setPage(page);
		}
		
		//根据shopName查询到shop的ID
		if(StringUtils.isNotBlank(inDTO.getShopName()))
		{
			ShopDTO shopTempDTO = new ShopDTO();
			shopTempDTO.setShopName(inDTO.getShopName());
			ExecuteResult<DataGrid<ShopDTO>>  shopResult = shopExportService.findShopInfoByCondition(shopTempDTO, null);
			List<ShopDTO> shopList = shopResult.getResult().getRows();
			List<Long> shopIDList = new ArrayList<Long>();
			if(shopList.size() > 0)
			{
				for(ShopDTO dto: shopList)
				{
					shopIDList.add(dto.getShopId());
				}
			}else
			{
				shopIDList.add(-1L);
			}
			inDTO.setShopIdList(shopIDList);
		}
		//根据itemName查询到item的ID
		if(StringUtils.isNotBlank(inDTO.getItemName()))
		{
			ItemQueryInDTO  itemDTO = new ItemQueryInDTO ();
			itemDTO.setItemName(inDTO.getItemName());
			List<Long> itemIDList = new ArrayList<Long>();
			DataGrid<ItemQueryOutDTO> data = itemExportService.queryItemList(itemDTO, null);
			List<ItemQueryOutDTO> itemOutList = data.getRows();
			if(itemOutList.size() > 0)
			{
				for(ItemQueryOutDTO dto : itemOutList)
				{
					itemIDList.add(dto.getItemId());
				}
			}else
			{
				itemIDList.add(-1L);
			}
			inDTO.setItemIdList(itemIDList);
		}
		
		//查询结果集
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(inDTO, pager);
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		JSONArray jsonArray = new JSONArray();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
				//查询退款单id
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(tradeOrdersDTO.getOrderId().toString());
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
				if(dg!=null && dg.getTotal()>0){
					//退款单id
					Long refundId = dg.getRows().get(0).getId();
					jsonObject.put("refundId", refundId);
				}
				//获取店铺名称
				ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
				if(result_shopDto!=null && result_shopDto.getResult()!=null){
					ShopDTO shopDTO = result_shopDto.getResult();
					jsonObject.put("shopId", tradeOrdersDTO.getShopId());
					jsonObject.put("shopName", shopDTO.getShopName());
				}
				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();

				JSONArray jsonArray_item = new JSONArray();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					JSONObject jsonObject_item = new JSONObject();

					jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
					jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());

			    	ItemShopCartDTO dto = new ItemShopCartDTO();
					dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
					dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
					dto.setQty(tradeOrderItemsDTO.getNum());//数量
					dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
					dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
					jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
					ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
					ItemShopCartDTO itemShopCartDTO = er.getResult();
					if( null != itemShopCartDTO){
						jsonObject_item.put("skuPicUrl", itemShopCartDTO.getSkuPicUrl());
					}else{
						jsonObject_item.put("skuPicUrl", "");
					}
					//获取商品名称
					jsonObject_item.put("itemId", tradeOrderItemsDTO.getItemId());
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						ItemDTO itemDTO = result_itemDTO.getResult();
						if(itemDTO.getCid()!=null){
							Map<String,String> categoryMap= this.getCategoryName(itemDTO.getCid());
							String cName = "";
							if(categoryMap.get("oneName")!=null){
								cName = categoryMap.get("oneName")+"/";
							}
							if(categoryMap.get("twoName")!=null){
								cName =cName+ categoryMap.get("twoName")+"/";
							}
							if(categoryMap.get("threeName")!=null){
								cName =cName+ categoryMap.get("threeName");
							}
							itemDTO.setcName(cName);
						}
						jsonObject_item.put("itemName", itemDTO.getItemName());
						jsonObject_item.put("categoryName", itemDTO.getcName());
					}
					jsonArray_item.add(jsonObject_item);
				}
				jsonObject.put("items", jsonArray_item);
				jsonArray.add(jsonObject);
			}
		}
		Pager<TradeOrdersDTO> pagerObj = new Pager<TradeOrdersDTO>();
		pagerObj.setRecords(dataGrid.getRows());
		pagerObj.setPage(pager.getPage());
		pagerObj.setTotalCount(dataGrid.getTotal().intValue());
		if (pagerObj.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
			pagerObj.setEndPageIndex(pagerObj.getStartPageIndex());
		}
		model.addAttribute("pager", pagerObj);
		model.addAttribute("jsonArray", jsonArray);

		model.addAttribute("pageState", "resell");
		return "/buyercenter/usedmarket/secondGoodsRelease";
	}
	
	/**
	 * 根据第三级类目获取平台一二级类目
	 * 
	 * @param cid 平台三级类目id
	 */
	@SuppressWarnings("null")
	private Map<String,String> getCategoryName(long cid){
		Map<String,String> resultMap = new HashMap<String,String>();
		 //获取三级类目的父类目信息
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory = itemCategoryService.queryParentCategoryList(cid);
        //如果获取类目不成功
        //遍历父类类目，获取到一级类目和二级类目、三级类目
        for(ItemCatCascadeDTO itemCatCascadeDTOOne :resultCategory.getResult()){
        	for(ItemCatCascadeDTO itemCatCascadeDTOTwo :itemCatCascadeDTOOne.getChildCats()){
        		for(ItemCatCascadeDTO itemCatCascadeDTOThree :itemCatCascadeDTOTwo.getChildCats()){
        			if(cid==itemCatCascadeDTOThree.getCid()){
						resultMap.put("threeName", itemCatCascadeDTOThree.getCname());
						resultMap.put("oneName", itemCatCascadeDTOOne.getCname());
						resultMap.put("twoName", itemCatCascadeDTOTwo.getCname());
						break ;
					}
        		}
        	}
        	
        }
        return resultMap;
	}

	/**
	 * 二手商品：保存/修改
	 */
	@ResponseBody
	@RequestMapping("/storageSecondGoods")
	public Map<String,Object> storageSecondGoods(ItemOldDTO itemOldDTO, String[] pictureUrl,
			Long itemId, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		/*基本-处理*/
		Long userId = WebUtil.getInstance().getUserId(request);
		Long shopId = WebUtil.getInstance().getShopId(request);
		itemOldDTO.setSellerId(userId);
		itemOldDTO.setStatus(2);
		List<ItemPictureDTO> itemPictureList = new ArrayList<ItemPictureDTO>();
		for(int i = 0; i < pictureUrl.length; i++){
			ItemPictureDTO itemPictureDTO = new ItemPictureDTO();
			//商品id/图片url/排序号/图片状态/卖家id/创建日期/修改日期/店铺id
			itemPictureDTO.setItemId(itemId);
			itemPictureDTO.setPictureUrl(pictureUrl[i]);
			itemPictureDTO.setSortNumber((i+1)+"");
			itemPictureDTO.setPictureStatus("1");
			itemPictureDTO.setSellerId(userId);
			itemPictureDTO.setCreated(new Date());
			itemPictureDTO.setModified(new Date());
			itemPictureDTO.setShopId(shopId);
			itemPictureList.add(itemPictureDTO);
		}
		ItemOldInDTO itemOldInDTO = new ItemOldInDTO();
		itemOldInDTO.setItemOldDTO(itemOldDTO);
		itemOldInDTO.setItemPictureDTO(itemPictureList);

		ExecuteResult<String> result;
		if(itemOldDTO!=null && itemOldDTO.getId()!=null && itemOldDTO.getId().longValue()>0){
			result = itemOldExportService.modifyItemOld(itemOldInDTO);
		} else {
			result = itemOldExportService.addItemOld(itemOldInDTO);
		}
		//重置待审核状态
		itemOldExportService.modifyItemOldStatus(null, null, 2L, itemId);
		if(result.isSuccess()){
			map.put("messages", "操作成功!");
		} else {
			map.put("messages", "操作失败!");
		}
		return map;
	}

}
