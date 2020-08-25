package com.camelot.ecm.evaluation.tag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.EvalTag;
import com.camelot.goodscenter.dto.EvalTagsOfCatDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.EvalTagService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.thinkgem.jeesite.common.persistence.Page;

/**
 * <p>商品评价标签管理</p>
 * <p>评价标签隶属于二级类目下</p>
 * Created on 2016年2月22日
 * @author  <a href="mailto: guyu@camelotchina.com">顾雨</a>
 * @version 1.0 
 * Copyright (c) 2016 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("${adminPath}/evalTag")
public class EvalTagController {
	
	@Resource
	private EvalTagService evalTagService;
	
	@Resource
	private ItemCategoryService itemCatService;
	
	@RequestMapping("/list")
	public String list(@ModelAttribute EvalTagsOfCatDTO evalTag, Pager pager, Model model) {
		ExecuteResult<DataGrid<EvalTagsOfCatDTO>> listResult =
				evalTagService.queryList(evalTag, pager);
		Page<EvalTagsOfCatDTO> page = new Page<EvalTagsOfCatDTO>();
		if (listResult.isSuccess()) {
			DataGrid<EvalTagsOfCatDTO> onePage = listResult.getResult();
			page.setCount(onePage.getTotal());
			page.setList(onePage.getRows());
			page.setPageNo(pager.getPage());
	        page.setPageSize(pager.getRows());
	        model.addAttribute("page", page);
		}
		
		// 一级类目 + 二级类目
		DataGrid<ItemCategoryDTO> catsL1 = itemCatService.queryItemCategoryList(0l);
		model.addAttribute("catsL1", catsL1.getRows());
		if (evalTag.getCidL1() != null) {
			long catL1OfSearch =  evalTag.getCidL1();
			DataGrid<ItemCategoryDTO> catsL2 = itemCatService.queryItemCategoryList(catL1OfSearch);
			model.addAttribute("catsL2", catsL2.getRows());
		}
		
		return "evalTag/tagList";
	}
	
	/**
	 * <p>跳转至标签新增页面</p>
	 * Created on 2016年2月23日
	 * @param model
	 * @return
	 * @author: 顾雨
	 */
	@RequestMapping("/toAdd")
	public String toAdd(Model model) {
		// 一级类目
		DataGrid<ItemCategoryDTO> catsL1 = itemCatService.queryItemCategoryList(0l);
		model.addAttribute("catsL1", catsL1.getRows());
		return "evalTag/add";
	}
	
	/**
	 * <p>保存标签新增</p>
	 * Created on 2016年2月23日
	 * @param evalTag
	 * @return
	 * @author: 顾雨
	 */
	@ResponseBody
	@RequestMapping("/saveAdd")
	public Object saveAdd(EvalTagsOfCatDTO evalTag, String tagNamesToAdd) {
		evalTag.setEvalTagNames(Arrays.asList(tagNamesToAdd.split(",,,")));
		ExecuteResult<String> addResult = evalTagService.add(evalTag);
		Map<String, Object> response = new HashMap<String, Object>();
		if (addResult.isSuccess()) {
			response.put("success", true);
			response.put("msg", addResult.getResult());
		} else {
			response.put("success", false);
			response.put("msg", addResult.getErrorMessages());
		}
		
		return response;
	}
	
	/**
	 * <p>通过2级类目检索</p>
	 * Created on 2016年2月23日
	 * @param evalTag
	 * @return
	 * @author: 顾雨
	 */
	@ResponseBody
	@RequestMapping("/queryTagsByCatL2")
	public Object queryTagsByCatL2(EvalTagsOfCatDTO evalTag) {
		ExecuteResult<EvalTagsOfCatDTO> listResult = evalTagService.queryOneGroupTags(evalTag);

		Map<String, Object> response = new HashMap<String, Object>();
		if (listResult.isSuccess()) {
			response.put("success", true);
			response.put("tags", listResult.getResult().getEvalTagGroups());
		} else {
			response.put("success", false);
		}

		return response;
	}
	
	/**
	 * <p>更新某个标签的名字</p>
	 * Created on 2016年2月23日
	 * @param tag
	 * @return
	 * @author: 顾雨
	 */
	@ResponseBody
	@RequestMapping("/updateNameOfOneTag")
	public Object updateNameOfOneTag(EvalTag tag) {
		
		Map<String, Object> response = new HashMap<String, Object>();
		
		ExecuteResult<String> updateResult = evalTagService.updateNameOfOneTag(tag);
		if (updateResult.isSuccess()) {
			response.put("success", true);
		} else {
			response.put("success", false);
			response.put("msg", updateResult.getErrorMessages().get(0));
		}
		return response;
	}
	
	@ResponseBody
	@RequestMapping("/deleteTags")
	public Object deleteTags(Integer cidL2, String toDelTagIds) {
		Map<String, Object> response = new HashMap<String, Object>();
		
		ExecuteResult<String> updateResult = evalTagService.deleteTags(cidL2,
				Arrays.asList(toDelTagIds.split(",")));
		if (updateResult.isSuccess()) {
			response.put("success", true);
		} else {
			response.put("success", false);
			response.put("msg", updateResult.getErrorMessages().get(0));
		}
		return response;
	}
}
