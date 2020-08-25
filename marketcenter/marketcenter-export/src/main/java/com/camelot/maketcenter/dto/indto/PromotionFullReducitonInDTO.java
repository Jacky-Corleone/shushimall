package com.camelot.maketcenter.dto.indto;

import java.io.Serializable;
import java.util.List;

import com.camelot.maketcenter.dto.PromotionFrDTO;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionInfoDTO;

/**
 * 
 * <p>Description: [促销满减整合类]</p>
 * Created on 2015-3-18
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class PromotionFullReducitonInDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private PromotionInfoDTO promotionInfoDTO;//促销类
	
	private  List<PromotionFrDTO> promotionFrDTOList;//促销满减List

	public PromotionInfoDTO getPromotionInfoDTO() {
		return promotionInfoDTO;
	}

	public void setPromotionInfoDTO(PromotionInfoDTO promotionInfoDTO) {
		this.promotionInfoDTO = promotionInfoDTO;
	}

	public List<PromotionFrDTO> getPromotionFrDTOList() {
		return promotionFrDTOList;
	}

	public void setPromotionFrDTOList(List<PromotionFrDTO> promotionFrDTOList) {
		this.promotionFrDTOList = promotionFrDTOList;
	}
	
}
