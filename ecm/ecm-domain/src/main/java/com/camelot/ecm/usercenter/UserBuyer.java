package com.camelot.ecm.usercenter;

import com.camelot.common.ExcelField;

/**
 * Created by menpg on 2015/5/26.
 */
public class UserBuyer {
    //序号
    private String num;
    //用户类型
    private String userType="买家";
    //用户编号
    private String uid;
    //公司名称
    private String companyName;
    //创建日期
    private String created;
    //地址
    private String companyAddr;
    //买家状态
    private String userStace;
    @ExcelField(title="序号", align=2, sort=10)
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    @ExcelField(title="类型", align=2, sort=20)
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    @ExcelField(title="编号号", align=2, sort=30)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @ExcelField(title="公司名称", align=2, sort=40)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    @ExcelField(title="开通时间", align=2, sort=50)
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    @ExcelField(title="公司地址", align=2, sort=60)
    public String getCompanyAddr() {
        return companyAddr;
    }

    public void setCompanyAddr(String companyAddr) {
        this.companyAddr = companyAddr;
    }
    @ExcelField(title="买家状态", align=2, sort=70)
    public String getUserStace() {
        return userStace;
    }

    public void setUserStace(String userStace) {
        this.userStace = userStace;
    }
}
