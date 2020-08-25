package com.camelot.third;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

@Controller
@RequestMapping("/qq")
public class QQVerify {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(QQVerify.class);
	
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
		try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/error";
	}
	
	@RequestMapping("/callback")
	public String callback(Model model, HttpServletRequest request, HttpServletResponse response){
		String accessToken = null,
		openID = null,
		nikename = null;
		Long tokenExpireIn = 0L, uid = null;
		
		try {
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
			
            if (accessTokenObj.getAccessToken().equals("")) {
				//	                我们的网站被CSRF攻击了或者用户取消了授权
				//	                做一些数据统计工作
                LOGGER.info("QQ登录没有获取到响应参数");
            } else {
	            accessToken = accessTokenObj.getAccessToken();
	            tokenExpireIn = accessTokenObj.getExpireIn();
	
	            request.getSession().setAttribute("demo_access_token", accessToken);
	            request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));
	
	            // 利用获取到的accessToken 去获取当前用的openid -------- start
	            OpenID openIDObj =  new OpenID(accessToken);
	            openID = openIDObj.getUserOpenID();
	
	            // 利用获取到的accessToken 去获取当前用户的openid --------- end
	            UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
	            UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                if (userInfoBean.getRet() == 0) {
                	nikename = userInfoBean.getNickname();  //获取用户昵称
                	boolean b = userExportService.verifyLoginName(openID + "_qq");
                	if(!b){
	                	RegisterInfoDTO infoDTO = new RegisterInfoDTO();
	                	infoDTO.setLoginname(openID + "_qq");
	                	infoDTO.setNickName(nikename);
	                	infoDTO.setUserType(1);
	                	infoDTO.setQuickType(2);
	                	uid = userExportService.registerUser(infoDTO);
                	} else {
                		RegisterDTO dto = userExportService.getUserInfoByUsername(openID + "_qq");
                		uid = dto.getUid();
                	}
                	RegisterDTO registerDTO = new RegisterDTO();
                	registerDTO.setUid(uid);
                	registerDTO.setLoginname(openID + "_qq");
                	CookieHelper.setCookie(response, Constants.USER_TOKEN, accessToken);
                	CookieHelper.setCookie(response, Constants.USER_ID, uid.toString());
                	CookieHelper.setCookie(response, Constants.USER_NAME, openID + "_qq");
                	userExportService.updateUserInfoToRedis(accessToken, registerDTO);
                } else {
                	LOGGER.info("很抱歉，我们没能正确获取到您的信息，原因是：{}" + userInfoBean.getMsg());
                }
            }
		} catch (QQConnectException e) {
			LOGGER.info("错误信息:{}" + e.getMessage());
		}
		return "redirect:/";
	}
	
}