package com.camelot.pricecenter.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.activity.dto.ActivityRecordDTO;
import com.camelot.common.enums.ActivityTypeEnum;
import com.camelot.maketcenter.dto.CouponUserDTO;
import com.camelot.maketcenter.dto.CouponUsingRangeDTO;
import com.camelot.maketcenter.dto.CouponsDTO;
import com.camelot.maketcenter.dto.emums.CouponTypeEnum;
import com.camelot.maketcenter.dto.emums.CouponUsingPlatformEnum;
import com.camelot.maketcenter.dto.emums.CouponUsingRangeEnum;
import com.camelot.maketcenter.service.CouponsExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ShopOutPriceDTO;
import com.camelot.pricecenter.dto.shopCart.ShopCartDTO;
import com.camelot.pricecenter.service.ShopCartCouponService;
import com.camelot.tradecenter.dto.TradeOrderItemsDiscountDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;

/**
 * 
 * <p>Description: [优惠券]</p>
 * Created on 2015年12月15日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopCartCouponService")
public class ShopCartCouponServiceImpl implements ShopCartCouponService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShopCartCouponServiceImpl.class);
	
	@Resource
	private CouponsExportService couponsExportService;
	
	@Resource
	private UserExportService userExportService;

	@Override
	public ExecuteResult<ShopCartDTO> calcCoupon(ShopCartDTO shopCartDTO, Long uid, String couponId, Integer paymentMethod, Integer platformId) {
		ExecuteResult<ShopCartDTO> calcCouponResult = new ExecuteResult<ShopCartDTO>();
		try {
			LOG.info("\n 方法[{}]，入参：[{}][{}][{}][{}][{}]", "ShopCartCouponServiceImpl-calcCoupon", JSON.toJSONString(shopCartDTO), uid, couponId, paymentMethod, platformId);
			// 科印可能为空
			if(platformId == null){
				platformId = CouponUsingPlatformEnum.KY.getCode();
			}
			if(StringUtils.isNotBlank(couponId) && shopCartDTO.getPayTotal().compareTo(BigDecimal.ZERO) > 0){
				// 查询优惠券
				ExecuteResult<CouponsDTO> coupons = couponsExportService.queryById(couponId);
				if (coupons.isSuccess() && coupons.getResult() != null && coupons.getResult().getDeleted() == 0) {
					CouponsDTO couponsDTO = coupons.getResult();
					// 查询用户领取的优惠券
					CouponUserDTO couponUserDTO = new CouponUserDTO();
					couponUserDTO.setUserId(uid);
					couponUserDTO.setDeleted(0);// 未删除
					couponUserDTO.setUserCouponType(0);// 未使用
					couponUserDTO.setCouponId(couponId);
					ExecuteResult<DataGrid<CouponUserDTO>> couponUserResult = couponsExportService.queryCouponsUserList(couponUserDTO, null);
					if (couponUserResult.isSuccess() 
							&& couponUserResult.getResult() != null 
							&& couponUserResult.getResult().getRows() != null
							&& couponUserResult.getResult().getRows().size() > 0) {
						// 需要计算优惠的商品
						List<ProductInPriceDTO> couponProducts = this.getCouponProducts(couponsDTO, shopCartDTO);
						// 需要计算优惠的商品总金额（不包括运费）
						BigDecimal payTotal = BigDecimal.ZERO;
						if (couponProducts != null && couponProducts.size() > 0) {
							// 计算参加优惠的商品总金额
							for (ProductInPriceDTO dto : couponProducts) {
								payTotal = payTotal.add(dto.getPayTotal());
							}
							// 校验优惠券
							ExecuteResult<ShopCartDTO> validateResult = this.validateCoupon(uid, couponId, paymentMethod, shopCartDTO, platformId);
							if(validateResult.isSuccess()){
								// 设置优惠券编号（用于创建完订单成功后将此优惠券设置为已使用）
								shopCartDTO.setCouponId(couponId);
								// 总优惠金额
								BigDecimal discountPrice = calcDiscountByCouponType(couponsDTO, payTotal);
								// 优惠分摊到商品
								this.shareCouponDiscountToProducts(couponProducts, payTotal, discountPrice);
								for (ShopOutPriceDTO shopOutPriceDTO : shopCartDTO.getShops()) {
									// 该店铺的优惠券总优惠金额
									BigDecimal totalCouponDiscount = BigDecimal.ZERO;
									if(shopOutPriceDTO.getProducts() != null && shopOutPriceDTO.getProducts().size() > 0){
										for(ProductInPriceDTO inPriceDTO : shopOutPriceDTO.getProducts()){
											if (inPriceDTO.getCouponDiscount() != null
													&& inPriceDTO.getCouponDiscount().compareTo(BigDecimal.ZERO) > 0) {
												totalCouponDiscount = totalCouponDiscount.add(inPriceDTO.getCouponDiscount());
												// 设置订单商品优惠券优惠
												TradeOrderItemsDiscountDTO discountDTO = inPriceDTO.getTradeOrderItemsDiscountDTO();
												if(discountDTO == null){
													discountDTO = new TradeOrderItemsDiscountDTO();
												} 
												discountDTO.setCouponId(couponsDTO.getCouponId());
												// 平台优惠券活动
												if (couponsDTO.getCostAllocation() == 1) {
													discountDTO.setCouponType(ActivityTypeEnum.PLATFORMCOUPONS.getStatus());
												} else{
													// 店铺优惠券活动
													discountDTO.setCouponType(ActivityTypeEnum.SHOPCOUPONS.getStatus());
												}
												discountDTO.setCouponDiscount(inPriceDTO.getCouponDiscount());
												inPriceDTO.setTradeOrderItemsDiscountDTO(discountDTO);
											}
										}
									}
									if(totalCouponDiscount.compareTo(BigDecimal.ZERO) > 0){
										// 创建活动记录对象
										ActivityRecordDTO recordDTO = new ActivityRecordDTO();
										recordDTO.setDiscountAmount(totalCouponDiscount);
										recordDTO.setOrderId(null); // 创建完平台订单后重新设置此值
										recordDTO.setPromotionId(couponId);
										recordDTO.setShopId(shopOutPriceDTO.getShopId());
										if (couponsDTO.getCostAllocation() == 1) {
											// 平台优惠券活动
											recordDTO.setType(ActivityTypeEnum.PLATFORMCOUPONS.getStatus());
										} else {
											// 店铺优惠券活动
											recordDTO.setType(ActivityTypeEnum.SHOPCOUPONS.getStatus());
										}
										shopOutPriceDTO.getActivityRecordDTOs().add(recordDTO);
									}
								}
							}
						}
					} else{
						calcCouponResult.addErrorMessage("优惠券不存在或已使用");
						shopCartDTO.getUnusualMsg().add("优惠券不存在或已使用");
					}
				} else{
					calcCouponResult.addErrorMessage("优惠券不存在");
					shopCartDTO.getUnusualMsg().add("优惠券不存在");
				}
			} else{
				calcCouponResult.addErrorMessage("优惠券编号为空");
			}
		} catch (Exception e) {
			calcCouponResult.addErrorMessage(e.toString());
			LOG.info("\n 方法[{}]，异常：[{}]", "ShopCartCouponServiceImpl-calcCoupon", e);
		}
		calcCouponResult.setResult(shopCartDTO);
		LOG.info("\n 方法[{}]，出参：[{}]", "ShopCartCouponServiceImpl-calcCoupon", JSONObject.toJSONString(calcCouponResult));
		return calcCouponResult;
	}

	@Override
	public ExecuteResult<ShopCartDTO> validateCoupon(Long uid, String couponId, Integer paymentMethod,
			ShopCartDTO shopCartDTO, Integer platformId) {
		LOG.info("\n 方法[{}]，入参：[{}][{}][{}][{}][{}]", "ShopCartCouponServiceImpl-validateCoupon", uid, couponId, paymentMethod, JSON.toJSONString(shopCartDTO), platformId);
		ExecuteResult<ShopCartDTO> result = new ExecuteResult<ShopCartDTO>();
		try{
			// 科印可能为空
			if(platformId == null){
				platformId = CouponUsingPlatformEnum.KY.getCode();
			}
			if(StringUtils.isNotBlank(couponId)){
				// 查询优惠券
				ExecuteResult<CouponsDTO> coupons = couponsExportService.queryById(couponId);
				if (coupons.isSuccess() && coupons.getResult() != null && coupons.getResult().getDeleted() == 0) {
					CouponsDTO couponsDTO = coupons.getResult();
					// 校验优惠券使用平台
					if(CouponUsingPlatformEnum.KYANDWX.getCode()!=couponsDTO.getPlatformId()){
						if(platformId != couponsDTO.getPlatformId()){
							result.addErrorMessage("该优惠券不能在本平台使用");
							shopCartDTO.getUnusualMsg().add("该优惠券不能在本平台使用");
							return result;
						}
					}
					UserDTO userDTO = userExportService.queryUserByfId(uid);
					if(userDTO == null){
						result.addErrorMessage("登录状态失效，请重新登录");
						shopCartDTO.getUnusualMsg().add("登录状态失效，请重新登录");
						return result;
					}
					// 校验优惠券会员等级
					if(StringUtils.isNotBlank(couponsDTO.getCouponUserMembershipLevel()) && !"0".equals(couponsDTO.getCouponUserMembershipLevel())){
						List<Integer> vipLevels = new ArrayList<Integer>();
						for(String level : couponsDTO.getCouponUserMembershipLevel().split(",")){
							vipLevels.add(Integer.parseInt(level));
						}
						// 获取成长值，判断用户会员等级
						BigDecimal growthValue = userDTO.getGrowthValue();
						if (growthValue == null) {
							growthValue = BigDecimal.ZERO;
						}
						VipLevelEnum vipLevel = VipLevelEnum.getVipLevelEnumByGrowthValue(growthValue);
						if (!vipLevels.contains(vipLevel.getId())) {
							result.addErrorMessage("会员等级不满足该优惠券使用限制");
							shopCartDTO.getUnusualMsg().add("会员等级不满足该优惠券使用限制");
							return result;
						}
					}
					// 校验优惠券使用时间
					Date couponStartTime = couponsDTO.getCouponStartTime();
					Date couponEndTime = couponsDTO.getCouponEndTime();
					Date now = new Date();
					if (now.compareTo(couponStartTime) < 0) {
						result.addErrorMessage("该优惠券还没到可使用时间");
						shopCartDTO.getUnusualMsg().add("该优惠券还没到可使用时间");
						return result;
					}
					if (now.compareTo(couponEndTime) > 0) {
						result.addErrorMessage("该优惠券已过期");
						shopCartDTO.getUnusualMsg().add("该优惠券已过期");
						return result;
					}
					// 满足优惠券的商品
					List<ProductInPriceDTO> couponProducts = this.getCouponProducts(couponsDTO, shopCartDTO);
					if(couponProducts == null || couponProducts.size() ==0){
						result.addErrorMessage("该优惠券不满足使用范围");
						shopCartDTO.getUnusualMsg().add("该优惠券不满足使用范围");
						return result;
					}
					BigDecimal payTotal = BigDecimal.ZERO;
					for(ProductInPriceDTO p : couponProducts){
						// 优惠券的计算优先级大于积分的计算，所以总金额加上integralDiscount和couponDiscount无所谓(这时integralDiscount和couponDiscount的值都为0)，这么做是为了订单核对页校验用户的可用优惠券
						payTotal = payTotal.add(p.getPayTotal()).add(p.getIntegralDiscount()).add(p.getCouponDiscount());
					}
					// 获取优惠券类型（1:满减券,2:折扣券,3:现金券）
					Integer couponType = couponsDTO.getCouponType();
					// 优惠券总优惠金额
					BigDecimal discountPrice = BigDecimal.ZERO;
					// 满减券
					if (CouponTypeEnum.FULL_REDUCTION.getCode() == couponType) {
						// 获取优惠券满足金额
						BigDecimal meetPrice = couponsDTO.getMeetPrice();
						if (payTotal.compareTo(meetPrice) < 0) {
							result.addErrorMessage("该优惠券不满足满减条件");
							shopCartDTO.getUnusualMsg().add("该优惠券不满足满减条件");
							return result;
						}
						discountPrice = couponsDTO.getCouponAmount();
					} else if (CouponTypeEnum.DISCOUNT.getCode() == couponType) {// 折扣券
						// 面额（折扣百分比,为1-10之间的数字）
						BigDecimal couponAmount = couponsDTO.getCouponAmount();
						// 限额
						BigDecimal couponMax = couponsDTO.getCouponMax();
						// 优惠金额
						discountPrice = payTotal.multiply(BigDecimal.valueOf(1).subtract(couponAmount.divide(BigDecimal.valueOf(10)))).setScale(2, BigDecimal.ROUND_UP);
						if (discountPrice.compareTo(couponMax) > 0) {
							discountPrice = couponMax;
						}
					} else if (CouponTypeEnum.CASH.getCode() == couponType) {// 现金券
						// 优惠金额
						discountPrice = couponsDTO.getCouponAmount();
					}
					// 支付金额为负数
					if(payTotal.compareTo(discountPrice) < 0){
						result.addErrorMessage("优惠券优惠金额大于支付金额");
						shopCartDTO.getUnusualMsg().add("优惠券优惠金额大于支付金额");
						return result;
					}
					// 个人支付还是企业支付
					Integer couponUserType = couponsDTO.getCouponUserType();
					if(couponUserType != null && couponUserType != 0 && paymentMethod != null){
						// 优惠券只能个人使用
						if (couponUserType == 1 && paymentMethod == 2) {
							result.addErrorMessage("该优惠券只支持个人支付");
							shopCartDTO.getUnusualMsg().add("该优惠券只支持个人支付");
							return result;
						} else if(couponUserType == 2 && paymentMethod == 1){
							result.addErrorMessage("该优惠券只支持企业支付");
							shopCartDTO.getUnusualMsg().add("该优惠券只支持企业支付");
							return result;
						}
					}
				} else{
					result.addErrorMessage("优惠券不存在");
					shopCartDTO.getUnusualMsg().add("优惠券不存在");
				}
			} else{
				result.addErrorMessage("优惠券编号为空");
				shopCartDTO.getUnusualMsg().add("优惠券编号为空");
			}
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			LOG.info("\n 方法[{}]，异常：[{}]", "ShopCartCouponServiceImpl-validateCoupon", e);
		}
		result.setResult(shopCartDTO);
		LOG.info("\n 方法[{}]，出参：[{}]", "ShopCartCouponServiceImpl-validateCoupon", JSONObject.toJSONString(result));
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[获取满足优惠券的商品]</p>
	 * Created on 2015年12月22日
	 * @param couponsDTO
	 * @param shopCartDTO
	 * @return
	 * @author:[宋文斌]
	 */
	private List<ProductInPriceDTO> getCouponProducts(CouponsDTO couponsDTO, ShopCartDTO shopCartDTO){
		// 需要计算优惠的商品
		List<ProductInPriceDTO> couponProducts = new ArrayList<ProductInPriceDTO>();
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
		// 获取优惠券使用范围(1:平台通用类,2:店铺通用类,3:品类通用类,4:SKU使用类)
		Integer couponUsingRange = couponsDTO.getCouponUsingRange();
		// 平台通用
		if (CouponUsingRangeEnum.PLATFORM.getCode() == couponUsingRange) {
			couponProducts = productInPriceDTOs;
		} else {
			// 店铺通用并且是卖家创建的优惠券
			if (CouponUsingRangeEnum.SHOP.getCode() == couponUsingRange && couponsDTO.getCostAllocation() == 2) {
				// 获取能使用此优惠券的店铺ID
				Long shopId = couponsDTO.getShopId();
				for (ProductInPriceDTO dto : productInPriceDTOs) {
					if (dto.getShopId().longValue() == shopId.longValue()) {
						// 需要计算优惠的商品
						couponProducts.add(dto);
					}
				}
			} else {
				// 查询优惠券使用范围
				CouponUsingRangeDTO couponUsingRangeDTO = new CouponUsingRangeDTO();
				couponUsingRangeDTO.setCouponId(couponsDTO.getCouponId());
				ExecuteResult<DataGrid<CouponUsingRangeDTO>> executeResult = couponsExportService
						.queryCouponUsingRangeList(couponUsingRangeDTO, null);
				if (executeResult.isSuccess() && executeResult.getResult() != null
						&& executeResult.getResult().getRows() != null
						&& executeResult.getResult().getRows().size() > 0) {
					// 店铺通用并且是平台创建的优惠券
					if (CouponUsingRangeEnum.SHOP.getCode() == couponUsingRange && couponsDTO.getCostAllocation() == 1) {
						// 获取能使用此优惠券的所有shopId
						List<CouponUsingRangeDTO> couponUsingRangeDTOs = executeResult.getResult().getRows();
						for (CouponUsingRangeDTO usingRangeDTO : couponUsingRangeDTOs) {
							// 获取shopId
							Long shopId = usingRangeDTO.getCouponUsingId();
							for (ProductInPriceDTO dto : productInPriceDTOs) {
								if (dto.getShopId().longValue() == shopId.longValue()) {
									couponProducts.add(dto);
								}
							}
						}
					} else if (CouponUsingRangeEnum.CATEGORY.getCode() == couponUsingRange) {// 类目通用
						// 获取能使用此优惠券的所有类目
						List<CouponUsingRangeDTO> couponUsingRangeDTOs = executeResult.getResult().getRows();
						for (CouponUsingRangeDTO usingRangeDTO : couponUsingRangeDTOs) {
							// 获取类目ID
							Long cid = usingRangeDTO.getCouponUsingId();
							for (ProductInPriceDTO dto : productInPriceDTOs) {
								if (dto.getCid().longValue() == cid.longValue()) {
									couponProducts.add(dto);
								}
							}
						}
					} else if (CouponUsingRangeEnum.SKU.getCode() == couponUsingRange) {// SKU使用类
						// 获取能使用此优惠券的所有skuId
						List<CouponUsingRangeDTO> couponUsingRangeDTOs = executeResult.getResult().getRows();
						for (CouponUsingRangeDTO usingRangeDTO : couponUsingRangeDTOs) {
							// 获取SkuId
							Long skuId = usingRangeDTO.getCouponUsingId();
							for (ProductInPriceDTO dto : productInPriceDTOs) {
								if (dto.getSkuId().longValue() == skuId.longValue()) {
									couponProducts.add(dto);
								}
							}
						}
					}
				}
			}
		}
		return couponProducts;
	}
	
	/**
	 * 
	 * <p>Description: [通过优惠券类型(1:满减券 2:折扣券 3:现金券)计算优惠金额]</p>
	 * Created on 2015年12月9日
	 * @param couponsDTO
	 * @param payTotal 实际支付总金额（不包括运费）
	 * @return 优惠券总优惠金额
	 * @author:[宋文斌]
	 */
	private BigDecimal calcDiscountByCouponType(CouponsDTO couponsDTO, BigDecimal payTotal) {
		// 获取优惠券类型
		Integer couponType = couponsDTO.getCouponType();
		BigDecimal discountPrice = BigDecimal.ZERO;
		// 满减券
		if (CouponTypeEnum.FULL_REDUCTION.getCode() == couponType) {
			// 获取优惠券满足金额
			BigDecimal meetPrice = couponsDTO.getMeetPrice();
			// 满足优惠券使用条件
			if (payTotal.compareTo(meetPrice) >= 0) {
				// 优惠金额
				BigDecimal couponAmount = couponsDTO.getCouponAmount();
				// 总优惠金额
				discountPrice = discountPrice.add(couponAmount);
			}
		} else if (CouponTypeEnum.DISCOUNT.getCode() == couponType) {// 折扣券
			// 面额（折扣百分比,为1-10之间的数字）
			BigDecimal couponAmount = couponsDTO.getCouponAmount();
			// 限额
			BigDecimal couponMax = couponsDTO.getCouponMax();
			// 优惠金额
			BigDecimal amountPrice = payTotal.multiply(BigDecimal.valueOf(1).subtract(couponAmount.divide(BigDecimal.valueOf(10))));
			if (amountPrice.compareTo(couponMax) > 0) {
				amountPrice = couponMax;
			}
			// 总优惠金额
			discountPrice = discountPrice.add(amountPrice);
		} else if (CouponTypeEnum.CASH.getCode() == couponType) {// 现金券
			// 优惠金额
			BigDecimal couponAmount = couponsDTO.getCouponAmount();
			// 总优惠金额
			discountPrice = discountPrice.add(couponAmount);
		}
		// 优惠金额不能大于总支付金额
		if(discountPrice.compareTo(payTotal) > 0){
			discountPrice = payTotal;
		}
		return discountPrice.setScale(2, BigDecimal.ROUND_UP);
	}
	
	/**
	 * 
	 * <p>Description: [优惠券优惠金额分摊到商品]</p>
	 * Created on 2015年12月10日
	 * @param orderItems 参与优惠的所有商品
	 * @param payTotal 参与优惠的所有商品的金额
	 * @param discountPrice 参与优惠的商品的总优惠金额
	 * @author:[宋文斌]
	 */
	private void shareCouponDiscountToProducts(List<ProductInPriceDTO> couponProducts, BigDecimal payTotal, BigDecimal discountPrice) {
		// 前面的优惠金额之和
		BigDecimal addPrice = BigDecimal.ZERO;
		int i = 1;
		for (ProductInPriceDTO inPriceDTO : couponProducts) {
			// 当前商品金额
			BigDecimal price = BigDecimal.ZERO;
			BigDecimal averagePrice = BigDecimal.ZERO;
			// 优惠金额*（单个商品总金额/活动商品总金额）
			averagePrice = (discountPrice.multiply(inPriceDTO.getPayTotal().divide(payTotal, 10, RoundingMode.HALF_UP))).setScale(4, RoundingMode.DOWN).setScale(2, RoundingMode.UP);
			if (couponProducts.size() > 1 && i == couponProducts.size()) {// 最后一件优惠=总优惠-前面的优惠金额之和（为防止最后校验不通过）
				if (discountPrice.compareTo(addPrice) >= 0) {
					price = inPriceDTO.getPayTotal().subtract(discountPrice.subtract(addPrice));// 当前商品金额-（优惠金额-前面的金额之和）
					inPriceDTO.setCouponDiscount(discountPrice.subtract(addPrice));// 优惠券优惠金额
				} else {
					price = inPriceDTO.getPayTotal();
					inPriceDTO.setCouponDiscount(BigDecimal.ZERO);
				}
				inPriceDTO.setPayTotal(price);// 优惠后金额
			} else {
				if (discountPrice.subtract(addPrice).compareTo(averagePrice) < 0) {
					price = inPriceDTO.getPayTotal().subtract(discountPrice.subtract(addPrice));
					inPriceDTO.setCouponDiscount(discountPrice.subtract(addPrice));// 优惠券优惠金额
				} else {
					price = inPriceDTO.getPayTotal().subtract(averagePrice);
					inPriceDTO.setCouponDiscount(averagePrice);
				}
				inPriceDTO.setPayTotal(price);// 优惠后金额
				addPrice = addPrice.add(averagePrice);
				i += 1;// 默认为1，然后循环+1，判断是否是最后一件商品
			}
		}
	}
}
