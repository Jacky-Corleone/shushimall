package com.camelot.sellercenter.malltheme.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
* @ClassName: MallThemeDTO 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 刘振江
* @date 2015年10月29日 下午5:49:02 
*
 */
public class MallThemeDTO implements Serializable {

	private static final long serialVersionUID = 4966571974266544190L;
	
	//mall_theme
	private Integer id;
	private String themeName;	//主题页 名称
	private Long primaryCid ;//一级类目
	private Long cId;	//二级类目
	private Integer clev;  //类目级别
	private Long addressId;//            bigint(20) comment '地区ID',
	private Long provinceCode;//		bigint(20) comment '省份CODE',
	private Long cityCode;//			bigint(20) comment '市份CODE',
	private Long villageCode;//			bigint(20) comment '县份CODE',
	private Integer type;	//                 bigint(3) comment '1：首页  2：类目子站 3：地区子站',
	private String color;   //             varchar(20) comment '颜色',
	private Integer status;	//               smallint(6) comment '状态：1、可用 2、不可用 3、已删除',
	private int[] statusGroup;
	private String userId;	//               varchar(100) comment '用户ID',
	private Integer sortNum;	//             smallint(6) comment '排序号',
	private Date created;	//              datetime comment '创建时间',
	private Date modified;	//             datetime comment '修改时间',
	private String cName;	//类目名称	
	private String addName; //区域名称
	private String colorb; //			颜色2
	private String platformId1;         //回显类目1
	private String platformId2;			//回显类目2
	
	private String themeNameNoLike;//名称 不作为模糊查询
	
	public String getColorb() {
		return colorb;
	}
	public void setColorb(String colorb) {
		this.colorb = colorb;
	}
	public String getPlatformId1() {
		return platformId1;
	}
	public String getcName() {
		return cName;
	}
	public void setcName(String cName) {
		this.cName = cName;
	}
	public String getAddName() {
		return addName;
	}
	public void setAddName(String addName) {
		this.addName = addName;
	}
	public void setPlatformId1(String platformId1) {
		this.platformId1 = platformId1;
	}
	public String getPlatformId2() {
		return platformId2;
	}
	public void setPlatformId2(String platformId2) {
		this.platformId2 = platformId2;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
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
	public Integer getClev() {
		return clev;
	}
	public void setClev(Integer clev) {
		this.clev = clev;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getSortNum() {
		return sortNum;
	}
	public void setSortNum(Integer sortNum) {
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
	public String getThemeNameNoLike() {
		return themeNameNoLike;
	}
	public void setThemeNameNoLike(String themeNameNoLike) {
		this.themeNameNoLike = themeNameNoLike;
	}
	public int[] getStatusGroup() {
		return statusGroup;
	}
	public void setStatusGroup(int[] statusGroup) {
		this.statusGroup = statusGroup;
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
