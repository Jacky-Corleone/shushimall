package com.camelot.mall.analysis;

import java.math.BigDecimal;
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

import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.report.dto.OrderDealReportIn;
import com.camelot.report.dto.OrderDealReportOut;
import com.camelot.report.service.OrderDealReportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/businessAnalysis")
public class BusinessAnalysisController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BusinessAnalysisController.class);
	@Resource
	private OrderDealReportService orderDealReportService;
	
	/**
	 * 运营情况分析
	 * 
	 * @author 王东晓
	 * create date 2015-07-17
	 * 
	 * @param request
	 * @param orderDealReportInDTO   查询入参，店铺IDshopID、开始时间startDate、结束时间endDate
	 * @param pager  分页
	 * @param model
	 * @return
	 */
	@RequestMapping("/init")
	public String initBusinessAnalysis(HttpServletRequest request,OrderDealReportIn orderDealReportInDTO,Pager<OrderDealReportOut> pager, Model model) {
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			return "redirect:/user/login";
		}
		//获取店铺ID
		Long shopId = WebUtil.getInstance().getShopId(request);
		if(shopId == null){
			return "redirect:/user/login";
		}
		if(orderDealReportInDTO==null){
			orderDealReportInDTO = new OrderDealReportIn();
		}
		//将查询条件返回到页面
		model.addAttribute("orderDealReportInDTO", orderDealReportInDTO);
		orderDealReportInDTO.setShopId(shopId);
		orderDealReportInDTO.setDateFormat("yyyy-MM-dd");
//		orderDealReportInDTO.setStartDate(orderDealReportInDTO.getStartDate());
//		orderDealReportInDTO.setEndDate(orderDealReportInDTO.getEndDate());
		//获取运营状况的的总体情况，  成交金额、购买人数、成交商品数、下单量、成交转化率
		OrderDealReportOut  orderDealReportOutDTO = orderDealReportService.getOrderDealSum(orderDealReportInDTO);
		//返回页面运营状况的总体情况DTO
		model.addAttribute("orderDealReportOutDTO", orderDealReportOutDTO);
