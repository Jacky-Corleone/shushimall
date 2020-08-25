package com.camelot.mall.evaluation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.dto.ItemEvaluationReplyDTO;
import com.camelot.goodscenter.dto.ItemEvaluationShowDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDTO;
import com.camelot.goodscenter.dto.ItemEvaluationTotalDetailDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.dto.ShopEvaluationTotalDTO;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/sellerEvaluation")
public class SellerEvaluationController {
	
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
	private ShopCategorySellerExportService shopCategorySellerExportService;
	/**
	 * 查看：商品评价
	 */
	@RequestMapping("/initProduct")
	public String initProduct(ItemEvaluationQueryInDTO itemEvaluationQueryInDTO,String shopLevelOneId,String userName, 
			Integer page, HttpServletRequest request,  Model model) {
		Long shopId = WebUtil.getInstance().getShopId(request);
		//itemEvaluationQueryInDTO.setByUserId(userId);
		//查询条件
		if(userName!=null&&!"".equals(userName)){
			if(itemEvaluationQueryInDTO!=null){
				//用户名
				UserDTO userDTO = new UserDTO();
				userDTO.setUname(userName);
				DataGrid<UserDTO> userGrid = userExportService.findUserListByCondition(userDTO, null, null);
				int total = userGrid.getTotal().intValue();
				Long[] userIds = new Long[total];
				for(int i=0; i<total; i++){
					UserDTO user = userGrid.getRows().get(i);
					userIds[i] = user.getUid();
				}
				if(total > 0){
					itemEvaluationQueryInDTO.setUserIds(userIds);
				}
			}
		}
		itemEvaluationQueryInDTO.setByShopId(shopId);
		//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
		itemEvaluationQueryInDTO.setType("1");
		itemEvaluationQueryInDTO.setReply("0");
		model.addAttribute("itemEvaluationQueryInDTO", itemEvaluationQueryInDTO);
		model.addAttribute("userName", userName);
		//分页对象-pager
		Pager<ItemEvaluationQueryOutDTO> pager = new Pager<ItemEvaluationQueryOutDTO>();
		if(null != page){
			pager.setPage(page);
		}
		DataGrid<ItemEvaluationQueryOutDTO> evaluationGrid = itemEvaluationService.queryItemEvaluationList(itemEvaluationQueryInDTO, pager);
		
		List<ItemEvaluationQueryOutDTO> lists=evaluationGrid.getRows();
		
		 //晒单图片实体类
		ItemEvaluationShowDTO itemEvaluationShowDTO;
		//晒单图片返回结果
		ExecuteResult<DataGrid<ItemEvaluationShowDTO>> showPicResult ;
		List<ItemEvaluationShowDTO> picList ;
		Map<Long,List<ItemEvaluationShowDTO>> picMap = new HashMap<Long,List<ItemEvaluationShowDTO>>();
		
		
		 if(!CollectionUtils.isEmpty(lists)){
			 Map<Long, List<EvalTag>> evaluationMap2Tags = new HashMap<Long, List<EvalTag>>();
			 model.addAttribute("evaluationMap2Tags", evaluationMap2Tags);
			for(ItemEvaluationQueryOutDTO list:lists){
				ItemEvaluationReplyDTO itemEvaluationReplyDTO=new ItemEvaluationReplyDTO();
				itemEvaluationReplyDTO.setEvaluationId(list.getId());
				DataGrid<ItemEvaluationReplyDTO> itemEvaluationReplyDTOs=itemEvaluationService.queryItemEvaluationReplyList(itemEvaluationReplyDTO, null);
			    if(null!=itemEvaluationReplyDTOs&&itemEvaluationReplyDTOs.getTotal()>0){
				List<ItemEvaluationReplyDTO> rows=itemEvaluationReplyDTOs.getRows();
			    list.setItemEvaluationReplyList(rows);
			    }
			    
			    //查询评价标签根据主键id
			    ExecuteResult<List<EvalTag>> tagsER = itemEvaluationService.queryEvalTagsOfOneEvaluation(list.getId());
			    List<EvalTag> tags = null;
			    if (tagsER.isSuccess() && !CollectionUtils.isEmpty(tags = tagsER.getResult())) {
			    	evaluationMap2Tags.put(list.getId(), tags);
			    }
			    
			    //查询晒单图片根据评价表主键id
	    		itemEvaluationShowDTO = new ItemEvaluationShowDTO();
	    		itemEvaluationShowDTO.setEvaluationId(list.getId());
	    		showPicResult = itemEvaluationService.queryItemEvaluationShowList(itemEvaluationShowDTO, new Pager());
	    		
	    		if(null != showPicResult.getResult()){
	    			picList = showPicResult.getResult().getRows();
	    			if(null != picList && !picList.isEmpty()){
	    				picMap.put(list.getId(), picList);
	    			}else{
	    				picMap.put(list.getId(), null);
	        		}
	    		}else{
	    			picMap.put(list.getId(), null);
	    		}
			 }
		}	
		//获取：销售属性/用户信息
		model.addAttribute("userDTOMap", this.getUserDTOMap(shopId, evaluationGrid.getRows()));
		model.addAttribute("picMap", picMap);
		model.addAttribute("isNotHave", "isNotHave_");
		model.addAttribute("urlFtp", SysProperties.getProperty("ftp_server_dir"));
		pager.setTotalCount(evaluationGrid.getTotal().intValue());
		pager.setRecords(evaluationGrid.getRows());
		//解决列表为空时，多余的0页
		if (pager.getEndPageIndex() == 0) {	
			pager.setEndPageIndex(pager.getStartPageIndex());
		}
		model.addAttribute("pager", pager);
		
		//获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategorySellerDTOList = this.getLevelOneShopCategory(request);
		model.addAttribute("shopCategorySellerDTOList", shopCategorySellerDTOList);
		//获取选中店铺一级分类对应的二级分类
		if(StringUtils.isNotEmpty(shopLevelOneId)){
			ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
			shopCategorySellerDTO.setParentCid(new Long(shopLevelOneId));
			ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO, null);
			List<ShopCategorySellerDTO> levelTwoCategory = result.getResult().getRows();
			model.addAttribute("levelTwoCategory", levelTwoCategory);
			model.addAttribute("shopLevelOneId", shopLevelOneId);
		}
		
