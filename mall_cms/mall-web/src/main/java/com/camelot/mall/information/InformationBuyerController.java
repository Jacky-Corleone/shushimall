package com.camelot.mall.information;

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

import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.mall.sellcenter.UserDtoUtils;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.sellercenter.logo.dto.LogoDTO;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.BusinessScale;
import com.camelot.usercenter.enums.UserEnums.CompanyPeopleNum;
import com.camelot.usercenter.enums.UserEnums.CompanyQualt;
import com.camelot.usercenter.enums.UserEnums.DepartMent;
import com.camelot.usercenter.enums.UserEnums.UsePurchaseType;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/information/informationBuyer")
public class InformationBuyerController {

	@Resource
	private UserExportService userExportService;

	@Resource
	private AddressBaseService addressBaseService;

	@Resource
	private UserExtendsService userExtendsService;

	@Resource
	private UserCompanyService userCompanyService;

	@Resource
	private LogoExportService logoService;

	/**
	 * 买家基本信息加载
	 */
	@RequestMapping("/initLoad")
	public String initLoad(HttpServletRequest request, Model model) {
		//获取省份、所在部门、经营范围、经营规模、企业人数、公司性质
		model.addAttribute("addressList", addressBaseService.queryAddressBase("0"));
		model.addAttribute("departMents", DepartMent.values());
		model.addAttribute("usePurchaseTypes", UsePurchaseType.values());
		model.addAttribute("businessScales", BusinessScale.values());
		model.addAttribute("companyPeopleNums", CompanyPeopleNum.values());
		model.addAttribute("companyQualts", CompanyQualt.values());
		//用户ID
		Long userId = WebUtil.getInstance().getUserId(request);
		model.addAttribute("userId", userId);
		//判断是否已经认证
		UserDTO userDTO = userExportService.queryUserById(userId);
		model.addAttribute("userDTO", userDTO);
		Integer uStatus = userDTO.getUserstatus();
		if( uStatus < 3 ){
			return "redirect:/information/register/initRegister?type=buyer";
		}
		//用户-扩展信息
		ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(userId);
		UserInfoDTO userInfoDTO = userInfoResult.getResult();
//		userInfoDTO.getUserBusinessDTO().setCompanyDeclinedAddress("公司地址chl06");
		UserDTO u = userInfoDTO.getUserDTO();
		u.setUserEmail(UserDtoUtils.hideUserEmail(userDTO.getUserEmail()));
		u.setUmobile(UserDtoUtils.hideUserCellPhone(userDTO.getUmobile()));

		model.addAttribute("userInfoDTO",userInfoDTO);
		//公司所在地
		String companyAddressCode = userInfoDTO.getUserBusinessDTO() == null ? "":userInfoDTO.getUserBusinessDTO().getCompanyAddress();
		model.addAttribute("companyAddress",this.getNameByCode(companyAddressCode));

		return "/information/informationBuyer";
	}

	/**
	 * 修改：买家基本信息
	 */
	@ResponseBody
	@RequestMapping("modifyBuyer")
	public Map<String,Object> modifyBuyer(UserInfoDTO userInfoDTO, String companyQualt, String companyPeoNum,
			String businessScale, HttpServletRequest request){
		Map<String,Object> map = new HashMap<String,Object>();

		//更改审批记录状态
		UserDTO userDTO = userExportService.queryUserById(userInfoDTO.getUserId());
		Integer userstatus = userDTO.getUserstatus();
		if(userstatus == 4 ){
			userstatus = 3;
		}else if(userstatus == 6){
			userstatus = 5;
		}
		userDTO.setUserstatus(userstatus);
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
        if(result.isSuccess()){
            map.put("message", result.getResultMessage());
        }else{
            map.put("message","修改失败");
        }
		request.setAttribute("map",map);
		return map;
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
