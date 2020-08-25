package com.camelot.payment.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.payment.domain.AccountInfo;

/**
 * <p>中信账户管理数据交互接口</p>
 *  
 *  @author learrings
 *  @createDate 
 **/
public interface AccountInfoDAO  extends BaseDAO<AccountInfo>{
	
	/**
	 * 根据附属账户查询
	 * @param outTradeNo
	 * @return
	 */
	public AccountInfo selectBySubAccNo(@Param("subAccNo") String subAccNo);
	
	/**
	 * 根据用户ID查询指定类型的附属账户是否已创建（审核通过、待审核、待生成）
	 * @param outTradeNo
	 * @return
	 */
	public AccountInfo selectByUId(@Param("uid")Long uid,@Param("accountType")Integer accountType);
	
	/**
	 * 查询所选用户组的账户信息(审核通过)
	 * 
	 * @param listUId - 用户ID组
	 * @param userType - 用户类型 2[买家]/3[卖家]，可空
	 * @return
	 */
	public List<AccountInfo> selectByUIds(@Param("listUId")List<Long> listUId,@Param("accountTypes")Integer[] accountTypes);
	
	/**
	 * 查询5分钟前，60分钟以内的指定状态的账号(超过五次尝试将不再生成)
	 * 
	 * @param accStatus -0 待生成 1待审核 2审核通过 3审核拒绝 4注销
	 * @return
	 */
	public List<AccountInfo> selectAccByStatus(@Param("accStatus")int accStatus);
	
}