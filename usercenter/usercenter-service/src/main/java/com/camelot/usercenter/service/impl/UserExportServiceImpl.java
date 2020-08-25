package com.camelot.usercenter.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.common.constant.GlobalConstant;
import com.camelot.common.util.DateUtil;
import com.camelot.common.util.ErrorUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.ExceptionUtils;
import com.camelot.openplatform.util.GeneralMsgCode;
import com.camelot.usercenter.dao.UserAuditMybatisDAO;
import com.camelot.usercenter.dao.UserExtendsMybatisDAO;
import com.camelot.usercenter.dao.UserMybatisDAO;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserAuditDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.dto.userInfo.UserAccountDTO;
import com.camelot.usercenter.dto.userInfo.UserBusinessDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserManageDTO;
import com.camelot.usercenter.dto.userInfo.UserOrganizationDTO;
import com.camelot.usercenter.dto.userInfo.UserTaxDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.ContractStatus;
import com.camelot.usercenter.enums.UserEnums.ContractType;
import com.camelot.usercenter.enums.UserEnums.UserStatus;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserAuditLogService;
import com.camelot.usercenter.service.UserContractService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;

@Service("userExportService")
public class UserExportServiceImpl implements UserExportService {
    private final static Logger logger = LoggerFactory.getLogger(UserExportServiceImpl.class);
    @Resource
    private UserMybatisDAO userMybatisDAO;

    @Resource
    private UserAuditMybatisDAO userAuditMybatisDAO;

    @Resource
    private UserExtendsMybatisDAO userExtendsMybatisDAO;

    @Resource
    private UserContractService userContractService;

    @Resource
    private UserExtendsService userExtendsService;

    @Resource
    private RedisDB redisDB;

    @Resource
    private UserAuditLogService userAuditLogService;

    /**
     * <p>Discription:[创建用户后 创建审核表]</p>
     * Created on 2015-3-12
     *
     * @param registerInfoDTO
     * @return
     * @author:[liuqingshan]
     */
    @Override
    public Long registerUser(RegisterInfoDTO registerInfoDTO) {
        try {
            String userId = userMybatisDAO.getUserIdFun();
            registerInfoDTO.setUid(Long.parseLong(userId));
            userMybatisDAO.registerUser(registerInfoDTO);

            //3 创建用户扩展信息
            UserInfoDTO userInfoDTO = new UserInfoDTO();
            userInfoDTO.setUserId(registerInfoDTO.getUid());
            userInfoDTO.setUserType(UserType.BUYER);

            UserBusinessDTO userBusinessDTO = new UserBusinessDTO();
            userBusinessDTO.setBusinessStatus(2);
            userInfoDTO.setUserBusinessDTO(userBusinessDTO);

            UserOrganizationDTO userOrganizationDTO = new UserOrganizationDTO();
            userOrganizationDTO.setOrganizationStatus(2);
            userInfoDTO.setUserOrganizationDTO(userOrganizationDTO);

            UserTaxDTO userTaxDTO = new UserTaxDTO();
            userTaxDTO.setTaxStatus(2);
            userInfoDTO.setUserTaxDTO(userTaxDTO);

            UserAccountDTO userAccountDTO = new UserAccountDTO();
            userAccountDTO.setBankAccountStatus(2);
            userInfoDTO.setUserAccountDTO(userAccountDTO);

            UserCiticDTO userCiticDTO = new UserCiticDTO();
            userCiticDTO.setAccountState(1);
            userInfoDTO.setUserCiticDTO(userCiticDTO);

            UserManageDTO userManageDTO = new UserManageDTO();
            userManageDTO.setUserManageStatus("2");
            userInfoDTO.setUserManageDTO(userManageDTO);
            userExtendsService.createUserExtends(userInfoDTO);

            if (registerInfoDTO.getUid() != null) {
                UserAuditDTO userAuditDto = new UserAuditDTO();
                userAuditDto.setContextId(registerInfoDTO.getUid());
                userAuditDto.setResult(UserStatus.BUYER_AUDIT.getCode());
                userAuditMybatisDAO.insert(userAuditDto);
            }

        } catch (Exception e) {
            logger.error("error:：" + e.getMessage());
            throw new RuntimeException(e);
        }
        return registerInfoDTO.getUid();
    }

