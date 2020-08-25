package com.camelot.mall.sellcenter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.common.enums.WithdrawEnums;
import com.camelot.mall.util.CaptchaUtil;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.util.TempFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.CookieHelper;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.util.Signature;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.FinanceWithdrawApplyExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.FinanceWithdrawApplyDTO;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserAccountDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/security")
public class SecurityController {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(SecurityController.class);
	private static String PRIKEY = SysProperties.getProperty("transfer.prikey");
    private static String MALL= SysProperties.getProperty("transfer.system");
	@Resource
	private UserExportService userExportService = null;
	@Resource
	private UserExtendsService userExtendsService = null;
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService = null;
	@Resource
	private FinanceWithdrawApplyExportService financeWithdrawApplyExportService = null;
	@Resource
	private CiticExportService citicExportService = null;
	@Resource
	private MessagePublisherService emailVerifyCodeQueuePublisher;
	@Resource
	private MessagePublisherService smsVerifyCodeQueuePublisher;
    @Resource
    private CaptchaUtil captchaUtil;
	/**
	 * <p>Discription:安全信息设置页面</p>
	 * Created on 2015年3月6日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("securitypage")
	public String securitypage(Model model, HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		UserDTO userDTO = userExportService.queryUserById(registerDTO.getUid());
		userDTO.setUserEmail(UserDtoUtils.hideUserEmail(userDTO.getUserEmail()));
		userDTO.setUmobile(UserDtoUtils.hideUserCellPhone(userDTO.getUmobile()));
		model.addAttribute("userinfo", userDTO);
		return "/security/security-settings";
	}
	
	/**
	 * <p>Discription:修改用户登录密码</p>
	 * Created on 2015年3月7日
	 * @param logname
	 * @param loginpwd
	 * @return
	 * @author:[创建者中文名字]
	 */
	@RequestMapping("modifypwd")
	public String modifypwd(Model model, String logname, String oldpwd, String loginpwd, String backUrl, String userType, HttpServletRequest request){
		if(loginpwd != null && logname != null && oldpwd != null){
			loginpwd = MD5.encipher(loginpwd);
			oldpwd = MD5.encipher(oldpwd);
		} else {
			return "/security/modify-pwd";
		}
		ExecuteResult<String> result = userExportService.modifyUserPw(logname, loginpwd, oldpwd);
		if(!result.isSuccess()){
			return "redirect:/security/verifyuser?backUrl="+backUrl+"&msg=1&userType=" + userType;
		}
		model.addAttribute("msg", "修改登录密码");
		model.addAttribute("backUrl", backUrl == null ? "":backUrl);
		model.addAttribute("userType", userType);
		request.setAttribute("result",result);
		return "/security/modify-pwd-success";
	}
	
