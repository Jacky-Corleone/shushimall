package com.camelot.storecenter.service.impl;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopCustomerServiceMybatisDAO;
import com.camelot.storecenter.dto.ShopAndCustomerDTO;
import com.camelot.storecenter.dto.ShopAndCustomerQueryDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopAndCustomerService;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.storecenter.service.ShopExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 业务逻辑实现类
 * </p>
 *
 * @author
 * @createDate
 */
@Service("shopAndCustomerService")
public class ShopAndCustomerServiceImpl implements ShopAndCustomerService {
    private final static Logger logger = LoggerFactory.getLogger(ShopAndCustomerServiceImpl.class);
    @Resource
    private ShopExportService shopExportService;
    @Resource
    private ShopCustomerServiceService shopCustomerServiceService;
    
    @Resource
    private ShopCustomerServiceMybatisDAO shopCustomerServiceMybatisDAO;


    @Override
    public DataGrid<ShopAndCustomerDTO> searchShopAndCustomer(Pager<ShopDTO> pager, ShopAndCustomerQueryDTO shopAndCustomerQueryDTO) {
        DataGrid<ShopAndCustomerDTO> res = new DataGrid<ShopAndCustomerDTO>();

        ShopDTO queryShop = new ShopDTO();
        queryShop.setShopId(shopAndCustomerQueryDTO.getShopId());
        queryShop.setShopName(shopAndCustomerQueryDTO.getShopName());
        queryShop.setStatus(5); //开通得店铺
        ExecuteResult<DataGrid<ShopDTO>> shopRes = shopExportService.findShopInfoByCondition(queryShop, pager);
        if (shopRes != null) {
            DataGrid<ShopDTO> shopData = shopRes.getResult();
            List<ShopDTO> shopList = shopData.getRows();
            if (shopList != null && shopList.size() > 0) {
                List<ShopAndCustomerDTO> resList = new ArrayList<ShopAndCustomerDTO>();
                for (ShopDTO item : shopList) {
                    ShopAndCustomerDTO itemRes = new ShopAndCustomerDTO();

                    ShopCustomerServiceDTO shopCustomerService = new ShopCustomerServiceDTO();
                    shopCustomerService.setShopId(item.getShopId());
                    shopCustomerService.setNumber(shopAndCustomerQueryDTO.getCustomerNum());
                 //   shopCustomerService.setStationId(shopAndCustomerQueryDTO.getStationId());
                    List<ShopCustomerServiceDTO> customerList = shopCustomerServiceService.searchByCondition(shopCustomerService);

                    itemRes.setShopName(item.getShopName());
                    itemRes.setShopId(item.getShopId());
                    itemRes.setCustomerList(customerList);
                    resList.add(itemRes);

                }
                res.setRows(resList);
                res.setTotal(shopData.getTotal());
            }
        }

        return res;
    }

    @Override
    public List<ShopAndCustomerDTO> searchShopAndCustomerByCondition(ShopAndCustomerQueryDTO shopAndCustomerQueryDTO) {
        List<ShopAndCustomerDTO> res = new ArrayList<ShopAndCustomerDTO>();

        ShopDTO queryShop = new ShopDTO();
        queryShop.setShopId(shopAndCustomerQueryDTO.getShopId());
        queryShop.setShopName(shopAndCustomerQueryDTO.getShopName());
        queryShop.setStatus(5); //开通得店铺
        Pager<ShopDTO> pager = new Pager<ShopDTO>();
        pager.setRows(99);
        ExecuteResult<DataGrid<ShopDTO>> shopRes = shopExportService.findShopInfoByCondition(queryShop, pager);
        if (shopRes != null) {
            DataGrid<ShopDTO> shopData = shopRes.getResult();
            List<ShopDTO> shopList = shopData.getRows();
            if (shopList != null && shopList.size() > 0) {

                for (ShopDTO item : shopList) {
                    ShopAndCustomerDTO itemRes = new ShopAndCustomerDTO();

                    ShopCustomerServiceDTO shopCustomerService = new ShopCustomerServiceDTO();
                    shopCustomerService.setShopId(item.getShopId());
                    shopCustomerService.setNumber(shopAndCustomerQueryDTO.getCustomerNum());
                    List<ShopCustomerServiceDTO> customerList = shopCustomerServiceService.searchByCondition(shopCustomerService);

                    itemRes.setShopName(item.getShopName());
                    itemRes.setShopId(item.getShopId());
                    itemRes.setCustomerList(customerList);
                    res.add(itemRes);

                }

            }
        }

        return res;
    }

	@Override
	public DataGrid<ShopCustomerServiceDTO> searchShopCustomer(
			Pager<ShopCustomerServiceDTO> pager,
			ShopAndCustomerQueryDTO shopAndCustomerQueryDTO) {
		DataGrid<ShopCustomerServiceDTO> dto=new DataGrid<ShopCustomerServiceDTO>(); 
		List<ShopCustomerServiceDTO> lists=shopCustomerServiceMybatisDAO.selectShopAndCustomer(shopAndCustomerQueryDTO,pager);
		long count=shopCustomerServiceMybatisDAO.selectShopAndCustomerCount(shopAndCustomerQueryDTO);
		dto.setRows(lists);
		dto.setTotal(count);
		return  dto;
	}
}
