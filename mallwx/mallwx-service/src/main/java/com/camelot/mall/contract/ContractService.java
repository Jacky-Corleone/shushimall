package com.camelot.mall.contract;

import java.util.List;
import java.util.Map;

import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractMatDTO;
import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.usercenter.dto.RegisterDTO;

/**
 * Created by 王帅 on 2015/7/3.
 * 按照用户ID查询对应的用户姓名
 */
public interface ContractService {
    /**
     * 获取用户与ID对照关系
     * @param contracts
     * @param ifseller
     * @param ifbuyer
     * @return
     */

    Map<String,String> pPUser(List<ContractInfoDTO> contracts,boolean ifseller,boolean ifbuyer);

    /**
     * 获取用户与公司名称对照关系
     * @param contracts
     * @return
     */
    Map<String,String> cPUser(List<ContractInfoDTO> contracts);

    /**
     * 拼装协议信息数据
     * @param contractInfoDTO
     * @return
     */

    List<ContractInfoDTO> assembleContracInfos(ContractInfoDTO contractInfoDTO);

    /**
     * 添加销售属性
     * @param contractMats
     */
    void putSalerAttr(Map contractMats);

    /**
     * 协议创建
     *
     * @param infoDTO
     * @param matDTOs
     * @param paymentTermDTO
     * @param registerDTO
     */
    void addContractInfos(ContractInfoDTO infoDTO, List<ContractMatDTO> matDTOs, ContractPaymentTermDTO paymentTermDTO, RegisterDTO registerDTO);

    /**
     * 协议修改
     *
     * @param infoDTO
     * @param matDTOs
     * @param paymentTermDTO
     * @param registerDTO
     */
    void updateContractInfos(ContractInfoDTO infoDTO, List<ContractMatDTO> matDTOs, ContractPaymentTermDTO paymentTermDTO, RegisterDTO registerDTO,String needApprove,List<Long> removeMatIds);
}
