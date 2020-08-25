package com.camelot.goodscenter.service;

import java.util.List;

import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;

/**
 * <p>Description: []</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemBrandExportService {
	
	/**
	 * <p>Discription:[类目下品牌添加]</p>
	 * Created on 2015年2月3日
	 * @param itemBrandDTO
	 * @return
	 * @author:[杨阳]
	 */
	public ExecuteResult<ItemBrandDTO> addItemBrand (ItemBrandDTO itemBrandDTO);
	
	/**
	 * <p>Discription:[品牌信息修改]</p>
	 * Created on 2015年2月4日
	 * @param itemBrandDTO
	 * @return
	 * @author:[杨阳]
	 */
	public ExecuteResult<ItemBrandDTO> modifyItemBrand(ItemBrandDTO itemBrandDTO);
	
	/**
	 * <p>Discription:[查询所有品牌列表]</p>
	 * Created on 2015年2月4日
	 * @param page
	 * @return
	 * @author:[杨阳]
	 */
	public DataGrid<ItemBrandDTO> queryItemBrandAllList(Pager page);
	
	/**
	 * 
	 * <p>Discription:[根据ID组查询品牌]</p>
	 * Created on 2015-3-10
	 * @param ids
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<List<ItemBrandDTO>> queryItemBrandByIds(Long... ids);
	
	/**
	 * <p>Discription:[根据类目查询品牌列表]</p>
	 * Created on 2015年2月8日
	 * @return
	 * @author:[杨阳]
	 */
	public DataGrid<ItemBrandDTO> queryItemBrandList(BrandOfShopDTO brandOfShopDTO);

	/**
	 * 
	 * <p>Discription:[批量添加品牌和类目关系]</p>
	 * Created on 2015-2-28
	 * @param brand 其中包含二级类目ID，三级类目ID和品牌ID数组
	 * @return
	 * @author:[wangcunshan]
	 */
	public ExecuteResult<ItemBrandDTO> addCategoryBrandBatch(ItemBrandDTO brand); 
	
	/**
	 * 
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015-4-17
	 * @param itemBrandDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<DataGrid<ItemBrandDTO>> queryBrandList(ItemBrandDTO itemBrandDTO,Pager page);
	
	/**
	 * 
	 * <p>Discription:[品牌删除]</p>
	 * Created on 2015-5-12
	 * @param brandId
	 * @return
	 * @author:yuht
	 */
	public ExecuteResult<String> deleteBrandById(Long brandId);
	
	/**
	 * 
	 * <p>Discription:[删除类目品牌关系]</p>
	 * Created on 2015-6-23
	 * @param cid  三级类目ID
	 * @param brandId 品牌ID
	 * @return
	 * @author:wangcs
	 */
	public ExecuteResult<String> deleteCategoryBrand(Long cid,Long brandId);
	
	/**
	 * 删除品牌，之前的删除品牌有问题，只判断了商品下有没有品牌，并没有判断和类目关系
	 * @param brandId
	 * @return
	 */
	public ExecuteResult<String> deleteBrand(Long brandId);
}
