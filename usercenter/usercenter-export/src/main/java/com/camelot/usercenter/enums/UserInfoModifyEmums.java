package com.camelot.usercenter.enums;

public class UserInfoModifyEmums {
	/**
	 * 
	 * <p>Description: [用户修改信息 列 字段枚举]</p>
	 * Created on 2015-3-19
	 * @author  <a href="mailto: xxx@camelotchina.com">liuqingshan</a>
	 * @version 1.0 
	 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	 */
	public enum UserInfoModifyColumn {
		
		company_name("公司名称"), artificial_person_name("法定代表人名称"),business_licence_id("注册号（营业执照号）"), artificial_person_pic_src("法人身份证电子版图片"),artificial_person_pic_back_src("法人身份证电子版图片(反面)"),
		registered_capital("注册资金"),business_licence_pic_src("营业执照电子版图片"),business_scope("经营范围"),business_licence_indate("营业执照有效期"),linkman("联系人"),company_address("公司"),company_phone("公司电话"),
		organization_id("组织机构代码"),organization_pic_src("组织机构图片"),tax_man_id("税务人识别号"),taxpayer_type("纳税人类型"),taxpayer_code("税务人类型税码"),tax_registration_certificate_pic_src("税务登记证电子版图片"),
		taxpayer_certificate_pic_src("纳税人资格证电子版图片"),bank_account_name("银行开户名"),bank_account("银行账号"),bank_branch_joint_line("开户行支行联行号"),bank_name("开户行支行名称"),bank_branch_is_located("开户行支行所在地"),
		bank_account_permits_pic_src("银行开户许可证电子版图片"),seller_frozen_account("卖家冻结账户"),seller_withdraws_cash_account("卖家收款账户"),
        buyer_pays_account("买家支付账户"),buyer_financing_account("买家融资账户"),is_citic_bank("是否中信银行"),company_scale("公司规模"),service_type("业务类型"),delivery_address("收货"),invoice_information("发票信息")
        ,company_declined_address("公司详细"),company_peo_num("公司人数规模"),company_qualt("公司性质"),business_scale("经营规模"),
        is_financing("是否融资"),financing_num("融资金额"),is_sanzheng("是否三证合一"),unified_credit_code("统一社会信用代码"),id_card_type("证件类型"),id_card_num("证件号码"),business_licence_address("营业执照所在地(省市县)")
        ,business_licenc_address_detail("营业执照详细"),business_licence_date("营业执照创建时间"),linkman_phone("联系人手机"),userful_time("组织机构代码有效期")
        ,tax_type("纳税类型"),dealer_type("经营类型"),home_page("公司网站"),sale_num("销售额"),is_have_ebusiness("是否有电子商务经验"),erp_type("erp类型"),web_opera_peo("网站运营人数")
        ,gp_product_certification("绿色印刷环境标志产品认证证书"),gp_print_business_license("印刷经营许可证"),gp_publication_license("出版物印制许可证（书刊印刷定点企业证书）")
        ,gp_commitment_book("承诺书"),gp_quality_management_certification("ISO9001质量管理体系认证证书"),gp_environmental_management_certification("ISO14001环境管理体系认证证书")
        ;



		
		private String label;
		UserInfoModifyColumn( String label) {
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
		
		
	}
}
