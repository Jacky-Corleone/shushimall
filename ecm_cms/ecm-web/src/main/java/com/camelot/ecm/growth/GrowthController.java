package com.camelot.ecm.growth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.basecenter.dto.GrowthDTO;
import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.service.GrowthService;
import com.camelot.common.DateUtil;
import com.camelot.common.MallType;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.GrowthTypeEnum;
import com.camelot.openplatform.util.SysProperties;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015-12-4
 * @author  <a href="mailto: fandongzang98@camelotchina.com">范东藏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/growth")
public class GrowthController extends BaseController{

	private Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private GrowthService growthService;
	
	/**
	 * 获取文档分类列表
	 * @param mallClassifyDTO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping({ "list", "" })
	public String list(GrowthDTO growthDTO,HttpServletRequest request,
			HttpServletResponse response, Model model){
		Page page = new Page<GrowthDTO>(request, response);
		// 将分页page转换为服务所需要的pager
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		pager.setSort("type");
		pager.setOrder("desc");
		DataGrid<GrowthDTO>  dg = growthService.queryGrowthList(pager, growthDTO);
		page.setList(dg.getRows());
		page.setCount(dg.getTotal());
		model.addAttribute("typeList", this.getTypeList());
		model.addAttribute("page", page);
		model.addAttribute("growthDTO",growthDTO);
		return "growth/growthList";
	}
	
	/**
	 * 
	 * <p>Discription:[新增]</p>
	 * Created on 2015-12-7
	 * @param mallClassifyDTO
	 * @param model
	 * @return
	 * @author:[范东藏]
	 */
	@RequestMapping({"form"})
	public String form(GrowthDTO growthDTO,Model model){
		model.addAttribute("growthDTO", growthDTO);
		
		DataGrid<GrowthDTO>  dg = growthService.queryGrowthList(null, growthDTO);
		List<Map<Integer, Object>> mapList = Lists.newArrayList();
		mapList = this.getTypeList();
		for (int i = 0; i < dg.getRows().size(); i++) {
			for (int j = 0; j < mapList.size(); j++) {
				if(mapList.get(j).containsKey(dg.getRows().get(i).getType())){
					mapList.remove(j);
					break;
				}
			}
		}
		model.addAttribute("typeList", mapList);
		return "growth/growthForm";
	}

	/**
	 * 保存
	 * @param GrowthDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(GrowthDTO growthDTO, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes){
		ExecuteResult<String> result =null;
		if (growthDTO.getId()!=null&&growthDTO.getId()!=0) {
			result = growthService.updateGrowth(growthDTO);
		}else{
			result = growthService.addGrowth(growthDTO);
		}
		addMessage(redirectAttributes, result.getResult());
		return "redirect:" + SysProperties.getAdminPath() + "/growth/list";
	}
	
	/**
	 * 获取获取积分类型集合
	 * @param response
	 * @return
	 */
	public List<Map<Integer, Object>> getTypeList() {
		List<Map<Integer, Object>> mapList = Lists.newArrayList();
		//获取所有的文档类型
		EnumSet<GrowthTypeEnum> currEnumSet = EnumSet.allOf (GrowthTypeEnum.class );
		for(GrowthTypeEnum growthTypeEnum : currEnumSet){
			Map<Integer,Object> typeMap = new HashMap<Integer,Object>();
			typeMap.put(growthTypeEnum.getId(), growthTypeEnum.getName());
			mapList.add(typeMap);
		}
		return mapList;
	}
	
	
}
