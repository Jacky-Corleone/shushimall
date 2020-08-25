package com.camelot.sellercenter.mallRec.dto;

import java.io.Serializable;


/** 
 <p>Description: [描述该类概要功能介绍:mall_recommend 商城楼层推荐 DTO类]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class MallRecDTO implements Serializable{

	private static final long serialVersionUID = -463555445077786802L;
	private java.lang.Long idDTO;//  id
	private Integer floorNumDTO;//  楼层编号
	private java.lang.String titleDTO;//  title
	private Integer positionDTO;//  1:左，2：中，3：右
	private Integer recTypeDTO;//  1,网站首页 2：类目子站   3:地区子站
	private java.util.Date createdDTO;//  创建时间
	private java.util.Date modifiedDTO;//  修改时间
	private Integer statusDTO;//  显示状态  0表示不显示 1表示显示
	private java.lang.Long categoryIdDTO;//  类目id
	private java.lang.String smalltitleDTO;//  右侧短名称
	private Integer floorTypeDTO; //是否为热卖单品 0:否  1:是
	private java.lang.String colorrefDTO; //楼层色值
	private java.lang.String bgColorDTO; //楼层背景色
	private Integer themeId; //主题id
	private String engNameDTO; //英文名称
	
	public java.lang.Long getIdDTO() {
		return idDTO;
	}
	public void setIdDTO(java.lang.Long idDTO) {
		this.idDTO = idDTO;
	}
	public Integer getFloorNumDTO() {
		return floorNumDTO;
	}
	public void setFloorNumDTO(Integer floorNumDTO) {
		this.floorNumDTO = floorNumDTO;
	}
	public java.lang.String getTitleDTO() {
		return titleDTO;
	}
	public void setTitleDTO(java.lang.String titleDTO) {
		this.titleDTO = titleDTO;
	}
	public Integer getPositionDTO() {
		return positionDTO;
	}
	public void setPositionDTO(Integer positionDTO) {
		this.positionDTO = positionDTO;
	}
	public Integer getRecTypeDTO() {
		return recTypeDTO;
	}
	public void setRecTypeDTO(Integer recTypeDTO) {
		this.recTypeDTO = recTypeDTO;
	}
	public java.util.Date getCreatedDTO() {
		return createdDTO;
	}
	public void setCreatedDTO(java.util.Date createdDTO) {
		this.createdDTO = createdDTO;
	}
	public java.util.Date getModifiedDTO() {
		return modifiedDTO;
	}
	public void setModifiedDTO(java.util.Date modifiedDTO) {
		this.modifiedDTO = modifiedDTO;
	}
	public Integer getStatusDTO() {
		return statusDTO;
	}
	public void setStatusDTO(Integer statusDTO) {
		this.statusDTO = statusDTO;
	}
	public java.lang.Long getCategoryIdDTO() {
		return categoryIdDTO;
	}
	public void setCategoryIdDTO(java.lang.Long categoryIdDTO) {
		this.categoryIdDTO = categoryIdDTO;
	}
	public java.lang.String getSmalltitleDTO() {
		return smalltitleDTO;
	}
	public void setSmalltitleDTO(java.lang.String smalltitleDTO) {
		this.smalltitleDTO = smalltitleDTO;
	}
	public java.lang.String getColorrefDTO() {
		return colorrefDTO;
	}
	public void setColorrefDTO(java.lang.String colorrefDTO) {
		this.colorrefDTO = colorrefDTO;
	}
	public java.lang.String getBgColorDTO() {
		return bgColorDTO;
	}
	public void setBgColorDTO(java.lang.String bgColorDTO) {
		this.bgColorDTO = bgColorDTO;
	}
	public Integer getThemeId() {
		return themeId;
	}
	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}
	public Integer getFloorTypeDTO() {
		return floorTypeDTO;
	}
	public void setFloorTypeDTO(Integer floorTypeDTO) {
		this.floorTypeDTO = floorTypeDTO;
	}
	public String getEngNameDTO() {
		return engNameDTO;
	}
	public void setEngNameDTO(String engNameDTO) {
		this.engNameDTO = engNameDTO;
	}
}
