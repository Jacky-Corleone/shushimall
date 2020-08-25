package com.camelot.ecm;

import com.camelot.common.enums.CiticEnums;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sevelli on 15-3-21.
 */
@Service("userService")
public class UserService {
    @Resource
    private CiticExportService citicExportService;
    @Resource
    private UserExportService userExportService;
    @Resource
    private UserExtendsService userExtendsService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户认证
     * @param userDTO
     * @param approveId
     * @return
     */
    public Map approveUser(UserDTO userDTO,String approveId){
        Integer auditStatus = userDTO.getAuditStatus();
        Map map = new HashMap();
        if (0==auditStatus){//驳回
            ExecuteResult<UserDTO> result = userExportService.modifyUserAuditStatusByUserIdAndAuditId(userDTO,approveId,userDTO.getAuditRemark());
            if (result.isSuccess()) {
                map.put("success",true);
                map.put("msg","驳回成功");
            }else{
                map.put("success",false);
                map.put("msg",result.getErrorMessages());
            }
        }else{//通过
            UserDTO originUser = userExportService.queryUserById(userDTO.getUid());

            ExecuteResult<UserDTO> result = userExportService.modifyUserAuditStatusByUserIdAndAuditId(userDTO,approveId,userDTO.getAuditRemark());
            UserDTO returnUser = result.getResult();
            ExecuteResult<UserInfoDTO> resultT = userExtendsService.findUserInfo(userDTO.getUid());
            UserInfoDTO reuserInfoDTO = resultT.getResult();
            // 用户审核信息处理，创建中信帐号

            if(result.isSuccess()){/*

                AccountInfoDto accountInfoDto = new AccountInfoDto();
                accountInfoDto.setUserId(returnUser.getUid());
                accountInfoDto.setSubAccNm(returnUser.getCompanyName());
                accountInfoDto.setBindingAccNm(reuserInfoDTO.getUserAccountDTO().getBankAccountName());
                accountInfoDto.setBankName(reuserInfoDTO.getUserAccountDTO().getBankName());
                String account = reuserInfoDTO.getUserAccountDTO().getBankAccount();
                accountInfoDto.setBindingAccNo(account);
                // 是否是本行
                accountInfoDto.setSameBank(reuserInfoDTO.getUserAccountDTO().getIsCiticBank()==null?1:reuserInfoDTO.getUserAccountDTO().getIsCiticBank());//1本行，0他行
                try {
                    if (returnUser.getUsertype()== CiticEnums.UserType.Buyer.getCode()){//买家
                        accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyFinancing);
                        ExecuteResult<AccountInfoDto> result1 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                        accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyPay);
                        ExecuteResult<AccountInfoDto> result2 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                        if(result1.isSuccess()&&result2.isSuccess()){
                            logger.info("创建中信账户成功:"+result1.getResult().getSubAccNo()+"|"+result2.getResult().getSubAccNo());
                            UserCiticDTO userCiticDTO = new UserCiticDTO();
                            userCiticDTO.setBuyerFinancingAccount(result1.getResult() == null ? "" : result1.getResult().getSubAccNo());
                            userCiticDTO.setBuyerPaysAccount(result2.getResult() == null ? "" : result2.getResult().getSubAccNo());
                            reuserInfoDTO.setUserCiticDTO(userCiticDTO);
                            reuserInfoDTO.setIsHaveBuyerPaysAccount("1");
                            reuserInfoDTO.setIsHaveSellerCashAccount("1");
                            userExtendsService.modifyUserExtends(reuserInfoDTO,UserEnums.UserExtendsType.CTIBANK);
                            map.put("success",true);
                            map.put("msg","用户审核成功,创建中信账号成功");
                        }else{
                            map.put("success",false);
                            map.put("msg","创建中信账号失败，用户审核不成功");
                            userExportService.modifyUserAuditStatusByUserIdAndAuditId(originUser,approveId,"创建中信账户失败");//回滚
                        }
                    }
                    if (returnUser.getUsertype()== CiticEnums.UserType.Seller.getCode()){//卖家

                        accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyFinancing);
                        ExecuteResult<AccountInfoDto> result1 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                        accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyPay);
                        ExecuteResult<AccountInfoDto> result2 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());

                        accountInfoDto.setAccType(CiticEnums.AccountType.AccSellFreeze);
                        ExecuteResult<AccountInfoDto> result3 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                        accountInfoDto.setAccType(CiticEnums.AccountType.AccSellReceipt);
                        ExecuteResult<AccountInfoDto> result4 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());

                        if(result1.isSuccess()&&result2.isSuccess()&&result3.isSuccess()&&result4.isSuccess()){
                            logger.info("创建中信账户成功:"+result1.getResult().getSubAccNo()+"|"+result2.getResult().getSubAccNo());
                            UserCiticDTO userCiticDTO = new UserCiticDTO();
                            userCiticDTO.setBuyerFinancingAccount(result1.getResult()==null?"":result1.getResult().getSubAccNo());
                            userCiticDTO.setBuyerPaysAccount(result2.getResult()==null?"":result2.getResult().getSubAccNo());
                            userCiticDTO.setSellerFrozenAccount(result3.getResult()==null?"":result3.getResult().getSubAccNo());
                            userCiticDTO.setSellerWithdrawsCashAccount(result4.getResult() == null ? "" : result4.getResult().getSubAccNo());

                            reuserInfoDTO.setUserCiticDTO(userCiticDTO);
                            reuserInfoDTO.setIsHaveBuyerPaysAccount("1");
                            reuserInfoDTO.setIsHaveSellerCashAccount("1");
                            userExtendsService.modifyUserExtends(reuserInfoDTO,UserEnums.UserExtendsType.CTIBANK);
                            map.put("success",true);
                            map.put("msg","用户审核成功,创建中信账号成功");
                        }else{
                            map.put("success",false);
                            map.put("msg","创建中信账号失败，用户审核不成功");
                            userExportService.modifyUserAuditStatusByUserIdAndAuditId(originUser,approveId,"创建中信账户失败");//回滚
                        }
                    }
//                    map.put("success",true);
//                    map.put("msg","修改用户状态成功，审核用户成功");
                } catch (Exception e) {
                    logger.error("创建中信账号失败",e);
                    map.put("success",false);
                    map.put("msg","创建中信账号失败，用户审核不成功");
                    userExportService.modifyUserAuditStatusByUserIdAndAuditId(originUser,approveId,userDTO.getAuditRemark());//回滚
                }*/
            	map.put("success",true);
                map.put("msg","用户审核成功");

            }else{
                map.put("success",false);
                map.put("msg","修改用户状态不成功，审核用户失败");
            }

        }


        return map;
    }

