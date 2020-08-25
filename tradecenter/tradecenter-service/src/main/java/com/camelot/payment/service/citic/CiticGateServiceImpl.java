package com.camelot.payment.service.citic;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.accountinfo.dao.FinanceAccountDAO;
import com.camelot.common.constants.SysConstants;
import com.camelot.common.enums.CiticEnums.AccStatus;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.CiticEnums.CiticPayStatus;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayOrderTypeEnum;
import com.camelot.common.enums.SystemKey;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.common.enums.WithdrawEnums;
import com.camelot.common.util.DateUtils;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.dao.LockTableDAO;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.dao.AccountInfoDAO;
import com.camelot.payment.dao.CiticPayJournalDAO;
import com.camelot.payment.dao.CompanyPayJobDAO;
import com.camelot.payment.dao.FinanceWithdrawApplyDAO;
import com.camelot.payment.dao.FinanceWithdrawRecordDAO;
import com.camelot.payment.dao.TransationsDAO;
import com.camelot.payment.domain.AccountInfo;
import com.camelot.payment.domain.CiticPayJournal;
import com.camelot.payment.domain.CompanyPayJob;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.FinanceWithdrawApplyDTO;
import com.camelot.payment.dto.FinanceWithdrawRecordDTO;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.RefundReqParam;
import com.camelot.payment.dto.citic.auxiliary.AffiliatedQuery;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.dto.citic.req.AffiliatedQueryDto;
import com.camelot.payment.dto.citic.req.MainBalanceDto;
import com.camelot.payment.dto.citic.req.OutTransferDto;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.camelot.payment.dto.citic.res.OutTransfer;
import com.camelot.payment.dto.citic.res.Transfer;
import com.camelot.payment.service.CiticService;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.service.UserExtendsService;

@Service("citicGateService")
public class CiticGateServiceImpl implements CiticService {
	
	private final static Logger logger = LoggerFactory.getLogger(CiticGateServiceImpl.class);
	@Resource
	private AccountInfoDAO accountInfoDAO;
	@Resource
	private TransationsDAO transationsDAO;
	@Resource
	private CiticPayJournalDAO citicPayJournalDAO;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private LockTableDAO lockTableDAO;
	@Resource
	private FinanceWithdrawApplyDAO financeWithdrawApplyDAO;
	@Resource
	private FinanceWithdrawRecordDAO financeWithdrawRecordDAO;
	@Resource
	private CompanyPayJobDAO companyPayJobDAO;
	@Resource
	private FinanceAccountDAO financeAccountDAO;
	
	public ExecuteResult<Integer> buildPayForm(PayReqParam payReqParam) {
		
		ExecuteResult<Integer> result = new ExecuteResult<Integer>();
		SortedMap<String, String> signParams = new TreeMap<String, String>();
		signParams.put("outTradeNo",payReqParam.getOutTradeNo());// 对外订单号
		signParams.put("orderNo",payReqParam.getOrderNo());// 订单号
		signParams.put("subject",payReqParam.getSubject());// 订单名称
		signParams.put("orderAmount",payReqParam.getTotalFee().toString());// 订单金额
		signParams.put("curType","CNY");// 币种
		signParams.put("toAccount",payReqParam.getToAccount());// 卖家账户
		signParams.put("seller",payReqParam.getSeller());// 卖家名称
		if(payReqParam.getPlatformId() == null){
			result.setResultMessage(CiticUtil.buildRequest(signParams,SysProperties.getProperty(SysConstants.CITIC_PAY_URL), "submit"));
		}else if(payReqParam.getPlatformId() == PlatformEnum.GREEN.getId()){
			result.setResultMessage(CiticUtil.buildRequest(signParams,SysProperties.getProperty(SysConstants.GREEN_CITIC_PAY_URL), "submit"));
		}
		return result;
	}

