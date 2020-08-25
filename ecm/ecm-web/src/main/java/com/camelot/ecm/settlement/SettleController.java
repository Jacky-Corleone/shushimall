package com.camelot.ecm.settlement;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.camelot.activity.dto.ActivityStatementsDTO;
import com.camelot.activity.service.ActivityStatementSerice;
import com.camelot.common.Json;
import com.camelot.common.enums.BankSettleTypeEnum;
import com.camelot.common.enums.FactorageEnums.FactorageStatus;
import com.camelot.common.enums.SettleEnum;
import com.camelot.ecm.SalesOrder.PayRecordQuery;
import com.camelot.ecm.itemcategory.ItemService;
import com.camelot.ecm.settlement.WX.WXSettlementDetail;
import com.camelot.ecm.settlement.alipay.AlipaySettlementDetail;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.FactorageJournalDTO;
import com.camelot.settlecenter.dto.BankSettleDetailDTO;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.combin.SettlementCombinDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.excel.ExportExcel;
import com.thinkgem.jeesite.common.utils.excel.ImportExcel;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * Created by sevelli on 15-3-9.
 * 
 * @description 结算中心controller
 */
@Controller
@RequestMapping(value = "${adminPath}/settle")
public class SettleController extends BaseController {
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private SattleCatExpenseExportService sattleCatExpenseExportService;
	@Resource
	private StatementExportService statementExportService;
	@Resource
	private PaymentExportService paymentExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private ItemService itemService;
	@Resource
    private ItemExportService itemExportService;
	@Resource
	private ActivityStatementSerice activityStatementSerice;

	@RequestMapping("/settlements")
	public String list(@ModelAttribute("settle") SettlementQuery dto,
			@RequestParam(required = false) String shopName, Pager pager,
			Model model) {

		DataGrid dataGrid = itemCategoryService.queryItemCategoryList(0L);
		if (dataGrid != null) {
			model.addAttribute("platformList", dataGrid.getRows());
		} else {
			model.addAttribute("platformList", new ArrayList());
		}
		DataGrid subdatagrid = itemCategoryService.queryItemCategoryList(dto
				.getFcid());
		if (subdatagrid != null) {
			model.addAttribute("subItemList", subdatagrid.getRows());
		} else {
			model.addAttribute("subItemList", new ArrayList());
		}

		DataGrid tdatagrid = itemCategoryService.queryItemCategoryList(dto
				.getSubcid());
		if (subdatagrid != null) {
			model.addAttribute("tItemList", tdatagrid.getRows());
		} else {
			model.addAttribute("tItemList", new ArrayList());
		}
		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}

		//20160302 修改结算 
		//model.addAttribute("allStatus", SettleEnum.SettleStatusEnum.values());

		ShopDTO shopDTO = new ShopDTO();
		shopDTO.setShopName(shopName);
		List<Long> shopIds = new ArrayList();
		ExecuteResult<DataGrid<ShopDTO>> shops = shopExportService
				.findShopInfoByCondition(shopDTO, null);
		Map<Long, ShopDTO> shopMap = new HashMap<Long, ShopDTO>();
		Long[] shopIdArr = null;
		// 前台模糊查询店铺名称，根据店铺名称查询出店铺对应的ID，添加到店铺ID组
		if (shops != null && shops.getResult() != null
				&& shops.getResult().getRows() != null) {
			shopIdArr = new Long[shops.getResult().getRows().size()];
			for (ShopDTO s : shops.getResult().getRows()) {
				shopIds.add(s.getShopId());
				shopMap.put(s.getShopId(), s);
			}
		}
		dto.setShopIds(shopIds.toArray(shopIdArr));
		ExecuteResult<DataGrid<SettlementCombinDTO>> result = statementExportService
				.querySettlementList(dto, pager);
		DataGrid<SettlementCombinDTO> datas = result.getResult();
		Page<SettlementCombinDTO> page = new Page<SettlementCombinDTO>();
		page.setCount(datas.getTotal());
		page.setList(datas.getRows());

