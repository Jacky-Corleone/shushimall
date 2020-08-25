package com.camelot.mall.information;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.example.controller.WeChatMsgProcess;
import com.camelot.example.controller.WeChatProcess;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserWxDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.BusinessScale;
import com.camelot.usercenter.enums.UserEnums.CompanyPeopleNum;
import com.camelot.usercenter.enums.UserEnums.CompanyQualt;
import com.camelot.usercenter.enums.UserEnums.DepartMent;
import com.camelot.usercenter.enums.UserEnums.UsePurchaseType;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserWxExportService;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.UserDtoUtils;
import com.camelot.util.WebUtil;
import com.camelot.util.WeiXinMessageModeId;

@Controller
@RequestMapping("/information/register")
public class RegisterController {

	private static final String VALUE_REGISTER_DUPLICATE_REDIS = "0";

	@Resource
	private UserExportService userExportService;
	@Resource
	private UserWxExportService userWxExportService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private LogoExportService logoService;
	@Resource
    private RedisDB redisDB;

	/**
	 * 注册-加载/买家认证-加载
	 */
	@RequestMapping("/initRegister")
	public String initRegister(String type, HttpServletRequest request, Model model) {
		//防止重复个人注册
		String keyPersonalRegisterDuplicateRedis = WebUtil.getInstance().getRegisterToken();
		redisDB.addObject(keyPersonalRegisterDuplicateRedis, VALUE_REGISTER_DUPLICATE_REDIS, 1800);
		model.addAttribute("keyPersonalRegisterDuplicateRedis", keyPersonalRegisterDuplicateRedis);
		//防止重复企业注册
		String keyEnterpriseRegisterDuplicateRedis = WebUtil.getInstance().getRegisterToken();
		redisDB.addObject(keyEnterpriseRegisterDuplicateRedis, VALUE_REGISTER_DUPLICATE_REDIS, 1800);
		model.addAttribute("keyEnterpriseRegisterDuplicateRedis", keyEnterpriseRegisterDuplicateRedis);
		//获取省份、所在部门、经营范围、经营规模、企业人数、公司性质
		model.addAttribute("addressList", addressBaseService.queryAddressBase("0"));
		model.addAttribute("departMents", DepartMent.values());
		model.addAttribute("usePurchaseTypes", UsePurchaseType.values());
		model.addAttribute("businessScales", BusinessScale.values());
		model.addAttribute("companyPeopleNums", CompanyPeopleNum.values());
		model.addAttribute("companyQualts", CompanyQualt.values());
		//返回地址
		String url = "/information/registerPersonal";
		//买家认证:用户名/验证手机/验证邮箱
		if("buyer".equals(type)){
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO userDTO = userExportService.queryUserById(userId);
			//判断是否已经买家认证
			Integer uStatus = userDTO.getUserstatus();
			if( uStatus > 2 ){
				return "redirect:/information/informationBuyer/initLoad";
			}
			model.addAttribute("userId", userId);
			model.addAttribute("userDTO", userDTO);
			url = "/information/registerEnterprise";
		}
		model.addAttribute("type", type);
		return url;
	}

	/**
	 * 注册-认证：个人注册
	 */
	@ResponseBody
	@RequestMapping("/submitPersonal")
	public String submitPersonal(RegisterInfoDTO registerInfoDTO,
			HttpServletRequest request, HttpServletResponse response, String keyPersonalRegisterDuplicateRedis) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject map = new JSONObject();
		//防止个人重复注册
		Object pesonalRegisterDuplicateObj = redisDB.getObject(keyPersonalRegisterDuplicateRedis);
		if(pesonalRegisterDuplicateObj == null){
			map.put("message", "不可重复提交!");
			request.setAttribute("map", map);
			return map.toJSONString();
		}

