package com.camelot.mall.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.openplatform.common.DataGrid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.CookieHelper;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.mall.Constants;
import com.camelot.mall.controller.BbsController;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.BusinessScale;
import com.camelot.usercenter.enums.UserEnums.CompanyPeopleNum;
import com.camelot.usercenter.enums.UserEnums.CompanyQualt;
import com.camelot.usercenter.enums.UserEnums.DepartMent;
import com.camelot.usercenter.enums.UserEnums.UsePurchaseType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/information/register")
public class RegisterController {

	private static final String VALUE_REGISTER_DUPLICATE_REDIS = "0";

	@Resource
	private UserExportService userExportService;
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
	public Map<String,Object> submitPersonal(RegisterInfoDTO registerInfoDTO,
			HttpServletRequest request, HttpServletResponse response, String keyPersonalRegisterDuplicateRedis) {
		Map<String,Object> map = new HashMap<String,Object>();

		//防止个人重复注册
		Object pesonalRegisterDuplicateObj = redisDB.getObject(keyPersonalRegisterDuplicateRedis);
		if(pesonalRegisterDuplicateObj == null){
			map.put("message", "不可重复提交!");
			map.put("success",false);
			request.setAttribute("map", map);
			return map;
		}
       if(StringUtils.isBlank(registerInfoDTO.getLoginname())||StringUtils.isBlank(registerInfoDTO.getLoginpwd())){
    	    map.put("message", "用户名或密码为空!");
    	    map.put("success",false);
			request.setAttribute("map", map);
			return map;
       }
		//根据电话号码查询用户信息，如果此电话号码之前已经注册过，则和用户解绑
		UserDTO user=new UserDTO();
		user.setUmobile(registerInfoDTO.getUserMobile());
		DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(user, null, null);
		List<UserDTO> list = dataGrid.getRows();
		if( list != null && list.size() > 0 ){
			for(int i=0;i<list.size();i++){
				UserDTO updateUserDTO = list.get(i);
				updateUserDTO.setUmobile("");
				userExportService.modifyUserInfoByMobile(updateUserDTO);
			}
		}

		String loginname = registerInfoDTO.getLoginname();
		String loginpwd = MD5.encipher(registerInfoDTO.getLoginpwd());
		//设置用户类型   买家角色
		registerInfoDTO.setUserType(2);
		//密码加密
		registerInfoDTO.setLoginpwd(loginpwd);
		//是否是快速注册
		registerInfoDTO.setQuickType(1);
		Long userId = userExportService.registerUser(registerInfoDTO);
		//修改用户状态
		UserDTO userDTO = userExportService.queryUserById(userId);
		//审核通过
		userDTO.setUserstatus(Integer.valueOf(4));
		userExportService.modifyUserInfo(userDTO);
		//注册完成后初始化登录
		this.initLogin(loginname, loginpwd, request, response);
		redisDB.del(keyPersonalRegisterDuplicateRedis);
		map.put("userId", userId);
		map.put("success",true);
		map.put("message", "操作成功!");
		request.setAttribute("map", map);
		
		//同步注册论坛
//        String bbs_url = SysProperties.getProperty("bbs_url");
//        String bbs_key = SysProperties.getProperty("bbs_key");//密钥
//        long logintime = (System.currentTimeMillis()/1000);//时间戳（秒）
//        String bbs_token = MD5.encipher(logintime+loginname+bbs_key);
//        BbsController.doGet("http://"+bbs_url+"/go.php?n="+loginname+"&t="+logintime+"&c="+bbs_token+"&p="+registerInfoDTO.getLoginpwd(),null,"UTF-8",true);
        
		return map;
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
			ExecuteResult<List<Long>> ids = userExportService.queryUserIds(Long.valueOf(userId));
			//用户类型	 liwl修改 提交认证type不变
//			userDTO.setUsertype(2);
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
			userDTO.setUidList(ids.getResult());
			//修改用户
			userExportService.modifyUserInfo(userDTO);
		} else {

			//根据电话号码查询用户信息，如果此电话号码之前已经注册过，则和用户解绑
			UserDTO user=new UserDTO();
			user.setUmobile(registerInfoDTO.getUserMobile());
			DataGrid<UserDTO> dataGrid = userExportService.findUserListByCondition(user, null, null);
			List<UserDTO> list = dataGrid.getRows();
			if( list != null && list.size() > 0 ){
				for(int i=0;i<list.size();i++){
					UserDTO updateUserDTO = list.get(i);
					updateUserDTO.setUmobile("");
					userExportService.modifyUserInfoByMobile(updateUserDTO);
				}
			}

			String loginname = registerInfoDTO.getLoginname();
			String loginpwd = MD5.encipher(registerInfoDTO.getLoginpwd());
			//用户类型
			registerInfoDTO.setUserType(1);
			//登录密码加密
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
			//注册完成后初始化登录
			this.initLogin(loginname, loginpwd, request, response);
		}
		//用户-扩展信息
		String result = this.saveEnterprise(type, userId, userInfoDTO, businessScale, companyPeoNum, companyQualt);
		redisDB.del(keyEnterpriseRegisterDuplicateRedis);
		map.put("userId", userId);
		map.put("message", result);
		request.setAttribute("map", map);
		
