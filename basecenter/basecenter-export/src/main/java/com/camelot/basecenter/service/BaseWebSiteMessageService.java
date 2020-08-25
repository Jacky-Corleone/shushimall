package com.camelot.basecenter.service;

import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface BaseWebSiteMessageService {
	
	/**
	 * <p>Discription:[站内信发送]</p>
	 * Created on 2015年1月26日
	 * @param webSiteMessageDTO
	 * @author:[杨阳]
	 */
	public ExecuteResult<String> addWebMessage(WebSiteMessageDTO webSiteMessageDTO);
	
	/**
	 * <p>Discription:[站内信数量统计]</p>
	 * Created on 2015年1月26日
	 * @param userId
	 * @param messageType
	 * @param ReadFlag
	 * @return
	 * @author:[杨阳]
	 */
	public int totalMessages (long userId, int messageType ,int readFlag);
	
	/**
	 * <p>Discription:[修改站内信阅读状态]</p>
	 * Created on 2015年1月26日
	 * @param ids
	 * @return
	 * @author:[杨阳]
	 */
	public ExecuteResult<String> modifyWebSiteMessage(String[] ids,String wmRead);
	
	/**
	 * <p>Discription:[按照参数查询站内信列表]</p>
	 * Created on 2015年1月27日
	 * @param webSiteMessageDTO
	 * @param page
	 * @return
	 * @author:[杨阳]
	 */
	public DataGrid<WebSiteMessageDTO> queryWebSiteMessageList(WebSiteMessageDTO webSiteMessageDTO ,Pager page);
	
	/**
	 * <p>Discription:[根据key获取redis中的值]</p>
	 * Created on 2015年2月5日
	 * @param key
	 * @return
	 * @author:[杨阳]
	 */
	public String getGeneralUserVerificationCode(String key);
	
	/**
	 * 
	 * <p>Discription:根据传入条件查询站内信详情</p>
	 * Created on 2015-3-3
	 * @param webSiteMessageDTO
	 * @return
	 * @author:yuht
	 */
	public  ExecuteResult<WebSiteMessageDTO> getWebSiteMessageInfo(WebSiteMessageDTO webSiteMessageDTO );
	
	/**
	 * 
	 * <p>消息计数</p>
	 * Created on 2016年3月22日
	 * @param webSiteMessageDTO
	 * @return
	 * @author: 顾雨
	 */
	public Long queryCount(WebSiteMessageDTO webSiteMessageDTO);
}
