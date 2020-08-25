package com.camelot.storecenter.service.impl;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dao.ShopPreferentialWayDAO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import com.camelot.storecenter.service.ShopPreferentialWayService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月18日
 *
 * @author <a href="mailto: guojianning@camelotchina.com">郭建宁</a>
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopPreferentialWayService")
public class ShopPreferentialWayServiceImpl implements ShopPreferentialWayService {
    private final static Logger logger= LoggerFactory.getLogger(ShopPreferentialWayDTO.class);
    @Resource
    private ShopPreferentialWayDAO shopPreferentialWayDAO;
    @Override
    public ExecuteResult<String> addShopPreferentialWay(ShopPreferentialWayDTO dto) {
        ExecuteResult<String> ex=new ExecuteResult<String>();
        try {
            shopPreferentialWayDAO.insert(dto);
            ex.setResultMessage("添加成功！");
        }catch(Exception e){
            logger.error(e.getMessage());
            ex.setResultMessage("添加失败！");
            throw new RuntimeException();
        }
        return ex;
    }

    @Override
    public ExecuteResult<String> deleteShopPreferentialWay(ShopPreferentialWayDTO dto) {
        ExecuteResult<String> ex=new ExecuteResult<String>();
        try {
            Long id=dto.getId();
            if(shopPreferentialWayDAO.delete(id)>0){
                ex.setResultMessage("删除成功！");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            ex.setResultMessage("删除失败！");
            throw new RuntimeException();
        }
        return ex;
    }

    @Override
    public ExecuteResult<String> updateShopPreferentialWay(ShopPreferentialWayDTO dto) {
        ExecuteResult<String> ex=new ExecuteResult<String>();
        try {
            if(shopPreferentialWayDAO.update(dto)>0){
                ex.setResultMessage("修改成功！");
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            ex.setResultMessage("修改失败！");
            throw new RuntimeException();
        }
        return ex;
    }

    @Override
    public ExecuteResult<List<ShopPreferentialWayDTO>> queryShopPreferentialWay(ShopPreferentialWayDTO dto) {
        ExecuteResult<List<ShopPreferentialWayDTO>> ex=new ExecuteResult<List<ShopPreferentialWayDTO>>();
        try {
            List<ShopPreferentialWayDTO> list=shopPreferentialWayDAO.selectListByCondition(dto);
            ex.setResult(list);
            ex.setResultMessage("success");
        }catch (Exception e){
            ex.setResultMessage("error");
            logger.error(e.getMessage());
            throw new RuntimeException();
        }
        return ex;
    }
}