		//同步注册论坛
//        String bbs_url = SysProperties.getProperty("bbs_url");
//        String bbs_key = SysProperties.getProperty("bbs_key");//密钥
//        long logintime = (System.currentTimeMillis()/1000);//时间戳（秒）
//        String bbs_token = MD5.encipher(logintime+registerInfoDTO.getLoginname()+bbs_key);
//        BbsController.doGet("http://"+bbs_url+"/go.php?n="+registerInfoDTO.getLoginname()+"&t="+logintime+"&c="+bbs_token+"&p="+registerInfoDTO.getLoginpwd(),null,"UTF-8",true);
		
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
		model.addAttribute("loginname", request.getParameter("loginname"));
		model.addAttribute("userType", request.getParameter("userType"));
		return "/information/registerSucceed";
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
	 * 注册完成后初始化登录
	 */
	private void initLogin(String loginname,String loginpwd, HttpServletRequest request, HttpServletResponse response){
		//生成用户登录token
		StringBuffer buffer = new StringBuffer();
		buffer.append(MD5.encipher(loginname));
		buffer.append("|");
		buffer.append(request.getRemoteAddr());
		buffer.append("|");
		buffer.append(SysProperties.getProperty("token.suffix"));
		String key = buffer.toString();
		//用户登录
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
	 * <p>Discription:注册校验用户名是否重复</p>
	 * Created on 2015年3月12日
	 * @param loginname
	 * @author:胡恒心
	 */
	@ResponseBody
	@RequestMapping("/verifyRegisterName")
	public boolean verifyRegisterName(String loginname) {
		boolean aa=userExportService.verifyRegisterName(loginname);
		return !aa;
	}


	/**
	 * <p>Discription:个人注册校验邮箱是否重复</p>
	 * Created on 2015年7月8日
	 * @param personalMailInput_div
	 * @author:董其超
	 */
	@ResponseBody
	@RequestMapping("/verifyPersonalEmail")
	public boolean verifyPersonalEmail(String personalMailInput_div) {
		boolean aa=userExportService.verifyEmail(personalMailInput_div);
		return !aa;
	}

    /**
     * <p>Discription:个人注册校验手机是否重复</p>
     * Created on 2015年7月8日
     * @param personalPhoneInput
     * @author:董其超
     */
    @ResponseBody
    @RequestMapping("/verifyPersonalMobile")
    public boolean verifyPersonalMobile(String personalPhoneInput) {
        boolean aa=userExportService.verifyMobile(personalPhoneInput);
        return !aa;
    }

    /**
     * <p>Discription:企业注册校验邮箱是否重复</p>
     * Created on 2015年7月8日
     * @param userEmail
     * @author:董其超
     */
    @ResponseBody
    @RequestMapping("/verifyEnterpriseEmail")
    public boolean verifyEnterpriseEmail(String userEmail) {
        boolean aa=userExportService.verifyEmail(userEmail);
        return !aa;
    }

    /**
     * <p>Discription:企业注册校验手机是否重复</p>
     * Created on 2015年7月8日
     * @param userMobile
     * @author:董其超
     */
    @ResponseBody
    @RequestMapping("/verifyEnterpriseMobile")
    public boolean verifyEnterpriseMobile(String userMobile) {
        boolean aa=userExportService.verifyMobile(userMobile);
        return !aa;
    }

}
