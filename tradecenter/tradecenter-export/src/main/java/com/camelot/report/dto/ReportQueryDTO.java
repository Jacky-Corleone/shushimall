package com.camelot.report.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 报表查询DTO
 *
 * @author
 */
public class ReportQueryDTO implements Serializable {


    private static final long serialVersionUID = -5811358359272015412L;
    private String shopName; //店铺名称

    private Long shopId;  //店铺ID

    private String userName; //用户名

    private String companyName; //公司名称

    private String companyAddress; //公司地址

    private Date createBegin; //创建时间 开始

    private Date createEnd; //创建时间 结束

    private java.util.Date passTimeBegin;// 开通时间开始

    private java.util.Date passTimeEnd;// 开通时间开始
    public Date getPassTimeBegin() {
        return passTimeBegin;
    }

    public void setPassTimeBegin(Date passTimeBegin) {
        this.passTimeBegin = passTimeBegin;
    }

    public Date getPassTimeEnd() {
        return passTimeEnd;
    }

    public void setPassTimeEnd(Date passTimeEnd) {
        this.passTimeEnd = passTimeEnd;
    }



    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public Date getCreateBegin() {
        return createBegin;
    }

    public void setCreateBegin(Date createBegin) {
        this.createBegin = createBegin;
    }

    public Date getCreateEnd() {
        return createEnd;
    }

    public void setCreateEnd(Date createEnd) {
        this.createEnd = createEnd;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }


}

