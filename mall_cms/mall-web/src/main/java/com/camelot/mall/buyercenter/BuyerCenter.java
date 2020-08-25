package com.camelot.mall.buyercenter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.basecenter.service.BaseConsultingSmsService;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.emums.CentralPurchasingActivityStatusEnum;
import com.camelot.mall.Constants;
import com.camelot.mall.centralPurchase.CentralPurchaseService;
import com.camelot.mall.home.FavouriteService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserCenterOperaService;
import com.camelot.usercenter.service.UserCreditExportService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/buyercenter")
public class BuyerCenter {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private ItemExportService itemService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private ItemSkuInquiryPriceExportService inquiryService;
	@Resource
	private BaseConsultingSmsService consultService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private UserCreditExportService userCreditExportService;
	@Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	@Resource
	private FavouriteService favouriteService;
	@Resource
	private TradeReturnExportService tradeReturnExportService;
	@Resource
	private UserCenterOperaService userCenterOperaService;
	@Resource
	private MallAdExportService mallAdExportService;
	@Resource
	private LogoExportService logoService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private CentralPurchaseService centralPurchaseService;
	@Resource
	private  QqCustomerService qqCustomerService;

	/*
	 * 买家基本信息加载
	 */
	@RequestMapping("/loadBuyerCenter")
	public String loadBuyerCenter(HttpServletRequest request, Integer state, Integer page,
			Model model) {

		/*//动态获取注册页面LOGO
		ExecuteResult<LogoDTO> logoDTOExecuteResult = logoService.getMallLogo();
		LogoDTO logoDTO = logoDTOExecuteResult.getResult();
		model.addAttribute("logoDTO", logoDTO);*/

		//查询条件对象
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		UserDTO userDTO=new UserDTO();
		Long userId=null;
	    userId = WebUtil.getInstance().getUserId(request);
	    ExecuteResult<List<Long>> ids = null;
		if(userId!=null){
			userDTO = userExportService.queryUserById(userId);
			ids = userExportService.queryUserIds(userId);
			model.addAttribute("userDTO",userDTO);
			if(ids.isSuccess()){
				inDTO.setBuyerIdList(ids.getResult());;
			}
		}else{
			return "redirect:/user/login";
		}

		if(state!=null && state>0){
			inDTO.setState(state);
		}
		//分页对象
		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		if(null != page){
			pager.setPage(page);
		}
		//买家标记/订单删除(1:无,2:是)
		inDTO.setUserType(1);
		inDTO.setDeleted(1);
		//待付款类型,默认为 1,  1是正常的  2 是分期付款
		String shipmentType = request.getParameter("shipmentType");
		model.addAttribute("shipmentType", StringUtils.isEmpty(shipmentType)?1:shipmentType);
//		if(state!=null&&(state==1||state==2)){
//			model.addAttribute("shipmentType", shipmentType==null?1:shipmentType);
//			inDTO.setShipmentType(Integer.parseInt(shipmentType));
//			if(state==1){
//				inDTO.setState(null);
//			}
//		}
		//查询待付款订单
		if(state ==  null || state ==  0){
			inDTO.setState(null);
		}else
		if(state == 1){
			//延期支付
			if("2".equals(shipmentType)){
				inDTO.setPaid(1);
				inDTO.setIsPayPeriod(1);
				//延期支付的订单搜索，可以根据订单状态查询
				inDTO.setState(null);
			}else{
				inDTO.setPaid(1);
				inDTO.setIsPayPeriod(2);
			}
			
		}else
		//查询待配送订单
		if(state == 2){
			//延期支付
			if("2".equals(shipmentType)){
				inDTO.setPaid(1);
				inDTO.setIsPayPeriod(1);
			}else{
				inDTO.setPaid(2);
				inDTO.setIsPayPeriod(2);
			}
		}
		
//		if("2".equals(shipmentType)){
//			model.addAttribute("shipmentType", Integer.parseInt(shipmentType));
//			inDTO.setState(null);
//			inDTO.setShipmentType(Integer.parseInt(shipmentType));
//		}else{
//			shipmentType = "1";
//			model.addAttribute("shipmentType", Integer.parseInt(shipmentType));
//			inDTO.setShipmentType(null);
//		}


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
				// 订单号加密
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", tradeOrdersDTO.getOrderId());
				LOG.info("加密后："+passKeyEr.getResult()+"；key"+userId+";orderId"+tradeOrdersDTO.getOrderId());
				if (passKeyEr.isSuccess()) {
					tradeOrdersDTO.setPassKey(passKeyEr.getResult());
				}
				//查询退款单id
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(tradeOrdersDTO.getOrderId().toString());
				tradeReturnDto.setDeletedFlag("0");
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
				//获取店铺名称
				ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
				if(result_shopDto!=null && result_shopDto.getResult()!=null){
					ShopDTO shopDTO = result_shopDto.getResult();
					jsonObject.put("shopId", tradeOrdersDTO.getShopId());
                    //获取qq客服站点
					List<Long> idlist = new ArrayList<Long>();
					idlist.add(tradeOrdersDTO.getSellerId());
					String stationId = qqCustomerService.getQqCustomerByIds(idlist, Constants.TYPE_SHOP);
                    if(!StringUtils.isBlank(stationId)){
                        jsonObject.put("stationId",stationId );
                    }else{
                        jsonObject.put("stationId", "无站点");
                    }
					jsonObject.put("shopName", shopDTO.getShopName());
				}
				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();

				JSONArray jsonArray_item = new JSONArray();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					JSONObject jsonObject_item = new JSONObject();
					if(dg!=null && dg.getTotal()>0){
						int orderState=tradeOrdersDTO.getState();
						for (TradeReturnGoodsDTO trg : dg.getRows()) {
							List<TradeReturnGoodsDetailDTO> details=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(trg.getId()));
							boolean flag=tradeOrderItemsDTO.getSkuId().equals(details.get(0).getSkuId());
							if(((new Integer(5).equals(trg.getState())||"0".equals(trg.getIsCustomerService())
									&&new Integer(5).equals(trg.getAfterService())&&orderState>3 && orderState<6)&&flag)){
									jsonObject_item.put("refundFlagg", false);
									jsonObject_item.put("refundId", trg.getId());// 退款单id
									jsonObject_item.put("afterService", trg.getAfterService());//售后服务
									continue;
								}
									if((orderState==2 ||orderState==3) && "0".equals(trg.getIsCustomerService())&&flag){
										jsonObject_item.put("refundFlag", true);
										jsonObject_item.put("refundId", trg.getId());// 退款单id
										jsonObject_item.put("afterService", trg.getAfterService());//售后服务
									}else if(((orderState>3 && orderState<6)||orderState==7) &&flag){
										jsonObject_item.put("refundFlag", true);
										jsonObject_item.put("refundId", trg.getId());// 退款单id
										jsonObject_item.put("afterService", trg.getAfterService());//售后服务
									}
						}
					}
					jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
					if(tradeOrdersDTO.getPaymentMethod()!=null&&tradeOrdersDTO.getPaymentMethod().equals(3)){
						jsonObject_item.put("payPrice", tradeOrderItemsDTO.getIntegral());
					}else{
						jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
					}
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
					//订单详情主键ID,根据该参数获取商品物流信息  (2015-11-17 xianmarui)
					jsonObject_item.put("orderItemId", tradeOrderItemsDTO.getOrderItemId());
					//2015-8-13宋文斌start
					ExecuteResult<String> result_attr = itemExportService.queryAttrBySkuId(tradeOrderItemsDTO.getSkuId());
					if (result_attr != null && result_attr.getResult() != null) {
						String attrStr = result_attr.getResult();
						//根据sku的销售属性keyId:valueId查询商品属性
						ExecuteResult<List<ItemAttr>> itemAttr = itemCategoryService.queryCatAttrByKeyVals(attrStr);
						jsonObject_item.put("itemAttr", itemAttr.getResult());
						// 鼠标移动到销售属性上显示title
						if (itemAttr.getResult() != null
								&& itemAttr.getResult().size() > 0) {
							String title = "";
							for (ItemAttr attr : itemAttr.getResult()) {
								title += attr.getName();
								title += "：";
								if (attr.getValues() != null
										&& attr.getValues().size() > 0) {
									boolean b = false;
									for (ItemAttrValue attrValue : attr
											.getValues()) {
										if(b){
											title += "，";
										}else{
											b = true;
										}
										title += attrValue.getName();
									}
								}
								title += " ";
							}
							jsonObject_item.put("itemAttrTitle", title);
						}
					}
					//2015-8-13宋文斌end
					
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						ItemDTO itemDTO = result_itemDTO.getResult();
						jsonObject_item.put("itemName", itemDTO.getItemName());
						//获取商品类型
						jsonObject_item.put("addSource", result_itemDTO.getResult().getAddSource());
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

