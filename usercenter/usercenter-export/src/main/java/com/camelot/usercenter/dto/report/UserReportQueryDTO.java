package com.camelot.usercenter.dto.report;

import java.io.Serializable;
import java.util.Date;

/**
 *

 */
public class UserReportQueryDTO implements Serializable {


    private static final long serialVersionUID = -7097448653965731869L;

    private Date createBeginDt; //创建开始

    private Date createEndDt ; // 创建结束时间

    public Date getCreateBeginDt() {
        return createBeginDt;
    }

    public void setCreateBeginDt(Date createBeginDt) {
        this.createBeginDt = createBeginDt;
    }

    public Date getCreateEndDt() {
        return createEndDt;
    }

    public void setCreateEndDt(Date createEndDt) {
        this.createEndDt = createEndDt;
    }
}
