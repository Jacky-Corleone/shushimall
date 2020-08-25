package com.camelot.mall.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.common.util.DateUtils;
import com.camelot.mall.util.CaptchaUtil;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

/**
 * 负责发送和校验验证码的控制器
 * @author fanjianghua
 *
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CaptchaController.class);
	
	@Resource
	private UserExportService userExportService = null;
	
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService = null;
	@Resource
	private MessagePublisherService emailVerifyCodeQueuePublisher;
	@Resource
	private MessagePublisherService smsVerifyCodeQueuePublisher;
	@Resource
	private CaptchaUtil captchaUtil;
	@Resource
	private RedisDB redisDB;
	
	
	/**
	 * 验证是否手机号是否符合发送验证码的需求，每个手机号，同一验证码每天只接收3次
	 * 
	 *  @param smsType   模块类型
	 */
	@ResponseBody
	@RequestMapping("/isSendCaptcha")
	public Map<String, Object> isSendCaptcha(HttpServletRequest request,String contact){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String smsType = "REGISTER";
		String[] addrs = { contact };
		for(String addr :addrs){
			String sendCount = redisDB.getHash(smsType+addr, smsType);
			//sendCount为空，则说明当前还没有发送过短信
			if(sendCount == null){
				
			}
			else if(Integer.parseInt(sendCount) >=3){
				//如果今天发送短信次数已经超过3次（包括3次），则不再发送短信
				resultMap.put("isOK", 1);
				resultMap.put("message", "验证码每天最多仅发送3次");
				return resultMap;
			}
		}
		resultMap.put("isOK", 2);
		return resultMap;
		
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
	public Map<String, Object> send(HttpServletRequest request, Integer type, String smsType,String contact,Long userId,String code,String picCaptchaType){
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
				return resultMap; 
			}
		}
		
		
		//判断是否为注册
		if(!"REGISTER".equals(smsType)&&!"EMAIL_REGISTER".equals(smsType)&&!"ORDERSUBMITPROXY".equals(smsType)){
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
			return resultMap; 
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
					return resultMap;
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
		return resultMap;
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
	 * 校验图片验证码
	 * 
	 * codeKey: 存放在redis中验证码的Key
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkCaptcha")
	public Map<String, Object> checkCaptcha(String code, HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		String codeKey = CaptchaUtil.KET_TOP + request.getSession().getId();
		boolean b = captchaUtil.check(codeKey, code);
		
		if(!b){
			resultMap.put("isOK", 1);
			resultMap.put("message", "图片验证码错误");
		}else {
			resultMap.put("isOK", 0);
		}
		return resultMap;
	}
	
}
