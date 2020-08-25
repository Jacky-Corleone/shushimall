package com.camelot.maketcenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.centralPurchasing.domain.CentralPurchasingRefEnterprise;
import com.camelot.maketcenter.dto.QuerySignUpInfoDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
/**
 * 集采活动
 * @author 周志军
 *
 */
public interface CentralPurchasingRefEnterpriseDAO extends BaseDAO<CentralPurchasingRefEnterprise> {
	/**
	 * 
	 * <p>Description: [查询报名信息]</p>
	 * Created on 2015年12月1日
	 * @param querySignUpInfoDTO
	 * @param page
	 * @return
	 * @author:[周志军]
	 */
	List<QuerySignUpInfoDTO> querySignUpInfo(
			@Param("entity") QuerySignUpInfoDTO querySignUpInfoDTO, @Param("page") Pager page);
	/**
	 * 
	 * <p>Description: [查询数量]</p>
	 * Created on 2015年12月1日
	 * @param querySignUpInfoDTO
	 * @return
	 * @author:[周志军]
	 */
	Long querySignUpInfoCount(@Param("entity") QuerySignUpInfoDTO querySignUpInfoDTO);
}
