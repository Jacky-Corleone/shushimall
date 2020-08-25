package com.camelot.mall.service;

/*import com.camelot.goodscenter.dto.SearchDTO;*/


/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
public interface SearchClientService {
	/**
	 * <p>Discription:[根据参数查询所有商品信息]</p>
	 * Created on 2015年2月3日
	 * @return
	 * @author:[周乐]
	 *//*
	public SearchDTO<ItemMainDTO> queryItemListByKeyword(Map<String, Object> params);
	
	*//**
	 * <p>Discription:[根据品牌id查询品牌分页]</p>
	 * Created on 2015年2月4日
	 * @param brandIds 品牌id，以逗号拼接
	 * @return
	 * @author:[周乐]
	 *//*
	public List<ItemBrandDTO> queryBrandListByItemIds(String brandIds);
	
	*//**
	 * <p>Discription:[根据属性id查询属性、属性值列表]</p>
	 * Created on 2015年2月3日
	 * @param ids 商品id
	 * @return
	 * @author:[周乐]
	 *//*
	public Map<String, List<String>> queryItemAttrListByItemIds(String attrIds);
	
	*//**
	 * <p>Discription:[根据品牌id查询品牌分页]</p>
	 * Created on 2015年2月4日
	 * @param page 分页对象
	 * @return
	 * @author:[周乐]
	 *//*
	public Pager<ItemBrandDTO> queryBrandByIdsWithPage(int page, ItemBrandDTO itemBrandDTO);*/
}
