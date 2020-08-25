package com.camelot.util;

import com.camelot.openplatform.util.SysProperties;

public class WeiXinMessageModeId {
	/**
	 *  注册成功
	 *  {{first.DATA}}
	 *  用户名：{{keyword1.DATA}}
	 *  注册时间：{{keyword2.DATA}}
	 *  {{remark.DATA}}"
	 *  【印刷家】尊敬的用户，欢迎您加入印刷家，请及时填写资质信息进行认证。
	 */
	public static String REGISTERED_SUCCESS = "A9UVhftLDhJM1aMsZw-kjbmD9J7XExqGFwI3aZ-ai9o";
	
	/**
	 * 修改密码
	 * {{first.DATA}}
	 * 您的{{productName.DATA}}密码已于{时间}成功修改。请知悉并确定这是您本人的操作。
	 * {{remark.DATA}}
	 * 【印刷家】尊敬的用户，您的{名字}密码已于{{time.DATA}}成功修改。请知悉并确定这是您本人的操作。
	 */
	public static String CHANGE_PASSWORD = SysProperties.getProperty("change_password");//"I5fRthr4N3RLaRKcCu0vylI2ACl_p0JMmD0SwNuMJxw";

	/**
	 * 重置密码
	 * {{first.DATA}}
	 * 用户ID：{{keyword1.DATA}}
	 * 操作：{{keyword2.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您的{名字}密码已于{时间}成功重置。请知悉并确定这是您本人的操作。
	 */
	public static String RESET_PASSWORD = "zrGmFF8gO0yPWmNIC69hyiJphUhKfjMGDHA-5Wu1KZQ";

	/**
	 * 绑定微信
	 * "{{first.DATA}}
	 * 绑定帐号：{{keyword1.DATA}}
	 * 绑定说明：{{keyword2.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，微信账号绑定成功。
	 */
	public static String BINDING_WECHAT = SysProperties.getProperty("binding_wechat");//"2CoaUwpEudQStyW2Cm1-lP0peDeBjyCzKQcrTxbyXsI";

	/**
	 * 解绑微信
	 * "{{first.DATA}}
	 *解绑帐号：{{keyword1.DATA}}
	 *解绑说明：{{keyword2.DATA}}
	 *{{remark.DATA}}"
	 * 【印刷家】尊敬的用户，微信账号解绑成功。
	 */
	public static String UNBUNDLING_WECHAT = SysProperties.getProperty("unbundling_wechat");//"EDakuUeq5lTJfmC98NALichjjC65VgeSRrovAwou7kw";

	/**
	 * 买家提交订单成功
	 * {{first.DATA}}
	 * 店铺：{{keyword1.DATA}}
	 * 下单时间：{{keyword2.DATA}}
	 * 商品：{{keyword3.DATA}}
	 * 金额：{{keyword4.DATA}}
	 * {{remark.DATA}}
	 * 【印刷家】尊敬的用户，订单****已提交成功，印刷家提醒您及时查看。
	 */
//	public static String SUBMIT_ORDERS = "BYKL95yZkB6gpmLIKVtDmVi2rzo7s_o0e_pN5VfG9b0";
	public static String SUBMIT_ORDERS = SysProperties.getProperty("submit_orders");//"BVBNTB5UsPNh5bVfvXWzi4kRUkdoeaPpalkLY5cxnAs";

	/**
	 * 订单取消
	 * "{{first.DATA}}
	 * 订单金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 收货信息：{{orderAddress.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您有一笔订单已取消，印刷家提醒您及时查看。
	 */
//	public static String ORDER_CANCELLATION = "UWGckgiAnTDbtVbxEFfGWvE389VxebdETu8xahGdHUk";
	public static String ORDER_CANCELLATION = SysProperties.getProperty("order_cancellation");//"JxN1R5CVxZK5b5-mSbRastrd79KY9x6CYOGn_bCac5c";

