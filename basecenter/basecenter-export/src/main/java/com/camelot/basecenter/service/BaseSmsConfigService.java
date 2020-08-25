package com.camelot.basecenter.service;
import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsConfigDTO;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.openplatform.common.ExecuteResult;

public interface BaseSmsConfigService {

	/**
	 * <p>Discription:[消息基础配置查询]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[周乐]
	 */
	public SmsConfigDTO getSmsConfig();
	
	/**
	 * <p>Discription:[绿印消息基础配置查询]</p>
	 * Created on 2015年9月8日
	 * @return
	 * @author:[李伟龙]
	 */
	public SmsConfigDTO getSmsConfigGreen();
	
	/**
	 * <p>Discription:[修改邮箱/短信配置信息]</p>
	 * Created on 2015年1月23日
	 * @return
	 * @author:[周乐]
	 */
	public ExecuteResult<String> modifySMSConfig(SmsConfigDTO smsConfigDTO);
	
	/**
	 * <p>Discription:[获取发送短信的状态报告]</p>
	 * Created on 2015年5月28日
	 * @param iReqType 请求类型(1:上行 2: 状态报告)
	 * @return
	 * @author:[王东晓]
	 */
	public void mongateGetDeliver(int iReqType);
}
