package com.camelot.cmscenter.service.impl;

import com.camelot.cmscenter.dao.CmsArticleTagsDAO;
import com.camelot.cmscenter.dao.CmsArticleTagsListDAO;
import com.camelot.cmscenter.dto.CmsArticleTagsDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.cmscenter.service.CmsArtTagsListService;
import com.camelot.cmscenter.service.CmsArtTagsService;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("cmsArtTagsService")
public class CmsArtTagsServiceImpl implements CmsArtTagsService {
	private static final Logger logger = LoggerFactory.getLogger(CmsArticleService.class);
	
	@Resource
	private CmsArticleTagsDAO cmsArticleTagsDAO;;

	@Override
	public DataGrid<CmsArticleTagsDto> queryCmsArtTagsList(Pager page, CmsArticleTagsDto cmsArticleTagsDto) {
		DataGrid<CmsArticleTagsDto> data = new DataGrid<CmsArticleTagsDto>();
		try {
			List<CmsArticleTagsDto> cmsArticleTagsDtos= cmsArticleTagsDAO.queryArtTagList(cmsArticleTagsDto, page);
			//查询数据库中所有的数据  返回个数
			long size = cmsArticleTagsDAO.queryCount(cmsArticleTagsDto);
			//将数据库中的总条数set到dataGrid的total中
			data.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			data.setRows(cmsArticleTagsDtos);

		} catch (Exception e) {
			logger.error("执行方法【queryArticleList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return data;
	}

	@Override
	public CmsArticleTagsDto getCmsArtTagsById(String id) {
		return cmsArticleTagsDAO.queryById(id);
	}
}