	@Override
	public ExecuteResult<Transations> buildTransatoins(Map<String, String> map, String type) {
		ExecuteResult<Transations> result =new ExecuteResult<Transations>();
		
		String system= map.get("system");
		String outTradeNo= map.get("v_oid");
		String buyAcc= map.get("v_buy_acc");
		String totelFee= map.get("v_totel_fee");
		String status= map.get("v_status");
		String markCode=map.get("v_mark_code");
		/*** 校验签名 ***/
		Map<String, String> mapSignature=new HashMap<String, String>();
		mapSignature.put("system", system);
		mapSignature.put("v_oid",outTradeNo);
		mapSignature.put("v_buy_acc",buyAcc);
		mapSignature.put("v_totel_fee",totelFee);
		mapSignature.put("v_status",status);
		mapSignature.put("v_mark_code",markCode); // 金额不足标记初始化
		if(Signature.verifySign(map.get("sign"),mapSignature,SysProperties.getProperty(SystemKey.CiticPay.getKeyPri()))){
			String statusText= map.get("v_status_text");
			if(TransationsStatusEnum.PAID_SUCCESS.getCode().equals(Integer.parseInt(status))){
				Transations transations= new Transations();
				transations.setOutTradeNo(outTradeNo);
				transations.setFromAccount(buyAcc);
				transations.setRealAmount(new BigDecimal(totelFee));
				transations.setCompletedTime(new Date());
				transations.setStatus(TransationsStatusEnum.PAID_SUCCESS.getCode());
				transations.setStatusText(statusText);
				result.setResult(transations);
				result.setResultMessage(statusText);
			}else{
				result.setResultMessage(markCode); // 金额不足标记 -1 代表不足
				result.addErrorMessage(statusText);
			}
		}else{
			result.addErrorMessage("支付结果校验失败");
		}
		return result;
	}

	@Override
	public boolean addAffiliated(AccountInfo accountInfo) {
		try {
			FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
			CiticUtil.addAffiliated(accountInfo,financeAccountInfoDto);
			accountInfo.setAccStatus(AccStatus.UnAudit.ordinal());
			accountInfo.setAccStatusText("账户生成成功");
			accountInfoDAO.update(accountInfo);
			return true;
		} catch (Exception e) {
			accountInfo.setAccStatus(AccStatus.AuditFail.ordinal());
			accountInfo.setAccStatusText("账户生成失败："+e.getMessage());
			accountInfoDAO.update(accountInfo);
			return true;
		}	
	}
	
	@Override
	public boolean updateAffiliatedStatus(AccountInfo accountInfo){
		logger.info("\n 方法[{}]，入参：[{}]","CiticServiceImpl-updateAffiliatedStatus[附属账户签约查询]",JSON.toJSONString(accountInfo));
		try{
			Date currentEnd=DateUtils.dayOffset(accountInfo.getCreated(), 1); // 查询结束时间=创建时间+1天
			// 查询当前附属账号状态
			FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
			AffiliatedQueryDto affiliatedQueryDto=CiticUtil.queryAffiliated(accountInfo.getSubAccNo(), null,
					DateUtils.format(accountInfo.getCreated(), DateUtils.YMD), DateUtils.format(currentEnd, DateUtils.YMD),financeAccountInfoDto); 
			AffiliatedQuery affiliatedQuery =affiliatedQueryDto.getList().get(0); // 不做空值校验，异常不做处理
			if(AccStatus.UnAudit.getCiticCode()!=affiliatedQuery.getStatus()){
				accountInfo.setAccStatus(AccStatus.getEnumByCiticCode(affiliatedQuery.getStatus()).ordinal());
				accountInfo.setAccStatusText(affiliatedQuery.getStatusText());
				accountInfoDAO.update(accountInfo);
				return true;
			}
		}catch (Exception e) {
			logger.info("\n 方法[{}]，异常：[{}]","CiticServiceImpl-updateAffiliatedStatus[附属账户签约查询]",e.getMessage());
		}
		return false;
	}

