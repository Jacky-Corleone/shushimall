	package com.camelot.mall.controller;
	
	import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.dto.RefundPayParam;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDetailDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoQueryDto;
import com.camelot.aftersale.dto.returngoods.TradeReturnPicDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.common.enums.TradeReturnStatusEnum;
import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.delivery.service.DeliveryService;
import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractMatDTO;
import com.camelot.goodscenter.dto.ContractOrderDTO;
import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemEvaluationDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemEvaluationService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.orderWx.ChildUserListPost;
import com.camelot.mall.orderWx.ContractInfo;
import com.camelot.mall.orderWx.OrderDataNeedInfo;
import com.camelot.mall.orderWx.OrderWxService;
import com.camelot.mall.orderWx.Persion;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.FTPUtils;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.pricecenter.dto.ProductAttrDTO;
import com.camelot.pricecenter.dto.PromotionDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartCouponService;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopEvaluationDTO;
import com.camelot.storecenter.dto.indto.ShopAudiinDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.storecenter.service.ShopEvaluationService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFareExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.service.InvoiceExportService;
import com.camelot.tradecenter.service.TradeApprovedOrdersExportService;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserStorePermissionExportService;
import com.camelot.usercenter.service.UserWxExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.ImageType;
import com.camelot.util.SendWeiXinMessageUtil;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;
	
	@Controller
	@RequestMapping(value="/orderWx")
	public class OrderWxController {
		
		private Logger LOG = Logger.getLogger(this.getClass());
		
		@Resource(name="orderWxService")
		private OrderWxService orderWxService;
		
		@Resource
		private UserExportService userService;
		@Resource
		private AddressInfoService addressInfoService;
		@Resource
		private AddressBaseService addressBaseService;
		@Resource
		private TradeOrderExportService tradeOrderExportService;
		@Resource
		private UserCompanyService companyService;
		@Resource
		private ShopExportService shopExportService;
		@Resource
		private ItemExportService itemService;
		@Resource
		private ProtocolExportService protocolExportService;
		@Resource
		private ShopExportService shopService;
		@Resource
		private ShopFareExportService shopFareService;
		@Resource
		private ShopCustomerServiceService shopCustomerServiceService;
		@Resource
		private TradeReturnExportService tradeReturnExportService;
		@Resource
		private ComplainExportService complainExportService;
		@Resource
		private UserExportService userExportService;
		@Resource
		private ItemEvaluationService itemEvaluationService;
		@Resource
		private ShopEvaluationService shopEvaluationService;
		@Resource
		private UserExtendsService userExtendsService;
		@Resource
		private UserWxExportService userWxExportService;
		@Resource
		private UserStorePermissionExportService userStorePermissionExportService;
		@Resource
		private ItemExportService itemExportService;
		@Resource
		private ItemCategoryService itemCategoryService;
		@Resource
		private TradeApprovedOrdersExportService tradeApprovedOrdersExportService;
		
		@Resource
		private InvoiceExportService invoiceExportService;
		
		@Resource
		private CouponsExportService couponsExportService;
		
		@Resource
		private ShopCartCouponService shopCartCouponService;
		
		@Resource
		private DeliveryService deliveryService;//物流（快递100）接口
		
		private FTPUtils ftpUtils = new FTPUtils(SysProperties.getProperty("file_server_ip"), SysProperties.getProperty("login_name"), SysProperties.getProperty("login_password"));
		/**
		 * 跳转到购物车详情页
		 * @param request
		 * @param uid
		 * @param ctoken
		 * @param regionid
		 * @return
		 */
		@RequestMapping(value="/toOrder")
		public ModelAndView toOrder(HttpServletRequest request,HttpServletResponse response,
				@CookieValue(value=Constants.USER_ID,required=true,defaultValue="1000752") String uid,
				@CookieValue(value=Constants.CART_TOKEN,required = false,defaultValue="") String ctoken,
				@CookieValue(value=Constants.REGION_CODE,required = false,defaultValue="11") String regionid){
			
	//		Cookie cookie = new Cookie("uid", "1000000639");
	//		Cookie cookie = new Cookie("uid", "1000000627");
	//		response.addCookie(cookie);
			
	//		this.addressInfoService.removeAddresBase(Long.valueOf("337"));
	//		this.addressInfoService.removeAddresBase(Long.valueOf("335"));
	//		this.addressInfoService.removeAddresBase(Long.valueOf("336"));
			
			
			ModelAndView mv = this.getOrderDetail(request,uid,ctoken,regionid);
	//		mv.setViewName("/order/order");
			List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
			UserDTO userDTO=userService.queryUserById(Long.valueOf(uid));
			if(userDTO.getParentId()!=null){
				UserDTO dto=userExportService.queryUserById(userDTO.getParentId());
				 if(dto!=null){
					ChildUserListPost childUserListPost = new ChildUserListPost();
					childUserListPost.setShopId(dto.getShopId());
					childUserListPost.setUserId(dto.getUid());
					childUserListPost.setUsername(dto.getUname());
					childUserListPost.setNickName(dto.getNickname());
					childUserListPosts.add(childUserListPost);
				 }
		    }
			if(userDTO!=null){
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(userDTO.getShopId());
				childUserListPost.setUserId(userDTO.getUid());
				childUserListPost.setUsername(userDTO.getUname());
				childUserListPost.setNickName(userDTO.getNickname());
				childUserListPosts.add(childUserListPost);
			 }
			Integer moduleType=1;
			ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService.queryChildUserList(Long.valueOf(uid), moduleType, null);
			DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();
			
			List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
			for(ChildUserDTO childUserDTO : childUserDTOs){
				String resourceName = "";
				List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO.getUserMallResourceList();
				for(UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs){
					resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
				}
				if(resourceName.length()>0){
					resourceName = resourceName.substring(0, resourceName.length()-1);
				}
				
				//查询店铺名称
				String name = "";
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(childUserDTO.getShopId());
				childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
				childUserListPost.setUserId(childUserDTO.getUserId());
				childUserListPost.setUsername(childUserDTO.getUsername());
				childUserListPost.setResourceIds(resourceName);
				childUserListPost.setNickName(childUserDTO.getNickName());
				childUserListPosts.add(childUserListPost);
			}
			mv.addObject("childUserListPosts", childUserListPosts);
			mv.addObject("userId", uid);
			
			mv.setViewName("/order/order_cart");
			
			return mv;
		}
	
		/**
		 * 获取购物车的具体信息
		 * @param request
		 * @return
		 */
		@RequestMapping(value="/getOrderDetail")
		public ModelAndView getOrderDetail(HttpServletRequest request,
				@CookieValue(value=Constants.USER_ID,required = true,defaultValue="1000752") String uid,
				@CookieValue(value=Constants.CART_TOKEN,required = false,defaultValue="") String ctoken,
				@CookieValue(value=Constants.REGION_CODE,required = false,defaultValue="11") String regionid) {
			
			ModelAndView mav = new ModelAndView();
			
			// 收货地址变更
			String ckAddrId = request.getParameter("address");
			LOG.debug("CHECK ADDRESS:"+ckAddrId);
			JSONObject json = this.getAddress(uid, ckAddrId);
			JSONObject ckAddress = json.getJSONObject("defAddress");
			mav.addObject("addresses", json.get("addresses"));
			mav.addObject("defAddress", ckAddress);
			mav.addObject("firstAddress", json.get("firstAddress"));//非默认地址中的第一个地址
			LOG.debug("DEF ADDRESS:"+JSON.toJSONString(ckAddress));
			if( ckAddress != null )
				regionid = ckAddress.getString("provicecode");
			this.orderWxService.changeRegion(ctoken, uid, regionid);
			
			// 发票抬头设置
			mav.addObject("invoice", request.getParameter("invoiceType"));//
			String invoiceTitle = request.getParameter("invoiceTitle");
			if( invoiceTitle == null || "".equals(invoiceTitle) ){
				ExecuteResult<UserCompanyDTO> erCompany = this.companyService.findUserCompanyByUId(Long.valueOf(uid));
				if( erCompany.getResult() != null )
					invoiceTitle = erCompany.getResult().getInvoiceInformation();
			}
			LOG.debug("INVOICE TITLE:"+invoiceTitle);
			mav.addObject("invoiceTitle", invoiceTitle);
			
			// 付款方式设置
			mav.addObject("shipmentType", request.getParameter("shipmentType"));
			mav.addObject("payPeriod", request.getParameter("payPeriod"));
			
			// 企业支付或个人支付（1个人 2企业）
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			String paymentMethod_ = request.getParameter("paymentMethod");
			Integer paymentMethod = null;
			if(StringUtils.isNotBlank(paymentMethod_)){
				paymentMethod = Integer.parseInt(paymentMethod_);
			} else{
				if(user.getUsertype() == UserType.ORDINARY.getCode()){
					paymentMethod = 1;
				} else if(user.getUsertype() > UserType.ORDINARY.getCode()){
					paymentMethod = 2;
				}
			}
			mav.addObject("userstatus", user.getUserstatus());
			Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
			ShopCartDTO myCart=null;
			// 优惠券编号
	        String couponId = request.getParameter("couponId");
	        //查询正在使用的优惠劵
	        if(StringUtils.isNotBlank(couponId)){
	        	ExecuteResult<CouponsDTO> couponResult = couponsExportService.queryById(couponId);
	        	mav.addObject("couponResult", couponResult);
	        	mav.addObject("couponId", couponId);
	        }
		    myCart = this.orderWxService.getMyCart(ctoken, uid, false, freightDeliveryType,null,couponId, paymentMethod, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
			mav.addObject("myCart", myCart);
			mav.addObject("freightDeliveryType", freightDeliveryType);
			mav.addObject("paymentMethod", paymentMethod);
			// 可使用的优惠券
			List<CouponsDTO> avaliableCoupons = new ArrayList<CouponsDTO>();
			// 不可使用的优惠券
			List<CouponsDTO> unavaliableCoupons = new ArrayList<CouponsDTO>();
			// 查询用户领取的优惠券
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			couponUserDTO.setUserId(Long.valueOf(uid));
			couponUserDTO.setDeleted(0);// 未删除
			couponUserDTO.setUserCouponType(0);// 未使用
			ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, null);
			if (couponUserResult.isSuccess() 
					&& couponUserResult.getResult() != null 
					&& couponUserResult.getResult().getRows() != null
					&& couponUserResult.getResult().getRows().size() > 0) {
				// 用户领取的优惠券
				List<CouponUserDTO> couponUserDTOs = couponUserResult.getResult().getRows();
				for(CouponUserDTO dto : couponUserDTOs){
					ExecuteResult<ShopCartDTO> validateResult = shopCartCouponService.validateCoupon(Long.parseLong(uid), dto.getCouponId(), paymentMethod, myCart, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
					ExecuteResult<CouponsDTO> couponResult = couponsExportService.queryById(dto.getCouponId());
					 if(couponResult.getResult().getShopId() == null || couponResult.getResult().getShopId() == 0){
						 couponResult.getResult().setPromulgator("平台");
					 }else{
						 ExecuteResult<ShopDTO> shopResult = shopService.findShopInfoById(couponResult.getResult().getShopId());
						 couponResult.getResult().setPromulgator(shopResult.getResult().getShopName());
					 }
					 if(couponResult.getResult().getCouponType() == 1){
						 couponResult.getResult().setDescr("满" + moneyToSting(couponResult.getResult().getMeetPrice().toString())+ "元，减" + moneyToSting(couponResult.getResult().getCouponAmount().toString())+ "元");
		   	    		 }else if(couponResult.getResult().getCouponType() == 2){
		   	    			couponResult.getResult().setDescr(moneyToSting(couponResult.getResult().getCouponAmount().toString())+"折，限额" + moneyToSting(couponResult.getResult().getCouponMax().toString())+ "元");
		   	    		 }else if(couponResult.getResult().getCouponType() == 3){
		   	    			couponResult.getResult().setDescr("直降" + moneyToSting(couponResult.getResult().getCouponAmount().toString())+ "元");
		   	    		 }
					if(validateResult.isSuccess()){
						if(couponResult.isSuccess() && couponResult.getResult() != null){
							avaliableCoupons.add(couponResult.getResult());
						}
					} else{
						if(couponResult.isSuccess() && couponResult.getResult() != null){
							unavaliableCoupons.add(couponResult.getResult());
						}
					}
				}
			}
			mav.addObject("avaliableCoupons", avaliableCoupons);// 可使用的优惠券
			mav.addObject("unavaliableCoupons", unavaliableCoupons);// 不可使用的优惠券
	        //mav.setViewName("/shopcart/order_detail");
			return mav;
		}
		/**
		 * @desc： 获得用户收货地址及默认选中地址
		 * @param uid
		 * @param ckAddrId
		 * @return
		 */
		private JSONObject getAddress(String uid,String ckAddrId ){
			DataGrid<AddressInfoDTO> dgAddress = this.addressInfoService.queryAddressinfo(Long.valueOf(uid));
			JSONArray addresses = new JSONArray();
			JSONObject defAddress = null;
			JSONObject ckAddress = null;
	
			for( int i=0; i<dgAddress.getRows().size(); i++ ){
				AddressInfoDTO addr = dgAddress.getRows().get(i);
				JSONObject address = JSON.parseObject(JSON.toJSONString(addr));
				ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService.queryNameById(Integer.valueOf(addr.getProvicecode()));
				address.put("provicename", erProvice.getResult().getName());
				ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService.queryNameById(Integer.valueOf(addr.getCitycode()));
				address.put("cityname", erCity.getResult().getName());
				ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService.queryNameById(Integer.valueOf(addr.getCountrycode()));
				address.put("countryname", erCountry.getResult().getName());
				if( ckAddrId != null && !"".equals(ckAddrId) && addr.getId().compareTo(Long.valueOf(ckAddrId)) == 0  )
					ckAddress = address;
				if( i == 0 )
					defAddress = address;
				if( addr.getIsdefault() == 1 )
					defAddress = address;
				addresses.add(address);
			}
			if( ckAddress == null )
				ckAddress = defAddress;
	
			JSONObject json = new JSONObject();
			json.put("defAddress", ckAddress);
			json.put("addresses", addresses);
			json.put("firstAddress", addresses.isEmpty() ? null : addresses.get(0));
			return json;
		}
		/**
		 * 获取地址列表
		 */
		@RequestMapping(value="/queryAddress")
		public String queryAddress(HttpServletRequest request,OrderDataNeedInfo orderDataNeedInfo,
						@CookieValue(value=Constants.USER_ID,required=true) String uid,
						@RequestParam(value = "contractFlag")String contractFlag,
						Model model,String type){
			String ckAddrId = orderDataNeedInfo.getAddress();
			JSONObject json = this.getAddress(uid, ckAddrId);
			model.addAttribute("selectedId", ckAddrId);
			model.addAttribute("addresses", json.get("addresses"));
			model.addAttribute("queryAddress", ckAddrId);
			model.addAttribute("orderType", type);
			model.addAttribute("contractNo", request.getParameter("contractNo"));
			model.addAttribute("couponId", request.getParameter("couponId"));
	
			
			/*************************************************************/
			model.addAttribute("invoiceType", orderDataNeedInfo.getInvoiceType());
			model.addAttribute("invoiceTitle", orderDataNeedInfo.getInvoiceTitle());
			/*************************************************************/
			return "/order/addresses";
		}
		/**
		 * 获取发票信息
		 */
		@RequestMapping(value="/queryInvoice")
		public String queryInvoice(HttpServletRequest request,OrderDataNeedInfo orderDataNeedInfo,
				@RequestParam(value="contractFlag") String contractFlag,
				@RequestParam(value="contractNo") String contractNo,
				Model model,String type){
			model.addAttribute("invoiceType", orderDataNeedInfo.getInvoiceType());
			model.addAttribute("invoiceTitle", orderDataNeedInfo.getInvoiceTitle());
			model.addAttribute("address", orderDataNeedInfo.getAddress());
			model.addAttribute("contractFlag", contractFlag);
			//李伟龙新增，在发票确实时候协议id未传，所以增加id到页面
			model.addAttribute("contractNo", contractNo);
			model.addAttribute("couponId", request.getParameter("couponId"));
			model.addAttribute("orderType", type);
			return "/order/invoice";
		}
		/**
		 * 
		 * @Title: submitInvoice
		 * @Description: 确认发票
		 * @param @param invoiceType
		 * @param @param invoiceTitle
		 * @param @param ckAddrId
		 * @param @return 设定文件
		 * @return String 返回类型
		 * @throws
		 */
		@RequestMapping(value="/submitInvoice")
		public String submitInvoice(OrderDataNeedInfo orderDataNeedInfo){
			LOG.info("invoiceType="+orderDataNeedInfo.getInvoiceType());
			LOG.info("invoiceTitle="+orderDataNeedInfo.getInvoiceTitle());
			LOG.info("address="+orderDataNeedInfo.getAddress());
			return "redirect:/orderWx/toOrder?address="+orderDataNeedInfo.getAddress()+"&invoice="+orderDataNeedInfo.getInvoiceType()+"&invoiceTitle="+orderDataNeedInfo.getInvoiceTitle();
		}
		
		@RequestMapping(value="/toEditAddress")
		public String toEditAddress(HttpServletRequest request,OrderDataNeedInfo orderDataNeedInfo,
				@RequestParam(value="contractFlag") String contractFlag,
				@RequestParam(value="contractNo") String contractNo,
				Model model){
			model.addAttribute("invoiceType", orderDataNeedInfo.getInvoiceType());
			model.addAttribute("invoiceTitle", orderDataNeedInfo.getInvoiceTitle());
			model.addAttribute("address", orderDataNeedInfo.getAddress());
			model.addAttribute("contractFlag", contractFlag);
			model.addAttribute("contractNo", contractNo);
			request.getParameter("contractNo");
			
			return "/order/editAddress";
		}
		
		/**
		 * 
		 * @Title: getAddress
		 * @Description: 获取地址信息
		 * @param @param id
		 * @param @return 设定文件
		 * @return AddressInfoDTO 返回类型
		 * @throws
		 */
		@RequestMapping(value="/getAddress")
		@ResponseBody
		public String getAddress(Long id){
			ExecuteResult<AddressInfoDTO> result = addressInfoService.queryAddressinfoById(id);
			return JSON.toJSONString(result.getResult());
		}
		/**
		 * 
		 * @Title: queryAddressBase
		 * @Description: 查询省市区
		 * @param @param id
		 * @param @return 设定文件
		 * @return List<AddressBase> 返回类型
		 * @throws
		 */
		@RequestMapping("/query")
		@ResponseBody
		public String queryAddressBase(String id) {
			//用户收获地址
			List<AddressBase> listshen=addressBaseService.queryAddressBase(id);
			LOG.info(JSON.toJSONString(listshen));
			return JSON.toJSONString(listshen);
		}
		/**
		 * 
		 * @Title: submitAddress
		 * @Description: 新增或是修改地址
		 * @param @return 设定文件
		 * @return String 返回类型
		 * @throws
		 */
		@RequestMapping(value="/submitAddress")
		@ResponseBody
		public String submitAddress(AddressInfoDTO addressInfoDTO,OrderDataNeedInfo orderDataNeedInfo,
				@CookieValue(value=Constants.USER_ID) String uid){
			
			ExecuteResult<String> result = new ExecuteResult<String>();
			List<String> errorMess = new ArrayList<String>();
			
			if(StringUtils.isEmpty(uid) || !StringUtils.isNumeric(uid)){
				errorMess.add("uid不能为空，并且必须是数字");
	    	}
			String provicecode = addressInfoDTO.getProvicecode();//省编号
			if(StringUtils.isEmpty(provicecode) || !StringUtils.isNumeric(provicecode)){
				errorMess.add("provicecode不能为空，并且必须是数字");
	    	}
			String citycode = addressInfoDTO.getCitycode();//市编号
			if(StringUtils.isEmpty(citycode) || !StringUtils.isNumeric(citycode)){
				errorMess.add("citycode不能为空，并且必须是数字");
	    	}
			String countrycode = addressInfoDTO.getCountrycode();//县、县级市、区编号
			if(StringUtils.isEmpty(countrycode) || !StringUtils.isNumeric(countrycode)){
				errorMess.add("countrycode不能为空，并且必须是数字");
	    	}
			String fulladdress = addressInfoDTO.getFulladdress();//详细地址
			if(StringUtils.isEmpty(fulladdress)){
				errorMess.add("fulladdress不能为空！");
	    	}
			String isdefault = String.valueOf(addressInfoDTO.getIsdefault());//1:默认2:不是
			if(!"1".equals(isdefault) && !"2".equals(isdefault)){
				errorMess.add("isdefault值不合法");
	    	}
			String contactperson = addressInfoDTO.getContactperson();//联系人、收货人
			if(StringUtils.isEmpty(contactperson)){
				errorMess.add("contactperson不能为空！");
	    	}
			String contactphone = addressInfoDTO.getContactphone();//联系人手机
			if(StringUtils.isEmpty(contactphone) || !StringUtils.isNumeric(contactphone)){
				errorMess.add("contactphone不能为空，并且必须是数字");
	    	}
	//		String contacttel = request.getParameter("contacttel");//联系人座机
			
			if(errorMess.size()>0){
				result.setErrorMessages(errorMess);
				return JSON.toJSONString(result);
			}
			
			addressInfoDTO.setBuyerid(Long.valueOf(uid));
			
			LOG.info("uid================="+orderDataNeedInfo.getAddress());
			if( StringUtils.isNotEmpty(orderDataNeedInfo.getAddress())){
				LOG.info("修改");
				result = addressInfoService.modifyAddressInfo(addressInfoDTO);
			}else{
				LOG.info("新增");
				result = addressInfoService.addAddressInfo(addressInfoDTO);
			}
			LOG.info("ExecuteResult<String>==="+JSON.toJSONString(result));
			
			return JSON.toJSONString(result);
		}
		
		
		
	
		
		
		 /**
		 * 
		 * @author 删除地址
		 * @param model
		 * @param request
		 */
			@ResponseBody
			@RequestMapping("/deladdr")
			public String deladdr(
					HttpServletRequest request, Model model,Long uid){
				ExecuteResult<String> result = new ExecuteResult<String>();
				String returnStr = "删除成功!";
				result = addressInfoService.removeAddresBase(uid);
				if(!result.isSuccess()){
					returnStr = "删除失败!"+result.getErrorMessages().get(0);
				}
				return returnStr;
			}
	
		
		
		
		/**
		 * 提交订单时校验订单数据
		 * @return
		 */
		@RequestMapping("/validate")
		public String validate(HttpServletRequest request,@CookieValue(value=Constants.USER_ID) String uid,@CookieValue(value=Constants.CART_TOKEN,required=false) String ctoken,
				@RequestParam(value="orderType",required=false) String orderType,
				Model model){
			Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
			ShopCartDTO myCart =this.orderWxService.getMyCart(ctoken, uid, false, freightDeliveryType,null, null, null, null);
			
			model.addAttribute("myCart", myCart);
			return "/order/order_validate";
		}
		/**
		 * 订单提交
		 * @return
		 */
		@RequestMapping(value = "/orderSubmit")
		public String orderSubmit(@CookieValue(value=Constants.USER_ID,defaultValue="1000752")String uid,
						@CookieValue(value=Constants.CART_TOKEN,required=false,defaultValue="")String ctoken,TradeOrdersDTO dto,
						HttpServletRequest request,HttpServletResponse response,
						String contractNo,String needApprove,Model model){
			
			Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
			ShopCartDTO myCart =this.orderWxService.getMyCart(ctoken, uid, false, freightDeliveryType,null, null, null, null);
			
			if(null!=myCart && myCart.getUnusualCount().compareTo(0)>0){
				model.addAttribute("myCart", myCart);
				return "/order/order_validate1";
			}
			
			String paymentMethod = request.getParameter("paymentMethod");
			String couponId = request.getParameter("couponId");
			dto.setPaymentType(Integer.valueOf(paymentMethod));
			dto.setBuyerId(Long.valueOf(uid));
			Map<String, Object> map = this.orderWxService.subimtOrder(ctoken, uid, dto,contractNo,needApprove,freightDeliveryType,null,couponId);
			if(!("2").equals(dto.getOrderType().toString())){
				this.orderWxService.delRedisCart(uid);
			}
			ExecuteResult<String> result = (ExecuteResult<String>) map.get("er");
			TradeOrderCreateDTO tradeOrderCreateDTO = (TradeOrderCreateDTO) map.get("tradeOrderCreateDTO");
			LOG.debug("===========>"+JSON.toJSONString(result));
			//将结果放入request中，仅供站内信发送拦截器使用
			request.setAttribute("result", result);
	//		return result;
			
			model.addAttribute("executeResult", result);
			
			if( result.isSuccess() && dto.getShipmentType() == 1 ){
				//--------------------------------------------订单提交发送微信消息-----------------------------------------------------//
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.SUBMIT_ORDERS);
				message.setFirst("【印刷家】尊敬的用户，订单"+String.valueOf(dto.getOrderId())+"已提交成功，印刷家提醒您及时查看。");
	//			Long shopId = tradeOrderCreateDTO.getParentOrder().getShopId();//获取一个店铺id
				message.setOrderID(String.valueOf(dto.getOrderId()));
				message.setOrderMoneySum(dto.getPaymentPrice().toString());
				SendWeiXinMessageUtil.sendWeiXinMessage(uid, message, request, response);
	//			LOG.debug("------------"+JSON.toJSONString(tradeOrderCreateDTO.getSubOrders()));
	//			LOG.debug("------------"+JSON.toJSONString(message));
	//			message.setFirst("【印刷家】尊敬的用户，买家"+userExportService.queryUserById(Long.valueOf(uid)).getUname()+"的订单"+String.valueOf(tradeOrderCreateDTO.getParentOrder().getOrderId())+"已提交成功，印刷家提醒您及时查看。");
	//			for(TradeOrdersDTO tradeOrdersDTO : tradeOrderCreateDTO.getSubOrders()){//给每一个卖家发微信消息
	//				SendWeiXinMessageUtil.sendWeiXinMessage(uid, message, request, response);
	//			}
				
				//-------------------------------------------------------------------------------------------------//
				return "redirect:/person/pay?orderNo=" + result.getResult();//支付地址待确认
			}else{
	//			return "/shopcart/submit_result";
				return "redirect:/orderWx/toOrderSubmit";
			}
		}
		
		
		/**
		 * 合同订单跳转到全部
		 * @param uid
		 * @param orderStatus
		 * @param model
		 * @return
		 */
		@RequestMapping(value="/toOrderSubmit")
		public String toOrder(@CookieValue(value=Constants.USER_ID,defaultValue="1000752")String uid,
					@RequestParam(value="orderStatus",required=false,defaultValue="")String orderStatus,
					@RequestParam(value="pageNo",defaultValue="1") int pageNo,
					@RequestParam(value="pageSize",defaultValue="3") int pageSize,
					@RequestParam(value="orderSource",defaultValue="buyers") String orderSource,
					@RequestParam(value="fuckStatus",required=false) String fuckStatus,
					Model model){
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
			inDTO.setUserType(1);//买家标记
			inDTO.setBuyerId(Long.valueOf(uid));
			if(StringUtils.isNotEmpty(orderStatus) && !"0".equals(orderStatus)){
				inDTO.setState(Integer.valueOf(orderStatus));
			}
			if(StringUtils.isNotEmpty(fuckStatus)){
				inDTO.setStateList(Arrays.asList(2,3,4,5));
			}
			model.addAttribute("fuckStatus", fuckStatus);
			//如果是卖家需查询卖家的店铺订单
			if("saler".equals(orderSource)){
				ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(Long.valueOf(uid));
		        UserInfoDTO userInfoDTO = result.getResult();
		        if(null==userInfoDTO){
		            return null;
		        }
				UserDTO userDTO = result.getResult().getUserDTO();
				Long shopId = userDTO.getShopId();
				inDTO.setShopId(shopId);
				inDTO.setBuyerId(null);
				inDTO.setSellerId(Long.valueOf(uid));
				inDTO.setUserType(2);
			}
			
			Pager<TradeOrdersQueryInDTO> pager = new Pager<TradeOrdersQueryInDTO>();
			pager.setPage(pageNo);
			pager.setRows(pageSize);
			ExecuteResult<Long> queryOrderQty = this.tradeOrderExportService.queryOrderQty(inDTO);
			if(null!=queryOrderQty){
				model.addAttribute("totalItem", queryOrderQty.getResult());
			}
			model.addAttribute("orderSource", orderSource);
			//查询未删除的订单
			inDTO.setDeleted(Constants.IS_NOT_DELETE_STATUS);
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = this.tradeOrderExportService.queryOrders(inDTO, pager);
			JSONArray jsonArray = new JSONArray();
			if (executeResult != null && executeResult.isSuccess()) {
				DataGrid<TradeOrdersDTO> dataGrid = executeResult.getResult();
				if(null!=dataGrid){
					List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
					for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
						jsonObject.put("locked", tradeOrdersDTO.getLocked());
						jsonObject.put("paymentPrice", tradeOrdersDTO.getPaymentPrice());//实际支付金额
						jsonObject.put("createTime", sf.format(tradeOrdersDTO.getCreateTime()));
						jsonObject.put("logisticsNo", tradeOrdersDTO.getLogisticsNo());//物流编号
						jsonObject.put("logisticsCompany", tradeOrdersDTO.getLogisticsCompany());//物流公司
						jsonObject.put("paid", tradeOrdersDTO.getPaid());
						jsonObject.put("refund", tradeOrdersDTO.getRefund());
						jsonObject.put("sellerEvaluate", tradeOrdersDTO.getSellerEvaluate());
						jsonObject.put("shipmentType", tradeOrdersDTO.getShipmentType());
						
						//--------------------------------------------------------------------------------------//
						//查询退款单id
						TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
						TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
						tradeReturnDto.setOrderId(tradeOrdersDTO.getOrderId().toString());
						tradeReturnDto.setDeletedFlag("0");
						queryDto.setTradeReturnDto(tradeReturnDto);
						DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
						//--------------------------------------------------------------------------------------//
						jsonObject.put("refund", tradeOrdersDTO.getRefund());
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
	//						ordreStatus = "待审核";
							//李伟龙修改保持订单状态与pc端一致
							ordreStatus = "已关闭";
						}else if(8==tradeOrdersDTO.getState()){
							ordreStatus = "待确认";
						}
						jsonObject.put("ordreStatus", ordreStatus);
						jsonObject.put("ordreStatusInt", tradeOrdersDTO.getState());
						//获取店铺名称
						ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
						if(result_shopDto!=null && result_shopDto.getResult()!=null){
							ShopDTO shopDTO = result_shopDto.getResult();
							jsonObject.put("shopId", tradeOrdersDTO.getShopId());
							jsonObject.put("shopName", shopDTO.getShopName());
							//获得店铺站点
							String stationId = StationUtil.getStationIdByShopId(shopDTO.getShopId());
							jsonObject.put("stationId", stationId);
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
							jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
	
					    	ItemShopCartDTO dto = new ItemShopCartDTO();
							dto.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
							dto.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
							dto.setQty(tradeOrderItemsDTO.getNum());//数量
							dto.setShopId(tradeOrdersDTO.getShopId());//店铺ID
							dto.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
							jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
							jsonObject_item.put("state", tradeOrdersDTO.getState());
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
							//获取该商品对应的物流主键ID,根据该参数判断物流的新增和修改操作
							DeliveryDTO deliveryDTO = deliveryService.getDeliveryInfoByItemId(tradeOrderItemsDTO.getOrderItemId().toString());
							if(deliveryDTO!=null){
								jsonObject_item.put("deliveryId", deliveryDTO.getId());
							}else{
								jsonObject_item.put("deliveryId", "");
							}
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
								jsonObject_item.put("shopFreightTemplateId", itemDTO.getShopFreightTemplateId());
							}
							jsonArray_item.add(jsonObject_item);
						}
						jsonObject.put("items", jsonArray_item);
						jsonArray.add(jsonObject);
					}
				}
				model.addAttribute("orderInfo", jsonArray);
			}
			
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			if(null!=user){
				model.addAttribute("userType", user.getUsertype());
				model.addAttribute("userState", user.getUserstatus());
			}
			
			
			if(StringUtils.isNotEmpty(orderStatus)){
				if(StringUtils.isEmpty(orderStatus)){
					model.addAttribute("orderStatus", "0");
				}else{
					model.addAttribute("orderStatus", orderStatus);
				}
				LOG.info("--------------------------------------/order/order_submit_detail");
				return "/order/order_submit_detail";
			}else{
				if(StringUtils.isEmpty(orderStatus)){
					model.addAttribute("orderStatus", "0");
				}else{
					model.addAttribute("orderStatus", orderStatus);
				}
				
				if(StringUtils.isNotBlank(fuckStatus)){
					if(pageNo>1){
						return "/order/queryStatusDetail";
					}else{
						return "/order/queryStatus";
					}
				}
				
				LOG.info("--------------------------------------/order/order");
				return "/order/order";
			}
		}
		
		/**
		 * 跳转到协议
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value="/toContract")
		public String toContract(@CookieValue(value=Constants.USER_ID) String uid,
				@RequestParam(value="contractNo",required=false,defaultValue="") String contractNo,
				@RequestParam(value="supplierName",required=false,defaultValue="") String supplierName, Model model) throws Exception{
			ContractInfoDTO dto = new ContractInfoDTO();
			dto.setCreateBy(uid);
			dto.setActiveFlag("0");//有效
			dto.setCreateRole(1);//买家
			if(StringUtils.isNotEmpty(contractNo)){
				dto.setContractNo(contractNo.trim());
				model.addAttribute("contractNo", contractNo.trim());
			}
			if(StringUtils.isNotBlank(supplierName)){
				model.addAttribute("supplierName", supplierName.trim());
				UserDTO userDTO = new UserDTO();
				userDTO.setUname(supplierName.trim());
				Pager<UserDTO> pager = new Pager<UserDTO>(1, Integer.MAX_VALUE);
				DataGrid<UserDTO> userList = this.userService.findUserListByCondition(userDTO, UserType.BUYER, pager);
				if(null!=userList){
					List<UserDTO> users = userList.getRows();
					List<String> supplierIds = new ArrayList<String>();
					if(users!=null && users.size()>0){
						for(UserDTO user : users){
							supplierIds.add(String.valueOf(user.getUid()));
						}
					}
					dto.setSupplierIdList(supplierIds);
				}
			}
			Pager<ContractInfoDTO> page = new Pager<ContractInfoDTO>(1,Integer.MAX_VALUE);
			ExecuteResult<DataGrid<ContractInfoDTO>> er = this.protocolExportService.queryContractInfoList(dto, page);
			if(null!=er && er.isSuccess()){
				DataGrid<ContractInfoDTO> dg = er.getResult();
				if(dg!=null){
					List<ContractInfoDTO> rows = dg.getRows();
					List<String> supplierIds = new ArrayList<String>(); 
					List<ContractInfo> contractInfos = new ArrayList<ContractInfo>();
					for(ContractInfoDTO contractInfoDTO:rows){
						ContractInfo contractInfo = new ContractInfo();
						BeanUtils.copyProperties(contractInfo, contractInfoDTO);
						contractInfos.add(contractInfo);
						supplierIds.add(String.valueOf(contractInfoDTO.getSupplierId()));
					}
					if(supplierIds.size()>0){
						ExecuteResult<List<UserDTO>> users = this.userService.findUserListByUserIds(supplierIds);
						Map<Long, String> map = new HashMap<Long, String>();
						if(null!=users && users.isSuccess()){
							List<UserDTO> userDTOs = users.getResult();
							for(UserDTO userDTO : userDTOs){
								map.put(userDTO.getUid(), userDTO.getUname());
							}
						}
						for(int i=0; contractInfos!=null && i<contractInfos.size(); i++){
							if(map.get(contractInfos.get(i).getSupplierId())!=null){
								contractInfos.get(i).setSupplierName(map.get(contractInfos.get(i).getSupplierId()));
							}
						}
					}
					model.addAttribute("contracts", contractInfos);
				}
			}
			return "/order/order_contract";
		}
		/**
		 * 获得协议明细
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		@RequestMapping(value="getContractDetail")
		public String getContractDetail(@RequestParam(value="contractNo",required=true)String contractNo,
				@CookieValue(value=Constants.USER_ID) String uid,Model model){
			ContractMatDTO dto = new ContractMatDTO();
			dto.setCreateBy(uid);
			dto.setContractNo(contractNo);
			Pager<ContractMatDTO> page = new Pager<ContractMatDTO>(1, Integer.MAX_VALUE);
			ExecuteResult<DataGrid<Map>> er = this.protocolExportService.queryContractMatList(dto, page);
			if(er!=null && er.isSuccess()){
				DataGrid<Map> dg = er.getResult();
				if(null!=dg){
					List<Map> contractMats = dg.getRows();
					model.addAttribute("contractMats", contractMats);
				}
			}
			return "/order/contract_detail";
		}
		
		/**
		 * 由协议或是询价提交的订单
		 * @return
		 */
		@RequestMapping(value="/getShopCart")
		public String getShopCart(@CookieValue(value=Constants.USER_ID) String uid,HttpServletRequest request,
				@CookieValue(value=Constants.REGION_CODE,required = false,defaultValue="11") String regionid,
				String jsonProducts,String orderType,String contractNo,Model model){
			
			List<ProductInPriceDTO> products = new ArrayList<ProductInPriceDTO>();
			if(StringUtils.isNotEmpty(orderType)){
				products = JSON.parseArray(this.orderWxService.getRedisCar(uid), ProductInPriceDTO.class);
				if(null!=products && products.size()>0){
					for(int i=0;i<products.size();i++){
						products.get(i).setTotal(products.get(i).getSkuPrice().multiply(BigDecimal.valueOf(products.get(i).getQuantity().longValue())));
					}
				}
	//			this.orderWxService.setRedisCar(uid,jsonProducts);
			}else{
				String redisStr = this.orderWxService.getRedisCar(uid);//获取redis中存的协议或是询价中的商品
				if(StringUtils.isNotEmpty(redisStr)){
					products = JSON.parseArray(redisStr, ProductInPriceDTO.class);
				}
			}
			
			// 收货地址变更
			String ckAddrId = request.getParameter("address");
			LOG.debug("CHECK ADDRESS:"+ckAddrId);
			JSONObject json = this.getAddress(uid, ckAddrId);
			JSONObject ckAddress = json.getJSONObject("defAddress");
			model.addAttribute("addresses", json.get("addresses"));
			model.addAttribute("defAddress", ckAddress);
			model.addAttribute("firstAddress", json.get("firstAddress"));//非默认地址中的第一个地址
			LOG.debug("DEF ADDRESS:"+JSON.toJSONString(ckAddress));
	
			// 发票抬头设置
			model.addAttribute("invoice", request.getParameter("invoiceType"));//
			String invoiceTitle = request.getParameter("invoiceTitle");
			if( invoiceTitle == null || "".equals(invoiceTitle) ){
				ExecuteResult<UserCompanyDTO> erCompany = this.companyService.findUserCompanyByUId(Long.valueOf(uid));
				if( erCompany.getResult() != null )
					invoiceTitle = erCompany.getResult().getInvoiceInformation();
			}
			LOG.debug("INVOICE TITLE:"+invoiceTitle);
			model.addAttribute("invoiceTitle", invoiceTitle);
			
			// 付款方式设置
			model.addAttribute("shipmentType", request.getParameter("shipmentType"));
			model.addAttribute("payPeriod", request.getParameter("payPeriod"));
			
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			model.addAttribute("userstatus", user.getUserstatus());
			
	//		List<Product> products = JSON.parseArray(jsonProducts, Product.class);
			List<ProductInPriceDTO> productList = new LinkedList<ProductInPriceDTO>();
			for(ProductInPriceDTO product : products){
				this.setSkuInfo(product);//设置商品的sku属性
				//TODO  此处  金额  不清
				product.setPayPrice(product.getSkuPrice());
				BigDecimal total = product.getPayPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
				product.setPayTotal( total );
				productList.add(product);
			}
			
			List<ShopOutPriceDTO> shops = this.convertToShop(products, getFreightDeliveryType(request));
			ShopCartDTO cart = new ShopCartDTO();
			cart.setShops(shops);
			model.addAttribute("myCart", cart);
			
			List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
			Long parentId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO=userService.queryUserById(parentId);
			if(userDTO.getParentId()!=null){
				UserDTO dto=userExportService.queryUserById(userDTO.getParentId());
				 if(dto!=null){
					ChildUserListPost childUserListPost = new ChildUserListPost();
					childUserListPost.setShopId(dto.getShopId());
					childUserListPost.setUserId(dto.getUid());
					childUserListPost.setUsername(dto.getUname());
					childUserListPost.setNickName(dto.getNickname());
					childUserListPosts.add(childUserListPost);
				 }
		    }
			if(userDTO!=null){
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(userDTO.getShopId());
				childUserListPost.setUserId(userDTO.getUid());
				childUserListPost.setUsername(userDTO.getUname());
				childUserListPost.setNickName(userDTO.getNickname());
				childUserListPosts.add(childUserListPost);
			 }
			Integer moduleType=1;
			ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService.queryChildUserList(parentId, moduleType, null);
			DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();
			
			List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
			for(ChildUserDTO childUserDTO : childUserDTOs){
				String resourceName = "";
				List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO.getUserMallResourceList();
				for(UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs){
					resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
				}
				if(resourceName.length()>0){
					resourceName = resourceName.substring(0, resourceName.length()-1);
				}
				
				//查询店铺名称
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(childUserDTO.getShopId());
				childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
				childUserListPost.setUserId(childUserDTO.getUserId());
				childUserListPost.setUsername(childUserDTO.getUsername());
				childUserListPost.setResourceIds(resourceName);
				childUserListPost.setNickName(childUserDTO.getNickName());
				childUserListPosts.add(childUserListPost);
			}
			model.addAttribute("childUserListPosts", childUserListPosts);
	        
			//获取协议订单的账期
			if(!"0".equals(orderType)){
				ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
				contractPaymentTermDTO.setContractNo(contractNo);
				contractPaymentTermDTO.setActiveFlag("1");
				ExecuteResult<ContractPaymentTermDTO> queryByContractPaymentTerm = this.protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO);
				if(queryByContractPaymentTerm.isSuccess()){
					ContractPaymentTermDTO result = queryByContractPaymentTerm.getResult();
					if(0==result.getPaymentType()){//天
						model.addAttribute("payPeriods", result.getPaymentDays()+"天");
						model.addAttribute("payPeriod", result.getPaymentDays());
					}else if(1==result.getPaymentType()){
						model.addAttribute("payPeriods", result.getPaymentDays()+"月");
						model.addAttribute("payPeriod", result.getPaymentDays() * 30);
					}
				}
			}
			
			model.addAttribute("contractFlag", "1");
			model.addAttribute("orderType", orderType);
			model.addAttribute("contractNo", contractNo);
			model.addAttribute("userId", parentId);
			
			return "/order/order_cart";
		}
		
		
		
		/**
		 * 由协议下单，运费切换
		 * @return
		 */
		@RequestMapping(value="/carriageGetShopCart")
		public ModelAndView carriageGetShopCart(@CookieValue(value=Constants.USER_ID) String uid,HttpServletRequest request,
				@CookieValue(value=Constants.REGION_CODE,required = false,defaultValue="11") String regionid,
				String jsonProducts,String orderType,String contractNo,Model model){
			ModelAndView mav = new ModelAndView();
			List<ProductInPriceDTO> products = new ArrayList<ProductInPriceDTO>();
			if(StringUtils.isNotEmpty(orderType)){
				products = JSON.parseArray(this.orderWxService.getRedisCar(uid), ProductInPriceDTO.class);
				if(null!=products && products.size()>0){
					for(int i=0;i<products.size();i++){
						products.get(i).setTotal(products.get(i).getSkuPrice().multiply(BigDecimal.valueOf(products.get(i).getQuantity().longValue())));
					}
				}
	//			this.orderWxService.setRedisCar(uid,jsonProducts);
			}else{
				String redisStr = this.orderWxService.getRedisCar(uid);//获取redis中存的协议或是询价中的商品
				if(StringUtils.isNotEmpty(redisStr)){
					products = JSON.parseArray(redisStr, ProductInPriceDTO.class);
				}
			}
			
			// 收货地址变更
			String ckAddrId = request.getParameter("address");
			LOG.debug("CHECK ADDRESS:"+ckAddrId);
			JSONObject json = this.getAddress(uid, ckAddrId);
			JSONObject ckAddress = json.getJSONObject("defAddress");
			mav.addObject("addresses", json.get("addresses"));
			mav.addObject("defAddress", ckAddress);
			mav.addObject("firstAddress", json.get("firstAddress"));//非默认地址中的第一个地址
			LOG.debug("DEF ADDRESS:"+JSON.toJSONString(ckAddress));
	
			// 发票抬头设置
			model.addAttribute("invoice", request.getParameter("invoiceType"));//
			String invoiceTitle = request.getParameter("invoiceTitle");
			if( invoiceTitle == null || "".equals(invoiceTitle) ){
				ExecuteResult<UserCompanyDTO> erCompany = this.companyService.findUserCompanyByUId(Long.valueOf(uid));
				if( erCompany.getResult() != null )
					invoiceTitle = erCompany.getResult().getInvoiceInformation();
			}
			LOG.debug("INVOICE TITLE:"+invoiceTitle);
			mav.addObject("invoiceTitle", invoiceTitle);
			
			// 付款方式设置
			mav.addObject("shipmentType", request.getParameter("shipmentType"));
			mav.addObject("payPeriod", request.getParameter("payPeriod"));
			
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			mav.addObject("userstatus", user.getUserstatus());
			
	//		List<Product> products = JSON.parseArray(jsonProducts, Product.class);
			List<ProductInPriceDTO> productList = new LinkedList<ProductInPriceDTO>();
			for(ProductInPriceDTO product : products){
				this.setSkuInfo(product);//设置商品的sku属性
				//TODO  此处  金额  不清
				product.setPayPrice(product.getSkuPrice());
				BigDecimal total = product.getPayPrice().multiply(BigDecimal.valueOf(product.getQuantity()));
				product.setPayTotal( total );
				productList.add(product);
			}
			
			Map<Long, Integer>  freightDeliveryType 	=getFreightDeliveryType(request);
			List<ShopOutPriceDTO> shops = this.convertToShop(products, freightDeliveryType);
			ShopCartDTO cart = new ShopCartDTO();
			cart.setShops(shops);
			mav.addObject("myCart", cart);
			
			List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
			Long parentId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO=userService.queryUserById(parentId);
			if(userDTO.getParentId()!=null){
				UserDTO dto=userExportService.queryUserById(userDTO.getParentId());
				 if(dto!=null){
					ChildUserListPost childUserListPost = new ChildUserListPost();
					childUserListPost.setShopId(dto.getShopId());
					childUserListPost.setUserId(dto.getUid());
					childUserListPost.setUsername(dto.getUname());
					childUserListPost.setNickName(dto.getNickname());
					childUserListPosts.add(childUserListPost);
				 }
		    }
			if(userDTO!=null){
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(userDTO.getShopId());
				childUserListPost.setUserId(userDTO.getUid());
				childUserListPost.setUsername(userDTO.getUname());
				childUserListPost.setNickName(userDTO.getNickname());
				childUserListPosts.add(childUserListPost);
			 }
			Integer moduleType=1;
			ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService.queryChildUserList(parentId, moduleType, null);
			DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();
			
			List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
			for(ChildUserDTO childUserDTO : childUserDTOs){
				String resourceName = "";
				List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO.getUserMallResourceList();
				for(UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs){
					resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
				}
				if(resourceName.length()>0){
					resourceName = resourceName.substring(0, resourceName.length()-1);
				}
				
				//查询店铺名称
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(childUserDTO.getShopId());
				childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
				childUserListPost.setUserId(childUserDTO.getUserId());
				childUserListPost.setUsername(childUserDTO.getUsername());
				childUserListPost.setResourceIds(resourceName);
				childUserListPost.setNickName(childUserDTO.getNickName());
				childUserListPosts.add(childUserListPost);
			}
			model.addAttribute("childUserListPosts", childUserListPosts);
	        
			//获取协议订单的账期
			if(!"0".equals(orderType)){
				ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
				contractPaymentTermDTO.setContractNo(contractNo);
				contractPaymentTermDTO.setActiveFlag("1");
				ExecuteResult<ContractPaymentTermDTO> queryByContractPaymentTerm = this.protocolExportService.queryByContractPaymentTerm(contractPaymentTermDTO);
				if(queryByContractPaymentTerm.isSuccess()){
					ContractPaymentTermDTO result = queryByContractPaymentTerm.getResult();
					if(0==result.getPaymentType()){//天
						mav.addObject("payPeriods", result.getPaymentDays()+"天");
						mav.addObject("payPeriod", result.getPaymentDays());
					}else if(1==result.getPaymentType()){
						mav.addObject("payPeriods", result.getPaymentDays()+"月");
						mav.addObject("payPeriod", result.getPaymentDays() * 30);
					}
				}
			}
			
			mav.addObject("contractFlag", "1");
			mav.addObject("orderType", orderType);
			mav.addObject("contractNo", contractNo);
			mav.addObject("userId", parentId);
			mav.addObject("freightDeliveryType", freightDeliveryType);
			mav.setViewName("/order/order_cart_view");
			return mav;
		}
		/**
		 * 获取购物车所有店铺及商品
		 * @param products
		 * @return
		 */
		private List<ShopOutPriceDTO> convertToShop(List<ProductInPriceDTO> products, Map<Long,Integer> freightDeliveryType) {
			Map<Long,ShopOutPriceDTO> shopMap = new HashMap<Long, ShopOutPriceDTO>();
			// 是否存在普通商品（非集采商品）
			boolean isExistNormalProduct = false;
			for (ProductInPriceDTO p : products) {
				Long shopid = p.getShopId();
				ShopOutPriceDTO shop = shopMap.get(shopid);
	
				if (shop == null) {
					shop = new ShopOutPriceDTO();
					shop.setShopId(shopid);
					shop.setSellerId(p.getSellerId());
	
					// 查询店铺名称
					ExecuteResult<ShopDTO> er = this.shopService.findShopInfoById(shopid);
					ShopDTO sd = er.getResult();
					String name = sd != null ? sd.getShopName() : "";
					shop.setShopTitle(name);
	
					shop.setProducts(new LinkedList<ProductInPriceDTO>());
					
					// 店铺IM
					ShopCustomerServiceDTO shopCustomerServiceDTO = new ShopCustomerServiceDTO();
					shopCustomerServiceDTO.setShopId(shopid);
					List<ShopCustomerServiceDTO> shopCustomerServiceDTOList = this.shopCustomerServiceService.searchByCondition(shopCustomerServiceDTO);
					if(shopCustomerServiceDTOList!=null && shopCustomerServiceDTOList.size()>0)
						shop.setStationId(shopCustomerServiceDTOList.get(0).getStationId());
					
					shopMap.put(shopid,shop);
				}
				List<PromotionDTO> promotionList=new ArrayList<PromotionDTO>();
				if (p.getPromotions() != null && p.getPromotions().size() > 0) {
					for (PromotionDTO promotion : p.getPromotions()) {
						if (promotion.getType() == 1) {
							continue;
						}
						// promotionMap.get(shopid).add(promotion.getId());
						promotionList.add(promotion);
					}
				}
	            p.setPromotions(promotionList);
				shop.getProducts().add(p);
				if (p.getChecked() && p.getActivitesDetailsId() == null) {
					isExistNormalProduct = true;
				}
			}
			
			List<ShopOutPriceDTO> shops = new LinkedList<ShopOutPriceDTO>();
			Iterator<Long> keys = shopMap.keySet().iterator();
			while( keys.hasNext() ){
				Long key = keys.next();
				ShopOutPriceDTO shop = shopMap.get(key);
	
				
				
				// 店铺运费设置
				ExecuteResult<BigDecimal> freightResult = orderWxService.calcShopFreight(shop, freightDeliveryType);
				shop.setFare(freightResult.getResult());
				shops.add(shop);
			}
			
			List<ShopOutPriceDTO> sps = new LinkedList<ShopOutPriceDTO>();
			Iterator<Entry<Long, ShopOutPriceDTO>> iterator = shopMap.entrySet().iterator();
			while( iterator.hasNext() ){
				Entry<Long, ShopOutPriceDTO> entry = iterator.next();
				ShopOutPriceDTO shop = entry.getValue();
				sps.add(shop);
			}
			
			return sps;
		}
		
		/**
		 * 设置商品的sku属性
		 * @param product
		 */
		private void setSkuInfo(ProductInPriceDTO product) {
			ItemShopCartDTO dto = new ItemShopCartDTO();
			// 查询商品SKU信息
			dto.setAreaCode(product.getRegionId());
			dto.setSkuId(product.getSkuId());
			dto.setQty(product.getQuantity());
			dto.setShopId(product.getShopId());
			dto.setItemId(product.getItemId());
			if (product.getSellerId() != null && !"".equals(product.getSellerId()))
				dto.setSellerId(Long.valueOf(product.getSellerId()));
			if (product.getUid() != null && !"".equals(product.getUid()))
				dto.setBuyerId(Long.valueOf(product.getUid()));
			LOG.debug("获取SKU参数：" + JSON.toJSONString(dto));
			
			ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto);
			
			LOG.debug("通过SKUID查询商品SKU信息："+JSON.toJSONString(er));
			
			if( er.isSuccess() && er.getResult() != null ){
				ItemShopCartDTO iscd = er.getResult();
	//			product.setSkuPrice(iscd.getSkuPrice());
	//			product.setPayPrice(iscd.getSkuPrice());
				product.setTitle(iscd.getItemName());
				product.setSrc(iscd.getSkuPicUrl());
				product.setCid(iscd.getCid());
				product.setStatus(iscd.getItemStatus());
				
				BigDecimal total = BigDecimal.valueOf(product.getQuantity()).multiply(product.getSkuPrice());
				product.setTotal(total);
				product.setPayTotal(total);
				product.setQty(iscd.getQty());
				
				// 设置商品 销售属性
				List<ProductAttrDTO> attrs = new LinkedList<ProductAttrDTO>();
				for(ItemAttr ia : iscd.getAttrSales()){
					ProductAttrDTO attr = new ProductAttrDTO();
					attr.setName(ia.getName());
					
					StringBuffer val = new StringBuffer();
					for( ItemAttrValue iav:ia.getValues() )
						val.append(iav.getName()).append("&nbsp;");
					
					attr.setValue(val.toString());
					attrs.add(attr);
				}
				product.setAttrs(attrs);
			}
		}
		/**
		 * 查询待审核的协议订单
		 * @param uid
		 * @param model
		 * @return
		 */
		@RequestMapping(value="/queryNeedApprove")
		public String queryNeedApprove(@CookieValue(value=Constants.USER_ID)String uid,Model model){
			UserDTO userDTO = new UserDTO();
			userDTO.setParentId(Long.valueOf(uid));
			userDTO.setUsertype(2);// 用户类型 1 普通用户 2 买家  3卖家
			List<String> idList = new ArrayList<String>();
			idList.add(uid);
			List<Long> ids = new ArrayList<Long>();
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			if(null!=user){
				model.addAttribute("userState", user.getUsertype());
			}
			if(null!=user && null==user.getParentId()){
				ids.add(Long.valueOf(uid));//如果是父id则有子id  父id可以自己审核自己的订单   将父id本身加入ids
				DataGrid<UserDTO> userDTOs = this.userService.queryUserListByCondition(userDTO, UserType.BUYER, idList, null);
				if(null!=userDTOs){
					List<UserDTO> rows = userDTOs.getRows();
					for(UserDTO dto : rows){
						ids.add(dto.getUid());
					}
				}
			}
			
			if(ids.size()<=0){
				return "/order/order_submit_detail";
			}
			
			
			Map<String, ContractOrderDTO> map = new HashMap<String, ContractOrderDTO>();
			ContractOrderDTO dto = new ContractOrderDTO();
	//		dto.setCreateBy(uid);
			dto.setIds(ids);
			dto.setState("0");//0待审核 1待确定 2待发货
			dto.setActiveFlag("0");
			Pager<ContractOrderDTO> page = new Pager<ContractOrderDTO>();
			page.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<ContractOrderDTO>> er = this.protocolExportService.queryContractOrderList(dto, page);
			if(er.isSuccess()){
				DataGrid<ContractOrderDTO> dg = er.getResult();
				List<ContractOrderDTO> contractOrderDTOs = dg.getRows();
				if(null!=contractOrderDTOs && contractOrderDTOs.size()>0){
					for(ContractOrderDTO contractOrderDTO : contractOrderDTOs){
						map.put(contractOrderDTO.getOrderNo(), contractOrderDTO);
					}
				}
			}
			
			TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
			inDTO.setBuyerIdList(ids);
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = this.tradeOrderExportService.queryOrders(inDTO, null);
			
			JSONArray jsonArray = new JSONArray();
			if(null!=executeResult && executeResult.isSuccess()){
				DataGrid<TradeOrdersDTO> dataGrid = executeResult.getResult();
				if(null!=dataGrid){
					List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
					for(TradeOrdersDTO tradeOrdersDTO : tradeOrdersDTOs){
						if(null!=map.get(String.valueOf(tradeOrdersDTO.getParentOrderId()))){
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
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
							//获取店铺名称
							ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
							if(result_shopDto!=null && result_shopDto.getResult()!=null){
								ShopDTO shopDTO = result_shopDto.getResult();
								jsonObject.put("shopId", tradeOrdersDTO.getShopId());
								jsonObject.put("shopName", shopDTO.getShopName());
								//获得店铺站点
								String stationId = StationUtil.getStationIdByShopId(shopDTO.getShopId());
								jsonObject.put("stationId", stationId);
							}
							List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
	
							JSONArray jsonArray_item = new JSONArray();
							for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
								JSONObject jsonObject_item = new JSONObject();
	
								jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
								jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
	
						    	ItemShopCartDTO itemShopCart = new ItemShopCartDTO();
						    	itemShopCart.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
						    	itemShopCart.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
						    	itemShopCart.setQty(tradeOrderItemsDTO.getNum());//数量
						    	itemShopCart.setShopId(tradeOrdersDTO.getShopId());//店铺ID
						    	itemShopCart.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
								jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
								ExecuteResult<ItemShopCartDTO> result = itemService.getSkuShopCart(itemShopCart); //调商品接口查url
								ItemShopCartDTO itemShopCartDTO = result.getResult();
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
					}
				}
				model.addAttribute("orderInfo", jsonArray);
			}
			
	//		UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
	//		if(null!=user){
	//			model.addAttribute("userState", user.getUsertype());
	//		}
			return "/order/order_submit_detail";
		}
	
		/**
		 * 审核需要审核的协议订单
		 * @param orderId
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value="/approveContractOrder")
		public void approveContractOrder(@RequestParam(value="orderId") String orderId,Writer writer) throws Exception{
	//		ContractOrderDTO dto = new ContractOrderDTO();
	//		dto.setOrderNo(orderId);
	//		dto.setState("1");//0待审核 1待确定 2待发货
	//		ExecuteResult<String> executeResult = this.protocolExportService.modifyContractOrder(dto);
			ExecuteResult<String> executeResult = tradeOrderExportService.modifyOrderStatus(orderId, 8);//7是待审核 8 是待确定
			if(null!=executeResult && "修改成功".equals(executeResult.getResult())){
				writer.write("true");
			}else{
				writer.write("false");
			}
		}
		/**
		 * 查询待确认的协议订单
		 * @return
		 */
		@RequestMapping(value="/queryNeedConfirm")
		public String queryNeedConfirm(@CookieValue(value=Constants.USER_ID) String uid,Model model){
			List<String> contractNos = new ArrayList<String>();
			
			ContractInfoDTO dto = new ContractInfoDTO();
			dto.setSupplierId(Integer.valueOf(uid));//卖家id
			dto.setActiveFlag("0");//有效
			Pager<ContractInfoDTO> page = new Pager<ContractInfoDTO>(1,Integer.MAX_VALUE);
			ExecuteResult<DataGrid<ContractInfoDTO>> er = this.protocolExportService.queryContractInfoList(dto, page);//根据卖家id查询协议
			if(null!=er && er.isSuccess()){
				DataGrid<ContractInfoDTO> result = er.getResult();
				if(null!=result){
					List<ContractInfoDTO> rows = result.getRows();
					for(ContractInfoDTO contractInfoDTO : rows){
						contractNos.add(contractInfoDTO.getContractNo());
					}
				}
			}
			
			
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = new ExecuteResult<DataGrid<TradeOrdersDTO>>();
			DataGrid<TradeOrdersDTO> dg = executeResult.getResult();
			if(null==dg){
				dg = new DataGrid<TradeOrdersDTO>();
				List<TradeOrdersDTO> tradeOrderDTOs = dg.getRows();
				if(null==tradeOrderDTOs){
					tradeOrderDTOs = new ArrayList<TradeOrdersDTO>();
				}
			}
			
			
			ContractOrderDTO contractOrder = new ContractOrderDTO();
			contractOrder.setContractNos(contractNos);
			contractOrder.setState("1");//0待审核 1待确定 2待发货
			contractOrder.setActiveFlag("0");
			Pager<ContractOrderDTO> pager = new Pager<ContractOrderDTO>();
			page.setRows(Integer.MAX_VALUE);
			ExecuteResult<DataGrid<ContractOrderDTO>> erdg = this.protocolExportService.queryContractOrderList(contractOrder, pager);
			if(er.isSuccess()){
				DataGrid<ContractOrderDTO> dataGrid = erdg.getResult();
				List<ContractOrderDTO> contractOrderDTOs = dataGrid.getRows();
				if(null!=contractOrderDTOs && contractOrderDTOs.size()>0){
					for(ContractOrderDTO contractOrderDTO : contractOrderDTOs){
						TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
						inDTO.setOrderId(contractOrderDTO.getOrderNo());//根据协议订单表中的订单号查询订单
						ExecuteResult<DataGrid<TradeOrdersDTO>> executeResultOrder = this.tradeOrderExportService.queryOrders(inDTO, null);
						if(executeResultOrder!=null && executeResultOrder.isSuccess()){
							DataGrid<TradeOrdersDTO> result = executeResultOrder.getResult();
							if(null!=result){
								List<TradeOrdersDTO> rows = result.getRows();
								dg.getRows().addAll(rows);
							}
						}
					}
				}
			}
			
			JSONArray jsonArray = new JSONArray();
			if(null!=executeResult && executeResult.isSuccess()){
				DataGrid<TradeOrdersDTO> dataGrid = dg;
				if(null!=dataGrid){
					List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
					for(TradeOrdersDTO tradeOrdersDTO : tradeOrdersDTOs){
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
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
						//获取店铺名称
						ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
						if(result_shopDto!=null && result_shopDto.getResult()!=null){
							ShopDTO shopDTO = result_shopDto.getResult();
							jsonObject.put("shopId", tradeOrdersDTO.getShopId());
							jsonObject.put("shopName", shopDTO.getShopName());
							//获得店铺站点
							String stationId = StationUtil.getStationIdByShopId(shopDTO.getShopId());
							jsonObject.put("stationId", stationId);
						}
						List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
	
						JSONArray jsonArray_item = new JSONArray();
						for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
							JSONObject jsonObject_item = new JSONObject();
	
							jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
							jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
	
					    	ItemShopCartDTO itemShopCart = new ItemShopCartDTO();
					    	itemShopCart.setAreaCode(tradeOrderItemsDTO.getAreaId()+"");//省市区编码
					    	itemShopCart.setSkuId(tradeOrderItemsDTO.getSkuId());//SKU id
					    	itemShopCart.setQty(tradeOrderItemsDTO.getNum());//数量
					    	itemShopCart.setShopId(tradeOrdersDTO.getShopId());//店铺ID
					    	itemShopCart.setItemId(tradeOrderItemsDTO.getItemId());//商品ID
							jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
							ExecuteResult<ItemShopCartDTO> result = itemService.getSkuShopCart(itemShopCart); //调商品接口查url
							ItemShopCartDTO itemShopCartDTO = result.getResult();
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
				}
				model.addAttribute("orderInfo", jsonArray);
			}
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			if(null!=user){
				model.addAttribute("userState", user.getUsertype());
			}
			return "/order/order_submit_detail";
		}
		
		/**
		 * 确认订单
		 * @param orderId
		 * @param writer
		 * @throws Exception
		 */
		@RequestMapping(value="/confirmContractOrder")
		public void confirmContractOrder(@RequestParam(value="orderId") String orderId,Writer writer) throws Exception{
	//		ContractOrderDTO dto = new ContractOrderDTO();
	//		dto.setOrderNo(orderId);
	//		dto.setState("2");//0待审核 1待确定 2待发货
	//		ExecuteResult<String> executeResult = this.protocolExportService.modifyContractOrder(dto);
			ExecuteResult<String> executeResult = this.tradeOrderExportService.modifyOrderStatus(orderId, 1);//确认订单将订单状态改为1
			if(null!=executeResult && "修改成功".equals(executeResult.getResult())){
				writer.write("true");
			}else{
				writer.write("false");
			}
		}
		/**
		 * 退款
		 * @return
		 */
		@RequestMapping(value="/refund")
		public String refund(@RequestParam(value="orderId") String orderId,Model model){
			
			TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
			inDTO.setOrderId(orderId);
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = this.tradeOrderExportService.queryOrders(inDTO, null);
			if(null!=executeResult){
				DataGrid<TradeOrdersDTO> dg = executeResult.getResult();
				if(dg!=null){
					List<TradeOrdersDTO> rows = dg.getRows();
					if(null!=rows && rows.size()>0){
						BigDecimal paymentPrice = rows.get(0).getPaymentPrice();
						if(paymentPrice!=null && paymentPrice.compareTo(BigDecimal.valueOf(0)) == 1 ){
							model.addAttribute("paymentPrice", paymentPrice.toString());
						}else{
							model.addAttribute("paymentPrice", "0");
						}
					}
				}
			}
			model.addAttribute("orderId", orderId);
			return "/order/refund";
		}
		
		/**
	     * 文件上传
	     * @return
		 * @throws Exception 
	     */
	    @RequestMapping(value = "upload")
	    public void fileUpload(@RequestParam("file")MultipartFile file, ImageType imageType,Writer writer) throws Exception{
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("success",true);
	        map.put("msg","上传成功");
	        try {
	        	String suffix = getSuffix(file);
	        	String uploadDir = getUploadDir(imageType);
	            String returnUrl = ftpUtils.upload(uploadDir, file.getInputStream(), suffix);
	            map.put("url",returnUrl);
	        } catch (IOException e) {
	            map.put("success",false);
	            map.put("msg","文件上传出现问题");
	        }
	        LOG.info("========"+JSON.toJSONString(map));
	        writer.write(JSON.toJSONString(map));
	    }
	    
	    
	    /**
	     * 获取文件后缀名
	     * @param filer
	     * @return
	     */
		private String getSuffix(MultipartFile file) {
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring( fileName.lastIndexOf("."), fileName.length());
			return suffix;
		}
		
		/**
	     * 根据上传文件类型(资质图片或者商品图片)获取上传的父级目录
	     * @param imageType
	     * @return
	     */
		private String getUploadDir(ImageType imageType) {
			String uploadDir = "/album";
			if(null != imageType){
				uploadDir += "/" + imageType.name().toLowerCase();
			}
			return uploadDir;
		}
		
		/**
		 * 提交申请退款
		 * @param orderId
		 * @param returnResult
		 * @param remark
		 * @param request
		 * @param model
		 * @return
		 */
		@RequestMapping(value="/refundAgreementSubmit")
		@ResponseBody
		public String refundAgreementSubmit(@RequestParam(value="orderId") String orderId,
							@RequestParam(value="returnResult") String returnResult,
							@RequestParam(value="remark") String remark,
							@CookieValue(value=Constants.USER_ID) String uid,
							HttpServletRequest request,HttpServletResponse response, Model model){
			
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			String json = "";
			String refundFreight = request.getParameter("refundFreight");//退款运费金额
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
				dataGrid = executeResult.getResult();
				List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
				if(tradeOrdersDTOs!=null && tradeOrdersDTOs.size()>0){
					//根据订单id 查询只会查询出一条记录
					TradeOrdersDTO tradeOrdersDTO = tradeOrdersDTOs.get(0);
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
					tradeReturnDto.setBuyId(tradeOrdersDTO.getBuyerId()+"");
					UserDTO user = userService.queryUserById(tradeOrdersDTO.getBuyerId());
					if(user!=null){
						tradeReturnDto.setBuyerName(user.getUname());
					}
					tradeReturnDto.setBuyerAddress(tradeOrdersDTO.getFullAddress());
					tradeReturnDto.setBuyerPhone(tradeOrdersDTO.getMobile());
					tradeReturnDto.setSellerId(tradeOrdersDTO.getSellerId()+"");
					tradeReturnDto.setOrderPrice(tradeOrdersDTO.getPaymentPrice());
					tradeReturnDto.setReturnResult(returnResult);//  退货原因
					tradeReturnDto.setRemark(remark);//  问题描述
					if(StringUtils.isNotEmpty(refundFreight)){
						tradeReturnDto.setRefundFreight(new BigDecimal(refundFreight));
					}
					
					//tradeReturnDto.setRefundGoods(new BigDecimal(refundGoods));
					tradeReturnDto.setRefundGoods(tradeOrdersDTO.getPaymentPrice());
	
					//获取店铺名称
					ExecuteResult<ShopDTO> result_shopDto = shopExportService.findShopInfoById(tradeOrdersDTO.getShopId());
					if(result_shopDto!=null && result_shopDto.getResult()!=null){
						ShopDTO shopDTO = result_shopDto.getResult();
						tradeReturnDto.setReturnAddress(shopDTO.getProvinceName()+"  "+shopDTO.getDistrictName()+"  "+shopDTO.getStreetName());
						tradeReturnDto.setReturnPhone(shopDTO.getMobile()+"");
						tradeReturnDto.setReturnPostcode(shopDTO.getZcode());
					}
					tradeReturnDto.setPicDTOList(picDTOList);
					//LOG.info("订单信息tradeReturnDto========="+JSON.toJSONString(tradeReturnDto));
					insertDto.setTradeReturnDto(tradeReturnDto);
	
					//构造订单下退货商品的信息
					List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList = new ArrayList<TradeReturnGoodsDetailDTO>();
					//JSONArray jsonArray = JSON.parseArray(jsonRefundItem);
	
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
					for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
						TradeReturnGoodsDetailDTO goodGto = new TradeReturnGoodsDetailDTO();
						goodGto.setGoodsId(tradeOrderItemsDTO.getItemId());
						goodGto.setSkuId(tradeOrderItemsDTO.getSkuId());
						goodGto.setRerurnCount(tradeOrderItemsDTO.getNum());
						goodGto.setReturnAmount(tradeOrderItemsDTO.getPayPriceTotal());
						goodGto.setPayPrice(tradeOrderItemsDTO.getPayPrice());
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
							goodGto.setGoodsPicUrl(itemShopCartDTO.getSkuPicUrl());
						}
						//获取商品名称
						ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
						if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
							ItemDTO itemDTO = result_itemDTO.getResult();
							goodGto.setGoodsName(itemDTO.getItemName());
						}
						tradeReturnGoodsDetailList.add(goodGto);
					}
					//LOG.info("商品列表tradeReturnGoodsDetailList========="+JSON.toJSONString(tradeReturnGoodsDetailList));
					insertDto.setTradeReturnGoodsDetailList(tradeReturnGoodsDetailList);
				}
				result = tradeReturnExportService.createTradeReturn(insertDto, TradeReturnStatusEnum.AUTH);
				request.setAttribute("insertDto",insertDto);
				//LOG.info("选择退款商品，申请退款==="+JSON.toJSONString(result));
			}
			//----------------------------------------------申请退换货售后-------------------------------------------------------------------//
			if(result.isSuccess()){
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.APPLICATION_FOR_DRAWBACK);
				message.setFirst("【印刷家】尊敬的用户， 买家用户"+userExportService.queryUserById(Long.valueOf(uid)).getUname()+"已经提交订单"+orderId+"退款/退货/售后服务申请，印刷家提醒您及时处理");
				message.setOrderProductPrice(executeResult.getResult().getRows().get(0).getPaymentPrice().toString());
				message.setOrderProductName("");
				message.setOrderName(orderId);
				SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(executeResult.getResult().getRows().get(0).getSellerId()), message, request, response);
			}
			//-----------------------------------------------------------------------------------------------------------------//
			
			json = JSON.toJSONString(result);
			return json;
		}
		
		/**
		 * 退款申请成功后的 跳转
		 * @return
		 */
		@RequestMapping(value="/refundSubmitSucc")
		public String refundSubmitSucc(HttpServletRequest request,Model model){
			String orderId = request.getParameter("orderId");
			String returnId = request.getParameter("returnId");
			String skuId = request.getParameter("skuId");
			
			model.addAttribute("returnId", returnId);
			model.addAttribute("skuId", skuId);
			
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
			return "/order/refundAgreementSubmit";
		}
		
		/**
		 * 订单详情
		 * @param request
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/queryOrderInfoBuyer" )
		public String queryOrderInfoBuyer(HttpServletRequest request,@CookieValue(value = Constants.USER_ID) Long userId, Model model) {
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = new ExecuteResult<DataGrid<TradeOrdersDTO>>();
			String orderId = request.getParameter("orderId");
			String approve=request.getParameter("approve");
			model.addAttribute("orderSource", request.getParameter("orderSource"));
			if(StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)){
				return "订单号不能为空，并且必须是数字";
			}
	//		Long userId = null;
	//		String userToken = LoginToken.getLoginToken(request);
	//		if(StringUtils.isNotEmpty(userToken)){
	//			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
	//			if(registerDto!=null){
	//				userId = registerDto.getUid();
	//			}
	//		}
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
	//			if(userId.longValue() == tradeOrdersDTO.getBuyerId().longValue()){//买家id和当前登录用户id相等时
					model.addAttribute("tradeOrder", tradeOrdersDTO);
					//LOG.info("tradeOrder===="+JSON.toJSONString(tradeOrdersDTO));
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
						ExecuteResult<ItemDTO> result_itemDTO = itemService.getItemById(tradeOrderItemsDTO.getItemId());
						if(result_itemDTO!=null && result_itemDTO.getResult()!=null){
							ItemDTO itemDTO = result_itemDTO.getResult();
							jsonObject_item.put("itemName", itemDTO.getItemName());
						}
						
						//商品属性
						String skuName = "";
						for(ItemAttr itemAttr : itemShopCartDTO.getAttrSales()){
							skuName += itemAttr.getName();
							for(ItemAttrValue itemAttrValue : itemAttr.getValues()){
								skuName += ":" + itemAttrValue.getName()+";";
							}
						}
						jsonObject_item.put("skuName", skuName);
						jsonArray_item.add(jsonObject_item);
					}
					model.addAttribute("jsonArray", jsonArray_item);
					//LOG.info("jsonArray===="+JSON.toJSONString(jsonArray_item));
	//			}
			}else{
				model.addAttribute("tradeOrder", null);
				model.addAttribute("jsonArray", null);
			}
			return "/order/orderInfo";
		}
		
		/**
		 * 买家中心和卖家中心点击不同的菜单跳转
		 * @return
		 */
		@RequestMapping(value="/toOrderSubmitByStatue")
		public String toOrderSubmitByStatue(@RequestParam(value="orderStatus") String orderStatus,
				@RequestParam(value="orderSource",defaultValue="buyers") String orderSource,
				@RequestParam(value="fuckStatus",defaultValue="2") String fuckStatus,
				Model model){
			model.addAttribute("orderStatus", orderStatus);
			model.addAttribute("orderSource", orderSource);
			model.addAttribute("fuckStatus", fuckStatus);
			return "/order/order";
		}
		
		/**
		 * 初始化买家和卖家的评价
		 * @return
		 */
		@RequestMapping(value="/initTrading")
		public String initTrading(@RequestParam(value="orderId") String orderId,@RequestParam(value="userType")String userType, Model model){
			//订单信息
			ExecuteResult<TradeOrdersDTO> tradeOrdersResult = tradeOrderExportService.getOrderById(orderId);
			TradeOrdersDTO tradeOrdersDTO = tradeOrdersResult.getResult();
			model.addAttribute("tradeOrdersDTO", tradeOrdersDTO);
			
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
				ExecuteResult<ItemShopCartDTO> itemShopCartResult = itemService.getSkuShopCart(itemShopCartDTO); //调商品接口查url
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
				ExecuteResult<ItemDTO> itemResult = itemService.getItemById(tradeOrderItemsDTO.getItemId());
				itemDTOList.add(itemResult.getResult());
			}
			model.addAttribute("userType", userType);
			model.addAttribute("itemShopCartDTOList", itemShopCartDTOList);
			model.addAttribute("itemDTOList", itemDTOList);
			
			return "/order/buyer_evaluation";
		}
		
		/**
		 * 买家评论提交
		 * @return
		 */
		@RequestMapping(value="/submitTrading")
		public void submitTrading(ShopEvaluationDTO shopEvaluationDTO, ItemEvaluationDTO itemEvaluationDTO,
				Long[] itemIds,Long[] skuIds,Integer skuScope,String content,HttpServletRequest request,
				HttpServletResponse response, PrintWriter writer){
			Map<String,Object> map = new HashMap<String,Object>();
			/*对商家评价*/
			shopEvaluationDTO.setResource("2");								//1：默认评价 2:手动评价
			ExecuteResult<ShopEvaluationDTO> shopEvaluationResult = shopEvaluationService.addShopEvaluation(shopEvaluationDTO);
			if( !shopEvaluationResult.isSuccess() ){
				map.put("messages", "提交失败!");
			} else {
				/*对商品评价*/
				List<ItemEvaluationDTO> itemEvaluationDTOList = new ArrayList<ItemEvaluationDTO>();
				for(int i = 0; i < itemIds.length; i++){
					//组装数据
					ItemEvaluationDTO itemEvaluation = new ItemEvaluationDTO();
					itemEvaluation.setOrderId(itemEvaluationDTO.getOrderId());		//订单ID
					itemEvaluation.setUserId(itemEvaluationDTO.getUserId());		//评价人
					itemEvaluation.setByUserId(itemEvaluationDTO.getByUserId());	//被评价人
					itemEvaluation.setByShopId(itemEvaluationDTO.getByShopId());	//被评价店铺id
					itemEvaluation.setItemId(itemIds[i]);							//商品id
					itemEvaluation.setSkuId(skuIds[i]);								//商品sku
					itemEvaluation.setType("1");									//类型 1:来自买家的评论 2:来自卖家的评价 3:售后评价
					itemEvaluation.setResource("2");								//1：默认评价 2:手动评价
					itemEvaluation.setSkuScope(skuScope);						//sku评分
					itemEvaluation.setContent(content);				
					itemEvaluationDTOList.add(itemEvaluation);
				}
				//批量提交
				ExecuteResult<ItemEvaluationDTO> itemEvaluationResult = itemEvaluationService.addItemEvaluations(itemEvaluationDTOList);
				if( itemEvaluationResult.isSuccess() ){
					//更新：订单状态
					TradeOrdersDTO tradeOrdersDTO = new TradeOrdersDTO();
					tradeOrdersDTO.setOrderId(itemEvaluationDTO.getOrderId());
					tradeOrdersDTO.setEvaluate(2);
					tradeOrderExportService.modifyEvaluationStatus(tradeOrdersDTO);
					map.put("messages", "提交成功!");
					//------------------------------------------买家提交评论----------------------------------------------------------//
	//				SendWeiXinMessage message = new SendWeiXinMessage();
	//				message.setModeId(WeiXinMessageModeId.APPLICATION_PLATFORM_INTERVENTION);
	//				message.setFirst("【印刷家】尊敬的用户，您的订单"+itemEvaluationDTO.getOrderId()+"被评价，印刷家提醒您及时查看。");
	//				message.setKeyword1(String.valueOf(itemEvaluationDTO.getOrderId()));
	//				message.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	//				SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(itemEvaluationDTO.getByUserId()) , message, request, response);
					//----------------------------------------------------------------------------------------------------//
				} else {
					map.put("messages", "提交失败!");
				}
			}
			request.setAttribute("map", map);
			writer.write(JSON.toJSONString(map));
		}
		/**
		 * 卖家评论提交
		 * @return
		 */
		@RequestMapping(value="/submitSellerToBuyers")
		public void submitSellerToBuyers(ItemEvaluationDTO itemEvaluationDTO,HttpServletRequest request,PrintWriter writer){
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
			writer.write(JSON.toJSONString(map));
		}
		
		/**
		 * 取消订单
		 * @return
		 */
		@RequestMapping(value="/cancelOrder")
		public void cancelOrder(HttpServletRequest request,HttpServletResponse response, Model model,PrintWriter writer,@CookieValue(value=Constants.USER_ID) String uid){
			String orderId = request.getParameter("orderId");
			String orderStatus = request.getParameter("orderStatus");
			ExecuteResult<String> result = new ExecuteResult<String>();
			request.setAttribute("result",result);
			List<String> errs = new ArrayList<String>();
			if(StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)){
				errs.add("订单号不能为空，并且必须是数字");
				result.setErrorMessages(errs);
				writer.write(JSON.toJSONString(result));
				return;
			}
			if(StringUtils.isEmpty(orderStatus) && !StringUtils.isNumeric(orderStatus)){
				errs.add("订单状态不能为空，并且必须是数字");
				result.setErrorMessages(errs);
				writer.write(JSON.toJSONString(result));
				return;
			}
			ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
			if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
				Integer nstate = resultTradeOrdersDTO.getResult().getState();
				if(nstate==1){//待付款
					result = tradeOrderExportService.modifyOrderStatus(orderId, Integer.parseInt(orderStatus));
				}else{
					errs.add("订单不是待付款状态");
					result.setErrorMessages(errs);
					writer.write(JSON.toJSONString(result));
					return;
				}
			}
			//--------------------------取消订单发送微信消息--------------------------//
			if(result.isSuccess()){
				LOG.info("--------------------------订单已成功取消---------------------------------");
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.ORDER_CANCELLATION);
	//			message.setFirst("【印刷家】尊敬的用户，您有一笔订单"+orderId+"已取消，印刷家提醒您及时查看。");
				message.setFirst("【印刷家】尊敬的用户，您的订单已成功取消。印刷家提醒您及时查看。");
				message.setKeyword1(orderId);
				message.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				SendWeiXinMessageUtil.sendWeiXinMessage(uid, message, request, response);
			}
			//----------------------------------------------------//
			writer.write(JSON.toJSONString(result));
			return;
		}
		/**
		 * 跳转到申请仲裁页面
		 */
		@RequestMapping(value = "/gocomplainadd")
	    public String gocomplainAdd(HttpServletRequest request,Model model){
	        String url= "/order/complainadd";
	      //flag 标识是从买家还是卖家跳转过来，防止菜单错误
	    	String flag=request.getParameter("flag");
	    	if(flag!=null && "2".equals(flag)){
	    		url= "/order/complainselleradd";
	    	}
	        
	        String tradeReturnid=request.getParameter("tradeReturnid");
	        String status=request.getParameter("status");
	        model.addAttribute("right1","block");
	        model.addAttribute("right","none");
	        //买家投诉
	        model.addAttribute("sellerOrBuyer","1");
	        ComplainDTO comp=new ComplainDTO();
	        comp.setRefundId(new Long(tradeReturnid));
	        comp.setComplainType("1");
	        if(status!=null && status.length()>0){
	        	comp.setStatus(Integer.parseInt(status));
	        }else{
	        	comp.setStatus(0);
	        }
	        //获取尚未投诉尚未处理完的投诉单
	        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	        DataGrid<ComplainDTO> da=complainExportService.findInfoByCondition(comp,null);
	        //若为查看仲裁信息，则返回详细页面
	        //progressBar为进度条当前到第几步
	        if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
	        	ComplainDTO complainDTO = da.getRows().get(0);
	        	model.addAttribute("complainDTO",complainDTO);
	        	if(complainDTO.getStatus()==0){
	        		model.addAttribute("progressBar",2);	//审核中
	        	}else if(complainDTO.getStatus()==1){
	        		model.addAttribute("progressBar",3);	//仲裁完成
	        	}
	        }else{
	        	model.addAttribute("progressBar",1);	//申请仲裁
	        }
	
	        //是否退款true表示为退款投诉，false表示为售后投诉
	        model.addAttribute("type","1");
	        model.addAttribute("iftk",true);
	        model.addAttribute("typeName","退款相关");
	        Map<String,String> map=new HashMap<String, String>();
	        //退款单状态
	        Map<Integer,String> tkstaceMap=tkstaceMap();
	        //订单状态
	        Map<Integer,String> orderstaceMap=orderstaceMap();
	        ExecuteResult<TradeReturnInfoDto> executeResult= tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(tradeReturnid);
	        if(executeResult!=null){
	            TradeReturnInfoDto infoDto=executeResult.getResult();
	            if(infoDto!=null){
	                TradeReturnGoodsDTO tradeReturnDto=infoDto.getTradeReturnDto();
	                if(tradeReturnDto!=null){
	                    if(tradeReturnDto.getSellerId()!=null){
	                        //卖家id
	                        model.addAttribute("sellerid",String.valueOf(tradeReturnDto.getSellerId()));
	                    }
	                    if(tradeReturnDto.getBuyId()!=null){
	                        //买家id
	                        model.addAttribute("buyerid",String.valueOf(tradeReturnDto.getBuyId()));
	                    }
	                    //获取获取退货单号
	                    String reproNo=tradeReturnDto.getCodeNo();
	
	                    //将退货id反到前台
	                    model.addAttribute("thid",tradeReturnDto.getId());
	                    if(reproNo!=null&&!"".equals(reproNo)){
	                        RefundPayParam refundPayParam=new RefundPayParam();
	                        refundPayParam.setReproNo(reproNo);
	                        map.put("reproNo",reproNo);
	                            if(tradeReturnDto.getApplyDt()!=null){
	                                //退款申请时间
	                                map.put("refcreated",simpleDateFormat.format(tradeReturnDto.getApplyDt()));
	                            }
	                            //退款原因
	                            map.put("returnResult",tradeReturnDto.getReturnResult());
	                            //退货单单状态
	                            if(tradeReturnDto.getState()!=null){
	                                map.put("tkstaceName",tkstaceMap.get(tradeReturnDto.getState()));
	                            }
	                            //订单退款货品金额
	                            if(tradeReturnDto.getRefundGoods()!=null){
	                                map.put("ordertkJe",tradeReturnDto.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
	                            }else{
	                                map.put("ordertkJe","0.00");
	                            }
	                            //订单退货运费金额
	                            if(tradeReturnDto.getRefundFreight()!=null){
	                                map.put("ordertkYf",tradeReturnDto.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
	                            }else{
	                                map.put("ordertkYf","0.00");
	                            }
	                            //判断是否是售后申请
	                            if(tradeReturnDto.getIsCustomerService()!=null&&"1".equals(tradeReturnDto.getIsCustomerService())){
	                                model.addAttribute("iftk",false);
	                                model.addAttribute("type","2");
	                                model.addAttribute("typeName","售后相关");
	                            }
	                            /*********************************************/
	                            //获取订单信息
	                            String orderId=tradeReturnDto.getOrderId();
	                            //订单号
	                            model.addAttribute("orderId",orderId);
	                            if(orderId!=null&&!"".equals(orderId)){
	                                TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
	                                Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
	                                pager.setPage(1);
	                                pager.setRows(10);
	                                tradeOrdersQueryInDTO.setOrderId(new String(orderId));
	                                ExecuteResult<DataGrid<TradeOrdersDTO>> executeResultorder= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
	                                DataGrid<TradeOrdersDTO> dataGridorder=executeResultorder.getResult();
	                                if(dataGridorder!=null&&dataGridorder.getRows()!=null&&dataGridorder.getRows().size()>0){
	                                    TradeOrdersDTO tradeOrdersDTO=dataGridorder.getRows().get(0);
	                                    //订单实际支付金额
	                                    if(tradeOrdersDTO.getPaymentPrice()!=null){
	                                        map.put("orderje",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
	                                    }else{
	                                        map.put("orderje","0.00");
	                                    }
	                                    //订单创建时间
	                                    if(tradeOrdersDTO.getCreateTime()!=null){
	                                        map.put("orderCreated",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
	                                    }
	                                    //订单付款时间
	                                    if(tradeOrdersDTO.getPaymentTime()!=null){
	                                        map.put("orderpayTime",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
	                                    }
	                                    //订单状态
	                                    if(tradeOrdersDTO.getState()!=null){
	                                        map.put("orderStace",orderstaceMap.get(tradeOrdersDTO.getState()));
	                                    }
	                                    //获取退货明细信息
	                                }
	                            }
	                    }
	                }
	            }
	        }
	        model.addAttribute("map",map);
	        return url;
	    }
		
		/**
	    *
	    * <p>
	    * Description: [跳到投诉查看页面,买家投诉]
	    * </p>
	    * Created on 2015-4-17
	    *
	    * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
	    * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	    */
	   @RequestMapping(value = "gocomplaindetail")
	   public String gocomplainDetail(HttpServletRequest request,Model model){
	       String url= "/order/complaindetail";
	       String complainid=request.getParameter("complainid");
	       //String tradeReturnid="24";
	       model.addAttribute("sellerOrBuyer","seller");
	       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	       ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(complainid));
	       if(executeResult!=null&&executeResult.getResult()!=null){
	           ComplainDTO complainDTO=executeResult.getResult();
	           String orderId=complainDTO.getOrderId();
	           if(orderId!=null){
	               model.addAttribute("orderId",String.valueOf(orderId));
	           }
	           Integer type=complainDTO.getType();
	           if(type!=null&&type==2){
	               model.addAttribute("typeName","售后相关");
	           }else{
	               model.addAttribute("typeName","退款相关");
	           }
	           if(complainDTO.getComplainType()!=null&&"1".equals(complainDTO.getComplainType())){
	               //是卖家还是买家投诉的
	               model.addAttribute("tusufang","买家");
	           }else if(complainDTO.getComplainType()!=null&&"2".equals(complainDTO.getComplainType())){
	               model.addAttribute("tusufang","卖家");
	           }else{
	               model.addAttribute("tusufang","无法识别");
	           }
	           Integer status=complainDTO.getStatus();
	           if(status!=null&&status==1){
	               model.addAttribute("zcstace","已经仲裁");
	               model.addAttribute("zcjgms","已经仲裁完毕，如有疑问请重新提交仲裁，谢谢理解：-）");
	               model.addAttribute("stace",String.valueOf(status));
	           }else if(status!=null&&status==2){
	               model.addAttribute("zcstace","已撤消投诉");
	               model.addAttribute("zcjgms","仲裁已经撤销，如有疑问请重新提交仲裁，谢谢理解：-）");
	               model.addAttribute("stace",String.valueOf(status));
	           }else{
	               model.addAttribute("zcstace","等待客服仲裁");
	               model.addAttribute("zcjgms","正在等待客服处理，请耐心等待，谢谢理解：-）");
	               model.addAttribute("stace",String.valueOf(0));
	           }
	           model.addAttribute("remark",complainDTO.getComplainContent());
	           if(complainDTO.getCreated()!=null){
	               model.addAttribute("createdate",simpleDateFormat.format(complainDTO.getCreated()));
	           }
	           //投诉原因
	           model.addAttribute("complainresion",complainDTO.getComplainResion());
	           //处理结果
	           model.addAttribute("stacetext",complainDTO.getStatusText());
	           //处理意见
	           model.addAttribute("comment",complainDTO.getComment());
	       }
	       return url;
	   }
		
		/**
		 * 申请仲裁
		 * @return
		 */
		@RequestMapping(value="/complainadd")
		public void gocomplainadd(HttpServletRequest request,HttpServletResponse response, Model model,PrintWriter writer,@CookieValue(value=Constants.USER_ID) String uid){
	        Json json=new Json();
	        try{
	            //订单号
	            String orderId=request.getParameter("orderId");
	            //退货id
	            String thid=request.getParameter("thid");
	            //退款id
	            String tkid=request.getParameter("tkid");
	            //投诉说明
	            String remark=request.getParameter("remark");
	            //买家电话
	            String buyermobile=request.getParameter("buyermobile");
	            //买家邮箱
	            String buyeremail=request.getParameter("buyeremail");
	            //投诉原因
	            String complainResion=request.getParameter("complainResion");
	            //投诉类型，是售后还是退款投诉
	            String type=request.getParameter("type");
	            //卖家
	            String sellerid=request.getParameter("sellerid");
	            //买家
	            String buyerid=request.getParameter("buyerid");
	            String url=request.getParameter("url");
	            String sellerOrBuyer=request.getParameter("sellerOrBuyer");
	
	            ComplainDTO complainDTO=new ComplainDTO();
	            if(orderId!=null&&!"".equals(orderId)){
	                complainDTO.setOrderId(new String(orderId));
	            }
	            if(thid!=null&&!"".equals(thid)){
	                complainDTO.setReturnGoodsId(new Long(thid));
	                complainDTO.setRefundId(new Long(thid));
	            }
	            if(type!=null&&!"".equals(type)){
	                complainDTO.setType(new Integer(type));
	            }
	            if(sellerid!=null&&!"".equals(sellerid)){
	                complainDTO.setSellerId(new Long(sellerid));
	                ShopAudiinDTO shopAudiinDTO=new ShopAudiinDTO();
	                shopAudiinDTO.setSellerId(new Long(sellerid));
	                ExecuteResult<List<ShopDTO>> executeResult= shopExportService.queryShopInfoByids(shopAudiinDTO);
	                if(executeResult.getResult()!=null&&executeResult.getResult().size()>0){
	                    //设置店铺名称
	                    complainDTO.setShopName(executeResult.getResult().get(0).getShopName());
	                }
	            }
	            complainDTO.setComplainContent(remark);
	            complainDTO.setComplainEmail(buyeremail);
	            complainDTO.setComplainPhone(buyermobile);
	            complainDTO.setComplainResion(complainResion);
	            complainDTO.setComplainPicUrl(url);
	            //1买家投诉,2卖家投诉
	            complainDTO.setComplainType(sellerOrBuyer);
	            //待仲裁
	            complainDTO.setStatus(new Integer(0));
	            //Long uid = WebUtil.getInstance().getUserId(request);
	            if(buyerid!=null&&!"".equals(buyerid)){
	                complainDTO.setBuyerId(new Long(buyerid));
	            }
	            ExecuteResult<ComplainDTO> executeResult=complainExportService.addComplainInfo(complainDTO);
	            if(executeResult.isSuccess()){
	            	//更新仲裁分组 
	            	ExecuteResult<String> updateComplainGroupInfo = updateComplainGroup(new Long(thid), executeResult.getResult().getId(),sellerOrBuyer);
	            	
	                Map<String,String> returnMap=new HashMap<String, String>();
	                returnMap.put("complainContent",remark);
	                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	                returnMap.put("date",simpleDateFormat.format(new Date()));
	                json.setObj(returnMap);
	                returnMap.put("complainId", executeResult.getResult().getId().toString());
	                if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
	                    Iterator<String> iterator=executeResult.getErrorMessages().iterator();
	                    while(iterator.hasNext()){
	                    	LOG.error(iterator.next());
	                    }
	                }
	                json.setMsg("投诉提交成功");
	                json.setSuccess(true);
	                
	                //-----------------------------------申请仲裁发送微信消息-----------------------------------------------------------//
	//                UserDTO userDTO = userExportService.queryUserById(Long.valueOf(uid));
	//                SendWeiXinMessage message = new SendWeiXinMessage();
	//				message.setModeId(WeiXinMessageModeId.APPLICATION_PLATFORM_INTERVENTION);
	//				message.setFirst("【印刷家】尊敬的用户，您的订单"+orderId+"已被买家"+userDTO.getUname()+"申请仲裁，印刷家提醒您及时处理");
	//				message.setKeyword1(userDTO.getUname());
	//				message.setKeyword2(userDTO.getUname()+"已申请仲裁,等待处理");
	//				SendWeiXinMessageUtil.sendWeiXinMessage(uid, message, request, response);
	                //----------------------------------------------------------------------------------------------//
	                
	            }else{
	                if(executeResult.getErrorMessages()!=null&&executeResult.getErrorMessages().size()>0){
	                    Iterator<String> iterator=executeResult.getErrorMessages().iterator();
	                    while(iterator.hasNext()){
	                        LOG.error(iterator.next());
	                    }
	                }
	                json.setMsg("投诉提交失败");
	                json.setSuccess(false);
	            }
	        }catch(Exception e){
	            json.setMsg("投诉提交失败");
	            LOG.error(e.getMessage()+e.toString());
	            json.setSuccess(false);
	        }
	        request.setAttribute("json",json);
	        LOG.info(json.toString());
	        LOG.info(JSON.toJSONString(json));
	        writer.write(JSON.toJSONString(json));
	    }
		
		public ExecuteResult<String> updateComplainGroup(Long refundId,Long complainId,String complainType){
	    	//若仲裁添加成功，判断是提交投诉还是申请辩解
	    	ComplainDTO queryComplainDTO=new ComplainDTO();
	    	if("1".equals(complainType)){
	    		queryComplainDTO.setComplainType("2");
	    	}else if("2".equals(complainType)){
	    		queryComplainDTO.setComplainType("1");
	    	}
	    	queryComplainDTO.setStatus(0);
	    	queryComplainDTO.setRefundId(refundId);
	    	List<ComplainDTO> list = complainExportService.getComplainByCondition(queryComplainDTO);
	    	//updateComplainDTO更新仲裁分组，若提交投诉，则complainGroup=id，  若申请辩解 ，则complainGroup=投诉的记录的id
	    	ComplainDTO updateComplainDTO=new ComplainDTO();
	    	updateComplainDTO.setId(complainId);
	    	if(list!=null && list.size()==1){
	    		updateComplainDTO.setComplainGroup(list.get(0).getId());
	    	}else{
	    		updateComplainDTO.setComplainGroup(complainId);
	    	}
	    	ExecuteResult<String> modifyComplainInfo = complainExportService.modifyComplainInfo(updateComplainDTO);
	    	return modifyComplainInfo;
	    }
		
		
		private Map<Integer,String> tkstaceMap(){
	        //1 退款申请等待卖家确认中  2 卖家不同意协议,等待买家修改 3 退款申请达成,等待买家发货   4 买家已退货,等待卖家确认收货  5 退款关闭  6 退款成功
	        Map<Integer,String> staceMap=new HashMap<Integer, String>();
	        staceMap.put(1,"退款申请等待卖家确认中");
	        staceMap.put(2,"卖家不同意协议,等待买家修改");
	        staceMap.put(3,"退款申请达成,等待买家发货");
	        staceMap.put(4,"买家已退货,等待卖家确认收货");
	        staceMap.put(5,"退款关闭");
	        staceMap.put(6,"退款成功");
	        return staceMap;
	    }
	    private Map<Integer,String> orderstaceMap(){
	        //1:待付款，2：待配送，3：待确认送货，4：待评价，5：已完成
	        Map<Integer,String> staceMap=new HashMap<Integer, String>();
	        staceMap.put(1,"待付款");
	        staceMap.put(2,"待配送");
	        staceMap.put(3,"待确认送货");
	        staceMap.put(4,"待评价");
	        staceMap.put(5,"已完成");
	        return staceMap;
	    }
		/**
		 * 发货提交
		 * @return
		 */
	    @RequestMapping(value="/deliverGoods")
	    public void deliverGoods(HttpServletRequest request, Model model,PrintWriter writer){
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			List<String> errorMessages = new ArrayList<String>();
			String returnId = request.getParameter("returnId");
			if(StringUtils.isEmpty(returnId) && StringUtils.isNumeric(returnId)){
				errorMessages.add("退款id不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
			}
			String expressNo = request.getParameter("expressNo");//退货快递单号
			if(StringUtils.isEmpty(expressNo)){
				errorMessages.add("退款原因不能为空");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
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
			writer.write(JSON.toJSONString(result));
			return;
	    }
		/**
		 * 收到货款
		 * @return
		 */
	    @RequestMapping(value="/updateTradeReturn")
	    public void updateTradeReturn(HttpServletRequest request, Model model,PrintWriter writer){
	
			ExecuteResult<TradeReturnGoodsDTO>  result = new ExecuteResult<TradeReturnGoodsDTO>();
			request.setAttribute("result",result);
			List<String> errorMessages = new ArrayList<String>();
			String returnId = request.getParameter("returnId");
			if(StringUtils.isEmpty(returnId) || !StringUtils.isNumeric(returnId)){
				errorMessages.add("退款id不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
			}
			String type = request.getParameter("type");//1、同意退款  2、不同意退款
			if(StringUtils.isEmpty(type) || !StringUtils.isNumeric(type)){
				errorMessages.add("操作类型type不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
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
				if(StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)){
					errorMessages.add("订单Id不能为空，必须是数字");
					result.setErrorMessages(errorMessages);
					writer.write(JSON.toJSONString(result));
					return;
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
						dto.setState(TradeReturnStatusEnum.PASS.getCode());
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
						result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.PASS);
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
							if(resultTradeOrdersDTO.getResult().getPaymentMethod()==2||PayBankEnum.OFFLINE.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())
								||PayBankEnum.OTHER.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
								dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());//线下支付，企业支付，其他支付不走ecm确认退款流程
							}else{
								dto.setState(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode());
								dto.setConfirmStatus("0");//待审核确认
								status=TradeReturnStatusEnum.PLATFORMTOREFUND;
							}
							if(parentId!=null && parentId.intValue()>0){
								dto.setSellerId(parentId.toString());
							}else{
								dto.setSellerId(userId.toString());
							}
							dto.setRefundTime(new Date());//同意退款时间
							result = tradeReturnExportService.updateTradeReturnStatus(dto, status);//待平台处理退款
							if(result.isSuccess()){
								// 计算退款金额
								tradeReturnExportService.calTotalRefundGoods(returnId);
							}
							/*}else{
							errorMessages.add("支付密码错误，请重新输入！");
							result.setErrorMessages(errorMessages);
							return result;
						}*/
					}
				}
			}else if("2".equals(type)){
				//不同意退款
				String auditRemark = request.getParameter("auditRemark");
				if(StringUtils.isEmpty(auditRemark) && StringUtils.isNumeric(auditRemark)){
					errorMessages.add("拒绝退款原因不能为空");
					result.setErrorMessages(errorMessages);
					return ;
				}
				TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
				dto.setId(Long.valueOf(returnId));
				dto.setState(TradeReturnStatusEnum.DISAGRESS.getCode());
				if(parentId!=null && parentId.intValue()>0){
					dto.setSellerId(parentId.toString());
				}else{
					dto.setSellerId(userId.toString());
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
				
	*/				TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
					dto.setId(Long.valueOf(returnId));
					ExecuteResult<TradeReturnInfoDto> executeResult=tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
					TradeReturnGoodsDTO tradeReturnGoodsDTO=executeResult.getResult().getTradeReturnDto();
					ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(tradeReturnGoodsDTO.getOrderId());
					TradeReturnStatusEnum status=TradeReturnStatusEnum.REFUNDING;
					if(resultTradeOrdersDTO.getResult().getPaymentMethod()==2||PayBankEnum.OFFLINE.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())
						||PayBankEnum.OTHER.name().equals(PayBankEnum.getEnumByQrCode(Integer.parseInt(tradeReturnGoodsDTO.getOrderPayBank())).name())){
						dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());//线下支付，企业支付，其他支付不走ecm确认退款流程
					}else{
						dto.setState(TradeReturnStatusEnum.PLATFORMTOREFUND.getCode());
						dto.setConfirmStatus("0");//待审核确认
						status=TradeReturnStatusEnum.PLATFORMTOREFUND;
					}
					if(parentId!=null && parentId.intValue()>0){
						dto.setSellerId(parentId.toString());
					}else{
						dto.setSellerId(userId.toString());
					}
					dto.setRefundTime(new Date());//同意退款时间
					result = tradeReturnExportService.updateTradeReturnStatus(dto, status);//待平台处理退款
					if(result.isSuccess()){
						// 计算退款金额
						tradeReturnExportService.calTotalRefundGoods(returnId);
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
			writer.write(JSON.toJSONString(result));
			return;
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
		 * 跳转物流信息
		 * @return
		 * @throws Exception 
		 */
		@RequestMapping(value="/addLogistics")
		public String addLogistics(@RequestParam(value="logisticsNo")String logisticsNo,
				@RequestParam(value="logisticsCompany")String logisticsCompany,
				@RequestParam(value="orderId") String orderId,
				@RequestParam(value="flag") String flag,
				Model model) throws Exception{
			LOG.debug("------------物流公司====="+logisticsCompany);
			logisticsCompany = URLDecoder.decode(logisticsCompany,"UTF-8");
			model.addAttribute("logisticsNo", logisticsNo);
			model.addAttribute("logisticsCompany", logisticsCompany);
			LOG.debug("------------物流编号和物流公司---------------------"+logisticsNo +"-----------------"+logisticsCompany);
			model.addAttribute("orderId",orderId);
			model.addAttribute("flag", flag);//flag=1 填写物流信息   flag=2 查看物流信息
			return "/order/addLogistics";
		}
		
		/**
		 * 填写物流信息
		 * @return
		 */
		@RequestMapping(value="/modifyLogistics")
		public void modifyLogistics(HttpServletRequest request,PrintWriter writer,@CookieValue(value=Constants.USER_ID) Long userId){
			ExecuteResult<String> result = new ExecuteResult<String>();
			List<String> errorMessages = new ArrayList<String>();
			String orderId = request.getParameter("orderId");
			if(StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)){
				errorMessages.add("订单Id不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
			}
			String logisticsNo = request.getParameter("logisticsNo");
			if(StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)){
				errorMessages.add("物流编号不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
			}
			String logisticsCompany = request.getParameter("logisticsCompany");
			if(StringUtils.isEmpty(orderId)){
				errorMessages.add("物流公司不能为空");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
			}
			//当前登录的卖家用户
	//		Long userId = WebUtil.getInstance().getUserId(request);
			if(userId==null){
				errorMessages.add("用户未登录");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
				return;
			}
			TradeOrdersDTO trd = new TradeOrdersDTO();
			trd.setOrderId(orderId);
			trd.setLogisticsNo(logisticsNo);//  物流编号
			trd.setLogisticsCompany(logisticsCompany);//  物流公司
			LOG.debug("------------物流编号和物流公司---------------------"+logisticsNo +"-----------------"+logisticsCompany);
			trd.setSellerId(userId);
			result = tradeOrderExportService.modifyLogisticsInfo(trd);
			writer.write(JSON.toJSONString(result));
		}
		
		/**
		 * 确认收货时收入支付密码
		 * @return
		 */
		@RequestMapping(value="/confirmReceipt")
		public void confirmReceipt(HttpServletRequest request,HttpServletResponse response, Model model,PrintWriter writer,@CookieValue(value=Constants.USER_ID) Long uid){
			String orderId = request.getParameter("orderId");
			String userToken = LoginToken.getLoginToken(request);
			String paypwd = request.getParameter("paypwd");
			ExecuteResult<String> result = new ExecuteResult<String>();
			List<String> errs = new ArrayList<String>();
			if(StringUtils.isEmpty(userToken)){
				errs.add("用户未登录！");
				result.setErrorMessages(errs);
				request.setAttribute("result",result);
				writer.write(JSON.toJSONString(result));
				return;
			}
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto==null){
				errs.add("未找到登录用户的信息！");
				result.setErrorMessages(errs);
				request.setAttribute("result",result);
				writer.write(JSON.toJSONString(result));
				return;
			}
			if(StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)){
				errs.add("订单号不能为空，并且必须是数字");
				result.setErrorMessages(errs);
				request.setAttribute("result",result);
				writer.write(JSON.toJSONString(result));
				return;
			}
			if(StringUtils.isEmpty(paypwd)){
				errs.add("支付密码不能为空");
				result.setErrorMessages(errs);
				request.setAttribute("result",result);
				writer.write(JSON.toJSONString(result));
				return;
			}
	//		Long uid = registerDto.getUid();
			paypwd = MD5.encipher(paypwd);
			ExecuteResult<String> payResult = userExportService.validatePayPassword(uid, paypwd);
			//LOG.info("payResult===="+JSON.toJSONString(payResult));
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
	                            			&&TradeReturnStatusEnum.REFUNDFAIL.getCode()!=dto.getState()&&TradeReturnStatusEnum.REFUNDAPPLICATION.getCode()!=dto.getState()){
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
					}
				}
				//--------------------------------------确认收货---------------------------------------------------------//
				LOG.info("------------------------------买家已确认收货-------------------------------------");
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.ORDER_CONFIRMATION);
	//			message.setFirst("【印刷家】尊敬的用户，订单"+orderId+"买家"+ userExportService.queryUserById(uid).getUname() +"已确认收货，印刷家提醒您及时查看。");
				message.setFirst("【印刷家】尊敬的用户，买家已确认收货，订单号（"+ orderId +"），印刷家提醒您及时查看。");
				message.setKeyword1(orderId);
				message.setKeyword2("");//TODO
				message.setKeyword3(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(resultTradeOrdersDTO.getResult().getCreateTime()));
				message.setKeyword5(new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date()));
				SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(resultTradeOrdersDTO.getResult().getSellerId()), message, request, response);
				//-----------------------------------------------------------------------------------------------//
			}else{
				errs.add("支付密码错误，请重新输入！");
				result.setErrorMessages(errs);
				request.setAttribute("result",result);
				writer.write(JSON.toJSONString(result));
				return;
			}
			writer.write(JSON.toJSONString(result));
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
		public void deliverOrder(HttpServletRequest request,HttpServletResponse response, Model model,PrintWriter writer) {
			String orderId = request.getParameter("orderId");
			String state = request.getParameter("state");
	
			ExecuteResult<String> result = new ExecuteResult<String>();
			request.setAttribute("result", result);
			List<String> errs = new ArrayList<String>();
			if(StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)){
				errs.add("订单号不能为空，并且必须是数字");
				result.setErrorMessages(errs);
				writer.write(JSON.toJSONString(result));
				return ;
			}
			if(StringUtils.isEmpty(state) && !StringUtils.isNumeric(state)){
				errs.add("订单状态不能为空，并且必须是数字");
				result.setErrorMessages(errs);
				writer.write(JSON.toJSONString(result));
				return ;
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
	                        			&&TradeReturnStatusEnum.REFUNDFAIL.getCode()!=dto.getState()&&TradeReturnStatusEnum.REFUNDAPPLICATION.getCode()!=dto.getState()){
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
			//--------------------------------------------------订单确认发货发送微信消息------------------------------------------------------------//
			if(result.isSuccess()){
				LOG.info("-------------------------卖家已完成发货-------------------------------------------");
				TradeOrdersDTO tradeOrdersDTO = resultTradeOrdersDTO.getResult();
	//			UserDTO userDTO = this.userExportService.queryUserById(tradeOrdersDTO.getSellerId());
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.ORDER_DELIVERY);
	//			message.setFirst("【印刷家】尊敬的用户，订单"+orderId+"卖家已发货，印刷家提醒您及时查看。");
				message.setFirst("【印刷家】尊敬的用户，卖家已完成发货，订单号（" + orderId + "），印刷家提醒您及时查看。");
				message.setKeyword1(orderId);
				ExecuteResult<TradeOrdersDTO> er = tradeOrderExportService.getOrderById(orderId);
				List<TradeOrderItemsDTO> items = er.getResult().getItems();
				StringBuffer sb = new StringBuffer();
				for(TradeOrderItemsDTO t : items){
					sb.append(t.getSkuName()).append(";");
				}
				message.setKeyword2(sb.toString());//
				message.setKeyword3(tradeOrdersDTO.getPaymentPrice().toString());
				SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(tradeOrdersDTO.getBuyerId()), message, request, response);
			}
			//--------------------------------------------------------------------------------------------------------------//
	//		return result;
			writer.write(JSON.toJSONString(result));
			return ;
		}
		
		/**
		 * 
		 * <p>Discription:[卖家： 确认付款]</p>
		 * Created on 2015年5月20日
		 * @param request
		 * @return
		 * @author:[马桂雷]
		 */
		@RequestMapping({ "modifyOrderPayStatus" })
		public void modifyOrderPayStatus(HttpServletRequest request,PrintWriter writer) {
			ExecuteResult<String> result = new ExecuteResult<String>();
			List<String> errorMessages = new ArrayList<String>();
			String orderId = request.getParameter("orderId");
			if(StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)){
				errorMessages.add("订单Id不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
	//			return result;
				writer.write(JSON.toJSONString(result));
				return ;
			}
			String paymentType = request.getParameter("paymentType");
			if(StringUtils.isEmpty(paymentType) || !StringUtils.isNumeric(paymentType)){
				errorMessages.add("支付类型不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
	//			return result;
				writer.write(JSON.toJSONString(result));
				return ;
			}
			//当前登录的卖家用户
			Long userId = WebUtil.getInstance().getUserId(request);
			if(userId==null){
				errorMessages.add("用户未登录");
				result.setErrorMessages(errorMessages);
	//			return result;
				writer.write(JSON.toJSONString(result));
				return ;
			}
			TradeOrdersDTO inDTO = new TradeOrdersDTO();
			inDTO.setOrderId(orderId);
			inDTO.setPaid(2);
			inDTO.setPaymentType(Integer.parseInt(paymentType));
			inDTO.setSellerId(userId);
			result = tradeOrderExportService.modifyOrderPayStatus(inDTO);
			request.setAttribute("result",result);
	//		return result;
			writer.write(JSON.toJSONString(result));
			return ;
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
		public String refundAgreement(HttpServletRequest request,HttpServletResponse response, Model model,@CookieValue(value=Constants.USER_ID) String uid) {
	
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			List<String> errorMessages = new ArrayList<String>();
	
			String orderId = request.getParameter("orderId");
			if(StringUtils.isEmpty(orderId) && !StringUtils.isNumeric(orderId)){
				errorMessages.add("订单id不正确");
				result.setErrorMessages(errorMessages);
				return "/order/refund";
			}
			model.addAttribute("orderId", orderId);
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
					//LOG.info("tradeOrdersDTO===="+JSON.toJSONString(tradeOrdersDTO));
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
	
					JSONArray jsonArray_item = new JSONArray();
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
					for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
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
					jsonObject.put("items", jsonArray_item);
	
					model.addAttribute("refundPriceTotal", refundPriceTotal);//退货商品的总金额
					model.addAttribute("jsonObject", jsonObject);
					//LOG.info("jsonObject===="+JSON.toJSONString(jsonObject));
				}
			}
			
			return "/order/refund";
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
					//LOG.info("result==========="+JSON.toJSONString(result));
					model.addAttribute("tradeReturnGoodsDetailList", result.getResult().getTradeReturnGoodsDetailList());
					TradeReturnGoodsDTO tradeReturnGoodsDTO = result.getResult().getTradeReturnDto();
					model.addAttribute("tradeReturnDto",  tradeReturnGoodsDTO);
					if(tradeReturnGoodsDTO.getState()==6){
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
					
					//如果正在投诉0或已仲裁1，页面按钮显示为“查看仲裁信息”，如果为已撤销2，页面按钮显示为“申请仲裁”
					String complainStatus="2";
					for (ComplainDTO comD : listComplainDTO) {
						if(comD.getStatus()==0 ){
							complainStatus="0";
						}else if(comD.getStatus()==1){
							complainStatus="1";
							break;
						}
					}
					model.addAttribute("complainStatus", complainStatus);
					
					model.addAttribute("progressBar", progressBar);	//页面进度条
				}
			}
			return "/order/refundInfo_buyer";
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
		@RequestMapping(value="/refundInfoSeller")
		public String refundInfoSeller(HttpServletRequest request, Model model) {
			String returnGoodId = request.getParameter("returnGoodId");
			
			int progressBar=3;	//默认进度条3
			
			if(StringUtils.isNotEmpty(returnGoodId)){
				ExecuteResult<TradeReturnInfoDto> result = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnGoodId);
				if(result!=null && result.getResult()!= null){
					//LOG.info("result==========="+JSON.toJSONString(result));
					model.addAttribute("tradeReturnGoodsDetailList", result.getResult().getTradeReturnGoodsDetailList());
					TradeReturnGoodsDTO tradeReturnDto = result.getResult().getTradeReturnDto();
					//对退货ID加密
					Long userId = WebUtil.getInstance().getUserId(request);
					ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", tradeReturnDto.getId()+"");
					if(passKeyEr.isSuccess()){
						tradeReturnDto.setPassKey(passKeyEr.getResult());
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
			
			return "/order/refundInfo_seller";
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
		@RequestMapping(value="/updateTradeReturnSeller")
		public void updateTradeReturnSeller(HttpServletRequest request,HttpServletResponse response, Model model,
				@CookieValue(value=Constants.USER_ID) String uid,
				PrintWriter writer) {
			ExecuteResult<TradeReturnGoodsDTO>  result = new ExecuteResult<TradeReturnGoodsDTO>();
			request.setAttribute("result",result);
			List<String> errorMessages = new ArrayList<String>();
			TradeOrdersDTO tradeOrdersDTO = new TradeOrdersDTO();
			String returnId = request.getParameter("returnId");
			if(StringUtils.isEmpty(returnId) || !StringUtils.isNumeric(returnId)){
				errorMessages.add("退款id不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
	//			return result;
				return ;
			}
			String type = request.getParameter("type");//1、同意退款  2、不同意退款
			if(StringUtils.isEmpty(type) || !StringUtils.isNumeric(type)){
				errorMessages.add("操作类型type不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				writer.write(JSON.toJSONString(result));
	//			return result;
				return ;
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
				if(StringUtils.isEmpty(orderId) || !StringUtils.isNumeric(orderId)){
					errorMessages.add("订单Id不能为空，必须是数字");
					result.setErrorMessages(errorMessages);
					writer.write(JSON.toJSONString(result));
	//				return result;
					return ;
				}
				ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(orderId);
				if(resultTradeOrdersDTO!=null && resultTradeOrdersDTO.getResult()!=null){
					tradeOrdersDTO = resultTradeOrdersDTO.getResult();
					Integer state = resultTradeOrdersDTO.getResult().getState();
					if(state > 3 && state < 6 ){
						//买家已收到货
						//卖家填写收货地址，让买家发货
						String returnAddress = request.getParameter("returnAddress");
						String returnPhone = request.getParameter("returnPhone");
						String returnPostcode = request.getParameter("returnPostcode");
	
						TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
						dto.setId(Long.valueOf(returnId));
						dto.setState(TradeReturnStatusEnum.PASS.getCode());
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
						result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.PASS);
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
							dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());
							if(parentId!=null && parentId.intValue()>0){
								dto.setSellerId(parentId.toString());
							}else{
								dto.setSellerId(userId.toString());
							}
							result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.REFUNDING);
						/*}else{
							errorMessages.add("支付密码错误，请重新输入！");
							result.setErrorMessages(errorMessages);
							return result;
						}*/
					}
				}
			}else if("2".equals(type)){
				//不同意退款
				String auditRemark = request.getParameter("auditRemark");
				if(StringUtils.isEmpty(auditRemark) && StringUtils.isNumeric(auditRemark)){
					errorMessages.add("拒绝退款原因不能为空");
					result.setErrorMessages(errorMessages);
					writer.write(JSON.toJSONString(result));
	//				return result;
					return ;
				}
				TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
				dto.setId(Long.valueOf(returnId));
				dto.setState(TradeReturnStatusEnum.DISAGRESS.getCode());
				if(parentId!=null && parentId.intValue()>0){
					dto.setSellerId(parentId.toString());
				}else{
					dto.setSellerId(userId.toString());
				}
				dto.setAuditRemark(auditRemark);
				result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.DISAGRESS);
				
				//-------------------------拒绝退款发送微信消息---------------------------------//
				ExecuteResult<TradeReturnInfoDto> ex = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
	//			ExecuteResult<TradeOrdersDTO>  resultTradeOrdersDTO = tradeOrderExportService.getOrderById(Long.valueOf(returnId));
				if(ex.isSuccess()){
					SendWeiXinMessage message = new SendWeiXinMessage();
					message.setModeId(WeiXinMessageModeId.APPLICATION_FOR_DRAWBACK);
					message.setFirst("【印刷家】尊敬的用户，卖家已拒绝订单"+ex.getResult().getTradeReturnDto().getOrderId()+"申请的退款退货，印刷家提醒您及时查看。");
					message.setOrderProductPrice(ex.getResult().getTradeReturnDto().getOrderPrice().toString());
					message.setOrderProductName(ex.getResult().getTradeReturnGoodsDetailList().get(0).getGoodsName());
					message.setOrderName(ex.getResult().getTradeReturnDto().getOrderId());
					SendWeiXinMessageUtil.sendWeiXinMessage(ex.getResult().getTradeReturnDto().getBuyId(), message, request, response);
				}
				//----------------------------------------------------------//
				
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
				
	*/				TradeReturnGoodsDTO dto = new TradeReturnGoodsDTO();
					dto.setId(Long.valueOf(returnId));
					dto.setState(TradeReturnStatusEnum.REFUNDING.getCode());
					if(parentId!=null && parentId.intValue()>0){
						dto.setSellerId(parentId.toString());
					}else{
						dto.setSellerId(userId.toString());
					}
					result = tradeReturnExportService.updateTradeReturnStatus(dto, TradeReturnStatusEnum.REFUNDING);
					
				/*}else{
					errorMessages.add("支付密码错误，请重新输入！");
					result.setErrorMessages(errorMessages);
					return result;
				}*/
					
					//-------------------------同意退款发送微信消息---------------------------------//
					if(result.isSuccess() && StringUtils.isBlank(returnId)){
						LOG.info("---------------------订单已经完成退款-----------------------------");
						SendWeiXinMessage message = new SendWeiXinMessage();
						message.setModeId(WeiXinMessageModeId.APPLICATION_FOR_DRAWBACK);
	//					message.setFirst("【印刷家】尊敬的用户，卖家已同意订单"+request.getParameter("orderId")+"申请的退款退货，印刷家提醒您及时查看。");
						message.setFirst("【印刷家】尊敬的用户，您的订单已经完成退款，¥"+ result.getResult().getOrderPrice().toString() +"已经退回您的付款账户，印刷家提醒您及时查看。");
						message.setOrderProductPrice(tradeOrdersDTO.getPaymentPrice().toString());
						ExecuteResult<TradeOrdersDTO> tradeOrder = tradeOrderExportService.getOrderById(result.getResult().getOrderId());
						List<TradeOrderItemsDTO> items = tradeOrder.getResult().getItems();
						StringBuffer sb = new StringBuffer();
						for(TradeOrderItemsDTO t : items){
							sb.append(t.getSkuName()).append(";");
						}
						message.setOrderProductName(sb.toString());
						message.setOrderName(request.getParameter("orderId"));
						SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(tradeOrdersDTO.getBuyerId()), message, request, response);
					}
					//----------------------------------------------------------//
				
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
			writer.write(JSON.toJSONString(result));
	//		return result;
			return ;
		}
		
		
		
		/**
		 * 
		 * <p>Discription:[卖家： 确认收货][卖家： 同意退款（买家未收货）]</p>
		 * Created on 2015年7月8日18:09:26
		 * @param request
		 * @return
		 * @author:[武超强]
		 */
		@RequestMapping(value="/getAskByPayType")
		public void getAskByPayType(HttpServletRequest request,PrintWriter writer){
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
	//			return jsonObject;
				writer.write(JSON.toJSONString(jsonObject));
				return ;
			}
			if(StringUtils.isEmpty(paypwd)){
				jsonObject.put("desc", "error");
				jsonObject.put("result", "支付密码不能为空");
	//			return jsonObject;
				writer.write(JSON.toJSONString(jsonObject));
				return ;
			}
			paypwd = MD5.encipher(paypwd);
			ExecuteResult<String> payResult = userExportService.validatePayPassword(userId, paypwd);
			if(!"1".equals(payResult.getResult())){//验证支付码是否正确
				jsonObject.put("desc", "error");
				jsonObject.put("result", "支付密码错误，请重新输入！");
	//			return jsonObject;
				writer.write(JSON.toJSONString(jsonObject));
				return ;
			}
			
			//获取订单信息
			ExecuteResult<TradeOrdersDTO> orderRes=tradeOrderExportService.getOrderById(orderId);
			if (orderRes != null && orderRes.getResult() != null) {
	            TradeOrdersDTO orderItem = orderRes.getResult();
	            //判断是否是线下支付
	            if(!(orderItem.getPaid()==2&&orderItem.getLocked()!=2&&orderItem.getPaymentType()!=3)){
	            	jsonObject.put("result", "nopay");
	            }
			}
	//		return jsonObject;
			writer.write(JSON.toJSONString(jsonObject));
			return ;
		}
		
		
		/**
	    *
	    * <p>
	    * Description: [跳到投诉新增界面,卖家投诉]
	    * </p>
	    * Created on 2015-4-17
	    *
	    * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
	    * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	    */
	   @RequestMapping(value = "/gocomplainselleradd")
	   public String gocomplainSellerAdd(HttpServletRequest request,Model model){
		   //flag 标识是从买家还是卖家跳转过来，防止菜单错误
		   String flag=request.getParameter("flag");
		   String url= "/order/complainselleradd";
		   String type ="2";
		   boolean isSell =false;
		   if(flag!=null && "1".equals(flag)){
		   	url="/order/complainadd";
		   	type ="1";
		   	isSell =true;
		   }
	       String tradeReturnid=request.getParameter("tradeReturnid");
	       String status=request.getParameter("status");
	       model.addAttribute("tradeReturnid", tradeReturnid);
	       model.addAttribute("complainStatus", status);
	       //String tradeReturnid="24";
	       model.addAttribute("right1","block");
	       model.addAttribute("right","none");
	       //卖家投诉
	       model.addAttribute("sellerOrBuyer","2");
	       ComplainDTO comp=new ComplainDTO();
	       comp.setRefundId(new Long(tradeReturnid));
	       comp.setComplainType(type);
	       if(status!=null && status.length()>0){
	       	comp.setStatus(Integer.parseInt(status));
	       }else{
	       	comp.setStatus(0);
	       }
	       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	       DataGrid<ComplainDTO> da=complainExportService.findInfoByCondition(comp,null);
	       
	       //若为查看仲裁信息，则返回详细页面
	       //progressBar为进度条当前到第几步
	       if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
	       	ComplainDTO complainDTO = da.getRows().get(0);
	       	model.addAttribute("complainDTO",complainDTO);
	       	if(complainDTO.getStatus()==0){
	       		model.addAttribute("progressBar",2);	//审核中
	       	}else if(complainDTO.getStatus()==1){
	       		model.addAttribute("progressBar",3);	//仲裁完成
	       	}
	       }else{
	       	model.addAttribute("progressBar",1);	//申请仲裁
	       }
	       //武超强修改 2015.7.3  注释以下代码
	       /*if(da!=null&&da.getRows()!=null&&da.getRows().size()>0){
	           model.addAttribute("right1","none");
	           model.addAttribute("right","bolck");
	           Long orderId=da.getRows().get(0).getOrderId();
	           if(orderId!=null){
	               model.addAttribute("orderId",String.valueOf(orderId));
	           }
	           Integer type=da.getRows().get(0).getType();
	           if(type!=null&&type==2){
	               model.addAttribute("typeName","售后相关");
	           }else{
	               model.addAttribute("typeName","退款相关");
	           }
	           model.addAttribute("zcstace","等待客服仲裁");
	           model.addAttribute("stace",String.valueOf(0));
	           model.addAttribute("remark",da.getRows().get(0).getComplainContent());
	           if(da.getRows().get(0).getCreated()!=null){
	               model.addAttribute("createdate",simpleDateFormat.format(da.getRows().get(0).getCreated()));
	           }
	           return url;
	       }*/
	
	       //是否退款true表示为退款投诉，false表示为售后投诉
	       model.addAttribute("type",type);
	       model.addAttribute("iftk",isSell);
	       model.addAttribute("typeName","退款相关");
	       Map<String,String> map=new HashMap<String, String>();
	       //退款单状态
	       Map<Integer,String> tkstaceMap=tkstaceMap();
	       //订单状态
	       Map<Integer,String> orderstaceMap=orderstaceMap();
	       ExecuteResult<TradeReturnInfoDto> executeResult= tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(tradeReturnid);
	       if(executeResult!=null){
	           TradeReturnInfoDto infoDto=executeResult.getResult();
	           if(infoDto!=null){
	               TradeReturnGoodsDTO tradeReturnDto=infoDto.getTradeReturnDto();
	               if(tradeReturnDto!=null){
	                   if(tradeReturnDto.getSellerId()!=null){
	                       //卖家id
	                       model.addAttribute("sellerid",String.valueOf(tradeReturnDto.getSellerId()));
	                   }
	                   if(tradeReturnDto.getBuyId()!=null){
	                       //买家id
	                       model.addAttribute("buyerid",String.valueOf(tradeReturnDto.getBuyId()));
	                   }
	                   //获取获取退货单号
	                   String reproNo=tradeReturnDto.getCodeNo();
	                   //将退货id反到前台
	                   model.addAttribute("thid",tradeReturnDto.getId());
	                   if(reproNo!=null&&!"".equals(reproNo)){
	                       RefundPayParam refundPayParam=new RefundPayParam();
	                       refundPayParam.setReproNo(reproNo);
	/*                        DataGrid<RefundPayParam> dataGrid= paymentExportService.findRefInfoByCondition(refundPayParam,null);
	                       if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0) {
	                           *//*****************************************//*
	                           //获取退款单和退货单信息
	                           RefundPayParam refundPayParam1 = dataGrid.getRows().get(0);
	                           //获取退款id
	                           model.addAttribute("tkid", refundPayParam1.getId());
	                           //退款单据号
	                           map.put("refundNo", refundPayParam1.getRefundNo());
	                       }*/
	                           map.put("reproNo",reproNo);
	                           if(tradeReturnDto.getApplyDt()!=null){
	                               //退款申请时间
	                               map.put("refcreated",simpleDateFormat.format(tradeReturnDto.getApplyDt()));
	                           }
	                           //退款原因
	                           map.put("returnResult",tradeReturnDto.getReturnResult());
	                           //退货单单状态
	                           if(tradeReturnDto.getState()!=null){
	                               map.put("tkstaceName",tkstaceMap.get(tradeReturnDto.getState()));
	                           }
	                           //订单退款货品金额
	                           if(tradeReturnDto.getRefundGoods()!=null){
	                               map.put("ordertkJe",tradeReturnDto.getRefundGoods().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
	                           }else{
	                               map.put("ordertkJe","0.00");
	                           }
	                           //订单退货运费金额
	                           if(tradeReturnDto.getRefundFreight()!=null){
	                               map.put("ordertkYf",tradeReturnDto.getRefundFreight().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
	                           }else{
	                               map.put("ordertkYf","0.00");
	                           }
	                           //判断是否是售后申请
	                           if(tradeReturnDto.getIsCustomerService()!=null&&"1".equals(tradeReturnDto.getIsCustomerService())){
	                               model.addAttribute("iftk",false);
	                               model.addAttribute("type","2");
	                               model.addAttribute("typeName","售后相关");
	                           }
	                           /*********************************************/
	                           //获取订单信息
	                           String orderId=tradeReturnDto.getOrderId();
	                           //订单号
	                           model.addAttribute("orderId",orderId);
	                           if(orderId!=null&&!"".equals(orderId)){
	                               TradeOrdersQueryInDTO tradeOrdersQueryInDTO=new TradeOrdersQueryInDTO();
	                               Pager<TradeOrdersQueryInDTO> pager=new Pager<TradeOrdersQueryInDTO>();
	                               pager.setPage(1);
	                               pager.setRows(10);
	                               tradeOrdersQueryInDTO.setOrderId(new String(orderId));
	                               ExecuteResult<DataGrid<TradeOrdersDTO>> executeResultorder= tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
	                               DataGrid<TradeOrdersDTO> dataGridorder=executeResultorder.getResult();
	                               if(dataGridorder!=null&&dataGridorder.getRows()!=null&&dataGridorder.getRows().size()>0){
	                                   TradeOrdersDTO tradeOrdersDTO=dataGridorder.getRows().get(0);
	                                   //订单实际支付金额
	                                   if(tradeOrdersDTO.getPaymentPrice()!=null){
	                                       map.put("orderje",tradeOrdersDTO.getPaymentPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString());
	                                   }else{
	                                       map.put("orderje","0.00");
	                                   }
	                                   //订单创建时间
	                                   if(tradeOrdersDTO.getCreateTime()!=null){
	                                       map.put("orderCreated",simpleDateFormat.format(tradeOrdersDTO.getCreateTime()));
	                                   }
	                                   //订单付款时间
	                                   if(tradeOrdersDTO.getPaymentTime()!=null){
	                                       map.put("orderpayTime",simpleDateFormat.format(tradeOrdersDTO.getPaymentTime()));
	                                   }
	                                   //订单状态
	                                   if(tradeOrdersDTO.getState()!=null){
	                                       map.put("orderStace",orderstaceMap.get(tradeOrdersDTO.getState()));
	                                   }
	                                   //获取退货明细信息
	                               }
	                           }
	                           /**********************************************/
	
	                   }
	               }
	           }
	       }
	       model.addAttribute("map",map);
	       return url;
	   }
		//-------------------------------------------------------------------------------------------//
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
		public String refundSeller(HttpServletRequest request, Model model,@RequestParam(value="type",required=false,defaultValue="") String type,
				@RequestParam(value="back",required=false,defaultValue="") String back,
				@RequestParam(value="pageNo",defaultValue="1") int pageNo,
				@RequestParam(value="pageSize",defaultValue="3") int pageSize) {
			
			model.addAttribute("back", back);
			model.addAttribute("type",type);
			
			Pager<TradeReturnGoodsDTO> pager = new Pager<TradeReturnGoodsDTO>();
			pager.setPage(pageNo);
			pager.setRows(pageSize);
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
					model.addAttribute("totalItem", pager.getTotalCount());
				}
			}
			if(StringUtils.isNotEmpty(type)){
				return "/order/refund_seller_detail";
			}else{
				return "/order/refund_seller";
			}
		}
		
		
		/**
	    *
	    * <p>
	    * Description: [跳到投诉查看页面,买家投诉]
	    * </p>
	    * Created on 2015-4-17
	    *
	    * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
	    * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	    */
	   @RequestMapping(value = "gocomplainsellerdetail")
	   public String gocomplainSellerDetail(HttpServletRequest request,Model model){
	       String url= "/order/complainsellerdetail";
	       String complainid=request.getParameter("complainid");
	       //String tradeReturnid="24";
	       SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	       ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(complainid));
	       if(executeResult!=null&&executeResult.getResult()!=null){
	           ComplainDTO complainDTO=executeResult.getResult();
	           String orderId=complainDTO.getOrderId();
	           if(orderId!=null){
	               model.addAttribute("orderId",String.valueOf(orderId));
	           }
	           Integer type=complainDTO.getType();
	           if(type!=null&&type==2){
	               model.addAttribute("typeName","售后相关");
	           }else{
	               model.addAttribute("typeName","退款相关");
	           }
	           if(complainDTO.getComplainType()!=null&&"1".equals(complainDTO.getComplainType())){
	               //是卖家还是买家投诉的
	               model.addAttribute("tusufang","买家");
	           }else if(complainDTO.getComplainType()!=null&&"2".equals(complainDTO.getComplainType())){
	               model.addAttribute("tusufang","卖家");
	           }else{
	               model.addAttribute("tusufang","无法识别");
	           }
	           Integer status=complainDTO.getStatus();
	           if(status!=null&&status==1){
	               model.addAttribute("zcstace","已经仲裁");
	               model.addAttribute("zcjgms","已经仲裁完毕，如有疑问请重新提交仲裁，谢谢理解：-）");
	               model.addAttribute("stace",String.valueOf(status));
	           }else if(status!=null&&status==2){
	               model.addAttribute("zcstace","已撤消投诉");
	               model.addAttribute("zcjgms","仲裁已经撤销，如有疑问请重新提交仲裁，谢谢理解：-）");
	               model.addAttribute("stace",String.valueOf(status));
	           }else{
	               model.addAttribute("zcstace","等待客服仲裁");
	               model.addAttribute("zcjgms","正在等待客服处理，请耐心等待，谢谢理解：-）");
	               model.addAttribute("stace",String.valueOf(0));
	           }
	           model.addAttribute("remark",complainDTO.getComplainContent());
	           if(complainDTO.getCreated()!=null){
	               model.addAttribute("createdate",simpleDateFormat.format(complainDTO.getCreated()));
	           }
	           //投诉原因
	           model.addAttribute("complainresion",complainDTO.getComplainResion());
	           //处理结果
	           model.addAttribute("stacetext",complainDTO.getStatusText());
	           //处理意见
	           model.addAttribute("comment",complainDTO.getComment());
	       }
	       return url;
	   }
		
	   
	   /**
	   *
	   * <p>
	   * Description: [跳到卖家投诉查询页面]
	   * </p>
	   * Created on 2015-4-17
	   *
	   * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
	   * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	   */
	  @RequestMapping(value = "gocomplainsellerlist")
	  public String goComplainSellerList(HttpServletRequest request,Model model,@RequestParam(value="type",required=false,defaultValue="") String type,
				@RequestParam(value="pageNo",defaultValue="1") int pageNo,
				@RequestParam(value="pageSize",defaultValue="3") int pageSize){
	      List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
	
	      String page=request.getParameter("pageNo");
	      String rows=request.getParameter("pageSize");
	      String complaintype=request.getParameter("complaintype");
	      if(page==null||"".equals(page)){
	          page="1";
	      }
	      if(rows==null||"".equals(rows)){
	          rows="3";
	      }
	      String orderId=request.getParameter("orderId");
	      String createTimeStart=request.getParameter("createTimeStart");
	      String createTimeEnd=request.getParameter("createTimeEnd");
	      //用户id
	      //用户id
	      Long uid = WebUtil.getInstance().getUserId(request);
	      UserDTO userDTO = userExportService.queryUserById(uid);
	      Pager pager=new Pager();
	      pager.setRows(new Integer(rows));
	      pager.setPage(new Integer(page));
	      pager.setTotalCount(0);
	      ComplainDTO complainDTO=new ComplainDTO();
	      complainDTO.setStatusSelect("2");	//投诉页面使用标识
	      if(userDTO.getParentId() == null){//主账号id
	          complainDTO.setSellerId(uid);
	      }else{//子账号id
	          complainDTO.setSellerId(userDTO.getParentId());
	      }
	      if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
	          complainDTO.setOrderId(orderId);
	      }
	      if(createTimeStart!=null&&!"".equals(createTimeStart)){
	          complainDTO.setCreatedBegin(createTimeStart);
	      }
	      if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
	          complainDTO.setCreatedEnd(createTimeEnd);
	      }
	      if(complaintype!=null&&!"".equals(complaintype)){
	          complainDTO.setComplainType(complaintype);
	      }
	      DataGrid<ComplainDTO> dataGrid=complainExportService.findEarlyComplainInfoByCondition(complainDTO,pager);
	      if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
	          List<ComplainDTO> list=dataGrid.getRows();
	          Map<String,List<Map<String,String>>> goodsDetail=goodsDetail(list);
	          if(list!=null){
	              Iterator<ComplainDTO> iterator=list.iterator();
	              SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	              while(iterator.hasNext()){
	                  ComplainDTO comp=iterator.next();
	                  Map<String,Object> map=new HashMap<String, Object>();
	                  map.put("orderId",comp.getOrderId());
	                  map.put("shopName",comp.getShopName());
	                  if(comp.getCreated()!=null){
	                      map.put("created",simpleDateFormat.format(comp.getCreated()));
	                  }
	                  if(comp.getSellerId()!=null){
	                      map.put("sellerId",String.valueOf(comp.getSellerId()));
	                  }
	                  if(comp.getRefundId()!=null){
	                      map.put("thid",String.valueOf(comp.getRefundId()));
	                      ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getRefundId()+"");
	                      if(passKeyEr.isSuccess()){
	                      	map.put("passKey",passKeyEr.getResult());
	                      }
	                      map.put("refundId",String.valueOf(comp.getRefundId()));
	                  }
	                  if(comp.getId()!=null){
	                      map.put("complainid",String.valueOf(comp.getId()));
	                      ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getId()+"");
	                      if(passKeyEr.isSuccess()){
	                      	map.put("passKey2",passKeyEr.getResult());
	                      }
	                  }
	                  if(comp.getStatus()!=null&&comp.getStatus()==1){
	                      map.put("staceName","退款已仲裁");
	                      map.put("stace","1");
	                  }else if(comp.getStatus()!=null&&comp.getStatus()==2){
	                      map.put("staceName","已取消");
	                      map.put("stace","2");
	                  }else{
	                      map.put("staceName","待客服仲裁");
	                      map.put("stace","0");
	                  }
	                  if(comp.getComplainType()!=null&&"1".equals(comp.getComplainType())){
	                      map.put("sellerOrBuyer","1");
	                      map.put("tousufang","买家投诉");
	                  }else if(comp.getComplainType()!=null&&"2".equals(comp.getComplainType())){
	                      map.put("sellerOrBuyer","2");
	                      map.put("tousufang","店铺投诉");
	                  }else{
	                      map.put("sellerOrBuyer","3");
	                      map.put("tousufang","无法判定投诉方");
	                  }
	                  if(comp.getRefundId()!=null){
	                      map.put("list",goodsDetail.get(String.valueOf(comp.getRefundId())));
	                  }
	                  listMap.add(map);
	              }
	          }
	          pager.setTotalCount(dataGrid.getTotal().intValue());
	      }
	      pager.setRecords(listMap);
	      model.addAttribute("total1", dataGrid.getTotal().intValue());
	      model.addAttribute("pageNo",new Integer(page));
	      model.addAttribute("pager",pager);
	      if(StringUtils.isNotEmpty(type)){
	    	  return "/order/complainsellerlist";
	      }else{
	    	  return "/order/refund_seller";
	      }
	  }
	   
	  private Map<String,List<Map<String,String>>> goodsDetail(List<ComplainDTO> list){
	      Map<String,List<Map<String,String>>> detailMaps=new HashMap<String, List<Map<String, String>>>();
	      Iterator<ComplainDTO> iterator=list.iterator();
	      Map<Long,String> mapgoodid=new HashMap<Long, String>();
	      while(iterator.hasNext()){
	          ComplainDTO complainDTO=iterator.next();
	          if(complainDTO.getReturnGoodsId()!=null){
	              mapgoodid.put(complainDTO.getReturnGoodsId(),"");
	          }
	      }
	      if(mapgoodid.size()>0){
	          Long[] goodids=new Long[mapgoodid.size()];
	          Iterator<Long> iterator1=mapgoodid.keySet().iterator();
	          int i=0;
	          while(iterator1.hasNext()){
	              goodids[i]=iterator1.next();
	              i++;
	          }
	          TradeReturnGoodsDetailDTO dto=new TradeReturnGoodsDetailDTO();
	          dto.setReturnGoodsIds(goodids);
	          ExecuteResult<List<TradeReturnGoodsDetailDTO>> executeResult=tradeReturnExportService.getTradeReturnGoodsDetaiByCondition(dto);
	          if(executeResult!=null&&executeResult.getResult()!=null&&executeResult.getResult().size()>0){
	              List<TradeReturnGoodsDetailDTO> list1=executeResult.getResult();
	              Iterator<TradeReturnGoodsDetailDTO> iterator2=list1.iterator();
	              while(iterator2.hasNext()){
	                  TradeReturnGoodsDetailDTO tra=iterator2.next();
	                  if(tra.getGoodsId()!=null){
	                      String id=String.valueOf(tra.getReturnGoodsId());
	                      List<Map<String,String>> listMaps=detailMaps.get(id);
	                      if(listMaps!=null){
	                          Map<String,String> mapdetail=new HashMap<String, String>();
	                          //skuurl
	                          mapdetail.put("skuUrl",tra.getGoodsPicUrl());
	                          mapdetail.put("skuName",tra.getGoodsName());
	                          if(tra.getSkuId()!=null){
	                              mapdetail.put("skuId",String.valueOf(tra.getSkuId()));
	                          }
	                          if(tra.getGoodsId()!=null){
	                              mapdetail.put("itemid",String.valueOf((tra.getGoodsId())));
	                          }
	                          if(tra.getPayPrice()!=null){
	                              mapdetail.put("payPrice",String.valueOf(tra.getPayPrice()));
	                          }
	                          if(tra.getRerurnCount()!=null){
	                              mapdetail.put("goodCount",String.valueOf(tra.getRerurnCount()));
	                          }
	                          if(tra.getReturnAmount()!=null){
	                              mapdetail.put("amount",String.valueOf(tra.getReturnAmount()));
	                          }
	                          listMaps.add(mapdetail);
	                      }else{
	                          listMaps=new ArrayList<Map<String, String>>();
	                          Map<String,String> mapdetail=new HashMap<String, String>();
	                          //skuurl
	                          mapdetail.put("skuUrl",tra.getGoodsPicUrl());
	                          mapdetail.put("skuName",tra.getGoodsName());
	                          if(tra.getSkuId()!=null){
	                              mapdetail.put("skuId",String.valueOf(tra.getSkuId()));
	                          }
	                          if(tra.getGoodsId()!=null){
	                              mapdetail.put("itemid",String.valueOf((tra.getGoodsId())));
	                          }
	                          if(tra.getPayPrice()!=null){
	                              mapdetail.put("payPrice",String.valueOf(tra.getPayPrice()));
	                          }
	                          if(tra.getRerurnCount()!=null){
	                              mapdetail.put("goodCount",String.valueOf(tra.getRerurnCount()));
	                          }
	                          if(tra.getReturnAmount()!=null){
	                              mapdetail.put("amount",String.valueOf(tra.getReturnAmount()));
	                          }
	                          listMaps.add(mapdetail);
	                          detailMaps.put(id,listMaps);
	                      }
	                  }
	              }
	          }
	      }
	      return detailMaps;
	  }
	  
	  /**
	   * 取消投诉
	   * @param request
	   * @return
	   */
	  @RequestMapping(value = "complaincancle")
	  public void complainCancle(HttpServletRequest request,PrintWriter writer){
	      Json json=new Json();
	      //仲裁id
	      String id=request.getParameter("id");
	      try{
	          ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(id));
	          if(executeResult.getResult()!=null){
	              ComplainDTO complainDTO=executeResult.getResult();
	              //取消仲裁
	              complainDTO.setStatus(new Integer(2));
	              complainDTO.setModified(new Date());
	              complainDTO.setResolutionTime(new Date());
	              ExecuteResult<String> executeResul= complainExportService.modifyComplainInfo(complainDTO);
	              json.setMsg("取消成功");
	              json.setSuccess(true);
	              writer.write(JSON.toJSONString(json));
	              return ;
	          }else{
	              json.setMsg("取消失败");
	              json.setSuccess(false);
	              writer.write(JSON.toJSONString(json));
	              return ;
	          }
	      }catch(Exception e){
	          json.setMsg("取消失败，请联系管理员");
	          json.setSuccess(false);
	          writer.write(JSON.toJSONString(json));
	          return ;
	      }
	//      return json;
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
		public String cservice(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, Integer page, HttpServletRequest request, Model model,
				@RequestParam(value="type",required=false,defaultValue="") String type,
				@RequestParam(value="back",required=false,defaultValue="") String back,
				@RequestParam(value="pageNo",defaultValue="1") int pageNo,
				@RequestParam(value="pageSize",defaultValue="3") int pageSize) {
			String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
			if(StringUtils.isNotEmpty(uid) && StringUtils.isNumeric(uid)){
				tradeOrdersQueryInDTO.setBuyerId(Long.valueOf(uid));
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
			pager.setRows(pageSize);
			pager.setPage(pageNo);
	
			List<Integer> stateList = new ArrayList<Integer>();
			stateList.add(2);//待配送
			stateList.add(3);//已发货
			stateList.add(4);//待评价
			stateList.add(5);//已完成
			stateList.add(6);//已取消
			stateList.add(7);//已关闭
			stateList.add(8);//待确认
			tradeOrdersQueryInDTO.setStateList(stateList);
			ExecuteResult<Long> queryOrderQty = this.tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO);
			if(null!=queryOrderQty){
				model.addAttribute("totalItem", queryOrderQty.getResult());
			}
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
			DataGrid<TradeOrdersDTO> dataGrid = new DataGrid<TradeOrdersDTO>();
			JSONArray jsonArray = new JSONArray();
			if (executeResult != null) {
				dataGrid = executeResult.getResult();
				List<TradeOrdersDTO> tradeOrdersDTOs = dataGrid.getRows();
				for(TradeOrdersDTO tradeOrdersDTO :  tradeOrdersDTOs){
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("orderId", tradeOrdersDTO.getOrderId());
					jsonObject.put("paid", tradeOrdersDTO.getPaid());
					jsonObject.put("shipmentType", tradeOrdersDTO.getShipmentType());
					
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
			model.addAttribute("type", type);
			model.addAttribute("back", back);
	
			tradeOrdersQueryInDTO.setState(2);//卖家待发货
			model.addAttribute("stayDelivery",tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO).getResult());
			tradeOrdersQueryInDTO.setState(3);//买家待收货
			model.addAttribute("stayReceipt",tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO).getResult());
			tradeOrdersQueryInDTO.setState(5);//已完成
			model.addAttribute("complete",tradeOrderExportService.queryOrderQty(tradeOrdersQueryInDTO).getResult());
	
			if(StringUtils.isNotEmpty(type)){
		    	return "/order/refundOrcserviceDetail";
		    }else{
		    	return "/order/refundOrcservice";
		    }
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
		 * <p>Discription:[买家：退款/售后管理]</p>
		 * Created on 2015年4月9日
		 * @param request
		 * @param model
		 * @return
		 * @author:[马桂雷]
		 */
		@RequestMapping({ "cserviceManager" })
		public String cserviceManager(HttpServletRequest request, Model model,
				@RequestParam(value="type",required=false,defaultValue="") String type,
				@RequestParam(value="pageNo",defaultValue="1") int pageNo,
				@RequestParam(value="pageSize",defaultValue="3") int pageSize) {
			
			
			Pager<TradeReturnGoodsDTO> pager = new Pager<TradeReturnGoodsDTO>();
			pager.setPage(pageNo);
			pager.setRows(pageSize);
			TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
			String userToken = LoginToken.getLoginToken(request);;
			if(userToken!=null){
				RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
				if(registerDto!=null){
					String orderId = request.getParameter("orderId");
					String state = request.getParameter("state");
	
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
					queryDto.setTradeReturnDto(tradeReturnDto);
					model.addAttribute("tradeReturnDto",tradeReturnDto);
					DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(pager, queryDto);
					int  i = 0;
					if(null != dg.getRows()){
						for(TradeReturnGoodsDTO dto : dg.getRows()){
							//暂时使用 add3字段存储skuid
							List<TradeReturnGoodsDetailDTO> details=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(dto.getId()));					
							if(null != details){
								dg.getRows().get(i).setAdd1(details.get(0).getSkuId()+"");
							}
							i++;
						}					
					}
					
					pager.setPage(pager.getPage());
					pager.setTotalCount(dg.getTotal().intValue());
					pager.setRecords(dg.getRows());
					if (pager.getEndPageIndex() == 0) {	//解决列表为空时，多余的0页
						pager.setEndPageIndex(pager.getStartPageIndex());
					}
					model.addAttribute("pager", pager);
				}
			}
			model.addAttribute("type",type);
			return "/order/refundOrcserviceManagerDetail";
		}
		
		
		/**
	    *
	    * <p>
	    * Description: [跳到投诉查询页面]
	    * </p>
	    * Created on 2015-4-17
	    *
	    * @author <a href="mailto: menguangyao63@camelotchina.com">门光耀</a>
	    * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	    */
	   @RequestMapping(value = "gocomplainlist")
	   public String goComplainList(HttpServletRequest request,Model model,
			   @RequestParam(value="type",required=false,defaultValue="") String type,
				@RequestParam(value="pageNo",defaultValue="1") int pageNo,
				@RequestParam(value="pageSize",defaultValue="3") int pageSize){
	       List<Map<String,Object>> listMap=new ArrayList<Map<String, Object>>();
	       String url= "/order/complainlist";
	       String page=request.getParameter("page");
	       String rows=request.getParameter("rows");
	       if(page==null||"".equals(page)){
	           page="1";
	       }
	       if(rows==null||"".equals(rows)){
	           rows="10";
	       }
	       String orderId=request.getParameter("orderId");
	       String createTimeStart=request.getParameter("createTimeStart");
	       String createTimeEnd=request.getParameter("createTimeEnd");
	       String complaintype=request.getParameter("complaintype");
	       //用户id
	       Long uid = WebUtil.getInstance().getUserId(request);
	       Pager pager=new Pager();
	       pager.setRows(new Integer(rows));
	       pager.setPage(new Integer(page));
	       pager.setTotalCount(0);
	       ComplainDTO complainDTO=new ComplainDTO();
	       //complainDTO.setStatusSelect("2");	//投诉页面使用标识
			ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(uid);
			complainDTO.setBuyIdList(idsEr.getResult());
	       
			if(!org.apache.commons.lang3.StringUtils.isBlank(orderId)){
	            complainDTO.setOrderId(orderId);
	        }
	        if(createTimeStart!=null&&!"".equals(createTimeStart)){
	            complainDTO.setCreatedBegin(createTimeStart);
	        }
	        if(createTimeEnd!=null&&!"".equals(createTimeEnd)){
	            complainDTO.setCreatedEnd(createTimeEnd);
	        }
	        if(complaintype!=null&&!"".equals(complaintype)){
	            complainDTO.setComplainType(complaintype);
	        }
	       pager.setPage(pageNo);
	       pager.setRows(pageSize);
	       DataGrid<ComplainDTO> dataGrid=complainExportService.findEarlyComplainInfoByCondition(complainDTO,pager);
	       model.addAttribute("total1", dataGrid.getTotal());
	       if(dataGrid!=null&&dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
	           List<ComplainDTO> list=dataGrid.getRows();
	           Map<String,List<Map<String,String>>> goodsDetail=goodsDetail(list);
	           if(list!=null){
	               Iterator<ComplainDTO> iterator=list.iterator();
	               SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
	               while(iterator.hasNext()){
	                   ComplainDTO comp=iterator.next();
	                   Map<String,Object> map=new HashMap<String, Object>();
	                   map.put("orderId",comp.getOrderId());
	                   map.put("shopName",comp.getShopName());
	                   if(comp.getCreated()!=null){
	                       map.put("created",simpleDateFormat.format(comp.getCreated()));
	                   }
	                   if(comp.getSellerId()!=null){
	                       map.put("sellerId",String.valueOf(comp.getSellerId()));
	                   }
	                   if(comp.getRefundId()!=null){
	                       map.put("thid",String.valueOf(comp.getRefundId()));
	                       map.put("skuId",String.valueOf(comp.getSkuId()));
	                       map.put("refundId",String.valueOf(comp.getRefundId()));
	                       ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getRefundId()+""+comp.getSkuId());
	                       if(passKeyEr.isSuccess()){
	                       	map.put("passKey", passKeyEr.getResult());
	                       }
	                   }
	                   if(comp.getId()!=null){
	                       map.put("complainid",String.valueOf(comp.getId()));
	                       ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(uid+"", comp.getId()+"");
	                       if(passKeyEr.isSuccess()){
	                       	map.put("passKey2", passKeyEr.getResult());
	                       }
	                   }
	                   if(comp.getStatus()!=null&&comp.getStatus()==1){
	                       map.put("staceName","退款已仲裁");
	                       map.put("stace","1");
	                   }else if(comp.getStatus()!=null&&comp.getStatus()==2){
	                       map.put("staceName","已取消");
	                       map.put("stace","2");
	                   }else{
	                       map.put("staceName","待客服仲裁");
	                       map.put("stace","0");
	                   }
	                   if(comp.getComplainType()!=null&&"1".equals(comp.getComplainType())){
	                       map.put("sellerOrBuyer","1");
	                       map.put("tousufang","买家投诉");
	                   }else if(comp.getComplainType()!=null&&"2".equals(comp.getComplainType())){
	                       map.put("sellerOrBuyer","2");
	                       map.put("tousufang","店铺投诉");
	                   }else{
	                       map.put("sellerOrBuyer","3");
	                       map.put("tousufang","无法判定投诉方");
	                   }
	                   if(comp.getRefundId()!=null){
	                       map.put("list",goodsDetail.get(String.valueOf(comp.getRefundId())));
	                   }
	                   listMap.add(map);
	               }
	           }
	           pager.setTotalCount(dataGrid.getTotal().intValue());
	       }
	       pager.setRecords(listMap);
	       model.addAttribute("pageNo",new Integer(page));
	       model.addAttribute("pager",pager);
	       model.addAttribute("type", type);
	       return url;
	   }
		
	   /**
	    * 查询待审核订单
	    * @param tradeOrdersQueryInDTO
	    * @param approve
	    * @param page
	    * @param request
	    * @param model
	    * @return
	    */
	   @RequestMapping(value="/queryApprove")
		public String query(TradeOrdersQueryInDTO tradeOrdersQueryInDTO, String approve,Integer page, HttpServletRequest request, Model model) {
			String status1=request.getParameter("approveStatus");
			String status = status1;
			if("0".equals(status1)){
				status="";
			}
			String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
			if(StringUtils.isNotEmpty(uid) && StringUtils.isNumeric(uid)){
				if("loadApprove".equals(approve)){
					tradeOrdersQueryInDTO.setAuditorId(Long.parseLong(uid));
					tradeOrdersQueryInDTO.setApproveStatus("0");//待审核状态订单
					tradeOrdersQueryInDTO.setNotApproved(false);
					if(StringUtils.isEmpty(status)){//如果审核状态为空的话，则为已提交页签
						tradeOrdersQueryInDTO.setAuditorId(null);
						tradeOrdersQueryInDTO.setApproveStatus(null);
						tradeOrdersQueryInDTO.setBuyerId(Long.parseLong(uid));
					}
				}else{
					tradeOrdersQueryInDTO.setNotApproved(true);
					tradeOrdersQueryInDTO.setBuyerId(Long.valueOf(uid));
				}
	           model.addAttribute("userId", uid);
			}else{
				return "/user/login";
	       }
	
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
			LOG.info("tradeOrdersQueryInDTO======="+JSON.toJSONString(tradeOrdersQueryInDTO));
			ExecuteResult<DataGrid<TradeOrdersDTO>> executeResult = tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO, pager);
			//LOG.info("executeResult==="+JSON.toJSONString(executeResult) );
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
	                   //获取客服站点
	                   String stationId = StationUtil.getStationIdByShopId(tradeOrdersDTO.getShopId());
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
			model.addAttribute("status","0".equals(status1) ? null : status1);
			model.addAttribute("totalItem", rs_pager.getRecords().size());
			
			TradeOrdersQueryInDTO inDTO = new TradeOrdersQueryInDTO();
			if(StringUtils.isNotEmpty(uid) && StringUtils.isNumeric(uid)){
				inDTO.setBuyerId(Long.valueOf(uid));
			}
			
			//买家标记/订单删除(1:无,2:是)
			inDTO.setUserType(1);
			inDTO.setDeleted(1);
			LOG.info("inDTO======="+JSON.toJSONString(inDTO));
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
			
			//LOG.info("tradeOrdersQueryInDTO==="+JSON.toJSONString(tradeOrdersQueryInDTO) );
			//LOG.info("pager==="+JSON.toJSONString(rs_pager));
			//LOG.info("jsonArray==="+JSON.toJSONString(jsonArray));
			//LOG.info("pageState==="+JSON.toJSONString(pageState));
			//获取登录用户
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO user = this.userExportService.queryUserById(userId);
			model.addAttribute("isHavePayPassword", user.getIsHavePayPassword());
	//		if("loadApprove".equals(approve)){
	//			return "/order/order_approved_buyer";
	//		}else{
	//			return "/order/order_buyer";
	//		}
			if(StringUtils.isNotEmpty(status1)){
	//			if("0".equals(status)){
	//				status="";
	//			}
	//			model.addAttribute("status",status);
				return "/order/order_approve_buyer_detail";
			}else{
				return "/order/order_approve_buyer";
			}
		}
	   
	   
	   private List<Long> getListShopId(String shopName){
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
	    * <p>Discription:[审核通过]</p>
	    * Created on 2015-8-25
	    * @param request
	    * @return
	    * @author:[王鹏]
	    */
	   @ResponseBody
	   @RequestMapping(value="approveSubmit")
	   public void modifyStatus(HttpServletRequest request,PrintWriter writer){
	   	 Json json = new Json();
	   	 try{
	   		 String orderId=request.getParameter("orderId");
	       	 String status=request.getParameter("status");
	       	 String parentId=request.getParameter("parentId");
	       	 TradeOrdersDTO dto=new TradeOrdersDTO();
	       	 dto.setApproveStatus(status);
	       	 dto.setOrderId(orderId);
	       	 dto.setParentOrderId(parentId);
	       	 this.tradeApprovedOrdersExportService.approveSubmit(dto);
	       	 json.setMsg("审核通过");
	       	 json.setSuccess(true);
	   	 }catch(Exception e){
	   		 LOG.error(e.getMessage());
	         json.setMsg("系统出现意外错误，请联系管理员");
	         json.setSuccess(false);
	   	 }
	   	 writer.write(JSON.toJSONString(json));
	//   	return json;
	   }
	   
	   
	   /**
	    * 
	    * <p>Discription:[审核驳回]</p>
	    * Created on 2015-8-25
	    * @param request
	    * @return
	    * @author:[王鹏]
	    */
	   @RequestMapping(value="approveReject")
	   public void approveReject(HttpServletRequest request,PrintWriter writer){
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
	   		 LOG.error(e.getMessage());
	         json.setMsg("系统出现意外错误，请联系管理员");
	         json.setSuccess(false);
	   	 }
	   	 writer.write(JSON.toJSONString(json));
	   }
	   
	   
	   @RequestMapping("/orderApproveSubmit")
	//	@ResponseBody
		public String orderApproveSubmit(HttpServletRequest request,TradeOrdersDTO dto,Model model,String needApprove,PrintWriter writer){
			// 0询价订单 1协议订单 2正常
			String orderType = request.getParameter("orderType");
			String contractNo = request.getParameter("contractNo");
	//		boolean access = true;
	//		if (!"0".equals(orderType) 
	//				&& !"1".equals(orderType)
	//				&& !"2".equals(orderType)) {
	//			access = false;
	//		}else if ("0".equals(orderType) || "1".equals(orderType)) {
	//			if (StringUtils.isBlank(contractNo)) {
	//				access = false;
	//			}
	//		}
	//		if (!access) {
	//			ExecuteResult<String> executeResult = new ExecuteResult<String>();
	//			executeResult.addErrorMessage("参数错误");
	//			model.addAttribute("executeResult", executeResult);
	//			return "/order/pay_approve_view";
	//		}
			
			String paymentMethod = request.getParameter("paymentMethod");
			String couponId = request.getParameter("couponId");
			dto.setPaymentType(Integer.valueOf(paymentMethod));
			
			String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
			String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
			dto.setBuyerId(Long.valueOf(uid));
			dto.setNotApproved(false);//订单需要审核
			dto.setApproveStatus("0");//待审核状态
			Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
			Map<String, Object> map = this.orderWxService.subimtOrder(ctoken, uid, dto,contractNo,needApprove,freightDeliveryType,null,couponId);
			ExecuteResult<String> result = (ExecuteResult<String>) map.get("er");
			LOG.debug("===========>"+JSON.toJSONString(result));
			// 将结果放入request中，仅供站内信发送拦截器使用
			request.setAttribute("result", result);
			model.addAttribute("executeResult", result);
			Map<String, Object> mapRes = new HashMap<String, Object>();
			mapRes.put("result", true);
			mapRes.put("orderId", result.getResult());
			return "/order/pay_approve_view";
	//		writer.write(JSON.toJSONString(mapRes));
		}
	   
		@RequestMapping(value="/testAjax")
		public String testAjax(){
			return "order/testAjax";
		}
		@RequestMapping(value="/submitPersion")
		@ResponseBody
		public String submitPersion(Persion persion){
			LOG.info(persion);
			persion.setName("战三");
			return JSON.toJSONString(persion);
		}
		@RequestMapping("/testOrder")
		public String order(){
			return "/order/order";
		}
		
		/**
		 *
		 * <p>Discription:[买家：选择退款商品，申请退款]</p>
		 * Created on 2015年11月25日
		 * @param request
		 * @param model
		 * @return
		 * @author:[李伟龙]
		 */
		@RequestMapping("/refundAgreementWx")
		public String refundAgreementWx(HttpServletRequest request, Model model) {
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			List<String> errorMessages = new ArrayList<String>();
			String orderId = request.getParameter("orderId");
			String skuId=request.getParameter("skuId");
			String passKey = request.getParameter("passKey");
			if(StringUtils.isEmpty(orderId)){
				errorMessages.add("订单id不正确");
				result.setErrorMessages(errorMessages);
				return "order/refundItem";
			}
			if(StringUtils.isEmpty(skuId)&& !StringUtils.isNumeric(skuId)){
				errorMessages.add("skuid不正确");
				result.setErrorMessages(errorMessages);
				return "order/refundItem";
			}
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
					//退货商品的总金额
					BigDecimal refundPriceTotal = new BigDecimal(0);
					JSONArray jsonArray_item = new JSONArray();
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
					for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
						if(tradeOrderItemsDTO.getSkuId().longValue()==Long.parseLong(skuId)){
							JSONObject jsonObject_item = new JSONObject();
							jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
							jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
							jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
							BigDecimal payPriceTotal = tradeOrderItemsDTO.getPayPriceTotal();
							jsonObject_item.put("payPriceTotal", payPriceTotal);
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
					    }
					}
					jsonObject.put("items", jsonArray_item);
	
					model.addAttribute("refundPriceTotal", refundPriceTotal);//退货商品的总金额
					model.addAttribute("jsonObject", jsonObject);
				}
			}
			//查询退款单id
			TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
			TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
			String returnGoodId=request.getParameter("returnGoodId");
			if(StringUtils.isNotEmpty(returnGoodId)){
				tradeReturnDto.setDeletedFlag("1");
			}
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
			
			//修改退款信息
			if(StringUtils.isNotEmpty(returnGoodId)){
				ExecuteResult<TradeReturnInfoDto> modidyresult = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnGoodId);
				List<TradeReturnGoodsDetailDTO> lists=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(returnGoodId);
				if(modidyresult.getResult() != null ){
					TradeReturnGoodsDTO returnGoodDto = modidyresult.getResult().getTradeReturnDto();
					model.addAttribute("remark",returnGoodDto.getRemark());
					model.addAttribute("returnGoodId",returnGoodId);
					model.addAttribute("refundGoods",returnGoodDto.getRefundGoods());
					model.addAttribute("returnResult",returnGoodDto.getReturnResult());
					model.addAttribute("picList",returnGoodDto.getPicDTOList());
					model.addAttribute("returnCount",lists.get(0).getRerurnCount());
				}
			}else{
				model.addAttribute("remark","");
				model.addAttribute("returnGoodId","");
				model.addAttribute("refundGoods","");
				model.addAttribute("returnResult","");
				model.addAttribute("picList","");
			}
			model.addAttribute("passKey",passKey);
			model.addAttribute("ftpServerDir",SysProperties.getProperty("ftp_server_dir"));
			return "order/refundItem";
		}
		
		
		/**
		 *
		 * <p>Discription:[申请退款查看]</p>
		 * Created on 2015年11月25日
		 * @param request
		 * @param model
		 * @return
		 * @author:[李伟龙]
		 */
		@RequestMapping("/refundAgreementSeeWx")
		public String refundAgreementSeeWx(HttpServletRequest request, Model model) {
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			List<String> errorMessages = new ArrayList<String>();
			String orderId = request.getParameter("orderId");
			String skuId=request.getParameter("skuId");
			String passKey = request.getParameter("passKey");
			if(StringUtils.isEmpty(orderId)){
				errorMessages.add("订单id不正确");
				result.setErrorMessages(errorMessages);
				return "order/refundItem";
			}
			if(StringUtils.isEmpty(skuId)&& !StringUtils.isNumeric(skuId)){
				errorMessages.add("skuid不正确");
				result.setErrorMessages(errorMessages);
				return "order/refundItem";
			}
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
					//退货商品的总金额
					BigDecimal refundPriceTotal = new BigDecimal(0);
					JSONArray jsonArray_item = new JSONArray();
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
					for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
						if(tradeOrderItemsDTO.getSkuId().longValue()==Long.parseLong(skuId)){
							JSONObject jsonObject_item = new JSONObject();
							jsonObject_item.put("num", tradeOrderItemsDTO.getNum());
							jsonObject_item.put("payPrice", tradeOrderItemsDTO.getPayPrice());
							jsonObject_item.put("skuId", tradeOrderItemsDTO.getSkuId());
							BigDecimal payPriceTotal = tradeOrderItemsDTO.getPayPriceTotal();
							jsonObject_item.put("payPriceTotal", payPriceTotal);
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
					    }
					}
					jsonObject.put("items", jsonArray_item);
	
					model.addAttribute("refundPriceTotal", refundPriceTotal);//退货商品的总金额
					model.addAttribute("jsonObject", jsonObject);
				}
			}
			String returnGoodId=request.getParameter("returnGoodId");
			//查询退款单id
			TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
			TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
			if(StringUtils.isNotEmpty(returnGoodId)){
				tradeReturnDto.setDeletedFlag("1");
			}
			tradeReturnDto.setOrderId(orderId);
			queryDto.setTradeReturnDto(tradeReturnDto);
			DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
			if(dg!=null&&dg.getTotal()>0){
				List<TradeReturnGoodsDTO> goodsDtos=dg.getRows();
				BigDecimal fundMoney=BigDecimal.ZERO;
				for(TradeReturnGoodsDTO goods:goodsDtos){
					BigDecimal money=BigDecimal.ZERO;
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
					model.addAttribute("totalMoney",money);
					model.addAttribute("num",num);
					model.addAttribute("refundFreight",refundFreight);//该商品已退运费
				}
				model.addAttribute("fundMoney",fundMoney);
				
			}
			//修改退款信息
			if(StringUtils.isNotEmpty(returnGoodId)){
				ExecuteResult<TradeReturnInfoDto> modidyresult = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnGoodId);
				List<TradeReturnGoodsDetailDTO> lists=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(returnGoodId);
				if(modidyresult.getResult() != null ){
					TradeReturnGoodsDTO returnGoodDto = modidyresult.getResult().getTradeReturnDto();
					model.addAttribute("remark",returnGoodDto.getRemark());
					model.addAttribute("returnGoodId",returnGoodId);
					model.addAttribute("refundGoods",returnGoodDto.getRefundGoods());
					model.addAttribute("returnResult",returnGoodDto.getReturnResult());
					model.addAttribute("picList",returnGoodDto.getPicDTOList());
					model.addAttribute("returnCount", lists.get(0).getRerurnCount());
				}
			}else{
				model.addAttribute("remark","");
				model.addAttribute("returnGoodId","");
				model.addAttribute("refundGoods","");
				model.addAttribute("returnResult","");
				model.addAttribute("picList","");
			}
			model.addAttribute("passKey",passKey);
			model.addAttribute("ftpServerDir",SysProperties.getProperty("ftp_server_dir"));
			return "order/refundItemSee";
		}
		
		
		/**
		 *
		 * <p>Discription:[买家：退款协议提交，处理页面]</p>
		 * Created on 2015年11月27日
		 * @param request
		 * @param model
		 * @return
		 * @author:[李伟龙]
		 */
		@RequestMapping(value="/refundAgreementSubmitWx")
		@ResponseBody
		public String refundAgreementSubmitWx(@RequestParam(value="orderId") String orderId,
				@RequestParam(value="returnResult") String returnResult,
				@RequestParam(value="remark") String remark,
				@RequestParam(value="picUrl") String picUrl,
				@RequestParam(value="skuId") String skuId,
				@RequestParam(value="amountNum") String amountNum,
				@RequestParam(value="refundMoney") String refundMoney,
				@RequestParam(value="haveRefundMoney") String haveRefundMoney,
				@CookieValue(value=Constants.USER_ID) String uid,
				HttpServletRequest request,HttpServletResponse response, Model model) {
			String totalfundMoney=request.getParameter("totalfundMoney");//单个商品退款总金额
			String json = "";
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			request.setAttribute("result",result);
			List<String> errorMessages = new ArrayList<String>();
			if(StringUtils.isEmpty(orderId) /*&& !StringUtils.isNumeric(orderId)*/){
				errorMessages.add("订单id不正确");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			if(StringUtils.isEmpty(skuId)&&!StringUtils.isNumeric(skuId)){
				errorMessages.add("skuId不正确");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			if(StringUtils.isEmpty(returnResult)){
				errorMessages.add("退款原因不能为空");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			if(StringUtils.isEmpty(remark)){
				errorMessages.add("退款说明不能为空");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			if(StringUtils.isEmpty(amountNum)&&!StringUtils.isNumeric(amountNum)){
				errorMessages.add("退款数量不能为空,必须是数字");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			if(StringUtils.isEmpty(refundMoney)&&!StringUtils.isNumeric(refundMoney)){
				errorMessages.add("退款金额不能为空,必须是数字");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			String refundFreight = request.getParameter("refundFreight");//退款运费金额
			BigDecimal totalMoney=BigDecimal.ZERO;//商品支付总金额
			String refundNum=request.getParameter("refundNum");//已经退款数量
			BigDecimal payTotal=BigDecimal.ZERO;//订单明细表中商品支付总金额
			int totalNum=0;//商品总数量
			BigDecimal refundMpney=BigDecimal.ZERO;//后台计算商品最多退款金额
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
						if(dto.getSkuId()==Long.parseLong(skuId)){
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
				   if(money.doubleValue()>(tradeOrdersDTO.getPaymentPrice().doubleValue())){
					    errorMessages.add("订单退款总金额不允许超过支付总金额！");
						result.setErrorMessages(errorMessages);
						json = JSON.toJSONString(result);
						return json;
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
							json = JSON.toJSONString(result);
							return json;
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
					tradeReturnDto.setOrderPrice(tradeOrdersDTO.getPaymentPrice());
					tradeReturnDto.setReturnResult(returnResult);//  退货原因
					tradeReturnDto.setRemark(remark);//  问题描述
					if(StringUtils.isNotEmpty(refundFreight)){
						tradeReturnDto.setRefundFreight(new BigDecimal(refundFreight));
					}
					
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
					insertDto.setTradeReturnDto(tradeReturnDto);
	
					//构造订单下退货商品的信息
					List<TradeReturnGoodsDetailDTO> tradeReturnGoodsDetailList = new ArrayList<TradeReturnGoodsDetailDTO>();
	
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = tradeOrdersDTO.getItems();
					for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
						if(tradeOrderItemsDTO.getSkuId().longValue()==Long.parseLong(skuId)){
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
								json = JSON.toJSONString(result);
								return json;
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
					  }
					}
					insertDto.setTradeReturnGoodsDetailList(tradeReturnGoodsDetailList);
				}
				result = tradeReturnExportService.createTradeReturn(insertDto, TradeReturnStatusEnum.AUTH);
				request.setAttribute("insertDto",insertDto);
				TradeReturnInfoQueryDto queryDto = new TradeReturnInfoQueryDto();
				TradeReturnGoodsDTO tradeReturnDto = new TradeReturnGoodsDTO();
				tradeReturnDto.setOrderId(orderId);
				queryDto.setTradeReturnDto(tradeReturnDto);
				DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(new Pager<TradeReturnGoodsDTO>(), queryDto);
				String returnIds = "";
				if(dg!=null&&dg.getTotal()>0){
					for(TradeReturnGoodsDTO dto:dg.getRows()){
						List<TradeReturnGoodsDetailDTO> deDto=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(String.valueOf(dto.getId()));
						if(dto.getAfterService()!=null&&deDto.get(0).getSkuId().longValue()==Long.parseLong(skuId)
							&&(4==dto.getAfterService().intValue()||5==dto.getAfterService().intValue())){//申請退款成功后將之前已關閉的或部分商品已关闭退货单置为已删除状态
							dto.setDeletedFlag("1");
							tradeReturnExportService.updateTradeReturnGoods(dto);
							returnIds += dto.getId()+";";
						}
						for(TradeReturnGoodsDetailDTO tradedto : deDto){
							if(skuId.equals(tradedto.getSkuId().toString()) && !returnIds.contains(tradedto.getReturnGoodsId().toString()) ){
								result.setResultMessage(String.valueOf(dto.getId()));
							}
						}
					}
				}
			}
			json = JSON.toJSONString(result);
			return json;
		}
		
		/**
		 *
		 * <p>Discription:[买家： 卖家不同意退款，买家重新修改退款原因，然后提交]</p>
		 * Created on 2015年11月27日
		 * @param request
		 * @param model
		 * @return
		 * @author:[李伟龙]
		 */
		@RequestMapping(value="/updateRefundAgreementWx")
		@ResponseBody
		public String updateRefundAgreementWx(HttpServletRequest request, Model model) {
			ExecuteResult<TradeReturnGoodsDTO> result = new ExecuteResult<TradeReturnGoodsDTO>();
			String json = "";
			List<String> errorMessages = new ArrayList<String>();
			String returnId = request.getParameter("returnId");
			if(StringUtils.isEmpty(returnId) && StringUtils.isNumeric(returnId)){
				errorMessages.add("退款id不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			String returnResult = request.getParameter("returnResult");
			if(StringUtils.isEmpty(returnResult)){
				errorMessages.add("退款原因不能为空");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			String remark = request.getParameter("remark");
			if(StringUtils.isEmpty(remark)){
				errorMessages.add("退款说明不能为空");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			String refundGoods = request.getParameter("refundGoods");//  退款货品总金额
			if(StringUtils.isEmpty(refundGoods) && !StringUtils.isNumeric(refundGoods)){
				errorMessages.add("退款金额不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			String num =request.getParameter("num");//退款数量
			if(StringUtils.isEmpty(num) && !StringUtils.isNumeric(num)){
				errorMessages.add("退款数量不能为空，必须是数字");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			String orderId = request.getParameter("orderId");
			if(StringUtils.isEmpty(orderId)){
				errorMessages.add("订单id不能为空");
				result.setErrorMessages(errorMessages);
				json = JSON.toJSONString(result);
				return json;
			}
			List<TradeReturnGoodsDetailDTO> detailDtos=tradeReturnExportService.getTradeReturnGoodsDetailReturnId(returnId);
			String haveRefundMoney=request.getParameter("haveRefundMoney");//已退款金额
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
						json = JSON.toJSONString(result);
						return json;
					}
				}else{
					if(money.doubleValue()>(tradeOrdersDTO.getPaymentPrice().doubleValue())){
					    errorMessages.add("订单退款总金额不允许超过支付总金额！");
						result.setErrorMessages(errorMessages);
						json = JSON.toJSONString(result);
						return json;
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
						json = JSON.toJSONString(result);
						return json;
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
			result.setResultMessage(returnId);
			json = JSON.toJSONString(result);
			return json;
		}
		
		/**
		 * 
		 * <p>Description: [获取运费模版对应的运送方式]</p>
		 * Created on 2015年11月3日
		 * @param request
		 * @return
		 * @author:[宋文斌]
		 */
		private Map<Long, Integer> getFreightDeliveryType(HttpServletRequest request){
			Map<Long, Integer> freightDeliveryType = new HashMap<Long, Integer>();
			String[] freightDeliveryTypes = request.getParameterValues("freightDeliveryType");
			if(freightDeliveryTypes != null && freightDeliveryTypes.length > 0){
				for (int i = 0; i < freightDeliveryTypes.length; i++) {
					String[] freightDeliveryTypeStr = freightDeliveryTypes[i].split("-");
					if(StringUtils.isNotBlank(freightDeliveryTypeStr[0]) && StringUtils.isNotBlank(freightDeliveryTypeStr[1])
							&& StringUtils.isNumeric(freightDeliveryTypeStr[0]) && StringUtils.isNumeric(freightDeliveryTypeStr[1])){
						freightDeliveryType.put(Long.parseLong(freightDeliveryTypeStr[0]), Integer.parseInt(freightDeliveryTypeStr[1]));
					}
				}
			}
			return freightDeliveryType;
		}
		/**
		 *
		 * <p>
		 * Discription: 订单核对
		 * </p>
		 * Created on 2015年3月23日
		 *
		 * @return
		 * @author:[Goma 郭茂茂]
		 */
		@RequestMapping("/toOrderDetail")
		public ModelAndView toOrderDetail(HttpServletRequest request) {
			ModelAndView mav = new ModelAndView();
			String promoCodeId=request.getParameter("promoCode");//vip卡号
			String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
			// 0询价订单 1协议订单 2正常
	//		Integer orderType = Integer.parseInt(request.getParameter("orderType"));
	//		String contractNo = request.getParameter("contractNo");
	//		mav.addObject("orderType", orderType);
	//		mav.addObject("contractNo", contractNo);
			LOG.debug("=====>" + uid);
			String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
			String regionid = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
	
			// 收货地址变更
			String ckAddrId = request.getParameter("address");
			LOG.debug("CHECK ADDRESS:" + ckAddrId);
			JSONObject json = this.getAddress(uid, ckAddrId);
			JSONObject ckAddress = json.getJSONObject("defAddress");
			mav.addObject("addresses", json.get("addresses"));
			mav.addObject("defAddress", ckAddress);
			LOG.debug("DEF ADDRESS:" + JSON.toJSONString(ckAddress));
	
			if (ckAddress != null){
				regionid = ckAddress.getString("provicecode");
			}
			this.orderWxService.changeRegion(ctoken, uid, regionid);
	
			// 发票抬头设置
			// mav.addObject("invoice", request.getParameter("invoice"));
			// String invoiceTitle = request.getParameter("invoiceTitle");
			// if( invoiceTitle == null || "".equals(invoiceTitle) ){
			// ExecuteResult<UserCompanyDTO> erCompany =
			// this.companyService.findUserCompanyByUId(Long.valueOf(uid));
			// if( erCompany.getResult() != null )
			// invoiceTitle = erCompany.getResult().getInvoiceInformation();
			// }
			// LOG.debug("INVOICE TITLE:"+invoiceTitle);
			// mav.addObject("invoiceTitle", invoiceTitle);
	
			// 查询用户最近使用的发票
			mav.addObject("invoiceDTO", invoiceExportService.queryRecentInvoice(Long.valueOf(uid)).getResult());
			// 付款方式设置
			mav.addObject("shipmentType", request.getParameter("shipmentType"));
			mav.addObject("payPeriod", request.getParameter("payPeriod"));
	
	
			// 企业支付或个人支付（1个人 2企业）
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			String paymentMethod_ = request.getParameter("paymentMethod");
			Integer paymentMethod = null;
			if(StringUtils.isNotBlank(paymentMethod_)){
				paymentMethod = Integer.parseInt(paymentMethod_);
			} else{
				if(user.getUsertype() == UserType.ORDINARY.getCode()){
					paymentMethod = 1;
				} else if(user.getUsertype() > UserType.ORDINARY.getCode()){
					paymentMethod = 2;
				}
			}
			mav.addObject("userstatus", user.getUserstatus());
			Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
			ShopCartDTO myCart=null;
			// 优惠券编号
	        String couponId = request.getParameter("couponId");
		    myCart = this.orderWxService.getMyCart(ctoken, uid, false, freightDeliveryType,null,couponId, paymentMethod, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
		    if(StringUtils.isNotBlank(couponId)){
		    	mav.addObject("couponId", couponId);
		    }
			mav.addObject("myCart", myCart);
			mav.addObject("freightDeliveryType", freightDeliveryType);
			mav.addObject("paymentMethod", paymentMethod);
			// 可使用的优惠券
			List<CouponsDTO> avaliableCoupons = new ArrayList<CouponsDTO>();
			// 不可使用的优惠券
			List<CouponsDTO> unavaliableCoupons = new ArrayList<CouponsDTO>();
			// 查询用户领取的优惠券
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			couponUserDTO.setUserId(Long.valueOf(uid));
			couponUserDTO.setDeleted(0);// 未删除
			couponUserDTO.setUserCouponType(0);// 未使用
			ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, null);
			if (couponUserResult.isSuccess() 
					&& couponUserResult.getResult() != null 
					&& couponUserResult.getResult().getRows() != null
					&& couponUserResult.getResult().getRows().size() > 0) {
				// 用户领取的优惠券
				List<CouponUserDTO> couponUserDTOs = couponUserResult.getResult().getRows();
				for(CouponUserDTO dto : couponUserDTOs){
					ExecuteResult<ShopCartDTO> validateResult = shopCartCouponService.validateCoupon(Long.parseLong(uid), dto.getCouponId(), paymentMethod, myCart, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
					ExecuteResult<CouponsDTO> couponResult = couponsExportService.queryById(dto.getCouponId());
					if(couponResult.isSuccess() && couponResult.getResult() != null){
						if (couponResult.getResult().getShopId() == null || couponResult.getResult().getShopId() == 0) {
							couponResult.getResult().setPromulgator("平台");
						} else {
							ExecuteResult<ShopDTO> shopResult = shopService.findShopInfoById(couponResult.getResult().getShopId());
							couponResult.getResult().setPromulgator(shopResult.getResult().getShopName());
						}
						if (couponResult.getResult().getCouponType() == 1) {
							couponResult.getResult().setDescr(
									"满" + moneyToSting(couponResult.getResult().getMeetPrice().toString()) + "元，减"
											+ moneyToSting(couponResult.getResult().getCouponAmount().toString()) + "元");
						} else if (couponResult.getResult().getCouponType() == 2) {
							couponResult.getResult().setDescr(
									moneyToSting(couponResult.getResult().getCouponAmount().toString()) + "折，限额"
											+ moneyToSting(couponResult.getResult().getCouponMax().toString()) + "元");
						} else if (couponResult.getResult().getCouponType() == 3) {
							couponResult.getResult().setDescr(
									"直降" + moneyToSting(couponResult.getResult().getCouponAmount().toString()) + "元");
						}
						if (validateResult.isSuccess()) {
							avaliableCoupons.add(couponResult.getResult());
						} else {
							// 不可用优惠券中不显示已过期的
							Date now = new Date();
							if (now.compareTo(couponResult.getResult().getCouponEndTime()) <= 0) {
								unavaliableCoupons.add(couponResult.getResult());
							}
						}
					}
				}
			}
			mav.addObject("avaliableCoupons", avaliableCoupons);// 可使用的优惠券
			mav.addObject("unavaliableCoupons", unavaliableCoupons);// 不可使用的优惠券
			mav.setViewName("/order/order_cart_view");
			return mav;
		}
		public String moneyToSting(String str) {
			while(str.endsWith("0")){
				str = str.substring(0, str.length()-1);
			}
			if(str.endsWith(".")){
				str = str.substring(0, str.length()-1);
			}
			return str;
		}
	}