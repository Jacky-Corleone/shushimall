package com.camelot.mall.shopcart;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.common.enums.BankSettleTypeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.TradeInventoryDTO;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.maketcenter.service.VipCardService;
import com.camelot.mall.Constants;
import com.camelot.mall.enums.IntegralItemTypeEnum;
import com.camelot.mall.sellcenter.ChildUserListPost;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartCouponService;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopDeliveryTypeService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFareExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.combin.TradeOrderCreateDTO;
import com.camelot.tradecenter.dto.enums.OrderTypeEnum;
import com.camelot.tradecenter.service.InvoiceExportService;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.ChildUserDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;
import com.camelot.usercenter.service.UserStorePermissionExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;

/**
 * <p>
 * Description: [描述该类概要功能介绍]
 * </p>
 * Created on 2015年3月3日
 *
 * @author <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/shopCart")
public class ShopCartController {
	private Logger LOG = LoggerFactory.getLogger(ShopCartController.class);
	@Resource
	private ShopCartService shopCartService;
	@Resource
	private AddressInfoService addressInfoService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private PaymentExportService paymentService;
	@Resource
	private UserCompanyService companyService;
	@Resource
	private UserExportService userService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private ShopExportService shopService;
	@Resource
	private ShopFareExportService shopFareService;

	@Resource
	private ItemExportService itemExportService;

	@Resource
	private RedisDB redisDB;
	@Resource
	private UserStorePermissionExportService userStorePermissionExportService;

	@Resource
	private UserExportService userExportService;

	@Resource
	private ProtocolExportService protocolExportService;

	@Resource
	private InvoiceExportService invoiceExportService;

	@Resource
	private TradeReturnExportService tradeReturnExportService;

	@Resource
	private ShopDeliveryTypeService shopDeliveryTypeService;

	@Resource
	private VipCardService vipCardService;

	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	@Resource
	private CouponsExportService couponsExportService;
	@Resource
	private IntegralConfigExportService integralConfigService;
	@Resource
	private ShopCartCouponService shopCartCouponService;
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private TradeInventoryExportService tradeInventoryExportService;
	@Resource
	private ItemExportService itemService;
	@Resource
	private QqCustomerService qqCustomerService;
	@Resource
	private ItemSkuPackageService itemSkuPackageService;
	@Resource
	private MallThemeService mallThemeService;
	/**
	 *
	 * <p>
	 * Discription:[购物车商品添加]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @param response
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ExecuteResult<String> add(HttpServletRequest request,
									 HttpServletResponse response, ProductInPriceDTO product) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request,
				Constants.REGION_CODE);

		if (ctoken == null || "".equals(ctoken)) {
			ctoken = UUID.randomUUID().toString();
			CookieHelper.setCookie(response, Constants.CART_TOKEN, ctoken);
		}

		product.setCtoken(ctoken);
		product.setUid(uid);
		if (product.getRegionId() == null || "".equals(product.getRegionId()))
			product.setRegionId(region);

		ExecuteResult<String> er = this.shopCartService.add(product);
		return er;
	}

	/**
	 *
	 * <p>
	 * Discription:[批量添加购物车商品]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @param response
	 * @param products
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/batchAdd")
	@ResponseBody
	public ExecuteResult<String> batchAdd(HttpServletRequest request,
						   HttpServletResponse response, String products) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		
		if (ctoken == null || "".equals(ctoken)) {
			ctoken = UUID.randomUUID().toString();
			CookieHelper.setCookie(response, Constants.CART_TOKEN, ctoken);
		}

		List<ProductInPriceDTO> pros = JSON.parseArray(products, ProductInPriceDTO.class);
		
		// 组装增值服务参数
		Map<Long, Long> valueAddedMap = new HashMap<Long, Long>();
		if (pros != null && pros.size() > 0) {
			for(ProductInPriceDTO product : pros){
				if (product.getValueAddedSkuIds() != null && product.getValueAddedSkuIds().size() > 0) {
					for (String valueAddedSkuIdStr : product.getValueAddedSkuIds()) {
						String[] kv = valueAddedSkuIdStr.split("-");
						Long itemId = Long.valueOf(kv[0]);
						Long skuId = Long.valueOf(kv[1]);
						valueAddedMap.put(itemId, skuId);
					}
					product.setValueAddedMap(valueAddedMap);
				}
			}
		}

		for (ProductInPriceDTO product : pros) {
			ExecuteResult<ItemDTO> itemResult = itemService.getItemById(product.getItemId());
			if(itemResult.isSuccess() && itemResult.getResult() != null){
				ItemDTO itemDTO = itemResult.getResult();
				if(itemDTO.getItemStatus() != 4){
					result.getErrorMessages().add("抱歉！该商品不是在售状态，暂时不能购买。");
					return result;
				}
				if(itemDTO.getAddSource() != null && itemDTO.getAddSource() == ItemAddSourceEnum.COMBINATION.getCode()){
					// 校验套装中单品是否下架
					ItemSkuPackageDTO packageDTO = new ItemSkuPackageDTO();
					packageDTO.setPackageItemId(product.getItemId());
					packageDTO.setPackageSkuId(product.getSkuId());
					packageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
					List<ItemSkuPackageDTO> packageDTOs = itemSkuPackageService.getPackages(packageDTO);
					if(packageDTOs != null && packageDTOs.size() > 0){
						for(ItemSkuPackageDTO itemSkuPackageDTO : packageDTOs){
							ExecuteResult<ItemDTO> subItemResult = itemService.getItemById(itemSkuPackageDTO.getSubItemId());
							if(subItemResult.isSuccess() && subItemResult.getResult() != null){
								ItemDTO subItem = subItemResult.getResult();
								if(subItem.getItemStatus() != 4){
									result.getErrorMessages().add("抱歉！该套装中存在非在售的组合单品，暂时不能购买。");
									return result;
								}
								if(subItem.getHasPrice() != 1){
									result.getErrorMessages().add("抱歉！该套装中存在非销售的组合单品，暂时不能购买。");
									return result;
								}
							}
						}
					}
				}
			}
			product.setCtoken(ctoken);
			product.setUid(uid);
			if (StringUtils.isBlank(product.getRegionId())){
				product.setRegionId(region);
			}
			this.shopCartService.add(product);
		}
		
		result.setResult(ctoken);
		return result;
	}
	
	
	/**
	 *
	 * <p>
	 * Discription:[购物车商品修改]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/edit")
	public ModelAndView edit(HttpServletRequest request, ProductInPriceDTO product) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		product.setCtoken(ctoken);
		product.setUid(uid);
		product.setRegionId(region);

		this.shopCartService.edit(product);
		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, null,region);
		// 获得店铺QQ客服
		List<ShopOutPriceDTO> cartShopList = myCart.getShops();
		myCart.setShops(StationUtil.getQQListByShopList(cartShopList));
		return new ModelAndView("/shopcart/cart", "myCart", myCart);
	}

	/**
	 *
	 * <p>
	 * Discription:[删除购物车商品]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/del")
	@ResponseBody
	public ModelAndView del(HttpServletRequest request, ProductInPriceDTO product) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		product.setCtoken(ctoken);
		product.setUid(uid);
		product.setRegionId(region);

		this.shopCartService.del(product);
		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, getFreightDeliveryType(request),region);
		return new ModelAndView("/shopcart/cart", "myCart", myCart);
	}

	@RequestMapping("/buyAgain")
	public String buyAgain(HttpServletRequest request, String orderId,
						   Model model) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		ExecuteResult<TradeOrdersDTO> tradeOrder = this.tradeOrderExportService.getOrderById(orderId);

		if (tradeOrder.isSuccess() && tradeOrder.getResult() != null) {
			TradeOrdersDTO tod = tradeOrder.getResult();
			ProductInPriceDTO product = new ProductInPriceDTO();
			for (TradeOrderItemsDTO toid : tod.getItems()) {
				product.setShopId(tod.getShopId());
				product.setSellerId(tod.getSellerId());
				product.setUid(tod.getBuyerId() + "");
				product.setCtoken(ctoken);

				product.setItemId(toid.getItemId());
				product.setSkuId(toid.getSkuId());
				product.setRegionId(toid.getAreaId() + "");
				product.setPromId(toid.getPromotionId());
				product.setQuantity(toid.getNum());
				
				this.shopCartService.add(product);
			}
		}
		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, getFreightDeliveryType(request),region);
		model.addAttribute("myCart", myCart);
		//return "/shopcart/shop_cart";
		//重定向:防止刷新页面时数量增加
		return "redirect:/shopCart/toCart";
	}

	/**
	 *
	 * <p>
	 * Discription:[购物车列表页跳转]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/toCart")
	public String toCart(HttpServletRequest request, Model model) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		if (StringUtils.isBlank(uid)) {
			model.addAttribute("userId", "");
		} else {
			model.addAttribute("userId", uid);
		}
		if(StringUtils.isBlank(region)){
			region="0";
		}
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, null,region);
		// 获得店铺QQ客服
		List<ShopOutPriceDTO> cartShopList = myCart.getShops();
		myCart.setShops(StationUtil.getQQListByShopList(cartShopList));
//		model.addAttribute("stationIdList", StationUtil.getStationIdListByShopList(cartShopList));
//		model.addAttribute("stationIdList", StationUtil.getQQListByShopList(cartShopList));
		model.addAttribute("myCart", myCart);
		return "/shopcart/shop_cart";
	}

	/**
	 *
	 * <p>
	 * Discription:[迷你购物车]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/mini")
	public ModelAndView mini(HttpServletRequest request) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, false, null,region);
		return new ModelAndView("/shopcart/mini_cart", "myCart", myCart);
	}

	@RequestMapping("/cart")
	@ResponseBody
	public ShopCartDTO getCart(HttpServletRequest request) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		return this.shopCartService.getMyCart(ctoken, uid, false, null,region);
	}

	/**
	 * 
	 * <p>Discription:[提交订单校验购物车商品、满减直降、优惠券、积分]</p>
	 * 必传参数：orderType,couponId,integral,paymentMethod,promotion
	 * Created on 2015年12月24日
	 * @param request
	 * @param model
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/validate")
	public String validate(HttpServletRequest request, Model model) {
		Long uid = WebUtil.getInstance().getUserId(request);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		ShopCartDTO myCart = null;
		// 订单类型：0询价订单 1协议订单 2正常
		String orderType_ = request.getParameter("orderType");

		Integer orderType = null;
		if (StringUtils.isNotBlank(orderType_) && StringUtils.isNumeric(orderType_)) {
			orderType = Integer.valueOf(orderType_);
		}
		if(orderType != null){
			if (orderType == OrderTypeEnum.NORMAL.getCode()) {
				// 获取当前用户购物车商品
				List<ProductInPriceDTO> products = shopCartService.getAllProducts(ctoken, uid == null ? null : uid + "");
				// 购物车中是否存在集采的商品
				boolean isExistCentralPurchasing = false;
				if(products != null && products.size() > 0){
					for(ProductInPriceDTO p : products){
						if(p.getChecked() && p.getActivitesDetailsId() != null){
							isExistCentralPurchasing = true;
							break;
						}
					}
				}
				
				 if (isExistCentralPurchasing) {// 如果购物车中有集采商品，那么还走原来的逻辑（getMyCart）
					myCart = this.shopCartService.getMyCart(ctoken, uid == null ? null : uid + "", false, null,region);
				} else {
					// 优惠券编号
			        String couponId = request.getParameter("couponId");
			        // 积分
			        String integral_ = request.getParameter("integral");
			        // 满减
			        String promotion = request.getParameter("promotion");
			        // 直降
			        String markdown = request.getParameter("markdown");
			        Integer integral = null;
			        if(StringUtils.isNotBlank(integral_) && StringUtils.isNumeric(integral_)){
			        	integral = Integer.parseInt(integral_);
			        } 
			        if(StringUtils.isNotBlank(promotion)){
			        	promotion = promotion.substring(0, promotion.length()-1);
			        }
			        if(StringUtils.isNotBlank(markdown)){
			        	markdown = markdown.substring(0, markdown.length()-1);
			        }
			        HttpSession session = request.getSession();
					String sessionProducts = (String) session.getAttribute("products");
					String promptly = "";
					List<ProductInPriceDTO> pros_re = new ArrayList<ProductInPriceDTO>();
					if (null != sessionProducts && !"".equals(sessionProducts)) {
						pros_re = batchAddPro(request, sessionProducts);
						promptly = "promptly";
					}
					UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
					// 企业支付或个人支付（1个人 2企业）
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
					myCart = this.shopCartService.getMyCartToOrderView(pros_re, ctoken, uid == null ? null : uid + "", false, promptly, couponId,
							paymentMethod, integral, null,
							com.camelot.openplatform.common.Constants.PLATFORM_KY_ID,promotion,markdown);
				}
			} else if (orderType == OrderTypeEnum.INQUIRY.getCode() || orderType == OrderTypeEnum.AGREEMENT.getCode()) {
				//myCart = this.shopCartService.getMyCart(ctoken, uid == null ? null : uid + "", false, null);
			}
		}
//		myCart.getUnusualMsg().add("优惠券已过期");
//		myCart.getUnusualMsg().add("积分不足");
//		myCart.getShops().get(1).getProducts().get(0).setUnusualState(4);
//		myCart.getShops().get(1).getProducts().get(0).getUnusualMsg().add("123333");
		LOG.info(JSON.toJSONString(myCart));
		model.addAttribute("myCart", myCart);
		return "/shopcart/cart_validate";
	}

	/**
	 *
	 * <p>
	 * Discription:[购物车店铺商品全选]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @param shopid
	 * @param checked
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/checkShop")
	public ModelAndView checkShop(HttpServletRequest request, Long shopid,
								  Boolean checked) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		this.shopCartService.checkShop(ctoken, uid, shopid, checked);

		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, getFreightDeliveryType(request),region);
		// 获得店铺QQ客服
		List<ShopOutPriceDTO> cartShopList = myCart.getShops();
		myCart.setShops(StationUtil.getQQListByShopList(cartShopList));
		return new ModelAndView("/shopcart/cart", "myCart", myCart);
	}
	
	/**
	 * 
	 * <p>Discription:[领取优惠券刷新购物车]</p>
	 * Created on 2016年2月27日
	 * @param request
	 * @return
	 * @author:[李伟龙]
	 */
	@RequestMapping("/getCoupons")
	public ModelAndView getCoupons(HttpServletRequest request){
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);

		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, getFreightDeliveryType(request),region);
		// 获得店铺QQ客服
		List<ShopOutPriceDTO> cartShopList = myCart.getShops();
		myCart.setShops(StationUtil.getQQListByShopList(cartShopList));
		return new ModelAndView("/shopcart/cart", "myCart", myCart);
	}

	/**
	 *
	 * <p>
	 * Discription:[购物车商品全选]
	 * </p>
	 * Created on 2015年3月10日
	 *
	 * @param request
	 * @param checked
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/checkAll")
	public ModelAndView checkAll(HttpServletRequest request, Boolean checked) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		this.shopCartService.checkAll(ctoken, uid, checked);

		ShopCartDTO myCart = this.shopCartService.getMyCart(ctoken, uid, true, getFreightDeliveryType(request),region);
		// 获得店铺QQ客服
		List<ShopOutPriceDTO> cartShopList = myCart.getShops();
		myCart.setShops(StationUtil.getQQListByShopList(cartShopList));
		return new ModelAndView("/shopcart/cart", "myCart", myCart);
	}

	/**
	 * @desc： 获得用户收货地址及默认选中地址
	 * @param uid
	 * @param ckAddrId
	 * @return
	 */
	private JSONObject getAddress(String uid, String ckAddrId) {
		DataGrid<AddressInfoDTO> dgAddress = this.addressInfoService
				.queryAddressinfo(Long.valueOf(uid));
		JSONArray addresses = new JSONArray();
		JSONObject defAddress = null;
		JSONObject ckAddress = null;

		for (int i = 0; i < dgAddress.getRows().size(); i++) {
			AddressInfoDTO addr = dgAddress.getRows().get(i);
			JSONObject address = JSON.parseObject(JSON.toJSONString(addr));
			ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService
					.queryNameById(Integer.valueOf(addr.getProvicecode()));
			address.put("provicename", erProvice.getResult().getName());
			ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService
					.queryNameById(Integer.valueOf(addr.getCitycode()));
			address.put("cityname", erCity.getResult().getName());
			//不获取三级地域
//			ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService
//					.queryNameById(Integer.valueOf(addr.getCountrycode()));
//			address.put("countryname", erCountry.getResult().getName());
			address.put("countryname", "");
			if (ckAddrId != null && !"".equals(ckAddrId)
					&& addr.getId().compareTo(Long.valueOf(ckAddrId)) == 0)
				ckAddress = address;
			if (i == 0)
				defAddress = address;
			if (addr.getIsdefault() == 1)
				defAddress = address;
			addresses.add(address);
		}
		if (ckAddress == null)
			ckAddress = defAddress;

		JSONObject json = new JSONObject();
		json.put("defAddress", ckAddress);
		json.put("addresses", addresses);
		return json;
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
	public ModelAndView toOrderDetail(HttpServletRequest request, String buyType) {
		ModelAndView mav = new ModelAndView();
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		
		// 0询价订单 1协议订单 2正常
		Integer orderType = 2;//Integer.parseInt(request.getParameter("orderType"));
		String contractNo = request.getParameter("contractNo");
		mav.addObject("orderType", orderType);
		mav.addObject("contractNo", contractNo);
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
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
			//取二级地域原来取三级地域
			regionid = ckAddress.getString("citycode");
		}
		this.shopCartService.changeRegion(ctoken, uid, regionid, orderType, buyType);

		// 查询用户最近使用的发票
		mav.addObject("invoiceDTO", invoiceExportService.queryRecentInvoice(Long.valueOf(uid)).getResult());
		// 付款方式设置
		mav.addObject("shipmentType", request.getParameter("shipmentType"));
		mav.addObject("payPeriod", request.getParameter("payPeriod"));

		UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
		// 企业支付或个人支付（1个人 2企业）
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
		List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
		if (user.getParentId() != null) {
			UserDTO dto = userExportService.queryUserById(user.getParentId());
			if (dto != null) {
				ChildUserListPost childUserListPost=this.createUserList(dto);
				childUserListPosts.add(childUserListPost);
			}
		}
		if (user != null) {
			ChildUserListPost childUserListPost=this.createUserList(user);
			childUserListPosts.add(childUserListPost);
		}
		Integer moduleType = 1;
		ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService
				.queryChildUserList(Long.valueOf(uid), moduleType, null);
		DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();

		List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
		for (ChildUserDTO childUserDTO : childUserDTOs) {
			String resourceName = "";
			List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO
					.getUserMallResourceList();
			for (UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs) {
				resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
			}
			if (resourceName.length() > 0) {
				resourceName = resourceName.substring(0, resourceName.length() - 1);
			}

			ChildUserListPost childUserListPost = new ChildUserListPost();
			childUserListPost.setShopId(childUserDTO.getShopId());
			childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
			childUserListPost.setUserId(childUserDTO.getUserId());
			childUserListPost.setUsername(childUserDTO.getUsername());
			childUserListPost.setResourceIds(resourceName);
			childUserListPost.setNickName(childUserDTO.getNickName());
			childUserListPosts.add(childUserListPost);
		}
		mav.addObject("childUserListPosts", childUserListPosts);
		mav.addObject("paymentMethod", paymentMethod);
		mav.addObject("userstatus", user.getUserstatus());
		mav.addObject("userType", user.getUsertype());
		Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
		ShopCartDTO myCart = null;
		List<ProductInPriceDTO> products = new ArrayList<ProductInPriceDTO>();
		String promptly = "";
		String buyNowProducts = redisDB.get("buyNowProducts");
		if (StringUtils.isNotBlank(buyType) && "NOW".equals(buyType) && StringUtils.isNotBlank(buyNowProducts)) {
			products = batchAddPro(request, buyNowProducts);
			promptly = "promptly";
			request.setAttribute("buyType", "NOW");
		} else {
			// 获取当前用户购物车商品
			products = shopCartService.getAllProducts(ctoken, uid);
		}
		// 购物车中是否存在集采的商品
		boolean isExistCentralPurchasing = false;
		if(products != null && products.size() > 0){
			for(ProductInPriceDTO p : products){
				if(p.getChecked() && p.getActivitesDetailsId() != null){
					isExistCentralPurchasing = true;
					break;
				}
			}
		}
		
		 if (isExistCentralPurchasing) {// 如果购物车中有集采商品，那么还走原来的逻辑（getMyCart）
			myCart = this.shopCartService.getMyCart(ctoken, uid, false, freightDeliveryType,region);
			// 标记属性为集采的商品，用来在订单核对页禁用所有活动
			mav.addObject("isCentralPurchasing", true);
		} else {
			// 优惠券编号
	        String couponId = request.getParameter("couponId");
	        // 积分
	        String integral_ = request.getParameter("integral");
	        Integer integral = null;
	        if(StringUtils.isNotBlank(integral_) && StringUtils.isNumeric(integral_)){
	        	integral = Integer.parseInt(integral_);
	        }
			myCart = this.shopCartService.getMyCartToOrderView(products, ctoken, uid, false, promptly, couponId,
					paymentMethod, integral, freightDeliveryType,
					com.camelot.openplatform.common.Constants.PLATFORM_KY_ID,null,null);
			mav.addObject("couponId", couponId);
			mav.addObject("integral", myCart.getIntegral());
		}
		
		mav.addObject("freightDeliveryType", freightDeliveryType);
		mav.addObject("myCart", myCart);
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
				ExecuteResult<ShopCartDTO> validateResult = shopCartCouponService.validateCoupon(Long.parseLong(uid), dto.getCouponId(), paymentMethod, myCart, null);
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
		// 查询积分可兑换金额
		IntegralConfigDTO configDTO = new IntegralConfigDTO();
		configDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_EXCHANGE.getCode());
		configDTO.setPlatformId(0);
		ExecuteResult<DataGrid<IntegralConfigDTO>> configResult = integralConfigService.queryIntegralConfigDTO(configDTO, null);
		BigDecimal exchangeRate = BigDecimal.ZERO;
		if (configResult.isSuccess() 
				&& configResult.getResult() != null 
				&& configResult.getResult().getRows() != null
				&& configResult.getResult().getRows().size() > 0
				&& configResult.getResult().getRows().get(0).getExchangeRate() != null) {
			exchangeRate = configResult.getResult().getRows().get(0).getExchangeRate();
		}
		mav.addObject("exchangeRate", exchangeRate);
		// 查询当前用户的可用积分
		ExecuteResult<Long> totalIntegralResult = userIntegralTrajectoryService.queryTotalIntegral(Long.parseLong(uid));
		if (totalIntegralResult.isSuccess() && totalIntegralResult.getResult() != null) {
			Long totalIntegral = totalIntegralResult.getResult();
			mav.addObject("totalIntegral", totalIntegral);
			// 查询每笔订单可使用的最大积分
			IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
			integralConfigDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_USING.getCode());
			integralConfigDTO.setPlatformId(0);
			ExecuteResult<DataGrid<IntegralConfigDTO>> er = integralConfigService.queryIntegralConfigDTO(integralConfigDTO, null);
			if (er.isSuccess() 
					&& er.getResult() != null
					&& er.getResult().getRows() != null 
					&& er.getResult().getRows().size() > 0) {
				IntegralConfigDTO dto = er.getResult().getRows().get(0);
				Long useIntegral = dto.getUseIntegral();
				if(useIntegral == null){
					useIntegral = 0L;
				}
				if(totalIntegral.longValue() < useIntegral.longValue()){
					useIntegral = totalIntegral;
				}
				// 计算实际最多可使用积分
				if(exchangeRate != null && exchangeRate.compareTo(BigDecimal.ZERO) > 0){
					// 该订单最多可使用的积分
					Integer newIntegral = myCart.getPayTotal().add(myCart.getIntegralDiscount()).divide(exchangeRate).setScale(0, RoundingMode.FLOOR).intValue();
					if(useIntegral.intValue() > newIntegral.intValue()){
						useIntegral = newIntegral.longValue();
					}
				} else{
					useIntegral = 0L;
				}
				mav.addObject("maxIntegral", useIntegral);
			} else{
				mav.addObject("maxIntegral", 0);
			}
		} else{
			mav.addObject("totalIntegral", 0);
			mav.addObject("maxIntegral", 0);
		}
		mav.setViewName("/shopcart/order_detail");
		return mav;
	}

	@RequestMapping("/toOrderView")
	public ModelAndView toOrderView(HttpServletRequest request, Model model) {
		// 订单类型：0询价订单 1协议订单 2正常
		String orderType_ = request.getParameter("orderType");
		// 协议编号或询价编号
		String contractNo = request.getParameter("contractNo");

		Integer orderType = null;
		if (StringUtils.isNotBlank(orderType_) && StringUtils.isNumeric(orderType_)) {
			orderType = Integer.valueOf(orderType_);
		}
		ModelAndView mav = new ModelAndView();
		if(orderType != null){
			if (orderType == OrderTypeEnum.NORMAL.getCode()) {
				mav = this.toOrderDetail(request, null);
			} else if (orderType == OrderTypeEnum.INQUIRY.getCode() || orderType == OrderTypeEnum.AGREEMENT.getCode()) {
				mav = this.toOrderDetail2(request, null);
			}
		}
		mav.addObject("orderType", orderType);
		mav.addObject("contractNo", contractNo);
		mav.setViewName("/shopcart/order_view");
		/*** 取userid ***/
		Long parentId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userService.queryUserById(parentId);
		model.addAttribute("status", userDTO.getUserstatus());
		model.addAttribute("type", userDTO.getUsertype());
		model.addAttribute("userId", parentId);
		return mav;
	}
	

	/**
	 *
	 * <p>Description: [询价订单或协议订单订单核对]</p>
	 * Created on 2015年10月12日
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/toOrderDetail2")
	public ModelAndView toOrderDetail2(HttpServletRequest request, String buyType) {
		ModelAndView mav = new ModelAndView();
		// 0询价订单 1协议订单 2正常
		Integer orderType = Integer.parseInt(request.getParameter("orderType"));
		String contractNo = request.getParameter("contractNo");
		mav.addObject("orderType", orderType);
		mav.addObject("contractNo", contractNo);
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String regionid = CookieHelper.getCookieVal(request,Constants.REGION_CODE);

		// 收货地址变更
		String ckAddrId = request.getParameter("address");
		LOG.debug("CHECK ADDRESS:" + ckAddrId);
		JSONObject json = this.getAddress(uid, ckAddrId);
		JSONObject ckAddress = json.getJSONObject("defAddress");
		mav.addObject("addresses", json.get("addresses"));
		mav.addObject("defAddress", ckAddress);
		LOG.debug("DEF ADDRESS:" + JSON.toJSONString(ckAddress));

		if (ckAddress != null){
			//取二级地域原来取三级地域
			regionid = ckAddress.getString("citycode");
		}
		this.shopCartService.changeRegion(ctoken, uid, regionid, orderType, buyType);

		// 查询用户最近使用的发票
		mav.addObject("invoiceDTO", invoiceExportService.queryRecentInvoice(Long.valueOf(uid)).getResult());

		// 付款方式设置
		mav.addObject("shipmentType", request.getParameter("shipmentType"));
		mav.addObject("payPeriod", request.getParameter("payPeriod"));

		if (orderType == OrderTypeEnum.AGREEMENT.getCode()) {// 协议订单
			// 设置协议订单默认的延期支付周期为协议账期
			ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
			contractPaymentTermDTO.setContractNo(contractNo);
			contractPaymentTermDTO.setActiveFlag("1");
			ExecuteResult<ContractPaymentTermDTO> executeResult = protocolExportService
					.queryByContractPaymentTerm(contractPaymentTermDTO);
			if (executeResult.isSuccess() && executeResult.getResult() != null) {
				ContractPaymentTermDTO dto = executeResult.getResult();
				int paymentType = dto.getPaymentType();// 0天 1月
				if (paymentType == 0) {
					mav.addObject("payPeriod", dto.getPaymentDays());
				} else {
					mav.addObject("payPeriod", dto.getPaymentDays() * 30);
				}
			}
			// 设置默认的订单补充说明为协议备注
			ContractInfoDTO contractInfoDTO = new ContractInfoDTO();
			contractInfoDTO.setContractNo(contractNo);
			contractInfoDTO.setActiveFlag("1");
			ExecuteResult<ContractInfoDTO> result = protocolExportService.queryByContractInfo(contractInfoDTO);
			if (result.isSuccess() && result.getResult() != null) {
				ContractInfoDTO dto = result.getResult();
				mav.addObject("memo", dto.getRemark());
			}
		}
		UserDTO user = this.userService.queryUserById(Long.valueOf(uid));
		// 企业支付或个人支付（1个人 2企业）
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
		List<ChildUserListPost> childUserListPosts = new ArrayList<ChildUserListPost>();
		if (user.getParentId() != null) {
			UserDTO dto = userExportService.queryUserById(user.getParentId());
			if (dto != null) {
				ChildUserListPost childUserListPost = new ChildUserListPost();
				childUserListPost.setShopId(dto.getShopId());
				childUserListPost.setUserId(dto.getUid());
				childUserListPost.setUsername(dto.getUname());
				childUserListPost.setNickName(dto.getNickname());
				childUserListPosts.add(childUserListPost);
			}
		}
		if (user != null) {
			ChildUserListPost childUserListPost = new ChildUserListPost();
			childUserListPost.setShopId(user.getShopId());
			childUserListPost.setUserId(user.getUid());
			childUserListPost.setUsername(user.getUname());
			childUserListPost.setNickName(user.getNickname());
			childUserListPosts.add(childUserListPost);
		}
		Integer moduleType = 1;
		ExecuteResult<DataGrid<ChildUserDTO>> executeResult = userStorePermissionExportService
				.queryChildUserList(Long.valueOf(uid), moduleType, null);
		DataGrid<ChildUserDTO> dataGrid = executeResult.getResult();

		List<ChildUserDTO> childUserDTOs = dataGrid.getRows();
		for (ChildUserDTO childUserDTO : childUserDTOs) {
			String resourceName = "";
			List<UserMallResourceDTO> userMallResourceDTOs = childUserDTO
					.getUserMallResourceList();
			for (UserMallResourceDTO userMallResourceDTO : userMallResourceDTOs) {
				resourceName = resourceName + userMallResourceDTO.getResourceName() + "|";
			}
			if (resourceName.length() > 0) {
				resourceName = resourceName.substring(0, resourceName.length() - 1);
			}

			ChildUserListPost childUserListPost = new ChildUserListPost();
			childUserListPost.setShopId(childUserDTO.getShopId());
			childUserListPost.setUpdateTime(childUserDTO.getUpdateTime());
			childUserListPost.setUserId(childUserDTO.getUserId());
			childUserListPost.setUsername(childUserDTO.getUsername());
			childUserListPost.setResourceIds(resourceName);
			childUserListPost.setNickName(childUserDTO.getNickName());
			childUserListPosts.add(childUserListPost);
		}
		mav.addObject("childUserListPosts", childUserListPosts);
		mav.addObject("paymentMethod", paymentMethod);
		mav.addObject("userstatus", user.getUserstatus());
		mav.addObject("userType", user.getUsertype());

		Map<Long, Integer> freightDeliveryType = getFreightDeliveryType(request);
		ShopCartDTO myCart = this.shopCartService.getMyOrder(uid, freightDeliveryType);
		mav.addObject("myCart", myCart);
		mav.addObject("freightDeliveryType", freightDeliveryType);
		mav.setViewName("/shopcart/order_detail");
		return mav;
	}
	
	/**
	 * 判断用户积分是否足以兑换积分商城的商品
	 * @param id 广告ID或广告ID
	 * @param integralType 1：轮播 2：广告
	 * @param request
	 * @return
	 */
	@RequestMapping("/validateIntegral")
	@ResponseBody
	public ExecuteResult<Integer> validateIntegral(Long id, Integer integralType, HttpServletRequest request){
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		try{
			Long userId = WebUtil.getInstance().getUserId(request);
			if(userId != null){
				// 查询当前用户的可用积分
				ExecuteResult<Long> totalIntegralResult = userIntegralTrajectoryService.queryTotalIntegral(userId);
				if (totalIntegralResult.isSuccess()) {
					if(totalIntegralResult.getResult() == null){
						totalIntegralResult.setResult(0L);
					}
				} else{
					LOG.error("查询积分异常：userID：{}", userId);
					result.addErrorMessage("查询积分异常！");
					return result;
				}
				Integer integral = Integer.MAX_VALUE;
				Long skuId=null;
				// 根据广告ID，查询商品积分信息，页面显示
				if (IntegralItemTypeEnum.BANNER.getId().equals(integralType)) {
					MallBannerDTO mallBannerDTO = this.mallBannerService.getMallBannerById(id);
					integral = mallBannerDTO.getIntegral();
					skuId=mallBannerDTO.getSkuId();
				} else if (IntegralItemTypeEnum.AD.getId().equals(integralType)) {
					MallAdDTO mallAdDTO = this.mallAdvertisService.getMallAdById(id);
					integral = mallAdDTO.getIntegral();
					skuId=mallAdDTO.getSkuId();
				}
				// 判断用户积分是否足够兑换商品
				if(totalIntegralResult.getResult().intValue() < integral.intValue()){
					result.addErrorMessage("您的可用积分为" + totalIntegralResult.getResult() + "，不足以兑换该商品！");
					return result;
				}
				Long uid = WebUtil.getInstance().getUserId(request);
				UserDTO user=null;
		    	if(uid != null){
		    		user = this.userExportService.queryUserById(uid);
		    	}		
				if(skuId!=null){
					ExecuteResult<ItemDTO> ext=itemService.getItemBySkuId(skuId);
					if(user!=null && user.getShopId()!=null){
						if(user.getShopId().equals(ext.getResult().getShopId())){
							 result.addErrorMessage("买家不允许购买自己店铺的商品");
							 return result;
						}
					}
		        }
		   }
		} catch(Exception e){
			LOG.error("\n 方法[{}]，异常：[{}]", "ShopCartController-validateIntegral", e.toString());
			result.addErrorMessage("服务器异常");
		}
		return result;
	}
	
	/**
	 * 积分兑换提交订单
	 * @param request
	 * @param dto
	 * @param model
	 * @return
	 */
	@RequestMapping("/orderSubmitByIntegral")
	@ResponseBody
	public ExecuteResult<String> orderSubmitByIntegral(HttpServletRequest request, TradeOrdersDTO dto,Model model ) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		String count = request.getParameter("count");
		String skuId = request.getParameter("skuId");
		if(StringUtils.isEmpty(skuId)){
			result.addErrorMessage("订单异常，请重新操作！");
			LOG.error("SKUID不能为空");
			return result;
		}
		ExecuteResult<ItemDTO> itemResult = this.itemExportService.getItemBySkuId(Long.parseLong(skuId));
		if(!itemResult.isSuccess() && itemResult.getResult() != null){
			result.addErrorMessage("订单异常，请重新操作！");
			LOG.error("查询不到商品信息");
			return result;
		}
		Long shopId = itemResult.getResult().getShopId();
		ExecuteResult<ShopDTO> shopResult = this.shopService.findShopInfoById(shopId);
		if(!shopResult.isSuccess() && shopResult.getResult() != null){
			result.addErrorMessage("订单异常，请重新操作！");
			LOG.error("无法获取店铺信息");
			return result;
		}
		Long userId = WebUtil.getInstance().getUserId(request);
		ShopDTO shop = shopResult.getResult();
		//如果是积分兑换商品，则直接扣除积分，订单状态修改为已支付待发货
		if(PayMethodEnum.PAY_INTEGRAL.getCode() == dto.getPaymentMethod()){
			//查询当前用户的可用积分
			ExecuteResult<Long> totalIntegralResult = userIntegralTrajectoryService.queryTotalIntegral(userId);
			if(!totalIntegralResult.isSuccess()&& totalIntegralResult.getResult() != null){
				LOG.error("查询积分异常：userID："+userId);
				result.addErrorMessage("订单异常，请重新操作！");
				return result;
			}
			Long totalIntegral = totalIntegralResult.getResult()==null?0l:totalIntegralResult.getResult();
			if(totalIntegral < dto.getIntegral().longValue()){
				LOG.error("积分不足：userID："+userId);
				result.addErrorMessage("积分不足，无法兑换！");
				return result;
			}
			UserDTO user=null;
	    	if(userId != null){
	    		user = this.userExportService.queryUserById(userId);
	    	}
			ExecuteResult<ItemDTO> ext=itemService.getItemBySkuId(Long.parseLong(skuId));
			if(user!=null && user.getShopId()!=null){
				if(user.getShopId().equals(ext.getResult().getShopId())){
					 result.addErrorMessage("买家不允许购买自己店铺的商品");
					 return result;
				}
			}
			dto.setBuyerId(userId);
			dto.setPaymentTime(new Date());
			dto.setPaid(2);
			dto.setPaymentType(PayBankEnum.INTEGRAL.getQrCode());
	        //店铺订单
			TradeOrderCreateDTO createDTO = new TradeOrderCreateDTO();
			TradeOrdersDTO tod = new TradeOrdersDTO();
	        BeanUtils.copyProperties(dto, tod);
	//                totalDiscount=shop.getTotal().subtract(shop.getPayTotal());
	        tod.setShopId(shop.getShopId());
	        tod.setSellerId(shop.getSellerId());
	        tod.setShipmentType(1);
			tod.setItems(new LinkedList<TradeOrderItemsDTO>());
			
			TradeOrderItemsDTO itemDto = new TradeOrderItemsDTO();
			itemDto.setItemId(itemResult.getResult().getItemId());//  SKU所属品类ID
			itemDto.setSkuId(Long.parseLong(skuId));//  SKU
			itemDto.setPayPrice(new BigDecimal(dto.getIntegral()+"").divide(new BigDecimal(count)));// 支付价格（优惠后的价格）
            itemDto.setNum(Integer.parseInt(count));//  数量
			itemDto.setPayPriceTotal(new BigDecimal(dto.getIntegral()+""));//优惠后金额
			itemDto.setCid(itemResult.getResult().getCid());
			itemDto.setIntegral(dto.getIntegral());
			
			tod.getItems().add(itemDto);
			createDTO.getSubOrders().add(tod);
			dto.setSellerId(itemResult.getResult().getSellerId());
			dto.setShopId(shopId);
			dto.setShipmentType(1);
			dto.setPaymentTime(new Date());
			createDTO.setParentOrder(dto);
		
			ExecuteResult<TradeOrderCreateDTO> tradeOrderResult = this.tradeOrderExportService.createOrder(createDTO);
			//订单提交成功，扣除买家积分
			if(tradeOrderResult.isSuccess()){
				TradeOrderCreateDTO tradeOrderCreateDTO = tradeOrderResult.getResult();
				TradeOrdersDTO tradeOrder = tradeOrderCreateDTO.getSubOrders().get(0);
				//给买家返回积分
	        	UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
	        	userIntegralTrajectoryDTO.setIntegralType(3);
	        	userIntegralTrajectoryDTO.setOrderId(tradeOrder.getOrderId());
	        	userIntegralTrajectoryDTO.setIntegralValue(-dto.getIntegral().longValue());
	        	userIntegralTrajectoryDTO.setUsingTime(new Date());
	        	userIntegralTrajectoryDTO.setUserId(tradeOrder.getBuyerId());
	        	userIntegralTrajectoryDTO.setShopId(tradeOrder.getShopId());
	        	userIntegralTrajectoryDTO.setInsertBy(tradeOrder.getBuyerId());
	        	userIntegralTrajectoryDTO.setInsertTime(new Date());
	        	ExecuteResult<UserIntegralTrajectoryDTO> integralResult = userIntegralTrajectoryService.addUserIntegralTrajectoryDTO(userIntegralTrajectoryDTO);
			}
		}
		result.setResultMessage("订单提交成功");
		return result;
	}
	
	/**
	 *
	 * <p>
	 * Discription:[提交订单]
	 * </p>
	 * Created on 2015年3月12日
	 *
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("/orderSubmit")
	// @ResponseBody
	public String orderSubmit(HttpServletRequest request, TradeOrdersDTO dto,Model model ) {
		boolean access = true;
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		// 0询价订单 1协议订单 2正常
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		if (dto.getOrderType() == null
				|| (dto.getOrderType() != OrderTypeEnum.INQUIRY.getCode()
						&& dto.getOrderType() != OrderTypeEnum.AGREEMENT.getCode() 
						&& dto.getOrderType() != OrderTypeEnum.NORMAL.getCode())) {
			executeResult.addErrorMessage("订单类型为空");
			access = false;
		} else if (dto.getOrderType() == OrderTypeEnum.INQUIRY.getCode() || dto.getOrderType() == OrderTypeEnum.AGREEMENT.getCode()) {
			if (StringUtils.isBlank(dto.getContractNo())) {
				if (dto.getOrderType() == OrderTypeEnum.INQUIRY.getCode()) {
					executeResult.addErrorMessage("询价订单编号为空");
				} else {
					executeResult.addErrorMessage("协议订单编号为空");
				}
				access = false;
			}
		}
		if (!access) {
			model.addAttribute("executeResult", executeResult);
			return "/shopcart/submit_result";
		}
		String shopPromoId=request.getParameter("shopPromoId");
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		// String paymentMethod = request.getParameter("paymentMethod");
		dto.setBuyerId(Long.valueOf(uid));
		// 将支付方式保存到订单中
		// dto.setPaymentType(Integer.parseInt(paymentMethod));
		dto.setNotApproved(true);
		
		List<ProductInPriceDTO> products = new ArrayList<ProductInPriceDTO>();
		String promptly = "";
		String buyNowProducts = redisDB.get("buyNowProducts");
		// 立即购买或加入购物车
		String buyType = request.getParameter("buyType");
		if (StringUtils.isNotBlank(buyType) && "NOW".equals(buyType) && StringUtils.isNotBlank(buyNowProducts)) {
			products = batchAddPro(request, buyNowProducts);
			promptly = "promptly";
			request.setAttribute("buyType", "NOW");
		}
        // 优惠券编号
        String couponId = request.getParameter("couponId");
        // 积分
        String integral_ = request.getParameter("integral");
        // 满减
        String promotion = request.getParameter("promotion");
        // 直降
        String markdown = request.getParameter("markdown");
        Integer integral = null;
        if(StringUtils.isNotBlank(integral_) && StringUtils.isNumeric(integral_)){
        	integral = Integer.parseInt(integral_);
        } 
        if(StringUtils.isNotBlank(promotion)){
        	promotion = promotion.substring(0, promotion.length()-1);
        }
        if(StringUtils.isNotBlank(markdown)){
        	markdown = markdown.substring(0, markdown.length()-1);
        }
        OrderParameter parameter=new OrderParameter();
        parameter.setProducts(products);
        parameter.setPromptly(promptly);
        parameter.setShopPromoId(shopPromoId);
        parameter.setCouponId(couponId);
        parameter.setIntegral(integral);
        parameter.setCtoken(ctoken);
        parameter.setUid(uid);
        parameter.setDto(dto);
        parameter.setFreightDeliveryType(getFreightDeliveryType(request));
        parameter.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_KY_ID);
        parameter.setPromotion(promotion);
        parameter.setMarkdown(markdown);
		ExecuteResult<String> result = this.shopCartService.subimtOrder(parameter);
		LOG.debug("===========>" + JSON.toJSONString(result));
		// 将结果放入request中，仅供站内信发送拦截器使用
		request.setAttribute("result", result);
		// return result;
		model.addAttribute("executeResult", result);
		if (result.isSuccess() && dto.getShipmentType() == 1) {
			return "redirect:/shopCart/payView?orderNo=" + result.getResult();
		} else {
			return "/shopcart/submit_result";
		}
	}

	@RequestMapping("/orderApproveSubmit")
	// @ResponseBody
	public String orderApproveSubmit(HttpServletRequest request,
									 TradeOrdersDTO dto, Model model, String needApprove) {
		// 0询价订单 1协议订单 2正常
		String orderType = request.getParameter("orderType");
		boolean access = true;
		if (StringUtils.isBlank(orderType)) {
			access = false;
		} else {
			if (OrderTypeEnum.INQUIRY.getCode() != Integer.parseInt(orderType)
					&& OrderTypeEnum.AGREEMENT.getCode() != Integer.parseInt(orderType)
					&& OrderTypeEnum.NORMAL.getCode() != Integer.parseInt(orderType)) {
				access = false;
			} else if (OrderTypeEnum.INQUIRY.getCode() == Integer.parseInt(orderType)
					|| OrderTypeEnum.AGREEMENT.getCode() == Integer.parseInt(orderType)) {
				String contractNo = request.getParameter("contractNo");
				if (StringUtils.isBlank(contractNo)) {
					access = false;
				}
			}
		}
		if (!access) {
			ExecuteResult<String> executeResult = new ExecuteResult<String>();
			executeResult.addErrorMessage("参数错误");
			model.addAttribute("executeResult", executeResult);
			return "/shopcart/pay_approve_view";
		}
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		dto.setBuyerId(Long.valueOf(uid));
		dto.setNotApproved(false);// 订单需要审核
		dto.setApproveStatus("0");// 待审核状态
		String shopPromoId=request.getParameter("shopPromoId");
		HttpSession session = request.getSession();
        String products=(String)session.getAttribute("products");
        String promptly="";
        List<ProductInPriceDTO> pros_re = new ArrayList<ProductInPriceDTO>();
        if(null != products && !"".equals(products)){
            pros_re = batchAddPro(request, products);
            promptly="promptly";
        }
        // 优惠券编号
        String couponId = request.getParameter("couponId");
        // 积分
        String integral_ = request.getParameter("integral");
        // 满减
        String promotion = request.getParameter("promotion");
        // 满减
        String markdown = request.getParameter("markdown");
        Integer integral = null;
        if(StringUtils.isNotBlank(integral_) && StringUtils.isNumeric(integral_)){
        	integral = Integer.parseInt(integral_);
        } 
        if(StringUtils.isNotBlank(promotion)){
        	promotion = promotion.substring(0, promotion.length()-1);
        }
        if(StringUtils.isNotBlank(markdown)){
        	markdown = markdown.substring(0, markdown.length()-1);
        }
        OrderParameter parameter=new OrderParameter();
        parameter.setProducts(pros_re);
        parameter.setPromptly(promptly);
        parameter.setShopPromoId(shopPromoId);
        parameter.setCouponId(couponId);
        parameter.setIntegral(integral);
        parameter.setCtoken(ctoken);
        parameter.setUid(uid);
        parameter.setDto(dto);
        parameter.setFreightDeliveryType(getFreightDeliveryType(request));
        parameter.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_KY_ID);
        parameter.setPromotion(promotion);
        parameter.setMarkdown(markdown);
		ExecuteResult<String> result = this.shopCartService.subimtOrder(parameter);
		LOG.debug("===========>" + JSON.toJSONString(result));
		// 将结果放入request中，仅供站内信发送拦截器使用
		request.setAttribute("result", result);
		model.addAttribute("executeResult", result);
		return "/shopcart/pay_approve_view";
	}

	@RequestMapping("/payView")
	public String toPayView(HttpServletRequest request, String orderNo,
							Model model) {
		LOG.debug("PAY VIEW ORDER NO:" + orderNo);
		model.addAttribute("orderNo", orderNo);

		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		ExecuteResult<UserInfoDTO> erUi = this.userExtendsService
				.findUserInfo(Long.valueOf(uid));
		int accountstatus = -1;
		if (erUi.isSuccess() && erUi.getResult() != null) {
			if (erUi.getResult().getUserCiticDTO() != null)
				accountstatus = erUi.getResult().getUserCiticDTO()
						.getAccountState();
		}
		model.addAttribute("accountstatus", accountstatus);

		ExecuteResult<TradeOrdersDTO> erTo = this.tradeOrderExportService
				.getOrderById(orderNo);
		if (erTo.isSuccess()) {
			String orderType = "1";
			// if( erTo.getResult().getParentOrderId().compareTo(
			// Long.valueOf(0) ) == 0 ){
			// orderType = "0";
			// }
			if ("0".equals(erTo.getResult().getParentOrderId())) {
				orderType = "0";
			}
			// 将选择的支付方式传递到页面，1 个人支付，2：企业支付
			model.addAttribute("paymentMethod", erTo.getResult()
					.getPaymentMethod());
			model.addAttribute("payType", erTo.getResult().getShipmentType());
			model.addAttribute("orderType", orderType);
			model.addAttribute("payTotal", erTo.getResult().getPaymentPrice());
		}
		return "/shopcart/pay_view";
	}

	@RequestMapping("/payOrder")
	public String payOrder(String orderNo, String type, String paymentMethod,
						   Model model) {
		ExecuteResult<Integer> er = this.shopCartService.payOrder(orderNo,
				type, paymentMethod);
//		 er.setResultMessage(er.getResultMessage().replace("z1.printhome.com:8081",
//		 "127.0.0.1:8080/mall-web"));
		//er.setResultMessage("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"+er.getResultMessage());
		model.addAttribute("er", er);
		if (PayBankEnum.WXPC.name().equals(paymentMethod)) {
			if (er.isSuccess()) {
				ExecuteResult<OrderInfoPay> orderInfo = paymentService
						.findTransByOrderNo(orderNo);
				model.addAttribute("orderInfo", orderInfo);
				return "/shopcart/pay_redirect2";
			}
		}
		return "/shopcart/pay_redirect";
	}

	// 同步
	@RequestMapping("/payCallBack/{payBank}")
	public String payCallBack(@PathVariable("payBank") String payBank,
							  HttpServletRequest request, Model model) {
		LOG.info("银行回调同步方法");
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getParameterNames();
		Map<String, String> params = new HashMap<String, String>();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			params.put(name, value);
		}
		LOG.info("银行回调同步方法 参数解析..");
		params.put("isNotify", "false");
		LOG.info("银行回调同步方法  调用执行接口");
		ExecuteResult<OrderInfoPay> er = this.paymentService.payResult(params,
				payBank);
		LOG.debug("支付成功回调：" + JSON.toJSONString(er));
		model.addAttribute("executeResult", er);
		request.setAttribute("er", er);
		return "/shopcart/pay_result";
	}
	
	// 同步
	@RequestMapping("/payCallBack_wx")
	public String payCallBack_wx(Model model) {
		@SuppressWarnings("rawtypes")
		ExecuteResult er = new ExecuteResult();
		model.addAttribute("executeResult", er);
		
		return "/shopcart/pay_result";
	}

	// 异步回调
	@RequestMapping("/payResult/{payBank}")
	@ResponseBody
	public String payResult(@PathVariable("payBank") String payBank,
							HttpServletRequest request,HttpServletResponse response) {
		LOG.info("银行回调异步方法");
		ExecuteResult<OrderInfoPay> er = new ExecuteResult<OrderInfoPay>();
		if (BankSettleTypeEnum.WXPC.getLable().equals(payBank)||BankSettleTypeEnum.WX.getLable().equals(payBank)) {
			try {
				ServletInputStream result = request.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				int length = -1;
				byte[] buff = new byte[1024];
				while ((length = result.read(buff)) != -1) {
					baos.write(buff, 0, length);
				}
				String xml = baos.toString();
				baos.close();
				ExecuteResult<Map<String, String>> erParam = new ExecuteResult<Map<String,String>>();
				erParam = this.paymentService.parseXml(xml);
				if(!erParam.getResult().isEmpty()){
					List<OrderInfoPay> trans = paymentService.findChildTransByOutTrades(erParam.getResult().get("out_trade_no"));
					if(null!=trans&&trans.size()>0&&trans.get(0).getStatus()!=2){
						er = this.paymentService.payResult(erParam.getResult(), payBank);
						if(er.isSuccess()){
							String key = "payResult"+er.getResult().getOutTradeNo();
							this.redisDB.set(key, "success");
						}
					}else{
						er.addErrorMessage("已完成支付！");
					}
				}else{
					er.setErrorMessages(erParam.getErrorMessages());
				}
			} catch (IOException e) {
				e.printStackTrace();
				er.addErrorMessage("银行回调失败，失败原因：" + e.getMessage());
				LOG.info("银行回调失败，失败原因：" , e);
			}
		} else {
			@SuppressWarnings("unchecked")
			Enumeration<String> names = request.getParameterNames();
			Map<String, String> params = new HashMap<String, String>();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String value = request.getParameter(name);
				params.put(name, value);
			}
			params.put("isNotify", "true");
			er = this.paymentService.payResult(params, payBank);
		}
		request.setAttribute("er", er);
		LOG.debug("支付成功回调：" + JSON.toJSONString(er));
		LOG.info("\n 方法[{}]，出参：[{}]","ShopCartController-payResult",JSON.toJSONString(er));
		return er.getResultMessage();
	}

	/**
	 * 退款异步回调接口
	 * @author zhouzhijun
	 * @param payBank 退款银行
	 * @param request
	 * @return
	 * @since 2015-10-13
	 */
	@RequestMapping("/refundResult/{payBank}")
	@ResponseBody
	public String refundResult(@PathVariable("payBank") String payBank,
							   HttpServletRequest request) {
		LOG.info("\n 方法[{}]，入参：[{},{}]","ShopCartController-refundResult",payBank,request);
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		LOG.info("PayBankEnum.CUP.getLable().equals(payBank)=="+PayBankEnum.CUP.getLable().equals(payBank));
		if ("AP".equals(payBank) || "CUP".equals(payBank))  {
			@SuppressWarnings("unchecked")
			Enumeration<String> names = request.getParameterNames();
			Map<String, String> params = new HashMap<String, String>();
			while (names.hasMoreElements()) {
				String name = names.nextElement();
				String value = request.getParameter(name);
				params.put(name, value);
			}
			LOG.info("----------"+params.toString());
			er = this.tradeReturnExportService.refundResult(params,payBank);
		}
		if(er.isSuccess()){
			LOG.debug("退款回调成功");
		}
		return er.getResultMessage();
	}
	
	@RequestMapping("/apRefundResult/{payBank}")
	@ResponseBody
	public String apRefundResult(@PathVariable("payBank") String payBank,
							   HttpServletRequest request) {
		LOG.info("\n 方法[{}]，入参：[{},{}]","ShopCartController-apRefundResult",payBank,request);
		ExecuteResult<Integer> er = new ExecuteResult<Integer>();
		
		@SuppressWarnings("unchecked")
		Enumeration<String> names = request.getParameterNames();
		Map<String, String> params = new HashMap<String, String>();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = request.getParameter(name);
			params.put(name, value);
		}
		er = this.tradeReturnExportService.apRefundResult(params, payBank);
	
		
		if(er.isSuccess()){
			LOG.debug("退款回调成功");
		}
		return er.getResultMessage();
	}


	/**
	 *
	 * <p>
	 * Discription:[判断用户是否登陆]
	 * </p>
	 * Created on 2015年6月23日
	 *
	 * @param request
	 * @return
	 * @author:[董其超]
	 */
	@RequestMapping("/ifLogin_shopCustomer")
	@ResponseBody
	public Map<String, Object> ifLogin(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = LoginToken.getLoginToken(request);
		if (StringUtils.isBlank(token)) {
			map.put("ifLogin_shopCustomer", false);
		}
		RegisterDTO registerDTO = userService.getUserByRedis(token);
		if (registerDTO == null) {
			map.put("ifLogin_shopCustomer", false);
		}
		map.put("ifLogin_shopCustomer", true);
		String stationId = qqCustomerService.getQqCustomerByIds(null, Constants.TYPE_ECM);
		map.put("stationId", stationId);
		return map;
	}
	/**
	 * 查询微信支付结果
	 */
	@RequestMapping("/queryResult")
	@ResponseBody
	public ExecuteResult<String> queryResult(String key){
		LOG.info("\n 方法[{}]，入参：[{}]","ShopCartController-queryResult",key);
		ExecuteResult<String> er = new ExecuteResult<String>();
		String result = this.redisDB.get(key);
		if(null!=result){
			er.setResult(result);
			this.redisDB.del(key);
		}else{
			er.addErrorMessage("未支付或支付失败");
		}
		LOG.info("\n 方法[{}]，出参：[{},key:{}]","ShopCartController-queryResult",JSON.toJSONString(er),key);
		return er;
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
	@ResponseBody
	@RequestMapping("/checkCITICStatus")
	public ExecuteResult<Boolean> checkCITICStatus(HttpServletRequest request,HttpServletResponse response){
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		UserInfoDTO backUserInfoDTO = userExtendsService.findUserInfo(Long.parseLong(uid)).getResult();
		if(null==backUserInfoDTO){
			er.addErrorMessage("用户中信账号信息不存在！");
			LOG.info("用户中信账号信息不存在！");
		}
		if(null==backUserInfoDTO.getUserCiticDTO()){
			er.addErrorMessage("中信账号不存在!");
			LOG.info("中信账号不存在!");
		}
		if(2==backUserInfoDTO.getUserCiticDTO().getAccountState()||3==backUserInfoDTO.getUserCiticDTO().getAccountState()){
			er.setResult(true);
		}else{
			er.setResult(false);
			LOG.info("中信账号未审核通过！");
		}
		return er;
	}
	
	
	
	/**
	 * 
	 * <p>Discription:[校验订单中的商品库存]</p>
	 * Created on 2016年1月18日
	 * @param orderId
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/checkOrderItemsInventory")
	@ResponseBody
	public ExecuteResult<Boolean> checkOrderItemsInventory(String orderId, HttpServletRequest request){
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			result.addErrorMessage("登录状态失效，请重新登录");
			return result;
		}
		if(StringUtils.isNotBlank(orderId)){
			ExecuteResult<TradeOrdersDTO> er = tradeOrderExportService.getOrderById(orderId);
			if(er.isSuccess() && er.getResult() != null){
				List<TradeOrderItemsDTO> itemsDTOs = er.getResult().getItems();
				for(TradeOrderItemsDTO itemsDTO : itemsDTOs){
					// 查询商品库存
					ExecuteResult<TradeInventoryDTO> executeResult = tradeInventoryExportService.queryTradeInventoryBySkuId(itemsDTO.getSkuId());
					if (executeResult.isSuccess() && executeResult.getResult() != null
							&& executeResult.getResult().getTotalInventory() != null
							&& executeResult.getResult().getTotalInventory().intValue() > 0) {
					} else{
						ExecuteResult<ItemDTO> itemExecuteResult = itemExportService.getItemById(itemsDTO.getItemId());
						if(itemExecuteResult.isSuccess() && itemExecuteResult.getResult() != null){
							result.addErrorMessage("商品【" + itemExecuteResult.getResult().getItemName() + "】已售罄，无法购买");
						} else{
							result.addErrorMessage("商品不存在");
						}
					}
				}
			} else{
				result.addErrorMessage("订单不存在");
			}
		} else{
			result.addErrorMessage("订单号不能为空");
		}
		return result;
	}
	
	private ChildUserListPost  createUserList(UserDTO dto){
		ChildUserListPost childUserListPost = new ChildUserListPost();
		childUserListPost.setShopId(dto.getShopId());
		childUserListPost.setUserId(dto.getUid());
		childUserListPost.setUsername(dto.getUname());
		childUserListPost.setNickName(dto.getNickname());
		return childUserListPost;
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
	
	
	/**
	 *
	 * <p>Discription:[立即购买前验证]</p>
	 * Created on 2016年2月16日
	 * @param request
	 * @return 状态 1验证通过 0 不通过
	 * @author:[王宏伟]
	 */
	@ResponseBody
    @RequestMapping("/promptlybuyProducts")
    public ExecuteResult<String> promptlybuyProducts(HttpServletRequest request){
		ExecuteResult<String> result = new ExecuteResult<String>();
		String products = request.getParameter("products");
		List<Product> pros = JSON.parseArray(products, Product.class);
		if (null != pros && pros.size() > 0) {
			for(Product product : pros){
				ExecuteResult<ItemDTO> itemResult = itemService.getItemById(product.getItemId());
				if(itemResult.isSuccess() && itemResult.getResult() != null){
					ItemDTO itemDTO = itemResult.getResult();
					if(itemDTO.getItemStatus() != 4){
						result.getErrorMessages().add("抱歉！该商品不是在售状态，暂时不能购买。");
						return result;
					}
					if(itemDTO.getAddSource() != null && itemDTO.getAddSource() == ItemAddSourceEnum.COMBINATION.getCode()){
						// 校验套装中单品是否下架
						ItemSkuPackageDTO packageDTO = new ItemSkuPackageDTO();
						packageDTO.setPackageItemId(product.getItemId());
						packageDTO.setPackageSkuId(product.getSkuId());
						packageDTO.setAddSource(ItemAddSourceEnum.NORMAL.getCode());
						List<ItemSkuPackageDTO> packageDTOs = itemSkuPackageService.getPackages(packageDTO);
						if(packageDTOs != null && packageDTOs.size() > 0){
							for(ItemSkuPackageDTO itemSkuPackageDTO : packageDTOs){
								ExecuteResult<ItemDTO> subItemResult = itemService.getItemById(itemSkuPackageDTO.getSubItemId());
								if(subItemResult.isSuccess() && subItemResult.getResult() != null){
									ItemDTO subItem = subItemResult.getResult();
									if(subItem.getItemStatus() != 4){
										result.getErrorMessages().add("抱歉！该套装中存在非在售的组合单品，暂时不能购买。");
										return result;
									}
									if(subItem.getHasPrice() != 1){
										result.getErrorMessages().add("抱歉！该套装中存在非销售的组合单品，暂时不能购买。");
										return result;
									}
								}
							}
						}
					}
				}
				// 获取该sku的库存
				ExecuteResult<TradeInventoryDTO> tradeInventoryDTO = tradeInventoryExportService
						.queryTradeInventoryBySkuId(product.getSkuId());
				if (null != tradeInventoryDTO) {
					if (product.getQuantity() > tradeInventoryDTO.getResult().getTotalInventory()) {
						result.addErrorMessage("库存不足，不支持购买！");
						return result;
					} else {
						redisDB.addObject("buyNowProducts", products, 60 * 60 * 24);
						return result;
					}
				} else {
					result.addErrorMessage("库存不足，不支持购买！");
					return result;
				}
			}
		}
		return result;
    }
	
	
	/**
	 *
	 * <p>Discription:[添加立即购买商品]</p>
	 * Created on 2015年3月10日
	 * @param request
	 * @param products
	 * @return
	 * @author:
	 */
	@RequestMapping("/batchAddPro")
	@ResponseBody
	public List<ProductInPriceDTO> batchAddPro(HttpServletRequest request,String products) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ptoken = null;//CookieHelper.getCookieVal(request, Constants.PROMPTLY_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);

		/*if( ptoken == null || "".equals(ptoken) ){
			ptoken = UUID.randomUUID().toString();
			CookieHelper.setCookie(response, Constants.PROMPTLY_TOKEN, ptoken);
		}*/

		List<ProductInPriceDTO> pros = JSON.parseArray(products, ProductInPriceDTO.class);
		// 组装增值服务参数
		Map<Long, Long> valueAddedMap = new HashMap<Long, Long>();
		if (pros != null && pros.size() > 0) {
			for(ProductInPriceDTO product : pros){
				if (product.getValueAddedSkuIds() != null && product.getValueAddedSkuIds().size() > 0) {
					for (String valueAddedSkuIdStr : product.getValueAddedSkuIds()) {
						String[] kv = valueAddedSkuIdStr.split("-");
						Long itemId = Long.valueOf(kv[0]);
						Long skuId = Long.valueOf(kv[1]);
						valueAddedMap.put(itemId, skuId);
					}
					product.setValueAddedMap(valueAddedMap);
				}
			}
		}
		List<ProductInPriceDTO> pros_re = new ArrayList<ProductInPriceDTO>();

		for (ProductInPriceDTO product : pros) {
			product.setCtoken(ptoken);
			product.setUid(uid);
			if (StringUtils.isBlank(product.getRegionId())) {
				product.setRegionId(region);
			}
			// this.shopCartService.addPro(product); //合并购物车 去掉合并
			pros_re.add(product);
		}
		return pros_re;
	}
	
	
	/**
	 *
	 * <p>Discription:[立即购买跳转到商品核对页]</p>
	 * Created on 2016年2月16日
	 * @param request
	 * @return
	 * @author:[王宏伟]
	 */
	@RequestMapping("/toOrderViewPromptly")
	public ModelAndView toOrderViewPromptly(HttpServletRequest request, Model model) {
		// 协议编号或询价编号
		String contractNo = request.getParameter("contractNo");
		Integer orderType = 2;
		ModelAndView mav = new ModelAndView();
		mav = this.toOrderDetail(request, "NOW");
		mav.addObject("orderType", orderType);
		mav.addObject("contractNo", contractNo);
		mav.setViewName("/shopcart/order_view");
		/*** 取userid ***/
		Long parentId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userService.queryUserById(parentId);
		model.addAttribute("status", userDTO.getUserstatus());
		model.addAttribute("type", userDTO.getUsertype());
		model.addAttribute("userId", parentId);
		return mav;
	}
	
	/**
	 * 
	 * <p>Discription:[校验子站是否存在]</p>
	 * Created on 2016年3月1日
	 * @param cityCode
	 * @return
	 * @author:[李伟龙]
	 */
	@RequestMapping("checkMallTheme")
	@ResponseBody
	public ExecuteResult<Boolean> checkMallTheme(String cityCode) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		if(null != cityCode && !"".equals(cityCode)){
			MallThemeDTO mallThemeDTO = new MallThemeDTO();
			mallThemeDTO.setCityCode(Long.parseLong(cityCode));
			mallThemeDTO.setStatus(1);
			DataGrid<MallThemeDTO> mallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO,null, new Pager());
			List<MallThemeDTO> list  = mallThemeList.getRows();
			if(list != null && !list.isEmpty() && list.size() > 0){
				result.setResult(true);
			}else{
				result.setResult(false);
				result.setResultMessage("无服务商，不允许购买");
			}
		}else{
			result.setResult(false);
			result.setResultMessage("未获取到地域信息，无法进行子站校验");
		}
		return result;
	}
	
}

