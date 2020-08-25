package com.camelot.mall.sellcenter.mallshop;

import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.camelot.basecenter.service.AddressBaseService;
/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月4日
 * @author  <a href="mailto: "></a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/shop")
public class shopInfoController {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(shopInfoController.class);
	
	@Resource
	private AddressBaseService addressBaseService;
	/**
	 * 
	 * <p>Discription:卖家中心基本信息</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("shopBaseInfo")
	public String editShopInfo(Model model){
		//商铺信息
		
		 return "/sellcenter/mallshop/baseInfo";
	}
	/**
	 * 
	 * <p>Discription:店铺信息修改跳转</p>
	 Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("toEdit")
	public String shopInfoPage(Model model,String shopId){
		//初始化省份
//		List<AddressBase> addressList = addressBaseService.queryAddressBase("0");
//		model.addAttribute("addressList", addressList);
		System.out.println("==========="+shopId);
		//商铺信息
	
		return "sellcenter/mallshop/editShopInfo";
	}
	/**
	 * 
	 * <p>Discription:卖家中心权限管理</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("toAuthoList")
	public String shopAuthoList(Model model){
		
		return "sellcenter/mallshop/authorityMage";
	}
	/**
	 * 
	 * <p>Discription:商品管理商品列表</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	
	@RequestMapping("goodsList")
	public String goodsList(){
		
		return "sellcenter/goodsMage/goodsList";
	}
	/**
	 * 
	 * <p>Discription:商品管理商品添加</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("addNew")
	public String addNew(){
		
		return "sellcenter/goodsMage/goodsAdd";
	}
	/**
	 * 
	 * <p>Discription:信息设置-安全信息设置</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("step1")
	public String verifyIndex(){
		
		return "sellcenter/information/authenticate2";
	}
	/**
	 * 
	 * <p>Discription:信息设置-安全信息设置</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("step2")
	public String step2(){
		
		return "sellcenter/information/newAuth";
	}
	/**
	 * 
	 * <p>Discription:信息设置-安全信息设置</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("stepOK")
	public String stepOK(){
		
		return "sellcenter/information/authOk";
	}
	/**
	 * 
	 * <p>Discription:信息设置-类目管理</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("category")
	public String category(){
		
		return "sellcenter/mallshop/categoryMage";
	}
}
