package com.camelot.mall;

/** 
 * <p>Description: [系统常用变量]</p>
 * Created on 2015年3月10日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class Constants {

	/**
	 * DESCRIPTION：用户Token
	 */
	public static final String USER_TOKEN = "logging_status";

	/**
	 * DESCRIPTION：用户名
	 */
	public static final String USER_NAME = "uname";

	/**
	 * DESCRIPTION：记住登录名用的
	 */
	public static final String LOG_NAME = "logname";
	
	/**
	 * DESCRIPTION：记住是否是记住登录
	 */
	public static final String AUTO_LOGON = "autologon";

	/**
	 * DESCRIPTION：用户ID
	 */
	public static final String USER_ID = "uid";

	/**
	 * DESCRIPTION：购物车临时Toke
	 */
	public static final String CART_TOKEN = "ctoken";

	/**
	 * DESCRIPTION：区域编码
	 */
	public static final String REGION_CODE = "regionCode";
	/**
	 * DESCRIPTION：区域名字
	 */
	public static final String REGION = "region";
	
	/**
	 * 表单重复提交Token
	 */
	public static final String TOKEN = "token";

	/**
	 * DESCRIPTION：记录请求地址
	 */
	public static final String REQUEST_URL = "requestUrl";
	
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
	
	/**
	 * 邮件发送
	 */
	public final static String MAIL_CONTENT_BOTTOM = "mail_content_bottom";
	
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
	 * 询价卖家姓名
	 */
	public final static String SELLER_NAME = "seller_name";
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
	public final static String USER_NAME_MESSAGE = "user_name";
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
	 * 协议名称
	 */
	public final static String AGREEMENT_NAME = "agreement_name";
	
	/**
	* 邮箱
	*/
	public final static String MAIL = "mail";
	
	/**
	 * 邮箱
	 */
	public final static String INQUIRY_NAME = "inquiry_name";
	
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
	* 短信（与mall中的sms_type，mail_type相对应）
	*/
	public final static String SMS_TYPE = "2";
	
	/**
	* 邮件
	*/
	public final static String MAIL_TYPE = "1";
	
	////////////redis站内信队列key///////////
	
	public final static String MESSAGE_KEY = "message1";
	
	/////////是否需要数据库key/////////////////////
	
	public final static String USE_DB_KEY = "use_db";
	
	public final static String DB_YES = "1";
	
	public final static String DB_NO = "0";

	////////////////买家中信账户名校验规则////////////////////////////
	public final static String YSJ_YES="舒适100";
	public final static String LY_YES="绿印";
	/**
	* 限制商品编辑列表sku数量
	*/
	public final static int LIMIT_SKU_NUM =64;
	
	/**
	* 商品在售状态
	*/
	public final static int ITEM_IS_SELL_STATUS =4;
	
	//////////////////成长值获取类型//////////////
	/*
	 * 登录获取
	 */
	public final static String GROWTH_VALUE_LOGIN = "1";
	/*
	 * 购物获取
	 */
	public final static String GROWTH_VALUE_SHOPPING = "2";
	/*
	 * 评价获取
	 */
	public final static String GROWTH_VALUE_EVALUATION = "3";
	/*
	 * 评价+晒照获取
	 */
	public final static String GROWTH_VALUE_EVALUATION_AND_EXPOSURE = "4";
	/*
	 * 针对登录拦截，查询redis使用
	 */
	public final static String GROWTH_FOR_LOGIN = "GrowthValueForLogin_";

	
	//个人
	public final static String INDIVIDUAL_STATUS = "1";
	//企业
	public final static String ENTERPRISE_STATUS = "2";
	//求购名称
	public final static String ASK_NAME = "ask_name";
	
	// '客服类型：2平台客服，1店铺客服'
	public final static Integer TYPE_ECM = 2;
	public final static Integer TYPE_SHOP = 1;
	//'是否逻辑删除标记：0在用，未伪删除；1停用，已伪删除'
	public final static Integer IS_DEL = 1;
	public final static Integer NO_DEL = 0;
	//add 新增  modify 修改
	public final static String ADD = "add";
	public final static String MODIFY = "modify";
	//是否默认客服 0 否 1是
	public final static Integer IS_DEFAULT = 1;
	public final static Integer NO_DEFAULT = 0;
}
