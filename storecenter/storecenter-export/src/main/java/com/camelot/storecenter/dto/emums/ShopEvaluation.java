package com.camelot.storecenter.dto.emums;



public class ShopEvaluation {
	
	
	/**
	 * 
	 * <p>Description: [店铺统计类型]</p>
	 * Created on 2015-3-11
	 * @author  yuht
	 * @version 1.0 
	 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
	 */
	public enum ShopEvaluationTypeColumn {
		all("all"), 
		shop_description("shopDescription"),
		shop_service("shopService"),
		shop_arrival("shopArrival");
		
		private String label;
		ShopEvaluationTypeColumn( String label) {
			this.label = label;
		}
		public String getLabel() {
			return label;
		}
	}
}
