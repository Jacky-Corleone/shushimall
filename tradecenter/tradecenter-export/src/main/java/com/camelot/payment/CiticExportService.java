package com.camelot.payment;

import java.util.List;
import java.util.Map;

import com.camelot.common.enums.CiticEnums.AccStatus;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.domain.AccountInfo;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CiticPayJournalDto;
import com.camelot.payment.dto.CiticTradeInDTO;
import com.camelot.payment.dto.CiticTradeOutDTO;
import com.camelot.payment.dto.CompanyPayJobDTO;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.dto.citic.req.MainBalanceDto;
import com.camelot.payment.dto.citic.req.QueryTransferDto;

/**
 * 供客户端调用的中信远程接口
 * 
 * @author
 * 
 */
public interface CiticExportService {

	/**
	 * 查询主体账户余额信息 system+key签名
	 * 
	 * @return
	 */
	ExecuteResult<MainBalanceDto> queryBalance(Map<String,String> map) ;
	
	/**
	 * 附属账户预签约(开通账户)
	 * 
	 * @param uid - [用户ID] 必填
	 * @param subAccNm -[附属账户名称] 必填，和创建账号绑定
	 * @param accountType - [附属账户类型] 必填，类型参见枚举AccountType
	 * @param bindingAccNo - [绑定账户],选填，一旦填写，提现将转出到此账号
	 * @param bindingAccNm - [绑定账户名称]，当bindingAccNo填写，此项为必填
	 * @param bankName/bankBranchJointLine - [开户行支行名称]/[开户行支行联行号]，当bindingAccNo填写，此项至少填一项
	 * @param sameBank -中信标识 0本行 1他行,当bindingAccNo填写，此项为必填
	 * @return
	 */
	ExecuteResult<AccountInfoDto> addAffiliated(AccountInfoDto accountInfoDto,Integer platformId) throws Exception;
	
	/**
	 * 附属账户签约状态查询
	 * 
	 * @param subAccNo 附属账号
	 * @return
	 */
	ExecuteResult<AccountInfoDto> queAffiliatedStatus(String subAccNo);
	
	/**
	 * 商户查询账户的余额信息。
	 * 
	 * @param	 system -系统编号,subAccNo - 附属账号,sign - 签名
	 * @return
	 */
	ExecuteResult<AffiliatedBalanceDto> querySubBalance(Map<String,String> map);
	
	/**
	 * 附属账户申请交易
	 * 
	 * @param payParams - system[系统编码]/outTradeNo[对外订单号]/uid[买家用户ID]/accType[买家用户类型]  --必填参数
	 * @param payParams - sign 加密串
	 * @return Map - 交易结果
	 */
	Map<String, String> payCitic(Map<String, String> payParams) throws Exception;
	
	/**
	 * 附属账户出金（提现）system，uid，accType.name()，withdrawPrice+key签名
	 * 当买/卖家已绑定实体账户，提现到该实体账户，否则必须填写绑定账户和绑定账户名称
	 * @param accountInfoDto - accType[附属账户类型]/userId[用户ID]/withdrawPrice[提现金额]  --必填参数
	 * @param bindingAccNo - [绑定账户],选填，提现将转出到此账号
	 * @param bindingAccNm - [绑定账户名称]，当bindingAccNo填写，此项为必填
	 * @param bankName - [开户行支行名称]、bankBranchJointLine - [开户行支行联行号]，当bindingAccNo填写，此项至少填一项
	 * @param sameBank -中信标识 1本行 0他行,当bindingAccNo填写，此项为必填						
	 * @return
	 * @see com.camelot.payment.CiticExportService#outPlatformTransfer(AccountInfoDto, Long)
	 */
	@Deprecated
	ExecuteResult<String> outPlatformTransfer(AccountInfoDto accountInfoDto) throws Exception;

	/**
	 * 查询5分钟前，60分钟以内的指定状态的账号(超过五次尝试/创建失败-将不再生成)
	 * 
	 * @param accStatus -0 待生成 1待审核
	 * @return
	 */
	List<AccountInfoDto> findAccByStatus(AccStatus accStatus) ;
	
