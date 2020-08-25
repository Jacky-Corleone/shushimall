package com.camelot.ecm.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.camelot.common.ExcelField;

public class BuyerReport implements Serializable{
	
	private String num;
	
	private String userName; //用户名

	private String umobile; // 电话
	
    private String createTime; //创建时间

    private String orderNum;//下单总数

    private String payPriceTotal;//商品价格总额

    private String shopNum; //购买过得店铺总数
    
    
    @ExcelField(title="序号", align=2, sort=5)
    public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@ExcelField(title="买家名称", align=2, sort=10)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@ExcelField(title="注册时间", align=2, sort=20)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@ExcelField(title="下单总数", align=2, sort=70)
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	@ExcelField(title="购买商品总额", align=2, sort=80)
	public String getPayPriceTotal() {
		return payPriceTotal;
	}

	public void setPayPriceTotal(String payPriceTotal) {
		this.payPriceTotal = payPriceTotal;
	}
	
	@ExcelField(title="联系方式", align=2, sort=90)
	public String getUmobile() {
		return umobile;
	}

	public void setUmobile(String umobile) {
		this.umobile = umobile;
	}

	@ExcelField(title="购买过的店铺总数", align=2, sort=100)
	public String getShopNum() {
		return shopNum;
	}

	public void setShopNum(String shopNum) {
		this.shopNum = shopNum;
	}
	
	
}
