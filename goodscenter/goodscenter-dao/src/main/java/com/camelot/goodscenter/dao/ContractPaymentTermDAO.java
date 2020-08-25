package com.camelot.goodscenter.dao;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import org.apache.ibatis.annotations.Param;

public interface ContractPaymentTermDAO extends BaseDAO<ContractPaymentTermDTO> {

	public List<ContractPaymentTermDTO> queryPage(@Param("pager") Pager pager, @Param("contractPaymentTerm") ContractPaymentTermDTO contractPaymentTerm);
	
	public Long queryPageCount(@Param("contractPaymentTerm") ContractPaymentTermDTO contractPaymentTerm);
	
	public ContractPaymentTermDTO findById(Integer id);
	
	public Integer insert(ContractPaymentTermDTO contractPaymentTerm);
	
	public Integer update(ContractPaymentTermDTO contractPaymentTerm);

	public void delete(@Param("codes") List<String> codes);
	
	public List<Map<String,Object>> findAll();

	public ContractPaymentTermDTO findByContractPaymentTermDTO(@Param("contractPaymentTerm")ContractPaymentTermDTO contractPaymentTerm);

    public ContractPaymentTermDTO findByContractNo(String contractNo);
}
