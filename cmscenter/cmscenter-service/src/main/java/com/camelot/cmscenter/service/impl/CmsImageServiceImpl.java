package com.camelot.cmscenter.service.impl;

import com.alibaba.fastjson.JSON;
import com.camelot.cmscenter.dao.CmsImgDAO;
import com.camelot.cmscenter.dto.CmsImageDto;
import com.camelot.cmscenter.service.CmsImageService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/** 
 * <p>Description: 商城广告服务实现</p>
 * Created on 2016年2月22日
 * @author mengbo
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("cmsImageService")
public class CmsImageServiceImpl implements CmsImageService {
	
	private static final Logger logger = LoggerFactory.getLogger(CmsImageService.class);

	/**
	 *
	 */
	@Resource
	private CmsImgDAO cmsImgDAO;
	/**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2016年2月22日
	 * @param page
	 * @param cmsImageDto
	 * @return
	 * @author:[mengbo]
	 * @throws Exception 
	 */
	@Override
	public DataGrid<CmsImageDto> queryCmsImageList(Pager page, CmsImageDto cmsImageDto){
		//查询数据库中所有的数据  返回list
		DataGrid<CmsImageDto> dataGrid = new DataGrid<CmsImageDto>();
		try {
			List<CmsImageDto> listCmsImageDto= cmsImgDAO.queryDTOList(cmsImageDto, page);
			//首先将dto转换成domain
//			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			//查询数据库中所有的数据  返回个数
			long size = cmsImgDAO.queryCount(cmsImageDto);
			//将数据库中的总条数set到dataGrid的total中
			dataGrid.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			dataGrid.setRows(listCmsImageDto);

		} catch (Exception e) {
			logger.error("执行方法【queryMallRecList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return dataGrid;
	}

	/**
	 * <p>Discription 图片详情查询</p>
	 * Created on 2015年1月26日
	 * @param id
	 * @return
	 * @author:[mengbo]
	 * @throws Exception 
	 */
	
	@Override
	public CmsImageDto getCmsImgById(String id) {
		return cmsImgDAO.queryDTOById(id);
	}

	/**
	 * <p>Discription:广告修改</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@Override
	public void modifyCmsImg(CmsImageDto cmsImageDto) {
		cmsImgDAO.modifyCmsImage(cmsImageDto);
	}

}
