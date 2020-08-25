package com.camelot.pricecenter.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.dto.emums.CouponUsingPlatformEnum;
import com.camelot.maketcenter.dto.emums.IntegralTypeEnum;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.openplatform.common.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartIntegralService;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.usercenter.service.UserIntegralTrajectoryExportService;

/**
 * 
 * <p>Description: [积分]</p>
 * Created on 2015年12月15日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopCartIntegralService")
public class ShopCartIntegralServiceImpl implements ShopCartIntegralService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShopCartIntegralServiceImpl.class);
	
	@Resource
	private UserIntegralTrajectoryExportService userIntegralTrajectoryService;
	
	@Resource
	private IntegralConfigExportService integralConfigService;
	
	@Override
	public ExecuteResult<ShopCartDTO> validateIntegral(ShopCartDTO shopCartDTO, Long uid, Integer integral,
			Integer platformId) {
		LOG.info("\n 方法[{}]，入参：[{}][{}][{}][{}]", "ShopCartIntegralServiceImpl-validateIntegral", JSON.toJSONString(shopCartDTO), uid, integral, platformId);
		ExecuteResult<ShopCartDTO> result = new ExecuteResult<ShopCartDTO>();
		try{
			// 科印可能为空
			if(platformId == null){
				platformId = CouponUsingPlatformEnum.KY.getCode();
			}
			if(integral != null){
				// 查询当前用户总积分
				ExecuteResult<Long> totalIntegralResult = userIntegralTrajectoryService.queryTotalIntegral(uid);
				if (totalIntegralResult.isSuccess() && totalIntegralResult.getResult() != null) {
					// 用户的总积分
					Long totalIntegral = totalIntegralResult.getResult();
					if(integral.intValue() > totalIntegral){
						result.addErrorMessage("积分不足");
						shopCartDTO.getUnusualMsg().add("积分不足");
					} else{
						// 查询每笔订单可使用的最大积分
						IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
						integralConfigDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_USING.getCode());
						integralConfigDTO.setPlatformId(platformId == Constants.PLATFORM_KY_ID ? 0 : platformId);
						ExecuteResult<DataGrid<IntegralConfigDTO>> executeResult = integralConfigService.queryIntegralConfigDTO(integralConfigDTO, null);
						if (executeResult.isSuccess() 
								&& executeResult.getResult() != null
								&& executeResult.getResult().getRows() != null 
								&& executeResult.getResult().getRows().size() > 0) {
							IntegralConfigDTO dto = executeResult.getResult().getRows().get(0);
							Long useIntegral = dto.getUseIntegral();
							if((useIntegral == null) || (useIntegral != null && integral.intValue() > useIntegral.intValue())){
								result.addErrorMessage("使用的积分超过了每笔订单可使用的最大积分限制");
								shopCartDTO.getUnusualMsg().add("使用的积分超过了每笔订单可使用的最大积分限制");
							}
							// 查询积分可兑换金额
							IntegralConfigDTO configDTO = new IntegralConfigDTO();
							configDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_EXCHANGE.getCode());
							configDTO.setPlatformId(0);
							ExecuteResult<DataGrid<IntegralConfigDTO>> configResult = integralConfigService.queryIntegralConfigDTO(configDTO, null);
							if (configResult.isSuccess() 
									&& configResult.getResult() != null 
									&& configResult.getResult().getRows() != null
									&& configResult.getResult().getRows().size() > 0
									&& configResult.getResult().getRows().get(0).getExchangeRate() != null
									&& configResult.getResult().getRows().get(0).getExchangeRate().compareTo(BigDecimal.ZERO) > 0) {
								configDTO = configResult.getResult().getRows().get(0);
								// 一积分可兑换金额
								BigDecimal exchangeRate = configDTO.getExchangeRate();
								BigDecimal discountPrice = BigDecimal.valueOf(integral).multiply(exchangeRate);
								if(discountPrice.compareTo(shopCartDTO.getPayTotal()) > 0){
									Integer newIntegral = shopCartDTO.getPayTotal().divide(exchangeRate).setScale(0, RoundingMode.FLOOR).intValue();
									if(newIntegral.intValue() > integral){
										newIntegral = integral;
									}
									shopCartDTO.setIntegral(newIntegral);
								}
							}
						}
					}
				} else{
					result.addErrorMessage("无可用积分");
					shopCartDTO.getUnusualMsg().add("无可用积分");
				}
			} else{
				result.addErrorMessage("积分为空");
				shopCartDTO.getUnusualMsg().add("积分为空");
			}
			result.setResult(shopCartDTO);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			LOG.info("\n 方法[{}]，异常：[{}]", "ShopCartIntegralServiceImpl-validateIntegral", e);
		}
		LOG.info("\n 方法[{}]，出参：[{}]", "ShopCartIntegralServiceImpl-validateIntegral", JSONObject.toJSONString(result));
		
		return result;
	}

	@Override
	public ExecuteResult<ShopCartDTO> calcIntegral(ShopCartDTO shopCartDTO, Long uid, Integer platformId) {
		LOG.info("\n 方法[{}]，入参：[{}][{}][{}]", "ShopCartIntegralServiceImpl-calcIntegral", JSON.toJSONString(shopCartDTO), uid, platformId);
		ExecuteResult<ShopCartDTO> calcIntegralResult = new ExecuteResult<ShopCartDTO>();
		try{
			if(shopCartDTO.getIntegral() == null){
				calcIntegralResult.addErrorMessage("积分为空");
			} else if(shopCartDTO.getIntegral().intValue() <= 0){
				calcIntegralResult.addErrorMessage("积分必须为大于0的正整数");
			} else if(shopCartDTO.getPayTotal().compareTo(BigDecimal.ZERO) <= 0){
				calcIntegralResult.addErrorMessage("订单支付金额必须为大于0的正整数，不满足积分的使用条件");
			}
			// 科印可能为空
			if(platformId == null){
				platformId = CouponUsingPlatformEnum.KY.getCode();
			}
			if(calcIntegralResult.isSuccess()){
				ExecuteResult<ShopCartDTO> validateResult = this.validateIntegral(shopCartDTO, uid, shopCartDTO.getIntegral(), platformId);
				if (validateResult.isSuccess() && validateResult.getResult() != null
						&& validateResult.getResult().getIntegral() != null
						&& validateResult.getResult().getIntegral().intValue() > 0) {
					shopCartDTO = validateResult.getResult();
					// 购物车中所有选中的商品
					List<ProductInPriceDTO> productInPriceDTOs = new ArrayList<ProductInPriceDTO>();

					for (ShopOutPriceDTO shopOutPriceDTO : shopCartDTO.getShops()) {
						if(shopOutPriceDTO.getProducts() != null && shopOutPriceDTO.getProducts().size() > 0){
							for (ProductInPriceDTO productInPriceDTO : shopOutPriceDTO.getProducts()) {
								if(productInPriceDTO.getChecked()){
									productInPriceDTOs.add(productInPriceDTO);
								}
							}
						}
					}
					
					// 参与优惠的商品应支付总额
					BigDecimal payTotal = BigDecimal.ZERO;
					for (ProductInPriceDTO productInPriceDTO : productInPriceDTOs) {
						payTotal = payTotal.add(productInPriceDTO.getPayTotal());
					}
					
					// 总优惠金额
					BigDecimal discountPrice = BigDecimal.ZERO;
					
					// 查询积分可兑换金额
					IntegralConfigDTO integralConfigDTO = new IntegralConfigDTO();
					integralConfigDTO.setIntegralType(IntegralTypeEnum.INTEGRAL_EXCHANGE.getCode());
					integralConfigDTO.setPlatformId(platformId == Constants.PLATFORM_KY_ID ? 0 : platformId);
					ExecuteResult<DataGrid<IntegralConfigDTO>> configResult = integralConfigService.queryIntegralConfigDTO(integralConfigDTO, null);
					// 每个积分可兑换金额（元为单位）
					BigDecimal exchangeRate = BigDecimal.ZERO;
					if (configResult.isSuccess() 
							&& configResult.getResult() != null 
							&& configResult.getResult().getRows() != null
							&& configResult.getResult().getRows().size() > 0) {
						IntegralConfigDTO dto = configResult.getResult().getRows().get(0);
						if(dto.getExchangeRate() != null){
							exchangeRate = dto.getExchangeRate();
						}
						discountPrice = BigDecimal.valueOf(shopCartDTO.getIntegral()).multiply(exchangeRate);
						if(discountPrice.compareTo(payTotal) > 0){
							discountPrice = payTotal;
						}
					}
					if(discountPrice.compareTo(BigDecimal.ZERO) > 0){
						// 积分优惠金额分摊到商品
						this.shareIntegralDiscountToProducts(productInPriceDTOs, payTotal, discountPrice);
						// 积分分摊到店铺
						this.shareIntegralDiscountToShop(shopCartDTO, shopCartDTO.getIntegral());
						// 设置父订单一个积分兑换的钱数
						shopCartDTO.setExchangeRate(exchangeRate);
						for (ShopOutPriceDTO shopOutPriceDTO : shopCartDTO.getShops()) {
							if(shopOutPriceDTO.getProducts() != null && shopOutPriceDTO.getProducts().size() > 0){
								for(ProductInPriceDTO inPriceDTO : shopOutPriceDTO.getProducts()){
									if (inPriceDTO.getIntegralDiscount() != null
											&& inPriceDTO.getIntegralDiscount().compareTo(BigDecimal.ZERO) > 0) {
										// 设置订单商品优惠券优惠
										TradeOrderItemsDiscountDTO discountDTO = inPriceDTO.getTradeOrderItemsDiscountDTO();
										if(discountDTO == null){
											discountDTO = new TradeOrderItemsDiscountDTO();
										} 
										discountDTO.setIntegralDiscount(inPriceDTO.getIntegralDiscount());
										discountDTO.setIntegralType(ActivityTypeEnum.PLATFORMINTEGRATION.getStatus());
										inPriceDTO.setTradeOrderItemsDiscountDTO(discountDTO);
									}
								}
							}
							if (shopOutPriceDTO.getIntegral() != null && shopOutPriceDTO.getIntegral().intValue() > 0) {
								// 设置子订单每个积分可兑换金额（元为单位）
								shopOutPriceDTO.setExchangeRate(exchangeRate);
								// 创建活动记录对象
								ActivityRecordDTO recordDTO = new ActivityRecordDTO();
								recordDTO.setDiscountAmount(shopOutPriceDTO.getIntegralDiscount());
								recordDTO.setOrderId(null); // 创建完平台订单后重新设置此值
								recordDTO.setPromotionId(null); // 创建完平台订单后重新设置此值(该值为积分使用记录的ID)
								recordDTO.setShopId(shopOutPriceDTO.getShopId());
								// 平台积分活动
								recordDTO.setType(ActivityTypeEnum.PLATFORMINTEGRATION.getStatus());
								shopOutPriceDTO.getActivityRecordDTOs().add(recordDTO);
							}
						}
					}
				} else{
					calcIntegralResult.getErrorMessages().addAll(validateResult.getErrorMessages());
				}
			} else{
				shopCartDTO.setIntegral(null);
			}
			calcIntegralResult.setResult(shopCartDTO);
		} catch (Exception e) {
			calcIntegralResult.addErrorMessage(e.toString());
			LOG.info("\n 方法[{}]，异常：[{}]", "ShopCartIntegralServiceImpl-calcIntegral", e);
		}
		LOG.info("\n 方法[{}]，出参：[{}]", "ShopCartIntegralServiceImpl-calcIntegral", JSONObject.toJSONString(calcIntegralResult));
		return calcIntegralResult;
	}
	
	/**
	 * 
	 * <p>Description: [积分优惠金额分摊到商品]</p>
	 * Created on 2015年12月10日
	 * @param productInPriceDTOs 参与优惠的所有商品
	 * @param payTotal 参与优惠的所有商品的金额
	 * @param discountPrice 参与优惠的商品的总优惠金额
	 * @author:[宋文斌]
	 */
	private void shareIntegralDiscountToProducts(List<ProductInPriceDTO> productInPriceDTOs, BigDecimal payTotal,
			BigDecimal discountPrice) {
		LOG.info("\n 方法[{}]，入参：[{}][{}][{}]", "ShopCartIntegralServiceImpl-shareIntegralDiscountToProducts", JSON.toJSONString(productInPriceDTOs), payTotal, discountPrice);
		if (discountPrice != null && discountPrice.compareTo(BigDecimal.ZERO) > 0) {
			// 前面的优惠金额之和
			BigDecimal addPrice = BigDecimal.ZERO;
			int i = 1;
			for (ProductInPriceDTO productInPriceDTO : productInPriceDTOs) {
				// 当前商品金额
				BigDecimal price = BigDecimal.ZERO;
				BigDecimal averagePrice = BigDecimal.ZERO;
				// 优惠金额*（单个商品总金额/活动商品总金额）
				averagePrice = (discountPrice.multiply(productInPriceDTO.getPayTotal().divide(payTotal, 10, RoundingMode.HALF_UP))).setScale(4, RoundingMode.DOWN).setScale(2, RoundingMode.UP);
				if (productInPriceDTOs.size() > 1 && i == productInPriceDTOs.size()) {// 最后一件优惠=总优惠-前面的优惠金额之和（为防止最后校验不通过）
					if (discountPrice.compareTo(addPrice) >= 0) {
						price = productInPriceDTO.getPayTotal().subtract(discountPrice.subtract(addPrice));// 当前商品金额-（优惠金额-前面的金额之和）
						productInPriceDTO.setIntegralDiscount(discountPrice.subtract(addPrice));// 积分优惠金额
					} else {
						price = productInPriceDTO.getPayTotal();
						productInPriceDTO.setIntegralDiscount(BigDecimal.ZERO);
					}
					productInPriceDTO.setPayTotal(price);// 优惠后金额
				} else {
					if (discountPrice.subtract(addPrice).compareTo(averagePrice) < 0) {
						price = productInPriceDTO.getPayTotal().subtract(discountPrice.subtract(addPrice));
						productInPriceDTO.setIntegralDiscount(discountPrice.subtract(addPrice));// 积分优惠金额
					} else {
						price = productInPriceDTO.getPayTotal().subtract(averagePrice);
						productInPriceDTO.setIntegralDiscount(averagePrice);
					}
					productInPriceDTO.setPayTotal(price);// 优惠后金额
					addPrice = addPrice.add(averagePrice);
					i += 1;// 默认为1，然后循环+1，判断是否是最后一件商品
				}
			}
		}
	}
	
	/**
	 * 
	 * <p>Description: [积分分摊到店铺]</p>
	 * Created on 2015年12月11日
	 * @param shopCartDTO
	 * @param integral 总使用积分
	 * @author:[宋文斌]
	 */
	private void shareIntegralDiscountToShop(ShopCartDTO shopCartDTO, Integer integral) {
		LOG.info("\n 方法[{}]，入参：[{}][{}]", "ShopCartIntegralServiceImpl-shareIntegralDiscountToShop", JSON.toJSONString(shopCartDTO), integral);
		if(integral != null && integral.intValue() > 0){
			List<ShopOutPriceDTO> shopDTOs = new ArrayList<ShopOutPriceDTO>();
			for(ShopOutPriceDTO shopOutPriceDTO : shopCartDTO.getShops()){
				if(shopOutPriceDTO.getShopId() != null && shopOutPriceDTO.getShopId() != 0){
					shopDTOs.add(shopOutPriceDTO);
				}
			}
			
			// 每个店铺需要分配的积分
//			int averageIntegral = integral / shopDTOs.size();
//			for (int i = 0; i < shopDTOs.size(); i++) {
//				ShopOutPriceDTO shopOutPriceDTO = shopDTOs.get(i);
//				if (i == shopDTOs.size() - 1) {
//					// 最后一个店铺=总优惠积分-前面几个店铺分摊的积分之和
//					shopOutPriceDTO.setIntegral(integral - (shopDTOs.size() - 1) * averageIntegral);
//				} else {
//					shopOutPriceDTO.setIntegral(averageIntegral);
//				}
//			}
			
			// 前面的优惠积分之和
			int addIntegral = 0;
			int i = 1;
			for(ShopOutPriceDTO shopOutPriceDTO : shopDTOs){
				int averageIntegral = 0;
				// 总积分*（店铺商品总金额/总支付金额）
				averageIntegral = (BigDecimal.valueOf(integral).multiply(shopOutPriceDTO.getPayTotal().add(shopOutPriceDTO.getIntegralDiscount()).divide(shopCartDTO.getPayTotal().add(shopCartDTO.getIntegralDiscount()), 10, RoundingMode.HALF_UP))).setScale(0, RoundingMode.UP).intValue();
				if (shopDTOs.size() > 1 && i == shopDTOs.size()) {// 最后一件优惠=总优惠-前面的优惠之和（为防止最后校验不通过）
					if (integral > addIntegral) {
						shopOutPriceDTO.setIntegral(integral - addIntegral);// 积分优惠
					} else {
						shopOutPriceDTO.setIntegral(0);
					}
				} else {
					if ((integral - addIntegral) < averageIntegral) {
						shopOutPriceDTO.setIntegral(integral - addIntegral);// 积分优惠
					} else {
						shopOutPriceDTO.setIntegral(averageIntegral);
					}
					addIntegral = addIntegral + averageIntegral;
					i += 1;// 默认为1，然后循环+1，判断是否是最后一件商品
				}
			}
		}
	}
	
}
