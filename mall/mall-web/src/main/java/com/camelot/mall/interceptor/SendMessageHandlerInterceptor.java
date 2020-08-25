package com.camelot.mall.interceptor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
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
import com.camelot.common.enums.PayBankEnum;
import com.camelot.goodscenter.dto.ContractInfoDTO;
import com.camelot.goodscenter.dto.InquiryInfoDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemSkuInquiryPriceDTO;
import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.dto.TranslationMatDTO;
import com.camelot.goodscenter.service.InquiryExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuInquiryPriceExportService;
import com.camelot.goodscenter.service.ProtocolExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.maketcenter.service.CouponsExportService;
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
	private InquiryExportService inquiryExportService;
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
    @Resource
    private ProtocolExportService protocolExportService;
    @Resource
	private TranslationExportService translationExportService;

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
		//确认收货
		if("buyer_recipt_success".equals(temp_type)){
			Object paymentMethod = request.getAttribute("paymentMethod");
			if(paymentMethod == null && "".equals(paymentMethod) && paymentMethod != PayBankEnum.OFFLINE.getQrCode()){
				webSiteMessageResult.setTempType("buyer_integral_success");
			}
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
				Object objCouponName = request.getAttribute("couponName");
				if (objCouponName != null) {
					webSiteMessageResult.setCouponName((String) objCouponName);
				}
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
			Object meObj = map.get("errorMessages");
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
			webSiteMessageResult.setAgreeOrReject(Integer.valueOf(type));
			// 判断是否是售后服务
			if ("1".equals(tradeReturnDto.getIsCustomerService())) {
				webSiteMessageResult.setTempType("update_trade_return_sh");
			} else {
				webSiteMessageResult.setTempType("update_trade_return_tk");
			}
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
			if(!"操作成功!".contains(messageObj.toString()) && !"商家修改信息 提交审核成功".contains(messageObj.toString()))
				return false;
			//获取用户
			Object userIdObj = request.getParameter("userId");
			if(userIdObj == null)
				return false;
			webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			webSiteMessageResult.setSellerId(new Long(userIdObj.toString()));
		}
		
		//商品询价
		if("add_trans_success".equals(temp_type)){
			Map<String,Object> rResult =(Map<String,Object>)request.getAttribute("addResult");
			if (((String)rResult.get("result")).contains("保存成功")) {
				Object userIdObj = request.getAttribute("userId");
				webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			}
		}
		//立即发布询价
		if("immed_release_inquiry".equals(temp_type)){
			if (request.getAttribute("releaseResult").toString().contains("发布成功")) {
				String[] shopIds = (String[]) request.getAttribute("shopIds");
				Set<String> shopIdSet = new HashSet<String>();
				for(String shopId:shopIds){
					shopIdSet.add(shopId);
				}
				Long[] sellerIds = new Long[shopIdSet.size()];
				int i = 0;
				for(String shopId:shopIdSet){
					ExecuteResult<ShopDTO> er = shopExportService.findShopInfoById(new Long(shopId));
				 	ShopDTO shop = er.getResult();
				 	sellerIds[i] = shop.getSellerId();
				 	i++;
				}
				webSiteMessageResult.setSellerIds(sellerIds);
				webSiteMessageResult.setInquiryName(request.getAttribute("inquiryName").toString());
				Object userIdObj = request.getAttribute("userId");
				webSiteMessageResult.setUserName(request.getAttribute("userName").toString());
				webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			}else{
				return false;
			}
		}
		//批量发布询价
		if("release_inquiry".equals(temp_type)){
			if (request.getAttribute("releaseResult").toString().contains("发布成功")) {
				String nos = request.getAttribute("ids").toString();
				ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryInfoDTOs = inquiryExportService.queryByInquiryNos(nos);
				List<InquiryInfoDTO> list = inquiryInfoDTOs.getResult().getRows();
				List<String> inquiryNames = new ArrayList<String>();
				String userName = request.getAttribute("userName").toString();
				Long[] sellerIds = new Long[list.size()];
				for(int i = 0;i<list.size();i++){
					InquiryInfoDTO inquiryInfo = list.get(i);
					inquiryNames.add(inquiryInfo.getInquiryName());
					ShopDTO shop = shopExportService.findShopInfoById(inquiryInfo.getSupplierId()).getResult();
					sellerIds[i] = shop.getSellerId();
				}
				webSiteMessageResult.setSellerIds(sellerIds);
				webSiteMessageResult.setReleaseInquirys(inquiryNames);
				webSiteMessageResult.setUserName(userName);
				Object userIdObj = request.getAttribute("userId");
				webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			}else{
				return false;
			}
		}
		//批量询价
		if("batch_inquiry".equals(temp_type)){
			if (request.getAttribute("updResult").toString().contains("保存成功")) {
				Object userIdObj = request.getAttribute("userId");
				webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
				webSiteMessageResult.setSellerName(request.getAttribute("sellerName").toString());
			}else{
				return false;
			}
		}
		//询价接受
		if("quote_inquiry".equals(temp_type)){
			if (request.getAttribute("updResult").toString().contains("成功")) {
				String detailIds = (String)request.getAttribute("detailIds");
				ExecuteResult<DataGrid<InquiryInfoDTO>> inquiryInfoDTOs = inquiryExportService.queryByMids(detailIds);
				List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
				List<InquiryInfoDTO> list = inquiryInfoDTOs.getResult().getRows();
				List<String> releaseInquirys = new ArrayList<String>();
				Long[] ids = new Long[list.size()];
				int i = 0;
				for(InquiryInfoDTO inquiryInfo:list){
					HashMap<String, String> map = new HashMap<String, String>();
					ShopDTO shop = shopExportService.findShopInfoById(inquiryInfo.getSupplierId()).getResult();
					UserDTO buyer = this.userExportService.queryUserById(shop.getSellerId());
					ids[i] = shop.getSellerId();
					releaseInquirys.add(inquiryInfo.getInquiryName());
					map.put("inquiryName", inquiryInfo.getInquiryName());
					map.put("sellerName", buyer.getUname());
					mapList.add(map);
					i++;
				}
				webSiteMessageResult.setReleaseInquirys(releaseInquirys);
				webSiteMessageResult.setSellerIds(ids);
				webSiteMessageResult.setUserName(request.getAttribute("userName").toString());
				webSiteMessageResult.setQuoteInquirys(mapList);
				Object userIdObj = request.getAttribute("userId");
				webSiteMessageResult.setBuyerId(new Long(userIdObj.toString()));
			}else{
				return false;
			}
		}
		//求购提交
		if("ask_commit_success".equals(temp_type)){
			
			Map<String, String> resultMap = (Map<String, String>)request.getAttribute("addResult");
			if(resultMap.get("result").contains("提交成功")){
				webSiteMessageResult.setBuyerId(new Long(request.getAttribute("userId").toString()));
			}
			
		}
		//求购报价
		if("ask_price_success".equals(temp_type)){
			
			Map<String, String> resultMap = (Map<String, String>)request.getAttribute("addResult");
			if(resultMap.get("result").contains("成功")){
				webSiteMessageResult.setTmpJuge("onlyToBuyer");
				webSiteMessageResult.setBuyerId(new Long(request.getAttribute("userId").toString()));
				webSiteMessageResult.setSellerId(new Long(request.getAttribute("sellerId").toString()));
			}
			
		}
		//报价确认
		if("ask_accept_success".equals(temp_type)){
			Map<String, String> resultMap = (Map<String, String>)request.getAttribute("addResult");
			if(resultMap.get("result").contains("成功")){
				String detailId = request.getAttribute("detailId").toString();
				String userId = request.getAttribute("userId").toString();
				//将明细的status字段更新为1
				String[] detail = detailId.split(",");
				List<Map<String, Object>> askList = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < detail.length; i++) {
					TranslationMatDTO tmp = new TranslationMatDTO();
					tmp.setId(Long.valueOf(detail[i]));
					Map<String, Object> tmpMap = null;
					ExecuteResult<TranslationMatDTO> erExecuteResult = translationExportService.queryByTranslationMat(tmp);
					if(erExecuteResult.getResult()!=null){
						TranslationMatDTO translationMatDTO = erExecuteResult.getResult();
						tmpMap = new HashMap<String, Object>();
						tmpMap.put("buyerId", userId);
						ExecuteResult<ShopDTO> erShop = shopExportService.findShopInfoById(translationMatDTO.getShopId());
						tmpMap.put("sellerId", erShop.getResult().getSellerId());
						tmpMap.put("transNo", translationMatDTO.getTranslationNo());
					}
					if (tmpMap!=null) {
						askList.add(tmpMap);
					}
				}
				if (askList.size()>0) {
					TranslationInfoDTO tmp = new TranslationInfoDTO();
					tmp.setTranslationNo(askList.get(0).get("transNo").toString());
					ExecuteResult<TranslationInfoDTO> erExecuteResult = translationExportService.queryByTranslationInfo(tmp);
					if (erExecuteResult != null) {
						TranslationInfoDTO translationInfoDTO = erExecuteResult.getResult();
						for (int i = 0; i < askList.size(); i++) {
							askList.get(i).put("transName", translationInfoDTO.getTranslationName());
						}
					}
				}
				webSiteMessageResult.setAskList(askList);
				webSiteMessageResult.setTmpJuge("toBuyerOrSeller");
			}
		}
		// 协议相关
		if ("confirmContractInfo".contentEquals(temp_type)) {
			String operation = request.getParameter("operation");
			String contractNos = request.getParameter("contractNos");
			String sourcePage = request.getParameter("sourcePage");
			String contractInfo = request.getParameter("contractInfo");
			//必须有指定操作
			if (StringUtils.isNotEmpty(operation)) {
			
				ContractInfoDTO dto = new ContractInfoDTO();
				List<String> contractNo = JSONObject.parseArray(contractNos, String.class);
				dto.setId(Long.parseLong(contractNo.get(0)));
				ExecuteResult<ContractInfoDTO> exeResult=protocolExportService.queryByContractInfo(dto);
				
				if ("重新提交".equals(operation)) {
					temp_type+="_resubmit_by";
				}else if ("拒绝".equals(operation)) {
					temp_type+="_reject_by";
				}else if ("同意".equals(operation)) {
					temp_type+="_agree_by";
				}else if ("发布".equals(operation)) {
					temp_type+="_release_by";
				}else if ("终止".equals(operation)) {
					temp_type+="_terminate_by";
				}
				
				if (exeResult.isSuccess()) {
					ContractInfoDTO resultDto = exeResult.getResult();
					webSiteMessageResult.setAgreementName(resultDto.getContractName());
					if ("seller".equals(sourcePage)) {
						temp_type +="_seller";
						webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getConfirmBy()));
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
					} else if ("buyer".equals(sourcePage)) {
						temp_type +="_buyer";
						webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getConfirmBy()));
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
					}
				}
				webSiteMessageResult.setTempType(temp_type);
				
			} else if (contractNos==null && contractInfo!=null) {
				ContractInfoDTO contractInfoDTO = JSONObject.parseObject(contractInfo, ContractInfoDTO.class);
				ExecuteResult<ContractInfoDTO> exeResult=protocolExportService.queryByContractInfo(contractInfoDTO);
				
				if (exeResult.isSuccess() && contractInfoDTO.getStatus()==null) {
					// 立即发布，所以operation是重新提交
					temp_type+="_resubmit_by";
					ContractInfoDTO resultDto = exeResult.getResult();
					webSiteMessageResult.setAgreementName(resultDto.getContractName());
					if ("seller".equals(sourcePage)) {
						temp_type +="_seller";
						webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getConfirmBy()));
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
					} else if ("buyer".equals(sourcePage)) {
						temp_type +="_buyer";
						webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getConfirmBy()));
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
					}
				}
				webSiteMessageResult.setTempType(temp_type);
			}
		}
		
		// 协议删除
		if ("deleteContractInfo".contentEquals(temp_type)) {
			String contractNos = request.getParameter("contractNos");
			
			ContractInfoDTO dto = new ContractInfoDTO();
			List<String> contractNo = JSONObject.parseArray(contractNos, String.class);
			dto.setContractNo(contractNo.get(0));
			ExecuteResult<ContractInfoDTO> exeResult=protocolExportService.queryByContractInfo(dto);
			if (exeResult.isSuccess()) {
				ContractInfoDTO resultDto = exeResult.getResult();
				if ("10".equals(resultDto.getStatus())) {
					Integer printerId = resultDto.getPrinterId();
					String createBy = resultDto.getCreateBy();
					String identity = createBy.equals(printerId + "") ? "buyer" : "seller";
					temp_type += "_by_" + identity;
					webSiteMessageResult.setAgreementName(resultDto.getContractName());
					webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getConfirmBy()));
					webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
				}
			}
			webSiteMessageResult.setTempType(temp_type);
			
		}
		
		// 协议审批相关
		if ("approveContractInfo".contentEquals(temp_type)) {
			String operation = request.getParameter("operation");
			String contractNos = request.getParameter("contractNos");
			String sourcePage = request.getParameter("sourcePage");
			if (StringUtils.isNotEmpty(operation)) {
				ContractInfoDTO dto = new ContractInfoDTO();
				List<String> contractNo = JSONObject.parseArray(contractNos, String.class);
				dto.setId(Long.parseLong(contractNo.get(0)));
				ExecuteResult<ContractInfoDTO> exeResult=protocolExportService.queryByContractInfo(dto);

				if (exeResult.isSuccess()) {
					ContractInfoDTO resultDto = exeResult.getResult();
					webSiteMessageResult.setAgreementName(resultDto.getContractName());
					if ("重新提交".equals(operation)) {
						temp_type += "_resubmit_by";
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getApproveBy()));
						webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getCreateBy()));
					} else if ("同意".equals(operation)) {
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
						webSiteMessageResult.setBuyerId(Long.parseLong(resultDto.getConfirmBy()));
						temp_type += "_agree_by";
					} else if ("拒绝".equals(operation)) {
						webSiteMessageResult.setSellerId(Long.parseLong(resultDto.getCreateBy()));
						temp_type += "_reject_by";
					}
					if ("同意".equals(operation)) {
						if ("seller".equals(sourcePage)) {
							temp_type +="_seller";
						} else if ("buyer".equals(sourcePage)) {
							temp_type +="_buyer";
						}
					}
				}
				webSiteMessageResult.setTempType(temp_type);
			}
		}
		
		//获取订单id
		String orderId = request.getParameter("orderId");
		if(StringUtils.isNotEmpty(orderId) && webSiteMessageResult.getOrderId() == null){
			webSiteMessageResult.setOrderId(orderId);
		}
		
		// 收到优惠券（买家自己领取）
		if ("coupons_get_success".equals(temp_type)) {
			Object objBuyerId = request.getAttribute("buyerId");
			Object objCouponName = request.getAttribute("couponName");
			if (objBuyerId != null) {
				webSiteMessageResult.setBuyerId((Long) objBuyerId);
			}
			if (objCouponName != null) {
				webSiteMessageResult.setCouponName((String) objCouponName);
			}
		}
		
		// 收到优惠券（卖家发放）
		if ("coupons_save_success".equals(temp_type)) {
			Object objCouponName = request.getAttribute("couponName");
			Object objBuyerIds = request.getAttribute("buyerIds");
			if (objCouponName != null) {
				webSiteMessageResult.setCouponName((String) objCouponName);
			}
			if (objBuyerIds != null) {
				Long[] buyerIds = (Long[]) objBuyerIds;
				if (buyerIds.length > 0) {
					webSiteMessageResult.setBuyerIds(buyerIds);
				}
			}
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
		//替换买家用户名称
		content = content.replaceAll(Constants.SELL_USER_NAME, seller == null ? shopDTO == null ? "" : shopDTO.getShopName() : seller.getUname());
		if(webSiteMessageResul.getUserName()!=null&&!"".equals(webSiteMessageResul.getUserName())){
			content = content.replaceAll(Constants.USER_NAME_MESSAGE, webSiteMessageResul.getUserName());
		}else{
			content = content.replaceAll(Constants.USER_NAME_MESSAGE, buyer == null ? "" : buyer.getUname());
		}
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
		//替换协议名称
		content = content.replaceAll(Constants.AGREEMENT_NAME, webSiteMessageResul.getAgreementName());
		//替换优惠券名称
		content = content.replaceAll(Constants.COUPON_NAME, webSiteMessageResul.getCouponName());
		//替换同意/拒绝
		if(webSiteMessageResul.getAgreeOrReject() != null){
			String str = "同意";
			if(webSiteMessageResul.getAgreeOrReject() == 2){
				str = "拒绝";
			}
			content = content.replaceAll(Constants.AGREE_OR_REJECT, str);
		}
		
		if(webSiteMessageResul.getQuoteInquirys()!=null&&webSiteMessageResul.getQuoteInquirys().size()>0){
			List<HashMap<String, String>> mapList = webSiteMessageResul.getQuoteInquirys();
			content = content.replaceAll(Constants.SELLER_NAME, mapList.get(0).get("sellerName"));
			content = content.replaceAll(Constants.INQUIRY_NAME, mapList.get(0).get("inquiryName"));
		}
		if(webSiteMessageResul.getReleaseInquirys()!=null&&webSiteMessageResul.getReleaseInquirys().size()>0){
			List<String> inquiryNames = webSiteMessageResul.getReleaseInquirys();
			content = content.replaceAll(Constants.INQUIRY_NAME, inquiryNames.get(0));
		}
		
		//替换询价名称
		content = content.replaceAll(Constants.INQUIRY_NAME, webSiteMessageResul.getInquiryName() == null ? "" : webSiteMessageResul.getInquiryName());
		//求购名称
		content = content.replaceAll(Constants.ASK_NAME, webSiteMessageResul.getTmp());
		//替换卖家名字
		content = content.replaceAll(Constants.SELLER_NAME, webSiteMessageResul.getSellerName() != null ? webSiteMessageResul.getSellerName():"");
		//content = content.replaceAll(Constants.SELLER_NAME, webSiteMessageResul.getSellerName() == null ? seller.getCompanyName() == null ? "" : seller.getCompanyName() : webSiteMessageResul.getSellerName());
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
			if((buyer != null && buyer.getUid() != null && buyer.getUid().longValue() != buyerId)||(webSiteMessageResult.getQuoteInquirys()!=null&&webSiteMessageResult.getQuoteInquirys().size()>0)){
				if (webSiteMessageResult.getTmpJuge() == null) {
					//向买家推送消息
					log.info("向买家推送消息");
					buyerSendMessage(buyer,seller,messageTemplateFileUtil,
							 buyer_ws_key, buyer_sms_key, buyer_mail_key, mail_title_key, temp_type,
							 webSiteMessageResult,buyerOrderId);
					log.info("向买家推送消息完成");
					buyerId = buyer.getUid();
				}else if (webSiteMessageResult.getTmpJuge().equals("onlyToBuyer")) {
					//向买家推送消息
					log.info("向买家推送消息");
					buyerSendMessage(buyer,seller,messageTemplateFileUtil,
							 buyer_ws_key, buyer_sms_key, buyer_mail_key, getMailTitle(temp_type), temp_type,
							 webSiteMessageResult,buyerOrderId);
					log.info("向买家推送消息完成");
					buyerId = buyer.getUid();
				}
				else if (webSiteMessageResult.getTmpJuge().equals("toBuyerOrSeller")) {
					//向买家推送消息
					log.info("向买家推送消息");
					if (webSiteMessageResult.getAskList()!=null && webSiteMessageResult.getAskList().size() > 0) {
						webSiteMessageResult.setTmp(webSiteMessageResult.getAskList().get(0).get("transName").toString());
					}
					buyerSendMessage(buyer,seller,messageTemplateFileUtil,
							 buyer_ws_key, buyer_sms_key, buyer_mail_key, getMailTitle(temp_type), temp_type,
							 webSiteMessageResult,buyerOrderId);
					log.info("向买家推送消息完成");
					webSiteMessageResult.setTmp(null);
				}
				
			}
			//相同的用户只允许发送一次提示消息
			if(seller != null && seller.getUid() != null && seller.getUid().longValue() != sellerId ){
				if (webSiteMessageResult.getTmpJuge() == null) {
					log.info("向卖家推送消息");
					sellerSendMessage(buyer,seller,messageTemplateFileUtil
							, webSiteMessageResult, sell_ws_key, temp_type, sell_sms_key
							, mail_title_key, sell_mail_key,orderId);
					log.info("向卖家推送消息完成");
					sellerId = seller.getUid();
					if(webSiteMessageResult.getReleaseInquirys()!=null&&webSiteMessageResult.getReleaseInquirys().size()>0){
						sellerId = 0L;
					}
				}else if (webSiteMessageResult.getTmpJuge().equals("toBuyerOrSeller")) {
					//向买家推送消息
					log.info("向卖家推送消息");
					if (webSiteMessageResult.getAskList()!=null && webSiteMessageResult.getAskList().size() > 0) {
						webSiteMessageResult.setTmp(webSiteMessageResult.getAskList().get(0).get("transName").toString());
					}
					sellerSendMessage(buyer,seller,messageTemplateFileUtil
							, webSiteMessageResult, sell_ws_key, temp_type, sell_sms_key
							, getMailTitle(temp_type), sell_mail_key,orderId);
					log.info("向卖家推送消息完成");
					webSiteMessageResult.setTmp(null);
				}
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
					//设置对应该买家或卖家的订单号
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
		}else if(webSiteMessageResult.getAskList()!=null && webSiteMessageResult.getAskList().size() > 0){
			List<Map<String, Object>> askList = webSiteMessageResult.getAskList();
			for (int i = 0; i < askList.size(); i++) {
				//发送对象
				SendMessageUser sendMessageUser_ = new SendMessageUser();
				UserDTO buyer = this.userExportService.queryUserById(Long.valueOf(askList.get(i).get("buyerId").toString()));
				UserDTO seller = this.userExportService.queryUserById(Long.valueOf(askList.get(i).get("sellerId").toString()));
				sendMessageUser_.setBuyer(buyer);
				sendMessageUser_.setSeller(seller);
				sendMessageUserList.add(sendMessageUser_);
			}
			
		}else{
			SendMessageUser sendMessageUser = new SendMessageUser();
			//获取买家对象
			if(webSiteMessageResult.getBuyerId() != null){
				UserDTO buyer = this.userExportService.queryUserById(webSiteMessageResult.getBuyerId());
				sendMessageUser.setBuyer(buyer);
				if(webSiteMessageResult.getQuoteInquirys()!=null&&webSiteMessageResult.getQuoteInquirys().size()>0){
					for(int i = 1;i<webSiteMessageResult.getQuoteInquirys().size();i++){
						sendMessageUserList.add(sendMessageUser);
					}
				}
				sendMessageUserList.add(sendMessageUser);
			} else if (webSiteMessageResult.getBuyerIds() != null && webSiteMessageResult.getBuyerIds().length > 0) {
				for (Long id : webSiteMessageResult.getBuyerIds()) {
					SendMessageUser smUser = new SendMessageUser();
					UserDTO buyer = this.userExportService.queryUserById(id);
					smUser.setBuyer(buyer);
					sendMessageUserList.add(smUser);
				}
			}
			//获取卖家对象
			if (webSiteMessageResult.getSellerId() != null) {
				UserDTO seller = this.userExportService.queryUserById(webSiteMessageResult.getSellerId());
				sendMessageUser.setSeller(seller);
				sendMessageUserList.add(sendMessageUser);
			} else if (webSiteMessageResult.getSellerIds() != null && webSiteMessageResult.getSellerIds().length > 0) {
				for (Long id : webSiteMessageResult.getSellerIds()) {
					SendMessageUser smUser = new SendMessageUser();
					UserDTO seller = this.userExportService.queryUserById(id);
					smUser.setSeller(seller);
					sendMessageUserList.add(smUser);
				}
			}
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
					// 不发送优惠券已使用的消息提醒
					if(content.contains(Constants.COUPON_NAME) && StringUtils.isEmpty(webSiteMessageResult.getCouponName())){
						continue;
					}
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					if (StringUtils.isNotEmpty(content)) {
						log.info("开始向买家推送站内信，详情【id:" + buyer.getUid() + " || name:" + buyer.getUname() + " 内容:" + content + "】");
						webSiteMessageDTO.setWmToUserid(buyer.getUid());
						webSiteMessageDTO.setWmToUsername(buyer.getUname());
						// 信息内容
						webSiteMessageDTO.setWmContext(content);
						baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
						log.info("向买家推送站内信完成");
					}
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
					// 不发送优惠券已使用的消息提醒
					if(content.contains(Constants.COUPON_NAME) && StringUtils.isEmpty(webSiteMessageResult.getCouponName())){
						continue;
					}
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					if (StringUtils.isNotEmpty(content)) {
						log.info("开始向买家推送短信，详情【phone:"+buyer.getUmobile()+" || 内容:"+content+"】");
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
						// 不发送优惠券已使用的消息提醒
						if(content.contains(Constants.COUPON_NAME) && StringUtils.isEmpty(webSiteMessageResult.getCouponName())){
							continue;
						}
						content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
						content = mailContent(content);
						//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
						if (StringUtils.isNotEmpty(content)) {
							log.info("开始向买家推送邮件，详情【phone:"+buyer.getUserEmail()+" || 内容:"+content+"】");
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
			if(webSiteMessageResult.getQuoteInquirys()!=null&&webSiteMessageResult.getQuoteInquirys().size()>0){
				List<HashMap<String, String>> mapList = webSiteMessageResult.getQuoteInquirys();
				mapList.remove(0);
				webSiteMessageResult.setQuoteInquirys(mapList);
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
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					if (StringUtils.isNotEmpty(content)) {
						log.info("开始向卖家推送站内信，详情【id:"+seller.getUid()+" || name:"+seller.getUname()+" 内容:"+content+"】");
						webSiteMessageDTO.setWmToUserid(seller.getUid());
						webSiteMessageDTO.setWmToUsername(seller.getUname());
						//信息内容
						webSiteMessageDTO.setWmContext(content); 
						baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
						log.info("开始向卖家推送站内信完成");
					}
				}
			}
		}	
		
		/*
		 * 发送短信
		 */
		/* 暂时不向卖家发送短信
		//向卖家发送短信
		message = messageTemplateFileUtil.getValue(sell_sms_key) == null ? "":messageTemplateFileUtil.getValue(sell_sms_key);
		contents = message.split("\\$");
		if(contents != null && contents.length > 0){
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && seller != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult,orderId);
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					if (StringUtils.isNotEmpty(content)) {
						log.info("开始向卖家推送短信，详情【phone:"+seller.getUmobile()+" || 内容:"+content+"】");
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
		}
		*/
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
					//判断订单支付成功后是否有数据重复发送
//						if("order_pay_online_success".equals(temp_type) && this.validateMessageIsSend(content)){
//							continue;
//						}
					if (StringUtils.isNotEmpty(content)) {
						log.info("开始向卖家推送邮件，详情【phone:"+seller.getUserEmail()+" || 内容:"+content+"】");
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
		if(webSiteMessageResult.getReleaseInquirys()!=null&&webSiteMessageResult.getReleaseInquirys().size()>0){
			List<String> strs = webSiteMessageResult.getReleaseInquirys();
			strs.remove(0);
			webSiteMessageResult.setReleaseInquirys(strs);
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
		if (StringUtils.isNotEmpty(content)) {
			//获取邮件内容模板 头信息
			String mail_content_top = MessageTemplateFileUtil.getInstance().getValue(Constants.MAIL_CONTENT_TOP);
			//获取邮件内容模板 底部信息
			String mail_content_bottom = MessageTemplateFileUtil.getInstance().getValue(Constants.MAIL_CONTENT_BOTTOM);
			return mail_content_top + content + mail_content_bottom;
		}
		return "";
	}
	
	public String getMailTitle(String temp_type){
		return MessageTemplateFileUtil.getInstance().getValue(Constants.MAIL_TITLE_PREFIX) + temp_type + "_"+ MessageTemplateFileUtil.getInstance().getValue(Constants.MAIL_TITLE_SUBFFIX);
	}
}
