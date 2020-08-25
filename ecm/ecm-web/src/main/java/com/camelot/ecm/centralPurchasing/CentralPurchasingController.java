package com.camelot.ecm.centralPurchasing;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.common.Json;
import com.camelot.ecm.goodscenter.view.ItemQueryInDTOView;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.TradeInventoryInDTO;
import com.camelot.goodscenter.dto.TradeInventoryOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.CentralPurchasingDTO;
import com.camelot.maketcenter.dto.CentralPurchasingDetails;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.QuerySignUpInfoDTO;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.searchcenter.dto.SearchItemSkuOutDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 集采活动
 * 
 * @author 周志军
 * @createDate - 2015-11-26 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/purchase")
public class CentralPurchasingController extends BaseController {
	@Resource
	private CentralPurchasingExportService centralPurchasingServiceImpl;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ShopExportService shopExportService;
	@Resource
	private TradeInventoryExportService tradeInventoryExportService;
	@Resource
	private ItemExportService itemExportService;

	/**
	 * 
	 * <p>Description: [进入新增集采页面]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "activityAdd")
	public String form(@ModelAttribute("goods") ItemQueryInDTOView goods,
			Pager pager, Model model,String isView,
			QueryCentralPurchasingDTO queryCentralPurchasingDTO) {
		DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
		if (null != queryCentralPurchasingDTO
				&& null != queryCentralPurchasingDTO.getActivitesDetailsId()) {
			queryCentralPurchasingDTO = centralPurchasingServiceImpl
					.queryCentralPurchasingByDetailId(queryCentralPurchasingDTO);
			TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
			tradeInventoryInDTO.setSkuId(queryCentralPurchasingDTO.getSkuId());
			ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, null);
			
			model.addAttribute("tradeInventoryOutDTO", dg.getResult().getRows().get(0));
		}
		model.addAttribute("queryCentralPurchasingDTO",
				queryCentralPurchasingDTO);
		model.addAttribute("isView", isView);
		return "centralPurchasing/centralPurchasingAdd";
	}
	/**
	 * 
	 * <p>Description: [新增集采活动]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "activitySave")
	public String saveActivity(CentralPurchasingDTO centralPurchasingDTO,CentralPurchasingDetails details) {
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		BeanUtils.copyProperties(centralPurchasingDTO, queryCentralPurchasingDTO);
		BeanUtils.copyProperties(details, queryCentralPurchasingDTO);
		if(checkSkuUnique(queryCentralPurchasingDTO).isSuccess()){
			List<CentralPurchasingDetails> list = new ArrayList<CentralPurchasingDetails>();
			list.add(details);
			centralPurchasingDTO.setDetails(list);
			// 平台创建
			centralPurchasingDTO.setActivityType(1);
			centralPurchasingServiceImpl.addCentralPurchasingActivityDTO(centralPurchasingDTO);
		}
		
		return "redirect:" + SysProperties.getAdminPath() + "/purchase/activityList";
	}
	/**
	 * 
	 * <p>Description: [新增集采活动]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "activityUpdate")
	public String updateActivity(CentralPurchasingDTO centralPurchasingDTO,CentralPurchasingDetails details) {
		boolean flag = false;
		if(details.getActivitesDetailsId()==null){
			return "redirect:" + SysProperties.getAdminPath() + "/purchase/activityList";
		}else if(details.getSkuId() == null || centralPurchasingDTO.getActivityEndTime() == null || centralPurchasingDTO.getActivitySignUpTime() == null){
			flag = true;
		}else{
			QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
			BeanUtils.copyProperties(centralPurchasingDTO, queryCentralPurchasingDTO);
			BeanUtils.copyProperties(details, queryCentralPurchasingDTO);
			flag = checkSkuUnique(queryCentralPurchasingDTO).isSuccess();
		}
		if(flag){
			
			List<CentralPurchasingDetails> list = new ArrayList<CentralPurchasingDetails>();
			
			list.add(details);
			centralPurchasingDTO.setDetails(list);
			
			centralPurchasingServiceImpl.updateCentralPurchasingActivityDTO(centralPurchasingDTO);
		}
		
		return "redirect:" + SysProperties.getAdminPath() + "/purchase/activityList";
	}
	/**
	 * 
	 * <p>Description: [集采活动列表]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "activityList")
	public String listActivity(QueryCentralPurchasingDTO queryCentralPurchasingDTO,Page page,Model model) {
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		if(null!=queryCentralPurchasingDTO.getPlatformId()&&1==queryCentralPurchasingDTO.getPlatformId()){
			queryCentralPurchasingDTO.setPlatformId(null);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		boolean flag = false;
		if(null == queryCentralPurchasingDTO || null == queryCentralPurchasingDTO.getItemName() || "".equals(queryCentralPurchasingDTO.getItemName())){
			flag = false;
		} else {
			flag = true;
		}
		if(flag){
			List<Long> itemIds = new ArrayList<Long>();
			ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
			itemInDTO.setItemName(queryCentralPurchasingDTO.getItemName());
			DataGrid<ItemQueryOutDTO> dg = itemExportService.queryItemList(itemInDTO, null);
			if(null != dg && dg.getTotal() >0){
				
				for(ItemQueryOutDTO item:dg.getRows()){
					itemIds.add(item.getItemId());
				}
				queryCentralPurchasingDTO.setItemIds(itemIds);
			} else {
				model.addAttribute("pager", page);
				model.addAttribute("queryCentralPurchasingDTO", queryCentralPurchasingDTO);
				
				return "centralPurchasing/centralPurchasingList";
			}
		}
		// 依据插入时间排序
		queryCentralPurchasingDTO.setOrderByType(1);
		// 平台发布
		queryCentralPurchasingDTO.setActivityType(1);
		ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> er = centralPurchasingServiceImpl.queryCentralPurchasingActivity(queryCentralPurchasingDTO, pager);
		if(er.isSuccess()){
			DataGrid<QueryCentralPurchasingDTO> dg = er.getResult();
			List<QueryCentralPurchasingDTO> backList = getQueryCentralPurchasingDTO(dg.getRows());
			page.setCount(dg.getTotal().intValue());
			page.setList(backList);
		}
		model.addAttribute("pager", page);
		model.addAttribute("queryCentralPurchasingDTO", queryCentralPurchasingDTO);
		
		return "centralPurchasing/centralPurchasingList";
	}
	
	/**
	 * 
	 * <p>Description: [集采活动列表]</p>
	 * Created on 2015-11-30
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "viewSignUpInfo")
	public String viewSignUpInfo(QuerySignUpInfoDTO querySignUpInfoDTO,Page page,Model model) {
		if(page.getPageSize()==-1){
			page.setPageSize(10);
		}
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		ExecuteResult<DataGrid<QuerySignUpInfoDTO>> er = centralPurchasingServiceImpl.querySignUpInfo(querySignUpInfoDTO, pager);
		if(er.isSuccess()){
			DataGrid<QuerySignUpInfoDTO> dg = er.getResult();
			page.setCount(dg.getTotal().intValue());
			page.setList(dg.getRows());
		}
		model.addAttribute("pager", page);
		
		return "centralPurchasing/signUpInfo";
	}
	
	/**
	 * 商品列表
	 * @param goods
	 * @param pager
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "gsListShop")
    public String gsListShop(@ModelAttribute("goods") ItemQueryInDTOView goods,@ModelAttribute("pager") Pager<SearchItemSkuOutDTO> pager,Integer platformId,Model model){
		TradeInventoryInDTO tradeInventoryInDTO = new TradeInventoryInDTO();
		tradeInventoryInDTO.setPlatformId(platformId);
		List<ShopDTO> shopList = new ArrayList<ShopDTO>();
    	//查询类目优先级 3>2>1
    	if(goods.getCid()!=null&&goods.getCid()>0){
    		tradeInventoryInDTO.setCid(goods.getCid().longValue());
    	}else if (goods.getPlatformId2()!=null && goods.getPlatformId2()>0){
    		tradeInventoryInDTO.setCid(goods.getPlatformId2().longValue());
    	}else if(goods.getPlatformId1()!=null && goods.getPlatformId1()>0){
    		tradeInventoryInDTO.setCid(goods.getPlatformId1().longValue());
    	}
    	if(goods.getShopName()!=null&&goods.getShopName().length()>0){
    		shopList = getShopsByName(goods.getShopName());
    		if(shopList.size()==0){
    	        return "centralPurchasing/goodsList";
    		}
    		tradeInventoryInDTO.setShopIds(getShopIdsByName(shopList));
    	}
    	if(goods.getItemQueryInDTO().getItemId() != null){
    		tradeInventoryInDTO.setItemId(goods.getItemQueryInDTO().getItemId().longValue());
    	}
    	if(goods.getItemQueryInDTO().getItemName() != null && !"".equals(goods.getItemQueryInDTO().getItemName())){
    		tradeInventoryInDTO.setItemName(goods.getItemQueryInDTO().getItemName());
    	}
    	// 在售商品
    	tradeInventoryInDTO.setItemStatus(4);
    	tradeInventoryInDTO.setHasPrice(1);//排除暂无报价的商品
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(tradeInventoryInDTO, pager);
    	Page<TradeInventoryOutDTO> p = new Page<TradeInventoryOutDTO>();
		p.setPageNo(pager.getPage());
		p.setPageSize(pager.getRows());
		p.setCount(dg.getResult().getTotal());
		p.setList(dg.getResult().getRows());
		p.setOrderBy(pager.getOrder());
    	model.addAttribute("page", p);
        return "centralPurchasing/goodsList";
    }
	/**
	 * 查询店铺
	 * @param shopName
	 * @return
	 */
	private List<Long> getShopIdsByName(List<ShopDTO> shopList){
		List<Long> longList = new ArrayList<Long>();
    	if(shopList.size()>0){
    		for(ShopDTO shopDTOModel : shopList){
    			longList.add(shopDTOModel.getShopId());
    		}
    	}
    	return longList;
    }
	/**
	 * 依据店铺名称获取店铺集合
	 * @param shopName
	 * @return
	 */
	private List<ShopDTO> getShopsByName(String shopName){
    	ShopDTO shopDTO = new ShopDTO();
    	shopDTO.setShopName(shopName);
    	Pager<ShopDTO> pager = new Pager<ShopDTO>();
    	pager.setPage(1);
    	pager.setRows(100);
    	ExecuteResult<DataGrid<ShopDTO>> result =shopExportService.findShopInfoByCondition(shopDTO, pager);
    	List<ShopDTO> shopList = result.getResult().getRows();
    	return shopList;
	}
	private String getCategory(Long cid){
    	String oneName="";
        String twoName = "";
        String threeName = "";
        StringBuilder category = new StringBuilder();
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory =  itemCategoryService.queryParentCategoryList(cid);
        for(ItemCatCascadeDTO itemCatCascadeOne : resultCategory.getResult()){
			for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
				for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
					if(cid.equals(itemCatCascadeThree.getCid())){
						if(threeName.length()==0){
							threeName = itemCatCascadeThree.getCname();
						}
						if(oneName.length()==0){
							oneName = itemCatCascadeOne.getCname();
						}
						if(twoName.length()==0){
							twoName = itemCatCascadeTwo.getCname();
						}
						itemCatCascadeTwo.getCname();
						break ;
					}
				}
			}
		}
        category.append(oneName+"/"+twoName+"/"+threeName);
        return category.toString();
    }
	
	/**
	 * 
	 * <p>Description: [获取集采活动商品信息]</p>
	 * Created on 2015-12-9
	 * @author 周志军
	 * @return 
	 */
	private List<QueryCentralPurchasingDTO> getQueryCentralPurchasingDTO(List<QueryCentralPurchasingDTO> list){
		List<QueryCentralPurchasingDTO> backList = new ArrayList<QueryCentralPurchasingDTO>();
		for(QueryCentralPurchasingDTO centralPurchase:list){
			QueryCentralPurchasingDTO backCentralPurchase = new QueryCentralPurchasingDTO();
			BeanUtils.copyProperties(centralPurchase, backCentralPurchase);
			TradeInventoryInDTO dto = new TradeInventoryInDTO();
			if(null == centralPurchase.getSkuId()){
				backList.add(backCentralPurchase);
				continue;
			}
			// 依据SKU获取商品信息,获取商品信息为唯一
			dto.setSkuId(centralPurchase.getSkuId());
			ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService.queryTradeInventoryList(dto, null);
			if(dg.isSuccess() && dg.getResult().getTotal() > 0){
				// 组装集采对象
				QueryCentralPurchasingDTO qc = dealQueryCentralPurchasingDTO(dg.getResult().getRows().get(0));
				backCentralPurchase.setItemAttr(qc.getItemAttr());
				backCentralPurchase.setItemName(qc.getItemName());
				backList.add(backCentralPurchase);
			}else{
				backList.add(backCentralPurchase);
				continue;
			}
		}
		return backList;
	}
	/**
	 * 
	 * <p>Description: [组装集采对象]</p>
	 * Created on 2015-12-9
	 * @author 周志军
	 * @return 
	 */
	private QueryCentralPurchasingDTO dealQueryCentralPurchasingDTO(TradeInventoryOutDTO tradeInventory){
		List<com.camelot.maketcenter.dto.ItemAttr> backItemAttrList = new ArrayList<com.camelot.maketcenter.dto.ItemAttr>();
		QueryCentralPurchasingDTO centralPurchase = new QueryCentralPurchasingDTO();
		for(ItemAttr itemAttr:tradeInventory.getItemAttr()){
			com.camelot.maketcenter.dto.ItemAttr backItemAttr = new com.camelot.maketcenter.dto.ItemAttr();
			BeanUtils.copyProperties(itemAttr, backItemAttr);
			backItemAttrList.add(backItemAttr);
			backItemAttr = null;
		}
		centralPurchase.setItemName(tradeInventory.getItemName());
		centralPurchase.setItemAttr(backItemAttrList);
		return centralPurchase;
	}
	/**
	 * 
	 * <p>Description: [校验SKU唯一性]</p>
	 * Created on 2015-12-15
	 * @author 周志军
	 * @return 
	 */
	@RequestMapping(value = "checkSkuUnique")
	@ResponseBody
	public Json checkSkuUnique(QueryCentralPurchasingDTO queryCentralPurchasingDTO) {
		ExecuteResult<Boolean> er = centralPurchasingServiceImpl.checkUniqueSku(queryCentralPurchasingDTO);
		Json json = new Json();
		json.setSuccess(er.isSuccess());
		return json;
	} 
}
