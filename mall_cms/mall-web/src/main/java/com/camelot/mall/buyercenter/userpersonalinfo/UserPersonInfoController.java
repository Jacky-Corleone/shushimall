package com.camelot.mall.buyercenter.userpersonalinfo;

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
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dto.userInfo.UserPersonalInfoDTO;
import com.camelot.usercenter.service.UserPersonalInfoService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/userPersonalInfo")
public class UserPersonInfoController {

	@Resource
	private UserPersonalInfoService userPersonalInfoService;

	@Resource
	private AddressBaseService addressBaseService;

	/**
	 * 展示买家信息
	 */
	@RequestMapping("listUserPersonalInfo")
	public String listUserPersonInfo(UserPersonalInfoDTO userPersonalInfoDTO,HttpServletRequest request, Model model) {
		Long userId = WebUtil.getInstance().getUserId(request);
		if(userId == null){
			return "500";
		}
		//获取个人信息资料完整度
		if(userPersonalInfoService.getPersonlInfoPerfectDegree(userId.toString()) != null){
			String userPersonalInfoPerfectDegree = userPersonalInfoService.getPersonlInfoPerfectDegree(userId.toString()).getResult();
			if(userPersonalInfoPerfectDegree != null){//买家已注册个人信息资料
				model.addAttribute("userPersonalInfoPerfectDegree", userPersonalInfoPerfectDegree);
			}else{//买家暂时未注册个人信息资料
				model.addAttribute("userPersonalInfoPerfectDegree", "0.0%");
			}
		}
		//获取UserPersonalInfoDTO
		UserPersonalInfoDTO userPersonalInfoDTO2 = new UserPersonalInfoDTO();
		userPersonalInfoDTO.setUserId(userId);
		List<UserPersonalInfoDTO> userPersonalInfoDTOList = userPersonalInfoService.searchByCondition(userPersonalInfoDTO);
		if(userPersonalInfoDTOList != null && userPersonalInfoDTOList.size()>0){//买家已注册个人信息资料
			userPersonalInfoDTO2 = userPersonalInfoDTOList.get(0);
		}else{
			userPersonalInfoDTO2.setUserId(userId);
		}
		model.addAttribute("userPersonalInfoDTO",userPersonalInfoDTO2);
		//获取籍贯代码
		List<AddressBase> originList = addressBaseService.queryAddressBase("0");
		model.addAttribute("originList", originList);
		//解析籍贯名称
		String originCode = userPersonalInfoDTO == null ? "":userPersonalInfoDTO2.getOrigin();
		model.addAttribute("finalOrigin",this.getNameByCode(originCode));
		return "buyercenter/userPersonalInfo";
	}

	/**
	 * 保存买家信息
	 */
	@RequestMapping("updateUserPersonalInfo")
	@ResponseBody
	public Map<String,Object> updateUserPersonalInfo(UserPersonalInfoDTO userPersonalInfoDTO,HttpServletRequest request, Model model){
		Map<String,Object> map = new HashMap<String,Object>();
		ExecuteResult<UserPersonalInfoDTO> userPersonalInfoResult;
		if(userPersonalInfoDTO.getId()!=null && userPersonalInfoDTO.getId().longValue() > 0){//买家已注册个人信息资料
			userPersonalInfoResult = userPersonalInfoService.updateUserPersonalInfoDTO(userPersonalInfoDTO);
		} else {//买家暂时未注册个人信息资料
			userPersonalInfoResult = userPersonalInfoService.createUserPersonalInfoDTO(userPersonalInfoDTO);
		}

		if(userPersonalInfoResult.isSuccess()){
			map.put("messages", "保存成功!");
		} else {
			map.put("messages", "保存失败!");
			map.put("userPersonalInfoDTO", userPersonalInfoResult.getResult());
		}
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
				if(addressBaseDTOList != null && addressBaseDTOList.size() > 0)
					address = addressBaseDTOList.get(0).getName();
				if(StringUtils.isNotEmpty(name)){
					name += ",";
				}
				name += address;
			}
		}
		return name;
	}

}