    /**
     * 快捷认证
     * @param userDTO
     * @param approveId
     * @return
     */
    public Map fastApproveUser(UserDTO userDTO,String approveId){

        Map map = new HashMap();
        UserDTO originUser = userExportService.queryUserById(userDTO.getUid());

        ExecuteResult<UserDTO> result = userExportService.quickAuditUser(userDTO,approveId,userDTO.getAuditRemark());
        UserDTO returnUser = result.getResult();
        ExecuteResult<UserInfoDTO> resultT = userExtendsService.findUserInfo(userDTO.getUid());
        UserInfoDTO reuserInfoDTO = resultT.getResult();
        // 用户审核信息处理，创建中信帐号

        if(result.isSuccess()){
            AccountInfoDto accountInfoDto = new AccountInfoDto();
            accountInfoDto.setUserId(returnUser.getUid());
            accountInfoDto.setSubAccNm(reuserInfoDTO.getUserBusinessDTO().getCompanyName());
            String account = reuserInfoDTO.getUserAccountDTO().getBankAccount();
            accountInfoDto.setBindingAccNo(account);
            // 是否是本行
            try {
                if (returnUser.getUsertype()== CiticEnums.UserType.Buyer.getCode()){//买家
                    accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyFinancing);
                    ExecuteResult<AccountInfoDto> result1 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                    accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyPay);
                    ExecuteResult<AccountInfoDto> result2 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                    if(result1.isSuccess()&&result2.isSuccess()){
                        logger.info("创建中信账户成功:"+result1.getResult().getSubAccNo()+"|"+result2.getResult().getSubAccNo());
                        UserCiticDTO userCiticDTO = new UserCiticDTO();
                        userCiticDTO.setBuyerFinancingAccount(result1.getResult() == null ? "" : result1.getResult().getSubAccNo());
                        userCiticDTO.setBuyerPaysAccount(result2.getResult() == null ? "" : result2.getResult().getSubAccNo());
                        reuserInfoDTO.setUserCiticDTO(userCiticDTO);
                        reuserInfoDTO.setIsHaveBuyerPaysAccount("1");
                        reuserInfoDTO.setIsHaveSellerCashAccount("1");
                        userExtendsService.modifyUserExtends(reuserInfoDTO,UserEnums.UserExtendsType.CTIBANK);
                        map.put("success",true);
                        map.put("msg","用户审核成功,创建中信账号成功");
                    }else{
                        map.put("success",false);
                        map.put("msg","创建中信账号失败，用户审核不成功");
                        logger.error("创建中信账号失败，用户审核不成功:"+result1.getErrorMessages()+"||"+result2.getErrorMessages());
                        userExportService.quickAuditUser(originUser, approveId, "创建中信账户失败");//回滚
                    }
                }
                if (returnUser.getUsertype()== CiticEnums.UserType.Seller.getCode()){//卖家

                    accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyFinancing);
                    ExecuteResult<AccountInfoDto> result1 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                    accountInfoDto.setAccType(CiticEnums.AccountType.AccBuyPay);
                    ExecuteResult<AccountInfoDto> result2 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());

