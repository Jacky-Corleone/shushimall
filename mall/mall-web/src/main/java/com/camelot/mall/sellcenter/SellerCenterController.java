package com.camelot.mall.sellcenter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.basecenter.service.BaseConsultingSmsService;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月18日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/sellercenter")
public class SellerCenterController {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private ItemSkuInquiryPriceExportService inquiryService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private BaseConsultingSmsService consultService;
	@Resource
	private ItemExportService itemService;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/inquiry")
	public String inquiry(HttpServletRequest request, Pager pager, ItemSkuInquiryPriceDTO dto, Model model){
		model.addAttribute("inquiry", dto);
		Long uid = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(uid);
        if(null != userDTO.getParentId()){//子账号：将uid设置为父账号的用户id
            uid = userDTO.getParentId();
        }
        dto.setSellerId(Long.valueOf(uid));
		
		LOG.debug("INQUIRY PARAMS:" + JSON.toJSONString(dto));
		
		// 查询当前登录用户的询价列表
		ExecuteResult<DataGrid<ItemSkuInquiryPriceDTO>> er = this.inquiryService.queryList(dto, pager);
		DataGrid<ItemSkuInquiryPriceDTO> dg = er.getResult();
		
		LOG.debug(JSON.toJSONString(dg));
		
		// 查询询价 买家用户名
		JSONArray array = new JSONArray();
		for (ItemSkuInquiryPriceDTO inquiry : dg.getRows()) {
			JSONObject obj = JSON.parseObject(JSON.toJSONString(inquiry));
			UserDTO user = this.userExportService.queryUserById(inquiry.getBuyerId());
			obj.put("buyerName", user.getUname());
			array.add(obj);
		}
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords( array );
		
		model.addAttribute("pager", pager);
		
		return "/sellcenter/seller_inquiry";
	}
	
	@RequestMapping("/inquiryInfo")
	@ResponseBody
	public ExecuteResult<JSONObject> getInquiry(Long id){
		ExecuteResult<JSONObject> er = new ExecuteResult<JSONObject>();
		ExecuteResult<ItemSkuInquiryPriceDTO>  dto = this.inquiryService.queryById(id);
		
		if( dto.isSuccess() ){
			ItemSkuInquiryPriceDTO inquiry = dto.getResult();
			JSONObject obj = JSON.parseObject(JSON.toJSONString(inquiry));
			ItemShopCartDTO sd = new ItemShopCartDTO();
			sd.setItemId(inquiry.getItemId());
			sd.setSkuId(inquiry.getSkuId());
			sd.setShopId(inquiry.getShopId());
			obj.put("itemId", inquiry.getItemId());
			obj.put("skuId", inquiry.getSkuId());
			LOG.debug("=====" + JSON.toJSONString(sd) );
			ExecuteResult<ItemShopCartDTO> sc = this.itemService.getSkuShopCart(sd);
			LOG.debug(">>>>>>" + JSON.toJSONString(sc));
			if( sc.isSuccess() ){
				ItemShopCartDTO isc = sc.getResult();
				StringBuffer sb = new StringBuffer();
				for( ItemAttr ia: isc.getAttrSales() ){
					String val = "";
					for(ItemAttrValue iav: ia.getValues() )
						val += iav.getName() + ";";
					sb.append(ia.getName()).append(":").append( val );
				}
				obj.put("itemName", isc.getItemName());
				obj.put("itemPic", isc.getSkuPicUrl());
				obj.put("itemAttr", sb.toString());
			}
			LOG.debug("=====::::" + obj.toJSONString());
			er.setResult(obj);
		}else {
			er.setErrorMessages(er.getErrorMessages());
		}
		return er;
	}
	
	@RequestMapping("/modifyInquiry")
	@ResponseBody
	public ExecuteResult<String> modifyInquiry(ItemSkuInquiryPriceDTO dto,HttpServletRequest request){
		ExecuteResult<String> result = this.inquiryService.modifyItemSkuInquiryPrice(dto);
		//供站内消息使用
		request.setAttribute("result",result);
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/consult")
	public String consult(HttpServletRequest request,Pager pager, BaseConsultingSmsDTO dto,Model model){
		Long uid = WebUtil.getInstance().getUserId(request);
        UserDTO userDTO = userExportService.queryUserById(uid);
        if(null != userDTO.getParentId()){//子账号：将uid设置为父账号的用户id
            uid = userDTO.getParentId();
        }
        dto.setSellerId(Long.valueOf(uid));
		DataGrid<BaseConsultingSmsDTO> dg = this.consultService.queryList(dto, pager);
		
		JSONArray consults = new JSONArray();
		for(BaseConsultingSmsDTO c : dg.getRows()){
			JSONObject consult = JSON.parseObject(JSON.toJSONString(c));
			ExecuteResult<ItemDTO> erItem = this.itemService.getItemById(c.getItemId());
			if(erItem.getResult() != null ){
				consult.put("itemName", erItem.getResult().getItemName());
				String[]picUrls = erItem.getResult().getPicUrls();
				consult.put("picUrl", picUrls.length > 0 ? picUrls[0] : "");
				consult.put("itemId", c.getItemId());
			}
			consults.add(consult);
		}
		
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(consults);
		
		model.addAttribute("pager", pager);
		return "/sellcenter/seller_consult";
	}

	@RequestMapping("/modifyConsult")
	@ResponseBody
	public ExecuteResult<String> modifyConsult(BaseConsultingSmsDTO dto){
		return this.consultService.modifyById(dto);
	}
	
	@RequestMapping("/delConsult")
	@ResponseBody
	public ExecuteResult<String> delConsult(Long id){
		return this.consultService.deleteById(id);
	}
}
