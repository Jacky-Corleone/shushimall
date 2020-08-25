package com.camelot.usercenter.service.impl;

import com.camelot.common.constant.GlobalConstant;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserModifyInfoMybatisDAO;
import com.camelot.usercenter.dao.UserMybatisDAO;
import com.camelot.usercenter.dto.audit.UserModifyDetailDTO;
import com.camelot.usercenter.dto.audit.UserModifyInfoDTO;
import com.camelot.usercenter.dto.report.UserAndShopReportDTO;
import com.camelot.usercenter.dto.report.UserReportDTO;
import com.camelot.usercenter.dto.report.UserReportQueryDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userReportService")
public class UserReportServiceImpl implements UserReportService {
	private final static Logger logger = LoggerFactory.getLogger(UserReportServiceImpl.class);

	@Resource
	private  UserExtendsService userExtendsService;

    @Resource
    private UserExportService userExportService;

    @Resource
    private UserMybatisDAO userMybatisDAO;


    @Override
    public ExecuteResult<UserAndShopReportDTO> getCustomerAndShopReportSum() {
        ExecuteResult<UserAndShopReportDTO> res=new ExecuteResult<UserAndShopReportDTO>();
        UserAndShopReportDTO resData= userMybatisDAO.getCustomerAndShopReportSum();
        res.setResult(resData);
        return res;
    }

    @Override
    public ExecuteResult<List<UserAndShopReportDTO>> getCustomerAndShopReportByCondition(Date beginDate, Date endDate)
    {
        ExecuteResult<List<UserAndShopReportDTO>> res=new ExecuteResult<List<UserAndShopReportDTO>>();
        List<UserAndShopReportDTO> resList=userMybatisDAO.getCustomerAndShopReportByCondition( beginDate,  endDate);
        res.setResult(resList);
        return res;
    }

    @Override
    public ExecuteResult<List<UserReportDTO>> getUserCreateReportByCreateDt(UserReportQueryDTO queryDto) {
        ExecuteResult<List<UserReportDTO>> res=new ExecuteResult<List<UserReportDTO>>();
        List<UserReportDTO> resList=new ArrayList<UserReportDTO>();
        resList= userMybatisDAO.getUserCreateReportByCreateDt(queryDto.getCreateBeginDt(),queryDto.getCreateEndDt());
        res.setResult(resList);

        return res;
    }
}
