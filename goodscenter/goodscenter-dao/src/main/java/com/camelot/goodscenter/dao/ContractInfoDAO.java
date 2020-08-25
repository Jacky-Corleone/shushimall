package com.camelot.goodscenter.dao;

import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import java.util.List;

/** 
 * <p>Description: [协议dao]</p>
 * Created on 2015年06月08日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */

public interface ContractInfoDAO extends BaseDAO<ContractInfoDTO> {

        public List<ContractInfoDTO> queryPage(@Param("pager") Pager pager,@Param("contractInfo")ContractInfoDTO contractInfo);

        public Long queryPageCount(@Param("contractInfo")ContractInfoDTO contractInfo);

        public ContractInfoDTO findById(Long id);

        public Integer insert(ContractInfoDTO contractInfo);

        public Integer update(ContractInfoDTO contractInfo);

        public void delete(@Param("codes") List<String> codes);

        public List<Map<String,Object>> findAll();

        public ContractInfoDTO findBycontractInfoDTO(@Param("contractInfo")ContractInfoDTO contractInfo);

        public List<ContractInfoDTO> queryContractInfoPager(@Param("pager") Pager pager, @Param("contractInfo") ContractInfoDTO contractInfo);

        public Long queryContractInfoPagerCount(@Param("contractInfo") ContractInfoDTO contractInfo);

}
