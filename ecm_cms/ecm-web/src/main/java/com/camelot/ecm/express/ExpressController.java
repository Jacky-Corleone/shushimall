package com.camelot.ecm.express;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.basecenter.service.DictionaryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
/**
 * 
 * <p>
 * Description: [快递管理]
 * </p>
 * Created on 2015-2-27
 * 
 * @author <a href="mailto: wanghongwei@camelotchina.com">王宏伟</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("${adminPath}/express")
public class ExpressController extends BaseController {

	@Resource
	private DictionaryService dictionaryService;

	/**
	 * 快递列表
	 * 
	 * @param pager 分页信息
	 * @param dictionary 快递信息
	 * @param model
	 * @return 分页列表
	 */
	@RequiresPermissions("basecenter:express:view")
	@RequestMapping("list")
	public String list(Pager pager, DictionaryDTO dictionary, Model model) {
		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}
		Page<DictionaryDTO> page = new Page<DictionaryDTO>();
		DataGrid<DictionaryDTO> dataGrid = dictionaryService
				.queryDictionaryByCondition(dictionary, pager);
		page.setCount(dataGrid.getTotal());
		page.setList(dataGrid.getRows());
		page.setPageNo(pager.getPage());
        page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("dictionaryDTO",dictionary);
		return "express/expressList";
	}
	
	/**
	 * 查看快递
	 * 
	 * @param id 快递ID
	 * @param model
	 * @return 快递信息
	 */
	@RequiresPermissions("basecenter:express:view")
	@RequestMapping("form")
	public String form(Long id, Model model){
		if(id!=null){
			ExecuteResult<DictionaryDTO> result = dictionaryService.queryDictionaryById(id);
			model.addAttribute("dictionary", result.getResult());
		}
		return "express/expressForm";
	}
	
	/**
	 * 修改快递新增快递信息
	 *
	 * @param dictionary 快递信息
	 * @param model
	 * @param redirectAttributes 
	 * @return 回调快递列表
	 */
	@RequiresPermissions("basecenter:express:edit")
	@RequestMapping("save")
	public String save(DictionaryDTO dictionary, Model model,RedirectAttributes redirectAttributes){
		ExecuteResult<String> result = null;
		if(dictionary.getId()!=null){
			result = dictionaryService.updDictionary(dictionary);
		}else{
			result = dictionaryService.addDictionary(dictionary);
		}
		addMessage(redirectAttributes, result.getResultMessage());
		return "redirect:" + SysProperties.getAdminPath() + "/express/list";
	}
}
