package com.camelot.usercenter.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.enums.UserEnums;

import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.constant.GlobalConstant;
import com.camelot.common.util.ErrorUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.openplatform.util.ExceptionUtils;
import com.camelot.openplatform.util.GeneralMsgCode;
import com.camelot.usercenter.dao.UserAuditLogMybatisDAO;
import com.camelot.usercenter.dao.UserContractMybatisDAO;
import com.camelot.usercenter.dao.UserExtendsMybatisDAO;
import com.camelot.usercenter.dao.UserModifyInfoMybatisDAO;
import com.camelot.usercenter.dao.UserMybatisDAO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.service.UserAuditLogService;
import com.camelot.usercenter.service.UserCenterOperaService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserModifyInfoService;

@Service("userExtendsService")
public class UserExtendsServiceImpl implements UserExtendsService {
    private final static Logger logger = LoggerFactory.getLogger(UserExtendsServiceImpl.class);
    @Resource
    UserExtendsMybatisDAO userExtendsMybatisDAO;
    @Resource
    UserContractMybatisDAO userContractMybatisDAO;

    @Resource
    UserMybatisDAO userMybatisDAO;
    @Resource
    UserModifyInfoMybatisDAO userModifyInfoMybatisDAO;

    @Resource
    UserCenterOperaService userCenterOperaService;

    @Resource
    UserExtendsService userExtendsService;

    @Resource
    UserAuditLogService userAuditLogService;

    @Resource
    UserModifyInfoService userModifyInfoService;


