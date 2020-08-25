package com.camelot.mall.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.mall.service.impl.CommonServiceImpl;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.searchcenter.dto.SearchBrand;
import com.camelot.searchcenter.dto.SearchCategory;
import com.camelot.searchcenter.dto.SearchItemAttr;
import com.camelot.searchcenter.dto.SearchItemSku;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.dto.SearchShopDTO;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.util.StationUtil;

@Controller
@RequestMapping("search")
public class SearchController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private CommonServiceImpl commonServiceImpl;
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ShopExportService shopExportService;

	/**
	 * 
	 * @author 马国平 创建时间：2015-6-19 上午10:11:12
	 * @return
	 */
	@RequestMapping("toSearch")
	public String toSearch(Model model,
			@RequestParam(value = "showMenu", required = false, defaultValue = "") String showMenu,
			@RequestParam(value = "searchItemId", required = false, defaultValue = "") String searchItemId,
			@RequestParam(value = "content", required = false, defaultValue = "") String content,
			@RequestParam(value = "searchType", required = false, defaultValue = "") String searchType
			) {
		JSONArray ja = commonServiceImpl.findCategory();
		model.addAttribute("itemList", ja);
		
		// ////控制页面是否默认显示类目菜单
		if (StringUtils.isEmpty(showMenu)) {
			model.addAttribute("showMenu", 0);
		} else {
			model.addAttribute("showMenu", 1);
		}
		model.addAttribute("searchItemId", searchItemId);
		model.addAttribute("content", content.equals("undefined")?"":content);
		model.addAttribute("searchType", searchType);
		return "search/search";
	}

	/**
	 * 点击类目3跳转到相关的产品列表页面
	 * 
	 * @author 马国平 创建时间：2015-6-19 下午3:52:19
	 * @return
	 */
	@RequestMapping("toItemSearch")
	public String toItem3Search() {

		return "";
	}

	@RequestMapping(value = "searchItemByCategory")
	public String searchItemByCategory(Long cid, Model model, HttpServletRequest request) {
		if (null != cid) {
			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));
			SearchItemSkuInDTO inDto = new SearchItemSkuInDTO();
			inDto.setCid(cid);
			Pager<SearchItemSkuOutDTO> pager_SearchItem = new Pager<SearchItemSkuOutDTO>();
			pager_SearchItem.setPage(3);
			pager_SearchItem.setRows(2);
			inDto.setPager(pager_SearchItem);

			logger.info("inDto=====" + JSON.toJSONString(inDto));
			SearchItemSkuOutDTO outDto = searchExportService.searchItemSku(inDto);
			DataGrid<SearchItemSku> itemSkus = outDto.getItemSkus();// 商品列表
			pager_SearchItem.setTotalCount(itemSkus.getTotal().intValue());

			List<SearchBrand> brandList = outDto.getBrands();// 品牌
			List<SearchItemAttr> attrList = outDto.getAttributes();// 商品非销售属性
			List<SearchCategory> categories = outDto.getCategories();// 商品类别

			model.addAttribute("pager", pager_SearchItem);
			// shopId
			List<SearchItemSku> listSearchItemSku = itemSkus.getRows();
			model.addAttribute("itemSkus", listSearchItemSku);
			if (itemSkus.getRows() != null && listSearchItemSku.size() > 0) {
				model.addAttribute("itemSkus_isNull", "false");
			} else {
				model.addAttribute("itemSkus_isNull", "true");
			}
			// 读取店铺名称
			Map<Long, String> map = new HashMap<Long, String>();
			if (itemSkus.getRows() != null && listSearchItemSku.size() > 0) {
				for (SearchItemSku searchItemSku : listSearchItemSku) {
					String valueShopName = map.get(searchItemSku.getShopId());
					if (StringUtils.isEmpty(valueShopName)) {
						ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(searchItemSku.getShopId());
						if (shopInfo != null && shopInfo.getResult() != null) {
							map.put(shopInfo.getResult().getShopId(), shopInfo.getResult().getShopName());
						}
					}

				}
			}
			// 构造店铺数据的json
			JSONArray jsonArray = new JSONArray();
			for (Map.Entry<Long, String> entry : map.entrySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("shopId", entry.getKey());
				jsonObject.put("shopName", entry.getValue());
				jsonArray.add(jsonObject);
			}
			model.addAttribute("brandList", brandList);
			model.addAttribute("attrList", attrList);
			model.addAttribute("categories", categories);
			model.addAttribute("jsonArrayShop", jsonArray);
			model.addAttribute("cid", cid);
			model.addAttribute("orderSort", inDto.getOrderSort());

			logger.info("pager=====" + JSON.toJSONString(pager_SearchItem));
			logger.info("itemSkus=====" + JSON.toJSONString(itemSkus.getRows()));
			logger.info("brandList=====" + JSON.toJSONString(brandList));
			logger.info("attrList=====" + JSON.toJSONString(attrList));
			logger.info("categories=====" + JSON.toJSONString(categories));
			logger.info("jsonArrayShop=====" + JSON.toJSONString(jsonArray));
		} else {
			model.addAttribute("pager", null);
			model.addAttribute("itemSkus", null);
			model.addAttribute("brandList", null);
			model.addAttribute("attrList", null);
			model.addAttribute("categories", null);
			model.addAttribute("jsonArrayShop", null);
			model.addAttribute("cid", null);
			model.addAttribute("orderSort", null);
		}
		return "search/searchResultPage";
	}

	@RequestMapping(value = "/searchItem")
	@ResponseBody
	public String searchItem(Long brandId,
			String attributes,
			Long cid,
			String areaCode,
			@RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(value = "pageSize", required = false, defaultValue = "8") Integer pageSize,
			Integer orderSort,
			String keyword,
			HttpServletRequest request) {
		Map<String,Object> modelMap=new HashMap<String,Object>();
		//if (StringUtils.isNotBlank(keyword) || null != cid) {
			if (true) {
			modelMap.put("gix", SysProperties.getProperty("ftp_server_dir"));
			SearchItemSkuInDTO inDto = new SearchItemSkuInDTO();
			String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
			String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);

			if (uid != null && !"".equals(uid)) {
				inDto.setBuyerId(Long.valueOf(uid));
				modelMap.put("userId", uid);
			} else {
				modelMap.put("userId", "");
			}

			inDto.setAreaCode(region);
			if (StringUtils.isNotEmpty(keyword)) {
				inDto.setKeyword(keyword);
			}
			if (null != brandId) {
				List<Long> brandIds = new ArrayList<Long>();
				brandIds.add(brandId);
				inDto.setBrandIds(brandIds);
			}
			if (StringUtils.isNotEmpty(attributes)) {
				inDto.setAttributes(attributes);
			}
			if (null != cid) {
				inDto.setCid(cid);
			}
			if (StringUtils.isNotEmpty(areaCode)) {
				inDto.setAreaCode(areaCode);
			}
			Pager<SearchItemSkuOutDTO> pager_SearchItem = new Pager<SearchItemSkuOutDTO>();
			pager_SearchItem.setRows(pageSize);
			if (null != page) {
				pager_SearchItem.setPage(page);
			}
			if (orderSort != null) {
				// 1 时间升序 2时间降序
				inDto.setOrderSort(orderSort);
			} else {
				// 默认降序排序
				inDto.setOrderSort(2);
			}
			inDto.setPager(pager_SearchItem);

			logger.debug("inDto=====" + JSON.toJSONString(inDto));
			SearchItemSkuOutDTO outDto = searchExportService.searchItemSku(inDto);
			DataGrid<SearchItemSku> itemSkus = outDto.getItemSkus();// 商品列表
			pager_SearchItem.setTotalCount(itemSkus.getTotal().intValue());

			List<SearchBrand> brandList = outDto.getBrands();// 品牌
			List<SearchItemAttr> attrList = outDto.getAttributes();// 商品非销售属性
			List<SearchCategory> categories = outDto.getCategories();// 商品类别
			modelMap.put("pager",JSON.toJSON( pager_SearchItem));

			// shopId
			List<SearchItemSku> listSearchItemSku = itemSkus.getRows();
				for(SearchItemSku sis :listSearchItemSku){
					sis.setDescribeUrl(null);
				}
			modelMap.put("itemSkus", listSearchItemSku);
			if (itemSkus.getRows() != null && listSearchItemSku.size() > 0) {
				modelMap.put("itemSkus_isNull", "false");
			} else {
				modelMap.put("itemSkus_isNull", "true");
			}
			// 读取店铺名称
			Map<Long, String> map = new HashMap<Long, String>();
			if (itemSkus.getRows() != null && listSearchItemSku.size() > 0) {
				for (SearchItemSku searchItemSku : listSearchItemSku) {
					String valueShopName = map.get(searchItemSku.getShopId());
					if (StringUtils.isEmpty(valueShopName)) {
						ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(searchItemSku.getShopId());
						if (shopInfo != null && shopInfo.getResult() != null) {
							map.put(shopInfo.getResult().getShopId(), shopInfo.getResult().getShopName());
						}
					}

				}
			}
			// 构造店铺数据的json
			JSONArray jsonArray = new JSONArray();
			for (Map.Entry<Long, String> entry : map.entrySet()) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("shopId", entry.getKey());
				jsonObject.put("shopName", entry.getValue());
				// 获取客服站点
				String stationId = StationUtil.getStationIdByShopId(entry.getKey());
				if (!StringUtils.isBlank(stationId)) {
					jsonObject.put("stationId", stationId);
				} else {
					jsonObject.put("stationId", "无站点");
				}
				jsonArray.add(jsonObject);
			}
			modelMap.put("brandList", JSON.toJSON( brandList));
			modelMap.put("attrList", JSON.toJSON( attrList));
			modelMap.put("categories", JSON.toJSON( categories));
			modelMap.put("jsonArrayShop", jsonArray);
			modelMap.put("keyword", keyword);
			modelMap.put("brandId", brandId);
			modelMap.put("attributes", attributes);
			modelMap.put("cid", cid);
			modelMap.put("orderSort", JSON.toJSON( inDto.getOrderSort()));

			logger.debug("pager=====" + JSON.toJSONString(pager_SearchItem));
			logger.debug("itemSkus=====" + JSON.toJSONString(itemSkus.getRows()));
			logger.debug("brandList=====" + JSON.toJSONString(brandList));
			logger.debug("attrList=====" + JSON.toJSONString(attrList));
			logger.debug("categories=====" + JSON.toJSONString(categories));
			logger.debug("jsonArrayShop=====" + JSON.toJSONString(jsonArray));
		} else {
			modelMap.put("pager", null);
			modelMap.put("itemSkus", null);
			modelMap.put("brandList", null);
			modelMap.put("attrList", null);
			modelMap.put("categories", null);
			modelMap.put("jsonArrayShop", null);
			modelMap.put("keyword", null);
			modelMap.put("brandId", null);
			modelMap.put("attributes", null);
			modelMap.put("cid", null);
			modelMap.put("orderSort", null);
		}
		modelMap.put("orderSort",orderSort);
		return JSON.toJSONString(modelMap);
	}

	/**
	 *
	 * <p>Discription:[店铺列表查询]</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "/searchShop")
	@ResponseBody
	public String searchItem(String keyword, Integer page, Integer orderSort,HttpServletRequest request) {
		Long buyerId=null;
		Map<String,Object> modelMap=new HashMap<String, Object>();
//		if(StringUtils.isNotBlank(keyword)){
			Pager<SearchShopDTO> pager = new Pager<SearchShopDTO>();
			if(page!=null){
				pager.setPage(page);
			}
			String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
			if(uid != null && !"".equals(uid)){
				buyerId=Long.valueOf(uid);
			}
			DataGrid<SearchShopDTO> dg = searchExportService.searchShop(keyword, pager, orderSort,buyerId,null);
			for(SearchShopDTO ssd:dg.getRows()){
				ssd.setRecommendItems(null);
			}

			pager.setTotalCount(dg.getTotal().intValue());
			pager.setRecords(dg.getRows());

			modelMap.put("pager", pager);
			modelMap.put("keyword", keyword);
			modelMap.put("orderSort", orderSort);
		/*}else{
			modelMap.put("pager", new Pager<SearchShopDTO>());
			modelMap.put("keyword", keyword);
			modelMap.put("orderSort", orderSort);
		}*/
		modelMap.put("gix", SysProperties.getProperty("ftp_server_dir"));
		return JSON.toJSONString(modelMap);
	}
}