		if (page.getList() != null) {
			for (int i = 0; i < page.getList().size(); i++) {
				if (page.getList().get(i).getSettlement() != null) {
					SettlementDTO settlement = page.getList().get(i).getSettlement();
					
					//------------20160302 修改结算 start----------
					if(settlement.getStatus()!=null){
						if(settlement.getStatus()==0){
							settlement.setStatusLabel("待结算");
						}else if(settlement.getStatus()==1){
							settlement.setStatusLabel("已结算");
						}else{
							settlement.setStatusLabel("");
						}
					}
					//----------end----------
					settlement.setShopName(shopMap.get(settlement.getShopId()).getShopName());
				}
			}
		}

		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("settle", dto);
		model.addAttribute("shopName", shopName);
		return "settlement/settlements";
	}

	/**
	 * 查询手续费
	 * 
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping("/factorageJournal")
	public String factorageJournalList(
			@ModelAttribute("fstatus") String fstatus,
			@ModelAttribute("factorageJournal") FactorageJournalDTO factorageJournalDTO,
			Pager pager, Model model) {
		if (StringUtils.isNotEmpty(fstatus)) {
			FactorageStatus fs = FactorageStatus.getEnumByCiticCode(Integer
					.parseInt(fstatus));
			factorageJournalDTO.setStatus(fs);
		} else {
			FactorageStatus fs = FactorageStatus.getEnumByCiticCode(0);
			factorageJournalDTO.setStatus(fs);
		}
		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}
		// 查询订单手续费状态
		FactorageStatus[] factorageStatuss = FactorageStatus.values();
		// 查询订单手续费明细列表
		DataGrid<FactorageJournalDTO> FactorageJournalList = this.paymentExportService
				.findFactorageJournal(factorageJournalDTO, pager);
		Page<FactorageJournalDTO> page = new Page<FactorageJournalDTO>();
		page.setCount(FactorageJournalList.getTotal());
		page.setList(FactorageJournalList.getRows());
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("factorageStatuss", factorageStatuss);
		model.addAttribute("factorageJournal", factorageJournalDTO);
		model.addAttribute("fstatus", fstatus);
		return "settlement/factorageJournalList";
	}
    
	/**
	 * 
	 * <p>Discription:[优惠活动结算单]</p>
	 * Created on 2015年12月15日
	 * @param activityStatementsDTO
	 * @param pager
	 * @param model
	 * @return
	 * @author:[罗少华]
	 */
	@RequestMapping("/activityStatements")
	public String activityStatementsList(
			@ModelAttribute("activityStatements") ActivityStatementsDTO activityStatementsDTO,
			Pager pager, Model model) {
		
		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}
		
		if(StringUtils.isNotBlank(activityStatementsDTO.getShopName())){
			ShopDTO shopDTO = new ShopDTO();
			shopDTO.setShopName(activityStatementsDTO.getShopName());
			ExecuteResult<DataGrid<ShopDTO>> shopResult = shopExportService.findShopInfoByCondition(shopDTO, null);
			List<Long> shopIds = new ArrayList<Long>();
			if (shopResult.isSuccess() 
					&& shopResult.getResult() != null 
					&& shopResult.getResult().getRows() != null
					&& shopResult.getResult().getRows().size() > 0) {
				for (ShopDTO dto : shopResult.getResult().getRows()) {
					shopIds.add(dto.getShopId());
				}
			} else{
				shopIds.add(-1L);
			}
			activityStatementsDTO.setShopIds(shopIds);
		}
		ActivityStatementsDTO inDTO = new ActivityStatementsDTO();
		BeanUtils.copyProperties(activityStatementsDTO, inDTO);
		// 默认查询有效的
		if (inDTO.getState() == null) {
			activityStatementsDTO.setState(1);
			inDTO.setState(1);
		} else if (inDTO.getState() == 0) {
			inDTO.setState(null);
		}
		// 查询列表
		ExecuteResult<DataGrid<ActivityStatementsDTO>> activityStatementsResult = this.activityStatementSerice
				.queryActivityStatementsDTO(inDTO, pager);
		// 查询店铺名称
		if (activityStatementsResult.isSuccess() && activityStatementsResult.getResult() != null
				&& activityStatementsResult.getResult().getRows() != null
				&& activityStatementsResult.getResult().getRows().size() > 0) {
			for(ActivityStatementsDTO dto : activityStatementsResult.getResult().getRows()){
				Long shopId = dto.getShopId();
				if(shopId != null){
					ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
					if(result.isSuccess() && result.getResult() != null){
						dto.setShopName(result.getResult().getShopName());
					}
				}
			}
		}
		Page<ActivityStatementsDTO> page = new Page<ActivityStatementsDTO>();
		page.setCount(activityStatementsResult.getResult().getTotal());
		page.setList(activityStatementsResult.getResult().getRows());
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("activityStatements", activityStatementsDTO);
		return "settlement/activityStatementsList";
	}
	/**
	 * 
	 * <p>Discription:[查询优惠活动结算单分页查询]</p>
	 * Created on 2015年12月16日
	 * @param request
	 * @return
	 * @author:[罗少华]
	 */
    @ResponseBody
    @RequestMapping("selectPageList")
    public Json<Page<Map<String,String>>> selectActivityStatementsList(HttpServletRequest request){
        Json json=new Json();
        try{
			Page page = new Page();
			Pager pager = new Pager();
            ActivityStatementsDTO activityStatementsDTO=new ActivityStatementsDTO();
            pager.setPage(new Integer(request.getParameter("page").toString()));
            pager.setRows(new Integer(request.getParameter("rows").toString()));
			// 订单id
			String orderId = request.getParameter("orderId");
			if (StringUtils.isNotBlank(orderId)) {
				activityStatementsDTO.setOrderId(orderId);
			}
			// 商店名称
			String shopName = request.getParameter("shopName");
			if (StringUtils.isNotBlank(shopName)) {
				activityStatementsDTO.setShopName(shopName);
			}
            //根据店铺名称查shopIds
            if(StringUtils.isNotBlank(activityStatementsDTO.getShopName())){
    			ShopDTO shopDTO = new ShopDTO();
    			shopDTO.setShopName(activityStatementsDTO.getShopName());
    			ExecuteResult<DataGrid<ShopDTO>> shopResult = shopExportService.findShopInfoByCondition(shopDTO, null);
    			List<Long> shopIds = new ArrayList<Long>();
    			if (shopResult.isSuccess() 
    					&& shopResult.getResult() != null 
    					&& shopResult.getResult().getRows() != null
    					&& shopResult.getResult().getRows().size() > 0) {
    				for (ShopDTO dto : shopResult.getResult().getRows()) {
    					shopIds.add(dto.getShopId());
    				}
    			} else{
    				shopIds.add(-1L);
    			}
    			activityStatementsDTO.setShopIds(shopIds);
    		}
			// 状态
			String state_ = request.getParameter("state");
			if (StringUtils.isNotBlank(state_)) {
				Integer state = Integer.parseInt(state_);
				if(state == 0){
					state = null;
				}
				activityStatementsDTO.setState(state);
			}
			// 创建开始时间
			String start = request.getParameter("createdBegin");
			// 创建结束时间
			String end = request.getParameter("createdEnd");
			if (StringUtils.isNotBlank(start)) {
				activityStatementsDTO.setCreatedBegin(start);
			}
			if (StringUtils.isNotBlank(end)) {
				activityStatementsDTO.setCreatedEnd(end);
			}
            ExecuteResult<DataGrid<ActivityStatementsDTO>> activityStatementsResult = this.activityStatementSerice
    				.queryActivityStatementsDTO(activityStatementsDTO, pager);
            
            List<ActivityStatementsDTO> listDto=activityStatementsResult.getResult().getRows();
			if (listDto != null && listDto.size() > 0) {
				// 查询店铺名称
				for(ActivityStatementsDTO dto : listDto){
    				Long shopId = dto.getShopId();
    				if(shopId != null){
    					ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
    					if(result.isSuccess() && result.getResult() != null){
    						dto.setShopName(result.getResult().getShopName());
    					}
    				}
    			}
                Iterator<ActivityStatementsDTO> iterator=listDto.iterator();
                List<Map<String,String>> listMap=new ArrayList<Map<String, String>>();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                int i=0;
                while(iterator.hasNext()){
                	ActivityStatementsDTO activityStatementsDTO1=iterator.next();
                    Map<String,String> map=new HashMap<String, String>();
                    i++;
                    map.put("num",String.valueOf(i));
                    map.put("orderId",activityStatementsDTO1.getOrderId()!=null?activityStatementsDTO1.getOrderId().toString():"");
                    map.put("shopId",activityStatementsDTO1.getShopId()!=null?activityStatementsDTO1.getShopId().toString():"");
                    map.put("shopName",activityStatementsDTO1.getShopName()!=null?activityStatementsDTO1.getShopName().toString():"");
                    if(activityStatementsDTO1.getCreateTime()!=null){
                        map.put("createTime",simpleDateFormat.format(activityStatementsDTO1.getCreateTime()));
                    }
                    if(activityStatementsDTO1.getUpdateTime()!=null){
                        map.put("updateTime",simpleDateFormat.format(activityStatementsDTO1.getUpdateTime()));
                    }
                    map.put("totalDiscountAmount",activityStatementsDTO1.getTotalDiscountAmount()!=null?activityStatementsDTO1.getTotalDiscountAmount().toString():"");
                    map.put("totalRefundAmount",activityStatementsDTO1.getTotalRefundAmount()!=null?activityStatementsDTO1.getTotalRefundAmount().toString():"");
                    map.put("totalSettleAmount",activityStatementsDTO1.getTotalSettleAmount()!=null?activityStatementsDTO1.getTotalSettleAmount().toString():"");
                    if(activityStatementsDTO1.getState()!=null&&!"".equals(activityStatementsDTO1.getState())&&activityStatementsDTO1.getState()==1){
                    	map.put("state","有效");
                    }
                    if(activityStatementsDTO1.getState()!=null&&!"".equals(activityStatementsDTO1.getState())&&activityStatementsDTO1.getState()==2){
                    	map.put("state","无效");
                    }
                    
                    listMap.add(map);
                }
                page.setList(listMap);
            }
            page.setCount(activityStatementsResult.getResult().getTotal());
            page.setPageNo(pager.getPage());
            page.setPageSize(pager.getRows());
            json.setObj(page);
            json.setSuccess(true);
            json.setMsg(page.toString());
        }catch(Exception e){
            json.setSuccess(false);
            json.setMsg("查询方法出错"+e.getMessage());
        }
        return json;
    }
    /**
	 * 导出优惠活动结算单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "exportCurrentpage")
    public String exportBuypage(HttpServletRequest request,HttpServletResponse response){
		Json<Page<Map<String, String>>> json = selectActivityStatementsList(request);
        if(json.isSuccess()){
            if(json.getObj()!=null){
                try{
                    String fileName = "优惠活动结算单"+ DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
					Page<Map<String, String>> page = json.getObj();
					if (page != null && page.getList() != null && page.getList().size() > 0) {
						List<Map<String, String>> listMap = page.getList();
						List<ActivityStatements> listuser = new ArrayList<ActivityStatements>();
						for (int i = 0; i < listMap.size(); i++) {
							ActivityStatements activityStatements = new ActivityStatements();
							PropertyUtils.copyProperties(activityStatements, listMap.get(i));
							listuser.add(activityStatements);
						}
						new ExportExcel("优惠活动结算单", ActivityStatements.class).setDataList(listuser).write(response, fileName).dispose();
					}
                }catch(Exception e){
                    logger.error("导出优惠活动结算单数据出现异常"+e.getMessage());
                }
            }
        }
        return null;
    } 	
	

	@RequestMapping(value = "settle")
	@ResponseBody
	public Map settle(@RequestParam(required = false) Long[] ids) {
		Map map = new HashMap();

		if (ids != null && ids.length > 0) {
			// SettlementQuery dto = new SettlementQuery();
			// for(Long id:ids){
			// dto.setSettlement_id(id);
			// ExecuteResult<DataGrid<SettlementCombinDTO>> resultData =
			// statementExportService.querySettlementList(dto, null);
			//
			// }
			//有资金流转
//			ExecuteResult<String> result = statementExportService.modifySettlementStates(ids);
			//无资金流转 只修改状态
			ExecuteResult<String> result = statementExportService.modifySettlementStatesNoPay(ids);
			
			if (result.isSuccess()) {
				map.put("success", true);
				map.put("msg", result.getResultMessage());
				map.put("result", result.getResult());
			} else {
				map.put("success", false);
				map.put("msg", result.getErrorMessages());
			}
		} else {
			map.put("success", false);
			map.put("msg", "传入的参数为空");
		}
		return map;
	}

	@RequestMapping(value = "backrate")
	public String backrate(@ModelAttribute BackRateQuery dto, Pager pager,
			Model model) {
		// dto.setCategoryId(dto.getTcid());
		dto.setTcid(dto.getCategoryId());
		//查询类目优先级 3>2>1
        if(dto.getCategoryId()==null){
        	if(dto.getSubcid()!=null && dto.getSubcid()>0){
        		dto.setCategoryId(dto.getSubcid());
        	}else if (dto.getFcid()!=null && dto.getFcid()>0){
        		dto.setCategoryId(dto.getFcid());
        	}
        }

		DataGrid itemsdatagrid = itemCategoryService.queryItemCategoryList(0L);
		if (itemsdatagrid != null) {
			model.addAttribute("platformList", itemsdatagrid.getRows());
		} else {
			model.addAttribute("platformList", new ArrayList());
		}

		DataGrid subdatagrid = itemCategoryService.queryItemCategoryList(dto
				.getFcid());
		if (subdatagrid != null) {
			model.addAttribute("subItemList", subdatagrid.getRows());
		} else {
			model.addAttribute("subItemList", new ArrayList());
		}

		DataGrid tdatagrid = itemCategoryService.queryItemCategoryList(dto
				.getSubcid());
		if (subdatagrid != null) {
			model.addAttribute("tItemList", tdatagrid.getRows());
		} else {
			model.addAttribute("tItemList", new ArrayList());
		}

		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}

		ExecuteResult<DataGrid<SettleCatExpenseDTO>> result = sattleCatExpenseExportService
				.queryCategoryExpenseList(dto, pager);
		DataGrid<SettleCatExpenseDTO> dataGrid = result.getResult();
		List<SettleCatExpenseDTO> settleCatExpenseDTOList = dataGrid.getRows();

		Long[] ids = new Long[settleCatExpenseDTOList.size()];
		int i = 0;
		for (SettleCatExpenseDTO s : settleCatExpenseDTOList) {
			ids[i] = s.getCategoryId();
			i++;
		}

		Map<Long, Map> itemMap = itemService.getAllParent(ids);

		List<BackRateQuery> backRateQueryList = new ArrayList<BackRateQuery>();
		for (SettleCatExpenseDTO s : settleCatExpenseDTOList) {
			// 封装类目信息
			BackRateQuery backRateQuery = new BackRateQuery();
			BeanUtils.copyProperties(s, backRateQuery);
			Map map = itemMap.get(s.getCategoryId());
			if (map != null) {
				backRateQuery.setFcid((Long) map.get("cid"));
				backRateQuery.setFcname((String) map.get("cname"));
				backRateQuery.setSubcid((Long) map.get("subcid"));
				backRateQuery.setSubcname((String) map.get("subcname"));
				backRateQuery.setTcid((Long) map.get("tcid"));
				backRateQuery.setTcmane((String) map.get("tcname"));
			}
			backRateQueryList.add(backRateQuery);

		}
		Page<BackRateQuery> page = new Page<BackRateQuery>();
		page.setCount(dataGrid.getTotal());
		page.setList(backRateQueryList);
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("backrate", dto);
		return "settlement/backrate";
	}

	@RequestMapping(value = "addBackRate")
	public String addBackRate(@ModelAttribute BackRateQuery dto, Pager pager,
			Model model) {

		Long id = dto.getId();
		if (null == id) {
			ExecuteResult<String> result = sattleCatExpenseExportService
					.insertCategoryExpense(dto);

			if (result.isSuccess()) {
				addMessage(model, "添加成功");
			} else {
				addMessage(model, "添加失败:" + result.getErrorMessages());
			}
		} else {
			ExecuteResult<String> result = sattleCatExpenseExportService
					.modifyCategoryExpense(dto);
			if (result.isSuccess()) {
				addMessage(model, "修改成功");
			} else {
				addMessage(model, "修改失败:" + result.getErrorMessages());
			}

		}

		return backrate(new BackRateQuery(), pager, model);
	}

	// 删除记录
	@RequestMapping(value = "deleteBr")
	public String deleteBr(@ModelAttribute BackRateQuery dto, Pager pager,
			Model model) {
		sattleCatExpenseExportService.deleteById(dto.getId());
		dto.setId(null);
		return backrate(dto, pager, model);
	}

	@RequestMapping(value = "payrecord")
	public String payrecord(TradeOrdersQueryInDTO dto, Pager pager, Model model) {
		DataGrid items = itemCategoryService.queryItemCategoryList(0L);
		if (items != null) {
			model.addAttribute("platformList", items.getRows());
		} else {
			model.addAttribute("platformList", new ArrayList());
		}
		if (pager.getPage() < 1) {
			pager.setPage(1);
		}
		if (pager.getRows() < 1) {
			pager.setRows(20);
		}

		ExecuteResult<DataGrid<TradeOrdersDTO>> result = tradeOrderExportService.queryOrders(dto, pager);
		result = isServiceOrder(result);
		DataGrid<TradeOrdersDTO> dataGrid = result.getResult();
		
		Page<TradeOrdersDTO> page = new Page<TradeOrdersDTO>();
		page.setCount(dataGrid.getTotal());
		page.setList(dataGrid.getRows());
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		// model.addAttribute("",);
		model.addAttribute("orderQuery", dto);
		return "settlement/payrecord";
	}

	/**
	 * 
	 * @param ordersDTO
	 * @param flag
	 *            0导出分页,1导出所有数据
	 * @param pager
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "exportPay")
	public String exportPay(TradeOrdersQueryInDTO ordersDTO, Integer flag,
			Pager pager, HttpServletResponse response) {
		String fileName = "支付记录" + DateUtils.getDate("yyyyMMddHHmmss")
				+ ".xlsx";
		try {
			ExecuteResult<DataGrid<TradeOrdersDTO>> result = null;
			if (0 == flag) {// 导出分页
				result = tradeOrderExportService.queryOrders(ordersDTO, pager);
			}
			if (1 == flag) {// 导出所有数据
				result = tradeOrderExportService.queryOrders(ordersDTO, null);
			}
			List orderList = new ArrayList();
			for (TradeOrdersDTO dto : result.getResult().getRows()) {
				PayRecordQuery query = new PayRecordQuery();
				try {
					PropertyUtils.copyProperties(query, dto);
				} catch (IllegalAccessException e) {
					logger.error("复制订单属性出错", e);
				} catch (InvocationTargetException e) {
					logger.error("复制订单属性出错", e);
				} catch (NoSuchMethodException e) {
					logger.error("复制订单属性出错", e);
				}
				orderList.add(query);
			}

			new ExportExcel("支付记录导出", PayRecordQuery.class)
					.setDataList(orderList).write(response, fileName).dispose();
		} catch (IOException e) {
			logger.error("支付记录导出出错", e);
		}
		return null;
	}

	@RequestMapping(value = "cidValidate")
	@ResponseBody
	public boolean cidValidate(Long categoryId) {
		ExecuteResult<List<SettleCatExpenseDTO>> result = sattleCatExpenseExportService
				.queryByIds(categoryId);
		if (result.getResult() != null && result.getResult().size() > 0) {
			return false;
		} else {
			return true;
		}
	}

	@RequestMapping(value = "import")
	public String imp(BankSettleDetailDTO dto, Pager pager, Model model) {
		// BankSettleDetailDTO bankSettleDetailDTO = new BankSettleDetailDTO();
		DataGrid<BankSettleDetailDTO> dataGrid = statementExportService
				.findBankSettle(dto, pager);
		Page<BankSettleDetailDTO> page = new Page<BankSettleDetailDTO>();
		page.setCount(dataGrid.getTotal());
		page.setList(dataGrid.getRows());
		page.setPageNo(pager.getPage());
		page.setPageSize(pager.getRows());
		model.addAttribute("page", page);
		model.addAttribute("comletedTimeStart", dto.getComletedTimeStart());
		model.addAttribute("comletedTimeEnd", dto.getComletedTimeEnd());
		model.addAttribute("outTradeNo", dto.getOutTradeNo());

		return "settlement/importsettle";
	}

	@RequestMapping(value = "importSettle")
	@ResponseBody
	public Map importSettle(MultipartFile file, String type) {

		Map map = new HashMap();

		List<BankSettleDetailDTO> bsDTOList = new ArrayList<BankSettleDetailDTO>();
		try {
			ImportExcel ei = null;
			// 如果是导入的支付宝结算单
			if (BankSettleTypeEnum.AP.getLable().equals(type)) {
				ei = new ImportExcel(file, 2, 0);
				List<AlipaySettlementDetail> alipaySettlementList = ei
						.getDataList(AlipaySettlementDetail.class);
				for (AlipaySettlementDetail query : alipaySettlementList) {
					// if(StringUtils.isEmpty(query.getNo()) ||
					// query.getNo().length()>10){
					// map.put("success",false);
					// map.put("msg", "导入的文件不是支付宝结算明细单！");
					// return map;
					// }
					if (StringUtils.isEmpty(query.getNo())
							&& query.getNo().startsWith("#")) {
						continue;
					}
					if(!(query.getFinanceType()!=null
							&&query.getFinanceType().equals("交易")
							&&query.getLiquidateStatusString() != null
							&& query.getLiquidateStatusString().contains(
									"已清算"))){
						continue;
					}
					if (query.getOutTradeNo().toString() != null
							&& query.getOutTradeNo().toString().startsWith("201")) {
						BankSettleDetailDTO dto = new BankSettleDetailDTO();
						if (query.getOrderStats() != null
								&& query.getOrderStats().contains("成功")) {
							dto.setOrderStatus(2);
						} else {
							// dto.setOrderStatus(1);
							map.put("success", false);
							map.put("msg", "存在订单状态不成功的记录！");
							return map;
						}
						dto.setOutTradeNo(query.getOutTradeNo());
						dto.setBankType(type);
						dto.setOrderAmount(new BigDecimal(query.getIncome()));
						dto.setCallBankTime(new Date());
						dto.setCompletedTime(DateUtils.parseDate(query
								.getBillTime()));

						dto.setFactorage(new BigDecimal(query.getFactorage()));
						dto.setRemark1(query.getRemark());

						if (query.getLiquidateStatusString() != null
								&& query.getLiquidateStatusString().contains(
										"已清算")) {
							dto.setLiquidateStatus(2);
						} else {
							// dto.setLiquidateStatus(1);
							map.put("success", false);
							map.put("msg", "存在清算状态为未清算的记录！");
							return map;
						}

						dto.setCreateId(UserUtils.getUser().getId());

						bsDTOList.add(dto);
					} else {
						// 订单号为
					}

				}
			} else if (BankSettleTypeEnum.CB.getLable().equals(type)) {
				ei = new ImportExcel(file, 0, 0);
				List<SettlementImportQuery> list = ei
						.getDataList(SettlementImportQuery.class);
				for (SettlementImportQuery query : list) {
					// if(query.getOrderNo().length()!=17){
					// map.put("success",false);
					// map.put("msg", "导入的文件不是网银在线结算明细单！");
					// return map;
					// }
					if (query.getOrderNo() != null
							&& query.getOrderNo().startsWith("301")) {
						BankSettleDetailDTO dto = new BankSettleDetailDTO();
						if (query.getOrderStatsString() != null
								&& query.getOrderStatsString().contains("成功")) {
							dto.setOrderStatus(2);
						} else {
							// dto.setOrderStatus(1);
							map.put("success", false);
							return map;
						}
						dto.setOutTradeNo(query.getOrderNo());
						dto.setBankType(type);
						dto.setOrderAmount(new BigDecimal(query
								.getOrderAmount()));
						dto.setCallBankTime(DateUtils.parseDate(query
								.getCallBankTime()));
						dto.setCompletedTime(DateUtils.parseDate(query
								.getCompletedTime()));

						dto.setFactorage(new BigDecimal(query.getFactorage()));
						dto.setRemark1(query.getRemark1());
						dto.setRemark2(query.getRemark2());

						if (query.getLiquidateStatusString() != null
								&& query.getLiquidateStatusString().contains(
										"已清算")) {
							dto.setLiquidateStatus(2);
						} else {
							dto.setLiquidateStatus(1);
						}

						dto.setCreateId(UserUtils.getUser().getId());

						bsDTOList.add(dto);
					} else {
						// 订单号为
					}

				}
			} else if (BankSettleTypeEnum.WX.getLable().equals(type)) {
				ei = new ImportExcel(file, 0, 0);
				List<WXSettlementDetail> wxSettlementDetail = ei
						.getDataList(WXSettlementDetail.class);
				for (WXSettlementDetail query : wxSettlementDetail) {
					if (!query.getBillTime().startsWith("`")) {
						continue;
					}
					if (query.getOutTradeNo() != null
							&& query.getOutTradeNo().startsWith("`101")) {
						BankSettleDetailDTO dto = new BankSettleDetailDTO();
						if (query.getOrderStauts() != null
								&& query.getOrderStauts().substring(1).contains("SUCCESS")) {
							dto.setOrderStatus(2);
						} else {
							// dto.setOrderStatus(1);
							map.put("success", false);
							map.put("msg", "存在订单状态不成功的记录！");
							return map;
						}
						dto.setOutTradeNo(query.getOutTradeNo().substring(1));
						dto.setBankType(type);
						dto.setOrderAmount(new BigDecimal(query.getTotalAmount().substring(1)));
						dto.setCallBankTime(new Date());
						dto.setCompletedTime(DateUtils.parseDate(query
								.getBillTime().substring(1)));

						dto.setFactorage(new BigDecimal(query.getFactorage().substring(1)));

						if (query.getLiquidateStatusString() != null
								&& query.getLiquidateStatusString().contains(
										"已清算")) {
							dto.setLiquidateStatus(2);
						} else {
							// dto.setLiquidateStatus(1);
							map.put("success", false);
							map.put("msg", "存在清算状态为未清算的记录！");
							return map;
						}

						dto.setCreateId(UserUtils.getUser().getId());

						bsDTOList.add(dto);
					} else {
						// 订单号为
					}

				}
			}
			if(bsDTOList.size()>0){
				ExecuteResult<String> result = statementExportService
						.saveBankSettle(bsDTOList);
				if (result.isSuccess()) {
					map.put("success", true);
					map.put("msg", "导入成功");
				} else {
					map.put("success", false);
					map.put("msg", result.getErrorMessages());
				}
			}else{
				map.put("success", false);
				map.put("msg", "导入数据为空");
			}
		} catch (Exception e) {
			logger.error("导入异常", e);
			map.put("success", false);
			map.put("msg", "导入出现异常" + e.getMessage());
		}
		return map;
	}
	
	private ExecuteResult<DataGrid<TradeOrdersDTO>> isServiceOrder(ExecuteResult<DataGrid<TradeOrdersDTO>> result){
		if( null != result.getResult() && null != result.getResult().getRows() && !result.getResult().getRows().isEmpty() && result.getResult().getRows().size() > 0){
			int i = 0;
			int orderType ; 
			for(TradeOrdersDTO tradeOrdersDTO : result.getResult().getRows()){
				List<TradeOrderItemsDTO> orderList = tradeOrdersDTO.getItems();
				//纯服务订单
				result.getResult().getRows().get(i).setOrderType(3);
				if(null != orderList && !orderList.isEmpty() && orderList.size() > 0){
		        	for(TradeOrderItemsDTO item : orderList){
		        		ExecuteResult<ItemDTO> itemResult =itemExportService.getItemById(item .getItemId());
		        		if(null != itemResult.getResult()){
		        			if(itemResult.getResult().getAddSource() < 4){
		        				//非纯服务订单
		        				result.getResult().getRows().get(i).setOrderType(4);
		        				break;
		        			}
		        		}
		        	}
		        }
				i++;
			}
		}
    	return result;
    }
}
