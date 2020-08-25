package com.camelot.basecenter.address;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.AddressBaseDAO;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.PagerModelDTO;
import com.camelot.basecenter.dto.ResultWrapperDTO;
import com.camelot.basecenter.dto.ZtreeNodeDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.openplatform.common.ExecuteResult;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("addressBaseService")
public class AddressBaseServiceImpl implements AddressBaseService {
  private final static Logger logger=LoggerFactory.getLogger(AddressBaseServiceImpl.class);
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月26日
	 * @param parentCode
	 * @return
	 * @author:[创建者中文名字]
	 */
    @Resource
	private AddressBaseDAO addressBaseDAO;
	
	@Override
	public List<AddressBase> queryAddressBase(String parentCode) {
		return addressBaseDAO.queryAddressBase(parentCode);
	}

	  /**
     * <p>Discription:[根据id查找地区名称]</p>
     * Created on 2015年3月5日
     * @param id 编号
     * @return
     * @author:[杨芳]
     */
	
	@Override
	public ExecuteResult<AddressBaseDTO> queryNameById(Integer id) {
		ExecuteResult<AddressBaseDTO> er=new ExecuteResult<AddressBaseDTO>();
		try{
			er.setResult(addressBaseDAO.queryNameById(id));
		}catch(Exception e){
		  logger.error(e.getMessage());
		  throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>Discription:[根据省市县编码查询]</p>
	 * Created on 2015年3月5日
	 * @param code
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<List<AddressBaseDTO>> queryNameByCode(String... code) {
		ExecuteResult<List<AddressBaseDTO>> er=new ExecuteResult<List<AddressBaseDTO>>();
		try{
			er.setResult(addressBaseDAO.queryNameByCode(code));
		}catch(Exception e){
		 logger.error(e.getMessage());
		 throw new RuntimeException(e);
		}
		return er;
	}

	/**
	 * <p>通过<code>areaCode</code>检索完整的地址名称</p>
	 * 
	 * Created on 2016年2月14日
	 * @param areaCode 地址的code
	 * @return 完整的地址名称
	 * @author 顾雨
	 */
	@Override
	public String getQualifiedName(int areaCode) {
		return addressBaseDAO.getQualifiedName(areaCode);
	}

	/**
	 * <p>取得指定{@code level}对应的地址</p>
	 * Created on 2016年2月17日
	 * @param level 区域等级
	 * @return
	 * @author: 顾雨
	 */
	@Override
	public List<AddressBaseDTO> getAddressesByLevel(int level) {
		return addressBaseDAO.getAddressesByLevel(level);
	}

	@Override
	public List<AddressBaseDTO> queryAddressListByName(String name) {
		return addressBaseDAO.queryAddressListByName(name);
	}
	
	//--------------------------------------------------------
	//thinking
	@Override
	@Deprecated 
	public void save(AddressBaseDTO addressBaseDTO) {
		
		if(addressBaseDTO == null){
			return ;
		}
		int id  = addressBaseDTO.getId();
		AddressBase po = new AddressBase();
		try {
			 BeanUtils.copyProperties(addressBaseDTO, po);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			
		}
		
		if(id <=  0){
			addressBaseDAO.add(po);
		}else{
			addressBaseDAO.update(po);
		}
		
	}

	@Override
	public ResultWrapperDTO delete(String code) {
		try {
			addressBaseDAO.delete(code);
		} catch (Exception e) {
			return ResultWrapperDTO.failedResultWraped("").setFlag("delete");
		}
		return ResultWrapperDTO.successResultWraped().setFlag("delete");
	}
	
	
	@Override
	public ResultWrapperDTO newSave(AddressBaseDTO addressBaseDTO) {
		
		if(addressBaseDTO == null){
			return ResultWrapperDTO.failedResultWraped("传入参数为空.");
		}
		String code  = addressBaseDTO.getCode();
		int  id  = addressBaseDTO.getId();
		
		if(id <= 0){
			//XXX: 之前使用手动生成Code的验证
		  /* boolean bool = this.isAddressesCodeExist(code);
			if(bool){
				return ResultWrapperDTO.failedResultWraped("区域编码已经存在,请确认!").setFlag("add");
			}*/
			// 自动生成Code
			String codeKey = System.currentTimeMillis()+"";
			addressBaseDTO.setCode(codeKey);
			addressBaseDAO.newAdd(addressBaseDTO);
			return ResultWrapperDTO.successResultWraped(codeKey,"增加地域信息成功").setFlag("add");
			
		}else{
			addressBaseDAO.newUpdate(addressBaseDTO);
			return ResultWrapperDTO.successResultWraped("修改地域信息成功").setFlag("update");
		}
		
		
	}

	@Override
	public List<AddressBaseDTO> getAddressBaseByparentCode(String parentCode) {
		
		List<AddressBaseDTO>  result   = addressBaseDAO.getChildsByParentCode(parentCode);
		
		return result;
	}

	@Override
	public AddressBaseDTO getAddressBaseByCode(String code) {
		AddressBaseDTO  bean = addressBaseDAO.getByCode(code);
		return bean;
	}
	
	@Override
	public List<ZtreeNodeDTO> getAddressesByLevels(int... levels) {
		
		return addressBaseDAO.getAddressesByLevels(levels);
	}
	
	
	@Override
	public ZtreeNodeDTO getTreeRoot() {
		
		return addressBaseDAO.getTreeRoot();
	}
	
	
	@Override
	public List<ZtreeNodeDTO> getTreeByParentCode(String parentCode) {
		
		return addressBaseDAO.getTreeByParentCode(parentCode);
	}

	@Override
	public PagerModelDTO<AddressBaseDTO> getAddressesWithPage(int pageNow, int pageSize, String parentCode) {
		int start = (pageNow - 1) * pageSize;
		PagerModelDTO<AddressBaseDTO> page  = new PagerModelDTO<AddressBaseDTO>();
		List<AddressBaseDTO> rows  = addressBaseDAO.getAddressesWithPage(start, pageSize, parentCode);
		long count = addressBaseDAO.getAddressesCountByParent(parentCode);
		
		if(pageNow == 1){
			String code  =  parentCode;
			AddressBaseDTO parentNode  = addressBaseDAO.getByCode(code);
			
			if(parentNode != null && parentNode.getCode() != null){
				rows.add(0, parentNode);
			}
			
		}
		page.setRows(rows);
		page.setPageNow(pageNow);
		page.setPageSize(pageSize);
		page.setTotal((int)count);
		
		return page;
	}

	@Override
	public boolean isAddressesCodeExist(String code) {
		int row  = addressBaseDAO.isAddressesCodeExist(code);
		
		return row > 0 ? true : false;
	}

	@Override
	public List<AddressBaseDTO> queryByNameLetter(String nameLetter) {
		return addressBaseDAO.queryByNameLetter(nameLetter);
	}

}
