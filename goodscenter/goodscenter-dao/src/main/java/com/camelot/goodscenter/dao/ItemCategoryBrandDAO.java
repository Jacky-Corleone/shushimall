package com.camelot.goodscenter.dao;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemCategoryBrand;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemCategoryBrandDAO extends BaseDAO<ItemCategoryBrand>{

	/**
	 * <p>Discription:[根据三级类目  二级类目和 品牌名称 查询品牌，确定类目品牌唯一性，同一类目下不能存在名字相投的品牌]</p>
	 * Created on 2015-3-2
	 * @param param 二级类目ID  三级类目ID 品牌  必填
	 * @return
	 * @author:[wangcunshan]
	 */
	ItemCategoryBrand queryICBByName(@Param("param") ItemBrandDTO param);

	/**
	 * <p>Discription:[根据三级类目  二级类目和 品牌ID 查询品牌，确定类目品牌唯一性]</p>
	 * Created on 2015-3-2
	 * @param param 二级类目ID  三级类目ID 品牌ID  必填
	 * @return
	 * @author:[wangcunshan]
	 */
	ItemCategoryBrand queryICBByBrandId(@Param("param") ItemCategoryBrand itemCategoryBrand);
	/**
	 * 
	 * <p>Discription:[查询该品牌是否关联类目]</p>
	 * Created on 2015-5-13
	 * @param brandId
	 * @return
	 * @author:yuht
	 */
	Long queryCbByBrandId(Long brandId);
}
