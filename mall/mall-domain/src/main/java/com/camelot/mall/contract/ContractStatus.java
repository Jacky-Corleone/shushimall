package com.camelot.mall.contract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王帅 on 2015/7/3.
 * 协议状态枚举类
 */
public enum ContractStatus {
	/**
	 * 合同状态 0：未提交 1：待审核  2：审核驳回3 ：待确认 4：确认驳回 5：待生效  6：协议生效 7：需要审批  8：协议生效（正在修改） 9：协议过期 10：协议终止
	 */
    NOTPUBLISH("未提交"), PENDINGAPPROVAL("待审批"), APPROVALREFUSE("审批驳回"), PENDINGCONFIRM("待确认"),
    CONFIRMREFUSE("确认驳回"), CONFIRMED("待生效"), PUBLISHED("协议生效"), NEEDAPPROVE("需要审批"), PBULISHED("协议生效(正在修改)"), OVERDUE("协议过期"),TERMINATION("协议终止");

    private String status;

    ContractStatus(String str) {
        this.status = str;
    }

    public String getStatus() {
        return this.status;
    }

    public static String valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal].status;
    }

    public static Map<String, String> valueMap() {
        Map<String, String> map = new HashMap<String, String>();
        ContractStatus[] a = ContractStatus.values();
        for (ContractStatus e : a) {
            map.put(String.valueOf(e.ordinal()), e.status);
        }
        return map;
    }

}