		TradeOrdersQueryInDTO torInDTO = new TradeOrdersQueryInDTO();
		if(ids.isSuccess()){
			torInDTO.setBuyerIdList(ids.getResult());
		}
		//买家标记/订单删除(1:无,2:是)
		torInDTO.setUserType(1);
		torInDTO.setDeleted(1);
		model.addAttribute("pager", pagerObj);
		model.addAttribute("jsonArray", jsonArray);
		model.addAttribute("state",state);
		torInDTO.setState(null);
		model.addAttribute("allOrdersCount",tradeOrderExportService.queryOrderQty(torInDTO).getResult());
		torInDTO.setState(1);
		model.addAttribute("stayPayment",tradeOrderExportService.queryOrderQty(torInDTO).getResult());
		torInDTO.setState(2);
		model.addAttribute("stayDelivery",tradeOrderExportService.queryOrderQty(torInDTO).getResult());
		torInDTO.setState(3);
		model.addAttribute("stayReceipt",tradeOrderExportService.queryOrderQty(torInDTO).getResult());
		torInDTO.setState(4);
		model.addAttribute("evaluation",tradeOrderExportService.queryOrderQty(torInDTO).getResult());
		torInDTO.setState(5);
		model.addAttribute("successOrder",tradeOrderExportService.queryOrderQty(torInDTO).getResult());
		torInDTO.setState(6);
		model.addAttribute("cancleOrder",tradeOrderExportService.queryOrderQty(torInDTO).getResult());

