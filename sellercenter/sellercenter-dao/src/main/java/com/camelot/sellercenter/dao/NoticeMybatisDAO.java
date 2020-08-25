package com.camelot.sellercenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.sellercenter.domain.Notice;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;

public interface NoticeMybatisDAO extends BaseDAO<Notice> {

	public MallNoticeDTO queryDTOById(Object id);
	
	public List<MallNoticeDTO> queryListDTO(@Param("entity") Notice notice, @Param("page") Pager page);

    //根据条件查询公告得最大排序号
    public Long  getSortNumByCondation(@Param("entity") Notice notice);

    public List<MallNoticeDTO> queryListByNextSort(@Param("entity") Notice notice, @Param("page") Pager page);
	
}
