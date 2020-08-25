package com.camelot.ecm.settlement;

import com.camelot.settlecenter.dto.indto.SettlementInDTO;

/**
 * Created by sevelli on 15-3-20.
 */
public class SettlementQuery extends SettlementInDTO {
    private String fcname;//一级类目
    private Long fcid;//一级类目
    private String subcname;//二级类目
    private Long subcid;//二级类目
    private String cmane;//三级类目

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

    public String getCmane() {
        return cmane;
    }

    public void setCmane(String cmane) {
        this.cmane = cmane;
    }
}
