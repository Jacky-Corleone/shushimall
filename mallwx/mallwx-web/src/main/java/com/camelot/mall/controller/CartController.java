package com.camelot.mall.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.mall.Constants;
import com.camelot.mall.service.ShopCartService;
import com.camelot.mall.shopcart.Product;
import com.camelot.mall.shopcart.Shop;
import com.camelot.mall.shopcart.ShopCart;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;


/**
 * 
 * <p>Description: [描述该类概要功能介绍:购物车]</p>
 * Created on 2015-5-21 下午12:44:47
 * @author  <a href="mailto: zihanmin@camelotchina.com">訾瀚民</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */

@Controller
@RequestMapping("/cart")
public class CartController {
	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private ShopCartService shopCartService;
	@Resource
	private AddressInfoService addressInfoService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private RedisDB redisDB;


	/**
	 * 
	 * <p>Discription:[方法功能中文描述：跳转到购物车页面，购物车其他数据使用ajax加载]</p>
	 * Created on 2015-5-21 下午12:44:47
	 * @param request
	 * @param response
	 * @param session
	 * @param model
	 * @return
	 * @author:[马国平]
	 */
	
	@RequestMapping("tocart")
	public String toCart(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model) {
		//检查用户的登录，获取用户登录信息，存在某个地方
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);

