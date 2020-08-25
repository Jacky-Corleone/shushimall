package com.camelot.util;

import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SpringApplicationContextHolder;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.LoginToken;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: [web controller工具类]</p>
 * Created on 2015年3月9日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class WebUtil {
    private static Logger LOG = LoggerFactory.getLogger(WebUtil.class);
	private UserExportService userExportService;

	private UserExtendsService userExtendsService;

	private WebUtil(){
		userExportService = SpringApplicationContextHolder.getBean("userExportService");
		userExtendsService = SpringApplicationContextHolder.getBean("userExtendsService");
	}

	private static WebUtil instance = null;

	public static WebUtil getInstance(){
		if(null == instance){
			synchronized (WebUtil.class) {
				if(null==instance){
					instance = new WebUtil();
				}
			}
		}
		return instance;
	}

	/**获取当前登录用户的用户id*/
	public Long getUserId(HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		if(StringUtils.isBlank(token)){
			return null;
		}
		RegisterDTO registerDTO = userExportService.getUserByRedis(LoginToken.getLoginToken(request));
		if(registerDTO == null){
			return null;
		}
		Long userId = registerDTO.getUid();
		return userId;
	}

	/**获取当前登录用户的店铺id*/
	public Long getShopId(HttpServletRequest request){
		ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(getUserId(request));
        UserInfoDTO userInfoDTO = result.getResult();
        if(null==userInfoDTO){
            return null;
        }
		UserDTO userDTO = result.getResult().getUserDTO();
		Long shopId = userDTO.getShopId();
		return shopId;
	}

	/**获取注册Token*/
	public String getRegisterToken(){
		return "register" + UUID.randomUUID();
	}

    /**
     * 去除html代码
     * @param inputString
     * @return
     */
    public String htmlToText(String inputString) {
        String htmlStr = inputString; //含html标签的字符串
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        java.util.regex.Pattern p_ba;
        java.util.regex.Matcher m_ba;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式
            String patternStr = "\\t+";

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            p_ba = Pattern.compile(patternStr,Pattern.CASE_INSENSITIVE);
            m_ba = p_ba.matcher(htmlStr);
            htmlStr = m_ba.replaceAll(" "); //过滤tab空白

            textStr = htmlStr;

        }catch(Exception e) {
            LOG.error("Html2Text error: " + e);
        }
        return textStr;//返回文本字符串
    }
    
    /**
	 * <p>Discription:获取客户端真实IP</p>
	 * Created on 2015年8月5日
	 * @param request
	 * @return
	 * @author:宋文斌
	 */
    public String getIpAddr(HttpServletRequest request) { 
        String ip = request.getHeader("x-forwarded-for"); 
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("Proxy-Client-IP"); 
        } 
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getHeader("WL-Proxy-Client-IP"); 
        } 
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) { 
            ip = request.getRemoteAddr(); 
        }
        if (ip.split(",").length > 1) {
			ip = ip.split(",")[0];
		}
        return ip;
    }
    
    /**
     * 
     * <p>Description: [如果URL地址不是以protocol开头，就补全协议]</p>
     * Created on 2015年8月27日
     * @param url 原始URL
     * @param protocol 要加的协议名：例如：http://
     * @return http://xxx
     * @author:[宋文斌]
     */
	public String addProtocol(String url,String protocol) {
		if (!StringUtils.isBlank(url) && !url.startsWith(protocol)) {
			url = protocol + url;
		}
		return url;
	}
}
