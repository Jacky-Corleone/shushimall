	package com.camelot.mall.coupons;
	
	import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.orderWx.OrderWxService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartCouponService;
import com.camelot.report.service.TradeReportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
	
	/**
	 * <p> Description: [优惠券] </p>
	 * Created on 2015年12月2日
	 * @author <a href="mailto: liweilong@camelotchina.com">李伟龙</a>
	 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	 */
	@Controller
	@RequestMapping("/coupons")
	public class CouponsController {
	
	    protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	    @Resource
	    private UserExportService userExportService;
	    @Resource
	    private ShopExportService shopService;
	    @Resource
	    private CouponsExportService couponsExportService;
	    @Resource
	    private ShopExportService shopExportService;
	    @Resource
	    private SearchItemExportService searchItemExportService;
	    @Resource
	    private ItemExportService itemService;
	    @Resource
	    private ItemCategoryService itemCategoryService;
	    @Resource
	    private TradeReportService tradeReportService;
	    
		@Resource
		private ShopCartCouponService shopCartCouponService;
		
		@Resource
		private OrderWxService orderWxService;
		
		@Resource
		private UserExportService userService;
	    
	    @Resource
	    private RedisDB redisDB;
	
	    /**
	     * <p>Discription:[店铺或者平台优惠券展示页面] </p>
	     * Created on 2015年12月29日
	     * @param costAllocation 1-平台 2-店铺 优惠券类型3核定订单
	     * @param model
	     * @param request
	     * @return
	     * @author:[李伟龙]
	     */
	    @RequestMapping("index")
	    public String index(Model model, HttpServletRequest request,
		    @RequestParam(value = "costAllocation") Integer costAllocation,
		    @RequestParam(value = "type") Integer type,String paymentMethod_) {
		//执行查询方法action
		String actionUrl = "";
		String shopId = "";
		// 分页
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		// type 区分是查询我的优惠券还是领取优惠券 1 -店铺或者平台领取优惠券 2 -我的优惠券
		if (type == 1) {//查询可领取的优惠券
		    List<Integer> stateList = new ArrayList<Integer>();
		    // 只查询1-已经开始和0-未开始的优惠券
		    stateList.add(0);
		    stateList.add(1);
		    // 查询条件
		    CouponsDTO couponsDTO = new CouponsDTO();
		    // 优惠券状态list 0 1 可领取
		    couponsDTO.setStateList(stateList);
		    // 1-平台的优惠券 2-店铺优惠券
		    couponsDTO.setCostAllocation(costAllocation);
		    // 如果为店铺则获取到shopId
		    if (costAllocation == 2) {
			shopId = request.getParameter("shopId");
			couponsDTO.setShopId(Long.parseLong(shopId));
		    }
		    model.addAttribute("costAllocation", costAllocation);
		    // 只查询未发放用户的优惠券,首页优惠券领取
		    couponsDTO.setSendCouponType(1);
		    // platformId 优惠券使用平台(1:印刷家平台 pc,2:绿印平台,3:小管家 4:pc+小管家)
			List<Integer> platformIdList = new ArrayList<Integer>();
			platformIdList.add(3);
			platformIdList.add(4);
			couponsDTO.setPlatformIdList(platformIdList);
		    
		    //查询不过期的优惠券
			couponsDTO.setIsEffective("5");
		    ExecuteResult<DataGrid<CouponsDTO>> result = couponsExportService.queryCouponsList(couponsDTO, pager);
		    if (result.getResult() != null) {
			model.addAttribute("totalCount", result.getResult().getTotal().intValue());
		    } else {
			model.addAttribute("totalCount", 0);
		    }
		    actionUrl = "/coupons/showCanReceiveCoupons?type="+type+"&costAllocation="+costAllocation+"&shopId="+shopId;
		} else if (type == 2) {//查询我的优惠券
		    String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		    if (StringUtils.isBlank(uid)) {
			return "redirect:/user/login";
		    }
		    // 查询登录用户下的优惠券信息
		    CouponUserDTO couponUserDTO = new CouponUserDTO();
		    couponUserDTO.setUserId(Long.valueOf(uid));
		    // platformId 优惠券使用平台(1:印刷家平台,2:绿印平台,3:小管家4:pc+小管家)
		    List<Integer> platformIdList = new ArrayList<Integer>();
		    platformIdList.add(3);
		    platformIdList.add(4);
		    couponUserDTO.setPlatformIdList(platformIdList);
		    ExecuteResult<DataGrid<CouponUserDTO>> result = couponsExportService.queryCouponsUserList(couponUserDTO,
			    pager);
		    if (result.getResult() != null) {
			model.addAttribute("totalCount", result.getResult().getTotal().intValue());
		    } else {
			model.addAttribute("totalCount", 0);
		    }
		    actionUrl = "/coupons/buyerCoupons?type="+type;
		}else if (type == 3) {//查询核对订单可用的优惠劵
		    String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		    if (StringUtils.isBlank(uid)) {
			return "redirect:/user/login";
		    }
			ShopCartDTO myCart=null;
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
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
		    myCart = orderWxService.getMyCart(ctoken, uid, false, null,null,null, paymentMethod, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
	
		    List<CouponsDTO> avaliableCoupons = new ArrayList<CouponsDTO>();
		    
		    List<String> couponIdList=new ArrayList<String>();
		    //获取发票类型
		    String invoiceType=request.getParameter("invoiceType");
		    //获取发票标题
		    String invoiceTitle=request.getParameter("invoiceTitle");
		    //获取收货地址变更
		    String address=request.getParameter("address"); 
		    model.addAttribute("address", address);
		    model.addAttribute("invoiceTitle", invoiceTitle);
		    model.addAttribute("invoiceType", invoiceType);
			// 不可使用的优惠券
			List<CouponsDTO> unavaliableCoupons = new ArrayList<CouponsDTO>();
			// 查询用户领取的优惠券
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			couponUserDTO.setUserId(Long.valueOf(uid));
			couponUserDTO.setDeleted(0);// 未删除
			couponUserDTO.setUserCouponType(0);// 未使用
			ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, pager);
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
							couponIdList.add(dto.getCouponId());
						}
					} else{
						if(couponResult.isSuccess() && couponResult.getResult() != null){
							unavaliableCoupons.add(couponResult.getResult());
							
						}
					}
				}
				
			}
		    if (couponUserResult.getResult() != null) {
			model.addAttribute("totalCount", avaliableCoupons.size());
			
		    } else {
			model.addAttribute("totalCount", 0);
		    }
			// 用于订单核对页使用优惠券时选中优惠券
			model.addAttribute("couponId", request.getParameter("couponId"));
		    actionUrl = "/coupons/buyerCouponsOrder?type="+type+"&paymentMethod="+paymentMethod;

		}
		model.addAttribute("actionUrl", actionUrl);
		model.addAttribute("type", type);
	
		return "/coupons/index";
	    }
	
	    /**
	     * <p>
	     * Description: [获取平台或者店铺优惠券]
	     * </p>
	     * Created on 2015年12月29日
	     * @param costAllocation 1-平台 2-店铺 优惠券类型
	     * @param model
	     * @param request
	     * @return 返回优惠券列表页面
	     * @author:[李伟龙]
	     */
	    @RequestMapping(value = "/showCanReceiveCoupons")
	    public String showCanReceiveCoupons(Model model, HttpServletRequest request,
		    @RequestParam(value = "costAllocation") Integer costAllocation,
		    @RequestParam(value = "type") Integer type) {
		// 返回结果
		List<JSONObject> couponsDTOs = new ArrayList<JSONObject>();
		// 分页
		String pageNoStr = request.getParameter("pageNo");
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		int pageNo = 1;
		if (pageNoStr != null && !"".equals(pageNoStr)) {
		    pageNo = Integer.parseInt(pageNoStr);
		}
		pager.setPage(pageNo);
		pager.setRows(5);
		// 只查询已经开始和未开始的优惠券
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(0);
		stateList.add(1);
		// 查询条件
		CouponsDTO couponsDTO = new CouponsDTO();
		// 优惠券状态list 0 1 可领取
		couponsDTO.setStateList(stateList);
		// 1-平台的优惠券 2-店铺优惠券
		couponsDTO.setCostAllocation(costAllocation);
		// 如果为店铺则获取到shopId
		if (costAllocation == 2) {
		    String shopId = request.getParameter("shopId");
		    couponsDTO.setShopId(Long.parseLong(shopId));
		}
		// 只查询未发放用户的优惠券,首页优惠券领取
		couponsDTO.setSendCouponType(1);
		// platformId 优惠券使用平台(1:印刷家平台 pc,2:绿印平台,3:小管家 4:pc+小管家)
		List<Integer> platformIdList = new ArrayList<Integer>();
		platformIdList.add(3);
		platformIdList.add(4);
		couponsDTO.setPlatformIdList(platformIdList);
		
		//查询不过期的优惠券
		couponsDTO.setIsEffective("5");
		ExecuteResult<DataGrid<CouponsDTO>> result = couponsExportService.queryCouponsList(couponsDTO, pager);
		DataGrid<CouponsDTO> dataGrid = new DataGrid<CouponsDTO>();
		if (result.isSuccess() && result.getResult() != null && result.getResult().getRows() != null
			&& result.getResult().getRows().size() > 0) {
		    dataGrid = result.getResult();
		    for (CouponsDTO dto : result.getResult().getRows()) {
			JSONObject coupons = this.convertToJSONObject(dto, request);
			couponsDTOs.add(coupons);
		    }
		}
	
		pager.setTotalCount(dataGrid.getTotal().intValue());
		model.addAttribute("couponsDTOs", couponsDTOs);
		model.addAttribute("type", type);
		return "/coupons/receiveCoupons";
	    }
	    
	    /**
	     * <p> Discription:[查询我的优惠券] </p>
	     * Created on 2015年12月8日
	     * @param page
	     * @param model
	     * @param request
	     * @return
	     * @author:[李伟龙]
	     */
	    @RequestMapping(value = "/buyerCoupons")
	    public String buyerCoupons(Model model, HttpServletRequest request,
		    		@RequestParam(value = "type") Integer type) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		if (StringUtils.isBlank(uid)) {
		    return "redirect:/user/login";
		}
		//返回结果
		List<JSONObject> couponsDTOs = new ArrayList<JSONObject>();
		// 分页
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		String pageNoStr = request.getParameter("pageNo");
		int pageNo = 1;
		if (pageNoStr != null && !"".equals(pageNoStr)) {
		    pageNo = Integer.parseInt(pageNoStr);
		}
		pager.setPage(pageNo);
		pager.setRows(5);
		// 查询登录用户下的优惠券信息
		CouponUserDTO couponUserDTO = new CouponUserDTO();
		couponUserDTO.setUserId(Long.valueOf(uid));
		// platformId 优惠券使用平台(1:印刷家平台,2:绿印平台,3:小管家 4:pc+小管家)
		List<Integer> platformIdList = new ArrayList<Integer>();
		platformIdList.add(3);
		platformIdList.add(4);
		couponUserDTO.setPlatformIdList(platformIdList);
		ExecuteResult<DataGrid<CouponUserDTO>> result = couponsExportService.queryCouponsUserList(couponUserDTO, pager);
		// 优惠券主表变量
		CouponsDTO couponsDTO = new CouponsDTO();
		if (null != result.getResult()) {
		    List<CouponUserDTO> list = result.getResult().getRows();
		    if (null != list && !list.isEmpty()) {
			for (CouponUserDTO cud : list) {
			    ExecuteResult<CouponsDTO> couponsResult = couponsExportService.queryById(cud.getCouponId());
			    couponsDTO = couponsResult.getResult();
			    JSONObject coupons = this.convertToJSONObject(couponsDTO, request);
			    couponsDTOs.add(coupons);
			}
		    }
		}
		pager.setTotalCount(result.getResult().getTotal().intValue());
		model.addAttribute("couponsDTOs", couponsDTOs);
		model.addAttribute("type", type);
		return "/coupons/receiveCoupons";
	    }
	    
	    
	    /**
	     * <p> Discription:[查询核对订单优惠券] </p>
	     * Created on 2015年12月30日15:27:30
	     * @param page
	     * @param model
	     * @param request
	     * @return
	     * @author:[訾瀚民]
	     */
	    @RequestMapping(value = "/buyerCouponsOrder")
	    public String buyerCouponsOrder(Model model, HttpServletRequest request,
		    		@RequestParam(value = "type") Integer type,Integer paymentMethod,String couponId) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		if (StringUtils.isBlank(uid)) {
		    return "redirect:/user/login";
		}
	
		// 分页
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		String pageNoStr = request.getParameter("pageNo");
		int pageNo = 1;
		if (pageNoStr != null && !"".equals(pageNoStr)) {
		    pageNo = Integer.parseInt(pageNoStr);
		}
		pager.setPage(pageNo);
		pager.setRows(10000);
		List<CouponsDTO> avaliableCoupons = new ArrayList<CouponsDTO>();
		// 不可使用的优惠券
		List<CouponsDTO> unavaliableCoupons = new ArrayList<CouponsDTO>();
		// 查询用户领取的优惠券
		CouponUserDTO couponUserDTO = new CouponUserDTO();
		couponUserDTO.setUserId(Long.valueOf(uid));
		couponUserDTO.setDeleted(0);// 未删除
		couponUserDTO.setUserCouponType(0);// 未使用
		ShopCartDTO myCart=null;
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
	    myCart = orderWxService.getMyCart(ctoken, uid, false, null,null,null, paymentMethod, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
	
		ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, pager);
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
		model.addAttribute("couponsDTOs", avaliableCoupons);
		model.addAttribute("type", type);
		model.addAttribute("couponId", couponId);
		
		return "/coupons/orderCoupons";
	    }
	    
	    
	    
	    
	
	    /**
	     * <p> Description: [领取优惠券] </p>
	     * Created on 2015年12月7日
	     * @param couponId 优惠券编号
	     * @param request
	     * @return
	     * @author:[宋文斌]
	     */
	    @RequestMapping("getCoupons")
	    @ResponseBody
	    public String getCoupons(String couponId, HttpServletRequest request) {
		// 返回结果
		JSONObject jores = new JSONObject();
		// 获取当前登录用户信息
		String token = LoginToken.getLoginToken(request);
		if (StringUtils.isBlank(token)) {
		    jores.put("resultMessage", "您还没有登录，请先登录");
		    return jores.toJSONString();
		}
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (null == registerDTO) {
		    jores.put("resultMessage", "您还没有登录，请先登录");
		    return jores.toJSONString();
		}
		CouponsDTO couponsDTO = null;
		ExecuteResult<CouponsDTO> executeResult = couponsExportService.queryById(couponId);
		if (executeResult.isSuccess() && executeResult.getResult() != null) {
		    couponsDTO = executeResult.getResult();
		    // 判断用户个人类型是否符合领取条件
		    UserDTO u = userExportService.queryUserById(registerDTO.getUid());
		    if (null != couponsDTO.getCouponUserType()) {
			// 验证是否为企业用户
			if (couponsDTO.getCouponUserType() == 2) {
			    if (u.getUsertype() == 1) {
				jores.put("resultMessage", "企业用户可以领取，您不符合条件，请您认证之后进行领取");
				return jores.toJSONString();
			    }
			    // 验证是仅为个人用户
			} else if (couponsDTO.getCouponUserType() == 1) {
			    if (u.getUsertype() == 2 || u.getUsertype() == 3) {
				jores.put("resultMessage", "个人用户可以领取，您不符合条件");
				return jores.toJSONString();
			    }
			}
		    }
		    if (u.getParentId() != null) {
			jores.put("resultMessage", "子账号不可以领取优惠券");
			return jores.toJSONString();
		    }
	
		    // 验证会员条件限制
		    if (!StringUtils.isNotEmpty(u.getVipLevel())) {
			// 普通会员
			u.setVipLevel("1");
		    }
		    Date now = new Date();
			//2-已结束 3-已中止
			if(couponsDTO.getState() == 2 ){
				jores.put("resultMessage", "优惠券已过期，不可以领取");
				return jores.toJSONString();
			}else if(couponsDTO.getState() == 3 ){
				jores.put("resultMessage", "优惠券已终止，不可以领取");
				return jores.toJSONString();
			}else if(now.after(couponsDTO.getCouponEndTime())){
				jores.put("resultMessage", "优惠券已过期，不可以领取");
				return jores.toJSONString();
			}
		    // 领取优惠券的会员等级限制
			if (StringUtils.isNotEmpty(couponsDTO.getCouponUserMembershipLevel())) {
				if (!"0".equals(couponsDTO.getCouponUserMembershipLevel())) {
					String[] levelArry = couponsDTO.getCouponUserMembershipLevel().split(",");
					boolean isNotIn = true;
					String levStr = "";
					for (String l : levelArry) {
						if (u.getVipLevel().equals(l)) {
							isNotIn = false;
						}
						levStr += VipLevelEnum.getEnumById(Integer.parseInt(l)).toString() + "、";
					}
					if (isNotIn) {
						jores.put("resultMessage", levStr.substring(0, levStr.length() - 1) + "可以领取该优惠券");
						return jores.toJSONString();
					}
				}
			}
		    // 判断用户有没有领取过
		    CouponUserDTO couponUserInDTO = new CouponUserDTO();
		    couponUserInDTO.setCouponId(couponsDTO.getCouponId());
		    couponUserInDTO.setUserId(registerDTO.getUid());
		    //用户删除的也算是领取过的字段过滤
		    couponUserInDTO.setDeleted(1);
		    ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserInDTO, null);
		    if (couponUserResult.isSuccess() && couponUserResult.getResult() != null
			    && couponUserResult.getResult().getRows() != null
			    && couponUserResult.getResult().getRows().size() > 0) {
			jores.put("resultMessage", "您已经领取过该优惠券，请不要重复领取");
			return jores.toJSONString();
		    }
		    // 保存用户优惠券变量
		    CouponUserDTO couponUserDTO = new CouponUserDTO();
		    // 优惠券编号
		    couponUserDTO.setCouponId(couponsDTO.getCouponId());
		    couponUserDTO.setDeleted(1);
		    // 判断是否超出优惠券的最大领取数量
		    if (couponsDTO.getCouponNum() <= couponsExportService.queryReceivedNumber(couponUserDTO)) {
			jores.put("resultMessage", "优惠券已被领完");
			return jores.toJSONString();
		    }
		    // 领取时间
		    couponUserDTO.setCouponReceiveTime(new Date());
		    // 未删除
		    couponUserDTO.setDeleted(0);
		    // 领取优惠券的状态
		    couponUserDTO.setUserCouponType(0);
		    // 优惠券领取者ID
		    couponUserDTO.setUserId(registerDTO.getUid());
		    ExecuteResult<String> addResult = couponsExportService.addCouponsUser(couponUserDTO);
		    if (addResult.isSuccess()) {
			jores.put("resultMessage", "领取成功");
			return jores.toJSONString();
		    } else {
			jores.put("resultMessage", "领取失败");
			return jores.toJSONString();
		    }
		} else {
		    jores.put("resultMessage", "此优惠券不存在");
		    return jores.toJSONString();
		}
	    }
	
	    /**
	     * <p>Description: [将优惠券DTO转换成JSONObject] </p>
	     * Created on 2015年12月2日
	     * @param couponsDTO
	     * @return
	     * @author:[宋文斌]
	     */
	    private JSONObject convertToJSONObject(CouponsDTO couponsDTO, HttpServletRequest request) {
		// 判断有效期时间是否有效
		Date date = new Date();
		// 当优惠券状态为未开始或者已开始时 或者已终止
		if (couponsDTO.getState() == 1 || couponsDTO.getState() == 0 || couponsDTO.getState() == 0) {
			// 保存用户优惠券变量
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			// 优惠券编号
			couponUserDTO.setCouponId(couponsDTO.getCouponId());
			couponUserDTO.setDeleted(1);
			// 判断优惠券是否已过期，
			if (date.before(couponsDTO.getCouponStartTime())) {
				couponsDTO.setState(0);
			} else if (date.after(couponsDTO.getCouponStartTime()) && date.before(couponsDTO.getCouponEndTime())) {
				couponsDTO.setState(1);
			} else if (date.after(couponsDTO.getCouponEndTime())) {
				couponsDTO.setState(2);
			}
			if (couponsDTO.getCouponNum() <= couponsExportService.queryReceivedNumber(couponUserDTO) && couponsDTO.getState() < 2) {
				// 判断是否超出优惠券的最大领取数量
				couponsDTO.setState(7);
			}
		}
	
		JSONObject coupons = (JSONObject) JSON.toJSON(couponsDTO);
		// 判断是不是折扣券-2 去掉最后边多余的0
		if ("2".equals(couponsDTO.getCouponType().toString())) {
		    coupons.put("couponsMoney", moneyToSting(couponsDTO.getCouponAmount().toString()) + "折");
		} else {
		    coupons.put("couponsMoney", "￥" + moneyToSting(couponsDTO.getCouponAmount().toString()));
		}
	
		// 查询店铺信息
		Long shopId = couponsDTO.getShopId();
		if (shopId != null) {
		    ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
		    if (result != null && result.getResult() != null) {
			coupons.put("shopName", result.getResult().getShopName());
		    }
		}
		// 查询用户信息,判断用户有没有领取过该优惠券
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO != null) {
		    CouponUserDTO couponUserDTO = new CouponUserDTO();
		    couponUserDTO.setCouponId(couponsDTO.getCouponId());
		    couponUserDTO.setUserId(registerDTO.getUid());
		    ExecuteResult<DataGrid<CouponUserDTO>> result = couponsExportService.queryCouponsUserList(couponUserDTO,
			    null);
		    if (result.isSuccess() && result.getResult() != null && result.getResult().getRows() != null
			    && result.getResult().getRows().size() > 0) {
			coupons.put("isGet", "true");
			// 存入优惠券用户关联表主键，用来删除用户优惠券
			coupons.put("couponUserId", result.getResult().getRows().get(0).getId());
			coupons.put("userCouponType", result.getResult().getRows().get(0).getUserCouponType());
		    } else {
			coupons.put("isGet", "false");
		    }
		} else {
		    coupons.put("isGet", "false");
		}
		return coupons;
	    }
	
	    /**
	     * <p> Discription:[删除用户优惠券] </p>
	     * Created on 2015年12月8日
	     * @param model
	     * @param request
	     * @param couponsId优惠券编号
	     * @return
	     * @author:[李伟龙]
	     */
	    @RequestMapping("couponsUserUpdate")
	    @ResponseBody
	    public String couponsUserUpdate(Long id, HttpServletRequest request) {
		// 返回结果
		JSONObject jores = new JSONObject();
		// 获取当前登录用户信息
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO == null) {
		    jores.put("result","您还没有登录，请先登录");
		    return jores.toJSONString();
		}
		// 删除用户优惠券
		if (null != id) {
		    if (couponsExportService.deleteCouponsUser(id) > 0) {
			jores.put("result","删除成功");
		    } else {
			jores.put("result","删除失败");
		    }
		}
		return jores.toJSONString();
	    }
	
	    public String moneyToSting(String str) {
		while (str.endsWith("0")) {
		    str = str.substring(0, str.length() - 1);
		}
		if (str.endsWith(".")) {
		    str = str.substring(0, str.length() - 1);
		}
		return str;
	    }
	
	    /**
	     * <p>Discription:[店铺或者平台优惠券展示页面] </p>
	     * Created on 2015年12月29日
	     * @param costAllocation 1-平台 2-店铺 优惠券类型3核定订单
	     * @param model
	     * @param request
	     * @return
	     * @author:[訾瀚民]
	     */
	    @RequestMapping("indexCoupons")
	    public String indexCoupons(Model model, HttpServletRequest request,
		    @RequestParam(value = "costAllocation") Integer costAllocation,
		    @RequestParam(value = "type") Integer type,String paymentMethod_,String couponId) {
		//执行查询方法action
		String actionUrl = "";
		// 分页
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		pager.setPage(1);
		pager.setRows(Integer.MAX_VALUE);
		if (type == 3) {//查询核对订单可用的优惠劵
		    String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		    if (StringUtils.isBlank(uid)) {
			return "redirect:/user/login";
		    }
			ShopCartDTO myCart=null;
			UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
			String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
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
		    myCart = orderWxService.getMyCart(ctoken, uid, false, null,null,null, paymentMethod, com.camelot.openplatform.common.Constants.PLATFORM_WX_ID);
	
		    List<CouponsDTO> avaliableCoupons = new ArrayList<CouponsDTO>();
		    
		    List<String> couponIdList=new ArrayList<String>();
		    //获取发票类型
		    String invoiceType=request.getParameter("invoiceType");
		    //获取发票标题
		    String invoiceTitle=request.getParameter("invoiceTitle");
		    //获取收货地址变更
		    String address=request.getParameter("address"); 
		    model.addAttribute("address", address);
		    model.addAttribute("invoiceTitle", invoiceTitle);
		    model.addAttribute("invoiceType", invoiceType);
			// 不可使用的优惠券
			List<CouponsDTO> unavaliableCoupons = new ArrayList<CouponsDTO>();
			// 查询用户领取的优惠券
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			couponUserDTO.setUserId(Long.valueOf(uid));
			couponUserDTO.setDeleted(0);// 未删除
			couponUserDTO.setUserCouponType(0);// 未使用
			ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, pager);
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
							couponIdList.add(dto.getCouponId());
						}
					} else{
						if(couponResult.isSuccess() && couponResult.getResult() != null){
							unavaliableCoupons.add(couponResult.getResult());
							
						}
					}
				}
				
			}
		    if (couponUserResult.getResult() != null) {
			model.addAttribute("totalCount", avaliableCoupons.size());
			
		    } else {
			model.addAttribute("totalCount", 0);
		    }
		    actionUrl = "/coupons/buyerCouponsOrder?type="+type+"&&couponId="+couponId+"&&paymentMethod="+paymentMethod;
		}
		model.addAttribute("actionUrl", actionUrl);
		model.addAttribute("type", type);
	
		return "/coupons/indexCoupon";
	    }
	    
	    
	    
	    
	    
	    
	}
