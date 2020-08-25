package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.domain.MallDocument;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.dto.MallTypeDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface MallDocumentDAO extends BaseDAO<MallDocument>{
	/**
	 * 
	 * <p>Discription:[帮助文档列表查询]</p>
	 * @param mallDocumentDTO,pager
	 * @return List
	 * Created on 2015-1-25
	 * @author:周立明
	 */
	public List<MallDocumentDTO> queryMallDocumentList(@Param("entity") MallDocumentDTO mallDocumentDTO, @Param("page") Pager page);
	/**
	 * 
	 * <p>Discription:[帮助文档详情查询]</p>
	 * @param id
	 * @return MallDocumentDTO
	 * Created on 2015-1-25
	 * @author:周立明
	 */
	public MallDocumentDTO getMallDocumentById(Object id);
	/**
	 * 
	 * <p>Discription:[帮助文档数量查询]</p>
	 * @param mallDocumentDTO
	 * @return Long
	 * Created on 2015-1-25
	 * @author:周立明
	 */
	public Long queryMallDocumentCount(@Param("entity") MallDocumentDTO mallDocumentDTO);
	
	/**
	 * <p>根据类型获取分类</p>
	 * Created on 2015年1月29日
	 * @param type
	 * @return
	 * @author:[周乐]
	 */
	public List<MallTypeDTO> queryMallDocumentListByType(Integer type);
}
