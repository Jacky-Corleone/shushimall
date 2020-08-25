package com.camelot.usercenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dao.FieldIdentificationPictureMybatisDAO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationAuditDTO;
import com.camelot.usercenter.dto.fieldIdentification.FieldIdentificationPictureDTO;
import com.camelot.usercenter.service.FieldIdentificationPictureService;

/**
 * 实地认证图片实现类
 * @author Klaus
 */
@Service("fieldIdentificationPictureService")
public class FieldIdentificationPictureServiceImpl implements FieldIdentificationPictureService{

    private final static Logger logger = LoggerFactory.getLogger(FieldIdentificationPictureServiceImpl.class);

    @Resource
    private FieldIdentificationPictureMybatisDAO pictureDAO;

    /**
     * @param pictureDTO 图片DTO
	 * @param pager 分页
	 * @return 表格
     */
    @Override
    public DataGrid<FieldIdentificationPictureDTO> findPictureListByCondition(FieldIdentificationPictureDTO pictureDTO, Pager<FieldIdentificationAuditDTO> pager) {
    	DataGrid<FieldIdentificationPictureDTO> dataGrid = new DataGrid<FieldIdentificationPictureDTO>();
    	if(pictureDTO == null){
    		return dataGrid;
    	}
    	long listSize = pictureDAO.selectCount(pictureDTO);
    	if (listSize > 0) {
    		List<FieldIdentificationPictureDTO> listPictureDTO = pictureDAO.selectList(pictureDTO, pager);
    		dataGrid.setRows(listPictureDTO);
    		dataGrid.setTotal(listSize);
    	}
    	return dataGrid;
    }

    /**
	 * 新增图片
	 * @param pictureDTO
	 * @return 执行结果信息类
	 */
	@Override
    public ExecuteResult<String> addPicture(FieldIdentificationPictureDTO pictureDTO) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
	        pictureDAO.addPicture(pictureDTO);
	        executeResult.setResultMessage("新增图片成功！");
        } catch (Exception e) {
        	executeResult.setResultMessage("新增图片失败！");
	        e.printStackTrace();
	        logger.error("【实地认证】-【新增图片】出现异常！");
        }
        return executeResult;
    }

	/**
	 * 修改图片
	 * @param pictureDTO
	 * @return 执行结果信息类
	 */
	@Override
    public ExecuteResult<String> modifyPicture(FieldIdentificationPictureDTO pictureDTO) {
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
        try {
	        pictureDAO.modifyPicture(pictureDTO);
	        executeResult.setResultMessage("修改图片成功！");
        } catch (Exception e) {
        	executeResult.setResultMessage("修改图片失败！");
	        e.printStackTrace();
	        logger.error("【实地认证】-【修改图片】出现异常！");
        }
        return executeResult;
    }

}
