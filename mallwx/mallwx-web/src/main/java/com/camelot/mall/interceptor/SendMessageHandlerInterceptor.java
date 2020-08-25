package com.camelot.mall.interceptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.CookieHelper;
import com.camelot.activeMQ.service.MessagePublisherService;
import com.camelot.aftersale.dto.returngoods.TradeReturnGoodsDTO;
import com.camelot.aftersale.dto.returngoods.TradeReturnInfoDto;
import com.camelot.aftersale.service.TradeReturnExportService;
import com.camelot.basecenter.dto.BaseConsultingSmsDTO;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseConsultingSmsService;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.util.Json;
import com.camelot.mall.util.MessageTemplateFileUtil;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.payment.dto.OrderInfoPay;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.SendMessageUser;
import com.camelot.util.WebSiteMessageResult;
import com.camelot.util.WebUtil;
/**
*
* <p>Description: [站内信发送拦截器]</p>
* Created on 2015年3月4日
* @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
* @version 1.0SendMessageHandlerInterceptor.java
* Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
*/
public class SendMessageHandlerInterceptor implements HandlerInterceptor {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	private UserExportService userExportService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	@Resource
	private BaseWebSiteMessageService baseWebSiteMessageService;
	@Resource
	private TradeReturnExportService tradeReturnExportService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private ItemSkuInquiryPriceExportService inquiryService;
	@Resource
	private BaseConsultingSmsService consultService;
	@Resource
	private BaseSendMessageService baseSendMessageService;
	@Resource
	private MessagePublisherService emailNoticQueuePublisher;
	@Resource
	private MessagePublisherService smsNoticQueuePublisher;
	@Resource
	private RedisDB redisDB;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView){
		log.info("进入消息拦截器");
		try {
			MessageTemplateFileUtil messageTemplateFileUtil = MessageTemplateFileUtil.getInstance();
			if(messageTemplateFileUtil == null){
				log.info("未读取到message_template.properties文件");
				return;
			}
			//获取节点编码
			log.info("获取节点编码");
			String temp_type = this.getWebSiteMessageType(handler, messageTemplateFileUtil);
			log.info("节点编码获取成功:"+temp_type);
			if(StringUtils.isEmpty(temp_type))
				return;
			//获取信息发送实体
			WebSiteMessageResult webSiteMessageResult = 
					request.getAttribute("webSiteMessageResult") == null ? 
					new WebSiteMessageResult():(WebSiteMessageResult)request.getAttribute("webSiteMessageResult");
			webSiteMessageResult.setTempType(temp_type);
			//对某些信息发送节点做出特殊处理
			log.info("节点特殊处理");
			boolean result = specialHandle(webSiteMessageResult,request,response);
			log.info("节点特殊处理完成，反馈结果:"+result);
			if(!result)
				return;
			//获取节点编码
			log.info("获取节点编码:");
			String code = webSiteMessageResult.getTempType();
			log.info("节点编码获取成功:");
			//获取发送人集合
			log.info("获取发送人集合");
			List<SendMessageUser> sendMessageUserList = getSendMessageUserList(webSiteMessageResult);
			log.info("发送人集合获取成功 数量:"+sendMessageUserList.size());
			//推送信息
			log.info("开始消息推送");
			sendMessage(messageTemplateFileUtil,sendMessageUserList,code,webSiteMessageResult);
			log.info("消息推送完成");
		} catch (Exception e) {
			log.debug("调用发送站内信、短信、邮件提醒拦截器发生错误，错误信息:" + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
	}
	
	/**
	 * 各节点特殊处理
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean specialHandle(WebSiteMessageResult webSiteMessageResult,
			HttpServletRequest request, HttpServletResponse response){
		String temp_type = webSiteMessageResult.getTempType();
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		Object obj = request.getAttribute("result");
		ExecuteResult<String> result = obj == null ? null : (ExecuteResult<String>)obj;
		if(result != null && !result.isSuccess()){
			return false;
		}
		//卖家改价
		if("modify_order_price_success".equals(temp_type)){
			String goodsId = request.getParameter("itemId");
			webSiteMessageResult.setGoodsId(Long.parseLong(goodsId));
		}
		//卖家回复买家评价成功
		
		if("replyBuyers_success".equals(temp_type)){
			//发送给买家
			String buyerId = request.getAttribute("buyerId")==null?"":request.getAttribute("buyerId").toString();
			String orderId = request.getAttribute("orderId")==null?"":request.getAttribute("orderId").toString();
			String goodId = request.getAttribute("goodId")==null?"":request.getAttribute("goodId").toString();
			webSiteMessageResult.setBuyerId(Long.valueOf(buyerId));
			webSiteMessageResult.setOrderId(orderId);
			webSiteMessageResult.setGoodsId(Long.parseLong(goodId));
			webSiteMessageResult.setContent(request.getAttribute("content").toString());
			return true;
		}
		//订单提交成功
		if("order_submit_success".equals(temp_type)){
			//获取返回的数据
			if(StringUtils.isNotEmpty(result.getResult())){
				webSiteMessageResult.setOrderId(result.getResult());
				return true;
			}
			return false;
		}
		//订单取消
		if("order_cancle".equals(temp_type)){
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		//订单发货
		if("sell_delivery_success".equals(temp_type)){
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		//买家评价 卖家评价
		if("buyer_order_evaluation".equals(temp_type) || "seller_evaluation".equals(temp_type)){
			//获取反馈结果
			Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
			Object meObj = map.get("messages");
			if(meObj == null){
				return false;
			}
			String message = meObj.toString();
			//提交成功
			if(!"提交成功!".equals(message)){
				return false;
			}
			String content = request.getParameter("content") == null ? "" : request.getParameter("content").toString();
			webSiteMessageResult.setContent(content);
		}
			
		//询价
		if("buyer_inquiry".equals(temp_type)){
    		//存放买家、卖家 供发送站内消息使用
    		webSiteMessageResult.setSellerId(Long.valueOf(request.getParameter("sellerId")));
    		webSiteMessageResult.setBuyerId(Long.valueOf(request.getParameter("buyerId")));
    		webSiteMessageResult.setGoodsId(Long.valueOf(request.getParameter("itemId")));
		}
		//咨询 
		if("buyer_consultation".equals(temp_type)){
			String buyerId = CookieHelper.getCookieVal(request, com.camelot.mall.Constants.USER_ID);
			String itemId = request.getParameter("itemId");
			String sellerId = request.getParameter("sellerId");
			webSiteMessageResult.setSellerId(Long.valueOf(sellerId));
    		webSiteMessageResult.setBuyerId(Long.valueOf(buyerId));
    		webSiteMessageResult.setGoodsId(Long.valueOf(itemId));
		}
		//询价回复
		if("seller_inquiry_reply".equals(temp_type)){
			String id = request.getParameter("id");
			if(StringUtils.isEmpty(id))
				return false;
			Long insId = new Long(id);
			//查询询价数据
			ExecuteResult<ItemSkuInquiryPriceDTO> inqResult = this.inquiryService.queryById(insId);
			if(!inqResult.isSuccess())
				return false;
			ItemSkuInquiryPriceDTO dto = inqResult.getResult();
			webSiteMessageResult.setSellerId(dto.getSellerId());
    		webSiteMessageResult.setBuyerId(dto.getBuyerId());
    		webSiteMessageResult.setGoodsId(dto.getItemId());
		}
		//咨询回复
		if("seller_consultation_reply".equals(temp_type)){
			String id = request.getParameter("id");
			if(StringUtils.isEmpty(id))
				return false;
			Long insId = new Long(id);
			BaseConsultingSmsDTO dto = new BaseConsultingSmsDTO();
			dto.setId(insId);
			//查询咨询数据
			DataGrid<BaseConsultingSmsDTO> inqResultList = this.consultService.queryList(dto, null);
			if(inqResultList == null || inqResultList.getRows() == null || inqResultList.getRows().size() == 0)
				return false;
			dto = inqResultList.getRows().get(0);
			if(dto == null)
				return false;
			webSiteMessageResult.setSellerId(dto.getSellerId());
    		webSiteMessageResult.setBuyerId(dto.getBuyerId());
    		webSiteMessageResult.setGoodsId(dto.getItemId());
		}
		//买家申请退款/售后
		if("refund_agreement_submit".equals(temp_type)){
			TradeReturnInfoDto insertDto = (TradeReturnInfoDto)request.getAttribute("insertDto");
			if(insertDto == null)
				return false;
			TradeReturnGoodsDTO tradeReturnDto = insertDto.getTradeReturnDto();
			if(tradeReturnDto == null)
				return false;
			//判断是售后申请还是退款申请
			if("1".equals(tradeReturnDto.getIsCustomerService()))
				webSiteMessageResult.setTempType("refund_agreement_submit_sh");
			else
				webSiteMessageResult.setTempType("refund_agreement_submit_tk");
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		if("update_trade_return".equals(temp_type)){
			String returnId = request.getParameter("returnId");
			if(StringUtils.isEmpty(returnId))
				return false;
			ExecuteResult<TradeReturnInfoDto> tradeReturnResult = tradeReturnExportService.getTradeReturnInfoByReturnGoodsId(returnId);
			if(!tradeReturnResult.isSuccess() || tradeReturnResult.getResult() == null)
				return false;
			TradeReturnGoodsDTO tradeReturnDto = tradeReturnResult.getResult().getTradeReturnDto();
			if(tradeReturnDto == null)
				return false;
			String type = request.getParameter("type");
			//同意退款/不同意退款状态
			if(!("1".equals(type) || "2".equals(type)))
				return false;
			//判断是否是售后服务
			if("1".equals(tradeReturnDto.getIsCustomerService()))
				webSiteMessageResult.setTempType("update_trade_return_sh");
			else
				webSiteMessageResult.setTempType("update_trade_return_tk");
			webSiteMessageResult.setDate(myFmt.format(new Date()));
			webSiteMessageResult.setOrderId(tradeReturnDto.getOrderId());
		}
		if("seller_eidt_information".equals(temp_type)){
			Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
			Object meObj = map.get("message");
			if(meObj == null)
				return false;
			String message = meObj.toString();
			//提交成功
			if(!"修改成功".equals(message))
				return false;
		}
		//修改登录 /支付密码、设置支付密码
		if("edit_login_password".equals(temp_type) || "edit_pay_password".equals(temp_type)||"set_pay_password".equals(temp_type)){
			Object editResultObj = request.getAttribute("result");
			if(editResultObj == null)
				return false;
			ExecuteResult<String> editResult = (ExecuteResult<String>)editResultObj;
			if(!editResult.isSuccess())
				return false;
			//获取登录用户
			String logname = request.getParameter("logname");
			String uid = request.getParameter("uid");
			if(StringUtils.isNotEmpty(logname)){
				UserDTO userDTO = new UserDTO();
				userDTO.setUname(logname);
				DataGrid<UserDTO> dg = this.userExportService.findUserListByCondition(userDTO, null, null);
				if(dg == null || dg.getRows() == null || dg.getRows().size() == 0){
					return false;
				}
				List<UserDTO> userDTOList = dg.getRows();
				userDTO = userDTOList.get(0);
				webSiteMessageResult.setBuyerId(userDTO.getUid());
			}else if(StringUtils.isNotEmpty(uid)){
				webSiteMessageResult.setBuyerId(new Long(uid));
			}
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		//修改邮箱/手机
		if("eidt_phone_or_mail".equals(temp_type)){
			//判断修改是否成功
			Object resultObj = request.getAttribute("isSuccess");
			if(resultObj == null)
				return false;
			Boolean isSuccess = (Boolean)resultObj;
			if(!isSuccess)
				return false;
			String uid = request.getParameter("uid");
			if(StringUtils.isNotEmpty(uid)){
				webSiteMessageResult.setBuyerId(new Long(uid));
			}
			//获取修改的类型
			String changeType = request.getParameter("changeType");
			String address = request.getParameter("address");
			if("phone".equals(changeType)){
				webSiteMessageResult.setTempType("edit_phone");
				webSiteMessageResult.setPhone(address);
			}else{
				webSiteMessageResult.setTempType("edit_mail");
				webSiteMessageResult.setMail(address);
			}
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		//提现
		if("security_withdraw".equals(temp_type)){
			//判断是否成功
			Object withdrawObj = request.getAttribute("withdrawResult");
			if(withdrawObj == null)
				return false;
			ExecuteResult<String> withdrawResult = (ExecuteResult<String>)withdrawObj;
			if(!withdrawResult.isSuccess())
				return false;
			//获取金额
			String money = request.getParameter("withdrawPrice");
			//获取用户
			String token = LoginToken.getLoginToken(request);
			RegisterDTO registerDTO = userExportService.getUserByRedis(token);
			webSiteMessageResult.setBuyerId(registerDTO.getUid());
			webSiteMessageResult.setMoney(money);
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		if("withdraw_deposit".equals(temp_type)){
			Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
			Boolean isSuccess = (Boolean)map.get("success");
			if(!isSuccess)
				return false;
			//获取金额
			String money = request.getParameter("amount");
			//用户
			Long uid = WebUtil.getInstance().getUserId(request);
			webSiteMessageResult.setSellerId(uid);
			webSiteMessageResult.setMoney(money);
			webSiteMessageResult.setDate(myFmt.format(new Date()));
		}
		//支付成功
		if("order_pay_online_success".equals(temp_type)){
			
			
			log.info("线上支付提醒开始...");
			Object resultObj = request.getAttribute("er");
			if(resultObj == null)
				return false;
			ExecuteResult<OrderInfoPay> er = (ExecuteResult<OrderInfoPay>)resultObj;
			if(!er.isSuccess())
				return false;
			//获取订单
			log.info("线上支付提醒订单数据获取");
			OrderInfoPay orderInfoPay = er.getResult();
			//如果这个订单号的支付成功已经给买家和卖家发送过通知，则不再发送（主要是拦截，银行异步推送支付信息，可能会多次拦截到银行的推送信息）
			String sendMessageResult = redisDB.get("PAYSUCCESS"+orderInfoPay.getOrderNo());
			if("success".equals(sendMessageResult)){
				return false;
			}
			int seconds =3600*24*10;//10天
			redisDB.setAndExpire("PAYSUCCESS"+orderInfoPay.getOrderNo(), "success", seconds);
			
			//同一个订单号支付成功只给买家和卖家提醒一次
			webSiteMessageResult.setOrderId(orderInfoPay.getOrderNo());
			webSiteMessageResult.setDate(myFmt.format(new Date()));
			
			log.info("线上支付提醒订单数据获取成功");
		}
		//卖家确认收款
		if("order_pay_line_success".equals(temp_type)){
			log.info("卖家确认收款开始...");
			Object resultObj = request.getAttribute("result");
			if(resultObj == null)
				return false;
			ExecuteResult<String> modifyOrderPayStatusResult = (ExecuteResult<String>)resultObj;
			if(!modifyOrderPayStatusResult.isSuccess())
				return false;
			
			log.info("卖家确认收款提醒数据获取成功");
		}
		//平台仲裁
		if("complain_add".equals(temp_type)){
			Json json = (Json)request.getAttribute("json");
			//判断是否成功
			if(!json.isSuccess())
				return false;
			webSiteMessageResult.setDate(myFmt.format(new Date()));
			//获取类型
			String sellerOrBuyer=request.getParameter("sellerOrBuyer");
			if("1".equals(sellerOrBuyer)){
				//买家投诉
				webSiteMessageResult.setTempType("complain_add_buyer");
			}else{
				//卖家投诉
				webSiteMessageResult.setTempType("complain_add_seller");
			}
		}
		//注册成功
		if("enterprice_regist_success".equals(temp_type)
				|| "person_regist_success".equals(temp_type)){
			if(request.getAttribute("map") == null)
				return false;
			Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
			//获取反馈结果
			Object messageObj = map.get("message");
			if(messageObj == null)
				return false;
			if(!"操作成功!".contains(messageObj.toString()))
				return false;
			//如果是调用企业注册方法，获取注册类型
			if("enterprice_regist_success".equals(temp_type)){
				String type = request.getParameter("type");
				//注册类型如果是个人认证则不需要发送提示消息
				if("buyer".equals(type)){
					return false;
				}
			}
			//用户用户id
			Object userIdObj = map.get("userId");
			if(userIdObj == null || userIdObj.toString().equals("0"))
				return false;
			webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			webSiteMessageResult.setTempType("regist_success");
		}
		//买家或者卖家基本信息修改
		if("modify_seller_info".equals(temp_type) || "modify_buyer_info".equals(temp_type)){
			if(request.getAttribute("map") == null)
				return false;
			Map<String,Object> map = (Map<String,Object>)request.getAttribute("map");
			//获取反馈结果
			Object messageObj = map.get("message");
			if(messageObj == null)
				return false;
			if(!"操作成功!".contains(messageObj.toString()))
				return false;
			//获取用户
			Object userIdObj = request.getParameter("userId");
			if(userIdObj == null)
				return false;
			webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			webSiteMessageResult.setSellerId(new Long(userIdObj.toString()));
		}
		//获取订单id
		String orderId = request.getParameter("orderId");
		if(StringUtils.isNotEmpty(orderId) && webSiteMessageResult.getOrderId() == null){
			webSiteMessageResult.setOrderId(orderId);
		}
		return true;
	}
	
	/**
	 * 校验信息是否发送
	 * @param wmContext
	 * @return
	 */
	private boolean validateMessageIsSend(String wmContext){
		WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
		webSiteMessageDTO.setWmContext(wmContext);
		DataGrid<WebSiteMessageDTO> dg = this.baseWebSiteMessageService.queryWebSiteMessageList(webSiteMessageDTO,null);
		if(dg == null || dg.getRows() == null || dg.getRows().size() == 0)
			return false;
		return true;
	}
	
	/**
	 * 替换信息内容
	 * @param content
	 * @return
	 */
	private String contentReplace(String content,UserDTO buyer,UserDTO seller,WebSiteMessageResult webSiteMessageResul,String orderId){
		if(StringUtils.isEmpty(content))
			return "";
		//获取店铺
		ShopDTO shopDTO = new ShopDTO();
		if(seller != null){
			shopDTO.setSellerId(seller.getUid());
			ExecuteResult<DataGrid<ShopDTO>> shopResult = shopExportService.findShopInfoByCondition(shopDTO, null);
			if(shopResult.isSuccess()){
				List<ShopDTO> shopDTOList = shopResult.getResult().getRows();
				if(shopDTOList != null && shopDTOList.size() > 0){
					shopDTO = shopDTOList.get(0);
				}
			}
		}
		//获取商品
		ExecuteResult<ItemDTO> itemResult = itemExportService.getItemById(webSiteMessageResul.getGoodsId());
		ItemDTO itemDTO = itemResult.getResult();
		//获取退款/退货
//		TradeReturnInfoQueryDto tradeReturnInfoQueryDto = new TradeReturnInfoQueryDto();
//		TradeReturnGoodsDTO tradeReturnGoodsDTO = new TradeReturnGoodsDTO();
//		tradeReturnGoodsDTO.setId(webSiteMessageResul.getApplyId());
//		tradeReturnInfoQueryDto.setTradeReturnDto(tradeReturnGoodsDTO);
//		DataGrid<TradeReturnGoodsDTO> dg = tradeReturnExportService.getTradeReturnInfoDto(null,tradeReturnInfoQueryDto);
//		if(dg.getRows() != null && dg.getRows().size() > 0)
//			tradeReturnGoodsDTO = dg.getRows().get(0);
		//替换订单编号
		content = content.replaceAll(Constants.ORDER_NO, orderId);
		//替换日期
		content = content.replaceAll(Constants.DATE, webSiteMessageResul.getDate() == null ? "" : webSiteMessageResul.getDate());
		//替换商品名称
		content = content.replaceAll(Constants.GOOD_NAME,itemDTO == null ? "" : itemDTO.getItemName());
		//替换店铺名称
		content = content.replaceAll(Constants.SHOP_NAME, shopDTO == null ? "" : shopDTO.getShopName());
		//替换申请单号
		//content = content.replaceAll(Constants.CUSTOMER_SERVICE_SN, tradeReturnGoodsDTO == null ? "" : tradeReturnGoodsDTO.getCodeNo());
		//替换卖家用户名称
		content = content.replaceAll(Constants.SELL_USER_NAME, seller == null ? "" : seller.getUname());
		//替换买家用户名称
		content = content.replaceAll(Constants.USER_NAME_MESSAGE, buyer == null ? "" : buyer.getUname());
		//替换邮箱
		content = content.replaceAll(Constants.MAIL, webSiteMessageResul.getMail() == null ? "" : webSiteMessageResul.getMail());
		//替换手机
		content = content.replaceAll(Constants.PHONE, webSiteMessageResul.getPhone() == null ? "" : webSiteMessageResul.getPhone());
		//替换金额
		content = content.replaceAll(Constants.MONEY, webSiteMessageResul.getMoney() == null ? "" : webSiteMessageResul.getMoney());
		//替换回复内容
		content = content.replaceAll(Constants.CONSULTATION_REPLY_CONTENT, webSiteMessageResul.getContent());
		//替换回复天数
		content = content.replaceAll(Constants.BACK_MONEY_DAY, "10");
		//替换平台处理天数
		content = content.replaceAll(Constants.APPLY_PLANT_DAY, "10");
		return content;
	}
	
	/**
	 * 获取节点编码
	 * @param handler
	 * @param messageTemplateFileUtil
	 * @return
	 */
	private String getWebSiteMessageType(Object handler,MessageTemplateFileUtil messageTemplateFileUtil){
		HandlerMethod handlerMethod = (HandlerMethod) handler;  
		//获取地址方法名称
		String methodName = handlerMethod.getMethod().getName();
		//获取类名称
		String className = handlerMethod.getBean().getClass().getName();
		//将包名称截取
		className = className.substring(className.lastIndexOf(".")+1, className.length());
		String urlKey = className + "_" + methodName;
		//获取对应的编码
		String code = messageTemplateFileUtil.getValue(urlKey);
		return code;
	}
	
	/**
	 * 信息发送
	 * @param messageTemplateFileUtil
	 * @param sendMessageUserList
	 * @param temp_type
	 * @param webSiteMessageResult
	 */
	private void sendMessage(MessageTemplateFileUtil messageTemplateFileUtil
			,List<SendMessageUser> sendMessageUserList,String temp_type,WebSiteMessageResult webSiteMessageResult){
		/*
		 * 根据节点类型获取站内信、短信、邮箱模版并发送
		 */
		//获取站内信模版前缀
		String website_prefix = messageTemplateFileUtil.getValue(Constants.WEBSITE_PREFIX);
		//获取短信模版前缀
		String sms_perfix = messageTemplateFileUtil.getValue(Constants.SMS_PREFIX);
		//获取邮箱模版前缀
		String mail_prefix = messageTemplateFileUtil.getValue(Constants.MAIL_PREFIX);
		//获取邮箱主题模版前缀
		String mail_title_prefix = messageTemplateFileUtil.getValue(Constants.MAIL_TITLE_PREFIX);
		//获取邮箱主题模版后缀
		String mail_title_subfix = messageTemplateFileUtil.getValue(Constants.MAIL_TITLE_SUBFFIX);
		//获取买家模版后缀
		String buyer_suffix = messageTemplateFileUtil.getValue(Constants.BUYER_SUBFFIX);
		//获取卖家模版后缀
		String sell_suffix = messageTemplateFileUtil.getValue(Constants.SELL_SUBFFIX);
		//站内信买家模版key
		String buyer_ws_key = website_prefix + temp_type + buyer_suffix;
		//站内信卖家模版key
		String sell_ws_key = website_prefix + temp_type + sell_suffix;
		//邮件买家模版key
		String buyer_mail_key = mail_prefix + temp_type + buyer_suffix;
		//邮件卖家模版key
		String sell_mail_key = mail_prefix + temp_type + sell_suffix;
		//邮件主题模版key 
		String mail_title_key = mail_title_prefix + mail_title_subfix;
		//短信买家模版key
		String buyer_sms_key = sms_perfix + temp_type + buyer_suffix;
		//短信卖家模版key
		String sell_sms_key = sms_perfix + temp_type + sell_suffix;
		
		long sellerId = 0L;
		long buyerId = 0L;
		//如果是提交订单成功，给买家发送短信，但是短信内容中包含多个子订单，则订单号处填写：orderId1/orderId2....
		String buyerOrderId = "";
		for(SendMessageUser sendMessageUser : sendMessageUserList){
			buyerOrderId = buyerOrderId+sendMessageUser.getOrderId()+"/";
		}
		buyerOrderId = buyerOrderId.substring(0, buyerOrderId.length()-1);
		for(SendMessageUser sendMessageUser : sendMessageUserList){
			UserDTO buyer = sendMessageUser.getBuyer();
			UserDTO seller = sendMessageUser.getSeller();
			String orderId = sendMessageUser.getOrderId();
			//相同用户只允许发送一次提示消息
			if(buyer != null && buyer.getUid() != null && buyer.getUid().longValue() != buyerId){
				//向买家推送消息
				log.info("向买家推送消息");
				buyerSendMessage(buyer,seller,messageTemplateFileUtil,
						 buyer_ws_key, buyer_sms_key, buyer_mail_key, mail_title_key, temp_type,
						 webSiteMessageResult,buyerOrderId);
				log.info("向买家推送消息完成");
				buyerId = buyer.getUid();
			}
			//相同的用户只允许发送一次提示消息
			if(seller != null && seller.getUid() != null && seller.getUid().longValue() != sellerId){
				log.info("向卖家推送消息");
				sellerSendMessage(buyer,seller,messageTemplateFileUtil
						, webSiteMessageResult, sell_ws_key, temp_type, sell_sms_key
						, mail_title_key, sell_mail_key,orderId);
				log.info("向卖家推送消息完成");
				sellerId = seller.getUid();
			}
		}
	}
	
	/**
	 * 获取发送人集合
	 * @param webSiteMessageResult
	 * @return
	 */
	public List<SendMessageUser> getSendMessageUserList(WebSiteMessageResult webSiteMessageResult){
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		//获取发送人集合
		List<SendMessageUser> sendMessageUserList = new ArrayList<SendMessageUser>();
		//获取订单
		TradeOrdersDTO tradeOrdersDTO = null;
		if(!org.apache.commons.lang3.StringUtils.isBlank(webSiteMessageResult.getOrderId())){
			ExecuteResult<TradeOrdersDTO> orderResult = tradeOrderExportService.getOrderById(webSiteMessageResult.getOrderId());
			tradeOrdersDTO = orderResult.getResult();
			//获取时间
			webSiteMessageResult.setDate(webSiteMessageResult.getDate() == null ? myFmt.format(tradeOrdersDTO.getCreateTime()) : webSiteMessageResult.getDate());
			//清空买家 卖家
			sendMessageUserList.clear();
			//如果传入的是父订单
			if ("0".equals(tradeOrdersDTO.getParentOrderId()) || "-1".equals(tradeOrdersDTO.getParentOrderId())) {
				//获取所有子订单
				TradeOrdersQueryInDTO tradeOrdersQueryInDTO = new TradeOrdersQueryInDTO();
				tradeOrdersQueryInDTO.setParentOrderId(webSiteMessageResult.getOrderId());
				ExecuteResult<DataGrid<TradeOrdersDTO>> er =  tradeOrderExportService.queryOrders(tradeOrdersQueryInDTO,null);
				//查询子订单对应的买家 卖家集合
				for(TradeOrdersDTO tradeOrdersDTO_ : er.getResult().getRows()){
					//查询买家
					UserDTO buyer = this.userExportService.queryUserById(tradeOrdersDTO_.getBuyerId());
					//卖家
					UserDTO seller = this.userExportService.queryUserById(tradeOrdersDTO_.getSellerId());
					//发送对象
					SendMessageUser sendMessageUser_ = new SendMessageUser();
					sendMessageUser_.setBuyer(buyer);
					sendMessageUser_.setSeller(seller);
					//设置对应该卖家或卖家的订单号
					sendMessageUser_.setOrderId(tradeOrdersDTO_.getOrderId().toString());
					sendMessageUserList.add(sendMessageUser_);
				}
			}else{
				SendMessageUser sendMessageUser = new SendMessageUser();
				//查询买家
				UserDTO buyer = this.userExportService.queryUserById(tradeOrdersDTO.getBuyerId());
				//卖家
				UserDTO seller = this.userExportService.queryUserById(tradeOrdersDTO.getSellerId());
				sendMessageUser.setBuyer(buyer);
				sendMessageUser.setSeller(seller);
				sendMessageUser.setOrderId(tradeOrdersDTO.getOrderId().toString());
				sendMessageUserList.add(sendMessageUser);
			}
		}else{
			SendMessageUser sendMessageUser = new SendMessageUser();
			//获取买家对象
			if(webSiteMessageResult.getBuyerId() != null){
				UserDTO buyer = this.userExportService.queryUserById(webSiteMessageResult.getBuyerId());
				sendMessageUser.setBuyer(buyer);
			}
			//获取卖家对象
			if(webSiteMessageResult.getSellerId() != null){
				UserDTO seller = this.userExportService.queryUserById(webSiteMessageResult.getSellerId());
				sendMessageUser.setSeller(seller);
			}
			sendMessageUserList.add(sendMessageUser);
		}
		return sendMessageUserList;
	}
	
	/**
	 * 向买家推送消息
	 */
	@SuppressWarnings("unused")
	private void buyerSendMessage(UserDTO buyer,UserDTO seller,MessageTemplateFileUtil messageTemplateFileUtil,
			String buyer_ws_key,String buyer_sms_key,String buyer_mail_key,String mail_title_key,String temp_type,
			WebSiteMessageResult webSiteMessageResult,String orderId){
		Long buyerId = buyer.getUid().longValue();
		String message = "";
		String[] contents = null;
		//短信 邮件信息实体
		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
		baseSendMessageDTO.setIsSend("0");
		baseSendMessageDTO.setSendNum(0);
		baseSendMessageDTO.setIsPildash("0");
		//站内信实体
		WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
		webSiteMessageDTO.setWmRead(1);
		webSiteMessageDTO.setWmCreated(new Date());
		webSiteMessageDTO.setModified(new Date());
		webSiteMessageDTO.setType(1);
		/*
		 * 发送站内信
		 */
		//向买家发送站内信
		message = messageTemplateFileUtil.getValue(buyer_ws_key) == null ? "" : messageTemplateFileUtil.getValue(buyer_ws_key);
		contents = message.split("\\$");
		if(contents != null && contents.length > 0){
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && buyer != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					log.info("开始向买家推送站内信，详情【id:"+buyer.getUid()+" || name:"+buyer.getUname()+" 内容:"+content+"】");
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					webSiteMessageDTO.setWmToUserid(buyer.getUid());
					webSiteMessageDTO.setWmToUsername(buyer.getUname());
					//信息内容
					webSiteMessageDTO.setWmContext(content); 
					baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
					log.info("开始向买家推送站内信完成");
				}
			}
		}
		
		/*
		 * 发送短信
		 */
		//向买家发送短信
		message = messageTemplateFileUtil.getValue(buyer_sms_key) == null ? "":messageTemplateFileUtil.getValue(buyer_sms_key);
		contents = message.split("\\$");
		if(contents != null && contents.length > 0){
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && buyer != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					log.info("开始向买家推送短信，详情【phone:"+buyer.getUmobile()+" || 内容:"+content+"】");
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					//存放短信数据
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setAddress(buyer.getUmobile());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(Constants.SMS_TYPE);
					this.saveSendMessage(baseSendMessageDTO);
					//baseSmsConfigService.sendSms(buyer.getUmobile(), content);
					log.info("开始向买家推送短信完成");
				}
			}
		}
		
		/*
		 * 发送邮件
		 */
		//向买家发送邮件
			message = messageTemplateFileUtil.getValue(buyer_mail_key) == null ? "": messageTemplateFileUtil.getValue(buyer_mail_key);
			contents = message.split("\\$");
			if(contents != null && contents.length > 0){
				//获取主题
				String title = messageTemplateFileUtil.getValue(mail_title_key);
				int index = 0;
				for(String content : contents){
					if(StringUtils.isNotEmpty(content) && buyer != null){
						content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
						content = mailContent(content);
						log.info("开始向买家推送邮件，详情【phone:"+buyer.getUserEmail()+" || 内容:"+content+"】");
						//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
						//邮件信息
						baseSendMessageDTO.setId(null);
						baseSendMessageDTO.setTitle(title);
						baseSendMessageDTO.setAddress(buyer.getUserEmail());
						baseSendMessageDTO.setContent(content);
						baseSendMessageDTO.setType(Constants.MAIL_TYPE);
						this.saveSendMessage(baseSendMessageDTO);
						log.info("开始向买家推送邮件完成");
						//baseSmsConfigService.sendEmail(buyer.getUserEmail(), title, content);
					}
				}
			}
		}
		
	
	/**
	 * 向卖家推送消息
	 */
	public void sellerSendMessage(UserDTO buyer,UserDTO seller,MessageTemplateFileUtil messageTemplateFileUtil
			,WebSiteMessageResult webSiteMessageResult,String sell_ws_key,String temp_type,String sell_sms_key
			,String mail_title_key,String sell_mail_key,String orderId){
		//Long buyerId = seller.getUid().longValue();
		String message = "";
		String[] contents = null;
		//短信 邮件信息实体
		BaseSendMessageDTO baseSendMessageDTO = new BaseSendMessageDTO();
		baseSendMessageDTO.setIsSend("0");
		baseSendMessageDTO.setSendNum(0);
		baseSendMessageDTO.setIsPildash("0");
		//站内信实体
		WebSiteMessageDTO webSiteMessageDTO = new WebSiteMessageDTO();
		webSiteMessageDTO.setWmRead(1);
		webSiteMessageDTO.setWmCreated(new Date());
		webSiteMessageDTO.setModified(new Date());
		webSiteMessageDTO.setType(1);
		/*
		 * 发送站内信
		 */
		//向卖家发送站内信
		message = messageTemplateFileUtil.getValue(sell_ws_key) == null ? "":messageTemplateFileUtil.getValue(sell_ws_key);
		contents = message.split("\\$");
		if(contents != null && contents.length > 0){
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && seller != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					log.info("开始向卖家推送站内信，详情【id:"+seller.getUid()+" || name:"+seller.getUname()+" 内容:"+content+"】");
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					webSiteMessageDTO.setWmToUserid(seller.getUid());
					webSiteMessageDTO.setWmToUsername(seller.getUname());
					//信息内容
					webSiteMessageDTO.setWmContext(content); 
					baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
					log.info("开始向卖家推送站内信完成");
				}
			}
		}	
		
		/*
		 * 发送短信
		 */
		//向卖家发送短信
		message = messageTemplateFileUtil.getValue(sell_sms_key) == null ? "":messageTemplateFileUtil.getValue(sell_sms_key);
		contents = message.split("\\$");
		if(contents != null && contents.length > 0){
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && seller != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					log.info("开始向卖家推送短信，详情【phone:"+seller.getUmobile()+" || 内容:"+content+"】");
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setAddress(seller.getUmobile());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(Constants.SMS_TYPE);
					this.saveSendMessage(baseSendMessageDTO);
					log.info("开始向卖家推送短信完成");
					//baseSmsConfigService.sendSms(seller.getUmobile(), content);
				}
			}
		}
		
		/*
		 * 发送邮件
		 */
		//获取主题
		String title = messageTemplateFileUtil.getValue(mail_title_key);
		title = title == null ? "" : title;
		//向卖家发送短信
		message = messageTemplateFileUtil.getValue(sell_mail_key) == null ? "":messageTemplateFileUtil.getValue(sell_mail_key);
		contents = message.split("\\$");
		if(contents != null && contents.length > 0){
//				String[] titles = messageTemplateFileUtil.getValue(mail_title_key).split("$");
			//int index = 0;
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && seller != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					//拼装邮件发送格式
					content = mailContent(content);
					log.info("开始向卖家推送邮件，详情【phone:"+seller.getUserEmail()+" || 内容:"+content+"】");
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setTitle(title);
					baseSendMessageDTO.setAddress(seller.getUserEmail());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(Constants.MAIL_TYPE);
					this.saveSendMessage(baseSendMessageDTO);
					log.info("开始向卖家推送邮件完成");
					//baseSmsConfigService.sendEmail(seller.getUserEmail(), title, content);
				}
			}
		}
		
	}
	
	/**
	 * 分别将需要发送的消息存入数据库和redis队列中
	 */
	public void saveSendMessage(BaseSendMessageDTO baseSendMessageDTO){
		
		if(StringUtils.isNotEmpty(baseSendMessageDTO.getAddress())){
			//2015-06-10，王东晓修改，发送短信和邮件通知时，不再将通知消息存入到redis中，而将其存入到activeMq中
			//存入队列中
//			this.redisDB.tailPush(Constants.MESSAGE_KEY, baseSendMessageDTO);
			//将通知消息存入到activeMQ的消息队列, 通知类型不同，使用不同的消息队列
			//如果是邮件通知
			if(Constants.MAIL_TYPE.equals(baseSendMessageDTO.getType())){
				emailNoticQueuePublisher.sendMessage(baseSendMessageDTO);
			}
			//如果是短信通知
			else if(Constants.SMS_TYPE.equals(baseSendMessageDTO.getType())){
				smsNoticQueuePublisher.sendMessage(baseSendMessageDTO);
			}else{
				return ;
			}
			//是否使用数据库
			String value = MessageTemplateFileUtil.getInstance().getValue(Constants.USE_DB_KEY);
			if(StringUtils.isNotEmpty(value) && Constants.DB_YES.equals(value)){
				//存入数据库
				ExecuteResult<BaseSendMessageDTO> result = this.baseSendMessageService.saveBaseSendMessage(baseSendMessageDTO);
			}
			
			
		}
	}
	
	/**
	 * 格式化邮箱内容模板
	 * @param content
	 * @return
	 */
	public String mailContent(String content){
		//获取邮件内容模板 头信息
		String mail_content_top = MessageTemplateFileUtil.getInstance().getValue(Constants.MAIL_CONTENT_TOP);
		//获取邮件内容模板 底部信息
		String mail_content_bottom = MessageTemplateFileUtil.getInstance().getValue(Constants.MAIL_CONTENT_BOTTOM);
		return mail_content_top + content + mail_content_bottom;
	}
}
