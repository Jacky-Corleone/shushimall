package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.usercenter.dto.LoginResDTO;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.RegisterInfoDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.report.UserAndShopReportDTO;
import com.camelot.usercenter.dto.report.UserReportDTO;
import com.camelot.usercenter.dto.report.UserReportQueryDTO;
import com.camelot.usercenter.enums.CommonEnums;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserType;

import java.util.Date;
import java.util.List;

/**
 * 
 * <p>Description: [用户得报表接口]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface UserReportService {
    //用户运营统计列表汇总
    public ExecuteResult<UserAndShopReportDTO> getCustomerAndShopReportSum();

    //用户运营统计列表明细
    public  ExecuteResult<List<UserAndShopReportDTO>> getCustomerAndShopReportByCondition(Date beginDate,Date endDate);

    // 用户每日新增数量统计
    public ExecuteResult<List<UserReportDTO>> getUserCreateReportByCreateDt(UserReportQueryDTO queryDto);

}
