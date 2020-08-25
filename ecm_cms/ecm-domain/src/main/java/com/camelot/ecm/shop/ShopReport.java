package com.camelot.ecm.shop;

import java.io.Serializable;
import java.math.BigDecimal;

import com.camelot.common.ExcelField;

public class ShopReport implements Serializable{
	private static final long serialVersionUID = 1L;
	//序号
    private String num;
    private String shopName;
    private String passTime;
    private String orderNum; 
    private String saleNum;  
    private String phoneNum ;
    private String customerNum;
    
    @ExcelField(title="序号", align=2, sort=10)
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	@ExcelField(title="店铺名称", align=2, sort=20)
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	@ExcelField(title="开店时间", align=3, sort=30)
	public String getPassTime() {
		return passTime;
	}
	public void setPassTime(String passTime) {
		this.passTime = passTime;
	}	@ExcelField(title="订单总数", align=2, sort=40)
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
		
	@ExcelField(title="销量", align=2, sort=50)
	public String getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(String saleNum) {
		this.saleNum = saleNum;
	}
	@ExcelField(title="联系方式", align=2, sort=60)
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	@ExcelField(title="客户数", align=2, sort=70)
	public String getCustomerNum() {
		return customerNum;
	}
	public void setCustomerNum(String customerNum) {
		this.customerNum = customerNum;
	}
    
}