	/**
	 * <p>Discription:设置支付密码页面</p>
	 * Created on 2015年3月6日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("setpaypwdpage")
	public String setpaypwdpage(String backUrl,String userType,Model model, HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		registerDTO.setUserEmail(UserDtoUtils.hideUserEmail(registerDTO.getUserEmail()));
		registerDTO.setUserMobile(UserDtoUtils.hideUserCellPhone(registerDTO.getUserMobile()));
		model.addAttribute("userinfo", registerDTO);
		model.addAttribute("backUrl", backUrl);
		model.addAttribute("userType",userType);
		return "/security/setPayPwd";
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
			return "/security/modify-pwd-success";
		}
		return "redirect:/security/setpaypwdpage";
	}
	
	/**
	 * <p>Discription:修改支付密码页面</p>
	 * Created on 2015年3月6日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("modifypaypwdpage")
	public String modifypaypwdpage(String backUrl,String userType,Model model, Integer msg, HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		registerDTO.setUserEmail(UserDtoUtils.hideUserEmail(registerDTO.getUserEmail()));
		registerDTO.setUserMobile(UserDtoUtils.hideUserCellPhone(registerDTO.getUserMobile()));
		model.addAttribute("userinfo", registerDTO);
		if(msg != null){
			model.addAttribute("msg", msg);
		}
		model.addAttribute("backUrl", backUrl);
		model.addAttribute("userType",userType);
		return "/security/modify-pay-pwd";
	}
	
	/**
	 * <p>Discription:修改支付密码</p>
	 * Created on 2015年3月6日
	 * @return
	 * @author:胡恒心
	 */
    @ResponseBody
	@RequestMapping("modifypaypwd")
	public Map<String,Object> modifypaypwd(Model model, String paypwd, String oldpwd, String level, String codeKey, String captcha, String backUrl, String userType, HttpServletRequest request){
        String token = LoginToken.getLoginToken(request);
        RegisterDTO registerDTO = userExportService.getUserByRedis(token);
        Long uid = registerDTO.getUid();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("message", "支付密码修改成功！");
        //对Redis中的KEY值进行非空校验
        if(StringUtils.isBlank(codeKey)){
            map.put("message", "系统未发送验证码，请点击发送验证码！");
            map.put("ifSuccessUrl", false);
            return map;
        }
        //对页面输入的验证码进行非空校验
        if(StringUtils.isBlank(captcha)){
            map.put("message", "验证码不能为空！");
            map.put("ifSuccessUrl", false);
            return map;
        }
        //对通过（发送验证码得到的Redis中的KEY值）得到Redis中的VALUE值进行非空校验
        String captchaValueRedis = userExportService.getValueByRedis(codeKey);
        if (StringUtils.isBlank(captchaValueRedis)) {
            map.put("message", "系统未发送验证码，请点击发送验证码！");
            map.put("ifSuccessUrl", false);
            return map;
        }
        //将Redis中的VALUE值与前台页面输入的验证码进行核对
//      boolean aBoolean = captchaValueRedis.equals(captcha);
        boolean aBoolean = captchaUtil.check(codeKey,captcha);
        if(!aBoolean){
            map.put("message", "验证码错误，请您重新输入！");
            map.put("ifSuccessUrl", false);
            return map;
        }
        if(uid != null && paypwd != null && oldpwd != null){
			paypwd = MD5.encipher(paypwd);
			oldpwd = MD5.encipher(oldpwd);
			ExecuteResult<String> result = userExportService.modifyPaypwdById(uid, paypwd, oldpwd, level);
			if(!result.isSuccess()){
                map.put("ifSuccessUrl", true);
                map.put("retUrl", request.getContextPath() + "/security/modifypaypwdpage?msg=1&userType=" + userType);
                return map;
			}
			model.addAttribute("msg", "修改支付密码");
			model.addAttribute("backUrl",backUrl);
			model.addAttribute("userType",userType);
			request.setAttribute("result",result);
            map.put("ifSuccessUrl", true);
            map.put("retUrl", request.getContextPath() + "/security/success?backUrl="+backUrl+"&userType="+userType + "&msg=2");
            return map;
        }
        map.put("ifSuccessUrl", true);
        map.put("retUrl", request.getContextPath() + "/security/securitypage");
        return map;
	}
	
