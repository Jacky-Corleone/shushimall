package com.camelot.payment.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.enums.CiticEnums.AccStatus;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.SystemKey;
import com.camelot.common.util.DateUtils;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.PaymentExportService;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CiticPayJournalDto;
import com.camelot.payment.dto.CompanyPayJobDTO;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.payment.dto.citic.auxiliary.QueryTransfer;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.enums.UserEnums.UserExtendsType;
import com.camelot.usercenter.enums.UserEnums.UserType;
import com.camelot.usercenter.service.UserExtendsService;

public class ConfirmCiticJob{

	private static final Logger logger = LoggerFactory.getLogger(ConfirmCiticJob.class);

	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private PaymentExportService paymentExportService;
	@Resource
	private CiticExportService citicExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	/**
	 * 针对账号申请添加的定时调用中信接口开通账号
	 * 
	 * @Title: createAccount
	 */
	public void createAccount() {
		logger.info("\n 定时调用账号开通，进行处理：启动时间：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
		int successCount=0;
		
		List<AccountInfoDto> listAcc = citicExportService.findAccByStatus(AccStatus.UnCreate);
		for (AccountInfoDto accountInfoDto:listAcc) {
			boolean flag= citicExportService.saveAffiliated(accountInfoDto);
			if(flag){
				successCount++;
			}
		}
		logger.info("\n 定时调用账号开通，进行处理：结束时间：{}，处理数量：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS),successCount);
	}
	
	/**
	 * 定时调用账号签约状态查询，进行处理
	 * 
	 * @Title: confirmAccStatus
	 */
	public void confirmAccStatus() {
		logger.info("\n 定时调用账号签约状态查询，进行处理：启动时间：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
		int successCount=0;
		boolean isModify=false;
		
		List<AccountInfoDto> listAccWaiting = citicExportService.findAccByStatus(AccStatus.UnAudit);
		for (AccountInfoDto accountInfoDto:listAccWaiting) {
			boolean flag=citicExportService.modifyAffiliatedStatus(accountInfoDto);
			if(flag){
				ExecuteResult<UserInfoDTO> executeResult=userExtendsService.findUserInfo(accountInfoDto.getUserId());
				if(executeResult!=null&&executeResult.getResult()!=null){
					UserInfoDTO userInfoDTO= executeResult.getResult();
					UserType userType=userInfoDTO.getUserType();
					List<Long> listUId=new ArrayList<Long>();
					listUId.add(userInfoDTO.getUserId());
					if(UserType.SELLER==userType){ // 卖家四个账户都通过则修改用户中心为审核通过
						Integer[] accountTypes={AccountType.AccBuyFinancing.getCode(),AccountType.AccBuyPay.getCode(),AccountType.AccSellFreeze.getCode(),
								AccountType.AccSellReceipt.getCode()};
						List<AccountInfoDto> listAccQuery=citicExportService.findByUIds(listUId, accountTypes);
						if(listAccQuery.size()==4){
							isModify=true;
						}
					}else{ // 买家两个账户都通过则修改用户中心为审核通过
						Integer[] accountTypes={AccountType.AccBuyFinancing.getCode(),AccountType.AccBuyPay.getCode()};
						List<AccountInfoDto> listAccQuery=citicExportService.findByUIds(listUId, accountTypes);
						if(listAccQuery.size()==2){
							isModify=true;
						}
					}
					
					if(isModify){
						userExtendsService.modifyStatusByType(userInfoDTO.getExtendId(), ComStatus.PASS, UserExtendsType.CTIBANK);
						successCount++;
					}
				}
			}
		}
		logger.info("\n 定时调用账号签约状态查询，进行处理：结束时间：{}，处理数量：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS),successCount);
	}
	
	/**
	 * 定时调用交易结果查询
	 * 
	 * @Title: confirmTranResult
	 */
	public void confirmTranResult() {
		logger.info("\n 定时调用未处理的交易结果查询，进行处理：启动时间：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
		int successCount=0;
		
		CiticPayJournalDto citicPayCondition = new CiticPayJournalDto();
		citicPayCondition.setDealFlag("1");
		List<CiticPayJournalDto> listJournalDto = citicExportService.findCiticPayJournalList(citicPayCondition);
		for (CiticPayJournalDto journalDto:listJournalDto) {
			try {
				QueryTransferDto queryTransferDto = citicExportService.queryTransfer(journalDto.getOutTradeNo());
				QueryTransfer queryTransfer=queryTransferDto.getList().get(0);
				successCount++;
				journalDto.setDealFlag("");
				journalDto.setDealText("状态标记："+queryTransfer.getStt());
				citicExportService.modifyCiticPayJournalDeal(journalDto);
			} catch (Exception e) {
				logger.info("\n 定时调用未处理的交易结果查询，进行处理：异常：{}",e.getMessage());
			}
		}
		logger.info("\n 定时调用未处理的交易结果查询，进行处理：结束时间：{}，处理数量：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS),successCount);
	}
	
	/**
	 * TODO 定时调用未处理的充值并支付的定时job记录处理支付。
	 * 
	 * @Title: confirmCompanyPayJob
	 */
	public void confirmCompanyPayJob() {
		logger.info("\n 定时调用未处理的充值并支付的定时，进行处理：启动时间：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS));
		int successCount=0;
		
		List<CompanyPayJobDTO> listComPayJobDto = citicExportService.findUnDealComPayJob();
		for (CompanyPayJobDTO companyPayJobDTO:listComPayJobDto) {
			List<OrderInfoPay> listChildTrans= paymentExportService.findChildTransByOutTrades(companyPayJobDTO.getOutTradeNo());
			for (OrderInfoPay orderInfoPay:listChildTrans) {
				ExecuteResult<TradeOrdersDTO> tradeOrderResult = tradeOrderExportService.getOrderById(orderInfoPay.getOrderNo());
				if(tradeOrderResult.isSuccess()&&tradeOrderResult.getResult().getState()==6){ 
					// 取消订单标记为已处理
					citicExportService.dealComPayJobById(companyPayJobDTO.getId());
				}else{
					try {
						Map<String, String> parameterMap=new HashMap<String,String>();
						parameterMap.put("system", SystemKey.CompanyJobPay.name());
						parameterMap.put("outTradeNo", companyPayJobDTO.getOutTradeNo());
						parameterMap.put("uid", companyPayJobDTO.getUserId()+"");
						parameterMap.put("accType", companyPayJobDTO.getAccType().getCode()+"");
						parameterMap.put("sign",Signature.createSign(parameterMap, SysProperties.getProperty(SystemKey.CompanyJobPay.getKeyPri())) );
						Map<String, String> payResult=citicExportService.payCitic(parameterMap);
						ExecuteResult<OrderInfoPay> executeResult = paymentExportService.payResult(payResult,PayBankEnum.CITIC.name());
						if(executeResult.isSuccess()){
							citicExportService.dealComPayJobById(companyPayJobDTO.getId());
							successCount++;
						}
					} catch (Exception e) {
						logger.info("\n 定时调用未处理的充值并支付的定时，进行处理：异常：{}",e.getMessage());
					}
					CompanyPayJobDTO comPayJob = new CompanyPayJobDTO();
					comPayJob.setId(companyPayJobDTO.getId());
					comPayJob.setBatch(companyPayJobDTO.getBatch()+1);
					citicExportService.modifyComPayJob(comPayJob);
				}
			}
		}
	    logger.info("\n 定时调用未处理的充值并支付的定时，进行处理：结束时间：{}，处理数量：{}",DateUtils.format(new Date(), DateUtils.YYDDMMHHMMSS),successCount);
	}
	
}
