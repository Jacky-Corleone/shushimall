package com.camelot.ecm.interceptor;

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

import com.camelot.aftersale.dto.ComplainDTO;
import com.camelot.aftersale.service.ComplainExportService;
import com.camelot.basecenter.dto.BaseSendMessageDTO;
import com.camelot.basecenter.dto.WebSiteMessageDTO;
import com.camelot.basecenter.service.BaseSendMessageService;
import com.camelot.basecenter.service.BaseSmsConfigService;
import com.camelot.basecenter.service.BaseWebSiteMessageService;
import com.camelot.common.Json;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.TranslationInfoDTO;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TranslationExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PlatformEnum;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.contract.UserContractDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.MessageConstants;
import com.camelot.util.MessageTemplateFileUtil;
import com.camelot.util.SendMessageUser;
import com.camelot.util.WebSiteMessageResult;
import com.thinkgem.jeesite.common.utils.StringUtils;
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
	private BaseSmsConfigService baseSmsConfigService;
	@Resource
	private BaseSendMessageService baseSendMessageService;
    @Resource
    private ComplainExportService complainExportService;
    @Resource
    private ShopCategoryExportService shopCategoryExportService;
    @Resource
   	private TranslationExportService translationExportService;
    
    private List<String> uriList;
    public List<String> getUriList() {
        return uriList;
    }
    public void setUriList(List<String> uriList) {
        this.uriList = uriList;
    }
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView){
		try {
			//校验是否是需要拦截的地址
			boolean valiResult = validateUrl(request);
			if(!valiResult)
				return;
			MessageTemplateFileUtil messageTemplateFileUtil = MessageTemplateFileUtil.getInstance();
			if(messageTemplateFileUtil == null){
				log.info("未读取到message_template.properties文件");
				return;
			}
			//获取节点编码
			String temp_type = this.getWebSiteMessageType(handler, messageTemplateFileUtil);
			if(StringUtils.isEmpty(temp_type))
				return;
			//获取信息发送实体
			WebSiteMessageResult webSiteMessageResult = 
					request.getAttribute("webSiteMessageResult") == null ? 
					new WebSiteMessageResult():(WebSiteMessageResult)request.getAttribute("webSiteMessageResult");
			webSiteMessageResult.setTempType(temp_type);
			//对某些信息发送节点做出特殊处理
			boolean result = specialHandle(webSiteMessageResult,request,response);
			if(!result)
				return;
			//获取节点编码
			String code = webSiteMessageResult.getTempType();
			//获取发送人集合
			List<SendMessageUser> sendMessageUserList = getSendMessageUserList(webSiteMessageResult);
			//推送信息
			sendMessage(messageTemplateFileUtil,sendMessageUserList,code,webSiteMessageResult);
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
	private boolean specialHandle(WebSiteMessageResult webSiteMessageResult,
			HttpServletRequest request, HttpServletResponse response){
		String temp_type = webSiteMessageResult.getTempType();
		Object obj = request.getAttribute("result");
		ExecuteResult<String> result = obj == null ? null : (ExecuteResult<String>)obj;
		if(result != null && !result.isSuccess()){
			return false;
		}
		//获取订单id
		String orderId = request.getParameter("orderId");
		if(StringUtils.isNotEmpty(orderId) && webSiteMessageResult.getOrderId() == null){
			webSiteMessageResult.setOrderId(orderId);
		}
		//买家认证通过，给买家发送短信和邮件、站内信
		if("appove_success".equals(temp_type)){
			Object mapObj = request.getAttribute("map");
			if(mapObj==null){
				return false;
			}
			Map<String,Object> map = (Map<String,Object>)mapObj;
			boolean success = (Boolean)map.get("success");
			if(!success)
				return false;
			//获取买家用户id
			Object buyerId = map.get("uid");
			if(buyerId == null)
				return false;
			Object userstatus = map.get("userstatus");
			if(userstatus == null){
				return false;
			}
			//买家审核
			if("4".equals(userstatus.toString())){
				webSiteMessageResult.setBuyerId(new Long(buyerId.toString()));
				return true;
			}else if("5".equals(userstatus.toString())){//卖家认证
				webSiteMessageResult.setSellerId(new Long(buyerId.toString()));
				if("0".equals(map.get("auditStatus").toString())){
					temp_type ="appove_error";
					webSiteMessageResult.setTempType(temp_type);
				}
				return true;
			}else if("6".equals(userstatus.toString())){//卖家认证
				webSiteMessageResult.setSellerId(new Long(buyerId.toString()));
				return true;
			}else{
				return false;
			}
			
		}
		//卖家认证审核通过
		if("seller_appove_success".equals(temp_type)){
			Object mapObj = request.getAttribute("map");
			if(mapObj==null){
				return false;
			}
			Map<String,Object> map = (Map<String,Object>)mapObj;
			boolean success = (Boolean)map.get("success");
			if(!success)
				return false;
			//获取买家用户id
			Object sellerId = map.get("uid");
			if(sellerId == null){
				return false;
			}
			webSiteMessageResult.setSellerId(new Long(sellerId.toString()));
			
			return true;
		}
		//上传合同
		if("contract_updateUrl_success".equals(temp_type)){
			Object mapObj = request.getAttribute("map");
			if(mapObj==null){
				return false;
			}
			Map<String,Object> map = (Map<String,Object>)mapObj;
			boolean success = (Boolean)map.get("success");
			if(!success){
				return false;
			}
			UserContractDTO userContractDTO  = (UserContractDTO) map.get("contract");
			Long shopId = userContractDTO.getShopId();
			UserDTO userDTO = new UserDTO();
			userDTO.setShopId(shopId);
			DataGrid<UserDTO> userDTOResult = userExportService.findUserListByCondition(userDTO, null, null);
			Long userId = userDTOResult.getRows().get(0).getUid();
			webSiteMessageResult.setSellerId(userId);
			return true;
		}
		//基础信息修改审核通过
		if("information_approve_success".equals(temp_type)){
			//获取反馈的结果数据
			Object jsonObj = request.getAttribute("json");
			if(jsonObj == null)
				return false;
			Json json = (Json)jsonObj;
			if(!"审核成功".equals(json.getMsg()))
				return false;
			//获取用户
			String uid = request.getParameter("uid");
			if(StringUtils.isEmpty(uid))
				return false;
			webSiteMessageResult.setBuyerId(new Long(uid));
			return true;
		}
		//店铺开通审核
		if("shop_approve_success".equals(temp_type)){
			//获取结果反馈
			Object mapObj = request.getAttribute("map");
			if(mapObj == null)
				return false;
			Map<String,Object> map = (Map<String,Object>)mapObj;
			boolean success = (Boolean)map.get("success");
			if(!success)
				return false;
			//获取卖家用户id
			Object sellerIdObj = request.getAttribute("sellerId");
			if(sellerIdObj == null)
				return false;
			//获取审核状态
			Integer status = Integer.parseInt(request.getParameter("status"));
			if(status == null ){
				return false;
			}else
			//审核驳回
			if(3 == status.intValue()){
				temp_type = "shop_approve_error";
				webSiteMessageResult.setTempType(temp_type);
			}
			webSiteMessageResult.setSellerId(new Long(sellerIdObj.toString()));
			return true;
		}
		//平台仲裁
		if("complain_result".equals(temp_type)){
			Object jsonObj = request.getAttribute("json");
			if(jsonObj == null)
				return false;
			if(!((Json)jsonObj).isSuccess())
				return false;
			String id = request.getParameter("id");
			ExecuteResult<ComplainDTO> executeResult=complainExportService.findInfoById(new Long(id));
	        if(executeResult.getResult() ==null)
	        	return false;
	        ComplainDTO complainDTO=executeResult.getResult();
			//获取订单
	        webSiteMessageResult.setOrderId(complainDTO.getOrderId());
	        //获取仲裁结果
	        String complainResult = request.getAttribute("complainResult") == null ? "" : request.getAttribute("complainResult").toString();
	        complainResult = complainResult.replace("；", "").replace("。", "");
	        webSiteMessageResult.setContent(complainResult);
	        SimpleDateFormat sf = new SimpleDateFormat();
	        if(complainDTO.getResolutionTime() == null){
	        	webSiteMessageResult.setDate(sf.format(new Date()));
	        }else{
	        	webSiteMessageResult.setDate(sf.format(complainDTO.getResolutionTime()));
	        }
	        return true;
		}
		//商品求购
		if("good_modifyStatus_success".equals(temp_type)){
			String success = (String)request.getAttribute("success");
			if (success.contains("true")) {
				String status =request.getAttribute("status").toString();
				List<Map<String, Object>> idsList  = (List<Map<String, Object>>)request.getAttribute("idsList");
				List<Map<String, Object>> tmpList = new ArrayList<Map<String,Object>>();
				//过滤出舒适100用户
				for (int i = 0; i < idsList.size(); i++) {
					UserDTO tmpUserDTO = userExportService.queryUserByfId(Long.valueOf(idsList.get(i).get("buyerId").toString()));
					if (tmpUserDTO!=null) {
						if (tmpUserDTO.getPlatformId()==null) {
							tmpList.add(idsList.get(i));
						}
					}
				}
				//遍历求购买家列表
				webSiteMessageResult.setMulMessageMaps(tmpList);
				//求购审核成功发给卖家
				if (status.contains("2")) {
					//允许发送多条消息
					if (webSiteMessageResult.getMulMessageMaps().size()>0) {
						webSiteMessageResult.getMulMessageMaps().get(0).put("mulMsg", true);
					}
					
					
					
				}
				//驳回成功
				if (status.contains("4")) {
					webSiteMessageResult.setTempType("good_modifyStatus_reject");
				}
				webSiteMessageResult.setBackResult(status);
				return true;
			}
		}
		//设计图上传成功 提醒买家
		if("plan_success".equals(temp_type)){
			Object buyerObj = request.getAttribute("planBuyerId");
			if (buyerObj==null) {
				return false;
			}
			Long buyerId = (Long) buyerObj;
			webSiteMessageResult.setBuyerId(buyerId);
			return true;
		}
		return false;
	}
	

	
	/**
	 * 替换信息内容
	 * @param content
	 * @return
	 */
	private String contentReplace(String content,UserDTO buyer,UserDTO seller,WebSiteMessageResult webSiteMessageResul){
		if(StringUtils.isEmpty(content))
			return "";
		
		//获取商品
		ExecuteResult<ItemDTO> itemResult = itemExportService.getItemById(webSiteMessageResul.getGoodsId());
		ItemDTO itemDTO = itemResult.getResult();
		//替换订单编号
		content = content.replaceAll(MessageConstants.ORDER_NO, webSiteMessageResul.getOrderId() == null ? "" : webSiteMessageResul.getOrderId().toString());
		//替换日期
		content = content.replaceAll(MessageConstants.DATE, webSiteMessageResul.getDate() == null ? "" : webSiteMessageResul.getDate());
		//替换商品名称
		content = content.replaceAll(MessageConstants.GOOD_NAME,itemDTO == null ? "" : itemDTO.getItemName());
		//替换卖家用户名称
		content = content.replaceAll(MessageConstants.SELL_USER_NAME, seller == null ? "" : seller.getUname());
		//替换买家用户名称
		content = content.replaceAll(MessageConstants.USER_NAME, buyer == null ? "" : buyer.getUname());
		//替换邮箱
		content = content.replaceAll(MessageConstants.MAIL, webSiteMessageResul.getMail() == null ? "" : webSiteMessageResul.getMail());
		//替换手机
		content = content.replaceAll(MessageConstants.PHONE, webSiteMessageResul.getPhone() == null ? "" : webSiteMessageResul.getPhone());
		//替换金额
		content = content.replaceAll(MessageConstants.MONEY, webSiteMessageResul.getMoney() == null ? "" : webSiteMessageResul.getMoney());
		//替换内容
		content = content.replaceAll(MessageConstants.CONSULTATION_REPLY_CONTENT, webSiteMessageResul.getContent() == null ? "" : webSiteMessageResul.getContent());
		//替换求购名字
		content = content.replaceAll(MessageConstants.ASK_TO_BUY, webSiteMessageResul.getTmp() == null ? "" : webSiteMessageResul.getTmp());
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
		String website_prefix = messageTemplateFileUtil.getValue(MessageConstants.WEBSITE_PREFIX);
		//获取短信模版前缀
		String sms_perfix = messageTemplateFileUtil.getValue(MessageConstants.SMS_PREFIX);
		//获取邮箱模版前缀
		String mail_prefix = messageTemplateFileUtil.getValue(MessageConstants.MAIL_PREFIX);
		//获取邮箱主题模版前缀
		String mail_title_prefix = messageTemplateFileUtil.getValue(MessageConstants.MAIL_TITLE_PREFIX);
		//获取邮箱主题模版后缀
		String mail_title_subfix = messageTemplateFileUtil.getValue(MessageConstants.MAIL_TITLE_SUBFFIX);
		//获取买家模版后缀
		String buyer_suffix = messageTemplateFileUtil.getValue(MessageConstants.BUYER_SUBFFIX);
		//获取卖家模版后缀
		String sell_suffix = messageTemplateFileUtil.getValue(MessageConstants.SELL_SUBFFIX);
		//站内信买家模版key
		String buyer_ws_key = website_prefix + temp_type + buyer_suffix;
		//站内信卖家模版key
		String sell_ws_key = website_prefix + temp_type + sell_suffix;
		//邮件买家模版key
		String buyer_mail_key = mail_prefix + temp_type + buyer_suffix;
		//邮件卖家模版key
		String sell_mail_key = mail_prefix + temp_type + sell_suffix;
		//邮件主题模版key 
		String mail_title_key = mail_title_prefix + temp_type + mail_title_subfix;
		//短信买家模版key
		String buyer_sms_key = sms_perfix + temp_type + buyer_suffix;
		//短信卖家模版key
		String sell_sms_key = sms_perfix + temp_type + sell_suffix;
		
		long sellerId = 0L;
		long buyerId = 0L;
		for(SendMessageUser sendMessageUser : sendMessageUserList){
			UserDTO buyer = sendMessageUser.getBuyer();
			UserDTO seller = sendMessageUser.getSeller();
			//发送内容数据
			String[] contents = null;
			String message = "";
			//相同用户只允许发送一次提示消息
			if(buyer != null && buyer.getUid() != null && buyer.getUid().longValue() != buyerId){
				//向买家推送消息
				buyerSendMessage(buyer,seller,messageTemplateFileUtil,
						 buyer_ws_key, buyer_sms_key, buyer_mail_key, mail_title_key, temp_type,
						 webSiteMessageResult, sendMessageUser.getPaltformId());
				buyerId = buyer.getUid();
			}
			//相同的用户只允许发送一次提示消息
			if(seller != null && seller.getUid() != null && seller.getUid().longValue() != sellerId){
				//如果MulMessageMaps() 不为null则为 求购  允许给多个用户发送消息
				if (webSiteMessageResult.getMulMessageMaps() != null ) {
					if (webSiteMessageResult.getMulMessageMaps().size() > 0) {
						List<Map<String, Object>>  maps = webSiteMessageResult.getMulMessageMaps();
						for (int i = 0; i < maps.size(); i++) {
							webSiteMessageResult.setTmp(maps.get(i).get("translationName").toString());
							buyer = (UserDTO) maps.get(i).get("buyer");
							//判断平台id如果是绿印的买家不需要发短信给科印
							if (buyer.getPlatformId() != null) {
								if (buyer.getPlatformId().toString().equals("2")) {
									/*sellerSendMessage(buyer,seller,messageTemplateFileUtil
											, webSiteMessageResult, sell_ws_key, temp_type, sell_sms_key
											, mail_title_key, sell_mail_key, sendMessageUser.getPaltformId());
									sellerId = seller.getUid();*/
								}
							}else if (buyer.getPlatformId() == null) {//如果是科印则发短信给买家
								sellerSendMessage(buyer,seller,messageTemplateFileUtil
										, webSiteMessageResult, sell_ws_key, temp_type, sell_sms_key
										, mail_title_key, sell_mail_key, sendMessageUser.getPaltformId());
								sellerId = seller.getUid();
							}
							webSiteMessageResult.setTmp(null);
						}
					}
				}else {
					sellerSendMessage(buyer,seller,messageTemplateFileUtil
							, webSiteMessageResult, sell_ws_key, temp_type, sell_sms_key
							, mail_title_key, sell_mail_key, sendMessageUser.getPaltformId());
					sellerId = seller.getUid();
				}
			}
		}
	}
	
	/**
	 * 获取发送人集合
	 * @param webSiteMessageResult
	 * @return
	 */
	public List getSendMessageUserList(WebSiteMessageResult webSiteMessageResult){
		SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		//获取发送人集合
		List<SendMessageUser> sendMessageUserList = new ArrayList<SendMessageUser>();
		//获取订单
		TradeOrdersDTO tradeOrdersDTO = null;
		if(webSiteMessageResult.getOrderId() != null){
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
					if(tradeOrdersDTO_.getPlatformId() != null){
						sendMessageUser_.setPaltformId(tradeOrdersDTO_.getPlatformId());
					}
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
				if(tradeOrdersDTO.getPlatformId() != null){
					sendMessageUser.setPaltformId(tradeOrdersDTO.getPlatformId());
				}
				sendMessageUserList.add(sendMessageUser);
			}
		}else if(webSiteMessageResult.getMulMessageMaps()!=null){
			List<Map<String, Object>> buyers = webSiteMessageResult.getMulMessageMaps();
			for (int i = 0; i < buyers.size(); i++) {
				UserDTO buyer = this.userExportService.queryUserById(Long.valueOf(buyers.get(i).get("buyerId").toString()));
				List<Map<String, Object>> maps = webSiteMessageResult.getMulMessageMaps();
				//遍历返回对象将返回放到mapdata里边
				for (int j = 0; j < maps.size(); j++) {
					if (maps.get(j).get("buyerId").toString().equals(buyer.getUid().toString()) && maps.get(j).get("buyer") == null) {
						maps.get(j).put("buyer",buyer);
						break;
					}
					
				}
				//发送对象
				SendMessageUser sendMessageUser_ = new SendMessageUser();
				sendMessageUser_.setBuyer(buyer);
				sendMessageUser_.setPaltformId(buyer.getPlatformId());
				sendMessageUserList.add(sendMessageUser_);
			}
			//如果是审核通过发送给所有卖家
			String status = webSiteMessageResult.getBackResult();
			if (status.contains("2")) {
				for (int i = 0; i < buyers.size(); i++) {
					String translationId = buyers.get(i).get("id").toString();
					TranslationInfoDTO translationInfoDTO = new TranslationInfoDTO();
					translationInfoDTO.setTranslationNo(translationId);
					Pager page=new Pager();
					page.setRows(1000);
					ExecuteResult<DataGrid<Map>> translationResult = translationExportService.queryTranslationInfoPager(translationInfoDTO, page);//.queryByTranslationInfo(translationInfoDTO);
					if(translationResult != null && translationResult.getResult() != null && translationResult.getResult().getRows() != null&& translationResult.getResult().getRows().size() != 0){
						List<Map> translationInfoList =  translationResult.getResult().getRows();
						for(Map translationInfo : translationInfoList){
							if(translationInfo.get("categoryId") != null){
								Integer categoryId = Integer.parseInt(translationInfo.get("categoryId").toString());
//								shopExportService.findShopInfoByCondition(shopDTO, pager)
								ShopCategoryDTO shopCategoryDTO = new ShopCategoryDTO();
								shopCategoryDTO.setCid(categoryId.longValue());
								ExecuteResult<DataGrid<ShopCategoryDTO>> shopCategoryReuslt = shopCategoryExportService.queryShopCategory(shopCategoryDTO, null);
								 if(shopCategoryReuslt != null && shopCategoryReuslt.getResult() != null && shopCategoryReuslt.getResult().getRows() != null && shopCategoryReuslt.getResult().getRows().size() != 0){
									 int size = shopCategoryReuslt.getResult().getRows().size();
									 for(int j = 0 ; j < size ; j++){
										 ShopCategoryDTO shopCategory =  shopCategoryReuslt.getResult().getRows().get(j);
										 Long sellerId = shopCategory.getSellerId();
//										 sellerList.add(sellerId);
										//发送对象
										SendMessageUser sendMessageUser_ = new SendMessageUser();
										UserDTO seller = this.userExportService.queryUserById(Long.valueOf(sellerId));
										sendMessageUser_.setSeller(seller);
										sendMessageUser_.setPaltformId(seller.getPlatformId());
										sendMessageUserList.add(sendMessageUser_);
									 }
									
								 }
								
								
							}
						}
					}
				}
				
			}
			
		}else{
			SendMessageUser sendMessageUser = new SendMessageUser();
			//获取买家对象
			if(webSiteMessageResult.getBuyerId() != null){
				UserDTO buyer = this.userExportService.queryUserById(webSiteMessageResult.getBuyerId());
				sendMessageUser.setBuyer(buyer);
				sendMessageUser.setPaltformId(buyer.getPlatformId());
			}
			//获取卖家对象
			if(webSiteMessageResult.getSellerId() != null){
				UserDTO seller = this.userExportService.queryUserById(webSiteMessageResult.getSellerId());
				sendMessageUser.setSeller(seller);
				sendMessageUser.setPaltformId(seller.getPlatformId());
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
			WebSiteMessageResult webSiteMessageResult ,Integer paltformId){
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
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult);
					webSiteMessageDTO.setWmToUserid(buyer.getUid());
					webSiteMessageDTO.setWmToUsername(buyer.getUname());
					//平台id  null科印   2 绿印
					if(paltformId != null){
						webSiteMessageDTO.setPlatformId(paltformId);
					}
					//信息内容
					webSiteMessageDTO.setWmContext(content); 
					baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
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
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult);
					//存放短信数据
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setAddress(buyer.getUmobile());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(MessageConstants.SMS_TYPE);
					//不分平台，发送短信
//					smsPublisherImpl.sendMessage(baseSendMessageDTO);
					if(paltformId != null){
						baseSendMessageDTO.setPlatformId(paltformId);
					}
					baseSendMessageDTO.setContentType(MessageConstants.CONTENT_TYPE);
					//分平台，发送短信
					baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
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
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult);
					//拼装邮件发送格式
					content = mailContent(content,paltformId);
					//邮件信息
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setTitle(title);
					baseSendMessageDTO.setAddress(buyer.getUserEmail());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(MessageConstants.MAIL_TYPE);
					//不分平台，发送邮件
//					emailPublisherImpl.sendMessage(baseSendMessageDTO);
					if(paltformId != null){
						baseSendMessageDTO.setPlatformId(paltformId);
					}
					baseSendMessageDTO.setContentType(MessageConstants.CONTENT_TYPE);
					//分平台，发送邮件
					baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
				}
			}
		}
	}
	
	/**
	 * 向卖家推送消息
	 */
	public void sellerSendMessage(UserDTO buyer,UserDTO seller,MessageTemplateFileUtil messageTemplateFileUtil
			,WebSiteMessageResult webSiteMessageResult,String sell_ws_key,String temp_type,String sell_sms_key
			,String mail_title_key,String sell_mail_key , Integer paltformId){
		Long buyerId = seller.getUid().longValue();
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
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult);
					webSiteMessageDTO.setWmToUserid(seller.getUid());
					webSiteMessageDTO.setWmToUsername(seller.getUname());
					//平台id  null科印   2 绿印
					if(paltformId != null){
						webSiteMessageDTO.setPlatformId(paltformId);
					}
					//信息内容
					webSiteMessageDTO.setWmContext(content); 
					baseWebSiteMessageService.addWebMessage(webSiteMessageDTO);
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
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult);
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setAddress(seller.getUmobile());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(MessageConstants.SMS_TYPE);
					//不分平台，发送短信
