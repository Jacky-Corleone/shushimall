package com.camelot.sellercenter.domain;

import java.io.Serializable;
import com.camelot.openplatform.common.PropertyMapping;

/**
 * 
 * <p>Description: [描述该类概要功能介绍:mall_recommend 商城楼层推荐 domain类]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">chenx</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public class MallRec implements Serializable {
	private static final long serialVersionUID = -463555445077786802L;
	@PropertyMapping("idDTO")
	private java.lang.Long id;//  id
	@PropertyMapping("floorNumDTO")
	private Integer floorNum;//  楼层编号
	@PropertyMapping("titleDTO")
	private java.lang.String title;//  title
	@PropertyMapping("positionDTO")
	private Integer position;//  1:左，2：中，3：右
	@PropertyMapping("recTypeDTO")
	private Integer recType;//  1,网站首页 2：商品单页 3：类目
	@PropertyMapping("createdDTO")
	private java.util.Date created;//  创建时间
	@PropertyMapping("modifiedDTO")
	private java.util.Date modified;//  修改时间
	@PropertyMapping("statusDTO")
	private Integer status;//  显示状态  2表示不显示 1表示显示
	@PropertyMapping("categoryIdDTO")
	private java.lang.Long categoryId;//  类目id
	@PropertyMapping("smalltitleDTO")
	private java.lang.String smalltitle;//  右侧短名称
	@PropertyMapping("floorTypeDTO")
	private Integer floorType;//  是否为热卖单品 1:是 0:否
	@PropertyMapping("colorrefDTO")
	private java.lang.String colorref;//  色值，楼层颜色
	@PropertyMapping("bgColorDTO")
	private java.lang.String bgColor;//  色值，楼层背景色
	@PropertyMapping("themeId")
	private Integer themeId; //主题id
	@PropertyMapping("engNameDTO")
	private java.lang.String engName;
	
	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public Integer getFloorNum() {
		return this.floorNum;
	}

	public void setFloorNum(Integer value) {
		this.floorNum = value;
	}

	public java.lang.String getTitle() {
		return this.title;
	}

	public void setTitle(java.lang.String value) {
		this.title = value;
	}

	public Integer getPosition() {
		return this.position;
	}

	public void setPosition(Integer value) {
		this.position = value;
	}

	public Integer getRecType() {
		return this.recType;
	}

	public void setRecType(Integer value) {
		this.recType = value;
	}

	public java.util.Date getCreated() {
		return this.created;
	}

	public void setCreated(java.util.Date value) {
		this.created = value;
	}

	public java.util.Date getModified() {
		return this.modified;
	}

	public void setModified(java.util.Date value) {
		this.modified = value;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public java.lang.Long getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(java.lang.Long value) {
		this.categoryId = value;
	}

	public java.lang.String getSmalltitle() {
		return this.smalltitle;
	}

	public void setSmalltitle(java.lang.String value) {
		this.smalltitle = value;
	}

	public java.lang.String getColorref() {
		return colorref;
	}

	public void setColorref(java.lang.String colorref) {
		this.colorref = colorref;
	}

	public java.lang.String getBgColor() {
		return bgColor;
	}

	public void setBgColor(java.lang.String bgColor) {
		this.bgColor = bgColor;
	}

	public Integer getThemeId() {
		return themeId;
	}

	public void setThemeId(Integer themeId) {
		this.themeId = themeId;
	}

	public Integer getFloorType() {
		return floorType;
	}

	public void setFloorType(Integer floorType) {
		this.floorType = floorType;
	}

	public java.lang.String getEngName() {
		return engName;
	}

	public void setEngName(java.lang.String engName) {
		this.engName = engName;
	}
	
}
