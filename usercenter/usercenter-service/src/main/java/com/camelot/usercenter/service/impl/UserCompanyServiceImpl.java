package com.camelot.usercenter.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.usercenter.dao.UserModifyInfoMybatisDAO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserCenterOperaService;
import com.camelot.usercenter.service.UserExtendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.constant.GlobalConstant;
import com.camelot.common.util.ErrorUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.usercenter.dao.UserComDeviceMybatisDAO;
import com.camelot.usercenter.dao.UserCompanyMybatisDAO;
import com.camelot.usercenter.domain.UserCompanyDevice;
import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.device.UserComDeviceDTO;
import com.camelot.usercenter.enums.UserEnums.DeviceType;
import com.camelot.usercenter.service.UserCompanyService;
import com.camelot.usercenter.service.util.UserCompanyUtil;

@Service("userCompanyService")
public class UserCompanyServiceImpl implements UserCompanyService {
	private final static Logger logger = LoggerFactory.getLogger(UserCompanyServiceImpl.class);
	@Resource
	UserCompanyMybatisDAO userCompanyMybatisDAO;
	@Resource
	UserComDeviceMybatisDAO userComDeviceMybatisDAO;
    @Resource
    UserExtendsService userExtendsService;
    @Resource
    UserModifyInfoMybatisDAO userModifyInfoMybatisDAO;

    @Resource
    UserCenterOperaService userCenterOperaService;
	@Override
	public ExecuteResult<UserCompanyDTO> saveUserCompany(UserCompanyDTO userCompanyDTO) {
		logger.info("方法[{}]，入参：[{}]","UserCompanyService-saveUserCompany",JSONObject.toJSON(userCompanyDTO));
		ExecuteResult<UserCompanyDTO> result=new ExecuteResult<UserCompanyDTO>();
		try {
			userCompanyDTO.setStatus(GlobalConstant.TEMP_STATUS);
			userCompanyMybatisDAO.insert(userCompanyDTO);
			result.setResultMessage("保存成功");
			result.setResult(userCompanyDTO);
			logger.info("方法[{}]，出参：[{}]","UserCompanyService-saveUserCompany",JSONObject.toJSON(result));
		} catch (Exception e) {
			result.getErrorMessages().add(ErrorUtil.buildErrorMsg("保存异常", e.getMessage()));
			logger.info("方法[{}]，异常：[{}]","UserCompanyService-saveUserCompany",e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<UserCompanyDTO> modifyUserCompany(UserCompanyDTO userCompanyDTO) {
		logger.info("方法[{}]，入参：[{}]","UserCompanyService-modifyUserCompany",JSONObject.toJSON(userCompanyDTO));
		ExecuteResult<UserCompanyDTO> result=new ExecuteResult<UserCompanyDTO>();
		try{
            UserCompanyDTO userCompanyBefore= userCompanyMybatisDAO.selectByUId(userCompanyDTO.getUserId());
            ExecuteResult<UserInfoDTO> beforeUserInfo = userExtendsService.findUserInfo(userCompanyDTO.getUserId());
            UserInfoDTO beforeUser = beforeUserInfo.getResult();
            if (beforeUser != null && (beforeUser.getUserDTO().getUserstatus() == 4 || beforeUser.getUserDTO().getUserstatus() == 6) ) {
                UserModifyInfoDTO modify = new UserModifyInfoDTO();
                modify.setApplicantName(beforeUser.getUserBusinessDTO().getCompanyName());
                modify.setApplicantUserid(userCompanyDTO.getUserId());
                modify.setApplyStatus(0); //默认未审核
                modify.setCreateTime(new Date());
                modify.setUserType(beforeUser.getUserDTO().getUsertype());
                userModifyInfoMybatisDAO.insert(modify);
                if (modify.getId() != null) {
                    userCenterOperaService.insertModifyDetailByCompanyInfo(userCompanyBefore, userCompanyDTO, modify);
                }
            }else{
                Integer count =userCompanyMybatisDAO.update(userCompanyDTO);
                if(count>0){
                    result.setResultMessage("修改成功");
                }else{
                    result.setResultMessage("修改失败");
                }
            }


			result.setResult(userCompanyDTO);
			logger.info("方法[{}]，出参：[{}]","UserCompanyService-modifyUserCompany",JSONObject.toJSON(result));
		}catch (Exception e) {
			result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改异常", e.getMessage()));
			logger.info("方法[{}]，异常：[{}]","UserCompanyService-modifyUserCompany",e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<UserCompanyDTO> findUserCompanyByUId(Long uid) {
		logger.info("方法[{}]，入参：[{}]","UserCompanyService-findUserCompanyByUId",uid);
		ExecuteResult<UserCompanyDTO> result=new ExecuteResult<UserCompanyDTO>();
		UserCompanyDTO userCompanyDTO =userCompanyMybatisDAO.selectByUId(uid);
		result.setResult(userCompanyDTO);
		logger.info("方法[{}]，出参：[{}]","UserCompanyService-findUserCompanyByUId",JSONObject.toJSON(result));
		return result;
	}

	@Override
	public ExecuteResult<UserComDeviceDTO> saveComDevice(UserComDeviceDTO userComDeviceDTO) {
		logger.info("方法[{}]，入参：[{}]","UserCompanyService-saveComDevice",JSONObject.toJSON(userComDeviceDTO));
		ExecuteResult<UserComDeviceDTO> result = new ExecuteResult<UserComDeviceDTO>();
		try{
			userComDeviceMybatisDAO.delete(userComDeviceDTO.getUserId());
			userComDeviceMybatisDAO.insert(userComDeviceDTO);
			result.setResultMessage("保存成功");
			result.setResult(userComDeviceDTO);
			logger.info("方法[{}]，出参：[{}]","UserCompanyService-saveComDevice",JSONObject.toJSON(result));
		}catch (Exception e) {
			result.getErrorMessages().add(ErrorUtil.buildErrorMsg("保存异常", e.getMessage()));
			logger.info("方法[{}]，异常：[{}]","UserCompanyService-saveComDevice",e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@Override
	public ExecuteResult<UserComDeviceDTO> findComDeviceListByUId(Long uid,DeviceType deviceType) {
		logger.info("方法[{}]，入参：[{}][{}]","UserCompanyService-findComDeviceListByUId",uid,deviceType);
		ExecuteResult<UserComDeviceDTO> result=new ExecuteResult<UserComDeviceDTO>();
		if(uid!=null){
			List<UserCompanyDevice> list=userComDeviceMybatisDAO.selectListByCondition(uid, deviceType.getCode());
			result=new UserCompanyUtil().buildUserComDeviceDTO(list,uid);
		}else{
			result.getErrorMessages().add(ErrorUtil.buildErrorMsg("查询失败：uid不能为空", null));
		}
		logger.info("方法[{}]，出参：[{}]","UserCompanyService-findComDeviceListByUId",JSONObject.toJSON(result));
		return result;
	}

	@Override
	public ExecuteResult<Integer> modifyUserCompanyStatus(UserCompanyDTO userCompanyDTO) {
		ExecuteResult<Integer> result=new ExecuteResult<Integer>();
		userCompanyDTO.setStatus(GlobalConstant.COMMIT_STATUS);
		Integer res=userCompanyMybatisDAO.updateUserCompanyStatus(userCompanyDTO);
		result.setResult(res);
		return result;
	}
	
}
