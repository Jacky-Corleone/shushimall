package com.camelot.basecenter.domain;

import java.util.Date;

import com.camelot.openplatform.common.PropertyMapping;

public class AddressInfo {
    @PropertyMapping("id")
	private long id;//主键
    @PropertyMapping("platformid")  
    private long platformid;     //平台编号
    @PropertyMapping("buyerid")
	private long buyerid;//用户ID
    @PropertyMapping("provicecode")
	private String provicecode;//省编号
    @PropertyMapping("citycode")
	private String citycode;//市编号
    @PropertyMapping("countrycode")
	private String countrycode;//县、县级市、区编号
    @PropertyMapping("towncode")
	private String towncode;//镇、街道编号
    @PropertyMapping("villagecode")
	private String villagecode;//村、社区
    @PropertyMapping("attachaddress")
	private String attachaddress;//地址备注
    @PropertyMapping("fulladdress")
	private String fulladdress;//详细地址
    @PropertyMapping("isdefault")
	private int isdefault;//1:默认2:不是
    @PropertyMapping("yn")
    private int yn;   //是否默认1:默认2:不是
    @PropertyMapping("contactperson")
	private String contactperson;//联系人、收货人
    @PropertyMapping("contactphone")
	private String contactphone;//联系人手机
    @PropertyMapping("contacttel")
	private String contacttel;//联系人座机
    @PropertyMapping("contactemail")
	private String contactemail;//联系人邮箱
    @PropertyMapping("postalcode")
	private String postalcode;//邮政编码
    @PropertyMapping("emergencyperson")
	private String emergencyperson;//紧急联系人
    @PropertyMapping("emergencyphone")
	private String emergencyphone;//紧急联系人电话
    @PropertyMapping("createtime")
	private Date createtime;//开始时间：样式-“yyyyMMdd”
    @PropertyMapping("updatetime")
	private Date updatetime;//结束时间：样式-“yyyyMMdd”
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getPlatformid() {
		return platformid;
	}
	public void setPlatformid(long platformid) {
		this.platformid = platformid;
	}
	public long getBuyerid() {
		return buyerid;
	}
	public void setBuyerid(long buyerid) {
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
	public int getIsdefault() {
		return isdefault;
	}
	public void setIsdefault(int isdefault) {
		this.isdefault = isdefault;
	}
	public int getYn() {
		return yn;
	}
	public void setYn(int yn) {
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
	
// setter and getter
	

}