	/**
	 * <p>Discription:找回支付密码页面</p>
	 * Created on 2015年3月6日
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("findpaypwdpage")
	public String findpaypwdpage(String backUrl,String userType,Model model, HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		registerDTO.setUserEmail(UserDtoUtils.hideUserEmail(registerDTO.getUserEmail()));
		registerDTO.setUserMobile(UserDtoUtils.hideUserCellPhone(registerDTO.getUserMobile()));
		model.addAttribute("userinfo", registerDTO);
		model.addAttribute("backUrl", backUrl);
		model.addAttribute("userType",userType);
		return "/security/find-paypwd-verify";
	}
	
	/**
	 * <p>Discription:编辑支付密码页面</p>
	 * Created on 2015年3月7日
	 * @param model
	 * @param uid
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("editpaypwd")
	public String editPaypwd(Model model, String uid, String backUrl, String userType, String codeKey, String captcha, HttpServletRequest request){
        String verifyUser = "/security/findpaypwdpage?backUrl="+backUrl+"&userType="+userType;
		model.addAttribute("uid", uid);
		model.addAttribute("backUrl",backUrl);
		model.addAttribute("userType",userType);
        //Redis中的KEY值进行非空校验
        if(StringUtils.isBlank(codeKey)){
            request.setAttribute("message", "系统未发送验证码，请点击发送验证码！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
        //页面输入的验证码进行非空校验
        if(StringUtils.isBlank(captcha)){
            request.setAttribute("message", "验证码不能为空！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
        //得到Redis中的VALUE值进行非空校验
        String captchaValueRedis = userExportService.getValueByRedis(codeKey);
        if (StringUtils.isBlank(captchaValueRedis)) {
            request.setAttribute("message", "系统未发送验证码，请点击发送验证码！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
        //将Redis中的VALUE值与前台页面输入的验证码进行核对，如果核对，如果核对准确，进入第三步设置新密码页面，否则返回第二步验证身份页面
        //boolean aBoolean = captchaValueRedis.equals(captcha);
        boolean aBoolean = captchaUtil.check(codeKey,captcha);
        if(aBoolean){
            return "/security/find-paypwd-editpwd";
        }else{
            request.setAttribute("message", "验证码输入错误！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
	}
	
	/**
	 * <p>Discription:修改支付密码</p>
	 * Created on 2015年3月7日
	 * @param model
	 * @param uid
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("findpaypwd")
	public String findpaypwd(String backUrl,String userType,Model model, Integer uid, String paypwd, String level, HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		if(uid != null && paypwd != null){
			paypwd = MD5.encipher(paypwd);
		} else {
			return "/security/find-paypwd-editpwd";
		}
		ExecuteResult<String> result = userExportService.modifyPaypwdById(uid, paypwd,null,level);
		userExportService.updateUserInfoToRedis(token, registerDTO);
		model.addAttribute("msg", "找回支付密码");
		backUrl = backUrl == null ? "" : backUrl;
		return "redirect:/security/success?msg=1&backUrl="+backUrl+"&userType="+userType;
	}
	
	/**
	 * <p>Discription:支付账户管理页面</p>
	 * Created on 2015年3月6日
	 * @param model
	 * @param request
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("payaccount")
	public String payAccount(Model model, Integer page, HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		//没有认证通过的用户，不支持此功能
  		if(registerDTO.getUserType() == 1){
  			return "/no_authentication";
  		}
		ExecuteResult<UserInfoDTO> executeResult = userExtendsService.findUserInfo(registerDTO.getUid());
		//--------账户卡号状态
		CommonEnums.ComStatus[] comStatuses= CommonEnums.ComStatus.values();
        Map<Integer,String> zxstatus=new HashMap<Integer, String>();
        for(CommonEnums.ComStatus cc:comStatuses){
            zxstatus.put(new Integer(cc.getCode()),cc.getLabel());
        }
		//--------
		UserInfoDTO userInfoDTO = null;
		String usableMoney = null;
		if(executeResult.isSuccess()){
			userInfoDTO = executeResult.getResult();
			if(userInfoDTO != null){
				UserCiticDTO userCiticDTO=userInfoDTO.getUserCiticDTO();
				if(userCiticDTO != null){
					//---------------- 2015.6.9增加卡号及状态 start-----------------------------------
					//买家支付账户
                    model.addAttribute("buyerPaysAccount", userCiticDTO.getBuyerPaysAccount());
                    //卖家中信账户（冻结账号，收款账号）状态
                    model.addAttribute("state", zxstatus.get(userCiticDTO.getAccountState()));
                    //--------------------- 2015.6.9增加卡号及状态 end--------------------------------
                    
					Map<String,String> withmap=new HashMap<String, String>();
                    withmap.put("system",MALL);
                    withmap.put("subAccNo",userCiticDTO.getBuyerPaysAccount());
                    withmap.put("sign", Signature.createSign(withmap,PRIKEY));
					String sellerWithdrawsCashAccount = userCiticDTO.getBuyerPaysAccount();
					if(sellerWithdrawsCashAccount != null){
						ExecuteResult<AffiliatedBalanceDto> executeResult2 = citicExportService.querySubBalance(withmap);
						if(executeResult2.isSuccess()){
							AffiliatedBalanceDto affiliatedBalanceDto = executeResult2.getResult();
							List<AffiliatedBalance> affiliatedBalances = affiliatedBalanceDto.getList();
							if(affiliatedBalances != null && affiliatedBalances.size() > 0){
								usableMoney = affiliatedBalances.get(0).getKYAMT();
								//2015-06-10王东晓添加
								//根据中信帐号名判断，当前用户是印刷家_买家还是买家
								String zxName = affiliatedBalances.get(0).getSUBACCNM();
								//如果中信帐号名是以印刷家_开头，则是印刷家买家
								if(Constants.YSJ_YES.equals(zxName)){
									model.addAttribute("isysj", true);
								}else{
									model.addAttribute("isysj", false);
								}
								//2015-06-10王东晓添加结束
							}else{
								LOGGER.error("查询余额发生异常，返回的状态信息："+affiliatedBalanceDto.getStatusText());
							}
						}else{
							LOGGER.error("查询余额发生异常-----原因："+executeResult2.getErrorMessages());
						}
					}
				}
			}
		}
		FinanceWithdrawApplyDTO dto = new FinanceWithdrawApplyDTO();
		dto.setUserId(registerDTO.getUid());
		
		Pager<FinanceWithdrawApplyDTO> pager = new Pager<FinanceWithdrawApplyDTO>();
		if(page != null){
			pager.setPage(page);
		}
		DataGrid<FinanceWithdrawApplyDTO> dataGrid = financeWithdrawApplyExportService.queryFinanceWithdrawByCondition(dto, pager);
		model.addAttribute("usableMoney", usableMoney);
        if(dataGrid.getRows()!=null&&dataGrid.getRows().size()>0){
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
            List<FinanceWithdrawApplyDTO> listw=dataGrid.getRows();
            Iterator<FinanceWithdrawApplyDTO> iterator=listw.iterator();
            while(iterator.hasNext()){
                Map<String,String> map=new HashMap<String, String>();
                FinanceWithdrawApplyDTO financeWithdrawApplyDTO=iterator.next();
                if(financeWithdrawApplyDTO.getAmount()!=null){
                    map.put("amount",financeWithdrawApplyDTO.getAmount().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
                }
                if(financeWithdrawApplyDTO.getCreatedTime()!=null){
                    map.put("createdTime",simpleDateFormat.format(financeWithdrawApplyDTO.getCreatedTime()));
                }
                if(financeWithdrawApplyDTO.getStatus()!=null){
                    WithdrawEnums withdrawEnums=WithdrawEnums.getEnumByCode(financeWithdrawApplyDTO.getStatus());
                    if(withdrawEnums!=null){
                        map.put("status",withdrawEnums.getLabel());
                    }
                }
                listMap.add(map);
            }
            model.addAttribute("data",listMap);
        }
		model.addAttribute("page", pager);
		model.addAttribute("userInfoDTO", userInfoDTO);
		return "/security/pay-account-manage";
	}
	
	/**
	 * 
	 * <p>Discription:修改密码时发送验证码</p>
	 * Created on 2015年3月6日
	 * @param contact 发送地址
	 * @param selected 邮箱-1/手机-2
	 * @param oldpwd 旧密码
	 * @param request
	 * @param response
	 * @return
	 * @author:胡恒心
	 */
	@ResponseBody
	@RequestMapping("/sendCaptcha")
	public int sendCaptcha(String contact, int selected, String oldpwd, int which, HttpServletRequest request,
			HttpServletResponse response) {
//		String token = LoginToken.getLoginToken(request);
//		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
//		oldpwd = MD5.encipher(oldpwd);
//		String verifyOldPwd = "";
//		if(which == 1){  //which = 1: 修改登录密码
//			verifyOldPwd = registerDTO.getLoginpwd();
//		} else if (which == 2){  //which = 2: 修改支付密码
//			verifyOldPwd = registerDTO.getPayCode();
//		}
//		if(verifyOldPwd.equals(oldpwd)){
			String key = request.getSession().getId();
			String[] addrs = { contact };
			
			//2015-06-10王东晓添加
			
			VerifyCodeMessageDTO messageDTO = new VerifyCodeMessageDTO();
			messageDTO.setEnumType("UPLOGINPWD");
			messageDTO.setKey(key);
			messageDTO.setAddress(addrs);
			messageDTO.setType(selected+"");
			//根据不同的验证码类型，发入不同的消息队列
			boolean isSuccess = false;
			if(selected==2){//短信
				isSuccess = smsVerifyCodeQueuePublisher.sendMessage(messageDTO);
			}else if(selected==1){//邮件
				isSuccess = emailVerifyCodeQueuePublisher.sendMessage(messageDTO);
			}
			//王东晓添加end
			
			
//			ExecuteResult<String> result = baseWebSiteMessageService.sendVerificationCode("UPLOGINPWD" ,key, addrs, selected); //发送验证码
			if(isSuccess){
				return 2;
			} else {
				return 1;
			}
//		} else {
//			return 0;
//		}
	}
	
