package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.domain.BaseWebsiteMessage;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>Description: [站内信服务]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface BaseWebSiteMessageDAO extends BaseDAO<WebSiteMessageDTO> {
	
	/**
	 * <p>Discription:[按照参数类型查询站内信列表]</p>
	 * Created on 2015年1月27日
	 * @param webSiteMessageDTO
	 * @param page
	 * @return
	 * @author:[杨阳]
	 */
	public List<WebSiteMessageDTO> queryWebSiteMessageList(@Param("entity") WebSiteMessageDTO webSiteMessageDTO, @Param("page") Pager page);
	
	/**
	 * <p>Discription:[站内信修改阅读状态]</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @author:[杨阳]
	 */
	public void modifyWebSiteMessage(@Param("id")String id,@Param("wmRead") String wmRead);
	
	/**
	 * <p>Discription:[站内信数量统计]</p>
	 * Created on 2015年1月26日
	 * @param webSiteMessageDTO
	 * @return
	 * @author:[杨阳]
	 */
	public int totalMessages(BaseWebsiteMessage baseWebsiteMessage);
	/**
	 * 
	 * <p>Discription:[根据输入参数返回站内信详情]</p>
	 * Created on 2015-3-3
	 * @param webSiteMessageDTO
	 * @return
	 * @author:yuht
	 */
	public WebSiteMessageDTO getWebSiteMessageInfo(@Param("entity") WebSiteMessageDTO webSiteMessageDTO);
	
	/**
	 * <p>消息计数</p>
	 * Created on 2016年3月22日
	 * @param webSiteMessageDTO
	 * @return
	 * @author:[顾雨]
	 */
	Long queryCount(@Param("entity") WebSiteMessageDTO webSiteMessageDTO);
}