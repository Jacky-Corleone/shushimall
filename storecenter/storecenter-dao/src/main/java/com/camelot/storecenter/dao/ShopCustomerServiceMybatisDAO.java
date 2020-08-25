package com.camelot.storecenter.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopAndCustomerQueryDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface ShopCustomerServiceMybatisDAO  extends BaseDAO<ShopCustomerServiceDTO> {
	public void deleteAll(@Param("idList") List<Long> idList);
	public int updateSelective(@Param("shopCustomerService") ShopCustomerServiceDTO shopCustomerService);
	public int updateAllWithDateTimeCheck(@Param("shopCustomerServiceDTO") ShopCustomerServiceDTO shopCustomerService, @Param("prevUpdDt") Date prevUpdDt);
	public int updateSelectiveWithDateTimeCheck(@Param("shopCustomerServiceDTO") ShopCustomerServiceDTO shopCustomerService, @Param("prevUpdDt") Date prevUpdDt);
	public List<ShopCustomerServiceDTO> searchByCondition(@Param("entity") ShopCustomerServiceDTO shopCustomerService);
	public long updateSelectiveByIdList(@Param("shopCustomerServiceDTO") ShopCustomerServiceDTO shopCustomerService, @Param("idList") List<Long> idList);
	public long updateAllByIdList(@Param("shopCustomerServiceDTO") ShopCustomerServiceDTO shopCustomerService, @Param("idList") List<Long> idList);
    public List<ShopCustomerServiceDTO>selectShopAndCustomer(@Param("entity") ShopAndCustomerQueryDTO ShopAndCustomerQueryDto,@Param("page") Pager page);
    public long selectShopAndCustomerCount(@Param("entity") ShopAndCustomerQueryDTO ShopAndCustomerQueryDto);

}