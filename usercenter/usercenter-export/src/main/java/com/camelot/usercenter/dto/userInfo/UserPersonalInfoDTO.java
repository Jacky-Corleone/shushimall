package com.camelot.usercenter.dto.userInfo;

import java.io.Serializable;

/**
 * <p>信息VO（展示）</p>
 *
 * @author
 * @createDate
 */
public class UserPersonalInfoDTO implements Serializable {
    private static final long serialVersionUID = -2650666796320741325L;
    private Long id;//  id
    private Long userId;//  userId
    private String nikeName;//  昵称
    private String sex;//  性别
    private String homePage;//  主页
    private String birthday;//  生日
    private String blood;//  血型
    private String origin;//  籍贯
    private String address;//  详细地址
    private String income;//  收入
    private String hobby;//  兴趣爱好
    private String evaluate;//  自我评价
    private java.util.Date createDt;//  createDt
    private java.util.Date lastUpdDt;//  lastUpdDt
    private String deletedFlag;//  deletedFlag
    private String add2;//  add2
    private String add1;//  add1
    private String add3;//  add3


    public Long getId() {
        return this.id;
    }

    public void setId(Long value) {
        this.id = value;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long value) {
        this.userId = value;
    }

    public String getNikeName() {
        return this.nikeName;
    }

    public void setNikeName(String value) {
        this.nikeName = value;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String value) {
        this.sex = value;
    }

    public String getHomePage() {
        return this.homePage;
    }

    public void setHomePage(String value) {
        this.homePage = value;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String value) {
        this.birthday = value;
    }

    public String getBlood() {
        return this.blood;
    }

    public void setBlood(String value) {
        this.blood = value;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String value) {
        this.origin = value;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String value) {
        this.address = value;
    }

    public String getIncome() {
        return this.income;
    }

    public void setIncome(String value) {
        this.income = value;
    }

    public String getHobby() {
        return this.hobby;
    }

    public void setHobby(String value) {
        this.hobby = value;
    }

    public String getEvaluate() {
        return this.evaluate;
    }

    public void setEvaluate(String value) {
        this.evaluate = value;
    }

    public java.util.Date getCreateDt() {
        return this.createDt;
    }

    public void setCreateDt(java.util.Date value) {
        this.createDt = value;
    }

    public java.util.Date getLastUpdDt() {
        return this.lastUpdDt;
    }

    public void setLastUpdDt(java.util.Date value) {
        this.lastUpdDt = value;
    }

    public String getDeletedFlag() {
        return this.deletedFlag;
    }

    public void setDeletedFlag(String value) {
        this.deletedFlag = value;
    }

    public String getAdd2() {
        return this.add2;
    }

    public void setAdd2(String value) {
        this.add2 = value;
    }

    public String getAdd1() {
        return this.add1;
    }

    public void setAdd1(String value) {
        this.add1 = value;
    }

    public String getAdd3() {
        return this.add3;
    }

    public void setAdd3(String value) {
        this.add3 = value;
    }
}

