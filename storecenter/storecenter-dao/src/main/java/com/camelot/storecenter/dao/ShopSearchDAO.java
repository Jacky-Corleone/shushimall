package com.camelot.storecenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopDTO;

public interface ShopSearchDAO {

	/**
	 * 
	 * <p>Discription:[搜索店铺]</p>
	 * Created on 2015-4-17
	 * @param inDTO
	 * @param page
	 * @return
	 * @author:wangcs
	 */
	@SuppressWarnings("rawtypes")
	List<ShopDTO> searchShop(@Param("entity") ShopDTO inDTO,@Param("page") Pager page);

	/**
	 * 
	 * <p>Discription:[搜索店铺总数]</p>
	 * Created on 2015-4-17
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	Long searchShopCount(@Param("entity") ShopDTO inDTO);

}
