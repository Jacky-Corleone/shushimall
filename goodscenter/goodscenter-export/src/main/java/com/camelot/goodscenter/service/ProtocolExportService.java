package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.*;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

import java.util.Map;

/** 
 * <p>Description: [商品SKU协议Service]</p>
 * Created on 2015年06月08日
 * @author  鲁鹏
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface ProtocolExportService {
	/**
	 * <p>Discription:[根据条件查询协议详情]</p>
	 * Created on 2015年06月08日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
    public ExecuteResult<ContractInfoDTO> queryByContractInfo(ContractInfoDTO dto);
	/**
	 * <p>Discription:[根据条件查询协议的列表]</p>
	 * Created on 2015年06月08日
	 * @param dto
	 * @return
	 * @author:鲁鹏
	 */
   public ExecuteResult<DataGrid<ContractInfoDTO>> queryContractInfoList(ContractInfoDTO dto, Pager page);
   /**
    * <p>Discription:[根据条件查询协议的列表包含物料名称等信息]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<ContractInfoDTO>> queryContractInfoPager(ContractInfoDTO dto,Pager page);
   /**
    * <p>Discription:[根据条件查询协议明细详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<ContractMatDTO> queryByContractMat(ContractMatDTO dto);
   /**
    * <p>Discription:[根据条件查询协议明细的列表]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<Map>> queryContractMatList(ContractMatDTO dto, Pager page);

   /**
    * <p>Discription:[根据条件查询协议订单详情]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<ContractOrderDTO> queryByContractOrder(ContractOrderDTO dto);
   /**
    * <p>Discription:[根据条件查询协议订单的列表]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<ContractOrderDTO>> queryContractOrderList(ContractOrderDTO dto, Pager page);

/**
    * <p>Discription:[生成协议详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addContractInfo(ContractInfoDTO dto);
   /**
    * <p>Discription:[生成协议明细]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addContractMat(ContractMatDTO dto);
   
   
   /**
    * <p>Discription:[生成协议附件明细]</p>
    * Created on2015年12月22日14:37:07
    * @param dto
    * @return
    * @author:訾瀚民
    */
   public ExecuteResult<String> addcontractUrlShow(ContractUrlShowDTO dto);
   
   
   /**
	 * <p>Discription:[根据条件查询协议附件详情]</p>
	 * Created on 2015年12月23日10:09:39
	 * @param dto
	 * @return
	 * @author:訾瀚民
	 */
   public ExecuteResult<DataGrid<ContractUrlShowDTO>> queryContractUrlShow(ContractUrlShowDTO dto);
	
   
   
   
   
   /**
    * <p>Discription:[生成协议订单]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addContractOrder(ContractOrderDTO dto);

   /**
    * <p>Discription:[修改协议详情]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyContractInfo(ContractInfoDTO dto);
   /**
    * <p>Discription:[修改协议明细]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyContractMat(ContractMatDTO dto);
   /**
    * <p>Discription:[修改协议明细]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyContractOrder(ContractOrderDTO dto);
   /**
    * <p>Discription:[生成协议编码]</p>
    * Created on 2015年06月23日
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> createContractNo();
   /**
    * <p>Discription:[根据条件查询协议账期]</p>
    * Created on 2015年06月28日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<ContractPaymentTermDTO> queryByContractPaymentTerm(ContractPaymentTermDTO dto);
   /**
    * <p>Discription:[根据条件查询协议账期列表]</p>
    * Created on 2015年06月28日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<DataGrid<ContractPaymentTermDTO>> queryContractPaymentTermList(ContractPaymentTermDTO dto, Pager page);
   /**
    * <p>Discription:[生成协议账期]</p>
    * Created on 2015年06月08日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> addContractPaymentTerm(ContractPaymentTermDTO dto);
   /**
    * <p>Discription:[修改协议账期]</p>
    * Created on 2015年06月10日
    * @param dto
    * @return
    * @author:鲁鹏
    */
   public ExecuteResult<String> modifyContractPaymentTerm(ContractPaymentTermDTO dto);
   /**
    * 
    * <p>Discription:[根据合同编号查询账期]</p>
    * Created on 2015-11-16
    * @param contractNo
    * @return
    * @author:[王鹏]
    */
   public ExecuteResult<ContractPaymentTermDTO> queryByContractPaymentTerm(String contractNo);
   
   
   /**
    * <p>Discription:[删除附件信息]</p>
    * Created on 2015年12月23日10:17:37
    * @param dto
    * @return
    * @author:訾瀚民
    */
   public ExecuteResult<String> deleteContractUrlShow(String id);
   
   
}
