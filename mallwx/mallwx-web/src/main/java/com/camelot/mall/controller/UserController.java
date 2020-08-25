package com.camelot.mall.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.common.util.DateUtils;
import com.camelot.mall.Constants;
import com.camelot.mall.orderWx.OrderDataNeedInfo;
import com.camelot.mall.orderWx.SendWeiXinMessage;
import com.camelot.mall.service.ShopCartService;
import com.camelot.mall.shopcart.Product;
import com.camelot.mall.util.CaptchaResult;
import com.camelot.mall.util.CaptchaUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.SendWeiXinMessageUtil;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;

@RequestMapping("user")
@Controller
public class UserController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserController.class);
	@Resource
	private UserExportService userExportService;
	@Resource
	private AddressInfoService addressInfoService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private MessagePublisherService smsVerifyCodeQueuePublisher;
	@Resource
	private MessagePublisherService emailVerifyCodeQueuePublisher;
	@Resource
	private CaptchaUtil captchaUtil;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private RedisDB redisDB;
	
	@Resource
	private ShopCartService shopCartService;
	
	/**
	 *
	 * @author 马国平 创建时间：2015-6-5 上午11:36:13
	 * @return
	 */
	@RequestMapping("registe")
	public String toRegiste() {

		return "user/userRegiste";
	}

	/**
	 * 用户登录
	 * @author 马国平 创建时间：2015-6-5 下午4:52:33
	 * @return
	 */
	@RequestMapping("login")
	@ResponseBody
	public String login(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,
			String url,
			HttpSession session,
			@CookieValue(value = Constants.USER_TOKEN, required = false, defaultValue = "") String userToken,
			@RequestParam(value = "remember", required = false, defaultValue = "") String remember,
			@RequestParam("userName") String userName,
			@RequestParam("password") String password,
			@RequestParam("pic_captcha_id") String picCaptchaType) {
		password = MD5.encipher(password);

		if(remember != null){
			CookieHelper.setCookie(response, Constants.AUTO_LOGON, "1");
		} else {
			CookieHelper.setCookie(response, Constants.AUTO_LOGON, "0");
		}
		String key = getToken(userName, request);
		ExecuteResult<LoginResDTO> er = userExportService.login(userName, password, key);
		if(er.isSuccess()){
		/**
			 * 新加验证码问题验证验证码是否有效
			 * zhm
			 * 
		*/
			String codeKey =  CaptchaUtil.KET_TOP_P + request.getSession().getId();//redisDB获取值
			if(StringUtils.isNoneBlank(picCaptchaType)){//前台获取输入的验证码
			boolean isCaptchaUsefull = captchaUtil.check(codeKey, picCaptchaType);
			if(!isCaptchaUsefull){
				JSONObject jores=new JSONObject();
				jores.put("errmsg", "图片验证码错误");			
				return jores.toJSONString();
			  }
			}
			LoginResDTO loginResDTO = er.getResult();
			if(loginResDTO.getUstatus() > 1){
				//登陆成功了
				CookieHelper.setCookie(response, Constants.USER_TOKEN, MD5.encipher(userName));
				CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
				CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
				CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());
				//把登录的uid放到session中，会在拦截器中调用
				session.setAttribute("uid",loginResDTO.getUid());
				/*if(url != null && url != ""){
					return "redirect:" + url.substring(url.indexOf("/", 1), url.length());
				}*/
				UserDTO user = userExportService.queryUserById(loginResDTO.getUid());
				JSONObject jores=new JSONObject();
				//登录成功之后返回用户类型
				jores.put("usertype", user.getUsertype());
				jores.put("success", true);
				
				//-----------------add by xj   登录成功消息发送--------------------------//
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.LOGIN);
				message.setFirst("【印刷家】尊敬的用户，您已成功登录小管家。");
				message.setKeyword1(userName);
				message.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(user.getUid()), message, request, response);
				//-----------------add by xj   登录成功消息发送--------------------------//
				return jores.toJSONString();
			} else {
				LOGGER.debug("不知道什么异常");
				/*attr.addAttribute("uid", loginResDTO.getUid());
				attr.addAttribute("loginname", loginname);
				attr.addAttribute("loginpwd", loginpwd);
				attr.addAttribute("again", "1");
				return "redirect:/user/register1";*/
				JSONObject jores=new JSONObject();
				jores.put("errmsg", "不知道什么异常");
				return jores.toJSONString();
			}
		} else {
			String message = "";
			if (er.getErrorMessages() != null && er.getErrorMessages().size() > 0)
				message = er.getErrorMessages().get(0);
			LOGGER.info(message);
			JSONObject jores=new JSONObject();
			jores.put("errmsg", message);
			jores.put("logname", userName);
			jores.put("url", url);
			return jores.toJSONString();
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
				
				//-----------------add by xj   登录成功消息发送--------------------------//
				SendWeiXinMessage message = new SendWeiXinMessage();
				message.setModeId(WeiXinMessageModeId.LOGIN);
				message.setFirst("【印刷家】尊敬的用户，您已成功登录小管家。");
				message.setKeyword1(loginname);
				message.setKeyword2(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				SendWeiXinMessageUtil.sendWeiXinMessage(String.valueOf(loginResDTO.getUid()), message, request, response);
				//-----------------add by xj   登录成功消息发送--------------------------//
				
				return er;
			} else {
				return er;
			}
		} else {
			return er;
		}
	}
	/**
	 * <p>Discription:[生成用户登录token]</p>
	 * Created on 2015年4月1日
	 * @param logname
	 * @param request
	 * @return
	 * @author:[胡恒心]
	 */
	public static String getToken(String logname, HttpServletRequest request){
		StringBuffer buffer = new StringBuffer();
		buffer.append(MD5.encipher(logname));
		buffer.append("|");
		//buffer.append(request.getRemoteAddr());
		//buffer.append("|");
		buffer.append(SysProperties.getProperty("token.suffix"));
		return buffer.toString();
	}

	/**
	 * 清理登录相关的cookie，并不会清除购物车的key
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 */
	@RequestMapping("unlogin")
	@ResponseBody
	public String unlogin(HttpServletRequest request,
						  HttpServletResponse response,HttpSession session){
		//并不会清除购物车的key
		CookieHelper.delCookie(request, response, Constants.USER_TOKEN);
		CookieHelper.delCookie(request, response, Constants.USER_ID);
		CookieHelper.delCookie(request, response, Constants.USER_NAME);
		CookieHelper.delCookie(request, response, Constants.REQUEST_URL);
		CookieHelper.delCookie(request, response,"logname");
		session.setAttribute("uid",null);
		return "true";
	}
	/**
	 * 返回已登录的帐号类型
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("checkLogin")
	@ResponseBody
	public String isLogin(HttpServletRequest request,
						  HttpServletResponse response,HttpSession session){
		String logname = CookieHelper.getCookieVal(request, Constants.USER_TOKEN);
		String token = LoginToken.getLoginToken(request);
		JSONObject jores=new JSONObject();
		if(StringUtils.isBlank(logname)){
			LOGGER.debug("没有检测到token信息，直接清理掉登录相关的cookie，退出登录");
			//this.unlogin(request,response,session);
			jores.put("isLogin", false);
		}else{
			RegisterDTO registerDTO = userExportService.getUserByRedis(token);
			if(registerDTO == null){
				LOGGER.debug("从token没有得到用户信息，清理掉登录相关的cookie，退出登录");
				//this.unlogin(request,response,session);
				jores.put("isLogin", false);
			}else{
				LOGGER.debug("从token得到了用户信息，userId:"+registerDTO.getUid());
				UserDTO user = userExportService.queryUserById(registerDTO.getUid());
				jores.put("isLogin", true);
				jores.put("userType", user.getUsertype());
				jores.put("userStatus", user.getUserstatus());
				jores.put("auditStatus", user.getAuditStatus());
				if(user.getUserstatus()>2){
					//用户-扩展信息
					ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(user.getUid());
					UserInfoDTO userInfoDTO = userInfoResult.getResult();
					UserDTO userRemark = userInfoDTO.getUserDTO();
					jores.put("auditRemark", userRemark.getAuditRemark());
				}
			}
		}

		return jores.toJSONString();
	}
	
	
	/**
	 * 计算首页购物车数量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("shocar")
	@ResponseBody
	public String shocar(HttpServletRequest request,
						  HttpServletResponse response,HttpSession session){

		JSONObject jores=new JSONObject();
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		String ctoken = CookieHelper.getCookieVal(request, Constants.CART_TOKEN);
		List<Product> allProducts =shopCartService.AllProducts(ctoken, uid);
		Integer quantity=0;
		for(int i=0;i<allProducts.size();i++){
			Product product=allProducts.get(i);
			quantity+=product.getQuantity();
		}
			jores.put("quantity", quantity);
		return jores.toJSONString();
	}
	/**
	 * 根据uid查询用户信息
	 * @return
	 */
	@RequestMapping(value="/queryUserInfoById")
	public String queryUserInfoById(@CookieValue(value=Constants.USER_ID) Long uid,
									@RequestParam(value="cus",required=false,defaultValue="buyers") String cus,
									Model model){
		UserDTO user = userExportService.queryUserById(uid);
		if(null!=user){
			model.addAttribute("user", user);
			model.addAttribute("cus", cus);
		}
		return "/user/userInfo";
	}

	/**
	 * 查询用户所有的地址
	 * @param uid
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/queryAddress")
	public String queryAddress(@CookieValue(value=Constants.USER_ID) Long uid,
							   @RequestParam(value="cus",required=false,defaultValue="buyers") String cus,
							   Model model){
		DataGrid<AddressInfoDTO> dg = addressInfoService.queryAddressinfo(uid);
		JSONArray addresses = new JSONArray();
		if(dg!=null){
			for( int i=0; i<dg.getRows().size(); i++ ){
				AddressInfoDTO addr = dg.getRows().get(i);
				JSONObject address = JSON.parseObject(JSON.toJSONString(addr));
				ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService.queryNameById(Integer.valueOf(addr.getProvicecode()));
				address.put("provicename", erProvice.getResult().getName());
				ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService.queryNameById(Integer.valueOf(addr.getCitycode()));
				address.put("cityname", erCity.getResult().getName());
				ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService.queryNameById(Integer.valueOf(addr.getCountrycode()));
				address.put("countryname", erCountry.getResult().getName());
				addresses.add(address);
			}
		}
		model.addAttribute("addresses", addresses);
		model.addAttribute("cus", cus);
		return "/user/userAddresses";
	}
	/**
	 *
	 * @param orderDataNeedInfo
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toEditAddress")
	public String toEditAddress(OrderDataNeedInfo orderDataNeedInfo,@CookieValue(value=Constants.USER_ID) String uid,
								@RequestParam(value="cus",required=false,defaultValue="buyers") String cus,
								Model model){
		model.addAttribute("address", orderDataNeedInfo.getAddress());
		model.addAttribute("uid", uid);
		model.addAttribute("cus", cus);
		return "/user/editAddress";
	}
	/**
	 * 设置密码
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/toSetPwd")
	public String setPwd(HttpServletRequest request,Model model){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			throw new RuntimeException("没有登录");
		}
		registerDTO.setUserEmail(this.hideUserEmail(registerDTO.getUserEmail()));
		registerDTO.setUserMobile(this.hideUserCellPhone(registerDTO.getUserMobile()));
		model.addAttribute("userinfo", registerDTO);
		return "/user/setPayPwd";
	}
	/**
	 * 隐藏邮箱中间四位
	 * @param email
	 * @return
	 */
	private String hideUserEmail(String email)
	{
		if(StringUtils.isNotBlank(email))
		{
			//Email去掉@后，剩下的字符串
			String emailTemp = email.substring(0, email.indexOf("@"));
			if(emailTemp.length() > 6)
			{
				//拼接隐藏用户中间四位的邮箱，格式：邮箱前（一半 -2）个字符 + "****"(4个*) + 邮箱（一半 + 2）后的字符。
				//邮箱前（一半 -2）个字符
				StringBuilder temp = new StringBuilder(emailTemp.substring(0, (emailTemp.length()-4) / 2));
				//"****"(4个*)
				temp.append("****");
				//邮箱（一半 + 2）后的字符
				temp.append(email.substring((emailTemp.length()-4) / 2 + 4));
				email = temp.toString();
			}else
			{
				StringBuilder temp = new StringBuilder(email.charAt(0) + "");
				for(int i =0; i < emailTemp.length() -2; i ++)
				{
					temp.append("*");
				}
				temp.append(email.substring(emailTemp.length() - 1)).toString();
				email = temp.toString();
			}
		}
		return email;
	}
	/**
	 * 隐藏手机号中间四位
	 * @param cellPhone 手机号
	 * @return
	 */
	private String hideUserCellPhone(String cellPhone)
	{
		if(StringUtils.isNotBlank(cellPhone) && cellPhone.trim().length() > 7)
		{
			//隐藏手机号码中间四位
			StringBuilder temp = new StringBuilder().append(cellPhone.substring(0, 3)).append("****").append(cellPhone.substring(7));
			cellPhone = temp.toString();
		}
		return cellPhone;
	}


	/**
	 * 发送验证码
	 * @param type 邮箱-1/手机-2/新邮箱-3/新手机-4
	 *  由于页面要隐藏手机号或邮箱中间四位， 所以type = 1,2要使用从数据库中查询的来的手机号或密码
	 *  3,4表示更新或绑定的手机号或密码，需要用新的手机号或邮箱接收验证码
	 *  @param userId ,用于找回密码操作
	 *  @param smsType
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/send")
	public String send(HttpServletRequest request, Integer type, String smsType,String contact,Long userId,String code,String picCaptchaType){
		LOGGER.info("type :" + type);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//如果是注册模块，手机接收验证码需要图片验证码验证，防止攻击
		if("REGISTER".equals(smsType)&&2==type){
			//验证验证码是否有效
			String codeKey = "";
			if("p_register".equals(picCaptchaType)){
				codeKey = CaptchaUtil.KET_TOP_P + request.getSession().getId();
			}else if("e_register".equals(picCaptchaType)){
				codeKey = CaptchaUtil.KET_TOP_E + request.getSession().getId();
			}else{
				codeKey = CaptchaUtil.KET_TOP + request.getSession().getId();
			}
			boolean isCaptchaUsefull = captchaUtil.check(codeKey, code);
			if(!isCaptchaUsefull){
				resultMap.put("isOK", 1);
				resultMap.put("message", "图片验证码错误");
				return JSON.toJSONString(resultMap);
			}
		}


		//判断是否为注册
		if(!"REGISTER".equals(smsType)&&!"EMAIL_REGISTER".equals(smsType)){
			if(userId == null){
				userId = WebUtil.getInstance().getUserId(request);
			}
			UserDTO userDTO = userExportService.queryUserById(userId);
			if(new Integer(1).equals(type)){
				//邮箱号
				contact = userDTO.getUserEmail();
			}else if(new Integer(2).equals(type)){
				//手机号
				contact = userDTO.getUmobile();
			}
		}
		if(StringUtils.isEmpty(contact)){
			resultMap.put("isOK", 1);
			resultMap.put("message", "手机号或邮箱为空");
			return JSON.toJSONString(resultMap);
		}
		String key = UUID.randomUUID().toString().replace("-", "");
		String[] addrs = { contact };
		//将发送短信的手机号存入到redis中，限定每个手机号每天只能收到3条短信
		if(type==2||type==4){
			for(String addr :addrs){
				String sendCount = redisDB.getHash(smsType+addr, smsType);
				//sendCount为空，则说明当前还没有发送过短信
				if(sendCount == null){
					String endTime = DateUtils.getDateStr(DateUtils.dayOffset(new Date(), +1), DateUtils.YMD_DASH);
					Date endDate = DateUtils.parseReturnTime(endTime, DateUtils.YMD_DASH);
					redisDB.setHash(smsType+addr, smsType, "1");
					redisDB.setExpire(smsType+addr, endDate);
				}else if(Integer.parseInt(sendCount) >=3){
					//如果今天发送短信次数已经超过3次（包括3次），则不再发送短信
					resultMap.put("isOK", 1);
					resultMap.put("message", "验证码每天最多仅发送3次");
					return JSON.toJSONString(resultMap);
				}else{
					int newSendCount = Integer.parseInt(sendCount)+1;
					//不再重新设置过时时间
					redisDB.setHash(smsType+addr, smsType, newSendCount+"");
				}
			}
		}

//		SmsTypeEnum smsEnum = SmsTypeEnum.valueOf(smsType);
		type = type > 2? type -2: type;
		//2015-06-10王东晓添加

		VerifyCodeMessageDTO messageDTO = new VerifyCodeMessageDTO();
		messageDTO.setEnumType(smsType);
		messageDTO.setKey(key);
		messageDTO.setAddress(addrs);
		messageDTO.setType(type+"");
		//根据不同的验证码类型，发入不同的消息队列
		boolean isSuccess = false;
		if(type==2){//短信
			isSuccess = smsVerifyCodeQueuePublisher.sendMessage(messageDTO);
		}else if(type==1){//邮件
			isSuccess = emailVerifyCodeQueuePublisher.sendMessage(messageDTO);
		}
		//王东晓添加end
//		ExecuteResult<String> result = baseWebSiteMessageService.sendVerificationCode(smsType, key, addrs, type);

		if(isSuccess){
			//成功 2
			resultMap.put("isOK", 2);
			resultMap.put("codeKey", key);
		} else {
			//失败
			resultMap.put("isOK", 1);
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 校验验证码
	 *
	 * codeKey: 存放在redis中验证码的Key
	 * @return
	 */
	@RequestMapping("/check")
	public void check(String codeKey, String code, HttpServletRequest request,
					  HttpServletResponse response) {
		boolean b = captchaUtil.check(codeKey, code);
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
	 * <p>Discription:设置支付密码</p>
	 * Created on 2015年3月6日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("setpaypwd")
	public String setpaypwd(Model model, Long uid, String level, String paypwd, String captcha,String backUrl,String userType, HttpServletRequest request){
//		String token = LoginToken.getLoginToken(request);
//		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(uid != null && paypwd != null){
			paypwd = MD5.encipher(paypwd);
			ExecuteResult<String> result = userExportService.modifyPaypwdById(uid, paypwd, null, level);
//			registerDTO.setPayCode(paypwd);
//			userExportService.updateUserInfoToRedis(token, registerDTO);
			model.addAttribute("msg", "设置支付密码");
			model.addAttribute("backUrl", backUrl == null ? "":backUrl);
			model.addAttribute("userType",userType);
			request.setAttribute("result", result);
			return "redirect:/user/queryUserInfoById?uid="+uid+"&cus=buyers";
		}
		return "redirect:/user/toSetPwd";
	}

	/**
	 * 跳转到银行列表去充值
	 * @return
	 */
	@RequestMapping("toBankList")
	public String toBankList(){
		return "order/bankList";
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
	 * 跳转到用户收藏页面
	 * @return
	 */
	@RequestMapping("toFav")
	public String toFav(Model model){

		model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
		return "user/favourite";
	}
}
