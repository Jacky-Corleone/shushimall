package com.camelot.basecenter.dto;

import java.io.Serializable;

/**
 * @author 周乐
 */
public class MallTypeDTO implements Serializable{
	private static final long serialVersionUID = 2990627565835351070L;
	
	private Integer mallClassifyId;//文档分类Id
	private String mallClassifyTitle;//文档分类标题
	private Integer mallType;//文档分类类型
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * @return Integer mallClassifyId.
	 */
	public Integer getMallClassifyId() {
		return mallClassifyId;
	}
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param mallClassifyId The mallClassifyId to set.
	 */
	public void setMallClassifyId(Integer mallClassifyId) {
		this.mallClassifyId = mallClassifyId;
	}
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * @return String mallClassifyTitle.
	 */
	public String getMallClassifyTitle() {
		return mallClassifyTitle;
	}
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param mallClassifyTitle The mallClassifyTitle to set.
	 */
	public void setMallClassifyTitle(String mallClassifyTitle) {
		this.mallClassifyTitle = mallClassifyTitle;
	}
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * @return Integer mallType.
	 */
	public Integer getMallType() {
		return mallType;
	}
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * @param mallType The mallType to set.
	 */
	public void setMallType(Integer mallType) {
		this.mallType = mallType;
	}
}
