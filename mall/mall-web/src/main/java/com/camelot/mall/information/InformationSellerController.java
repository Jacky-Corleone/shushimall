package com.camelot.mall.information;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.mall.sellcenter.UserDtoUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.enums.UserEnums.DepartMent;
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
@RequestMapping("/information/informationSeller")
public class InformationSellerController {

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
	private LogoExportService logoService;
	/**
	 * 基础信息编辑
	 * @return
	 */
	@RequestMapping("initLoad")
	public String initLoad(HttpServletRequest request,Model model){
		//所在部门（给所在部门下拉框赋值）下面一行修改Bug1876时添
		model.addAttribute("departMents", DepartMent.values());
		Long userId = WebUtil.getInstance().getUserId(request);
		
        UserDTO userDTO = userExportService.queryUserById(userId);
        if(null != userDTO.getParentId()){//子账号：将uid设置为父账号的用户id
        	userId = userDTO.getParentId();
        }
		
		 userDTO = userExportService.queryUserById(userId);
		//下面一行修改Bug1876时添
		model.addAttribute("userDTO",userDTO);
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
				return "redirect:/information/registerSeller/initSeller";
			}
			//判断是否已经认证通过
			ExecuteResult<ShopDTO> shopResult = shopExportService.findShopInfoById(userDTO.getShopId());
			ShopDTO shop = shopResult.getResult();
			if(shop==null || shop.getRunStatus()==null || shop.getStatus().longValue() < 4){
				return "redirect:/information/progressSeller/initProgress";
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
		model.addAttribute("userManageDTO",userInfoDTO.getUserManageDTO());
		//获取省市
		List<AddressBase> addressList = addressBaseService.queryAddressBase("0");
		model.addAttribute("addressList", addressList);
        //营业执照所在地
		String busLicAddrCode = userInfoDTO.getUserBusinessDTO().getBusinessLicenceAddress();
        String busLicProvince = "";//营业执照所在地：省编码
        String busLicCity = "";//营业执照所在地：市编码
        String busLicArea = "";//营业执照所在地：地区编码
        if(StringUtils.isNotBlank(busLicAddrCode)){
            String[] adCodeArr = busLicAddrCode.split("\\,");
            if(StringUtils.isNotBlank(adCodeArr[0])){
                busLicProvince = adCodeArr[0];
            }
            if(StringUtils.isNotBlank(adCodeArr[1])){
                busLicCity = adCodeArr[1];
            }
            /*if(StringUtils.isNotBlank(adCodeArr[2])){
                busLicArea = adCodeArr[2];
            }*/
        }
        model.addAttribute("busLicProvince",busLicProvince);
        model.addAttribute("busLicCity",busLicCity);
        model.addAttribute("busLicArea",busLicArea);
        model.addAttribute("busLicAddr",this.getNameByCode(busLicAddrCode));
        model.addAttribute("dealerTypes", UserEnums.DealerType.values());
        model.addAttribute("erpTypes", UserEnums.ERPType.values());

		//公司所在地
		String companyAddressCode = userInfoDTO.getUserBusinessDTO() == null ? "":userInfoDTO.getUserBusinessDTO().getCompanyAddress();
        String companyProvince = ""; //公司所在地：省编码
        String companyCity = ""; //公司所在地：市编码
        String companyArea = ""; //公司所在地：地区编码
        if(StringUtils.isNotBlank(companyAddressCode)){
            String[] adCodeArr = companyAddressCode.split("\\,");
            if(adCodeArr.length > 0){
        		companyProvince = adCodeArr[0];
            }
            if(adCodeArr.length > 1){
        		companyCity = adCodeArr[1];
            }
            /*if(adCodeArr.length > 2){
        		companyArea = adCodeArr[2];
            }*/
        }
        model.addAttribute("companyProvince",companyProvince);
        model.addAttribute("companyCity",companyCity);
        model.addAttribute("companyArea",companyArea);
        model.addAttribute("companyAddress",this.getNameByCode(companyAddressCode));
		//开户行支行所在地
		String bankBranchIsLocatedCode = userInfoDTO.getUserBusinessDTO() == null ? "":userInfoDTO.getUserAccountDTO().getBankBranchIsLocated();
		String bankProvince = "";
        String bankCity = "";
        String bankArea = "";
        if(StringUtils.isNotBlank(bankBranchIsLocatedCode)){
            String[] adCodeArr = bankBranchIsLocatedCode.split("\\,");
            if(adCodeArr.length > 0){
            	bankProvince = adCodeArr[0];
            }
            if(adCodeArr.length > 1){
            	bankCity = adCodeArr[1];
            }
            /*if(adCodeArr.length > 2){
            	bankArea = adCodeArr[2];
            }*/
        }
        model.addAttribute("bankProvince",bankProvince);
        model.addAttribute("bankCity",bankCity);
        model.addAttribute("bankArea",bankArea);
		model.addAttribute("bankBranchIsLocated",this.getNameByCode(bankBranchIsLocatedCode));

		//格式化营业执照有效期
		DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
		String businessLicenceIndate = "";
		UserBusinessDTO userBusinessDTO = userInfoDTO.getUserBusinessDTO();
		if(userBusinessDTO != null && userBusinessDTO.getBusinessLicenceIndate() != null){
			businessLicenceIndate = formatDate.format(userBusinessDTO.getBusinessLicenceIndate());
		}
		model.addAttribute("businessLicenceIndate",businessLicenceIndate);

		return "/information/informationSeller";
	}

