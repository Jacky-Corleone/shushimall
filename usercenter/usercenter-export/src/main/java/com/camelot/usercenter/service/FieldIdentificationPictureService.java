package com.camelot.usercenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPictureDTO;

/**
 * 实地认证图片服务类
 * @author Klaus
 */
public interface FieldIdentificationPictureService {

	public DataGrid<FieldIdentificationPictureDTO> findPictureListByCondition(FieldIdentificationPictureDTO pictureDTO, Pager<FieldIdentificationAuditDTO> pager);

	/**
	 * 新增图片
	 * @param pictureDTO
	 * @return 执行结果信息类
	 */
	public ExecuteResult<String> addPicture(FieldIdentificationPictureDTO pictureDTO);

	/**
	 * 修改图片
	 * @param pictureDTO
	 * @return 执行结果信息类
	 */
	public ExecuteResult<String> modifyPicture(FieldIdentificationPictureDTO pictureDTO);

}
