package com.camelot.storecenter.service;

import java.util.List;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopModifyInfoDTO;

/**
 * 
 * <p>Description: [店铺 信息 修改 总]</p>
 * Created on 2015-3-13
 * @author  yuht
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ShopModifyInfoExportService {

	/**
	 * 
	 * <p>Discription:[店铺修改信息列表查询]</p>
	 * Created on 2015-3-13
	 * @param shopModifyInfoDTO
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ShopModifyInfoDTO>> queryShopModifyInfo(ShopModifyInfoDTO shopModifyInfoDTO,Pager page);
	
	/**
	 * 
	 * <p>Discription:[修改  店铺信息修改表  状态  ]</p>
	 * Created on 2015-3-13
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String>  modifyShopModifyStatus(ShopModifyInfoDTO shopModifyInfoDTO);
	/**
	 * 
	 * <p>Discription:[根据店铺Id查询 店铺信息修改状态]</p>
	 * Created on 2015-3-14
	 * @param shopIds
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<List<ShopModifyInfoDTO>> queryShopModifyInfoById(Long... shopIds);
}
