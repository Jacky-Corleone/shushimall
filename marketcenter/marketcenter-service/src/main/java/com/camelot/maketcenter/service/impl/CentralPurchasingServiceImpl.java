package com.camelot.maketcenter.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.centralPurchasing.domain.CentralPurchasingActivites;
import com.camelot.centralPurchasing.domain.CentralPurchasingActivitesDetails;
import com.camelot.centralPurchasing.domain.CentralPurchasingEnterprise;
import com.camelot.centralPurchasing.domain.CentralPurchasingRefEnterprise;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.maketcenter.dao.CentralPurchasingActivitesDAO;
import com.camelot.maketcenter.dao.CentralPurchasingActivitesDetailsDAO;
import com.camelot.maketcenter.dao.CentralPurchasingEnterpriseDAO;
import com.camelot.maketcenter.dao.CentralPurchasingRefEnterpriseDAO;
import com.camelot.maketcenter.dto.CentralPurchasingDTO;
import com.camelot.maketcenter.dto.CentralPurchasingDetails;
import com.camelot.maketcenter.dto.CentralPurchasingRefEnterpriseDTO;
import com.camelot.maketcenter.dto.EnterpriseSignUpInfoDTO;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.QuerySignUpInfoDTO;
import com.camelot.maketcenter.dto.SignUpRefPurchasingDetail;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
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
@Service("centralPurchasingServiceImpl")
public class CentralPurchasingServiceImpl implements
		CentralPurchasingExportService {

	private final static Logger logger = LoggerFactory.getLogger(CentralPurchasingServiceImpl.class);
	@Resource
	private CentralPurchasingActivitesDAO centralPurchasingActivitesDAO;
	@Resource
	private CentralPurchasingActivitesDetailsDAO centralPurchasingActivitesDetailsDAO;
	@Resource
	private CentralPurchasingEnterpriseDAO centralPurchasingEnterpriseDAO;
	@Resource
	private CentralPurchasingRefEnterpriseDAO centralPurchasingRefEnterpriseDAO;
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Override
	public ExecuteResult<Boolean> addCentralPurchasingActivityDTO(
			CentralPurchasingDTO centralPurchasingDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-addCentralPurchasingActivityDTO",JSON.toJSONString(centralPurchasingDTO));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		// 1、将集采活动DTO拆分
		CentralPurchasingActivites activity = new CentralPurchasingActivites();
		CentralPurchasingActivitesDetails detail = new CentralPurchasingActivitesDetails();
		BeanUtils.copyProperties(centralPurchasingDTO, activity);
		try {
			// 2、保存集采活动
			centralPurchasingActivitesDAO.add(activity);
			// 3、保存集采活动详情
			for(CentralPurchasingDetails details: centralPurchasingDTO.getDetails()){
				BeanUtils.copyProperties(details, detail);
				detail.setCentralPurchasingId(activity.getCentralPurchasingId());
				centralPurchasingActivitesDetailsDAO.add(detail);
				detail = new CentralPurchasingActivitesDetails();
			}
		}catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-addCentralPurchasingActivityDTO",e);
			er.addErrorMessage("添加集采活动失败！");
		}
		if(er.isSuccess()){
			er.setResult(true);
		}else{
			er.setResult(false);
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> updateCentralPurchasingActivityDTO(
			CentralPurchasingDTO centralPurchasingDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-updateCentralPurchasingActivityDTO",JSON.toJSONString(centralPurchasingDTO));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		// 1、将集采活动DTO拆分
		CentralPurchasingActivites activity = new CentralPurchasingActivites();
		CentralPurchasingActivitesDetails detail = new CentralPurchasingActivitesDetails();
		BeanUtils.copyProperties(centralPurchasingDTO, activity);
		Boolean flag = true;
		try {
			for(CentralPurchasingDetails details: centralPurchasingDTO.getDetails()){
				BeanUtils.copyProperties(details, detail);
				if(null != detail.getActivitesDetailsId()) {
					if(flag){
						// 2、更新集采活动
						er = toUpdateActivity(detail,activity);
						flag = false;
					}
					// 3、更新集采活动详情
					centralPurchasingActivitesDetailsDAO.update(detail);
					detail = new CentralPurchasingActivitesDetails();
				}
				
				
			}
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-updateCentralPurchasingActivityDTO",e);
			er.addErrorMessage("更新集采活动失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> queryCentralPurchasingActivityDTO(
			QueryCentralPurchasingDTO queryCentralPurchasingDTO,Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-queryCentralPurchasingActivityDTO",JSON.toJSONString(queryCentralPurchasingDTO));
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = new ExecuteResult<DataGrid<QueryCentralPurchasingDTO>>();
		DataGrid<QueryCentralPurchasingDTO> dataGrid = new DataGrid<QueryCentralPurchasingDTO>();
		List<QueryCentralPurchasingDTO> centralPurchasing = new ArrayList<QueryCentralPurchasingDTO>();
		// 1、将集采活动DTO拆分为集采活动及集采详情
		CentralPurchasingActivitesDetails detail = new CentralPurchasingActivitesDetails();
		Pager<CentralPurchasingActivitesDetails> p = new Pager<CentralPurchasingActivitesDetails>();
		BeanUtils.copyProperties(queryCentralPurchasingDTO, detail);
		p.setPage(page.getPage());
		try {
			// 2、查询集采详情
			Long count = centralPurchasingActivitesDetailsDAO.queryCount(detail);
			if(count < 1){
				er.addErrorMessage("数据不存在！");
				return er;
			}
			List<CentralPurchasingActivitesDetails> detailList = centralPurchasingActivitesDetailsDAO.queryList(detail, p);
			// 3、查询集采活动
			for (CentralPurchasingActivitesDetails d:detailList) {
				CentralPurchasingActivites activity = new CentralPurchasingActivites();
				BeanUtils.copyProperties(queryCentralPurchasingDTO, activity);
				activity.setCentralPurchasingId(d.getCentralPurchasingId());
				List<CentralPurchasingActivites> backActivity = centralPurchasingActivitesDAO.queryList(activity, null);
				if(null == backActivity || backActivity.size() < 1){
					er.addErrorMessage("数据不存在！");
					continue;
				}
				QueryCentralPurchasingDTO backDTO = new QueryCentralPurchasingDTO();
				BeanUtils.copyProperties(d, backDTO);
				BeanUtils.copyProperties(backActivity.get(0), backDTO);
				centralPurchasing.add(backDTO);
			}
			dataGrid.setTotal(count);
			dataGrid.setRows(centralPurchasing);
			er.setResult(dataGrid);
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-queryCentralPurchasingActivityDTO",e);
			er.addErrorMessage("查询集采活动出现异常！");
		}
		return er;
	}

	private ExecuteResult<Boolean> toUpdateActivity(CentralPurchasingActivitesDetails detail,CentralPurchasingActivites activity){
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		if(null != detail.getActivitesDetailsId()) {
			CentralPurchasingActivitesDetails backDetail = centralPurchasingActivitesDetailsDAO.queryById(detail.getActivitesDetailsId());
			// 验证集采活动ID
			if(null == activity.getCentralPurchasingId() || activity.getCentralPurchasingId().equals(backDetail.getCentralPurchasingId())) {
				activity.setCentralPurchasingId(backDetail.getCentralPurchasingId());
			}else{
				logger.info("\n 方法[{}]，异常：[{}]","CentralPurchasingServiceImpl-updateCentralPurchasingActivityDTO","更新集采活动时，集采活动ID与详情中集采活动ID不一致");
				er.addErrorMessage("更新数据异常！");
			}
			// 更新集采活动
			centralPurchasingActivitesDAO.update(activity);
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> addSignUpInfo(EnterpriseSignUpInfoDTO enterpriseSignUpInfoDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-addSignUpInfo",JSON.toJSONString(enterpriseSignUpInfoDTO));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		// 1、拆分DTO
		CentralPurchasingEnterprise enterprise = new CentralPurchasingEnterprise();
		BeanUtils.copyProperties(enterpriseSignUpInfoDTO, enterprise);
		try {
			// 2、新增企业信息
			centralPurchasingEnterpriseDAO.add(enterprise);
			// 3、新增关联信息
			for(SignUpRefPurchasingDetail details: enterpriseSignUpInfoDTO.getSignUpRefPurchasingDetail()){
				CentralPurchasingRefEnterprise detail = new CentralPurchasingRefEnterprise();
				BeanUtils.copyProperties(details, detail);
				detail.setEnterpriseId(enterprise.getEnterpriseId());
				centralPurchasingRefEnterpriseDAO.add(detail);
				// 4、更新已报名人数
				centralPurchasingActivitesDetailsDAO.plusSignUpNum(detail.getActivitesDetailsId(), 1);
			}
			er.setResult(true);
		}catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-addSignUpInfo",e);
			er.addErrorMessage("添加报名信息失败！");
		}
		return er;
	}

	@Override
	public QueryCentralPurchasingDTO queryCentralPurchasingByDetailId(
			QueryCentralPurchasingDTO queryCentralPurchasingDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-queryCentralPurchasingByDetailId",JSON.toJSONString(queryCentralPurchasingDTO));
		QueryCentralPurchasingDTO backDTO = new QueryCentralPurchasingDTO();
		try {
			// 1、查询明细
			CentralPurchasingActivitesDetails detail = centralPurchasingActivitesDetailsDAO.queryById(queryCentralPurchasingDTO.getActivitesDetailsId());
			if(null == detail || null == detail.getCentralPurchasingId()){
				return null;
			}
			// 2、查询活动
			CentralPurchasingActivites activity = centralPurchasingActivitesDAO.queryById(detail.getCentralPurchasingId());
			if(null == activity){
				return null;
			}
			// 3、组装DTO
			BeanUtils.copyProperties(activity, backDTO);
			BeanUtils.copyProperties(detail, backDTO);
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-queryCentralPurchasingDTO",e);
			backDTO = null;
		}
		return backDTO;
	}

	@Override
	public ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> queryCentralPurchasingActivity(
			QueryCentralPurchasingDTO queryCentralPurchasingDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-queryCentralPurchasingActivity",JSON.toJSONString(queryCentralPurchasingDTO));
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = new ExecuteResult<DataGrid<QueryCentralPurchasingDTO>>();
		try {
			DataGrid<QueryCentralPurchasingDTO> resultPager = new DataGrid<QueryCentralPurchasingDTO>();
			List<Long> catIdSet = new ArrayList<Long>();//类目ID
			Long cid = queryCentralPurchasingDTO.getCid();
			if (cid != null) {// 类目ID 转换为三级类目ID组
				ExecuteResult<List<ItemCategoryDTO>> cresult = itemCategoryService.queryThirdCatsList(cid);
				if (cresult.isSuccess() && cresult.getResult() != null) {
					List<ItemCategoryDTO> catsList = cresult.getResult();
					for (ItemCategoryDTO itemCategory : catsList) {
						catIdSet.add(itemCategory.getCategoryCid());
					}
				}
			}
			queryCentralPurchasingDTO.setCids(catIdSet);
			List<QueryCentralPurchasingDTO> queryCentralPurchasingDTOs = centralPurchasingActivitesDAO.queryCentralPurchasingActivites(queryCentralPurchasingDTO, page);
			long size = centralPurchasingActivitesDAO.queryCentralPurchasingActivitesCount(queryCentralPurchasingDTO);
			resultPager.setRows(queryCentralPurchasingDTOs);
			resultPager.setTotal(size);
			er.setResult(resultPager);
		} catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-queryCentralPurchasingActivity",e);
			er.addErrorMessage("查询活动信息失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> queryCentralPurchasingRefEnterprise(CentralPurchasingRefEnterpriseDTO centralPurchasingRefEnterpriseDTO) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-queryCentralPurchasingRefEnterprise",JSON.toJSONString(centralPurchasingRefEnterpriseDTO));
		ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> er = new ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>>();
		try {
			List<CentralPurchasingRefEnterpriseDTO> enterpriseDTOs = new ArrayList<CentralPurchasingRefEnterpriseDTO>();
			// 构造入参对象
			CentralPurchasingRefEnterprise purchasingRefEnterprise = new CentralPurchasingRefEnterprise();
			BeanUtils.copyProperties(centralPurchasingRefEnterpriseDTO, purchasingRefEnterprise);
			List<CentralPurchasingRefEnterprise> centralPurchasingRefEnterprises = centralPurchasingRefEnterpriseDAO
					.queryList(purchasingRefEnterprise, null);
			if (centralPurchasingRefEnterprises != null && centralPurchasingRefEnterprises.size() > 0) {
				for (CentralPurchasingRefEnterprise refEnterprise : centralPurchasingRefEnterprises) {
					CentralPurchasingRefEnterpriseDTO dto = new CentralPurchasingRefEnterpriseDTO();
					BeanUtils.copyProperties(refEnterprise, dto);
					enterpriseDTOs.add(dto);
				}
			}
			er.setResult(enterpriseDTOs);
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-queryCentralPurchasingRefEnterprise", e);
			er.addErrorMessage("查询集采详情失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<DataGrid<QuerySignUpInfoDTO>> querySignUpInfo(
			QuerySignUpInfoDTO querySignUpInfoDTO, Pager page) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-querySignUpInfo",JSON.toJSONString(querySignUpInfoDTO));
		ExecuteResult<DataGrid<QuerySignUpInfoDTO>> er = new ExecuteResult<DataGrid<QuerySignUpInfoDTO>>();
		try {
			DataGrid<QuerySignUpInfoDTO> resultPager = new DataGrid<QuerySignUpInfoDTO>();
			List<QuerySignUpInfoDTO> querySignUpInfoDTOs = centralPurchasingRefEnterpriseDAO.querySignUpInfo(querySignUpInfoDTO, page);
			long size = centralPurchasingRefEnterpriseDAO.querySignUpInfoCount(querySignUpInfoDTO);
			resultPager.setRows(querySignUpInfoDTOs);
			resultPager.setTotal(size);
			er.setResult(resultPager);
		} catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-querySignUpInfo",e);
			er.addErrorMessage("查询报名信息失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<QueryCentralPurchasingDTO> queryByDetailId(Long activitesDetailsId) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-queryByDetailId", activitesDetailsId);
		ExecuteResult<QueryCentralPurchasingDTO> result = new ExecuteResult<QueryCentralPurchasingDTO>();
		try {
			QueryCentralPurchasingDTO centralPurchasingDTO = centralPurchasingActivitesDAO.queryByDetailId(activitesDetailsId);
			result.setResult(centralPurchasingDTO);
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-queryByDetailId", e);
			result.addErrorMessage("查询集采活动失败！");
		}
		logger.info("\n 方法[{}]，出参：[{}]","CentralPurchasingServiceImpl-queryByDetailId", JSON.toJSONString(result));
		return result;
	}
	
	@Override
	public ExecuteResult<Boolean> plusSignUpNum(Long activitesDetailsId, Integer num) {
		logger.info("\n 方法[{}]，入参：[{},{}]","CentralPurchasingServiceImpl-plusSignUpNum", activitesDetailsId, num);
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			centralPurchasingActivitesDetailsDAO.plusSignUpNum(activitesDetailsId, num);
			er.setResult(true);
		} catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-plusSignUpNum",e);
			er.addErrorMessage("报名人数修改失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> plusPlaceOrderNum(Long activitesDetailsId, Integer num) {
		logger.info("\n 方法[{}]，入参：[{},{}]","CentralPurchasingServiceImpl-plusPlaceOrderNum", activitesDetailsId, num);
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			centralPurchasingActivitesDetailsDAO.plusPlaceOrderNum(activitesDetailsId, num);
			er.setResult(true);
		} catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-plusPlaceOrderNum",e);
			er.addErrorMessage("下单人数修改失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> plusPaidNum(Long activitesDetailsId, Integer num) {
		logger.info("\n 方法[{}]，入参：[{},{}]","CentralPurchasingServiceImpl-plusPaidNum", activitesDetailsId, num);
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			centralPurchasingActivitesDetailsDAO.plusPaidNum(activitesDetailsId, num);
			er.setResult(true);
		} catch(Exception e) {
			logger.error("CentralPurchasingServiceImpl-plusPaidNum",e);
			er.addErrorMessage("付款人数修改失败！");
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> checkUniqueSku(
			QueryCentralPurchasingDTO queryCentralPurchasingDTO) {
		logger.info("\n 方法[{}]，入参：[{},{}]","CentralPurchasingServiceImpl-checkUniqueSku", JSON.toJSONString(queryCentralPurchasingDTO));
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			int flag = centralPurchasingActivitesDAO.checkUniqueSku(queryCentralPurchasingDTO);
			if(flag == 0){
				er.setResult(true);
			}else{
				er.setResult(false);
				er.addErrorMessage("该时间段内已存在该商品");
				logger.error("该时间段内已存在该商品");
			}
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-checkUniqueSku",e);
			er.addErrorMessage("skuId校验失败");
		}
		return er;
	}

	@Override
	public ExecuteResult<Boolean> deleteBatch(String ids) {
		logger.info("\n 方法[{}]，入参：[{}]","CentralPurchasingServiceImpl-deleteBatch", ids);
		ExecuteResult<Boolean> er = new ExecuteResult<Boolean>();
		try {
			centralPurchasingActivitesDAO.deleteBatch(ids);
		} catch (Exception e) {
			logger.error("CentralPurchasingServiceImpl-checkUniqueSku",e);
			er.addErrorMessage("批量删除失败");
		}
		return er;
	}
	
}
