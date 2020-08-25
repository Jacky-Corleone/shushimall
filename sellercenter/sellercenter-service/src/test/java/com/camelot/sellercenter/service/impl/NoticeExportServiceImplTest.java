package com.camelot.sellercenter.service.impl;

import org.junit.Assert;
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
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;

public class NoticeExportServiceImplTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(NoticeExportServiceImplTest.class);
	ApplicationContext ctx = null;
	NoticeExportService noticeExportService = null;
	@Before
	public void setUp() throws Exception {
		ctx = new ClassPathXmlApplicationContext("classpath*:/test.xml");
		noticeExportService = (NoticeExportService) ctx.getBean("noticeExportService");
	}

	@Test
	public void test() {
		Pager page=new Pager();
		MallNoticeDTO dto=new MallNoticeDTO();
		dto.setNoticeStatus(1);
		DataGrid<MallNoticeDTO> res=noticeExportService.queryNoticeList(page, dto);
		LOGGER.info("操作方法{}，结果信息{}","queryNoticeList", JSONObject.toJSONString(res));
	}
	@Test
    public void test_add() {
        MallNoticeDTO addDto=new MallNoticeDTO();
        addDto.setPlatformId(0L);
        addDto.setNoticeType(1);
        addDto.setNoticeTitle("1");
        addDto.setUrl("123");
        addDto.setNoticeContent("1");
        ExecuteResult<String> result = noticeExportService.addNotice(addDto);

        LOGGER.info("操作方法{}，结果信息{}","queryNoticeList", JSONObject.toJSONString(addDto));
        Assert.assertEquals(true, result.isSuccess());
    }
    @Ignore
    public void test_updateNoticSortNum() {
        MallNoticeDTO addDto=new MallNoticeDTO();
        addDto.setNoticeId(145L);
        addDto.setPlatformId(2000000091L);
        addDto.setSortNum(5);
        ExecuteResult<MallNoticeDTO> result = noticeExportService.updateNoticSortNum(addDto, -1);

        LOGGER.info("操作方法{}，结果信息{}","queryNoticeList", JSONObject.toJSONString(addDto));
        Assert.assertEquals(true, result.isSuccess());
    }
    
    @Test
    public void testModifyNmeanoticeInfo(){
    	MallNoticeDTO dto = new MallNoticeDTO();
    	dto.setUrl("3455");
    	dto.setNoticeStatus(1);
    	dto.setSortNum(3);
    	dto.setNoticeTitle("修改测试");
    	dto.setNoticeContent("修改后的内容");
    	dto.setNoticeId(395L);
    	dto.setNoticeType(2);
    	ExecuteResult<String> result = this.noticeExportService.modifyNoticeInfo(dto);

    	Assert.assertEquals(true, result.isSuccess());
    }
    
}
