package com.camelot.maketcenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.maketcenter.dao.VipCardDAO;
import com.camelot.maketcenter.dto.VipCardDTO;
import com.camelot.maketcenter.service.VipCardService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * vip卡服务
 * @author zhangzq
 *
 */
@Service("vipCardService")
public class VipCardServiceImpl implements VipCardService {
	private static final Logger LOGGER=LoggerFactory
			.getLogger(VipCardServiceImpl.class);
	
	@Resource
	private VipCardDAO vipCardDAO;
	
	/**
	 * <p>Discription:查询vip卡集合</p>
	 * Created on 2015年3月13日
	 * @param dto vip卡对应实体，可封装查询条件
	 * @return vip卡集合
	 * @author:[ zhangzq]
	 */
	@Override
	public ExecuteResult<DataGrid<VipCardDTO>> queryVipCardList(
			VipCardDTO vipCardDTO, Pager page) {
		ExecuteResult<DataGrid<VipCardDTO>> result = new ExecuteResult<DataGrid<VipCardDTO>>();
		try {
			DataGrid<VipCardDTO> dg = new DataGrid<VipCardDTO>();
			List<VipCardDTO> vipCardDTOList = this.vipCardDAO.queryList(vipCardDTO, page);
			dg.setRows(vipCardDTOList);
			result.setResult(dg);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			LOGGER.error("查询vip卡",e);
		}
		return result;
	}
	
	/**
	 * <p>Discription:查询vip卡单条数据</p>
	 * Created on 2015年3月13日
	 * @param dto vip卡对应实体，可封装查询条件
	 * @return vip卡实体
	 * @author:[ zhangzq]
	 */
	@Override
	public ExecuteResult<VipCardDTO> queryVipCard(VipCardDTO vipCardDTO) {
		ExecuteResult<VipCardDTO> result = new ExecuteResult<VipCardDTO>();
		try {
			List<VipCardDTO> vipCardDTOList = this.vipCardDAO.queryList(vipCardDTO,null);
			if(vipCardDTOList != null && vipCardDTOList.size() > 0){
				result.setResult(vipCardDTOList.get(0));
			}
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			LOGGER.error("查询vip卡",e);
		}
		return result;
	}
	
	/**
	 * 查询vipCard表中数据总条数
	 * @param vipCardDTO vip卡实体
	 * @return int 总条数
	 */
	@Override
	public ExecuteResult<Long> queryCountVipCard(VipCardDTO vipCardDTO) {
		ExecuteResult<Long> result = new ExecuteResult<Long>();
		try {
			Long count = this.vipCardDAO.queryCount(vipCardDTO);
			result.setResult(count);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			LOGGER.error("查询vip卡",e);
		}
		return result;
	}

	@Override
	public ExecuteResult<VipCardDTO> updateVipCard(VipCardDTO vipCardDTO) {
		ExecuteResult<VipCardDTO> result = new ExecuteResult<VipCardDTO>();
		try{
			if(vipCardDTO.getId()==null){
				result.addErrorMessage("vip卡id不能为空");
			}
			this.vipCardDAO.update(vipCardDTO);
			List<VipCardDTO> vipCardDTOList = this.vipCardDAO.queryList(vipCardDTO,null);
			if(vipCardDTOList != null && vipCardDTOList.size() > 0){
				result.setResult(vipCardDTOList.get(0));
			}
		}catch(Exception e){
			result.addErrorMessage(e.getMessage());
			LOGGER.error("查询vip卡",e);
		}
		return result;
	}

}
