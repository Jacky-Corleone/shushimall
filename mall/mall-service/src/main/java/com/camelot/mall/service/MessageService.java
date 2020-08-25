package com.camelot.mall.service;

import com.camelot.basecenter.dto.EmailTypeEnum;
import com.camelot.basecenter.dto.SmsTypeEnum;
import com.camelot.openplatform.common.ExecuteResult;

/**
 * Created by Administrator on 2015/6/12.
 */
public interface MessageService {

    /**
     * <p>Discription:[发送邮箱/短信验证码]</p>
     * Created on 2015年1月26日
     * @param verificationCodes 电话号码或邮箱地址
     * @param verificationCodeType  1 邮箱发验证码  2 短信发验证码
     * @param key  存入redis中的key值
     * @return
     * @author:[杨阳]
     */
    public ExecuteResult<String> sendVerificationCode(String enumType, String key , String[] verificationCodes ,int verificationCodeType);

    /**
     * <p>Discription:[验证码保存到redis并设置有效时间]</p>
     * Created on 2015年1月28日
     * @param generalUserVerificationCode
     * @author:[杨阳]
     */
    public void saveGeneralUserVerificationCode(String key , String generalUserVerificationCode);

    /**
     * <p>Discription:[短信发送]</p>
     * Created on 2015年1月26日
     * @param telephone 手机号码
     * @param content	短信内容
     * @param smsType 短信类型
     * @return
     * @author:[周乐]
     */
    public ExecuteResult<String> sendMessage(SmsTypeEnum smsType,String telephone, String content);

    /**
     * <p>Discription:[短信发送]</p>
     * Created on 2015年1月26日
     * @param telephone 手机号码
     * @param content	短信内容
     * @return
     * @author:[周乐]
     */
    public ExecuteResult<String> sendSms(String telephone, String content);

    /**
     * <p>Discription:[邮件发送]</p>
     * Created on 2015年1月26日
     * @param account 接收邮件的邮箱账号
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return
     * @author:[周乐]
     */
    public ExecuteResult<String> sendEmail(String account, String subject, String content);
    /**
     * <p>Discription:[根据模版，邮件发送]</p>
     * Created on 2015年5月15日
     * @param emailType 使用的邮箱模版
     * @param account 接收邮件的邮箱账号
     * @param subject 邮件主题
     * @param content 邮件内容
     * @return
     * @author:[王东晓]
     */
    public ExecuteResult<String> sendEmailByType(EmailTypeEnum emailType,String account, String subject, String content);
}
