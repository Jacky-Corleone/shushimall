package com.camelot.mall.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.ItemSkuSellReportIn;
import com.camelot.report.dto.ItemSkuSellReportOut;
import com.camelot.report.service.ItemSkuSellReportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/salesAnalysis")
public class SalesAnalysisController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SalesAnalysisController.class);
	@Resource
	private ItemSkuSellReportService itemSkuSellReportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private SearchItemExportService searchItemExportService;
	@Resource
	private TradeInventoryExportService tradeInventoryExportService;
	
	/**
	 *  销售分页页面展示
	 * @param request
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping("/init")
	public String initSalesAnalysis(HttpServletRequest request,Pager<ItemSkuSellReportOut> pager , Model model) {
		String perItemId = request.getParameter("itemId");
		String perSkuId = request.getParameter("skuId");
		String itemName = request.getParameter("itemName");
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			return "redirect:/user/login";
		}
		String url = "/analysis/salesAnalysis";
		Long shopId = WebUtil.getInstance().getShopId(request);
		
		//获取当前店铺的所有在售商品
		ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
		//拼装查询条件
		Integer itemStatus = ItemStatusEnum.SALING.getCode();
		Long[] shopIds = {shopId};
		itemInDTO.setItemStatus(itemStatus);
		itemInDTO.setShopIds(shopIds);
		DataGrid<ItemQueryOutDTO> queryResult = itemExportService.queryItemList(itemInDTO, null);
		if(queryResult == null || queryResult.getRows().size() == 0){
			LOGGER.info("该店铺："+shopId+"下不存在商品！");
			model.addAttribute("dealDateList", "1");
			model.addAttribute("sellPriceTotalList","1");
			model.addAttribute("sellTotalNumList","1");
			model.addAttribute("orderNumList","1");
			model.addAttribute("orderPayNumList","1");
			model.addAttribute("itemSize",0);
			return url;
		}
		//如果该店铺存在商品
		List<ItemQueryOutDTO> itemList = queryResult.getRows();
		//商品信息返回到页面
		model.addAttribute("itemList", itemList);
		model.addAttribute("itemSize",itemList.size());
		//默认显示第一个商品的sku信息
		ItemQueryOutDTO item =  itemList.get(0);
		Long itemId = 0L;
		if(!StringUtils.isEmpty(perItemId)){
			itemId = Long.parseLong(perItemId);
		}else{
			itemId = item.getItemId();
		}
		if(!StringUtils.isEmpty(itemName)){
			model.addAttribute("itemName", itemName);
		}else{
			model.addAttribute("itemName", item.getItemName());
		}
		model.addAttribute("itemInfo", item);
		TradeInventoryInDTO dto = new TradeInventoryInDTO();
		
		dto.setItemId(itemId);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> executeResult = tradeInventoryExportService.queryTradeInventoryList(dto, null);
		
		if(!executeResult.isSuccess()){
			return "";
		}
		List<TradeInventoryOutDTO> tradeInventoryOutDTOList = executeResult.getResult().getRows();
		model.addAttribute("tradeInventoryOutDTOList", tradeInventoryOutDTOList);
		
		ExecuteResult<ItemDTO> itemResult =  itemExportService.getItemById(itemId);
		 
		if(!itemResult.isSuccess()){
			LOGGER.info("查询商品失败！商品id:"+itemId);
			return url;
		}
		ItemDTO itemDTO = itemResult.getResult();
		model.addAttribute("item", itemDTO);
		
		if(itemDTO == null){
			LOGGER.info("查询商品失败！商品id:"+itemId);
			return url;
		}
		//sku信息，并返回到页面
		List<ItemAttr> attrSaleList =  itemDTO.getAttrSale();
		model.addAttribute("attrSaleList", attrSaleList);
		
		//页面的折线图显示和列表显示，开始默认显示第一个sku属性的销售统计
		if(attrSaleList.size() == 0){
			LOGGER.info("商品不存在销售属性！商品ID："+itemId);
			return url;
		}
		//获取sku属性，返回到页面
//		ItemAttr attrSale = attrSaleList.get(0);
		//查询sku
//		ItemShopCartDTO dto = new ItemShopCartDTO();
//		dto.setAreaCode(region);//省市区编码
//		dto.setSkuId(skuInfo.getSkuId());//SKU id
//		dto.setQty(1);//数量
//		dto.setShopId(item.getShopId());//店铺ID
//		dto.setItemId(itemId);//商品ID
//		ExecuteResult<ItemShopCartDTO> skuItem = itemExportService.getSkuShopCart(dto); //调商品接口查url
		
//		model.addAttribute("attrSale", attrSale);
		
		List<SkuInfo> skuInfoList = itemDTO.getSkuInfos();
		if(skuInfoList.size() == 0){
			LOGGER.info("商品不存在sku属性！商品ID："+itemId);
			return url;
		}
		Long skuId = 0L;
		if(!StringUtils.isEmpty(perSkuId)){
			skuId = Long.parseLong(perSkuId);
		}else{
			skuId = skuInfoList.get(0).getSkuId();
		}
		//获取当前店铺shopId中商品sku属性的销售状况折线图
		model.addAttribute("skuId", skuId);
		int dayInterval = 7;//折线图间隔时间（天）
		ItemSkuSellReportIn itemSkuSellReportIn = new ItemSkuSellReportIn();
		itemSkuSellReportIn.setShopId(shopId);
		itemSkuSellReportIn.setSkuId(skuId);
		itemSkuSellReportIn.setDayInterval(dayInterval);
		String startDate = request.getParameter("startDate");
		itemSkuSellReportIn.setStartDate(startDate);
		String endDate = request.getParameter("endDate");
		itemSkuSellReportIn.setEndDate(endDate);
		itemSkuSellReportIn.setDateFormat("yyyy-MM-dd");//设置时间格式
		DataGrid<ItemSkuSellReportOut> itemSkuSellReportResult = null;
		try {
			itemSkuSellReportResult = itemSkuSellReportService.getItemSkuSellListByLion(itemSkuSellReportIn);
		} catch (Exception e) {
			LOGGER.error("获取 销售额、销售量 折线图 失败！");
			e.printStackTrace();
		}
		model.addAttribute("startDate",startDate);
		model.addAttribute("endDate",endDate);
		//即便折线图数据为空，也需要查询销售额和销售量的列表数据
		List<String> sellPriceTotalList = new ArrayList<String>(dayInterval);
		List<String> sellTotalNumList = new ArrayList<String>(dayInterval);
		List<String> dealDateList = new ArrayList<String>(dayInterval);
		if(itemSkuSellReportResult != null && itemSkuSellReportResult.getRows() != null){
			List<ItemSkuSellReportOut> itemSkuSellReportList = itemSkuSellReportResult.getRows();
			for(ItemSkuSellReportOut itemSkuSellReport : itemSkuSellReportList){
				sellPriceTotalList.add(itemSkuSellReport.getSellPriceTotal()+"");
				sellTotalNumList.add(itemSkuSellReport.getSellTotalNumStr());
				dealDateList.add("'"+itemSkuSellReport.getDealDate()+"'");
			}
		}
		model.addAttribute("sellTotalNumList", sellTotalNumList.toString());
		model.addAttribute("sellPriceTotalList", sellPriceTotalList.toString());
		model.addAttribute("dealDateList", dealDateList.toString());
		 
		
		//查询销售额、销售量列表数据
		DataGrid<ItemSkuSellReportOut> itemSkuSellReportListResult = itemSkuSellReportService.getItemSkuSellListByPager(itemSkuSellReportIn, pager);
		if(itemSkuSellReportListResult != null && itemSkuSellReportListResult.getRows().size() != 0){
			model.addAttribute("itemSkuSellReportList", itemSkuSellReportListResult.getRows());
			pager.setTotalCount(itemSkuSellReportListResult.getTotal().intValue());
			model.addAttribute("pager", pager);
		}
		
		return url;
	}
	/**
	 * 点击商品图片时，获取sku，和销售额与销售量
	 * @param request
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/getItemSKUByItemId")
	@ResponseBody
	public Map<String,Object> getItemSKUByItemId(HttpServletRequest request,Long itemId ){
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		ExecuteResult<ItemDTO> itemResult =  itemExportService.getItemById(itemId);
		if(!itemResult.isSuccess()){
			LOGGER.info("查询商品失败！商品id:"+itemId);
			return null;
		}
		ItemDTO itemDTO = itemResult.getResult();
		
		if(itemDTO == null){
			LOGGER.info("查询商品失败！商品id:"+itemId);
			return null;
		} 
		//sku信息，并返回到页面
		List<SkuInfo> skuInfoList =  itemDTO.getSkuInfos();
		if(skuInfoList == null || skuInfoList.size() == 0){
			return null;
		}
		//默认显示第一个sku的销售额和销售量
		Long skuId = skuInfoList.get(0).getSkuId();
		Long shopId = WebUtil.getInstance().getShopId(request);
		ItemSkuSellReportIn itemSkuSellReportIn = new ItemSkuSellReportIn();
		itemSkuSellReportIn.setShopId(shopId);
		itemSkuSellReportIn.setSkuId(skuId);
		itemSkuSellReportIn.setStartDate(startDate);
		itemSkuSellReportIn.setEndDate(endDate);
		Map<String,Object> resultMap = this.getSkuSellReportLine(itemSkuSellReportIn);
//		resultMap.put("skuInfoList", skuInfoList);
//		resultMap.put("item", itemDTO);
//		Map<String,Object> resultMap = new HashMap<String,Object>();
		TradeInventoryInDTO dto = new TradeInventoryInDTO();
		dto.setItemId(itemId);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> executeResult = tradeInventoryExportService.queryTradeInventoryList(dto, null);
		
		if(!executeResult.isSuccess()){
			return null;
		}
		List<TradeInventoryOutDTO> tradeInventoryOutDTOList = executeResult.getResult().getRows();
		resultMap.put("tradeInventoryOutDTOList",tradeInventoryOutDTOList);
		
		//获取列表数据
		Pager<ItemSkuSellReportOut> pager = new Pager<ItemSkuSellReportOut>();
		 
		Map<String,Object> resultMap2 = this.getSkuSellReportList(itemSkuSellReportIn,pager);
		resultMap.put("itemSkuSellReportList", resultMap2.get("itemSkuSellReportList"));
		int listSize=0;
		if(resultMap2.get("listSize")!=null){
			listSize=Integer.parseInt(resultMap2.get("listSize").toString());
		}
		pager.setTotalCount(listSize);
		resultMap.put("pager", pager);
		return resultMap;
	}
	
	/**
	 * 根据销售属性获取销售的折线图
	 * @param shopId
	 * @param skuId
	 * @return
	 */
	@RequestMapping("/getSKUSellLineById")
	@ResponseBody
	public Map<String,Object> getSKUSellLineById(HttpServletRequest request,Long skuId){
		Long shopId = WebUtil.getInstance().getShopId(request);
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		
		ItemSkuSellReportIn itemSkuSellReportIn = new ItemSkuSellReportIn();
		itemSkuSellReportIn.setShopId(shopId);
		itemSkuSellReportIn.setSkuId(skuId);
		itemSkuSellReportIn.setStartDate(startDate);
		itemSkuSellReportIn.setEndDate(endDate);
		Map<String,Object> resultMap = this.getSkuSellReportLine(itemSkuSellReportIn);
		//获取列表数据
		Pager<ItemSkuSellReportOut> pager = new Pager<ItemSkuSellReportOut>();
		 
		Map<String,Object> resultMap2 = this.getSkuSellReportList(itemSkuSellReportIn,pager);
		System.out.println(resultMap2.get("listSize"));
		String listSize = null;
		if(resultMap2.get("listSize") == null){
			listSize = "0";
		}else{
			listSize = resultMap2.get("listSize").toString();
		}
		 
		pager.setTotalCount(Integer.parseInt(listSize));
		resultMap.put("pager", pager);
		resultMap.put("itemSkuSellReportList", resultMap2.get("itemSkuSellReportList"));
		return resultMap;
	}

	@RequestMapping("/getSKUSellListById")
	@ResponseBody
	public Map<String,Object> getSKUSellListById(HttpServletRequest request,Long skuId,Pager<ItemSkuSellReportOut> pager){
		
		Long shopId = WebUtil.getInstance().getShopId(request);
		
		ItemSkuSellReportIn itemSkuSellReportIn = new ItemSkuSellReportIn();
		itemSkuSellReportIn.setShopId(shopId);
		itemSkuSellReportIn.setSkuId(skuId);
		Map<String,Object> resultMap = this.getSkuSellReportList(itemSkuSellReportIn,pager);
		
		return resultMap;
	}
	
	
	/**
	 * 根据shopId 和 skuID获取销售折线图数据
	 * 
	 * @param itemSkuSellReportIn
	 * @return
	 */
	private Map<String,Object> getSkuSellReportLine(ItemSkuSellReportIn itemSkuSellReportIn){
		int dayInterval = 7 ;//折线图时间间隔
		itemSkuSellReportIn.setDayInterval(dayInterval);
		itemSkuSellReportIn.setDateFormat("yyyy-MM-dd");
		DataGrid<ItemSkuSellReportOut> itemSkuSellReportResult = null;
		try {
			itemSkuSellReportResult = itemSkuSellReportService.getItemSkuSellListByLion(itemSkuSellReportIn);
		} catch (Exception e) {
			LOGGER.error("获取 销售额、销售量 折线图 失败！");
			e.printStackTrace();
		}
		if(itemSkuSellReportResult ==null || itemSkuSellReportResult.getRows().size() == 0){
			return new HashMap<String,Object>(3);
		}
		//转换为前台需要的数组格式
		Map<String,Object> resultMap = new HashMap<String,Object>(3);
		List<String> sellPriceTotalList = new ArrayList<String>(dayInterval);
		List<String> sellTotalNumList = new ArrayList<String>(dayInterval);
		List<String> dealDateList = new ArrayList<String>(dayInterval);
		if(itemSkuSellReportResult != null && itemSkuSellReportResult.getRows().size() != 0){
			List<ItemSkuSellReportOut> itemSkuSellReportList = itemSkuSellReportResult.getRows();
			for(ItemSkuSellReportOut itemSkuSellReport : itemSkuSellReportList){
				sellPriceTotalList.add(itemSkuSellReport.getSellPriceTotal()+"");
				sellTotalNumList.add(itemSkuSellReport.getSellTotalNumStr());
				dealDateList.add(itemSkuSellReport.getDealDate());
			}
		}
		resultMap.put("sellPriceTotalList", sellPriceTotalList);
		resultMap.put("sellTotalNumList", sellTotalNumList);
		resultMap.put("dealDateList", dealDateList);
		return resultMap;
	}
	
	/**
	 * 根据shopId 和 skuID获取销售列表数据
	 * 
	 * @param itemSkuSellReportIn
	 * @return
	 */
	private Map<String,Object> getSkuSellReportList(ItemSkuSellReportIn itemSkuSellReportIn,Pager<ItemSkuSellReportOut> pager){
		Map<String , Object> resultMap = new HashMap<String , Object>();
		//查询销售额、销售量列表数据
		 
		DataGrid<ItemSkuSellReportOut> itemSkuSellReportListResult = itemSkuSellReportService.getItemSkuSellListByPager(itemSkuSellReportIn, pager);
		if(itemSkuSellReportListResult != null && itemSkuSellReportListResult.getRows().size() != 0){
			resultMap.put("itemSkuSellReportList", itemSkuSellReportListResult.getRows());
			resultMap.put("listSize", itemSkuSellReportListResult.getTotal());
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/export")
	public String export(HttpServletRequest request,ItemSkuSellReportIn itemSkuSellReportIn,Pager<ItemSkuSellReportOut> pager, Model model){
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			return "redirect:/user/login";
		}
		//获取店铺ID
		Long shopId = WebUtil.getInstance().getShopId(request);
		if(shopId == null){
			return "redirect:/user/login";
		}
		String skuId  = request.getParameter("skuId");
		itemSkuSellReportIn.setShopId(shopId);
		itemSkuSellReportIn.setDateFormat("yyyy-MM-dd");
		itemSkuSellReportIn.setSkuId(Long.parseLong(skuId));
		 
		DataGrid<ItemSkuSellReportOut> itemSkuSellReportListResult = itemSkuSellReportService.getItemSkuSellListByPager(itemSkuSellReportIn, pager);
		List<Map<String,Object>> excelList = new ArrayList<Map<String,Object>>();
		
		if(itemSkuSellReportListResult != null && itemSkuSellReportListResult.getRows().size() != 0){
			
			List<ItemSkuSellReportOut> itemSkuSellReportList =itemSkuSellReportListResult.getRows();
			int i = 0 ;
			
			for(ItemSkuSellReportOut itemSkuSellReportOut : itemSkuSellReportList){
				Map<String,Object> map = new HashMap<String,Object>();
				i++;
				map.put("no", i+"");
				String dealDate = itemSkuSellReportOut.getDealDate();
				map.put("dealDate", dealDate);
				String sellPriceTotal = itemSkuSellReportOut.getSellPriceTotalStr();
				map.put("sellPriceTotal", sellPriceTotal);
				String sellTotalNum = itemSkuSellReportOut.getSellTotalNumStr();
				map.put("sellTotalNum", sellTotalNum);
				excelList.add(map);
			}
		}
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(excelList);

		// 动态指定报表模板url
		model.addAttribute("url", "/WEB-INF/jasper/sales_analysis_excel.jasper");
		model.addAttribute("format", "xls"); // 报表格式
		model.addAttribute("jrMainDataSource", jrDataSource);

		return "reportView"; // 对应jasper-views.xml中的bean id
	}
	
}
