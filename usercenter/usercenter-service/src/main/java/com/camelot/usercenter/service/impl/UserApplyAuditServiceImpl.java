package com.camelot.usercenter.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Protocol.Command;

import com.camelot.common.constant.GlobalConstant;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserModifyInfoMybatisDAO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.contract.UserApplyAuditInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserApplyAuditService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserModifyDetailService;
import com.camelot.usercenter.service.UserModifyInfoService;

@Service("userApplyAuditService")
public class UserApplyAuditServiceImpl implements UserApplyAuditService {
	private final static Logger logger = LoggerFactory.getLogger(UserApplyAuditServiceImpl.class);
	@Resource
	private  UserModifyInfoService userModifyInfoService;
	
	@Resource
	private  UserModifyDetailService userModifyDetailService;
	
	@Resource
	private  UserExtendsService userExtendsService;
	
	@Resource
	private UserModifyInfoMybatisDAO userModifyInfoMybatisDAO;
	
	@Override
	public DataGrid<UserModifyInfoDTO> findUserAuditListByCondition(UserModifyInfoDTO dto, Pager pager) {
		
		DataGrid<UserModifyInfoDTO> res= userModifyInfoService.getUserModifyInfoList(dto, pager);
		
		return res;
	}

	@Override
	public DataGrid<UserModifyDetailDTO> findUserModifyDetailByCondition(UserModifyInfoDTO userModifyInfoDTO,Pager pager) {
		DataGrid<UserModifyDetailDTO> resut=new DataGrid<UserModifyDetailDTO>();
		UserModifyDetailDTO userModifyDetailDTO=new UserModifyDetailDTO();
		userModifyDetailDTO.setModifyInfoId(userModifyInfoDTO.getId());
		
		DataGrid<UserModifyDetailDTO> res=userModifyDetailService.getUserModifyDetailList(userModifyDetailDTO, pager);
		return res;
	}

	@Override
	public ExecuteResult<Integer> moifyUserApplyAudit(String UserModifyInfoId,String auditUserId, ComStatus status,String remark) {
		ExecuteResult<Integer> executeResult=new ExecuteResult<Integer>();
		Integer res=0;
		try{
        //查询用户信息
		UserModifyInfoDTO modifyDto= userModifyInfoMybatisDAO.selectById(Long.parseLong(UserModifyInfoId));
		ExecuteResult<UserInfoDTO> UserInfoDTOResult=userExtendsService.findUserInfo(modifyDto.getApplicantUserid());

		//通过
		if(status.getCode()==2){
			modifyDto.setApplyStatus(1);
            if(GlobalConstant.USER_MODIFY_BUINESS.equals(modifyDto.getModifyType())){
                userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.PASS, UserExtendsType.BUSINESS);
            }else if(GlobalConstant.USER_MODIFY_ORG.equals(modifyDto.getModifyType())){
                userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.PASS, UserExtendsType.ORGANIZATION);
            }else if(GlobalConstant.USER_MODIFY_TAX.equals(modifyDto.getModifyType())){
                userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.PASS, UserExtendsType.TAX);
            }else if(GlobalConstant.USER_MODIFY_ACCOUNT.equals(modifyDto.getModifyType())){
                userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.PASS, UserExtendsType.ACCOUNT);
            }else if(GlobalConstant.USER_MANAGE.equals(modifyDto.getModifyType())){
                userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.PASS, UserExtendsType.MANAGE);
            }
            userModifyDetailService.updateModfiyAllTablesAndCoumn(Long.parseLong( UserModifyInfoId ),remark,auditUserId);

		//驳回 
		}else if(status.getCode()==0){
			modifyDto.setApplyStatus(2);
			if(GlobalConstant.USER_MODIFY_BUINESS.equals(modifyDto.getModifyType())){
				userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.REJECT, UserExtendsType.BUSINESS);
			}else if(GlobalConstant.USER_MODIFY_ORG.equals(modifyDto.getModifyType())){
				userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.REJECT, UserExtendsType.ORGANIZATION);
			}else if(GlobalConstant.USER_MODIFY_TAX.equals(modifyDto.getModifyType())){
				userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.REJECT, UserExtendsType.TAX);
			}else if(GlobalConstant.USER_MODIFY_ACCOUNT.equals(modifyDto.getModifyType())){
				userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.REJECT, UserExtendsType.ACCOUNT);
			}else if(GlobalConstant.USER_MANAGE.equals(modifyDto.getModifyType())){
                userExtendsService.modifyStatusByType(UserInfoDTOResult.getResult().getExtendId(), ComStatus.REJECT, UserExtendsType.MANAGE);
            }
			
		}

		modifyDto.setModifyTime(new Date());
		modifyDto.setRemark(remark);
		userModifyInfoService.updateModifyInfoSelective(modifyDto);

        res=1;

		}catch(Exception e){
			res=0;
			throw new RuntimeException("商家修改信息审核失败"+e.getMessage());
		}
		executeResult.setResult(res);
		return executeResult;
	}

	
	
	
}
