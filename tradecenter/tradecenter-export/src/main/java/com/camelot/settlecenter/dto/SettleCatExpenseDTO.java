package com.camelot.settlecenter.dto;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * <p>Description: [类目费用]</p>
 * Created on 2015-3-9
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SettleCatExpenseDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  主键
		private java.lang.Long categoryId;//  类目id
		private BigDecimal rebateRate;//  返点率
		private BigDecimal serviceFee;//  技术服务费
		private BigDecimal cashDeposit;//  保证金
		private java.util.Date created;//  创建时间
		private java.util.Date modified;//  更新时间
		private String childCategorys; //子集的集合 以"，"连接

		
		public java.lang.Long getId() {
			return id;
		}
		public void setId(java.lang.Long id) {
			this.id = id;
		}
		public java.lang.Long getCategoryId() {
			return categoryId;
		}
		public void setCategoryId(java.lang.Long categoryId) {
			this.categoryId = categoryId;
		}
		public BigDecimal getRebateRate() {
			return rebateRate;
		}
		public void setRebateRate(BigDecimal rebateRate) {
			this.rebateRate = rebateRate;
		}
		public BigDecimal getServiceFee() {
			return serviceFee;
		}
		public void setServiceFee(BigDecimal serviceFee) {
			this.serviceFee = serviceFee;
		}
		public BigDecimal getCashDeposit() {
			return cashDeposit;
		}
		public void setCashDeposit(BigDecimal cashDeposit) {
			this.cashDeposit = cashDeposit;
		}
		public java.util.Date getCreated() {
			return created;
		}
		public void setCreated(java.util.Date created) {
			this.created = created;
		}
		public java.util.Date getModified() {
			return modified;
		}
		public void setModified(java.util.Date modified) {
			this.modified = modified;
		}
		public String getChildCategorys() {
			return childCategorys;
		}
		public void setChildCategorys(String childCategorys) {
			this.childCategorys = childCategorys;
		}
	
}

