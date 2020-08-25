package com.camelot.mall.contract.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractMatDTO;
import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.mall.contract.ContractService;
import com.camelot.mall.contract.ContractStatus;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/**
 * Created by 王帅 on 2015/7/3.
 */
@Service
public class ContractServiceImpl implements ContractService {
    @Resource
    private UserExportService userExportService;
    @Resource
    private ProtocolExportService protocolExportService;
    @Resource
    private ItemExportService itemService;

    @Override
    public Map<String, String> pPUser(List<ContractInfoDTO> contracts,
                                      boolean ifseller, boolean ifbuyer) {
        // TODO Auto-generated method stub
        Map<String, String> map = new HashMap<String, String>();
        if (contracts != null && contracts.size() > 0) {
            Map<Long, String> map1 = new HashMap<Long, String>();
            Iterator<ContractInfoDTO> iterator = contracts.iterator();
            while (iterator.hasNext()) {
                ContractInfoDTO contractInfoDTO = iterator.next();
                map1.put(Long.parseLong(contractInfoDTO.getCreateBy()), "1");
                if (ifseller) {
                    if (contractInfoDTO.getSupplierId() != null) {
                        map1.put(contractInfoDTO.getSupplierId().longValue(), "1");
                    }
                }
                if (ifbuyer) {
                    if (contractInfoDTO.getPrinterId() != null) {
                        map1.put(contractInfoDTO.getPrinterId().longValue(), "1");
                    }
                }
            }
            if (map1.size() > 0) {
                Iterator<Long> iteratorids = map1.keySet().iterator();
                List<String> ids = new ArrayList<String>();
                //根据ids查询用户
                while (iteratorids.hasNext()) {
                    ids.add(new BigDecimal(iteratorids.next()).toPlainString());
                }
                //UserDTO userDTO=new UserDTO();
                //DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO, null,null);
                ExecuteResult<List<UserDTO>> executeResult = userExportService.findUserListByUserIds(ids);
                List<UserDTO> list = executeResult.getResult();
                if (list != null && list.size() > 0) {
                    Iterator<UserDTO> iteratordto = list.iterator();
                    while (iteratordto.hasNext()) {
                        UserDTO userDTO = iteratordto.next();
                        map.put(userDTO.getUid().toString(), userDTO.getUname());
                    }
                }
            }
        }
        return map;
    }

    public Map<String, String> cPUser(List<ContractInfoDTO> contracts) {
        Map<String, String> map = new HashMap<String, String>();
        if (contracts != null && contracts.size() > 0) {
            Map<Long, String> map1 = new HashMap<Long, String>();
            Iterator<ContractInfoDTO> iterator = contracts.iterator();
            while (iterator.hasNext()) {
                ContractInfoDTO contractInfoDTO = iterator.next();
                if (contractInfoDTO.getCreateBy() != null) {
                    map1.put(Long.valueOf(contractInfoDTO.getCreateBy()), "1");
                }
                if (contractInfoDTO.getSupplierId() != null) {
                    map1.put(contractInfoDTO.getSupplierId().longValue(), "1");
                }
                if (contractInfoDTO.getPrinterId() != null) {
                    map1.put(contractInfoDTO.getPrinterId().longValue(), "1");
                }
            }
            if (map1.size() > 0) {
                Iterator<Long> iteratorids = map1.keySet().iterator();
                List<String> ids = new ArrayList<String>();
                //根据ids查询用户
                while (iteratorids.hasNext()) {
                    ids.add(new BigDecimal(iteratorids.next()).toPlainString());
                }
                //UserDTO userDTO=new UserDTO();
                //DataGrid<UserDTO> userDTODataGrid1=userExportService.findUserListByCondition(userDTO, null,null);
                ExecuteResult<List<UserDTO>> executeResult = userExportService.findUserListByUserIds(ids);
                List<UserDTO> list = executeResult.getResult();
                if (list != null && list.size() > 0) {
                    Iterator<UserDTO> iteratordto = list.iterator();
                    while (iteratordto.hasNext()) {
                        UserDTO userDTO = iteratordto.next();
                        map.put(userDTO.getUid().toString(), userDTO.getCompanyName());
                    }
                }
            }
        }
        return map;
    }

    /**
     * 根据传入的协议DTO查询该协议下的物品及账期等信息,然后将以上数据进行拼装并返回
     * @param contractInfoDTO
     * @return
     */
    @Override
    public List<ContractInfoDTO> assembleContracInfos(ContractInfoDTO contractInfoDTO) {
        Pager<ContractInfoDTO> pager = new Pager<ContractInfoDTO>();
        pager.setRows(Integer.MAX_VALUE);
        ContractMatDTO contractMatDTO = new ContractMatDTO();
        List<ContractInfoDTO> results = protocolExportService
                .queryContractInfoList(contractInfoDTO, pager).getResult().getRows();
        for (ContractInfoDTO con : results) {

            contractMatDTO.setContractNo(con.getContractNo());
            contractMatDTO.setActiveFlag("1");
            con.setContractMatDTOs(protocolExportService.queryContractMatList(contractMatDTO, pager).getResult().getRows());
            for (Map e : con.getContractMatDTOs()) {
                putSalerAttr(e);
            }
        }
        return results;
    }

    /**
     * 向Map中添加销售属性字段
     * @param contractMats
     */