	/**
	 * 订单付款成功
	 * "{{first.DATA}}
	 * 订单金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 收货信息：{{orderAddress.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，订单成功完成付款，印刷家提醒您及时查看并尽快完成发货。
	 */
	public static String PAYMENT_SUCCESS = SysProperties.getProperty("payment_success");//"HVzXoesG5KZ5iRhkUkBB93xEJpEwBa33Q8sxyIeeDNY";

	/**
	 * 订单发货
	 * "{{first.DATA}}
	 * 用户ID：{{keyword1.DATA}}
	 * 操作：{{keyword2.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，订单****卖家已发货，印刷家提醒您及时查看。
	 */
//	public static String ORDER_DELIVERY = "zrGmFF8gO0yPWmNIC69hyiJphUhKfjMGDHA-5Wu1KZQ";
	public static String ORDER_DELIVERY = SysProperties.getProperty("order_delivery");//"dVx2doWXx2xgwQ_2_I071skz1u3K_---6rG6UaAJfUU";

	/**
	 * 订单确认收货
	 * "{{first.DATA}}
	 * 订单号：{{keyword1.DATA}}
	 * 商品名称：{{keyword2.DATA}}
	 * 下单时间：{{keyword3.DATA}}
	 * 发货时间：{{keyword4.DATA}}
	 * 确认收货时间：{{keyword5.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，订单****买家已确认收货，印刷家提醒您及时查看。
	 */
	public static String ORDER_CONFIRMATION = SysProperties.getProperty("order_confirmation");//"Ycc_SD1Qz1fgc46GxmwKMGqN64UxBk7donN2sGfxpR8";

	/**
	 * 买家申请退款提交
	 * "{{first.DATA}}
	 * 退款金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，买家用户****已经于****提交订单****退款申请，印刷家提醒您及时处理
	 */
	public static String  APPLICATION_FOR_DRAWBACK = SysProperties.getProperty("application_for_drawback");//"IXSVxcJ2MVRz70l2GqK4RJdxW4O8xNEbYCMlbEWH_G8";

	/**
	 * 买家申请售后提交
	 * "{{first.DATA}}
	 * 退款金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，买家用户****已经于****提交订单****售后服务申请，印刷家提醒您及时查看。
	 */
	public static String  APPLICATION_FOR_SALE = "IXSVxcJ2MVRz70l2GqK4RJdxW4O8xNEbYCMlbEWH_G8";

	/**
	 * 买家申请平台介入
	 * "{{first.DATA}}
	 * 用户ID：{{keyword1.DATA}}
	 * 操作：{{keyword2.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，买家用户****已经于****提交订单****平台仲裁，印刷家提醒您及时查看。
	 */
	public static String  APPLICATION_PLATFORM_INTERVENTION = "zrGmFF8gO0yPWmNIC69hyiJphUhKfjMGDHA-5Wu1KZQ";

	/**
	 * 平台仲裁处理结果
	 * "{{first.DATA}}
	 * 用户ID：{{keyword1.DATA}}
	 * 操作：{{keyword2.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，平台已对申诉订单进行了仲裁（订单编号：AAAAAA；仲裁时间：BBBBB），仲裁结果是：CCCCCC。
	 */
	public static String  ARBITRATION_PROCESSING_RESULTS = "zrGmFF8gO0yPWmNIC69hyiJphUhKfjMGDHA-5Wu1KZQ";

	/**
	 * 买家评价
	 * "{{first.DATA}}
	 * 用户ID：{{keyword1.DATA}}
	 * 操作：{{keyword2.DATA}}
	 * {{remark.DATA}}"
	 * 【【印刷家】尊敬的用户，买家xxxx对您的订单xxxx发表了评价，评价内容是：XXXX，印刷家提醒您及时查看，并对买家进行评价。
	 */
	public static String  BUYERS_EVALUATION = "zrGmFF8gO0yPWmNIC69hyiJphUhKfjMGDHA-5Wu1KZQ";

