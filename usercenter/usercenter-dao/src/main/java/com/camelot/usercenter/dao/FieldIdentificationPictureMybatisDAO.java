package com.camelot.usercenter.dao;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPictureDTO;

/**
 * 实地认证图片DAO
 * @author Klaus
 */
public interface FieldIdentificationPictureMybatisDAO extends BaseDAO<FieldIdentificationPictureDTO> {

	/**
	 * 新增图片
	 * @param pictureDTO
	 * @return 受影响记录数
	 */
	public int addPicture(FieldIdentificationPictureDTO pictureDTO);

	/**
	 * 修改图片
	 * @param pictureDTO
	 * @return 受影响记录数
	 */
	public int modifyPicture(FieldIdentificationPictureDTO pictureDTO);

}
