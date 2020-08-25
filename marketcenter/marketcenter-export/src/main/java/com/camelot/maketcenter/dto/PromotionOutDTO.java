package com.camelot.maketcenter.dto;

import java.io.Serializable;

public class PromotionOutDTO implements Serializable{

	/**
	 * <p>Discription:[活动查询出参]</p>
	 */
	private static final long serialVersionUID = 5496467105856308491L;

	private  PromotionFullReduction promotionFullReduction;
	
	private  PromotionInfo promotionInfo;
	
	private  PromotionMarkdown promotionMarkdown;

	public PromotionFullReduction getPromotionFullReduction() {
		return promotionFullReduction;
	}

	public void setPromotionFullReduction(PromotionFullReduction promotionFullReduction) {
		this.promotionFullReduction = promotionFullReduction;
	}

	public PromotionInfo getPromotionInfo() {
		return promotionInfo;
	}

	public void setPromotionInfo(PromotionInfo promotionInfo) {
		this.promotionInfo = promotionInfo;
	}

	public PromotionMarkdown getPromotionMarkdown() {
		return promotionMarkdown;
	}

	public void setPromotionMarkdown(PromotionMarkdown promotionMarkdown) {
		this.promotionMarkdown = promotionMarkdown;
	}

	
	
	
	
}