	@Override
	public synchronized ExecuteResult<String> transferAffiliated(PayReqParam payReqParam,String orderParentTradeNo,CiticPayTypeCode citicPayTypeCode){
//		this.lock("transfer_affiliated",10); // 锁定
		logger.info("\n 方法[{}]，入参：[{}][{}][{}]","CiticGateServiceImpl-transferAffiliated[附属账户之间的普通转账]",JSON.toJSONString(payReqParam)
				,orderParentTradeNo,citicPayTypeCode.getCode());
		ExecuteResult<String> result= new ExecuteResult<String>(); 
		
		if(payReqParam!=null&&payReqParam.getOutTradeNo()!=null){
			// 判断当前对外交易号是否交易成功过
			CiticPayJournal queryJournal = citicPayJournalDAO.selectByTradeNo(payReqParam.getOutTradeNo());
			if(queryJournal ==null){
				FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
				// 初始化支付信息记录.
				CiticPayJournal citicPayJournal= new CiticPayJournal();
				citicPayJournal.setOutTradeNo(payReqParam.getOutTradeNo());
				citicPayJournal.setOrderParentTradeNo(orderParentTradeNo);
				citicPayJournal.setPayAccNo(payReqParam.getFromAccount());
				citicPayJournal.setRecvAccNo(payReqParam.getToAccount());
				citicPayJournal.setRecvAccNm(payReqParam.getSeller());
				citicPayJournal.setTranAmt(payReqParam.getTotalFee());
				citicPayJournal.setPayType(citicPayTypeCode.getCode()); //交易类型
				citicPayJournal.setMemo(payReqParam.getExtraParam());// 备注
				try {
					int compare=payReqParam.getTotalFee().compareTo(new BigDecimal("0.00"));
					if(compare!=0){ // 0.00直接通过
						// 每个子单付款
						Transfer transfer = new Transfer();
						transfer.setClientID(citicPayJournal.getOutTradeNo());
						transfer.setPayAccNo(payReqParam.getFromAccount());
						transfer.setRecvAccNo(payReqParam.getToAccount());
						transfer.setRecvAccNm(payReqParam.getSeller());//  退款时代表买家名称
						transfer.setTranAmt(String.format("%.2f",payReqParam.getTotalFee()));
						transfer.setTranType("BF");
						// TODO 暂时屏蔽交易
						CiticUtil.transfer(transfer,financeAccountInfoDto);
					}
					citicPayJournal.setStatus(2);// 交易状态 1交易失败/2交易成功 
					citicPayJournal.setStatusText("success");
					result.setResultMessage("付款成功");
				} catch (Exception e) {
					citicPayJournal.setStatus(1);
					citicPayJournal.setStatusText(e.getMessage());
					result.addErrorMessage("付款失败："+e.getMessage());
					// 以前的交易订单置为无效
					CiticPayJournal dealCiticPayJournal =new CiticPayJournal();
					dealCiticPayJournal.setOutTradeNo(citicPayJournal.getOutTradeNo());
					dealCiticPayJournal.setEnableFlag(0);
					citicPayJournalDAO.updateByOutTradeNo(dealCiticPayJournal);
				}
				citicPayJournalDAO.add(citicPayJournal);
			}else{
				result.setResultMessage("支付完成，无需重复支付");
			}
		}else{
			result.addErrorMessage("无效支付");
		}
		
		logger.info("\n 方法[{}]，出参：[{}]","CiticGateServiceImpl-transferAffiliated[附属账户之间的普通转账]",JSON.toJSON(result));
		return result;
	}
	
	@Override
	public MainBalanceDto queryBalance() throws IOException, DocumentException{
		FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
		return CiticUtil.queryBalance(financeAccountInfoDto);
	}
	
	@Override
	public AffiliatedBalanceDto querySubBalance(String subAccNo) throws IOException, DocumentException{
		FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
		return CiticUtil.querySubBalance(subAccNo,financeAccountInfoDto);
	}
	
	@Override
	public  QueryTransferDto queryTransfer(String outTradeNo) throws IOException, DocumentException{
		FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
		return CiticUtil.queryTransfer(outTradeNo,financeAccountInfoDto);
	}
	
