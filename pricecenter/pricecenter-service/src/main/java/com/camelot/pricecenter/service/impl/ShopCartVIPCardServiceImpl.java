package com.camelot.pricecenter.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.maketcenter.dto.VipCardDTO;
import com.camelot.maketcenter.service.VipCardService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.pricecenter.dto.indto.ProductInPriceDTO;
import com.camelot.pricecenter.dto.outdto.ProductOutPriceDTO;
import com.camelot.pricecenter.service.ShopCartVIPCardService;

/**
 * 
 * <p>Description: [计算VIP卡业务实现]</p>
 * Created on 2015年11月24日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("shopCartVIPCardService")
public class ShopCartVIPCardServiceImpl implements ShopCartVIPCardService {
	
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private VipCardService vipCardService;
	
	@Resource
    private ItemCategoryService itemCategoryService;

	@Override
	public ExecuteResult<List<ProductOutPriceDTO>> calculateVIPCard(List<ProductInPriceDTO> productInPriceDTOs, String promoCode) {
		ExecuteResult<List<ProductOutPriceDTO>> result = new ExecuteResult<List<ProductOutPriceDTO>>();
		List<ProductOutPriceDTO> productOutPriceDTOs = new ArrayList<ProductOutPriceDTO>();
		try {
			if (productInPriceDTOs != null && productInPriceDTOs.size() > 0) {
				if (StringUtils.isNotBlank(promoCode)) {
					BigDecimal sum = BigDecimal.ZERO;
					for (ProductInPriceDTO p : productInPriceDTOs) {
						// 集采活动商品不参与任何活动
						if(p.getActivitesDetailsId() == null){
							// 计算vip卡优惠活动
							this.calculateVIPCard(p, promoCode, sum);
							if (p.getDiscountTotal() != null) {
								sum = sum.add(p.getDiscountTotal());
							}
						}
						ProductOutPriceDTO productOutPriceDTO = new ProductOutPriceDTO();
						BeanUtils.copyProperties(p, productOutPriceDTO);
						productOutPriceDTOs.add(productOutPriceDTO);
					}
				} else{
					for (ProductInPriceDTO p : productInPriceDTOs) {
						ProductOutPriceDTO productOutPriceDTO = new ProductOutPriceDTO();
						BeanUtils.copyProperties(p, productOutPriceDTO);
						productOutPriceDTOs.add(productOutPriceDTO);
					}
				}
			}
			result.setResult(productOutPriceDTOs);
		} catch (Exception e) {
			result.addErrorMessage(e.getMessage());
			LOG.error("\n 方法[ShopCartPromotionServiceImpl-calculateVIPCard]异常：", e);
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Discription:[计算vip卡优惠]</p>
	 * Created on 2015-11-20
	 * @param product
	 * @param promoCode
	 * @author:[王鹏]
	 */
	private void calculateVIPCard(ProductInPriceDTO p, String promoCode, BigDecimal sum) {
		if (p.getChecked()) {
			BigDecimal discount = BigDecimal.ZERO;
			VipCardDTO vipCardDTO = new VipCardDTO();
			vipCardDTO.setVip_id(Integer.parseInt(promoCode));
			// 根据vip卡号查询vip卡信息
			ExecuteResult<VipCardDTO> res = vipCardService.queryVipCard(vipCardDTO);
			Long cid = p.getCid();
			ExecuteResult<List<ItemCatCascadeDTO>> result = itemCategoryService.queryParentCategoryList(cid);
			List<ItemCatCascadeDTO> itemCatCascadeDTOList = result.getResult();
			if (itemCatCascadeDTOList != null && itemCatCascadeDTOList.size() > 0) {
				ItemCatCascadeDTO itemCatCascadeDTO = itemCatCascadeDTOList.get(0);
				String discountType = res.getResult().getIgnore_type();// 忽略商品类型
				if (StringUtils.isNotEmpty(discountType)) {
					String tyeps[] = discountType.split(",");
					List<String> list = Arrays.asList(tyeps);
					if (!list.contains(itemCatCascadeDTO.getCid().toString())) {
						BigDecimal residualamount = res.getResult().getResidual_amount().subtract(sum);// 余额
						discount = p.getPayTotal().multiply(new BigDecimal(res.getResult().getDiscount_percent()))
								.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_UP);// 折扣金额
						p.setOriginalDiscount(discount);
						if (residualamount.compareTo(discount) < 0) {
							discount = residualamount;
						}
					} else {
						p.setIgnoreName(p.getTitle());
					}
				} else {
					BigDecimal residualamount = res.getResult().getResidual_amount().subtract(sum);// 余额
					discount = p.getPayTotal().multiply(new BigDecimal(res.getResult().getDiscount_percent()))
							.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_UP);// 折扣金额
					p.setOriginalDiscount(discount);
					if (residualamount.compareTo(discount) < 0) {
						discount = residualamount;
					}
				}
			}
			p.setPayTotal(p.getPayTotal().subtract(discount));
			p.setDiscountTotal(discount);
		}
	}

}