//		
		//获取运营状况的情况列表， 成交金额、购买人数、成交商品数、下单量、成交转化率
		DataGrid<OrderDealReportOut> orderDealReportOutResult = orderDealReportService.getOrderDealListByPager(orderDealReportInDTO, pager);
		
		if(orderDealReportOutResult != null&&orderDealReportOutResult.getRows() != null){
			List<OrderDealReportOut> orderDealReportOutList =orderDealReportOutResult.getRows();
			model.addAttribute("orderDealReportOutList", orderDealReportOutList);
			//分页
			pager.setTotalCount(orderDealReportOutResult.getTotal().intValue());
			model.addAttribute("pager", pager);
			
		}
		//获取运营状况的折线图数据     成交金额、购买人数、成交商品数、下单量、成交转化率 
		int dayInterval = 7;//折线图的日期间隔
		DataGrid<OrderDealReportOut> orderDealReportOutLineResult =null;
		try {
			orderDealReportInDTO.setDayInterval(dayInterval);
			orderDealReportOutLineResult =orderDealReportService.getOrderDealListByLion(orderDealReportInDTO);
		} catch (Exception e) {
			LOGGER.error("查询运营状况折线图数据异常："+e.getMessage());
			
		}
		if(orderDealReportOutLineResult != null && orderDealReportOutLineResult.getRows() != null){
			List<OrderDealReportOut> orderDealReportOutLineList = orderDealReportOutLineResult.getRows();
			//将list转换为echat需要的格式
			List<String> payPriceTotalList = new ArrayList<String>(dayInterval);
			List<String> buyPersonNumList = new ArrayList<String>(dayInterval);
			List<String> payGoodsNumList = new ArrayList<String>(dayInterval);
			List<String> orderNumList = new ArrayList<String>(dayInterval);
			List<BigDecimal> payConversionList = new ArrayList<BigDecimal>(dayInterval);
			List<String> dealDateList = new ArrayList<String>(dayInterval);
			
			for(OrderDealReportOut orderDealReportOut : orderDealReportOutLineList){
				
				payPriceTotalList.add(orderDealReportOut.getPayPriceTotal()+"");
				buyPersonNumList.add(orderDealReportOut.getBuyPersonNumStr());
				payGoodsNumList.add(orderDealReportOut.getPayGoodsNumStr());
				orderNumList.add(orderDealReportOut.getOrderNumStr());
				payConversionList.add(orderDealReportOut.getPayConversion().multiply(new BigDecimal(100)).setScale(2));
				dealDateList.add("'"+orderDealReportOut.getDealDate()+"'");
				
			}
			model.addAttribute("payPriceTotalList", payPriceTotalList.toString());
			model.addAttribute("buyPersonNumList", buyPersonNumList.toString());
			model.addAttribute("payGoodsNumList", payGoodsNumList.toString());
			model.addAttribute("orderNumList", orderNumList.toString());
			model.addAttribute("payConversionList", payConversionList.toString());
			model.addAttribute("dealDateList", dealDateList.toString());
			
			model.addAttribute("orderDealReportOutLineList", orderDealReportOutLineList);
		}
		
		
		return "/analysis/businessAnalysis";
	}
	@RequestMapping(value = "/export")
	public String export(HttpServletRequest request,OrderDealReportIn orderDealReportInDTO,Pager<OrderDealReportOut> pager, Model model){
		Long uid = WebUtil.getInstance().getUserId(request);
		if(uid == null){
			return "redirect:/user/login";
		}
		//获取店铺ID
		Long shopId = WebUtil.getInstance().getShopId(request);
		if(shopId == null){
			return "redirect:/user/login";
		}
		orderDealReportInDTO.setShopId(shopId);
		orderDealReportInDTO.setDateFormat("yyyy-MM-dd");
		DataGrid<OrderDealReportOut> orderDealReportOutResult = orderDealReportService.getOrderDealListByPager(orderDealReportInDTO, null);
		
		List<Map<String,Object>> excelList = new ArrayList<Map<String,Object>>();
		
		if(orderDealReportOutResult != null&&orderDealReportOutResult.getRows() != null){
			
			List<OrderDealReportOut> orderDealReportOutList =orderDealReportOutResult.getRows();
			int i = 0 ;
			
			for(OrderDealReportOut orderDealReportOut : orderDealReportOutList){
				Map<String,Object> map = new HashMap<String,Object>();
				i++;
				map.put("no", i+"");
				String dealDate = orderDealReportOut.getDealDate();
				map.put("dealDate", dealDate);
				String payPriceTotal = orderDealReportOut.getPayPriceTotalStr();
				map.put("payPriceTotal", payPriceTotal);
				String buyPersonNum = orderDealReportOut.getBuyPersonNumStr();
				map.put("buyPersonNum", buyPersonNum);
				String payGoodsNum = orderDealReportOut.getPayGoodsNumStr();
				map.put("payGoodsNum", payGoodsNum);
				String orderNum = orderDealReportOut.getOrderNumStr();
				map.put("orderNum", orderNum);
				String payConversion = orderDealReportOut.getPayConversionStr();
				map.put("payConversion", payConversion);
				excelList.add(map);
			}
		}
		
		JRDataSource jrDataSource = new JRBeanCollectionDataSource(excelList);

		// 动态指定报表模板url
		model.addAttribute("url", "/WEB-INF/jasper/analysis_excel.jasper");
		model.addAttribute("format", "xls"); // 报表格式
		model.addAttribute("jrMainDataSource", jrDataSource);

		return "reportView"; // 对应jasper-views.xml中的bean id
	}
	public static void main(String[] args){
		List<String> list = new ArrayList<String>();
		list.add("aa");
		list.add("bb");
		list.add("bb");
		list.add("bb");
		list.add("bb");
		System.out.println(list.toString());
	}

}
