package com.camelot.mall.coupons;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.common.util.DateUtils;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponUsingRangeDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.dto.emums.CouponUsingRangeEnum;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.report.dto.ReportQueryDTO;
import com.camelot.report.dto.ShopCustomerReportDTO;
import com.camelot.report.service.TradeReportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

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
    private UserExtendsService userExtendsService;
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
    private RedisDB redisDB;
    @Resource
	private TradeInventoryExportService tradeInventoryExportService;

    /**
     * <p> Description: [平台优惠券首页] </p>
     * Created on 2015年12月6日
     * @param page
     * @param couponsDTO
     * @param model
     * @param request
     * @return
     * @author:[宋文斌]
     */
	@RequestMapping("")
	public String index(Integer page, CouponsDTO couponsDTO, Model model, HttpServletRequest request) {
		// 获取当前登录用户信息
		Long uid = WebUtil.getInstance().getUserId(request);
		if (uid == null) {
			model.addAttribute("logging_status", "false");
		} else {
			model.addAttribute("logging_status", "true");
		}
		List<JSONObject> couponsDTOs = new ArrayList<JSONObject>();
		Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
		pager.setRows(30);
		if (null != page) {
			pager.setPage(page);
		}
		// 只查询已经开始和未开始的优惠券
		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(0);
		stateList.add(1);
		couponsDTO.setStateList(stateList);
		// 只显示平台的优惠券
		couponsDTO.setCostAllocation(1);
		// 只查询未发放用户的优惠券,首页优惠券领取
		couponsDTO.setSendCouponType(1);
		// platformId 优惠券使用平台(1:印刷家平台 pc,2:绿印平台,3:小管家 4:pc+小管家)
		List<Integer> platformIdList = new ArrayList<Integer>();
		platformIdList.add(1);
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
		model.addAttribute("pager", pager);
		return "/coupons/index";
	}

    /**
     * <p> Description: [领取优惠券]  </p>
     * Created on 2015年12月7日
     * @param couponId 优惠券编号
     * @param request
     * @return
     * @author:[宋文斌]
     */
	@RequestMapping("getCoupons")
	@ResponseBody
	public ExecuteResult<Boolean> getCoupons(String couponId, HttpServletRequest request) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		// 获取当前登录用户信息
		String token = LoginToken.getLoginToken(request);
		if (StringUtils.isBlank(token)) {
			result.setResultMessage("您还没有登录，请先登录");
			return result;
		}
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (null == registerDTO) {
			result.setResultMessage("您还没有登录，请先登录");
			return result;
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
						result.setResultMessage("企业用户可以领取，您不符合条件，请您认证之后进行领取");
						return result;
					}
					// 验证是仅为个人用户
				} else if (couponsDTO.getCouponUserType() == 1) {
					if (u.getUsertype() == 2 || u.getUsertype() == 3) {
						result.setResultMessage("个人用户可以领取，您不符合条件");
						return result;
					}
				}
			}
			if (u.getParentId() != null) {
				result.setResultMessage("子账号不可以领取优惠券");
				return result;
			}

			// 验证会员条件限制
			if (!StringUtils.isNotEmpty(u.getVipLevel())) {
				// 普通会员
				u.setVipLevel("1");
			}
			Date now = new Date();
			//2-已结束 3-已中止
			if(couponsDTO.getState() == 2 ){
			    result.setResultMessage("优惠券已过期，不可以领取");
			    return result;
			}else if(couponsDTO.getState() == 3 ){
			    result.setResultMessage("优惠券已终止，不可以领取");
			    return result;
			}else if(now.after(couponsDTO.getCouponEndTime())){
			    result.setResultMessage("优惠券已过期，不可以领取");
			    return result;
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
						result.setResultMessage(levStr.substring(0, levStr.length() - 1) + "可以领取该优惠券");
						return result;
					}
				}
			}

			// 判断用户有没有领取过
			CouponUserDTO couponUserInDTO = new CouponUserDTO();
			couponUserInDTO.setCouponId(couponsDTO.getCouponId());
			couponUserInDTO.setUserId(registerDTO.getUid());
			// 用户删除的也算是领取过的字段过滤
			couponUserInDTO.setDeleted(1);
			ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(
					couponUserInDTO, null);
			if (couponUserResult.isSuccess() && couponUserResult.getResult() != null
					&& couponUserResult.getResult().getRows() != null
					&& couponUserResult.getResult().getRows().size() > 0) {
				result.setResultMessage("您已经领取过该优惠券，请不要重复领取");
				return result;
			}
			// 保存用户优惠券变量
			CouponUserDTO couponUserDTO = new CouponUserDTO();
			// 优惠券编号
			couponUserDTO.setCouponId(couponsDTO.getCouponId());
			couponUserDTO.setDeleted(1);
			// 判断是否超出优惠券的最大领取数量
			if (couponsDTO.getCouponNum() <= couponsExportService.queryReceivedNumber(couponUserDTO)) {
				result.setResultMessage("优惠券已被领完");
				return result;
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
				result.setResult(true);
				// 供站内信拦截器使用
				String couponName = null;
				if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.PLATFORM.getCode()) {
					couponName = CouponUsingRangeEnum.PLATFORM.getLabel();
				} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.SHOP.getCode()) {
					couponName = CouponUsingRangeEnum.SHOP.getLabel();
				} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.CATEGORY.getCode()) {
					couponName = CouponUsingRangeEnum.CATEGORY.getLabel();
				} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.SKU.getCode()) {
					couponName = CouponUsingRangeEnum.SKU.getLabel();
				}
				request.setAttribute("couponName", couponName);
				request.setAttribute("buyerId", registerDTO.getUid());
			} else {
				result.getErrorMessages().addAll(addResult.getErrorMessages());
				result.setResultMessage("领取失败");
				return result;
			}
		} else {
			result.setResultMessage("此优惠券不存在");
			return result;
		}
		result.setResultMessage("领取成功");
		return result;
	}

    /**
     * <p> Description: [将优惠券DTO转换成JSONObject] </p>
     * Created on 2015年12月2日
     * @param couponsDTO
     * @return
     * @author:[宋文斌]
     */
	private JSONObject convertToJSONObject(CouponsDTO couponsDTO, HttpServletRequest request) {
		// 判断有效期时间是否有效
		Date date = new Date();
		// 当优惠券状态为未开始或者已开始时 ,或者为已终止
		if (couponsDTO.getState() == 1 || couponsDTO.getState() == 0 || couponsDTO.getState() == 3) {
		    	// 保存用户优惠券变量
		 	CouponUserDTO couponUserDTO = new CouponUserDTO();
		 	couponUserDTO.setDeleted(1);
		 	// 优惠券编号
		 	couponUserDTO.setCouponId(couponsDTO.getCouponId());
			// 判断优惠券是否已过期，
			if (date.before(couponsDTO.getCouponStartTime())) {
				couponsDTO.setState(0);
			} else if (date.after(couponsDTO.getCouponStartTime()) && date.before(couponsDTO.getCouponEndTime())) {
				couponsDTO.setState(1);
			} else if (date.after(couponsDTO.getCouponEndTime())) {
				couponsDTO.setState(2);
			} 
			if(couponsDTO.getCouponNum() <= couponsExportService.queryReceivedNumber(couponUserDTO) && couponsDTO.getState() < 2){
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
			couponUserDTO.setDeleted(1);
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
     * <p> Discription:[跳转到优惠券新增或者修改方法] </p>
     * Created on 2015年12月2日
     * @param model
     * @param request
     * @param type 判断是新增还是修改 1新增，2修改
     * @param couponsId 优惠券编码，修改时有值，新增时空
     * @return
     * @author:[李伟龙]
     */
    @RequestMapping("couponsAdd")
    public String couponsAdd(Model model, HttpServletRequest request, @RequestParam(value = "type") String type,
	    @RequestParam(value = "couponsId") String couponsId) {
	// 获取当前登录用户信息
	String token = LoginToken.getLoginToken(request);
	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	if (null == registerDTO) {
	    return "redirect:/user/login";
	}

	// 获取该卖家发放用户的次数
	String key = registerDTO.getUid() + "_coupon";
	String saveCount = redisDB.get(key);
	if (null != saveCount && !"".equals(saveCount)) {
	    model.addAttribute("saveCount", saveCount);
	} else {
	    model.addAttribute("saveCount", "0");
	}

	// 查询是否是父账号
	UserDTO isParent = userExportService.queryUserById(registerDTO.getUid());
	UserDTO user = null;
	if (null == isParent.getParentId()) {
	    // 父账号
	    user = userExportService.queryUserById(registerDTO.getUid());
	} else {
	    // 子账号
	    user = userExportService.queryUserByfId(registerDTO.getUid());
	}
	// 获取店铺信息
	ShopDTO shop = null;
	ShopDTO queryShop = new ShopDTO();
	queryShop.setSellerId(user.getUid());
	ExecuteResult<DataGrid<ShopDTO>> shopResule = shopService.findShopInfoByCondition(queryShop, new Pager<ShopDTO>());
	if (null != shopResule.getResult()) {
	    List<ShopDTO> shopList = shopResule.getResult().getRows();
	    if (null != shopList) {
		shop = shopList.get(0);
	    }
	}
	// 如果不为空则查询出该条优惠券信息为空直接跳转到新增页面
	if (StringUtils.isNotEmpty(couponsId)) {
	    // 查询子表信息分页
	    Pager page = new Pager();
	    page.setPage(1);
	    page.setRows(Integer.MAX_VALUE);
	    // 优惠券主表信息实体
	    CouponsDTO couponsDTO = new CouponsDTO();
	    // 查看页面跳转，查询优惠券信息，子表关联信息
	    ExecuteResult<CouponsDTO> result = couponsExportService.queryById(couponsId);
	    if (null != result.getResult()) {
		couponsDTO = result.getResult();
		if(couponsDTO.getShopId() != 0){
			user = userExportService.queryUserById(Long.parseLong(couponsDTO.getCreateUser()));
		}
		ExecuteResult<ShopDTO> shopResult = shopService.findShopInfoById(couponsDTO.getShopId());
		shop = shopResult.getResult();
		// 优惠券描述
		if (couponsDTO.getCouponType() == 1) {
		    couponsDTO.setDescr("满" + moneyToSting(couponsDTO.getMeetPrice().toString()) + "元，减" + moneyToSting(couponsDTO.getCouponAmount().toString()) + "元");
		} else if (couponsDTO.getCouponType() == 2) {
		    couponsDTO.setDescr(moneyToSting(couponsDTO.getCouponAmount().toString()) + "折，限额" + moneyToSting(couponsDTO.getCouponMax().toString()) + "元");
		} else if (couponsDTO.getCouponType() == 3) {
		    couponsDTO.setDescr("直降" + moneyToSting(couponsDTO.getCouponAmount().toString()) + "元");
		}
		model.addAttribute("couponsDTO", couponsDTO);
	    }
	    // 根据 优惠券使用范围(1:平台通用类,2:店铺通用类,3:品类通用类,4:SKU使用类) 判断是否需要查询子表信息
	    if (null != couponsDTO.getCouponUsingRange()) {
		// 查询sku子表信息，状态为2时为该卖家店铺通用，不查询，子表信息
		if (4 == couponsDTO.getCouponUsingRange()) {
		    // 类目信息
		    DataGrid<ItemCategoryDTO> categories = itemCategoryService.queryItemCategoryAllList(null);
		    Map<String, String> catMap = new HashMap<String, String>();
		    for (ItemCategoryDTO e : categories.getRows()) {
			catMap.put(e.getCategoryCid().toString(), e.getCategoryCName());
		    }
		    // 优惠券子表信息
		    CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
		    couponUsingRangeDTO.setCouponId(couponsId);
		    ExecuteResult<DataGrid<CouponUsingRangeDTO>> couponUsingRangeResult = couponsExportService.queryCouponUsingRangeList(couponUsingRangeDTO, page);
		    if (null != couponUsingRangeResult.getResult()) {
			// 获取sku属性
			ItemShopCartDTO dto = new ItemShopCartDTO();
			// 根据skuid获取商品信息
			ItemDTO itemSku;
			ExecuteResult<ItemDTO> itemSkuResult;
			// sku属性名称
			String skuName = "";
			// 使用范围子表信息
			List<CouponUsingRangeDTO> couponUsingRangeList = couponUsingRangeResult.getResult().getRows();
			int i = 0;
			for (CouponUsingRangeDTO singRange : couponUsingRangeList) {
			    skuName = "";
			    itemSkuResult = itemService.getItemBySkuId(singRange.getCouponUsingId());
			    itemSku = itemSkuResult.getResult();

			    // 获取sku属性
			    dto.setAreaCode("0");
			    dto.setShopId(Long.valueOf(itemSku.getShopId()));
			    dto.setSkuId(Long.valueOf(singRange.getCouponUsingId()));
			    dto.setItemId(Long.valueOf(itemSku.getItemId()));
			    ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); // 调商品接口查url
			    ItemShopCartDTO itemShopCartDTO = er.getResult();
			    if (null != itemShopCartDTO) {
				// 商品属性
				for (ItemAttr itemAttr : itemShopCartDTO.getAttrSales()) {
				    skuName += itemAttr.getName();
				    for (ItemAttrValue itemAttrValue : itemAttr.getValues()) {
					skuName += ":" + itemAttrValue.getName() + ";";
				    }
				}
			    }

			    couponUsingRangeList.get(i).setSkuAttr(skuName);
			    couponUsingRangeList.get(i).setItemId(itemSku.getItemId().toString());
			    couponUsingRangeList.get(i).setItemName(itemSku.getItemName());
			    couponUsingRangeList.get(i).setCategoryNme(catMap.get(itemSku.getCid().toString()));
			    i++;
			}
			model.addAttribute("couponUsingRangeList", couponUsingRangeList);
		    }
		}
	    }
	    // 根据店铺 优惠券发放方式 判断是否需要查询已发放的用户
	    if (null != couponsDTO.getSendCouponType()) {
		// 1-待用户领取 2-直接发放用户
		if (2 == couponsDTO.getSendCouponType()) {
		    // 查询用户优惠券关联表条件实体
		    CouponUserDTO couponUserDTO = new CouponUserDTO();
		    couponUserDTO.setCouponId(couponsId);
		    // 查询优惠券user表信息
		    ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, page);
		    if (null != couponUserResult.getResult()) {
			// 查询的user优惠券关联表信息
			List<CouponUserDTO> couponUserList = couponUserResult.getResult().getRows();
			if (null != couponUserList && !couponUserList.isEmpty()) {
			    int i = 0;
			    for (CouponUserDTO dto : couponUserList) {
				// 查询用户信息
				UserDTO userDto = userExportService.queryUserByfId(dto.getUserId());
				if (null != userDto) {
				    couponUserList.get(i).setUserName(userDto.getUname());
				    couponUserList.get(i).setPhone(userDto.getUmobile());
				    couponUserList.get(i).setNickName(userDto.getNickname());
				    ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(userDto.getUid());
					String companyName = userInfoResult != null && userInfoResult.isSuccess() && userInfoResult.getResult() != null?
							userInfoResult.getResult().getUserBusinessDTO().getCompanyName():"";
				    couponUserList.get(i).setCompanyName(companyName);
				    
				    //获取会员等级
					VipLevelEnum vip = VipLevelEnum.getVipLevelEnumByGrowthValue(userDto.getGrowthValue());
					if(null != vip){
						couponUserList.get(i).setLevel(vip.getName());
					}else{
						couponUserList.get(i).setLevel("红象会员");
					}
				}
				i++;
			    }
			    model.addAttribute("couponUserList", couponUserList);
			}
		    }
		    // 带用户领取
		} else if (1 == couponsDTO.getSendCouponType()) {
		    String level = couponsDTO.getCouponUserMembershipLevel();
		    if (StringUtils.isNotEmpty(level)) {
			if ("0".equals(level)) {
			    model.addAttribute("vipLevel", "未限制");
			} else {
			    String[] levelArry = level.split(",");
			    String str = "";
			    for (String l : levelArry) {
				str += VipLevelEnum.getEnumById(Integer.parseInt(l)).toString() + "、";
			    }
			    model.addAttribute("vipLevel", str.substring(0, str.length() - 1));
			}
		    }
		}
	    }
	} else {
	    couponsId = createCouponsId();
	}

	model.addAttribute("couponsId", couponsId);
	model.addAttribute("type", type);
	model.addAttribute("user", user);
	model.addAttribute("shop", shop);
	return "/coupons/couponsAdd";
    }

    /**
     * <p>Discription:[加载优惠券列表信息]</p>
     * Created on 2015年12月4日
     * @param model
     * @param request
     * @param pager
     * @return
     * @author:[李伟龙]
     */
    @RequestMapping("couponsList")
    public String couponsList(Model model, HttpServletRequest request, Pager pager, String contractSearchModel) {
	// 获取当前登录用户信息
	String token = LoginToken.getLoginToken(request);
	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	if (registerDTO == null) {
	    return "redirect:/user/login";
	}

	ShopDTO queryShop = new ShopDTO();
	// 查询条件实体
	CouponsDTO couponsDTO = new CouponsDTO();

	// platformId 优惠券使用平台(1:印刷家平台,2:绿印平台,3:小管家 4:pc+小管家)
	List<Integer> platformIdList = new ArrayList<Integer>();
	platformIdList.add(1);
	platformIdList.add(3);
	platformIdList.add(4);
	couponsDTO.setPlatformIdList(platformIdList);
	// 放入页面查询条件
	if (StringUtils.isNotEmpty(contractSearchModel)) {
	    JSONObject searchModel = JSONObject.parseObject(contractSearchModel, JSONObject.class);
	    String couponType = searchModel.getString("couponType");
	    String couponName = searchModel.getString("couponName");
	    String platformId = searchModel.getString("platformId");
	    if (StringUtils.isNotEmpty(couponType)) {
		couponsDTO.setCouponType(Integer.parseInt(couponType));
	    }
	    if (StringUtils.isNotEmpty(couponName)) {
		couponsDTO.setCouponName(couponName);
	    }
	    if (StringUtils.isNotEmpty(platformId)) {
		couponsDTO.setPlatformId(Integer.parseInt(platformId));
		couponsDTO.setPlatformIdList(null);
	    }
	}

	// 获取主子帐号
	ExecuteResult<List<Long>> userIdList = userExportService.queryUserIds(registerDTO.getUid());
	// 查询shopid
	ExecuteResult<DataGrid<ShopDTO>> shopResule;
	// 用主子账号的集合查询
	if (userIdList.getResult() != null) {
	    List<Long> supplierIdList = new ArrayList<Long>();
	    for (Long userId : userIdList.getResult()) {
		queryShop.setSellerId(userId);
		// 查询shopid
		shopResule = shopService.findShopInfoByCondition(queryShop, new Pager<ShopDTO>());
		if (null != shopResule.getResult()) {
		    List<ShopDTO> shopList = shopResule.getResult().getRows();
		    if (null != shopList && !shopList.isEmpty()) {
			supplierIdList.add(shopList.get(0).getShopId());
		    }
		}
	    }
	    couponsDTO.setShopIds(supplierIdList);
	}
	// 查询用户优惠券关联表条件实体
	CouponUserDTO couponUserDTO = new CouponUserDTO();
	// 查询优惠券list
	ExecuteResult<DataGrid<CouponsDTO>> result = couponsExportService.queryCouponsList(couponsDTO, pager);
	List<CouponsDTO> list = result.getResult().getRows();
	// 用来判断优惠券是否已经过期
	Date date = new Date();
	if (null != list && !list.isEmpty()) {
	    int i = 0;
	    for (CouponsDTO cou : list) {
		if (cou.getCouponType() == 1) {
		    list.get(i).setDescr("满" + moneyToSting(cou.getMeetPrice().toString()) + "元，减" + moneyToSting(cou.getCouponAmount().toString()) + "元");
		} else if (cou.getCouponType() == 2) {
		    list.get(i).setDescr(moneyToSting(cou.getCouponAmount().toString()) + "折，限额" + moneyToSting(cou.getCouponMax().toString()) + "元");
		} else if (cou.getCouponType() == 3) {
		    list.get(i).setDescr("直降" + moneyToSting(cou.getCouponAmount().toString()) + "元");
		}

		// 当优惠券状态为未开始或者已开始时
		if (cou.getState() == 1 || cou.getState() == 0) {
		    // 判断优惠券是否已过期，
		    if (cou.getCouponStartTime().getTime() > date.getTime()) {
			cou.setState(0);
		    } else if (cou.getCouponStartTime().getTime() < date.getTime() && cou.getCouponEndTime().getTime() > date.getTime()) {
			cou.setState(1);
		    } else if (cou.getCouponEndTime().getTime() < date.getTime()) {
			cou.setState(2);
		    }
		}
		// 查询出用户名 注释掉不显示在页面上
		// list.get(i).setUserName(userExportService.queryUserByfId(cou.getCreateUser()).getUname());
		// 根据优惠编号查询出该优惠券的已领取数量
		couponUserDTO.setCouponId(cou.getCouponId());
		couponUserDTO.setDeleted(1);
		list.get(i).setReceivedNumber(couponsExportService.queryReceivedNumber(couponUserDTO));
		i++;
	    }
	}
	// 查处用户名与已经领取的数量
	pager.setTotalCount(result.getResult().getTotal().intValue());
	pager.setRecords(list);
	model.addAttribute("pager", pager);

	if (StringUtils.isNotEmpty(contractSearchModel)) {
	    return "/coupons/couponsPage";
	}

	return "/coupons/couponsList";
    }

    /**
     * 
     * <p> Discription:[保存优惠券] </p>
     * Created on 2015年12月7日
     * @param model
     * @param request
     * @param couponsDTO 实体
     * @return
     * @author:[李伟龙]
     */
    @RequestMapping("saveCouponsInfo")
	public String saveCouponsInfo(Model model, HttpServletRequest request, CouponsDTO couponsDTO) {

		// 获取当前登录用户信息
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if (registerDTO == null) {
			return "redirect:/user/login";
		}
		if (null != couponsDTO) {
			// 当前时间
			Date date = new Date();
			Date start = DateUtils.parseReturnTime(request.getParameter("couponStartTime"), DateUtils.YYDDMMHHMMSS);
			Date end = DateUtils.parseReturnTime(request.getParameter("couponEndTime"), DateUtils.YYDDMMHHMMSS);
			couponsDTO.setCouponStartTime(start);
			couponsDTO.setCouponEndTime(end);
			// 用户优惠券使用状态
			int userCouponType = 0;// 0-未使用，1-已使用，2-已过期 3-未开始 4-已终止
			// 优惠券状态
			int state = 0;
			// 判断优惠券是否已经开始
			if (start.getTime() > date.getTime()) {
				state = 0;
				userCouponType = 3;
			} else if (start.getTime() < date.getTime() && end.getTime() > date.getTime()) {
				state = 0;
				userCouponType = 0;
			} else if (end.getTime() < date.getTime()) {
				state = 2;
				userCouponType = 2;
			}
			// 修改时间
			couponsDTO.setUpdateDate(date);
			// 优惠券状态
			couponsDTO.setState(state);
			// 是否删除
			couponsDTO.setDeleted(0);
			// 插入优惠券主表
			couponsExportService.addCoupons(couponsDTO);
			if (null != couponsDTO.getSendCouponType()) {
				// 如果卖家选择的发放方式为直接发放给用户 则直接存入到优惠券用户关联表
				if (couponsDTO.getSendCouponType() == 2) {
					String key = registerDTO.getUid() + "_coupon";
					String sendCount = redisDB.get(key);
					String endTime = DateUtils.getDateStr(DateUtils.dayOffset(new Date(), +1), DateUtils.YMD_DASH);
					Date endDate = DateUtils.parseReturnTime(endTime, DateUtils.YMD_DASH);
					// 插入该卖家发放用户的次数
					if (null != sendCount) {
						int count = Integer.parseInt(sendCount) + 1;
						redisDB.set(key, count + "");
						redisDB.setExpire(key, endDate);
					} else {
						redisDB.set(key, "1");
						redisDB.setExpire(key, endDate);
					}
					// 优惠券用户关联表实体
					CouponUserDTO couponUserDTO = new CouponUserDTO();
					// 发放时间 、领取时间
					couponUserDTO.setCouponReceiveTime(new Date());
					// 优惠券编号
					couponUserDTO.setCouponId(couponsDTO.getCouponId());
					// 删除标识 0-未删除 1-已删除
					couponUserDTO.setDeleted(0);
					// 用户领取优惠券的状态
					couponUserDTO.setUserCouponType(userCouponType);
					if (null != couponsDTO.getUserIds()) {
						String[] userIdArry = couponsDTO.getUserIds().split(";");
						List<Long> buyerIds = new ArrayList<Long>();
						// 循环插入优惠券用户关联表
						for (String userId : userIdArry) {
							// 用户id
							couponUserDTO.setUserId(Long.valueOf(userId));
							// 插入数据
							couponsExportService.addCouponsUser(couponUserDTO);
							buyerIds.add(Long.valueOf(userId));
						}
						// 供站内信拦截器使用
						String couponName = null;
						if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.PLATFORM.getCode()) {
							couponName = CouponUsingRangeEnum.PLATFORM.getLabel();
						} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.SHOP.getCode()) {
							couponName = CouponUsingRangeEnum.SHOP.getLabel();
						} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.CATEGORY.getCode()) {
							couponName = CouponUsingRangeEnum.CATEGORY.getLabel();
						} else if (couponsDTO.getCouponUsingRange() == CouponUsingRangeEnum.SKU.getCode()) {
							couponName = CouponUsingRangeEnum.SKU.getLabel();
						}
						request.setAttribute("couponName", couponName);
						request.setAttribute("buyerIds", buyerIds.toArray(new Long[buyerIds.size()]));
					}
				}
			}

			// 是否限制sku
			if (null != couponsDTO.getCouponUsingRange()) {
				// 适用范围表 实体
				CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
				// 优惠编号
				couponUsingRangeDTO.setCouponId(couponsDTO.getCouponId());
				// 如果适用范围为 限制sku则插入数据到子表 优惠券使用范围表
				if (couponsDTO.getCouponUsingRange() == 4) {
					if (null != couponsDTO.getSkuIds()) {
						String[] skuIdArry = couponsDTO.getSkuIds().split(";");
						// 循环插入优惠券用户关联表
						for (String couponUsingId : skuIdArry) {
							// skuid
							couponUsingRangeDTO.setCouponUsingId(Long.parseLong(couponUsingId));
							// 插入数据
							couponsExportService.addCouponUsingRange(couponUsingRangeDTO);
						}
					}
					// 店铺内通用
				} else if (couponsDTO.getCouponUsingRange() == 2) {
					// shopId 店铺Id
					couponUsingRangeDTO.setCouponUsingId(couponsDTO.getShopId());
					// 插入数据
					couponsExportService.addCouponUsingRange(couponUsingRangeDTO);
				}
			}
		}

		return "redirect:/coupons/couponsList";
	}

    /**
     * <p> Discription:[删除优惠券方法] </p>
     * Created on 2015年12月7日
     * @param model
     * @param request 
     * @param couponsIds 优惠券编号字符串 用 ; 隔开
     * @return 返回到列表页面
     * @author:[李伟龙]
     */
    @RequestMapping("couponsDelete")
    public String couponsDelete(Model model, HttpServletRequest request, @RequestParam(value = "couponsIds") String couponsIds) {
	// 获取当前登录用户信息
	String token = LoginToken.getLoginToken(request);
	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	if (registerDTO == null) {
	    return "redirect:/user/login";
	}

	if (StringUtils.isNotEmpty(couponsIds)) {
	    String[] couponsIdsArry = couponsIds.split(";");
	    for (String couponsId : couponsIdsArry) {
		couponsExportService.deleteCoupons(couponsId);
	    }
	}
	return "redirect:/coupons/couponsList";
    }

    /**
     * <p> Discription:[更新优惠券状态方法] </p>
     * Created on 2015年12月7日
     * @param model
     * @param request
     * @param couponsIds 优惠券编号字符串 用 ; 隔开
     * @param state 状态码
     * @return
     * @author:[李伟龙]
     */
    @RequestMapping("couponsUpdate")
    public String couponsUpdate(Model model, HttpServletRequest request,
	    @RequestParam(value = "couponsIds") String couponsIds, @RequestParam(value = "state") int state) {
	// 获取当前登录用户信息
	String token = LoginToken.getLoginToken(request);
	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	if (registerDTO == null) {
	    return "redirect:/user/login";
	}
	// 查询优惠券信息返回的变量
	ExecuteResult<CouponsDTO> result;
	CouponsDTO couponsDTO;
	// 循环更新优惠券状态
	if (StringUtils.isNotEmpty(couponsIds)) {
	    String[] couponsIdsArry = couponsIds.split(";");
	    for (String couponsId : couponsIdsArry) {
		result = couponsExportService.queryById(couponsId);
		if (null != result.getResult()) {
		    couponsDTO = result.getResult();
		    couponsDTO.setState(state);
		    couponsExportService.updateCouponsInfo(couponsDTO);
		}
	    }
	}
	return "redirect:/coupons/couponsList";
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
    public ExecuteResult<Boolean> couponsUserUpdate(Long id, HttpServletRequest request) {
	ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
	// 获取当前登录用户信息
	String token = LoginToken.getLoginToken(request);
	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	if (registerDTO == null) {
	    result.addErrorMessage("您还没有登录，请先登录");
	    return result;
	}
	// 删除用户优惠券
	if (null != id) {
	    if (couponsExportService.deleteCouponsUser(id) > 0) {
		result.setResult(true);
	    } else {
		result.setResult(false);
		result.addErrorMessage("删除失败");
	    }
	}
	return result;
    }

    /**
     * <p> Discription:[买家优惠券查看] </p>
     * Created on 2015年12月8日
     * @param page
     * @param model
     * @param request
     * @return
     * @author:[李伟龙]
     */
    @RequestMapping("buyerCoupons")
    public String buyerCoupons(Integer page, Model model, HttpServletRequest request) {
	String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
	if (StringUtils.isBlank(uid)) {
	    return "redirect:/user/login";
	}
	List<JSONObject> couponsDTOs = new ArrayList<JSONObject>();
	Pager<CouponsDTO> pager = new Pager<CouponsDTO>();
	pager.setRows(28);
	if (null != page) {
	    pager.setPage(page);
	}
	// 查询登录用户下的优惠券信息
	CouponUserDTO couponUserDTO = new CouponUserDTO();
	couponUserDTO.setUserId(Long.valueOf(uid));
	// platformId 优惠券使用平台(1:印刷家平台,2:绿印平台,3:小管家 4:pc +小管家)
	List<Integer> platformIdList = new ArrayList<Integer>();
	platformIdList.add(1);
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
	model.addAttribute("pager", pager);
	return "/coupons/buyerCoupons";
    }

    /**
     * <p> Discription:[查询所有用户] </p>
     * Created on 2015年12月4日
     * @param page 分页
     * @param model
     * @param type 1代表个人，2代表企业
     * @param level 会员等级
     * @param userName 用户名称
     * @param shopId 是否在本店铺买过东西，有值过滤，空则不过滤
     * @return 买家信息页面
     * @author:[李伟龙]
     */
    @RequestMapping("/getUserDetail")
    public String getUserDetail(Pager<UserDTO> page, Model model, @RequestParam(value = "type") String type,
	    @RequestParam(value = "level") String level, @RequestParam(value = "shopId") String shopId, 
	    @RequestParam(value = "userName") String userName,@RequestParam(value = "companyName") String companyName,String nickName) {
	// 查询条件实体
	UserDTO userDto = new UserDTO();
	//用户名称
	if (StringUtils.isNotEmpty(userName)) {
	    userDto.setUname(userName);
	}
	
	if (StringUtils.isNotEmpty(type)) {
	    // 用户状态组
	    List<Integer> userStatusList = new ArrayList<Integer>();
	    // 个人
	    if (Constants.INDIVIDUAL_STATUS.equals(type)) {
		userStatusList.add(1);
		userStatusList.add(2);
		userStatusList.add(3);
		// 企业
	    } else if (Constants.ENTERPRISE_STATUS.equals(type)) {
		userStatusList.add(4);
		// userStatusList.add(5);
		userStatusList.add(6);
	    }
	    // 放入实体，查询用
	    userDto.setUserStatusList(userStatusList);
	}

	// 会员等级
	if (StringUtils.isNotEmpty(level)) {
	    userDto.setVipLevel(level);
	}

	// 是否在该店铺买过商品
	if (StringUtils.isNotEmpty(shopId) && !"2".equals(shopId)) {
	    ReportQueryDTO queryDto = new ReportQueryDTO();
	    Pager p = new Pager();
	    p.setPage(1);
	    p.setRows(Integer.MAX_VALUE);
	    queryDto.setShopId(Long.parseLong(shopId));
	    DataGrid<ShopCustomerReportDTO> customerData = tradeReportService.queryShopCustomerList(queryDto, p);
	    List<ShopCustomerReportDTO> list = customerData.getRows();
	    List<String> uidList = new ArrayList<String>();
	    // 取到所有会员的Userid
	    if (null != list && !list.isEmpty()) {
		for (ShopCustomerReportDTO vip : list) {
		    uidList.add(vip.getUserId().toString());
		}
		userDto.setIdList(uidList);
	    }
	}

	DataGrid<UserDTO> users = new DataGrid<UserDTO>();
	userDto.setParentId(-1L);
	//公司名称
	userDto.setCompanyName(companyName);
	userDto.setNickname(nickName);
	users = userExportService.findUserListByCondition(userDto, null, page);
	page.setTotalCount(users.getTotal().intValue());
	page.setRecords(users.getRows());
	model.addAttribute("userList", users.getRows());
	model.addAttribute("page", page);
	return "/coupons/userDetail";
    }

    /**
     * <p>Discription:[修改优惠券的数量]</p>
     * Created on 2015年12月30日
     * @param couponId 优惠券编号
     * @param couponNum	增加的数量
     * @param request
     * @return
     * @author:[李伟龙]
     */
    @RequestMapping("modifyCouponsNum")
    @ResponseBody
    public ExecuteResult<Boolean> modifyCouponsNum(String couponId, int couponNum, HttpServletRequest request) {
	ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
	// 获取当前登录用户信息
	String token = LoginToken.getLoginToken(request);
	RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	if (null == registerDTO) {
	    result.setResultMessage("您还没有登录，请先登录");
	    return result;
	}
	// 获取优惠券信息
	ExecuteResult<CouponsDTO> couponsResult = couponsExportService.queryById(couponId);
	CouponsDTO couponsDTO = couponsResult.getResult();
	// 更新优惠券数量
	couponsDTO.setCouponNum((couponNum + couponsDTO.getCouponNum()));
	ExecuteResult<String> updateResult = couponsExportService.updateCouponsInfo(couponsDTO);
	if (updateResult.getResultMessage().equals("success")) {
	    result.setResultMessage("增加成功");
	} else {
	    result.setResultMessage("增加失败");
	}
	return result;
    }

    /**
     * <p>Discription:[创建优惠券编号]</p>
     * Created on 2015年12月30日
     * @return
     * @author:[李伟龙]
     */
    public String createCouponsId() {
	Date date = new Date();
	Random random = new Random();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	String str = sdf.format(date) + (random.nextInt(99) + 100);
	return str;
    }

    /**
     * <p>Discription:[去掉最后边无用的0]</p>
     * Created on 2015年12月30日
     * @param str
     * @return
     * @author:[李伟龙]
     */
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
	 * 
	 * <p>Discription:[查看优惠劵活动详情]</p>
	 * Created on 2016-1-19
	 * @return
	 * @author:[周志军]
	 */
	@RequestMapping(value="couponShowList")
	@ResponseBody
	public String couponShowList(String couponsId){
		ExecuteResult<CouponsDTO> res = couponsExportService.queryById(couponsId);
		JSONArray array = new JSONArray();
		if(res != null && res.getResult() != null){
			CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
			couponUsingRangeDTO.setCouponId(couponsId);
			ExecuteResult<DataGrid<CouponUsingRangeDTO>> dg = couponsExportService.queryCouponUsingRangeList(couponUsingRangeDTO, null);
			for(CouponUsingRangeDTO dto : dg.getResult().getRows()){
				if(res.getResult().getCouponUsingRange()==2){
					if(array.size()<1){
						//用于区分店铺、类目、SKU
						array.add(res.getResult());
					}
					ExecuteResult<ShopDTO> shop=shopExportService.findShopInfoById(dto.getCouponUsingId());
					dto.setItemName(shop.getResult().getShopName());//店铺名称
					dto.setItemId(shop.getResult().getShopId().toString());//店铺id
					dto.setSkuAttr(shop.getResult().getSellerId().toString());//卖家id
					dto.setPrice(shop.getResult().getCreated().toString());//申请时间
					array.add(dto);
				}else if(res.getResult().getCouponUsingRange()==3){//类目通用
					if(array.size()<1){
						//用于区分类目、SKU
						array.add(res.getResult());
					}
					String oneName="";
			        String twoName = "";
			        String threeName = "";
			        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory =  itemCategoryService.queryParentCategoryList(dto.getCouponUsingId());
			        for(ItemCatCascadeDTO itemCatCascadeOne : resultCategory.getResult()){
						for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
							for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
								if(dto.getCouponUsingId().equals(itemCatCascadeThree.getCid())){
									if(threeName.length()==0){
										threeName = itemCatCascadeThree.getCname();
									}
									if(oneName.length()==0){
										oneName = itemCatCascadeOne.getCname();
									}
									if(twoName.length()==0){
										twoName = itemCatCascadeTwo.getCname();
									}
									itemCatCascadeTwo.getCname();
									break ;
								}
							}
						}
					}
			        dto.setItemName(oneName);//一级类目名称
					dto.setItemId(twoName);//二级类目名称
					dto.setSkuAttr(threeName);//三级类目名称
					array.add(dto);
				}else if(res.getResult().getCouponUsingRange()==4){//sku同用
					if(array.size()<1){
						//用于区分类目、SKU
						array.add(res.getResult());
					}
					TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
					tradeInventoryInDTO.setSkuId(dto.getCouponUsingId());
					ExecuteResult<DataGrid<TradeInventoryOutDTO>> re = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, null);
					JSONObject obj = JSON.parseObject(JSON.toJSONString(dto));
					
					obj.put("itemName", re.getResult().getRows().get(0).getItemName());
					obj.put("itemId",re.getResult().getRows().get(0).getSkuId());
					obj.put("skuAttr", re.getResult().getRows().get(0).getItemAttr());
					obj.put("categoryNme", re.getResult().getRows().get(0).getItemCatCascadeDTO());
					obj.put("price", re.getResult().getRows().get(0).getAreaPrices());
					array.add(obj);
				}
			}
			
		}
		return JSON.toJSONString(array);
	}
}