    @Override
    public void putSalerAttr(Map contractMats) {
        ItemShopCartDTO dto = new ItemShopCartDTO();
        dto.setAreaCode("0");
        dto.setShopId(Long.valueOf(contractMats.get("shopId").toString()));
        dto.setSkuId(Long.valueOf(contractMats.get("skuId").toString()));
        dto.setItemId(Long.valueOf(contractMats.get("itemId").toString()));
        ExecuteResult<ItemShopCartDTO> er = itemService.getSkuShopCart(dto); //调商品接口查url
        ItemShopCartDTO itemShopCartDTO = er.getResult();
        if (null != itemShopCartDTO) {
            contractMats.put("skuPicUrl", itemShopCartDTO.getSkuPicUrl());
            //商品属性
            String skuName = "";
            for (ItemAttr itemAttr : itemShopCartDTO.getAttrSales()) {
                skuName += itemAttr.getName();
                for (ItemAttrValue itemAttrValue : itemAttr.getValues()) {
                    skuName += ":" + itemAttrValue.getName() + ";";
                }
            }
            contractMats.put("salerAttr", skuName);

        }
    }

    @Override
    public void addContractInfos(ContractInfoDTO infoDTO, List<ContractMatDTO> matDTOs, ContractPaymentTermDTO paymentTermDTO, RegisterDTO registerDTO) {
        //执行创建协议动作
        protocolExportService.addContractInfo(infoDTO);
        protocolExportService.addContractPaymentTerm(paymentTermDTO);
        for (ContractMatDTO matDTO : matDTOs) {
            matDTO.setContractNo(infoDTO.getContractNo());
            matDTO.setCreateBy(registerDTO.getUid().toString());
            matDTO.setActiveFlag("1");
            protocolExportService.addContractMat(matDTO);
        }
    }

    @Override
    public void updateContractInfos(ContractInfoDTO infoDTO, List<ContractMatDTO> matDTOs, ContractPaymentTermDTO paymentTermDTO,
                                    RegisterDTO registerDTO, String needApprove, List<Long> removeMatIds) {

        //执行修改协议动作,页面点选了需要审批,则将协议状态改为需要审批状态,否则改为未提交状态
        if (StringUtils.isNotEmpty(infoDTO.getApproveBy())) {
            infoDTO.setStatus(String.valueOf(ContractStatus.NEEDAPPROVE.ordinal()));
        } else {
            infoDTO.setStatus(String.valueOf(ContractStatus.NOTPUBLISH.ordinal()));
            infoDTO.setApproveBy("");
        }
        //状态6为协议生效状态，当协议生效状态下进行修改时，要新创建一条协议，同时不对原协议进行修改
        if ("6".equals(infoDTO.getStatus())) {
            infoDTO.setContractNo(protocolExportService.createContractNo().getResult());
            infoDTO.setCreateBy(registerDTO.getUid().toString());
            infoDTO.setCreateDate(new Date());
            ContractInfoDTO temp = new ContractInfoDTO();
            temp.setId(infoDTO.getId());
            temp.setActiveFlag("1");
            ContractInfoDTO con = protocolExportService.queryByContractInfo(temp).getResult();
            if (null != con) {
                con.setStatus("8");
                con.setUpdatedContractNo(infoDTO.getContractNo());
            }

            protocolExportService.addContractInfo(infoDTO);
        } else {
            protocolExportService.modifyContractInfo(infoDTO);
        }
        //设置查询dto进行协议几项查询
        paymentTermDTO.setContractNo(infoDTO.getContractNo());
        ExecuteResult<ContractPaymentTermDTO> tempTermDTO = protocolExportService.queryByContractPaymentTerm(paymentTermDTO.getContractNo());
        //如果查询结果为空,则创建,不为空则修改
        if (null == tempTermDTO.getResult()) {
            paymentTermDTO.setCreateBy(registerDTO.getUid().toString());
            paymentTermDTO.setCreateDate(new Date());
            protocolExportService.addContractPaymentTerm(paymentTermDTO);
        } else {
            protocolExportService.modifyContractPaymentTerm(paymentTermDTO);
        }
        //前台修改协议时如果做了替换物品的操作,检查物品移除列表是否为空
        if (null != removeMatIds && removeMatIds.size() > 0) {
            ContractMatDTO temp = new ContractMatDTO();
            Pager<ContractMatDTO> pager = new Pager<ContractMatDTO>();
            pager.setRows(Integer.MAX_VALUE);
            temp.setContractNo(infoDTO.getContractNo());

            temp.setActiveFlag("1");
            ExecuteResult<DataGrid<Map>> tempMats = protocolExportService.queryContractMatList(temp, pager);
            //不为空则删除物品表中对应的物品
            if (null != tempMats.getResult().getRows() && tempMats.getResult().getRows().size() > 0) {
                for (Long e : removeMatIds) {
                    temp.setId(e);
                    ContractMatDTO result = protocolExportService.queryByContractMat(temp).getResult();
                    result.setActiveFlag("0");
                    protocolExportService.modifyContractMat(result);
                }
            }
        }
        //修改物品信息或者有新增物品则添加
        for (ContractMatDTO matDTO : matDTOs) {
            matDTO.setContractNo(infoDTO.getContractNo());
            matDTO.setActiveFlag("1");
            ContractMatDTO temp = protocolExportService.queryByContractMat(matDTO).getResult();
            if (null == temp) {
                matDTO.setId(null);
                matDTO.setCreateBy(registerDTO.getUid().toString());
                matDTO.setCreateDate(new Date());
                matDTO.setActiveFlag("1");
                protocolExportService.addContractMat(matDTO);
            } else {
                matDTO.setUpdateBy(registerDTO.getUid().toString());
                matDTO.setCreateDate(new Date());
                protocolExportService.modifyContractMat(matDTO);
            }


        }
    }


}
