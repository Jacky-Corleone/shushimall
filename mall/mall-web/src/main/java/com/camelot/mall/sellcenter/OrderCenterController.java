package com.camelot.mall.sellcenter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.activity.service.ActivityStatementSerice;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.service.DictionaryService;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.common.enums.CiticEnums;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.delivery.service.DeliveryService;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.InvoiceDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrderItemsPackageDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersEnclosureDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.InvoiceExportService;
import com.camelot.tradecenter.service.TradeApprovedOrdersExportService;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.tradecenter.service.TradeOrderItemsPackageExportService;
import com.camelot.tradecenter.service.TradeOrdersEnclosureService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.WebUtil;

/**
 *
 * <p>
 * Description: [订单中心]
 * </p>
 * Created on 2015-3-10
 *
 * @author <a href="mailto: guochao@camelotchina.com">呙超</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping(value = "/order")
public class OrderCenterController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private TradeReturnExportService tradeReturnExportService;
	@Resource
	private PaymentExportService paymentExportService;
	@Resource
	private ComplainExportService complainExportService;
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private TradeApprovedOrdersExportService tradeApprovedOrdersExportService;
	@Resource
	private InvoiceExportService invoiceExportService;
	@Resource
	private DictionaryService dictionaryService;//物流公司字典接口

	@Resource
	private CiticExportService citicExportService;//中信接口

	@Resource
	private DeliveryService deliveryService;//物流（快递100）接口
	
	@Resource
	private ActivityStatementSerice activityStatementSerice;//优惠价格修改记录
	
	@Resource
	private TradeOrdersEnclosureService tradeOrdersEnclosureService;//订单附件
	
	@Resource
	private  QqCustomerService qqCustomerService;
	
	@Resource
	private  TradeOrderItemsPackageExportService tradeOrderItemsPackageExportService;
	
	/**
	 * 跳转到查看详情
	 *
	 * @param tradeOrdersQueryInDTO
	 * @param model
	 * @return
	 */
	@RequestMapping({ "detailPage" })
	public String form(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, Model model) {
		Pager<TradeOrdersDTO> rs_pager = new Pager<TradeOrdersDTO>();

		model.addAttribute("pager", rs_pager);
		model.addAttribute("pageState", 0); // 默认跳转第一个tab页面

		return "sellcenter/order/allOrder";
	}

	/**
	 *
	 * <p>Discription:[买家订单列表查询]</p>
	 * Created on 2015年3月16日
	 * @param tradeOrdersQueryInDTO
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "queryBuyer" })
	public String query(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, String approve,Integer page, HttpServletRequest request, Model model,Integer yq_state) {
		String status=request.getParameter("approveStatus");
//		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		Long uid = WebUtil.getInstance().getUserId(request);
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(uid);
		if(uid != null){
			if("loadApprove".equals(approve)){//订单审核
				tradeOrdersQueryInDTO.setAuditorId(uid);
				tradeOrdersQueryInDTO.setApproveStatus("0");//待审核状态订单
				tradeOrdersQueryInDTO.setNotApproved(false);
				if(StringUtils.isEmpty(status)){//如果审核状态为空的话，则为已提交页签
					tradeOrdersQueryInDTO.setAuditorId(null);
					tradeOrdersQueryInDTO.setApproveStatus(null);
					tradeOrdersQueryInDTO.setBuyerId(uid);
				}
			}else{
				tradeOrdersQueryInDTO.setNotApproved(true);
				if(idsEr.isSuccess()){
					tradeOrdersQueryInDTO.setBuyerIdList(idsEr.getResult());
				}
//				tradeOrdersQueryInDTO.setBuyerId(Long.valueOf(uid));
			} 
            model.addAttribute("userId", uid);
		}else{
			return "redirect:/user/login";
        }

		String selectTime = request.getParameter("selectTime");
		if(StringUtils.isNotEmpty(selectTime) && StringUtils.isNumeric(selectTime)){
			model.addAttribute("selectTime", Integer.parseInt(selectTime));
		}
		Integer pageState = tradeOrdersQueryInDTO.getState();
		//待付款类型,默认为 1,  1是正常的  2 是延期付款
		String shipmentType = request.getParameter("shipmentType");
		model.addAttribute("yq_state", yq_state);
		if (tradeOrdersQueryInDTO.getState() != null
				&& (tradeOrdersQueryInDTO.getState().intValue() == 1 
				|| tradeOrdersQueryInDTO.getState().intValue() == 2)) {
			model.addAttribute("shipmentType", StringUtils.isBlank(shipmentType) ? 1 : shipmentType);
		}
		//查询待付款订单
		if(pageState ==  null || pageState == 0){
			if(yq_state != null && yq_state != 0){
				tradeOrdersQueryInDTO.setState(yq_state);
			}else{
				tradeOrdersQueryInDTO.setState(null);
			}
		}else
		if(pageState == 1){
			//延期支付
			if("2".equals(shipmentType)){
				tradeOrdersQueryInDTO.setPaid(1);
				tradeOrdersQueryInDTO.setIsPayPeriod(1);
				//延期支付的订单搜索，可以根据订单状态查询
				if(yq_state != null && yq_state != 0){
					tradeOrdersQueryInDTO.setState(yq_state);
				}else{
					tradeOrdersQueryInDTO.setState(null);
				}
			}else{
				tradeOrdersQueryInDTO.setPaid(1);
				tradeOrdersQueryInDTO.setIsPayPeriod(2);
			}
			
		}else
		//查询待配送订单
		if(pageState == 2){
			//延期支付
			if("2".equals(shipmentType)){
				tradeOrdersQueryInDTO.setPaid(1);
				tradeOrdersQueryInDTO.setIsPayPeriod(1);
			}else{
				tradeOrdersQueryInDTO.setPaid(2);
				tradeOrdersQueryInDTO.setIsPayPeriod(2);
			}
		}
		String approveStatus=tradeOrdersQueryInDTO.getApproveStatus();
		//根据店铺名称查询
		String shopName = tradeOrdersQueryInDTO.getShopName();
		if(StringUtils.isNotEmpty(shopName)){
			List<Long> list = getListShopId(shopName);
			if(list!=null && list.size()==0){
				list.add(-1L);
			}
			tradeOrdersQueryInDTO.setShopIdList(list);
		}
		//根据商品名称查询
		String itemName = tradeOrdersQueryInDTO.getItemName();
		if(StringUtils.isNotEmpty(itemName)){
			tradeOrdersQueryInDTO.setItemIdList(getListItemId(itemName));
		}

		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		if(null != page){
			pager.setPage(page);
		}
		//买家标记/订单删除(1:无,2:是)
		tradeOrdersQueryInDTO.setUserType(1);
		tradeOrdersQueryInDTO.setDeleted(1);
		System.out.println("tradeOrdersQueryInDTO======="+JSON.toJSONString(tradeOrdersQueryInDTO));
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
		//System.out.println("executeResult==="+JSON.toJSONString(executeResult) );
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		JSONArray jsonArray = new JSONArray();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
				// 订单号加密
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", tradeOrdersDTO.getOrderId());
				logger.info("加密后："+passKeyEr.getResult()+"；key"+uid+";orderId"+tradeOrdersDTO.getOrderId());
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
					jsonObject.put("shopName", shopDTO.getShopName());
                    
                    //获取qq客服站点
					List<Long> idlist = new ArrayList<Long>();
					idlist.add(tradeOrdersDTO.getSellerId());
					String stationId = qqCustomerService.getQqCustomerByIds(idlist, Constants.TYPE_SHOP);
                    if(!StringUtils.isBlank(stationId)){
                        jsonObject.put("stationId",stationId );
                    }else{
                        jsonObject.put("stationId", "无站点");
                    }
                    
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
							//退款已关闭或者   退款退货一部分在进行售后，此时为部分商品已退货，并且订单状态为待评价或已完成
							if(((new Integer(5).equals(trg.getState())||"0".equals(trg.getIsCustomerService())
									&&new Integer(5).equals(trg.getAfterService())&&orderState>3 && orderState<6)&&flag)){
									jsonObject_item.put("refundFlag", false);//申请退款退货
									jsonObject_item.put("refundId", trg.getId());// 退款单id
									jsonObject_item.put("afterService", trg.getAfterService());//售后服务
									jsonObject_item.put("returnId", trg.getId());
									continue;
								}
									if((orderState==2 ||orderState==3) && "0".equals(trg.getIsCustomerService())
									  &&flag){//待配送或待确认收货并且申请退款退货
										jsonObject_item.put("refundFlag", true);//查看退款退货进度
										jsonObject_item.put("refundId", trg.getId());// 退款单id
										jsonObject_item.put("afterService", trg.getAfterService());//售后服务
									}else if(((orderState>3 && orderState<6)||orderState==7) && flag){//订单状态待评价已完成或者已关闭
										jsonObject_item.put("refundFlag", true);//查看退款退货进度
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

		Pager<TradeOrdersDTO> rs_pager = new Pager<TradeOrdersDTO>();
		rs_pager.setPage(pager.getPage());
		rs_pager.setTotalCount(dataGrid.getTotal().intValue());
		rs_pager.setRecords(dataGrid.getRows());

		if (rs_pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
			rs_pager.setEndPageIndex(rs_pager.getStartPageIndex());
		}

		model.addAttribute("tradeOrdersQueryInDTO", tradeOrdersQueryInDTO);
		model.addAttribute("pager", rs_pager);
		model.addAttribute("jsonArray", jsonArray);
		model.addAttribute("pageState", pageState);
		model.addAttribute("approveStatus",approveStatus);
		model.addAttribute("status",status);
		
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		if(idsEr.isSuccess()){
			inDTO.setBuyerIdList(idsEr.getResult());
		}
//		inDTO.setBuyerId(Long.parseLong(uid));
		//买家标记/订单删除(1:无,2:是)
		inDTO.setUserType(1);
		inDTO.setDeleted(1);
		System.out.println("inDTO======="+JSON.toJSONString(inDTO));
		inDTO.setState(null);
		model.addAttribute("allOrdersCount",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(1);
		model.addAttribute("stayPayment",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(2);
		model.addAttribute("stayDelivery",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(3);
		model.addAttribute("stayReceipt",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(4);
		model.addAttribute("evaluation",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(5);
		model.addAttribute("successOrder",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(6);
		model.addAttribute("cancleOrder",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		
		//System.out.println("tradeOrdersQueryInDTO==="+JSON.toJSONString(tradeOrdersQueryInDTO) );
		//System.out.println("pager==="+JSON.toJSONString(rs_pager));
		//System.out.println("jsonArray==="+JSON.toJSONString(jsonArray));
		//System.out.println("pageState==="+JSON.toJSONString(pageState));
		//获取登录用户
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO user = this.userExportService.queryUserById(userId);
		model.addAttribute("isHavePayPassword", user.getIsHavePayPassword());
		if("loadApprove".equals(approve)){
			return "sellcenter/order/order_approved_buyer";
		}else{
			return "sellcenter/order/order_buyer";
		}
	}
	public List<Long> getListShopId(String shopName){
		List<Long> listShopId = new ArrayList<Long>();
		if(StringUtils.isNotEmpty(shopName)){
			ShopDTO shopDTO = new ShopDTO();
			shopDTO.setShopName(shopName);
			ExecuteResult<DataGrid<ShopDTO>> result = shopExportService.findShopInfoByCondition(shopDTO, null);
			DataGrid<ShopDTO> dg = result.getResult();
			if(dg!=null){
				List<ShopDTO> listShopDTO = dg.getRows();
				if(listShopDTO.size()>0){
					for(ShopDTO dto : listShopDTO){
						listShopId.add(dto.getShopId());
					}
				}
			}
		}
		return listShopId;
	}

	/**
	 *
	 * <p>Discription:[卖家订单查询]</p>
	 * Created on 2015年3月16日
	 * @param tradeOrdersQueryInDTO
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "querySeller" })
	public String querySeller(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, Integer page, HttpServletRequest request, Model model,Integer yq_state) {

		Long uid = WebUtil.getInstance().getUserId(request);
		Long shopId = null;
		//-------------2015.7.6 修改获取shopid的方法  start-------------
		shopId = WebUtil.getInstance().getShopId(request);
		if (shopId == null) {
			return "redirect:/user/login";
		}
		
		/*String userToken = LoginToken.getLoginToken(request);
		if(StringUtils.isNotEmpty(userToken)){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				shopId = registerDto.getShopId();
				WebUtil.getInstance().getShopId(request);
			}
		}*/
		//----------2015.7.6 修改获取shopid的方法-end-------------------
		tradeOrdersQueryInDTO.setShopId(shopId);
		String selectTime = request.getParameter("selectTime");
		if(StringUtils.isNotEmpty(selectTime) && StringUtils.isNumeric(selectTime)){
			model.addAttribute("selectTime", Integer.parseInt(selectTime));
		}
		//待付款类型,默认为 1,  1是正常的  2 是分期付款
		String shipmentType = request.getParameter("shipmentType");
		Integer pageState = tradeOrdersQueryInDTO.getState();
		model.addAttribute("yq_state", yq_state);
		// 订单状态 1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
		if (tradeOrdersQueryInDTO.getState() != null
				&& (tradeOrdersQueryInDTO.getState().intValue() == 1 
				|| tradeOrdersQueryInDTO.getState().intValue() == 2)) {
			model.addAttribute("shipmentType", StringUtils.isBlank(shipmentType) ? 1 : shipmentType);
		}
