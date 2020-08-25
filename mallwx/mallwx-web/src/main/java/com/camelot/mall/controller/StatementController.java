package com.camelot.mall.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.tradecenter.dto.MonthlyStatementDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.MonthlyStatementExportService;
import com.camelot.tradecenter.service.TradeOrderExportService;

/** 
 * <p>Description: [对账单]</p>
 * Created on 2015-7-21
 * @author  <a href="mailto: ">wanghao </a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping(value = "/statement")
public class StatementController {
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private MonthlyStatementExportService monthlyStatementExportService;
	@Resource
	private ItemExportService itemService;
	private Logger logger = Logger.getLogger(this.getClass());
	/**
	 * <p>Discription:[对账单首页]</p>
	 * Created on 2015-7-24
	 * @return
	 * @throws Exception
	 * @author:[wanghao]
	 */
	@RequestMapping("/statementPage")
	public String toStatement(Model model,
							  HttpServletRequest request,String source) throws Exception {
		model.addAttribute("userType",source);
		return "/statement/statementsHome";
	}

	/**
	 * <p>Discription:[获取订单id]</p>
	 * Created on 2015-7-16
	 * @param model
	 * @return
	 * @throws Exception
	 * @author:[wanghao]
	 */
	@RequestMapping("/statementOrderId")
	public String showOrder(Model model) throws Exception {
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(inDTO, null);
		if (null != executeResult) {
			DataGrid<TradeOrdersDTO> dgd = executeResult.getResult();
			if (null != dgd) {
				List<TradeOrdersDTO> tod = dgd.getRows();
				for (TradeOrdersDTO tradeOrdersDTO : tod) {
					model.addAttribute("model", tradeOrdersDTO.getOrderId());
					//System.out.println("---------------------------" + tradeOrdersDTO.getOrderId());
				}
			}
		}
		return "/statement/order";
	}

	/**
	 * <p>Discription:[根据年份月份查询订单]</p>
	 * Created on 2015-7-14
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author:[wanghao]
	 */
	@RequestMapping("/statementMMinfo")
	@ResponseBody
	public String toOrder( HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = Constants.USER_ID, required = true) long uid,
			@RequestParam(value = "page", defaultValue = "1") int pageNo,
			@RequestParam(value="month")int month,
			@RequestParam(value="year")int year,
			@RequestParam(value = "pageSize", defaultValue = "8") int pageSize,
			@RequestParam(value="userType")String userType ) throws Exception {
		JSONObject modelJo=new JSONObject();
		modelJo.put("imageServerAddr", SysProperties.getProperty("ftp_server_dir"));

		//初始化一个dateformat
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
//		inDTO.setBuyerId(Long.valueOf(uid));

		ItemShopCartDTO itemShopCart = new ItemShopCartDTO();
		//---------------------------------------------------------------------------------//
		//设置时间间隔为一个月的第一天和一个月的最后一天
		String dateStr = year + "-" + month;
		String firstDayStr = dateStr + "-01 " + "00:00:00";
		int maxDay = this.getMaxDay(dateStr);
		String lastDayStr = dateStr + "-" + maxDay + " 23:59:59";
		//---------------------------------------------------------------------------------//
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startDate = df.parse(firstDayStr);
		Date endDate = df.parse(lastDayStr);
		inDTO.setCreateStart(startDate);
		inDTO.setCreateEnd(endDate);
		//判断是  买家-2 还是 卖家-3
		if("2".equals(userType)){
			inDTO.setBuyerId(uid);
			//买家
			inDTO.setUserType(1);
		}else if("3".equals(userType)){
			inDTO.setSellerId(uid);
			//卖家
			inDTO.setUserType(2);
		}
		
		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		pager.setPage(pageNo);
		pager.setRows(pageSize);
		
		List<Integer> stateList = new ArrayList<Integer>();// 订单状态组
		stateList.add(1);
		stateList.add(2);
		stateList.add(3);
		stateList.add(4);
		stateList.add(5);
		stateList.add(8);
		inDTO.setStateList(stateList);
		//查询结果
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = this.monthlyStatementExportService.queryOrders(inDTO, pager);
		JSONArray jsonArray = new JSONArray();
		if (executeResult != null && executeResult.isSuccess()) {
			DataGrid<TradeOrdersDTO> dataGrid = executeResult.getResult();
			if(null!=dataGrid){
				List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
				for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
					//把结果封装成json
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
					jsonObject.put("paymentPrice", tradeOrdersDTO.getPaymentPrice());//实际支付金额
					jsonObject.put("createTime", sf.format(tradeOrdersDTO.getCreateTime()));
					jsonObject.put("logisticsNo", tradeOrdersDTO.getLogisticsNo());//物流编号
					jsonObject.put("logisticsCompany", tradeOrdersDTO.getLogisticsCompany());//物流公司
					jsonObject.put("paid", tradeOrdersDTO.getPaid());
					jsonObject.put("refund", tradeOrdersDTO.getRefund());

					//--------------------------------------------------------------------------------------//

					jsonObject.put("refund", tradeOrdersDTO.getRefund());
					//转义订单状态
					//订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
					String ordreStatus = "";
					if(1==tradeOrdersDTO.getState()){
						ordreStatus = "待付款";
					}else if(2==tradeOrdersDTO.getState()){
						ordreStatus = "待配送";
					}else if(3==tradeOrdersDTO.getState()){
						ordreStatus = "待收货";
					}else if(4==tradeOrdersDTO.getState()){
						ordreStatus = "待评价";
					}else if(5==tradeOrdersDTO.getState()){
						ordreStatus = "已完成";
					}else if(6==tradeOrdersDTO.getState()){
						ordreStatus = "已取消";
					}else if(7==tradeOrdersDTO.getState()){
						ordreStatus = "待审核";
					}else if(8==tradeOrdersDTO.getState()){
						ordreStatus = "待确认";
					}
					jsonObject.put("ordreStatus", ordreStatus);
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();

					JSONArray jsonArray_item = new JSONArray();
					//获得订单中的商品的详细信息
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

							//商品属性
							String skuName = "";
							for(ItemAttr itemAttr : itemShopCartDTO.getAttrSales()){
								skuName += itemAttr.getName();
								for(ItemAttrValue itemAttrValue : itemAttr.getValues()){
									skuName += ":" + itemAttrValue.getName()+";";
								}
							}
							jsonObject_item.put("skuName", skuName);

						}else{
							jsonObject_item.put("skuPicUrl", "");
						}

						//获取商品名称
						jsonObject_item.put("itemId", tradeOrderItemsDTO.getItemId());
						ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
						if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
							ItemDTO itemDTO = result_itemDTO.getResult();
							jsonObject_item.put("itemName", itemDTO.getItemName());
						}
						jsonArray_item.add(jsonObject_item);
					}
					jsonObject.put("items", jsonArray_item);
					jsonArray.add(jsonObject);
				}
				pager.setTotalCount(dataGrid.getTotal().intValue());
				modelJo.put("pager", pager);
				modelJo.put("orderInfos", jsonArray);
			}else{
				//没有数据要展示
			}
		}
