package com.camelot.goodscenter.service.impl;

import com.camelot.goodscenter.dao.ContractInfoDAO;
import com.camelot.goodscenter.dao.ContractMatDAO;
import com.camelot.goodscenter.dao.ContractOrderDAO;
import com.camelot.goodscenter.dao.ContractPaymentTermDAO;
import com.camelot.goodscenter.dao.ContractUrlShowDAO;
import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.ContractMatDTO;
import com.camelot.goodscenter.dto.ContractOrderDTO;
import com.camelot.goodscenter.dto.ContractPaymentTermDTO;
import com.camelot.goodscenter.dto.ContractUrlShowDTO;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: [协议功能接口实现]</p>
 * Created on 2015年06月08日
 *
 * @author 鲁鹏
 * @version 1.0
 *          Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("protocolExportService")
public class ProtocolExportServiceImpl implements ProtocolExportService {

    @Resource
    private ContractInfoDAO contractInfoDAO;
    @Resource
    private ContractMatDAO contractMatDAO;
    @Resource
    private ContractOrderDAO contractOrderDAO;
    @Resource
    private ContractPaymentTermDAO contractPaymentTermDAO;
    @Resource
    private ContractUrlShowDAO contractUrlShowDAO;
    

    /**
     * <p>Discription:[根据条件查询详情接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:[鲁鹏]
     */
    public ExecuteResult<ContractInfoDTO> queryByContractInfo(ContractInfoDTO dto) {
        ExecuteResult<ContractInfoDTO> er = new ExecuteResult<ContractInfoDTO>();
        ContractInfoDTO contractInfoDTO = contractInfoDAO.findBycontractInfoDTO(dto);
        try {
            if (contractInfoDTO != null) {
                er.setResult(contractInfoDTO);
                er.setResultMessage("success");
            } else {
                er.setResultMessage("您要查询的数据不存在");
            }
        } catch (Exception e) {
            er.setResultMessage(e.getMessage());
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议的列表接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<DataGrid<ContractInfoDTO>> queryContractInfoList(ContractInfoDTO dto, Pager page) {
        ExecuteResult<DataGrid<ContractInfoDTO>> er = new ExecuteResult<DataGrid<ContractInfoDTO>>();
        DataGrid<ContractInfoDTO> dg = new DataGrid<ContractInfoDTO>();
        List<ContractInfoDTO> list = contractInfoDAO.queryPage(page, dto);
        Long count = contractInfoDAO.queryPageCount(dto);
        try {
            if (list != null) {
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            } else {
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议的列表包含物料名称等信息]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<DataGrid<ContractInfoDTO>> queryContractInfoPager(ContractInfoDTO dto, Pager page) {
        ExecuteResult<DataGrid<ContractInfoDTO>> er = new ExecuteResult<DataGrid<ContractInfoDTO>>();
        DataGrid<ContractInfoDTO> dg = new DataGrid<ContractInfoDTO>();
        List<ContractInfoDTO> list = contractInfoDAO.queryContractInfoPager(page, dto);
        Long count = contractInfoDAO.queryContractInfoPagerCount(dto);
        try {
            if (list != null) {
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            } else {
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议明细详情接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<ContractMatDTO> queryByContractMat(ContractMatDTO dto) {
        ExecuteResult<ContractMatDTO> er = new ExecuteResult<ContractMatDTO>();
        ContractMatDTO contractMatDTO = contractMatDAO.findByContractMatDTO(dto);
        try {
            if (contractMatDTO != null) {
                er.setResult(contractMatDTO);
                er.setResultMessage("success");
            } else {
                er.setResultMessage("您要查询的数据不存在");
            }
        } catch (Exception e) {
            er.setResultMessage(e.getMessage());
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议明细的列表接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<DataGrid<Map>> queryContractMatList(ContractMatDTO dto, Pager page) {
        ExecuteResult<DataGrid<Map>> er = new ExecuteResult<DataGrid<Map>>();
        DataGrid<Map> dg = new DataGrid<Map>();
        List<Map> list = contractMatDAO.queryPage(page, dto);
        Long count = contractMatDAO.queryPageCount(dto);
        try {
            if (list != null) {
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            } else {
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议订单详情]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<ContractOrderDTO> queryByContractOrder(ContractOrderDTO dto) {
        ExecuteResult<ContractOrderDTO> er = new ExecuteResult<ContractOrderDTO>();
        ContractOrderDTO contractOrderDTO = contractOrderDAO.findByContractOrderDTO(dto);
        try {
            if (contractOrderDTO != null) {
                er.setResult(contractOrderDTO);
                er.setResultMessage("success");
            } else {
                er.setResultMessage("您要查询的数据不存在");
            }
        } catch (Exception e) {
            er.setResultMessage(e.getMessage());
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议订单的列表]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<DataGrid<ContractOrderDTO>> queryContractOrderList(ContractOrderDTO dto, Pager page) {
        ExecuteResult<DataGrid<ContractOrderDTO>> er = new ExecuteResult<DataGrid<ContractOrderDTO>>();
        DataGrid<ContractOrderDTO> dg = new DataGrid<ContractOrderDTO>();
        List<ContractOrderDTO> list = contractOrderDAO.queryPage(page, dto);
        Long count = contractOrderDAO.queryPageCount(dto);
        try {
            if (list != null) {
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            } else {
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议账期接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<ContractPaymentTermDTO> queryByContractPaymentTerm(ContractPaymentTermDTO dto) {
        ExecuteResult<ContractPaymentTermDTO> er = new ExecuteResult<ContractPaymentTermDTO>();
        ContractPaymentTermDTO contractPaymentTermDTO = contractPaymentTermDAO.findByContractPaymentTermDTO(dto);
        try {
            if (contractPaymentTermDTO != null) {
                er.setResult(contractPaymentTermDTO);
                er.setResultMessage("success");
            } else {
                er.setResultMessage("您要查询的数据不存在");
            }
        } catch (Exception e) {
            er.setResultMessage(e.getMessage());
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[根据条件查询协议账期的列表接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<DataGrid<ContractPaymentTermDTO>> queryContractPaymentTermList(ContractPaymentTermDTO dto, Pager page) {
        ExecuteResult<DataGrid<ContractPaymentTermDTO>> er = new ExecuteResult<DataGrid<ContractPaymentTermDTO>>();
        DataGrid<ContractPaymentTermDTO> dg = new DataGrid<ContractPaymentTermDTO>();
        List<ContractPaymentTermDTO> list = contractPaymentTermDAO.queryPage(page, dto);
        Long count = contractPaymentTermDAO.queryPageCount(dto);
        try {
            if (list != null) {
                dg.setRows(list);
                dg.setTotal(count);
                er.setResult(dg);
            } else {
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[生成协议详情接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> addContractInfo(ContractInfoDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        try {
            if (dto.getContractNo() == null || dto.getContractNo().equals(0L)) {
                er.setResultMessage("协议编码不能为空");
                return er;
            }
            contractInfoDAO.insert(dto);
            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[生成协议详情接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> addContractMat(ContractMatDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        try {
            if (dto.getContractNo() == null || dto.getContractNo().equals(0L)) {
                er.setResultMessage("协议编码不能为空");
                return er;
            }
            contractMatDAO.insert(dto);
            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[生成协议订单]</p>
     * Created on 2015年06月10日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> addContractOrder(ContractOrderDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        try {
            if (dto.getContractNo() == null || dto.getContractNo().equals(0L)) {
                er.setResultMessage("协议编码不能为空");
                return er;
            }
            //判断传递订单号是多个还是一个
            if (dto.getOrderNos() != null && dto.getOrderNos().size() > 0) {
                for (String orderNo : dto.getOrderNos()) {
                    dto.setOrderNo(orderNo);
                    contractOrderDAO.insert(dto);
                }
            } else {
                contractOrderDAO.insert(dto);
            }
            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[生成协议账期]</p>
     * Created on 2015年06月10日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> addContractPaymentTerm(ContractPaymentTermDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        try {
            if (dto.getContractNo() == null || dto.getContractNo().equals(0L)) {
                er.setResultMessage("协议编码不能为空");
                return er;
            }
            //判断传递订单号是多个还是一个
            if (dto.getPaymentDays() != null && dto.getPaymentDays().equals(0L)) {
                er.setResultMessage("账期不能为空");
                return er;
            }
            contractPaymentTermDAO.insert(dto);
            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

    /**
     * <p>Discription:[修改协议详情接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> modifyContractInfo(ContractInfoDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        ContractInfoDTO contractInfo = new ContractInfoDTO();
        contractInfo.setContractNo(dto.getContractNo());
        ContractInfoDTO contractInfoDTO = contractInfoDAO.findBycontractInfoDTO(contractInfo);
        try {
            if (contractInfoDTO != null) {
                if (StringUtils.isNotBlank(dto.getContractOrderNo())) {
                    contractInfoDTO.setContractOrderNo(dto.getContractOrderNo());
                }
                if (StringUtils.isNotBlank(dto.getContractName())) {
                    contractInfoDTO.setContractName(dto.getContractName());
                }
                if (null != dto.getPrinterId()) {
                    contractInfoDTO.setPrinterId(dto.getPrinterId());
                }
                if (null != dto.getSupplierId()) {
                    contractInfoDTO.setSupplierId(dto.getSupplierId());
                }
                if (dto.getBeginDate() != null) {
                    contractInfoDTO.setBeginDate(dto.getBeginDate());
                }
                if (dto.getEndDate() != null) {
                    contractInfoDTO.setEndDate(dto.getEndDate());
                }
                if (dto.getStatus() != null) {
                    contractInfoDTO.setStatus(dto.getStatus());
                }
                if (StringUtils.isNotBlank(dto.getActiveFlag())) {
                    contractInfoDTO.setActiveFlag(dto.getActiveFlag());
                }
                if (StringUtils.isNotBlank(dto.getRemark())) {
                    contractInfoDTO.setRemark(dto.getRemark());
                }
                if (StringUtils.isNotBlank(dto.getConfirmBy())) {
                    contractInfoDTO.setConfirmBy(dto.getConfirmBy());
                }
                contractInfoDTO.setApproveBy(dto.getApproveBy());
                if (StringUtils.isNotBlank(dto.getUpdateBy())) {
                    contractInfoDTO.setUpdateBy(dto.getUpdateBy());
                }
                if (dto.getUpdateDate() != null) {
                    contractInfoDTO.setUpdateDate(dto.getUpdateDate());
                }
                if (StringUtils.isNotBlank(dto.getAnnex())) {
                    contractInfoDTO.setAnnex(dto.getAnnex());
                }
                if (StringUtils.isNotBlank(dto.getRefusalReason())) {
                    contractInfoDTO.setRefusalReason(dto.getRefusalReason());
                }
                if (StringUtils.isNotBlank(dto.getUpdatedContractNo())) {
                    contractInfoDTO.setUpdatedContractNo(dto.getUpdatedContractNo());
                }
                if (contractInfoDAO.update(contractInfoDTO) > 0) {
                    er.setResult("修改成功");
                } else {
                    er.setResult("修改失败");
                }
            }
        } catch (Exception e) {
            er.setResult("error");
            throw new RuntimeException(e);
        }

        return er;
    }

    /**
     * <p>Discription:[修改协议明细接口实现]</p>
     * Created on 2015年06月08日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */

    public ExecuteResult<String> modifyContractMat(ContractMatDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        ContractMatDTO contractMat = new ContractMatDTO();
        contractMat.setContractNo(dto.getContractNo());
        contractMat.setMatCd(dto.getMatCd());
        List<ContractMatDTO> contractMatDTOs = contractMatDAO.findAll(contractMat);
        for (ContractMatDTO contractMatDTO : contractMatDTOs) {
            try {
                if (dto.getMatPrice() != null) {
                    contractMatDTO.setMatPrice(dto.getMatPrice());
                }
                if (dto.getActiveFlag() != null) {
                    contractMatDTO.setActiveFlag(dto.getActiveFlag());
                }
                if (dto.getUpdateBy() != null) {
                    contractMatDTO.setUpdateBy(dto.getUpdateBy());
                }
                if (dto.getUpdateDate() != null) {
                    contractMatDTO.setUpdateDate(dto.getUpdateDate());
                }
                if (dto.getCost() != null) {
                    contractMatDTO.setCost(dto.getCost());
                }
                if (dto.getNumber() != null) {
                    contractMatDTO.setNumber(dto.getNumber());
                }
                if (contractMatDAO.update(contractMatDTO) > 0) {
                    er.setResult("修改成功");
                } else {
                    er.setResult("修改失败");
                }
            } catch (Exception e) {
                er.setResult("error");
                throw new RuntimeException(e);
            }
        }

        return er;
    }

    /**
     * <p>Discription:[修改协议明细]</p>
     * Created on 2015年06月10日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> modifyContractPaymentTerm(ContractPaymentTermDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        ContractPaymentTermDTO contractPaymentTermDTO = new ContractPaymentTermDTO();
        contractPaymentTermDTO.setContractNo(dto.getContractNo());
        ContractPaymentTermDTO contractPaymentTerm = contractPaymentTermDAO.findByContractPaymentTermDTO(contractPaymentTermDTO);
        try {
            if (contractPaymentTerm != null) {
                if (dto.getActiveFlag() != null) {
                    contractPaymentTerm.setActiveFlag(dto.getActiveFlag());
                }
                if (dto.getPaymentDays() != null) {
                    contractPaymentTerm.setPaymentDays(dto.getPaymentDays());
                }
                if (dto.getPaymentType() != null) {
                    contractPaymentTerm.setPaymentType(dto.getPaymentType());
                }
                if (contractPaymentTermDAO.update(contractPaymentTerm) > 0) {
                    er.setResult("修改成功");
                } else {
                    er.setResult("修改失败");
                }
            }
        } catch (Exception e) {
            er.setResult("error");
            throw new RuntimeException(e);
        }

        return er;
    }

    /**
     * <p>Discription:[修改协议账期]</p>
     * Created on 2015年06月10日
     *
     * @param dto
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> modifyContractOrder(ContractOrderDTO dto) {
        ExecuteResult<String> er = new ExecuteResult<String>();
        ContractOrderDTO contractOrder = new ContractOrderDTO();
        contractOrder.setContractNo(dto.getContractNo());
        contractOrder.setOrderNo(dto.getOrderNo());
        ContractOrderDTO contractOrderDTO = contractOrderDAO.findByContractOrderDTO(contractOrder);
        try {
            if (contractOrderDTO != null) {
                if (dto.getActiveFlag() != null) {
                    contractOrderDTO.setActiveFlag(dto.getActiveFlag());
                }
                if (dto.getState() != null) {
                    contractOrderDTO.setState(dto.getState());
                }
                if (contractOrderDAO.update(contractOrderDTO) > 0) {
                    er.setResult("修改成功");
                } else {
                    er.setResult("修改失败");
                }
            }
        } catch (Exception e) {
            er.setResult("error");
            throw new RuntimeException(e);
        }

        return er;
    }

    /**
     * <p>Discription:[生成协议编码]</p>
     * Created on 2015年06月23日
     *
     * @return
     * @author:鲁鹏
     */
    public ExecuteResult<String> createContractNo() {
        ExecuteResult<String> er = new ExecuteResult<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Integer b = (int) (Math.random() * 10000);
        String a = "XY" + sdf.format(new Date()) + b.toString();
        er.setResult(a);
        return er;
    }
    public ExecuteResult<ContractPaymentTermDTO> queryByContractPaymentTerm(String contractNo){
    	ExecuteResult<ContractPaymentTermDTO> er = new ExecuteResult<ContractPaymentTermDTO>();
    	if(StringUtils.isEmpty(contractNo)){
    		er.addErrorMessage("合同编号不能为空！");
    		return er;
    	}
    	ContractPaymentTermDTO contractPaymentTermDTO= contractPaymentTermDAO.findByContractNo(contractNo);
    	 try {
             if (contractPaymentTermDTO != null) {
                 er.setResult(contractPaymentTermDTO);
                 er.setResultMessage("success");
             } else {
                 er.setResultMessage("您要查询的数据不存在");
             }
         } catch (Exception e) {
             er.setResultMessage(e.getMessage());
             throw new RuntimeException(e);
         }
    	return er;
    }

	@Override
	public ExecuteResult<String> addcontractUrlShow(ContractUrlShowDTO dto) {
		 ExecuteResult<String> er = new ExecuteResult<String>();
	        try {
	        	contractUrlShowDAO.insert(dto);
	            er.setResultMessage("success");
	        } catch (Exception e) {
	            er.setResultMessage("error");
	            throw new RuntimeException(e);
	        }
	        return er;
	
	}

	@Override

	
    public ExecuteResult<DataGrid<ContractUrlShowDTO>> queryContractUrlShow(ContractUrlShowDTO dto) {
        ExecuteResult<DataGrid<ContractUrlShowDTO>> er = new ExecuteResult<DataGrid<ContractUrlShowDTO>>();
        DataGrid<ContractUrlShowDTO> dg = new DataGrid<ContractUrlShowDTO>();
        List<ContractUrlShowDTO> list = contractUrlShowDAO.findByContractUrlShowDTO(dto);
       
        try {
            if (list != null) {
                dg.setRows(list);
                er.setResult(dg);
            } else {
                er.setResultMessage("要查询的数据不存在");
            }

            er.setResultMessage("success");
        } catch (Exception e) {
            er.setResultMessage("error");
            throw new RuntimeException(e);
        }
        return er;
    }

	@Override
	public ExecuteResult<String> deleteContractUrlShow(String id) {
		ExecuteResult<String> er = new ExecuteResult<String>();
			try{
				if(StringUtils.isNotEmpty(id)){
				   contractUrlShowDAO.delete(id);
				   er.setResultMessage("success");
				}else{
				  er.setResultMessage("删除失败");
				}
	        } catch (Exception e) {
	            er.setResult("error");
	            throw new RuntimeException(e);
	        }
	        return er;
	}
}
