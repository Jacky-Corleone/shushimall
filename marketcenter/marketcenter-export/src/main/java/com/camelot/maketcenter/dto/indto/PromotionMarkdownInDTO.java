package com.camelot.maketcenter.dto.indto;

import java.io.Serializable;
import java.util.List;

import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionInfoDTO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionMdDTO;

/**
 * 
 * <p>Description: [直降整合类]</p>
 * Created on 2015-3-18
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class PromotionMarkdownInDTO implements Serializable{


	private static final long serialVersionUID = 1L;
	
	
	private PromotionInfoDTO promotionInfoDTO;//促销类
	
	private  List< PromotionMdDTO> promotionMdDTOList;//促销直降List

	public PromotionInfoDTO getPromotionInfoDTO() {
		return promotionInfoDTO;
	}

	public void setPromotionInfoDTO(PromotionInfoDTO promotionInfoDTO) {
		this.promotionInfoDTO = promotionInfoDTO;
	}

	public List<PromotionMdDTO> getPromotionMdDTOList() {
		return promotionMdDTOList;
	}

	public void setPromotionMdDTOList(List<PromotionMdDTO> promotionMdDTOList) {
		this.promotionMdDTOList = promotionMdDTOList;
	}
}