		String loginname = registerInfoDTO.getLoginname();
		String loginpwd = MD5.encipher(registerInfoDTO.getLoginpwd());
		//设置用户类型
		registerInfoDTO.setUserType(1);
		//密码加密
		registerInfoDTO.setLoginpwd(loginpwd);
		//是否是快速注册
		registerInfoDTO.setQuickType(1);
		//是否是快速注册
		Long userId = userExportService.registerUser(registerInfoDTO);
		//修改用户状态
		UserDTO userDTO = userExportService.queryUserById(userId);
		userDTO.setUserstatus(Integer.valueOf(2));
		//添加到作用域
		request.setAttribute("userDTO", userDTO);
		userExportService.modifyUserInfo(userDTO);
		//注册完成后初始化登陆
		this.initLogin(loginname, loginpwd, request, response);
		redisDB.del(keyPersonalRegisterDuplicateRedis);
		map.put("userId", userId);
		map.put("message", "操作成功!");
		request.setAttribute("map", map);
		//发送信息
		UserWxDTO userWxDTO=new UserWxDTO();
		userWxDTO.setUname(loginname);
		ExecuteResult<UserWxDTO> executeResult= userWxExportService.getUserInfoByOpenId(userWxDTO);
		if(executeResult.getResult()!=null) {
			UserWxDTO userWx=executeResult.getResult();
			if (userWx.getWxopenid() != null) {
				WeChatProcess weChatProcess = new WeChatMsgProcess();
				Map simap = new HashMap();
				simap.put("openId", userWx.getWxopenid());
				simap.put("modeId", WeiXinMessageModeId.REGISTERED_SUCCESS);
				simap.put("first", "注册成功！");
				simap.put("keyword1", loginname);
				simap.put("keyword2",  df.format(new Date()));
				simap.put("remark", "【印刷家】尊敬的用户，欢迎您加入印刷家！");
				weChatProcess.SendInformation(simap, request, response);
			}
		}
		return map.toJSONString();
	}

	/**
	 * 注册-认证：企业注册、买家认证 微信端
	 */
	@ResponseBody
	@RequestMapping("/submitEnterpriseWx")
	public String submitEnterpriseWx(String type, RegisterInfoDTO registerInfoDTO, UserInfoDTO userInfoDTO,
			String departMentVal,String companyPeoNum,String businessScale,String companyQualt,
			HttpServletRequest request, HttpServletResponse response, String keyEnterpriseRegisterDuplicateRedis) {
		//调用web端买家认证方法
		Map<String,Object> map = submitEnterprise(type, registerInfoDTO, userInfoDTO, departMentVal, companyPeoNum, 
							     businessScale, companyQualt, request, response, keyEnterpriseRegisterDuplicateRedis);
		ExecuteResult<String> result = new ExecuteResult<String>();
		List<String> errorMess = new ArrayList<String>();
		String message = map.get("message").toString();
		errorMess.add(message);
		result.setErrorMessages(errorMess);
		return JSON.toJSONString(result);
	}
	
	/**
	 * 注册-认证：企业注册、买家认证
	 */
	@ResponseBody
	@RequestMapping("/submitEnterprise")
	public Map<String,Object> submitEnterprise(String type, RegisterInfoDTO registerInfoDTO, UserInfoDTO userInfoDTO,
			String departMentVal,String companyPeoNum,String businessScale,String companyQualt,
			HttpServletRequest request, HttpServletResponse response, String keyEnterpriseRegisterDuplicateRedis) {
		Map<String,Object> map = new HashMap<String,Object>();

		//防止企业重复注册
		Object enterpriseRegisterDuplicateObj = redisDB.getObject(keyEnterpriseRegisterDuplicateRedis);
		if(enterpriseRegisterDuplicateObj == null){
			map.put("message", "不可重复提交!");
			request.setAttribute("map", map);
			return map;
		}

		//获取用户ID
		Long userId = 0L;
		if("buyer".equals(type)){
			userId = userInfoDTO.getUserId();
			UserDTO userDTO = userExportService.queryUserById(userId);
			//用户类型
			userDTO.setUsertype(1);
			//用户状态
			userDTO.setUserstatus(3);
			//联系人姓名
			userDTO.setLinkMan(registerInfoDTO.getLinkMan());
			//所在部门
			userDTO.setDepartment(DepartMent.valueOf(departMentVal));
			//固定电话
			userDTO.setLinkPhoneNum(registerInfoDTO.getLinkPhoneNum());
			//验证手机
			userDTO.setUmobile(registerInfoDTO.getUserMobile());
			//联系人邮箱
			userDTO.setUserEmail(registerInfoDTO.getUserEmail());
			//修改用户
			userExportService.modifyUserInfo(userDTO);
		} else {
			String loginname = registerInfoDTO.getLoginname();
			String loginpwd = MD5.encipher(registerInfoDTO.getLoginpwd());
			//用户类型
			registerInfoDTO.setUserType(2);
			//登陆密码加密
			registerInfoDTO.setLoginpwd(loginpwd);
			//是否是快速注册
			registerInfoDTO.setQuickType(1);
			registerInfoDTO.setDepartment(DepartMent.valueOf(departMentVal));
			//添加用户
			userId = userExportService.registerUser(registerInfoDTO);
			//修改用户状态
			UserDTO userDTO = userExportService.queryUserById(userId);
			userDTO.setUserstatus(Integer.valueOf(3));
			userExportService.modifyUserInfo(userDTO);
			//注册完成后初始化登陆
			this.initLogin(loginname, loginpwd, request, response);
		}
		//用户-扩展信息
		String result = this.saveEnterprise(type, userId, userInfoDTO, businessScale, companyPeoNum, companyQualt);
		redisDB.del(keyEnterpriseRegisterDuplicateRedis);
		map.put("userId", userId);
		map.put("message", result);
		request.setAttribute("map", map);
		return map;
	}

	/**
	 * 查询-地址：省份-城市-城区
	 */
	@ResponseBody
	@RequestMapping("/queryAddress")
	public List<AddressBase> queryAddress(String parentCode,Model model) {
		List<AddressBase> addressList = addressBaseService.queryAddressBase(parentCode);
		return addressList;
	}

	/**
	 * <p>Discription:个人注册，企业注册成功跳转页面</p>
	 * Created on 2015年5月27日
	 * @return
	 * @author:董其超
	 */
	@RequestMapping("/registerSucceed")
	public String registerSucceed(Model model, HttpServletRequest request) {
		
		//model.addAttribute("userDTO",request.getAttribute("userDTO"));
		//model.addAttribute("userType", CookieHelper.getCookie(request, Constants.));
		return "/buyers/registerSucceed";
	}

	/*
	 * 用户-扩展信息：用户ID、买家/卖家、状态信息
	 */
	private String saveEnterprise(String type, Long userId, UserInfoDTO userInfoDTO,
			 String businessScale, String companyPeoNum,String companyQualt){
		//用户ID
		userInfoDTO.setUserId(userId);
		//用户类型：买家用
		userInfoDTO.setUserType(UserType.BUYER);
		//营业执照信息：状态、经营规模、公司人数、公司性质
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();
		userBusinessDTO.setBusinessStatus(1);
		if(!StringUtils.isBlank(businessScale)){
			userBusinessDTO.setBusinessScale(BusinessScale.valueOf(businessScale));
		}
		if(!StringUtils.isBlank(companyPeoNum)){
			userBusinessDTO.setCompanyPeoNum(CompanyPeopleNum.valueOf(companyPeoNum));
		}
		if(!StringUtils.isBlank(companyQualt)){
			userBusinessDTO.setCompanyQualt(CompanyQualt.valueOf(companyQualt));
		}
		//保存:用户扩展信息
		ExecuteResult<UserInfoDTO> result = userExtendsService.createUserExtends(userInfoDTO);
		//返回结果
        String result_str = null;
        //个人买家认证
        if("buyer".equals(type)){
            result_str = "操作成功!";
        //企业买家注册及认证
        }else{
            result_str = "您的认证信息已提交，请耐心等待平台工作人员审核!";
        }
		if(!result.isSuccess()){
			result_str = "操作失败!";
		}
		return result_str;
	}

	/*
	 * 注册完成后初始化登陆
	 */
	private void initLogin(String loginname,String loginpwd, HttpServletRequest request, HttpServletResponse response){
		//生成用户登录token
		StringBuffer buffer = new StringBuffer();
		buffer.append(MD5.encipher(loginname));
		buffer.append("|");
		//buffer.append(request.getRemoteAddr());
		//buffer.append("|");
		buffer.append(SysProperties.getProperty("token.suffix"));
		String key = buffer.toString();
		//用户登陆
		ExecuteResult<LoginResDTO> er = userExportService.login(loginname, loginpwd, key);
		if(er.isSuccess()){
			LoginResDTO loginResDTO = er.getResult();
			CookieHelper.setCookie(response, Constants.AUTO_LOGON, "0");
			CookieHelper.setCookie(response, "logging_status", MD5.encipher(loginname));
			CookieHelper.setCookie(response, Constants.USER_NAME, loginResDTO.getNickname());
			CookieHelper.setCookie(response, Constants.LOG_NAME, loginResDTO.getNickname());
			CookieHelper.setCookie(response, Constants.USER_ID, loginResDTO.getUid().toString());
		}
	}

	/**
	 * <p>Discription:校验用户名是否重复</p>
	 * Created on 2015年3月12日
	 * @param loginname
	 * @author:胡恒心
	 */
	@ResponseBody
	@RequestMapping("/verifyLoginName")
	public String verifyLoginName(String loginname) {
		String mag="true";
		boolean aa=userExportService.verifyRegisterName(loginname);
		if(aa){
			mag="true";
		}else{
			mag="false";
		}
		return mag;
	}

	/**
	 * <p>Discription:校验用户名并获取手机号</p>
	 * Created on 2015年7月13日
	 * @param loginname
	 * @author:鲁鹏
	 */
	@ResponseBody
	@RequestMapping("/getUserInfoByLoginname")
	public String getUserInfoByLoginname(String loginname) {
		UserDTO userDTO = findUserByUserInfo(loginname);
		return JSONObject.toJSONString(userDTO);
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
		DataGrid<UserDTO> userDTODataGrid_Uname = userExportService.findUserListByCondition(userDTO_Uname, null, null);
		List<UserDTO> userDTOList_Uname = userDTODataGrid_Uname.getRows();
		if(userDTOList_Uname != null && userDTOList_Uname.size() > 0){
			retUserDTO = userDTOList_Uname.get(0);
		}

		//通过邮箱找到用户
		UserDTO userDTO_UEmail = new UserDTO();
		userDTO_UEmail.setUserEmail(loginInfo);
		DataGrid<UserDTO> userDTODataGrid_UserEmail = userExportService.findUserListByCondition(userDTO_UEmail, null, null);
		List<UserDTO>userDTOList_UEmail = userDTODataGrid_UserEmail.getRows();
		if(userDTOList_UEmail != null && userDTOList_UEmail.size() > 0){
			retUserDTO = userDTOList_UEmail.get(0);
		}

		//通过手机找到用户
		UserDTO userDTO_UMobile = new UserDTO();
		userDTO_UMobile.setUmobile(loginInfo);
		DataGrid<UserDTO> userDTODataGrid_UMobile = userExportService.findUserListByCondition(userDTO_UMobile, null, null);
		List<UserDTO> userDTOList_UMobile = userDTODataGrid_UMobile.getRows();
		if(userDTOList_UMobile != null && userDTOList_UMobile.size() > 0){
			retUserDTO = userDTOList_UMobile.get(0);
		}

		//隐藏用户的邮箱和手机号
		//retUserDTO.setUserEmail(UserDtoUtils.hideUserEmail(retUserDTO.getUserEmail()));
		if(retUserDTO!=null) {
			retUserDTO.setLinkPhoneNum(retUserDTO.getUmobile());
			retUserDTO.setUmobile(UserDtoUtils.hideUserCellPhone(retUserDTO.getUmobile()));
		}

		return retUserDTO;
	}
	
	 /**
     * <p>Discription:微信企业注册校验邮箱是否重复</p>
     * Created on 2015年10月10日
     * @param userEmail
     * @author:李伟龙
     */
    @RequestMapping(value="/verifyEnterpriseEmailWx")
	@ResponseBody
    public String verifyEnterpriseEmailWx(String userEmail) {
    	ExecuteResult<String> result = new ExecuteResult<String>();
		boolean aa=userExportService.verifyEmail(userEmail);
		if(!aa){
			result.setResult("1");
		}else{
			result.setResult("邮箱已存在，请重新输入");
		}
		return JSON.toJSONString(result);
    }
    /**
	 * 修改：买家基本信息
	 */
	@ResponseBody
	@RequestMapping("modifyBuyerWx")
	public String modifyBuyer(UserInfoDTO userInfoDTO, String companyQualt, String companyPeoNum,
			String businessScale, HttpServletRequest request){
		//更改审批记录状态
		UserDTO userDTO = userExportService.queryUserById(userInfoDTO.getUserId());
		Integer userstatus = userDTO.getUserstatus();
		if(userstatus==3 || userDTO.getUserstatus()==5){
			userDTO.setAuditStatus(1);
			userExportService.modifyUserInfoAndAuditStatus(userDTO);
		}
		//修改-公司信息;公司性质,企业人数,经营规模
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();
		if(!StringUtils.isBlank(companyQualt)){
			userBusinessDTO.setCompanyQualt(CompanyQualt.valueOf(companyQualt));
		}
		if(!StringUtils.isBlank(companyPeoNum)){
			userBusinessDTO.setCompanyPeoNum(CompanyPeopleNum.valueOf(companyPeoNum));
		}
		if(!StringUtils.isBlank(businessScale)){
			userBusinessDTO.setBusinessScale(BusinessScale.valueOf(businessScale));
		}
        //驳回状态时由前台负责修改为待审核状态，已审核状态时由后台负责修改为待审核状态
        if(userDTO.getUserstatus().intValue() != 4 && userDTO.getUserstatus().intValue() != 6){
            userExtendsService.modifyStatusByType(userInfoDTO.getExtendId(), ComStatus.AUTH, UserExtendsType.BUSINESS);
        }
        ExecuteResult<UserInfoDTO> result = userExtendsService.modifyUserExtends(userInfoDTO, UserExtendsType.BUSINESS);
        ExecuteResult<String> resultJson = new ExecuteResult<String>();
        if(result.isSuccess()){
        	resultJson.setResult(result.getResultMessage());
        }else{
        	resultJson.setResult("修改失败");
        }
		return JSON.toJSONString(resultJson);
	}
}
