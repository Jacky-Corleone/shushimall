package com.camelot.mall.bank;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.util.Signature;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CiticTradeInDTO;
import com.camelot.payment.dto.CiticTradeInfoDTO;
import com.camelot.payment.dto.CiticTradeOutDTO;
import com.camelot.payment.dto.CompanyPayJobDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedBalance;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.MD5;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月17日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/citic")
public class CiticController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private CiticExportService citicService;
	@Resource
	private PaymentExportService paymentService;
	@Resource
	private UserExportService userService;
	@Resource
	private UserExtendsService userExtendsService;



	@RequestMapping("/payHome")
	public String payHome(HttpServletRequest request,BankOrder bo, Model model) {
	    String PRIKEY = SysProperties.getProperty("transfer.prikey");
	    String MALL= SysProperties.getProperty("transfer.system");
	    BigDecimal balance = BigDecimal.ZERO;
	    Long uid = WebUtil.getInstance().getUserId(request);
		ExecuteResult<UserInfoDTO> er = this.userExtendsService.findUserInfo(uid);
		String errorUrl = "/bank/citic_pay_error";
		AffiliatedBalance affiliatedBalance = null;
		if (er.isSuccess() && er.getResult() != null) {
			UserInfoDTO userInfoDTO = er.getResult();
			UserCiticDTO userCiticDTO = userInfoDTO.getUserCiticDTO();
			//修改买家购买时由原来判断卖家收款账号的逻辑改完买家的支付账号不为空20150612
			if (userCiticDTO != null
					&& userCiticDTO.getBuyerPaysAccount() != null
					&& !"".equals(userCiticDTO.getBuyerPaysAccount())) {

				Map<String,String> withmap=new HashMap<String, String>();
                withmap.put("system",MALL);
                withmap.put("subAccNo",userCiticDTO.getBuyerPaysAccount());
                withmap.put("sign", Signature.createSign(withmap,PRIKEY));
                ExecuteResult<AffiliatedBalanceDto> affx= this.citicService.querySubBalance(withmap);
                
				if (affx.isSuccess() && affx.getResult() != null) {
					AffiliatedBalanceDto affiliatedBalanceDto = affx.getResult();
					List<AffiliatedBalance> listaff = affiliatedBalanceDto.getList();
					if (listaff != null && listaff.size() > 0) {
						affiliatedBalance = listaff.get(0);

						String balanceStr = affiliatedBalance.getKYAMT();
						if( balanceStr != null && !"".equals(balanceStr) ){
							balance = new BigDecimal(balanceStr);
						}else{
							LOG.error("余额查询结果:"+affx.getResult().getStatusText());
							return errorUrl;
						}
					}else{
						LOG.error("余额查询结果:"+affx.getResult().getStatusText());
						return errorUrl;
					}
				}else{
					//2015-06-25王东晓添加start
					//如果查询余额失败，直接告知页面不跳转，并给出提示信息
					LOG.error("余额查询结果:"+affx.getErrorMessages());
					return errorUrl;
					//王东晓添加end
				}

			}
		}
		bo.setBalance(balance);
		model.addAttribute("bankOrder", bo);
		//王东晓添加
		//获取用户的基本信息，主要目的是获取用户是否有支付密码
		UserDTO userInfo = userService.queryUserById(Long.valueOf(uid));
		model.addAttribute("userInfo",userInfo);
		//王东晓添加end
		model.addAttribute("affiliatedBalance", affiliatedBalance);

		//王东晓添加：
		//如果当前用户为印刷家买家，则需要校验账户余额是否充足，如果账户余额不足，则进入充值页面，否则进入金融通支付页面，
		//如果当前用户为买家（非印刷家买家），则不需要校验账户余额，直接进入金融通支付页面
		String url = "/bank/citic_pay";//金融通支付页面
		//如果当前用户为印刷家用户,判断中信账户名是否为印刷家的规则是否已"印刷家_"开头
		//吕勇修改：
		//不再判断是否有“印刷家_”开头，余额不足跳到充值页面，余额够用就跳到输入密码支付页面。
		//if(affiliatedBalance!=null&&(affiliatedBalance.getSUBACCNM().startsWith(Constants.YSJ_YES) || affiliatedBalance.getSUBACCNM().startsWith(Constants.LY_YES))){
			int compareResult =  balance.compareTo(bo.getOrderAmount()==null?new BigDecimal(0):bo.getOrderAmount());
			//如果compareResult == 1 说明订单金额小于余额，0：两者相等，-1：订单金额大于余额
			LOG.error("判断余额："+compareResult+"..1 说明订单金额小于余额，0：两者相等，-1：订单金额大于余额");
			if(compareResult==-1){
				
				//余额不够用,进入充值页面
				//往company_pay_job表中插入数据，充值完成后，等待定时任务自动付款
				CompanyPayJobDTO comPayJob = new CompanyPayJobDTO();
				comPayJob.setOrderNo(bo.getOrderNo());
				comPayJob.setOutTradeNo(bo.getOutTradeNo());
				comPayJob.setUserId(Long.valueOf(uid));
				comPayJob.setAccType(AccountType.AccBuyPay);
				LOG.info("余额不足，不进行支付，插入数据库，等待自动支付");
				ExecuteResult<String> result = null;
				try {
					result = this.citicService.saveComPayJob(comPayJob);
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error("数据库插入数据失败：进入异常页面："+e);
					//如果发生异常，则进入异常页面
					return errorUrl;
				}
				//如果数据库插入失败,进入异常页面，如果成功，则进入充值页面充值
				if(!result.isSuccess()){
					return errorUrl;
				}
				LOG.info("数据库插入数据成功");
				url = "/bank/citic_recharge";
			}
		//}
		//王东晓添加end
		return url;
	}

	@RequestMapping("/payOrder")
	@ResponseBody
	public ExecuteResult<String> payOrder(HttpServletRequest request,BankOrder order){
		Long uid = WebUtil.getInstance().getUserId(request);
		LOG.debug(JSON.toJSON(order));
		ExecuteResult<String> erStr = this.userService.validatePayPassword( uid, MD5.encipher(order.getPayPassword()));
		LOG.debug("支付密码校验："+JSON.toJSONString(erStr));
		order.setBuyer( uid );

		if( erStr.isSuccess() && "1".equals( erStr.getResult() ) ){
			ExecuteResult<OrderInfoPay> er = new ExecuteResult<OrderInfoPay>();
			try {
				// 必填信息设置
				Map<String, String> parameterMap =new HashMap<String, String>();



				parameterMap=new HashMap<String,String>();
				parameterMap.put("system", SysProperties.getProperty("transfer.system"));
				parameterMap.put("outTradeNo", order.getOutTradeNo());
				parameterMap.put("accType", AccountType.AccBuyPay.getCode()+"");
				parameterMap.put("uid", order.getBuyer().toString());
				parameterMap.put("sign", Signature.createSign(parameterMap, SysProperties.getProperty("transfer.prikey")));

				Map<String, String> payResult = this.citicService.payCitic(parameterMap);

				LOG.debug("CITIC PAY PARAMS：" + JSON.toJSONString(parameterMap));
				er = this.paymentService.payResult(payResult, PayBankEnum.CITIC.name());
				LOG.debug("CITIC PAY RESULT：" + JSON.toJSONString(er));

				if( !er.isSuccess() ){
					erStr.addErrorMessage("订单支付失败!");
					erStr.setErrorMessages(er.getErrorMessages());
				}
			} catch (Exception e) {
				LOG.error("中信支付失败！",e);
				erStr.addErrorMessage("订单支付失败!");
			}
		}else{
			erStr.addErrorMessage("支付密码错误！");
		}

		return erStr;
	}

	@RequestMapping("/modifyOrderJob")
	@ResponseBody
	public String modifyOrderJob(String outTradeNo){
		this.citicService.modifyComPayJobByOrder(outTradeNo);
		return "";
	}
	@ResponseBody
	@RequestMapping("/queryCiticTradeList")
	public ExecuteResult<CiticTradeOutDTO> queryCiticTradeList(HttpServletRequest request,Integer startRecord,String orderDate,String userType){
		ExecuteResult<CiticTradeOutDTO> result = new ExecuteResult<CiticTradeOutDTO>();
		CiticTradeInDTO citicTradeInDTO = new CiticTradeInDTO();
		//根据当前用户获取用户的支付账户/冻结账户
		Long userId = WebUtil.getInstance().getUserId(request);
		Integer accountType =null;
		if("buyer".equals(userType)){
			//获取支付帐号
			accountType = 21;
		}else if("seller".equals(userType)){
			//获取用户的冻结账户
			accountType = 32;
		}else if("settlement".equals(userType)){
			//获取卖家的收款账户（结算账户） 
			accountType = 31;
		}else{
			result.addErrorMessage("无法获取账户信息");
			return result;
		}
		ExecuteResult<AccountInfoDto> accountInfoDTO = citicService.getAccountInfoByUserIdAndAccountType(userId, accountType);
		if(!accountInfoDTO.isSuccess() || accountInfoDTO.getResult() == null){
			result.setErrorMessages(accountInfoDTO.getErrorMessages());
			return result;
		}
		String subAccNo = accountInfoDTO.getResult().getSubAccNo();
		citicTradeInDTO.setSubAccNo(subAccNo);
		citicTradeInDTO.setStartDate(orderDate);
		citicTradeInDTO.setEndDate(orderDate);
		citicTradeInDTO.setStartRecord(startRecord);
		citicTradeInDTO.setPageNumber(10);
		result = citicService.queryCiticTradeList(citicTradeInDTO);
		return result;
	}
	
	@RequestMapping("/getCiticTradeInto")
	public String getCiticTradeInto(HttpServletRequest request, Model model,String subAccNo,String printVerifyCode){
		model.addAttribute("subAccNo", subAccNo);
		model.addAttribute("printVerifyCode", printVerifyCode);
		return "/bank/citic_trade_info";
	}
	
	public static void main(String[] args){
		BigDecimal a = new BigDecimal(4.0);
		BigDecimal b = new BigDecimal(3.0);
		System.out.println(a.compareTo(b));

	}

}
