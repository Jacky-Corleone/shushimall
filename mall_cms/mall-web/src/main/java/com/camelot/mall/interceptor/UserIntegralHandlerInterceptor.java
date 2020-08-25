package com.camelot.mall.interceptor;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.common.enums.PayBankEnum;
import com.camelot.common.enums.PayMethodEnum;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.mall.userIntegral.UserIntegralTrajectoryService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.UserIntegralTrajectoryDTO;
/**
*
* <p>Description: [用户积分拦截器]</p>
* Created on 2015年12月8日
* @author  周志军
* Copyright (c) 2015 北京柯莱特科技有限公司 交付部
*/
public class UserIntegralHandlerInterceptor implements HandlerInterceptor {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource
	private UserIntegralTrajectoryService userIntegralService;
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView){
		log.info("进入消息拦截器");
		try {
			//获取节点编码
			log.info("获取节点编码");
			Integer tempType = this.getTempType(handler);
			if(null == tempType){
				log.info("获取积分失败，未知的类型");
				return;
			}
			//用户积分信息
			UserIntegralTrajectoryDTO userIntegralTrajectoryDTO = new UserIntegralTrajectoryDTO();
			Object orderId = request.getAttribute("orderId");
			Object shopId = request.getAttribute("shopId");
			Object userId = request.getAttribute("userId");
			Object isBaskOrder = request.getAttribute("isBaskOrder");
			Object paymentMethod = request.getAttribute("paymentMethod");
			if(orderId == null || "".equals(orderId) || shopId == null ){
				log.info("获取积分失败，订单或店铺不存在");
				return;
			}
			if(userId == null){
				log.info("获取积分失败，用户不存在");
				return;
			}
			BigDecimal amount = null;
			if(tempType==1){
				Object money = request.getAttribute("amount");
				amount = new BigDecimal(money.toString());
				if(paymentMethod == PayMethodEnum.PAY_INTEGRAL.getCode()){
					log.info("获取积分失败，积分商品无法再次获取积分");
					return ;
				}
				if(paymentMethod == PayBankEnum.OFFLINE.getQrCode()){
					log.info("获取积分失败，线下支付无法获取积分");
					return ;
				}
			}else{
				if(null == isBaskOrder) {
					// 评论获取的积分
					userIntegralTrajectoryDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_EVALUATE.getCode());
				} else {
					// 评论+晒单获取的积分
					userIntegralTrajectoryDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_BASKORDER.getCode());
				}
				amount = null;
				if(paymentMethod == PayMethodEnum.PAY_INTEGRAL.getCode()){
					log.info("获取积分失败，积分商品无法再次获取积分");
					return ;
				}
			}
			userIntegralTrajectoryDTO.setOrderId(orderId.toString());
			userIntegralTrajectoryDTO.setUserId(Long.valueOf(userId.toString()));
			userIntegralTrajectoryDTO.setShopId(Long.valueOf(shopId.toString()));
			userIntegralService.saveUserIntegral(userIntegralTrajectoryDTO,amount);
		} catch (Exception e) {
			log.error("调用用户积分信息拦截器发生错误，错误信息:",e);
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex){
	}
	
	/**
	 * 获取积分类型
	 * @param handler
	 * @param messageTemplateFileUtil
	 * @return
	 */
	private Integer getTempType(Object handler){
		HandlerMethod handlerMethod = (HandlerMethod) handler;  
		//获取地址方法名称
		String methodName = handlerMethod.getMethod().getName();
		//获取类名称
		String className = handlerMethod.getBean().getClass().getName();
		//将包名称截取
		className = className.substring(className.lastIndexOf(".")+1, className.length());
		String urlKey = className + "_" + methodName;
		
		if("BuyerEvaluationController_submitTrading".equals(urlKey)){
			return 2;
		}else{
			return 1;
		}
	}

}
