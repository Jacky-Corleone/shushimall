package com.camelot.maketcenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.centralPurchasing.domain.CentralPurchasingActivitesDetails;
import com.camelot.openplatform.dao.orm.BaseDAO;
/**
 * 集采活动
 * @author 周志军
 *
 */
public interface CentralPurchasingActivitesDetailsDAO extends BaseDAO<CentralPurchasingActivitesDetails> {
	
	/**
	 * 
	 * <p>Description: [增加已报名人数]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @param num
	 * @author:[宋文斌]
	 */
	public void plusSignUpNum(@Param("activitesDetailsId") Long activitesDetailsId, @Param("num") Integer num);
	/**
	 * 
	 * <p>Description: [增加已下单人数]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @param num
	 * @author:[宋文斌]
	 */
	public void plusPlaceOrderNum(@Param("activitesDetailsId") Long activitesDetailsId, @Param("num") Integer num);
	/**
	 * 
	 * <p>Description: [增加已付款人数]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @param num
	 * @author:[宋文斌]
	 */
	public void plusPaidNum(@Param("activitesDetailsId") Long activitesDetailsId, @Param("num") Integer num);
}
