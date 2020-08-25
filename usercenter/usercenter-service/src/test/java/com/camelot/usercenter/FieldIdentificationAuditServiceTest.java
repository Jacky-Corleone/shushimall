package com.camelot.usercenter;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONObject;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.enums.UserEnums.FieldIdentificationAuditStatus;
import com.camelot.usercenter.service.FieldIdentificationAuditService;


public class FieldIdentificationAuditServiceTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(FieldIdentificationAuditServiceTest.class);
	ApplicationContext ctx = null;
	FieldIdentificationAuditService auditService = null;

	@Before
	public void setUp() {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		auditService = (FieldIdentificationAuditService) ctx.getBean("fieldIdentificationAuditService");
	}

	@Test
	public void addAuditTest() {
    	FieldIdentificationAuditDTO auditDTO = new FieldIdentificationAuditDTO();
		auditDTO.setUserId(1000000626L);
		auditDTO.setExtendId(1000000626L);
		auditDTO.setAuditorId("1001032");
		ExecuteResult<String> er = auditService.addAudit(auditDTO);
		LOGGER.info("操作方法{}，结果信息{}","addAudit", JSONObject.toJSONString(er.getResultMessage()));
	}

	@Ignore
	public void QueryDTOByUserIdTest() {
    	FieldIdentificationAuditDTO auditDTO = new FieldIdentificationAuditDTO();
    	auditDTO = auditService.queryAuditByUserId(1000000626L);
		LOGGER.info("操作方法{}，结果信息{}","queryAuditByUserId", JSONObject.toJSONString(auditDTO));
	}

	@Ignore
	public void testFindUserListByCondition(){
		FieldIdentificationAuditDTO AuditDTO = new FieldIdentificationAuditDTO();
		Pager<FieldIdentificationAuditDTO> pager =new Pager<FieldIdentificationAuditDTO>();
		DataGrid<FieldIdentificationAuditDTO> dataGrid=auditService.findAuditListByCondition(AuditDTO, FieldIdentificationAuditStatus.ACCEPTED, pager);
		LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(dataGrid));
	}

	@Ignore
	public void testModifyAuditStatus(){
		Long id = 1L;
		String auditorId = "1000000861";
		String auditRemark = "通过";
		int auditStatus = 2;
		ExecuteResult<String> executeResult = auditService.modifyAuditStatus(id, auditorId, auditRemark, auditStatus);
		LOGGER.info("操作方法{}，结果信息{}","testFindUserListByCondition", JSONObject.toJSONString(executeResult));
	}



}
