package com.camelot.goodscenter.service;

import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.indto.ItemOldInDTO;
import com.camelot.goodscenter.dto.indto.ItemOldSeachInDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldOutDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldSeachOutDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * 
 * <p>Description: [二手市场]</p>
 * Created on 2015-5-4
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemOldExportService {
	
	
	/**
	 * 
	 * <p>Discription:[二手商品添加]</p>
	 * Created on 2015-5-5
	 * @param itemOldInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> addItemOld(ItemOldInDTO itemOldInDTO);
	
	
	/**
	 * 
	 * <p>Discription:[二手商品修改]</p>
	 * Created on 2015-5-5
	 * @param itemOldInDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyItemOld(ItemOldInDTO itemOldInDTO);
	
	/**
	 * 
	 * <p>Discription:[查询二手商品列表]</p>
	 * Created on 2015-5-5
	 * @param itemOldDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ItemOldDTO>>  queryItemOld(ItemOldDTO itemOldDTO,Pager page);
	
	/**
	 * 
	 * <p>Discription:[根据ID查询]</p>
	 * Created on 2015-5-5
	 * @param itemId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<ItemOldOutDTO>   getItemOld(Long itemId);
	
	
	/**
	 * 
	 * <p>Discription:[修改状态  20审核驳回 3待上架 4在售(上架) 5已下架  8删除 （可批量）]</p>
	 * Created on 2015-5-5
	 * @param status
	 * @param itemId
	 * @param platformUserId 批注人id
	 * @param comment  批注
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> modifyItemOldStatus(String comment,String platformUserId,Long status,Long... itemId);
	/**
	 * 
	 * <p>Discription:[二手商品搜索]</p>
	 * Created on 2015-5-6
	 * @param itemOldSeachInDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<ItemOldSeachOutDTO> seachItemOld(ItemOldSeachInDTO itemOldSeachInDTO,Pager page);
}
