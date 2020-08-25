package com.camelot.ecm.usercenter;

import com.camelot.common.ExcelField;

/**
 * Created by menpg on 2015/5/28.
 */
public class UserAll {
    private String num;
    private String uid;
    private String uname;
    private String nickname;
    private String userstatusname;
    private String umobile;
    private String userEmail;
    private String companyName;
    private String created;
    private String quickType;
    @ExcelField(title="序号", align=2, sort=10)
    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
    @ExcelField(title="用户编号", align=2, sort=20)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    @ExcelField(title="用户名", align=2, sort=30)
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
    @ExcelField(title="昵称", align=2, sort=40)
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    @ExcelField(title="用户状态", align=2, sort=50)
    public String getUserstatusname() {
        return userstatusname;
    }

    public void setUserstatusname(String userstatusname) {
        this.userstatusname = userstatusname;
    }
    @ExcelField(title="手机号码", align=2, sort=60)
    public String getUmobile() {
        return umobile;
    }

    public void setUmobile(String umobile) {
        this.umobile = umobile;
    }
    @ExcelField(title="邮箱", align=2, sort=70)
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    @ExcelField(title="公司名称", align=2, sort=80)
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    @ExcelField(title="注册时间", align=2, sort=90)
    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    @ExcelField(title="注册类型", align=2, sort=100)
    public String getQuickType() {
        return quickType;
    }

    public void setQuickType(String quickType) {
        this.quickType = quickType;
    }
}
