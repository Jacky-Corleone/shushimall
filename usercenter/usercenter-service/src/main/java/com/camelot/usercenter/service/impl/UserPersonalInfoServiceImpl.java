package com.camelot.usercenter.service.impl;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.common.constant.GlobalConstant;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.openplatform.util.DateTimeUtil;
import com.camelot.usercenter.dao.UserPersonalInfoMybatisDAO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserPersonalInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserPersonalInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 业务逻辑实现类
 * </p>
 *
 * @author
 * @createDate
 */
@Service("userPersonalInfoService")
public class UserPersonalInfoServiceImpl implements UserPersonalInfoService {
    private final static Logger logger = LoggerFactory.getLogger(UserPersonalInfoServiceImpl.class);
    @Resource
    private UserPersonalInfoMybatisDAO userPersonalInfoMybatisDAO;
    @Resource
    private UserExportService userExportService;

    @Override
    public ExecuteResult<String> getPersonlInfoPerfectDegree(String userId) {
        ExecuteResult<String> res = new ExecuteResult<String>();
        float degree=0.0f;
        UserDTO user= userExportService.queryUserById(Long.parseLong(userId));
        if(user!=null){
            if(user.getUserstatus()==2){
                degree=0.3f;
            }else if(user.getUserstatus()>2){
                degree=0.6f;
            }
        }

        UserPersonalInfoDTO userPersonalInfo = new UserPersonalInfoDTO();
        userPersonalInfo.setUserId(Long.parseLong(userId));
        List<UserPersonalInfoDTO> userPersonalInfos = userPersonalInfoMybatisDAO.selectList(userPersonalInfo, new Pager());
        if (userPersonalInfos != null && userPersonalInfos.size() > 0) {
            UserPersonalInfoDTO userItem = userPersonalInfos.get(0);
            float valueTotal = 0;
            if (userItem.getAddress() != null&& StringUtils.isNotEmpty(userItem.getAddress())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getBirthday() != null&& StringUtils.isNotEmpty(userItem.getBirthday())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getBlood() != null&& StringUtils.isNotEmpty(userItem.getBlood())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getEvaluate() != null&& StringUtils.isNotEmpty(userItem.getEvaluate())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getHobby() != null&& StringUtils.isNotEmpty(userItem.getHobby())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getHomePage() != null&& StringUtils.isNotEmpty(userItem.getHomePage())) {
                valueTotal = valueTotal + 1;
            }

            if (userItem.getIncome() != null&& StringUtils.isNotEmpty(userItem.getIncome())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getNikeName() != null&& StringUtils.isNotEmpty(userItem.getNikeName())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getOrigin() != null&& StringUtils.isNotEmpty(userItem.getOrigin())) {
                valueTotal = valueTotal + 1;
            }
            if (userItem.getSex() != null&& StringUtils.isNotEmpty(userItem.getSex())) {
                valueTotal = valueTotal + 1;
            }
            degree = (float) (degree+ valueTotal / 10*0.4);

        }
        degree= degree*100;
        DecimalFormat   fnum   =   new DecimalFormat("##0.00");
        String   resStr=fnum.format(degree);
        res.setResult(resStr+"%");
        return res;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> createUserPersonalInfoDTO(UserPersonalInfoDTO userPersonalInfo) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        Date curDate = DateTimeUtil.getCurrentDateByTimeZone();
        userPersonalInfo.setCreateDt(curDate);
        userPersonalInfo.setDeletedFlag(GlobalConstant.DEFUNCT_IND_FALSE_STRING);
        userPersonalInfoMybatisDAO.insert(userPersonalInfo);
        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> updateUserPersonalInfoDTO(UserPersonalInfoDTO userPersonalInfo) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        setLastUpdInfo(userPersonalInfo);
        userPersonalInfoMybatisDAO.updateSelective(userPersonalInfo);
        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> deleteById(Long userPersonalInfoId) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();

        userPersonalInfoMybatisDAO.delete(userPersonalInfoId);
        return executeResult;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> deleteAll(List<Long> idList) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        userPersonalInfoMybatisDAO.deleteAll(idList);
        return executeResult;
    }

