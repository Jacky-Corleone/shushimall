package com.camelot.mall.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.CookieHelper;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.mall.Constants;
import com.camelot.mall.util.CaptchaResult;
import com.camelot.mall.util.CaptchaUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserController.class);

	@Resource
	private UserExportService userExportService = null;

	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService = null;

	@Resource
	private MallAdExportService mallAdvertisService;

	@Resource
	private LogoExportService logoService;
	@Resource
	private MessagePublisherService emailVerifyCodeQueuePublisher;
	@Resource
	private MessagePublisherService smsVerifyCodeQueuePublisher;
	@Resource
	private RedisDB redisDB;
	/**
	 * <p>Discription:注册页面</p>
	 * Created on 2015年3月12日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("/registerInfo")
	public String registerInfoPage() {
		return "/user/registerInfo";
	}

	/**
	 * <p>Discription:获取图片验证码</p>
	 * Created on 2015年3月12日
	 * @param request
	 * @param response
	 * @author:胡恒心
	 */
	@RequestMapping("/acquire")
	public void acquire(HttpServletRequest request, HttpServletResponse response,String type) {
		CaptchaUtil captchaUtil = new CaptchaUtil();
		CaptchaResult captchaResult = captchaUtil.acquire(request,type);
//		userExportService.saveVerifyCodeToRedis(captchaResult.getKey(),
//				captchaResult.getValue());
		redisDB.setAndExpire(captchaResult.getKey(), captchaResult.getValue(), 600);
		BufferedImage img = captchaResult.getBufferedImage();
		try {
			ImageIO.write(img, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}

	/**
	 * <p>Discription:校验用户名是否重复</p>
	 * Created on 2015年3月12日
	 * @param loginname
	 * @param response
	 * @author:胡恒心
	 */
	@RequestMapping("/verifyLoginName")
	public void verifyLoginName(String loginname, HttpServletResponse response) {
		boolean b = userExportService.verifyLoginName(loginname);
		String result = "{\"message\": " + b + "}";
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}

	@RequestMapping("/getUidByMobile")
	@ResponseBody
	public Object getUidByMobile(String mobile) {

		UserDTO userDTO = new UserDTO();
		userDTO.setUmobile(mobile);
		UserDTO dto = userExportService.findUserByUserNameOrEmailOrPhone(userDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		if (dto != null) {
			Long uid = dto.getUid();
			result.put("uid", uid);
		}
		return result;
	}

	/**
	 * 过期方法，等待删除
	 * <p>Discription:给手机或邮箱发送验证码</p>
	 * Created on 2015年3月12日
	 * @param addr
	 * @param request
	 * @param response
	 * @author:胡恒心
	 */
	@RequestMapping("/verifyEmailOrMobileExist")
	public void verifyEmailOrMobileExist(String addr,
			HttpServletRequest request, HttpServletResponse response) {
		boolean b = userExportService.verifyEmailOrMobile(addr);
		Pattern p = Pattern.compile("^[1]{1}[0-9]{10}$");
		String result = null;
		if (b) {
			String key = request.getSession().getId();
			String[] addrs = { addr };
			
			//2015-06-10王东晓修改
			
			VerifyCodeMessageDTO messageDTO = new VerifyCodeMessageDTO();
			messageDTO.setEnumType("REGISTER");
			messageDTO.setKey(key);
			messageDTO.setAddress(addrs);
			//根据不同的验证码类型，发入不同的消息队列
			boolean isSuccess = false;
			
			if(p.matcher(addr).matches()){
				messageDTO.setType("2");
				isSuccess = smsVerifyCodeQueuePublisher.sendMessage(messageDTO);
//				this.baseWebSiteMessageService.sendVerificationCode("REGISTER", key, addrs, 2);
			}else {
				messageDTO.setType("1");
				isSuccess = emailVerifyCodeQueuePublisher.sendMessage(messageDTO);
//				this.baseWebSiteMessageService.sendVerificationCode("REGISTER", key, addrs, 1);
			}
			//王东晓修改end
			result = "{\"message\": true}";
		} else {
			result = "{\"message\": false}";
		}
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}



	/**
	 * <p>Discription:登录页面</p>
	 * Created on 2015年3月12日
	 * @param model
	 * @param request
	 * @param msg
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping({ "login" })
	public String loginUI(Model model, HttpServletRequest request, Integer msg) {
		String uname = null;
		uname = CookieHelper.getCookieVal(request, "uname");
		if(uname != null){
			model.addAttribute("logname", uname);
//			model.addAttribute("logpwd", CookieHelper.getCookieVal(request, "lpwd"));
		}
//		if(msg != null && msg == 1){
//			model.addAttribute("loginmsg", "用户名或密码错误");
//		} else if(msg != null && msg == 2){
//			model.addAttribute("loginmsg", "2");
//		}
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(1000);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(2);
		DataGrid<MallAdDTO> dataGrid = mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		List<MallAdDTO> list = dataGrid.getRows();
		if( list != null && list.size() > 0 ){
			int idx = new Random().nextInt(list.size());
			MallAdDTO mallAdDTO = list.get(idx);
			model.addAttribute("mallAd", mallAdDTO);
		}
        String retUrl = request.getParameter("retUrl");
        if(StringUtils.isNotBlank(retUrl)){
            request.setAttribute("url", retUrl);
        }
      //退出bbs登录
        String bbs_logout = request.getParameter("o");
        String bbs_url = SysProperties.getProperty("bbs_url");
        request.setAttribute("bout", bbs_logout);
        request.setAttribute("bbs_url", bbs_url);
		return "/user/login";
	}

	/**
	 * <p>Discription:验证用户登录</p>
	 * Created on 2015年3月12日
	 * @param captcha
	 * @param loginname
	 * @param loginpwd
	 * @param remember
	 * @param response
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("/homepage")
    @ResponseBody
	public Map<String,String> verifyLogin(RedirectAttributes attr, String captcha, String loginname, String loginpwd, String url, String[] remember,
			HttpServletResponse response, HttpServletRequest request,Model model) {
        Map<String,String> map = new HashMap<String, String>();
        
        String remoteAddr = WebUtil.getInstance().getIpAddr(request);
		if (!StringUtils.isBlank(redisDB.get(remoteAddr)) 
				&& Integer.parseInt(redisDB.get(remoteAddr)) >= 3) {
			if(StringUtils.isBlank(captcha)){
				map.put("message", "needcode");
				return map;
			}
			String value = userExportService.getValueByRedis(CaptchaUtil.KET_TOP + request.getSession().getId());
			LOGGER.info("测试前台验证码获取：front captcha:"+captcha+"; redis captcha:"+value);
			boolean b = false;
			if (captcha != null) {
				b = captcha.toLowerCase().equals(value);
			}
			if (!b) {
				map.put("message", "errcode");
				return map;
			}
		}
        String bbspwd = loginpwd;
		loginpwd = MD5.encipher(loginpwd);
		if(remember != null){
			CookieHelper.setCookie(response, Constants.AUTO_LOGON, "1");
		} else {
			CookieHelper.setCookie(response, Constants.AUTO_LOGON, "0");
		}
		String key = getToken(loginname, request);
		ExecuteResult<LoginResDTO> er = userExportService.login(loginname, loginpwd, key);
		if(er.isSuccess()){
			LoginResDTO loginResDTO = er.getResult();
			if(loginResDTO.getUstatus() > 1){
				CookieHelper.setCookie(response, "logging_status", MD5.encipher(loginname));
				CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
				CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
				CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());
                map.put("message", "可以登录");
                redisDB.del(remoteAddr);
                //同步注册/登录论坛
//                String bbs_url = SysProperties.getProperty("bbs_url");
//                String bbs_key = SysProperties.getProperty("bbs_key");//密钥
//                long logintime = (System.currentTimeMillis()/1000);//时间戳（秒）
//                String bbs_token = MD5.encipher(logintime+loginname+bbs_key);
                //System.out.println("http://"+bbs_url+"/go.php?n="+loginname+"&t="+logintime+"&c="+bbs_token+"&p="+bbspwd+"&bbs_key="+bbs_key);
//                BbsController.doGet("http://"+bbs_url+"/go.php?n="+loginname+"&t="+logintime+"&c="+bbs_token+"&p="+bbspwd,null,"UTF-8",true);
//                if(url.equals(bbs_url)){
//                	map.put("retUrl", "http://"+bbs_url+"/go.php?n="+loginname+"&t="+logintime+"&c="+bbs_token);
//                    return map;
//                }
//                if(url != null && !"".equals(url)){
//                    map.put("retUrl", SysProperties.getProperty("site.domain")+url);
//                    return map;
//					//return "redirect:" + url;
//				}
                map.put("retUrl", SysProperties.getProperty("site.domain"));
                request.setAttribute("GrowthUserId",loginResDTO.getUid()); //用于成长值拦截器   登录成功，用户id
                return map;
				//return "redirect:/";
			} else {
				attr.addAttribute("uid", loginResDTO.getUid());
				attr.addAttribute("loginname", loginname);
				attr.addAttribute("loginpwd", loginpwd);
				attr.addAttribute("again", "1");
                map.put("message","");
                map.put("retUrl","/user/register1");
                return map;
				//return "redirect:/user/register1";
			}
		} else {
			// 将用户两小时中登录错误次数放入redis
			if (StringUtils.isBlank(redisDB.get(remoteAddr))) {
				redisDB.setAndExpire(remoteAddr, "1", 60 * 60 * 2);
			} else {
				int errCount = Integer.parseInt(redisDB.get(remoteAddr));
				redisDB.setAndExpire(remoteAddr, String.valueOf(++errCount), 60 * 60 * 2);
			}
			model.addAttribute("logname", loginname);
			model.addAttribute("url", url);
			String message = "";
			if (er.getErrorMessages() != null && er.getErrorMessages().size() > 0)
				message = er.getErrorMessages().get(0);
			model.addAttribute("loginmsg", message );

            Pager page = new Pager();
            page.setPage(1);
            page.setRows(1000);
            MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
            mallAdQueryDTO.setStatus(1);
            mallAdQueryDTO.setAdType(2);
            DataGrid<MallAdDTO> dataGrid = mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
            List<MallAdDTO> list = dataGrid.getRows();
            if( list != null && list.size() > 0 ){
                int idx = new Random().nextInt(list.size());
                MallAdDTO mallAdDTO = list.get(idx);
                model.addAttribute("mallAd", mallAdDTO);
            }

            map.put("message", message);
            map.put("retUrl","/user/login");
            return map;
//            return "/user/login";
		}
	}

	@RequestMapping("ajaxLogin")
	@ResponseBody
	public ExecuteResult<LoginResDTO> ajaxLogin(String loginname, String loginpwd, String[] remember, Integer def, HttpServletRequest request, HttpServletResponse response){
		loginpwd = MD5.encipher(loginpwd);
		String key = getToken(loginname, request);
		ExecuteResult<LoginResDTO> er = userExportService.login(loginname, loginpwd, key);
		if(er.isSuccess()){
			LoginResDTO loginResDTO = er.getResult();
			if(loginResDTO.getUstatus() > 1){
				CookieHelper.setCookie(response, "logging_status", MD5.encipher(loginname));
				CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
				CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());
				return er;
			} else {
				return er;
			}
		} else {
			return er;
		}
	}

	@RequestMapping("/checkPwd")
	@ResponseBody
	public ExecuteResult<String> checkPwd(HttpServletRequest request,String pwd){
		ExecuteResult<String> er = new ExecuteResult<String>();
		pwd = MD5.encipher(pwd);
		String loginname = CookieHelper.getCookieVal(request, Constants.USER_NAME);
		String key = getToken(loginname, request);
		ExecuteResult<LoginResDTO> err = userExportService.login(loginname, pwd, key);

		if( !err.isSuccess() ){
			er.addErrorMessage("error");
		}
		return er;
	}

	@RequestMapping("/changepwd1")
	public String changePwdByCode() {
		return "/user/changePwd/mobileAndEmail";
	}
/*
	@RequestMapping("/changepwdverifycode")
	public void changePwdVerifyCode(String code, HttpServletRequest request,
			HttpServletResponse response) {
		String value = userExportService.getValueByRedis(request
				.getSession().getId());

		boolean b = false;
		if (code != null) {
			b = code.equals(value);
		}
		String result = "{\"message\": " + b + "}";
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}*/

	@RequestMapping("/changepwd2")
	public String changePwdPage(Model model, String addr) {
		model.addAttribute("addr", addr);
		return "/user/changePwd/changepwd";
	}

	@RequestMapping("/changepwd3")
	public String changePwd(String addr, String loginpwd) {
		loginpwd = MD5.encipher(loginpwd);
		userExportService.modifyUserPwdByEmail(addr, loginpwd);
		return "/user/changePwd/changeSucceed";
	}

	@RequestMapping("/removeCodeByRedis")
	public void removeCodeByRedis(HttpServletRequest request, HttpServletResponse response){
		userExportService.saveVerifyCodeToRedis(request.getSession().getId(), "");
		String result = "{\"message\": " + true + "}";
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}

	/**
	 * <p>Discription:注销登录</p>
	 * Created on 2015年3月12日
	 * @param request
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("/logout")
	public String logoff(HttpServletRequest request, HttpServletResponse response){
		String token = LoginToken.getLoginToken(request);
		userExportService.removeValueByRedis(token);
		CookieHelper.delCookie(request, response, Constants.USER_TOKEN);
		CookieHelper.delCookie(request, response, Constants.USER_ID);
		CookieHelper.delCookie(request, response, Constants.USER_NAME);
		CookieHelper.delCookie(request, response, Constants.REQUEST_URL);
		return "redirect:/user/login?o="+(System.currentTimeMillis()/1000);
	}

	@ResponseBody
	@RequestMapping("/status")
	public Map<String, Object> status(HttpServletRequest request, HttpServletResponse response){
		String token = LoginToken.getLoginToken(request);
		String bbs_url = SysProperties.getProperty("bbs_url");
		String bbs_key = SysProperties.getProperty("bbs_key");//密钥
		Map<String, Object> map = new HashMap<String, Object>();
		if(token != null){
			RegisterDTO dto = userExportService.getUserByRedis(token);
			if(dto != null){
				map.put("msg", true);
				map.put("user", dto);
				map.put("b", bbs_url);
				map.put("u", dto.getLoginname());
				map.put("t", (System.currentTimeMillis()/1000));
				map.put("c", MD5.encipher((System.currentTimeMillis()/1000)+dto.getLoginname()+bbs_key));
				//获取用户类型
				UserDTO queryUserDTO = new UserDTO();
				queryUserDTO.setUname(dto.getLoginname());
				DataGrid<UserDTO> datagrid = userExportService.findUserListByCondition(queryUserDTO, null, null);
				int userType = datagrid.getRows().get(0).getUsertype();
				if(userType == 2){
					map.put("buyer",true);
				}else if(userType == 3){
					map.put("buyer",false);
				}
				return map;
			}else{
				LOGGER.debug("移除COOKIE中用户ID");
				CookieHelper.delCookie(request, response, Constants.USER_ID);
				CookieHelper.delCookie(request, response, Constants.USER_NAME);
			}
		}
		map.put("msg", false);
		return map;
	}

	/**
	 * <p>Discription:[生成用户登录token]</p>
	 * Created on 2015年4月1日
	 * @param logname
	 * @param request
	 * @return
	 * @author:[胡恒心]
	 */
	private String getToken(String logname, HttpServletRequest request){
		StringBuffer buffer = new StringBuffer();
		buffer.append(MD5.encipher(logname));
		buffer.append("|");
		buffer.append(request.getRemoteAddr());
		buffer.append("|");
		buffer.append(SysProperties.getProperty("token.suffix"));
		return buffer.toString();
	}

	/**
	 * <p>Discription:校验验证码</p>
	 * Created on 2015年3月12日
	 * @param captcha
	 * @param request
	 * @param response
	 * @author:胡恒心
	 */
	@RequestMapping("/verifyCaptcha")
	public void verifyCaptcha(String captcha, HttpServletRequest request, HttpServletResponse response) {
		String value = userExportService.getValueByRedis(CaptchaUtil.KET_TOP + request.getSession().getId());
		LOGGER.info("测试前台验证码获取：front captcha:"+captcha+"; redis captcha:"+value);
        boolean b = false;
		if (captcha != null) {
			b = captcha.toLowerCase().equals(value);
		}
		String result = "{\"message\": " + b + "}";
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}
	
	/**
	 * <p>Discription:根据用户登录失败次数来判断是否显示验证码</p>
	 * Created on 2015年8月5日
	 * @param request
	 * @param response
	 * @author:宋文斌
	 */
	@RequestMapping("/verifyLoginErrCount")
	public void verifyLoginErrCount(HttpServletRequest request, HttpServletResponse response) {
		String remoteAddr = WebUtil.getInstance().getIpAddr(request);
		boolean b = false;
		if (!StringUtils.isBlank(redisDB.get(remoteAddr))) {
			if (Integer.parseInt(redisDB.get(remoteAddr)) >= 3) {
				b = true;
			}
		}
		String result = "{\"message\": " + b + "}";
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}
	
}