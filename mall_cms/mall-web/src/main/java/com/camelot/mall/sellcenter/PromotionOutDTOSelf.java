package com.camelot.mall.sellcenter;

import java.io.Serializable;

import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInfo;
import com.camelot.maketcenter.dto.PromotionMarkdown;

public class PromotionOutDTOSelf implements Serializable{
	public PromotionFullReductionSelf getPromotionFullReduction() {
		return promotionFullReduction;
	}

	public void setPromotionFullReduction(
			PromotionFullReductionSelf promotionFullReduction) {
		this.promotionFullReduction = promotionFullReduction;
	}

	public PromotionInfoSelf getPromotionInfo() {
		return promotionInfo;
	}

	public void setPromotionInfo(PromotionInfoSelf promotionInfo) {
		this.promotionInfo = promotionInfo;
	}

	public PromotionMarkdownSelf getPromotionMarkdown() {
		return promotionMarkdown;
	}

	public void setPromotionMarkdown(PromotionMarkdownSelf promotionMarkdown) {
		this.promotionMarkdown = promotionMarkdown;
	}

	/**
	 * <p>Discription:[活动查询出参]</p>
	 */
	private static final long serialVersionUID = 5496467105856308491L;

	private  PromotionFullReductionSelf promotionFullReduction;
	
	private  PromotionInfoSelf promotionInfo;
	
	private  PromotionMarkdownSelf promotionMarkdown;

}
