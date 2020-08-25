package com.camelot.openplatform.common.email;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: [邮件发送工具类]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class SendMailUtil {
	private final static Logger logger = LoggerFactory.getLogger(SendMailUtil.class);
	
	private static final String charset = "GBK";
    private static final String defaultMimetype = "text/plain";
    
    //发件人名称
    private String sender;
    
    //发件人地址
    private String sendAddress;
    
    //服务器类型
    private EmailType emailType;
    
    //接收服务器
    private String receiveServer;
    
    //接收服务器端口
    private String receiveServerPort;
    
    //发送服务器
    private String sendServer;
    
    //发送服务器端口
    private String sendServerPort;
    
    //邮箱发送账号 
    private String username;
    
    //邮箱发送密码
    private String password;
    
	public static void main(String[] args) throws Exception {
        /*
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.camelotchina.com");
        props.setProperty("mail.smtp.auth", "true");
         */
		SendMailUtil sender = new SendMailUtil();
		sender.setSender("舒适100");
		sender.setSendAddress("survey@camelotchina.com");
		sender.setEmailType(EmailType.SMTP);
		sender.setSendServer("smtp.camelotchina.com");
		sender.setUsername("survey@camelotchina.com");
		sender.setPassword("123123b");
    	sender.send(new String[]{"zhoulenihao@126.com"}, "您好，我是科印集团", "<b>周乐乐啦啦啦啦啦啦啦13579</b>", null , "text/html");
    }
	
	public SendMailUtil(){
	}
	
    /**
     * 发送邮件
     * @param receiver 收件人
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     */
    public boolean send(String receiver, String subject, String mailContent, String mimetype) {
    	return send(new String[]{receiver}, subject, mailContent, mimetype);
    }
    /**
     * 发送邮件
     * @param receivers 收件人
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     */
    public boolean send(String[] receivers, String subject, String mailContent, String mimetype) {
    	return send(receivers, subject, mailContent, null, mimetype);
    }
    /**
     * 发送邮件
     * @param receivers 收件人
     * @param subject 标题
     * @param mailContent 邮件内容
     * @param attachements 附件
     * @param mimetype 内容类型 默认为text/plain,如果要发送HTML内容,应设置为text/html
     */
    public boolean send(String[] receivers, String subject, String mailContent, File[] attachements, String mimetype) {
        Properties props = new Properties();
        props.put("mail.transport.protocol", this.getEmailType().name().toLowerCase());
        props.put("mail.smtp.host", this.getSendServer());//smtp服务器地址
        props.put("mail.smtp.auth", "true");//需要校验

        final String account = this.getUsername();
        final String pwd = this.getPassword();

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(account,pwd);//登录用户名/密码
            }
        });
        session.setDebug(true);
        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(account));//发件人邮件

            InternetAddress[] toAddress = new InternetAddress[receivers.length];
            for (int i=0; i<receivers.length; i++) {
                toAddress[i] = new InternetAddress(receivers[i]);
            }
            mimeMessage.setRecipients(Message.RecipientType.TO, toAddress);//收件人邮件
            mimeMessage.setSubject(subject, charset);
            
            Multipart multipart = new MimeMultipart();
            //正文
            MimeBodyPart body = new MimeBodyPart();
            body.setContent(mailContent, (mimetype!=null && !"".equals(mimetype) ? mimetype : defaultMimetype)+ ";charset="+ charset);
            multipart.addBodyPart(body);//发件内容
            //附件
            if(attachements!=null){
	            for (File attachement : attachements) {
	                MimeBodyPart attache = new MimeBodyPart();
	                attache.setDataHandler(new DataHandler(new FileDataSource(attachement)));
	                String fileName = getLastName(attachement.getName());
	                attache.setFileName(MimeUtility.encodeText(fileName, charset, null));
	                multipart.addBodyPart(attache);
	            }
            }
            mimeMessage.setContent(multipart);
            // SimpleDateFormat formcat = new SimpleDateFormat("yyyy-MM-dd");            
            mimeMessage.setSentDate(new Date());//formcat.parse("2010-5-23")
            Transport.send(mimeMessage);
            return true;
        } catch (Exception e) {
        	logger.error("邮件主题为【"+subject+"】的邮件发送失败："+e);
        	return false;
        }
    }

    private static String getLastName(String fileName) {
        int pos = fileName.lastIndexOf("\\");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        pos = fileName.lastIndexOf("/");
        if (pos > -1) {
            fileName = fileName.substring(pos + 1);
        }
        return fileName;
    }
	
    /**
     * 邮件类型枚举类
     */
    public static enum EmailType{
    	SMTP,POP3,IMAP
    }
    
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getSendAddress() {
		return sendAddress;
	}
	
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	
	public EmailType getEmailType() {
		return emailType;
	}
	
	public void setEmailType(EmailType emailType) {
		this.emailType = emailType;
	}
	
	public String getReceiveServer() {
		return receiveServer;
	}
	
	public void setReceiveServer(String receiveServer) {
		this.receiveServer = receiveServer;
	}
	
	public String getReceiveServerPort() {
		return receiveServerPort;
	}
	
	public void setReceiveServerPort(String receiveServerPort) {
		this.receiveServerPort = receiveServerPort;
	}
	
	public String getSendServer() {
		return sendServer;
	}
	
	public void setSendServer(String sendServer) {
		this.sendServer = sendServer;
	}
	
	public String getSendServerPort() {
		return sendServerPort;
	}
	
	public void setSendServerPort(String sendServerPort) {
		this.sendServerPort = sendServerPort;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}
