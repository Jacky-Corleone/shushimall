package com.camelot.payment.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.dom4j.DocumentException;

import com.camelot.common.enums.CiticEnums.AccountType;
import com.camelot.common.enums.CiticEnums.CiticPayTypeCode;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.payment.domain.AccountInfo;
import com.camelot.payment.domain.Transations;
import com.camelot.payment.dto.PayReqParam;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.dto.citic.req.MainBalanceDto;
import com.camelot.payment.dto.citic.req.QueryTransferDto;

/**
 *  中信服务接口-关联中信接口
 * 
 * @Description -
 * @author - learrings
 * @createDate - 2015-3-19
 */
public interface CiticService extends PayService{
	
	/**
	 * 添加附属账户
	 * 
	 * @param accountInfo - 附属账户信息
	 * @return 添加是否成功
	 */
	boolean addAffiliated(AccountInfo accountInfo);
	
	/**
	 * 更新附属账户签约状态
	 * 
	 * @param	accountInfo - 附属账户信息
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	boolean updateAffiliatedStatus(AccountInfo accountInfo);
	
	
	/**
	 * 普通附属账户间的支付
	 * 
	 * @param payReqParam
	 * @param orderParentTradeNo - 父级对外交易号
	 * @param payType - 交易类型  0支付/1结算
	 * @return
	 */
	ExecuteResult<String> transferAffiliated(PayReqParam payReqParam,String orderParentTradeNo,CiticPayTypeCode citicPayTypeCode);

	/**
	 * 查询主体账户余额
	 * 
	 * @return
	 * @throws DocumentException,IOException - 链接异常，解析异常 
	 * @throws  
	 */
	MainBalanceDto queryBalance() throws IOException, DocumentException;
	
	/**
	 * 查询附属账户余额
	 * 
	 * @param subAccNo - 
	 * @return AffiliatedBalanceDto
	 * @throws DocumentException,IOException - 链接异常，解析异常 
	 */
	AffiliatedBalanceDto querySubBalance(String subAccNo) throws IOException, DocumentException;
	/**
	 * 查询附属账户余额是否足以支付
	 * 
	 * @param payPrice 应付金额,必传，不可为空
	 * @param subAccNo - 支付账户
	 * @return true 余额足够，false余额不足
	 * @throws DocumentException,IOException - 链接异常，解析异常 
	 */
	boolean querySubBalanceForPay(BigDecimal payPrice , String subAccNo);
	
	/**
	 * 根据对外交易号查询交易记录
	 * 
	 * @param outTradeNo - 
	 * @return QueryTransferDto
	 * @throws DocumentException,IOException - 链接异常，解析异常 
	 */
	QueryTransferDto queryTransfer(String outTradeNo) throws IOException, DocumentException;
	
	/**
	 * 平台出金
	 * 
	 * @param outTransfer
	 * @return
	 * @throws DocumentException,IOException - 链接异常，解析异常 
	 * @see com.camelot.payment.service.CiticService#outPlatformTransfer(AccountInfo, BigDecimal, Long)
	 */
	@Deprecated
	ExecuteResult<String> outPlatformTransfer(AccountInfo accInfoBuild,BigDecimal withdrawPrice) throws IOException, DocumentException;
	
	/**
	 * 中信交易
	 * 
	 * @param outTradeNo - 对外订单号
	 * @param buyId - 买家ID
	 * @param accType - 买家账户类型
	 * @return fromAccount - 付款账号，orderAmount - 支付金额
	 */
	ExecuteResult<Transations> payCitic(String outTradeNo,Long buyId,AccountType accType);

	/**
	 * 平台出金
	 * 
	 * @param accInfoBuild 中信账户信息记录
	 * @param withdrawPrice 提现金额
	 * @param uid 提现人ID
	 * @return
	 * @throws DocumentException 解析异常 
	 * @throws IOException 链接异常
	 * @author zhouzhijun
	 */
	ExecuteResult<String> outPlatformTransfer(AccountInfo accInfoBuild,
			BigDecimal withdrawPrice, Long uid) throws IOException,
			DocumentException;

}
