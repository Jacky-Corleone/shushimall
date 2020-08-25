package com.camelot.mall.home;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.service.ShopFavouriteExportService;
import com.camelot.usercenter.service.UserExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月10日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/favourite")
public class FavouriteController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private FavouriteService favouriteService;
	@Resource
	private ItemFavouriteExportService itemFavouriteService;
	@Resource
	private ShopFavouriteExportService shopFavouriteService;
	@Resource
	private UserExportService userService;
	

	@RequestMapping("/shops")
	public String shops(HttpServletRequest request,Pager<JSONObject> pager,ShopFavouriteDTO favourite,Model model){

		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		favourite.setUserId(Integer.valueOf(uid));
		
		DataGrid<JSONObject> dg = this.favouriteService.shops(pager, favourite);

		LOG.debug("收藏店铺列表："+JSON.toJSONString(dg));
		
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		LOG.debug(JSON.toJSONString(pager));
		
		model.addAttribute("pager", pager);
		return "/home/favourite_shop";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/products")
	public String products(HttpServletRequest request,Pager<JSONObject> pager,ItemFavouriteDTO favourite,Model model){
		
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		favourite.setUserId(Integer.valueOf(uid));
		
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
		return "/home/favourite_product";
	}
	
	@RequestMapping("/addItem")
	@ResponseBody
	public ExecuteResult<String> addItem(HttpServletRequest request,ItemFavouriteDTO favourite){
		ExecuteResult<String> er = new ExecuteResult<String>();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		favourite.setUserId(Integer.valueOf(uid));
		er = this.itemFavouriteService.add(favourite);
		return er;
	}
	
	@RequestMapping("/addShop")
	@ResponseBody
	public ExecuteResult<String> addShop(HttpServletRequest request,ShopFavouriteDTO favourite,Model model){
		ExecuteResult<String> er = new ExecuteResult<String>();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		favourite.setUserId(Integer.valueOf(uid));
		er = this.shopFavouriteService.add(favourite);
		DataGrid<JSONObject> dgShops = this.favouriteService.shops(new Pager(), favourite);
		if(dgShops!=null && dgShops.getTotal()>0){
			model.addAttribute("favouriteShop", "true");
		}else{
			model.addAttribute("favouriteShop", "false");
		}
		return er;
	}
	
	@RequestMapping("/delItem")
	@ResponseBody
	public ExecuteResult<String> delItem(HttpServletRequest request,@RequestParam("ids[]")List<String> ids){
		ExecuteResult<String> er = new ExecuteResult<String>();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		
		for(String id:ids){
			this.itemFavouriteService.del(id, uid);
		}
		return er;
	}
	
	@RequestMapping("/delShop")
	@ResponseBody
	public ExecuteResult<String> delShop(HttpServletRequest request,@RequestParam("ids[]")List<String> ids){
		ExecuteResult<String> er = new ExecuteResult<String>();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		
		for(String id:ids){
			this.shopFavouriteService.del(id, uid);
		}
		return er;
	}
}