    /**
     * 基本信息编辑
     * @param userInfoDTO
     * @param editType
     * @param request
     * @return
     */
	@ResponseBody
	@RequestMapping("modifySeller")
	public Map<String,Object> modifySeller(UserInfoDTO userInfoDTO,int editType,HttpServletRequest request){
		UserExtendsType userExtendsType = null;
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("message", "操作成功");
        UserDTO userDTO_ = userExportService.queryUserById(userInfoDTO.getUserId());
		switch(editType)
		{
			case 1:
				//编辑用户基本信息
				//userInfoDTO.getUserDTO().setAuditStatus(1);
//                com.camelot.usercenter.enums.UserEnums.DepartMent dpt = userInfoDTO.getUserDTO().getDepartment();

				boolean result_= userExportService.modifyUserInfo(userInfoDTO.getUserDTO());
				if(!result_){
					map.put("message", "修改失败");
				}
				break;
			case 2:
				//编辑营业执照信息
				userExtendsType = UserExtendsType.BUSINESS;
                //驳回状态时由前台负责修改为待审核状态，已审核状态时由后台负责修改为待审核状态
                if(userDTO_.getUserstatus().intValue() != 4 && userDTO_.getUserstatus().intValue() != 6){
                    userInfoDTO.getUserBusinessDTO().setBusinessStatus(1);
                }
				break;
			case 3:
				//编辑组织机构代码信息
				userExtendsType = UserExtendsType.ORGANIZATION;
                //驳回状态时由前台负责修改为待审核状态，已审核状态时由后台负责修改为待审核状态
                if(userDTO_.getUserstatus().intValue() != 4 && userDTO_.getUserstatus().intValue() != 6){
                    userInfoDTO.getUserOrganizationDTO().setOrganizationStatus(1);
                }
				break;
			case 4:
				//编辑税务登记证信息
				userExtendsType = UserExtendsType.TAX;
                //驳回状态时由前台负责修改为待审核状态，已审核状态时由后台负责修改为待审核状态
                if(userDTO_.getUserstatus().intValue() != 4 && userDTO_.getUserstatus().intValue() != 6){
                    userInfoDTO.getUserTaxDTO().setTaxStatus(1);
                }
				break;
            case 5:
                //公司经营信息
                userExtendsType = UserExtendsType.MANAGE;
                //驳回状态时由前台负责修改为待审核状态，已审核状态时由后台负责修改为待审核状态
                if(userDTO_.getUserstatus().intValue() != 4 && userDTO_.getUserstatus().intValue() != 6){
                    userInfoDTO.getUserManageDTO().setUserManageStatus("1");
                }
                break;
			default:
				//编辑出金账户信息
				userExtendsType = UserExtendsType.ACCOUNT;
                //驳回状态时由前台负责修改为待审核状态，已审核状态时由后台负责修改为待审核状态
                if(userDTO_.getUserstatus().intValue() != 4 && userDTO_.getUserstatus().intValue() != 6){
                    userInfoDTO.getUserAccountDTO().setBankAccountStatus(1);
                }
				break;
		}
		if(userExtendsType != null){
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
		}
		request.setAttribute("map",map);
		return map;
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
				if(addressBaseDTOList != null && !addressBaseDTOList.isEmpty() && addressBaseDTOList.size()>0){
					address = addressBaseDTOList.get(0).getName();
				}
				if(StringUtils.isNotEmpty(name)){
					name += ",";
				}
				name += address;
			}
		}
		return name;
	}

}
