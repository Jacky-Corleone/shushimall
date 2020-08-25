package com.camelot.basecenter.smsconfig;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.dao.BaseWebSiteMessageDAO;
import com.camelot.basecenter.domain.BaseWebsiteMessage;
import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.util.MessageTemplateFileUtil;
import com.camelot.openplatform.dao.util.RedisDB;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("baseWebSiteMessageService")
public class BaseWebSiteMessageServiceImpl implements BaseWebSiteMessageService {
	private final static Logger logger = LoggerFactory.getLogger(BaseWebSiteMessageServiceImpl.class);
	
	@Resource
	private RedisDB redisDB;
	
	@Resource
	private BaseWebSiteMessageDAO baseWebSiteMessageDAO;

	@Override
	public ExecuteResult<String> addWebMessage(WebSiteMessageDTO webSiteMessageDTO) {
		ExecuteResult<String>  executeResult = new ExecuteResult<String>();
		Integer platformId = webSiteMessageDTO.getPlatformId();
		String content = MessageTemplateFileUtil.getInstance().replaceByPlatformId(webSiteMessageDTO.getWmContext(), platformId);
		webSiteMessageDTO.setWmContext(content);
		try {
			if(null != webSiteMessageDTO.getId()){
				baseWebSiteMessageDAO.update(webSiteMessageDTO);
				executeResult.setResult("修改成功！");
			}else{
				baseWebSiteMessageDAO.add(webSiteMessageDTO);
				executeResult.setResult("添加成功！");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			executeResult.setResult("操作失败！");
			throw new RuntimeException(e);
		}
		return executeResult;
	}

	@Override
	public int totalMessages(long userId, int messageType, int readFlag) {
		BaseWebsiteMessage baseWebsiteMessage = new BaseWebsiteMessage();
		baseWebsiteMessage.setWmFromUserid(userId);
		baseWebsiteMessage.setType(messageType);
		baseWebsiteMessage.setWmRead(readFlag);
		return baseWebSiteMessageDAO.totalMessages(baseWebsiteMessage);
	}

	@Override
	public ExecuteResult<String> modifyWebSiteMessage(String[] ids,String wmRead) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		try {
			for (String id : ids) {
				baseWebSiteMessageDAO.modifyWebSiteMessage(id,wmRead);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			executeResult.addErrorMsg(e.getMessage());
			throw new RuntimeException(e);
		}
		return executeResult; 
	}

	@Override
	public DataGrid<WebSiteMessageDTO> queryWebSiteMessageList(WebSiteMessageDTO webSiteMessageDTO, Pager page) {
		DataGrid<WebSiteMessageDTO> dataGrid = new DataGrid<WebSiteMessageDTO>();
		try {
			List<WebSiteMessageDTO> webSiteMessageDTOs = baseWebSiteMessageDAO.queryWebSiteMessageList(webSiteMessageDTO, page);
			long num = baseWebSiteMessageDAO.queryCount(webSiteMessageDTO);
			dataGrid.setRows(webSiteMessageDTOs);
			dataGrid.setTotal(num);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return dataGrid;
	}

	@Override
	public String getGeneralUserVerificationCode(String key) {
		String value = this.redisDB.get(key);
		return value;
	}

	@Override
	public ExecuteResult<WebSiteMessageDTO> getWebSiteMessageInfo(WebSiteMessageDTO webSiteMessageDTO) {
		ExecuteResult<WebSiteMessageDTO> result=new ExecuteResult<WebSiteMessageDTO>();
		try {
			WebSiteMessageDTO wsmDTO=baseWebSiteMessageDAO.getWebSiteMessageInfo(webSiteMessageDTO);
			result.setResult(wsmDTO);
			result.setResultMessage("success");
		} catch (Exception e) {
			result.getErrorMessages().add(e.getMessage());
			result.setResultMessage("error");
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public Long queryCount(WebSiteMessageDTO webSiteMessageDTO) {
		return baseWebSiteMessageDAO.queryCount(webSiteMessageDTO);
	}

	
}
