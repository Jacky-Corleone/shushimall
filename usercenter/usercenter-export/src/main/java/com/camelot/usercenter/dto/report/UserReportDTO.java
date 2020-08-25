package com.camelot.usercenter.dto.report;

import java.io.Serializable;
import java.util.Date;

/**
 *

 */
public class UserReportDTO implements Serializable {


    private static final long serialVersionUID = 5736334661578885967L;

    private Long createUserNum ; //每日新增用户数量

    private Date createDt; //创建时间

    public Long getCreateUserNum() {
        return createUserNum;
    }

    public void setCreateUserNum(Long createUserNum) {
        this.createUserNum = createUserNum;
    }

    public Date getCreateDt() {
        return createDt;
    }

    public void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }
}
