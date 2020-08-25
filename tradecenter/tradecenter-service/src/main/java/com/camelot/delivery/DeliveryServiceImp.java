package com.camelot.delivery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.delivery.dao.DeliveryDao;
import com.camelot.delivery.dto.DeliveryDTO;
import com.camelot.delivery.dto.DeliveryReturnMessage;
import com.camelot.delivery.dto.PushDelibery;
import com.camelot.delivery.dto.SendBodyDTO;
import com.camelot.delivery.service.DeliveryService;
import com.camelot.delivery.utils.DeliveryUtil;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.tradecenter.domain.TradeOrderItems;
import com.camelot.tradecenter.dto.TradeOrderItemsDTO;
import com.camelot.tradecenter.dto.TradeOrdersDTO;
import com.camelot.tradecenter.service.TradeOrderExportService;

/**
 * 物流信息service实现类
 * @author xianmarui
 *
 */
@Service("deliveryService")
public class DeliveryServiceImp implements DeliveryService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryServiceImp.class);
	
	@Resource
	private DeliveryDao deliveryDao;
	
	//物流（快递100）接口
	@Resource
	private DeliveryService deliveryService;
	
	@Resource
	private TradeOrderExportService tradeOrderExportService;
	
	
	/**
	 * 保存快递100的订阅状态
	 */
	@Override
	public String saveDeliverStatus(SendBodyDTO sendBody, DeliveryDTO delivery) {
		
		DeliveryReturnMessage drm = null;
		String returnMsg = "true";
		returnMsg = DeliveryUtil.sendDeliveryMessage(sendBody);
		if ("error".equals(returnMsg)) {
			return "500";
		}
		try {
			drm = JSON.parseObject(returnMsg, DeliveryReturnMessage.class);
		} catch (Exception e) {
			LOGGER.info("快递100物流订阅返回信息");
			return "500";
		}
		
		delivery.setCreateTime(new Date());
		delivery.setDeliveryNumber(sendBody.getNumber());
		delivery.setDeliveryCompanyCode(sendBody.getCompany());
		
		if (drm.getReturnCode() == 200 && drm != null) {
			delivery.setStatus(0);
		} else {
			return drm.getReturnCode().toString();
		}
		
		deliveryDao.saveDeliverStatus(delivery);
		
		return drm.getReturnCode().toString();
		
	}

	/**
	 * 更新快递100推回的物流信息 解析收据拿到物流单号，更新物流信息
	 */
	@Override
	public void updateDeliverMessage(String message) {
		// 解析messge字符串得到物流ID
		DeliveryDTO delibery = new DeliveryDTO();
		PushDelibery pb = JSON.parseObject(message, PushDelibery.class);
		delibery.setMessage(message);
		delibery.setDeliveryNumber(pb.getLastResult().getNu());
		delibery.setDeliveryCompanyCode(pb.getLastResult().getCom());
		deliveryDao.updateDeliverMessage(delibery);
	}

	/**
	 * 更具订单信息查询物流信息
	 */
	@Override
	public DeliveryDTO getDeliveryInfoByItemId(String orderItemId) {
		return deliveryDao.selectDeliveryInfoByItemId(orderItemId);
	}
	
	/**
	 * 更新快递100订阅状态 查询异常订单信息
	 */
	@Override
	public String updateDeliverStatus(SendBodyDTO sendBodyDTO, DeliveryDTO delibery) {
		String returnMsg = DeliveryUtil.sendDeliveryMessage(sendBodyDTO);
		DeliveryReturnMessage drm = JSON.parseObject(returnMsg, DeliveryReturnMessage.class);
		if (drm.getReturnCode() == 200 && drm != null) {
			delibery.setStatus(0);
			deliveryDao.updateDeliverStatus(delibery);
		}
		return drm.getReturnCode().toString();
	}

	/**
	 * 查询异常的物流单
	 */
	@Override
	public List<DeliveryDTO> selectDliverUnusualStatus() {
		return deliveryDao.selectDliverUnusualStatus();
	}

	/**
	 * 
	 * <p>Discription:[新增快递100订阅信息]</p>
	 * Created on 2016年1月14日
	 * @param sendBodyDTO
	 * @param deliberys
	 * @return
	 * @author:[宋文斌]
	 */
	@Override
	public ExecuteResult<String> batchAddDeliverInfo(SendBodyDTO sendBodyDTO, DeliveryDTO delibery) {
		LOGGER.info("\n 方法[{}]，入参：[{}][{}]", "DeliveryServiceImpl-batchAddDeliverInfo", JSONObject.toJSONString(sendBodyDTO), JSONObject.toJSONString(delibery));
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			if (sendBodyDTO != null && delibery != null) {
				sendBodyDTO.setCompany(delibery.getDeliveryCompanyCode());
				sendBodyDTO.setNumber(delibery.getDeliveryNumber());
				// 是否需要订阅
				boolean isNeedSubscribe = true;
				// 需要订阅的订单商品详情数量
				int count = 0;
				// 需要添加的物流信息
				List<DeliveryDTO> addDeliberys = new ArrayList<DeliveryDTO>();
				// 需要修改的物流信息
				List<DeliveryDTO> updateDeliberys = new ArrayList<DeliveryDTO>();
				// 根据订单ID(orderId)查询该订单对应的订单信息(order)
				ExecuteResult<TradeOrdersDTO> executeResult = tradeOrderExportService.getOrderById(delibery.getOrderId());
				if(executeResult.isSuccess() && executeResult.getResult() != null){
					// 需要修改或添加的物流商品
					List<TradeOrderItemsDTO> itemsDTOs = new ArrayList<TradeOrderItemsDTO>();
					List<TradeOrderItemsDTO> tradeOrderItemsDTOs = executeResult.getResult().getItems();
					// 是否应用到相同运费模版的其他商品
					if(delibery.getIsSametemplate().equals("yes")){
						for(TradeOrderItemsDTO tradeOrderItemsDTO : tradeOrderItemsDTOs){
							if(delibery.getShopFreightTemplateId().equals(tradeOrderItemsDTO.getShopFreightTemplateId())){
								itemsDTOs.add(tradeOrderItemsDTO);
							}
						}
					} else{
						for(TradeOrderItemsDTO orderItemsDTO : tradeOrderItemsDTOs){
							if(orderItemsDTO.getOrderItemId().longValue() == delibery.getOrderItemId().longValue()){
								itemsDTOs.add(orderItemsDTO);
								break;
							}
						}
					}
					for(TradeOrderItemsDTO tradeOrderItemsDTO : itemsDTOs){
						// 保存之前判断订单是否在物流表中存在
						DeliveryDTO deliveryDTO = deliveryService.getDeliveryInfoByItemId(tradeOrderItemsDTO.getOrderItemId().toString());
						// 如果存在订单号则进行修改，不存在则新增
						if (deliveryDTO != null) {
							// 判断是否修改了物流编号和物流公司，如果修改了，那么重新订阅
							String oldDeliveryNumber = deliveryDTO.getDeliveryNumber();
							String oldDeliveryCompanyCode = deliveryDTO.getDeliveryCompanyCode();
							if(!sendBodyDTO.getNumber().equals(oldDeliveryNumber) || !sendBodyDTO.getCompany().equals(oldDeliveryCompanyCode)){
								count ++;
							}
							deliveryDTO.setDeliveryCompanyCode(delibery.getDeliveryCompanyCode());
							deliveryDTO.setDeliveryCompanyName(delibery.getDeliveryCompanyName());
							deliveryDTO.setDeliveryNumber(delibery.getDeliveryNumber());
							deliveryDTO.setIsSametemplate(delibery.getIsSametemplate());
							deliveryDTO.setDeliveryRemark(delibery.getDeliveryRemark());
							deliveryDTO.setMobile(delibery.getMobile());
							deliveryDTO.setStatus(0);
							deliveryDTO.setUpdateTime(new Date());
							updateDeliberys.add(deliveryDTO);
						} else{
							deliveryDTO = new DeliveryDTO();
							BeanUtils.copyProperties(delibery, deliveryDTO);
							deliveryDTO.setOrderItemId(tradeOrderItemsDTO.getOrderItemId());
							deliveryDTO.setItemId(tradeOrderItemsDTO.getItemId());
							deliveryDTO.setStatus(0);
							deliveryDTO.setCreateTime(new Date());
							addDeliberys.add(deliveryDTO);
						}
					}
					
					if(isNeedSubscribe){
						// 订阅请求，获取返回信息
						String returnMsg = DeliveryUtil.sendDeliveryMessage(sendBodyDTO);
						DeliveryReturnMessage drm = null;
						if (result.isSuccess() && "error".equals(returnMsg)) {
							result.setResult("500");
							result.addErrorMessage("500");
						}
						if(result.isSuccess()){
							try {
								drm = JSON.parseObject(returnMsg, DeliveryReturnMessage.class);
							} catch (Exception e) {
								LOGGER.error("快递100物流订阅返回信息{}", returnMsg);
								result.setResult("500");
								result.addErrorMessage("500");
							}
						}
						if (result.isSuccess() && drm == null) {
							result.setResult("500");
							result.addErrorMessage("500");
						}
						if (result.isSuccess() && drm != null && drm.getReturnCode() != 200) {
							result.setResult(drm.getReturnCode().toString());
							result.addErrorMessage(drm.getReturnCode().toString());
						}
					}
					if (result.isSuccess()) {
						// 修改
						for (DeliveryDTO deliveryDTO : updateDeliberys){
							deliveryDao.updateDeliverInfo(deliveryDTO);
						}
						// 新增
						for (DeliveryDTO deliveryDTO : addDeliberys){
							deliveryDao.saveDeliverStatus(deliveryDTO);
						}
						result.setResult("200");
					}
				}
			}
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			LOGGER.info("\n 方法[{}]，异常：[{}]", "DeliveryServiceImpl-batchAddDeliverInfo", e);
		}
		LOGGER.info("\n 方法[{}]，出参：[{}]", "DeliveryServiceImpl-batchAddDeliverInfo", JSONObject.toJSONString(result));
		return result;
	}
	
	/**
	 * 新增快递100订阅信息
	 */
	@Override
	public String addDeliverInfo(SendBodyDTO sendBodyDTO, DeliveryDTO delibery) {
		
		//订阅请求，获取返回信息
		String returnMsg = DeliveryUtil.sendDeliveryMessage(sendBodyDTO);
		
		DeliveryReturnMessage drm = null;
		if ("error".equals(returnMsg)) {
			return "500";
		}
		try {
			drm = JSON.parseObject(returnMsg, DeliveryReturnMessage.class);
		} catch (Exception e) {
			LOGGER.info("快递100物流订阅返回信息" + returnMsg);
			return "500";
		}
		if (drm.getReturnCode() == 200 && drm != null) {
			delibery.setStatus(0);//0：正常
		} else {
			return drm.getReturnCode().toString();
		}
		
		delibery.setCreateTime(new Date());
		//delibery.setDeliveryNumber(sendBodyDTO.getNumber());
		//delibery.setDeliveryCompanyCode(sendBodyDTO.getCompany());
		
		//新增
		deliveryDao.saveDeliverStatus(delibery);
		return drm.getReturnCode().toString();
	}
	
	/**
	 * 修改快递100订阅信息
	 */
	@Override
	public String updateDeliverInfo(SendBodyDTO sendBodyDTO, DeliveryDTO delibery) {
		LOGGER.info("\n 方法[{}]，入参：[{}][{}]", "DeliveryServiceImp-updateDeliverInfo", JSONObject.toJSONString(sendBodyDTO), JSONObject.toJSONString(delibery));
		// 查询订单物流信息
		DeliveryDTO deliveryDTO = deliveryService.getDeliveryInfoByItemId(delibery.getOrderItemId().toString());
		if (deliveryDTO != null) {
			// 判断是否修改了物流编号和物流公司，如果修改了，那么重新订阅
			String oldDeliveryNumber = deliveryDTO.getDeliveryNumber();
			String oldDeliveryCompanyCode = deliveryDTO.getDeliveryCompanyCode();
			if(!sendBodyDTO.getNumber().equals(oldDeliveryNumber) || !sendBodyDTO.getCompany().equals(oldDeliveryCompanyCode)){
				// 订阅请求，获取返回信息
				String returnMsg = DeliveryUtil.sendDeliveryMessage(sendBodyDTO);

				DeliveryReturnMessage drm = null;
				if ("error".equals(returnMsg)) {
					return "500";
				}
				try {
					drm = JSON.parseObject(returnMsg, DeliveryReturnMessage.class);
				} catch (Exception e) {
					LOGGER.info("快递100物流订阅返回信息" + drm.toString() + returnMsg);
					return "500";
				}

				if (drm != null && drm.getReturnCode() == 200) {
					delibery.setStatus(0);// 0：正常
				} else {
					return drm.getReturnCode().toString();
				}
			}
			
			delibery.setUpdateTime(new Date());
			// delibery.setDeliveryNumber(sendBodyDTO.getNumber());
			// delibery.setDeliveryCompanyCode(sendBodyDTO.getCompany());
			// 修改
			deliveryDao.updateDeliverInfo(delibery);
			return "200";
		} else {
			return "500";
		}
		
	}
}
