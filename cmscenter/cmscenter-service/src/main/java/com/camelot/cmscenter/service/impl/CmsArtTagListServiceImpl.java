package com.camelot.cmscenter.service.impl;

import com.camelot.cmscenter.dao.CMSArticleDAO;
import com.camelot.cmscenter.dao.CmsArticleTagsListDAO;
import com.camelot.cmscenter.dto.CmsArticleDTO;
import com.camelot.cmscenter.dto.CmsArticleTagsListDto;
import com.camelot.cmscenter.dto.CmsArticleTagsListJoinDto;
import com.camelot.cmscenter.service.CmsArtTagsListService;
import com.camelot.cmscenter.service.CmsArticleService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@SuppressWarnings("SpringJavaAutowiringInspection")
@Service("cmsArtTagsListService")
public class CmsArtTagListServiceImpl implements CmsArtTagsListService{
	private static final Logger logger = LoggerFactory.getLogger(CmsArticleService.class);
	
	@Resource
	private CmsArticleTagsListDAO cmsArticleTagsListDAO;;

	@Override
	public DataGrid<CmsArticleTagsListDto> queryCmsArtTagsListList(Pager page, CmsArticleTagsListDto cmsArticleTagsListDto) {
		DataGrid<CmsArticleTagsListDto> data = new DataGrid<CmsArticleTagsListDto>();
		try {
			List<CmsArticleTagsListDto> cmsArticleTagsListDtos= cmsArticleTagsListDAO.queryArtTagList(cmsArticleTagsListDto, page);
			//首先将dto转换成domain
//			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			//查询数据库中所有的数据  返回个数
			long size = cmsArticleTagsListDAO.queryCount(cmsArticleTagsListDto);
			//将数据库中的总条数set到dataGrid的total中
			data.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			data.setRows(cmsArticleTagsListDtos);

		} catch (Exception e) {
			logger.error("执行方法【queryArticleList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return data;
	}
	@Override
	public DataGrid<CmsArticleTagsListJoinDto> queryCmsArtTagsListListJion(Pager page, CmsArticleTagsListDto cmsArticleTagsListDto) {
		DataGrid<CmsArticleTagsListJoinDto> data = new DataGrid<CmsArticleTagsListJoinDto>();
		try {
			List<CmsArticleTagsListJoinDto> cmsArticleTagsListDtos= cmsArticleTagsListDAO.queryArtTagjoinList(cmsArticleTagsListDto, page);
			//首先将dto转换成domain
//			MallRec mallRec = EntityTranslator.transObj(mallRecDTO, MallRec.class, false);
			//查询数据库中所有的数据  返回个数
			long size = cmsArticleTagsListDAO.queryCount(cmsArticleTagsListDto);
			//将数据库中的总条数set到dataGrid的total中
			data.setTotal(size);
			//将数据库的数据set到dataGrid的总行数中
			data.setRows(cmsArticleTagsListDtos);

		} catch (Exception e) {
			logger.error("执行方法【queryArticleList】报错！{}",e);
			throw new RuntimeException(e);
		}
		return data;
	}
	@Override
	public List<CmsArticleTagsListDto> getCmsArtTagsListByArtId(String id) {
		List<CmsArticleTagsListDto> listDtos= (List<CmsArticleTagsListDto>) cmsArticleTagsListDAO.queryByArtId(id);
		return listDtos;
	}
	@Override
	public List<CmsArticleTagsListDto> getCmsArtTagsListByImgId(String id) {
		List<CmsArticleTagsListDto> listDtos= (List<CmsArticleTagsListDto>) cmsArticleTagsListDAO.queryByImgId(id);
		return listDtos;
	}
	@Override
	public CmsArticleTagsListDto getCmsArtTagsListById(String id) {
		CmsArticleTagsListDto cmsDto= cmsArticleTagsListDAO.queryById(id);
		return cmsDto;
	}
	@Override
	public void modifyArtTagsList(CmsArticleTagsListDto cmsArticleTagsListDto) {
		 cmsArticleTagsListDAO.modifyCmsArtTagsList(cmsArticleTagsListDto);
	}
	@Override
	public void insertTagList(CmsArticleTagsListDto cmsArticleTagsListDto) {
		cmsArticleTagsListDAO.insertTagList(cmsArticleTagsListDto);
	}
	@Override
	public void deleteArtTagsList(String id) {
		cmsArticleTagsListDAO.delete(id);
	}
	@Override
	public void deleteArtTagsListByImgId(String id) {
		cmsArticleTagsListDAO.deleteByImg(id);
	}
}
