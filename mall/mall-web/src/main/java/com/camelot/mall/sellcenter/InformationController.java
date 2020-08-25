package com.camelot.mall.sellcenter;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.CookieHelper;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.dto.VerifyCodeMessageDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.service.UserContractService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

/** 
 * <p>Description: [卖家基本信息维护]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/sellcenter/information")
public class InformationController {
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private UserExtendsService userExtendsService;
	
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	
	@Resource
	private AddressBaseService addressBaseService;
	
	@Resource
	private ShopExportService shopExportService;
	
	@Resource
	private UserContractService userContractService;
	
	@Resource
	private ShopCategoryExportService shopCategoryExportService;

	@Resource
	private SattleCatExpenseExportService sattleCatExpenseExportService;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private MessagePublisherService emailVerifyCodeQueuePublisher;
	@Resource
	private MessagePublisherService smsVerifyCodeQueuePublisher;
	/**
	 * 基础信息编辑
	 * @return
	 */
	@RequestMapping("")
	public String information(HttpServletRequest request,Model model){
		Long userId = this.getLoginUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		Long parentId = userDTO.getParentId();
		ExecuteResult<UserInfoDTO> result;
		//判断是否为子账号
		if(parentId!=null && parentId.longValue() > 0){
			model.addAttribute("parentId",parentId);
			result = userExtendsService.findUserInfo(parentId);
		} else {
			//判断是否已经提交认证
			Integer uStatus = userDTO.getUserstatus();
			if(uStatus!=5 && uStatus!=6 ){
				return "redirect:/controller/configuration/seller";
			}
			//判断是否已经认证通过
			ExecuteResult<ShopDTO> shopResult = shopExportService.findShopInfoById(userDTO.getShopId());
			ShopDTO shop = shopResult.getResult();
			if(shop==null || shop.getRunStatus()==null || shop.getStatus().longValue() < 4){
				return "redirect:/sellcenter/information/approveProgress";
			}
			result = userExtendsService.findUserInfo(userId);
		}
		UserInfoDTO userInfoDTO = result.getResult();
		if(userInfoDTO == null){
			return "error";
		}
		UserDTO dto = userInfoDTO.getUserDTO();
		//隐藏用户的手机号和邮箱
		dto.setUserEmail(UserDtoUtils.hideUserEmail(dto.getUserEmail()));
		dto.setUmobile(UserDtoUtils.hideUserCellPhone(dto.getUmobile()));
		model.addAttribute("userInfoDTO",userInfoDTO);
		//获取省市
		List<AddressBase> addressList = addressBaseService.queryAddressBase("0");
		model.addAttribute("addressList", addressList);
		//解析公司/银行所在地
		String bankAddressCode = userInfoDTO.getUserAccountDTO() == null ? "":userInfoDTO.getUserAccountDTO().getBankBranchIsLocated();
		String companyAddressCode = userInfoDTO.getUserBusinessDTO() == null ? "":userInfoDTO.getUserBusinessDTO().getCompanyAddress();
		if(StringUtils.isNotEmpty(bankAddressCode)){
			String[] bankAddressCodeArray = bankAddressCode.split(",");
			model.addAttribute("bankProvince",bankAddressCodeArray[0]);
			model.addAttribute("bankCity",bankAddressCodeArray.length > 1 ? bankAddressCodeArray[1]:"");
			model.addAttribute("bankArea",bankAddressCodeArray.length > 2 ? bankAddressCodeArray[2] : "");
		}
		if(StringUtils.isNotEmpty(companyAddressCode)){
			String[] companyAddressCodeArray = companyAddressCode.split(",");
			model.addAttribute("companyProvince",companyAddressCodeArray[0]);
			model.addAttribute("companyCity",companyAddressCodeArray.length > 1 ? companyAddressCodeArray[1]:"");
			model.addAttribute("companyArea",companyAddressCodeArray.length > 2 ? companyAddressCodeArray[2]:"");
		}
		model.addAttribute("companyAddress",this.getNameByCode(companyAddressCode));
		model.addAttribute("bankAddress",this.getNameByCode(bankAddressCode));
		//格式化营业执照有效期
		DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String businessLicenceIndate = "";
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();
		if(userBusinessDTO != null && userBusinessDTO.getBusinessLicenceIndate() != null){
			businessLicenceIndate = formatDate.format(userBusinessDTO.getBusinessLicenceIndate());
		}
		model.addAttribute("businessLicenceIndate",businessLicenceIndate);
		
		return "/sellcenter/information/InformationEdit";
	}
	
	/**
	 * 基本信息编辑
	 * @param model
	 * @return
	 */
	@RequestMapping("editUserInfo")
	@ResponseBody
	public Map<String,Object> editUserInfo(UserInfoDTO userInfoDTO,int editType,Model model,HttpServletRequest request){
		UserExtendsType userExtendsType = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("message", "修改成功");
		switch(editType) 
		{ 
			case 1: 
				//编辑用户基本信息
				//userInfoDTO.getUserDTO().setAuditStatus(1);
				boolean result_= userExportService.modifyUserInfo(userInfoDTO.getUserDTO());
				if(!result_){
					map.put("message", "修改失败");
				}
				break; 
			case 2: 
				//编辑营业执照信息
				userExtendsType = UserExtendsType.BUSINESS;
				userInfoDTO.getUserBusinessDTO().setBusinessStatus(1);
				break; 
			case 3: 
				//编辑组织机构代码信息
				userExtendsType = UserExtendsType.ORGANIZATION;
				userInfoDTO.getUserOrganizationDTO().setOrganizationStatus(1);
				break; 
			case 4: 
				//编辑税务登记证信息
				userExtendsType = UserExtendsType.TAX;
				userInfoDTO.getUserTaxDTO().setTaxStatus(1);
				break; 	
			default: 
				//编辑出金账户信息
				userExtendsType = UserExtendsType.ACCOUNT;
				userInfoDTO.getUserAccountDTO().setBankAccountStatus(1);
				break; 
		} 
		
		
		if(userExtendsType != null){
			UserDTO userDTO_ = userExportService.queryUserById(userInfoDTO.getUserId());
			if(userDTO_.getUserstatus().intValue() != 6){
				UserDTO userDTO = new UserDTO();
				userDTO.setUsertype(3);
				userDTO.setUid(userInfoDTO.getUserId());
				userDTO.setAuditStatus(1);
				userExportService.modifyUserInfoAndAuditStatus(userDTO);
			}
			ExecuteResult<UserInfoDTO> result = userExtendsService.modifyUserExtends(userInfoDTO, userExtendsType);
			if(result.isSuccess()){
				map.put("message", result.getResultMessage());
			}else{
				map.put("message","修改失败");
			}
//			map.put("message", result.getResultMessage());
		}
		request.setAttribute("map",map);
		return map;
	}
	
	/**
	 * 安全信息设置
	 * @return
	 */
	@RequestMapping("safety")
	public String safety(HttpServletRequest request,Model model){
		Long userId = this.getLoginUserId(request);
		UserDTO userDTO = this.userExportService.queryUserById(userId);
		//计算安全级别
		int safeLevel = 40;
		if(StringUtils.isNoneEmpty(userDTO.getUmobile())){
			safeLevel += 30;
		}
		if(StringUtils.isNoneEmpty(userDTO.getUmobile())){
			safeLevel += 30;
		}
		userDTO.setUserEmail(UserDtoUtils.hideUserEmail(userDTO.getUserEmail()));
		userDTO.setUmobile(UserDtoUtils.hideUserCellPhone(userDTO.getUmobile()));
		model.addAttribute("userDTO",userDTO);
		model.addAttribute("safeLevel",safeLevel);
		model.addAttribute("cla","style='width:100px;'");
		return "/sellcenter/information/safety";
	}
	
	/**
	 * 邮件/短信 身份验证
	 * @return
	 */
	@RequestMapping("authenticate")
	public String authenticate(UserDTO userDTO,String changeType,String backUrl,Model model){
		//查询
		userDTO = this.userExportService.queryUserById(userDTO.getUid());
		userDTO.setUserEmail(UserDtoUtils.hideUserEmail(userDTO.getUserEmail()));
		userDTO.setUmobile(UserDtoUtils.hideUserCellPhone(userDTO.getUmobile()));
		model.addAttribute("changeType",changeType);
		model.addAttribute("userDTO",userDTO);
		model.addAttribute("backUrl",backUrl);
		return "/sellcenter/information/authenticate";
	}
	
	/**
	 * 
	 * @returns
	 */
	@RequestMapping("newAuth")
	public String newAuth(UserDTO userDTO,String changeType,String backUrl,Model model){
		//查询
		userDTO = this.userExportService.queryUserById(userDTO.getUid());
		model.addAttribute("changeType",changeType);
		model.addAttribute("userDTO",userDTO);
		model.addAttribute("backUrl",backUrl);
		return "/sellcenter/information/newAuth";
	}

	/**
	 * 
	 * @return
	 */
	@RequestMapping("newAuthEdit")
	public String newAuthEdit(HttpServletRequest request, UserDTO userDTO,String changeType,String address,String backUrl,Model model){
		String token = LoginToken.getLoginToken(request);
		RegisterDTO registerDTO = userExportService.getUserByRedis(token);
		if("phone".equals(changeType)){
			userDTO.setUmobile(address);
			registerDTO.setUserMobile(address);
		}else{
			userDTO.setUserEmail(address);
			registerDTO.setUserEmail(address);
		}
		boolean isSuccess = this.userExportService.modifyUserInfo(userDTO);
		userExportService.updateUserInfoToRedis(token, registerDTO);
		model.addAttribute("changeType",changeType);
		model.addAttribute("backUrl",backUrl);
		request.setAttribute("isSuccess",isSuccess);
		return "/sellcenter/information/authOk";
	}
	
	/**
	 * 审核进度
	 * @return
	 */
	@RequestMapping("approveProgress")
	public String approveProgress(HttpServletRequest request,Model model){
		Long userId = this.getLoginUserId(request);
		model.addAttribute("userId",userId);
		/* 卖家资质审核 */
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("userDTO",userDTO);
		ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(userId);
		model.addAttribute("userInfoDTO",userInfoResult.getResult());
		/* 合同信息 */
		UserContractDTO userContractDTO  = new UserContractDTO();
		userContractDTO.setShopId(userDTO.getShopId());
		ExecuteResult<UserContractDTO> userContractResult = userContractService.findUserContractByCondition(userContractDTO);
		model.addAttribute("userContractDTO",userContractResult.getResult());
		/* 店铺确定-店铺开通 */
		ExecuteResult<ShopDTO> shopResult = shopExportService.findShopInfoById(userDTO.getShopId());
		model.addAttribute("shopDTO",shopResult.getResult());
		/* 付款确定 */
		//查询类目ID
		ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
		shopCategoryDTO.setShopId(userDTO.getShopId());
		ExecuteResult<DataGrid<ShopCategoryDTO>> shopCategoryResult = shopCategoryExportService.queryShopCategoryAll(shopCategoryDTO, null);
		List<ShopCategoryDTO> shopCategoryList = shopCategoryResult.getResult().getRows();
		model.addAttribute("shopCategorySum",shopCategoryList.size());
		model.addAttribute("shopCategoryList",shopCategoryList);
		if(shopCategoryList!=null && shopCategoryList.size()>0){
			Long[] cids = new Long[shopCategoryList.size()];
			for(int i=0; i < shopCategoryList.size(); i++){
				cids[i] = shopCategoryList.get(i).getCid();
			}
			//查询：类目名称
			ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascadeResult = itemCategoryService.queryParentCategoryList(cids);
			List<ItemCatCascadeDTO> itemCatCascadeList = itemCatCascadeResult.getResult();
			Map<Long,List<String>> itemCatCascadeMap = new HashMap<Long,List<String>>();
			//一级类目
			for(ItemCatCascadeDTO itemCatFirst : itemCatCascadeList){
				String firstName = itemCatFirst.getCname();
				//二级类目
				for(ItemCatCascadeDTO itemCatSecond : itemCatFirst.getChildCats()){
					String secondName = itemCatSecond.getCname();
					//三级类目
					for(ItemCatCascadeDTO itemCatThird : itemCatSecond.getChildCats()){
						List<String> itemCatNameList = new ArrayList<String>();
						itemCatNameList.add(firstName);
						itemCatNameList.add(secondName);
						itemCatNameList.add(itemCatThird.getCname());
						itemCatCascadeMap.put(itemCatThird.getCid(), itemCatNameList);
					}
				}
			}
			//查询：平台质量保证金-平台使用费
			ExecuteResult<List<SettleCatExpenseDTO>> settleCatExpenseResult = sattleCatExpenseExportService.queryByIds(cids);
			List<SettleCatExpenseDTO> settleCatExpenseList = settleCatExpenseResult.getResult();
			Map<Long,SettleCatExpenseDTO> settleCatExpenseMap = new HashMap<Long,SettleCatExpenseDTO>();
			for(SettleCatExpenseDTO settleCatExpense:settleCatExpenseList){
				settleCatExpenseMap.put(settleCatExpense.getCategoryId(), settleCatExpense);
			}
			//数据处理-用于显示
			List<List<String>> marginList = new ArrayList<List<String>>();
			List<List<String>> royaltiesList = new ArrayList<List<String>>();
			double marginSummary = 0L;
			double royaltiesSummary = 0L;
			for(int i=0; i < shopCategoryList.size(); i++){
				
				List<String> margin = new ArrayList<String>();
				List<String> royalties = new ArrayList<String>();
				ShopCategoryDTO shopCategory = shopCategoryList.get(i);
				Long cid = shopCategory.getCid();
				
				//类目对象：一级名称/二级名称/三级名称
				List<String> itemCatNameList = itemCatCascadeMap.get(cid);
				margin.add(itemCatNameList.get(0));
				royalties.add(itemCatNameList.get(0));
				margin.add(itemCatNameList.get(1));
				royalties.add(itemCatNameList.get(1));
				margin.add(itemCatNameList.get(2));
				royalties.add(itemCatNameList.get(2));
				
				//金额：平台质量保证金-平台使用费
				SettleCatExpenseDTO settleCatExpense = settleCatExpenseMap.get(cid);
				if(settleCatExpense!=null){
					BigDecimal cashDeposit = settleCatExpense.getCashDeposit();
					BigDecimal serviceFee = settleCatExpense.getServiceFee();
					
					royalties.add(String.format("%.2f", serviceFee));
					margin.add(String.format("%.2f", cashDeposit));
					marginList.add(margin);
					royaltiesList.add(royalties);
					
					marginSummary += cashDeposit.doubleValue();
					royaltiesSummary += serviceFee.doubleValue();
				}
			}
			model.addAttribute("marginList",marginList);
			model.addAttribute("royaltiesList",royaltiesList);
			model.addAttribute("marginSummary",String.format("%.2f", marginSummary));
			model.addAttribute("royaltiesSummary",String.format("%.2f", royaltiesSummary));
		}
		return "/sellcenter/information/progress";
	}
	
	@ResponseBody
	@RequestMapping("updateUserContract")
	public Map<String,Object> updateUserContract(String state, String auditRemark, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();
		Long userId = this.getLoginUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		UserContractDTO  userContractDTO  = new UserContractDTO();
		userContractDTO.setShopId(userDTO.getShopId());
		ExecuteResult<UserContractDTO> findResult = userContractService.findUserContractByCondition(userContractDTO);
		userContractDTO = findResult.getResult();
		//【驳回-0】【确定-3】
		if("0".equals(state)){
			userContractDTO.setContractStatus(0);
			userContractDTO.setAuditRemark(auditRemark);
		}else if("3".equals(state)){
			userContractDTO.setContractStatus(3);
		}
		ExecuteResult<UserContractDTO> modifyResult = userContractService.modifyUserContractById(userContractDTO, String.valueOf(userId));
		List<String> error = modifyResult.getErrorMessages();
		if(error!=null && error.size()>0){
			map.put("message", error);
		} else {
			map.put("userContractDTO", modifyResult.getResult());
			map.put("message", "操作成功!");
		}
		return map;
	}
	
	/**
	 * 短信/邮箱验证
	 * @param address
	 * @param type
	 * @param request
	 * @return
	 */
	@RequestMapping("sendCode")
	@ResponseBody
	public Map<String,Object> sendEmailOrPhoneCode(String address,int type,HttpServletRequest request){
		String[] addrs = new String[]{address};
		UUID uuid = UUID.randomUUID();
		String key = uuid.toString();
		
		//2015-06-10王东晓添加
		
		VerifyCodeMessageDTO messageDTO = new VerifyCodeMessageDTO();
		messageDTO.setEnumType("UPBINDPHONE");
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
		
		//验证码发送
//		baseWebSiteMessageService.sendVerificationCode("UPBINDPHONE",key, addrs, type);
		//接收已发送的验证码
		String code = baseWebSiteMessageService.getGeneralUserVerificationCode(key);
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("code", code);
		return result;
	}
	
	/**
	 * 获取地址名称
	 * @param code
	 * @return
	 */
	private String getNameByCode(String code){
		String name = "";
		if(StringUtils.isNotEmpty(code)){
			String[] codeArray = code.split(",");
			if(codeArray == null || codeArray.length == 0)
				return "";
			for(String codeEvery : codeArray){
				//获取编码对应的地址
				String[] codes = new String[]{codeEvery};
				ExecuteResult<List<AddressBaseDTO>> result = addressBaseService.queryNameByCode(codes);
				List<AddressBaseDTO> addressBaseDTOList = result.getResult();
				String address = "";
				if(addressBaseDTOList != null)
					address = addressBaseDTOList.get(0).getName();
				if(StringUtils.isNotEmpty(name)){
					name += ",";
				}
				name += address;
			}
		}
		return name;
	}
	
	private String getAddressByCode(String code){
		if(code.equals("37")){
			return "山东省";
		}else if(code.equals("3701")){
			return "济南市";
		}else if(code.equals("370102")){
			return "历下区";
		}else if(code.equals("370103")){
			return "市中区";
		}
		return "";
	}
	
	/**
	 * 获取登录用户Id
	 * @param request
	 * @return
	 */
	private Long getLoginUserId(HttpServletRequest request){
		return WebUtil.getInstance().getUserId(request);
	}
	
}
