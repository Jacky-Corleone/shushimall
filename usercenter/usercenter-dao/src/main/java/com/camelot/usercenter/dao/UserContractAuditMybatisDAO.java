package com.camelot.usercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.contract.UserContractAuditDTO;

/**
 * <p>用户合同审核记录数据交互接口</p>
 *  
 * @author - learrings
 * @createDate - 2015-3-6
 **/
public interface UserContractAuditMybatisDAO  extends BaseDAO<UserContractAuditDTO>{
	
	/**
	 * 查询当前合同ID下的审核记录
	 * 
	 * @param id - 合同ID
	 * @param pager - 分页，可空
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<UserContractAuditDTO> selectList(@Param("cid") Long id, @Param("pager") Pager pager);
	/**
	 * 查询当前合同ID下的审核记录条数
	 * 
	 * @param cid - 合同ID
	 * @return
	 */
	public Long selectCount(@Param("cid") Long cid);
}