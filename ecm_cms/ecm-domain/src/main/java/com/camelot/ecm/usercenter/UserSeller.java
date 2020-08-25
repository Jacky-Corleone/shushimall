package com.camelot.ecm.usercenter;

import com.camelot.common.ExcelField;

/**
 * Created by menpg on 2015/5/26.
 */
public class UserSeller {
    //序号
    private String num;
    //卖家编号
    private String uid;
    //商家类型
    private String userType="卖家";
    //公司名称
    private String companyName;
    //卖家状态
    private String userstatus;
    //商家地址
    private String address;
    //店铺name和id
    private String dpni;
    //经营品类
    private String pl;
    //店铺状态
    private String status;
    //开通时间
    private String passtime;
    @ExcelField(title="序号", align=2, sort=10)
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    @ExcelField(title="商家编号", align=2, sort=30)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @ExcelField(title="商家类型", align=2, sort=20)
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    @ExcelField(title="公司名称", align=2, sort=60)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    @ExcelField(title="卖家状态", align=2, sort=70)
    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }
    @ExcelField(title="公司地址", align=2, sort=100)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @ExcelField(title="店铺名称/编号", align=2, sort=40)
    public String getDpni() {
        return dpni;
    }

    public void setDpni(String dpni) {
        this.dpni = dpni;
    }
    @ExcelField(title="经营品类", align=2, sort=50)
    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }
    @ExcelField(title="店铺状态", align=2, sort=80)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    @ExcelField(title="店铺开通时间", align=2, sort=90)
    public String getPasstime() {
        return passtime;
    }

    public void setPasstime(String passtime) {
        this.passtime = passtime;
    }
}
