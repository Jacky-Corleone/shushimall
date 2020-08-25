package com.camelot.basecenter.dao;

import com.camelot.basecenter.domain.BaseSmsConfig;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>Description: [短信、邮件配置、发送等基础服务]</p>
 * Created on 2015年1月23日
 * @author  <a href="mailto: xxx@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface BaseSmsConfigDAO extends BaseDAO<BaseSmsConfig> {
	public SmsConfigDTO getSmsConfig();
	
	/**
	 * <p>Discription:[获取绿印发短信信息]</p>
	 * Created on 2015年9月8日 
	 * @return
	 * @author:[李伟龙]
	 */
	public SmsConfigDTO getSmsConfigGreen();
	
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[创建者中文名字]
	 */
	public ExecuteResult<String> modifySMSConfig(SmsConfigDTO smsConfigDTO);
}