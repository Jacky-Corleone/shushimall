package com.camelot.usercenter.dto.fieldIdentification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 卖家实地认证图片组装DTO
 * 董其超
 * 2015年7月17日
 */
public class FieldIdentificationPackagePictureDTO implements Serializable {

    private static final long serialVersionUID = -5689891134465270373L;

  	private List<FieldIdentificationPictureDTO> pictureDTOList = new ArrayList<FieldIdentificationPictureDTO>();

	public List<FieldIdentificationPictureDTO> getPictureDTOList() {
		return pictureDTOList;
	}

	public void setPictureDTOList(List<FieldIdentificationPictureDTO> pictureDTOList) {
		this.pictureDTOList = pictureDTOList;
	}

}
