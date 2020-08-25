package com.camelot.goodscenter.dto;

import java.io.Serializable;

public class ContractUrlShowDTO implements Serializable{

	/**
	 * <p>Discription:[协议订单图片上传]</p>
	 * Created on 2015年12月22日10:33:56
	 * @author:訾瀚民
	 */
	private static final long serialVersionUID = -6241858618435829940L;
	private Long id;//主键
	private String contractInfoId;//协议号
	private String imgUrl;//图片地址
	private String isDelete; //删除标示位
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public String getContractInfoId() {
		return contractInfoId;
	}
	public void setContractInfoId(String contractInfoId) {
		this.contractInfoId = contractInfoId;
	}
	
	
	
	
	
	
	

	
	
	
}
