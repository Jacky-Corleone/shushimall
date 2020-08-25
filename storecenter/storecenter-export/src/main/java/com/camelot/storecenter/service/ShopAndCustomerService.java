package com.camelot.storecenter.service;


import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.storecenter.dto.ShopAndCustomerDTO;
import com.camelot.storecenter.dto.ShopAndCustomerQueryDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;

import java.util.Date;
import java.util.List;

public interface ShopAndCustomerService {
    /**
     * 分页 查询返回 商铺信息 以及客服列表
     */
    public DataGrid<ShopAndCustomerDTO> searchShopAndCustomer(Pager<ShopDTO> pager, ShopAndCustomerQueryDTO shopAndCustomerQueryDTO);


    /**
     *  按照条件 查询 店铺以及 客服信息
     */
    public List<ShopAndCustomerDTO> searchShopAndCustomerByCondition(ShopAndCustomerQueryDTO shopAndCustomerQueryDTO);
    
    /**
     * 分页查询客服信息
     */
    public DataGrid<ShopCustomerServiceDTO> searchShopCustomer(Pager<ShopCustomerServiceDTO> pager, ShopAndCustomerQueryDTO shopAndCustomerQueryDTO);

}
