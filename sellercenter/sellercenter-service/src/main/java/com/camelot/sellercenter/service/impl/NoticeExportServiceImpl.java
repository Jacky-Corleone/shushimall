package com.camelot.sellercenter.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.EntityTranslator;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.dao.NoticeMybatisDAO;
import com.camelot.sellercenter.domain.Notice;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;

/**
 * 
 * <p>Description: [公告服务的处理接口的实现类]</p>
 * Created on 2015年1月22日
 * @author  <a href="mailto: xxx@camelotchina.com">胡恒心</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Service("noticeExportService")
public class NoticeExportServiceImpl implements NoticeExportService {
	private final static Logger LOGGER = LoggerFactory.getLogger(NoticeExportServiceImpl.class);

	@Resource
	private NoticeMybatisDAO noticeMybatisDAO;

	@Override
	public DataGrid<MallNoticeDTO> queryNoticeList(Pager page, MallNoticeDTO dto) {

		DataGrid<MallNoticeDTO> dataGrid = new DataGrid<MallNoticeDTO>();
		try {

			Notice queryNotice = new Notice();
			queryNotice.setContent(dto.getNoticeContent());
			queryNotice.setCreated(dto.getCreated());
			queryNotice.setIsRecommend(dto.getIsRecommend());
			queryNotice.setModified(dto.getModified());
			queryNotice.setPlatformId(dto.getPlatformId());
			queryNotice.setSortNum(dto.getSortNum());
			queryNotice.setStatus(dto.getNoticeStatus());
			queryNotice.setTitle(dto.getNoticeTitle());
			queryNotice.setPlatformId(dto.getPlatformId());
            queryNotice.setNoticeType(dto.getNoticeType());
            queryNotice.setCreateDtBegin(dto.getCreateDtBegin());
            queryNotice.setCreateDtEnd(dto.getCreateDtEnd());
            queryNotice.setPublishDtBegin(dto.getPublishDtBegin());
            queryNotice.setPublishDtEnd(dto.getPublishDtEnd());
            queryNotice.setThemeId(dto.getThemeId());
            queryNotice.setThemeType(dto.getThemeType());
			List<MallNoticeDTO> noticeDTOs = noticeMybatisDAO.queryListDTO(queryNotice, page);
			Long count = noticeMybatisDAO.queryCount(queryNotice);

			dataGrid.setRows(noticeDTOs);
			dataGrid.setTotal(count);
		} catch (Exception e) {
			LOGGER.error("执行方法【queryNoticeList】报错！{}", e);
			throw new RuntimeException(e);
		}
		return dataGrid;
	}

	@Override
	public MallNoticeDTO getNoticeInfo(Long id) {
		return noticeMybatisDAO.queryDTOById(id);
	}

	@Override
	public ExecuteResult<String> deleteNoticeById(Long id) {
		ExecuteResult<String> res=new ExecuteResult<String>();
		Integer count=noticeMybatisDAO.delete(id);
		if(count>0){
			res.setResult("1");
		}else{
			res.setResult("0");
		}
		return  res;
	}

	@Override
	public ExecuteResult<String> modifyNoticeRecommend(Long id, Integer isRecommend) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			Notice notice = new Notice();
			notice.setId(id);
			notice.setIsRecommend(isRecommend);
			int count = noticeMybatisDAO.updateBySelect(notice);
			if (count > 0) {
				result.setResultMessage("操作成功");
			} else {
				result.setResultMessage("操作失败");
			}
		} catch (Exception e) {
			LOGGER.error("执行方法【modifyNoticeRecommend】报错！{}", e);
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> modifyNoticeStatus(Long id, Integer status) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		try {
			Notice notice = new Notice();
			notice.setId(id);
			notice.setStatus(status);
			int count = noticeMybatisDAO.updateBySelect(notice);
			if (count > 0) {
				result.setResultMessage("操作成功");
			} else {
				result.setResultMessage("操作失败");
			}
		} catch (Exception e) {
			LOGGER.error("执行方法【modifyNoticeStatus】报错！{}", e);
			result.getErrorMessages().add(e.getMessage());
			throw new RuntimeException(e);
		}
		return result;
	}

	@Override
	public ExecuteResult<String> addNotice(MallNoticeDTO mallNoticeDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if(StringUtils.isBlank(mallNoticeDTO.getNoticeTitle())){
			result.addErrorMessage("公告标题不能为空！");
			return result;
		}
		if(mallNoticeDTO.getNoticeType()==null){
			result.addErrorMessage("公告类型不能为空！");
			return result;
		}
		if(1==mallNoticeDTO.getNoticeType() && StringUtils.isBlank(mallNoticeDTO.getNoticeContent())){
			result.addErrorMessage("公告内容不能为空");
			return result;
		}
		if(2==mallNoticeDTO.getNoticeType() && StringUtils.isBlank(mallNoticeDTO.getUrl())){
			result.addErrorMessage("公告链接不能为空");
			return result;
		}
		Notice notice = new Notice();
		//1 首先查询 公告的下一个序号
		notice.setPlatformId(mallNoticeDTO.getPlatformId());

		Long currentSortNum = noticeMybatisDAO.getSortNumByCondation(notice);
		Long nextSortNum = 0L;
		if (currentSortNum != null) {
			nextSortNum = currentSortNum + 1;
		}
		notice.setSortNum(nextSortNum.intValue());
		notice.setContent(mallNoticeDTO.getNoticeContent());
		if (mallNoticeDTO.getIsRecommend() != null) {//默认不置顶
			notice.setIsRecommend(mallNoticeDTO.getIsRecommend());
		} else {
			notice.setIsRecommend(0);
		}
		if (mallNoticeDTO.getNoticeStatus() != null) {//默认草稿
			notice.setStatus(mallNoticeDTO.getNoticeStatus());
		} else {
			notice.setStatus(2);
		}
		if (mallNoticeDTO.getPlatformId() != null) {//默认平台公告
			notice.setPlatformId(mallNoticeDTO.getPlatformId());
		} else {
			notice.setPlatformId(0L);
		}
		notice.setTitle(mallNoticeDTO.getNoticeTitle());
		notice.setNoticeType(mallNoticeDTO.getNoticeType());
		notice.setUrl(mallNoticeDTO.getUrl());
		notice.setThemeId(mallNoticeDTO.getThemeId());
		notice.setThemeType(mallNoticeDTO.getThemeType());

		noticeMybatisDAO.add(notice);
		result.setResultMessage("操作成功");
		return result;
	}

	@Override
	public ExecuteResult<String> modifyNoticeInfo(MallNoticeDTO mallNoticeDTO) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		if (mallNoticeDTO.getNoticeId() == null) {
			result.addErrorMessage("公告ID不能为空！");
			return result;
		}
		Notice notice = this.noticeMybatisDAO.queryById(mallNoticeDTO.getNoticeId());
		if(notice==null){
			result.addErrorMessage("公告不存在！");
			return result;
		}
		notice.setStatus(mallNoticeDTO.getNoticeStatus());
		notice.setTitle(mallNoticeDTO.getNoticeTitle());
		notice.setContent(mallNoticeDTO.getNoticeContent());
		notice.setUrl(mallNoticeDTO.getUrl());
		notice.setId(mallNoticeDTO.getNoticeId());
		notice.setSortNum(mallNoticeDTO.getSortNum());
		notice.setNoticeType(mallNoticeDTO.getNoticeType());
		int count = noticeMybatisDAO.update(notice);
		if (count > 0) {
			result.setResultMessage("操作成功");
		} else {
			result.setResultMessage("操作失败");
		}
		return result;
	}

	@Override
	public ExecuteResult<MallNoticeDTO> updateNoticSortNum(MallNoticeDTO mallNoticeDTO, Integer modifyNum) {
		ExecuteResult<MallNoticeDTO> res = new ExecuteResult<MallNoticeDTO>();
		res.setResult(mallNoticeDTO);
		if (mallNoticeDTO.getNoticeId() == null || modifyNum == null) {
			throw new RuntimeException("公告ID 和 操作代码值不能为空");
		}
		
		Notice queryNotice = new Notice();
		queryNotice.setPlatformId(mallNoticeDTO.getPlatformId());
		queryNotice.setSortNum(mallNoticeDTO.getSortNum() );
        if(modifyNum>0){
            queryNotice.setIsRecommend(1);
        }else{
            queryNotice.setIsRecommend(-1);
        }
		List<MallNoticeDTO> noticList = noticeMybatisDAO.queryListByNextSort(queryNotice, new Pager());
		if (noticList != null && noticList.size() > 0) {
			MallNoticeDTO preDto = noticList.get(0);
			Notice preUpdateNotice = new Notice();
			preUpdateNotice.setId(preDto.getNoticeId());
			preUpdateNotice.setSortNum(mallNoticeDTO.getSortNum());
			noticeMybatisDAO.update(preUpdateNotice);

		}
		//		}
		Notice notice = new Notice();
		notice.setId(mallNoticeDTO.getNoticeId());
		notice.setSortNum(mallNoticeDTO.getSortNum() + modifyNum);
		noticeMybatisDAO.update(notice);

		//2 查询上一个或者下一个

		return res;

	}
}
