package com.camelot.util;

public class MessageConstants {
	/**
	 * 模块类型：买家模块
	 */
	public static Integer MODULE_TYPE_BUYER = 1;
	
	/**
	 * 模块类型：卖家模块
	 */
	public static Integer MODULE_TYPE_SELLER = 2;
	
	/////////////////////////信息模版前缀、后缀key///////////////////////
	
	/**
	 * 站内信模版前缀
	 */
	public final static String WEBSITE_PREFIX = "website_prefix";
	
	/**
	 * 短信模版前缀
	 */
	public final static String SMS_PREFIX = "sms_prefix";
	
	/**
	 * 邮件模版前缀
	 */
	public final static String MAIL_PREFIX = "mail_prefix";
	
	/**
	 * 邮件主题模版前缀
	 */
	public final static String MAIL_TITLE_PREFIX = "mail_title_prefix";
	
	/**
	 * 邮件主题模版后缀
	 */
	public final static String MAIL_TITLE_SUBFFIX = "mail_title_subffix";
	
	/**
	 * 买家模版后缀
	 */
	public final static String BUYER_SUBFFIX = "buyer_subffix";
	
	/**
	 * 卖家模版后缀
	 */
	public final static String SELL_SUBFFIX = "sell_subffix";
	
	/**
	 * 邮件发送内容头信息
	 */
	public final static String MAIL_CONTENT_TOP = "mail_content_top";
	public final static String MAIL_CONTENT_TOP_GREEN = "mail_content_top_green";
	
	/**
	 * 邮件发送
	 */
	public final static String MAIL_CONTENT_BOTTOM = "mail_content_bottom";
	public final static String MAIL_CONTENT_BOTTOM_GREEN = "mail_content_bottom_green";
	
	/////////////////////////信息模版占位符///////////////////////
	
	/**
	 * 订单编号
	 */
	public final static String ORDER_NO = "order_no";
	
	/**
	 * 日期
	 */
	public final static String DATE = "date";
	/**
	 * 商品名称
	 */
	public final static String GOOD_NAME = "good_name";
	/**
	 * 店铺名称
	 */
	public final static String SHOP_NAME = "shop_name";
	/**
	 * 反馈结果
	 */
	public final static String BACK_RESULT = "back_result";
	/**
	 * 反馈结果后缀
	 */
	public final static String BACK_RESULT_SUBFFIX = "back_result_subffix";
	/**
	 * 申请单号
	 */
	public final static String CUSTOMER_SERVICE_SN = "customer_service_sn";
	/**
	 * 买家用户名称
	 */
	public final static String USER_NAME = "user_name";
	/**
	 * 卖家用户名称
	 */
	public final static String SELL_USER_NAME = "sell_user_name";
	/**
	 * 回复内容
	 */
	public final static String CONSULTATION_REPLY_CONTENT = "consultation_reply_content";
	/**
	 * 评价地址
	 */
	public final static String EVALUATION_URL = "evaluation_url";
	/**
	 * 回复天数
	 */
	public final static String BACK_MONEY_DAY = "back_money_day";
	/**
	 * 平台处理天数
	 */
	public final static String APPLY_PLANT_DAY = "apply_plant_day";
	
	/**
	 * 邮箱
	 */
	public final static String MAIL = "mail";
	
	/**
	 * 手机
	 */
	public final static String PHONE = "phone";
	
	/**
	 * 金额
	 */
	public final static String MONEY = "money";
	
	////////////////数据类型///////////////////
	
	/**
	 * 短信
	 */
	public final static String SMS_TYPE = "2";
	
	/**
	 * 邮件
	 */
	public final static String MAIL_TYPE = "1";
	
	/**
	 * 消息
	 */
	public final static Integer CONTENT_TYPE = 2;
	
	/**
	 * 验证码
	 */
	public final static Integer CODE_CONTENT_TYPE = 1;
	
	////////////redis站内信队列key///////////
		
	public final static String MESSAGE_KEY = "message";
	
	/////////是否需要数据库key/////////////////////
		
	public final static String USE_DB_KEY = "use_db";
	
	public final static String DB_YES = "1";
	
	public final static String DB_NO = "0";
	/**
	 * 求购名称
	 */
	public final static String ASK_TO_BUY = "ask_to_buy";
		
}
