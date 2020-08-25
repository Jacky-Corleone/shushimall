/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.thinkgem.jeesite.modules.sys.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.PagerModelDTO;
import com.camelot.basecenter.dto.ResultWrapperDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 标签Controller
 * 
 * @author ThinkGem
 * @version 2013-3-23
 */
@Controller
@RequestMapping("${adminPath}/tag")
public class TagController extends BaseController {

	@Autowired
	private AddressBaseService addressBaseService;
	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequestMapping("treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); // 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("module", request.getParameter("module")); // 过滤栏目模型（仅针对CMS的Category树）
		return "modules/sys/tagTreeselect";
	}
	
	/**
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("treeselectadress")
	public String treeselectAdrress(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); // 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("module", request.getParameter("module")); // 过滤栏目模型（仅针对CMS的Category树）
		return "modules/sys/tagTreeselectAddress";
	}
	
	@RequestMapping("treeselectadressList")
	public String treeselectAdrressList(HttpServletRequest request, Model model) {
		return "modules/sys/tagTreeselectAddressmgr";
	}
	/**
	 * 根据父节点显示子节点列表信息（包含父节点）
	 * @param user
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("treeselectadresstab")
	public String treeselectAdrressTab(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String parentCode = request.getParameter("parentCode");
		if(parentCode == null){
			parentCode = "0";
		}
		Model model1  = addAttrForPage(request, response, model, parentCode);
		request.getSession(false).setAttribute("leftTreeSelectNode", parentCode);
		return "modules/sys/tagTreeselectAddresstab";
	}
	
	protected Model addAttrForPage(HttpServletRequest request, HttpServletResponse response, Model model,String parentCode){
		Page<AddressBaseDTO> thikPage = new Page<AddressBaseDTO>(request, response);
		int pageNow = thikPage.getPageNo();
		int pageSize = thikPage.getPageSize();
		PagerModelDTO<AddressBaseDTO> dto = addressBaseService.getAddressesWithPage(pageNow, pageSize, parentCode);
		
		thikPage.setList(dto.getRows());
		thikPage.setCount(dto.getTotal());
		model.addAttribute("page", thikPage);
		model.addAttribute("parentCodeTag", parentCode);
		
		return model;
	}
	
	
	
	
	@RequiresUser
	@RequestMapping("addressbase/show")
	public String treeselectAdrressBaseSave(AddressBaseDTO addressBaseDTO,HttpServletRequest request,Model model) {
		//String code = request.getParameter("code");
		String code  = addressBaseDTO.getCode();
		String parentCode  = addressBaseDTO.getParentcode();
		
		int level = addressBaseDTO.getLevel();
		//存放父节点
		model.addAttribute("addressBaseDTO", addressBaseDTO);
		//update
		if(code != null){
			AddressBaseDTO addressBase = addressBaseService.getAddressBaseByCode(code);
			model.addAttribute("addressBaseDTO", addressBase);
		}
		//add
		else{
			level ++;
			addressBaseDTO.setLevel(level);
			
		}
		return "modules/sys/tagTreeselectAddressinfo";
	}
	
	@RequiresUser
	@RequestMapping("addressbase/save")
	public String treeselectAdrressBaseSave(AddressBaseDTO addressBaseDTO, Model model) {
		
		ResultWrapperDTO resultW =   addressBaseService.newSave(addressBaseDTO);
		
		model.addAttribute("resultW",resultW);
		model.addAttribute("addressBaseDTO", addressBaseDTO);
		
		if(resultW.isSuccessful() && ResultWrapperDTO.FLAG_ADD.equals(resultW.getFlag())){
			model.addAttribute("addCode", resultW.getResultValue());
		}
		return "modules/sys/tagTreeselectAddressinfo";
	}
	
	@RequiresUser
	@RequestMapping("addressbase/delete")
	public String treeselectAdrressBaseDelete(AddressBaseDTO addressBaseDTO,HttpServletRequest request, HttpServletResponse response, Model model) {
		String code  = addressBaseDTO.getCode();
		String parentCode =  (String) request.getSession(false).getAttribute("leftTreeSelectNode");
		ResultWrapperDTO resultW = addressBaseService.delete(code);
	    request.getSession(false).setAttribute("lastDeleteACode", code);
		addAttrForPage(request, response, model, parentCode);
		model.addAttribute("resultW", resultW);
		return "modules/sys/tagTreeselectAddresstab";
	}
	
	


	/**
	 * 图标选择标签（iconselect.tag）
	 */
	@RequiresUser
	@RequestMapping("iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "modules/sys/tagIconselect";
	}
	


}
