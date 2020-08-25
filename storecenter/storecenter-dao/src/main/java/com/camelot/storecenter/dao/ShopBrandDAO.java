package com.camelot.storecenter.dao;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;
import com.camelot.storecenter.dto.ShopBrandDTO;


/**
 * <p>数据交互接口</p>
 *  
 *  @author 
 *  @createDate 
 **/
public interface ShopBrandDAO  extends BaseDAO<ShopBrandDTO>{

	/**
	 * 
	 * <p>Discription:[修改店铺状态]</p>
	 * Created on 2015-3-12
	 * @param shopBrandDTO
	 * @return
	 * @author:yuht
	 */
	Integer modifyShopCategoryStatus(ShopBrandDTO shopBrandDTO);
	/**
	 * 
	 * <p>Discription:[根据店铺ID状态查询店铺品牌]</p>
	 * Created on 2015-3-12
	 * @param shopId
	 * @param status
	 * @return
	 * @author:yuht
	 */
	List<ShopBrandDTO> selectByShopId(@Param("shopId")Long shopId, @Param("status")Integer status);
	/**
	 * 
	 * <p>Discription:[查询所有类目]</p>
	 * Created on 2015-3-23
	 * @param shopBrandDTO
	 * @return
	 * @author:yuht
	 */
	Long selectCountByConditionAll(@Param("entity") ShopBrandDTO shopBrandDTO);
	/**
	 * 
	 * <p>Discription:[查询所有类目数量]</p>
	 * Created on 2015-3-23
	 * @param shopBrandDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	List<ShopBrandDTO> selectListByConditionAll(@Param("entity") ShopBrandDTO shopBrandDTO,@Param("page")  Pager page);
	/**
	 * 
	 * <p>Discription:[根据店铺ID删除品牌]</p>
	 * Created on 2015-3-25
	 * @param id
	 * @author:yuht
	 */
	void deleteByShopId(Long id);
	/**
	 * 
	 * <p>Discription:[根据id查找品牌所有信息]</p>
	 * Created on 2015-7-29
	 * @param id
	 * @return
	 * @author:jiangpeng
	 */
	List<ShopBrandDTO> selectBrandIdById(Long id);
	/**
	 * 
	 * <p>Discription:[根据BrandId修改曾经被驳回的品牌的status的值为-1]</p>
	 * Created on 2015-7-29
	 * @param brandId
	 * @return
	 * @author:jiangpeng
	 */
	void updateStatusByIdAndBrandId(Long brandId);
	
	/**
	 * 
	 * <p>Description: [根据店铺ID和店铺经营的类目ID修改品牌的status的值为-1]</p>
	 * Created on 2015年8月26日
	 * @param shopId
	 * @param cid
	 * @author:[宋文斌]
	 */
	void updateStatusByShopIdAndCid(@Param("shopId") Long shopId,@Param("cid") Long cid);
	
	/**
	 * 
	 * <p>Description: [根据店铺ID和店铺经营的品牌ID修改品牌的status的值为-1]</p>
	 * Created on 2015年8月26日
	 * @param shopId
	 * @param brandId
	 * @author:[宋文斌]
	 */
	void updateStatusByShopIdAndBrandId(@Param("shopId") Long shopId,@Param("brandId") Long brandId);
}