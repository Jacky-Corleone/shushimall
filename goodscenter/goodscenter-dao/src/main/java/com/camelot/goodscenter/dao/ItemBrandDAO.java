package com.camelot.goodscenter.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.camelot.goodscenter.domain.ItemBrand;
import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.orm.BaseDAO;

/**
 * <p>Description: [类目品牌操作类]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: xxx@camelotchina.com">杨阳</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
public interface ItemBrandDAO extends BaseDAO<ItemBrand>{
	/**
	 * <p>Discription:[根据品牌id查询品牌列表]</p>
	 * Created on 2015年2月4日
	 * @param brandIds 品牌id，以逗号拼接
	 * @return
	 * @author:[周乐]
	 */
	public List<ItemBrandDTO> queryBrandByIds(List<Long> brandIds);
	
	/**
	 * <p>Discription:[根据品牌id查询品牌分页]</p>
	 * Created on 2015年2月4日
	 * @param rows 每页显示记录数
	 * @return pageOffset 当前页起始记录
	 * @author:[周乐]
	 */
	public List<ItemBrandDTO> queryBrandByIdsWithPage(@Param("page") Pager<ItemBrandDTO> page, @Param("entity") ItemBrandDTO dto);
	
	/**
	 * <p>Discription:[查询品牌数据的数量]</p>
	 * Created on 2015年2月5日
	 * @param dto
	 * @return
	 * @author:[创建者中文名字]
	 */
	public long queryBrandCounts(@Param("entity") ItemBrandDTO dto);

	/**
	 * <p>Discription:[查询所有品牌列表]</p>
	 * Created on 2015年2月4日
	 * @param page
	 * @return
	 * @author:[创建者中文名字]
	 */
	public List<ItemBrandDTO> queryItemBrandAllList ( @Param("page") Pager page);
	
	/**
	 * <p>Discription:[根据类目查询类目下的品牌]</p>
	 * Created on 2015年2月8日
	 * @return
	 * @author:[杨阳]
	 */
	public List<ItemBrandDTO> queryItemBrandList(@Param("entity") BrandOfShopDTO brandOfShopDTO);
	/**
	 * 
	 * <p>Discription:[查询品牌列表]</p>
	 * Created on 2015-4-17
	 * @param itemBrandDTO
	 * @param page
	 * @return
	 * @author:yuht
	 */
	public List<ItemBrandDTO> queryBrandList(@Param("entity")ItemBrandDTO itemBrandDTO,@Param("page")Pager page);
	/**
	 * 
	 * <p>Discription:[查询数量]</p>
	 * Created on 2015-4-17
	 * @param itemBrandDTO
	 * @return
	 * @author:yuht
	 */
	public Long queryCountBrandList(@Param("entity")ItemBrandDTO itemBrandDTO);
	/**
	 * 
	 * <p>Discription:[修改有效状态]</p>
	 * Created on 2015-5-12
	 * @param brandId
	 * @author:yuht
	 */
	public void updateStatus(Long brandId);

	/**
	 * 
	 * <p>Discription:[物理删除类目品牌关系]</p>
	 * Created on 2015-6-23
	 * @param cid 三级类目ID
	 * @param brandId 品牌ID
	 * @author:wangcs
	 */
	public void deleteItemBrand(@Param("cid") Long cid, @Param("brandId") Long brandId);
	
	/**
	 * <p>Discription:[根据品牌id查询出该品牌是否类目关联]</p>
	 * @param itemBrandDTO
	 * @return
	 */
	public List<ItemBrandDTO> queryBrandCategoryById(@Param("brandId")Long brandId);
}
