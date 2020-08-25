package com.camelot.mall.service.impl;

import org.springframework.stereotype.Service;

import com.camelot.mall.service.SearchClientService;
/*import com.camelot.goodscenter.dto.SearchDTO;*/

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: xxx@camelotchina.com">作者中文名</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Service("searchClientService")
public class SearchClientServiceImpl implements SearchClientService{
/*	@Resource
	private SearchService searchService;

	*//**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年2月5日
	 * @param keyword
	 * @param page
	 * @return
	 * @author:[创建者中文名字]
	 *//*
	@Override
	public SearchDTO<ItemMainDTO> queryItemListByKeyword(Map<String, Object> params) {
		SearchDTO<ItemMainDTO> searchDTO = searchService.queryItemListByKeyword(params);
		//Pager<ItemMainDTO> pager =new Pager<ItemMainDTO>();
		return searchDTO;
	}

	*//**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年2月5日
	 * @param brandIds
	 * @return
	 * @author:[创建者中文名字]
	 *//*
	@Override
	public List<ItemBrandDTO> queryBrandListByItemIds(String brandIds) {
		//return searchService.queryBrandListByItemIds(brandIds);
		return null;
	}

	*//**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年2月5日
	 * @param attrIds
	 * @return
	 * @author:[创建者中文名字]
	 *//*
	@Override
	public Map<String, List<String>> queryItemAttrListByItemIds(String attrIds) {
		//return searchService.queryItemAttrListByItemIds(attrIds);
		return null;
	}

	*//**
	 * <p>Discription:[方法功能中文描述]</p>
	 * Created on 2015年2月5日
	 * @param page
	 * @param itemBrandDTO
	 * @return
	 * @author:[创建者中文名字]
	 *//*
	@Override
	public Pager<ItemBrandDTO> queryBrandByIdsWithPage(int page,
			ItemBrandDTO itemBrandDTO) {
		//return searchService.queryBrandByIdsWithPage(page, itemBrandDTO);
		return null;
	}

	@Override
	public Pager<ItemBrandClientDTO> queryBrandByIdsWithPage(int page, ItemBrandClientDTO clientDTO) {
		ItemBrandDTO itemBrandDTO = new ItemBrandDTO();
		Pager<ItemBrandDTO> pager = searchService.queryBrandByIdsWithPage(page, itemBrandDTO);
		
		List<ItemBrandClientDTO> records = new ArrayList<ItemBrandClientDTO>();
		for(int i=0; null!=pager.getRecords() && i<pager.getRecords().size(); i++){
			ItemBrandDTO dto = pager.getRecords().get(i);
			ItemBrandClientDTO _clientDTO = new ItemBrandClientDTO();
			_clientDTO.setBrandId(dto.getBrandId());
			_clientDTO.setBrandLogoUrl(dto.getBrandLogoUrl());
			_clientDTO.setBrandName(dto.getBrandName());
			_clientDTO.setBrandStatus(dto.getBrandStatus());
			_clientDTO.setCreated(dto.getCreated());
			_clientDTO.setModified(dto.getModified());
			
			records.add(_clientDTO);
		}
		
		Pager<ItemBrandClientDTO> retVal = new Pager<ItemBrandClientDTO>(pager.getPage(), pager.getRows());
		retVal.setTotalCount(pager.getTotalCount());
		retVal.setRecords(records);
		
		return retVal;
	}*/
}
