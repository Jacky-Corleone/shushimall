package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.indto.ItemOldSeachInDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

public interface ItemOldDAO extends BaseDAO<ItemOldDTO> {

	void updateStatus(@Param("itemId")Long itemId, @Param("status")Long status, @Param("comment")String comment, @Param("platformUserId")String platformUserId);

	List<ItemOldDTO> querySeachItemOldList(@Param("entity")ItemOldSeachInDTO itemOldSeachInDTO,@Param("page")Pager page);

}
