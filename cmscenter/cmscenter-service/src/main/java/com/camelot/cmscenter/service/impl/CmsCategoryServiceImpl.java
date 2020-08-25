package com.camelot.cmscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.cmscenter.dao.CMSCategoryDAO;
import com.camelot.cmscenter.dto.CmsCategoryDTO;
import com.camelot.cmscenter.service.CmsCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;


@Service("cmsCategoryService")
public class CmsCategoryServiceImpl implements CmsCategoryService{
	private static final Logger logger = LoggerFactory.getLogger(CmsCategoryService.class);
	
	@Resource
	private CMSCategoryDAO cmsCategoryDAO;;

	@Override
	public DataGrid<CmsCategoryDTO> queryCmsCategoryList(Pager page,
			CmsCategoryDTO cmsCategoryDTO) {
		DataGrid<CmsCategoryDTO> data = new DataGrid<CmsCategoryDTO>();
		try {
			List<CmsCategoryDTO> listCmsCategory= cmsCategoryDAO.queryCategoryList(cmsCategoryDTO, page);
			//首先将dto转换成domain
//			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			//查询数据库中所有的数据  返回个数
			long size = cmsCategoryDAO.queryCount(cmsCategoryDTO);
			//将数据库中的总条数set到dataGrid的total中
			data.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			data.setRows(listCmsCategory);

		} catch (Exception e) {
			logger.error("执行方法【queryCategoryList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public CmsCategoryDTO getCmsCategoryById(String id) {
		return cmsCategoryDAO.queryById(id);
	}

}
