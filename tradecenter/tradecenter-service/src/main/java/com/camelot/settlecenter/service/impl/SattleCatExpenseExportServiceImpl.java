package com.camelot.settlecenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.outdto.QueryChildCategoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.settlecenter.dao.SettlementCategoryExpenseMybatisDAO;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;


@Service("sattleCatExpenseExportService")
public class SattleCatExpenseExportServiceImpl implements SattleCatExpenseExportService {
	private final static Logger logger = LoggerFactory.getLogger(SattleCatExpenseExportServiceImpl.class);
	@Resource
	private SettlementCategoryExpenseMybatisDAO settlementCategoryExpenseMybatisDAO;
	@Resource
	private ItemCategoryService itemCategoryService;
	/**
	 * <p>Discription:[类目费用列表查询]</p>
	 * Created on 2015年3月9日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */

	@Override
	public ExecuteResult<DataGrid<SettleCatExpenseDTO>> queryCategoryExpenseList(SettleCatExpenseDTO dto,@SuppressWarnings("rawtypes") Pager pager) {
	 ExecuteResult<DataGrid<SettleCatExpenseDTO>> result=new ExecuteResult<DataGrid<SettleCatExpenseDTO>>();
	 try {
		//根据父id查询子id的集合
		 if(dto.getCategoryId()!=null && dto.getCategoryId()>0){
			 ItemCategoryDTO itemCategoryDTO=new ItemCategoryDTO();
			 itemCategoryDTO.setCategoryCid(dto.getCategoryId());
			 ExecuteResult<QueryChildCategoryOutDTO> queryChildCategory = itemCategoryService.queryAllChildCategory(itemCategoryDTO);
			 if(queryChildCategory.isSuccess()){
				 dto.setChildCategorys(queryChildCategory.getResult().getChildCategorys());
			 }
		 }
		DataGrid<SettleCatExpenseDTO> dg=new DataGrid<SettleCatExpenseDTO>();
		 dg.setRows(settlementCategoryExpenseMybatisDAO.queryList(dto, pager));
		 dg.setTotal(settlementCategoryExpenseMybatisDAO.queryCount(dto));
		 result.setResult(dg);
		 result.setResultMessage("success");
	} catch (Exception e) {
		result.setResultMessage("error");
		result.getErrorMessages().add(e.getMessage());
		logger.error(e.getMessage());
		throw new RuntimeException(e);
	}
	return result;
	}
	/**
	 * <p>Discription:[根据类目id或者类目id数组查询]</p>
	 * Created on 2015年3月9日
	 * @param ids
	 * @return
	 * @author:[杨芳]
	 */
	
	@Override
	public ExecuteResult<List<SettleCatExpenseDTO>> queryByIds(Long... ids) {
		ExecuteResult<List<SettleCatExpenseDTO>> result=new ExecuteResult<List<SettleCatExpenseDTO>>();
		try{
			result.setResult(settlementCategoryExpenseMybatisDAO.queryByIds(ids));
			result.setResultMessage("success");
		}catch(Exception e){
			result.setResultMessage("error");
			result.getErrorMessages().add(e.getMessage());
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * <p>Discription:[插入类目费用]</p>
	 * Created on 2015年3月9日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	
	@Override
	public ExecuteResult<String> insertCategoryExpense(SettleCatExpenseDTO dto) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		try{
			List<SettleCatExpenseDTO> list=settlementCategoryExpenseMybatisDAO.queryByIds(dto.getCategoryId());
			if(list == null || list.size()==0){
				settlementCategoryExpenseMybatisDAO.add(dto);
				result.setResultMessage("succes");	
			}else{
				result.setResultMessage("该类目已存在");	
			}
		}catch(Exception e){
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	/**
	 * <p>Discription:[根据id删除]</p>
	 * Created on 2015年3月9日
	 * @param id
	 * @return
	 * @author:[杨芳]
	 */
	@Override
	public ExecuteResult<String> deleteById(long id) {
	  ExecuteResult<String> result=new ExecuteResult<String>();
	  
	  try {
		   Integer count = settlementCategoryExpenseMybatisDAO.delete(id);
		   result.setResult(count.toString());
		   result.setResultMessage("success");
	} catch (Exception e) {
		result.setResultMessage("error");
		result.getErrorMessages().add(e.getMessage());
		logger.error(e.getMessage());
		throw new RuntimeException(e);
	}
		return result;
	}
	/**
	 * <p>Discription:[修改类目费用]</p>
	 * Created on 2015年3月9日
	 * @param dto
	 * @return
	 * @author:[杨芳]
	 */
	
	@Override
	public ExecuteResult<String> modifyCategoryExpense(SettleCatExpenseDTO dto) {
	 ExecuteResult<String> result=new ExecuteResult<String>();
	 try {
		SettleCatExpenseDTO scd=settlementCategoryExpenseMybatisDAO.queryById(dto.getId());
		 if(scd==null){
			 result.setResultMessage("该类目不存在！");
		  }else {
			  if(dto.getCategoryId()!=null&&dto.getCategoryId()!=0L){scd.setCategoryId(dto.getCategoryId());}
			  if(dto.getCashDeposit()!=null){scd.setCashDeposit(dto.getCashDeposit());}
			  if(dto.getCreated()!=null){scd.setCreated(dto.getCreated()); }
			  if(dto.getRebateRate()!=null){scd.setRebateRate(dto.getRebateRate());} 
			  if(dto.getServiceFee()!=null){scd.setServiceFee(dto.getServiceFee());}
			  Integer count = settlementCategoryExpenseMybatisDAO.update(scd);
			  result.setResult(count.toString());
			  result.setResultMessage("success");
		  }
	} catch (Exception e) {
		result.setResultMessage("error");
		result.getErrorMessages().add(e.getMessage());
		logger.error(e.getMessage());
		throw new RuntimeException(e);
	}
     return result;
	}

}
