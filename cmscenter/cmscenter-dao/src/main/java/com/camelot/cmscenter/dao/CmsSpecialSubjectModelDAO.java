package com.camelot.cmscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.cmscenter.dto.CmsSpecialSubjectModelDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.CmsDAO;

public interface CmsSpecialSubjectModelDAO extends CmsDAO<CmsSpecialSubjectModelDTO>{
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询专题的列表  ]</p>
	 * Created on 2015年2月25日
	 * @param cmsSpecialSubjectDTO
	 * @param page
	 * @return
	 * @author:[weizb]
	 */
	public List<CmsSpecialSubjectModelDTO> queryModelList(@Param("cmsSpecialSubjectModelDTO") CmsSpecialSubjectModelDTO cmsSpecialSubjectModelDTO,@Param("page") Pager<?> page);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:  专题删除 ]</p>
	 * Created on 2015年2月25日
	 * @param id
	 * @return
	 * @author:[weizb]
	 */
	public void deleteModel(String id);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:  保存 ]</p>
	 * Created on 2015年2月25日
	 * @param id
	 * @return
	 * @author:[weizb]
	 */
	public void insert(CmsSpecialSubjectModelDTO cmsSpecialSubjectModelDTO);
	/**
	 * 更新
	 */
	public void update(CmsSpecialSubjectModelDTO cmsSpecialSubjectModelDTO);
	/**
	 * 根据Id查询
	 * @param id
	 * @return
	 */
	public CmsSpecialSubjectModelDTO queryModelById(String id);
	
	/**
	 * 获取条数
	 * @param id
	 * @return
	 */
	public long getCount(@Param("cmsSpecialSubjectModelDTO") CmsSpecialSubjectModelDTO cmsSpecialSubjectModelDTO);
	
	/**
	 * 查询列表
	 * @return
	 */
	public List<CmsSpecialSubjectModelDTO> queryAllModelList();
}
