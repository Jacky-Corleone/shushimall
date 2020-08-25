package com.camelot.usercenter.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;


import com.camelot.usercenter.dto.UserCompanyDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.audit.UserAuditLogDTO;
import com.camelot.usercenter.enums.UserEnums;
import com.camelot.usercenter.service.UserAuditLogService;
import com.camelot.usercenter.service.UserExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.common.constant.GlobalConstant;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.DateTimeUtil;
import com.camelot.openplatform.util.StrUtil;
import com.camelot.usercenter.dao.UserModifyDetailMybatisDAO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.UserInfoModifyEmums.UserInfoModifyColumn;
import com.camelot.usercenter.service.UserCenterOperaService;
import com.camelot.usercenter.service.UserModifyInfoService;

@Service("userCenterOperaService")
public class UserCenterOpearServiceImpl implements UserCenterOperaService {
	private final static Logger logger = LoggerFactory.getLogger(UserCenterOpearServiceImpl.class);
	@Resource
	private  UserModifyInfoService userModifyInfoService;

	@Resource
	UserModifyDetailMybatisDAO userModifyDetailMybatisDAO;

    @Resource
    UserAuditLogService userAuditLogService;

    @Resource
    UserExportService userExportService;

    @Override
    public ExecuteResult<String> insertModifyDetailByUserInfo(UserInfoDTO before, UserInfoDTO after,
                                                              UserModifyInfoDTO modify) {

        ExecuteResult<String> res = new ExecuteResult<String>();
        UserModifyDetailDTO insertDto = new UserModifyDetailDTO();
        insertDto.setTableName("user_extends");
        insertDto.setChangeValueId(before.getExtendId().toString());
        insertDto.setChangeColumnName("extend_id");
        insertDto.setCreateTime(new Date());
        insertDto.setModifyInfoId(modify.getId());

        String userModfyType = "";
        String deletedFlag  = GlobalConstant.DEFUNCT_IND_TRUE_STRING;;
        //公司名称修改
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getCompanyName(), after.getUserBusinessDTO().getCompanyName())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getCompanyName());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getCompanyName());
            insertDto.setColumnName(UserInfoModifyColumn.company_name.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_name.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }

        //公司地址修改
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getCompanyAddress(), after.getUserBusinessDTO().getCompanyAddress())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getCompanyAddress());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getCompanyAddress());
            insertDto.setColumnName(UserInfoModifyColumn.company_address.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_address.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //公司电话修改
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getCompanyPhone(), after.getUserBusinessDTO().getCompanyPhone())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getCompanyPhone());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getCompanyPhone());
            insertDto.setColumnName(UserInfoModifyColumn.company_phone.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_phone.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //法定代表人名称
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getArtificialPersonName(), after.getUserBusinessDTO().getArtificialPersonName())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getArtificialPersonName());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getArtificialPersonName());
            insertDto.setColumnName(UserInfoModifyColumn.artificial_person_name.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.artificial_person_name.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //法人身份证电子版图片地址(反面)
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getArtificialPersonPicBackSrc(), after.getUserBusinessDTO().getArtificialPersonPicBackSrc())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getArtificialPersonPicBackSrc());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getArtificialPersonPicBackSrc());
            insertDto.setColumnName(UserInfoModifyColumn.artificial_person_pic_back_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.artificial_person_pic_back_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //法人身份证电子版图片地址
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getArtificialPersonPicSrc(), after.getUserBusinessDTO().getArtificialPersonPicSrc())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getArtificialPersonPicSrc());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getArtificialPersonPicSrc());
            insertDto.setColumnName(UserInfoModifyColumn.artificial_person_pic_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.artificial_person_pic_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //注册号（营业执照号） 修改
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessLicenceId(), after.getUserBusinessDTO().getBusinessLicenceId())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getBusinessLicenceId());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getBusinessLicenceId());
            insertDto.setColumnName(UserInfoModifyColumn.business_licence_id.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_licence_id.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //注册资金  修改
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getRegisteredCapital(), after.getUserBusinessDTO().getRegisteredCapital())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getRegisteredCapital());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getRegisteredCapital());
            insertDto.setColumnName(UserInfoModifyColumn.registered_capital.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.registered_capital.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //营业执照有效期
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessLicenceIndate(), after.getUserBusinessDTO().getBusinessLicenceIndate())) {
            insertDto.setBeforeChangeValue(DateTimeUtil.dateFormatToString(before.getUserBusinessDTO().getBusinessLicenceIndate(), DateTimeUtil.DATETIME_MINUTE_FORMAT));
            insertDto.setAfterChangeValue(DateTimeUtil.dateFormatToString(after.getUserBusinessDTO().getBusinessLicenceIndate(), DateTimeUtil.DATETIME_MINUTE_FORMAT));
            insertDto.setColumnName(UserInfoModifyColumn.business_licence_indate.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_licence_indate.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //营业执照电子版图片地址
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessLicencePicSrc(), after.getUserBusinessDTO().getBusinessLicencePicSrc())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getBusinessLicencePicSrc());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getBusinessLicencePicSrc());
            insertDto.setColumnName(UserInfoModifyColumn.business_licence_pic_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_licence_pic_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //经营范围
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessScope(), after.getUserBusinessDTO().getBusinessScope())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getBusinessScope());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getBusinessScope());
            insertDto.setColumnName(UserInfoModifyColumn.business_scope.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_scope.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //联系人
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getLinkman(), after.getUserBusinessDTO().getLinkman())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getLinkman());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getLinkman());
            insertDto.setColumnName(UserInfoModifyColumn.linkman.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.linkman.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //公司详细地址
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getCompanyDeclinedAddress(), after.getUserBusinessDTO().getCompanyDeclinedAddress())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getCompanyDeclinedAddress());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getCompanyDeclinedAddress());
            insertDto.setColumnName(UserInfoModifyColumn.company_declined_address.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_declined_address.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //公司人数规模
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getCompanyPeoNum(), after.getUserBusinessDTO().getCompanyPeoNum())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getCompanyPeoNum()==null?null:before.getUserBusinessDTO().getCompanyPeoNum().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getCompanyPeoNum()==null?null:after.getUserBusinessDTO().getCompanyPeoNum().toString());
            insertDto.setColumnName(UserInfoModifyColumn.company_peo_num.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_peo_num.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //公司性质
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getCompanyQualt(), after.getUserBusinessDTO().getCompanyQualt())) {
            insertDto.setBeforeChangeValue((before.getUserBusinessDTO().getCompanyQualt()==null?null:before.getUserBusinessDTO().getCompanyQualt().toString()));
            insertDto.setAfterChangeValue((after.getUserBusinessDTO().getCompanyQualt()==null?null:after.getUserBusinessDTO().getCompanyQualt().toString()));
            insertDto.setColumnName(UserInfoModifyColumn.company_qualt.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_qualt.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //经营规模
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessScale(), after.getUserBusinessDTO().getBusinessScale())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getBusinessScale()==null?null:before.getUserBusinessDTO().getBusinessScale().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getBusinessScale()==null?null:after.getUserBusinessDTO().getBusinessScale().toString());
            insertDto.setColumnName(UserInfoModifyColumn.business_scale.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_scale.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }

        //是否融资
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getIsFinancing(), after.getUserBusinessDTO().getIsFinancing())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getIsFinancing());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getIsFinancing());
            insertDto.setColumnName(UserInfoModifyColumn.is_financing.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.is_financing.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //融资金额
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getFinancingNum(), after.getUserBusinessDTO().getFinancingNum())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getFinancingNum());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getFinancingNum());
            insertDto.setColumnName(UserInfoModifyColumn.financing_num.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.financing_num.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }

        //是否三证合一
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getIsSanzheng(), after.getUserBusinessDTO().getIsSanzheng())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getIsSanzheng().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getIsSanzheng().toString());
            insertDto.setBeforeChange(before.getUserBusinessDTO().getIsSanzheng() ==1?"是":"否");
            insertDto.setAfterChange(after.getUserBusinessDTO().getIsSanzheng() ==1?"是":"否");
            insertDto.setColumnName(UserInfoModifyColumn.is_sanzheng.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.is_sanzheng.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //统一信用代码
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getUnifiedCreditCode(), after.getUserBusinessDTO().getUnifiedCreditCode())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getUnifiedCreditCode());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getUnifiedCreditCode());
            insertDto.setColumnName(UserInfoModifyColumn.unified_credit_code.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.unified_credit_code.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        //证件类型
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getIdCardType(), after.getUserBusinessDTO().getIdCardType())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getIdCardType());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getIdCardType());
            insertDto.setColumnName(UserInfoModifyColumn.id_card_type.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.id_card_type.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        
        //证件号码（原：身份证）
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getIdCardNum(), after.getUserBusinessDTO().getIdCardNum())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getIdCardNum());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getIdCardNum());
            insertDto.setColumnName(UserInfoModifyColumn.id_card_num.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.id_card_num.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //营业执照地址
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessLicenceAddress(), after.getUserBusinessDTO().getBusinessLicenceAddress())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getBusinessLicenceAddress());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getBusinessLicenceAddress());
            insertDto.setColumnName(UserInfoModifyColumn.business_licence_address.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_licence_address.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        
        //营业执照详细地址
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessLicencAddressDetail(), after.getUserBusinessDTO().getBusinessLicencAddressDetail())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getBusinessLicencAddressDetail());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getBusinessLicencAddressDetail());
            insertDto.setColumnName(UserInfoModifyColumn.business_licenc_address_detail.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_licenc_address_detail.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        
        //business_licence_date
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getBusinessLicenceDate(), after.getUserBusinessDTO().getBusinessLicenceDate())) {
            insertDto.setBeforeChangeValue(DateTimeUtil.dateFormatToString( before.getUserBusinessDTO().getBusinessLicenceDate(),DateTimeUtil.DATETIME_SECOND_FORMAT));
            insertDto.setAfterChangeValue(DateTimeUtil.dateFormatToString(after.getUserBusinessDTO().getBusinessLicenceDate(),DateTimeUtil.DATETIME_SECOND_FORMAT) );
            insertDto.setColumnName(UserInfoModifyColumn.business_licence_date.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.business_licence_date.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //linkman_phone
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getLinkmanPhone(), after.getUserBusinessDTO().getLinkmanPhone())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getLinkmanPhone());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getLinkmanPhone() );
            insertDto.setColumnName(UserInfoModifyColumn.linkman_phone.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.linkman_phone.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        
        //银行账号
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getBankAccount(), after.getUserAccountDTO().getBankAccount())) {
            insertDto.setBeforeChangeValue(before.getUserAccountDTO().getBankAccount());
            insertDto.setAfterChangeValue(after.getUserAccountDTO().getBankAccount());
            insertDto.setColumnName(UserInfoModifyColumn.bank_account.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.bank_account.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        
        //银行开户名
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getBankAccountName(), after.getUserAccountDTO().getBankAccountName())) {
            insertDto.setBeforeChangeValue(before.getUserAccountDTO().getBankAccountName());
            insertDto.setAfterChangeValue(after.getUserAccountDTO().getBankAccountName());
            insertDto.setColumnName(UserInfoModifyColumn.bank_account_name.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.bank_account_name.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //是否是中信银行
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getIsCiticBank(), after.getUserAccountDTO().getIsCiticBank())) {
            insertDto.setBeforeChangeValue(StrUtil.object2String(before.getUserAccountDTO().getIsCiticBank()));
            insertDto.setAfterChangeValue(StrUtil.object2String(after.getUserAccountDTO().getIsCiticBank().toString()));
            insertDto.setColumnName(UserInfoModifyColumn.is_citic_bank.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.is_citic_bank.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }


        //银行开户许可证电子版图片地址
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getBankAccountPermitsPicSrc(), after.getUserAccountDTO().getBankAccountPermitsPicSrc())) {
            insertDto.setBeforeChangeValue(before.getUserAccountDTO().getBankAccountPermitsPicSrc());
            insertDto.setAfterChangeValue(after.getUserAccountDTO().getBankAccountPermitsPicSrc());
            insertDto.setColumnName(UserInfoModifyColumn.bank_account_permits_pic_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.bank_account_permits_pic_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //开户行支行所在地
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getBankBranchIsLocated(), after.getUserAccountDTO().getBankBranchIsLocated())) {
            insertDto.setBeforeChangeValue(before.getUserAccountDTO().getBankBranchIsLocated());
            insertDto.setAfterChangeValue(after.getUserAccountDTO().getBankBranchIsLocated());
            insertDto.setColumnName(UserInfoModifyColumn.bank_branch_is_located.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.bank_branch_is_located.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //开户行支行联行号
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getBankBranchJointLine(), after.getUserAccountDTO().getBankBranchJointLine())) {
            insertDto.setBeforeChangeValue(before.getUserAccountDTO().getBankBranchJointLine());
            insertDto.setAfterChangeValue(after.getUserAccountDTO().getBankBranchJointLine());
            insertDto.setColumnName(UserInfoModifyColumn.bank_branch_joint_line.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.bank_branch_joint_line.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //开户行支行名称
        if (after.getUserAccountDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserAccountDTO().getBankName(), after.getUserAccountDTO().getBankName())) {
            insertDto.setBeforeChangeValue(before.getUserAccountDTO().getBankName());
            insertDto.setAfterChangeValue(after.getUserAccountDTO().getBankName());
            insertDto.setColumnName(UserInfoModifyColumn.bank_name.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.bank_name.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ACCOUNT;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //组织机构代码
        if (after.getUserOrganizationDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserOrganizationDTO().getOrganizationId(), after.getUserOrganizationDTO().getOrganizationId())) {
            insertDto.setBeforeChangeValue(before.getUserOrganizationDTO().getOrganizationId());
            insertDto.setAfterChangeValue(after.getUserOrganizationDTO().getOrganizationId());
            insertDto.setColumnName(UserInfoModifyColumn.organization_id.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.organization_id.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ORG;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //组织机构图片地址
        if (after.getUserOrganizationDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserOrganizationDTO().getOrganizationPicSrc(), after.getUserOrganizationDTO().getOrganizationPicSrc())) {
            insertDto.setBeforeChangeValue(before.getUserOrganizationDTO().getOrganizationPicSrc());
            insertDto.setAfterChangeValue(after.getUserOrganizationDTO().getOrganizationPicSrc());
            insertDto.setColumnName(UserInfoModifyColumn.organization_pic_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.organization_pic_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ORG;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }


        //userful_time
        if (after.getUserOrganizationDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserOrganizationDTO().getUserfulTime(), after.getUserOrganizationDTO().getUserfulTime())) {
            insertDto.setBeforeChangeValue(before.getUserOrganizationDTO().getUserfulTime());
            insertDto.setAfterChangeValue(after.getUserOrganizationDTO().getUserfulTime());
            insertDto.setColumnName(UserInfoModifyColumn.userful_time.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.userful_time.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_ORG;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //税务人识别号
        if (after.getUserTaxDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserTaxDTO().getTaxManId(), after.getUserTaxDTO().getTaxManId())) {
            insertDto.setBeforeChangeValue(before.getUserTaxDTO().getTaxManId());
            insertDto.setAfterChangeValue(after.getUserTaxDTO().getTaxManId());
            insertDto.setColumnName(UserInfoModifyColumn.tax_man_id.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.tax_man_id.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_TAX;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //税务登记证电子版图片地址
        if (after.getUserTaxDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserTaxDTO().getTaxpayerCertificatePicSrc(), after.getUserTaxDTO().getTaxpayerCertificatePicSrc())) {
            insertDto.setBeforeChangeValue(before.getUserTaxDTO().getTaxpayerCertificatePicSrc());
            insertDto.setAfterChangeValue(after.getUserTaxDTO().getTaxpayerCertificatePicSrc());
            insertDto.setColumnName(UserInfoModifyColumn.tax_registration_certificate_pic_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.tax_registration_certificate_pic_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_TAX;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        if (after.getUserTaxDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserTaxDTO().getTaxpayerType(), after.getUserTaxDTO().getTaxpayerType())) {
            insertDto.setBeforeChangeValue(before.getUserTaxDTO().getTaxpayerType());
            insertDto.setAfterChangeValue(after.getUserTaxDTO().getTaxpayerType());
            insertDto.setColumnName(UserInfoModifyColumn.taxpayer_type.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.taxpayer_type.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_TAX;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //纳税人类型
        if (after.getUserTaxDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserTaxDTO().getTaxpayerCode(), after.getUserTaxDTO().getTaxpayerCode())) {
            insertDto.setBeforeChangeValue(before.getUserTaxDTO().getTaxpayerCode());
            insertDto.setAfterChangeValue(after.getUserTaxDTO().getTaxpayerCode());
            insertDto.setColumnName(UserInfoModifyColumn.taxpayer_code.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.taxpayer_code.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_TAX;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //纳税人资格证电子版图片地址
        if (after.getUserTaxDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserTaxDTO().getTaxRegistrationCertificatePicSrc(), after.getUserTaxDTO().getTaxRegistrationCertificatePicSrc())) {
            insertDto.setBeforeChangeValue(before.getUserTaxDTO().getTaxRegistrationCertificatePicSrc());
            insertDto.setAfterChangeValue(after.getUserTaxDTO().getTaxRegistrationCertificatePicSrc());
            insertDto.setColumnName(UserInfoModifyColumn.taxpayer_certificate_pic_src.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.taxpayer_certificate_pic_src.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_TAX;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //tax_type
        if (after.getUserTaxDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserTaxDTO().getTaxType(), after.getUserTaxDTO().getTaxType())) {
            insertDto.setBeforeChangeValue(before.getUserTaxDTO().getTaxType()==null?null:before.getUserTaxDTO().getTaxType().toString());
            insertDto.setAfterChangeValue(after.getUserTaxDTO().getTaxType()==null?null:after.getUserTaxDTO().getTaxType().toString());
            insertDto.setColumnName(UserInfoModifyColumn.tax_type.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.tax_type.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_TAX;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //dealer_type
        if (after.getUserManageDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserManageDTO().getDealerType(), after.getUserManageDTO().getDealerType())) {
            insertDto.setBeforeChangeValue(before.getUserManageDTO().getDealerType()==null?null:before.getUserManageDTO().getDealerType().toString());
            insertDto.setAfterChangeValue(after.getUserManageDTO().getDealerType()==null?null:after.getUserManageDTO().getDealerType().toString());
            insertDto.setColumnName(UserInfoModifyColumn.dealer_type.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.dealer_type.getLabel());
            userModfyType = GlobalConstant.USER_MANAGE;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //home_page
        if (after.getUserManageDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserManageDTO().getHomePage(), after.getUserManageDTO().getHomePage())) {
            insertDto.setBeforeChangeValue(before.getUserManageDTO().getHomePage()==null?null:before.getUserManageDTO().getHomePage().toString());
            insertDto.setAfterChangeValue(after.getUserManageDTO().getHomePage()==null?null:after.getUserManageDTO().getHomePage().toString());
            insertDto.setColumnName(UserInfoModifyColumn.home_page.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.home_page.getLabel());
            userModfyType = GlobalConstant.USER_MANAGE;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //sale_num
        if (after.getUserManageDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserManageDTO().getSaleNum(), after.getUserManageDTO().getSaleNum())) {
            insertDto.setBeforeChangeValue(before.getUserManageDTO().getSaleNum()==null?null:before.getUserManageDTO().getSaleNum().toString());
            insertDto.setAfterChangeValue(after.getUserManageDTO().getSaleNum()==null?null:after.getUserManageDTO().getSaleNum().toString());
            insertDto.setColumnName(UserInfoModifyColumn.sale_num.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.sale_num.getLabel());
            userModfyType = GlobalConstant.USER_MANAGE;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //is_have_ebusiness
        if (after.getUserManageDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserManageDTO().getIsHaveEbusiness(), after.getUserManageDTO().getIsHaveEbusiness())) {
            insertDto.setBeforeChangeValue(before.getUserManageDTO().getIsHaveEbusiness()==null?null:before.getUserManageDTO().getIsHaveEbusiness().toString());
            insertDto.setAfterChangeValue(after.getUserManageDTO().getIsHaveEbusiness()== null?null:after.getUserManageDTO().getIsHaveEbusiness().toString());
            insertDto.setColumnName(UserInfoModifyColumn.is_have_ebusiness.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.is_have_ebusiness.getLabel());
            userModfyType = GlobalConstant.USER_MANAGE;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //erp_type
        if (after.getUserManageDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserManageDTO().getErpType(), after.getUserManageDTO().getErpType())) {
            insertDto.setBeforeChangeValue(before.getUserManageDTO().getErpType()==null?null:before.getUserManageDTO().getErpType().toString());
            insertDto.setAfterChangeValue(after.getUserManageDTO().getErpType()==null?null:after.getUserManageDTO().getErpType().toString());
            insertDto.setColumnName(UserInfoModifyColumn.erp_type.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.erp_type.getLabel());
            userModfyType = GlobalConstant.USER_MANAGE;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //web_opera_peo
        if (after.getUserManageDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserManageDTO().getWebOperaPeo(), after.getUserManageDTO().getWebOperaPeo())) {
            insertDto.setBeforeChangeValue(before.getUserManageDTO().getWebOperaPeo()==null?null:before.getUserManageDTO().getWebOperaPeo().toString());
            insertDto.setAfterChangeValue(after.getUserManageDTO().getWebOperaPeo()==null?null:after.getUserManageDTO().getWebOperaPeo().toString());
            insertDto.setColumnName(UserInfoModifyColumn.web_opera_peo.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.web_opera_peo.getLabel());
            userModfyType = GlobalConstant.USER_MANAGE;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //绿色印刷环境标志产品认证证书
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getGpProductCertification(), after.getUserBusinessDTO().getGpProductCertification())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getGpProductCertification()==null?null:before.getUserBusinessDTO().getGpProductCertification().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getGpProductCertification()==null?null:after.getUserBusinessDTO().getGpProductCertification().toString());
            insertDto.setColumnName(UserInfoModifyColumn.gp_product_certification.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.gp_product_certification.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //印刷经营许可证
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getGpPrintBusinessLicense(), after.getUserBusinessDTO().getGpPrintBusinessLicense())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getGpPrintBusinessLicense()==null?null:before.getUserBusinessDTO().getGpPrintBusinessLicense().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getGpPrintBusinessLicense()==null?null:after.getUserBusinessDTO().getGpPrintBusinessLicense().toString());
            insertDto.setColumnName(UserInfoModifyColumn.gp_print_business_license.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.gp_print_business_license.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //承诺书
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getGpCommitmentBook(), after.getUserBusinessDTO().getGpCommitmentBook())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getGpCommitmentBook()==null?null:before.getUserBusinessDTO().getGpCommitmentBook().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getGpCommitmentBook()==null?null:after.getUserBusinessDTO().getGpCommitmentBook().toString());
            insertDto.setColumnName(UserInfoModifyColumn.gp_commitment_book.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.gp_commitment_book.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //ISO9001质量管理体系认证证书
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getGpQualityManagementCertification(), after.getUserBusinessDTO().getGpQualityManagementCertification())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getGpQualityManagementCertification()==null?null:before.getUserBusinessDTO().getGpQualityManagementCertification().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getGpQualityManagementCertification()==null?null:after.getUserBusinessDTO().getGpQualityManagementCertification().toString());
            insertDto.setColumnName(UserInfoModifyColumn.gp_quality_management_certification.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.gp_quality_management_certification.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        //ISO14001环境管理体系认证证书 
        if (after.getUserBusinessDTO() != null && StrUtil.validaIsNotSameBoforeAndAfter(before.getUserBusinessDTO().getGpEnvironmentalManagementCertification(), after.getUserBusinessDTO().getGpEnvironmentalManagementCertification())) {
            insertDto.setBeforeChangeValue(before.getUserBusinessDTO().getGpEnvironmentalManagementCertification()==null?null:before.getUserBusinessDTO().getGpEnvironmentalManagementCertification().toString());
            insertDto.setAfterChangeValue(after.getUserBusinessDTO().getGpEnvironmentalManagementCertification()==null?null:after.getUserBusinessDTO().getGpEnvironmentalManagementCertification().toString());
            insertDto.setColumnName(UserInfoModifyColumn.gp_environmental_management_certification.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.gp_environmental_management_certification.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_BUINESS;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag =GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }
        modify.setDeletedFlag(deletedFlag);
        modify.setModifyType(userModfyType);
        Integer modInfo= userModifyInfoService.updateModifyInfoSelective(modify);
        if(modInfo>0&&deletedFlag.equals("0")){
            res.setResult(GlobalConstant.SUCCESS_STR);
        }else{
            res.setResult(GlobalConstant.FAIL_STR);
        }
        return res;
    }

    @Override
    public ExecuteResult<String> insertModifyDetailByCompanyInfo(UserCompanyDTO beforeDto, UserCompanyDTO afterDto, UserModifyInfoDTO modify) {
        ExecuteResult<String> res = new ExecuteResult<String>();
        UserModifyDetailDTO insertDto = new UserModifyDetailDTO();
        insertDto.setTableName("user_buyer_company");
        insertDto.setChangeValueId(beforeDto.getId().toString());

        insertDto.setChangeColumnName("id");
        insertDto.setCreateTime(new Date());
        insertDto.setModifyInfoId(modify.getId());

        String userModfyType = GlobalConstant.USER_MODIFY_COMPANY ;
        String deletedFlag = GlobalConstant.DEFUNCT_IND_TRUE_STRING;
        //公司经营范围修改
        if (afterDto.getCompanyScale()!= null && StrUtil.validaIsNotSameBoforeAndAfter(beforeDto.getCompanyScale(), afterDto.getCompanyScale())) {
            insertDto.setBeforeChangeValue(beforeDto.getCompanyScale());
            insertDto.setAfterChangeValue(afterDto.getCompanyScale());
            insertDto.setColumnName(UserInfoModifyColumn.company_scale.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.company_scale.getLabel());
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag = GlobalConstant.DEFUNCT_IND_FALSE_STRING;
        }

        //公司服务类型修改
        if (afterDto.getServiceType()!= null && StrUtil.validaIsNotSameBoforeAndAfter(beforeDto.getServiceType(), afterDto.getServiceType())) {
            insertDto.setBeforeChangeValue(beforeDto.getServiceType());
            insertDto.setAfterChangeValue(afterDto.getServiceType());
            insertDto.setColumnName(UserInfoModifyColumn.service_type.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.service_type.getLabel());

            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag = GlobalConstant.DEFUNCT_IND_FALSE_STRING;


        }
        //公司收货地址修改
        if (afterDto.getDeliveryAddress()!= null && StrUtil.validaIsNotSameBoforeAndAfter(beforeDto.getDeliveryAddress(), afterDto.getDeliveryAddress())) {
            insertDto.setBeforeChangeValue(beforeDto.getDeliveryAddress());
            insertDto.setAfterChangeValue(afterDto.getDeliveryAddress());
            insertDto.setColumnName(UserInfoModifyColumn.delivery_address.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.delivery_address.getLabel());
            userModfyType = GlobalConstant.USER_MODIFY_COMPANY;
            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag = GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }


        if (afterDto.getInvoiceInformation()!= null && StrUtil.validaIsNotSameBoforeAndAfter(beforeDto.getInvoiceInformation(), afterDto.getInvoiceInformation())) {
            insertDto.setBeforeChangeValue(beforeDto.getInvoiceInformation());
            insertDto.setAfterChangeValue(afterDto.getInvoiceInformation());
            insertDto.setColumnName(UserInfoModifyColumn.invoice_information.name());
            insertDto.setPropertiesColumn(UserInfoModifyColumn.invoice_information.getLabel());

            userModifyDetailMybatisDAO.insert(insertDto);
            deletedFlag = GlobalConstant.DEFUNCT_IND_FALSE_STRING;

        }
        modify.setDeletedFlag(deletedFlag);
        modify.setModifyType(userModfyType);
        Integer modInfo= userModifyInfoService.updateModifyInfoSelective(modify);
        if(modInfo>0&&deletedFlag.equals("0")){
            res.setResult("SUCCESS");
        }else{
            res.setResult("FAIL");
        }
        return res;
    }

    @Override
    public ExecuteResult<UserAuditLogDTO> saveUserAuditLogDTO(UserAuditLogDTO userAuditLog,UserEnums.UserOperaType userOperaType) {
        if(userOperaType!=null){
            userAuditLog.setAuditLogType(String.valueOf(userOperaType.getCode()));
        }else{
            throw new RuntimeException("日志类型不能为空");
        }
        ExecuteResult<UserAuditLogDTO> res= userAuditLogService.saveUserAuditLogDTO(userAuditLog);
        return res;
    }

    @Override
    public List<UserAuditLogDTO> findListByCondition(UserAuditLogDTO userAuditLog, UserEnums.UserOperaType userOperaType) {
        userAuditLog.setDeletedFlag(GlobalConstant.DEFUNCT_IND_FALSE_STRING);
        if(userOperaType!=null){
            userAuditLog.setAuditLogType(String.valueOf(userOperaType.getCode()));
        }
        return userAuditLogService.searchByCondition(userAuditLog);
    }

    @Override
    public ExecuteResult<UserDTO> changeParentAndSonAccount(UserDTO sonUser) {
        ExecuteResult<UserDTO> res=new ExecuteResult<UserDTO>();
        try{
            if(sonUser!=null&&sonUser.getUid()!=null){
                UserDTO sonUserRes= userExportService.queryUserById(sonUser.getUid());
                if(sonUserRes!=null&&sonUserRes.getParentId()!=null){
                    UserDTO parentRes= userExportService.queryUserById(sonUser.getParentId());

                    String sonUname=sonUserRes.getUname();
                    String sonPass=sonUserRes.getPassword();
                    String sonEmail=sonUserRes.getUserEmail();
                    String sonMobile=sonUserRes.getUmobile();
                    String sonNickName=sonUserRes.getNickname();
                    String parentUname=parentRes.getUname();
                    String parentPass=parentRes.getPassword();
                    String parentEmail=parentRes.getUserEmail();
                    String parentMobile=parentRes.getUmobile();
                    String parentNickName=parentRes.getNickname();
                    parentRes.setUname(sonUname);
                    parentRes.setPassword(sonPass);
                    parentRes.setUserEmail(sonEmail);
                    parentRes.setUmobile(sonMobile);
                    parentRes.setNickname(sonNickName);
                    sonUserRes.setUname(parentUname);
                    sonUserRes.setPassword(parentPass);
                    sonUserRes.setUserEmail(parentEmail);
                    sonUserRes.setUmobile(parentMobile);
                    sonUserRes.setNickname(parentNickName);
                    userExportService.modifyUserInfo(parentRes);
                    userExportService.modifyUserInfo(sonUserRes);

                    UserAuditLogDTO userAuditLog=new UserAuditLogDTO();

                    userAuditLog.setAuditLogType(String.valueOf(UserEnums.UserOperaType.ZFZHSH.getCode()));
                    userAuditLog.setAuditId(parentRes.getUid());
                    List<UserAuditLogDTO> logRes= userAuditLogService.searchByCondition(userAuditLog);
                    if(logRes!=null&&logRes.size()>0){
                        UserAuditLogDTO userAuditLogRes=  logRes.get(0);
                        userAuditLogRes.setAuditStatus(2);
                        userAuditLogRes.setAuditDate(new Date());

                        userAuditLogService.updateSelective(userAuditLogRes);
                    }


                    res.setResult(parentRes);
                }else{
                    res.addErrorMessage("用户父账号ID为空");
                }
            }
        }catch (Exception e){
            throw new RuntimeException("子账号申请更改父账号权限失败"+e.getMessage());
        }


        return res;
    }


}
