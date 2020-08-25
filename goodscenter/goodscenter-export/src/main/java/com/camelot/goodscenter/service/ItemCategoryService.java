package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.outdto.QueryChildCategoryOutDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

public interface ItemCategoryService {
	/**
	 * 平台类目添加功能接口
	 * @param itemCategoryDTO
	 * @return executeResult
	 * @author 周立明
	 */
	public ExecuteResult<String> addItemCategory(ItemCategoryDTO itemCategoryDTO) ;
	
	/**
	 * 
	 * <p>Discription:[删除商品类目]</p>
	 * Created on 2015-5-5
	 * @param cid
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> deleteItemCategory(Long cid);
	
	/**
	 * 平台所有类目列表查询
	 * @param pager
	 * @return dataGrid
	 * @author 周立明
	 */
	public DataGrid<ItemCategoryDTO> queryItemCategoryAllList(Pager Pager);
	/**
	 * 根据父级id查询平台类目类目列表
	 * @param parentCid
	 * @return dataGrid
	 * @author 周立明
	 */
	public DataGrid<ItemCategoryDTO> queryItemCategoryList(Long parentCid);
	
	/**
	 * 
	 * <p>Discription:[根据第三级类目ID查询父级类目ID的信息]</p>
	 * Created on 2015-3-9
	 * @param cid
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<List<ItemCatCascadeDTO>> queryParentCategoryList(Long... cid);
	/**
	 * 
	 * <p>Discription:[根据lev层级及第二/三级类目ID查询父级类目ID的信息]</p>
	 * Created on 2015-3-9
	 * @param cid
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<List<ItemCatCascadeDTO>> queryParentCategoryList(Integer lev,Long... cid);
	/**
	 * 平台类目属性添加
	 * @param cid
	 * @param attrName
	 * @param attrType 属性类型:1:销售属性;2:非销售属性
	 * @return executeResult
	 * @author 周立明
	 */
	public ExecuteResult<Long> addCategoryAttr(Long cid,String attrName,Integer attrType,int isSenior);
	/**
	 * 平台类目类目属性查询
	 * @param cid
	 * @param attrType 属性类型:1:销售属性;2:非销售属性
	 * @return dataGrid
	 * @author 周立明
	 */
	public DataGrid<CategoryAttrDTO> queryCategoryAttrList(Long cid,Integer attrType);
	/**
	 * 平台类目属性值添加
	 * @param cid
	 * @param attrId
	 * @param valueName
	 * @return executeResult
	 * @author 周立明
	 */
	public ExecuteResult<Long> addCategoryAttrValue(Long cid,Long attrId,String valueName);
	
	
	/**
	 * 
	 * <p>Discription:[查询卖家属性查询]</p>
	 * Created on 2015-3-11
	 * @param inDTO   inDTO.attrType : 属性类型:1:销售属性;2:非销售属性
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<List<ItemAttr>> queryCatAttrSellerList(CatAttrSellerDTO inDTO);
	
	/**
	 * 
	 * <p>Discription:[
	 * 根据属性键值对查询属性列表
	 * ]</p>
	 * Created on 2015-3-11
	 * @param attrStr
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<List<ItemAttr>> queryCatAttrByKeyVals(String attrStr);
	
	/**
	 * 
	 * <p>Discription:[增加卖家商品类目属性]</p>
	 * Created on 2015-3-11
	 * @param inDTO  inDTO.attrType属性类型:1:销售属性;2:非销售属性 必填
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttr> addItemAttrSeller(CatAttrSellerDTO inDTO);
	
	/**
	 * 
	 * <p>Discription:[增加卖家商品类目属性值]</p>
	 * Created on 2015-3-11
	 * @param inDTO  
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<ItemAttrValue> addItemAttrValueSeller(CatAttrSellerDTO inDTO);
	
	/**
	 * 
	 * <p>Description: [根据商品属性删除商品类别属性关系]</p>
	 * Created on 2015年8月16日
	 * @param cid
	 * @param attr_id
	 * @param attrType 平台类目属性：2 销售类目属性：1
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> deleteCategoryAttr(Long cid, Long attr_id, Integer attrType);
	
	/**
	 * 
	 * <p>Description: [根据商品属性值删除商品类别属性值关系]</p>
	 * Created on 2015年8月16日
	 * @param cid
	 * @param attr_id
	 * @param value_id
	 * @param attrType 平台类目属性：2 销售类目属性：1
	 * @return
	 * @author:[宋文斌]
	 */
	public ExecuteResult<String> deleteCategoryAttrValue(Long cid, Long attr_id, Long value_id, Integer attrType);
	
	/**
	 * 
	 * <p>Discription:[修改类目]</p>
	 * Created on 2015-8-24
	 * @param itemCategoryDTO
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<String> updateCategory(ItemCategoryDTO itemCategoryDTO);
	
	/**
	 * <p>Discription:[查询子集类目，包含自己]</p>
	 * @param itemCategoryDTO
	 * @return	字符串拼接的结果集
	 */
	public ExecuteResult<QueryChildCategoryOutDTO> queryAllChildCategory(ItemCategoryDTO itemCategoryDTO);

	/**
	 * <p>Discription:[根据类目级别查询对应级别的所有类目]</p>
	 * Created on 2015年10月29日
	 * @param categoryLev
	 * @return
	 * @author:[刘喜洋]
	 */
	public DataGrid<ItemCategoryDTO> queryItemByCategoryLev(Integer categoryLev);
	
	/**
	 * 
	 * <p>Discription:[通过类目id查询类目信息]</p>
	 * Created on 2015年12月24日
	 * @param cid
	 * @return
	 * @author:[王鹏]
	 */
	public ExecuteResult<ItemCategoryDTO> queryItemByCategoryById(Long cid);
	
	/**
	 * 查询销售属性--销售属性值copy后的记录
	 * @param attr 销售属性：销售属性值；
	 * @return 复制后的销售属性--销售属性值
	 */
	public ExecuteResult<List<ItemAttr>> addCatAttrByKeyValsBak(String attr);
	
	/**
	 * 
	 * <p>Description: [根据类目ID查询类目ID下的所有三级类目]</p>
	 * Created on 2015年11月30日
	 * @param cid
	 * @return
	 * @author:[宋文斌]
	 */
	ExecuteResult<List<ItemCategoryDTO>> queryThirdCatsList(Long cid);
	
}
