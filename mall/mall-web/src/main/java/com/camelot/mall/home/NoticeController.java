package com.camelot.mall.home;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月3日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private NoticeExportService noticeService;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@RequestMapping("/toView/{id}")
	public String toView(@PathVariable Long id,Model model){
		MallNoticeDTO notice = this.noticeService.getNoticeInfo(id);
		model.addAttribute("notice", notice);
		return "/home/notice_detail";
	}
	
	@RequestMapping("/list")
	public String list(Pager<MallNoticeDTO> pager,MallNoticeDTO dto,Model model){
		LOG.debug("NOTICE LIST："+JSON.toJSONString(dto));
		dto.setPlatformId(Long.valueOf(0));
		dto.setNoticeStatus(1);
		dto.setThemeType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallNoticeDTO> dg = this.noticeService.queryNoticeList(pager,dto);
		
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		LOG.debug(JSON.toJSONString(pager));
		
		model.addAttribute("notice", dto);
		model.addAttribute("pager", pager);
		return "/home/notice_list";
	}
	/**
	 * 
	 * <p>Discription:[二级类目子站公告列表]</p>
	 * Created on 2015年12月30日
	 * @param pager
	 * @param dto
	 * @param model
	 * @param request
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping("/sonhomeList")
	public String sonhomeList(Pager<MallNoticeDTO> pager,MallNoticeDTO dto,Model model,HttpServletRequest request){
		LOG.debug("NOTICE LIST："+JSON.toJSONString(dto));
		String themId=request.getParameter("themId");
		String cid=request.getParameter("cid");
		if (StringUtils.isBlank(themId)||StringUtils.isBlank(cid)) {
			return "redirect:/";
		}
		dto.setPlatformId(Long.valueOf(0));
		dto.setNoticeStatus(1);
		dto.setThemeId(Integer.parseInt(themId));
		dto.setThemeType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallNoticeDTO> dg = this.noticeService.queryNoticeList(pager,dto);
		
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		LOG.debug(JSON.toJSONString(pager));
		ExecuteResult<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.queryItemByCategoryById(Long.parseLong(cid));
		if(itemCategoryDTO!=null&&itemCategoryDTO.getResult()!=null){
			model.addAttribute("cname",itemCategoryDTO.getResult().getCategoryCName());
		}
		model.addAttribute("notice", dto);
		model.addAttribute("pager", pager);
		model.addAttribute("cid",cid);
		model.addAttribute("themId",themId);
		return "/sonhome/sonhomeNoticeList";
	}
	/**
	 * 
	 * <p>Discription:[二级类目子站公告查看]</p>
	 * Created on 2015年12月30日
	 * @param id
	 * @param model
	 * @param request
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping("/toSonHomeView/{id}")
	public String toSonHomeView(@PathVariable Long id,Model model,HttpServletRequest request){
		String cid=request.getParameter("cid");
		if (StringUtils.isBlank(cid)) {
			return "redirect:/";
		}
		ExecuteResult<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.queryItemByCategoryById(Long.parseLong(cid));
		if(itemCategoryDTO!=null&&itemCategoryDTO.getResult()!=null){
			model.addAttribute("cname",itemCategoryDTO.getResult().getCategoryCName());
		}
		MallNoticeDTO notice = this.noticeService.getNoticeInfo(id);
		model.addAttribute("notice", notice);
		model.addAttribute("cid",cid);
		return "/sonhome/notice_detail";
	}
}
