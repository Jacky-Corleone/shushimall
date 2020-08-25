package com.camelot.maketcenter.service;

import java.util.List;

import com.camelot.maketcenter.dto.CentralPurchasingDTO;
import com.camelot.maketcenter.dto.CentralPurchasingRefEnterpriseDTO;
import com.camelot.maketcenter.dto.EnterpriseSignUpInfoDTO;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.QuerySignUpInfoDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [集采活动]</p>
 * Created on 2015-11-25
 * @author  周志军
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface CentralPurchasingExportService {
	/**
	 * 
	 * <p>Description: [新增集采活动]</p>
	 * Created on 2015-11-25
	 * @author 周志军
	 * @param centralPurchasingDTO
	 * @return 
	 */
	public ExecuteResult<Boolean> addCentralPurchasingActivityDTO(CentralPurchasingDTO centralPurchasingDTO);
	/**
	 * 
	 * <p>Description: [更新集采活动]</p>
	 * Created on 2015-11-25
	 * @author 周志军
	 * @param centralPurchasingDTO 集采详情ID为必传项
	 * @return 
	 */
	public ExecuteResult<Boolean> updateCentralPurchasingActivityDTO(CentralPurchasingDTO centralPurchasingDTO);
	/**
	 * 
	 * <p>Description: [查询集采活动]</p>
	 * Created on 2015-11-25
	 * @author 周志军
	 * @param centralPurchasingDTO
	 * @param page
	 * @return 
	 */
	public ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> queryCentralPurchasingActivityDTO(QueryCentralPurchasingDTO queryCentralPurchasingDTO,Pager page);
	/**
	 * 
	 * <p>Description: [查询集采活动]</p>
	 * Created on 2015-12-1
	 * @author 周志军
	 * @param querySignUpInfoDTO
	 * @param page
	 * @return 
	 */
	public ExecuteResult<DataGrid<QuerySignUpInfoDTO>> querySignUpInfo(QuerySignUpInfoDTO querySignUpInfoDTO,Pager page);


	/**
	 * 
	 * <p>Description: [查询集采活动]</p>
	 * Created on 2015年11月27日
	 * @param queryCentralPurchasingDTO
	 * @param page
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> queryCentralPurchasingActivity(QueryCentralPurchasingDTO queryCentralPurchasingDTO,Pager page);
	/**
	 * 
	 * <p>Description: [查询集采活动]</p>
	 * <p>编辑时，依据集采活动的详情ID反查出集采活动及集采详情</p>
	 * Created on 2015-11-25
	 * @author 周志军
	 * @param centralPurchasingDTO 集采详情ID为必传项
	 * @param page
	 * @return 
	 */
	public QueryCentralPurchasingDTO queryCentralPurchasingByDetailId(QueryCentralPurchasingDTO queryCentralPurchasingDTO);
	
	/**
	 * 
	 * <p>Description: [根据集采活动详情ID查询集采活动]</p>
	 * Created on 2015年12月3日
	 * @param activitesDetailsId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<QueryCentralPurchasingDTO> queryByDetailId(Long activitesDetailsId);
	/**
	 * 
	 * <p>Description: [新增企业报名信息]</p>
	 * Created on 2015-11-25
	 * @author 周志军
	 * @param enterpriseSignUpInfoDTO
	 * @param page
	 * @return 
	 */
	public ExecuteResult<Boolean> addSignUpInfo(EnterpriseSignUpInfoDTO enterpriseSignUpInfoDTO);
	
	/**
	 * 
	 * <p>Description: [查询集采详情]</p>
	 * Created on 2015年12月1日
	 * @param centralPurchasingRefEnterpriseDTO
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> queryCentralPurchasingRefEnterprise(CentralPurchasingRefEnterpriseDTO centralPurchasingRefEnterpriseDTO);
	
	/**
	 * 
	 * <p>Description: [增加已报名人数]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<Boolean> plusSignUpNum(Long activitesDetailsId, Integer num);
	/**
	 * 
	 * <p>Description: [增加已下单人数]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<Boolean> plusPlaceOrderNum(Long activitesDetailsId, Integer num);
	/**
	 * 
	 * <p>Description: [增加已付款人数]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<Boolean> plusPaidNum(Long activitesDetailsId, Integer num);
	/**
	 * 
	 * <p>Description: [检测同一SKU参加的集采活动时间是否重叠]</p>
	 * Created on 2015年12月15日
	 * @param queryCentralPurchasingDTO
	 * @return
	 * @author:[周志军]
	 */
	public ExecuteResult<Boolean> checkUniqueSku(QueryCentralPurchasingDTO queryCentralPurchasingDTO);
	/**
	 * 
	 * <p>Description: [批量删除集采活动]</p>
	 * <p>Description: [活动详情id组，以','分隔]</p>
	 * Created on 2015年12月16日
	 * @param ids
	 * @return
	 * @author:[周志军]
	 */
	public ExecuteResult<Boolean> deleteBatch(String ids);
}
