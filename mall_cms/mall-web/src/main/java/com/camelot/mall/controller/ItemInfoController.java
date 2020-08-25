package com.camelot.mall.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.mall.service.ItemInfoService;


@Controller
@RequestMapping("/itemInfoController")
public class ItemInfoController {
	

	@Resource
	private ItemInfoService itemInfoService;

	@RequestMapping("/getItemInfo")
	public String getItemInfo(String id, Model model){
		//ItemDTO itemDTO=itemInfoService.getItemInfoById(id);
		ItemDTO  item=itemInfoService.getItemInfoById(id);
		model.addAttribute("item", item);
		return "/page/productDetails";
	}

}
