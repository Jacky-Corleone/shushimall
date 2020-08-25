package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.domain.MallAdvertise;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;

public interface MallAdvertiseDAO extends BaseDAO<MallAdvertise>{
	public List<MallAdDTO> queryPage(@Param("entity") MallAdvertise mallAdvertise, @Param("page") Pager page);
	
	public void modifyMallAdStatus(@Param("id")Long id, @Param("status")String status);
	/**
	 * 将定时上架时间小于当前时间的广告上架
	 * @return
	 */
	public Integer autoBatchPublishAD();
	/**
	 * 将定时下架时间小于当前时间的广告下架
	 * @return
	 */
	public Integer autoBatchCancelAD();
	/**
	 * 删除方法
	 * @param 主键id
	 * @return
	 */
	public Integer deleteById(Long id);
	/**
	 * 
	 * <p>Description: [删除广告]</p>
	 * Created on 2015-12-23
	 * @param id
	 * @author 周志军
	 * @return 
	 */
	public void deleteMallAd(@Param("id")Long id);
}
