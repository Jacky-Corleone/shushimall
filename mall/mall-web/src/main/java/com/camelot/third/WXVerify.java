package com.camelot.third;

import java.io.IOException;
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
import com.camelot.openplatform.util.SysProperties;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.MD5;

@Controller
@RequestMapping("/wx")
public class WXVerify {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(WXVerify.class);
	
	@Resource
	private UserExportService userExportService = null;

	/**
	 * <p>Discription:微信第三方登录</p>
	 * Created on 2015年3月28日
	 * @return
	 * @author:李涛
	 */
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		String appid = WxConfig.getValue("appId");
		String redirect_uri = WxConfig.getValue("redirect_uri");
		String state = WxConfig.getValue("state");
//		
//		String appid = SysProperties.getProperty("wx_appId");
//		String redirect_uri = SysProperties.getProperty("wx_redirect_uri");
//		String state = SysProperties.getProperty("wx_state");
		
		String url = "https://open.weixin.qq.com/connect/qrconnect?"
				+ "appid="+appid
				+ "&redirect_uri="+redirect_uri
				+ "&response_type=code"
				+ "&scope=snsapi_login"
				+ "&state="+state
				+ "#wechat_redirect";
		try {
			response.sendRedirect(url);
			return null;
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
		return "redirect:/";
	}
	
	/**
	 * <p>Discription:微信第三方登录回调方法</p>
	 * Created on 2015年3月28日
	 * @return
	 * @author:李涛
	 */
	@RequestMapping("/callback")
	public String callback(Model model, String code,HttpServletRequest request,HttpServletResponse response){
//		LOGGER.info("微信回调方法>>>>>>>>>>>>>>>>>>>>>>start");
//		String accessToken = null;
//		Long uid = null;
//		String appId = WxConfig.getValue("appId");
//		String secret =WxConfig.getValue("secret");
//		String grant_type = WxConfig.getValue("grant_type");
		
//		String appId = WxConfig.getValue("wx_appId");
//		String secret =WxConfig.getValue("wx_secret");
//		String grant_type = WxConfig.getValue("wx_grant_type");
		
//		String accessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?"
//				+ "appid="+appId
//				+"&secret="+secret
//				+"&code="+code
//				+"&grant_type="+grant_type;
//		
//		String resultStr = RequestUtil.httpsRequest(accessTokenUrl, "GET", null);
//		LOGGER.info("resultStr>>>>>>>>>>>>>>>>>>>>>>"+resultStr);
//		JSONObject result = JSONObject.parseObject(resultStr);
//		LOGGER.info("result>>>>>>>>>>>>>>>>>>>>>>"+result);
//		if(result==null || result.containsKey("errcode")){
//			LOGGER.info("微信第三方登录失败：获取微信用户信息失败！");
//			return "redirect:/error";
//		}
//		//获取微信接口返回的openid  用户唯一标识
//		String openid = result.getString("openid").toString();
//		LOGGER.info("微信openid>>>>>>>>>>>>>>>>>>>>>>"+openid);
//		//获取微信接口返回的access_token
//		accessToken = result.getString("access_token").toString();
//		LOGGER.info("微信accessToken>>>>>>>>>>>>>>>>>>>>>>"+accessToken);
		String openid = "oQcoqv3c_92mtnXFLmkLXQ6g0XFw";
		String accessToken = "";
		Long uid = new Long(1000000045);
		boolean b = userExportService.verifyLoginName(openid+"_wx");
    	if(!b){
        	RegisterInfoDTO infoDTO = new RegisterInfoDTO();
        	infoDTO.setLoginname(openid+"_wx");
        	infoDTO.setUserType(1);
        	infoDTO.setQuickType(2);
        	uid = userExportService.registerUser(infoDTO);
    	} else {
    		//RegisterDTO dto = userExportService.getUserInfoByUsername(openid+"_wx");
    		UserDTO user=new UserDTO();
    		user.setUname(openid+"_wx");;
    		DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(user, null, null);
    		List<UserDTO> list = dataGrid.getRows();
    		if(list!=null && list.size()>0){
    			uid = list.get(0).getUid();
    		}
    		
    	}
    	RegisterDTO registerDTO = new RegisterDTO();
    	registerDTO.setUid(uid);
    	registerDTO.setLoginname(openid+"_wx");
    	
    	StringBuffer buffer = new StringBuffer();
		buffer.append(openid+"_wx");
		buffer.append("|");
		buffer.append(request.getRemoteAddr());
		buffer.append("|");
		buffer.append(SysProperties.getProperty("token.suffix"));
		accessToken = buffer.toString();
    	CookieHelper.setCookie(response, Constants.USER_TOKEN, accessToken);//
    	CookieHelper.setCookie(response, "logging_status", MD5.encipher(accessToken));
		CookieHelper.setCookie(response, Constants.USER_NAME, openid+"_wx");
		CookieHelper.setCookie(response, Constants.LOG_NAME, openid+"_wx");
		CookieHelper.setCookie(response, Constants.USER_ID, uid.toString());
    	
    	userExportService.updateUserInfoToRedis(accessToken, registerDTO);//
    	LOGGER.info("微信回调方法>>>>>>>>>>>>>>>>>>>>>>end");
    	return "redirect:/";
    	
	}
	
}