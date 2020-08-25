package com.camelot.basecenter.message;



import java.util.List;

import javax.annotation.Resource;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.basecenter.activemq.MessagePublisherService;
import com.camelot.basecenter.dao.BaseSendMessageDAO;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.util.ImplUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.MessageTypeEnum;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.openplatform.common.util.MessageTemplateFileUtil;

/**
 * <p>Description: [tdk设置实现类]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("baseSendMessageService")
public class BaseSendMessageServiceImpl extends ImplUtil implements BaseSendMessageService {
	private static final Logger LOGGER=LoggerFactory
			.getLogger(BaseSendMessageServiceImpl.class);
	
	@Resource
	private BaseSendMessageDAO baseSendMessageDAO;
	@Resource
	private MessagePublisherService messagePublisherServiceImpl;
	
	@Override
	public ExecuteResult<BaseSendMessageDTO> saveBaseSendMessage(
			BaseSendMessageDTO baseSendMessageDTO) {
		ExecuteResult<BaseSendMessageDTO> result = new ExecuteResult<BaseSendMessageDTO>();
		try {
			baseSendMessageDAO.add(baseSendMessageDTO);
			result.setResult(baseSendMessageDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【saveBaseSendMessage】出错,错误:" + e);
			result.addErrorMessage("执行方法【saveBaseSendMessage】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public DataGrid<BaseSendMessageDTO> questyBaseSendMessage(
			BaseSendMessageDTO baseSendMessageDTO,Pager page) {
		DataGrid<BaseSendMessageDTO> dg = new DataGrid<BaseSendMessageDTO>();
		try {
			List<BaseSendMessageDTO> list = this.baseSendMessageDAO.questyBaseSendMessageList(baseSendMessageDTO, page);
			dg.setRows(null);
			if(list != null && list.size() > 0){
				dg.setRows(list);
			}
			//查询总条数
			Long count = this.baseSendMessageDAO.queryCount(baseSendMessageDTO);
			dg.setTotal(count);
		} catch (Exception e) {
			LOGGER.error("调用方法【questyBaseSendMessage】出错,错误:" + e);
			throw new RuntimeException(e);
		}
		return dg;
	}

	@Override
	public ExecuteResult<BaseSendMessageDTO> editBaseSendMessage(
			BaseSendMessageDTO baseSendMessageDTO) {
		ExecuteResult<BaseSendMessageDTO> result = new ExecuteResult<BaseSendMessageDTO>();
		try {
			baseSendMessageDAO.update(baseSendMessageDTO);
			result.setResult(baseSendMessageDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【editBaseSendMessage】出错,错误:" + e);
			result.addErrorMessage("执行方法【editBaseSendMessage】报错:"+e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}
	
	public ExecuteResult<String> editBaseSendMessageBatch(List<BaseSendMessageDTO> list){
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			baseSendMessageDAO.updateAll(list);
		} catch (Exception e) {
			LOGGER.error("调用方法【editBaseSendMessageBatch】出错,错误:" + e);
			result.addErrorMessage("执行方法【editBaseSendMessageBatch】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	
	/**
	 * 删除站内信、短信、邮件信息
	 */
	@Override
	public ExecuteResult<String> deleteSendMessage(
			BaseSendMessageDTO baseSendMessageDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			baseSendMessageDAO.deleteSendMessage(baseSendMessageDTO);
		} catch (Exception e) {
			LOGGER.error("调用方法【deleteSendMessage】出错,错误:" + e);
			result.addErrorMessage("执行方法【deleteSendMessage】报错:"+e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}
	
	@Override
	public ExecuteResult<String> sendMessageToMQ(BaseSendMessageDTO baseSendMessageDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		//获取生产者实例
		Integer platformId = baseSendMessageDTO.getPlatformId();
		String type = baseSendMessageDTO.getType(); 
		Integer contentType = baseSendMessageDTO.getContentType();
		ActiveMQQueue queueDestination = this.getQueueDestination(platformId, type,contentType);
		if(queueDestination == null){
			LOGGER.error("无法获取MQ消息队列实例，请查看：platformId:"+platformId+";type:"+type);
			result.addErrorMessage("无法获取MQ消息队列实例，请查看：platformId:"+platformId+";type:"+type);
			return result;
		}
		String content  = baseSendMessageDTO.getContent();
		if(StringUtils.isEmpty(content)){
			LOGGER.error("发送的内容为空");
			result.addErrorMessage("发送的内容为空");
			return result;
		}
		//根据平台id将platformName转换成汉字
		String newContent = MessageTemplateFileUtil.getInstance().replaceByPlatformId(content, platformId);
		//如果是绿印短信，则在后面添加签名
		if(platformId == PlatformEnum.GREEN.getId() && MessageTypeEnum.SMS.getId().equals(baseSendMessageDTO.getType())){
			newContent = MessageTemplateFileUtil.getInstance().addMessageSignature(newContent);
		}
		baseSendMessageDTO.setContent(newContent);
		//如果是邮件，则标题也需要转换为汉字
		if(MessageTypeEnum.MAIL.getId().equals(baseSendMessageDTO.getType()) && baseSendMessageDTO.getTitle() != null){
			String newTitle = MessageTemplateFileUtil.getInstance().replaceByPlatformId(baseSendMessageDTO.getTitle(), platformId);
			baseSendMessageDTO.setTitle(newTitle);
		}
		ExecuteResult<String> sendResult = messagePublisherServiceImpl.sendMessage(queueDestination,baseSendMessageDTO);
		if(!sendResult.isSuccess()){
			result.setErrorMessages(sendResult.getErrorMessages());
			return result;
		}
		return result;
	}

}
