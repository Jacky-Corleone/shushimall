package com.camelot.mall.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.camelot.usercenter.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;

@Controller
@RequestMapping("/seller")
public class SellerController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SellerController.class);

	@Resource
	private UserExportService userExportService = null;
	
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService = null;
	
	@RequestMapping("news")
	public String newspage(Model model, Integer page, HttpServletRequest request,Integer wmRead,String messageDate){
		
		Pager<WebSiteMessageDTO> pager = new Pager<WebSiteMessageDTO>();
		if(page != null){
			pager.setPage(page);
		}
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		
		WebSiteMessageDTO siteMessageDTO = new WebSiteMessageDTO();
		if(wmRead!=null){
			siteMessageDTO.setWmRead(wmRead);
		}
		if(!StringUtils.isEmpty(messageDate)){
			siteMessageDTO.setWmCreated(Date.valueOf(messageDate));
		}
		// 2016-01-20 modified by 宋文斌
//		UserDTO userDTO = userExportService.queryUserById(registerDTO.getUid());
//		主账号：则根据主账号id查询；子账号：则根据子账号的父级id查询
//		if (userDTO.getParentId() == null) {
//			siteMessageDTO.setWmToUserid(registerDTO.getUid());
//		} else {
//			siteMessageDTO.setWmToUserid(userDTO.getParentId());
//		}
        siteMessageDTO.setWmToUserid(registerDTO.getUid());
		DataGrid<WebSiteMessageDTO> allMag = baseWebSiteMessageService.queryWebSiteMessageList(siteMessageDTO, pager);
		siteMessageDTO.setType(1);
		DataGrid<WebSiteMessageDTO> sysMag = baseWebSiteMessageService.queryWebSiteMessageList(siteMessageDTO, pager);
		model.addAttribute("allMag", allMag);
		model.addAttribute("sysMag", sysMag);
		model.addAttribute("newsType", "news");
		model.addAttribute("read", wmRead);
		model.addAttribute("messageDate", messageDate);
		pager.setRecords(allMag.getRows());
		pager.setTotalCount(allMag.getTotal().intValue());
		model.addAttribute("page", pager);
		return "/user/seller/news-center";
	}
	
	@RequestMapping("sysnews")
	public String sysnews(Model model, WebSiteMessageDTO dto, Integer page, HttpServletRequest request,Integer wmRead,String messageDate){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		dto.setWmToUserid(registerDTO.getUid());
		if(wmRead!=null){
			dto.setWmRead(wmRead);
		}
		if(!StringUtils.isEmpty(messageDate)){
			dto.setWmCreated(Date.valueOf(messageDate));
		}
		Pager<WebSiteMessageDTO> pager = new Pager<WebSiteMessageDTO>();
		if(page != null){
			pager.setPage(page);
		}
		if(dto.getType() == null){
			dto.setType(1);
		}
		DataGrid<WebSiteMessageDTO> sysMag = baseWebSiteMessageService.queryWebSiteMessageList(dto, pager);
		if(dto.getType() != null){
			dto.setType(null);
		}
		DataGrid<WebSiteMessageDTO> allMag = baseWebSiteMessageService.queryWebSiteMessageList(dto, pager);
		model.addAttribute("allMag", allMag);
		
		model.addAttribute("sysMag", sysMag);
		model.addAttribute("newsType", "sysnews");
		model.addAttribute("read", wmRead);
		model.addAttribute("messageDate", messageDate);
		model.addAttribute("page", pager);
		return "/user/seller/news-center";
	}
	
	@RequestMapping("setread")
	public String setRead(Model model,String[] ids,String newsType,String wmRead,String messageDate){
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids,"2");
		model.addAttribute("msg", result.getResultMessage());
		return "redirect:/seller/"+newsType+"?wmRead="+wmRead+"&messageDate="+messageDate;
	}
	 @RequestMapping(value="/ajaxSetread")
	 @ResponseBody
	public Map  ajaxSetread(Model model,String[] ids,String newsType,String wmRead,String messageDate){
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids,"2");
		model.addAttribute("msg", result.getResultMessage());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("id", ids);
		if(result.isSuccess()){
			map.put("meessage", result.getResultMessage());
			return map;
		}else{
			map.put("meessage", result.getErrorMessages());
			return map;
		}
	}
	/**
	 * 根据id删除站内信
	 * 
	 * @author 王东晓
	 * create date 2015-05-28
	 * 
	 * @param ids  站内信id
	 * @return
	 */
	@RequestMapping("delNews")
	public String delNews(String[] ids,Model model,String newsType,String wmRead,String messageDate){
		
		ExecuteResult<String> result = baseWebSiteMessageService.modifyWebSiteMessage(ids,"3");
		String message  = "";
		if(result.isSuccess()){
			message = "删除成功";
		}else{
			message="删除失败";
		}
		model.addAttribute("message", message);
		return "redirect:/seller/"+newsType+"?wmRead="+wmRead+"&messageDate="+messageDate;
	}
}