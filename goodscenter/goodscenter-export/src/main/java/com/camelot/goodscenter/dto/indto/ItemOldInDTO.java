package com.camelot.goodscenter.dto.indto;

import java.io.Serializable;
import java.util.List;

import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.ItemPictureDTO;
/**
 * 
 * <p>Description: [添加修改二手商品]</p>
 * Created on 2015-5-4
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class ItemOldInDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	private ItemOldDTO itemOldDTO;// 二手商品信息
	private List<ItemPictureDTO> itemPictureDTO; //二手商品图片  
	
	
	
	public ItemOldDTO getItemOldDTO() {
		return itemOldDTO;
	}
	public void setItemOldDTO(ItemOldDTO itemOldDTO) {
		this.itemOldDTO = itemOldDTO;
	}
	public List<ItemPictureDTO> getItemPictureDTO() {
		return itemPictureDTO;
	}
	public void setItemPictureDTO(List<ItemPictureDTO> itemPictureDTO) {
		this.itemPictureDTO = itemPictureDTO;
	}
	
	
	
}