		if (StringUtils.isEmpty(uid)) {
			//没有登录，待处理
			//生成一个ctoken
			return "cart/cart";
		} else {
			//登陆之后，直接跳转到页面，数据加载放到前台

			//String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
			//ShopCart myCart = this.shopCartService.getMyCart(ctoken, uid, true);
			//model.addAttribute("myCart",myCart);
			//获得店铺站点
			//List<Shop> cartShopList = myCart.getShops();
			//model.addAttribute("stationIdList",StationUtil.getStationIdListByShopList(cartShopList));
			//DataGrid<AddressInfoDTO> dgAddress = this.addressInfoService.queryAddressinfo(Long.valueOf(uid));

			/**
			 * 获得收货地址列表
			 */

			/*JSONObject dgAddress = this.getAddress(uid, "");
			model.addAttribute("addresses", dgAddress);
			System.out.println(dgAddress.toJSONString());*/
			return "cart/cart";
		}

	}
	

	/**
	 * 
	 * <p>Discription:[方法功能中文描述：合并购物车]</p>
	 * Created on 2016年1月28日
	 * @param request
	 * @param product
	 * @return
	 * @author:[马国平]
	 */
	
	
	@RequestMapping("edit")
	public ModelAndView getCartProducts(HttpServletRequest request, Product product) {
		//检查用户的登录，获取用户登录信息，存在某个地方
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		product.setCtoken(ctoken);
		product.setUid(uid);
		product.setRegionId(region);
		this.shopCartService.edit(product);
		
		Map<String, Object> modelMap = new HashMap<String, Object>();
		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		modelMap.put("imgUrlPre", imgUrlPre);
		if (StringUtils.isEmpty(uid)) {
			//没有登录
			ShopCart myCart = this.shopCartService.getMyCart(ctoken, uid, true);
			modelMap.put("myCart", myCart);
			ModelAndView mav = new ModelAndView("cart/cartProducts", modelMap);
			return mav;
		} else {
			//已登录
			//检查是否有未登录的token，然后合并
			ShopCart myCart = this.shopCartService.getMyCart(ctoken, uid, true);
			modelMap.put("myCart", myCart);
			ModelAndView mav = new ModelAndView("cart/cartProducts", modelMap);
			return mav;
		}
	}
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：合并购物车]</p>
	 * Created on 2016年1月28日
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @author:[马国平]
	 */

	@RequestMapping("getCartProducts")
	public ModelAndView getCartProducts(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//检查用户的登录，获取用户登录信息，存在某个地方
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		modelMap.put("imgUrlPre", imgUrlPre);
		if (StringUtils.isEmpty(uid)) {
			//没有登录
			String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
			ShopCart myCart = this.shopCartService.getMyCart(ctoken, uid, true);
			modelMap.put("myCart", myCart);
			ModelAndView mav = new ModelAndView("cart/cartProducts", modelMap);
			return mav;
		} else {
			//已登录
			//检查是否有未登录的token，然后合并
			String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
			ShopCart myCart = this.shopCartService.getMyCart(ctoken, uid, true);
			modelMap.put("myCart", myCart);
			ModelAndView mav = new ModelAndView("cart/cartProducts", modelMap);
			return mav;
		}
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述：检验购物车的数据是否可以提交到订单确认页面]</p>
	 * Created on 2015-6-18 下午12:47:35
	 * @param request
	 * @return
	 * @author:[马国平]
	 */
	@RequestMapping("validate")
	@ResponseBody
	public String validate(HttpServletRequest request) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		ShopCart myCart = this.shopCartService.getMyCart(ctoken, uid, false);
		JSONObject jo = new JSONObject();
		if(StringUtils.isEmpty(uid)){
			jo.put("success", false);
			jo.put("act_code", 0);//需要登录
			return jo.toJSONString();
		}
		StringBuffer sb = new StringBuffer();
		if (myCart.getUnusualCount() > 0) {
			sb.append("以下商品不可购买");
			for (Shop shop : myCart.getShops()) {
				if (shop.getUnusualCount() > 0) {
					for (Product pro : shop.getProducts()) {
						if (pro.getUnusualState() > 0) {
							sb.append(pro.getTitle());
							sb.append("：");
							sb.append(pro.getUnusualMsg());
							sb.append("\n");
						}
					}
				}
			}
			jo.put("success", false);
			jo.put("msg", sb.toString());
		} else {
			jo.put("success", true);
		}
		String jostr = jo.toJSONString();
		return jostr;
	}


	/**
	 * 
	 * <p>Discription:[方法功能中文描述： 获取购物车页面下方热销商品]</p>
	 * Created on 2015-6-12 上午11:20:17
	 * @param model
	 * @return
	 * @author:[马国平]
	 */
	@RequestMapping("getHotProductPage")
	public String getHotProductPage(Model model) {
		/**
		 * 加载热销商品
		 */
		Pager<MallAdDTO> page = new Pager<MallAdDTO>();
		page.setRows(4);
		page.setPage(1);

		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(5);
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		model.addAttribute("jcProducts", advertises.getRows());

		//热销商品
		MallAdQueryDTO adQuery = new MallAdQueryDTO();
		adQuery.setStatus(1);
		adQuery.setAdType(1);
		DataGrid<MallAdDTO> dg = this.mallAdvertisService.queryMallAdList(page, adQuery);
		model.addAttribute("hotProducts", dg.getRows());

		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		model.addAttribute("imgUrlPre", imgUrlPre);
		return "cart/hotProducts";
	}



	/**
	 * 
	 * <p>Discription:[方法功能中文描述：获得用户收货地址及默认选中地址]</p>
	 * Created on 2016年1月28日
	 * @param uid
	 * @param ckAddrId
	 * @return
	 * @author:[马国平]
	 */
	
	
	private JSONObject getAddress(String uid, String ckAddrId) {
		DataGrid<AddressInfoDTO> dgAddress = this.addressInfoService.queryAddressinfo(Long.valueOf(uid));
		JSONArray addresses = new JSONArray();

		for (int i = 0; i < dgAddress.getRows().size(); i++) {
			AddressInfoDTO addr = dgAddress.getRows().get(i);
			JSONObject address = JSON.parseObject(JSON.toJSONString(addr));
			ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService.queryNameById(Integer.valueOf(addr
					.getProvicecode()));
			address.put("provicename", erProvice.getResult().getName());
			ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService.queryNameById(Integer.valueOf(addr
					.getCitycode()));
			address.put("cityname", erCity.getResult().getName());
			ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService.queryNameById(Integer.valueOf(addr
					.getCountrycode()));
			address.put("countryname", erCountry.getResult().getName());
			if (ckAddrId != null && !"".equals(ckAddrId) && addr.getId().compareTo(Long.valueOf(ckAddrId)) == 0) {
				address.put("ischeck", 1);
			}
			if (i == 0) {
				address.put("isdefault", 1);
			}
			if (addr.getIsdefault() == 1) {
				address.put("isdefault", 1);
			}
			addresses.add(address);
		}

		JSONObject json = new JSONObject();
		json.put("addresses", addresses);
		return json;
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2016年1月28日
	 * @param model
	 * @return
	 * @author:[马国平]
	 */
	
	@RequestMapping("/recommend")
	public String recommend(Model model) {
		Pager<MallAdDTO> page = new Pager<MallAdDTO>();
		page.setRows(5);
		page.setPage(1);

		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(5);
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		model.addAttribute("jcProducts", advertises.getRows());

		MallAdQueryDTO adQuery = new MallAdQueryDTO();
		adQuery.setStatus(1);
		adQuery.setAdType(1);
		DataGrid<MallAdDTO> dg = this.mallAdvertisService.queryMallAdList(page, adQuery);
		model.addAttribute("hotProducts", dg.getRows());
		return "/common/recommend";
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述：购物车商品修改]</p>
	 * Created on 2015-6-12 下午3:28:09
	 * @param request
	 * @param response
	 * @param session
	 * @param product
	 * @return
	 * @author:[马国平]
	 */
	
	@RequestMapping("/changeProduct")
	public ModelAndView changeProduct(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			Product product) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		product.setCtoken(ctoken);
		product.setUid(uid);
		product.setRegionId(region);
		this.shopCartService.edit(product);
		ModelAndView mav = this.getCartProducts(request, response, session);
		return mav;
	}

	/**
	 *
	 * <p>Discription:[购物车店铺商品全选]</p>
	 * Created on 2015年3月10日
	 * @param request
	 * @param shopid
	 * @param checked
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/checkShop")
	public ModelAndView checkShop(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			Long shopid,
			Boolean checked) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		this.shopCartService.checkShop(ctoken, uid, shopid, checked);
		return this.getCartProducts(request, response, session);
	}

	/**
	 *
	 * <p>Discription:[购物车商品全选]</p>
	 * Created on 2015年3月10日
	 * @param request
	 * @param checked
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/checkAll")
	public ModelAndView checkAll(
			HttpServletRequest request,
			HttpServletResponse response,
			HttpSession session,
			Boolean checked) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		this.shopCartService.checkAll(ctoken, uid, checked);
		return this.getCartProducts(request, response, session);
	}

	/**
	 *
	 * <p>Discription:[购物车商品添加]</p>
	 * Created on 2015年3月10日
	 * @param request
	 * @param response
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("add2Cart")
	@ResponseBody
	public String add2Cart(HttpServletRequest request, HttpServletResponse response, Product product) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		JSONObject jores = new JSONObject();
		if (StringUtils.isEmpty(uid)) {
			jores.put("success", false);
			jores.put("msg", "请登录");
			return jores.toJSONString();
		}
		if (ctoken == null || "".equals(ctoken)) {
			ctoken = UUID.randomUUID().toString();
			CookieHelper.setCookie(response, Constants.CART_TOKEN, ctoken);
		}

		product.setCtoken(ctoken);
		product.setUid(uid);
		if (product.getRegionId() == null || "".equals(product.getRegionId())) {
			product.setRegionId(region);
		}
		try {
			ExecuteResult<String> er = this.shopCartService.add(product);
			String ermsg = er.getResult();
			jores.put("success", true);
			jores.put("msg", ermsg);
		} catch (Exception e) {
			jores.put("success", false);
			jores.put("msg", e.getMessage());

		}

		return jores.toJSONString();
	}

	/**
	 *
	 * <p>Discription:[批量添加购物车商品]</p>
	 * Created on 2015年3月10日
	 * @param request
	 * @param response
	 * @param products
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/batchAdd")
	@ResponseBody
	public String batchAdd(HttpServletRequest request,HttpServletResponse response,String products) {
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);

		if(StringUtils.isEmpty(ctoken)){
			ctoken = UUID.randomUUID().toString();
			CookieHelper.setCookie(response, Constants.CART_TOKEN, ctoken);
		}

		List<Product> pros = JSON.parseArray(products, Product.class);

		for( Product product: pros ){
			product.setCtoken(ctoken);
			product.setUid(uid);
			if(StringUtils.isEmpty(product.getRegionId())){	
				product.setRegionId(region);
			}
			
			this.shopCartService.add(product);
	
		}
		return ctoken;
	}
	
	/**
	 *
	 * <p>Discription:[删除购物车商品]</p>
	 * Created on 2015年3月10日
	 * @param request
	 * @param product
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/del")
	@ResponseBody
	public ModelAndView del(HttpServletResponse response,HttpSession session, HttpServletRequest request,Product product){
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		product.setCtoken(ctoken);
		product.setUid(uid);
		product.setRegionId(region);
		try{
			ExecuteResult<String> result = this.shopCartService.del(product);
			logger.info(")))))");
		}catch(Exception e){
			e.printStackTrace();
		}
		ModelAndView mav = this.getCartProducts(request, response, session);
		return mav;
	}
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：删除购物车，redisDB的值]</p>
	 * Created on 2016年1月28日
	 * @param key
	 * @return
	 * @author:[訾瀚民]
	 */
	@RequestMapping("delEdit")
	@ResponseBody
	public String redisEdit(@RequestParam("key")String key) {
		redisDB.del(key);
		return "true";
	}
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述：获取购物车，redisDB的值]</p>
	 * Created on 2016年1月28日
	 * @param key
	 * @return
	 * @author:[訾瀚民]
	 */
	@RequestMapping("getRedis")
	@ResponseBody
	public String getRedis(@RequestParam("key")String key) {
		return redisDB.get(key);
	}

	/**
	 * 
	 * <p>Discription:[方法功能中文描述：]</p>
	 * Created on 2016年1月28日
	 * @return
	 * @author:[訾瀚民]
	 */
	@RequestMapping("toAdd2Cart")
	public String toAdd2Cart() {

		return "cart/addtocart";
	}
	
	/**
	 * 
	 * <p>Discription:根据复选框内容进行批量删除购物</p>
	 * Created on 2015年11月13日15:27:57
	 * @param product 缓存商品信息
	 * @param objskuId 复选框选择商品
	 * @return
	 * @author:[訾瀚民]
	 */
	@RequestMapping("/delAll")
	@ResponseBody
	public ModelAndView delAll(HttpServletResponse response,HttpSession session, HttpServletRequest request,Product product,String objskuId){
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		product.setCtoken(ctoken);
		product.setUid(uid);
		product.setRegionId(region);
		try{
		      this.shopCartService.delAll(product,objskuId);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		ModelAndView mav = this.getCartProducts(request, response, session);
		return mav;
	}
	
	
	
	
}
