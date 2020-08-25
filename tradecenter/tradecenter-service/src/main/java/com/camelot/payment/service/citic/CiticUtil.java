package com.camelot.payment.service.citic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.camelot.common.enums.CiticEnums.AccStatus;
import com.camelot.common.enums.CiticEnums.CiticMethodCode;
import com.camelot.common.enums.MsgCodeEnum;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.exception.CommonCoreException;
import com.camelot.payment.domain.AccountInfo;
import com.camelot.payment.dto.AccountInfoDto;
import com.camelot.payment.dto.CiticTradeInDTO;
import com.camelot.payment.dto.CiticTradeOutDTO;
import com.camelot.payment.dto.citic.req.AffiliatedAddDto;
import com.camelot.payment.dto.citic.req.AffiliatedBalanceDto;
import com.camelot.payment.dto.citic.req.AffiliatedQueryDto;
import com.camelot.payment.dto.citic.req.MainBalanceDto;
import com.camelot.payment.dto.citic.req.OutTransferDto;
import com.camelot.payment.dto.citic.req.QueryTransferDto;
import com.camelot.payment.dto.citic.req.TransferDto;
import com.camelot.payment.dto.citic.res.OutTransfer;
import com.camelot.payment.dto.citic.res.Transfer;
import com.camelot.tradecenter.dto.FinanceAccountInfoDto;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CiticUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(CiticUtil.class);
    /**
     * 建立请求，以表单HTML形式构造（默认）
     * @param sParaTemp 请求参数数组
     * @param strMethod 提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
    public static String buildRequest(Map<String, String> sParaTemp, String url, String strButtonName) {
        //待请求参数数组
        List<String> keys = new ArrayList<String>(sParaTemp.keySet());

        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append("<form id=\"form\" name=\"E_FORM\" action=\"" + url+ "\" method=\"post\">");

        for (int i = 0; i < keys.size(); i++) {
            String name = (String) keys.get(i);
            String value = (String) sParaTemp.get(name);
            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }
        sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");

        return sbHtml.toString();
    }
    
    /**
	 * 发送到前置机，获取回传信息-主
	 * @param sendXml
	 * @param citicFrontEndProcessor
	 * 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static String postRequest(String sendXml,String citicFrontEndProcessor) throws IOException, DocumentException{
		logger.info("\n 方法[{}]，入参：[{}]","CiticUtil-postRequest","发送报文："+sendXml);
		Document document = null;
		// 发送报文
		URL sendUrl = new URL(citicFrontEndProcessor);
		URLConnection connection = sendUrl.openConnection();
		connection.setConnectTimeout(30000);
		connection.setReadTimeout(30000);
		connection.setDoOutput(true);
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "GBK");
		out.write(sendXml);
		out.flush();
		out.close();
	   
		// 一旦发送成功，用以下方法就可以得到服务器的回应：
		InputStream is = connection.getInputStream();
		InputStreamReader isr = new InputStreamReader(is,"GBK");
		SAXReader saxReader = new SAXReader(); 
		document = saxReader.read(isr);
		logger.info("\n 方法[{}]，出参：[{}]","CiticUtil-postRequest","接收报文："+document.asXML());
		return document.asXML();
	}
	
	/**
	 * 封装发送信息-主
	 * @param citicType
	 * @param content
	 * @param citicUserName
	 * @return
	 */
	public static String buildSendXml(CiticMethodCode citicMethodCode,String content,String citicUserName){
		StringBuffer sb=new StringBuffer("<?xml version=\"1.0\" encoding=\"GBK\"?>");
		sb.append("<stream>")
			.append(	"<action>"+citicMethodCode.getAction()+"</action>")
			.append(	"<userName>"+citicUserName+"</userName>")//<!--登录名varchar(30)-->
			.append(content)
			.append("</stream>");
		return sb.toString();
	}
	
	/**
	 * 查询主体账户余额信息
	 * @param accountNo
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static MainBalanceDto queryBalance(FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException{
		StringBuffer sb=new StringBuffer();
		sb.append("<list name=\"userDataList\">")
			.append(	"<row>" )
			.append(		"<accountNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</accountNo>" )
			.append(	"</row>")
			.append("</list>");
		String document = postRequest(buildSendXml(CiticMethodCode.MainAccount_Query, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(MainBalanceDto.class);	
		return (MainBalanceDto)xstream.fromXML(document);
	}
	
	/**
	 * 附属账户预签约
	 * 
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void addAffiliated(AccountInfo accountInfo,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException,CommonCoreException{
		StringBuffer sb=new StringBuffer();
		
		sb.append("<mainAccNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</mainAccNo>") //<!-- 主体账号char(19)-->
			.append("<appFlag>2</appFlag>" )//<!--应用系统char(1)， 2：B2B电子商务-->
			.append("<accGenType>1</accGenType>")//<!--附属账户生成方式char(1) ，1：手动生成-->
			.append("<subAccNo>"+accountInfo.getSubAccNo().substring(5)+"</subAccNo>")// <!--附属账号 char(14) ，在accGenType生成方式为1：手动输入时，必输-->
			.append("<subAccNm>"+accountInfo.getSubAccNm()+"</subAccNm>")//<!--附属账户名称 varchar(100)，可空，appFlag为2时必输-->
			.append("<accType>03</accType>")//<!--附属账户类型 char(2)，03一般交易账号 04：保证金账号；11：招投标保证金-->
			.append("<calInterestFlag>0</calInterestFlag>")//<!--是否计算利息标志 char(1)， 0：不计息-->
			.append("<interestRate>0</interestRate>")//<!--默认计息利率 decimal(9.7)-->
			.append("<overFlag>0</overFlag>")//<!--是否允许透支char(1)，0：不允许；-->
			.append("<overAmt></overAmt>")//<!--透支额度decimal(15.2)，必须为空-->
			.append("<overRate></overRate>")//<!--透支利率decimal(9.7)，必须为空-->
			.append("<autoAssignInterestFlag>0</autoAssignInterestFlag>")//<!--自动分配利息标示char(1)，0：否-->
			.append("<autoAssignTranFeeFlag>0</autoAssignTranFeeFlag>")//<!--自动分配转账手续费标char(1)，0：否-->
			.append("<feeType>0</feeType>")//<!--手续费收取方式 char(1)，0：不收取；-->
			.append("<realNameParm>1</realNameParm>")//<!--实名制更换char(1) ，1：账户名与账号全换；-->
			.append("<subAccPrintParm>1</subAccPrintParm>")//<!--附属账户凭证打印更换 char(1)，1：显示附属账户名和账号；-->
			.append("<mngNode>211101</mngNode>");//<!--会员确认中心char(6) 211101北京-->
		String document = postRequest(buildSendXml(CiticMethodCode.AffiliatedAccount_add, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
			
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(AffiliatedAddDto.class);	
		AffiliatedAddDto affiliatedAddDto=(AffiliatedAddDto)xstream.fromXML(document);
		if(!(affiliatedAddDto!=null&&affiliatedAddDto.getStatus().contains("AAAAAA"))){
			throw new CommonCoreException("处理失败-["+affiliatedAddDto.getStatus()+"]_"+affiliatedAddDto.getStatusText());
		}
	}
	
	/**
	 * 附属账户签约状态查询
	 * 
	 * @param affiliatedId - 附属账号，可空
	 * @param affiliatedType - 查询状态，可空
	 * @param startDate - 开始时间 格式：yyyyMMdd
	 * @param endDate - 结束时间 格式：yyyyMMdd
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static AffiliatedQueryDto queryAffiliated(String affiliatedId,AccStatus accStatus,String startDate,String endDate,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException{
		StringBuffer sb=new StringBuffer();
		
		sb.append("<mainAccNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</mainAccNo>") //<!-- 主体账号char(19)-->
			.append("<subAccNo>"+(StringUtils.isNotBlank(affiliatedId)?affiliatedId:"")+"</subAccNo>")//<!--附属账号 char(19) 可空-->
			.append("<stt>"+(accStatus!=null?accStatus.ordinal():"")+"</stt>")//<!--状态 char(1)，0：正常；1：待审核；2：注销；3：审核拒绝；可空，空则代表全部状态-->
			.append("<startDate>"+startDate+"</startDate>")//<!--开始日期 char(8)-->
			.append("<endDate>"+endDate+"</endDate>");//<!--截止日期 char(8)--> 
		String document = postRequest(buildSendXml(CiticMethodCode.AffiliatedAccountStatus_Query, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
			
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(AffiliatedQueryDto.class);	
		return (AffiliatedQueryDto)xstream.fromXML(document);
	}
	
	/**
	 * 商户查询账户的余额信息。
	 * @param subAccNo 附属账号varchar(19)
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static AffiliatedBalanceDto querySubBalance(String subAccNo,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException{
		StringBuffer sb=new StringBuffer();
		sb.append(	"<accountNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</accountNo>" )
			.append(	"<subAccNo>"+subAccNo+"</subAccNo>");
		String document = postRequest(buildSendXml(CiticMethodCode.AffiliatedAccount_Query, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(AffiliatedBalanceDto.class);	
		return (AffiliatedBalanceDto)xstream.fromXML(document);
	}
	
//	/**
//	 * [暂时无用，退款可能用到]普通转账 公共计息收费附属账户向附属账户转账
//	 * 
//	 */
//	public static void transferMainToAffiliated(Transfer transfer) throws IOException, DocumentException,CommonCoreException{
//		StringBuffer sb=new StringBuffer();
//		sb.append(	"<clientID>"+transfer.getClientID()+"</clientID>")//<!--客户流水号varchar (20) -->
//			.append(	"<payAccNo>"+transfer.getPayAccNo()+"</payAccNo>")//<!--付款账号varchar(19) -->
//			.append(	"<recvAccNo>"+transfer.getRecvAccNo()+"</recvAccNo>")//<!--收款账号varchar(19) -->
//			.append(	"<recvAccNm>"+transfer.getRecvAccNm()+"</recvAccNm>")//<!--收款账户名称varchar(60) -->
//			.append(	"<tranAmt>"+transfer.getTranAmt()+"</tranAmt>")//<!--交易金额decimal(15,2) -->
//			.append(	"<memo>"+transfer.getMemo()+"</memo>");//<!--摘要varchar(22) 可空-->
//		String document = postRequest(buildSendXml(CiticMethodCode.Platform_Tenant_Transfer, sb.toString()));
//		XStream xstream = new XStream(new DomDriver());
//		xstream.processAnnotations(TransferDto.class);	
//		TransferDto transferDto =(TransferDto)xstream.fromXML(document);
//		if(!(transferDto!=null&&transferDto.getStatus().contains("AAAAAA"))){
//			throw new CommonCoreException("处理失败:"+transferDto.getStatus()+transferDto.getStatusText());
//		}
//	}
	
	/**
	 * 强制转账
	 * 附属账户之间的普通转账
	 */
	public static void transfer(Transfer transfer,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException,CommonCoreException{
		StringBuffer sb=new StringBuffer();
		sb.append(	"<accountNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</accountNo>")// <!--主体账号varchar(19) -->
			.append(	"<clientID>"+transfer.getClientID()+"</clientID>")//<!--客户流水号varchar (20) -->
			.append(	"<payAccNo>"+transfer.getPayAccNo()+"</payAccNo>")//<!--付款账号varchar(19) -->
			.append(	"<tranType>"+transfer.getTranType()+"</tranType>")//<!--转账类型varchar(2) "BF"：转账-->
			.append(	"<recvAccNo>"+transfer.getRecvAccNo()+"</recvAccNo>")//<!--收款账号varchar(19) -->
			.append(	"<recvAccNm>"+transfer.getRecvAccNm()+"</recvAccNm>")//<!--收款账户名称varchar(60) -->
			.append(	"<tranAmt>"+transfer.getTranAmt()+"</tranAmt>")//<!--交易金额decimal(15,2) -->
			.append(	"<freezeNo></freezeNo>")//<!--冻结编号varchar(22) 转账类型为“解冻”或“解冻支付”时，必输-->
			.append(	"<memo>"+transfer.getMemo()+"</memo>");//<!--摘要varchar(22) 可空-->
		String document = postRequest(buildSendXml(CiticMethodCode.TenantAccount_Transfer, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(TransferDto.class);	
		TransferDto transferDto =(TransferDto)xstream.fromXML(document);
		if(!(transferDto!=null&&transferDto.getStatus().contains("AAAAAA"))){
			logger.error("处理失败:"+transferDto.getStatus()+transferDto.getStatusText());
			throw new CommonCoreException("处理失败:"+transferDto.getStatus()+transferDto.getStatusText());
		}
	}
	
	/**
	 * 查询商户间交易结果
	 */
	public static QueryTransferDto queryTransfer(String outTradeNo,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException,CommonCoreException{
		StringBuffer sb=new StringBuffer();
		sb.append(	"<accountNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</accountNo>")// <!--主体账号varchar(19) -->
			.append(	"<clientID>"+outTradeNo+"</clientID>")//<!--客户流水号varchar (20) -->
		//	<!--原请求代码char(8)，可空，若客户能保证各交易类型的流水号唯一，则可空，否则需上送原请求代码。  资金初始化：DLFNDINI 调账入款：DLTRSFIN 错账调回：DLWFDRTN 入金：DLFONDIN 出金：DLFNDOUT 保证金退还：DLGTYRTN 转账：DLSUBTRN 强制转账：DLMDETRN-->
			.append(	"<type></type>");
		String document = postRequest(buildSendXml(CiticMethodCode.Tenant_Trade_Query, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(QueryTransferDto.class);	
		QueryTransferDto queryTransferDto = (QueryTransferDto)xstream.fromXML(document);
		if(!(queryTransferDto!=null&&"AAAAAAA".equals(queryTransferDto.getStatus()))){
			throw new CommonCoreException("处理失败:"+queryTransferDto.getStatus()+queryTransferDto.getStatusText());
		}else{
			return  queryTransferDto;
		}
	}
	
	/**
	 * 入金
	 * 说明：商户将实体结算账户中的资金转入至自有附属账户。
	 * 	
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static OutTransferDto inTransfer(OutTransfer outTransfer,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException{
		
		StringBuffer sb=new StringBuffer();
		sb.append("<clientID>"+outTransfer.getClientID()+"</clientID>")//--客户流水号 char(20) --
			.append("<accountNo>"+outTransfer.getAccountNo()+"</accountNo>")//<!--付款账号varchar(19) -->
			.append("<subAccNo>"+outTransfer.getRecvAccNo()+"</subAccNo>")//<!--收款账号varchar(19) -->
			.append("<subAccNm>"+outTransfer.getRecvAccNm()+"</subAccNm>")//<!--收款账户名称varchar(60) -->
			.append("<tranAmt>"+outTransfer.getTranAmt()+"</tranAmt>")//<!--交易金额decimal(15,2) -->
			.append("<memo>"+outTransfer.getMemo()+"</memo>");//<!--摘要varchar(22) 可空-->
			
		String document = postRequest(buildSendXml(CiticMethodCode.Tenant_InTransfer, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(OutTransferDto.class);	
		OutTransferDto outTransferDto = (OutTransferDto)xstream.fromXML(document);
		if(!(outTransferDto!=null&&"AAAAAAA".equals(outTransferDto.getStatus()))){
			throw new CommonCoreException("处理失败:"+outTransferDto.getStatus()+outTransferDto.getStatusText());
		}else{
			return  outTransferDto;
		}
	}
//	
//	/**
//	 * 出金
//	 * 说明：商户将自有附属账户中的资金转出至实体结算账户，只有他行账户才需要上送收款信息。
//	 *		1、若附属账户已绑定出入金结算账户，则收款账户必须在绑定结算账户范围内；
//	 *		2、若附属账户未绑定出入金结算账户，则可以向任意收款账户出金。
//	 * @return
//	 * @throws DocumentException 
//	 * @throws IOException 
//	 */
//	public static OutTransferDto outTransfer(OutTransfer outTransfer) throws IOException, DocumentException{
//		
//		StringBuffer sb=new StringBuffer();
//		sb.append("<clientID>"+outTransfer.getClientID()+"</clientID>")//--客户流水号 char(20) --
//			.append("<accountNo>"+outTransfer.getAccountNo()+"</accountNo>")//<!--付款账号varchar(19) -->
//			.append("<recvAccNo>"+outTransfer.getRecvAccNo()+"</recvAccNo>")//<!--收款账号varchar(19) -->
//			.append("<recvAccNm>"+outTransfer.getRecvAccNm()+"</recvAccNm>")//<!--收款账户名称varchar(60) -->
//			.append("<tranAmt>"+outTransfer.getTranAmt()+"</tranAmt>")//<!--交易金额decimal(15,2) -->
//			.append("<sameBank>"+(outTransfer.getSameBank()==1?"0":"1")+"</sameBank>")//--中信标识char(1) 0：本行 1： 他行--// 前台逻辑原因数字颠倒
//			//--收款账户开户行信息begin--
//			//--收款账户若为他行，则收款账户开户行支付联行号与收款账户开户行名至少一项不为空--
//			.append("<recvTgfi>"+outTransfer.getRecvTgfi()+"</recvTgfi>")//--收款账户开户行支付联行号varchar(12) --
//			.append("<recvBankNm>"+outTransfer.getRecvBankNm()+"</recvBankNm>")//--收款账户开户行名varchar (60) --
//			//--收款账户开户行信息end-->
//			.append("<memo>"+outTransfer.getMemo()+"</memo>")//<!--摘要varchar(22) 可空-->
//			.append("<preFlg>"+outTransfer.getPreFlg()+"</preFlg>")//--预约标志（0：非预约1：预约）char(1) --
//			.append("<preDate>"+outTransfer.getPreDate()+"</preDate>")//--预约日期（格式：YYYYMMDD 预约时非空）char(8)--
//			.append("<preTime>"+outTransfer.getPreTime()+"</preTime>");//--预约时间（格式：hhmmss 预约时非空，只限100000、120000、140000、160000四个时间点）char(6)--
//			
//		String document = postRequest(buildSendXml(CiticMethodCode.Tenant_OutTransfer, sb.toString()));
//		XStream xstream = new XStream(new DomDriver());
//		xstream.processAnnotations(OutTransferDto.class);	
//		return (OutTransferDto)xstream.fromXML(document);
//	}
	
	/**
	 * 平台出金 
	 * 商户可使用此接口完成会员交易资金附属账户出金功能。
	 * 
	 * @return
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static OutTransferDto outPlatformTransfer(OutTransfer outTransfer,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException{
		
		StringBuffer sb=new StringBuffer();
		sb.append("<clientID>"+outTransfer.getClientID()+"</clientID>")//--客户流水号 char(20) --
			.append("<accountNo>"+outTransfer.getAccountNo()+"</accountNo>")//<!--付款账号varchar(19) -->
			.append("<recvAccNo>"+outTransfer.getRecvAccNo()+"</recvAccNo>")//<!--收款账号varchar(19) -->
			.append("<recvAccNm>"+outTransfer.getRecvAccNm()+"</recvAccNm>")//<!--收款账户名称varchar(60) -->
			.append("<tranAmt>"+outTransfer.getTranAmt()+"</tranAmt>")//<!--交易金额decimal(15,2) -->
			.append("<sameBank>"+(outTransfer.getSameBank()==1?"0":"1")+"</sameBank>")//--中信标识char(1) 0：本行 1： 他行-- // 前台逻辑原因数字颠倒
			//--收款账户开户行信息begin--
			//--收款账户若为他行，则收款账户开户行支付联行号与收款账户开户行名至少一项不为空--
			.append("<recvTgfi>"+outTransfer.getRecvTgfi()+"</recvTgfi>")//--收款账户开户行支付联行号varchar(12) --
			.append("<recvBankNm>"+outTransfer.getRecvBankNm()+"</recvBankNm>")//--收款账户开户行名varchar (60) --
			//--收款账户开户行信息end-->
			.append("<memo>"+outTransfer.getMemo()+"</memo>")//<!--摘要varchar(22) 可空-->
			.append("<preFlg>"+outTransfer.getPreFlg()+"</preFlg>")//--预约标志（0：非预约1：预约）char(1) --
			.append("<preDate>"+outTransfer.getPreDate()+"</preDate>")//--预约日期（格式：YYYYMMDD 预约时非空）char(8)--
			.append("<preTime>"+outTransfer.getPreTime()+"</preTime>");//--预约时间（格式：hhmmss 预约时非空，只限100000、120000、140000、160000四个时间点）char(6)--
			
		String document = postRequest(buildSendXml(CiticMethodCode.Tenant_Out_Platform_Transfer, sb.toString(),financeAccountInfoDto.getBankLoginUsername()),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(OutTransferDto.class);	
		OutTransferDto outTransferDto=(OutTransferDto)xstream.fromXML(document);
		if(!(outTransferDto!=null&&outTransferDto.getStatus().contains("AAAAAA"))){
			throw new CommonCoreException("处理失败:"+outTransferDto.getStatus()+outTransferDto.getStatusText());
		}else{
			return outTransferDto;
		}
	}
	
	/**
	 * 验证账户信息有效性
	 * 
	 * @param accountInfoDto
	 * @return
	 */
	public static ExecuteResult<AccountInfoDto> vetifyAddAcc(AccountInfoDto accountInfoDto){
		ExecuteResult<AccountInfoDto> result =new  ExecuteResult<AccountInfoDto>();
		if(accountInfoDto!=null){
			if(accountInfoDto.getUserId()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "userId"));
			}else if(StringUtils.isBlank(accountInfoDto.getSubAccNm())){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "subAccNm"));
			}else if(accountInfoDto.getAccType()==null){
				result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "accountType"));
			}else if(StringUtils.isNotBlank(accountInfoDto.getBindingAccNo())){
				if(StringUtils.isBlank(accountInfoDto.getBindingAccNm())||accountInfoDto.getSameBank()==null
						||(StringUtils.isBlank(accountInfoDto.getBankName())&&StringUtils.isBlank(accountInfoDto.getBankBranchJointLine()))){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "当BindingAccNo存在：bindingAccNm,sameBank必须并存;bankName/bankBranchJointLine至少填一项."));
				}
				if(StringUtils.isNotBlank(accountInfoDto.getBankBranchJointLine())){
					if(accountInfoDto.getBankBranchJointLine().length()>12){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "bankBranchJointLine不得超过12位"));
					}
					if(StringUtils.isNumeric(accountInfoDto.getBankBranchJointLine())){
						result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "bankBranchJointLine非法账户"));
					}
				}
				if(StringUtils.isNotBlank(accountInfoDto.getBankName())&&accountInfoDto.getBankName().length()>50){
					result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Error, "bankName不得超过50位"));
				}
			}
		}else{
			result.addErrorMessage(MsgCodeEnum.info(MsgCodeEnum.Req_Param_Null, "对象不存在"));
		}
		return result;
	}
	
	public static CiticTradeOutDTO queryCiticTradeList(CiticTradeInDTO citicTradeInDTO,FinanceAccountInfoDto financeAccountInfoDto) throws IOException, DocumentException{
//		StringBuffer sb=new StringBuffer();
//		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n");
//		sb.append("<stream>")
//			.append(	"<action>"+CiticMethodCode.CiticTradeInfo_Query.getAction()+"</action>" )
//			.append(	"<userName>djzh</userName>" )
//			.append(	"<mainAccNo>7117510182600016825</mainAccNo>" )
//			.append(	"<subAccNo>1154515061032001096</subAccNo>" )
//			.append(	"<startDate>20150625</startDate>" )
//			.append(	"<endDate>20150625</endDate>" )
//			.append(	"<startRecord>"+citicTradeInDTO.getStartRecord()+"</startRecord>" )
//			.append(	"<pageNumber>"+citicTradeInDTO.getPageNumber()+"</pageNumber>" )
//			.append("</stream>");
//		String document = postRequest(sb.toString(),"http://220.194.55.237:6796");
//		XStream xstream = new XStream(new DomDriver());
//		xstream.processAnnotations(CiticTradeOutDTO.class);	
//		CiticTradeOutDTO citicTradeOutDTO=(CiticTradeOutDTO)xstream.fromXML(document);
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?>\n");
		sb.append("<stream>")
		.append(	"<action>"+CiticMethodCode.CiticTradeInfo_Query.getAction()+"</action>" )
		.append(	"<userName>"+financeAccountInfoDto.getBankLoginUsername()+"</userName>" )
		.append(	"<mainAccNo>"+financeAccountInfoDto.getMasterAccountNumber()+"</mainAccNo>" )
		.append(	"<subAccNo>"+citicTradeInDTO.getSubAccNo()+"</subAccNo>" )
		.append(	"<startDate>"+citicTradeInDTO.getStartDate()+"</startDate>" )
		.append(	"<endDate>"+citicTradeInDTO.getEndDate()+"</endDate>" )
		.append(	"<startRecord>"+citicTradeInDTO.getStartRecord()+"</startRecord>" )
		.append(	"<pageNumber>"+citicTradeInDTO.getPageNumber()+"</pageNumber>" )
		.append("</stream>");
		String document = postRequest(sb.toString(),financeAccountInfoDto.getCiticFrontEndProcessor());
		XStream xstream = new XStream(new DomDriver());
		xstream.processAnnotations(CiticTradeOutDTO.class);	
		CiticTradeOutDTO citicTradeOutDTO=(CiticTradeOutDTO)xstream.fromXML(document);
		return citicTradeOutDTO;
	}
}
