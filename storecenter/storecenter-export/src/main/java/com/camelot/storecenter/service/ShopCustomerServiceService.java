package com.camelot.storecenter.service;


import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.storecenter.dto.ShopAndCustomerDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import java.util.Date;
import java.util.List;
public interface ShopCustomerServiceService{


    /**
     *  创建对象
     */
    public ExecuteResult<ShopCustomerServiceDTO> createShopCustomerServiceDTO(ShopCustomerServiceDTO shopCustomerService);

    /**
     * 更新对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>updateSelective(ShopCustomerServiceDTO shopCustomerService);

    /**
     * 分页查询
     */
    public DataGrid<ShopCustomerServiceDTO> searchShopCustomerServiceDTOs(Pager<ShopCustomerServiceDTO> pager, ShopCustomerServiceDTO shopCustomerService);

    /**
     * 根据ID查询对象
     */
    public ShopCustomerServiceDTO getShopCustomerServiceDTOById(Long shopCustomerServiceId);

    /**
     * 根据条件查询
     */
    public List<ShopCustomerServiceDTO> searchByCondition(ShopCustomerServiceDTO shopCustomerService);

    /**
     * 根据ID删除对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>deleteById(Long shopCustomerServiceId);




    /**
     *  更新对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>updateShopCustomerServiceDTO(ShopCustomerServiceDTO shopCustomerService);

    /**
     * 删除所有对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>deleteAll(List<Long> idList);

    /**
     * 根据时间戳更新对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>updateSelectiveWithDateTimeCheck(ShopCustomerServiceDTO shopCustomerService, Date prevUpdDt)throws CommonCoreException;

    /**
     * 根据IDLIST 更新对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>updateSelectiveByIdList(ShopCustomerServiceDTO shopCustomerService, List<Long> idList);
    /**
     * 根据ID更新所有数据
     */
    public ExecuteResult<ShopCustomerServiceDTO>updateAllByIdList(ShopCustomerServiceDTO shopCustomerService, List<Long> idList);
    /**
     * 逻辑删除对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>defunctById(Long shopCustomerServiceId);
    /**
     * 批量逻辑删除对象
     */
    public ExecuteResult<ShopCustomerServiceDTO>defunctByIdList(List<Long> idList);
}
