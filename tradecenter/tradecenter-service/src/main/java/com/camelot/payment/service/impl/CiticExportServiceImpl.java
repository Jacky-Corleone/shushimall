package com.camelot.payment.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.accountinfo.dao.FinanceAccountDAO;
import com.camelot.common.enums.CiticEnums.AccStatus;
import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.common.enums.SystemKey;
import com.camelot.common.enums.TransationsStatusEnum;
import com.camelot.common.util.Signature;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.payment.CiticExportService;
import com.camelot.payment.dao.AccountInfoDAO;
import com.camelot.payment.dao.CiticPayJournalDAO;
import com.camelot.payment.dao.CompanyPayJobDAO;
import com.camelot.payment.domain.AccountInfo;
import com.camelot.payment.domain.CiticPayJournal;
import com.camelot.payment.domain.CompanyPayJob;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CiticPayJournalDto;
import com.camelot.payment.dto.CiticTradeInDTO;
import com.camelot.payment.dto.CiticTradeOutDTO;
import com.camelot.payment.dto.CompanyPayJobDTO;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.dto.citic.req.MainBalanceDto;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.camelot.payment.service.CiticService;
import com.camelot.payment.service.ImplUtil;
import com.camelot.payment.service.citic.CiticUtil;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.camelot.usercenter.dto.userInfo.UserAccountDTO;
import com.camelot.usercenter.dto.userInfo.UserCiticDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.enums.CommonEnums.ComStatus;
import com.camelot.usercenter.service.UserExtendsService;

/**
 *  中信对外信息主入口
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-12
 */
@Service("citicExportService")
public class CiticExportServiceImpl extends ImplUtil implements CiticExportService {
	private final static Logger logger = LoggerFactory.getLogger(CiticExportServiceImpl.class);
	
	@Resource(name="citicGateService")
	private CiticService citicService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private AccountInfoDAO accountInfoDAO;
	@Resource
	private CiticPayJournalDAO citicPayJournalDAO;
	@Resource
	private CompanyPayJobDAO companyPayJobDAO;
	@Resource
	private FinanceAccountDAO financeAccountDAO;

