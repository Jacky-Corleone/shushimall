package com.camelot.cmscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.cmscenter.dto.CmsSpecialSubjectDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.CmsDAO;

public interface CmsSpecialSubjectDAO extends CmsDAO<CmsSpecialSubjectDTO>{
	
	/**
	 * 根据Id查询
	 * @param id
	 * @return
	 */
	public CmsSpecialSubjectDTO queryById(String id);
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:查询专题的列表  ]</p>
	 * Created on 2015年2月25日
	 * @param cmsSpecialSubjectDTO
	 * @param page
	 * @return
	 * @author:[weizb]
	 */
	public List<CmsSpecialSubjectDTO> queryDTOList(@Param("page") Pager<?> page,@Param("cmsSpecialSubjectDTO") CmsSpecialSubjectDTO cmsSpecialSubjectDTO);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:  专题删除 ]</p>
	 * Created on 2015年2月25日
	 * @param id
	 * @return
	 * @author:[weizb]
	 */
	public void deleteCss(CmsSpecialSubjectDTO cmsSpecialSubjectDTO);
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述:  保存 ]</p>
	 * Created on 2015年2月25日
	 * @param id
	 * @return
	 * @author:[weizb]
	 */
	public void insertCss(CmsSpecialSubjectDTO cmsSpecialSubjectDTO);
	/**
	 * 更新
	 */
	public void updateCss(CmsSpecialSubjectDTO cmsSpecialSubjectDTO);
	/**
	 * 查询条数
	 * @return
	 */
	public long getCount(@Param("cmsSpecialSubjectDTO")CmsSpecialSubjectDTO cmsSpecialSubjectDTO);
	/**
	 * 发布
	 * @param cmsSpecialSubjectDTO
	 */
	public void releaseCss(CmsSpecialSubjectDTO cmsSpecialSubjectDTO);
	
}
