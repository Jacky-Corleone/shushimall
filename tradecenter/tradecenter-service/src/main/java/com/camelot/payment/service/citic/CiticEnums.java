package com.camelot.payment.service.citic;
	
public class CiticEnums{

//	/**
//	 *  中信调用接口代码
//	 * 
//	 * @Description -
//	 * @author - learrings
//	 * @createDate - 2015-3-18
//	 */
//	public enum CiticMethodCode{
//		
//		MainAccount_Query("DLBALQRY","主体账户查询"),
//		
//		AffiliatedAccount_add("DLBREGST","附属账户预签约"),
//		AffiliatedAccountStatus_Query("DLSASQRY","附属账户签约状态查询"),
//		AffiliatedAccount_Query("DLSBALQR","附属账户余额查询"),
//		Platform_Tenant_Transfer("DLSUBTRN","平台账号向附属账户资金划转"),
//		TenantAccount_Transfer("DLMDETRN","附属账户间转账,除公共计息收费附属账户、公共调账附属账户外"),
//		Tenant_Trade_Query("DLCIDSTT","附属账户间交易查询"),
////		Tenant_OutTransfer("DLFNDOUT","附属账户出金"),
//		Tenant_Out_Platform_Transfer("DLFCSOUT","附属账户平台出金"),
//		TenantAccount_Trade_Detail_Query("DLSTRNDT","附属账户交易明细查询"),
//		Tenant_InTransfer("DLFONDIN","附属账户充值");
//		
//		private String action;
//		private String label;
//		CiticMethodCode(String action,String label){
//			this.action=action;
//			this.label=label;
//		}
//		public String getAction() {
//			return action;
//		}
//		public String getLabel() {
//			return label;
//		}
//	}
//	
//	/**
//	 *  中信银行交互支付方式
//	 */
//	public enum CiticPayTypeCode{
//		pay(0,"支付"),settle(1,"结算"),withdraw(2,"提现"),refund(3,"退款"),factorage(4,"手续费差异");
//		private int code;
//		private String label;
//		CiticPayTypeCode(int code,String label){
//			this.code=code;
//			this.label=label;
//		}
//		
//		public static CiticPayTypeCode getEnumByCode(int code) {
//			for (CiticPayTypeCode citicPayType:CiticPayTypeCode.values()) {
//				if(citicPayType.getCode()==code){
//					return citicPayType;
//				}
//			}
//			return null;
//		}
//		
//		public int getCode() {
//			return code;
//		}
//		public String getLabel() {
//			return label;
//		}
//	}
}