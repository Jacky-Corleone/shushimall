package com.camelot.common.enums;

public class CiticEnums {
	
	/**
	 *  中信附属账户状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum AccStatus{
		UnCreate(-1,"待生成"),UnAudit(1,"待审核"),AuditPass(0,"审核通过"),
		AuditFail(3,"审核拒绝"),cancel(2,"注销");
		
		private Integer citicCode;// 中信接口设定的编码
		private String label;
		AccStatus(Integer citicCode,String label){
			this.citicCode=citicCode;
			this.label=label;
		}
		
		public static AccStatus getEnumByCiticCode(Integer citicCode) {
			for (AccStatus accStatus:AccStatus.values()) {
				if(accStatus.getCiticCode().equals(citicCode)){
					return accStatus;
				}
			}
			return null;
		}
		
		public Integer getCiticCode() {
			return citicCode;
		}

		public String getLabel() {
			return label;
		}
	}
	/**
	 *  附属账户类型
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum AccountType{
		AccBuyPay(21,"买家支付账户"),AccBuyFinancing(22,"买家融资账户"),AccSellReceipt(31,"卖家收款账户"),AccSellFreeze(32,"卖家冻结账户");
		private int code;
		private String label;
		AccountType(int code,String label){
			this.code=code;
			this.label=label;
		}
		
		public static AccountType getEnumByCode(int code){
			for (AccountType accountType:AccountType.values()) {
				if(accountType.getCode()==code){
					return accountType;
				}
			}
			return null;
		}
		
		public int getCode() {
			return code;
		}
		public String getLabel() {
			return label;
		}
	}
	
	/**
	 *  用户类型类型
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-13
	 */
	public enum UserType{
		Buyer(2,"买家"),Seller(3,"卖家");
		private int code;
		private String label;
		UserType(int code,String label){
			this.code=code;
			this.label=label;
		}
		public int getCode() {
			return code;
		}
		public String getLabel() {
			return label;
		}
	}
	
	/**
	 *  中信支付记录交易状态
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-4-09
	 */
	public enum CiticPayStatus{
		PayInitialize(0,"初始化"),PayFail(1,"交易失败"),PaySuccess(2,"交易成功");
		private int code;
		private String label;
		
		CiticPayStatus(int code,String label){
			this.code=code;
			this.label=label;
		}
		public int getCode() {
			return code;
		}
		public String getLabel() {
			return label;
		}
	}
	
	/**
	 *  中信调用接口代码
	 * 
	 * @Description -
	 * @author - learrings
	 * @createDate - 2015-3-18
	 */
	public enum CiticMethodCode{
		
		MainAccount_Query("DLBALQRY","主体账户查询"),
		
		AffiliatedAccount_add("DLBREGST","附属账户预签约"),
		AffiliatedAccountStatus_Query("DLSASQRY","附属账户签约状态查询"),
		AffiliatedAccount_Query("DLSBALQR","附属账户余额查询"),
		Platform_Tenant_Transfer("DLSUBTRN","平台账号向附属账户资金划转"),
		TenantAccount_Transfer("DLMDETRN","附属账户间转账,除公共计息收费附属账户、公共调账附属账户外"),
		Tenant_Trade_Query("DLCIDSTT","附属账户间交易查询"),
//		Tenant_OutTransfer("DLFNDOUT","附属账户出金"),
		Tenant_Out_Platform_Transfer("DLFCSOUT","附属账户平台出金"),
		TenantAccount_Trade_Detail_Query("DLSTRNDT","附属账户交易明细查询"),
		Tenant_InTransfer("DLFONDIN","附属账户充值"),
		CiticTradeInfo_Query("DLPTDTQY","非登录打印查询");
		
		private String action;
		private String label;
		CiticMethodCode(String action,String label){
			this.action=action;
			this.label=label;
		}
		public String getAction() {
			return action;
		}
		public String getLabel() {
			return label;
		}
	}
	
	/**
	 *  中信银行交互支付方式
	 */
	public enum CiticPayTypeCode{
		pay(0,"支付"),settle(1,"结算"),withdraw(2,"提现"),refund(3,"退款"),factorage(4,"手续费差异");
		private int code;
		private String label;
		CiticPayTypeCode(int code,String label){
			this.code=code;
			this.label=label;
		}
		
		public static CiticPayTypeCode getEnumByCode(int code) {
			for (CiticPayTypeCode citicPayType:CiticPayTypeCode.values()) {
				if(citicPayType.getCode()==code){
					return citicPayType;
				}
			}
			return null;
		}
		
		public int getCode() {
			return code;
		}
		public String getLabel() {
			return label;
		}
	}
}
