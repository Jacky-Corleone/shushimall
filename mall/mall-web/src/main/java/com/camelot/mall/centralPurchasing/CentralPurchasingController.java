package com.camelot.mall.centralPurchasing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.common.util.DateUtils;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.CentralPurchasingDTO;
import com.camelot.maketcenter.dto.CentralPurchasingDetails;
import com.camelot.maketcenter.dto.CentralPurchasingRefEnterpriseDTO;
import com.camelot.maketcenter.dto.CentralPurchasingRefOrderDTO;
import com.camelot.maketcenter.dto.EnterpriseSignUpInfoDTO;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.QuerySignUpInfoDTO;
import com.camelot.maketcenter.dto.SignUpRefPurchasingDetail;
import com.camelot.maketcenter.dto.emums.CentralPurchasingActivityStatusEnum;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.maketcenter.service.CentralPurchasingRefOrderExportService;
import com.camelot.mall.service.ItemInfoService;
import com.camelot.mall.util.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

/**
 * 
 * <p>Description: [集采活动]</p>
 * Created on 2015年11月26日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/centralPurchasing")
public class CentralPurchasingController {
	
	protected Logger LOG = LoggerFactory.getLogger(getClass());
	
	@Resource
	private CentralPurchasingExportService centralPurchasingExportService;
	
	@Resource
	private ItemExportService itemExportService;
	
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Resource
	private TradeInventoryExportService tradeInventoryExportService;
	
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	
	@Resource
	private UserExportService userExportService;
	
	@Resource
	private ItemInfoService itemInfoService;
	
	@Resource
	private CentralPurchasingRefOrderExportService centralPurchasingRefOrderExportService;
	
	/**
	 * 
	 * <p>Description: [集采活动首页]</p>
	 * Created on 2015年11月29日
	 * @param levOneCid
	 * @param levTwoCid
	 * @param levThreeCid
	 * @param page
	 * @param queryCentralPurchasingDTO
	 * @param model
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("")
	public String index(Long levOneCid, Long levTwoCid, Long levThreeCid, Integer page, QueryCentralPurchasingDTO queryCentralPurchasingDTO, Model model, HttpServletRequest request) {
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			model.addAttribute("logging_status", "false");
		} else{
			model.addAttribute("logging_status", "true");
		}
		List<JSONObject> centralPurchasingActivitys = new ArrayList<JSONObject>();
		Pager<QueryCentralPurchasingDTO> pager = new Pager<QueryCentralPurchasingDTO>();
		pager.setRows(28);
		if (null != page) {
			pager.setPage(page);
		}
		if (null != levThreeCid) {
			queryCentralPurchasingDTO.setCid(levThreeCid);
		} else if (null != levTwoCid) {
			queryCentralPurchasingDTO.setCid(levTwoCid);
		} else if (null != levOneCid) {
			queryCentralPurchasingDTO.setCid(levOneCid);
		}
		// 默认显示进行中的
		if(queryCentralPurchasingDTO.getDetailedStatus() == null){
			queryCentralPurchasingDTO.setDetailedStatus(200);
		} else if(queryCentralPurchasingDTO.getDetailedStatus() == 0){
			queryCentralPurchasingDTO.setDetailedStatus(null);
		}
		// 不需要查询的状态
		List<Integer> notActivityStatusList = new ArrayList<Integer>();
		// 未发布
		notActivityStatusList.add(CentralPurchasingActivityStatusEnum.UNPUBLISHED.getCode());
		queryCentralPurchasingDTO.setNotActivityStatusList(notActivityStatusList);
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> result = centralPurchasingExportService
				.queryCentralPurchasingActivity(queryCentralPurchasingDTO, pager);
		DataGrid<QueryCentralPurchasingDTO> dataGrid = new DataGrid<QueryCentralPurchasingDTO>();
		if (result.isSuccess() 
				&& result.getResult() != null 
				&& result.getResult().getRows() != null
				&& result.getResult().getRows().size() > 0) {
			dataGrid = result.getResult();
			for (QueryCentralPurchasingDTO centralPurchasingDTO : result.getResult().getRows()) {
				JSONObject centralPurchasing = this.convertToJSONObject(centralPurchasingDTO);
				centralPurchasingActivitys.add(centralPurchasing);
			}
		}
		
		pager.setTotalCount(dataGrid.getTotal().intValue());
		
		model.addAttribute("centralPurchasingActivitys", centralPurchasingActivitys);
		model.addAttribute("pager", pager);
		model.addAttribute("queryCentralPurchasingDTO", queryCentralPurchasingDTO);
		model.addAttribute("levOneCid", levOneCid);
		model.addAttribute("levTwoCid", levTwoCid);
		model.addAttribute("levThreeCid", levThreeCid);
		// 返回服务器当前时间用于倒计时
		Date date= new Date();
		model.addAttribute("serverTime", DateUtils.format(new java.sql.Date(date.getTime()), "yyyy/MM/dd HH:mm:ss"));
		return "/centralPurchasing/index";
	}
	
	/**
	 * 
	 * <p>Description: [报名]</p>
	 * Created on 2015年11月26日
	 * @param model
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/signUp")
	@ResponseBody
	public ExecuteResult<Boolean> signUp(EnterpriseSignUpInfoDTO enterpriseSignUpInfoDTO, Model model, HttpServletRequest request) {
		ExecuteResult<Boolean> result = new ExecuteResult<Boolean>();
		Long uid = WebUtil.getInstance().getUserId(request);
		if (uid != null) {
			// 设置报名人
			enterpriseSignUpInfoDTO.setInsertBy(uid);
			if (enterpriseSignUpInfoDTO != null 
					&& enterpriseSignUpInfoDTO.getSignUpRefPurchasingDetail() != null
					&& enterpriseSignUpInfoDTO.getSignUpRefPurchasingDetail().size() > 0) {
				// 设置报名人
				for (SignUpRefPurchasingDetail detail : enterpriseSignUpInfoDTO.getSignUpRefPurchasingDetail()) {
					detail.setInsertBy(uid);
				}
				
				// 查询集采详情
				CentralPurchasingRefEnterpriseDTO centralPurchasingRefEnterpriseDTO = new CentralPurchasingRefEnterpriseDTO();
				centralPurchasingRefEnterpriseDTO.setActivitesDetailsId(enterpriseSignUpInfoDTO.getSignUpRefPurchasingDetail().get(0).getActivitesDetailsId());
				centralPurchasingRefEnterpriseDTO.setInsertBy(enterpriseSignUpInfoDTO.getInsertBy());
				ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> er = centralPurchasingExportService.queryCentralPurchasingRefEnterprise(centralPurchasingRefEnterpriseDTO);
				if (er.isSuccess()) {
					if (er.getResult() != null && er.getResult().size() > 0) {
						result.addErrorMessage("您已经报名过此活动，请不要重复报名");
					} else{
						// 添加报名信息
						result = centralPurchasingExportService.addSignUpInfo(enterpriseSignUpInfoDTO);
					}
				} else{
					result.getErrorMessages().addAll(er.getErrorMessages());
				}
			}
		} else{
			result.addErrorMessage("您还没有登录，请先登录");
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Description: [根据活动详情ID查询活动]</p>
	 * Created on 2015年12月2日
	 * @param activitesDetailsId
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/queryByActivitesDetailsId")
	public String queryByActivitesDetailsId(Long activitesDetailsId, Model model, HttpServletRequest request) {
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			model.addAttribute("logging_status", "false");
		} else{
			model.addAttribute("logging_status", "true");
		}
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		queryCentralPurchasingDTO.setActivitesDetailsId(activitesDetailsId);
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = centralPurchasingExportService
				.queryCentralPurchasingActivity(queryCentralPurchasingDTO, null);
		if (er.isSuccess()) {
			if (er.getResult() != null && er.getResult().getRows() != null && er.getResult().getRows().size() > 0) {
				QueryCentralPurchasingDTO centralPurchasingDTO = er.getResult().getRows().get(0);
				JSONObject jsonObject = this.convertToJSONObject(centralPurchasingDTO);
				model.addAttribute("entity", jsonObject);
			}
		}
		// 返回服务器当前时间用于倒计时
		Date date= new Date();
		model.addAttribute("serverTime", DateUtils.format(new java.sql.Date(date.getTime()), "yyyy/MM/dd HH:mm:ss"));
		return "/centralPurchasing/product_box";
	}
	
	/**
	 * 
	 * <p>Description: [将集采活动DTO转换成JSONObject，同时查询出商品的销售属性]</p>
	 * Created on 2015年12月2日
	 * @param centralPurchasingDTO
	 * @return
	 * @author:[宋文斌]
	 */
	private JSONObject convertToJSONObject(QueryCentralPurchasingDTO centralPurchasingDTO) {
		JSONObject centralPurchasing = (JSONObject) JSON.toJSON(centralPurchasingDTO);
		// 查询销售属性
		ExecuteResult<String> result_attr = itemExportService.queryAttrBySkuId(centralPurchasingDTO.getSkuId());
		if (result_attr != null && result_attr.getResult() != null) {
			String attrStr = result_attr.getResult();
			// 根据sku的销售属性keyId:valueId查询商品属性
			ExecuteResult<List<ItemAttr>> attributes = itemCategoryService.queryCatAttrByKeyVals(attrStr);
			if (attributes != null && attributes.getResult() != null && attributes.getResult().size() > 0) {
				centralPurchasing.put("attributes", attributes.getResult());
			}
		}
		if(centralPurchasingDTO.getItemId() != null){
			ItemDTO itemDTO = itemInfoService.getItemInfoById(centralPurchasingDTO.getItemId() + "");
			if(itemDTO != null){
				centralPurchasing.put("originalPrice", itemDTO.getMarketPrice());
			}
		}
		// 查询已报名人数
		CentralPurchasingRefEnterpriseDTO centralPurchasingRefEnterpriseDTO = new CentralPurchasingRefEnterpriseDTO();
		centralPurchasingRefEnterpriseDTO.setActivitesDetailsId(centralPurchasingDTO.getActivitesDetailsId());
		ExecuteResult<List<CentralPurchasingRefEnterpriseDTO>> result = centralPurchasingExportService.queryCentralPurchasingRefEnterprise(centralPurchasingRefEnterpriseDTO);
		if(result.isSuccess() && result.getResult() != null){
			centralPurchasing.put("signUpNum", result.getResult().size());
		}
		// 查询已购买人数
		CentralPurchasingRefOrderDTO centralPurchasingRefOrderDTO = new CentralPurchasingRefOrderDTO();
		centralPurchasingRefOrderDTO.setActivitesDetailsId(centralPurchasingDTO.getActivitesDetailsId());
		centralPurchasingRefOrderDTO.setItemId(centralPurchasingDTO.getItemId());
		centralPurchasingRefOrderDTO.setSkuId(centralPurchasingDTO.getSkuId());
		ExecuteResult<DataGrid<CentralPurchasingRefOrderDTO>> executeResult = centralPurchasingRefOrderExportService.queryCentralPurchasingRefOrder(centralPurchasingRefOrderDTO, null);
		if(executeResult.isSuccess() && executeResult.getResult() != null && executeResult.getResult().getRows() != null){
			centralPurchasing.put("purchaseNum", executeResult.getResult().getRows().size());
		}
		return centralPurchasing;
	}
	
	/**
	 * 
	 * <p>Description: [进入新增集采页面]</p>
	 * Created on 2015-12-1
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "/activityAdd")
	public String form(Pager pager, Model model,String isView,HttpServletRequest request,
			QueryCentralPurchasingDTO queryCentralPurchasingDTO) {
		//获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		//通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> result =  itemCategoryService.queryParentCategoryList(categoryIds);
		model.addAttribute("itemCatCascadeDTOList",result.getResult());
		if (null != queryCentralPurchasingDTO
				&& null != queryCentralPurchasingDTO.getActivitesDetailsId()) {
			queryCentralPurchasingDTO = centralPurchasingExportService
					.queryCentralPurchasingByDetailId(queryCentralPurchasingDTO);
			TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
			tradeInventoryInDTO.setSkuId(queryCentralPurchasingDTO.getSkuId());
			ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, null);
			
			model.addAttribute("tradeInventoryOutDTO", dg.getResult().getRows().get(0));
		}
		model.addAttribute("queryCentralPurchasingDTO",
				queryCentralPurchasingDTO);
		model.addAttribute("isView", isView);
		return "/centralPurchasing/centralPurchasingAdd";
	}
	
	/**
	 * 
	 * <p>Description: [新增集采活动]</p>
	 * Created on 2015-12-2
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "/activitySave")
	public String saveActivity(CentralPurchasingDTO centralPurchasingDTO,CentralPurchasingDetails details,HttpServletRequest request) {
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		BeanUtils.copyProperties(centralPurchasingDTO, queryCentralPurchasingDTO);
		BeanUtils.copyProperties(details, queryCentralPurchasingDTO);
		if(checkSkuUnique(queryCentralPurchasingDTO).isSuccess()){
			List<CentralPurchasingDetails> list = new ArrayList<CentralPurchasingDetails>();
			centralPurchasingDTO.setActivityBeginTime(DateUtils.parse(request.getParameter("activityBeginTime"), DateUtils.YYDDMMHHMMSS));
			centralPurchasingDTO.setActivityEndTime(DateUtils.parse(request.getParameter("activityEndTime"), DateUtils.YYDDMMHHMMSS));
			centralPurchasingDTO.setActivitySignUpTime(DateUtils.parse(request.getParameter("activitySignUpTime"), DateUtils.YYDDMMHHMMSS));
			centralPurchasingDTO.setActivitySignUpEndTime(DateUtils.parse(request.getParameter("activitySignUpEndTime"), DateUtils.YYDDMMHHMMSS));
			centralPurchasingDTO.setShopId(getShopId(request));
			list.add(details);
			centralPurchasingDTO.setDetails(list);
			// 店铺发布
			centralPurchasingDTO.setActivityType(2);
			centralPurchasingExportService.addCentralPurchasingActivityDTO(centralPurchasingDTO);
		}
		return "redirect:/centralPurchasing/activityList";
	}
	
	/**
	 * 
	 * <p>Description: [新增集采活动]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "/activityUpdate")
	@ResponseBody
	public String updateActivity(CentralPurchasingDTO centralPurchasingDTO,CentralPurchasingDetails details,HttpServletRequest request) {
		boolean flag = false;
		if(details.getActivitesDetailsId()==null){
			return "redirect:/centralPurchasing/activityList";
		}else if(details.getSkuId() == null || centralPurchasingDTO.getActivityEndTime() == null || centralPurchasingDTO.getActivitySignUpTime() == null){
			flag = true;
		}else{
			QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
			BeanUtils.copyProperties(centralPurchasingDTO, queryCentralPurchasingDTO);
			BeanUtils.copyProperties(details, queryCentralPurchasingDTO);
			flag = checkSkuUnique(queryCentralPurchasingDTO).isSuccess();
		}
		if(flag){
			List<CentralPurchasingDetails> list = new ArrayList<CentralPurchasingDetails>();
			centralPurchasingDTO.setActivityBeginTime(DateUtils.parse(request.getParameter("activityBeginTime"), "yyyy-MM-dd hh-mm-ss"));
			centralPurchasingDTO.setActivityEndTime(DateUtils.parse(request.getParameter("activityEndTime"), "yyyy-MM-dd hh-mm-ss"));
			centralPurchasingDTO.setActivitySignUpTime(DateUtils.parse(request.getParameter("activitySignUpTime"), "yyyy-MM-dd hh-mm-ss"));
			centralPurchasingDTO.setActivitySignUpEndTime(DateUtils.parse(request.getParameter("activitySignUpEndTime"), "yyyy-MM-dd hh-mm-ss"));
			list.add(details);
			centralPurchasingDTO.setDetails(list);
			
			centralPurchasingExportService.updateCentralPurchasingActivityDTO(centralPurchasingDTO);
		}
		return "redirect:/centralPurchasing/activityList";
	}
	/**
	 * 
	 * <p>Description: [集采活动列表]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "/activityList")
	public String listActivity(QueryCentralPurchasingDTO queryCentralPurchasingDTO,Pager page,Model model,HttpServletRequest request) {
		// 查询该店铺的集采
		queryCentralPurchasingDTO.setShopId(getShopId(request));
		// 店铺发布
		queryCentralPurchasingDTO.setActivityType(2);
		// 依据插入时间排序
		queryCentralPurchasingDTO.setOrderByType(1);
		List<Long> itemIds = new ArrayList<Long>();
		String itemName = queryCentralPurchasingDTO.getItemName();
		if(null != itemName && !"".equals(itemName)) {
			TradeInventoryInDTO dto = new TradeInventoryInDTO();
			dto.setItemName(queryCentralPurchasingDTO.getItemName());
			// 在售商品
			dto.setItemStatus(4);
			ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(dto, null);
			if(dg.isSuccess()){
				for(TradeInventoryOutDTO item:dg.getResult().getRows()){
					itemIds.add(item.getItemId());
				};
				queryCentralPurchasingDTO.setItemIds(itemIds);
			}
		}
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = centralPurchasingExportService.queryCentralPurchasingActivity(queryCentralPurchasingDTO, page);
		if(er.isSuccess()){
			DataGrid<QueryCentralPurchasingDTO> dg = er.getResult();
			List<QueryCentralPurchasingDTO> backList = getQueryCentralPurchasingDTO(dg.getRows());
			page.setTotalCount(dg.getTotal().intValue());
			page.setRecords(backList);
		}
		model.addAttribute("pager", page);
		model.addAttribute("queryPurchase", queryCentralPurchasingDTO);
		
		return "/centralPurchasing/centralPurchasingList";
	}
	
	/**
	 * 
	 * <p>Description: [集采活动列表]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "/viewSignUpInfo")
	public String viewSignUpInfo(QuerySignUpInfoDTO querySignUpInfoDTO,Pager pager,Model model) {
		ExecuteResult<DataGrid<QuerySignUpInfoDTO>> er = centralPurchasingExportService.querySignUpInfo(querySignUpInfoDTO, pager);
		if(er.isSuccess()){
			DataGrid<QuerySignUpInfoDTO> dg = er.getResult();
			pager.setTotalCount(dg.getTotal().intValue());
			pager.setRecords(dg.getRows());
		}
		model.addAttribute("pager", pager);
		return "/centralPurchasing/signUpInfo";
	}
	
	/**
	 * 获取店铺id
	 * @param request
	 * @return
	 */
	private Long getShopId(HttpServletRequest request){
		return WebUtil.getInstance().getShopId(request);
	}
	
	@RequestMapping("initItemInventory")
	public String initItemInventory(HttpServletRequest request,TradeInventoryInDTO dto,Integer page,Model model){
		Pager<TradeInventoryOutDTO> pager = new Pager<TradeInventoryOutDTO>();
		if(page == null){
			page = 1;
		}
        Long userId = WebUtil.getInstance().getUserId(request);
        UserDTO userDTO = userExportService.queryUserById(userId);
        if(userDTO.getParentId() == null){//主账号id
            dto.setSellerId(userId);
        }else{//子账号id
            dto.setSellerId(userDTO.getParentId());
        }
		pager.setPage(page);
		// 在售商品
		dto.setItemStatus(4);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(dto, pager);

		if(dg.getResult() != null){
			pager.setRecords(dg.getResult().getRows());
			pager.setTotalCount(dg.getResult().getTotal().intValue());
		}else{
			pager.setRecords(new ArrayList<TradeInventoryOutDTO>());
			pager.setTotalCount(1);
		}
		model.addAttribute("TradeInventoryInDTO", dto);
		model.addAttribute("pager", pager);
		return "/centralPurchasing/itemInventory";
	}
	/**
	 * 
	 * <p>Description: [获取集采活动商品信息]</p>
	 * Created on 2015-12-9
	 * @author 周志军
	 * @return 
	 */
	private List<QueryCentralPurchasingDTO> getQueryCentralPurchasingDTO(List<QueryCentralPurchasingDTO> list){
		List<QueryCentralPurchasingDTO> backList = new ArrayList<QueryCentralPurchasingDTO>();
		for(QueryCentralPurchasingDTO centralPurchase:list){
			QueryCentralPurchasingDTO backCentralPurchase = new QueryCentralPurchasingDTO();
			BeanUtils.copyProperties(centralPurchase, backCentralPurchase);
			TradeInventoryInDTO dto = new TradeInventoryInDTO();
			if(null == centralPurchase.getSkuId()){
				backList.add(backCentralPurchase);
				continue;
			}
			// 依据SKU获取商品信息,获取商品信息为唯一
			dto.setSkuId(centralPurchase.getSkuId());
			ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(dto, null);
			if(dg.isSuccess() && dg.getResult().getTotal() > 0){
				// 组装集采对象
				QueryCentralPurchasingDTO qc = dealQueryCentralPurchasingDTO(dg.getResult().getRows().get(0));
				backCentralPurchase.setItemAttr(qc.getItemAttr());
				backCentralPurchase.setItemName(qc.getItemName());
				backList.add(backCentralPurchase);
			}else{
				backList.add(backCentralPurchase);
				continue;
			}
		}
		return backList;
	}
	/**
	 * 
	 * <p>Description: [组装集采对象]</p>
	 * Created on 2015-12-9
	 * @author 周志军
	 * @return 
	 */
	private QueryCentralPurchasingDTO dealQueryCentralPurchasingDTO(TradeInventoryOutDTO tradeInventory){
		List<com.camelot.maketcenter.dto.ItemAttr> backItemAttrList = new ArrayList<com.camelot.maketcenter.dto.ItemAttr>();
		QueryCentralPurchasingDTO centralPurchase = new QueryCentralPurchasingDTO();
		for(ItemAttr itemAttr:tradeInventory.getItemAttr()){
			com.camelot.maketcenter.dto.ItemAttr backItemAttr = new com.camelot.maketcenter.dto.ItemAttr();
			BeanUtils.copyProperties(itemAttr, backItemAttr);
			backItemAttrList.add(backItemAttr);
			backItemAttr = null;
		}
		centralPurchase.setItemName(tradeInventory.getItemName());
		centralPurchase.setItemAttr(backItemAttrList);
		return centralPurchase;
	}
	/**
	 * 
	 * <p>Description: [校验SKU唯一性]</p>
	 * Created on 2015-12-15
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "/checkSkuUnique")
	@ResponseBody
	public Json checkSkuUnique(QueryCentralPurchasingDTO queryCentralPurchasingDTO) {
		ExecuteResult<Boolean> er = centralPurchasingExportService.checkUniqueSku(queryCentralPurchasingDTO);
		Json json = new Json();
		json.setSuccess(er.isSuccess());
		return json;
	}
	/**
	 * 
	 * <p>Description: [批量删除活动]</p>
	 * Created on 2015-12-15
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping("/deleteAll")
	@ResponseBody
	public Map<String,Object> deleteAll(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String ids = request.getParameter("ids");
			// 解析商品id
			ExecuteResult<Boolean> result = centralPurchasingExportService.deleteBatch(ids);
			if(result.isSuccess()){
				resultMap.put("result",true);
			}else{
				resultMap.put("result",false);
			}
		} catch (Exception e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			LOG.error("批量删除活动",e);
		}
		return resultMap;
	}
}