	@Override
	public synchronized ExecuteResult<String> outPlatformTransfer(AccountInfo accInfoBuild,BigDecimal withdrawPrice) throws IOException, DocumentException{

		return this.outPlatformTransfer(accInfoBuild, withdrawPrice, null);
	}
	@Override
	public synchronized ExecuteResult<String> outPlatformTransfer(AccountInfo accInfoBuild,BigDecimal withdrawPrice,Long uid) throws IOException, DocumentException{
//		this.lock("out_platform_transfer",10);// 加锁，保持数据一致性
		
		ExecuteResult<String> result =new ExecuteResult<String>();
		// 1.校验提现金额是否超额
		AffiliatedBalanceDto affiliatedBalanceDto=this.querySubBalance(accInfoBuild.getSubAccNo());
		try{
			String sjAmt=affiliatedBalanceDto.getList().get(0).getSJAMT();
			int compareResult = withdrawPrice.compareTo(new BigDecimal(sjAmt));
			if(compareResult>0){				
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"提现金额超限"));
				return result;
			}
		}catch (Exception e) {
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Citic_Service_Error,e.getMessage()));
			return result;
		}
				
		String tradeNo = financeWithdrawApplyDAO.getWithdrawNo();
		// 2.1提现申请
		FinanceWithdrawApplyDTO entityApply = new FinanceWithdrawApplyDTO();
		entityApply.setUserId(accInfoBuild.getUserId());
		entityApply.setAmount(withdrawPrice);
		entityApply.setTradeNo(tradeNo);
		if(AccountType.AccSellReceipt.getCode()==accInfoBuild.getAccType()||AccountType.AccBuyPay.getCode()==accInfoBuild.getAccType()){ 
			entityApply.setStatus(WithdrawEnums.WithdrawalApplicationSuccess.getCode());
		}else{// 其他账户申请提现,生成提现申请表
			entityApply.setStatus(WithdrawEnums.WithdrawDispose.getCode());
		}
		financeWithdrawApplyDAO.add(entityApply);
		
		// 2.2提现记录
		FinanceWithdrawRecordDTO entityRecord = new FinanceWithdrawRecordDTO();
		entityRecord.setTradeNo(entityApply.getTradeNo());
		entityRecord.setUserId(uid!=null?uid:accInfoBuild.getUserId());
		entityRecord.setAmount(withdrawPrice);
		entityRecord.setType(accInfoBuild.getAccType());
		entityRecord.setStatus(entityApply.getStatus());
		financeWithdrawRecordDAO.add(entityRecord);
				
		// 3.交易记录并执行提现， 卖家收款账户、买家支付账户直接提现
		if(AccountType.AccSellReceipt.getCode()==accInfoBuild.getAccType()||AccountType.AccBuyPay.getCode()==accInfoBuild.getAccType()){ 
			FinanceAccountInfoDto financeAccountInfoDto=this.findFinanceAcc();
			// 初始化支付信息记录.
			CiticPayJournal citicPayJournal= new CiticPayJournal();
			citicPayJournal.setOutTradeNo(transationsDAO.selectOutTranNo(PayBankEnum.CITIC.name()));
			citicPayJournal.setOrderParentTradeNo(entityApply.getTradeNo());
			citicPayJournal.setPayAccNo(accInfoBuild.getSubAccNo());
			citicPayJournal.setRecvAccNo(accInfoBuild.getBindingAccNo());
			citicPayJournal.setRecvAccNm(accInfoBuild.getBindingAccNm());
			citicPayJournal.setTranAmt(withdrawPrice);
			citicPayJournal.setPayType(CiticPayTypeCode.withdraw.getCode()); //交易类型
			try {
				// TODO 暂时屏蔽
				OutTransferDto outTransferDto = CiticUtil.outPlatformTransfer(this.buildOutTransfer(citicPayJournal.getOutTradeNo(),accInfoBuild,withdrawPrice),financeAccountInfoDto); // 提现操作
				result.setResultMessage("出金提交成功："+outTransferDto.getStatus()+"-"+outTransferDto.getStatusText());
				citicPayJournal.setStatus(CiticPayStatus.PaySuccess.getCode());// 交易成功
			} catch (Exception e) {
				entityApply.setContent("提现异常："+e.getMessage());
				entityApply.setStatus(WithdrawEnums.WithdrawalApplicationFail.getCode());
				financeWithdrawApplyDAO.update(entityApply);
				entityRecord.setStatus(WithdrawEnums.WithdrawalApplicationFail.getCode());
				entityRecord.setFailReason(e.getMessage());
				financeWithdrawRecordDAO.update(entityRecord);
				citicPayJournal.setStatus(CiticPayStatus.PayFail.getCode());// 交易失败 定时查询交易结果 原因在dealText中查询
				citicPayJournal.setDealFlag("");
				citicPayJournal.setDealText(e.getMessage());
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Citic_Service_Error,e.getMessage()));
			}
			citicPayJournalDAO.add(citicPayJournal);
		}
		
		return result;
	}
	
	@Override
	public Transations queryTradeInfo(Transations tran){
		// 定时调用，显示在dealText字段
		return tran;
	}
	
	
	/**
	 * 中信支付并做记录
	 * 
	 * @param tran
	 * @param fromSubAccNo
	 * @return
	 */
	public synchronized ExecuteResult<Transations> payCitic(String outTradeNo,Long buyId,AccountType accType){
//		this.lock("pay_citic",10); // 加锁，避免数据不一致
		ExecuteResult<Transations> result =new ExecuteResult<Transations>();
		Transations tranResult= new Transations();
		// 1.校验订单有效性
		Transations tranQuery=transationsDAO.selectTransByOutTrade(outTradeNo);
		if(tranQuery ==null||(tranQuery !=null&&TransationsStatusEnum.USER_LESS.getCode().equals(tranQuery.getStatus()))){
			result.addErrorMessage("该对外订单号不存在或无效，请尝试用子单支付");
			return result;
		}
		tranResult.setOrderAmount(tranQuery.getOrderAmount());// 加入支付金额
		
		// 2. 校验买家账号
		AccountInfo accBuy =new AccountInfo();
		ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(buyId);
		if(userInfo!=null&&userInfo.getResult()!=null&&userInfo.getResult().getUserCiticDTO()!=null){
			if(AccountType.AccBuyFinancing==accType){ // 融资账户
				accBuy.setSubAccNo(userInfo.getResult().getUserCiticDTO().getBuyerFinancingAccount());
			}else{ // 支付账户
				accBuy.setSubAccNo(userInfo.getResult().getUserCiticDTO().getBuyerPaysAccount());
			}
			
			accBuy.setAccStatus(userInfo.getResult().getUserCiticDTO().getAccountState());
			if(!(accBuy!=null&&StringUtils.isNotBlank(accBuy.getSubAccNo())&&
					(ComStatus.PASS.getCode()==accBuy.getAccStatus()||3==accBuy.getAccStatus()))){// 排除审核用过[2]/买家审核通过但卖家认证正在审核的[3],特殊标识，用户中心未加入枚举中
				result.addErrorMessage("买家账户尚未审核通过");
				return result;
			}		
		}else{
			result.addErrorMessage("买家(账户)不存在");
			return result;
		}
		tranResult.setFromAccount(accBuy.getSubAccNo());// 加入买家账号
		
		// 3.校验买家账户余额是否足以支付
		try {
			AffiliatedBalanceDto affiliatedBalanceDto = this.querySubBalance(accBuy.getSubAccNo());
			BigDecimal userKyAmt=new BigDecimal(affiliatedBalanceDto.getList().get(0).getKYAMT());// 买家可用余额
			int isPay=tranQuery.getOrderAmount().compareTo(userKyAmt);
			if(isPay>0){
				// 插入金额不足待充值支付的记录 
				CompanyPayJob companyPayJob = new CompanyPayJob();
				companyPayJob.setOutTradeNo(outTradeNo);
				companyPayJob.setOrderNo(tranQuery.getOrderNo());
				companyPayJob.setUserId(buyId);
				companyPayJob.setAccType(accType.getCode());
				companyPayJobDAO.add(companyPayJob);
				
				result.setResultMessage("-1");// 买家金额不足标记
				result.addErrorMessage("买家账户余额不足");
				return result;
			}
		} catch (Exception e) {
			result.addErrorMessage("买家账户异常");
			return result;
		}
		
		/** 4.支付订单顺序支付 **/
		StringBuffer payResultMsg =new StringBuffer(); // 执行支付时的记录
		int paySuccessCount=0; // 执行成功的条数
		
		if(PayOrderTypeEnum.Parent.ordinal()==tranQuery.getPayOrderType()){ // 父单
			// 2.查询所有未付款的子订单 买家向卖家冻结账户打钱
			Transations tranCondition =new Transations();
			tranCondition.setOrderParentNo(tranQuery.getOrderNo());
			tranCondition.setStatus(TransationsStatusEnum.PAYING.getCode());
			List<Transations> listItem=transationsDAO.queryList(tranCondition,null);
			payResultMsg.append("父单支付：支付记录【");
			for (Transations tranItem:listItem) {
				tranItem.setOutTradeNo(outTradeNo+""+paySuccessCount);
				ExecuteResult<String> resultPay = this.transferAffiliated(this.bulidPayReqParam(tranItem,accBuy.getSubAccNo()),tranQuery.getOutTradeNo(),CiticPayTypeCode.pay);
				if(resultPay.isSuccess()){
					paySuccessCount+=1;
					payResultMsg.append("子订单号：").append(tranItem.getOrderNo()).append(resultPay.getResultMessage()+";");
				}
			}
			payResultMsg.append("】---支付成功率：").append(paySuccessCount).append("/").append(listItem.size());
			if(paySuccessCount==0)result.addErrorMessage("总单支付失败");
		}else{ // 子单
			ExecuteResult<String> resultPay = this.transferAffiliated(this.bulidPayReqParam(tranQuery,accBuy.getSubAccNo()),"",CiticPayTypeCode.pay);
			payResultMsg.append("子单支付：支付记录【子订单号：").append(tranQuery.getOrderNo()); 
			if(resultPay.isSuccess()){
				paySuccessCount+=1;
				payResultMsg.append("支付成功】");
			}else{
				payResultMsg.append("支付失败,").append(resultPay.getErrorMessages().get(0)).append("】");
			}
		}
		
		result.setResult(tranResult);
		// 判断是否有支付成功的子订单
		if(paySuccessCount>0){
			result.setResultMessage(payResultMsg.toString());
		}else{
			result.addErrorMessage(payResultMsg.toString());
		}
		
		return result;
	}
	
	
	/**
	 * 方法【payCitic】构建参数
	 * 
	 * @param tran - 支付订单信息
	 * @param fromAcc - 付款方账号
	 * @return
	 */
	private PayReqParam bulidPayReqParam(Transations tran,String fromAcc){
		PayReqParam payReqParam =new PayReqParam();
		payReqParam.setOutTradeNo(tran.getOutTradeNo());
		payReqParam.setFromAccount(fromAcc);
		payReqParam.setToAccount(tran.getToAccount());
		payReqParam.setSeller(tran.getSeller());
		payReqParam.setTotalFee(tran.getOrderAmount());
		return payReqParam;
	}
	
	/**
	 * 方法：【outTransfer】-构建提现入参参数
	 * 
	 * @param payReq -支付接收参数
	 * @param status - 状态
	 * @return 封装支付订单对象
	 */
	protected OutTransfer buildOutTransfer(String outTradeNo,AccountInfo accInfoQuery,BigDecimal withdrawPrice) {
		OutTransfer outTransfer = new OutTransfer();
		outTransfer.setClientID(outTradeNo);
		outTransfer.setAccountNo(accInfoQuery.getSubAccNo());
		outTransfer.setRecvAccNo(accInfoQuery.getBindingAccNo());
		outTransfer.setRecvAccNm(accInfoQuery.getBindingAccNm());
		outTransfer.setRecvTgfi(accInfoQuery.getBankBranchJointLine());
		outTransfer.setRecvBankNm(accInfoQuery.getBankName());
		outTransfer.setSameBank(accInfoQuery.getSameBank());
		outTransfer.setTranAmt(withdrawPrice);
		outTransfer.setPreFlg("0");// 预约标志（0：非预约1：预约）
		// 提现预约第二天上午10点
//		String nextDateStr=DateUtils.format(DateUtils.dayOffset(new Date(), 1), DateUtils.YMD);
//		outTransfer.setPreDate(nextDateStr);
//		outTransfer.setPreDate("100000");
		return outTransfer;
	}
	
	/**
	 * 查询金融账户
	 * 
	 * @return
	 */
	private FinanceAccountInfoDto findFinanceAcc() {
		return financeAccountDAO.queryById(1);
	}
	
	/**
	 * 加锁
	 */
	private void lock(String methodStr,int batch) {
		lockTableDAO.updateByTable(methodStr, batch);
	}
	
	@Override
	public ExecuteResult<HashMap<String,String>> buildRefundForm(RefundReqParam refundReqParam) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean querySubBalanceForPay(BigDecimal payPrice, String subAccNo){
		if(payPrice == null){
			return false;
		}
		AffiliatedBalanceDto affiliatedBalanceDto = null;
		try {
			affiliatedBalanceDto = this.querySubBalance(subAccNo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询余额失败："+subAccNo);
			return false;
		}
		BigDecimal userKyAmt=new BigDecimal(affiliatedBalanceDto.getList().get(0).getKYAMT());// 付款可用余额
		int isPay=payPrice.compareTo(userKyAmt);
		if(isPay>0){
			return false;
		}
		return true;
	}
}