	/**
	 * 添加附属账户
	 * 
	 * @param accountInfo - 附属账户信息
	 * @return 添加是否成功
	 */
	boolean saveAffiliated(AccountInfoDto accountInfoDto);
	
	/**
	 * 更新附属账户签约状态
	 * 
	 * @param	accountInfo - 附属账户信息
	 * @return
	 */
	boolean modifyAffiliatedStatus(AccountInfoDto accountInfoDto);
	
	/**
	 * 根据对外交易号查询交易结果
	 * 
	 * @param outTradeNo
	 * @return
	 */
	QueryTransferDto queryTransfer(String outTradeNo) throws Exception;
	
	/**
	 * 查询所选用户组的账户信息(审核通过)
	 * 
	 * @param listUId - 用户ID组
	 * @param userType - 用户类型 2[买家]/3[卖家]，可空
	 * @return
	 */
	List<AccountInfoDto> findByUIds(List<Long> listUId,Integer[] accountTypes);
	
	/**
	 * 根据条件查询中信支付信息
	 * 
	 * @param citicPayJournalDto
	 * @return
	 */
	List<CiticPayJournalDto> findCiticPayJournalList(CiticPayJournalDto citicPayJournalDto);
	
	/**
	 * 修改中信支付处理标记
	 * 
	 * @param citicPayJournalDto
	 * @return
	 */
	int modifyCiticPayJournalDeal(CiticPayJournalDto citicPayJournalDto);
	
	/**
	 * 充值并支付job初始化添加
	 * 
	 * @param companyPayJobDTO - outTradeNo OrderNo buyId accType
	 * @return
	 * @throws Exception
	 */
	ExecuteResult<String> saveComPayJob(CompanyPayJobDTO companyPayJobDTO) throws Exception;
	/**
	 * 根据批次升序查询未处理的金额不足待支付交易(72小时内,10条)
	 * 
	 * @return
	 */
	List<CompanyPayJobDTO> findUnDealComPayJob();
	
	/**
	 * 更新金额不足待支付的交易变为有效性
	 * 
	 * @param outTradeNo - 对外交易号
	 * 
	 * @return
	 */
	int modifyComPayJobByOrder(String outTradeNo);
	
	/**
	 * 金额不足待支付交易修改处理标记
	 * 
	 * @param id - 主键
	 * @return
	 */
	int dealComPayJobById(Long id);
	
	/**
	 * 修改充值并支付job信息
	 * 
	 * @param companyPayJobDTO - 
	 * @return
	 */
	int modifyComPayJob(CompanyPayJobDTO companyPayJobDTO);
	
	/**
	 * 普通附属账户间的支付
	 * 
	 * @param payReqParam
	 * @param orderParentTradeNo - 父级对外交易号
	 * @param code - CiticPayTypeCode
	 * @return
	 */
	ExecuteResult<String> transferAffiliated(PayReqParam payReqParam,String orderParentTradeNo,int code);

	/**
	 * 附属账户出金（提现）system，uid，accType.name()，withdrawPrice+key签名
	 * 当买/卖家已绑定实体账户，提现到该实体账户，否则必须填写绑定账户和绑定账户名称
	 * @param accDto 中信账户信息
	 * @param uid 提现用户ID						
	 * @return
	 */
	ExecuteResult<String> outPlatformTransfer(AccountInfoDto accDto, Long uid)
			throws Exception;
	/**
	 * 根据用户ID和账户类型，查询帐号信息
	 * @param userId  用户ID
	 * @param accountType  账户类型
	 * @return
	 */
	public ExecuteResult<AccountInfoDto> getAccountInfoByUserIdAndAccountType(Long userId,Integer accountType);
	/**
	 * 查询当前帐号，当前中信银行的交易记录，用于非登录打印
	 * @param citicTradeInDTO
	 * @return
	 */
	public ExecuteResult<CiticTradeOutDTO> queryCiticTradeList(CiticTradeInDTO citicTradeInDTO);
}
