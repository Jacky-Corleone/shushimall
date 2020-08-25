package com.camelot.usercenter.service;


import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.usercenter.dto.userInfo.UserPersonalInfoDTO;

import java.util.Date;
import java.util.List;

public interface UserPersonalInfoService{

    /**
     * 获得个人资料完善度
     * @param userId
     * @return
     */
    public  ExecuteResult<String> getPersonlInfoPerfectDegree(String userId);
    /**
     *  创建对象
     */
    public ExecuteResult<UserPersonalInfoDTO> createUserPersonalInfoDTO(UserPersonalInfoDTO userPersonalInfo);
    /**
     *  更新对象
     */
    public ExecuteResult<UserPersonalInfoDTO>updateUserPersonalInfoDTO(UserPersonalInfoDTO userPersonalInfo);
    /**
     * 根据ID删除对象
     */
    public ExecuteResult<UserPersonalInfoDTO>deleteById(Long userPersonalInfoId);
    /**
     * 删除所有对象
     */
    public ExecuteResult<UserPersonalInfoDTO>deleteAll(List<Long> idList);

    /**
     * 分页查询
     */
    public DataGrid<UserPersonalInfoDTO> searchUserPersonalInfoDTOs(Pager<UserPersonalInfoDTO> pager, UserPersonalInfoDTO userPersonalInfo);

    /**
     * 根据ID查询对象
     */
    public UserPersonalInfoDTO getUserPersonalInfoDTOById(Long userPersonalInfoId);
    /**
     * 更新对象
     */
    public ExecuteResult<UserPersonalInfoDTO>updateSelective(UserPersonalInfoDTO userPersonalInfo);
    /**
     * 根据时间戳更新对象
     */
    public ExecuteResult<UserPersonalInfoDTO>updateSelectiveWithDateTimeCheck(UserPersonalInfoDTO userPersonalInfo, Date prevUpdDt)throws CommonCoreException;

    /**
     * 根据条件查询
     */
    public List<UserPersonalInfoDTO>searchByCondition(UserPersonalInfoDTO userPersonalInfo);

    /**
     * 根据IDLIST 更新对象
     */
    public ExecuteResult<UserPersonalInfoDTO>updateSelectiveByIdList(UserPersonalInfoDTO userPersonalInfo, List<Long> idList);
    /**
     * 根据ID更新所有数据
     */
    public ExecuteResult<UserPersonalInfoDTO>updateAllByIdList(UserPersonalInfoDTO userPersonalInfo, List<Long> idList);
    /**
     * 逻辑删除对象
     */
    public ExecuteResult<UserPersonalInfoDTO>defunctById(Long userPersonalInfoId);
    /**
     * 批量逻辑删除对象
     */
    public ExecuteResult<UserPersonalInfoDTO>defunctByIdList(List<Long> idList);
}
