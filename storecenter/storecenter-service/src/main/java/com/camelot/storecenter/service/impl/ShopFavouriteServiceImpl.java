package com.camelot.storecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dao.ShopFavouriteDAO;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.service.ShopFavouriteExportService;

/** 
 * <p>Description: 店铺收藏</p>
 * Created on 2015年3月13日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("shopFavouriteService")
public class ShopFavouriteServiceImpl implements ShopFavouriteExportService {
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private ShopFavouriteDAO shopDao;
	/**
	 * <p>Discription:收藏</p>
	 * Created on 2015年3月13日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@Override
	public ExecuteResult<String> add(ShopFavouriteDTO favourite) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			Integer count = this.shopDao.list(favourite).size();
			if( count == 0 ){
				this.shopDao.add(favourite);
			}
		} catch (Exception e) {
			LOG.error("店铺收藏错误！",e);
			er.addErrorMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:收藏查询</p>
	 * Created on 2015年3月13日
	 * @param pager
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@SuppressWarnings("rawtypes")
	@Override
	public DataGrid<ShopFavouriteDTO> datagrid(Pager pager, ShopFavouriteDTO favourite) {
		DataGrid<ShopFavouriteDTO> dg = new DataGrid<ShopFavouriteDTO>();
		List<ShopFavouriteDTO> shops = this.shopDao.queryPage(pager, favourite);
		Integer total = this.shopDao.queryPageCount(favourite);
		
		dg.setRows(shops);
		dg.setTotal(Long.valueOf(total));
		
		return dg;
	}

	/**
	 * <p>Discription:收藏删除</p>
	 * Created on 2015年3月13日
	 * @param favourite
	 * @return
	 * @author:[Goma 郭茂茂]
	 */

	@Override
	public ExecuteResult<String> del(String id,String uid) {
		ExecuteResult<String> er = new ExecuteResult<String>();
		try {
			this.shopDao.del(id,uid);
		} catch (Exception e) {
			LOG.error("删除店铺收藏错误！",e);
			er.addErrorMessage(e.getMessage());
			throw new RuntimeException(e);
		}
		return er;
	}

}
