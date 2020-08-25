package com.camelot.mall.timedtask;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryInDTO;
import com.camelot.goodscenter.dto.ItemEvaluationQueryOutDTO;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.dto.ShopEvaluationQueryInDTO;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

/**
 * 
 * <p>Description: [定时任务-系统默认好评功能代码]</p>
 * Created on 2015年4月17日
 * @author  <a href="mailto: xxx@camelotchina.com">程海龙</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class EvaluationTimingTask {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TradeOrderExportService tradeOrderExportService;
    
	@Resource
	private ItemEvaluationService itemEvaluationService;
	
	@Resource
	private ShopEvaluationService shopEvaluationService;
    
	/**
	 * 
	 * <p>Discription:[定时任务-系统默认好评功能方法]</p>
	 * Created on 2015年4月17日
	 * @author:[chenghailong]
	 */
    public  void performEvaluation(){
    	System.out.println("---------------------------测试-----------------------");
    	/*
    	//获取：指定日期-需要默认评价-订单信息
    	DataGrid<TradeOrdersDTO> tradeOrdersGrid = this.getTradeOrdersGrid();
    	for(TradeOrdersDTO tradeOrders : tradeOrdersGrid.getRows()){
			//默认-好评：对买家评价/对卖家评价（商品评价）
    		for(TradeOrderItemsDTO tradeOrderItems : tradeOrders.getItems()){
    			String itemState = this.defaultEvaluation(tradeOrders, tradeOrderItems);
    			if(!"true".equals(itemState) && !"false".equals(itemState)){
    				logger.debug("----------【定时任务-默认好评】【订单ID:"+tradeOrders.getOrderId()+"-"+"skuId:"+tradeOrderItems.getSkuId()+itemState+"】----------");
    			}
    		}
    	}*/
    }
    
    /*
     * 获取：指定日期-订单信息
     */
    private DataGrid<TradeOrdersDTO> getTradeOrdersGrid(){
    	/*查询-当天：确认收货时间为两周前的订单;例如：今天(2015-04-16)/查询时间(2015-04-02)*/
    	TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
    	//获取: 查询-开始时间 /查询-结束时间
    	Date dNow = new Date();
    	Calendar calendar = Calendar.getInstance(); 	//得到日历
    	calendar.setTime(dNow);							//把当前时间赋给日历
    	calendar.add(Calendar.DAY_OF_MONTH, -14);
    	Date startDate = calendar.getTime();
    	calendar.add(Calendar.DAY_OF_MONTH, -14);
    	Date endDate = calendar.getTime();
    	
    	//设置查询：订单状态、开始时间 、结束时间
    	inDTO.setState(4); 								//订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成 
    	inDTO.setUpdateStart(startDate);
    	inDTO.setUpdateEnd(endDate);
    	ExecuteResult<DataGrid<TradeOrdersDTO>> tradeOrdersResult = tradeOrderExportService.queryOrders(inDTO, null);
    	if(!tradeOrdersResult.isSuccess()){
    		logger.debug("----------【定时任务-默认好评】【指定日期-订单信息查询异常】----------");
    		return null;
    	}
    	return tradeOrdersResult.getResult();
    }
    
    /*
     * 默认-好评：对买家评价/对卖家评价（商品评价）
     */
    private String defaultEvaluation(TradeOrdersDTO tradeOrders, TradeOrderItemsDTO tradeOrderItems){
    	String orderId = tradeOrders.getOrderId();
    	/*查询评价数据*/
    	ItemEvaluationQueryInDTO itemEvaluationQueryIn = new ItemEvaluationQueryInDTO();
		itemEvaluationQueryIn.setOrderId(orderId);	//订单-ID
		itemEvaluationQueryIn.setSkuId(tradeOrderItems.getSkuId());	//SKU-ID
		DataGrid<ItemEvaluationQueryOutDTO> itemEvaluationGrid = itemEvaluationService.queryItemEvaluationList(itemEvaluationQueryIn, null);
		
		/*评价状态*/
		boolean isBuyerToSeller = false;
		boolean isSellerToBuyer = false;
		boolean isAfterSales = false; 
		for(ItemEvaluationQueryOutDTO itemEvaluationQueryOut : itemEvaluationGrid.getRows()){
			//1:买家对卖家评价 2:卖家对买家评价  3:售后评价
			String type = itemEvaluationQueryOut.getType();
			if("1".equals(type)){
				isBuyerToSeller = true;
			}else if("2".equals(type)){
				isSellerToBuyer = true;
			}else if("3".equals(type)){
				isAfterSales = true;
			}
		}
		/*买家未评价*/
		if(isBuyerToSeller){
			//默认：对卖家评价（商品评价）
			if( !this.evaluationToItem("1", tradeOrders, tradeOrderItems) ){
				return "/对卖家评价（商品评价）异常";
			}
			//默认-好评：对商家评价
			String shopState = this.evaluationToShop(tradeOrders);
			if(!"true".equals(shopState) && !"false".equals(shopState)){
				logger.debug("----------【定时任务-默认好评】【订单ID:"+orderId+shopState+"】----------");
			}
			//更新-订单状态：买家-评价状态
			if(!this.updateEvaluationStatus(orderId, "1")){
				return "/买家-订单状态-更新异常";
			}
		}
		/*卖家未评价*/
		if(isSellerToBuyer){
			//默认：对买家评价
			if( !this.evaluationToItem("2", tradeOrders, tradeOrderItems) ){
				return "/对买家评价异常";
			}
			//更新-订单状态：卖家-评价状态
			if( !this.updateEvaluationStatus(orderId, "2") ){
				return "/卖家-订单状态-更新异常";
			}
		}
		/*售后未评价*/
		if(isAfterSales){
			
		}
		
		return "true";
    }
    
    /*
     * 添加：评价明细
     */
    private Boolean evaluationToItem(String type,TradeOrdersDTO tradeOrders, TradeOrderItemsDTO tradeOrderItems){
    	//组装数据
    	ItemEvaluationDTO itemEvaluationDTO = new ItemEvaluationDTO();
    	itemEvaluationDTO.setOrderId(tradeOrders.getOrderId());			//订单ID
    	itemEvaluationDTO.setItemId(tradeOrderItems.getItemId());		//商品id
    	itemEvaluationDTO.setSkuId(tradeOrderItems.getSkuId());			//商品sku
    	itemEvaluationDTO.setSkuScope(5);								//sku评分
    	itemEvaluationDTO.setContent("默认好评!");						//评价内容
    	itemEvaluationDTO.setCreateTime(new Date());					//创建时间
    	itemEvaluationDTO.setModifyTime(new Date());					//编辑时间
    	itemEvaluationDTO.setResource("1");;							//1：默认评价 2:手动评价
//      itemEvaluationDTO.setUserShopId();								//评价人店铺id
    	//1:买家对卖家评价 2:卖家对买家评价  3:售后评价
    	itemEvaluationDTO.setType(type);
    	if("1".equals(type)){
        	itemEvaluationDTO.setUserId(tradeOrders.getBuyerId());			//评价人
        	itemEvaluationDTO.setByUserId(tradeOrders.getSellerId());		//被评价人
        	itemEvaluationDTO.setByShopId(tradeOrders.getShopId());			//被评价店铺id
    	}else if("2".equals(type)){
        	itemEvaluationDTO.setUserId(tradeOrders.getSellerId());			//评价人
        	itemEvaluationDTO.setByUserId(tradeOrders.getBuyerId());		//被评价人
    	}
    	//服务调用
		ExecuteResult<ItemEvaluationDTO> itemEvaluationResult = itemEvaluationService.addItemEvaluation(itemEvaluationDTO);
		if(itemEvaluationResult.isSuccess()){
			return true;
		} else {
			return false;
		}
    }
    
    /*
     * 默认-好评：对商家评价
     */
    private String evaluationToShop(TradeOrdersDTO tradeOrders){
    	//查询-服务调用
    	ShopEvaluationQueryInDTO shopEvaluationQueryInDTO = new ShopEvaluationQueryInDTO();
    	shopEvaluationQueryInDTO.setUserId(tradeOrders.getBuyerId());	//评价人id
    	shopEvaluationQueryInDTO.setOrderId(tradeOrders.getOrderId());	//订单ID
    	DataGrid<ShopEvaluationDTO> shopEvaluationGrid = shopEvaluationService.queryShopEvaluationDTOList(shopEvaluationQueryInDTO,null);
		if(null!=shopEvaluationGrid && shopEvaluationGrid.getTotal().longValue()>0){
			return "false";
		}
    	//组装数据
		ShopEvaluationDTO shopEvaluationDTO = new ShopEvaluationDTO();
		shopEvaluationDTO.setUserId(tradeOrders.getBuyerId());			//评价人id
		shopEvaluationDTO.setOrderId(tradeOrders.getOrderId());			//订单ID
		shopEvaluationDTO.setResource("1");								//1:默认回复 2:手动回复
//		shopEvaluationDTO.setUserShopId(userShopId);					//评价人店铺id
		shopEvaluationDTO.setByShopId(tradeOrders.getShopId());			//被评价店铺ID
		shopEvaluationDTO.setShopDescriptionScope(5);					//描述相符评分
		shopEvaluationDTO.setShopServiceScope(5);						//服务态度评分
		shopEvaluationDTO.setShopArrivalScope(5);						//到货速度评分
		shopEvaluationDTO.setCreateTime(new Date());					//创建时间
		shopEvaluationDTO.setModifyTime(new Date());					//编辑时间
		//保存-服务调用
		ExecuteResult<ShopEvaluationDTO> shopEvaluationResult = shopEvaluationService.addShopEvaluation(shopEvaluationDTO);
		if( !shopEvaluationResult.isSuccess() ){
			return "/对店铺评价-添加异常";
		} 
		return "true";
    }
    
    /*
     * 更新-订单状态：买家/卖家评价状态
     */
    private Boolean updateEvaluationStatus(String orderId,String status){
    	TradeOrdersDTO tradeOrdersDTO = new TradeOrdersDTO();
    	tradeOrdersDTO.setOrderId(orderId);
    	if("1".equals(status)){
    		tradeOrdersDTO.setEvaluate(2);
    	}else if("2".equals(status)){
    		tradeOrdersDTO.setSellerEvaluate(2);
    	}
    	ExecuteResult<String> result = tradeOrderExportService.modifyEvaluationStatus(tradeOrdersDTO);
    	if(!result.isSuccess()){
    		return false;
    	}
    	return true;
    }
}
