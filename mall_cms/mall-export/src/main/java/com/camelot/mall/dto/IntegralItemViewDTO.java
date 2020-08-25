package com.camelot.mall.dto;

import java.io.Serializable;

import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;

/**
 * 用于积分商城首页积分商品的信息展示
 * @author 王东晓
 *
 */
public class IntegralItemViewDTO implements Serializable {
	private static final long serialVersionUID = 650892521148846251L;
	
	private MallAdDTO mallAdDTO;//广告信息
	private ItemDTO itemDTO;//商品信息
	public MallAdDTO getMallAdDTO() {
		return mallAdDTO;
	}
	public void setMallAdDTO(MallAdDTO mallAdDTO) {
		this.mallAdDTO = mallAdDTO;
	}
	public ItemDTO getItemDTO() {
		return itemDTO;
	}
	public void setItemDTO(ItemDTO itemDTO) {
		this.itemDTO = itemDTO;
	}
}
