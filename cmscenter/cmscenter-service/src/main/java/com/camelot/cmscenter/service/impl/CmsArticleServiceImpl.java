package com.camelot.cmscenter.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.camelot.cmscenter.dao.CMSArticleDAO;
import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;


@Service("cmsArticleService")
public class CmsArticleServiceImpl implements CmsArticleService{
	private static final Logger logger = LoggerFactory.getLogger(CmsArticleService.class);
	
	@Resource
	private CMSArticleDAO cmsArticleDAO;;

	@Override
	public DataGrid<CmsArticleDTO> queryCmsArticleList(Pager page,
			CmsArticleDTO cmsArticleDTO) {
		DataGrid<CmsArticleDTO> data = new DataGrid<CmsArticleDTO>();
		try {
			List<CmsArticleDTO> listCmsArticle= cmsArticleDAO.queryArticleList(cmsArticleDTO, page);
			//首先将dto转换成domain
//			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			//查询数据库中所有的数据  返回个数
			long size = cmsArticleDAO.queryCount(cmsArticleDTO);
			//将数据库中的总条数set到dataGrid的total中
			data.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			data.setRows(listCmsArticle);

		} catch (Exception e) {
			logger.error("执行方法【queryArticleList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public CmsArticleDTO getCmsArticleById(String id) {
		return cmsArticleDAO.queryById(id);
	}

}
