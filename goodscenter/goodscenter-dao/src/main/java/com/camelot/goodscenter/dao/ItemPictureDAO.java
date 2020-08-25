package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemPicture;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemPictureDAO extends BaseDAO<ItemPicture>{

	/**
	 * 
	 * <p>Discription:[根据商品ID获取商品图片]</p>
	 * Created on 2015-3-6
	 * @param itemId
	 * @return
	 * @author:wangcs
	 */
	List<ItemPicture> queryItemPicsById(@Param("itemId")Long itemId);

	/**
	 * 
	 * <p>Discription:[根据商品ID删除商品图片]</p>
	 * Created on 2015-3-17
	 * @param itemId
	 * @author:wangcs
	 */
	void deleteItemPicture(@Param("itemId") Long itemId);
}
