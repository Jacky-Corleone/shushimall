package com.camelot.sellercenter.domain;

import java.util.Date;

import javax.management.loading.PrivateClassLoader;

/**
 * 
* @ClassName: MallTheme 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 刘振江
* @date 2015年10月30日 上午11:31:48 
*
 */
public class MallTheme {
	
		private int id;
		private String themeName;	//主题页 名称
		private Long primaryCid;//一级类目
		private Long cId;	//二级类目
		private int clev;  //类目级别
		private Long addressId;//            bigint(20) comment '地区ID',
		private Long provinceCode;//		bigint(20) comment '省份CODE',
		private Long cityCode;//			bigint(20) comment '市份CODE',
		private Long villageCode;//			bigint(20) comment '县份CODE',
		private int type;	//                 bigint(3) comment '1：首页  2：类目子站 3：地区子站',
		private String color;   //             varchar(20) comment '颜色',
		private int status;	//               smallint(6) comment '状态：1、可用 2、不可用 3、已删除',
		private String userId;	//               bigint(20) comment '用户ID',
		private int sortNum;	//             smallint(6) comment '排序号',
		private Date created;	//              datetime comment '创建时间',
		private Date modified;	//             datetime comment '修改时间'
		private String colorb; //				varchar(20) comment '颜色2',
		
		
		public String getColorb() {
			return colorb;
		}
		public void setColorb(String colorb) {
			this.colorb = colorb;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getThemeName() {
			return themeName;
		}
		public void setThemeName(String themeName) {
			this.themeName = themeName;
		}
		public Long getcId() {
			return cId;
		}
		public void setcId(Long cId) {
			this.cId = cId;
		}
		public int getClev() {
			return clev;
		}
		public void setClev(int clev) {
			this.clev = clev;
		}
		public Long getAddressId() {
			return addressId;
		}
		public void setAddressId(Long addressId) {
			this.addressId = addressId;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public int getSortNum() {
			return sortNum;
		}
		public void setSortNum(int sortNum) {
			this.sortNum = sortNum;
		}
		public Date getCreated() {
			return created;
		}
		public void setCreated(Date created) {
			this.created = created;
		}
		public Date getModified() {
			return modified;
		}
		public void setModified(Date modified) {
			this.modified = modified;
		}
		public Long getPrimaryCid() {
			return primaryCid;
		}
		public void setPrimaryCid(Long primaryCid) {
			this.primaryCid = primaryCid;
		}
		public Long getProvinceCode() {
			return provinceCode;
		}
		public void setProvinceCode(Long provinceCode) {
			this.provinceCode = provinceCode;
		}
		public Long getCityCode() {
			return cityCode;
		}
		public void setCityCode(Long cityCode) {
			this.cityCode = cityCode;
		}
		public Long getVillageCode() {
			return villageCode;
		}
		public void setVillageCode(Long villageCode) {
			this.villageCode = villageCode;
		}
}
