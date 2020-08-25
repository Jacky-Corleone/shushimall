package com.camelot.mall.contract;

import java.io.Serializable;

/**
 * Created by 王帅 on 2015/7/3.
 */
public class ContractSearchModel implements Serializable {

    private String contractName;
    private String itemName;
    private String companyName;

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
