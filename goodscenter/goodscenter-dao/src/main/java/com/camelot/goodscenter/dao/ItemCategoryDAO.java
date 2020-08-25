package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemCategory;
import com.camelot.goodscenter.domain.ItemCategoryCascade;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemCategoryDAO extends BaseDAO<ItemCategoryDTO>{
	/**
	 * 平台所有类目列表查询
	 * @param Pager
	 * @return dataGrid
	 * @author 周立明
	 */
	public List<ItemCategoryDTO> queryItemCategoryAllList(@Param("entity") ItemCategoryDTO itemCategoryDTO,@Param("page") Pager pager);

	/**
	 * 
	 * <p>Discription:[]</p>
	 * Created on 2015-3-9
	 * @param cids
	 * @return
	 * @author:wangcs
	 */
	 
//	public List<ItemCategoryCascade> queryParentCats(@Param("cids") Long[] cids);
	/**
	 * 
	 * <p>Discription:[]</p>
	 * Created on 2015-3-9
	 * @param cids
	 * @return
	 * @author:wangcs
	 */
	 
	public List<ItemCategoryCascade> queryParentCats(@Param("lev")Integer lev,@Param("cids") Long[] cids);

	/**
	 * 
	 * <p>Discription:[根据属性ID组查询属性]</p>
	 * Created on 2015-3-19
	 * @param attrIds
	 * @return
	 * @author:wangcs
	 */
	public List<ItemAttr> queryItemAttrList(@Param("keyList") List<String> attrIds);
    
	/**
	 * 
	 * <p>Discription:[根据商品属性ID，和商品属性值ID查询对应属性信息]</p>
	 * Created on 2015-3-5
	 * @param key 商品属性ID
	 * @param value 商品属性值ID 集合  若此参数为空  则查询商品属性ID下的所有属性
	 * @return
	 * @author:wangcs
	 */
	public List<ItemAttrValue> queryItemAttrValueList(@Param("keyId") Long key, @Param("valueList") List<String> value);

	/**
	 * 
	 * <p>Discription:[根据类目ID查询类目ID下的所有三级类目]</p>
	 * Created on 2015-3-20
	 * @param cid
	 * @return
	 * @author:wangcs
	 */
	public List<ItemCategoryDTO> queryThirdCatsList(Long cid);
	/**
	 * <p>Discription:[根据类目级别查询对应级别的所有类目]</p>
	 * Created on 2015年10月29日
	 * @param categoryLev
	 * @return
	 * @author:[刘喜洋]
	 */
	public List<ItemCategoryDTO> queryItemByCategoryLev(@Param("categoryLev")Integer categoryLev);
	
	/**
	 * 
	 * <p>Discription:[查询三级类目是否高级]</p>
	 * Created on 2015年12月24日
	 * @param attrIds
	 * @return
	 * @author:[王鹏]
	 */
	public List<ItemAttr> queryItemAttrListByIsSenior(@Param("keyList") List<String> attrIds,@Param("isSenior")Integer isSenior);
	
}
