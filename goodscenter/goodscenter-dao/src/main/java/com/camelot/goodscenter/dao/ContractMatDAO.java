package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.ContractMatDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;
import com.camelot.openplatform.common.Pager;

public interface ContractMatDAO extends BaseDAO<ContractMatDTO> {

	public List<Map> queryPage(@Param("pager") Pager pager, @Param("contractMat") ContractMatDTO contractMat);
	
	public Long queryPageCount(@Param("contractMat") ContractMatDTO contractMat);
	
	public ContractMatDTO findById(Long id);
	
	public Integer insert(ContractMatDTO contractMat);
	
	public Integer update(ContractMatDTO contractMat);

	public void delete(@Param("codes") List<String> codes);
	
	public List<ContractMatDTO> findAll(@Param("contractMat")ContractMatDTO contractMat);

	public ContractMatDTO findByContractMatDTO(@Param("contractMat")ContractMatDTO contractMat);
}
