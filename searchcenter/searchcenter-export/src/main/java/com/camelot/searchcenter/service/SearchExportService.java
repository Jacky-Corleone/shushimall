package com.camelot.searchcenter.service;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchShopDTO;

public interface SearchExportService {

	/**
	 * 
	 * <p>Discription:[根据关键字搜索店铺]</p>
	 * Created on 2015-4-17
	 * @param keyword 店铺关键字
	 * @param pager
	 * @param sort 1 修改时间升序(默认) 2 修改时间降序 3 评分升序 4评分降序 5销量升序 6销量降序
	 * @param buyerId 卖家Id
	 * @return
	 * @author:wangcs
	 */
	public DataGrid<SearchShopDTO> searchShop(String keyword,Pager pager,Integer sort,Long buyerId,String areaCode);
	
	/**
	 * 
	 * <p>Description: [根据平台ID和关键字搜索店铺]</p>
	 * Created on 2015年9月2日
	 * @param platformId 平台ID
	 * @param keyword 店铺关键字
	 * @param pager
	 * @param sort 修改时间升序(默认) 2 修改时间降序 3 评分升序 4评分降序 5销量升序 6销量降序
	 * @param buyerId 卖家Id
	 * @return
	 * @author:[宋文斌]
	 */
	public DataGrid<SearchShopDTO> searchShopByPlatformId(Integer platformId, String keyword, Pager pager, Integer sort, Long buyerId,String areaCode);
	
	/**
	 * 
	 * <p>Discription:[商品搜索]</p>
	 * Created on 2015-3-5
	 * @param keyword
	 * @return
	 * @author:wangcs
	 */
//	public SearchItemOutDTO searchItem(SearchItemInDTO inDto);
	
	/**
	 * 
	 * <p>Discription:[商品搜索，具体到SKU]</p>
	 * Created on 2015-3-19
	 * @param inDTO
	 * @return
	 * @author:wangcs
	 */
	public SearchItemSkuOutDTO searchItemSku(SearchItemSkuInDTO inDTO);
	
}
