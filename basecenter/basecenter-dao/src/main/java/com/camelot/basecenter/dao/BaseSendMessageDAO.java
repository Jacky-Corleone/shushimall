package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>Description: [tdk设置DAO]</p>
 * Created on 2015年5月12日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface BaseSendMessageDAO extends BaseDAO<BaseSendMessageDTO> {
	
	public List<BaseSendMessageDTO> questyBaseSendMessageList(@Param("entity") BaseSendMessageDTO baseSendMessageDTO, @Param("page") Pager page);
	
	public void updateAll(@Param("messages")List<BaseSendMessageDTO> list);
	
	public void deleteSendMessage(@Param("entity") BaseSendMessageDTO baseSendMessageDTO);
	
}
