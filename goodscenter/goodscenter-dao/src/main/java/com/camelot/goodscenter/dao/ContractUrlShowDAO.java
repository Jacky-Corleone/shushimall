package com.camelot.goodscenter.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractUrlShowDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;


public interface ContractUrlShowDAO extends BaseDAO<ContractUrlShowDAO> {
	
	
	public Integer insert(ContractUrlShowDTO contractUrlShowDTO);
	

    public List<ContractUrlShowDTO> findByContractUrlShowDTO(ContractUrlShowDTO contractUrlShowDTO);
    
    
    public Integer delete(String id);



	
}
