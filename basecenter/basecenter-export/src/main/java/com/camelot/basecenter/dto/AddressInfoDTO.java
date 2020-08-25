package com.camelot.basecenter.dto;

import java.io.Serializable;
import java.util.Date;

/** 
 * <p>Description: [收货地址]</p>
 * Created on 2015年1月26日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public class AddressInfoDTO  implements Serializable{
	
		private static final long serialVersionUID = 8767423877108918552L;
		private Long id;//主键
		 private Long platformid;     //平台编号
		private Long buyerid;//用户ID
		private String provicecode;//省编号
		private String citycode;//市编号
		private String countrycode;//县、县级市、区编号
		private String towncode;//镇、街道编号
		private String villagecode;//村、社区
		private String attachaddress;//地址备注
		private String fulladdress;//详细地址
		private Integer isdefault;//1:默认2:不是
		private Integer yn;   //是否默认1:默认2:不是
		private String contactperson;//联系人、收货人
		private String contactphone;//联系人手机
		private String contacttel;//联系人座机
		private String contactemail;//联系人邮箱
		private String postalcode;//邮政编码
		private String emergencyperson;//紧急联系人
		private String emergencyphone;//紧急联系人电话
		private Date createtime;//开始时间：样式-“yyyyMMdd”
		private Date updatetime;//结束时间：样式-“yyyyMMdd”
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Long getPlatformid() {
			return platformid;
		}
		public void setPlatformid(Long platformid) {
			this.platformid = platformid;
		}
		public Long getBuyerid() {
			return buyerid;
		}
		public void setBuyerid(Long buyerid) {
			this.buyerid = buyerid;
		}
		public String getProvicecode() {
			return provicecode;
		}
		public void setProvicecode(String provicecode) {
			this.provicecode = provicecode;
		}
		public String getCitycode() {
			return citycode;
		}
		public void setCitycode(String citycode) {
			this.citycode = citycode;
		}
		public String getCountrycode() {
			return countrycode;
		}
		public void setCountrycode(String countrycode) {
			this.countrycode = countrycode;
		}
		public String getTowncode() {
			return towncode;
		}
		public void setTowncode(String towncode) {
			this.towncode = towncode;
		}
		public String getVillagecode() {
			return villagecode;
		}
		public void setVillagecode(String villagecode) {
			this.villagecode = villagecode;
		}
		public String getAttachaddress() {
			return attachaddress;
		}
		public void setAttachaddress(String attachaddress) {
			this.attachaddress = attachaddress;
		}
		public String getFulladdress() {
			return fulladdress;
		}
		public void setFulladdress(String fulladdress) {
			this.fulladdress = fulladdress;
		}
		public Integer getIsdefault() {
			return isdefault;
		}
		public void setIsdefault(Integer isdefault) {
			this.isdefault = isdefault;
		}
		public Integer getYn() {
			return yn;
		}
		public void setYn(Integer yn) {
			this.yn = yn;
		}
		public String getContactperson() {
			return contactperson;
		}
		public void setContactperson(String contactperson) {
			this.contactperson = contactperson;
		}
		public String getContactphone() {
			return contactphone;
		}
		public void setContactphone(String contactphone) {
			this.contactphone = contactphone;
		}
		public String getContacttel() {
			return contacttel;
		}
		public void setContacttel(String contacttel) {
			this.contacttel = contacttel;
		}
		public String getContactemail() {
			return contactemail;
		}
		public void setContactemail(String contactemail) {
			this.contactemail = contactemail;
		}
		public String getPostalcode() {
			return postalcode;
		}
		public void setPostalcode(String postalcode) {
			this.postalcode = postalcode;
		}
		public String getEmergencyperson() {
			return emergencyperson;
		}
		public void setEmergencyperson(String emergencyperson) {
			this.emergencyperson = emergencyperson;
		}
		public String getEmergencyphone() {
			return emergencyphone;
		}
		public void setEmergencyphone(String emergencyphone) {
			this.emergencyphone = emergencyphone;
		}
		public Date getCreatetime() {
			return createtime;
		}
		public void setCreatetime(Date createtime) {
			this.createtime = createtime;
		}
		public Date getUpdatetime() {
			return updatetime;
		}
		public void setUpdatetime(Date updatetime) {
			this.updatetime = updatetime;
		}
		


}
