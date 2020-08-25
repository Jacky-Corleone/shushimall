package com.camelot.usercenter.service.impl;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.UserWxDAO;
import com.camelot.usercenter.dto.*;
import com.camelot.usercenter.service.*;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("userWxExportService")
public class UserWxExportServiceImpl implements UserWxExportService {
    private final static Logger logger = LoggerFactory.getLogger(UserWxExportServiceImpl.class);
    @Resource
    private UserWxDAO userWxDAO;


    @Override
    public ExecuteResult<UserWxDTO> bindingWX(UserWxDTO userWxDTO){
        ExecuteResult<UserWxDTO> executeResult = new ExecuteResult<UserWxDTO>();
        int count = userWxDAO.bindingWX(userWxDTO.getUid(), userWxDTO.getWxopenid());
        if (count > 0) {
            executeResult.setResultMessage("操作成功！");
        } else {
            executeResult.setResultMessage("操作失败！");
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<UserWxDTO> getUserInfoByOpenId(UserWxDTO userWxDTO){
        ExecuteResult<UserWxDTO> res = new ExecuteResult<UserWxDTO>();
        UserWxDTO user=userWxDAO.getUserInfoByOpenId(userWxDTO);
        res.setResult(user);
        return res;
    }

    @Override
    public ExecuteResult<UserWxDTO> cancelBinding(UserWxDTO userWxDTO){
        ExecuteResult<UserWxDTO> executeResult = new ExecuteResult<UserWxDTO>();
        int count = userWxDAO.cancelBinding(userWxDTO.getUid());
        if (count > 0) {
            executeResult.setResultMessage("操作成功！");
        } else {
            executeResult.setResultMessage("操作失败！");
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<DataGrid<UserDTO>> queryUser(UserDTO userDTO,Pager page){
        ExecuteResult<DataGrid<UserDTO>> er=new ExecuteResult<DataGrid<UserDTO>>();
        DataGrid<UserDTO> dg=new DataGrid<UserDTO>();
        List<UserDTO> list=userWxDAO.queryPage(page, userDTO);
        Long count=userWxDAO.queryPageCount(userDTO);
        try{
            if(list != null ){
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            }else{
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        }catch(Exception e){
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    @Override
    public ExecuteResult<DataGrid<Map>> findUserListByUserIds(List<String> idList){
        ExecuteResult<DataGrid<Map>> er=new ExecuteResult<DataGrid<Map>>();
        DataGrid<Map> dg=new DataGrid<Map>();
        List<Map> list=userWxDAO.findUserListByUserIds(idList);
        Long count=userWxDAO.findUserListByUserIdsCount(idList);
        try{
            if(list != null ){
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            }else{
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        }catch(Exception e){
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;

    }


}
