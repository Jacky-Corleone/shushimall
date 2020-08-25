package com.camelot.storecenter.dao;




import java.util.List;

import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopModifyDetailDTO;


/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface ShopModifyDetailDAO  extends BaseDAO<ShopModifyDetailDTO>{
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015-3-13
	 * @param id
	 * @return
	 * @author:yuht
	 */
	List<ShopModifyDetailDTO> selectListById(Long id);

	
	
}