    @Override
    public ExecuteResult<String> modifyUserPw(String username, String newPassword, String oldPassword) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
            int count = userMybatisDAO.modifyUserPw(username, newPassword, oldPassword);
            if (count > 0) {
                executeResult.setResultMessage("修改成功");
            } else {
                executeResult.addErrorMessage("旧密码错误");
            }
        } catch (Exception e) {
            logger.error("error:：" + e.getMessage());
            executeResult.getErrorMessages().add(e.getMessage());
            throw new RuntimeException(e);
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<LoginResDTO> login(String username, String password, String key) {
        ExecuteResult<LoginResDTO> res = new ExecuteResult<LoginResDTO>();
        LoginResDTO loginResDTO = null;

        try {
            RegisterDTO registerDTO = null;
            if (username != null && password != null) {
                registerDTO = userMybatisDAO.login(username, password);
            }
            if (registerDTO != null) {
                if (null != registerDTO.getDeleted() && registerDTO.getDeleted() == 1) {
                    loginResDTO = null;
                    res.addErrorMessage("账号已被冻结");
                } else {
                    loginResDTO = new LoginResDTO();
                    loginResDTO.setNickname(registerDTO.getLoginname());
                    redisDB.addObject(key, registerDTO, 60 * 30);
                    loginResDTO.setToken(key);
                    loginResDTO.setUid(registerDTO.getUid());
                    loginResDTO.setUstatus(registerDTO.getuStatus());
                    loginResDTO.setPlatformId(registerDTO.getPlatformId());
                }

            } else {
                res.addErrorMessage("用户名或密码不正确");
            }
            res.setResult(loginResDTO);
        } catch (Exception e) {
            logger.error("error:：" + e.getMessage());
            throw new RuntimeException(e);
        }
        return res;
    }

    @Override
    public boolean verifyLoginName(String loginname) {
        try {
        	UserDTO userDTO = new UserDTO();
        	userDTO.setUname(loginname);
        	UserDTO resultDTO = userMybatisDAO.queryUserByCondtion(userDTO);
            if (resultDTO != null && resultDTO.getUname() != null) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            logger.error("error:：" + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifyRegisterName(String registerName) {
        return userMybatisDAO.verifyRegisterName(registerName) > 0 ? true : false;

    }

    @Override
    public boolean verifyEmailOrMobile(String attr) {
        return userMybatisDAO.verifyEmail(attr) + userMybatisDAO.verifyMobile(attr) > 0 ? true : false;

    }

    @Override
    public boolean verifyEmail(String email) {
        return userMybatisDAO.verifyEmail(email)  > 0 ? true : false;
    }

    @Override
    public boolean verifyMobile(String mobile) {
        return userMybatisDAO.verifyMobile(mobile)  > 0 ? true : false;
    }

    @Override
    public ExecuteResult<String> modifyUserInfoAndAuditStatus(UserDTO userDTO) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        UserDTO queryUto = userMybatisDAO.queryUserById(userDTO.getUid());
        queryUto.setAuditStatus(userDTO.getAuditStatus());
        queryUto.setUserstatus(userDTO.getUserstatus());
        queryUto.setUpdated(new Date());
        if (userDTO.getNickname() != null) {
            queryUto.setNickname(userDTO.getNickname());
        }

        if (queryUto.getAuditStatus() != null) {
        	//李伟龙修改=====start=====
        	//如果是 认证买家用户
          if (3 == queryUto.getUserstatus() || 4 == queryUto.getUserstatus()) {
              //买家  驳回 设置为 买家待审核状态
              if (0 == queryUto.getAuditStatus()) {
                  queryUto.setUserstatus(UserStatus.BUYER_UNAUDIT.getCode());
                  //买家 未审核 设置为买家待审核
              } else if (1 == queryUto.getAuditStatus()) {
                  queryUto.setUserstatus(UserStatus.BUYER_UNAUDIT.getCode());
                  //买家审核通过 设置为 买家审核通过
              } else if (2 == queryUto.getAuditStatus()) {
                  queryUto.setUserstatus(UserStatus.BUYER_AUDIT.getCode());
              } else {
                  queryUto.setUserstatus(UserStatus.BUYER_UNAUDIT.getCode());
              }
              //如果是 认证卖家用户
          } else if (5 == queryUto.getUserstatus() || 6 == queryUto.getUserstatus()) {
              //卖家  驳回 设置为 卖家待审核状态
              if (0 == queryUto.getAuditStatus()) {
                  queryUto.setUserstatus(UserStatus.SELLER_UNAUDIT.getCode());
                  //卖家  未审核 设置为卖家待审核
              } else if (1 == queryUto.getAuditStatus()) {
                  queryUto.setUserstatus(UserStatus.SELLER_UNAUDIT.getCode());
                  //卖家  审核通过 设置为 卖家审核通过
              } else if (2 == queryUto.getAuditStatus()) {
                  queryUto.setUserstatus(UserStatus.SELLER_AUDIT.getCode());
              } else {
                  queryUto.setUserstatus(UserStatus.SELLER_UNAUDIT.getCode());
              }
            //李伟龙修改=====end=====
        	
//            //如果是 买家用户
//            if (2 == queryUto.getUsertype()) {
//                //买家  驳回 设置为 买家待审核状态
//                if (0 == queryUto.getAuditStatus()) {
//                    queryUto.setUserstatus(UserStatus.BUYER_UNAUDIT.getCode());
//                    //买家 未审核 设置为买家待审核
//                } else if (1 == queryUto.getAuditStatus()) {
//                    queryUto.setUserstatus(UserStatus.BUYER_UNAUDIT.getCode());
//                    //买家审核通过 设置为 买家审核通过
//                } else if (2 == queryUto.getAuditStatus()) {
//                    queryUto.setUserstatus(UserStatus.BUYER_AUDIT.getCode());
//                } else {
//                    queryUto.setUserstatus(UserStatus.BUYER_UNAUDIT.getCode());
//                }
//                //如果是 卖家用户
//            } else if (3 == queryUto.getUsertype()) {
//                //卖家  驳回 设置为 卖家待审核状态
//                if (0 == queryUto.getAuditStatus()) {
//                    queryUto.setUserstatus(UserStatus.SELLER_UNAUDIT.getCode());
//                    //卖家  未审核 设置为卖家待审核
//                } else if (1 == queryUto.getAuditStatus()) {
//                    queryUto.setUserstatus(UserStatus.SELLER_UNAUDIT.getCode());
//                    //卖家  审核通过 设置为 卖家审核通过
//                } else if (2 == queryUto.getAuditStatus()) {
//                    queryUto.setUserstatus(UserStatus.SELLER_AUDIT.getCode());
//                } else {
//                    queryUto.setUserstatus(UserStatus.SELLER_UNAUDIT.getCode());
//                }
            }
            UserAuditDTO userAuditDto = new UserAuditDTO();
            userAuditDto.setAuditorId(null);
            userAuditDto.setResult(userDTO.getAuditStatus());
            userAuditDto.setContextId(userDTO.getUid());

            Integer auditRes = userAuditMybatisDAO.update(userAuditDto);
            Integer userRes = userMybatisDAO.modifyUserInfo(queryUto);
            if (auditRes > 0 && userRes > 0) {
                result.setResult("1");
            } else {
                result.setResult("0");
            }
        }
        return result;
    }

    @Override
    public boolean modifyUserInfo(UserDTO userDTO) {
    	boolean flag = false;
        flag = userMybatisDAO.modifyUserInfo(userDTO) > 0 ? true : false;
		if (userDTO.getUidList() != null && userDTO.getUidList().size() > 0 && flag) {
			flag = userMybatisDAO.modifyChildUserInfo(userDTO) > 0 ? true : false;
		}
        return flag;
    }

	@Override
	public boolean modifyUserInfoByMobile(UserDTO userDTO) {
		return userMybatisDAO.modifyUserInfoByMobile(userDTO) > 0 ? true : false;
	}

	@Override
    public UserDTO queryUserById(long uid) {
        return userMybatisDAO.queryUserById(uid);
    }

    @Override
    public RegisterDTO getUserByRedis(String key) {
        return (RegisterDTO) redisDB.getObject(key);
    }

    @Override
    public void saveVerifyCodeToRedis(String key, String value) {
        redisDB.setAndExpire(key, value, 10000);
    }

    @Override
    public String getValueByRedis(String key) {
        return redisDB.get(key);
    }

    @Override
    public ExecuteResult<String> modifyUserPwdByEmail(String email, String newPassword) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
            int count = userMybatisDAO.modifyUserPwdByEmail(email, newPassword);
            if (count > 0) {
                executeResult.setResultMessage("操作成功");
            } else {
                executeResult.setResultMessage("操作失败");
            }
        } catch (Exception e) {
            logger.error("error:：" + e.getMessage());
            executeResult.getErrorMessages().add(e.getMessage());
            executeResult.setResultMessage("操作失败");
            throw new RuntimeException(e);
        }

        return executeResult;
    }

	@SuppressWarnings("rawtypes")
    @Override
    public UserDTO findUserByUserNameOrEmailOrPhone(UserDTO userDTO) {
        logger.info("方法[{}]，入参：[{}][{}][{}]", "UserExportService-findUserByUserNameOrEmailOrPhone");
        UserDTO newUserDTO = new UserDTO();
		if (userDTO.getUname() != null) {
			newUserDTO = userMybatisDAO.selectUserByUname(userDTO);
		} else if (userDTO.getUserEmail() != null) {
			newUserDTO = userMybatisDAO.selectUserByUserEmail(userDTO);
		} else if (userDTO.getUmobile() != null) {
			newUserDTO = userMybatisDAO.selectUserByUmobile(userDTO);
		}
        logger.info("方法[{}]，出参：[{}]", "UserExportService-findUserByUserNameOrEmailOrPhone", JSONObject.toJSON(newUserDTO));
        return newUserDTO;
    }
	
    @SuppressWarnings("rawtypes")
    @Override
    public DataGrid<UserDTO> findUserListByCondition(UserDTO userDTO, UserType userType, Pager pager) {
        logger.info("方法[{}]，入参：[{}][{}][{}]", "UserExportService-findUserListByCondition", userType, pager);
        DataGrid<UserDTO> dataGrid = new DataGrid<UserDTO>();

        if (userDTO != null) {
            if (userType != null) {
                userDTO.setUsertype(userType.getCode());
            }
            if (StringUtils.isNotBlank(userDTO.getAuditorEnd())) {
                userDTO.setAuditorEnd(DateUtil.addDateStr(userDTO.getAuditorEnd(), 1, null));
            }
            if (StringUtils.isNotBlank(userDTO.getCreateTimeEnd())) {
                userDTO.setCreateTimeEnd(DateUtil.addDateStr(userDTO.getCreateTimeEnd(), 1, null));
            }

        }
        long size = userMybatisDAO.selectCount(userDTO);
        if (size > 0) {
            List<UserDTO> listUserDTO = userMybatisDAO.selectList(userDTO, pager);
            dataGrid.setRows(listUserDTO);
            dataGrid.setTotal(size);
        }
        logger.info("方法[{}]，出参：[{}]", "UserExportService-findUserListByCondition", JSONObject.toJSON(dataGrid));
        return dataGrid;
    }

    @Override
    public ExecuteResult<List<UserDTO>> findUserListByUserIds(List<String> idList) {
        logger.info("方法[{}]，入参：[{}][{}][{}]", "UserExportService-findUserListByUserIds", idList);
        ExecuteResult<List<UserDTO>> resultList = new ExecuteResult<List<UserDTO>>();
        if (idList != null && idList.size() > 0) {
            List<UserDTO> listUserDTO = userMybatisDAO.findUserListByUserIds(idList);
            resultList.setResult(listUserDTO);
        } else {
            resultList.addErrorMessage("IDLIST 传入为空");
        }

        return resultList;
    }

    @Override
    public ExecuteResult<String> modifyUserPwdByUsername(String username, String newPassword) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        int count = userMybatisDAO.modifyUserPwdByUsername(username, newPassword);
        if (count > 0) {
            executeResult.setResultMessage("操作成功");
        } else {
            executeResult.setResultMessage("操作失败");
        }
        return executeResult;
    }

    @Override
    public RegisterDTO getUserInfoByUsername(String username) {
        RegisterDTO registerDTO = null;
        if (username != null) {
            registerDTO = userMybatisDAO.findUserByLonginName(username);
        }
        return registerDTO;
    }

    @Override
    public ExecuteResult<String> modifyPaypwdById(long uid, String paypwd, String oldpwd, String securityLevel) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
            int count = userMybatisDAO.modifyPaypwdById(uid, paypwd, oldpwd, securityLevel);
            if (count > 0) {
                executeResult.setResultMessage("操作成功");
            } else {
                executeResult.setResultMessage("操作失败");
                executeResult.addErrorMessage("旧密码错误");
            }
        } catch (Exception e) {
            executeResult.getErrorMessages().add(e.getMessage());
            throw new RuntimeException(e);
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<String> modifyPayStatusByUId(long uid, ComStatus comStatus) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExportService-modifyPayStatusByUId", uid, comStatus);
        ExecuteResult<String> result = new ExecuteResult<String>();
        try {
            result = modifyStatusByUId(uid, comStatus, "payStatus");
            logger.info("方法[{}]，出参：[{}]", "UserExportService-modifyPayStatusByUId", JSONObject.toJSON(result));
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExportService-modifyPayStatusByUId", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ExecuteResult<String> modifyAuditStatusByUId(long uid, ComStatus comStatus) {
        logger.info("方法[{}]，入参：[{}][{}]", "UserExportService-modifyAuditStatusByUId", uid, comStatus);
        ExecuteResult<String> result = new ExecuteResult<String>();
        try {
            result = modifyStatusByUId(uid, comStatus, "auditStatus");
            logger.info("方法[{}]，出参：[{}]", "UserExportService-modifyAuditStatusByUId", JSONObject.toJSON(result));
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExportService-modifyAuditStatusByUId", e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public ExecuteResult<UserDTO> modifyUserAuditStatusByUserIdAndAuditId(UserDTO userDto, String auditUserId,
                                                                          String remark) throws CommonCoreException {
        ExecuteResult<UserDTO> result = new ExecuteResult<UserDTO>();
        try {

            if (userDto.getUserstatus() == null || userDto.getAuditStatus() == null || userDto.getUsertype() == null) {
                ExceptionUtils
                        .throwCoreException(GeneralMsgCode.NULL_POINT_ERROR, "用户ID 用户状态 用户审核状态 用户类型 不能为空", null, logger);
            } else {
                UserDTO vlaUser= userMybatisDAO.queryUserById(userDto.getUid());
                if(vlaUser!=null&&vlaUser.getParentId()!=null&&userDto.getUserstatus()==6){
                    result.addErrorMessage("子帐号不能申请卖家权限");
                    throw new RuntimeException("子帐号不能申请卖家权限");
                }

                //1 先修改用户状态
                userMybatisDAO.modifyUserInfo(userDto);
                //修改子账号状态
                List<Long> ids = userMybatisDAO.queryUserIds(userDto.getUid());
                userDto.setUidList(ids);
                UserDTO childUser = new UserDTO();
                childUser.setUidList(ids);
                childUser.setUserstatus(userDto.getUserstatus());
                userMybatisDAO.modifyChildUserInfo(childUser);

                UserDTO reDto = userMybatisDAO.queryUserById(userDto.getUid());
                result.setResult(reDto);
                //2   修改 审核表状态
                UserAuditDTO userAuditDto = new UserAuditDTO();
                userAuditDto.setContextId(userDto.getUid());//插入USERID
                userAuditDto.setResult(userDto.getAuditStatus());
                userAuditDto.setAuditorId(auditUserId);
                userAuditDto.setRemarks(remark);
                userAuditMybatisDAO.update(userAuditDto);

                //3修改 用户扩展信息状态
                ExecuteResult<UserInfoDTO> uInfoRes = userExtendsService.findUserInfo(userDto.getUid());
                //修改用户扩展信息状态
                userExtendsMybatisDAO.updateAllStatusByUserExtendId(uInfoRes.getResult().getExtendId(),
                        userDto.getAuditStatus());
                //如果 买家已经审核通过 卖家重新审核，将账号 中信账号状态改为，卖家待审核
                if (uInfoRes.getResult().getUserCiticDTO() != null && 2 ==
                        uInfoRes.getResult().getUserCiticDTO().getAccountState() && 3 ==
                        uInfoRes.getResult().getUserDTO().getUsertype()) {

                    UserInfoDTO updateObj = new UserInfoDTO();
                    updateObj.setUserId(userDto.getUid());
                    UserCiticDTO citi = new UserCiticDTO();
                    citi.setAccountState(3); //卖家待审核
                    updateObj.setUserCiticDTO(citi);
                    userExtendsMybatisDAO.updateSelective(updateObj);
                }

                //4如果是 卖家 创建 合同
                if (userDto.getUsertype() == 3 && userDto.getAuditStatus() == 2 && userDto.getUserstatus() == 6) {


                    UserContractDTO userContractDTO = new UserContractDTO();
                    userContractDTO.setCreatorId(userDto.getUid());
                    userContractDTO.setContractType(ContractType.RECRUITMENT.getCode());
                    userContractDTO.setCompanyName(reDto.getCompanyName());
                    userContractDTO.setShopId(reDto.getShopId());
                    userContractDTO.setContractStatus(ContractStatus.UPLOAD.getCode());
                    userContractDTO.setCreatedTime(new Date());

                    userContractService.createOrUpdateContract(userContractDTO);
                } else if (userDto.getUsertype() == 3 && userDto.getAuditStatus() == 1 && userDto.getUserstatus() == 5) {
                    //如果商家待审核状态 判断有没有合同 删除掉
                    UserContractDTO queryDto = new UserContractDTO();
                    queryDto.setCreatorId(userDto.getUid());
                    ExecuteResult<UserContractDTO> resContract = userContractService.findUserContractByCondition(queryDto);
                    if (resContract != null) {
                        userContractService.deleteUserContractById(resContract.getResult().getId());
                    }
                }
                // 5 创建用户审核日志
                //查询一下 审核信息最新状态
                userAuditDto = userAuditMybatisDAO.getUserAuditByUserId(userDto.getUid());

                UserAuditLogDTO userAuditLog = new UserAuditLogDTO();
                if (userAuditDto != null) {
                    userAuditLog.setAuditId(userAuditDto.getId());
                    userAuditLog.setAuditDate(new Date());
                    userAuditLog.setAuditLogType("1");
                    userAuditLog.setAuditStatus(userDto.getAuditStatus());
                    userAuditLog.setRemark(remark);
                    userAuditLog.setUserId(userDto.getUid());
                    userAuditLog.setUserType(userDto.getUsertype());
                    ExecuteResult<UserAuditLogDTO> res = userAuditLogService.saveUserAuditLogDTO(userAuditLog);
                }

            }
        } catch (Exception t) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("修改用户审核异常", t.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExportService-modifyAuditStatusByUId", t.getMessage());
            ExceptionUtils.throwCoreException("用户审核失败", "", t, logger);
        } catch (Throwable t) {
            ExceptionUtils.throwThrowable(t, logger);
        }


        return result;
    }

    @Override
    public ExecuteResult<UserDTO> quickAuditUser(UserDTO userDto, String auditUserId, String remark) throws CommonCoreException {
        ExecuteResult<UserDTO> result = new ExecuteResult<UserDTO>();
        try {
            if (userDto.getUserstatus() == null || userDto.getAuditStatus() == null || userDto.getUsertype() == null) {
                ExceptionUtils
                        .throwCoreException(GeneralMsgCode.NULL_POINT_ERROR, "用户ID 用户状态 用户审核状态 用户类型 不能为空", null, logger);
            } else {
                //1 先修改用户状态
                userMybatisDAO.modifyUserInfo(userDto);

                UserDTO reDto = userMybatisDAO.queryUserById(userDto.getUid());
                result.setResult(reDto);
                //2   修改 审核表状态
                UserAuditDTO userAuditDto = new UserAuditDTO();
                userAuditDto.setContextId(userDto.getUid());//插入USERID
                userAuditDto.setResult(userDto.getAuditStatus());
                userAuditDto.setAuditorId(auditUserId);
                userAuditDto.setRemarks(remark);
                userAuditMybatisDAO.update(userAuditDto);

                //3 创建用户扩展信息
                UserInfoDTO userInfoDTO = new UserInfoDTO();
                userInfoDTO.setUserId(userDto.getUid());
                userInfoDTO.setUserType(UserType.BUYER);

                UserBusinessDTO userBusinessDTO = new UserBusinessDTO();
                userBusinessDTO.setBusinessStatus(2);
                userBusinessDTO.setCompanyName("科印-快捷认证" + userDto.getUid());
                userInfoDTO.setUserBusinessDTO(userBusinessDTO);

                UserOrganizationDTO userOrganizationDTO = new UserOrganizationDTO();
                userOrganizationDTO.setOrganizationStatus(2);
                userInfoDTO.setUserOrganizationDTO(userOrganizationDTO);

                UserTaxDTO userTaxDTO = new UserTaxDTO();
                userTaxDTO.setTaxStatus(2);
                userInfoDTO.setUserTaxDTO(userTaxDTO);

                UserAccountDTO userAccountDTO = new UserAccountDTO();
                userAccountDTO.setBankAccountStatus(2);
                userInfoDTO.setUserAccountDTO(userAccountDTO);

                UserCiticDTO userCiticDTO = new UserCiticDTO();
                userCiticDTO.setAccountState(1);
                userInfoDTO.setUserCiticDTO(userCiticDTO);

                UserManageDTO userManageDTO = new UserManageDTO();
                userManageDTO.setUserManageStatus("2");
                userInfoDTO.setUserManageDTO(userManageDTO);
                userExtendsService.createUserExtends(userInfoDTO);


                //查询一下 审核信息最新状态
                userAuditDto = userAuditMybatisDAO.getUserAuditByUserId(userDto.getUid());

                UserAuditLogDTO userAuditLog = new UserAuditLogDTO();
                userAuditLog.setAuditId(userAuditDto.getId());
                userAuditLog.setAuditDate(new Date());
                userAuditLog.setAuditLogType("1");
                userAuditLog.setAuditStatus(userDto.getAuditStatus());
                userAuditLog.setRemark(remark);
                userAuditLog.setUserId(userDto.getUid());
                userAuditLog.setUserType(userDto.getUsertype());
                userAuditLogService.saveUserAuditLogDTO(userAuditLog);

                result.setResult(userDto);
            }
        } catch (Exception e) {
            result.getErrorMessages().add(ErrorUtil.buildErrorMsg("用户快速认证异常", e.getMessage()));
            logger.info("方法[{}]，异常：[{}]", "UserExportService-quickAuditUser", e.getMessage());
            ExceptionUtils.throwCoreException("用户快速认证异常", "", e, logger);
        }
        return result;
    }

    /**
     * 修改状态统一类
     *
     * @param uid       - 用户ID
     * @param comStatus - 状态
     * @param type      - 修改类型
     * @return
     */
    public ExecuteResult<String> modifyStatusByUId(Long uid, ComStatus comStatus, String type) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        UserDTO userDTO = new UserDTO();
        userDTO.setUid(uid);
        if (type.equals("payStatus")) {
            userDTO.setPaymentStatus(comStatus.getCode());
        } else if (type.equals("auditStatus")) {
            userDTO.setUserstatus(comStatus.getCode());
        }
        Integer count = userMybatisDAO.modifyUserInfo(userDTO);
        if (count > 0) {
            result.setResultMessage("修改成功");
        } else {
            result.setResultMessage("修改失败");
        }
        return result;
    }

    @Override
    public void removeValueByRedis(String key) {
        redisDB.del(key);
    }

    @Override
    public void updateUserInfoToRedis(String token, RegisterDTO registerDTO) {
        if (token != null && registerDTO != null) {
            redisDB.addObject(token, registerDTO, 604800000);
        }
    }

    @Override
    public ExecuteResult<String> validatePayPassword(long uid, String paypwd) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
            Long count = userMybatisDAO.validatePayPassword(uid, paypwd);
            if (count > 0) {
                executeResult.setResult("1");
                executeResult.setResultMessage("校验成功");
            } else {
                executeResult.setResult("0");
                executeResult.setResultMessage("校验失败");

            }
        } catch (Exception e) {
            executeResult.setResult("0");
            executeResult.getErrorMessages().add(e.getMessage());
            throw new RuntimeException(e);
        }
        return executeResult;
    }
    
    @Override
    public ExecuteResult<String> validateLoginPassword(long uid, String loginpwd) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
            Long count = userMybatisDAO.validateLoginPassword(uid, loginpwd);
            if (count > 0) {
                executeResult.setResult("1");
                executeResult.setResultMessage("校验成功");
            } else {
                executeResult.setResult("0");
                executeResult.setResultMessage("校验失败");

            }
        } catch (Exception e) {
            executeResult.setResult("0");
            executeResult.getErrorMessages().add(e.getMessage());
            throw new RuntimeException(e);
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<String> resetUserPassword(Long userId, String resetPassword) {
        ExecuteResult<String> executeResult = new ExecuteResult<String>();
        int count = userMybatisDAO.resetUserPassword(userId, resetPassword);
        if (count > 0) {
            executeResult.setResultMessage("操作成功");
        } else {
            executeResult.setResultMessage("操作失败");
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<String> frozenUser(String userId, CommonEnums.FrozenStatus status) {
        ExecuteResult<String> result = new ExecuteResult<String>();
        if (StringUtils.isNotBlank(userId) && status != null) {

            UserDTO userDTO = new UserDTO();
            userDTO.setUid(Long.parseLong(userId));
            userDTO.setDeleted(status.getCode());
            Integer count = userMybatisDAO.modifyUserInfo(userDTO);
            if (count > 0) {
                result.setResult("账号冻结成功");
            } else {
                result.setResultMessage("账号冻结失败");
                result.addErrorMessage("账号冻结失败");
            }
            result.setResult("SUCCESS");

        }
        return result;
    }


    public ExecuteResult<UserDTO> changeParentAndSonAccount(UserDTO sonUser) {
        ExecuteResult<UserDTO> res = new ExecuteResult<UserDTO>();
        try {
            if (sonUser != null && sonUser.getUid() != null) {
                UserDTO sonUserRes = queryUserById(sonUser.getUid());
                if (sonUserRes != null && sonUserRes.getParentId() != null) {
                    UserDTO parentRes = queryUserById(sonUser.getParentId());

                    parentRes.setUname(sonUserRes.getUname());
                    parentRes.setPassword(sonUserRes.getPassword());
                    parentRes.setUserEmail(sonUserRes.getUserEmail());
                    parentRes.setUmobile(sonUserRes.getUmobile());


                    sonUserRes.setUname(parentRes.getUname());
                    sonUserRes.setPassword(parentRes.getPassword());
                    sonUserRes.setUserEmail(parentRes.getUserEmail());
                    sonUserRes.setUmobile(parentRes.getUmobile());

                    modifyUserInfo(parentRes);
                    modifyUserInfo(sonUserRes);

                    res.setResult(parentRes);
                } else {
                    res.addErrorMessage("用户父账号ID为空");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("子账号申请更改父账号权限失败" + e.getMessage());
        }


        return res;
    }


    @Override
    public DataGrid<UserDTO> queryUserListByCondition(UserDTO userDTO, UserType userType, List<String> idList, Pager pager) {
        logger.info("方法[{}]，入参：[{}][{}][{}]", "UserExportService-queryUserListByCondition", userType, pager);
        DataGrid<UserDTO> dataGrid = new DataGrid<UserDTO>();

        if (userDTO != null) {
            if (userType != null) {
                userDTO.setUsertype(userType.getCode());
            }
            if (idList != null && idList.size() > 0) {
                userDTO.setIdList(idList);
            }
            if (StringUtils.isNotBlank(userDTO.getAuditorEnd())) {
                userDTO.setAuditorEnd(DateUtil.addDateStr(userDTO.getAuditorEnd(), 1, null));
            }

        }
        long size = userMybatisDAO.selectCount(userDTO);
        if (size > 0) {
            List<UserDTO> listUserDTO = userMybatisDAO.selectList(userDTO, pager);
            dataGrid.setRows(listUserDTO);
            dataGrid.setTotal(size);
        }
        logger.info("方法[{}]，出参：[{}]", "UserExportService-findUserListByCondition", JSONObject.toJSON(dataGrid));
        return dataGrid;
    }

	@Override
	public ExecuteResult<List<Long>> queryUserIds(Long uid) {
		List<Long> ids = userMybatisDAO.queryUserIds(uid);
		ExecuteResult<List<Long>> er = new ExecuteResult<List<Long>>();
		if(null!=ids&&ids.size()>0){
			er.setResult(ids);
		}else{
			List<String> errMsg = new ArrayList<String>();
			errMsg.add("不存在用户");
			er.setErrorMessages(errMsg);
		}
		return er;
	}

	
	//根据父账号查询用户信息
	@Override
	public UserDTO queryUserByfId(long uid) {
		// TODO Auto-generated method stub
		return userMybatisDAO.queryUserByfId(uid);
	}

	  @SuppressWarnings("rawtypes")
	    @Override
	    public DataGrid<UserDTO> findUserListPid(UserDTO userDTO, UserType userType, Pager pager) {
	        logger.info("方法[{}]，入参：[{}][{}][{}]", "UserExportService-findUserListByCondition", userType, pager);
	        DataGrid<UserDTO> dataGrid = new DataGrid<UserDTO>();

	        if (userDTO != null) {
	            if (userType != null) {
	                userDTO.setUsertype(userType.getCode());
	            }
	            if (StringUtils.isNotBlank(userDTO.getAuditorEnd())) {
	                userDTO.setAuditorEnd(DateUtil.addDateStr(userDTO.getAuditorEnd(), 1, null));
	            }
	            if (StringUtils.isNotBlank(userDTO.getCreateTimeEnd())) {
	                userDTO.setCreateTimeEnd(DateUtil.addDateStr(userDTO.getCreateTimeEnd(), 1, null));
	            }

	        }
	        long size = userMybatisDAO.selectCountPid(userDTO);
	        if (size > 0) {
	            List<UserDTO> listUserDTO = userMybatisDAO.selectparentId(userDTO, pager);
	            dataGrid.setRows(listUserDTO);
	            dataGrid.setTotal(size);
	        }
	        logger.info("方法[{}]，出参：[{}]", "UserExportService-findUserListByCondition", JSONObject.toJSON(dataGrid));
	        return dataGrid;
	    }

	@Override
	public ExecuteResult<String> validateExistPaymentCode(Long uid) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
            Long count = userMybatisDAO.validateExistPaymentCode(uid);
            if (count > 0) {
                executeResult.setResult("1");
                executeResult.setResultMessage("已设置支付密码");
            } else {
                executeResult.setResult("0");
                executeResult.setResultMessage("未设置支付密码");
            }
        } catch (Exception e) {
            executeResult.setResult("0");
            executeResult.getErrorMessages().add(e.getMessage());
            throw new RuntimeException(e);
        }
        return executeResult;
	}
}