//		if("2".equals(shipmentType)){
//			model.addAttribute("shipmentType", Integer.parseInt(shipmentType));
//			tradeOrdersQueryInDTO.setState(null);
//			tradeOrdersQueryInDTO.setShipmentType(Integer.parseInt(shipmentType));
//		}else{
//			shipmentType = "1";
//			model.addAttribute("shipmentType", Integer.parseInt(shipmentType));
//			tradeOrdersQueryInDTO.setShipmentType(null);
//		}
		//查询待付款订单
		if(pageState ==  null || pageState ==  0){
			if(yq_state != null && yq_state != 0){
				tradeOrdersQueryInDTO.setState(yq_state);
			}else{
				tradeOrdersQueryInDTO.setState(null);
			}
		}else
		if(pageState == 1){
			//延期支付
			if("2".equals(shipmentType)){
				tradeOrdersQueryInDTO.setPaid(1);
				tradeOrdersQueryInDTO.setIsPayPeriod(1);
				//延期支付的订单搜索，可以根据订单状态查询
				if(yq_state != null && yq_state != 0){
					tradeOrdersQueryInDTO.setState(yq_state);
				}else{
					tradeOrdersQueryInDTO.setState(null);
				}
			}else{
				tradeOrdersQueryInDTO.setPaid(1);
				tradeOrdersQueryInDTO.setIsPayPeriod(2);
			}
			
		}else
		//查询待配送订单
		if(pageState == 2){
			//延期支付
			if("2".equals(shipmentType)){
				tradeOrdersQueryInDTO.setPaid(1);
				tradeOrdersQueryInDTO.setIsPayPeriod(1);
			}else{
				tradeOrdersQueryInDTO.setPaid(2);
				tradeOrdersQueryInDTO.setIsPayPeriod(2);
			}
		}

		//根据商品名称查询
		String itemName = tradeOrdersQueryInDTO.getItemName();
		if(StringUtils.isNotEmpty(itemName)){
			tradeOrdersQueryInDTO.setItemIdList(getListItemId(itemName));
		}
		//根据买家名查询
		String buyerName = request.getParameter("buyerName");
		if(StringUtils.isNotEmpty(buyerName)){
			tradeOrdersQueryInDTO.setBuyerIdList(getListUid(buyerName));
		}
		//卖家标记
		tradeOrdersQueryInDTO.setUserType(2);

		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		if(null != page){
			pager.setPage(page);
		}
		tradeOrdersQueryInDTO.setDeleted(1);
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
		//System.out.println("executeResult==="+JSON.toJSONString(executeResult) );
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		JSONArray jsonArray = new JSONArray();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
				//查询退款单id
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(tradeOrdersDTO.getOrderId().toString());
				tradeReturnDto.setDeletedFlag("0");
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
				// 订单号加密
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", tradeOrdersDTO.getOrderId());
				logger.info("加密后："+passKeyEr+"；key:"+uid+";orderId:"+tradeOrdersDTO.getOrderId());
				if (passKeyEr.isSuccess()) {
					tradeOrdersDTO.setPassKey(passKeyEr.getResult());
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
					int count=0;
					JSONObject jsonObject_item = new JSONObject();
					if(dg!=null && dg.getTotal()>0){
						int orderState=tradeOrdersDTO.getState();
						for (TradeReturnGoodsDTO trg : dg.getRows()) {
							List<TradeReturnGoodsDetailDTO> details=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(trg.getId()));
									if(tradeOrderItemsDTO.getSkuId().equals(details.get(0).getSkuId())){
										jsonObject_item.put("refundId", trg.getId());// 退款单id
										jsonObject_item.put("afterService", trg.getAfterService());//售后服务
										Integer[] states = {TradeReturnStatusEnum.SUCCESS.getCode(),TradeReturnStatusEnum.REFUNDING.getCode(),TradeReturnStatusEnum.PLATFORMTOREFUND.getCode(),TradeReturnStatusEnum.PLATFORMDEALING.getCode(),
										    TradeReturnStatusEnum.REFUNDFAIL.getCode(),TradeReturnStatusEnum.REFUNDAPPLICATION.getCode(),TradeReturnStatusEnum.REFUNDAPPLICATION_CUP.getCode(),TradeReturnStatusEnum.WAITBUYERSUBMIT.getCode()
										};
										if(Arrays.asList(states).contains(trg.getState())){
											count+=details.get(0).getRerurnCount();
										}
									}
						    }
					}
					if( tradeOrderItemsDTO.getNum()==count){
						jsonObject_item.put("isShowDely",false);
					}else{
						jsonObject_item.put("isShowDely",true);
					}
					jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
					if(tradeOrdersDTO.getPaymentMethod()!=null&&tradeOrdersDTO.getPaymentMethod().equals(3)){
						jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice().toBigInteger());
					}else{
						jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
					}
					jsonObject_item.put("payPriceTotal", tradeOrderItemsDTO.getPayPriceTotal());
					jsonObject_item.put("promotionDiscount", tradeOrderItemsDTO.getPromotionDiscount());
					
					if (tradeOrderItemsDTO.getPromotionDiscount()==null) {
						tradeOrderItemsDTO.setPromotionDiscount(new BigDecimal(0.00));
					}
					if (tradeOrderItemsDTO.getCouponDiscount()==null) {
						tradeOrderItemsDTO.setCouponDiscount(new BigDecimal(0.00));
					}
					jsonObject_item.put("sumDiscount", tradeOrderItemsDTO.getPromotionDiscount()
							.add(tradeOrderItemsDTO.getCouponDiscount()));
					jsonObject_item.put("primitivePrice", tradeOrderItemsDTO.getPrimitivePrice());
					jsonObject_item.put("integralDiscount", tradeOrderItemsDTO.getIntegralDiscount());

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
					
					/*
					 * 2015-11-17 xianmarui start
					 */
					//订单详情主键ID,根据该参数获取商品物流信息  
					jsonObject_item.put("orderItemId", tradeOrderItemsDTO.getOrderItemId());
					//获取该商品对应的物流主键ID,根据该参数判断物流的新增和修改操作
					DeliveryDTO deliveryDTO = deliveryService.getDeliveryInfoByItemId(tradeOrderItemsDTO.getOrderItemId().toString());
					if(deliveryDTO!=null){
						jsonObject_item.put("deliveryId", deliveryDTO.getId());
					}else{
						jsonObject_item.put("deliveryId", "");
					}
					/*
					 * 2015-11-17 xianmarui end
					 */
					
					//2015-8-13宋文斌start
					ExecuteResult<String> result_attr = itemExportService.queryAttrBySkuId(tradeOrderItemsDTO.getSkuId());
					if (result_attr != null && result_attr.getResult() != null) {
						String attrStr = result_attr.getResult();
						//根据sku的销售属性keyId:valueId查询商品属性
						ExecuteResult<List<ItemAttr>> itemAttr = itemCategoryService.queryCatAttrByKeyVals(attrStr);
						jsonObject_item.put("itemAttr", itemAttr.getResult());
						// 鼠标移动到销售属性上显示title
						if (itemAttr.getResult() != null && itemAttr.getResult().size() > 0) {
							String title = "";
							for (ItemAttr attr : itemAttr.getResult()) {
								title += attr.getName();
								title += "：";
								if (attr.getValues() != null && attr.getValues().size() > 0) {
									boolean b = false;
									for (ItemAttrValue attrValue : attr.getValues()) {
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
					
					
					jsonObject_item.put("shopFreightTemplateId", tradeOrderItemsDTO.getShopFreightTemplateId());
					jsonArray_item.add(jsonObject_item);
				}
				jsonObject.put("items", jsonArray_item);
				jsonArray.add(jsonObject);
			}
		}

		Pager<TradeOrdersDTO> rs_pager = new Pager<TradeOrdersDTO>();
		rs_pager.setPage(pager.getPage());
		rs_pager.setTotalCount(dataGrid.getTotal().intValue());
		rs_pager.setRecords(dataGrid.getRows());

		if (rs_pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
			rs_pager.setEndPageIndex(rs_pager.getStartPageIndex());
		}

		model.addAttribute("tradeOrdersQueryInDTO", tradeOrdersQueryInDTO);
		model.addAttribute("pager", rs_pager);
		model.addAttribute("jsonArray", jsonArray);
		model.addAttribute("pageState", pageState);
		model.addAttribute("buyerName", buyerName);
		
		TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
		inDTO.setShopId(shopId);
		//卖家标记
		inDTO.setUserType(2);
		inDTO.setDeleted(1);
		inDTO.setState(null);
		model.addAttribute("allOrdersCount",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(1);
		model.addAttribute("stayPayment",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(2);
		model.addAttribute("stayDelivery",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(3);
		model.addAttribute("stayReceipt",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(4);
		model.addAttribute("evaluation",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(5);
		model.addAttribute("successOrder",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		inDTO.setState(6);
		model.addAttribute("cancleOrder",tradeOrderExportService.queryOrderQty(inDTO).getResult());
		//System.out.println("tradeOrdersQueryInDTO==="+JSON.toJSONString(tradeOrdersQueryInDTO) );
		//System.out.println("pager==="+JSON.toJSONString(rs_pager));
		//System.out.println("jsonArray==="+JSON.toJSONString(jsonArray));
		//System.out.println("pageState==="+JSON.toJSONString(pageState));
		return "sellcenter/order/order_seller";
	}

	public List<Long> getListUid(String buyerName){
		List<Long> listUid = new ArrayList<Long>();
		if(StringUtils.isNotEmpty(buyerName)){
			UserDTO userDTO = new UserDTO();
			userDTO.setUname(buyerName);
			DataGrid<UserDTO> dgUserDto = userExportService.findUserListByCondition(userDTO, UserType.BUYER, null);
			List<UserDTO> listUserDTO = dgUserDto.getRows();
			if(listUserDTO!=null && listUserDTO.size()>0){
				for(UserDTO user : listUserDTO){
					listUid.add(user.getUid());
				}
			}
		}
		return listUid;
	}

	public List<Long> getListItemId(String itemName){
		List<Long> listItemId = new ArrayList<Long>();
		if(StringUtils.isNotEmpty(itemName)){
			ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
			itemInDTO.setItemName(itemName);
			DataGrid<ItemQueryOutDTO> dg = itemService.queryItemList(itemInDTO, null);
			List<ItemQueryOutDTO> listDto = dg.getRows();
			if(listDto!=null && listDto.size()>0){
				for(ItemQueryOutDTO item : listDto){
					listItemId.add(item.getItemId());
				}
			}
		}
		return listItemId;
	}
	/**
	 *
	 * <p>Discription:[查看订单详情]</p>
	 * Created on 2015年3月17日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "queryOrderInfoBuyer" })
	public String queryOrderInfo(HttpServletRequest request, Model model) {
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = new ExecuteResult<DataGrid<TradeOrdersDTO>>();
		String orderId = request.getParameter("orderId");
		String approve=request.getParameter("approve");
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if(StringUtils.isEmpty(orderId)/* && !StringUtils.isNumeric(orderId)*/){
//			return "订单号不能为空，并且必须是数字";
			return "订单号不能为空";
		}
		Long userId = null;
		String userToken = LoginToken.getLoginToken(request);
		if(StringUtils.isNotEmpty(userToken)){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				userId = registerDto.getUid();
			}
		}
		TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
		tradeOrdersQueryInDTO.setOrderId(orderId);
		if("true".equals(approve)){
			tradeOrdersQueryInDTO.setNotApproved(false);//走审核流程
		}else{
			tradeOrdersQueryInDTO.setNotApproved(true);//不走审核流程
		}
		executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
		if(executeResult.getResult()!=null && executeResult.getResult().getRows()!=null && executeResult.getResult().getRows().size()>0){
			//根据订单id 只会查出一个订单，所以在这 get(0)
			TradeOrdersDTO tradeOrdersDTO = executeResult.getResult().getRows().get(0);
			ExecuteResult<List<Long>> list=userExportService.queryUserIds(userId);
			if(null!=list.getResult()&&list.getResult().size()>0){
				for(Long l:list.getResult()){
					if(l.longValue()==tradeOrdersDTO.getBuyerId().longValue()||userId.equals(tradeOrdersDTO.getAuditorId())){//买家id和当前登录用户id相等时
				// 查询订单的发票信息
				if (tradeOrdersDTO.getInvoice()!=null &&(tradeOrdersDTO.getInvoice() == 2 || tradeOrdersDTO.getInvoice() == 3)) {
					ExecuteResult<InvoiceDTO> result = invoiceExportService.queryByOrderId(tradeOrdersDTO.getOrderId());
					model.addAttribute("invoiceDTO", result.getResult());
				}
				//查询订单交易信息
				ExecuteResult<AccountInfoDto> accountInfoResult = citicExportService.getAccountInfoByUserIdAndAccountType(userId, CiticEnums.AccountType.AccBuyPay.getCode());
				if(accountInfoResult.isSuccess()){
					model.addAttribute("accountSuccess", "true");
					AccountInfoDto accountInfoDTO = accountInfoResult.getResult();
					//帐号
					String accountNo = accountInfoDTO.getSubAccNo();
					model.addAttribute("accountNo", accountNo);
					//账户名称
					String accountName = accountInfoDTO.getSubAccNm();
					model.addAttribute("accountName", accountName);
				}else{
					model.addAttribute("accountSuccess", "false");
				}
				model.addAttribute("tradeOrder", tradeOrdersDTO);
				//System.out.println("tradeOrder===="+JSON.toJSONString(tradeOrdersDTO));
				// 积分抵扣金额
				BigDecimal totalIntegralDiscount = BigDecimal.ZERO.setScale(2);
				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
				JSONArray jsonArray_item = new JSONArray();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					JSONObject jsonObject_item = new JSONObject();
					jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
			    	ItemShopCartDTO dto = new ItemShopCartDTO();
					dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
					dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
					dto.setQty(tradeOrderItemsDTO.getNum());//数量
					dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
					dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
					ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
					ItemShopCartDTO itemShopCartDTO = er.getResult();
					if( null != itemShopCartDTO){
						jsonObject_item.put("skuPicUrl", itemShopCartDTO.getSkuPicUrl());
					}else{
						jsonObject_item.put("skuPicUrl", "");
					}
					//获取商品名称
					jsonObject_item.put("itemId", tradeOrderItemsDTO.getItemId());
					
					//2015-8-17宋文斌start
					ExecuteResult<String> result_attr = itemExportService.queryAttrBySkuId(tradeOrderItemsDTO.getSkuId());
					if (result_attr != null && result_attr.getResult() != null) {
						String attrStr = result_attr.getResult();
						//根据sku的销售属性keyId:valueId查询商品属性
						ExecuteResult<List<ItemAttr>> itemAttr = itemCategoryService.queryCatAttrByKeyVals(attrStr);
						jsonObject_item.put("itemAttr", itemAttr.getResult());
						// 鼠标移动到销售属性上显示title
						if (itemAttr.getResult() != null && itemAttr.getResult().size() > 0) {
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
					//2015-8-17宋文斌end
					//===========================套装单品&基础服务&辅料类型&增值服务=START==============================
					//=========================================1.套装单品=========================================
					// key=套装商品skuId，value=套装单品
					Map<Long,JSONArray> subItemsMap = new HashMap<Long,JSONArray>();
					// 查询套餐单品
					TradeOrderItemsPackageDTO itemsPackageDTO = new TradeOrderItemsPackageDTO();
					itemsPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					itemsPackageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> result = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(itemsPackageDTO, null);
					if (result.isSuccess() && result.getResult() != null
							&& result.getResult().getTotal() != null && result.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : result.getResult().getRows()){
							JSONObject jsonObject_sub_item = new JSONObject();
							ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
							if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
								ItemDTO subItemDTO = itemDTOResult.getResult();
								// itemId
								jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
								jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
								// skuPicture
								ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
								shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
								shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
								shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
								shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
								shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
								ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
								if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
									jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
								} else {
									jsonObject_sub_item.put("skuPicUrl", "");
								}
								jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
								ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
								if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
									String attrStr = itemAttr.getResult();
									//根据sku的销售属性keyId:valueId查询商品属性
									ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
									jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
								}
								jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
								jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
								jsonArray_sub_item.add(jsonObject_sub_item);
							}
						}
						subItemsMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("subItemsMap", subItemsMap);
					//=========================================2.基础服务=========================================
					Set<Long> basicItemIds = new HashSet<Long>();
					// key=套装商品skuId，value=基础服务
					Map<Long,JSONArray> basicMap = new HashMap<Long,JSONArray>();
					// 查询套餐基础服务
					TradeOrderItemsPackageDTO basicPackageDTO = new TradeOrderItemsPackageDTO();
					basicPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					basicPackageDTO.setAddSource(ItemAddSourceEnum.BASICSERVICE.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> basicResult = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(basicPackageDTO, null);
					if (basicResult.isSuccess() && basicResult.getResult() != null
							&& basicResult.getResult().getTotal() != null && basicResult.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : basicResult.getResult().getRows()){
							if(!basicItemIds.contains(orderItemsPackageDTO.getSubItemId())){
								JSONObject jsonObject_sub_item = new JSONObject();
								ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
								if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
									ItemDTO subItemDTO = itemDTOResult.getResult();
									// itemId
									jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
									jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
									// skuPicture
									ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
									shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
									shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
									shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
									shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
									shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
									ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
									if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
										jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
									} else {
										jsonObject_sub_item.put("skuPicUrl", "");
									}
									jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
									ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
									if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
										String attrStr = itemAttr.getResult();
										//根据sku的销售属性keyId:valueId查询商品属性
										ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
										jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
									}
									jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
									jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
									jsonArray_sub_item.add(jsonObject_sub_item);
									basicItemIds.add(orderItemsPackageDTO.getSubItemId());
								}
							}
						}
						basicMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("basicMap", basicMap);
					//=========================================3.辅料类型=========================================
					Set<Long> auxiliaryItemIds = new HashSet<Long>();
					// key=套装商品skuId，value=辅料类型
					Map<Long,JSONArray> auxiliaryMap = new HashMap<Long,JSONArray>();
					// 查询套餐基础服务
					TradeOrderItemsPackageDTO auxiliaryPackageDTO = new TradeOrderItemsPackageDTO();
					auxiliaryPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					auxiliaryPackageDTO.setAddSource(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> auxiliaryResult = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(auxiliaryPackageDTO, null);
					if (auxiliaryResult.isSuccess() && auxiliaryResult.getResult() != null
							&& auxiliaryResult.getResult().getTotal() != null && auxiliaryResult.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : auxiliaryResult.getResult().getRows()){
							if(!auxiliaryItemIds.contains(orderItemsPackageDTO.getSubItemId())){
								JSONObject jsonObject_sub_item = new JSONObject();
								ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
								if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
									ItemDTO subItemDTO = itemDTOResult.getResult();
									// itemId
									jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
									jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
									// skuPicture
									ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
									shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
									shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
									shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
									shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
									shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
									ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
									if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
										jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
									} else {
										jsonObject_sub_item.put("skuPicUrl", "");
									}
									jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
									ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
									if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
										String attrStr = itemAttr.getResult();
										//根据sku的销售属性keyId:valueId查询商品属性
										ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
										jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
									}
									jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
									jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
									jsonArray_sub_item.add(jsonObject_sub_item);
									auxiliaryItemIds.add(orderItemsPackageDTO.getSubItemId());
								}
							}
						}
						auxiliaryMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("auxiliaryMap", auxiliaryMap);
					//=========================================4.增值服务=========================================
					// key=套装商品skuId，value=增值服务
					Map<Long,JSONArray> valueAddedMap = new HashMap<Long,JSONArray>();
					// 查询套餐单品
					TradeOrderItemsPackageDTO valueAddedPackageDTO = new TradeOrderItemsPackageDTO();
					valueAddedPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					valueAddedPackageDTO.setAddSource(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> valueAddedResult = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(valueAddedPackageDTO, null);
					if (valueAddedResult.isSuccess() && valueAddedResult.getResult() != null
							&& valueAddedResult.getResult().getTotal() != null && valueAddedResult.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : valueAddedResult.getResult().getRows()){
							JSONObject jsonObject_sub_item = new JSONObject();
							ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
							if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
								ItemDTO subItemDTO = itemDTOResult.getResult();
								// itemId
								jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
								jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
								// skuPicture
								ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
								shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
								shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
								shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
								shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
								shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
								ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
								if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
									jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
								} else {
									jsonObject_sub_item.put("skuPicUrl", "");
								}
								jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
								ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
								if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
									String attrStr = itemAttr.getResult();
									//根据sku的销售属性keyId:valueId查询商品属性
									ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
									jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
								}
								jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
								jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
								jsonArray_sub_item.add(jsonObject_sub_item);
							}
						}
						valueAddedMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("valueAddedMap", valueAddedMap);
					// 增值服务结束
					//=========================================套装单品&基础服务&辅料类型&增值服务=END============================================
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						ItemDTO itemDTO = result_itemDTO.getResult();
						jsonObject_item.put("itemName", itemDTO.getItemName());
					}
					jsonArray_item.add(jsonObject_item);
					if (tradeOrderItemsDTO.getIntegralDiscount()!=null) {
						totalIntegralDiscount = totalIntegralDiscount.add(tradeOrderItemsDTO.getIntegralDiscount());
					}
				}
				//判断是否为
				String isServiceItem = "0";
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						if(result_itemDTO.getResult().getAddSource() < 4){
							isServiceItem = "1";
							break;
						}
					}
				}
				model.addAttribute("isServiceItem", isServiceItem);
				model.addAttribute("jsonArray", jsonArray_item);
				model.addAttribute("totalIntegralDiscount", totalIntegralDiscount);
				//System.out.println("jsonArray===="+JSON.toJSONString(jsonArray_item));
			}
				}
			}
			//查询出订单的附件
			TradeOrdersEnclosureDTO dto = new TradeOrdersEnclosureDTO();
			dto.setOrderId(orderId);
			dto.setIsDelete(0);
			DataGrid<TradeOrdersEnclosureDTO> urlResult =tradeOrdersEnclosureService.queryEnclosures(dto, new Pager());
			if(null != urlResult.getRows() && !urlResult.getRows().isEmpty() && urlResult.getRows().size()>0){
				model.addAttribute("EnclosureList", urlResult.getRows());
				for (TradeOrdersEnclosureDTO enclosureDTO : urlResult.getRows()) {
					if (enclosureDTO.getRemark()!=null && !"".equals(enclosureDTO.getRemark())) {
						if (enclosureDTO.getType() == 0) {
							model.addAttribute("enclosureId", enclosureDTO.getId());
							model.addAttribute("enclosureRemark", enclosureDTO.getRemark());
						}else {
							model.addAttribute("projectRemark", enclosureDTO.getRemark());
						}
					}
				}
			}
		}else{
			model.addAttribute("tradeOrder", null);
			model.addAttribute("jsonArray", null);
			model.addAttribute("EnclosureList", null);
			model.addAttribute("isServiceItem", null);
		}
		return "sellcenter/order/orderInfo_buyer";
	}
	@RequestMapping({ "queryOrderInfoSeller" })
	public String queryOrderInfoSeller(HttpServletRequest request, Model model) {
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = new ExecuteResult<DataGrid<TradeOrdersDTO>>();
		String orderId = request.getParameter("orderId");
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
//			return "订单号不能为空，并且必须是数字";
			return "订单号不能为空";
		}
		Long userId = null;
		String userToken = LoginToken.getLoginToken(request);
		if(StringUtils.isNotEmpty(userToken)){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				userId = registerDto.getUid();
			}
		}
		UserDTO userDto = userExportService.queryUserById(userId);
		if(userDto!=null){
			if(userDto.getParentId()!=null){
				userId = userDto.getParentId();
			}
		}
		TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
		tradeOrdersQueryInDTO.setOrderId(orderId);
		executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
		if(executeResult.getResult()!=null && executeResult.getResult().getRows()!=null && executeResult.getResult().getRows().size()>0){
			//根据订单id 只会查出一个订单，所以在这 get(0)
			TradeOrdersDTO tradeOrdersDTO = executeResult.getResult().getRows().get(0);
			if(userId.longValue() == tradeOrdersDTO.getSellerId().longValue()){//卖家id和当前登录用户id相等时
				
				// 查询订单的发票信息
				if (tradeOrdersDTO.getInvoice() != null && ( tradeOrdersDTO.getInvoice() == 2 || tradeOrdersDTO.getInvoice() == 3)) {
					ExecuteResult<InvoiceDTO> result = invoiceExportService.queryByOrderId(tradeOrdersDTO.getOrderId());
					model.addAttribute("invoiceDTO", result.getResult());
				}
				
				model.addAttribute("tradeOrder", tradeOrdersDTO);
				//System.out.println("tradeOrder===="+JSON.toJSONString(tradeOrdersDTO));
				// 积分抵扣金额
				BigDecimal totalIntegralDiscount = BigDecimal.ZERO.setScale(2);
				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
				JSONArray jsonArray_item = new JSONArray();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					JSONObject jsonObject_item = new JSONObject();
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
					
					//2015-8-17宋文斌start
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
					//2015-8-17宋文斌end
					//===========================套装单品&基础服务&辅料类型&增值服务=START==============================
					//=========================================1.套装单品=========================================
					// key=套装商品skuId，value=套装单品
					Map<Long,JSONArray> subItemsMap = new HashMap<Long,JSONArray>();
					// 查询套餐单品
					TradeOrderItemsPackageDTO itemsPackageDTO = new TradeOrderItemsPackageDTO();
					itemsPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					itemsPackageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> result = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(itemsPackageDTO, null);
					if (result.isSuccess() && result.getResult() != null
							&& result.getResult().getTotal() != null && result.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : result.getResult().getRows()){
							JSONObject jsonObject_sub_item = new JSONObject();
							ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
							if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
								ItemDTO subItemDTO = itemDTOResult.getResult();
								// itemId
								jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
								jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
								// skuPicture
								ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
								shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
								shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
								shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
								shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
								shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
								ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
								if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
									jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
								} else {
									jsonObject_sub_item.put("skuPicUrl", "");
								}
								jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
								ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
								if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
									String attrStr = itemAttr.getResult();
									//根据sku的销售属性keyId:valueId查询商品属性
									ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
									jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
								}
								jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
								jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
								jsonArray_sub_item.add(jsonObject_sub_item);
							}
						}
						subItemsMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("subItemsMap", subItemsMap);
					//=========================================2.基础服务=========================================
					Set<Long> basicItemIds = new HashSet<Long>();
					// key=套装商品skuId，value=基础服务
					Map<Long,JSONArray> basicMap = new HashMap<Long,JSONArray>();
					// 查询套餐基础服务
					TradeOrderItemsPackageDTO basicPackageDTO = new TradeOrderItemsPackageDTO();
					basicPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					basicPackageDTO.setAddSource(ItemAddSourceEnum.BASICSERVICE.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> basicResult = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(basicPackageDTO, null);
					if (basicResult.isSuccess() && basicResult.getResult() != null
							&& basicResult.getResult().getTotal() != null && basicResult.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : basicResult.getResult().getRows()){
							if(!basicItemIds.contains(orderItemsPackageDTO.getSubItemId())){
								JSONObject jsonObject_sub_item = new JSONObject();
								ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
								if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
									ItemDTO subItemDTO = itemDTOResult.getResult();
									// itemId
									jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
									jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
									// skuPicture
									ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
									shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
									shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
									shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
									shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
									shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
									ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
									if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
										jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
									} else {
										jsonObject_sub_item.put("skuPicUrl", "");
									}
									jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
									ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
									if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
										String attrStr = itemAttr.getResult();
										//根据sku的销售属性keyId:valueId查询商品属性
										ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
										jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
									}
									jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
									jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
									jsonArray_sub_item.add(jsonObject_sub_item);
									basicItemIds.add(orderItemsPackageDTO.getSubItemId());
								}
							}
						}
						basicMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("basicMap", basicMap);
					//=========================================3.辅料类型=========================================
					Set<Long> auxiliaryItemIds = new HashSet<Long>();
					// key=套装商品skuId，value=辅料类型
					Map<Long,JSONArray> auxiliaryMap = new HashMap<Long,JSONArray>();
					// 查询套餐基础服务
					TradeOrderItemsPackageDTO auxiliaryPackageDTO = new TradeOrderItemsPackageDTO();
					auxiliaryPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					auxiliaryPackageDTO.setAddSource(ItemAddSourceEnum.AUXILIARYMATERIAL.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> auxiliaryResult = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(auxiliaryPackageDTO, null);
					if (auxiliaryResult.isSuccess() && auxiliaryResult.getResult() != null
							&& auxiliaryResult.getResult().getTotal() != null && auxiliaryResult.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : auxiliaryResult.getResult().getRows()){
							if(!auxiliaryItemIds.contains(orderItemsPackageDTO.getSubItemId())){
								JSONObject jsonObject_sub_item = new JSONObject();
								ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
								if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
									ItemDTO subItemDTO = itemDTOResult.getResult();
									// itemId
									jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
									jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
									// skuPicture
									ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
									shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
									shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
									shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
									shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
									shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
									ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
									if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
										jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
									} else {
										jsonObject_sub_item.put("skuPicUrl", "");
									}
									jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
									ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
									if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
										String attrStr = itemAttr.getResult();
										//根据sku的销售属性keyId:valueId查询商品属性
										ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
										jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
									}
									jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
									jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
									jsonArray_sub_item.add(jsonObject_sub_item);
									auxiliaryItemIds.add(orderItemsPackageDTO.getSubItemId());
								}
							}
						}
						auxiliaryMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("auxiliaryMap", auxiliaryMap);
					//=========================================4.增值服务=========================================
					// key=套装商品skuId，value=增值服务
					Map<Long,JSONArray> valueAddedMap = new HashMap<Long,JSONArray>();
					// 查询套餐单品
					TradeOrderItemsPackageDTO valueAddedPackageDTO = new TradeOrderItemsPackageDTO();
					valueAddedPackageDTO.setOrderItemsId(tradeOrderItemsDTO.getOrderItemId());
					valueAddedPackageDTO.setAddSource(ItemAddSourceEnum.VALUEADDEDSERVICE.getCode());
					ExecuteResult<DataGrid<TradeOrderItemsPackageDTO>> valueAddedResult = tradeOrderItemsPackageExportService
							.queryTradeOrderItemsPackageDTOs(valueAddedPackageDTO, null);
					if (valueAddedResult.isSuccess() && valueAddedResult.getResult() != null
							&& valueAddedResult.getResult().getTotal() != null && valueAddedResult.getResult().getTotal() > 0) {
						JSONArray jsonArray_sub_item = new JSONArray();
						for(TradeOrderItemsPackageDTO orderItemsPackageDTO : valueAddedResult.getResult().getRows()){
							JSONObject jsonObject_sub_item = new JSONObject();
							ExecuteResult<ItemDTO> itemDTOResult = itemService.getItemById(orderItemsPackageDTO.getSubItemId());
							if(itemDTOResult.isSuccess() && itemDTOResult.getResult() != null){
								ItemDTO subItemDTO = itemDTOResult.getResult();
								// itemId
								jsonObject_sub_item.put("itemId", orderItemsPackageDTO.getSubItemId());
								jsonObject_sub_item.put("skuId", orderItemsPackageDTO.getSubSkuId());
								// skuPicture
								ItemShopCartDTO shopCartDTO = new ItemShopCartDTO();
								shopCartDTO.setAreaCode(tradeOrderItemsDTO.getAreaId() + "");// 省市区编码
								shopCartDTO.setSkuId(orderItemsPackageDTO.getSubSkuId());// SKUid
								shopCartDTO.setQty(orderItemsPackageDTO.getSubNum());// 数量
								shopCartDTO.setShopId(subItemDTO.getShopId());// 店铺ID
								shopCartDTO.setItemId(orderItemsPackageDTO.getSubItemId());// 商品ID
								ExecuteResult<ItemShopCartDTO> shopCartDTOResult = itemService.getSkuShopCart(shopCartDTO); //调商品接口查url
								if (shopCartDTOResult.isSuccess() && null != shopCartDTOResult.getResult()) {
									jsonObject_sub_item.put("skuPicUrl", shopCartDTOResult.getResult().getSkuPicUrl());
								} else {
									jsonObject_sub_item.put("skuPicUrl", "");
								}
								jsonObject_sub_item.put("itemName", subItemDTO.getItemName());
								ExecuteResult<String> itemAttr = itemExportService.queryAttrBySkuId(orderItemsPackageDTO.getSubSkuId());
								if (itemAttr.isSuccess() && StringUtils.isNotBlank(itemAttr.getResult())) {
									String attrStr = itemAttr.getResult();
									//根据sku的销售属性keyId:valueId查询商品属性
									ExecuteResult<List<ItemAttr>> itemAttrs = itemCategoryService.queryCatAttrByKeyVals(attrStr);
									jsonObject_sub_item.put("itemAttr", itemAttrs.getResult());
								}
								jsonObject_sub_item.put("skuPrice", orderItemsPackageDTO.getSubSkuPrice());
								jsonObject_sub_item.put("num", orderItemsPackageDTO.getSubNum());
								jsonArray_sub_item.add(jsonObject_sub_item);
							}
						}
						valueAddedMap.put(tradeOrderItemsDTO.getSkuId(), jsonArray_sub_item);
					}
					model.addAttribute("valueAddedMap", valueAddedMap);
					// 增值服务结束
					//=========================================套装单品&基础服务&辅料类型&增值服务=END============================================
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						ItemDTO itemDTO = result_itemDTO.getResult();
						jsonObject_item.put("itemName", itemDTO.getItemName());
					}
					jsonArray_item.add(jsonObject_item);
					if (tradeOrderItemsDTO.getIntegralDiscount()!=null) {
						totalIntegralDiscount = totalIntegralDiscount.add(tradeOrderItemsDTO.getIntegralDiscount());
					}
				}
				
				//判断是否为
				String isServiceItem = "0";
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						if(result_itemDTO.getResult().getAddSource() < 4){
							isServiceItem = "1";
							break;
						}
					}
				}
				model.addAttribute("isServiceItem", isServiceItem);
				model.addAttribute("jsonArray", jsonArray_item);
				model.addAttribute("totalIntegralDiscount", totalIntegralDiscount);
				//System.out.println("jsonArray===="+JSON.toJSONString(jsonArray_item));
				//查询出订单的附件
				TradeOrdersEnclosureDTO dto = new TradeOrdersEnclosureDTO();
				dto.setOrderId(orderId);
				dto.setIsDelete(0);
				DataGrid<TradeOrdersEnclosureDTO> urlResult =tradeOrdersEnclosureService.queryEnclosures(dto, new Pager());
				if(null != urlResult.getRows() && !urlResult.getRows().isEmpty() && urlResult.getRows().size()>0){
					model.addAttribute("EnclosureList", urlResult.getRows());
					for (TradeOrdersEnclosureDTO enclosureDTO : urlResult.getRows()) {
						if (enclosureDTO.getRemark()!=null && !"".equals(enclosureDTO.getRemark())) {
							if (enclosureDTO.getType() == 0) {
								model.addAttribute("enclosureId", enclosureDTO.getId());
								model.addAttribute("enclosureRemark", enclosureDTO.getRemark());
							}else {
								model.addAttribute("projectRemark", enclosureDTO.getRemark());
							}
						}
					}
				}
			}
		}else{
			model.addAttribute("tradeOrder", null);
			model.addAttribute("jsonArray", null);
			model.addAttribute("EnclosureList", null);
			model.addAttribute("isServiceItem", null);
		}
		return "sellcenter/order/orderInfo_seller";
	}
	@ResponseBody
	@RequestMapping({ "modifyOrderStatus" })
	public ExecuteResult<String> modifyOrderStatus(HttpServletRequest request, Model model) {
		String orderId = request.getParameter("orderId");
		String orderStatus = request.getParameter("orderStatus");
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errs = new ArrayList<String>();
		if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
//			errs.add("订单号不能为空，并且必须是数字");
			errs.add("订单号不能为空");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		if(StringUtils.isEmpty(orderStatus) && !StringUtils.isNumeric(orderStatus)){
			errs.add("订单状态不能为空，并且必须是数字");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		result = tradeOrderExportService.modifyOrderStatus(orderId, Integer.parseInt(orderStatus));
		return result;
	}
	/**
	 * 
	 * <p>Discription:[取消订单]</p>
	 * Created on 2015年5月22日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "cancelOrder" })
	public ExecuteResult<String> cancelOrder(HttpServletRequest request, Model model) {
		String orderId = request.getParameter("orderId");
		String orderStatus = request.getParameter("orderStatus");
		ExecuteResult<String> result = new ExecuteResult<String>();
		request.setAttribute("result",result);
		List<String> errs = new ArrayList<String>();
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
//			errs.add("订单号不能为空，并且必须是数字");
			errs.add("订单号不能为空");
			result.setErrorMessages(errs);
			return result;
		}
		if(StringUtils.isEmpty(orderStatus) && !StringUtils.isNumeric(orderStatus)){
			errs.add("订单状态不能为空，并且必须是数字");
			result.setErrorMessages(errs);
			return result;
		}
		ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
		if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
			Integer nstate = resultTradeOrdersDTO.getResult().getState();
			Integer paid = resultTradeOrdersDTO.getResult().getPaid();
			if(nstate==1 ||( paid == 1 && nstate <3)){//待付款
				result = tradeOrderExportService.modifyOrderStatus(orderId, Integer.parseInt(orderStatus));
			}else{
				errs.add("订单不是待付款状态");
				result.setErrorMessages(errs);
				return result;
			}
		}
		return result;
	}
	/**
	 *
	 * <p>Discription:[卖家： 确认发货]</p>
	 * Created on 2015年4月27日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "deliverOrder" })
	public ExecuteResult<String> deliverOrder(HttpServletRequest request, Model model) {
        String orderId = request.getParameter("orderId");
        String state = request.getParameter("state");
        ExecuteResult<String> result = new ExecuteResult<String>();
        request.setAttribute("result", result);
        List<String> errs = new ArrayList<String>();
        try{
	//       	退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
	//          if(!StringUtils.isNumeric(orderId)){
	//              errs.add("订单号不能为空，并且必须是数字");
	//              result.setErrorMessages(errs);
	//              return result;
	//          }
		      if(StringUtils.isBlank(orderId)){
		          errs.add("订单号不能为空");
		          result.setErrorMessages(errs);
		          return result;
		      }
            if(!StringUtils.isNumeric(state)){
                errs.add("订单状态不能为空，并且必须是数字");
                result.setErrorMessages(errs);
                return result;
            }
            ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
            if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
                Integer nstate = resultTradeOrdersDTO.getResult().getState();
                Integer refund = resultTradeOrdersDTO.getResult().getRefund();
                if(nstate==2){//待配送 状态
                    TradeOrdersDTO orderDTO = new TradeOrdersDTO();
                    orderDTO.setOrderId(orderId);
                    orderDTO.setDeliverTime(new Date());//发货时间
                    if(refund == 2){//订单是在申请退款
                        orderDTO.setRefund(1);
                        //先查出退款订单，然后再修改退款订单
                        TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
                        TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
                        tradeReturnDto.setOrderId(orderId);
                        queryDto.setTradeReturnDto(tradeReturnDto);
                        DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
                        if(dg!=null && dg.getTotal()>0){
                            for(TradeReturnGoodsDTO dto : dg.getRows()){
                            	//待平台处理，平台处里中，退款失败，退款申请成功,等待同意退款，待买家收到货款确认发货,退款成功，即卖家同意退款后不关闭退款流程
                            	if(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode()!=dto.getState()&&TradeReturnStatusEnum.PLATFORMDEALING.getCode()!=dto.getState()
                            			&&TradeReturnStatusEnum.REFUNDING.getCode()!=dto.getState()&&TradeReturnStatusEnum.SUCCESS.getCode()!=dto.getState()
                            			&&TradeReturnStatusEnum.REFUNDFAIL.getCode()!=dto.getState()&&TradeReturnStatusEnum.REFUNDAPPLICATION.getCode()!=dto.getState()
                            			&&TradeReturnStatusEnum.REFUNDAPPLICATION_CUP.getCode()!=dto.getState()){
                            		//dto.setDeletedFlag("1");
                                    dto.setState(TradeReturnStatusEnum.CLOSE.getCode());
                                    tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.CLOSE);
                                    //取消退款单的投诉
                                    complainCancle1(dto.getId().toString());
                            	}
                            }
                        }
                    }
                    tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);
                    result = tradeOrderExportService.modifyOrderStatus(orderId, 3);
                }
            }
        } catch (Exception e) {
            errs.add(e.getMessage());
            result.setErrorMessages(errs);
            e.printStackTrace();
        }
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[校验用户是否设置了支付密码]</p>
	 * Created on 2016年3月8日
	 * @param request
	 * @param model
	 * @return
	 * @author:[宋文斌]
	 */
	@ResponseBody
	@RequestMapping({ "validateExistPaymentCode" })
	public ExecuteResult<String> validateExistPaymentCode(HttpServletRequest request, Model model){
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errs = new ArrayList<String>();
		Long uid = WebUtil.getInstance().getUserId(request);
		if (uid == null) {
			errs.add("用户未登录！");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		ExecuteResult<String> executeResult = userExportService.validateExistPaymentCode(uid);
		if ("0".equals(executeResult.getResult())) {
			errs.add("请先设置支付密码，再确认收货！");
			result.setErrorMessages(errs);
			request.setAttribute("result", result);
			return result;
		}
		return result;
	}
	/**
	 *
	 * <p>Discription:[确认收货]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @param model
	 * @return
	 * @author:[创建者中文名字]
	 */
	@ResponseBody
	@RequestMapping({ "confirmReceipt" })
	public ExecuteResult<String> confirmReceipt(HttpServletRequest request, Model model) {
		int groType = 0;  //用于判断是否确认收货成功   0 不成功  1  成功
		BigDecimal payValue = null; //  最后支付的金额   用于成长值拦截器  传递该参数
		String orderId = request.getParameter("orderId");
		String userToken = LoginToken.getLoginToken(request);
		String paypwd = request.getParameter("paypwd");
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errs = new ArrayList<String>();
		if(StringUtils.isEmpty(userToken)){
			errs.add("用户未登录！");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
		if(registerDto==null){
			errs.add("未找到登录用户的信息！");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if (StringUtils.isEmpty(orderId) /* && !StringUtils.isNumeric(orderId) */) {
			// errs.add("订单号不能为空，并且必须是数字");
			errs.add("订单号不能为空");
			result.setErrorMessages(errs);
			request.setAttribute("result", result);
			return result;
		}
		if(StringUtils.isEmpty(paypwd)){
			errs.add("支付密码不能为空");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		Long uid = registerDto.getUid();
		ExecuteResult<String> executeResult = userExportService.validateExistPaymentCode(uid);
		if ("0".equals(executeResult.getResult())) {
			errs.add("请先设置支付密码，再确认收货！");
			result.setErrorMessages(errs);
			request.setAttribute("result", result);
			return result;
		}
		paypwd = MD5.encipher(paypwd);
		//支付密码校验
		ExecuteResult<String> payResult = userExportService.validatePayPassword(uid, paypwd);
		//System.out.println("payResult===="+JSON.toJSONString(payResult));
		if("1".equals(payResult.getResult())){
			ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
			if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
				Integer nstate = resultTradeOrdersDTO.getResult().getState();
				Integer refund = resultTradeOrdersDTO.getResult().getRefund();
				if(nstate==3){//确认收货状态
					if(refund == 2){//订单是在申请退款
						TradeOrdersDTO orderDTO = new TradeOrdersDTO();
						orderDTO.setOrderId(orderId);
						orderDTO.setRefund(1);
						tradeOrderExportService.updateTradeOrdersDTOSelective(orderDTO);

						//先查出退款订单，然后再修改退款订单
						TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
						TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
						tradeReturnDto.setOrderId(orderId);
						queryDto.setTradeReturnDto(tradeReturnDto);
						DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
		 				if(dg!=null && dg.getTotal()>0){
							for(TradeReturnGoodsDTO dto : dg.getRows()){
								//待平台处理，平台处里中，退款失败，退款申请成功,等待同意退款，待买家收到货款确认发货,退款成功，即卖家同意退款后不关闭退款流程
								if(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode()!=dto.getState()&&TradeReturnStatusEnum.PLATFORMDEALING.getCode()!=dto.getState()
                            			&&TradeReturnStatusEnum.REFUNDING.getCode()!=dto.getState()&&TradeReturnStatusEnum.SUCCESS.getCode()!=dto.getState()
                            			&&TradeReturnStatusEnum.REFUNDFAIL.getCode()!=dto.getState()&&TradeReturnStatusEnum.REFUNDAPPLICATION.getCode()!=dto.getState()
                            			&&TradeReturnStatusEnum.REFUNDAPPLICATION_CUP.getCode()!=dto.getState()){
									//dto.setDeletedFlag("1");
									dto.setState(TradeReturnStatusEnum.CLOSE.getCode());
									tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.CLOSE);
									
									//取消退款单的投诉
									complainCancle1(dto.getId().toString());
								}
							}
						}
					}
					//“确认收货”这一步完成后下一状态应该是 4待评价
					result = tradeOrderExportService.modifyOrderStatus(orderId, 4);
					if(result.isSuccess() && resultTradeOrdersDTO.getResult().getPaymentMethod() != PayMethodEnum.PAY_INTEGRAL.getCode()){
						request.setAttribute("orderId", orderId);
						request.setAttribute("shopId", resultTradeOrdersDTO.getResult().getShopId());
						request.setAttribute("userId", resultTradeOrdersDTO.getResult().getBuyerId());
						request.setAttribute("amount", resultTradeOrdersDTO.getResult().getPaymentPrice());
						request.setAttribute("paymentMethod", resultTradeOrdersDTO.getResult().getPaymentMethod());
						
					}
					//查询订单信息
					ExecuteResult<TradeOrdersDTO> toDTO = tradeOrderExportService.getOrderById(orderId);
					//查询订单退款记录
					TradeReturnInfoQueryDto tradeReturnInfoQueryDto = new TradeReturnInfoQueryDto();
					TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
					tradeReturnGoodsDTO.setOrderId(orderId);
					tradeReturnInfoQueryDto.setTradeReturnDto(tradeReturnGoodsDTO);
					DataGrid<TradeReturnGoodsDTO> trgDTOS = tradeReturnExportService.getTradeReturnInfoDto(null,tradeReturnInfoQueryDto);
					payValue = toDTO.getResult().getPaymentPrice();  //实际支付金额
					List<TradeReturnGoodsDTO> trgList = trgDTOS.getRows();
					//实际支付金额 - 退款金额
					for(int i=0;i<trgList.size();i++){
						payValue = payValue.subtract(trgList.get(i).getRefundGoods());
					}
					groType = 1;
				}
			}
		}else{
			errs.add("支付密码错误，请重新输入！");
			result.setErrorMessages(errs);
			request.setAttribute("result",result);
			return result;
		}
		if(groType==1){
			request.setAttribute("payValue", payValue); //计算后的支付金额
			request.setAttribute("GrowthValue", Constants.GROWTH_VALUE_SHOPPING); //成长值类型
		}
		return result;
	}
	/**
	 *
	 * <p>Discription:[删除订单]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "deleteOrderById" })
	public ExecuteResult<String> deleteOrderById(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errs = new ArrayList<String>();
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if (StringUtils.isEmpty(orderId)/* && !StringUtils.isNumeric(orderId)*/) {
			// errs.add("订单号不能为空，并且必须是数字");
			errs.add("订单号不能为空");
			result.setErrorMessages(errs);
			return result;
		}
		result = tradeOrderExportService.deleteOrderById(orderId);
		return result;
	}
	/**
	 *
	 * <p>Discription:[订单延迟收货]</p>
	 * Created on 2015年4月22日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "delayDelivery" })
	public ExecuteResult<String> delayDelivery(HttpServletRequest request) {
		String orderId = request.getParameter("orderId");
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errs = new ArrayList<String>();
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if (StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/) {
//			errs.add("订单号不能为空，并且必须是数字");
			errs.add("订单号不能为空");
			result.setErrorMessages(errs);
			return result;
		}
		result = tradeOrderExportService.delayDelivery(orderId);
		return result;
	}

	/**
	 *
	 * <p>Discription:[订单改价]</p>
	 * Created on 2015年4月15日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "modifyOrderPrice" })
	public ExecuteResult<String> modifyOrderPrice(HttpServletRequest request) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errs = new ArrayList<String>();
		String userToken = LoginToken.getLoginToken(request);
		RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
		if(StringUtils.isEmpty(userToken)){
			errs.add("用户未登录");
			result.setErrorMessages(errs);
			return result;
		}
		if(registerDto==null){
			errs.add("未找到登录用户");
			result.setErrorMessages(errs);
			return result;
		}
		String orderId = request.getParameter("orderId");
		String oldPrice=request.getParameter("oldPrice");//改价之前的实际支付金额
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if (StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/) {
//			errs.add("订单号不能为空，并且必须是数字");
			errs.add("订单号不能为空");
			result.setErrorMessages(errs);
			return result;
		}
		String itemId = request.getParameter("itemId");
		if (StringUtils.isEmpty(itemId) && !StringUtils.isNumeric(itemId)) {
			errs.add("itemId不能为空，并且必须是数字");
			result.setErrorMessages(errs);
			return result;
		}
		String skuId = request.getParameter("skuId");
		if (StringUtils.isEmpty(skuId) && !StringUtils.isNumeric(skuId)) {
			errs.add("skuId不能为空，并且必须是数字");
			result.setErrorMessages(errs);
			return result;
		}
		String freight = request.getParameter("freight");
		String payPriceTotal = request.getParameter("payPriceTotal");
		String changedPrice = request.getParameter("changedPrice");
		String promotionDiscount = request.getParameter("promotionDiscount");
		TradeOrdersDTO orderDTO = new TradeOrdersDTO();
		orderDTO.setOrderId(orderId);
		if(StringUtils.isNotEmpty(freight)){
			BigDecimal bd = new BigDecimal(freight);
			orderDTO.setFreight(bd);
		}
		orderDTO.setSellerId(registerDto.getUid());

		List<TradeOrderItemsDTO>  items = new ArrayList<TradeOrderItemsDTO>();
		TradeOrderItemsDTO toid = new TradeOrderItemsDTO();
		toid.setOrderId(orderId);
		toid.setItemId(Long.valueOf(itemId));
		toid.setSkuId(Long.valueOf(skuId));
		if(StringUtils.isNotEmpty(promotionDiscount)){
			toid.setPromotionDiscount(new BigDecimal(promotionDiscount));
		}
		toid.setPrimitivePrice(new BigDecimal(payPriceTotal));
		toid.setPayPriceTotal(new BigDecimal(changedPrice));
		items.add(toid);
		orderDTO.setItems(items);
		result = tradeOrderExportService.modifyOrderPrice(orderDTO);
		
		//System.out.println("订单改价==="+JSON.toJSONString(result));
		if(result.isSuccess()){
			try {
				ExecuteResult<String> paymentResult = paymentExportService.modifyOrderPrice(orderId);
			} catch (Exception e) {
				e.printStackTrace();
				errs.add("订单改价异常："+e);
				result.setErrorMessages(errs);
				return result;
			}
		}
		//添加优惠修改记录
		ActivityRecordDTO activityRecordDTO = new ActivityRecordDTO();
		activityRecordDTO.setOrderId(orderId);
		activityRecordDTO.setShopId(WebUtil.getInstance().getShopId(request));
		activityRecordDTO.setType(ActivityTypeEnum.SHOPCHANGEPRICE.getStatus());
		activityRecordDTO.setDiscountAmount(new BigDecimal(oldPrice==null?"0":oldPrice).subtract(new BigDecimal(changedPrice)));
		ExecuteResult<String> addResult = activityStatementSerice.addActivityRecord(activityRecordDTO);
		if(!addResult.isSuccess()){
			Log.error("修改价格后，优惠金额记录插入失败");
		}
		request.setAttribute("result", result);
		request.setAttribute("orderId", orderId);
		request.setAttribute("itemId", itemId);
		return result;
	}



	/**
	 *
	 * <p>Discription:[买家：退款/售后申请]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundOrcservice" })
	public String cservice(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, Integer page, HttpServletRequest request, Model model) {
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid != null){
			ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(uid);
			tradeOrdersQueryInDTO.setBuyerIdList(idsEr.getResult());			
//			tradeOrdersQueryInDTO.setBuyerId(Long.valueOf(uid));
		}else{
			return "redirect:/user/login";
		}
		Integer pageState = tradeOrdersQueryInDTO.getState();
		Integer allState = null ;
		if(pageState == null || pageState == 0){
			tradeOrdersQueryInDTO.setState(allState);
		}
		//根据商品名称查询
		String itemName = tradeOrdersQueryInDTO.getItemName();
		if(StringUtils.isNotEmpty(itemName)){
			tradeOrdersQueryInDTO.setItemIdList(getListItemId(itemName));
		}
		//买家标记
		tradeOrdersQueryInDTO.setUserType(1);

		Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
		if(null != page){
			pager.setPage(page);
		}

		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(2);//待配送
		stateList.add(3);//已发货
		stateList.add(4);//待评价
		stateList.add(5);//已完成
		stateList.add(7);//已关闭
		tradeOrdersQueryInDTO.setRemoveServiceGoodsFlag(1);
		tradeOrdersQueryInDTO.setStateList(stateList);
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
		////System.out.println("executeResult==="+JSON.toJSONString(executeResult) );
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		JSONArray jsonArray = new JSONArray();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
				// 订单号加密
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", tradeOrdersDTO.getOrderId());
				if (passKeyEr.isSuccess()) {
					tradeOrdersDTO.setPassKey(passKeyEr.getResult());
				}
				//获取店铺名称
				ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
				if(result_shopDto!=null && result_shopDto.getResult()!=null){
					ShopDTO shopDTO = result_shopDto.getResult();
					jsonObject.put("shopId", tradeOrdersDTO.getShopId());
					jsonObject.put("shopName", shopDTO.getShopName());
				}
				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
				//查询退款单id
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(tradeOrdersDTO.getOrderId().toString());
				tradeReturnDto.setDeletedFlag("0");
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
				JSONArray jsonArray_item = new JSONArray();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					
					JSONObject jsonObject_item = new JSONObject();
					
					if(dg!=null && dg.getTotal()>0){
						int orderState=tradeOrdersDTO.getState();
						for (TradeReturnGoodsDTO trg : dg.getRows()) {
							//售后加密
							ExecuteResult<String> passKeyResult = EncrypUtil.EncrypStrByAES(uid+"", tradeOrderItemsDTO.getOrderId()+tradeOrderItemsDTO.getSkuId());
							if(passKeyResult.isSuccess()){
								jsonObject_item.put("passKey", passKeyResult.getResult());
							}
							List<TradeReturnGoodsDetailDTO> details=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(trg.getId()));
							boolean flag=tradeOrderItemsDTO.getSkuId().equals(details.get(0).getSkuId());
							if(((new Integer(5).equals(trg.getState())||"0".equals(trg.getIsCustomerService())&&new Integer(5).equals(trg.getAfterService())&&orderState>3 && orderState<6)
									&&flag)){
									//申请售后
									jsonObject_item.put("refundFlag", false);
									jsonObject_item.put("refundId", trg.getId());// 退款单id
									jsonObject_item.put("afterService", trg.getAfterService());//售后服务
									continue;
								}
									if((orderState==2 ||orderState==3) && "0".equals(trg.getIsCustomerService())
										&&flag){
										//查看退款/退货进度
										jsonObject_item.put("refundFlag", true);
										jsonObject_item.put("refundId", trg.getId());// 退款单id
										jsonObject_item.put("afterService", trg.getAfterService());//售后服务
										passKeyResult = EncrypUtil.EncrypStrByAES(uid+"", trg.getId()+""+tradeOrderItemsDTO.getSkuId());
										if(passKeyResult.isSuccess()){
											jsonObject_item.put("passKey", passKeyResult.getResult());
										}
									}else if(((orderState>3 && orderState<6)||orderState==7)&&flag){
										jsonObject_item.put("refundFlag", true);
										jsonObject_item.put("refundId", trg.getId());// 退款单id
										jsonObject_item.put("afterService", trg.getAfterService());//售后服务
										passKeyResult = EncrypUtil.EncrypStrByAES(uid+"", trg.getId()+""+tradeOrderItemsDTO.getSkuId());
										if(passKeyResult.isSuccess()){
											jsonObject_item.put("passKey", passKeyResult.getResult());
										}
									}
									
						}
					}else{
						ExecuteResult<String> passKeyResult = EncrypUtil.EncrypStrByAES(uid+"", tradeOrderItemsDTO.getOrderId()+""+tradeOrderItemsDTO.getSkuId());
						if(passKeyResult.isSuccess()){
							jsonObject_item.put("passKey", passKeyResult.getResult());
						}
					}
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

		Pager<TradeOrdersDTO> rs_pager = new Pager<TradeOrdersDTO>();
		rs_pager.setPage(pager.getPage());
		rs_pager.setTotalCount(dataGrid.getTotal().intValue());
		rs_pager.setRecords(dataGrid.getRows());

		if (rs_pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
			rs_pager.setEndPageIndex(rs_pager.getStartPageIndex());
		}

		model.addAttribute("tradeOrdersQueryInDTO", tradeOrdersQueryInDTO);
		model.addAttribute("pager", rs_pager);
		model.addAttribute("jsonArray", jsonArray);
		model.addAttribute("pageState", pageState);

		////System.out.println("tradeOrdersQueryInDTO==="+JSON.toJSONString(tradeOrdersQueryInDTO) );
		////System.out.println("pager==="+JSON.toJSONString(rs_pager));
		//System.out.println("jsonArray==="+JSON.toJSONString(jsonArray));
		//System.out.println("pageState==="+JSON.toJSONString(pageState));

		tradeOrdersQueryInDTO.setState(2);//卖家待发货
		model.addAttribute("stayDelivery",tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO).getResult());
		tradeOrdersQueryInDTO.setState(3);//买家待收货
		model.addAttribute("stayReceipt",tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO).getResult());
		tradeOrdersQueryInDTO.setState(5);//已完成
		model.addAttribute("complete",tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO).getResult());

		return "sellcenter/order/refundOrcservice";
	}
	/**
	 *
	 * <p>Discription:[买家：退款/售后管理]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "cserviceManager" })
	public String cserviceManager(HttpServletRequest request, Model model) {

		Pager<TradeReturnGoodsDTO> pager = new Pager<TradeReturnGoodsDTO>();
		TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
		String userToken = LoginToken.getLoginToken(request);;
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){

				String page = request.getParameter("page");
				if(StringUtils.isNotEmpty(page) && StringUtils.isNumeric(page)){
					pager.setPage(Integer.parseInt(page));
					model.addAttribute("page", page);
				}
				String orderId = request.getParameter("orderId");
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				String state = request.getParameter("state");
				/*model.addAttribute("orderId", orderId);
				model.addAttribute("startTime", startTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("state", state);*/

				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(registerDto.getUid());
//				tradeReturnDto.setBuyId(registerDto.getUid().toString());
				tradeReturnDto.setBuyIdList(idsEr.getResult());
				if(StringUtils.isNotEmpty(orderId)){
					tradeReturnDto.setOrderId(orderId);
				}
				if(StringUtils.isNotEmpty(state) && StringUtils.isNumeric(state)){
					tradeReturnDto.setState(Integer.parseInt(state));
				}
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				//申请开始日期
				if(StringUtils.isNotEmpty(startTime)){
					try {
						tradeReturnDto.setApplyDtBegin(format.parse(startTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				//申请结束日期
				if(StringUtils.isNotEmpty(endTime)){
					try {
						tradeReturnDto.setApplyDtEnd(format.parse(endTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				queryDto.setTradeReturnDto(tradeReturnDto);
				model.addAttribute("tradeReturnDto",tradeReturnDto);
				
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(pager, queryDto);
				if(dg != null && dg.getRows() != null){
					int i = 0;
					for(TradeReturnGoodsDTO tradeReturnGoodsDTO : dg.getRows()){
						ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(WebUtil.getInstance().getUserId(request)+"", tradeReturnGoodsDTO.getId()+"");
						if(passKeyEr.isSuccess()){
							tradeReturnGoodsDTO.setPassKey(passKeyEr.getResult());
						}
						
						//判断是否为纯服务商品
						ExecuteResult<TradeReturnInfoDto> result = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(tradeReturnGoodsDTO.getId().toString());
						if(result!=null && result.getResult()!= null){
							ExecuteResult<ItemDTO> item ;
							dg.getRows().get(i).setIsService("1");//纯服务商品
							for( TradeReturnGoodsDetailDTO tradeReturnGoods: result.getResult().getTradeReturnGoodsDetailList()){
								 item = itemExportService.getItemById(tradeReturnGoods.getGoodsId());
								 if(item.getResult().getAddSource() < 4 ){
									 dg.getRows().get(i).setIsService("2");//不是纯服务商品
									 break;
								 }
							}
						}
						
						i ++;
					}
				}
				pager.setPage(pager.getPage());
				pager.setTotalCount(dg.getTotal().intValue());
				pager.setRecords(dg.getRows());
				if (pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
					pager.setEndPageIndex(pager.getStartPageIndex());
				}
				model.addAttribute("pager", pager);
				//System.out.println("pager==="+JSON.toJSONString(pager));
			}
		}


		return "sellcenter/order/refundOrcserviceManager";
	}
	/**
	 *
	 * <p>Discription:[买家：申请退款详情页]</p>
	 * Created on 2015年4月15日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundInfoBuyer" })
	public String refundAgreementInfo(HttpServletRequest request, Model model) {
		String returnGoodId = request.getParameter("returnGoodId");
		
		int progressBar=3;	//默认进度条3
		
		if(StringUtils.isNotEmpty(returnGoodId)){
			ExecuteResult<TradeReturnInfoDto> result = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnGoodId);
			if(result!=null && result.getResult()!= null){
				//System.out.println("result==========="+JSON.toJSONString(result));
				model.addAttribute("tradeReturnGoodsDetailList", result.getResult().getTradeReturnGoodsDetailList());
				TradeReturnGoodsDTO tradeReturnGoodsDTO = result.getResult().getTradeReturnDto();
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(WebUtil.getInstance().getUserId(request)+"", tradeReturnGoodsDTO.getId()+"");
				if(passKeyEr.isSuccess()){
					tradeReturnGoodsDTO.setPassKey(passKeyEr.getResult());
				}
				
				//判断是否为纯服务商品
				ExecuteResult<ItemDTO> item ;
				tradeReturnGoodsDTO.setIsService("1");//纯服务商品
				for( TradeReturnGoodsDetailDTO tradeReturnGoods: result.getResult().getTradeReturnGoodsDetailList()){
					 item = itemExportService.getItemById(tradeReturnGoods.getGoodsId());
					 if(item.getResult().getAddSource() < 4 ){
						 tradeReturnGoodsDTO.setIsService("2");//不是纯服务商品
						 break;
					 }
				}
				
				model.addAttribute("tradeReturnDto",  tradeReturnGoodsDTO);
				if(tradeReturnGoodsDTO.getState()==6){
					progressBar=4;	//退款完成 4  进度条
				}
				List<TradeReturnGoodsDetailDTO> detailDto=result.getResult().getTradeReturnGoodsDetailList();
				//订单信息
				String orderId = result.getResult().getTradeReturnDto().getOrderId();
				ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
				if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
					
					model.addAttribute("tradeOrdersDTO", resultTradeOrdersDTO.getResult());
					List<TradeOrderItemsDTO> itemDtos=resultTradeOrdersDTO.getResult().getItems();
					for(TradeOrderItemsDTO dto:itemDtos){
						for(TradeReturnGoodsDetailDTO goodsDto:result.getResult().getTradeReturnGoodsDetailList()){
							if(dto.getSkuId().longValue()==goodsDto.getSkuId().longValue()){
								model.addAttribute("num",dto.getNum());
								model.addAttribute("payPriceTotal",dto.getPayPriceTotal());
								model.addAttribute("interal",dto.getIntegral());
							}
						}
					}
					//获取店铺名称
					ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(resultTradeOrdersDTO.getResult().getShopId());
					if(result_shopDto!=null && result_shopDto.getResult()!=null){
						ShopDTO shopDTO = result_shopDto.getResult();
						model.addAttribute("shopName", shopDTO.getShopName());
					}
				}
				//获取卖家用户名称
				String sellerId = result.getResult().getTradeReturnDto().getSellerId();
				UserDTO user = userExportService.queryUserById(Long.valueOf(sellerId));
				if(user!=null){
					model.addAttribute("sellerName", user.getUname());
					model.addAttribute("sellerMobile", user.getUmobile());
				}
				/*	
				BigDecimal refundPriceTotal = new BigDecimal(0);//退货商品的总金额
				for(TradeReturnGoodsDetailDTO detailDto : result.getResult().getTradeReturnGoodsDetailList()){
					refundPriceTotal = refundPriceTotal.add(detailDto.getReturnAmount());
				}
				model.addAttribute("refundPriceTotal", refundPriceTotal);
				*/
				ComplainDTO complainDTO = new ComplainDTO();
				complainDTO.setOrderId(orderId);
				complainDTO.setReturnGoodsId(Long.valueOf(returnGoodId));
				List<ComplainDTO> listComplainDTO = complainExportService.getComplainByCondition(complainDTO);
				
				Map<String, String> statusMap = getComplainStatus(listComplainDTO);
				
				// 如果正在投诉0或已仲裁1，页面按钮显示为“查看仲裁信息”，如果为已撤销2，页面按钮显示为“申请仲裁”
				model.addAttribute("buyerStatus", statusMap.get("buyerStatus"));
				model.addAttribute("sellerStatus", statusMap.get("sellerStatus"));
				
				model.addAttribute("progressBar", progressBar);	//页面进度条
				//查询退款单id
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(orderId);
				tradeReturnDto.setDeletedFlag("1");
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
				if(dg!=null&&dg.getTotal()>0){
					List<TradeReturnGoodsDTO> goodsDtos=dg.getRows();
					BigDecimal money=BigDecimal.ZERO;
					for(TradeReturnGoodsDTO goods:goodsDtos){
						BigDecimal fundMoney=BigDecimal.ZERO;
						String num="";
						BigDecimal refundFreight=BigDecimal.ZERO;
						List<TradeReturnGoodsDetailDTO> deDto=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(goods.getId()));
						if(deDto.get(0).getSkuId().longValue()==detailDto.get(0).getSkuId().longValue()&&5==goods.getAfterService().longValue()){
							fundMoney=deDto.get(0).getReturnAmount();//部分商品已退款金额
							num=deDto.get(0).getRerurnCount().toString();
							refundFreight=goods.getRefundFreight();
						}
						if(goods.getAfterService().longValue()!=4){
							money=money.add(deDto.get(0).getReturnAmount());
						}
						model.addAttribute("fundMoney",fundMoney);//该商品已退款总金额
						model.addAttribute("refundNum",num);//该商品已退款数量
						model.addAttribute("refundFreight",refundFreight);//该商品已退运费
					}
					model.addAttribute("totalMoney",money);//已退款总金额
				}
			}
		}
		return "sellcenter/order/refundInfo_buyer";
	}
	/**
	 *
	 * <p>Discription:[买家： 卖家不同意退款，买家重新修改退款原因，然后提交]</p>
	 * Created on 2015年4月17日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "updateRefundAgreement" })
	@ResponseBody
	public ExecuteResult<TradeReturnGoodsDTO> updateRefundAgreement(HttpServletRequest request, Model model) {
		ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
		List<String> errorMessages = new ArrayList<String>();
		String returnId = request.getParameter("returnId");
		if(StringUtils.isEmpty(returnId) && StringUtils.isNumeric(returnId)){
			errorMessages.add("退款id不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String returnResult = request.getParameter("returnResult");
		if(StringUtils.isEmpty(returnResult)){
			errorMessages.add("退款原因不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String remark = request.getParameter("remark");
		if(StringUtils.isEmpty(remark)){
			errorMessages.add("退款说明不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String refundGoods = request.getParameter("refundGoods");//  退款货品总金额
		if(StringUtils.isEmpty(refundGoods) && !StringUtils.isNumeric(refundGoods)){
			errorMessages.add("退款金额不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String num =request.getParameter("num");//退款数量
		if(StringUtils.isEmpty(num) && !StringUtils.isNumeric(num)){
			errorMessages.add("退款数量不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String orderId = request.getParameter("orderId");
		if(StringUtils.isEmpty(orderId)){
			errorMessages.add("订单id不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		List<TradeReturnGoodsDetailDTO> detailDtos=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(returnId);
		String haveRefundMoney=request.getParameter("haveRefundMoney");//已退款金额
		//String refundFreight = request.getParameter("refundFreight");//退款运费金额
		String totalfundMoney=request.getParameter("totalfundMoney");//单个商品退款总金额
		BigDecimal totalMoney=BigDecimal.ZERO;//商品支付总金额
		String refundNum=request.getParameter("refundNum");//已经退款数量
		int totalNum=0;//商品总数量
		BigDecimal refundMpney=BigDecimal.ZERO;//后台计算商品最多退款金额
		 ExecuteResult<TradeOrdersDTO> orderRes = tradeOrderExportService.getOrderById(orderId);
		 TradeOrdersDTO tradeOrdersDTO=orderRes.getResult();
		 BigDecimal money=new BigDecimal(0.0);
		 for(TradeOrderItemsDTO item:tradeOrdersDTO.getItems()){
				if(item.getSkuId().equals(detailDtos.get(0).getSkuId())){
					totalMoney=item.getPayPriceTotal();
					totalNum=item.getNum();
				}
			}
		 if(StringUtils.isEmpty(haveRefundMoney)){
				money=new BigDecimal(refundGoods);
			}else{
				money=new BigDecimal(refundGoods).add(new BigDecimal(haveRefundMoney));
			}
			if(tradeOrdersDTO.getPaymentMethod() == PayMethodEnum.PAY_INTEGRAL.getCode()){
				if(money.intValue()>tradeOrdersDTO.getIntegral()){
					errorMessages.add("订单退款总积分不允许超过支付总积分！");
					result.setErrorMessages(errorMessages);
					return result;
				}
			}else{
				if(money.doubleValue()>(tradeOrdersDTO.getPaymentPrice().doubleValue())){
				    errorMessages.add("订单退款总金额不允许超过支付总金额！");
					result.setErrorMessages(errorMessages);
					return result;
			   }
			}
		//判断再次退款判断是否全退，如果最后一件退掉，则退款金额=支付总金额-已经退款总额
		   if(!"".equals(totalfundMoney)&& !"".equals(refundNum)&&new BigDecimal(num).compareTo(new BigDecimal(totalNum).subtract(new BigDecimal(refundNum)))==0){
			   refundMpney=totalMoney.subtract(new BigDecimal(totalfundMoney));
		   }else{
			   //退款金额=支付总金额/支付总数量*退款总数量     计算结果保留两位小数进上去
			   refundMpney= (totalMoney.divide(new BigDecimal(totalNum),10,RoundingMode.DOWN).multiply(new BigDecimal(num))).setScale(4,BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_UP); 
		   }
		   if(new BigDecimal(refundGoods).compareTo(refundMpney.add(tradeOrdersDTO.getFreight() == null?new BigDecimal("0"):tradeOrdersDTO.getFreight()))>0){
				  errorMessages.add("退款金额不允许超过该商品支付总金额和运费之和！");
					result.setErrorMessages(errorMessages);
					return result;
		   }
		String picUrl = request.getParameter("picUrl");//上传凭证
		List<TradeReturnPicDTO> picDTOList = new ArrayList<TradeReturnPicDTO>();
		if(StringUtils.isNotEmpty(picUrl)){
			String[] arrayPic = picUrl.split(";");
			for(String pic : arrayPic){
				TradeReturnPicDTO picDto = new TradeReturnPicDTO();
				picDto.setPicUrl(pic);
				picDto.setReturnGoodsId(Long.valueOf(returnId));
				picDTOList.add(picDto);
			}
		}
		TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
		dto.setId(Long.valueOf(returnId));
		dto.setReturnResult(returnResult);//  退货原因
		dto.setRemark(remark);//  问题描述
		dto.setRefundGoods(new BigDecimal(refundGoods));
		 //退款金额大于单个商品实际支付总金额（不包括运费）
		if(new BigDecimal(refundGoods).subtract(refundMpney).compareTo(BigDecimal.valueOf(0))>0){
			dto.setRefundFreight(new BigDecimal(refundGoods).subtract(refundMpney));//退款运费金额
		}
		dto.setPicDTOList(picDTOList);
		dto.setState(TradeReturnStatusEnum.AUTH.getCode());
		result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.AUTH);
		detailDtos.get(0).setReturnAmount(new BigDecimal(refundGoods));//退款金额
		detailDtos.get(0).setRerurnCount(Integer.parseInt(num));//退款数量
		tradeReturnExportService.updateTradeReturnGoodsDetailDTO(detailDtos.get(0));//更新退货详细信息
		return result;
	}
	/**
	 *
	 * <p>Discription:[买家：买家申请退款    发货给卖家]</p>
	 * Created on 2015年4月17日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "deliverGoods" })
	@ResponseBody
	public ExecuteResult<TradeReturnGoodsDTO> deliverGoods(HttpServletRequest request, Model model) {
		ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
		List<String> errorMessages = new ArrayList<String>();
		String returnId = request.getParameter("returnId");
		if(StringUtils.isEmpty(returnId) && StringUtils.isNumeric(returnId)){
			errorMessages.add("退款id不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String expressNo = request.getParameter("expressNo");//退货快递单号
		if(StringUtils.isEmpty(expressNo)){
			errorMessages.add("退货快递单号不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String expressName = request.getParameter("expressName");//快递公司

		TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
		dto.setId(Long.valueOf(returnId));
		dto.setExpressNo(expressNo);
		dto.setState(TradeReturnStatusEnum.ORDERRERURN.getCode());
		if(StringUtils.isNotEmpty(expressName)){
			dto.setExpressName(expressName);
		}
		result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.ORDERRERURN);
		return result;
	}
	/**
	 *
	 * <p>Discription:[买家：选择退款商品，申请退款]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundAgreement" })
	public String refundAgreement(HttpServletRequest request, Model model) {

		ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
		List<String> errorMessages = new ArrayList<String>();

		String orderId = request.getParameter("orderId");
		String skuId=request.getParameter("skuId");
		String passKey = request.getParameter("passKey");
		if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
			errorMessages.add("订单id不正确");
			result.setErrorMessages(errorMessages);
			return "sellcenter/order/refundAgreement";
		}
		if(StringUtils.isEmpty(skuId)&& !StringUtils.isNumeric(skuId)){
			errorMessages.add("skuid不正确");
			result.setErrorMessages(errorMessages);
			return "sellcenter/order/refundAgreement";
		}
		/*
		String jsonRefundItem = request.getParameter("jsonRefundItem");
		if(StringUtils.isEmpty(jsonRefundItem)){
			errorMessages.add("订单退货商品不能为空");
			result.setErrorMessages(errorMessages);
			return "sellcenter/order/refundAgreement";
		}
		 */
		TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
		tradeOrdersQueryInDTO.setOrderId(orderId);
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			if(tradeOrdersDTOs!=null && tradeOrdersDTOs.size()>0){
				//根据订单id 查询只会查询出一条记录
				TradeOrdersDTO tradeOrdersDTO = tradeOrdersDTOs.get(0);

				model.addAttribute("tradeOrdersDTO", tradeOrdersDTO);
				//System.out.println("tradeOrdersDTO===="+JSON.toJSONString(tradeOrdersDTO));
				//获取卖家用户名称
				UserDTO user = userExportService.queryUserById(tradeOrdersDTO.getSellerId());
				if(user!=null){
					model.addAttribute("sellerName", user.getUname());
					model.addAttribute("sellerMobile", user.getUmobile());
				}
				//获取店铺名称
				ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
				if(result_shopDto!=null && result_shopDto.getResult()!=null){
					ShopDTO shopDTO = result_shopDto.getResult();
					model.addAttribute("shopName", shopDTO.getShopName());
				}

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderId", tradeOrdersDTO.getOrderId());

				//model.addAttribute("jsonRefundItem", jsonRefundItem);//用户选择的退款的商品
				//JSONArray jsonArray = JSON.parseArray(jsonRefundItem);

				BigDecimal refundPriceTotal = new BigDecimal(0);//退货商品的总金额
				
				Integer interal  = 0 ;

				JSONArray jsonArray_item = new JSONArray();
				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					if(tradeOrderItemsDTO.getSkuId().longValue()==Long.parseLong(skuId)){
					//for(int i=0;i<jsonArray.size(); i++){
						//JSONObject jsonobj = (JSONObject) jsonArray.get(i);
						//Long itemId = Long.valueOf((String) jsonobj.get("itemId"));
						//Long skuId = Long.valueOf((String) jsonobj.get("skuId"));
						//if(tradeOrderItemsDTO.getItemId().equals(itemId) && tradeOrderItemsDTO.getSkuId().equals(skuId)){
							JSONObject jsonObject_item = new JSONObject();
							jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
							jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
							jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
							BigDecimal payPriceTotal = tradeOrderItemsDTO.getPayPriceTotal();
							jsonObject_item.put("payPriceTotal", payPriceTotal);
							
							jsonObject_item.put("integral", tradeOrderItemsDTO.getIntegral());
							if(tradeOrderItemsDTO.getIntegral() != null){
								interal = interal+ tradeOrderItemsDTO.getIntegral();
							}
							refundPriceTotal = refundPriceTotal.add(payPriceTotal);

							//获取商品图片
							ItemShopCartDTO dto = new ItemShopCartDTO();
							dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
							dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
							dto.setQty(tradeOrderItemsDTO.getNum());//数量
							dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
							dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
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
								jsonObject_item.put("itemName", itemDTO.getItemName());
							}
							jsonArray_item.add(jsonObject_item);
						//}
					//}
				    }
				}
				jsonObject.put("items", jsonArray_item);

				model.addAttribute("refundPriceTotal", refundPriceTotal);//退货商品的总金额
				model.addAttribute("interal", interal);//退货商品的总金额
				model.addAttribute("jsonObject", jsonObject);
				//System.out.println("jsonObject===="+JSON.toJSONString(jsonObject));
			}

		}
		//查询退款单id
		TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
		TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
		tradeReturnDto.setOrderId(orderId);
		queryDto.setTradeReturnDto(tradeReturnDto);
		DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
		if(dg!=null&&dg.getTotal()>0){
			List<TradeReturnGoodsDTO> goodsDtos=dg.getRows();
			BigDecimal money=BigDecimal.ZERO;
			for(TradeReturnGoodsDTO goods:goodsDtos){ 
				BigDecimal fundMoney=BigDecimal.ZERO;
				String num="";
				BigDecimal refundFreight=BigDecimal.ZERO;
				List<TradeReturnGoodsDetailDTO> deDto=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(goods.getId()));
				if(deDto.get(0).getSkuId().longValue()==Long.parseLong(skuId)&&5==goods.getAfterService().longValue()){
					fundMoney=deDto.get(0).getReturnAmount();//该sku部分商品已退款金额
					num=deDto.get(0).getRerurnCount().toString();//该sku已退款数量
					refundFreight=goods.getRefundFreight();
				}
				if(goods.getAfterService().longValue()!=4){//售后服务状态不是已关闭的状态，计算出该订单已经退款总金额
					money=money.add(deDto.get(0).getReturnAmount());
				}
				model.addAttribute("fundMoney",fundMoney);
				model.addAttribute("num",num);
				model.addAttribute("refundFreight",refundFreight);
			}
			model.addAttribute("totalMoney",money);
		}
		model.addAttribute("passKey",passKey);
		return "sellcenter/order/refundAgreement";
	}

	/**
	 *
	 * <p>Discription:[买家：退款协议提交，处理页面]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundAgreementSubmit" })
	@ResponseBody
	public ExecuteResult<TradeReturnGoodsDTO> refundAgreementSubmit(HttpServletRequest request, Model model) {
		ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
		request.setAttribute("result",result);
		List<String> errorMessages = new ArrayList<String>();
		String orderId = request.getParameter("orderId");
		if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
			errorMessages.add("订单id不正确");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String skuId=request.getParameter("skuId");
		if(StringUtils.isEmpty(skuId)&&!StringUtils.isNumeric(skuId)){
			errorMessages.add("skuId不正确");
			result.setErrorMessages(errorMessages);
			return result;
		}
		/*String jsonRefundItem = request.getParameter("jsonRefundItem");
		if(StringUtils.isEmpty(jsonRefundItem)){
			errorMessages.add("订单退货商品不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}*/
		String returnResult = request.getParameter("returnResult");
		if(StringUtils.isEmpty(returnResult)){
			errorMessages.add("退款原因不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String remark = request.getParameter("remark");
		if(StringUtils.isEmpty(remark)){
			errorMessages.add("退款说明不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String amountNum=request.getParameter("amountNum");
		if(StringUtils.isEmpty(amountNum)&&!StringUtils.isNumeric(amountNum)){
			errorMessages.add("退款数量不能为空,必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String refundMoney=request.getParameter("refundMoney");
		if(StringUtils.isEmpty(refundMoney)&&!StringUtils.isNumeric(refundMoney)){
			errorMessages.add("退款金额不能为空,必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String haveRefundMoney=request.getParameter("haveRefundMoney");//订单已退款总金额
		String totalfundMoney=request.getParameter("totalfundMoney");//单个商品退款总金额
		/*String refundGoods = request.getParameter("refundGoods");//  退款货品总金额
		if(StringUtils.isEmpty(refundGoods) && !StringUtils.isNumeric(refundGoods)){
			errorMessages.add("退款金额不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}*/
		BigDecimal totalMoney=BigDecimal.ZERO;//商品支付总金额
		String refundNum=request.getParameter("refundNum");//已经退款数量
		BigDecimal payTotal=BigDecimal.ZERO;//订单明细表中商品支付总金额
		int totalNum=0;//商品总数量
		BigDecimal refundMpney=BigDecimal.ZERO;//后台计算商品最多退款金额
		String picUrl = request.getParameter("picUrl");//上传凭证
		List<TradeReturnPicDTO> picDTOList = new ArrayList<TradeReturnPicDTO>();
		if(StringUtils.isNotEmpty(picUrl)){
			String[] arrayPic = picUrl.split(";");
			for(String pic : arrayPic){
				TradeReturnPicDTO picDto = new TradeReturnPicDTO();
				picDto.setPicUrl(pic);
				picDTOList.add(picDto);
			}
		}
		TradeReturnInfoDto insertDto = new TradeReturnInfoDto();

		TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
		tradeOrdersQueryInDTO.setOrderId(orderId);
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		if (executeResult != null) {
			BigDecimal money=new BigDecimal(0.0);
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			if(tradeOrdersDTOs!=null && tradeOrdersDTOs.size()>0){
				//根据订单id 查询只会查询出一条记录
				TradeOrdersDTO tradeOrdersDTO = tradeOrdersDTOs.get(0);
				List<TradeOrderItemsDTO> items=tradeOrdersDTO.getItems();
				for(TradeOrderItemsDTO dto:items){
					if(dto.getSkuId().equals(Long.parseLong(skuId))){
						totalMoney=dto.getPayPriceTotal();
						totalNum=dto.getNum();
						dto.getPayPriceTotal();
						payTotal=dto.getPayPriceTotal();//支付总金额
					}
				}
				if(StringUtils.isEmpty(haveRefundMoney)){
					money=new BigDecimal(refundMoney);
				}else{
					money=new BigDecimal(refundMoney).add(new BigDecimal(haveRefundMoney));
				}
				if(tradeOrdersDTO.getPaymentMethod() == PayMethodEnum.PAY_INTEGRAL.getCode()){
					if(money.intValue()>tradeOrdersDTO.getIntegral()){
						errorMessages.add("订单退款总积分不允许超过支付总积分！");
						result.setErrorMessages(errorMessages);
						return result;
					}
				}else{
					if(money.doubleValue()>(tradeOrdersDTO.getPaymentPrice().doubleValue())){
					    errorMessages.add("订单退款总金额不允许超过支付总金额！");
						result.setErrorMessages(errorMessages);
						return result;
				   }
				}
			   //判断再次退款判断是否全退，如果最后一件退掉，则退款金额=支付总金额-已经退款总额
			   if(!"".equals(totalfundMoney)&& !"".equals(refundNum)&&new BigDecimal(amountNum).compareTo(new BigDecimal(totalNum).subtract(new BigDecimal(refundNum)))==0){
				   refundMpney=totalMoney.subtract(new BigDecimal(totalfundMoney));
			   }else{
				   //退款金额=支付总金额/支付总数量*退款总数量     计算结果保留两位小数进上去
				   refundMpney= (totalMoney.divide(new BigDecimal(totalNum),10,RoundingMode.DOWN).multiply(new BigDecimal(amountNum))).setScale(4,BigDecimal.ROUND_DOWN).setScale(2, BigDecimal.ROUND_UP); 
			   }
			   if(new BigDecimal(refundMoney).compareTo(refundMpney.add(tradeOrdersDTO.getFreight() == null?new BigDecimal("0"):tradeOrdersDTO.getFreight()))>0){
					  errorMessages.add("退款金额不允许超过该商品支付总金额和运费之和！");
						result.setErrorMessages(errorMessages);
						return result;
			   }
				//构造订单信息
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(orderId);
				Integer state = tradeOrdersDTO.getState();
				if(state!=null){
					tradeReturnDto.setOrderStatus(state.toString());
					if(state > 3 && state < 6 ){
						tradeReturnDto.setIsCustomerService("1");
					}else{
						tradeReturnDto.setIsCustomerService("0");
					}
				}
				//退款金额大于单个商品实际支付总金额（不包括运费）
				if(new BigDecimal(refundMoney).subtract(refundMpney).compareTo(BigDecimal.valueOf(0))>0){
					tradeReturnDto.setRefundFreight(new BigDecimal(refundMoney).subtract(refundMpney));//退款运费金额
				}
				tradeReturnDto.setBuyId(tradeOrdersDTO.getBuyerId()+"");
				UserDTO user = userExportService.queryUserById(tradeOrdersDTO.getBuyerId());
				if(user!=null){
					tradeReturnDto.setBuyerName(user.getUname());
				}
				tradeReturnDto.setBuyerAddress(tradeOrdersDTO.getFullAddress());
				tradeReturnDto.setBuyerPhone(tradeOrdersDTO.getMobile());
				tradeReturnDto.setSellerId(tradeOrdersDTO.getSellerId()+"");
				//如果是积分支付，则设置退货单中的退款金额为支付积分
				if(PayMethodEnum.PAY_INTEGRAL.getCode().equals(tradeOrdersDTO.getPaymentMethod())){
					tradeReturnDto.setOrderPrice(new BigDecimal(tradeOrdersDTO.getIntegral()+""));
				}else{
					tradeReturnDto.setOrderPrice(tradeOrdersDTO.getPaymentPrice());
				}
				tradeReturnDto.setReturnResult(returnResult);//  退货原因
				tradeReturnDto.setRemark(remark);//  问题描述
				//tradeReturnDto.setRefundGoods(new BigDecimal(refundGoods));
				if(StringUtils.isNoneEmpty(refundMoney)){
					tradeReturnDto.setRefundGoods(new BigDecimal(refundMoney));//货品退款总金额
				}

				//获取店铺名称
				ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
				if(result_shopDto!=null && result_shopDto.getResult()!=null){
					ShopDTO shopDTO = result_shopDto.getResult();
					tradeReturnDto.setReturnAddress(shopDTO.getProvinceName()+"  "+shopDTO.getDistrictName()+"  "+shopDTO.getStreetName());
					if(shopDTO.getMobile()!=null){
						tradeReturnDto.setReturnPhone(shopDTO.getMobile() + "");
					}
					tradeReturnDto.setReturnPostcode(shopDTO.getZcode());
				}
				tradeReturnDto.setPicDTOList(picDTOList);
				//System.out.println("订单信息tradeReturnDto========="+JSON.toJSONString(tradeReturnDto));
				insertDto.setTradeReturnDto(tradeReturnDto);

				//构造订单下退货商品的信息
				List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList = new ArrayList<TradeReturnGoodsDetailDTO>();
				//JSONArray jsonArray = JSON.parseArray(jsonRefundItem);

				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					if(tradeOrderItemsDTO.getSkuId().longValue()==Long.parseLong(skuId)){
					//for(int i=0;i<jsonArray.size(); i++){
					//	JSONObject jsonobj = (JSONObject) jsonArray.get(i);
					//	Long itemId = Long.valueOf((String) jsonobj.get("itemId"));
					//	Long skuId = Long.valueOf((String) jsonobj.get("skuId"));
					//	if(tradeOrderItemsDTO.getItemId().equals(itemId) && tradeOrderItemsDTO.getSkuId().equals(skuId)){
							TradeReturnGoodsDetailDTO goodGto = new TradeReturnGoodsDetailDTO();
							goodGto.setGoodsId(tradeOrderItemsDTO.getItemId());
							goodGto.setSkuId(tradeOrderItemsDTO.getSkuId());
							goodGto.setRerurnCount(Integer.parseInt(amountNum));//退款数量
						   if(StringUtils.isNoneEmpty(refundMoney)){
							 goodGto.setReturnAmount(new BigDecimal(refundMoney));//退款金额
							}
							goodGto.setPayPrice(tradeOrderItemsDTO.getPayPrice());//商品单价
							//获取商品图片
							ItemShopCartDTO dto = new ItemShopCartDTO();
							dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
							dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
							dto.setQty(tradeOrderItemsDTO.getNum());//数量
							if(Integer.parseInt(amountNum)>tradeOrderItemsDTO.getNum()){
								errorMessages.add("退款数量不能大于购买数量！");
								result.setErrorMessages(errorMessages);
								return result;
							}
							dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
							dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
							ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
							ItemShopCartDTO itemShopCartDTO = er.getResult();
							if( null != itemShopCartDTO){
								goodGto.setGoodsPicUrl(itemShopCartDTO.getSkuPicUrl());
							}
							//获取商品名称
							ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
							if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
								ItemDTO itemDTO = result_itemDTO.getResult();
								goodGto.setGoodsName(itemDTO.getItemName());
							}
							tradeReturnGoodsDetailList.add(goodGto);
					//	}
					//}
				  }
				}
				//System.out.println("商品列表tradeReturnGoodsDetailList========="+JSON.toJSONString(tradeReturnGoodsDetailList));
				insertDto.setTradeReturnGoodsDetailList(tradeReturnGoodsDetailList);
			}
			result = tradeReturnExportService.createTradeReturn(insertDto, TradeReturnStatusEnum.AUTH);
			request.setAttribute("insertDto",insertDto);
			//System.out.println("选择退款商品，申请退款==="+JSON.toJSONString(result));
			TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
			TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
			tradeReturnDto.setOrderId(orderId);
			queryDto.setTradeReturnDto(tradeReturnDto);
			DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
			if(dg!=null&&dg.getTotal()>0){
				for(TradeReturnGoodsDTO dto:dg.getRows()){
					List<TradeReturnGoodsDetailDTO> deDto=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(dto.getId()));
					if(dto.getAfterService()!=null&&deDto.get(0).getSkuId().longValue()==Long.parseLong(skuId)
						&&(4==dto.getAfterService().intValue()||5==dto.getAfterService().intValue())){//申請退款成功后將之前已關閉的或部分商品已关闭退货单置为已删除状态
						dto.setDeletedFlag("1");
						tradeReturnExportService.updateTradeReturnGoods(dto);
					}
				}
			}
		}
		return result;
	}
	/**
	 *
	 * <p>Discription:[买家： 提交申请退款成功]</p>
	 * Created on 2015年4月15日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundSubmitSucc" })
	public String refundSubmitSucc(HttpServletRequest request, Model model) {
		String orderId = request.getParameter("orderId");
		String returnId = request.getParameter("returnId");
		Long uid = WebUtil.getInstance().getUserId(request);
		int progressBar = 3; // 默认进度条3

		ComplainDTO complainDTO = new ComplainDTO();
		if(StringUtils.isNotEmpty(returnId)){
			complainDTO.setReturnGoodsId(Long.valueOf(returnId));
			ExecuteResult<TradeReturnInfoDto> result = tradeReturnExportService
					.getTradeReturnInfoByReturnGoodsId(returnId);
			if (result != null && result.getResult() != null) {
				TradeReturnGoodsDTO tradeReturnGoodsDTO = result.getResult()
						.getTradeReturnDto();
				model.addAttribute("tradeReturnDto", tradeReturnGoodsDTO);
				if (tradeReturnGoodsDTO.getState() == 6) {
					progressBar = 4; // 退款完成 4 进度条
				}
				//为查看仲裁按钮加密
				ExecuteResult<String> passKeyResult = EncrypUtil.EncrypStrByAES(uid+"", tradeReturnGoodsDTO.getId()+"");
				if(passKeyResult.isSuccess()){
					model.addAttribute("passKey", passKeyResult.getResult());
				}
			}
		}else{
			if (StringUtils.isNotEmpty(orderId) /*&& StringUtils.isNumeric(orderId)*/){
				complainDTO.setOrderId(orderId);
				// 查询退款单id
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(orderId);
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService
						.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(),
								queryDto);
				if (dg != null && dg.getTotal() > 0) {
					// 退款单id
					TradeReturnGoodsDTO tradeReturnGoodsDTO = dg.getRows().get(0);
					model.addAttribute("tradeReturnDto", tradeReturnGoodsDTO);
					if (tradeReturnGoodsDTO.getState() == 6) {
						progressBar = 4; // 退款完成 4 进度条
					}
					//为查看仲裁按钮加密
					ExecuteResult<String> passKeyResult = EncrypUtil.EncrypStrByAES(uid+"", tradeReturnGoodsDTO.getId()+"");
					if(passKeyResult.isSuccess()){
						model.addAttribute("passKey", passKeyResult.getResult());
					}
				}
			}
		}
		
		List<ComplainDTO> listComplainDTO = complainExportService
				.getComplainByCondition(complainDTO);
		Map<String, String> statusMap = getComplainStatus(listComplainDTO);

		// 如果正在投诉0或已仲裁1，页面按钮显示为“查看仲裁信息”，如果为已撤销2，页面按钮显示为“申请仲裁”
		model.addAttribute("buyerStatus", statusMap.get("buyerStatus"));
		model.addAttribute("sellerStatus", statusMap.get("sellerStatus"));

		model.addAttribute("progressBar", progressBar); // 页面进度条
		
		return "sellcenter/order/refundAgreementSubmit";
	}

	/**
	 *
	 * <p>Discription:[买家：申请退款弹出框]</p>
	 * Created on 2015年4月10日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundBox" })
	public String refundBox(HttpServletRequest request, Model model) {
		String orderId = request.getParameter("orderId");
		if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
			//参数不对直接返回
			return "sellcenter/order/refund_box";
		}

		TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
		tradeOrdersQueryInDTO.setOrderId(orderId);
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
				jsonObject.put("state", tradeOrdersDTO.getState());

				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
				JSONArray jsonArray_item = new JSONArray();
				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					JSONObject jsonObject_item = new JSONObject();
					jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
					jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
					jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());

			    	ItemShopCartDTO dto = new ItemShopCartDTO();
					dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
					dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
					dto.setQty(tradeOrderItemsDTO.getNum());//数量
					dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
					dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
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
						jsonObject_item.put("itemName", itemDTO.getItemName());
					}
					jsonArray_item.add(jsonObject_item);
				}
				jsonObject.put("items", jsonArray_item);

				model.addAttribute("jsonObject", jsonObject);
				//System.out.println("jsonObject===="+JSON.toJSONString(jsonObject));
			}
		}

		return "sellcenter/order/refund_box";
	}
	/**
	 *
	 * <p>Discription:[卖家： 查询退款订单列表]</p>
	 * Created on 2015年4月16日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundSeller" })
	public String refundSeller(HttpServletRequest request, Model model) {
		Pager<TradeReturnGoodsDTO> pager = new Pager<TradeReturnGoodsDTO>();
		TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
		String userToken = LoginToken.getLoginToken(request);
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				String page = request.getParameter("page");
				if(StringUtils.isNotEmpty(page) && StringUtils.isNumeric(page)){
					pager.setPage(Integer.parseInt(page));
					model.addAttribute("page", page);
				}
				String orderId = request.getParameter("orderId");
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				String state = request.getParameter("state");
				model.addAttribute("orderId", orderId);
				model.addAttribute("startTime", startTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("state", state);

				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
                UserDTO userDTO = userExportService.queryUserById(registerDto.getUid());
                //此处特殊处理：如果存在父级id说明是子账号(也就是店员)，子账号应该能看到自己所在店铺内的所有退款/退货处理信息
                if(null == userDTO.getParentId()){
                    tradeReturnDto.setSellerId(registerDto.getUid().toString());
                }else{
                    tradeReturnDto.setSellerId(userDTO.getParentId() + "");
                }
				if(StringUtils.isNotEmpty(orderId)){
					tradeReturnDto.setOrderId(orderId);
				}
				if(StringUtils.isNotEmpty(state) && StringUtils.isNumeric(state)){
					tradeReturnDto.setState(Integer.parseInt(state));
				}
				if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					try {
						tradeReturnDto.setApplyDtBegin(format.parse(startTime));
						tradeReturnDto.setApplyDtEnd(format.parse(endTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				queryDto.setTradeReturnDto(tradeReturnDto);

				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(pager, queryDto);
				if(dg != null && dg.getRows() != null){
					for(int i = 0 ; i< dg.getRows().size() ; i++){
						TradeReturnGoodsDTO  tradeReturnGoodsDTO = dg.getRows().get(i);
						Long userId = userDTO.getUid();
						ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", tradeReturnGoodsDTO.getId()+"");
						if(passKeyEr.isSuccess()){
							tradeReturnGoodsDTO.setPassKey(passKeyEr.getResult());
						}
					}
				}
				pager.setPage(pager.getPage());
				pager.setTotalCount(dg.getTotal().intValue());
				pager.setRecords(dg.getRows());
				if (pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
					pager.setEndPageIndex(pager.getStartPageIndex());
				}
				model.addAttribute("pager", pager);
				//System.out.println("pager==="+JSON.toJSONString(pager));
			}
		}
		return "sellcenter/order/refund_seller";
	}
	/**
	 *
	 * <p>Discription:[卖家： 查看退款详情]</p>
	 * Created on 2015年4月17日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "refundInfoSeller" })
	public String refundInfoSeller(HttpServletRequest request, Model model) {
		String returnGoodId = request.getParameter("returnGoodId");
		
		int progressBar=3;	//默认进度条3
		
		if(StringUtils.isNotEmpty(returnGoodId)){
			ExecuteResult<TradeReturnInfoDto> result = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnGoodId);
			if(result!=null && result.getResult()!= null){
				//System.out.println("result==========="+JSON.toJSONString(result));
				model.addAttribute("tradeReturnGoodsDetailList", result.getResult().getTradeReturnGoodsDetailList());
				TradeReturnGoodsDTO tradeReturnDto = result.getResult().getTradeReturnDto();
				//对退货ID加密
				Long userId = WebUtil.getInstance().getUserId(request);
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", tradeReturnDto.getId()+"");
				if(passKeyEr.isSuccess()){
					tradeReturnDto.setPassKey(passKeyEr.getResult());
				}
				
				
				//判断是否为纯服务商品
				ExecuteResult<ItemDTO> item ;
				tradeReturnDto.setIsService("1");//纯服务商品
				for( TradeReturnGoodsDetailDTO tradeReturnGoods: result.getResult().getTradeReturnGoodsDetailList()){
					 item = itemExportService.getItemById(tradeReturnGoods.getGoodsId());
					 if(item.getResult().getAddSource() < 4 ){
						 tradeReturnDto.setIsService("2");//不是纯服务商品
						 break;
					 }
				}
				
				model.addAttribute("tradeReturnDto", tradeReturnDto);
				if(tradeReturnDto.getState()==6){
					progressBar=4;	//退款完成 4  进度条
				}
				//订单信息
				String orderId = result.getResult().getTradeReturnDto().getOrderId();
				ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
				if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
					model.addAttribute("tradeOrdersDTO", resultTradeOrdersDTO.getResult());

					//获取店铺名称
					ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(resultTradeOrdersDTO.getResult().getShopId());
					if(result_shopDto!=null && result_shopDto.getResult()!=null){
						ShopDTO shopDTO = result_shopDto.getResult();
						model.addAttribute("shopName", shopDTO.getShopName());
					}
				}
				//获取卖家用户名称
				String sellerId = result.getResult().getTradeReturnDto().getSellerId();
				UserDTO user = userExportService.queryUserById(Long.valueOf(sellerId));
				if(user!=null){
					model.addAttribute("sellerName", user.getUname());
					model.addAttribute("sellerMobile", user.getUmobile());
				}
				
				//查询提交的仲裁信息
				ComplainDTO complainDTO=new ComplainDTO();
				complainDTO.setOrderId(orderId);
				complainDTO.setReturnGoodsId(Long.valueOf(returnGoodId));
				List<ComplainDTO> listComplainDTO = complainExportService.getComplainByCondition(complainDTO);
				
				Map<String, String> statusMap = getComplainStatus(listComplainDTO);
				
				// 如果正在投诉0或已仲裁1，页面按钮显示为“查看仲裁信息”，如果为已撤销2，页面按钮显示为“申请仲裁”
				model.addAttribute("buyerStatus", statusMap.get("buyerStatus"));
				model.addAttribute("sellerStatus", statusMap.get("sellerStatus"));
				
				model.addAttribute("progressBar", progressBar);	//页面进度条
			}
		}
		
		return "sellcenter/order/refundInfo_seller";
	}
	
	public Map<String,String> getComplainStatus(List<ComplainDTO> listComplainDTO){
		
		String buyerStatus="2";
		String sellerStatus="2";
		
		for (ComplainDTO comD : listComplainDTO) {
			 int status=comD.getStatus();
			 String complainType=comD.getComplainType();
			 if("1".equals(complainType)){
				 if("1".equals(buyerStatus)){
					 continue;
				 }
				 
				 if(status==1){
					 buyerStatus="1";
				 }else if (status==0){
					 buyerStatus="0";
				 }
				 
			 }else if ("2".equals(complainType)){
				 if("1".equals(sellerStatus)){
					 continue;
				 }
				 
				 if(status==1){
					 sellerStatus="1";
				 }else if (status==0){
					 sellerStatus="0";
				 }
			 }
			 
		}
		Map<String,String> map=new HashMap<String, String>();
		map.put("buyerStatus", buyerStatus);
		map.put("sellerStatus", sellerStatus);
		return map;
	}
	/**
	 *
	 * <p>Discription:[卖家： 同意/拒绝 退款；买家：确认收款]</p>
	 * Created on 2015年4月17日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "updateTradeReturn" })
	@ResponseBody
	public ExecuteResult<TradeReturnGoodsDTO> updateTradeReturn(HttpServletRequest request, Model model) {
		ExecuteResult<TradeReturnGoodsDTO>  result = new ExecuteResult<TradeReturnGoodsDTO>();
		
		List<String> errorMessages = new ArrayList<String>();
		String returnId = request.getParameter("returnId");
		if(StringUtils.isEmpty(returnId) || !StringUtils.isNumeric(returnId)){
			errorMessages.add("退款id不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String type = request.getParameter("type");//1、同意退款  2、不同意退款
		if(StringUtils.isEmpty(type) || !StringUtils.isNumeric(type)){
			errorMessages.add("操作类型type不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		Long userId = -1L;
		String userToken = LoginToken.getLoginToken(request);
		if(StringUtils.isNotEmpty(userToken)){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				userId = registerDto.getUid();
			}
		}
		Long parentId = -1L;
		UserDTO userDto = userExportService.queryUserById(userId);
		if(userDto!=null){
			if(userDto.getParentId()!=null){
				parentId = userDto.getParentId();
			}
		}
		if("1".equals(type)){
			//同意退款
			String orderId = request.getParameter("orderId");
			// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
			if(StringUtils.isEmpty(orderId) /*|| !StringUtils.isNumeric(orderId)*/){
//				errorMessages.add("订单Id不能为空，必须是数字");
				errorMessages.add("订单Id不能为空");
				result.setErrorMessages(errorMessages);
				return result;
			}
			ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
			if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
				Integer state = resultTradeOrdersDTO.getResult().getState();
				if(state > 3 && state < 6 ){
					//买家已收到货
					//卖家填写收货地址，让买家发货
					String returnAddress = request.getParameter("returnAddress");
					String returnPhone = request.getParameter("returnPhone");
					String returnPostcode = request.getParameter("returnPostcode");

					TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
					dto.setId(Long.valueOf(returnId));
					if(StringUtils.isNotEmpty(returnAddress)){
						dto.setReturnAddress(returnAddress);
					}
					if(StringUtils.isNotEmpty(returnPhone)){
						dto.setReturnPhone(returnPhone);
					}
					if(StringUtils.isNotEmpty(returnPostcode)){
						dto.setReturnPostcode(returnPostcode);
					}
					if(parentId!=null && parentId.intValue()>0){
						dto.setSellerId(parentId.toString());
					}else{
						dto.setSellerId(userId.toString());
					}
					dto.setOrderId(orderId);
					
					ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
					TradeReturnGoodsDTO tradeReturnGoodsDTO=executeResult.getResult().getTradeReturnDto();
					if(null != tradeReturnGoodsDTO){
						if(tradeReturnGoodsDTO.getState() == TradeReturnStatusEnum.ORDERRERURN.getCode()){
							//卖家收获同意退款
							dto.setState(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode());
							dto.setConfirmStatus("0");//待审核确认
							result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.PLATFORMTOREFUND);
						}else{
							dto.setRefundTime(new Date());//同意退款时间
							dto.setState(TradeReturnStatusEnum.PASS.getCode());
							result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.PASS);
						}
					}else{
						dto.setRefundTime(new Date());//同意退款时间
						dto.setState(TradeReturnStatusEnum.PASS.getCode());
						result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.PASS);
					}
				}else{
					//买家未收货
					/*String paypwd = request.getParameter("paypwd");
					if(userId.equals(new Long(-1))){//判断用户是否登录
						errorMessages.add("用户未登录！");
						result.setErrorMessages(errorMessages);
						return result;
					}
					if(StringUtils.isEmpty(paypwd)){
						errorMessages.add("支付密码不能为空");
						result.setErrorMessages(errorMessages);
						return result;
					}
					paypwd = MD5.encipher(paypwd);
					ExecuteResult<String> payResult = userExportService.validatePayPassword(userId, paypwd);
					if("1".equals(payResult.getResult())){//验证支付码是否正确
*/						TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
						dto.setId(Long.valueOf(returnId));
						dto.setOrderId(orderId);
						ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
						TradeReturnGoodsDTO tradeReturnGoodsDTO=executeResult.getResult().getTradeReturnDto();
						TradeReturnStatusEnum status=TradeReturnStatusEnum.REFUNDING;
						//默认所以支付都是个人支付
						resultTradeOrdersDTO.getResult().setPaymentMethod(1);
						/*if(resultTradeOrdersDTO.getResult().getPaymentMethod() != 1||PayBankEnum.OFFLINE.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())
							||PayBankEnum.OTHER.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
							dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());//线下支付，企业支付，其他支付不走ecm确认退款流程
						}else{*/
						
						//}
						//没有线下支付
						dto.setState(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode());
						dto.setConfirmStatus("0");//待审核确认
						status=TradeReturnStatusEnum.PLATFORMTOREFUND;
						if(parentId!=null && parentId.intValue()>0){
							dto.setSellerId(parentId.toString());
						}else{
							dto.setSellerId(userId.toString());
						}
						dto.setRefundTime(new Date());//同意退款时间
						result = tradeReturnExportService.updateTradeReturnStatus(dto, status);//待平台处理退款
					/*}else{
						errorMessages.add("支付密码错误，请重新输入！");
						result.setErrorMessages(errorMessages);
						return result;
					}*/
					if(result.isSuccess()){
						//发送短信、邮件、站内信提醒
						request.setAttribute("result",result);
						// 计算退款金额
						tradeReturnExportService.calTotalRefundGoods(returnId);
						// 计算积分退还
						tradeReturnExportService.calcRefundIntegral(returnId);
					}
				}
			}
		}else if("2".equals(type)){
			//不同意退款
			String returnAddress = request.getParameter("returnAddress");
			String returnPhone = request.getParameter("returnPhone");
			String returnPostcode = request.getParameter("returnPostcode");
			String auditRemark = request.getParameter("auditRemark");
			if(StringUtils.isEmpty(auditRemark) && StringUtils.isNumeric(auditRemark)){
				errorMessages.add("拒绝退款原因不能为空");
				result.setErrorMessages(errorMessages);
				return result;
			}
			TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
			dto.setId(Long.valueOf(returnId));
			dto.setState(TradeReturnStatusEnum.DISAGRESS.getCode());
			if(parentId!=null && parentId.intValue()>0){
				dto.setSellerId(parentId.toString());
			}else{
				dto.setSellerId(userId.toString());
			}
			if(StringUtils.isNotEmpty(returnAddress)){
				dto.setReturnAddress(returnAddress);
			}
			if(StringUtils.isNotEmpty(returnPhone)){
				dto.setReturnPhone(returnPhone);
			}
			if(StringUtils.isNotEmpty(returnPostcode)){
				dto.setReturnPostcode(returnPostcode);
			}
			dto.setAuditRemark(auditRemark);
			result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.DISAGRESS);
		}else if("3".equals(type)){
			//卖家确认收货
			/*String paypwd = request.getParameter("paypwd");
			if(userId.equals(new Long(-1))){//判断用户是否登录
				errorMessages.add("用户未登录！");
				result.setErrorMessages(errorMessages);
				return result;
			}
			if(StringUtils.isEmpty(paypwd)){
				errorMessages.add("支付密码不能为空");
				result.setErrorMessages(errorMessages);
				return result;
			}
			paypwd = MD5.encipher(paypwd);
			ExecuteResult<String> payResult = userExportService.validatePayPassword(userId, paypwd);
			if("1".equals(payResult.getResult())){//验证支付码是否正确
			//退款退货同意退款
	*/				
				TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
				dto.setId(Long.valueOf(returnId));
				ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
				TradeReturnGoodsDTO tradeReturnGoodsDTO=executeResult.getResult().getTradeReturnDto();
				ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(tradeReturnGoodsDTO.getOrderId());
				TradeReturnStatusEnum status=TradeReturnStatusEnum.REFUNDING;
				/*if(resultTradeOrdersDTO.getResult().getPaymentMethod()!=1||PayBankEnum.OFFLINE.name().equals(PayBankEnum.getEnumByQrCode(tradeReturnGoodsDTO.getOrderPayBank() == null?-1:Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())
					||PayBankEnum.OTHER.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
					dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());//线下支付，企业支付，其他支付不走ecm确认退款流程
				}else{*/
					
				//}//没有线下支付
				dto.setState(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode());
				dto.setConfirmStatus("0");//待审核确认
				status=TradeReturnStatusEnum.PLATFORMTOREFUND;
				if(parentId!=null && parentId.intValue()>0){
					dto.setSellerId(parentId.toString());
				}else{
					dto.setSellerId(userId.toString());
				}
				dto.setRefundTime(new Date());//同意退款时间
				result = tradeReturnExportService.updateTradeReturnStatus(dto, status);//待平台处理退款
				//发送短信、邮件通知
				if(result.isSuccess()){
					request.setAttribute("result",result);
					// 计算退款金额
					tradeReturnExportService.calTotalRefundGoods(returnId);
					// 计算积分退还
					tradeReturnExportService.calcRefundIntegral(returnId);
				}
			/*}else{
				errorMessages.add("支付密码错误，请重新输入！");
				result.setErrorMessages(errorMessages);
				return result;
			}*/
			
		}else if("4".equals(type)){
			//买家 确认收款
			TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
			dto.setId(Long.valueOf(returnId));
			dto.setState(TradeReturnStatusEnum.SUCCESS.getCode());
			dto.setBuyId(userId.toString());
			dto.setOrderStatus("5");//订单设为已完成状态
			result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.SUCCESS);
			//退款成功后调用
			if(result.isSuccess()){
				complainCancle1(returnId);
			}
		}
		return result;
	}
	//取消投诉
	//tradeReturnid是退货单id
	private Map<String,String> complainCancle1(String tradeReturnid){
        Map<String,String> map=new HashMap<String, String>();
        try{
            ComplainDTO comp=new ComplainDTO();
            comp.setRefundId(new Long(tradeReturnid));
            comp.setStatus(0);
            //获取尚未投诉尚未处理完的投诉单
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            DataGrid<ComplainDTO> da=complainExportService.findInfoByCondition(comp,null);
            if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
                List<ComplainDTO> list=da.getRows();
                for(int i=0;i<list.size();i++){
                    ComplainDTO complainDTO=list.get(0);
                    //取消仲裁
                    complainDTO.setStatus(new Integer(2));
                    complainDTO.setModified(new Date());
                    complainDTO.setResolutionTime(new Date());
                    ExecuteResult<String> executeResul= complainExportService.modifyComplainInfo(complainDTO);
                }
            }
            map.put("flag","success");
            map.put("msg","取消完成");
        }catch(Exception e){
            map.put("flag","error");
            map.put("msg","取消投诉出现意外错误："+e.getMessage());
        }
        return map;
    }
	/**
	 *
	 * <p>Discription:[卖家： 退款完成页面]</p>
	 * Created on 2015年4月17日
	 * @param request
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping({ "updateTradeReturnSucc" })
	public String updateTradeReturnSucc(HttpServletRequest request, Model model) {
		return "sellcenter/order/refundSucc_seller";
	}
	
	/**
	 * 
	 * <p>Discription:[卖家：添加物流信息]</p>
	 * Created on 2015年5月20日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "modifyLogistics" })
	public ExecuteResult<String> modifyLogistics(HttpServletRequest request) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errorMessages = new ArrayList<String>();
		String orderId = request.getParameter("orderId");
		//退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if(StringUtils.isEmpty(orderId) /*|| !StringUtils.isNumeric(orderId)*/){
			//errorMessages.add("订单Id不能为空，必须是数字");
			errorMessages.add("订单Id不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String logisticsNo = request.getParameter("logisticsNo");
		if(StringUtils.isEmpty(logisticsNo)){
			errorMessages.add("物流编号不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String logisticsCompany = request.getParameter("logisticsCompany");
		String logisticsRemark = request.getParameter("logisticsRemark");
		
		if(StringUtils.isEmpty(logisticsCompany)){
			errorMessages.add("物流公司不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		//当前登录的卖家用户
		Long userId = WebUtil.getInstance().getUserId(request);
		if(userId==null){
			errorMessages.add("用户未登录");
			result.setErrorMessages(errorMessages);
			return result;
		}
		TradeOrdersDTO trd = new TradeOrdersDTO();
		trd.setOrderId(orderId);
		trd.setLogisticsNo(logisticsNo);//  物流编号
		trd.setLogisticsCompany(logisticsCompany);//  物流公司
		trd.setLogisticsRemark(logisticsRemark);//  备注
		trd.setSellerId(userId);
		result = tradeOrderExportService.modifyLogisticsInfo(trd);
		return result;
	}
	/**
	 * 
	 * <p>Discription:[卖家： 确认付款]</p>
	 * Created on 2015年5月20日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "modifyOrderPayStatus" })
	public ExecuteResult<String> modifyOrderPayStatus(HttpServletRequest request) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errorMessages = new ArrayList<String>();
		String orderId = request.getParameter("orderId");
		// 退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
		if(StringUtils.isEmpty(orderId) /*|| !StringUtils.isNumeric(orderId)*/){
//			errorMessages.add("订单Id不能为空，必须是数字");
			errorMessages.add("订单Id不能为空");
			result.setErrorMessages(errorMessages);
			return result;
		}
		String paymentType = request.getParameter("paymentType");
		if(StringUtils.isEmpty(paymentType) || !StringUtils.isNumeric(paymentType)){
			errorMessages.add("支付类型不能为空，必须是数字");
			result.setErrorMessages(errorMessages);
			return result;
		}
		//当前登录的卖家用户
		Long userId = WebUtil.getInstance().getUserId(request);
		if(userId==null){
			errorMessages.add("用户未登录");
			result.setErrorMessages(errorMessages);
			return result;
		}
		TradeOrdersDTO inDTO = new TradeOrdersDTO();
		inDTO.setOrderId(orderId);
		inDTO.setPaid(2);
		inDTO.setPaymentType(Integer.parseInt(paymentType));
		inDTO.setSellerId(userId);
		result = tradeOrderExportService.modifyOrderPayStatus(inDTO);
		request.setAttribute("result",result);
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[卖家： 确认收货][卖家： 同意退款（买家未收货）]</p>
	 * Created on 2015年7月8日18:09:26
	 * @param request
	 * @return
	 * @author:[武超强]
	 */
	@RequestMapping("getAskByPayType")
	@ResponseBody
	public JSONObject getAskByPayType(HttpServletRequest request){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("desc", "success");
		String orderId = request.getParameter("orderId");
		String paypwd = request.getParameter("paypwd");
		Long userId = -1L;
		String userToken = LoginToken.getLoginToken(request);
		if(StringUtils.isNotEmpty(userToken)){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				userId = registerDto.getUid();
			}
		}
		
		if(userId.equals(new Long(-1))){//判断用户是否登录
			jsonObject.put("desc", "error");
			jsonObject.put("result", "用户未登录！");
			return jsonObject;
		}
		if(StringUtils.isEmpty(paypwd)){
			jsonObject.put("desc", "error");
			jsonObject.put("result", "支付密码不能为空");
			return jsonObject;
		}
		paypwd = MD5.encipher(paypwd);
		ExecuteResult<String> payResult = userExportService.validatePayPassword(userId, paypwd);
		if(!"1".equals(payResult.getResult())){//验证支付码是否正确
			jsonObject.put("desc", "error");
			jsonObject.put("result", "支付密码错误，请重新输入！");
			return jsonObject;
		}
		
		//获取订单信息
		ExecuteResult<TradeOrdersDTO> orderRes=tradeOrderExportService.getOrderById(orderId);
		if (orderRes != null && orderRes.getResult() != null) {
            TradeOrdersDTO orderItem = orderRes.getResult();
            //判断是否是线下支付
            if(orderItem.getPaymentType() == 3){
            	jsonObject.put("result", "nopay");
            }
		}
		return jsonObject;
	}


	@RequestMapping(value = "/export", method = RequestMethod.POST)
	public String export(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, Integer page, HttpServletRequest request, Model model) {
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			return "redirect:/user/login";
		}

		Long shopId = null;
		shopId = WebUtil.getInstance().getShopId(request);
		if (shopId == null) {
			return "redirect:/user/login";
		}

		tradeOrdersQueryInDTO.setShopId(shopId);
		String selectTime = request.getParameter("selectTime");
		if(StringUtils.isNotEmpty(selectTime) && StringUtils.isNumeric(selectTime)){
			model.addAttribute("selectTime", Integer.parseInt(selectTime));
		}
		//待付款类型,默认为 1,  1是正常的  2 是分期付款
		String shipmentType = request.getParameter("shipmentType");
		if("2".equals(shipmentType)){
			model.addAttribute("shipmentType", Integer.parseInt(shipmentType));
			tradeOrdersQueryInDTO.setState(null);
			tradeOrdersQueryInDTO.setShipmentType(Integer.parseInt(shipmentType));
		}else{
			shipmentType = "1";
			model.addAttribute("shipmentType", Integer.parseInt(shipmentType));
			tradeOrdersQueryInDTO.setShipmentType(null);
		}


		Integer pageState = tradeOrdersQueryInDTO.getState();
		Integer allState = null ;
		if(pageState == null || pageState == 0){
			tradeOrdersQueryInDTO.setState(allState);
		}

		//根据商品名称查询
		String itemName = tradeOrdersQueryInDTO.getItemName();
		if(StringUtils.isNotEmpty(itemName)){
			tradeOrdersQueryInDTO.setItemIdList(getListItemId(itemName));
		}
		//根据买家名查询
		String buyerName = request.getParameter("buyerName");
		if(StringUtils.isNotEmpty(buyerName)){
			tradeOrdersQueryInDTO.setBuyerIdList(getListUid(buyerName));
		}
		//卖家标记
		tradeOrdersQueryInDTO.setUserType(2);


		tradeOrdersQueryInDTO.setDeleted(1);
		ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, null);
		SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd: hh:mm:ss");

		DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
		List orderList = new ArrayList();
		JSONArray jsonArray = new JSONArray();
		if (executeResult != null) {
			dataGrid = executeResult.getResult();
			List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
			int i=0;
			for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){

				//获取店铺名称
				ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());

				List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();

				for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
					HashMap map = new HashMap();
					map.put("id",String.valueOf(++i));
					map.put("orderId",String.valueOf(tradeOrdersDTO.getOrderId()));
					if(tradeOrdersDTO.getState()==1){
						map.put("state","待付款");
					}else if(tradeOrdersDTO.getState()==2){
						map.put("state","待配送 ");
					}else if(tradeOrdersDTO.getState()==3){
						map.put("state","待收货");
					}else if (tradeOrdersDTO.getState()==4){
						map.put("state","待评价");
					}else if (tradeOrdersDTO.getState()==5){
						map.put("state","已完成");
					}else if(tradeOrdersDTO.getState()==6){
						map.put("state","已取消");
					}
					map.put("name",String.valueOf(tradeOrdersDTO.getName()));
					if(tradeOrdersDTO.getAfterService()==1){
						map.put("afterService","无");
					}else if(tradeOrdersDTO.getAfterService()==2){
						map.put("afterService","是");
					}else{
						map.put("afterService","完成");
					}

					if(result_shopDto!=null && result_shopDto.getResult()!=null){
						ShopDTO shopDTO = result_shopDto.getResult();
						map.put("shopId", tradeOrdersDTO.getShopId());
						map.put("shopName", String.valueOf(shopDTO.getShopName()));
					}
					map.put("createTime", String.valueOf(sdf2.format(tradeOrdersDTO.getCreateTime())));

					map.put("num", String.valueOf(tradeOrderItemsDTO.getNum()));
					map.put("payPrice", String.valueOf(tradeOrderItemsDTO.getPayPrice()));
					map.put("payPriceTotal", String.valueOf(tradeOrderItemsDTO.getPayPriceTotal()));
					map.put("promotionDiscount", tradeOrderItemsDTO.getPromotionDiscount());

					ItemShopCartDTO dto = new ItemShopCartDTO();
					dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
					dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
					dto.setQty(tradeOrderItemsDTO.getNum());//数量
					dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
					dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
					map.put("skuId", tradeOrderItemsDTO.getSkuId());
					ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
					//获取商品名称
					map.put("itemId", tradeOrderItemsDTO.getItemId());
					ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
					if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
						ItemDTO itemDTO = result_itemDTO.getResult();
						map.put("itemName", itemDTO.getItemName());
					}
					orderList.add(map);
				}

			}
		}

		JRDataSource jrDataSource = new JRBeanCollectionDataSource(orderList);

		// 动态指定报表模板url
		model.addAttribute("url", "/WEB-INF/jasper/mall_order.jasper");
		model.addAttribute("format", "xls"); // 报表格式
		model.addAttribute("jrMainDataSource", jrDataSource);

		return "reportView"; // 对应jasper-views.xml中的bean id
	}

    @RequestMapping(value = "/toPrint")
    public String orderPrint(String orderId, HttpServletRequest request, Model model){
    	List<Double> refundMoney=new ArrayList<Double>();
    	double totalMoney=0.0;
        model.addAttribute("message","订单号异常！");
        model.addAttribute("siteUrl", "/order/querySeller");
//      退款冲抵账户（平台的特殊账户）的订单号以“R”开头，不需要判断是不是数字
//      if(!StringUtils.isNumeric(orderId)){
//          return "/message";
//      }
        if(StringUtils.isBlank(orderId)){
            return "/message";
        }
        ExecuteResult<com.camelot.tradecenter.dto.TradeOrdersDTO> result = tradeOrderExportService.getOrderById(orderId);
        TradeOrdersDTO tradeOrdersDTO = result.getResult();
        if(null == tradeOrdersDTO){
            return "/message";
        }
      //获取订单中商品的总数量
        int totalNum = getTotalNumForOrder(tradeOrdersDTO);//商品总数量
        model.addAttribute("orderTotalNum", totalNum);
        TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
		TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
		tradeReturnDto.setOrderId(tradeOrdersDTO.getOrderId().toString());
		queryDto.setTradeReturnDto(tradeReturnDto);
		DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
		 List<TradeOrderItemsDTO> items=tradeOrdersDTO.getItems();
	        for(TradeOrderItemsDTO item : items){
	        	List<Integer> list=new ArrayList<Integer>();
	        	int sum=0;
	        	for (TradeReturnGoodsDTO trg : dg.getRows()) {
					int num=0;
	        		List<TradeReturnGoodsDetailDTO> deDto=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(trg.getId()));
	        		boolean flag=item.getSkuId().longValue()==deDto.get(0).getSkuId().longValue();
	        		if((TradeReturnStatusEnum.PLATFORMTOREFUND.getCode()==trg.getState()||TradeReturnStatusEnum.PLATFORMDEALING.getCode()==trg.getState()
                			||TradeReturnStatusEnum.REFUNDING.getCode()==trg.getState()||TradeReturnStatusEnum.SUCCESS.getCode()==trg.getState()
                			||TradeReturnStatusEnum.REFUNDFAIL.getCode()==trg.getState()||TradeReturnStatusEnum.REFUNDAPPLICATION.getCode()==trg.getState()
                			||TradeReturnStatusEnum.REFUNDAPPLICATION_CUP.getCode()==trg.getState())
                			&&"0".equals(trg.getIsCustomerService())&&flag){//待平台处理，平台处理中，退款失败，退款申请成功,等待同意退款，退款中，退款成功即卖家同意退款后并且是申请退款退货时售后不算因为售后是已经发货，才计算退货的数量和退货的金额
	        			 num=deDto.get(0).getRerurnCount();
	        			 refundMoney.add(deDto.get(0).getReturnAmount().doubleValue());
	        		}
	        		list.add(num);
	        		
	        	}
	        	for(int i=0;i<list.size();i++){
        			int j=(Integer) list.get(i);
        			sum+=j;
        		}
	        		item.setNum(item.getNum()-sum);
	        		
	        		//2015-8-13宋文斌start
					ExecuteResult<String> result_attr = itemExportService.queryAttrBySkuId(item.getSkuId());
					if (result_attr != null && result_attr.getResult() != null) {
						String attrStr = result_attr.getResult();
						//根据sku的销售属性keyId:valueId查询商品属性
						ExecuteResult<List<ItemAttr>> itemAttr = itemCategoryService.queryCatAttrByKeyVals(attrStr);
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
							item.setSkuString(title);						}
					}
	        		
	        }
        //获取店铺名称
        ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
        if(result_shopDto!=null && result_shopDto.getResult()!=null){
            ShopDTO shopDTO = result_shopDto.getResult();
            model.addAttribute("shopId", tradeOrdersDTO.getShopId());
            model.addAttribute("shopName", shopDTO.getShopName());
        }
        //获取买家用户名
        UserDTO buyer = userExportService.queryUserById(tradeOrdersDTO.getBuyerId());
        model.addAttribute("buyerUserName", buyer.getUname());
        //获取订单项列表
        List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
        JSONArray orderItems = buildOrderItem(tradeOrdersDTO);
        //获取卖家信息
        UserDTO seller = userExportService.queryUserById(tradeOrdersDTO.getSellerId());
        model.addAttribute("sellerUserName", seller.getUname());//发货人
        model.addAttribute("sellerMobile", seller.getUmobile());//发货人联系方式

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("tradeOrdersDTO", tradeOrdersDTO);
        for(int i=0;i<refundMoney.size();i++){
			double j=(double) refundMoney.get(i);
			totalMoney+=j;
		}
        model.addAttribute("totalMoney", totalMoney);
        return "sellcenter/order/order_print";
    }

    /**
     * 组装订单中商品的总数量
     * @param tradeOrdersDTO
     * @return
     */
    private int getTotalNumForOrder(TradeOrdersDTO tradeOrdersDTO) {
        int totalNum = 0;
        List<TradeOrderItemsDTO> items = tradeOrdersDTO.getItems();
        for(int i=0; items!=null && i<items.size(); i++){
            TradeOrderItemsDTO item = items.get(i);
            totalNum += item.getNum();
        }
        return totalNum;
    }

    /**
     * 构建订单项
     * @param tradeOrdersDTO
     * @return
     */
    private JSONArray buildOrderItem(TradeOrdersDTO tradeOrdersDTO) {
        JSONArray jsonArray_item = new JSONArray();
        for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrdersDTO.getItems()){
            JSONObject jsonObject_item = new JSONObject();
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
                jsonObject_item.put("itemName", itemDTO.getItemName());
            }
            jsonArray_item.add(jsonObject_item);
        }
        return jsonArray_item;
    }
    /**
     * 
     * <p>Discription:[审核通过]</p>
     * Created on 2015-8-25
     * @param request
     * @return
     * @author:[王鹏]
     */
    @ResponseBody
    @RequestMapping(value="approveSubmit")
    public Json modifyStatus(HttpServletRequest request){
    	 Json json = new Json();
    	 try{
    		 String orderId=request.getParameter("orderId");
        	 String status=request.getParameter("status");
        	 String parentId=request.getParameter("parentId");
        	 TradeOrdersDTO dto=new TradeOrdersDTO();
        	 dto.setApproveStatus(status);
        	 dto.setOrderId(orderId);
        	 dto.setParentOrderId(parentId);
        	 tradeApprovedOrdersExportService.approveSubmit(dto);
        	 json.setMsg("审核通过");
        	 json.setSuccess(true);
    	 }catch(Exception e){
    		 logger.error(e.getMessage());
             json.setMsg("系统出现意外错误，请联系管理员");
             json.setSuccess(false);
    	 }
    	return json;
    }
    /**
     * 
     * <p>Discription:[审核驳回]</p>
     * Created on 2015-8-25
     * @param request
     * @return
     * @author:[王鹏]
     */
    @ResponseBody
    @RequestMapping(value="approveReject")
    public Json approveReject(HttpServletRequest request){
    	 Json json = new Json();
    	 try{
    		 String orderId=request.getParameter("orderId");
        	 String status=request.getParameter("status");
        	 String reason=request.getParameter("reason");
        	 TradeOrdersDTO dto=new TradeOrdersDTO();
        	 dto.setApproveStatus(status);
        	 dto.setOrderId(orderId);
        	 dto.setRejectReason(reason);
        	 tradeApprovedOrdersExportService.approveReject(dto);
        	 json.setMsg("驳回成功");
        	 json.setSuccess(true);
    	 }catch(Exception e){
    		 logger.error(e.getMessage());
             json.setMsg("系统出现意外错误，请联系管理员");
             json.setSuccess(false);
    	 }
    	return json;
    }
    
    
    /**
	 *
	 * <p>Discription:[订单附件上传]</p>
	 * Created on 2015年4月9日
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@ResponseBody
	@RequestMapping({ "addEnclosure" })
	public ExecuteResult<String> addEnclosure(String url,String orderId,String enclosureName,String remark,long id) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		TradeOrdersEnclosureDTO dto = new TradeOrdersEnclosureDTO();
        dto.setEnclosureUrl(url);
        dto.setIsDelete(0);
        dto.setOrderId(orderId);
        dto.setEnclosureName(enclosureName);
        dto.setRemark(remark);
        dto.setId(id);
        ExecuteResult<TradeOrdersEnclosureDTO> addEnclosure = tradeOrdersEnclosureService.addEnclosure(dto);
        result.setResult(String.valueOf(addEnclosure.getResult().getId()));
        result.setResultMessage("success");
		return result;
	}
	
	@ResponseBody
	@RequestMapping("delEnclosure")
	public ExecuteResult<String> delEnclosure(long id){
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(id!=0){
			result = tradeOrdersEnclosureService.delEnclosure(id);
		}
		return result;
	}
	
	/**
	 * <p> Discription:[同意退款方法，调用退款接口] </p>
	 * Created on 2015-9-28
	 * @param request
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping("agreeRefund")
    @ResponseBody
	public ExecuteResult<Boolean> agreeRefund(HttpServletRequest request ,String returnId ) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		
		String token = LoginToken.getLoginToken(request);
		if (StringUtils.isBlank(token)) {
			result.setResultMessage("您还没有登录，请先登录");
			return result;
		}
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		
		try {
			if (StringUtils.isNotBlank(returnId)) {
				ExecuteResult<TradeReturnInfoDto> executeResult = tradeReturnExportService
						.getTradeReturnInfoByReturnGoodsId(returnId);
				TradeReturnInfoDto tradeReturnInfoDto = executeResult.getResult();
				TradeReturnGoodsDTO tradeReturnGoodsDTO = tradeReturnInfoDto.getTradeReturnDto();
				if (null != tradeReturnGoodsDTO) {
					List<TradeReturnGoodsDetailDTO> details = tradeReturnExportService
							.getTradeReturnGoodsDetailReturnId(String.valueOf(tradeReturnGoodsDTO.getId()));
					RefundReqParam param = new RefundReqParam();
					param.setOrderNo(tradeReturnGoodsDTO.getOrderId());// 订单id
					param.setTotalAmount(tradeReturnGoodsDTO.getOrderPrice());// 订单总金额
					param.setRefundAmount(details.get(0).getReturnAmount());// 退款金额
					param.setPayBank(PayBankEnum
							.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name());// 支付方式
					param.setRefundReason(tradeReturnGoodsDTO.getReturnResult());// 退款原因
					param.setDesc(tradeReturnGoodsDTO.getRemark());// 退款描述
//					User currentUser = UserUtils.getUser();
					param.setId(registerDTO.getUid().toString());
					param.setCodeNo(tradeReturnGoodsDTO.getCodeNo());// 退款单号
					// refundTransationsDTO.setOutRefundNo(param.getOutRefundNo());//对外交易号
					ExecuteResult<Integer> res = paymentExportService.refundApply(param);
					if (1 == res.getResult()) {// 退款成功
						tradeReturnGoodsDTO.setConfirmStatus("1");// 确认同意退款
						tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDING.getCode());// 退款中
						ExecuteResult<TradeReturnGoodsDTO> returnGood = tradeReturnExportService
								.updateTradeReturnGoods(tradeReturnGoodsDTO);
						result.setResultMessage("操作成功");
						result.setResult(true);
					} else if (3 == res.getResult()) {// 退款申请成功
						tradeReturnGoodsDTO.setConfirmStatus("1");// 确认同意退款
						if("AP".equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
							tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDAPPLICATION.getCode());// 退款申请成功
						}else if("CUP".equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
							tradeReturnGoodsDTO.setState(TradeReturnStatusEnum.REFUNDAPPLICATION_CUP.getCode());// 退款申请成功
						}
						
						ExecuteResult<TradeReturnGoodsDTO> returnGood = tradeReturnExportService
								.updateTradeReturnGoods(tradeReturnGoodsDTO);
						result.setResultMessage("操作成功");
						result.setResult(true);
					}else if (4 == res.getResult()){
						result.setResultMessage("退款失败");
						result.setResult(false);
					} else {// 退款失败
						result.setResultMessage(res.getErrorMessages().toString());
						result.setResult(false);
					}
				}
			} else {
				result.setResultMessage("退货单id为空");
				result.setResult(false);
			}
		} catch (Exception e) {
			result.setResultMessage("系统出现意外错误，请联系管理员");
			result.setResult(false);
		}
		return result;
	}
    
  }