	/**
	 * 
	 * <p>Discription:创建密码时发送验证码</p>
	 * Created on 2015年3月6日
	 * @param contact 发送地址
	 * @param selected 邮箱-1/手机-2
	 * @param request
	 * @param response
	 * @return
	 * @author:胡恒心
	 */
	@ResponseBody
	@RequestMapping("/sendCaptcha2")
	public int sendCaptcha2(String contact, int selected, HttpServletRequest request,
			HttpServletResponse response) {
		String key = request.getSession().getId();
		String[] addrs = { contact };
		
		
		//2015-06-10王东晓添加
		
		VerifyCodeMessageDTO messageDTO = new VerifyCodeMessageDTO();
		messageDTO.setEnumType("SETPAYPWD");
		messageDTO.setKey(key);
		messageDTO.setAddress(addrs);
		messageDTO.setType(selected+"");
		//根据不同的验证码类型，发入不同的消息队列
		boolean isSuccess = false;
		if(selected==2){//短信
			isSuccess = smsVerifyCodeQueuePublisher.sendMessage(messageDTO);
		}else if(selected==1){//邮件
			isSuccess = emailVerifyCodeQueuePublisher.sendMessage(messageDTO);
		}
		//王东晓添加end
		
//		ExecuteResult<String> result = baseWebSiteMessageService.sendVerificationCode("SETPAYPWD", key, addrs, selected);
		if(isSuccess){
			return 2;
		}
		return 1;
	}

