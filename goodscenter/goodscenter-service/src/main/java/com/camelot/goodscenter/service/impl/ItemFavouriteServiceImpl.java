package com.camelot.goodscenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dao.ItemFavouriteDAO;
import com.camelot.goodscenter.dto.FavouriteCountDTO;
import com.camelot.goodscenter.dto.ItemFavouriteDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: 商品收藏</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("ItemFavouriteService")
public class ItemFavouriteServiceImpl implements ItemFavouriteExportService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private ItemFavouriteDAO favouriteDao;
	@Resource
	private  ItemExportService itemExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	
	/**
	 * <p>Discription:商品收藏</p>
	 * Created on 2015年3月13日
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@Override
	public ExecuteResult<String> add(ItemFavouriteDTO favourite) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try{
			Integer count = this.favouriteDao.list(favourite).size();
			if( count == 0 ){
				this.favouriteDao.add(favourite);
			}
		}catch(Exception e){
			LOG.error("添加商品收藏错误！！", e);
			er.addErrorMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:收藏查询</p>
	 * Created on 2015年3月13日
	 * @param pager
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<ItemFavouriteDTO> datagrid(Pager pager, ItemFavouriteDTO favourite) {
		DataGrid<ItemFavouriteDTO> dg = new DataGrid<ItemFavouriteDTO>();
		
		List<ItemFavouriteDTO> items = this.favouriteDao.queryPage(pager, favourite);
		for (ItemFavouriteDTO itemFavouriteDTO : items) {
			itemFavouriteDTO.setAttributes(this.itemCategoryService.queryCatAttrByKeyVals(itemFavouriteDTO.getSkuAttributeStr()).getResult());
		}
		Integer count = this.favouriteDao.queryPageCount(favourite);
		
		dg.setTotal(Long.valueOf(count));
		dg.setRows(items);
		
		return dg;
	}

	/**
	 * <p>Discription:删除收藏</p>
	 * Created on 2015年3月13日
	 * @param id
	 * @param uid
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@Override
	public ExecuteResult<String> del(String id,String uid) {
		
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			this.favouriteDao.del(id,uid);
		} catch (Exception e) {
			LOG.error("删除收藏商品错误！",e);
			er.addErrorMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<FavouriteCountDTO>> queryFavouriteCount(Long shopId,Pager pager) {
		 ExecuteResult<DataGrid<FavouriteCountDTO>> result=new ExecuteResult<DataGrid<FavouriteCountDTO>>();
		 
		 try {
			DataGrid<FavouriteCountDTO> dataGrid=new DataGrid<FavouriteCountDTO>();
			 List<FavouriteCountDTO> fclist=new ArrayList<FavouriteCountDTO>();
			 
			 
			 List<ItemFavouriteDTO> list= favouriteDao.queryFavouriteCount(shopId,pager);
			 Long count=favouriteDao.queryCountFavouriteCount(shopId);
			 
			
			
			 for (ItemFavouriteDTO itemFavouriteDTO : list) {
				 FavouriteCountDTO fc=new FavouriteCountDTO();
				 ItemShopCartDTO skuDto=new ItemShopCartDTO();
				 skuDto.setItemId(Long.valueOf(itemFavouriteDTO.getItemId()));
				 skuDto.setSkuId(Long.valueOf(itemFavouriteDTO.getSkuId()));
				 skuDto.setShopId(Long.valueOf(itemFavouriteDTO.getShopId()));
				 ExecuteResult<ItemShopCartDTO> item = itemExportService.getSkuShopCart(skuDto);
				 fc.setItemShopCartDTO(item.getResult());
				 fc.setFavouriteCount(itemFavouriteDTO.getFavouriteCount());
				 fclist.add(fc);
			}
			 dataGrid.setRows(fclist);
			 dataGrid.setTotal(count);
			 result.setResult(dataGrid);
			 result.setResultMessage("success");
		} catch (Exception e) {
			LOG.error(e.getMessage());
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			throw new RuntimeException();
		}
	
		 
		return result;
	}
	

}
