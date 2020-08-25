package com.camelot.storecenter.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopCustomerServiceMybatisDAO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.service.ShopCustomerServiceService;
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
@Service("shopCustomerServiceService")
public class ShopCustomerServiceServiceImpl implements ShopCustomerServiceService {
    private final static Logger logger = LoggerFactory.getLogger(ShopCustomerServiceServiceImpl.class);
    @Resource
    private ShopCustomerServiceMybatisDAO shopCustomerServiceMybatisDAO;


    @Override
    public ExecuteResult<ShopCustomerServiceDTO> createShopCustomerServiceDTO(ShopCustomerServiceDTO shopCustomerService) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();

        shopCustomerService.setCreatedDt(new Date());
        shopCustomerService.setDeletedFlag(0);
        shopCustomerServiceMybatisDAO.insert(shopCustomerService);
        executeResult.setResult(shopCustomerService);
        return executeResult;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> updateShopCustomerServiceDTO(ShopCustomerServiceDTO shopCustomerService) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        setLastUpdInfo(shopCustomerService);
        shopCustomerServiceMybatisDAO.updateSelective(shopCustomerService);
        executeResult.setResult(shopCustomerService);
        return executeResult;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> deleteById(Long shopCustomerServiceId) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();

        shopCustomerServiceMybatisDAO.delete(shopCustomerServiceId);
        return executeResult;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> deleteAll(List<Long> idList) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        shopCustomerServiceMybatisDAO.deleteAll(idList);
        return executeResult;
    }


    @Override
    public DataGrid<ShopCustomerServiceDTO> searchShopCustomerServiceDTOs(Pager<ShopCustomerServiceDTO> pager, ShopCustomerServiceDTO shopCustomerService) {
        DataGrid<ShopCustomerServiceDTO> resultPager = new DataGrid<ShopCustomerServiceDTO>();
        List<ShopCustomerServiceDTO> shopCustomerServices = shopCustomerServiceMybatisDAO.selectListByCondition(shopCustomerService, pager);
        long size = shopCustomerServiceMybatisDAO.selectCountByCondition(shopCustomerService);
        resultPager.setRows(shopCustomerServices);
        resultPager.setTotal(size);
        return resultPager;
    }

    @Override
    public ShopCustomerServiceDTO getShopCustomerServiceDTOById(Long shopCustomerServiceId) {
        return shopCustomerServiceMybatisDAO.selectById(shopCustomerServiceId);
    }


    @Override
    public ExecuteResult<ShopCustomerServiceDTO> updateSelective(ShopCustomerServiceDTO shopCustomerService) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        setLastUpdInfo(shopCustomerService);
        int count = shopCustomerServiceMybatisDAO.updateSelective(shopCustomerService);
        if (count > 0) {
            executeResult.setResult(shopCustomerService);
        }
        return executeResult;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> updateSelectiveWithDateTimeCheck(ShopCustomerServiceDTO shopCustomerService, Date prevUpdDt) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        setLastUpdInfo(shopCustomerService);
        int resInt = shopCustomerServiceMybatisDAO.updateSelectiveWithDateTimeCheck(shopCustomerService, prevUpdDt);
        if (resInt > 0) {
            executeResult.setResult(shopCustomerService);
        }

        return executeResult;
    }

    @Override
    public List<ShopCustomerServiceDTO> searchByCondition(ShopCustomerServiceDTO shopCustomerService) {
        shopCustomerService.setDeletedFlag(0);
        return shopCustomerServiceMybatisDAO.searchByCondition(shopCustomerService);
    }


    @Override
    public ExecuteResult<ShopCustomerServiceDTO> updateSelectiveByIdList(ShopCustomerServiceDTO shopCustomerService, List<Long> idList) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        if (idList != null && idList.size() > 0 && shopCustomerService != null) {
            setLastUpdInfo(shopCustomerService);
            shopCustomerServiceMybatisDAO.updateSelectiveByIdList(shopCustomerService, idList);
        }
        executeResult.setResult(shopCustomerService);
        return executeResult;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> updateAllByIdList(ShopCustomerServiceDTO shopCustomerService, List<Long> idList) {
        return null;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> defunctById(Long shopCustomerServiceId) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        ShopCustomerServiceDTO shopCustomerService = new ShopCustomerServiceDTO();
        shopCustomerService.setDeletedFlag(1);
        shopCustomerService.setId(shopCustomerServiceId);
        shopCustomerServiceMybatisDAO.updateSelective(shopCustomerService);
        executeResult.setResult(shopCustomerService);
        return executeResult;
    }

    @Override
    public ExecuteResult<ShopCustomerServiceDTO> defunctByIdList(List<Long> idList) {
        ExecuteResult<ShopCustomerServiceDTO> executeResult = new ExecuteResult<ShopCustomerServiceDTO>();
        ShopCustomerServiceDTO shopCustomerService = new ShopCustomerServiceDTO();
        shopCustomerService.setDeletedFlag(1);
        shopCustomerServiceMybatisDAO.updateSelectiveByIdList(shopCustomerService, idList);
        executeResult.setResult(shopCustomerService);
        return executeResult;
    }

    private void setLastUpdInfo(ShopCustomerServiceDTO shopCustomerService) {

        Date curDate = new Date();

        shopCustomerService.setLastUpdDt(curDate);
    }

}
