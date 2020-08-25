package com.camelot.cmscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.cmscenter.dto.CmsExperStoreDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.CmsDAO;

/**
 * 体验店Dao
 * @author jh
 *
 */
public interface CMSExperStoreDAO extends CmsDAO<CmsExperStoreDTO>{
	
	/**
	 * 增
	 */
	public void add(CmsExperStoreDTO cmsExperStoreDTO);
	/**
	 * 删
	 * @param cmsExperStoreInfo
	 */
	public void delete(CmsExperStoreDTO cmsExperStoreDTO);
	/**
	 * 改
	 */
	public void update(CmsExperStoreDTO cmsExperStoreDTO);
	/**
	 * 查   根据Id查询
	 * @param id
	 */
	public CmsExperStoreDTO queryObjByid(String id);
	
	/**
	 * 分页查询
	 * @param page
	 * @param cmsExperStoreInfo
	 */
	public List<CmsExperStoreDTO> queryByPageList(@Param("page") Pager<?> page,@Param("cmsExperStoreDTO")CmsExperStoreDTO cmsExperStoreDTO);
	
	/**
	 * 查询条数
	 * @return
	 */
	public long getCount(@Param("cmsExperStoreDTO")CmsExperStoreDTO cmsExperStoreDTO);
	

}