	/**
	 * 
	 * <p>Discription:校验用户是否存在</p>
	 * Created on 2015年3月6日
	 * @param model
	 * @param request
	 * @return
	 * @author:胡恒心
	 */
	@RequestMapping("verifyuser")
	public String verifyuser(Model model, HttpServletRequest request, String backUrl, Integer msg, String userType){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		registerDTO.setUserEmail(UserDtoUtils.hideUserEmail(registerDTO.getUserEmail()));
		registerDTO.setUserMobile(UserDtoUtils.hideUserCellPhone(registerDTO.getUserMobile()));
		model.addAttribute("userinfo", registerDTO);
		model.addAttribute("backUrl", backUrl);
		if(msg != null){
			model.addAttribute("msg", msg);
		}
		model.addAttribute("userType", userType);
		return "/security/modify-pwd";
	}
	
	/**
	 * <p>Discription:[修改密码成功后跳转成功页面]</p>
	 * Created on 2015年4月9日
	 * @param model
	 * @return
	 * @author:[胡恒心]
	 */
	@RequestMapping("success")
	public String success(String backUrl,String userType,String msg,Model model){
		model.addAttribute("msg", msg);
		model.addAttribute("backUrl", backUrl == null ? "":backUrl);
		model.addAttribute("userType",userType);
		return "/security/modify-pwd-success";
	}
	
	/**
	 * <p>Discription:[选择银行页面]</p>
	 * Created on 2015年4月9日
	 * @return
	 * @author:[胡恒心]
	 */
	@RequestMapping("selectBank")
	public String selectBank(HttpServletRequest request,Model model){
		
		
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if(registerDTO == null){
			return "redirect:/user/login";
		}
		ExecuteResult<UserInfoDTO> executeResult = userExtendsService.findUserInfo(registerDTO.getUid());
		//--------账户卡号状态
		CommonEnums.ComStatus[] comStatuses= CommonEnums.ComStatus.values();
        Map<Integer,String> zxstatus=new HashMap<Integer, String>();
        for(CommonEnums.ComStatus cc:comStatuses){
            zxstatus.put(new Integer(cc.getCode()),cc.getLabel());
        }
		//--------
		UserInfoDTO userInfoDTO = null;
		String usableMoney = null;
		if(executeResult.isSuccess()){
			userInfoDTO = executeResult.getResult();
			if(userInfoDTO != null){
				UserCiticDTO userCiticDTO=userInfoDTO.getUserCiticDTO();
				if(userCiticDTO != null){
					//---------------- 2015.6.9增加卡号及状态 start-----------------------------------
					//买家支付账户
                    model.addAttribute("buyerPaysAccount", userCiticDTO.getBuyerPaysAccount().trim());
                   
                    Map<String,String> withmap=new HashMap<String, String>();
                    withmap.put("system",MALL);
                    withmap.put("subAccNo",userCiticDTO.getBuyerPaysAccount());
                    withmap.put("sign", Signature.createSign(withmap,PRIKEY));
                    ExecuteResult<AffiliatedBalanceDto> affx= this.citicExportService.querySubBalance(withmap);
                    if (affx.isSuccess() && affx.getResult() != null) {
    					AffiliatedBalanceDto affiliatedBalanceDto = affx.getResult();
    					List<AffiliatedBalance> listaff = affiliatedBalanceDto.getList();
    					if (listaff != null && listaff.size() > 0) {
    						AffiliatedBalance affiliatedBalance = listaff.get(0);
    						//添加支付账户名称 
    						model.addAttribute("buyerPaysAccountName",affiliatedBalance.getSUBACCNM());
    					}
                    }
				}
			}
		}
		return "/security/select-bank";
	}
	
