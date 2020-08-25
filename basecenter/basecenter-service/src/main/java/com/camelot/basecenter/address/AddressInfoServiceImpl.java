package com.camelot.basecenter.address;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.AddressInfoDAO;
import com.camelot.basecenter.domain.AddressInfo;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressInfoService;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("addressInfoService")
public class AddressInfoServiceImpl implements AddressInfoService {
	private final static Logger LOGGER = LoggerFactory.getLogger(AddressInfoServiceImpl.class);
    @Resource
	private AddressInfoDAO addressInfoDAO;
    
	/**
	 * <p>Discription:[收货人地址列表查询]</p>
	 * Created on 2015年1月26日
	 * @param buyerId
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public DataGrid<AddressInfoDTO> queryAddressinfo(long buyerId) {
		DataGrid<AddressInfoDTO>  dg=new DataGrid<AddressInfoDTO> ();
		dg.setRows(addressInfoDAO.queryAddressinfo(buyerId));
		dg.setTotal(addressInfoDAO.queryAddressinfoCount(buyerId));
		return dg;
	}
	/**
	 * <p>Discription:[收货人地址详情查询]</p>
	 * Created on 2015年3月10日
	 * @param buyerId
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<AddressInfoDTO> queryAddressinfoById(long id){
		ExecuteResult<AddressInfoDTO> er=new ExecuteResult<AddressInfoDTO>();
		try{
		    er.setResult(addressInfoDAO.queryById(id));
		    er.setResultMessage("查询详情成功！");
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			er.setResultMessage("查询详情失败！");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[收货人地址添加]</p>
	 * Created on 2015年1月26日
	 * @param addressInfoDTO
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<String> addAddressInfo(AddressInfoDTO addressInfoDTO) {
		 ExecuteResult<String> er=new  ExecuteResult<String>();
		 try { 
		  Integer isDefault= addressInfoDTO.getIsdefault();
		  if(isDefault == 1){
			  addressInfoDAO.updateIsDefault(addressInfoDTO.getBuyerid());    
		  }
		  addressInfoDAO.add(addressInfoDTO);  
		  er.setResult("添加成功!");
		 } catch (Exception e) {
			LOGGER.error(e.getMessage());
			er.setResult("添加失败！");
			throw new RuntimeException(e);
		}
		return er;
	}
	/**
	 * <p>Discription:[收货地址修改]</p>
	 * Created on 2015年1月28日
	 * @param addressInfoDTO
	 * @return ExecuteResult
	 * @author:[周立明]
	 */
	@Override
	public ExecuteResult<String> modifyAddressInfo(AddressInfoDTO addressInfoDTO){
		
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			addressInfoDAO.update(addressInfoDTO);
			result.setResultMessage("修改成功！");
		} catch (Exception e) {
			result.setResultMessage("修改失败！");
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * <p>Discription:[收货地址删除]</p>
	 * Created on 2015年1月28日
	 * @param id
	 * @return ExecuteResult
	 * @author:[周立明]
	 */
	@Override
	public ExecuteResult<String> removeAddresBase(Long id){
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			addressInfoDAO.delete(id);
			result.setResultMessage("删除成功！");
		} catch (Exception e) {
			result.setResultMessage("删除失败！");
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * <p>Discription:[设置默认收货地址]</p>
	 * Created on 2015年1月28日
	 * @param id
	 * @return ExecuteResult
	 * @author:[周立明]
	 */
	@Override
	public ExecuteResult<String> modifyDefaultAddress(Long id,long buyerId){
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			addressInfoDAO.updateIsDefault(buyerId);
			addressInfoDAO.updateIsDefaultById(id);
			result.setResultMessage("修改成功！");
		} catch (Exception e) {
			result.setResultMessage("修改失败！");
			throw new RuntimeException(e);
		}
		return result;
	}
}
