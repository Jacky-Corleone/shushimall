package com.camelot.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 投诉
 *
 * @author
 */
public class ShopReportDTO implements Serializable {

    private static final long serialVersionUID = 2L;
    
    private Long shopId;// 店铺id
    private String shopName;// 店铺名称
    private Long sellerId;// 卖家id
    private java.util.Date passTime;// 开通时间
    private String provinceCode;// 省份代码
    private String provinceName;// 省份名字
    private String zcode;// 邮政编码
    private String streetName;// 街道名字
    private Long mobile;// 手机号码
    
    private BigDecimal orderNum; //订单数量
    private BigDecimal saleNum;  //销量
    private BigDecimal customerNum; //客户数
    
    private String passTimeStart;	//店铺开通开始日期  yyyyMMdd	(查询时用)
    private String passTimeEnd;	//店铺开通结束日期  yyyyMMdd	(查询时用)
    
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getSellerId() {
		return sellerId;
	}
	public void setSellerId(Long sellerId) {
		this.sellerId = sellerId;
	}
	public java.util.Date getPassTime() {
		return passTime;
	}
	public void setPassTime(java.util.Date passTime) {
		this.passTime = passTime;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getZcode() {
		return zcode;
	}
	public void setZcode(String zcode) {
		this.zcode = zcode;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public Long getMobile() {
		return mobile;
	}
	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}
	public BigDecimal getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(BigDecimal orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getSaleNum() {
		return saleNum;
	}
	public void setSaleNum(BigDecimal saleNum) {
		this.saleNum = saleNum;
	}
	public BigDecimal getCustomerNum() {
		return customerNum;
	}
	public void setCustomerNum(BigDecimal customerNum) {
		this.customerNum = customerNum;
	}
	public String getPassTimeStart() {
		return passTimeStart;
	}
	public void setPassTimeStart(String passTimeStart) {
		this.passTimeStart = passTimeStart;
	}
	public String getPassTimeEnd() {
		return passTimeEnd;
	}
	public void setPassTimeEnd(String passTimeEnd) {
		this.passTimeEnd = passTimeEnd;
	}
	

}

