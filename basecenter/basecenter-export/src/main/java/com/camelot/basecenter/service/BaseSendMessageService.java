package com.camelot.basecenter.service;

import java.util.List;

import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/** 
 * <p>Description: [TDK设置服务]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface BaseSendMessageService {
	
	/**
	 * 添加邮件/短信发送数据
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<BaseSendMessageDTO> saveBaseSendMessage(BaseSendMessageDTO baseSendMessageDTO);
	
	/**
	 * 查询邮件/短信发送数据
	 * @param baseSendMessageDTO
	 * @return
	 */
	public DataGrid<BaseSendMessageDTO> questyBaseSendMessage(BaseSendMessageDTO baseSendMessageDTO,Pager pager);
	
	/**
	 * 编辑邮件/短信发送数据
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<BaseSendMessageDTO> editBaseSendMessage(BaseSendMessageDTO baseSendMessageDTO);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	public ExecuteResult<String> editBaseSendMessageBatch(List<BaseSendMessageDTO> list);
	
	/**
	 * 删除消息
	 * @param baseSendMessageDTO
	 * @return
	 */
	public ExecuteResult<String> deleteSendMessage(BaseSendMessageDTO baseSendMessageDTO);
	
	/**
	 * 向mq中发送消息
	 * 
	 * @param baseSendMessageDTO  消息实体类
	 * @author 王东晓
	 * @return 
	 */
	public ExecuteResult<String> sendMessageToMQ(BaseSendMessageDTO baseSendMessageDTO);
}
