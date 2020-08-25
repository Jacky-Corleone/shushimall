package com.camelot.mall.goodscenter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.enums.HouseTypeEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.maketcenter.dto.PromotionFullReduction;
import com.camelot.maketcenter.dto.PromotionInDTO;
import com.camelot.maketcenter.dto.PromotionMarkdown;
import com.camelot.maketcenter.dto.PromotionOutDTO;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.searchcenter.dto.SearchBrand;
import com.camelot.searchcenter.dto.SearchCategory;
import com.camelot.searchcenter.dto.SearchItemAttr;
import com.camelot.searchcenter.dto.SearchItemSku;
import com.camelot.searchcenter.dto.SearchItemSkuInDTO;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.searchcenter.dto.SearchShopDTO;
import com.camelot.searchcenter.service.SearchExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;


/**
 * <p>Description: [搜索页面控制类]</p>
 * Created on 2015年2月4日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/searchController")
public class SearchController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private SearchExportService searchExportService;
	@Resource
	private ShopExportService shopExportService;
    @Resource
    private UserExportService userService;
    @Resource
	private ItemCategoryService itemCategoryService;
    @Resource
	private QqCustomerService qqCustomerService;
    @Resource
    private PromotionService promotionService;
	/**
	 *
	 * <p>Discription:[商品列表查询:头部搜索框、商品搜索结果页会调用]</p>
	 * Created on 2015年3月4日
	 * @param brandId 品牌id
	 * @param keyword 关键词
	 * @param page	分页
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "/searchItem")
	public String searchItem(Long brandId, String attributes, Long cid, String areaCode, Integer page,Integer orderSort,
				String keyword, String addSourcesStr, String houseType, String minPrice, String maxPrice,
				Model model, HttpServletRequest request) {
		if(null != keyword || null != cid){
			SearchItemSkuInDTO inDto = new SearchItemSkuInDTO();
			String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
			Long uid = WebUtil.getInstance().getUserId(request);

			if(uid != null){
                inDto.setBuyerId(uid);
                model.addAttribute("userId", uid);
            }else{
                model.addAttribute("userId", "");
            }

			inDto.setAreaCode(region);
			if(StringUtils.isNotEmpty(keyword)){
				inDto.setKeyword(keyword);
			}
			if(null != brandId){
				List<Long> brandIds = new ArrayList<Long>();
				brandIds.add(brandId);
				inDto.setBrandIds(brandIds);
			}
			if(StringUtils.isNotEmpty(attributes)){
				inDto.setAttributes(attributes);
			}
			if(null != cid){
				inDto.setCid(cid);
			}
			if(StringUtils.isNotEmpty(areaCode)){
				inDto.setAreaCode(areaCode);
			}
			Pager<SearchItemSkuOutDTO> pager_SearchItem = new Pager<SearchItemSkuOutDTO>();
			pager_SearchItem.setRows(28);
			if(null != page){
				pager_SearchItem.setPage(page);
			}
			if(orderSort != null){
				//1 时间升序 2时间降序
				inDto.setOrderSort(orderSort);
			}else{
				//默认降序排序
				inDto.setOrderSort(2);
			}
			inDto.setPager(pager_SearchItem);
			if (StringUtils.isNotEmpty(addSourcesStr)) {
				inDto.setAddSources(Arrays.asList(addSourcesStr.split(",")));
			}
			if (StringUtils.isNotEmpty(houseType)) {
				inDto.setHouseType(houseType);
			}
			if (StringUtils.isNotEmpty(minPrice)) {
				inDto.setMinPrice(new BigDecimal(minPrice));
			}
			if (StringUtils.isNotEmpty(maxPrice)) {
				inDto.setMaxPrice(new BigDecimal(maxPrice));
			}

			LOG.debug("inDto====="+JSON.toJSONString(inDto));
			SearchItemSkuOutDTO outDto = searchExportService.searchItemSku(inDto);
			DataGrid<SearchItemSku> itemSkus = outDto.getItemSkus();//商品列表
			pager_SearchItem.setTotalCount(itemSkus.getTotal().intValue());

			List<SearchBrand> brandList = outDto.getBrands();//品牌
			List<SearchItemAttr> attrList = outDto.getAttributes();//商品非高级非销售属性
			List<SearchItemAttr> seniorAttrList = outDto.getSeniorAttributes();//商品高级非销售属性
			JSONArray array = new JSONArray();
			for(SearchItemAttr list:seniorAttrList){
				JSONObject searchItemAttr = JSON.parseObject(JSON.toJSONString(list));
				array.add(searchItemAttr);
			}
			List<SearchCategory> categories = outDto.getCategories();//商品类别
			model.addAttribute("pager", pager_SearchItem);

			//shopId
			List<SearchItemSku> listSearchItemSku = itemSkus.getRows();
			
			if(itemSkus.getRows()!=null && listSearchItemSku.size()>0){
				model.addAttribute("itemSkus_isNull", "false");
			}else{
				model.addAttribute("itemSkus_isNull", "true");
			}
			Map<Long, String> map = new HashMap<Long, String>();
			//满减
			Map<Long, Long> fullMap = new HashMap<Long, Long>();
			//促销 直降
            Map<Long, BigDecimal> downMap = new HashMap<Long, BigDecimal>();
			if(itemSkus.getRows()!=null && listSearchItemSku.size()>0){
				for(SearchItemSku searchItemSku : listSearchItemSku){
					//读取店铺名称
					String valueShopName = map.get(searchItemSku.getShopId());
					if(StringUtils.isEmpty(valueShopName)){
						ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(searchItemSku.getShopId());
						if(shopInfo!=null && shopInfo.getResult()!=null){
							map.put(shopInfo.getResult().getShopId(), shopInfo.getResult().getShopName());
						}
					}
					//满减
					fullMap.put(searchItemSku.getItemId(),searchItemSku.getShopId());
					//折扣
					PromotionInDTO promotInDTO = new PromotionInDTO();
					promotInDTO.setItemId(searchItemSku.getItemId());
					promotInDTO.setShopId(searchItemSku.getShopId());
					promotInDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
					promotInDTO.setType(1);//查询折扣
					ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(promotInDTO, null);
					if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
						List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
			 			for (PromotionOutDTO pro : listPromotionOutDTO) {
			 				if (pro.getPromotionInfo().getShopId()==0) {//平台直降
			 					PromotionMarkdown markdown = pro.getPromotionMarkdown();
				 				BigDecimal disPrice = new BigDecimal(0);
				 				if(searchItemSku.getSkuPrice()==null){
				 					searchItemSku.setSkuPrice(BigDecimal.ZERO);
				 				}
				 				//计算促销价格
				 				if (markdown.getMarkdownType() == 1) {//百分比满减
				 					disPrice = searchItemSku.getSkuPrice().multiply(markdown.getDiscountPercent()).setScale(2, BigDecimal.ROUND_CEILING);
								}else {//直降固定金额
									disPrice = searchItemSku.getSkuPrice().subtract(markdown.getPromotionPrice());
								}
				 				if(disPrice.compareTo(new BigDecimal(0))<=0){
				 					disPrice=BigDecimal.ZERO;
						    	}
				 				//设置SKU价格 为促销价
				 				//searchItemSku.setSkuPrice(disPrice);
				 				downMap.put(searchItemSku.getSkuId(), disPrice);
			 				}else {//店铺直降
			 					PromotionMarkdown markdown = pro.getPromotionMarkdown();
				 				BigDecimal disPrice = new BigDecimal(0);
				 				if(searchItemSku.getSkuPrice()==null){
				 					searchItemSku.setSkuPrice(BigDecimal.ZERO);
				 				}
				 				//计算促销价格
				 				if (markdown.getMarkdownType() == 1) {//百分比满减
				 					disPrice = searchItemSku.getSkuPrice().multiply(markdown.getDiscountPercent()).setScale(2, BigDecimal.ROUND_CEILING);
								}else {//直降固定金额
									disPrice = searchItemSku.getSkuPrice().subtract(markdown.getPromotionPrice());
								}
				 				if(disPrice.compareTo(new BigDecimal(0))<=0){
				 					disPrice=BigDecimal.ZERO;
						    	}
				 				//设置SKU价格 为促销价
				 				//searchItemSku.setSkuPrice(disPrice);
				 				downMap.put(searchItemSku.getSkuId(), disPrice);
							}
			 			}
					}
				}
			}
			
			model.addAttribute("itemSkus", listSearchItemSku);
			//查询得到商品满减信息
			JSONArray fulljsonArray = fullReduction(fullMap);
			//构造店铺数据的json
			JSONArray jsonArray = getShopInfo(map);
			//配置搜索页的SEO
			BaseTDKConfigDTO baseTDKConfigDTO=new BaseTDKConfigDTO();
			String titleDelimiter = StringUtils.isNotEmpty(keyword)?" - ":"";
			baseTDKConfigDTO.setTitle(keyword + titleDelimiter +"舒适100");
			baseTDKConfigDTO.setDescription("在舒适100找到了包含了“"+keyword+"”等类型的商品");
			baseTDKConfigDTO.setKeywords(keyword);
			model.addAttribute("baseTDKConfigDTO7", baseTDKConfigDTO);
			
			model.addAttribute("brandList", brandList);
			model.addAttribute("attrList", attrList);
			model.addAttribute("seniorAttrList", array);
			model.addAttribute("categories", categories);
			model.addAttribute("jsonArrayShop", jsonArray);
			model.addAttribute("downMap", downMap);
			model.addAttribute("fulljsonArray", fulljsonArray);
			model.addAttribute("keyword", keyword);
			model.addAttribute("brandId",brandId);
			model.addAttribute("attributes",attributes);
			model.addAttribute("cid",cid);
			model.addAttribute("orderSort",inDto.getOrderSort());
			model.addAttribute("addSourcesStr", addSourcesStr);
			model.addAttribute("minPrice", minPrice);
			model.addAttribute("maxPrice", maxPrice);
			model.addAttribute("houseType", houseType);
			model.addAttribute("houseTypeItems", HouseTypeEnum.values());

			LOG.debug("pager====="+JSON.toJSONString(pager_SearchItem));
			LOG.debug("itemSkus====="+JSON.toJSONString(itemSkus.getRows()));
			LOG.debug("brandList====="+JSON.toJSONString(brandList));
			LOG.debug("attrList====="+JSON.toJSONString(attrList));
			LOG.debug("categories====="+JSON.toJSONString(categories));
			LOG.debug("jsonArrayShop====="+JSON.toJSONString(jsonArray));
		}else{
			model.addAttribute("pager", null);
			model.addAttribute("itemSkus", null);
			model.addAttribute("brandList", null);
			model.addAttribute("attrList", null);
			model.addAttribute("categories", null);
			model.addAttribute("jsonArrayShop", null);
			model.addAttribute("keyword", null);
			model.addAttribute("brandId",null);
			model.addAttribute("attributes",null);
			model.addAttribute("cid",null);
			model.addAttribute("orderSort",null);
		}
		return "/goodscenter/search/productSearch";
	}
	/**
	 *
	 * <p>Discription:[传入三级类目id 查询]</p>
	 * Created on 2015年3月13日
	 * @param cid 第三级类目Id
	 * @param model
	 * @param request
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "/searchItemByCategory")
	public String searchItemByCategory(Long cid, Model model, HttpServletRequest request) {
        String token = LoginToken.getLoginToken(request);
        String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
        if(StringUtils.isBlank(token)){
            model.addAttribute("userId", "");
        }else{
			RegisterDTO registerDTO = userService.getUserByRedis(token);
			if (registerDTO == null) {
				model.addAttribute("userId", "");
			} else {
				model.addAttribute("userId", registerDTO.getUid());
			}
        }
		if(null != cid){
			SearchItemSkuInDTO inDto = new SearchItemSkuInDTO();
			inDto.setCid(cid);
			inDto.setAreaCode(region);
			Pager<SearchItemSkuOutDTO> pager_SearchItem = new Pager<SearchItemSkuOutDTO>();
			pager_SearchItem.setRows(28);
			inDto.setPager(pager_SearchItem);

			LOG.debug("inDto====="+JSON.toJSONString(inDto));
			SearchItemSkuOutDTO outDto = searchExportService.searchItemSku(inDto);
			DataGrid<SearchItemSku> itemSkus = outDto.getItemSkus();//商品列表
			pager_SearchItem.setTotalCount(itemSkus.getTotal().intValue());

			List<SearchBrand> brandList = outDto.getBrands();//品牌
			List<SearchItemAttr> attrList = outDto.getAttributes();//商品非高级非销售属性
			List<SearchItemAttr> seniorAttrList = outDto.getSeniorAttributes();//商品高级非销售属性
			JSONArray array = new JSONArray();
			for(SearchItemAttr list:seniorAttrList){
				JSONObject searchItemAttr = JSON.parseObject(JSON.toJSONString(list));
				array.add(searchItemAttr);
			}
			List<SearchCategory> categories = outDto.getCategories();//商品类别

			model.addAttribute("pager", pager_SearchItem);
			//shopId
			List<SearchItemSku> listSearchItemSku = itemSkus.getRows();
			if(itemSkus.getRows()!=null && listSearchItemSku.size()>0){
				model.addAttribute("itemSkus_isNull", "false");
			}else{
				model.addAttribute("itemSkus_isNull", "true");
			}
			Map<Long, String> map = new HashMap<Long, String>();
			//满减
			Map<Long, Long> fullMap = new HashMap<Long, Long>();
			//促销 直降
            Map<Long, BigDecimal> downMap = new HashMap<Long, BigDecimal>();
			if(itemSkus.getRows()!=null && listSearchItemSku.size()>0){
				for(SearchItemSku searchItemSku : listSearchItemSku){
					//读取店铺名称
					String valueShopName = map.get(searchItemSku.getShopId());
					if(StringUtils.isEmpty(valueShopName)){
						ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(searchItemSku.getShopId());
						if(shopInfo!=null && shopInfo.getResult()!=null){
							map.put(shopInfo.getResult().getShopId(), shopInfo.getResult().getShopName());
						}
					}
					//满减
					fullMap.put(searchItemSku.getItemId(),searchItemSku.getShopId());
					//折扣
					PromotionInDTO promotInDTO = new PromotionInDTO();
					promotInDTO.setItemId(searchItemSku.getItemId());
					promotInDTO.setShopId(searchItemSku.getShopId());
					promotInDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
					promotInDTO.setType(1);//查询折扣
					ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(promotInDTO, null);
					if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
						List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
			 			for (PromotionOutDTO pro : listPromotionOutDTO) {
			 				PromotionMarkdown markdown = pro.getPromotionMarkdown();
			 				BigDecimal disPrice = new BigDecimal(0);
			 				if(searchItemSku.getSkuPrice()==null){
			 					searchItemSku.setSkuPrice(BigDecimal.ZERO);
			 				}
			 				//计算促销价格
			 				if (markdown.getMarkdownType() == 1) {//百分比满减
			 					disPrice = searchItemSku.getSkuPrice().multiply(markdown.getDiscountPercent()).setScale(2, BigDecimal.ROUND_CEILING);
							}else {//直降固定金额
								disPrice = searchItemSku.getSkuPrice().subtract(markdown.getPromotionPrice());
							}
			 				if(disPrice.compareTo(new BigDecimal(0))<=0){
			 					disPrice=BigDecimal.ZERO;
					    	}
			 				//设置SKU价格 为促销价
			 				//searchItemSku.setSkuPrice(disPrice);
			 				downMap.put(searchItemSku.getSkuId(), disPrice);
			 			}
					}
				}
			}
			
			model.addAttribute("itemSkus", listSearchItemSku);
			//查询得到商品满减信息
			JSONArray fulljsonArray = fullReduction(fullMap);
			//构造店铺数据的json
			JSONArray jsonArray = getShopInfo(map);
			
			//配置商品分类页的SEO
			//当前分类名称_子分类一_子分类二。。。_子分类N – 舒适100
			//当前分类名称,子分类一,子分类二…子分类N
			//舒适100专业提供分类名称一,分类名称二,分类名称三,。。。分类名称N的最新报价、促销、评论、导购、图片等相关信息!			
			ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascade = itemCategoryService.queryParentCategoryList(cid);
			List<ItemCatCascadeDTO> catCascade = itemCatCascade.getResult();

			String CategoryName="";
			String SeoKeyword="";
			for(Iterator<ItemCatCascadeDTO> i=catCascade.iterator(); i.hasNext();)
			{
				ItemCatCascadeDTO one=i.next();
				if(null != one.getCname()){
					CategoryName+=one.getCname()+"_";
					SeoKeyword += one.getCname()+",";
				   }
				 	for(Iterator<ItemCatCascadeDTO> CategoryName1=one.getChildCats().iterator(); CategoryName1.hasNext();){
				 		ItemCatCascadeDTO two=CategoryName1.next();
				 		if(null != one.getCname()){
				 			CategoryName+=two.getCname()+"_";
				 			SeoKeyword+=two.getCname()+",";
				 		}
				 			for(Iterator<ItemCatCascadeDTO> CategoryName2=two.getChildCats().iterator(); CategoryName2.hasNext();){
				 					ItemCatCascadeDTO three =CategoryName2.next();
				 					CategoryName+=three.getCname();
				 					SeoKeyword+=two.getCname();
				 			}
				}
			}
			
			
			//配置分类页的SEO
			BaseTDKConfigDTO  baseTDKConfigDTO=new BaseTDKConfigDTO();
			baseTDKConfigDTO.setTitle(CategoryName+" - 舒适100");
			baseTDKConfigDTO.setDescription("舒适100专业提供"+CategoryName+"的最新报价、促销、评论、导购、图片等相关信息");
			baseTDKConfigDTO.setKeywords("舒适100为您找到了"+SeoKeyword+"相关商品，您可以继续搜索您感兴趣的商品。");
			model.addAttribute("baseTDKConfigDTO7", baseTDKConfigDTO);
//			model.addAttribute("shopEvaluationResult",jsonArray );
			
			model.addAttribute("brandList", brandList);
			model.addAttribute("attrList", attrList);
			model.addAttribute("seniorAttrList", array);
			model.addAttribute("categories", categories);
			model.addAttribute("jsonArrayShop", jsonArray);
			model.addAttribute("downMap", downMap);
			model.addAttribute("fulljsonArray", fulljsonArray);
			model.addAttribute("cid",cid);
			model.addAttribute("catCascade", catCascade);
			model.addAttribute("orderSort",inDto.getOrderSort());

			LOG.debug("pager====="+JSON.toJSONString(pager_SearchItem));
			LOG.debug("itemSkus====="+JSON.toJSONString(itemSkus.getRows()));
			LOG.debug("brandList====="+JSON.toJSONString(brandList));
			LOG.debug("attrList====="+JSON.toJSONString(attrList));
			LOG.debug("categories====="+JSON.toJSONString(categories));
			LOG.debug("jsonArrayShop====="+JSON.toJSONString(jsonArray));
		}else{
			model.addAttribute("pager", null);
			model.addAttribute("itemSkus", null);
			model.addAttribute("brandList", null);
			model.addAttribute("attrList", null);
			model.addAttribute("categories", null);
			model.addAttribute("jsonArrayShop", null);
			model.addAttribute("cid",null);
			model.addAttribute("orderSort",null);
		}
		return "/goodscenter/search/productSearch";
	}
	/**
	 *
	 * <p>Discription:[店铺列表查询]</p>
	 * Created on 2015年3月4日
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping(value = "/searchShop")
	public String searchItem(String keyword, Integer page, Integer orderSort, Model model,HttpServletRequest request) {
		Long buyerId=null;
		if(StringUtils.isNotBlank(keyword)){
			Pager<SearchShopDTO> pager = new Pager<SearchShopDTO>();
			if(page!=null){
				pager.setPage(page);
			}
			Long uid = WebUtil.getInstance().getUserId(request);
			String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
			
			if(uid != null){
                buyerId = uid;
               
			}
			DataGrid<SearchShopDTO> dg = searchExportService.searchShop(keyword, pager, orderSort,buyerId,region);
			pager.setTotalCount(dg.getTotal().intValue());
			pager.setRecords(dg.getRows());

			model.addAttribute("pager",pager);
			model.addAttribute("keyword", keyword);
			model.addAttribute("orderSort", orderSort);
			LOG.debug("店铺搜索-------"+keyword+"---"+pager+"----"+JSON.toJSONString(pager));
		}else{
			model.addAttribute("pager",new Pager<SearchShopDTO>());
			model.addAttribute("keyword", keyword);
			model.addAttribute("orderSort", orderSort);
		}
		return "/goodscenter/search/shopSearch";
	}
	
	/**
	 * 
	 * <p>Discription:[查询商品满减信息]</p>
	 * Created on 2015年12月4日
	 * @param fullMap
	 * @return
	 * @author: 尹归晋
	 */
	public JSONArray fullReduction(Map<Long, Long> fullMap){
		//满减
		JSONArray fulljsonArray = new JSONArray();
		for (Map.Entry<Long, Long> entry : fullMap.entrySet()) {
			//满减数据json
			JSONObject jsonObj = new JSONObject();
			//查询商品满减
			PromotionInDTO promotInDTO = new PromotionInDTO();
			promotInDTO.setItemId(entry.getKey());
			promotInDTO.setShopId(entry.getValue());
			promotInDTO.setIsEffective(String.valueOf(PromotionTimeStatusEnum.UNDERWAY.getStatus()));
			promotInDTO.setType(2);//查询满减
			ExecuteResult<DataGrid<PromotionOutDTO>>  promotions = this.promotionService.getPromotion(promotInDTO, null);
			if(promotions!=null && promotions.getResult()!=null && promotions.getResult().getRows()!=null && promotions.getResult().getRows().size()>0){
				JSONArray fullJsonArray = new JSONArray();
				List<PromotionOutDTO> listPromotionOutDTO = promotions.getResult().getRows();
	 			for (PromotionOutDTO pro : listPromotionOutDTO) {
	 				JSONObject jo = new JSONObject();
	 				//名称
	 				String activityName = pro.getPromotionInfo().getActivityName();
	 				jo.put("activityName", activityName);
	 				SimpleDateFormat format = new SimpleDateFormat( "yyyy/MM/dd HH:mm:ss");
	 				Date startTimeDate = pro.getPromotionInfo().getStartTime();
	 				String startTime = format.format(startTimeDate);
	 				jo.put("startTime", startTime);
	 				Date endTimeDate = pro.getPromotionInfo().getEndTime();
	 				String endTime = format.format(endTimeDate);
	 				jo.put("endTime", endTime);
	 				
	 				PromotionFullReduction full = pro.getPromotionFullReduction();
	 				if(full!=null){
 						BigDecimal meetPrice = full.getMeetPrice();//满值
 						jo.put("meetPrice", meetPrice);
 						BigDecimal discountPrice = full.getDiscountPrice();//减值
 						jo.put("discountPrice", discountPrice);
 						fullJsonArray.add(jo);
 					}
				}
	 			jsonObj.put("shopId", entry.getValue());
	 			jsonObj.put("itemId", entry.getKey());
	 			jsonObj.put("promotionFullReduction", fullJsonArray);
			}
			//载入满减信息
			fulljsonArray.add(jsonObj);
		}
		return fulljsonArray;
	}
	
	/**
	 * 
	 * <p>Discription:[查询商品店铺信息]</p>
	 * Created on 2015年12月4日
	 * @param map
	 * @return
	 * @author: 尹归晋
	 */
	public JSONArray getShopInfo(Map<Long,String> map){
		JSONArray jsonArray = new JSONArray();
		//构造店铺数据的json
		for (Map.Entry<Long, String> entry : map.entrySet()) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("shopId", entry.getKey());
			jsonObject.put("shopName", entry.getValue());
			//获取QQ客服
			ExecuteResult<ShopDTO> shopInfo = shopExportService.findShopInfoById(entry.getKey());
			if(shopInfo!=null && shopInfo.getResult()!=null){
				List<Long> idlist = new ArrayList<Long>();
				idlist.add(shopInfo.getResult().getSellerId());
				String stationId = qqCustomerService.getQqCustomerByIds(idlist, Constants.TYPE_SHOP);
				jsonObject.put("stationId", stationId);
			}else{
				jsonObject.put("stationId", "");
			}
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
}
