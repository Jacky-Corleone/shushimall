package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.ContractOrderDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;
import com.camelot.openplatform.common.Pager;

public interface ContractOrderDAO extends BaseDAO<ContractOrderDTO> {

	public List<ContractOrderDTO> queryPage(@Param("pager") Pager pager, @Param("contractOrder") ContractOrderDTO contractOrder);
	
	public Long queryPageCount(@Param("contractOrder") ContractOrderDTO contractOrder);
	
	public ContractOrderDTO findById(Long id);
	
	public Integer insert(ContractOrderDTO contractOrder);
	
	public Integer update(ContractOrderDTO contractOrder);

	public void delete(@Param("codes") List<String> codes);
	
	public List<Map<String,Object>> findAll();

	public ContractOrderDTO findByContractOrderDTO(@Param("contractOrder")ContractOrderDTO contractOrder);
}
