package com.camelot.mall.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.camelot.mall.Constants;
//import com.camelot.mall.sellcenter.UserDtoUtils;
import com.camelot.mall.service.impl.CommonServiceImpl;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.BusinessScale;
import com.camelot.usercenter.enums.UserEnums.CompanyPeopleNum;
import com.camelot.usercenter.enums.UserEnums.CompanyQualt;
import com.camelot.usercenter.enums.UserEnums.DepartMent;
import com.camelot.usercenter.enums.UserEnums.UsePurchaseType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;

/** 
 * <p>Description: [买家中心页面]</p>
 * Created on 2015年6月2日
 * @author  <a href="mailto: xxx@camelotchina.com">成文涛</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
public class BuyersPageController {
	private static final String VALUE_REGISTER_DUPLICATE_REDIS = "0";
	
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private CommonServiceImpl commonServiceImpl;
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private MallRecExportService mallRecService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
    private RedisDB redisDB;
	/**
	 * 跳转到主页
	 * @author 成文涛 创建时间：2015-6-2 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("buyers")
	public String goBuyersPage(Model model, HttpServletRequest request,
			HttpServletResponse response,@CookieValue(value=Constants.USER_ID) Long uid){
		if(null == uid){
			return "/";
		}
		UserDTO user = userExportService.queryUserById(uid);
		if(null!=user){
			model.addAttribute("user", user);
		}
		return "/buyers/buyersPage";
	}

	@RequestMapping("buyers/getbuyersDatas")
	@ResponseBody
	public String getbuyersDatas(){
		JSONArray ja=commonServiceImpl.findCategory();
		return ja.toJSONString(); 
	}
	
	/**
	 * 跳转到买家认证页面
	 * @author 李伟龙 创建时间：2015年10月8日 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("buyerAuthentication")
	public String goBuyerAuthentication(Model model, HttpServletRequest request,
			HttpServletResponse response,@CookieValue(value=Constants.USER_ID) Long uid){
		
		//防止重复企业注册
		String keyEnterpriseRegisterDuplicateRedis = WebUtil.getInstance().getRegisterToken();
		redisDB.addObject(keyEnterpriseRegisterDuplicateRedis, VALUE_REGISTER_DUPLICATE_REDIS, 1800);
		model.addAttribute("keyEnterpriseRegisterDuplicateRedis", keyEnterpriseRegisterDuplicateRedis);
		
		//所在部门、经营范围、经营规模、企业人数、公司性质
		model.addAttribute("departMents", DepartMent.values());
		model.addAttribute("usePurchaseTypes", UsePurchaseType.values());
		model.addAttribute("businessScales", BusinessScale.values());
		model.addAttribute("companyPeopleNums", CompanyPeopleNum.values());
		model.addAttribute("companyQualts", CompanyQualt.values());
		model.addAttribute("userId", uid);
		//返回路径
		String url = "";
		//加载用户信息
		UserDTO userDTO = userExportService.queryUserById(uid);
		if(null!=userDTO){
			model.addAttribute("userDTO", userDTO);
			//认证状态
			int status = userDTO.getUserstatus();
			// 2 普通用户认证完成
			if(status <= 2){
				 url = "/buyers/buyerAuthentication";
			// 提交认证之后的状态
			}else if(status >2){
				//用户-扩展信息
				ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(uid);
				UserInfoDTO userInfoDTO = userInfoResult.getResult();
				UserDTO userRemark = userInfoDTO.getUserDTO();
//				u.setUserEmail(UserDtoUtils.hideUserEmail(userDTO.getUserEmail()));
//				u.setUmobile(UserDtoUtils.hideUserCellPhone(userDTO.getUmobile()));
				model.addAttribute("userInfoDTO",userInfoDTO);
				model.addAttribute("userInfo",userRemark);
				url = "/buyers/buyerAuthenModify";
			}
		}
		
		return url;
	}
}
