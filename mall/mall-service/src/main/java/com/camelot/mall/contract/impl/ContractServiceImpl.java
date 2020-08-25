package com.camelot.mall.contract.impl;

import com.camelot.goodscenter.dto.*;
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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

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
                Pager<UserDTO> pager = new Pager<UserDTO>();
                pager.setRows(Integer.MAX_VALUE);
                DataGrid<UserDTO> executeResult = userExportService.queryUserListByCondition(new UserDTO(), null, ids, pager);

                List<UserDTO> list = executeResult.getRows();
                if (list != null && list.size() > 0) {
                    Iterator<UserDTO> iteratordto = list.iterator();
                    while (iteratordto.hasNext()) {
                        UserDTO userDTO = iteratordto.next();
                        // 子账号无法直接检索到店铺名，需要根据其父账号检索
                        if (userDTO.getCompanyName() == null) {
                        	DataGrid<UserDTO> parentUsers = userExportService.queryUserListByCondition(new UserDTO(), null, Arrays.asList(userDTO.getParentId()+""), pager);
                        	String companyName = null;
                        	if (parentUsers.getTotal() > 0) {
                        		companyName = parentUsers.getRows().get(0).getCompanyName();
                        	}
                        	map.put(userDTO.getUid().toString(), companyName);
                        } else {
                        	map.put(userDTO.getUid().toString(), userDTO.getCompanyName());
                        }
                    }
                }

            }
        }


        return map;
    }

    @Override
    public ExecuteResult<DataGrid<ContractInfoDTO>> assembleContracInfos(ContractInfoDTO contractInfoDTO, Pager pager) {
        ContractMatDTO contractMatDTO = new ContractMatDTO();
        ExecuteResult<DataGrid<ContractInfoDTO>> results = protocolExportService
                .queryContractInfoList(contractInfoDTO, pager);
        List<ContractInfoDTO> tempResult = results.getResult().getRows();
        for (ContractInfoDTO con : tempResult) {
            contractMatDTO.setContractNo(con.getContractNo());
            con.setContractMatDTOs(protocolExportService.queryContractMatList(contractMatDTO, pager).getResult().getRows());
            for (Map e : con.getContractMatDTOs()) {
                putSalerAttr(e);
            }
        }
        return results;
    }

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
        ContractInfoDTO contractInfoDTO = protocolExportService.queryByContractInfo(infoDTO).getResult();
        paymentTermDTO.setContractNo(contractInfoDTO.getContractNo());
        protocolExportService.addContractPaymentTerm(paymentTermDTO);
        for (ContractMatDTO matDTO : matDTOs) {
            matDTO.setContractNo(contractInfoDTO.getId().toString());
            matDTO.setContractNo(infoDTO.getContractNo());
            matDTO.setCreateBy(registerDTO.getUid().toString());
            matDTO.setActiveFlag("1");
            protocolExportService.addContractMat(matDTO);
        }
    }

    @Override
    public void updateContractInfos(ContractInfoDTO infoDTO, List<ContractMatDTO> matDTOs, ContractPaymentTermDTO paymentTermDTO,
                                    RegisterDTO registerDTO, String needApprove, List<Long> removeMatIds) {

        //执行修改协议动作
        if ("1".equals(needApprove)) {
            infoDTO.setStatus(String.valueOf(ContractStatus.NEEDAPPROVE.ordinal()));
        } else {
            infoDTO.setStatus(String.valueOf(ContractStatus.NOTPUBLISH.ordinal()));
            infoDTO.setApproveBy("");
        }
        //状态7为协议生效状态，当协议生效状态下进行修改时，要新创建一条协议，同时不对原协议进行修改
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
                //dubbo发版后可用
                con.setUpdatedContractNo(infoDTO.getContractNo());
            }
            protocolExportService.addContractInfo(infoDTO);
        } else {
            protocolExportService.modifyContractInfo(infoDTO);
        }
        paymentTermDTO.setContractNo(infoDTO.getContractNo());
        ExecuteResult<ContractPaymentTermDTO> tempTermDTO = protocolExportService.queryByContractPaymentTerm(paymentTermDTO.getContractNo());
        if (null == tempTermDTO.getResult()) {
            paymentTermDTO.setCreateBy(registerDTO.getUid().toString());
            paymentTermDTO.setCreateDate(new Date());
            protocolExportService.addContractPaymentTerm(paymentTermDTO);
        } else {
            protocolExportService.modifyContractPaymentTerm(paymentTermDTO);
        }

        if (null != removeMatIds && removeMatIds.size() > 0) {
            ContractMatDTO temp = new ContractMatDTO();
            Pager<ContractMatDTO> pager = new Pager<ContractMatDTO>();
            pager.setRows(Integer.MAX_VALUE);
            temp.setContractNo(infoDTO.getContractNo());
            temp.setActiveFlag("1");
            ExecuteResult<DataGrid<Map>> tempMats = protocolExportService.queryContractMatList(temp, pager);
            if (null != tempMats.getResult().getRows() && tempMats.getResult().getRows().size() > 0) {
                for (Long e : removeMatIds) {
                    temp.setId(e);
                    ContractMatDTO result = protocolExportService.queryByContractMat(temp).getResult();
                    result.setActiveFlag("0");
                    protocolExportService.modifyContractMat(result);
                }
            }
        }

        for (ContractMatDTO matDTO : matDTOs) {
            matDTO.setContractNo(infoDTO.getContractNo());
            //BUG:3231
            double matPrice = matDTO.getMatPrice();
            matDTO.setMatPrice(null);
            ContractMatDTO temp = protocolExportService.queryByContractMat(matDTO).getResult();
            if (null == temp) {
                matDTO.setId(null);
                matDTO.setMatPrice(matPrice);
                matDTO.setCreateBy(registerDTO.getUid().toString());
                matDTO.setCreateDate(new Date());
                matDTO.setActiveFlag("1");
        
                matDTO.setProtocolType(infoDTO.getProtocolType());
                protocolExportService.addContractMat(matDTO);
            } else {
            	matDTO.setMatPrice(matPrice);
                matDTO.setUpdateBy(registerDTO.getUid().toString());
                matDTO.setCreateDate(new Date());
                matDTO.setProtocolType(infoDTO.getProtocolType());
                protocolExportService.modifyContractMat(matDTO);
            }


        }
    }


}
