package com.camelot.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.FactorageJournal;

/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface FactorageJournalDAO  extends BaseDAO<FactorageJournal>{
	
	/**
	 * 根据订单号查询
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	List<FactorageJournal> selectByOrderNoAndStatus(@Param("orderNo")String orderNo , @Param("status") Integer status);
	
	/**
	 * 根据ID修改状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	int updateStatusById(@Param("id")Long id,@Param("status")int status);
}