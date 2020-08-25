package com.camelot.usercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.FieldIdentificationAuditMybatisDAO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.enums.UserEnums.FieldIdentificationAuditStatus;
import com.camelot.usercenter.service.FieldIdentificationAuditService;

/**
 * 实地认证审核实现类
 * @author Klaus
 */
@Service("fieldIdentificationAuditService")
public class FieldIdentificationAuditServiceImpl implements FieldIdentificationAuditService{

    private final static Logger logger = LoggerFactory.getLogger(FieldIdentificationAuditServiceImpl.class);

    @Resource
    private FieldIdentificationAuditMybatisDAO auditDAO;

    /**
	 * 根据卖家Id查询审核DTO
	 * @param auditDTO
	 * @return 审核DTO
	 */
	@Override
    public FieldIdentificationAuditDTO queryAuditByUserId(Long userId) {
		FieldIdentificationAuditDTO auditDTO = null;
        try {
        	auditDTO = auditDAO.queryAuditByUserId(userId);
        } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("【实地认证】-【根据卖家Id查询审核DTO】出现异常！");
        }
        return auditDTO;
    }

	/**
	 * 根据条件查询审核DTO表格
	 * @param auditDTO
	 * @return 审核DTO表格
	 */
    @Override
    public DataGrid<FieldIdentificationAuditDTO> findAuditListByCondition(FieldIdentificationAuditDTO auditDTO, FieldIdentificationAuditStatus auditStatus, Pager<FieldIdentificationAuditDTO> pager) {
        DataGrid<FieldIdentificationAuditDTO> dataGrid = new DataGrid<FieldIdentificationAuditDTO>();
        if(auditDTO == null){
        	return dataGrid;
        }
        if(auditStatus != null){
        	auditDTO.setAuditStatus(auditStatus.getCode());
        }
        long listSize = auditDAO.selectCount(auditDTO);
        if (listSize > 0) {
            List<FieldIdentificationAuditDTO> listAuditDTO = auditDAO.selectList(auditDTO, pager);
            dataGrid.setRows(listAuditDTO);
            dataGrid.setTotal(listSize);
        }
        return dataGrid;
    }

    /**
	 * 新增审核
	 * @param auditDTO
	 * @return 执行结果信息类
	 */
	@Override
    public ExecuteResult<String> addAudit(FieldIdentificationAuditDTO auditDTO) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
        	auditDAO.addAudit(auditDTO);
	        executeResult.setResultMessage("新增审核成功！");
        } catch (Exception e) {
        	executeResult.setResultMessage("新增审核失败！");
	        e.printStackTrace();
	        logger.error("【实地认证】-【新增审核】出现异常！");
        }
        return executeResult;
    }

	/**
	 * 根据主键Id修改审核状态
	 * @param auditDTO
	 * @return 执行结果信息类
	 */
	@Override
    public ExecuteResult<String> modifyAuditStatus(Long id, String auditorId, String auditRemark, int auditStatus) {
	    ExecuteResult<String> executeResult = new ExecuteResult<String>();
	    try {
	        auditDAO.modifyAuditStatus(id, auditorId, auditRemark, auditStatus);
	        executeResult.setResultMessage("修改审核状态成功！");
	    } catch (Exception e) {
	    	executeResult.setResultMessage("修改审核状态失败！");
	    	e.printStackTrace();
	        logger.error("【实地认证】-【根据主键Id修改审核状态】出现异常！");
	    }
	    return executeResult;
    }
}