                    accountInfoDto.setAccType(CiticEnums.AccountType.AccSellFreeze);
                    ExecuteResult<AccountInfoDto> result3 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());
                    accountInfoDto.setAccType(CiticEnums.AccountType.AccSellReceipt);
                    ExecuteResult<AccountInfoDto> result4 =  citicExportService.addAffiliated(accountInfoDto,originUser.getPlatformId());

                    if(result1.isSuccess()&&result2.isSuccess()&&result3.isSuccess()&&result4.isSuccess()){
                        logger.info("创建中信账户成功:"+result1.getResult().getSubAccNo()+"|"+result2.getResult().getSubAccNo());
                        UserCiticDTO userCiticDTO = new UserCiticDTO();
                        userCiticDTO.setBuyerFinancingAccount(result1.getResult()==null?"":result1.getResult().getSubAccNo());
                        userCiticDTO.setBuyerPaysAccount(result2.getResult()==null?"":result2.getResult().getSubAccNo());
                        userCiticDTO.setSellerFrozenAccount(result3.getResult()==null?"":result3.getResult().getSubAccNo());
                        userCiticDTO.setSellerWithdrawsCashAccount(result4.getResult() == null ? "" : result4.getResult().getSubAccNo());

                        reuserInfoDTO.setUserCiticDTO(userCiticDTO);
                        reuserInfoDTO.setIsHaveBuyerPaysAccount("1");
                        reuserInfoDTO.setIsHaveSellerCashAccount("1");
                        userExtendsService.modifyUserExtends(reuserInfoDTO,UserEnums.UserExtendsType.CTIBANK);
                        map.put("success",true);
                        map.put("msg","用户审核成功,创建中信账号成功");
                    }else{
                        map.put("success",false);
                        map.put("msg","创建中信账号失败，用户审核不成功");
                        userExportService.quickAuditUser(originUser, approveId, "创建中信账户失败");//回滚
                    }
                }
//                    map.put("success",true);
//                    map.put("msg","修改用户状态成功，审核用户成功");
            } catch (Exception e) {
                logger.error("创建中信账号失败",e);
                map.put("success",false);
                map.put("msg","创建中信账号失败，用户审核不成功");
                userExportService.quickAuditUser(originUser, approveId, userDTO.getAuditRemark());//回滚
            }


        }else{
            map.put("success",false);
            map.put("msg","修改用户状态不成功，审核用户失败");
        }


        return map;
    }
}
