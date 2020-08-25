package com.camelot.mall.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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

import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.mall.sellcenter.UserDtoUtils;
import com.camelot.mall.util.CaptchaResult;
import com.camelot.mall.util.CaptchaUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.MD5;

@Controller
@RequestMapping("/findpwd")
public class UserPwdFindController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserPwdFindController.class);

	@Resource
	private UserExportService userExportService;

    //activeMQ消息生产者接口
    @Resource
	private MessagePublisherService emailVerifyCodeQueuePublisher;

    //activeMQ消息生产者接口
	@Resource
	private MessagePublisherService smsVerifyCodeQueuePublisher;

    @Resource
    private CaptchaUtil captchaUtil;

	@RequestMapping("editusername")
	public String editUsername(){
		return "/user/findPwd/findPwd-editUsername";
	}

    /**
     * 通过用户名/邮箱/手机验证用户
     * @param loginInfo 用户名/邮箱/手机
     * @param captcha 图片验证码
     * @return 用户信息
     */
	@RequestMapping("verifyuser")
	public String verifyUser(String loginInfo, String captcha, HttpServletRequest request, Model model){
        //对前台输入用户名/邮箱/手机进行非空校验，如果为空，返回第一步填写账户名页面
		if(StringUtils.isBlank(loginInfo)){
            return "forward:/findpwd/editusername";
        }
        //通过用户名/邮箱/手机找到用户
        UserDTO retUserDTO = findUserByUserInfo(loginInfo);
        //对通过用户名/邮箱/手机找到的用户进行非空校验，如果为空，返回第一步填写账户名页面
        if(null == retUserDTO){
            return "forward:/findpwd/editusername";
        }
        //从第二步验证身份页面回退过来的页面不跳转到第一步填写账户名页面
        if(StringUtils.isNotBlank(captcha)){
            //校验图片验证码，如果校验不通过，返回第一步填写账户名页面
            if(!validCaptcha(captcha, request.getSession().getId())){
                request.setAttribute("message", "验证码输入错误！");
                //重新请求第一步填写账户名页面
                request.setAttribute("siteUrl", request.getContextPath() + "/findpwd/editusername");
                return "/message";
            }
        }
        //SpringMVC模型绑定用户
        model.addAttribute("userinfo", retUserDTO);
        //进入第二步验证身份页面
		return "/user/findPwd/findPwd-verifyUser";
	}

    /**
     * 通过用户名/邮箱/手机找到用户
     * @param loginInfo 用户名/邮箱/手机
     * @return 用户信息
     */
    private UserDTO findUserByUserInfo(String loginInfo) {
        UserDTO retUserDTO = null;
        //通过用户名找到用户
        UserDTO userDTO_Uname = new UserDTO();
        userDTO_Uname.setUname(loginInfo);
        UserDTO userDTODataGrid_Uname = userExportService.findUserByUserNameOrEmailOrPhone(userDTO_Uname);
//        DataGrid<UserDTO> userDTODataGrid_Uname = userExportService.findUserListByCondition(userDTO_Uname, null, null);
//        List<UserDTO> userDTOList_Uname = userDTODataGrid_Uname.getRows();
        if(userDTODataGrid_Uname != null){
            retUserDTO = userDTODataGrid_Uname;
        }

        //通过邮箱找到用户
        UserDTO userDTO_UEmail = new UserDTO();
        userDTO_UEmail.setUserEmail(loginInfo);
        UserDTO userDTODataGrid_UserEmail = userExportService.findUserByUserNameOrEmailOrPhone(userDTO_UEmail);
        if(userDTODataGrid_UserEmail != null){
            retUserDTO = userDTODataGrid_UserEmail;
        }
//        DataGrid<UserDTO> userDTODataGrid_UserEmail = userExportService.findUserListByCondition(userDTO_UEmail, null, null);
//        List<UserDTO>userDTOList_UEmail = userDTODataGrid_UserEmail.getRows();
//        if(userDTOList_UEmail != null && userDTOList_UEmail.size() > 0){
//            retUserDTO = userDTOList_UEmail.get(0);
//        }

        //通过手机找到用户
        UserDTO userDTO_UMobile = new UserDTO();
        userDTO_UMobile.setUmobile(loginInfo);
        UserDTO userDTODataGrid_UMobile = userExportService.findUserByUserNameOrEmailOrPhone(userDTO_UMobile);
        if(userDTODataGrid_UMobile != null){
            retUserDTO = userDTODataGrid_UMobile;
        }
//        DataGrid<UserDTO> userDTODataGrid_UMobile = userExportService.findUserListByCondition(userDTO_UMobile, null, null);
//        List<UserDTO> userDTOList_UMobile = userDTODataGrid_UMobile.getRows();
//        if(userDTOList_UMobile != null && userDTOList_UMobile.size() > 0){
//            retUserDTO = userDTOList_UMobile.get(0);
//        }

        //隐藏用户的邮箱和手机号
        retUserDTO.setUserEmail(UserDtoUtils.hideUserEmail(retUserDTO.getUserEmail()));
        retUserDTO.setUmobile(UserDtoUtils.hideUserCellPhone(retUserDTO.getUmobile()));
        return retUserDTO;
    }

    /**
     * 校验验证码
     * @return
     */
    public boolean validCaptcha(String captcha, String uid){
        if(StringUtils.isBlank(captcha) || StringUtils.isBlank(uid)){
            return false;
        }
        String retCaptcha = userExportService.getValueByRedis(CaptchaUtil.KET_TOP + uid);
        return retCaptcha.equals(captcha);
    }

	@RequestMapping("editpwd")
	public String modifyPwd(String logname, String codeKey, String captcha, Model model, HttpServletRequest request){
        String verifyUser = "/findpwd/verifyuser?loginInfo="+logname;
        //对通过（第一步填写账户名页面输入的用户名/邮箱/手机）找到的用户的用户名进行非空校验，如果为空，返回第一步填写账户名页面
        if(StringUtils.isBlank(logname)){
            return "/user/findPwd/findPwd-editUsername";
        }
        //对第二步验证身份页面发送验证码得到的Redis中的KEY值进行非空校验，如果为空，通过Controller跳转返回到第二步验证身份页面
        if(StringUtils.isBlank(codeKey)){
            request.setAttribute("message", "系统未发送验证码，请点击发送验证码！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
        //对第二步验证身份页面输入的验证码进行非空校验，如果为空，通过Controller跳转返回到第二步验证身份页面
        if(StringUtils.isBlank(captcha)){
            request.setAttribute("message", "验证码不能为空！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
        //对通过（第二步验证身份页面发送验证码得到的Redis中的KEY值）得到Redis中的VALUE值进行非空校验，如果为空，通过Controller跳转返回到第二步验证身份页面
        String captchaValueRedis = userExportService.getValueByRedis(codeKey);
        if (StringUtils.isBlank(captchaValueRedis)) {
            request.setAttribute("message", "系统未发送验证码，请点击发送验证码！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
        //将Redis中的VALUE值与前台页面输入的验证码进行核对，如果核对，如果核对准确，进入第三步设置新密码页面，否则返回第二步验证身份页面
//      boolean aBoolean = captchaValueRedis.equals(captcha);
        boolean aBoolean = captchaUtil.check(codeKey,captcha);
        if(aBoolean){
            model.addAttribute("logname", logname);
            return "/user/findPwd/findPwd-editPwd";
        }else{
            request.setAttribute("message", "验证码输入错误！");
            request.setAttribute("siteUrl", verifyUser);
            return "/message";
        }
	}


	@RequestMapping("modifypwd")
	public String modifypwd(String logname, String loginpwd){
		loginpwd = MD5.encipher(loginpwd);
		userExportService.modifyUserPwdByUsername(logname, loginpwd);
		return "/user/findPwd/findPwd-success";
	}

	@RequestMapping("verifyloginfo")
	public void verifyLoginInfo(String loginInfo, HttpServletResponse response) {
		//验证用户名
		boolean nameBoolean = userExportService.verifyRegisterName(loginInfo);
		//验证邮箱、手机
		boolean emailOrMobileBoolean = userExportService.verifyEmailOrMobile(loginInfo);
		//验证登录信息（用户名、邮箱或手机）
		boolean loginInfoBoolean = nameBoolean || emailOrMobileBoolean;
		String result = "{\"message\": " +loginInfoBoolean +  "}";
		PrintWriter out = null;
		response.setContentType("application/json; charset=utf-8");
		try {
			out = response.getWriter();
			out.write(result);
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}

	@RequestMapping("/acquire")
	public void acquire(HttpServletRequest request, HttpServletResponse response) {
		CaptchaUtil captchaUtil = new CaptchaUtil();
		CaptchaResult captchaResult = captchaUtil.acquire(request,"");
		userExportService.saveVerifyCodeToRedis(captchaResult.getKey(), captchaResult.getValue());
		BufferedImage img = captchaResult.getBufferedImage();
		try {
			ImageIO.write(img, "JPEG", response.getOutputStream());
		} catch (IOException e) {
			LOGGER.info("异常信息：{}", e.getMessage());
		}
	}

	@RequestMapping("/verifyCaptcha")
	public void verifyCaptcha(String captcha, HttpServletRequest request, HttpServletResponse response) {
		String value = userExportService.getValueByRedis(CaptchaUtil.KET_TOP + request.getSession().getId());
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

	@RequestMapping("/sendCaptcha")
	public void sendCaptcha(String contact, int selected, HttpServletRequest request, HttpServletResponse response) {
		String result = null;
		String key = request.getSession().getId();
		String[] addrs = { contact };
		//2015-06-10王东晓添加
		VerifyCodeMessageDTO messageDTO = new VerifyCodeMessageDTO();
		messageDTO.setEnumType("UPLOGINPWD");
		messageDTO.setKey(key);
		messageDTO.setAddress(addrs);
		messageDTO.setType(selected+"");
		//根据不同的验证码类型，放入不同的消息队列
		boolean isSuccess = false;
		if(selected==2){//短信
			isSuccess = smsVerifyCodeQueuePublisher.sendMessage(messageDTO);
		}else if(selected==1){//邮件
			isSuccess = emailVerifyCodeQueuePublisher.sendMessage(messageDTO);
		}
		//王东晓添加end
		result = "{\"message\": true}";
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