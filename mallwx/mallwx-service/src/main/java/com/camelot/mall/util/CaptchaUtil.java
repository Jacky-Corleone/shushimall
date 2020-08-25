package com.camelot.mall.util;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.camelot.openplatform.dao.util.RedisDB;

@Service
public class CaptchaUtil {
	
	@Resource
	private RedisDB redisDB;
	
	public static final String KET_TOP = "USER_REGISTER_VALIDATOR_CODE_";
	public static final String KET_TOP_P = "P_USER_REGISTER_VALIDATOR_CODE_";//个人注册，接收手机验证码时的图片验证码
	public static final String KET_TOP_E = "E_USER_REGISTER_VALIDATOR_CODE_";//企业注册，接收手机验证码的图片验证码
	public CaptchaResult acquire(HttpServletRequest request,String type){
		VCode vc = new VCode();
		CaptchaResult captchaResult = new CaptchaResult();
		captchaResult.setBufferedImage(vc.getImage());
		String key = "";
		if("p_register".equals(type)){//个人注册
			key = KET_TOP_P + request.getSession().getId();
		}else if("e_register".equals(type)){//企业注册
			key = KET_TOP_E + request.getSession().getId();
		}else{
			key = KET_TOP + request.getSession().getId();
		}
		
		captchaResult.setKey(key);
		captchaResult.setValue(vc.getCode().toLowerCase());
		return captchaResult;
	}
	
	/**验证验证码是否正确
	 * 
	 * @param codeKey redis中存的验证码的key
	 * @param code  用户输入的验证码
	 * @return
	 */
	public boolean check(String codeKey, String code){
		if(code == null){
			return false;
		}
		String values = redisDB.get(codeKey);
		return code.trim().equals(values);
	}
}
