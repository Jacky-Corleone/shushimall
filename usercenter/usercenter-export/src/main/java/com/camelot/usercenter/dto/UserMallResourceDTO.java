package com.camelot.usercenter.dto;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * @author
 * 
 */
public class UserMallResourceDTO  implements Serializable {
    
	private static final long serialVersionUID =1l;
		private java.lang.Long id;//  id
		private java.lang.Integer type;//  用户类型：1-买家/2-卖家/3-平台
		private java.lang.String url;//  资源对应的url
		private java.lang.String resourceName;//  资源名称
		private java.lang.Long parentId;//  父菜单为-1
		private java.lang.Integer modularType;//模块类型 ：1买家中心 2卖家中心  
		private Integer filterMenu;//当filterMenu=2时，表示绿印平台要去的菜单。filterMenu为空时，则全部保留
		private List<UserMallResourceDTO> userMallResourceList; //二级资源List
	
	
		
	
	public java.lang.Integer getModularType() {
			return modularType;
		}

		public void setModularType(java.lang.Integer modularType) {
			this.modularType = modularType;
		}

	public List<UserMallResourceDTO> getUserMallResourceList() {
			return userMallResourceList;
		}

		public void setUserMallResourceList(List<UserMallResourceDTO> userMallResourceList) {
			this.userMallResourceList = userMallResourceList;
		}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long value) {
		this.id = value;
	}
	
	public java.lang.Integer getType() {
		return this.type;
	}

	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	
	public java.lang.String getUrl() {
		return this.url;
	}

	public void setUrl(java.lang.String value) {
		this.url = value;
	}
	
	public java.lang.String getResourceName() {
		return this.resourceName;
	}

	public void setResourceName(java.lang.String value) {
		this.resourceName = value;
	}
	
	public java.lang.Long getParentId() {
		return this.parentId;
	}

	public void setParentId(java.lang.Long value) {
		this.parentId = value;
	}

	public Integer getFilterMenu() {
		return filterMenu;
	}

	public void setFilterMenu(Integer filterMenu) {
		this.filterMenu = filterMenu;
	}


}

