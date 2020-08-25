package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.contract.UserContractAuditDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
/**
 *  用户合同对外接口
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-4
 */
@SuppressWarnings("rawtypes")
public interface UserContractService {
	
	/**
	 * 根据合同ID修改合同信息
	 *
	 * @param userContractDTO - 合同信息
	 * @return 
	 */
	public ExecuteResult<UserContractDTO> modifyUserContractById(UserContractDTO userContractDTO,String currentUserId);
	
	/**
	 * 根据合同ID查询用户合同信息，含审核记录列表
	 *
	 * @param id - 合同ID
	 * @param pager - 合同审核历史记录分页信息，可空
	 * @return 
	 */
	public UserContractDTO findUserContractById(Long id,Pager pager);
	
	/**
	 * 根据条件查询用户合同信息组
	 *
	 * @param userContract - 查询条件，可空
	 * @param pager - 分页，可空
	 * @return 
	 */
	public DataGrid<UserContractDTO> findListByCondition(UserContractDTO userContract,Pager pager);
	
	/**
	 * 根据合同ID查询合同审核记录列表
	 *
	 * @param id - 合同ID
	 * @param pager - 分页，可空
	 * @return 
	 */
	public DataGrid<UserContractAuditDTO> findAuditListByCId(Long cid,Pager pager);
	
	/**
	 * 根据条件查询用户合同信息组
	 *
	 * @param userContract - 查询条件，可空
	 * @param pager - 
	 * @return 
	 */
	public ExecuteResult<UserContractDTO> findUserContractByCondition(UserContractDTO userContract);
	
	/**
	 * 
	 * <p>Discription:[创建或者更新 用户合同]</p>
	 * Created on 2015-3-20
	 * @param userContract
	 * @return
	 * @author:[liuqingshan]
	 */
	public ExecuteResult<UserContractDTO> createOrUpdateContract(UserContractDTO userContract);

    public ExecuteResult<String> deleteUserContractById(Long id);
	
}
