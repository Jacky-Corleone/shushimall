package com.camelot.storecenter.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;


/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface ShopModifyInfoDAO  extends BaseDAO<ShopModifyInfoDTO>{

	/**
	 * 
	 * <p>Discription:[查询存在修改详情的列]</p>
	 * Created on 2015-3-13
	 * @param shopModifyInfoDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	List<ShopModifyInfoDTO> selectListGroupShopId(@Param("entity")ShopModifyInfoDTO shopModifyInfoDTO, @Param("page")Pager page);
	/**
	 * 
	 * <p>Discription:[查询存在修改详情的列 条数]</p>
	 * Created on 2015-3-13
	 * @param shopModifyInfoDTO
	 * @return
	 * @author:yuht
	 */
	Long selectCountGroupShopId(@Param("entity")ShopModifyInfoDTO shopModifyInfoDTO);
	/**
	 * 
	 * <p>Discription:[修改申请时间]</p>
	 * Created on 2015-3-13
	 * @param id
	 * @author:yuht
	 */
	void updateDate(Long id);
	
	/**
	 * 
	 * <p>Discription:[查询店铺信息修改总表 ]</p>
	 * Created on 2015-3-14
	 * @param shopIds
	 * @return
	 * @author:yuht
	 */
	List<ShopModifyInfoDTO> selectByIds(@Param("shopIds") Long[] shopIds);

	
}