	/**
	 * <p>Discription:[异步获取用户账户信息]</p>
	 * Created on 2015年4月9日
	 * @return
	 * @author:[胡恒心]
	 */
	@ResponseBody
	@RequestMapping("account")
	public UserInfoDTO accountInfo(HttpServletRequest request){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		ExecuteResult<UserInfoDTO> result = userExtendsService.findUserInfo(registerDTO.getUid());
		if(result.isSuccess()) {
			return result.getResult();
		}
		return null;
	}
	
	/**
	 * <p>Discription:[提现方法]</p>
	 * Created on 2015年4月9日
	 * @return
	 * @author:[胡恒心]
	 */
	@ResponseBody
	@RequestMapping("withdraw")
	public int withdraw(HttpServletRequest request, BigDecimal withdrawPrice, String paypwd){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		paypwd = MD5.encipher(paypwd);
		ExecuteResult<String> result = userExportService.validatePayPassword(registerDTO.getUid(), paypwd);
		if("0".equals(result.getResult())){
			return 2;
		}
		AccountType accType=AccountType.AccBuyPay;
		String system=MALL;
		String priKey=PRIKEY;
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("system", system);
		map.put("uid", registerDTO.getUid().toString());  //用户Id
		map.put("accType", accType.name());
		map.put("withdrawPrice", withdrawPrice.toString());  //取现金额
		String sign=Signature.createSign(map, priKey);
		
		AccountInfoDto accountInfoDto =new AccountInfoDto();
		accountInfoDto.setUserId(registerDTO.getUid());
		accountInfoDto.setAccType(accType);
		accountInfoDto.setWithdrawPrice(withdrawPrice);
		accountInfoDto.setSystem(system);
		accountInfoDto.setSign(sign);
		ExecuteResult<String> executeResult = null;
		try {
			executeResult = citicExportService.outPlatformTransfer(accountInfoDto);
			request.setAttribute("withdrawResult",executeResult);
		} catch (Exception e) {
			LOGGER.info("操作结果{}", JSON.toJSON(executeResult));
		}
		if(executeResult.isSuccess()){
			return 1;
		}
		return 0;
	}
	
	@RequestMapping("/checkPayPwd")
	@ResponseBody
	public ExecuteResult<String> checkPayPwd(HttpServletRequest request,String pwd){
		ExecuteResult<String> er = new ExecuteResult<String>();
		String paypwd = MD5.encipher(pwd);
		Long uid = WebUtil.getInstance().getUserId(request);
		ExecuteResult<String> result = userExportService.validatePayPassword(uid, paypwd);
		if("0".equals(result.getResult())){
			//校验成功
			er.addErrorMessage("error");
		}
		return er;
	}
	
	/**
	 * <p>Description: [校验登录密码]</p>
	 * Created on 2015年8月7日
	 * @param request
	 * @param pwd
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/checkLoginPwd")
	@ResponseBody
	public ExecuteResult<String> checkLoginPwd(HttpServletRequest request,String pwd){
		ExecuteResult<String> er = new ExecuteResult<String>();
		String loginpwd = MD5.encipher(pwd);
		Long uid = WebUtil.getInstance().getUserId(request);
		ExecuteResult<String> result = userExportService.validateLoginPassword(uid, loginpwd);
		if("0".equals(result.getResult())){
			//校验成功
			er.addErrorMessage("error");
		}
		return er;
	}
	
}