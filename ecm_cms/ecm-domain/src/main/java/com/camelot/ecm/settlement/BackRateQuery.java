package com.camelot.ecm.settlement;

import com.camelot.settlecenter.dto.SettleCatExpenseDTO;

public class BackRateQuery extends SettleCatExpenseDTO {
    private String fcname;//一级类目
    private Long fcid;//一级类目
    private String subcname;//二级类目
    private Long subcid;//二级类目
    private String tcmane;//三级类目
    private Long tcid;//三级类目

    public String getFcname() {
        return fcname;
    }

    public void setFcname(String fcname) {
        this.fcname = fcname;
    }

    public Long getFcid() {
        return fcid;
    }

    public void setFcid(Long fcid) {
        this.fcid = fcid;
    }

    public String getSubcname() {
        return subcname;
    }

    public void setSubcname(String subcname) {
        this.subcname = subcname;
    }

    public Long getSubcid() {
        return subcid;
    }

    public void setSubcid(Long subcid) {
        this.subcid = subcid;
    }

    public String getTcmane() {
        return tcmane;
    }

    public void setTcmane(String tcmane) {
        this.tcmane = tcmane;
    }

    public Long getTcid() {
        return tcid;
    }

    public void setTcid(Long tcid) {
        this.tcid = tcid;
    }
}