/*		String orderStatus="";
		if(StringUtils.isEmpty(orderStatus)){
			modelJo.addAttribute("orderStatus", "0");
		}else{
			modelJo.addAttribute("orderStatus", orderStatus);
		}*/
		return modelJo.toJSONString();
	}

	/**
	 * <p>Discription:[格式化时间]</p>
	 * Created on 2015-7-17
	 * @param str
	 * @return
	 * @throws Exception
	 * @author:[wanghao]
	 */
	public int getMaxDay(String str) throws Exception {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		Date date = df.parse(str);
		cal.setTime(date);
		int actualMaximum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		logger.info(actualMaximum);
		return actualMaximum;
	}

	/**
	 * 生成对账单
	 * @param model
	 * @param uid
	 * @param datas
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/newStatement")
	@ResponseBody
	public String saveStatement(Model model, @CookieValue(value = Constants.USER_ID, required = true) long uid,
			@RequestParam(value = "datas") String datas,@RequestParam(value = "userType")String userType, 
			HttpServletRequest request, HttpServletResponse response) {
		ExecuteResult<TradeOrdersDTO> tradeOrdersDTOResult ;
		//把前台传来的订单号json转成list
		List<String> jc = JSON.parseArray(datas, String.class);
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
		String dateStr=simpleDateFormat.format(new Date());
		String uuid=UUID.randomUUID().toString();
		String statementId=dateStr+uuid.substring(0,5);//14+5   生成唯一的对账单号  时间+随机串
		for (String orderId : jc) {
			MonthlyStatementDTO msDTO = new MonthlyStatementDTO();

			//根据订单号，获取订单状态，复制给对账单的对象
			tradeOrdersDTOResult = tradeOrderExportService.getOrderById(orderId);
			TradeOrdersDTO tradeOrdersDTO=tradeOrdersDTOResult.getResult();
			//msDTO.setTradeName(tsDTO.getItemName());
			msDTO.setOrderCode(tradeOrdersDTO.getOrderId());

			msDTO.setStatementId(statementId);//14+5
			msDTO.setOrderStates(tradeOrdersDTO.getState());//订单状态
			msDTO.setCreateBy(Long.toString(uid));
			msDTO.setCreateDate(new Date());
			msDTO.setActiveFlag(1);
			msDTO.setAlternateField1(userType);//买家或者卖家创建
			/**
			 * 订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
			 */


			//              金额未存(已付/未付/总金额)
			//根据订单的类型处理金额
			if (1 == tradeOrdersDTO.getState() || 6 == tradeOrdersDTO.getState() || 7 == tradeOrdersDTO.getState() || 8 == tradeOrdersDTO.getState()) {
				msDTO.setNpaidAmount(tradeOrdersDTO.getPaymentPrice().doubleValue());
			} else if (2 == tradeOrdersDTO.getState() || 3 == tradeOrdersDTO.getState() || 4 == tradeOrdersDTO.getState()
					|| 5 == tradeOrdersDTO.getState()) {
				msDTO.setPaidAmount(tradeOrdersDTO.getPaymentPrice().doubleValue());
			}
			msDTO.setAmount(tradeOrdersDTO.getPaymentPrice().doubleValue());
			//每一个对账单的订单占数据库一条数据
			monthlyStatementExportService.addMonthlyStatement(msDTO);
		}
		return "true";

	}

	/**
	 * <p>Discription:[根据对账单号和生成时间查询对账单]</p>
	 * Created on 2015-7-24
	 * @param request
	 * @param response
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 * @author:[wanghao]
	 */
	@RequestMapping("/showStatement")
	@ResponseBody
	public String showStatement( HttpServletRequest request, HttpServletResponse response,
			@CookieValue(value = Constants.USER_ID, required = true) long uid,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "8") int pageSize,
			@RequestParam(value = "statementId",required = false,defaultValue = "") String statementId,//对账单号
			@RequestParam(value = "createDate",required = false,defaultValue = "") String createDateStr)//对账单创建时间
		throws Exception {
		JSONObject modelJO=new JSONObject();

		ExecuteResult<DataGrid<JSONObject>> executeResult = new ExecuteResult<DataGrid<JSONObject>>();
		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		pager.setPage(pageNo);
		pager.setRows(Integer.MAX_VALUE);
		MonthlyStatementDTO msDto=new MonthlyStatementDTO();
		msDto.setCreateBy(Long.toString(uid));
		if(StringUtils.isNotBlank(createDateStr)){
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");

			msDto.setCreateDate(simpleDateFormat.parse(createDateStr));
		}

		msDto.setStatementId(statementId);
		//根据uid查询对账单结果，返回封装之后的结果（每个对账单内包含订单信息）
		
		String userType = request.getParameter("userType");
		msDto.setAlternateField1(userType);
		executeResult = monthlyStatementExportService.queryPageGroupByUid(msDto, pager);
		if (null != executeResult) {
			DataGrid<JSONObject> dg = executeResult.getResult();
			modelJO.put("rows",dg.getRows());
			pager.setTotalCount(dg.getTotal().intValue());
			modelJO.put("pager",pager);
		}
		return modelJO.toJSONString();
	}

	/**
	 * <p>Discription:[显示所有的对账单，目前无用的方法]</p>
	 * Created on 2015-7-25
	 * @param uid
	 * @param pageNo
	 * @param pageSize
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author:[wanghao]
	 */
	@RequestMapping("/selectAllStatement")
	public String selectAllStatement(@CookieValue(value = Constants.USER_ID, required = true) long uid,
			@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
			@RequestParam(value = "pageSize", defaultValue = "3") int pageSize,
			//			@RequestParam(value="statementDate") Date data,
			Model model, HttpServletRequest request, HttpServletResponse response) {

		MonthlyStatementDTO msDTO = new MonthlyStatementDTO();
		ExecuteResult<DataGrid<MonthlyStatementDTO>> executeResult = new ExecuteResult<DataGrid<MonthlyStatementDTO>>();
		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		pager.setPage(pageNo);
		pager.setRows(pageSize);
		msDTO.setCreateBy(Long.toString(uid));
		//		msDTO.setCreateDate(data);
		executeResult = monthlyStatementExportService.queryMonthlyStatementList(msDTO, pager);
		if (null != executeResult) {
			DataGrid<MonthlyStatementDTO> dgm = executeResult.getResult();
			if (null != dgm) {
				List<MonthlyStatementDTO> LDgm = dgm.getRows();
				model.addAttribute("allStatement", LDgm);
			}
		}
		return "/statement/statamentShowAll";
	}

}