		return "/evaluation/sellerProduct";
	}
	
	/**
	 * 获取店铺分类
	 * @return
	 */
	@RequestMapping("getShopCategory")
	@ResponseBody
	public Map<String,Object> getShopCategory(ShopCategorySellerDTO shopCategorySellerDTO){
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO, null);
		List<ShopCategorySellerDTO> list = result.getResult().getRows();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("result", list);
		return resultMap;
	}
	/**
	 * 回复：买家给出的评价
	 */
	@ResponseBody
	@RequestMapping("/replyBuyers")
	public Map<String,Object> replyBuyers(HttpServletRequest request, ItemEvaluationReplyDTO itemEvaluationReplyDTO) {
		Map<String,Object> map = new HashMap<String,Object>();
		String content = itemEvaluationReplyDTO.getContent();
		String buyerId = request.getParameter("buyerId");
		String orderId = request.getParameter("ordersId");
		String goodId = request.getParameter("goodId");
		ExecuteResult<ItemEvaluationReplyDTO> replyResult = itemEvaluationService.addItemEvaluationReply(itemEvaluationReplyDTO);
		if(replyResult.isSuccess()){
			//将结果放入request中，仅供站内信发送拦截器使用
			ExecuteResult<String> result = new ExecuteResult<String>();
			request.setAttribute("result", result);
			request.setAttribute("content", content);
			request.setAttribute("buyerId", buyerId);
			request.setAttribute("orderId", orderId);
			request.setAttribute("goodId", goodId);
			map.put("messages", "回复成功!");
		} else {
			map.put("messages", "回复失败!");
		}
		return map;
	}
	
	/**
	 * 查看：收到的评价/给出的评价
	 */
	@RequestMapping("/initEvaluation")
	public String initEvaluation(ItemEvaluationQueryInDTO itemEvaluationQueryInDTO,ShopEvaluationQueryInDTO shopEvaluationQueryInDTO,
			String pageState, Integer page, HttpServletRequest request, Model model) {
		/*基本-处理*/
		Long userId = WebUtil.getInstance().getUserId(request);
		Long shopId = WebUtil.getInstance().getShopId(request);
		//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
		if("given".equals(pageState)){
			itemEvaluationQueryInDTO.setType("2");
			itemEvaluationQueryInDTO.setUserIds(new Long[]{userId});
		} else {
			itemEvaluationQueryInDTO.setType("1");
			itemEvaluationQueryInDTO.setByUserId(userId);
			if(pageState==null || "".equals(pageState)){
				pageState = "shop";
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
		//解决列表为空时，多余的0页
		if (pager.getEndPageIndex() == 0) {	
			pager.setEndPageIndex(pager.getStartPageIndex());
		}
		model.addAttribute("pager", pager);
		model.addAttribute("scopeVal", itemEvaluationQueryInDTO.getScope());
		model.addAttribute("contentEmptyVal", itemEvaluationQueryInDTO.getContentEmpty());
		itemEvaluationQueryInDTO.setScope(null);
		itemEvaluationQueryInDTO.setContentEmpty(null);
		
		/*总评价人数/店铺总体评分*/
		itemEvaluationQueryInDTO.setByShopId(shopId);
		ExecuteResult<ItemEvaluationTotalDTO> itemEvaluationTotal = itemEvaluationService.queryItemEvaluationTotal(itemEvaluationQueryInDTO);
		ItemEvaluationTotalDTO itemEvaluationTotalDTO = itemEvaluationTotal.getResult();
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
		
		/*店铺动态评分-处理:描述相符/态度服务/发货速度*/
		shopEvaluationQueryInDTO.setByShopId(shopId);
		ExecuteResult<ShopEvaluationTotalDTO> shopEvaluationTotal = shopEvaluationService.queryShopEvaluationTotal(shopEvaluationQueryInDTO);
		ShopEvaluationTotalDTO shopEvaluation = shopEvaluationTotal.getResult();
		model.addAttribute("shopEvaluationResult", shopEvaluation);
		List<Double> shopDescriptionList = new ArrayList<Double>();
		List<Double> shopServiceList = new ArrayList<Double>();
		List<Double> shopArrivalList = new ArrayList<Double>();
		if(shopEvaluation != null){
			shopDescriptionList.add(shopEvaluation.getShopDescription());
			shopServiceList.add(shopEvaluation.getShopService());
			shopArrivalList.add(shopEvaluation.getShopArrival());
			Map<Integer,Double> shopDescriptionDetails = shopEvaluation.getShopDescriptionDetails();
			Map<Integer,Double> shopServiceDetails = shopEvaluation.getShopServiceDetails();
			Map<Integer,Double> shopArrivalDetails = shopEvaluation.getShopArrivalDetails();
			for(int i=0; i<5; i++){
				if(shopDescriptionDetails != null){
					shopDescriptionList.add(shopDescriptionDetails.get(i+1));
				} else {
					shopDescriptionList.add(0.0);
				}
				if(shopServiceDetails != null){
					shopServiceList.add(shopServiceDetails.get(i+1));
				} else {
					shopServiceList.add(0.0);
				}
				if(shopArrivalDetails != null){
					shopArrivalList.add(shopArrivalDetails.get(i+1));
				} else {
					shopArrivalList.add(0.0);
				}
			}
		} else {
			for(int i=0 ; i<6 ; i++){
				shopDescriptionList.add(0.00);
				shopServiceList.add(0.00);
				shopArrivalList.add(0.00);
			}
		}
		model.addAttribute("shopDescriptionList", shopDescriptionList);
		model.addAttribute("shopServiceList", shopServiceList);
		model.addAttribute("shopArrivalList", shopArrivalList);
		
		return "/evaluation/sellerEvaluation";
	}
	
	/**
	 * 初始化：给的买家评价页面
	 */
	@RequestMapping("/initSellerToBuyers")
	public String initSellerToBuyers(String orderId, Model model) {
		//订单信息
		ExecuteResult<TradeOrdersDTO> tradeOrdersResult = tradeOrderExportService.getOrderById(orderId);
		TradeOrdersDTO tradeOrdersDTO = tradeOrdersResult.getResult();
		model.addAttribute("tradeOrdersDTO", tradeOrdersDTO);
		UserDTO userDTO = userExportService.queryUserById(tradeOrdersDTO.getBuyerId());
		model.addAttribute("userDTO",userDTO);
		//订单明细信息:商品图片/商品属性/商品名称
		List<ItemShopCartDTO> itemShopCartDTOList = new ArrayList<ItemShopCartDTO>();
		List<ItemDTO> itemDTOList = new ArrayList<ItemDTO>();
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
		}
		model.addAttribute("itemShopCartDTOList", itemShopCartDTOList);
		model.addAttribute("itemDTOList", itemDTOList);
				
		return "/evaluation/sellerToBuyers";
	}
	
	/**
	 * 提交：给出的评价
	 */
	@ResponseBody
	@RequestMapping("/submitSellerToBuyers")
	public Map<String,Object> submitSellerToBuyers(ItemEvaluationDTO itemEvaluationDTO,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<String,Object>();
		//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
		itemEvaluationDTO.setType("2");
		//1：默认评价 2:手动评价
		itemEvaluationDTO.setResource("2");
		//对买家评价
		ExecuteResult<ItemEvaluationDTO> itemEvaluationResult = itemEvaluationService.addItemEvaluation(itemEvaluationDTO);
		if(itemEvaluationResult.isSuccess()){
			//更新：订单状态
			TradeOrdersDTO tradeOrdersDTO = new TradeOrdersDTO();
			tradeOrdersDTO.setOrderId(itemEvaluationDTO.getOrderId());
			tradeOrdersDTO.setSellerEvaluate(2);
			tradeOrderExportService.modifyEvaluationStatus(tradeOrdersDTO);
			map.put("messages", "提交成功!");
		} else {
			map.put("messages", "提交失败!");
		}
		request.setAttribute("map",map);
		return map;
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
	/**
	 * 获取店铺id
	 * @param request
	 * @return
	 */
	private Long getShopId(HttpServletRequest request){
		return WebUtil.getInstance().getShopId(request);
	}
	/**
	 * 获取一级店铺分类
	 * @return
	 */
	private List<ShopCategorySellerDTO> getLevelOneShopCategory(HttpServletRequest request){
		//获取店铺id
		ShopCategorySellerDTO shopCategorySellerDTO_ = new ShopCategorySellerDTO();
		shopCategorySellerDTO_.setShopId(getShopId(request));
		shopCategorySellerDTO_.setLev(1);
		//获取一级店铺分类
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> shopCategoryResult = this.shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO_, null);
		return shopCategoryResult.getResult().getRows();
	}
	
}
