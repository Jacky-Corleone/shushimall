package com.camelot.mall.sellcenter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.payment.PaymentExportService;
import com.camelot.settlecenter.dto.SettlementDTO;
import com.camelot.settlecenter.dto.SettlementDetailDTO;
import com.camelot.settlecenter.dto.combin.SettlementCombinDTO;
import com.camelot.settlecenter.dto.indto.SettlementInDTO;
import com.camelot.settlecenter.service.StatementExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;
/** 
 * <p>Description: [卖家中心-结算中心]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("/sellcenter/settlement")
public class SettlementController {

	@Resource
	private StatementExportService statementExportService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private PaymentExportService paymentExportService;
	/**
	 * 结算单
	 * */
	@RequestMapping("initBills")
	public String initBills(HttpServletRequest request,SettlementInDTO settlementInDTO,Integer page,Model model){
		Long userId = WebUtil.getInstance().getUserId(request);
		if(userId == null){
			return "redirect:/user/login";
		}
		Pager<SettlementCombinDTO> pager = new Pager<SettlementCombinDTO>();
		if(page == null){
			page = 1;
		}
		pager.setPage(page);
//		Long[] shopIds=new Long[1];
//		for (int i = 0; i < shopIds.length; i++) {
//			shopIds[i]=1L;
//		}
		//获取订单号
		String orderId = request.getParameter("orderId");
		if(!StringUtils.isEmpty(orderId)){
			//根据订单号查询结算单编号
			SettlementDetailDTO settleDetailCondition = new SettlementDetailDTO();
			settleDetailCondition.setOrderId(orderId);
			List<SettlementDetailDTO> settlementDetatilDTOList = statementExportService.findSettlementDetail(settleDetailCondition);
			//订单和结算明细是一一对应的
			if(settlementDetatilDTOList != null && settlementDetatilDTOList.size() != 0){
				SettlementDetailDTO settlementDetailDTO = settlementDetatilDTOList.get(0);
				Long settlementId = settlementDetailDTO.getSettlementId();
				settlementInDTO.setSettlement_id(settlementId);
			}else{
				settlementInDTO.setSettlement_id(-2l);
			}
			model.addAttribute("orderId", orderId);
		}
		//获取店铺ID
		Long shopId = WebUtil.getInstance().getShopId(request);
		settlementInDTO.setShopId(shopId);
		ExecuteResult<DataGrid<SettlementCombinDTO>> settlementCombindDtoes= statementExportService.querySettlementList(settlementInDTO, pager);
		DataGrid<SettlementCombinDTO> dateGrid = settlementCombindDtoes.getResult();
		// 加密orderId
		if (dateGrid != null) {
			for (SettlementCombinDTO settlementCombinDTO : dateGrid.getRows()) {
				if (settlementCombinDTO.getSettlementDetailList() != null) {
					for (SettlementDetailDTO settlementDetailDTO : settlementCombinDTO.getSettlementDetailList()) {
						// 订单号加密
						ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", settlementDetailDTO.getOrderId());
						if (passKeyEr.isSuccess()) {
							settlementDetailDTO.setPassKey(passKeyEr.getResult());
						}
					}
				}
			}
		}
		pager.setRecords(dateGrid.getRows());
		pager.setTotalCount(dateGrid.getTotal().intValue());

		//查询店铺信息
		ExecuteResult<ShopDTO> myShop = shopExportService.findShopInfoById(shopId);
		model.addAttribute("shopDTO", myShop.getResult());
		model.addAttribute("settlementInDTO", settlementInDTO);
		model.addAttribute("pager", pager);
			
		return "sellcenter/settlement/settlementBills";
	}

	/**
	 * 财务核定
	 * @throws ParseException 
	 * */
	@RequestMapping("financialCheck")
	public String financialCheck(HttpServletRequest request,SettlementInDTO settlementInDTO,Integer page,Model model) throws ParseException{
		Long userId = WebUtil.getInstance().getUserId(request);
		if(userId == null){
			return "redirect:/user/login";
		}
		Pager<SettlementCombinDTO> pager = new Pager<SettlementCombinDTO>();
		SettlementDTO settlementDTO = new SettlementDTO();
		
		//获取当前用户/店铺id
//		settlementInDTO.setShopId(this.getShopId(request));
		settlementInDTO.setShopId(WebUtil.getInstance().getShopId(request));

		BigDecimal totalIncome = new BigDecimal(0);		//总收入
		BigDecimal totalOutcome = new BigDecimal(0);	//总支出
		BigDecimal refundTotalMoney = new BigDecimal(0);//  退款总金额
		BigDecimal commissionTotalMoney = new BigDecimal(0);//  佣金总金额（扣点总金额）
		BigDecimal wangyinTotal = new BigDecimal(0);//网银在线支付总金额
		BigDecimal zhongxinTotal = new BigDecimal(0);//中信账户结算总额
		if(page == null){
			page = 1;
		}
		//初始查询设置初始时间段:当前月第一天---当前日期
		if(settlementInDTO.getBillDatestr()==null && settlementInDTO.getBillDateend()==null){
			System.out.println("这是初始化查询.......");
			Calendar c = Calendar.getInstance();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");   
			c.add(Calendar.MONTH, 0);
	        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
			Date start = format.parse(format.format(c.getTime()));
			Date end = new Date();
			//默认出账起始日期
			settlementInDTO.setBillDatestr(start);
			settlementInDTO.setBillDateend(end);
			
		}
//		System.out.println(settlementInDTO.getBillDatestr()+"======="+settlementInDTO.getBillDateend());
		ExecuteResult<DataGrid<SettlementCombinDTO>> settlementCombindDtoes= statementExportService.querySettlementList(settlementInDTO, pager);
		//查询店铺信息
		ExecuteResult<ShopDTO> myShop = shopExportService.findShopInfoById(WebUtil.getInstance().getShopId(request));
		DataGrid<SettlementCombinDTO> dateGrid = settlementCombindDtoes.getResult();
		pager.setRecords(dateGrid.getRows());
		pager.setTotalCount(dateGrid.getTotal().intValue());
		//网银在线支付：1/中信账户结算:2
		
		for(int i = 0;i < dateGrid.getRows().size();i++){
			
			totalIncome = totalIncome.add(dateGrid.getRows().get(i).getSettlement().getSellerIncome());
			totalOutcome = totalOutcome.add(dateGrid.getRows().get(i).getSettlement().getSellerExpenditure());
			refundTotalMoney = refundTotalMoney.add(dateGrid.getRows().get(i).getSettlement().getRefundTotalMoney());
			commissionTotalMoney = commissionTotalMoney.add(dateGrid.getRows().get(i).getSettlement().getCommissionTotalMoney());
			for(int j = 0;j < dateGrid.getRows().get(i).getSettlementDetailList().size();j++){
				SettlementDetailDTO settlementDetailDTO = dateGrid.getRows().get(i).getSettlementDetailList().get(j);
				//网银支付
				if(settlementDetailDTO.getPaymentMethod() == 1){
					wangyinTotal = wangyinTotal.add(settlementDetailDTO.getSellerIncome());
				//中信支付
				}else if(settlementDetailDTO.getPaymentMethod() == 2){
					zhongxinTotal = zhongxinTotal.add(settlementDetailDTO.getSellerIncome());
				}
				// 加密orderId
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"",settlementDetailDTO.getOrderId());
				if (passKeyEr.isSuccess()) {
					settlementDetailDTO.setPassKey(passKeyEr.getResult());
				}
			}
		}
		settlementDTO.setSellerIncome(totalIncome);
		settlementDTO.setSellerExpenditure(totalOutcome);
		settlementDTO.setRefundTotalMoney(refundTotalMoney);
		settlementDTO.setCommissionTotalMoney(commissionTotalMoney);
		
		model.addAttribute("wangyinTotal",wangyinTotal );
		model.addAttribute("zhongxinTotal",zhongxinTotal );
		model.addAttribute("shopDTO", myShop.getResult());
		model.addAttribute("settlementInDTO", settlementInDTO);
		model.addAttribute("settlementDTO", settlementDTO);
		model.addAttribute("pager", pager);
		return "sellcenter/settlement/financialCheck";
	}
	
	/**
	 * @throws Exception 
	 * 
	 * 结算单确认
	 * */
	@RequestMapping("proceedSettlement")
	@ResponseBody
	public Map<String,Object> proceedSettlement(Long settlementId) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		
		ExecuteResult<String> eRequest = statementExportService.proceedSettle(settlementId);
//		System.out.println(settlementId+"===="+eRequest.isSuccess());
		//确认结算单成功？
		if(eRequest.isSuccess()){
			result.put("flag", "success");
			result.put("messager", eRequest.getResultMessage());
		}else{
			result.put("flag", "failure");
			result.put("messager", eRequest.getErrorMessages());
		}
		return result;
	}
	/*
	 * (key : value)
	 * */
	@SuppressWarnings("unchecked")
	@RequestMapping("forwardPaymentTask")
	@ResponseBody
	public void paymentTaskUrl(HttpServletRequest request){
		Map<String,String[]> result = request.getParameterMap();
		Map<String,String> resultMap = new HashMap<String,String>();
		if(result != null){
			for(String key : result.keySet()){
				String[] values = result.get(key);
				resultMap.put(key , values.length == 1 ? values[0].trim() : "");
			}
		}
		//
		paymentExportService.paySearchResult(resultMap);
	}
}