		ExecuteResult<Long> totalIntegral =  userIntegralTrajectoryService.queryTotalIntegral(userId);
		model.addAttribute("totalIntegral",totalIntegral.getResult());

		//获取登录用户
		model.addAttribute("isHavePayPassword", userDTO.getIsHavePayPassword());

		return "/buyercenter/buyerCenter";
	}
	
	@RequestMapping("ajax_buyer")
	public String ajax_buyer(HttpServletRequest request,Pager<JSONObject> pagerJSON, Integer pageJSON,Model model,Integer tabFlag){
		Long userId = WebUtil.getInstance().getUserId(request);
		if(tabFlag==1){
			/*收藏商品*/
			ItemFavouriteDTO favourite = new ItemFavouriteDTO();
			favourite.setUserId(userId.intValue());
			String regionId = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
			if( regionId == null || "".equals(regionId) ){
				regionId = "0";
			}
			pagerJSON.setRows(5);
			if(null != pageJSON){
				pagerJSON.setPage(pageJSON);
			}
			DataGrid<JSONObject> dg = this.favouriteService.products(pagerJSON, favourite, regionId);
			LOG.debug("收藏商品列表："+JSON.toJSONString(dg));
			pagerJSON.setTotalCount(dg.getTotal().intValue());
			pagerJSON.setRecords(dg.getRows());
			LOG.debug(JSON.toJSONString(pagerJSON));
			model.addAttribute("pagerJSON", pagerJSON);
		}else if(tabFlag==2){
			//热销商品start--------------------------------------------------------------------------------------------
			//设置DUBBO第一个参数
			Pager<MallAdDTO> hotMallAdDTOPager = new Pager<MallAdDTO>();
			hotMallAdDTOPager.setRows(5);
			hotMallAdDTOPager.setPage(pagerJSON.getPage());//2015.7.2增加页数赋值
			if(null != pageJSON){
				hotMallAdDTOPager.setPage(pageJSON);
			}
			//设置DUBBO第二个参数
			MallAdQueryDTO hotMallAdQueryDTO = new MallAdQueryDTO();
			//猜你喜欢 的 商城广告
			hotMallAdQueryDTO.setAdType(5);
			//已发布 的 商城广告
			hotMallAdQueryDTO.setStatus(1);
			DataGrid<MallAdDTO> hotDataGrid = this.mallAdExportService.queryMallAdList(hotMallAdDTOPager,hotMallAdQueryDTO);
			LOG.debug("热销商品DataGrid："+JSON.toJSONString(hotDataGrid));
			hotMallAdDTOPager.setTotalCount(hotDataGrid.getTotal().intValue());
			hotMallAdDTOPager.setRecords(hotDataGrid.getRows());
			LOG.debug("热销商品Pager："+JSON.toJSONString(hotMallAdDTOPager));
			model.addAttribute("pagerJSON", hotMallAdDTOPager);
			//热销商品end--------------------------------------------------------------------------------------------
		}else{
			//集采商品start----------------------------------------------------------------------------------------------
			//设置DUBBO第一个参数
			Pager<QueryCentralPurchasingDTO> page = new Pager<QueryCentralPurchasingDTO>();
			QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
			page.setRows(5);
			page.setPage(pagerJSON.getPage());//2015.7.2增加页数赋值
			if(null != pageJSON){
				page.setPage(pageJSON);
			}
			queryCentralPurchasingDTO.setActivityStatus(CentralPurchasingActivityStatusEnum.PUBLISHED.getCode());
			// 科印平台
			queryCentralPurchasingDTO.setPlatformId(0);
			queryCentralPurchasingDTO.setActivityType(1);
			page = centralPurchaseService.getCentralPurchase(queryCentralPurchasingDTO,page);
			LOG.debug("集采商品Pager："+JSON.toJSONString(page));
			model.addAttribute("pagerJSON", page);
			//集采商品end----------------------------------------------------------------------------------------------
		}
		
		return "/buyercenter/ajax_buyerCenter";
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/inquiry")
	public String inquiry( HttpServletRequest request, Pager pager, ItemSkuInquiryPriceDTO dto, Model model ){
		model.addAttribute("inquiry", dto);
		// TODO GOMA TEMP 这里报错不用管，登录拦截器会拦截，进入此之前必须登录
		String ctoken = LoginToken.getLoginToken(request);
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		//没有认证通过的用户，不支持此功能
		if(register.getUserType() == 1){
			return "/no_authentication";
		}
		Long userId = register.getUid();
//		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(userId);
		dto.setBuyerId(userId);
//		dto.setBuyerIdList(idsEr.getResult());

		ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> er = this.inquiryService.queryList(dto, pager);
		DataGrid<ItemSkuInquiryPriceDTO> dg = er.getResult();

		LOG.debug(JSON.toJSONString(dg));

		JSONArray array = new JSONArray();
		for (ItemSkuInquiryPriceDTO inquiry : dg.getRows()) {
			JSONObject obj = JSON.parseObject(JSON.toJSONString(inquiry));
			UserDTO user = this.userExportService.queryUserById(inquiry.getSellerId());

			obj.put("sellerName", user.getUname());
			array.add(obj);
		}

		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords( array );

		model.addAttribute("pager", pager);

		return "/buyercenter/buyerInquiry";
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/consult")
	public String consult(HttpServletRequest request, Pager pager, BaseConsultingSmsDTO dto, Model model) {
		// TODO GOMA TEMP 这里报错不用管，登录拦截器会拦截，进入此之前必须登录
		String ctoken = LoginToken.getLoginToken(request);
		RegisterDTO register = userExportService.getUserByRedis(ctoken);
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(register.getUid());
//		dto.setBuyerId(userId);
		dto.setBuyerIdList(idsEr.getResult());
		DataGrid<BaseConsultingSmsDTO> dg = this.consultService.queryList(dto, pager);

		JSONArray consults = new JSONArray();
		for(BaseConsultingSmsDTO c : dg.getRows()){
			JSONObject consult = JSON.parseObject(JSON.toJSONString(c));
			ExecuteResult<ItemDTO> erItem = this.itemService.getItemById(c.getItemId());
			if(erItem.getResult() != null ){
				consult.put("itemName", erItem.getResult().getItemName());
				String[]picUrls = erItem.getResult().getPicUrls();
				consult.put("picUrl", picUrls.length > 0 ? picUrls[0] : "");
			}
			consults.add(consult);
		}

		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(consults);

		model.addAttribute("pager", pager);
		return "buyercenter/buyerConsult";
	}

	@RequestMapping("/favouriteProducts")
	public String favProduct(HttpServletRequest request,Pager<JSONObject> pager,ItemFavouriteDTO favourite,Model model){
		Long uid = WebUtil.getInstance().getUserId(request);
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(uid);
//		favourite.setUserId(Integer.valueOf(uid));
		favourite.setUserIdList(idsEr.getResult());

		String regionId = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		if( regionId == null || "".equals(regionId) ){
			regionId = "0";
		}
		Map<String, Object> map = this.favouriteService.productsMarketing(pager, favourite, regionId);
		
		DataGrid<JSONObject> dg = (DataGrid<JSONObject>) map.get("rlt");
		//满减
		JSONArray fullReduction = (JSONArray) map.get("fullReduction");
		//促销
		Map<String, BigDecimal> downMap = (Map<String, BigDecimal>) map.get("downMap");
		LOG.debug("收藏商品列表："+JSON.toJSONString(dg));

		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		LOG.debug(JSON.toJSONString(pager));

		model.addAttribute("pager", pager);
		model.addAttribute("downVal", downMap);
		model.addAttribute("fulljsonArray", fullReduction);
		return "/buyercenter/favourite_product";
	}


	@RequestMapping("/favouriteShops")
	public String shops(HttpServletRequest request,Pager<JSONObject> pager,ShopFavouriteDTO favourite,Model model){

		Long uid = WebUtil.getInstance().getUserId(request);
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(uid);
//		favourite.setUserId(Integer.valueOf(uid));
		favourite.setUserIdList(idsEr.getResult());

		DataGrid<JSONObject> dg = this.favouriteService.shops(pager, favourite);

		LOG.debug("收藏店铺列表："+JSON.toJSONString(dg));

		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		LOG.debug(JSON.toJSONString(pager));

		model.addAttribute("pager", pager);
		return "/buyercenter/favourite_shop";
	}

	@RequestMapping("/toSwitch")
	public String toAcctSwitch(HttpServletRequest request,Pager<JSONObject> pager,ShopFavouriteDTO favourite,Model model){
		return "/buyercenter/accountSwitch";
	}

	/**
	 * 切换主子账号申请
	 * @param request
	 * @param model
	 * @param reason 申请原因
	 * @return
	 */
	@RequestMapping("/accountSwitch")
	@ResponseBody
	public Map<String, String> acctSwitch(String reason, HttpServletRequest request, Model model){
		Map<String, String> result = new HashMap<String, String>();
		result.put("result","success");

		Long uid = WebUtil.getInstance().getUserId(request);
		UserDTO user = getUserByUid(uid);
		if(user == null){
			result.put("result","failure");
			return result;
		}
		if(user.getParentId()==null){//无父级账户无法申请
			result.put("result","noParentId");
			return result;
		}
		if(uid.equals(user.getParentId())){//自己不能向自己申请
			result.put("result","equalsAccount");
			return result;
		}

		/**校验是否已经切换过(作为父账号时)**/
        UserAuditLogDTO applyedAudit = new UserAuditLogDTO();
        applyedAudit.setUserId(uid);
        applyedAudit.setAuditStatus(1);//查询待审核的自主账号切换
        List<UserAuditLogDTO> applyedList = userCenterOperaService.findListByCondition(applyedAudit, UserEnums.UserOperaType.ZFZHSH);
        if(applyedList!=null && applyedList.size()>0){//已经申请过不能再次申请
        	result.put("result","hasApplyed");
			return result;
        }

		/**校验是否已经切换过(作为父账号时)**/
        UserAuditLogDTO changedAudit = new UserAuditLogDTO();
        changedAudit.setAuditId(uid);
        changedAudit.setAuditStatus(1);//查询待审核的
        List<UserAuditLogDTO> changedList = userCenterOperaService.findListByCondition(changedAudit, UserEnums.UserOperaType.ZFZHSH);
        if(changedList!=null && changedList.size()>0){//已经切换过不能再次切换
        	result.put("result","hasChanged");
			return result;
        }
        /**申请子主账号切换**/
        UserAuditLogDTO applyingAudit = new UserAuditLogDTO();
        applyingAudit.setUserId(uid);
        applyingAudit.setAuditId(user.getParentId());
        applyingAudit.setAuditStatus(1);//状态置为待审核
        applyingAudit.setRemark(reason);
        //申请切换主子账号
		userCenterOperaService.saveUserAuditLogDTO(applyingAudit, UserEnums.UserOperaType.ZFZHSH);

		return result;
	}

	/**
	 * 根据用户id获取用户信息
	 * @param uid
	 * @return
	 */
	private UserDTO getUserByUid(Long uid) {
		List<String> idlist = new ArrayList<String>();
		idlist.add(uid+"");
		ExecuteResult<List<UserDTO>> result = userExportService.findUserListByUserIds(idlist);
		List<UserDTO> listRecord = result.getResult();
		if(listRecord==null || listRecord.size()==0){
			return null;
		}
		return listRecord.get(0);
	}
	
}
