package com.camelot.basecenter.message;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseTDKConfigService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.MessageContentTypeEnum;
import com.camelot.openplatform.common.enums.MessageTypeEnum;

public class BaseSendMessageServiceImplTest {
	
	ApplicationContext ctx = null;
	BaseSendMessageService baseSendMessageService = null; 
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		baseSendMessageService = (BaseSendMessageService) ctx.getBean("baseSendMessageService");
	}
	
	@Test
	public void testSaveBaseSendMessage() {
		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
		baseSendMessageDTO.setAddress("847145807");
		baseSendMessageDTO.setContent("1111");
		baseSendMessageDTO.setIsSend("0");
		baseSendMessageDTO.setIsPildash("0");
		baseSendMessageDTO.setType("1");
		baseSendMessageDTO.setSendNum(0);
		ExecuteResult<BaseSendMessageDTO> result = this.baseSendMessageService.saveBaseSendMessage(baseSendMessageDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}

//	@Test
//	public void testQuestyBaseSendMessage() {
//		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
//		//baseSendMessageDTO.setIsSend("0");
//		baseSendMessageDTO.setMinSendNum(0);
//		baseSendMessageDTO.setMaxSendNum(2);
//		DataGrid<BaseSendMessageDTO> dg = this.baseSendMessageService.questyBaseSendMessage(baseSendMessageDTO, null);
//		Assert.assertEquals(dg.getRows() != null, true);
//	}
//
//	@Test
//	public void testEditBaseSendMessage() {
//		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
//		baseSendMessageDTO.setId(1L);
//		baseSendMessageDTO.setAddress("18611555674");
//		baseSendMessageDTO.setContent("22222");
//		baseSendMessageDTO.setIsSend("0");
//		baseSendMessageDTO.setIsPildash("0");
//		baseSendMessageDTO.setType("1");
//		baseSendMessageDTO.setSendNum(1);
//		ExecuteResult<BaseSendMessageDTO> result = this.baseSendMessageService.editBaseSendMessage(baseSendMessageDTO);
//		Assert.assertEquals(result.isSuccess(), true);
//	}
//	
//	@Test
//	public void testEditBaseSendMessageBatch() {
//		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
//		baseSendMessageDTO.setId(4L);
//		baseSendMessageDTO.setIsSend("1");
//		baseSendMessageDTO.setSendNum(1);
//		BaseSendMessageDTO baseSendMessageDTO2 = new BaseSendMessageDTO();
//		baseSendMessageDTO2.setId(5L);
//		baseSendMessageDTO2.setIsSend("1");
//		baseSendMessageDTO2.setSendNum(1);
//		List<BaseSendMessageDTO> list = new ArrayList<BaseSendMessageDTO>();
//		list.add(baseSendMessageDTO);
//		list.add(baseSendMessageDTO2);
//		
//		ExecuteResult<String> result = this.baseSendMessageService.editBaseSendMessageBatch(list);
//		Assert.assertEquals(result.isSuccess(), true);
//	}
//	
//	@Test
//	public void testDeleteSendMessage() throws ParseException {
//		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		baseSendMessageDTO.setBeginTime("2015-05-22");
//		baseSendMessageDTO.setEndTime("2015-05-25");
//		
//		ExecuteResult<String> result = this.baseSendMessageService.deleteSendMessage(baseSendMessageDTO);
//		Assert.assertEquals(result.isSuccess(), true);
//	}
	
	@Test
	public void testSendEmailToMQ() {
		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
		baseSendMessageDTO.setId(1L);
		baseSendMessageDTO.setAddress("songwenbin@camelotchina.com");
		baseSendMessageDTO.setContent("sdfdsfsd");
		baseSendMessageDTO.setType(MessageTypeEnum.MAIL.getId()); // 邮件
		baseSendMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId()); // 通知

		ExecuteResult<String> result = this.baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}

	@Test
	public void testSendSMSToMQ() {
		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
		baseSendMessageDTO.setAddress("13501133340");
		//baseSendMessageDTO.setContent("尊敬的用户，由于您未在7日内处理订单：201511230000201的退款/退货申请，系统已默认同意买家的退款/退货申请，如有疑问请联系舒适100。");
		baseSendMessageDTO.setContent("尊敬的卖家用户，您有一个延期支付订单已到结算期（订单编号：201511230000201），由于买家的支付账户余额不足，系统自动扣款失败，请及时与买家联系。");
		baseSendMessageDTO.setType(MessageTypeEnum.SMS.getId()); // 短信
		baseSendMessageDTO.setContentType(MessageContentTypeEnum.NOTIC.getId()); // 通知

		ExecuteResult<String> result = this.baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
		Assert.assertEquals(result.isSuccess(), true);
	}

}
