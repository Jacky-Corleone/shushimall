package com.camelot.pricecenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.DeliveryTypeFreightDTO;
import com.camelot.pricecenter.dto.DeliveryTypePreferentialWayDTO;
import com.camelot.pricecenter.dto.GroupProductDTO;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.service.ShopCartFreightService;
import com.camelot.storecenter.dto.ShopDeliveryTypeDTO;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.dto.ShopPreferentialWayDTO;
import com.camelot.storecenter.dto.emums.ShopFreightTemplateEnum;
import com.camelot.storecenter.dto.emums.ShopFreightTemplateEnum.ShopPreferentialWayEnum;
import com.camelot.storecenter.service.ShopFreightTemplateService;

/**
 * 
 * <p>Description: [运费计算业务实现]</p>
 * Created on 2015年11月24日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopCartFreightService")
public class ShopCartFreightServiceImpl implements ShopCartFreightService {
	
	private static final Logger LOG = LoggerFactory.getLogger(ShopCartFreightServiceImpl.class);
	
	@Resource
	private ShopFreightTemplateService shopFreightTemplateService;
	
	@Resource
	private AddressBaseService addressBaseService;

	@Override
	public ExecuteResult<List<DeliveryTypeFreightDTO>> calcEveryDeliveryTypeFreight(List<ProductInPriceDTO> productInPriceDTOs, boolean isPreferental) {
		ExecuteResult<List<DeliveryTypeFreightDTO>> result = new ExecuteResult<List<DeliveryTypeFreightDTO>>();
		List<DeliveryTypeFreightDTO> typeFreightDTOs = new ArrayList<DeliveryTypeFreightDTO>();
		try {
			if (productInPriceDTOs != null && productInPriceDTOs.size() > 0) {
				GroupProductDTO groupProductDTO = this.initGroupProduct(productInPriceDTOs);
				LOG.info("groupProductDTO：{}", JSON.toJSONString(groupProductDTO));
				typeFreightDTOs = this.calcGroupProductFreight(groupProductDTO, isPreferental);
				// 根据运费金额升序排序,价格相同的按运送方式升序排序
				Collections.sort(typeFreightDTOs, new Comparator<DeliveryTypeFreightDTO>() {

					@Override
					public int compare(DeliveryTypeFreightDTO o1, DeliveryTypeFreightDTO o2) {
						if (o1.getGroupFreight().compareTo(o2.getGroupFreight()) > 0) {
							return 1;
						} else if (o1.getGroupFreight().compareTo(o2.getGroupFreight()) < 0) {
							return -1;
						}
						if (o1.getDeliveryType() > o2.getDeliveryType()) {
							return 1;
						} else if (o1.getDeliveryType() < o2.getDeliveryType()) {
							return -1;
						}
						return 0;
					}

				});
			}
			result.setResult(typeFreightDTOs);
		} catch (Exception e) {
			result.addErrorMessage(e.toString());
			LOG.error("\n 方法[ShopCartPromotionServiceImpl-calcEveryDeliveryTypeFreight]异常：", e);
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Description: [将使用同一运费模版的商品的价格、数量、体积、重量相加形成新的GroupProduct]</p>
	 * Created on 2015年12月2日
	 * @param products
	 * @return
	 * @author:[宋文斌]
	 */
	private GroupProductDTO initGroupProduct(List<ProductInPriceDTO> products){
		GroupProductDTO groupProduct = new GroupProductDTO();
		groupProduct.setShopFreightTemplateId(products.get(0).getShopFreightTemplateId());
		groupProduct.setRegionId(products.get(0).getRegionId());
		for (int i = 0; i < products.size(); i++) {
			ProductInPriceDTO product = products.get(i);
			// 设置商品总支付价格
			groupProduct.setTotalPrice(groupProduct.getTotalPrice().add(product.getPayTotal()));
			// 设置商品总数量
			groupProduct.setTotalQty(groupProduct.getTotalQty() + product.getQuantity());
			// 设置商品总体积
			if (product.getVolume() != null) {
				groupProduct.setTotalVolume(groupProduct.getTotalVolume().add(
						BigDecimal.valueOf(product.getQuantity()).multiply(product.getVolume())));
			}
			// 设置商品总重量
			if (product.getWeight() != null) {
				String weightUnit = product.getWeightUnit();
				if (!StringUtils.isBlank(weightUnit)) {
					BigDecimal unit = BigDecimal.valueOf(1);
					if ("g".equals(weightUnit)) {
						unit = BigDecimal.valueOf(1000);
					} else if ("mg".equals(weightUnit)) {
						unit = BigDecimal.valueOf(1000).pow(2);
					} else if ("μg".equals(weightUnit)) {
						unit = BigDecimal.valueOf(1000).pow(3);
					}
					groupProduct.setTotalWeight(groupProduct.getTotalWeight().add(
							BigDecimal.valueOf(product.getQuantity()).multiply(product.getWeight().divide(unit))));
				}
			}
		}
		return groupProduct;
	}
	
	/**
	 * 
	 * <p>Description: [计算商品的每种运送方式的运费]</p>
	 * Created on 2015年11月9日
	 * @param groupProduct
	 * @param isPreferental 是否计算运费优惠  true：是  false：否
	 * @return
	 * @author:[宋文斌]
	 */
	private List<DeliveryTypeFreightDTO> calcGroupProductFreight(GroupProductDTO groupProduct, boolean isPreferental) {
		// 商品对应的每种运送方式的运费
		List<DeliveryTypeFreightDTO> deliveryTypeFreightDTOs = new ArrayList<DeliveryTypeFreightDTO>();
		// 查询一级区域
		if(StringUtils.isNotBlank(groupProduct.getRegionId())){
			ExecuteResult<List<AddressBaseDTO>> addressBaseResult = addressBaseService.queryNameByCode(groupProduct.getRegionId());
			if(addressBaseResult.isSuccess() && addressBaseResult.getResult() != null && addressBaseResult.getResult().size() > 0){
				AddressBaseDTO baseDTO = addressBaseResult.getResult().get(0);
				// 如果是二级地域，就设置商品为一级区域
				if(baseDTO.getLevel() == 2){
					groupProduct.setRegionId(baseDTO.getParentcode());
				}
			}
		}
		// 查询商品使用的运费模版
		ExecuteResult<ShopFreightTemplateDTO> shopFreightTemplateDTOResult = shopFreightTemplateService
				.queryByRegionIdAndTemplateId(Long.parseLong(groupProduct.getRegionId()),
						groupProduct.getShopFreightTemplateId());

		if (shopFreightTemplateDTOResult != null && shopFreightTemplateDTOResult.getResult() != null) {
			ShopFreightTemplateDTO shopFreightTemplateDTO = shopFreightTemplateDTOResult.getResult();
			if (shopFreightTemplateDTO.getPostageFree() == 1
					&& shopFreightTemplateDTO.getShopDeliveryTypeList() != null
					&& shopFreightTemplateDTO.getShopDeliveryTypeList().size() > 0) {// 买家承担运费
				// 查询运费策略
				List<ShopDeliveryTypeDTO> deliveryTypeDTOs = shopFreightTemplateDTO.getShopDeliveryTypeList();
				for (ShopDeliveryTypeDTO source : deliveryTypeDTOs) {
					DeliveryTypeFreightDTO target = new DeliveryTypeFreightDTO();
					BeanUtils.copyProperties(source, target);
					deliveryTypeFreightDTOs.add(target);
				}

				// 计价方式，1件数，2重量，3体积
				Integer valuationWay = shopFreightTemplateDTO.getValuationWay();
				// 计算每种运送方式的运费
				for (DeliveryTypeFreightDTO deliveryTypeFreight : deliveryTypeFreightDTOs) {
					// 运费
					BigDecimal freight = BigDecimal.ZERO;
					// 总件数/总重量/总体积
					BigDecimal total = BigDecimal.ZERO;
					// 件数
					if (valuationWay == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.NUMBERS.getCode()) {
						total = BigDecimal.valueOf(groupProduct.getTotalQty());
					} else if (valuationWay == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.WEIGHT.getCode()) { // 重量
						total = groupProduct.getTotalWeight();
					} else if (valuationWay == ShopFreightTemplateEnum.ShopDeliveryTypeEnum.VOLUME.getCode()) { // 体积
						total = groupProduct.getTotalVolume();
					}
					// =================基本运费计算开始=================
					// 首价格
					freight = freight.add(deliveryTypeFreight.getFirstPrice());
					if (total.compareTo(deliveryTypeFreight.getFirstPart()) > 0) {
						// 剩余
						total = total.subtract(deliveryTypeFreight.getFirstPart());
						if (deliveryTypeFreight.getContinues().compareTo(BigDecimal.ZERO) > 0) {
							BigDecimal[] count = total.divideAndRemainder(deliveryTypeFreight.getContinues());
							// 首价格+续价格
							freight = freight.add(count[0].multiply(deliveryTypeFreight.getContinuePrice()));
							if (count[1].compareTo(BigDecimal.ZERO) != 0) {
								freight = freight.add(deliveryTypeFreight.getContinuePrice());
							}
						}
					}
					// =================基本运费计算结束=================
					// =================运费优惠计算开始=================
					if(isPreferental){
						if (shopFreightTemplateDTO.getShopPreferentialWayList() != null
								&& shopFreightTemplateDTO.getShopPreferentialWayList().size() > 0) {
							ShopPreferentialWayDTO shopPreferentialWayDTO = shopFreightTemplateDTO
									.getShopPreferentialWayList().get(0);
							// 用于在订单核对页显示运费优惠信息
							DeliveryTypePreferentialWayDTO deliveryTypePreferentialWayDTO = new DeliveryTypePreferentialWayDTO();
							BeanUtils.copyProperties(shopPreferentialWayDTO, deliveryTypePreferentialWayDTO);
							// 优惠方式：1件数，2重量，3体积 4金额
							Integer delivery_type = shopPreferentialWayDTO.getDeliveryType();
							// 策略，1满减，2包邮
							Integer strategy = shopPreferentialWayDTO.getStrategy();
							// 总件数/总重量/总体积/总金额
							BigDecimal total_ = BigDecimal.ZERO;
							if (delivery_type == 1) {
								total_ = BigDecimal.valueOf(groupProduct.getTotalQty());
							} else if (delivery_type == 2) {
								total_ = groupProduct.getTotalWeight();
							} else if (delivery_type == 3) {
								total_ = groupProduct.getTotalVolume();
							} else if (delivery_type == 4) {
								total_ = groupProduct.getTotalPrice();
							}
							BigDecimal full = shopPreferentialWayDTO.getFull();
							if (full != null) {
								if (strategy == 1) {
									BigDecimal reduce = shopPreferentialWayDTO.getReduce();
									if (total_.compareTo(full) >= 0) {
										freight = freight.subtract(reduce);
										deliveryTypeFreight.setDeliveryTypePreferentialWayDTO(deliveryTypePreferentialWayDTO);
									}
								} else if (strategy == 2) {
									if (total_.compareTo(full) >= 0) {
										freight = BigDecimal.ZERO;
										deliveryTypeFreight.setDeliveryTypePreferentialWayDTO(deliveryTypePreferentialWayDTO);
									}
								}
							}
							// 防止运费出现负数
							if (freight.compareTo(BigDecimal.ZERO) < 0) {
								freight = BigDecimal.ZERO;
							}
						}
					}
					// =================运费优惠计算结束=================
					deliveryTypeFreight.setGroupFreight(freight.setScale(2, BigDecimal.ROUND_UP));
				}
			}
		}
		if (deliveryTypeFreightDTOs.size() == 0) {
			// 如果没有运送方式，默认为快递
			DeliveryTypeFreightDTO expressage = new DeliveryTypeFreightDTO();
			expressage.setContinuePrice(BigDecimal.ZERO);
			expressage.setContinues(BigDecimal.ZERO);
			expressage.setDeliveryType(ShopPreferentialWayEnum.EXPRESSAGE.getCode());
			expressage.setFirstPart(BigDecimal.ZERO);
			expressage.setFirstPrice(BigDecimal.ZERO);
			expressage.setGroupFreight(BigDecimal.ZERO);
			deliveryTypeFreightDTOs.add(expressage);
		}
		return deliveryTypeFreightDTOs;
	}
	
}
