package com.camelot.storecenter.dto.emums;

public class ShopModify {
	
	
	/**
	 * 
	 * <p>Description: [店铺经营类目状态]</p>
	 * Created on 2015-3-11
	 * @author  yuht
	 * @version 1.0 
	 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	 */
	public enum ShopModifyColumn {
		
		shop_name("店铺名称"), logo_url("店铺logo"),introduce("店铺简介"),main_sell("店铺主营"),price_min("混批金额要求"),mount_min("批混数量要求"),
		mutilPrice("是否混批"),mutil_condition("混批条件"),initial_price("起批价格"),initial_mount("起批数量"),initial_condition("起批条件"),city_code("城市"),
		city_name("城市名称"),district_code("区编码"),district_name("区名称"),province_code("省编码"),province_name("省名称"),zcode("邮政编码"),street_name("街道名字"),
		shop_type("店铺类型"),brand_type("品牌类型"),business_type("经营类型"),disclaimer("免责声明"),
		trademark_regist_cert("商标注册证/商品注册申请书扫描件"),inspection_report("质检、检疫、检验报告/报关单类扫描件"),
		production_license("卫生/生产许可证扫描件"),marketing_auth("销售授权书扫描件"),financing("是否有融资需求"),financing_amt("融资金额"),
		gp_commitment_book("承诺书");

		
		private String label;
		ShopModifyColumn( String label) {
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
		
		
	}

}