	/**
	 * 买家申请退款／退货结果
	 * "{{first.DATA}}
	 * 退款金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您收到一条退款/退货申请，请在退换货中进行查看。
	 */
	public static String  APPLICATION_FOR_REFUND = "IXSVxcJ2MVRz70l2GqK4RJdxW4O8xNEbYCMlbEWH_G8";

	/**
	 * 卖家确认退款／退货结果
	 * "{{first.DATA}}
	 * 退款金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您已同意xxxx申请的退款／退货，请在退换货中进行查看。
	 */
	public static String  CONFIRM_REFUND_RESULTS = "IXSVxcJ2MVRz70l2GqK4RJdxW4O8xNEbYCMlbEWH_G8";

	/**
	 * 卖家拒绝退款/ 退货结果
	 * "{{first.DATA}}
	 * 退款金额：{{orderProductPrice.DATA}}
	 * 商品详情：{{orderProductName.DATA}}
	 * 订单编号：{{orderName.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您收到一条退款/退货申请，请在退换货中进行查看。
	 */
	public static String  REFUSED_REFUND_RESULTS = "IXSVxcJ2MVRz70l2GqK4RJdxW4O8xNEbYCMlbEWH_G8";

	/**
	 * 询价-卖家接收询价信息
	 * "{{first.DATA}}
	 * 询价单位：{{keyword1.DATA}}
	 * 询价时间：{{keyword2.DATA}}
	 * 产品名称：{{keyword3.DATA}}
	 * 需求数量：{{keyword4.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，买家用户xxxx发布了关于xxxx商品的询价，印刷家提醒您及时查看。
	 */
	public static String  INQUIRY_INFORMATION = "ss2LBgy4Y-8xM-DZjPaHJy86w1yw67xWasrvnnukUmQ";

	/**
	 * 询价-买家接收报价信息
	 * "{{first.DATA}}
	 * 产品：{{keyword1.DATA}}
	 * 报价：{{keyword2.DATA}}
	 * 理由：{{keyword3.DATA}}
	 * 时间：{{keyword4.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，卖家用户xxxx提交了关于xxxx商品的报价，印刷家提醒您及时查看。
	 */
	public static String  RECEIVING_QUOTATION = "wNP_OLUO9yDPYCSprIwaun2kI68jUpZfQIma8SsrTK4";

	/**
	 * 协议-审核提醒
	 * "{{first.DATA}}
	 * 业务名称：{{keyword1.DATA}}
	 * 审核人：{{keyword2.DATA}}
	 * 审核时间：{{keyword3.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您有一条协议待审核，印刷家提醒您及时查看。
	 */
	public static String  AUDIT_REMINDER = "ikPDQ3RS9hggOL4U3yWh74yoQRa9Jd4QTVp153gh9RY";

	/**
	 * 协议-确认提醒（买家）
	 * "{{first.DATA}}
	 * 类型：{{keyword1.DATA}}
	 * 申请人：{{keyword2.DATA}}
	 * 申请时间：{{keyword3.DATA}}
	 * 内容详情：{{keyword4.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您有一条协议待确认，印刷家提醒您及时查看。
	 */
	public static String  CONFIRM_REMINDER_BUYERS = "Frto6tzLotCjQfhGf6zBa5FqEK8uXxdmDYluUholL1E";

	/**
	 * 协议-确认提醒（卖家）
	 * "{{first.DATA}}
	 * 类型：{{keyword1.DATA}}
	 * 申请人：{{keyword2.DATA}}
	 * 申请时间：{{keyword3.DATA}}
	 * 内容详情：{{keyword4.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，您有一条协议待确认，印刷家提醒您及时查看。
	 */
	public static String  CONFIRM_REMINDER_SELLER = "Frto6tzLotCjQfhGf6zBa5FqEK8uXxdmDYluUholL1E";

