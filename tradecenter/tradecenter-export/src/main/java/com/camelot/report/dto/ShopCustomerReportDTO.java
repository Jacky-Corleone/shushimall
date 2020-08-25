package com.camelot.report.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 店铺会员报表
 *
 * @author
 */
public class ShopCustomerReportDTO implements Serializable {


    private static final long serialVersionUID = -8097936869064528974L;

    private String userName; //用户名

    private Long userId; //用户ID

    private String nikeName; //昵称

    private String companyName; //公司名称

    private String address; //地址

    private Date createDt; //创建时间

    private Long orderNum;//下单总数

    private BigDecimal goodsPriceSum;//商品价格总额

    private String phone; //电话 联系方式

    private Long buyShopNum; //购买过得店铺总数

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getGoodsPriceSum() {
        return goodsPriceSum;
    }

    public void setGoodsPriceSum(BigDecimal goodsPriceSum) {
        this.goodsPriceSum = goodsPriceSum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getBuyShopNum() {
        return buyShopNum;
    }

    public void setBuyShopNum(Long buyShopNum) {
        this.buyShopNum = buyShopNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
}

