package com.camelot.mall.evaluation;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDetailDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.EvalTagService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/buyerEvaluation")
public class BuyerEvaluationController {
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
	@Resource
	private ItemExportService itemExportService;
	
	@Resource
	private ItemEvaluationService itemEvaluationService;
	
	@Resource
	private ShopEvaluationService shopEvaluationService;
	
	@Resource
	private EvalTagService evalTagService;
	
	/**
	 * 初始化：交易评价页面
	 */
	@RequestMapping("/initTrading")
	public String initTrading(String orderId, Model model) {
		//订单信息
		ExecuteResult<TradeOrdersDTO> tradeOrdersResult = tradeOrderExportService.getOrderById(orderId);
		TradeOrdersDTO tradeOrdersDTO = tradeOrdersResult.getResult();
		model.addAttribute("tradeOrdersDTO", tradeOrdersDTO);
		
		//订单明细信息:商品图片/商品属性/商品名称
		List<ItemShopCartDTO> itemShopCartDTOList = new ArrayList<ItemShopCartDTO>();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
		
		// 每种SPU对应一组评价标签
		Map<Long, List<EvalTag>> evalTagsOfItems = new HashMap<Long, List<EvalTag>>();
		model.addAttribute("evalTagsOfItems", evalTagsOfItems);
		for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrdersDTO.getItems()){
			//商品图片
			ItemShopCartDTO itemShopCartDTO = new ItemShopCartDTO();
			itemShopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
			itemShopCartDTO.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
			itemShopCartDTO.setQty(tradeOrderItemsDTO.getNum());//数量
			itemShopCartDTO.setShopId(tradeOrdersDTO.getShopId());//店铺ID
			itemShopCartDTO.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
			ExecuteResult<ItemShopCartDTO> itemShopCartResult = itemExportService.getSkuShopCart(itemShopCartDTO); //调商品接口查url
			//商品属性
			ItemShopCartDTO itemShopCart = itemShopCartResult.getResult();
			String skuName = "";
			for(ItemAttr itemAttr : itemShopCart.getAttrSales()){
				skuName += itemAttr.getName();
				for(ItemAttrValue itemAttrValue : itemAttr.getValues()){
					skuName += ":" + itemAttrValue.getName()+";";
				}
			}
			tradeOrderItemsDTO.setSkuName(skuName);
			itemShopCartDTOList.add(itemShopCartResult.getResult());
			//商品名称
			ExecuteResult<ItemDTO> itemResult = itemExportService.getItemById(tradeOrderItemsDTO.getItemId());
			itemDTOList.add(itemResult.getResult());
			
			// 商品评价标签
			Long itemId = tradeOrderItemsDTO.getItemId();
			List<EvalTag> evalTagsOfItemId = evalTagService.queryByItemId(itemId);
			if (!CollectionUtils.isEmpty(evalTagsOfItemId)) {
				evalTagsOfItems.put(itemId, evalTagsOfItemId);
			}
		}
		model.addAttribute("itemShopCartDTOList", itemShopCartDTOList);
		model.addAttribute("itemDTOList", itemDTOList);
		model.addAttribute("imageServerAddr", SysProperties.getProperty("ftp_server_dir"));
		return "/evaluation/buyerTrading";
	}
	
	/**
	 * 提交：商品评价-店铺评价
	 */
	@ResponseBody
	@RequestMapping("/submitTrading")
	public Map<String,Object> submitTrading(ShopEvaluationDTO shopEvaluationDTO, ItemEvaluationDTO itemEvaluationDTO,
			Long[] itemId,Long[] skuId, Integer[] skuScope,String[] content,
			String[] tagIdsGroup, HttpServletRequest request) {
		int groType = 0; //用于判断是是评价   还是评价加晒单   0 评价   1  评价+晒单
		Map<String,Object> map = new HashMap<String,Object>();
		/*对商家评价*/
		shopEvaluationDTO.setResource("2");								//1：默认评价 2:手动评价
		ExecuteResult<ShopEvaluationDTO> shopEvaluationResult = shopEvaluationService.addShopEvaluation(shopEvaluationDTO);
		if( !shopEvaluationResult.isSuccess() ){
			map.put("errorMessages", "提交失败!");
		} else {
			/*对商品评价*/
			List<ItemEvaluationDTO> itemEvaluationDTOList = new ArrayList<ItemEvaluationDTO>();
			for(int i = 0; i < itemId.length; i++){
				//组装数据
				ItemEvaluationDTO itemEvaluation = new ItemEvaluationDTO();
				itemEvaluation.setOrderId(itemEvaluationDTO.getOrderId());		//订单ID
				itemEvaluation.setUserId(itemEvaluationDTO.getUserId());		//评价人
				itemEvaluation.setByUserId(itemEvaluationDTO.getByUserId());	//被评价人
				itemEvaluation.setByShopId(itemEvaluationDTO.getByShopId());	//被评价店铺id
				itemEvaluation.setItemId(itemId[i]);							//商品id
				itemEvaluation.setSkuId(skuId[i]);								//商品sku
				itemEvaluation.setType("1");									//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
				itemEvaluation.setResource("2");								//1：默认评价 2:手动评价
				itemEvaluation.setSkuScope(skuScope[i]);						//sku评分
				if(i < content.length){											//评价内容
					itemEvaluation.setContent(content[i]);						
				}
				itemEvaluationDTOList.add(itemEvaluation);
			}
			//批量提交并且返回实体list
			ExecuteResult<List<ItemEvaluationDTO>> itemEvaluationResult = itemEvaluationService.addItemEvaluationsReturnList(itemEvaluationDTOList);
			
			if( itemEvaluationResult.isSuccess() ){
				
				// 商品评价贴上标签
				insertItemTagsEvaluation(itemEvaluationResult.getResult(), tagIdsGroup);
				
				//获取晒单的图片的url
				String imgurls = request.getParameter("imgurls");
				//晒单图片数组
				String[] imgUrl = null ;
				String[] sku_url = null;
				//新增晒单图片
				ItemEvaluationShowDTO itemEvaluationShowDTO = new ItemEvaluationShowDTO();
				//插入晒单图片
				if(!StringUtils.isBlank(imgurls)){
					imgUrl = imgurls.split(";");
					for(String url : imgUrl){
						List<ItemEvaluationDTO> list= itemEvaluationResult.getResult();
						sku_url = url.split("_");//格式为skuid_url
						if(null != list && !list.isEmpty()){
							for(ItemEvaluationDTO i : list){
								//找到该skuid对应的评价表主表id
								if(sku_url[0].equals(i.getSkuId().toString())){
									itemEvaluationShowDTO.setEvaluationId(i.getId());
									itemEvaluationShowDTO.setSkuId(Long.parseLong(sku_url[0]));
									itemEvaluationShowDTO.setIsDelete(0);
									itemEvaluationShowDTO.setImgUrl(sku_url[1]);
									itemEvaluationService.addItemEvaluationShow(itemEvaluationShowDTO);
								}
							}
						}
					}
					groType = 1;
					request.setAttribute("isBaskOrder", true);
				}
				//更新：订单状态
				TradeOrdersDTO tradeOrdersDTO = new TradeOrdersDTO();
				tradeOrdersDTO.setOrderId(itemEvaluationDTO.getOrderId());
				tradeOrdersDTO.setEvaluate(2);
				tradeOrderExportService.modifyEvaluationStatus(tradeOrdersDTO);
				//拦截评价内容
				request.setAttribute("content", content);
				request.setAttribute("orderId", itemEvaluationDTO.getOrderId());
				request.setAttribute("shopId", itemEvaluationDTO.getByShopId());
				request.setAttribute("userId", itemEvaluationDTO.getUserId());
				map.put("errorMessages", "提交成功!");
			} else {
				map.put("errorMessages", "提交失败!");
			}
		}
		if(map.get("errorMessages").toString().equals("提交成功!")&&groType == 0){ //用于成长值拦截器     评价成功  放入成长值增长类型“ 评价类型   3”
			request.setAttribute("GrowthValue",Constants.GROWTH_VALUE_EVALUATION); 
		}else if(map.get("errorMessages").toString().equals("提交成功!")&&groType == 1){ //用于成长值拦截器     评价成功  放入成长值增长类型“ 评价+晒单类型   4”
			request.setAttribute("GrowthValue",Constants.GROWTH_VALUE_EVALUATION_AND_EXPOSURE); 
		}
		request.setAttribute("map", map);
		return map;
	}
	
	/**
	 * <p>商品评价贴上标签</p>
	 * Created on 2016年2月24日
	 * @param result 商品评价记录
	 * @param tagIdsGroup 每条商品评价记录，都有对应的一个或多个标签
	 * @author: 顾雨
	 */
	private void insertItemTagsEvaluation(List<ItemEvaluationDTO> result, String[] tagIdsGroup) {

		int tagIdsGroupIndex = 0;
		for (ItemEvaluationDTO itemEvaluation: result) {
			Long evalId = itemEvaluation.getId();
			Long skuId = itemEvaluation.getSkuId();
			String tagIdsStr = tagIdsGroup[tagIdsGroupIndex++];
			// tagIdsStr包括多个tagId，约定用,隔开，为空表示无标签评价
			if (StringUtils.isEmpty(tagIdsStr)){
				continue;
			}
			List<String> tagIds = Arrays.asList(tagIdsStr.split("_"));
			itemEvaluationService.addItemTagsEvaluation(evalId, tagIds, skuId);
		}
	}

	/**
	 * 查看：给出的评价/收到的评价
	 */
	@RequestMapping("/initEvaluation")
	public String initEvaluation(ItemEvaluationQueryInDTO itemEvaluationQueryInDTO, String pageState,
			Integer page, HttpServletRequest request, Model model) {
		/*基本-处理*/
		Long userId = WebUtil.getInstance().getUserId(request);
		Long shopId = WebUtil.getInstance().getShopId(request);
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(userId);
		//1:默认回复 2:手动回复
//		itemEvaluationQueryInDTO.setResource("1");
		//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
		if("given".equals(pageState)){
			itemEvaluationQueryInDTO.setType("1");
//			itemEvaluationQueryInDTO.setUserIds(new Long[]{userId});
			itemEvaluationQueryInDTO.setUserIds(idsEr.getResult().toArray(new Long[idsEr.getResult().size()]));
		} else {
			itemEvaluationQueryInDTO.setType("2");
//			itemEvaluationQueryInDTO.setUserIds(idsEr.getResult().toArray(new Long[idsEr.getResult().size()]));
			itemEvaluationQueryInDTO.setByUserId(userId);
			if(pageState==null || "".equals(pageState)){
				pageState = "received";
			}
		}
		//页面状态
		model.addAttribute("pageState", pageState);
		//评价内容
		if(itemEvaluationQueryInDTO!=null && "".equals(itemEvaluationQueryInDTO.getContentEmpty())){
			itemEvaluationQueryInDTO.setContentEmpty(null);
		}
		
		/*分页查询：给出的评价/收到的评价*/
		//分页对象-pager
		Pager<ItemEvaluationQueryOutDTO> pager = new Pager<ItemEvaluationQueryOutDTO>();
		if(null != page){
			pager.setPage(page);
		}
		DataGrid<ItemEvaluationQueryOutDTO> evaluationGrid = itemEvaluationService.queryItemEvaluationList(itemEvaluationQueryInDTO, pager);
		//获取：销售属性/用户信息
		model.addAttribute("userDTOMap", this.getUserDTOMap(shopId, evaluationGrid.getRows()));
		
		pager.setTotalCount(evaluationGrid.getTotal().intValue());
		pager.setRecords(evaluationGrid.getRows());
		// 增加商品评价标签
		List<ItemEvaluationQueryOutDTO> lists=evaluationGrid.getRows();
		if(!CollectionUtils.isEmpty(lists)){
			 Map<Long, List<EvalTag>> evaluationMap2Tags = new HashMap<Long, List<EvalTag>>();
			 
			 model.addAttribute("evaluationMap2Tags", evaluationMap2Tags);
			 for(ItemEvaluationQueryOutDTO list:lists){
				//查询评价标签根据主键id
				    ExecuteResult<List<EvalTag>> tagsER = itemEvaluationService.queryEvalTagsOfOneEvaluation(list.getId());
				    List<EvalTag> tags = null;
				    if (tagsER.isSuccess() && !CollectionUtils.isEmpty(tags = tagsER.getResult())) {
				    	evaluationMap2Tags.put(list.getId(), tags);
				    }
			 }
		}
		
		//解决列表为空时，多余的0页
		if (pager.getEndPageIndex() == 0) {	
			pager.setEndPageIndex(pager.getStartPageIndex());
		}
		model.addAttribute("pager", pager);
		model.addAttribute("scopeVal", itemEvaluationQueryInDTO.getScope());
		model.addAttribute("contentEmptyVal", itemEvaluationQueryInDTO.getContentEmpty());
		//重置条件
		itemEvaluationQueryInDTO.setScope(null);
		itemEvaluationQueryInDTO.setContentEmpty(null);
//		itemEvaluationQueryInDTO.setResource(null);
		
		/*总评价人数/店铺总体评分*/
		ExecuteResult<ItemEvaluationTotalDTO> evaluationTotal = itemEvaluationService.queryItemEvaluationTotal(itemEvaluationQueryInDTO);
		ItemEvaluationTotalDTO itemEvaluationTotalDTO = evaluationTotal.getResult();
		model.addAttribute("evaluationTotal", itemEvaluationTotalDTO);
		//星际-总计：处理
		List<ItemEvaluationTotalDetailDTO> totalDetailList = new ArrayList<ItemEvaluationTotalDetailDTO>();
		if(itemEvaluationTotalDTO != null){
			Map<Integer,ItemEvaluationTotalDetailDTO> scopeAvgDetails = itemEvaluationTotalDTO.getScopeAvgDetails();
			totalDetailList.add(scopeAvgDetails.get(5));
			totalDetailList.add(scopeAvgDetails.get(4));
			totalDetailList.add(scopeAvgDetails.get(3));
			totalDetailList.add(scopeAvgDetails.get(2));
			totalDetailList.add(scopeAvgDetails.get(1));
		} else {
			for(int i=0 ; i<5 ;i++){
				ItemEvaluationTotalDetailDTO totalDetail = new ItemEvaluationTotalDetailDTO();
				totalDetail.setPercent(0.00);
				totalDetail.setCount(0);
				totalDetailList.add(totalDetail);
			}
		}
		model.addAttribute("totalDetailList", totalDetailList);
		
		return "/evaluation/buyerEvaluation";
	}
	
	/*
	 * 获取：销售属性/用户信息
	 */
	private Map<Long,UserDTO> getUserDTOMap(Long shopId,List<ItemEvaluationQueryOutDTO> evaluationList){
		Map<Long,UserDTO> userDTOMap = new HashMap<Long,UserDTO>();
		for(ItemEvaluationQueryOutDTO itemEvaluationQueryOut : evaluationList){
			//用户基本信息
			Long uId = itemEvaluationQueryOut.getUserId();
			userDTOMap.put(uId,userExportService.queryUserById(itemEvaluationQueryOut.getUserId()));
			//添加：分页结果集中对象-销售属性
			ItemShopCartDTO skuDto = new ItemShopCartDTO();
			skuDto.setShopId(shopId);								//店铺ID
			skuDto.setItemId(itemEvaluationQueryOut.getItemId());	//商品ID
			skuDto.setSkuId(itemEvaluationQueryOut.getSkuId());		//SKU id
			ExecuteResult<ItemShopCartDTO> itemShopCartResult = itemExportService.getSkuShopCart(skuDto);
			if(itemShopCartResult.isSuccess()){
				ItemShopCartDTO itemShopCartDTO = itemShopCartResult.getResult();
				String skuName = "";
				for(ItemAttr itemAttr : itemShopCartDTO.getAttrSales()){
					skuName += itemAttr.getName();
					for(ItemAttrValue itemAttrValue : itemAttr.getValues()){
						skuName += "：" + itemAttrValue.getName()+"；";
					}
				}
				itemEvaluationQueryOut.setSkuName(skuName);
			}
		}
		return userDTOMap;
	}
	
}