//					smsPublisherImpl.sendMessage(baseSendMessageDTO);
					if(paltformId != null){
						baseSendMessageDTO.setPlatformId(paltformId);
					}
					baseSendMessageDTO.setContentType(MessageConstants.CONTENT_TYPE);
					//分平台，发送短信
					baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
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
//			String[] titles = messageTemplateFileUtil.getValue(mail_title_key).split("$");
			int index = 0;
			for(String content : contents){
				if(StringUtils.isNotEmpty(content) && seller != null){
					content = this.contentReplace(content, buyer, seller, webSiteMessageResult);
					//拼装邮件发送格式
					content = mailContent(content,paltformId);
					baseSendMessageDTO.setId(null);
					baseSendMessageDTO.setTitle(title);
					baseSendMessageDTO.setAddress(seller.getUserEmail());
					baseSendMessageDTO.setContent(content);
					baseSendMessageDTO.setType(MessageConstants.MAIL_TYPE);
					//不分平台，发送邮件
//					emailPublisherImpl.sendMessage(baseSendMessageDTO);
					if(paltformId != null){
						baseSendMessageDTO.setPlatformId(paltformId);
					}
					baseSendMessageDTO.setContentType(MessageConstants.CONTENT_TYPE);
					//分平台，发送邮件
					baseSendMessageService.sendMessageToMQ(baseSendMessageDTO);
				}
			}
		}
	}
	
	/**
	 * 校验是否是需要拦截的地址
	 * @param request
	 * @return
	 */
	private boolean validateUrl(HttpServletRequest request){
		String requestRri = request.getRequestURI();
		if (uriList!=null&&uriList.size()>0){
            for (String uri:uriList){
                if(requestRri.contains(uri)){
                   return true;
                }
            }
        }
		return false;
	}
	
	/**
	 * 格式化邮箱内容模板
	 * @param content
	 * @return
	 */
	public String mailContent(String content,Integer platformId){
		//获取邮件内容模板 头信息
		String mail_content_top = "";
		String mail_content_bottom = "";
		if(platformId == null){
			mail_content_top = MessageTemplateFileUtil.getInstance().getValue(MessageConstants.MAIL_CONTENT_TOP);
		}else if(platformId == PlatformEnum.GREEN.getId()){
			mail_content_top = MessageTemplateFileUtil.getInstance().getValue(MessageConstants.MAIL_CONTENT_TOP_GREEN);
		}
		
		//获取邮件内容模板 底部信息
		if(platformId == null){
			mail_content_bottom = MessageTemplateFileUtil.getInstance().getValue(MessageConstants.MAIL_CONTENT_BOTTOM);
		}else if(platformId == PlatformEnum.GREEN.getId()){
			mail_content_bottom = MessageTemplateFileUtil.getInstance().getValue(MessageConstants.MAIL_CONTENT_BOTTOM_GREEN);
		}
		return mail_content_top + content + mail_content_bottom;
	}
}