    @Override
    public DataGrid<UserPersonalInfoDTO> searchUserPersonalInfoDTOs(Pager<UserPersonalInfoDTO> pager, UserPersonalInfoDTO userPersonalInfo) {
        DataGrid<UserPersonalInfoDTO> resultPager = new DataGrid<UserPersonalInfoDTO>();
        List<UserPersonalInfoDTO> userPersonalInfos = userPersonalInfoMybatisDAO.selectList(userPersonalInfo, pager);
        long size = userPersonalInfoMybatisDAO.selectCount(userPersonalInfo);
        resultPager.setRows(userPersonalInfos);
        resultPager.setTotal(size);
        return resultPager;
    }

    @Override
    public UserPersonalInfoDTO getUserPersonalInfoDTOById(Long userPersonalInfoId) {
        return userPersonalInfoMybatisDAO.selectById(userPersonalInfoId);
    }


    @Override
    public ExecuteResult<UserPersonalInfoDTO> updateSelective(UserPersonalInfoDTO userPersonalInfo) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        setLastUpdInfo(userPersonalInfo);
        userPersonalInfoMybatisDAO.updateSelective(userPersonalInfo);
        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> updateSelectiveWithDateTimeCheck(UserPersonalInfoDTO userPersonalInfo, Date prevUpdDt) throws CommonCoreException {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        setLastUpdInfo(userPersonalInfo);
        int resInt = userPersonalInfoMybatisDAO.updateSelectiveWithDateTimeCheck(userPersonalInfo, prevUpdDt);

        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    @Override
    public List<UserPersonalInfoDTO> searchByCondition(UserPersonalInfoDTO userPersonalInfo) {
        userPersonalInfo.setDeletedFlag(GlobalConstant.DEFUNCT_IND_FALSE_STRING);
        return userPersonalInfoMybatisDAO.searchByCondition(userPersonalInfo);
    }


    @Override
    public ExecuteResult<UserPersonalInfoDTO> updateSelectiveByIdList(UserPersonalInfoDTO userPersonalInfo, List<Long> idList) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        if (idList != null && idList.size() > 0 && userPersonalInfo != null) {
            setLastUpdInfo(userPersonalInfo);
            userPersonalInfoMybatisDAO.updateSelectiveByIdList(userPersonalInfo, idList);
        }
        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> updateAllByIdList(UserPersonalInfoDTO userPersonalInfo, List<Long> idList) {
        return null;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> defunctById(Long userPersonalInfoId) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        UserPersonalInfoDTO userPersonalInfo = new UserPersonalInfoDTO();
        userPersonalInfo.setDeletedFlag(GlobalConstant.DEFUNCT_IND_TRUE_STRING);
        userPersonalInfo.setId(userPersonalInfoId);
        userPersonalInfoMybatisDAO.updateSelective(userPersonalInfo);
        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    @Override
    public ExecuteResult<UserPersonalInfoDTO> defunctByIdList(List<Long> idList) {
        ExecuteResult<UserPersonalInfoDTO> executeResult = new ExecuteResult<UserPersonalInfoDTO>();
        UserPersonalInfoDTO userPersonalInfo = new UserPersonalInfoDTO();
        userPersonalInfo.setDeletedFlag(GlobalConstant.DEFUNCT_IND_TRUE_STRING);
        userPersonalInfoMybatisDAO.updateSelectiveByIdList(userPersonalInfo, idList);
        executeResult.setResult(userPersonalInfo);
        return executeResult;
    }

    private void setLastUpdInfo(UserPersonalInfoDTO userPersonalInfo) {

        Date curDate = DateTimeUtil.getCurrentDateByTimeZone();

        userPersonalInfo.setLastUpdDt(curDate);
    }

}