	/**
	 * 求购-卖家接收求购信息
	 * "{{first.DATA}}
	 * 提交时间：{{tradeDateTime.DATA}}
	 * 订单类型：{{orderType.DATA}}
	 * 客户信息：{{customerInfo.DATA}}
	 * {{orderItemName.DATA}}：{{orderItemData.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，买家用户xxxx发布了关于xxxx商品的求购，印刷家提醒您及时查看。
	 */
	public static String  PURCHASE_INFORMATION_SELLER = "2xsMsjMPqoauB-mPoWPh_S1a9C2cKBYSScm2j10m1ZM";

	/**
	 * 求购-买家接收求购信息
	 * "{{first.DATA}}
	 * 产品：{{keyword1.DATA}}
	 * 报价：{{keyword2.DATA}}
	 * 理由：{{keyword3.DATA}}
	 * 时间：{{keyword4.DATA}}
	 * {{remark.DATA}}"
	 * 【印刷家】尊敬的用户，卖家用户xxxx提交了关于xxxx商品的求购应答，印刷家提醒您及时查看。
	 */
	public static String  PURCHASE_INFORMATION_BUYERS = "wNP_OLUO9yDPYCSprIwaun2kI68jUpZfQIma8SsrTK4";
	
	/**
	 *  登录提示信息
	    {{first.DATA}}
		登录账号：{{keyword1.DATA}}
		登录时间：{{keyword2.DATA}}
		{{remark.DATA}}
	 */
	public static String LOGIN = SysProperties.getProperty("login");//"A9UVhftLDhJM1aMsZw-kjbmD9J7XExqGFwI3aZ-ai9o";
	
	/**
	 * 注册成功通知
	 *  {{first.DATA}}
		用户名：{{keyword1.DATA}}
		注册时间：{{keyword2.DATA}}
		{{remark.DATA}}
	 */
	public static String REGIST_SUCCESS = SysProperties.getProperty("regist_success");//"s_4cC_D05fUcANzpgoK9xDJcUNu5cr88RSjFo54xVX0";

	/**
	 * 协议-确认提醒
	 *  {{first.DATA}}
		类型：{{keyword1.DATA}}
		申请人：{{keyword2.DATA}}
		申请时间：{{keyword3.DATA}}
		内容详情：{{keyword4.DATA}}
		{{remark.DATA}}
	 * 
	 */
//	public static String CONTRACT_ADD_SUCCESS = "Frto6tzLotCjQfhGf6zBa5FqEK8uXxdmDYluUholL1E";
	public static String CONTRACT_ADD_SUCCESS = SysProperties.getProperty("contract_add_success");//"TMJBMZijK-hFta8C_BxvCu_o_hAh9dmXQLXFFB2nbx4";

	/**
	 * 新询价单通知
	    {{first.DATA}}
		询价用户：{{keyword1.DATA}}
		联系方式：{{keyword2.DATA}}
		电子邮箱：{{keyword3.DATA}}
		{{remark.DATA}}
	 */
	public static String REQUEST_PRICE_NOTICE = SysProperties.getProperty("request_price_notice");//"ss2LBgy4Y-8xM-DZjPaHJy86w1yw67xWasrvnnukUmQ";

	/**
	 * 询价单答复通知
	 *  {{first.DATA}}
		产品：{{keyword1.DATA}}
		报价：{{keyword2.DATA}}
		理由：{{keyword3.DATA}}
		时间：{{keyword4.DATA}}
		{{remark.DATA}}
	 */
	public static String REQUEST_PRICE_REC = SysProperties.getProperty("request_price_rec");//"wNP_OLUO9yDPYCSprIwaun2kI68jUpZfQIma8SsrTK4";
	
	
	/**
	 * 报价提醒
	 *  {{first.DATA}}
		交易编号：{{keyword1.DATA}}
		报价日期：{{keyword2.DATA}}
		{{remark.DATA}}
	 */
	public static String ASK_ITEM = SysProperties.getProperty("ask_item");//"BCi5-MEcMTIA-Al42FkRVsV5EGkql06TE6rpLrAgKXY";
}