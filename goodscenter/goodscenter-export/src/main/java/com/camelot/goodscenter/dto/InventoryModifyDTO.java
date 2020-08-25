package com.camelot.goodscenter.dto;

import java.io.Serializable;

/** 
 * <p>Description: [批量修改库存量的入参]</p>
 * Created on 2015年3月14日
 * @author  <a href="mailto: xxx@camelotchina.com">杨芳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class InventoryModifyDTO implements Serializable{
  
   private static final long serialVersionUID = 4562318562602731030L;

   private Long skuId;  // sku编码
   private Integer totalInventory;  //时间库存量
   
   
public Long getSkuId() {
	return skuId;
}
public void setSkuId(Long skuId) {
	this.skuId = skuId;
}
public Integer getTotalInventory() {
	return totalInventory;
}
public void setTotalInventory(Integer totalInventory) {
	this.totalInventory = totalInventory;
}
   
}
