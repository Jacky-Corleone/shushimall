package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemAttrBean;
import com.camelot.goodscenter.domain.ItemAttrSeller;
import com.camelot.goodscenter.domain.ItemAttrValueBean;
import com.camelot.goodscenter.domain.ItemAttrValueSeller;
import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.outdto.QueryChildCategoryOutDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface CategoryAttrDAO extends BaseDAO<CategoryAttrDTO>{
	/**
	 * 添加商品属性
	 * @param categoryAttrDTO
	 */
	public int addAttr(CategoryAttrDTO categoryAttrDTO);
	/**
	 * 添加商品属性值
	 * @param categoryAttrDTO
	 */
	public void addAttrValue(CategoryAttrDTO categoryAttrDTO);
	/**
	 * 添加商品类别属性关系值
	 * @param categoryAttrDTO
	 */
	public void addCategoryAttrValue(CategoryAttrDTO categoryAttrDTO);
	/**
	 * 根据类目id和商品type查出属性name和属性id
	 * @param categoryAttrDTO
	 */
	public List<CategoryAttrDTO> queryAttrNameList(CategoryAttrDTO categoryAttrDTO);
	/**
	 * 根据属性id查出属性值name和属性值id
	 * @param categoryAttrDTO
	 */
	public List<CategoryAttrDTO> queryValueNameList(CategoryAttrDTO categoryAttrDTO);
	
	/**
	 * 
	 * <p>Discription:[查询卖家店铺类目属性]</p>
	 * Created on 2015-3-11
	 * @param inDTO inDTO.attrType : 属性类型:1:销售属性;2:非销售属性
	 * @return
	 * @author:wangcs
	 */
	public List<ItemAttrSeller> queryAttrSellerList(@Param("param") CatAttrSellerDTO inDTO);
	
	/**
	 * 
	 * <p>Discription:[查询卖家店铺类目属性值]</p>
	 * Created on 2015-3-11
	 * @param sellerAttrId
	 * @return
	 * @author:wangcs
	 */
	public List<ItemAttrValueSeller> queryAttrValueSellerList(@Param("sellerAttrId") Long sellerAttrId);
	
	/**
	 * 
	 * <p>Discription:[插入item_attribute商品属性]</p>
	 * Created on 2015-3-11
	 * @param bean
	 * @author:wangcs
	 */
	public void insertItemAttr(@Param("param") ItemAttrBean bean);
	
	/**
	 * 
	 * <p>Discription:[插入item_attr商家属性关联表]</p>
	 * Created on 2015-3-11
	 * @param attrSeller
	 * @author:wangcs
	 */
	public void insertItemAttrSeller(@Param("param") ItemAttrSeller attrSeller);
	
	/**
	 * 
	 * <p>Discription:[根据条件查询item_attr商家属性关联表]</p>
	 * Created on 2015-3-11
	 * @param sellerId 卖家ID
	 * @param shopId 店铺ID
	 * @param cid 类目ID
	 * @param attrId 属性ID
	 * @return
	 * @author:wangcs
	 */
	public ItemAttrSeller getItemAttrSeller(@Param("sellerId") Long sellerId,@Param("shopId") Long shopId,@Param("cid") Long cid,@Param("attrId") Long attrId);
	
	/**
	 * 
	 * <p>Discription:[插入item_attribute_value商品属性值表]</p>
	 * Created on 2015-3-11
	 * @param bean
	 * @author:wangcs
	 */
	public void insertItemAttrValue(@Param("param") ItemAttrValueBean bean);
	
	/**
	 * 
	 * <p>Discription:[插入item_attr_value商家属性值关联表]</p>
	 * Created on 2015-3-11
	 * @param attrValSeller
	 * @author:wangcs
	 */
	public void insertItemAttrValueSeller(@Param("param") ItemAttrValueSeller attrValSeller);
	
	/**
	 * 
	 * <p>Description: [根据商品属性删除商品类别属性关系]</p>
	 * Created on 2015年8月16日
	 * @param value_id
	 * @author:[宋文斌]
	 */
	public void deleteCategoryAttrByAttrId(@Param("attr_id") Long attr_id);
	
	/**
	 * 
	 * <p>Description: [根据商品属性值删除商品类别属性值关系]</p>
	 * Created on 2015年8月16日
	 * @param value_id
	 * @author:[宋文斌]
	 */
	public void deleteCategoryAttrValueByValueId(@Param("value_id") Long value_id);
	
	/**
	 * 
	 * <p>Description: [根据商品属性删除商品类别属性值关系]</p>
	 * Created on 2015年8月16日
	 * @param attr_id
	 * @author:[宋文斌]
	 */
	public void deleteCategoryAttrValueByAttrId(@Param("attr_id") Long attr_id);
	/**
	 * 
	 * <p>Discription:[查询子集类目，包含自己]</p>
	 */
	
	public QueryChildCategoryOutDTO queryChildCategory(@Param("entity")ItemCategoryDTO itemCategoryDTO);

	
}