    @Override
    public ExecuteResult<UserInfoDTO> saveUserExtends(UserInfoDTO userInfoDTO, UserExtendsType userExtendsType) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExtendsService-saveUserExtends", JSONObject.toJSON(userInfoDTO),
                userExtendsType);
        ExecuteResult<UserInfoDTO> result = new ExecuteResult<UserInfoDTO>();
        try {
            verifySave(userInfoDTO); // 校验数据

            if (userInfoDTO.getExtendId() == null) {
                // 用户扩展信息初始化
                if (userInfoDTO.getUserCiticDTO() == null) {
                    UserCiticDTO cto = new UserCiticDTO();
                    cto.setAccountState(1); //默认待审核
                    userInfoDTO.setUserCiticDTO(cto);
                }
                userExtendsMybatisDAO.insert(userInfoDTO);

                result.setResultMessage("保存成功");
                result.setResult(userInfoDTO);
            } else {

                result = modifyUserExtends(userInfoDTO, userExtendsType);

            }
            logger.info("方法[{}]，出参：[{}]", "UserExtendsService-saveUserExtends", JSONObject.toJSON(result));
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("保存异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExtendsService-saveUserExtends", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ExecuteResult<UserInfoDTO> createUserExtends(UserInfoDTO userInfoDTO) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExtendsService-createUserExtends", JSONObject.toJSON(userInfoDTO));
        ExecuteResult<UserInfoDTO> result = new ExecuteResult<UserInfoDTO>();
        try {
            verifySave(userInfoDTO); // 校验数据
            //新增前先删除下 可能存在的重复数据
            userExtendsMybatisDAO.deleteUserExtendByUserId(userInfoDTO.getUserId());
            if (userInfoDTO.getExtendId() == null) {
                // 用户扩展信息初始化
                if (userInfoDTO.getUserCiticDTO() == null) {
                    UserCiticDTO cto = new UserCiticDTO();
                    cto.setAccountState(1); //默认待审核
                    userInfoDTO.setUserCiticDTO(cto);
                }

                userExtendsMybatisDAO.insert(userInfoDTO);

                result.setResultMessage("保存成功");
                result.setResult(userInfoDTO);
            }
            logger.info("方法[{}]，出参：[{}]", "UserExtendsService-createUserExtends", JSONObject.toJSON(result));
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("保存异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExtendsService-createUserExtends", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ExecuteResult<UserInfoDTO> updateUserExtends(UserInfoDTO userInfoDTO) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExtendsService-updateUserExtends", JSONObject.toJSON(userInfoDTO));
        if (userInfoDTO == null || userInfoDTO.getExtendId() == null) {
            ExceptionUtils
                    .throwCoreException(GeneralMsgCode.NULL_POINT_ERROR, "用户扩展信息ID  不能为空", null, logger);
        }
        ExecuteResult<UserInfoDTO> res = new ExecuteResult<UserInfoDTO>();
        Integer count = userExtendsMybatisDAO.updateBySelect(userInfoDTO, -1);
        res.setResult(userInfoDTO);
        return res;
    }

    @Override
    public ExecuteResult<UserInfoDTO> modifyUserExtends(UserInfoDTO userInfoDTO, UserExtendsType userExtendsType) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExtendsService-modifyUserExtends", JSONObject.toJSON(userInfoDTO),
                userExtendsType);
        ExecuteResult<UserInfoDTO> result = new ExecuteResult<UserInfoDTO>();
        if (userInfoDTO == null || userInfoDTO.getUserId() == null) {
            ExceptionUtils
                    .throwCoreException(GeneralMsgCode.NULL_POINT_ERROR, "用户ID  不能为空", null, logger);
        }
        try {
            Integer type = userExtendsType != null ? userExtendsType.ordinal() : -1; //TODO 后期修改为枚举
            //修改时候 首先判断一下 用户当前审核状态 如果是已经审核通过 ,接下来是变更信息审核
            // 要修改根据中信账号状态
            ExecuteResult<UserInfoDTO> beforeUserInfo = userExtendsService.findUserInfo(userInfoDTO.getUserId());
            UserInfoDTO beforeUser = beforeUserInfo.getResult();
            if (beforeUser != null && (beforeUser.getUserDTO().getUserstatus() == UserEnums.UserStatus.BUYER_AUDIT.getCode() || beforeUser.getUserDTO().
                    getUserstatus() == UserEnums.UserStatus.SELLER_AUDIT.getCode()) && (!GlobalConstant.DEFUNCT_IND_TRUE_STRING.equals(userInfoDTO.getIsHaveBuyerPaysAccount()) ||
                    !GlobalConstant.DEFUNCT_IND_TRUE_STRING.equals(userInfoDTO.getIsHaveSellerCashAccount()))) {
                UserModifyInfoDTO modify = new UserModifyInfoDTO();
                modify.setApplicantName(beforeUser.getUserBusinessDTO().getCompanyName());
                modify.setApplicantUserid(userInfoDTO.getUserId());
                modify.setApplyStatus(0); //默认未审核
                modify.setCreateTime(new Date());
                modify.setUserType(beforeUser.getUserDTO().getUsertype());
                userModifyInfoMybatisDAO.insert(modify);
                if (modify.getId() != null) {
                    ExecuteResult<String> insertRes = userCenterOperaService.insertModifyDetailByUserInfo(beforeUser, userInfoDTO, modify);
                    if (GlobalConstant.SUCCESS_STR.equals(insertRes.getResult())) {
                        modifyStatusByType(userInfoDTO.getExtendId(), ComStatus.AUTH, userExtendsType);
                        result.setResultMessage("商家修改信息 提交审核成功");
                    } else {
                        result.setResultMessage("没有变更过的商家信息需要审核");
                    }
                }


            } else {
                Integer count = userExtendsMybatisDAO.updateBySelect(userInfoDTO, type);
                if (count > 0) {
                    result.setResultMessage("修改成功");
                } else {
                    result.setResultMessage("修改失败");
                }
            }

            result.setResult(userInfoDTO);
            logger.info("方法[{}]，出参：[{}]", "UserExtendsService-modifyUserExtends", JSONObject.toJSON(result));
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExtendsService-modifyUserExtends", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    //查询用户信息同时  查询用户审核日志
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public ExecuteResult<UserInfoDTO> findUserInfo(Long uid) {
        logger.info("方法[{}]，入参：[{}]", "UserExtendsService-findUserInfo", uid);
        ExecuteResult<UserInfoDTO> result = new ExecuteResult<UserInfoDTO>();
        //1 判断是否有子账号
        UserDTO user = userMybatisDAO.queryUserById(uid);
        if (user != null && user.getParentId() != null) {
            uid = user.getParentId();
        }

        UserInfoDTO userInfoDTO = userExtendsMybatisDAO.selectById(uid);
        if (userInfoDTO != null) {
            result.setResultMessage("查询成功");
            //2 查询 商家入驻审核日志
            Pager pager = new Pager();
            pager.setRows(100);
            UserAuditLogDTO userAuditLog = new UserAuditLogDTO();
            userAuditLog.setUserId(uid);
            userAuditLog.setAuditLogType("1"); //1 入驻审核
            userAuditLog.setUserType(userInfoDTO.getUserDTO().getUsertype());
            DataGrid<UserAuditLogDTO> logList = userAuditLogService.searchByConditionByPager(pager, userAuditLog);
            if (logList != null && logList.getRows() != null) {
                List<UserAuditLogDTO> auditLog = logList.getRows();
                if (auditLog.size() > 0) {
                    userInfoDTO.setUserAuditLogList(auditLog);
                }
            }
            //3查询 用户驳回备注
            queryUserModifyInfoRemak(userInfoDTO);


        } else {
            result.setResultMessage("查询失败");
        }
        result.setResult(userInfoDTO);
        logger.info("方法[{}]，出参：[{}]", "UserExtendsService-findUserInfo", JSONObject.toJSON(result));
        return result;
    }

    private void queryUserModifyInfoRemak(UserInfoDTO userInfoDTO) {
        //2 如果是用户审核状态 审核通过，查询扩展信息审核日志 查询驳回的审核日志 第一条
        if (userInfoDTO.getUserDTO().getUserstatus() == 4 || userInfoDTO.getUserDTO().getUserstatus() == 6) {
            UserModifyInfoDTO userModifyInfoDTO = new UserModifyInfoDTO();
            userModifyInfoDTO.setApplicantUserid(userInfoDTO.getUserDTO().getUid());
            userModifyInfoDTO.setApplyStatus(2); //查询驳回的
            userModifyInfoDTO.setUserType(userInfoDTO.getUserDTO().getUsertype());

            DataGrid<UserModifyInfoDTO> dataGrid = userModifyInfoService.getUserModifyInfoList(userModifyInfoDTO, new Pager());
            if (dataGrid != null && dataGrid.getRows() != null && dataGrid.getRows().size() > 0) {
               List<UserModifyInfoDTO> modifyList= dataGrid.getRows();
               for(UserModifyInfoDTO u:modifyList) {
                   if (u != null) {
                       if (GlobalConstant.USER_MODIFY_BUINESS.equals(u.getModifyType()) && userInfoDTO.getUserBusinessDTO().getBusinessStatus() == 0) {
                           if(StringUtils.isEmpty(userInfoDTO.getUserBusinessDTO().getAuditRemark())){
                               userInfoDTO.getUserBusinessDTO().setAuditRemark(u.getRemark());
                           }
                       }
                       if (GlobalConstant.USER_MODIFY_ACCOUNT.equals(u.getModifyType()) && userInfoDTO.getUserAccountDTO().getBankAccountStatus() == 0) {
                           if(StringUtils.isEmpty(userInfoDTO.getUserAccountDTO().getAuditRemark())) {
                               userInfoDTO.getUserAccountDTO().setAuditRemark(u.getRemark());
                           }
                       }
                       if (GlobalConstant.USER_MODIFY_ORG.equals(u.getModifyType()) && userInfoDTO.getUserOrganizationDTO().getOrganizationStatus() == 0) {
                           if(StringUtils.isEmpty(userInfoDTO.getUserOrganizationDTO().getAuditRemark())) {
                               userInfoDTO.getUserOrganizationDTO().setAuditRemark(u.getRemark());
                           }
                       }
                       if (GlobalConstant.USER_MODIFY_TAX.equals(u.getModifyType()) && userInfoDTO.getUserTaxDTO().getTaxStatus() == 0) {
                           if(StringUtils.isEmpty(userInfoDTO.getUserTaxDTO().getAuditRemark())) {
                               userInfoDTO.getUserTaxDTO().setAuditRemark(u.getRemark());
                           }
                       }
                       if (GlobalConstant.USER_MANAGE.equals(u.getModifyType()) && "0".equals(userInfoDTO.getUserManageDTO().getUserManageStatus()) ) {
                           if(StringUtils.isEmpty(userInfoDTO.getUserManageDTO().getAuditRemark())) {
                               userInfoDTO.getUserManageDTO().setAuditRemark(u.getRemark());
                           }
                       }
                   }
               }



            }

        }
    }

    @Override
    public ExecuteResult<String> modifyStatusByType(Long extendId, ComStatus comStatus, UserExtendsType userExtendsType) {
        logger.info("方法[{}]，入参：[{}][{}][{}]", "UserExtendsService-modifyStatusByType", extendId, comStatus,
                userExtendsType);
        ExecuteResult<String> result = new ExecuteResult<String>();
        try {
            Integer type = userExtendsType != null ? userExtendsType.ordinal() : -1; //TODO 后期修改为枚举
            Integer count = userExtendsMybatisDAO.updateStatusByType(extendId, comStatus.getCode(), type);
            if (count > 0) {
                result.setResultMessage("修改成功");
            } else {
                result.setResultMessage("修改失败");
            }
            logger.info("方法[{}]，出参：[{}]", "UserExtendsService-modifyStatusByType", JSONObject.toJSON(result));
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExtendsService-modifyStatusByType", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public DataGrid<UserInfoDTO> findUserInfoList(UserInfoDTO userInfoDTO, Pager pager) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExtendsService-findUserInfoList", JSONObject.toJSON(userInfoDTO), pager);
        DataGrid<UserInfoDTO> dataGrid = new DataGrid<UserInfoDTO>();
        long size = userExtendsMybatisDAO.selectCount(userInfoDTO);
        if (size > 0) {
            List<UserInfoDTO> listUserInfoDTO = userExtendsMybatisDAO.selectList(userInfoDTO, pager);
            dataGrid.setRows(listUserInfoDTO);
            dataGrid.setTotal(size);
        }
        logger.info("方法[{}]，出参：[{}]", "UserExtendsService-findUserInfoList", JSONObject.toJSON(dataGrid));
        return dataGrid;
    }

    private void verifySave(UserInfoDTO userInfoDTO) throws CommonCoreException {
        if (!(userInfoDTO != null && userInfoDTO.getUserId() != null && userInfoDTO.getUserType() != null)) {
            throw new CommonCoreException("必填项不可为空");
        }
    }
	@Override
	public ExecuteResult<UserInfoDTO> queryUserExtendsByname(String companyName) {
		 logger.info("方法[{}]，入参：[{}]", "UserExtendsService-queryUserExtendsByname", companyName);
	        ExecuteResult<UserInfoDTO> result = new ExecuteResult<UserInfoDTO>();
	        
	        UserInfoDTO infoDTO = userExtendsMybatisDAO.queryUserExtendsByname(companyName);

	        if (null!=infoDTO) {
	            result.setResultMessage("查询成功");

	        } else {
	            result.setResultMessage("查询失败");
	        }
	        result.setResult(infoDTO);
 	        logger.info("方法[{}]，出参：[{}]", "UserExtendsService-queryUserExtendsByname", JSONObject.toJSON(result));
	        return result;
	}


}
