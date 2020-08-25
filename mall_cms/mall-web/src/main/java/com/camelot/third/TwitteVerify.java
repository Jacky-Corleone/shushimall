package com.camelot.third;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.service.UserExportService;

@Controller
@RequestMapping("/twitte")
public class TwitteVerify {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TwitteVerify.class);
	
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
//			response.sendRedirect(oauth.authorize("code"));
			BareBonesBrowserLaunch.openURL(oauth.authorize("code"));
			return "redirect:/";
		} catch (Exception e) {
			LOGGER.info("调用微博三方登录出错");
			return "redirect:/error";
		}
	}
	
	@RequestMapping("/callback")
	public String callback(Model model, String code, HttpServletResponse response){
		Oauth oauth = new Oauth();
		String accessToken = null, uid = null;
		Users um = null;
		User user = null;
		try{
			accessToken = oauth.getAccessTokenByCode(code).getAccessToken();
			uid = oauth.getAccessTokenByCode(code).getUid();
			um = new Users(accessToken);
			user = um.showUserById(uid);
		} catch (WeiboException e) {
			if(401 == e.getStatusCode()){
				LOGGER.info("Unable to get the access token.");
			}else{
				LOGGER.info(e.getError());
			}
			return "redirect:/error";
		}
		RegisterInfoDTO dto = new RegisterInfoDTO();
		dto.setLoginname(uid + "_twitte");
		dto.setUserType(1);
		dto.setQuickType(2);
		Long ruid = userExportService.registerUser(dto);
    	RegisterDTO registerDTO = new RegisterDTO();
    	registerDTO.setUid(ruid);
    	CookieHelper.setCookie(response, Constants.USER_TOKEN, accessToken);
    	userExportService.updateUserInfoToRedis(accessToken, registerDTO);
    	
    	return "redirect:/";
	}
	
}