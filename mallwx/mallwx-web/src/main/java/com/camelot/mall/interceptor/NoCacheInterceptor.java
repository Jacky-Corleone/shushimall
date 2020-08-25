package com.camelot.mall.interceptor;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.mall.controller.UserController;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;

/**
 * Created by 马国平 on 2015/8/12.
 */
public class NoCacheInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(TokenInterceptor.class);
    @Resource
    private UserExportService userExportService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        /*LOGGER.debug("NoCacheInterceptor拦截的路径：{}", request.getRequestURI());
        HttpSession session =request.getSession();
        String token = LoginToken.getLoginToken(request);
        String loginToken = CookieHelper.getCookieVal(request, Constants.USER_TOKEN);
        if(StringUtils.isEmpty(token) || StringUtils.isEmpty(loginToken)){
            LOGGER.debug("用户TOKEN为NULL，移除COOKIE中用户信息");
            CookieHelper.delCookie(request, response, Constants.USER_ID);
            CookieHelper.delCookie(request, response, Constants.USER_NAME);
            CookieHelper.delCookie(request, response, Constants.USER_TOKEN);
            return true;
        }

        //如果根据token取不到用户信息，那么就根据session中的uid再登录上去
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        if (registerDTO == null) {
            LOGGER.debug("根据token取不到用户信息，那么就根据session中的uid再登录上去");
            Long uid=(Long)session.getAttribute("uid");
            if(null != uid){
                UserDTO ud=userExportService.queryUserById(uid);
                String key = UserController.getToken(ud.getUname(), request);
                ExecuteResult<LoginResDTO> er = userExportService.login(ud.getUname(), ud.getPassword(), key);
                if(er.isSuccess()){
                    LoginResDTO loginResDTO = er.getResult();
                    if(loginResDTO.getUstatus() > 1){
                        //登陆成功了
                        CookieHelper.setCookie(response, Constants.USER_TOKEN, MD5.encipher(ud.getUname()));
                        CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
                        CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
                        CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());

                        //把登录的uid放到session中，会在拦截器中调用
                        session.setAttribute("uid",loginResDTO.getUid());
                    } else {
                        LOGGER.error("登录未知异常");
                    }
                }
            }else{
                LOGGER.debug("session中也没有uid信息，删除cookie");
                CookieHelper.delCookie(request, response, Constants.USER_ID);
                CookieHelper.delCookie(request, response, Constants.USER_NAME);
            }
            return true;
        }else{
            //如果token已经登录，而session没有登录，在session中设置uid
            //这里可以设置自动登录的逻辑
            if(null==session.getAttribute("uid")){
                LOGGER.debug("token已经登录，而session中没有uid信息，在session中设置uid");
                session.setAttribute("uid",registerDTO.getUid());
            }
        }*/
        return true;
    }

    public void dispose(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        if(isAjaxRequest(request)){
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(600);
            response.getWriter().write("");
        } else {
            @SuppressWarnings("rawtypes")
            Enumeration enumeration = request.getParameterNames();
            String name;
            String[] values;
            StringBuffer url = new StringBuffer();
            url.append(request.getRequestURI());
            url.append("?");
            while (enumeration.hasMoreElements()) {
                name = (String) enumeration.nextElement();
                values = request.getParameterValues(name);
                for (int i = 0; i < values.length; i++) {
                    url.append(name);
                    url.append("=");
                    url.append(values[i]);
                    url.append("&");
                }
            }
            request.setAttribute("url", url.toString());
            request.getRequestDispatcher("/user/login").forward(request, response);
        }
    }
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        LOGGER.debug("NoCacheInterceptor拦截的路径：{}", request.getRequestURI());
        HttpSession session =request.getSession();
        String token = LoginToken.getLoginToken(request);
        String loginToken = CookieHelper.getCookieVal(request, Constants.USER_TOKEN);
        if(StringUtils.isEmpty(token) || StringUtils.isEmpty(loginToken)){
            LOGGER.debug("用户TOKEN为NULL，移除COOKIE中用户信息");
            CookieHelper.delCookie(request, response, Constants.USER_ID);
            CookieHelper.delCookie(request, response, Constants.USER_NAME);
            CookieHelper.delCookie(request, response, Constants.USER_TOKEN);
        }

        //如果根据token取不到用户信息，那么就根据session中的uid再登录上去
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        if (registerDTO == null) {
            LOGGER.debug("根据token取不到用户信息，那么就根据session中的uid再登录上去");
            Long uid=(Long)session.getAttribute("uid");
            if(null != uid){
                UserDTO ud=userExportService.queryUserById(uid);
                String key = UserController.getToken(ud.getUname(), request);
                ExecuteResult<LoginResDTO> er = userExportService.login(ud.getUname(), ud.getPassword(), key);
                if(er.isSuccess()){
                    LoginResDTO loginResDTO = er.getResult();
                    if(loginResDTO.getUstatus() > 1){
                        //登陆成功了
                        CookieHelper.setCookie(response, Constants.USER_TOKEN, MD5.encipher(ud.getUname()));
                        CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
                        CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
                        CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());

                        //把登录的uid放到session中，会在拦截器中调用
                        session.setAttribute("uid",loginResDTO.getUid());
                    } else {
                        LOGGER.error("登录未知异常");
                    }
                }
            }else{
                LOGGER.debug("session中也没有uid信息，删除cookie");
                CookieHelper.delCookie(request, response, Constants.USER_ID);
                CookieHelper.delCookie(request, response, Constants.USER_NAME);
            }
        }else{
            //如果token已经登录，而session没有登录，在session中设置uid
            //这里可以设置自动登录的逻辑
            if(null==session.getAttribute("uid")){
                LOGGER.debug("token已经登录，而session中没有uid信息，在session中设置uid");
                session.setAttribute("uid",registerDTO.getUid());
            }
        }
    }

    /**
     * <p>Discription:判断是否为Ajax请求</p>
     * Created on 2015年3月20日
     * @param request
     * @return 是true, 否false
     * @author:胡恒心
     */
    public boolean isAjaxRequest(HttpServletRequest request) {
        String requestType = request.getHeader("X-Requested-With");
        if (requestType != null && requestType.equals("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }
}
