package com.camelot.third;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;
import weibo4j.util.WeiboConfig;

@Controller
@RequestMapping("/twitte")
public class TwitteVerify {
	private static final Logger LOGGER = LoggerFactory.getLogger(TwitteVerify.class);
	
	@Resource
	private UserExportService userExportService = null;

	/**
	 * <p>Discription:注册页面</p>
	 * Created on 2015年3月12日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		Oauth oauth = new Oauth();
		try {
			String url = oauth.authorize("code","","all");
			LOGGER.info("url>>>>>>>>>>>>>>>>>"+url);
			response.sendRedirect(url);
			//BareBonesBrowserLaunch.openURL(url);
//			return "redirect:/";
			return null;
		} catch (Exception e) {
			LOGGER.info("调用微博三方登录出错");
			return "redirect:/error";
		}
	}
	
	@RequestMapping("/callback")
	public String callback(Model model, String code, HttpServletResponse response){
		LOGGER.info("callback>>>>>>>>>>>>>>start");
		LOGGER.info("code>>>>>>>>>>>>>>"+code);
		Oauth oauth = new Oauth();
		String accessToken = null;
		String openId =null;
		Long uid = null;
		try{
			LOGGER.info("AccessToken>>>>>>>>>>>>>>start");
			AccessToken at = oauth.getAccessTokenByCode(code);
			LOGGER.info("AccessToken>>>>>>>>>>>>>>"+at);
			openId = at.getUid();
			LOGGER.info("微博用户-Uid(openId)>>>>>>>>>>>>>>"+openId);
			
			accessToken = at.getAccessToken();
			LOGGER.info("微博访问accessToken>>>>>>>>>>>>>>"+accessToken);
			
		} catch (WeiboException e) {
			if(401 == e.getStatusCode()){
				LOGGER.info("Unable to get the access token.");
			}else{
				LOGGER.info(e.getError());
			}
			LOGGER.info(e.getError());
			return "redirect:/error";
		}
    	
    	boolean b = userExportService.verifyLoginName(openId + "_twitte");
    	if(!b){
        	RegisterInfoDTO infoDTO = new RegisterInfoDTO();
        	infoDTO.setLoginname(openId + "_twitte");
        	infoDTO.setUserType(1);
        	infoDTO.setQuickType(2);
        	uid = userExportService.registerUser(infoDTO);
    	} else {
    		UserDTO user=new UserDTO();
    		user.setUname(openId + "_twitte");
    		DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(user, null, null);
    		List<UserDTO> list = dataGrid.getRows();
    		if(list!=null && list.size()>0){
    			uid = list.get(0).getUid();
    		}
    	}
    	RegisterDTO registerDTO = new RegisterDTO();
    	registerDTO.setUid(uid);
    	registerDTO.setLoginname(openId + "_twitte");
    	CookieHelper.setCookie(response, Constants.USER_TOKEN, accessToken);
    	CookieHelper.setCookie(response, Constants.USER_ID, uid.toString());
    	CookieHelper.setCookie(response, Constants.USER_NAME, openId + "_twitte");
    	userExportService.updateUserInfoToRedis(accessToken, registerDTO);
    	LOGGER.info("callback>>>>>>>>>>>>>>end");
    	return "redirect:/";
	}
	
}