	@Override
	public ExecuteResult<MainBalanceDto> queryBalance(Map<String,String> map){
		logger.info("\n 方法[{}]，入参：[{}]","CiticExportServiceImpl-queryBalance[查询主体账户余额]",JSON.toJSONString(map));
		ExecuteResult<MainBalanceDto> result =new  ExecuteResult<MainBalanceDto>();
		
		// 签名校验
		Map<String, String> mapSignature=new HashMap<String, String>();
		mapSignature.put("system", map.get("system"));
		if(!Signature.verifySign(map.get("sign"),mapSignature,SysProperties.getProperty(SystemKey.MainBalance.getKeyPri()))){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Signature_Error,""));
			return result;
		}
		
		try {
			MainBalanceDto mainBalanceDto=citicService.queryBalance();
			if(mainBalanceDto!=null&&mainBalanceDto.getStatus().contains("AAAAAA")){ // 成功
				result.setResult(mainBalanceDto);
			}else{ // 失败
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Citic_Business_Error,"查询主体账户余额失败"));
			}
			logger.info("\n 方法[{}]，出参：[{}]","CiticServiceImpl-queryBalance[查询主体账户余额]",JSON.toJSON(result));
		} catch (Exception e) {
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Citic_Service_Error,e.getMessage()));
			logger.info("\n 方法[{}]，异常：[{}]","CiticServiceImpl-queryBalance[查询主体账户余额]",e.getMessage());
		} 
		return result;
	}
	
	@Override
	public  ExecuteResult<AccountInfoDto> addAffiliated(AccountInfoDto accountInfoDto,Integer platformId) throws Exception {
		logger.info("\n 方法[{}]，入参：[{}]","CiticExportServiceImpl-addAffiliated[附属账户预签约]",JSON.toJSONString(accountInfoDto));
		// 校验
		ExecuteResult<AccountInfoDto> result = CiticUtil.vetifyAddAcc(accountInfoDto);
		if (!result.isSuccess()) {
			return result;
		}
		// 获取账号的存在性，不存在则添加否则返回结果
		AccountInfo accountInfo=accountInfoDAO.selectByUId(accountInfoDto.getUserId(),accountInfoDto.getAccType().getCode());
		if(accountInfo ==null){// 创建账户
			accountInfo =EntityTranslator.transObj(accountInfoDto,AccountInfo.class,false);
			accountInfo.setAccType(accountInfoDto.getAccType().getCode());
			/*if(AccountType.AccBuyPay==accountInfoDto.getAccType()){
				//判断当前用户是绿印平台还是科印平台
				if(platformId == null){
					accountInfo.setSubAccNm("印刷家"+accountInfo.getSubAccNm());
				}else if(platformId == 2){
					accountInfo.setSubAccNm("绿印"+accountInfo.getSubAccNm());
				}
				
			}*/
			accountInfoDAO.add(accountInfo);
		}
		// 封装信息
		AccountInfoDto accDto=EntityTranslator.transObj(accountInfo,AccountInfoDto.class, true);
		accDto.setAccType(accountInfoDto.getAccType());
		result.setResult(accDto);
		logger.info("\n 方法[{}]，出参：[{}]","CiticExportServiceImpl-addAffiliated[附属账户预签约]",JSON.toJSON(result));
		return result;
	}
	
	@Override
	public ExecuteResult<AccountInfoDto> queAffiliatedStatus(String subAccNo){
		logger.info("\n 方法[{}]，入参：[{}]","CiticExportServiceImpl-queAffiliatedStatus[附属账户签约查询]","");
		ExecuteResult<AccountInfoDto> result =new  ExecuteResult<AccountInfoDto>();
		AccountInfo  accQuery=accountInfoDAO.selectBySubAccNo(subAccNo);
		if(accQuery!=null){
			result.setResult(EntityTranslator.transObj(accQuery, AccountInfoDto.class, true));
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Logic_Error,"未查询当该附属账号状态"));
		}
		logger.info("\n 方法[{}]，出参：[{}]","CiticExportServiceImpl-queAffiliatedStatus[附属账户签约查询]",JSON.toJSON(result));
		return result;
	}
	
	@Override
	public ExecuteResult<AffiliatedBalanceDto> querySubBalance(Map<String,String> map){
		logger.info("\n 方法[{}]，入参：[{}]","CiticExportServiceImpl-querySubBalance[查询商户账户余额]",JSON.toJSONString(map));
		ExecuteResult<AffiliatedBalanceDto> result =new  ExecuteResult<AffiliatedBalanceDto>();
		// 签名校验
		Map<String, String> mapSignature=new HashMap<String, String>();
		mapSignature.put("system", map.get("system"));
		mapSignature.put("subAccNo", map.get("subAccNo"));
		if(!Signature.verifySign(map.get("sign"),mapSignature,SysProperties.getProperty(SystemKey.getOpenKeyPri(map.get("system"))))){
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Signature_Error,""));
			return result;
		}
		
		try {
			result.setResult(citicService.querySubBalance(map.get("subAccNo")));
			logger.info("\n 方法[{}]，出参：[{}]","CiticExportServiceImpl-querySubBalance[查询商户账户余额]",JSON.toJSON(result));
		} catch (Exception e) {
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Citic_Service_Error,e.getMessage()));
			logger.info("\n 方法[{}]，异常：[{}]","CiticExportServiceImpl-querySubBalance[查询商户账户余额]",e.getMessage());
		} 
		return result;
	}
	
	@Override
	public Map<String, String> payCitic(Map<String, String> map) throws Exception{
		
		// 初始化
		Map<String, String> result=new HashMap<String, String>();
		result.put("system", SystemKey.CiticPay.name());
		result.put("v_status",TransationsStatusEnum.PAID_FAIL.getCode().toString());
		result.put("v_buy_acc", "");
		result.put("v_totel_fee", "");
		result.put("v_oid", map.get("outTradeNo"));
		result.put("v_mark_code","0"); // 金额不足标记初始化
		String resultMsg="";
		
		// 解析入参map参数
		/*** 校验签名 ***/
		Map<String, String> verifyMap=new HashMap<String, String>();
		verifyMap.put("system", map.get("system"));
		verifyMap.put("outTradeNo",map.get("outTradeNo"));
		verifyMap.put("uid",map.get("uid"));
		verifyMap.put("accType",map.get("accType"));
		if(Signature.verifySign(map.get("sign"),verifyMap, SysProperties.getProperty(SystemKey.getOpenKeyPri(map.get("system"))))){
			// 执行支付
			AccountType accType=  AccountType.getEnumByCode(Integer.parseInt(map.get("accType")));
			ExecuteResult<Transations> payResult=citicService.payCitic(map.get("outTradeNo"), Long.parseLong(map.get("uid")), accType);
			if(payResult.isSuccess()){
				Transations tranResult=payResult.getResult();
				result.put("v_buy_acc", tranResult.getFromAccount());
				result.put("v_totel_fee", tranResult.getOrderAmount().toString());
				result.put("v_status",TransationsStatusEnum.PAID_SUCCESS.getCode().toString());
				resultMsg=payResult.getResultMessage();
			}else{
				resultMsg=payResult.getErrorMessages().get(0);
				if(StringUtils.isNotBlank(payResult.getResultMessage())){ // 买家账户余额不足 此标记有效
					result.put("v_mark_code",payResult.getResultMessage());
				}
			}
		}else{
			resultMsg="支付校验失败";
		}
		result.put("sign",Signature.createSign(result,SysProperties.getProperty(SystemKey.CiticPay.getKeyPri())));
		result.put("v_status_text",resultMsg);
		result.put("isNotify", "false");// 同步标记
		return result;
	}
	
	@Override
	public ExecuteResult<String> outPlatformTransfer(AccountInfoDto accDto) throws Exception{
		return this.outPlatformTransfer(accDto, null);
	}
	
	@Override
	public ExecuteResult<String> outPlatformTransfer(AccountInfoDto accDto,Long uid) throws Exception{
		logger.info("\n 方法[{}]，入参：[{}]","CiticExportServiceImpl-outPlatformTransfer[商户出金]",JSON.toJSONString(accDto));
		// 1.校验参数 
		ExecuteResult<String> result =verifyOutTransfer(accDto);
		if(!result.isSuccess()){
			return result;
		}
		
		// 2.查询结果数据封装到 accInfoBuild
		AccountInfo accInfoBuild =new AccountInfo(); 
		accInfoBuild.setUserId(accDto.getUserId());
		ExecuteResult<UserInfoDTO> userInfo = userExtendsService.findUserInfo(accDto.getUserId());
		if(userInfo!=null&&userInfo.getResult()!=null&&userInfo.getResult().getUserAccountDTO()!=null){
			UserAccountDTO userAcc = userInfo.getResult().getUserAccountDTO();
			// 封装用户账户信息
			UserCiticDTO userCiticAcc = userInfo.getResult().getUserCiticDTO();
			
			if(AccountType.AccBuyPay==accDto.getAccType()) {
				accInfoBuild.setSubAccNo(userCiticAcc.getBuyerPaysAccount());
			}else if(AccountType.AccBuyFinancing==accDto.getAccType()){
				accInfoBuild.setSubAccNo(userCiticAcc.getBuyerFinancingAccount());
			}else if(AccountType.AccSellReceipt==accDto.getAccType()){
				accInfoBuild.setSubAccNo(userCiticAcc.getSellerWithdrawsCashAccount());
			}
			accInfoBuild.setAccType(accDto.getAccType().getCode());
			
			if(StringUtils.isNotBlank(userAcc.getBankAccount())){
				accInfoBuild.setBindingAccNo(userAcc.getBankAccount());
				accInfoBuild.setBindingAccNm(userAcc.getBankAccountName());
				accInfoBuild.setBankBranchJointLine(userAcc.getBankBranchJointLine());
				accInfoBuild.setBankName(userAcc.getBankName());
				accInfoBuild.setSameBank(userAcc.getIsCiticBank());
			}else if(StringUtils.isNotBlank(accDto.getBindingAccNo())){
				accInfoBuild.setBindingAccNo(accDto.getBindingAccNo());
				accInfoBuild.setBindingAccNm(accDto.getBindingAccNm());
				accInfoBuild.setBankBranchJointLine(accDto.getBankBranchJointLine());
				accInfoBuild.setBankName(accDto.getBankName());
				accInfoBuild.setSameBank(accDto.getSameBank());
			}
			
			if(StringUtils.isBlank(accInfoBuild.getSubAccNo())&&ComStatus.PASS.getCode() != userAcc.getBankAccountStatus()){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"该用户不存在可出金的附属账户"));
				return result;
			}
			// 数据库中绑定账户不存在并且入参没有加入绑定账户
			if(StringUtils.isBlank(accInfoBuild.getBindingAccNo())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"请输入出金实体账户"));
				return result;
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Business_Param_Null,"当前用户相关信息不存在"));
			return result;
		}
		
		result=citicService.outPlatformTransfer(accInfoBuild,accDto.getWithdrawPrice(),uid); // 提现操作
		logger.info("\n 方法[{}]，出参：[{}]","CiticExportServiceImpl-outPlatformTransfer",JSON.toJSON(result));
		return result;
	}

	@Override
	public List<AccountInfoDto> findAccByStatus(AccStatus accStatus) {
		List<AccountInfoDto> listResult=new ArrayList<AccountInfoDto>();
		List<AccountInfo> listAcc = accountInfoDAO.selectAccByStatus(accStatus.ordinal());
		for (AccountInfo accQuery:listAcc) {
			listResult.add(EntityTranslator.transObj(accQuery, AccountInfoDto.class, true));
		}
		return listResult;
	}
	
	@Override
	public boolean saveAffiliated(AccountInfoDto accountInfoDto){
		return citicService.addAffiliated(EntityTranslator.transObj(accountInfoDto, AccountInfo.class, false));
	}
	
	@Override
	public boolean modifyAffiliatedStatus(AccountInfoDto accountInfoDto){
		return citicService.updateAffiliatedStatus(EntityTranslator.transObj(accountInfoDto, AccountInfo.class, false));
	}

	@Override
	public QueryTransferDto queryTransfer(String outTradeNo) throws Exception{
		return citicService.queryTransfer(outTradeNo);
	}
	
	@Override
	public List<AccountInfoDto> findByUIds(List<Long> listUId,Integer[] accountTypes) {
		List<AccountInfoDto> listResult=new ArrayList<AccountInfoDto>();
		List<AccountInfo> listAcc = accountInfoDAO.selectByUIds(listUId, accountTypes);
		for (AccountInfo accQuery:listAcc) {
			listResult.add(EntityTranslator.transObj(accQuery, AccountInfoDto.class, true));
		}
		return listResult;
	}
	
	@Override
	public List<CiticPayJournalDto> findCiticPayJournalList(CiticPayJournalDto citicPayJournalDto) {
		List<CiticPayJournalDto> listResult=new ArrayList<CiticPayJournalDto>();
		CiticPayJournal citicPayJournalQuery = new CiticPayJournal();
		BeanUtils.copyProperties(citicPayJournalDto, citicPayJournalQuery);
		List<CiticPayJournal> listJournal = citicPayJournalDAO.queryList(citicPayJournalQuery,null);
		for (CiticPayJournal citicPayJournal:listJournal) {
			BeanUtils.copyProperties(citicPayJournal, citicPayJournalDto);
			listResult.add(citicPayJournalDto);
		}
		return listResult;
	}
	
	@Override
	public int modifyCiticPayJournalDeal(CiticPayJournalDto citicPayJournalDto){
		return citicPayJournalDAO.update(EntityTranslator.transObj(citicPayJournalDto, CiticPayJournal.class, false));
	}

	@Override
	public ExecuteResult<String> saveComPayJob(CompanyPayJobDTO companyPayJobDTO) throws Exception {
		ExecuteResult<String> result =this.verifySaveComPayJob(companyPayJobDTO);
		if(!result.isSuccess()){
			return result;
		}
		long count =companyPayJobDAO.queryByOutTrade(companyPayJobDTO.getOutTradeNo());
		if(count==0){
			CompanyPayJob companyPayJob = new CompanyPayJob();
			companyPayJob.setOutTradeNo(companyPayJobDTO.getOutTradeNo());
			companyPayJob.setOrderNo(companyPayJobDTO.getOrderNo());
			companyPayJob.setUserId(companyPayJobDTO.getUserId());
			companyPayJob.setAccType(companyPayJobDTO.getAccType().getCode());
			companyPayJobDAO.add(companyPayJob);
		}
		return result;
	}
	
	@Override
	public List<CompanyPayJobDTO> findUnDealComPayJob() {
		List<CompanyPayJobDTO> listResult=new ArrayList<CompanyPayJobDTO>();
		List<CompanyPayJob> listCompanyPayJob =companyPayJobDAO.selectUnDeal();
		for (CompanyPayJob companyPayJob:listCompanyPayJob) {
			CompanyPayJobDTO comDTO=EntityTranslator.transObj(companyPayJob, CompanyPayJobDTO.class, true);
			comDTO.setAccType(AccountType.getEnumByCode(companyPayJob.getAccType()));
			listResult.add(comDTO);
		}
		return listResult;
	}

	@Override
	public int dealComPayJobById(Long id) {
		return companyPayJobDAO.dealById(id);
	}

	@Override
	public int modifyComPayJobByOrder(String outTradeNo) {
		return companyPayJobDAO.enableByOutTradeNo(outTradeNo);
	}

	@Override
	public int modifyComPayJob(CompanyPayJobDTO companyPayJobDTO) {
		return companyPayJobDAO.update(EntityTranslator.transObj(companyPayJobDTO, CompanyPayJob.class, false));
	}

	@Override
	public ExecuteResult<String> transferAffiliated(PayReqParam payReqParam,
			String orderParentTradeNo, int code) {
		ExecuteResult<String> result=new ExecuteResult<String>();
		CiticPayTypeCode citicPayTypeCode =CiticPayTypeCode.getEnumByCode(code);
		if(citicPayTypeCode!=null){
			result= citicService.transferAffiliated(payReqParam, orderParentTradeNo, citicPayTypeCode);
		}else{
			result.addErrorMessage("code异常");
		}
		return result;
	}
	@Override
	public ExecuteResult<AccountInfoDto> getAccountInfoByUserIdAndAccountType(Long userId,Integer accountType){
		ExecuteResult<AccountInfoDto> result = new ExecuteResult<AccountInfoDto>();
		AccountInfo accountInfo=accountInfoDAO.selectByUId(userId,accountType);
		if(accountInfo == null){
			logger.error("无法获取用户的支付账号。userId:"+userId+",accountType："+accountType);
			result.addErrorMessage("无法获取用户的支付帐号");
			return result;
		}
		//转换对象
		AccountInfoDto accountInfoDto=EntityTranslator.transObj(accountInfo,AccountInfoDto.class, true);
		result.setResult(accountInfoDto);
		return result;
	}

	@Override
	public ExecuteResult<CiticTradeOutDTO> queryCiticTradeList(CiticTradeInDTO citicTradeInDTO) {
		ExecuteResult<CiticTradeOutDTO> result = new ExecuteResult<CiticTradeOutDTO>();
		FinanceAccountInfoDto financeAccountInfoDto = financeAccountDAO.queryById(1);
		try {
			CiticTradeOutDTO citicTradeOutDTO = CiticUtil.queryCiticTradeList(citicTradeInDTO, financeAccountInfoDto);
			result.setResult(citicTradeOutDTO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("查询交易信息发生异常",e);
			result.addErrorMessage("查询交易信息发生异常");
		}
		return result;
	}
}
