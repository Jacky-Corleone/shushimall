package com.camelot.storecenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;

public interface ShopCategorySellerExportService {
	
	
	/**
	 * <p>Discription:[店铺自定义分类修改]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */
	public ExecuteResult<String> update(ShopCategorySellerDTO dto);
	
	/**
	 * <p>Discription:[店铺自定义分类列表查询]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<DataGrid<ShopCategorySellerDTO>> queryShopCategoryList(ShopCategorySellerDTO dto,Pager pager);
   /**
	 * <p>Discription:[店铺自定义分类单个删除]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<String> deleteById(long id);
  /**
	 * <p>Discription:[店铺自定义分类添加]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<Long> addShopCategory(ShopCategorySellerDTO dto);
  /**
	 * <p>Discription:[店铺自定义分类全部删除]</p>
	 * Created on 2015年3月5日
	 * @param dto
	 * @param pager
	 * @return
	 * @author:[杨芳]
	 */
  public ExecuteResult<String> deletes(Long[] ids);
}
