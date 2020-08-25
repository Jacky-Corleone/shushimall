package com.camelot.basecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.basecenter.domain.MallClassify;
import com.camelot.basecenter.dto.MallClassifyDTO;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface MallClassifyDAO extends BaseDAO<MallClassify>{
	
	public List<MallClassifyDTO>  queryMallCassifyList(@Param("mallClassifyDTO") MallClassifyDTO mallCassifyDTO,@Param("page") Pager page);
    
	public long queryMallCassifyCount(@Param("mallClassifyDTO") MallClassifyDTO mallCassifyDTO);
	
    public int addMallCassify(MallClassify mallClassify);
    
    public int modifyStatusById(@Param("id")int id, @Param("status")int status);
}
