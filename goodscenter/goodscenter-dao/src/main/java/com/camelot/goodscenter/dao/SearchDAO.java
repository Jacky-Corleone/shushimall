package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.Item;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.openplatform.common.Pager;

public interface SearchDAO {

	/**
	 * 
	 * <p>Discription:[查询搜索商品]</p>
	 * Created on 2015-3-26
	 * @param inDTO
	 * @param pager
	 * @return
	 * @author:wangcs
	 */
	@SuppressWarnings("rawtypes")
	List<ItemSkuDTO> queryItemSkus(@Param("entity") SearchInDTO inDTO, @Param("page") Pager pager);

	/**
	 * 
	 * <p>Discription:[查询搜索商品总条数]</p>
	 * Created on 2015-3-26
	 * @param inDTO
	 * @param object
	 * @return
	 * @author:wangcs
	 */
	long queryItemSkusCount(@Param("entity") SearchInDTO inDTO,@Param("page") Pager pager);

	/**
	 * 
	 * <p>Discription:[查询搜索页要返回的搜索条件ID
	 * 属性串 、品牌  和类目
	 * ]</p>
	 * Created on 2015-3-26
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	List<Item> queryConditionIds(@Param("entity") SearchInDTO inDTO);

	
	
}
