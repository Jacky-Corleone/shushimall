package com.camelot.mall.sellcenter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.CategoryAttrDTO;
import com.camelot.goodscenter.dto.ItemAdDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemAttrValueItemDTO;
import com.camelot.goodscenter.dto.ItemBaseDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemShopCidDTO;
import com.camelot.goodscenter.dto.ItemSkuPackageDTO;
import com.camelot.goodscenter.dto.ItemStatusModifyDTO;
import com.camelot.goodscenter.dto.enums.HouseTypeEnum;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemAttrValueItemExportService;
import com.camelot.goodscenter.service.ItemAttributeExportService;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemSkuPackageService;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.emums.CentralPurchasingActivityRealStatusEnum;
import com.camelot.maketcenter.service.CentralPurchasingExportService;
import com.camelot.maketcenter.service.PromotionFrExportService;
import com.camelot.maketcenter.service.PromotionInfoExportService;
import com.camelot.maketcenter.service.PromotionMdExportService;
import com.camelot.mall.Constants;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.encrypt.EncrypUtil;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopFreightTemplateDTO;
import com.camelot.storecenter.dto.ShopRenovationDTO;
import com.camelot.storecenter.dto.ShopRenovationDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopFreightTemplateService;
import com.camelot.storecenter.service.ShopRenovationExportService;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [卖家商品管理]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sellcenter/sellProduct")
public class SellProductController {
	@Resource
	private UserExportService userExportService;

	@Resource
	private ItemExportService itemExportService;

	@Resource
	private UserExtendsService userExtendsService;

	@Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;

	@Resource
    private ItemCategoryService itemCategoryService;

	@Resource
	private ShopCategoryExportService shopCategoryExportService;

	@Resource
	private ShopBrandExportService shopBrandExportService;
	
	@Resource
	private ShopExportService shopExportService;

	@Resource
	private ItemBrandExportService itemBrandExportService;

	@Resource
	private AddressBaseService addressBaseService;
	
	@Resource
	private ItemAttributeExportService itemAttributeExportService;
	
	@Resource
	private ItemAttrValueItemExportService itemAttrValueItemExportService;
	
	@Resource
	private ShopRenovationExportService shopRenovationExportService;
	@Resource
    private ShopFreightTemplateService shopFreightTemplateService;
	
	@Resource
	private CentralPurchasingExportService centralPurchasingExportService;
	
	@Resource
	private PromotionInfoExportService promotionInfoExportService;
	
	@Resource
	private PromotionFrExportService promotionFrExportService;
	
	@Resource
	private PromotionMdExportService promotionMdExportService;
	
	@Resource
	private ItemSkuPackageService itemSkuPackageService;
	
	/**
	 * 前台的Form 元素绑定到 后台的JaveBean对象，做的一个映射，但是这个映射的List长度
	 * @param binder
	 */
	@InitBinder  
	public void initListBinder(WebDataBinder binder){  
	      // 设置需要包裹的元素个数，默认为256  
	      binder.setAutoGrowCollectionLimit(8192);  
	} 
	/**
	 * 商家商品列表
	 * @return
	 */
	@RequestMapping("goodsList")
	public String goodsList(Integer page,Integer rows,Integer oldRows,ItemQueryInDTO itemInDTO,String sort,String order,String shopLevelOneId,String queryType,HttpServletRequest request,Model model){
		String url = "sellcenter/sellgoods/sellGoodList";
		if(StringUtils.isNotEmpty(queryType) && "forSale".equals(queryType)){
			if(itemInDTO.getItemStatus() == null || itemInDTO.getItemStatus() == 0){
				List<Integer> itemStatusList = new ArrayList<Integer>();
				itemStatusList.add(3);
				itemStatusList.add(5);
				itemInDTO.setItemStatusList(itemStatusList);
			}
			url = "sellcenter/sellgoods/forSaleItemList";
		}

		//此判断过滤掉伪删除状态的记录（与后台商定，后续修改后台，不再在前台做判断）
		else if(itemInDTO.getItemStatus() == null){
			List<Integer> itemStatusList = new ArrayList<Integer>();
			itemStatusList.add(ItemStatusEnum.NOT_PUBLISH.getCode());
			itemStatusList.add(ItemStatusEnum.AUDITING.getCode());
			itemStatusList.add(ItemStatusEnum.SHELVING.getCode());
			itemStatusList.add(ItemStatusEnum.SALING.getCode());
			itemStatusList.add(ItemStatusEnum.UNSHELVED.getCode());
			itemStatusList.add(ItemStatusEnum.LOCKED.getCode());
			itemStatusList.add(ItemStatusEnum.APPLYING.getCode());
			itemStatusList.add(ItemStatusEnum.REJECTED.getCode());
			itemInDTO.setItemStatusList(itemStatusList);
		}
		//获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		//通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> ItemCatCascadeDTOResult =  itemCategoryService.queryParentCategoryList(categoryIds);
		//将分类列表转换为map形式
		Map<String,String> categoryMap = new HashMap<String,String>();
		for(ItemCatCascadeDTO itemCatCascadeOne : ItemCatCascadeDTOResult.getResult()){
			for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
				for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
					categoryMap.put(itemCatCascadeThree.getCid().toString(),itemCatCascadeOne.getCname() + "/" 
								+ itemCatCascadeTwo.getCname() + "/" + itemCatCascadeThree.getCname());
				}
			}
		}
		//拼装查询参数
		itemInDTO.setShopIds(new Long[]{shopId});
		Pager<ItemQueryOutDTO> pager = new Pager<ItemQueryOutDTO>();
		if(page == null){
			page = 1;
		}
		if(rows!=null){
			pager.setRows(rows);//指定每页显示条数
		}
		if(oldRows==null){
			oldRows=10;//oldRows默认10条   rows本身默认就10条
		}
		pager.setPage(page);
		pager.setOrder(order);
		pager.setSort(sort);
		//对itemID字段加密，防止随便修改其他用户的商品信息
		Long userId = WebUtil.getInstance().getUserId(request);
		DataGrid<ItemQueryOutDTO> dg = itemExportService.queryItemListFlag(itemInDTO, pager,oldRows);
		//遍历查询出的商品，将一级、二级分类展示出来
		List<ItemQueryOutDTO> itemQueryOutDTOList = dg.getRows();
		if(itemQueryOutDTOList != null){
			for(ItemQueryOutDTO itemQueryOutDTO : itemQueryOutDTOList){
				itemQueryOutDTO.setcName(categoryMap.get(itemQueryOutDTO.getCid().toString()));
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", itemQueryOutDTO.getItemId()+"");
				if(passKeyEr.isSuccess()){
					itemQueryOutDTO.setPassKey(passKeyEr.getResult());
				}
			}
		}
		
		pager.setRecords(dg.getRows());
		pager.setPage(dg.getPageNum());
		pager.setTotalCount(dg.getTotal().intValue());
		model.addAttribute("pager", pager);
		model.addAttribute("itemInDTO", itemInDTO);
		//获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategorySellerDTOList = this.getLevelOneShopCategory(request);
		model.addAttribute("shopCategorySellerDTOList", shopCategorySellerDTOList);
		//获取选中店铺一级分类对应的二级分类
		if(StringUtils.isNotEmpty(shopLevelOneId)){
			ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
			shopCategorySellerDTO.setParentCid(new Long(shopLevelOneId));
			ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO, null);
			List<ShopCategorySellerDTO> levelTwoCategory = result.getResult().getRows();
			model.addAttribute("levelTwoCategory", levelTwoCategory);
			model.addAttribute("shopLevelOneId", shopLevelOneId);
		}
		//获取店铺id
        ShopBrandDTO shopBrandDTO=new ShopBrandDTO();
        if(null!=itemInDTO.getShopIds()&&itemInDTO.getShopIds().length>0){
		   shopBrandDTO.setShopId(itemInDTO.getShopIds()[0]);
        }
		//审核通过
		shopBrandDTO.setStatus(2);
		List<ItemBrandDTO> itemBrandDTOList=new ArrayList<ItemBrandDTO>();
		ExecuteResult<DataGrid<ShopBrandDTO>> executeResult = this.shopBrandExportService.queryShopBrand(shopBrandDTO, null);
		if(executeResult.isSuccess()){
			List<ShopBrandDTO> shopBrandDTOList = executeResult.getResult().getRows();
			if(null!=shopBrandDTOList&&shopBrandDTOList.size()>0){
				for(ShopBrandDTO shopBrandDTOEvery:shopBrandDTOList){
					ExecuteResult<List<ItemBrandDTO>> result = itemBrandExportService.queryItemBrandByIds(shopBrandDTOEvery.getBrandId());
					if(result.isSuccess() && result.getResult() != null && result.getResult().size() > 0)
						itemBrandDTOList.add(result.getResult().get(0));
				  }
			   }
		}
		List<ItemBrandDTO> resList= new ArrayList<ItemBrandDTO>();
		List<String> list= new ArrayList<String>();
		if(null!=itemBrandDTOList&&itemBrandDTOList.size()>0){
			for(ItemBrandDTO dto:itemBrandDTOList){
				if(!list.contains(dto.getBrandName())){
					list.add(dto.getBrandName());
					resList.add(dto);
				}
			}
		}
		model.addAttribute("itemBrandDTOs", resList);
		return url;
	}
	
	/**
	 * 校验店铺是否申请了平台分类
	 * @param request
	 * @return
	 */
	@RequestMapping("validateAttrAndBrand")
	@ResponseBody
	public Map<String,Object> validateAttrAndBrand(HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//校验店铺类目
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> result = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		if(result.isSuccess()){
			List<ShopCategoryDTO> shopCategoryDTOList = result.getResult();
			if(shopCategoryDTOList == null || shopCategoryDTOList.size() == 0){
				resultMap.put("result", false);
				resultMap.put("message","请先申请店铺经营类目!");
				return resultMap;
			}
		}
		//校验品牌
		ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
		shopBrandDTO.setShopId(shopId);
		shopBrandDTO.setStatus(2);
		ExecuteResult<DataGrid<ShopBrandDTO>> executeResult = this.shopBrandExportService.queryShopBrand(shopBrandDTO, null);
		if(executeResult.isSuccess()){
			DataGrid<ShopBrandDTO> dg = executeResult.getResult();
			if(dg == null || dg.getRows() == null || dg.getRows().size() == 0){
				resultMap.put("result", false);
				resultMap.put("message","请先申请店铺经营品牌!");
				return resultMap;
			}
		}
		resultMap.put("result", true);
		return resultMap;
	}

	/**
	 * 平台商品列表
	 * @return
	 */
	@RequestMapping("platformGoodsList")
	public String platformGoodsList(Integer page,ItemQueryInDTO itemInDTO,Long levelOneId,Long levelTwoId,HttpServletRequest request,Model model){
		Pager<ItemQueryOutDTO> pager = new Pager<ItemQueryOutDTO>();
		if(page == null){
			page = 1;
		}
		pager.setPage(page);
		//平台
		itemInDTO.setOperator(2);
		itemInDTO.setPlatLinkStatus(3);
		itemInDTO.setItemStatus(4);
		//获取店铺ID
		Long shopId = this.getShopId(request);
		//通过店铺经营分类获取分类列表
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		ExecuteResult<List<ItemCatCascadeDTO>> result =  itemCategoryService.queryParentCategoryList(categoryIds);
		model.addAttribute("itemCategoryDTOList",result.getResult());
		//只能上传店铺维护的平台分类下的商品
		itemInDTO.setCids(categoryIds);
		//品牌限制 只能上传店铺维护品牌下的商品
		List<Long> brandIdList = new ArrayList<Long>();
		ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
		shopBrandDTO.setShopId(shopId);
		shopBrandDTO.setStatus(2);
		ExecuteResult<DataGrid<ShopBrandDTO>> executeResult = this.shopBrandExportService.queryShopBrand(shopBrandDTO, null);
		if(executeResult.isSuccess()){
			List<ShopBrandDTO> shopBrandDTOList = executeResult.getResult().getRows();
			for(ShopBrandDTO shopBrandDTOEvery:shopBrandDTOList){
				brandIdList.add(shopBrandDTOEvery.getBrandId());
			}
		}
		itemInDTO.setBrandIdList(brandIdList);
		Long userId = WebUtil.getInstance().getUserId(request);
		//商品查询
		DataGrid<ItemQueryOutDTO> dg = itemExportService.queryItemList(itemInDTO, pager);
		if(dg.getTotal()!=0L){
			for(int k = 0 ; k < dg.getRows().size() ; k++){
				Map<String,String> categoryMap =this.getCategoryName(dg.getRows().get(k).getCid());
				String cName = "";
				if(categoryMap.get("oneName")!=null){
					cName = categoryMap.get("oneName")+"/";
				}
				if(categoryMap.get("twoName")!=null){
					cName =cName+ categoryMap.get("twoName")+"/";
				}
				if(categoryMap.get("threeName")!=null){
					cName =cName+ categoryMap.get("threeName");
				}
				dg.getRows().get(k).setcName(cName);
				ItemQueryOutDTO itemQueryOutDTO = dg.getRows().get(k);
				ExecuteResult<String> passKeyEr = EncrypUtil.EncrypStrByAES(userId+"", itemQueryOutDTO.getItemId()+"");
				if(passKeyEr.isSuccess()){
					itemQueryOutDTO.setPassKey(passKeyEr.getResult());
				}
			}
		}
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		model.addAttribute("pager", pager);
		//获取平台二级分类
//		if(levelOneId != null){
//			DataGrid<ItemCategoryDTO> dataGridTwo = itemCategoryService.queryItemCategoryList(levelOneId);
//			List<ItemCategoryDTO> levelTwoItemCategoryDTOList = dataGridTwo.getRows();
//			model.addAttribute("levelTwoItemCategoryDTOList",levelTwoItemCategoryDTOList);
//		}
//		if(levelTwoId != null){
//			DataGrid<ItemCategoryDTO> dataGridThree = itemCategoryService.queryItemCategoryList(levelTwoId);
//			List<ItemCategoryDTO> levelThreeItemCategoryDTOList = dataGridThree.getRows();
//			model.addAttribute("levelThreeItemCategoryDTOList",levelThreeItemCategoryDTOList);
//		}
		List<ItemCatCascadeDTO> ItemCatCascadeLevelTwoList = null;
		List<ItemCatCascadeDTO> ItemCatCascadeLevelThreeList = null;
		if(itemInDTO.getCid() != null){
			labelA:
				for(ItemCatCascadeDTO itemCatCascadeOne : result.getResult()){
					for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
						for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
							if(itemInDTO.getCid().equals(itemCatCascadeThree.getCid())){
								ItemCatCascadeLevelTwoList = itemCatCascadeOne.getChildCats();
								ItemCatCascadeLevelThreeList = itemCatCascadeTwo.getChildCats();
								break labelA;
							}
						}
					}
				}
		}
		model.addAttribute("ItemCatCascadeLevelTwoList",ItemCatCascadeLevelTwoList);
		model.addAttribute("ItemCatCascadeLevelThreeList",ItemCatCascadeLevelThreeList);
		model.addAttribute("levelOneId",levelOneId);
		model.addAttribute("levelTwoId",levelTwoId);
		model.addAttribute("cid",itemInDTO.getCid());
		model.addAttribute("itemName",itemInDTO.getItemName());
		model.addAttribute("productId",itemInDTO.getProductId());
		model.addAttribute("id",itemInDTO.getId());
		return "sellcenter/sellgoods/platformGoodsList";
	}

	/**
	 * 商品添加方式
	 * @return
	 */
	@RequestMapping("itemAddSelect")
	public String itemAddSelect(){
		return "sellcenter/sellgoods/goodsAddSelect";
	}

	/**
	 * 添加/编辑商品页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(ItemDTO itemDTO,Integer addSource,String doType,HttpServletRequest request,Model model,Integer page,Integer rows){
		Integer operator = 1;
		//获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategoryDTOList = this.getLevelOneShopCategory(request);
		//获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		//通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> result =  itemCategoryService.queryParentCategoryList(categoryIds);

		if(itemDTO.getItemId() != null){
			ExecuteResult<ItemDTO> ItemResult = itemExportService.getItemById(itemDTO.getItemId());
			itemDTO = ItemResult.getResult();
			operator = itemDTO.getOperator();
			addSource = itemDTO.getAddSource();
			//获取商品对应的一级、二级、三级平台分类
			Long oneLevelId = null;
			Long twoLevelId = null;
			Long threeLevelId = itemDTO.getCid();
			List<ItemCatCascadeDTO> ItemCatCascadeLevelTwoList = null;
			List<ItemCatCascadeDTO> ItemCatCascadeLevelThreeList = null;
			labelA:
			for(ItemCatCascadeDTO itemCatCascadeOne : result.getResult()){
				for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
					for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
						if(itemDTO.getCid().equals(itemCatCascadeThree.getCid())){
							twoLevelId = itemCatCascadeTwo.getCid();
							oneLevelId = itemCatCascadeOne.getCid();
							ItemCatCascadeLevelTwoList = itemCatCascadeOne.getChildCats();
							ItemCatCascadeLevelThreeList = itemCatCascadeTwo.getChildCats();
							break labelA;
						}
					}
				}
			}
			model.addAttribute("oneLevelId",oneLevelId);
			model.addAttribute("twoLevelId",twoLevelId);
			model.addAttribute("ItemCatCascadeLevelTwoList",ItemCatCascadeLevelTwoList);
			model.addAttribute("ItemCatCascadeLevelThreeList",ItemCatCascadeLevelThreeList);
			//获取店铺一级类目
			if(itemDTO.getShopCid() != null){
				ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
				shopCategorySellerDTO.setCid(itemDTO.getShopCid());
				ExecuteResult<DataGrid<ShopCategorySellerDTO>> scResult = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO,null);
				List<ShopCategorySellerDTO> ShopCategorySellerList = scResult.getResult().getRows();
				if(ShopCategorySellerList != null && ShopCategorySellerList.size() > 0){
					model.addAttribute("shopCatLevelOneId",ShopCategorySellerList.get(0).getParentCid());
				}
			}
			//商品对象转换为json 排除商品描述、广告词以用来防止含有特殊字符无法在前台转换
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ItemDTO.class);
			filter.getExcludes().add("describeUrl");
			filter.getExcludes().add("ad");
			filter.getExcludes().add("keywords");
			filter.getExcludes().add("packingList");
			filter.getExcludes().add("afterService");
			filter.getExcludes().add("authentication");
			filter.getExcludes().add("specification");
			model.addAttribute("itemDTOJson",JSON.toJSONString(itemDTO,filter));
			//遍历图片获取缺失的图片在前台进行补充，最多允许上传4张
			List<Integer> picList = new ArrayList<Integer>();
			int num = itemDTO.getPicUrls().length;
			for(int k = 1;k<=4 - itemDTO.getPicUrls().length;k++){
				picList.add(num + k);
			}
			model.addAttribute("picList",picList);
		}else{
			model.addAttribute("itemDTOJson",false);
		}
		List<AddressBase> addressBaseList = this.addressBaseService.queryAddressBase("0");
		//获取是否直营店状态
		ExecuteResult<ShopDTO> shResult = shopExportService.findShopInfoById(shopId);
		if(shResult.isSuccess()){
			ShopDTO shDTO = shResult.getResult();
			model.addAttribute("directsale",shDTO.getDirectSale());
		}
		// 查询店铺的运费模版
		ShopFreightTemplateDTO inDTO = new ShopFreightTemplateDTO();
		inDTO.setShopId(shopId);
		inDTO.setDelState("1");
		ExecuteResult<DataGrid<ShopFreightTemplateDTO>> executeResult = shopFreightTemplateService.queryShopFreightTemplateList(inDTO, null);
		if (executeResult.isSuccess()) {
			model.addAttribute("shopFreightTemplateList", executeResult.getResult().getRows());
		}
		model.addAttribute("shopCategoryDTOList",shopCategoryDTOList);
		model.addAttribute("itemCatCascadeDTOList",result.getResult());
		model.addAttribute("itemDTO",itemDTO);
		model.addAttribute("addressBaseList",addressBaseList);
		model.addAttribute("operator",operator);
		model.addAttribute("addSource",addSource);
		model.addAttribute("doType",doType);
		model.addAttribute("page", page);
		model.addAttribute("rows", rows);
		model.addAttribute("housetypes", HouseTypeEnum.values());
		return "sellcenter/sellgoods/goodsAdd";
	}

	/**
	 * 从平台上传商品
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("plantForm")
	public String plantForm(ItemDTO itemDTO,Integer addSource,String isPlantEdit,String doType,HttpServletRequest request,Model model,Integer page,Integer rows){
		Integer operator = 1;
		Long shopId = this.getShopId(request);
		//获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategoryDTOList = this.getLevelOneShopCategory(request);
		ExecuteResult<ItemDTO> ItemResult = itemExportService.getItemById(itemDTO.getItemId());
		itemDTO = ItemResult.getResult();
		model.addAttribute("plstItemId",itemDTO.getItemId());
		//获取平台分类
		ExecuteResult<List<ItemCatCascadeDTO>> result =  itemCategoryService.queryParentCategoryList(itemDTO.getCid());
		List<ItemCatCascadeDTO> itemCatCascadeDTOList = result.getResult();
		if(itemCatCascadeDTOList != null && itemCatCascadeDTOList.size() > 0){
			ItemCatCascadeDTO itemCatCascadeDTO = itemCatCascadeDTOList.get(0);
			ItemCatCascadeDTO twoItemCatCascadeDTO = itemCatCascadeDTO.getChildCats().get(0);
			ItemCatCascadeDTO threeItemCatCascadeDTO = twoItemCatCascadeDTO.getChildCats().get(0);
			model.addAttribute("itemCatCascadeDTO",itemCatCascadeDTO);
			model.addAttribute("twoItemCatCascadeDTO",twoItemCatCascadeDTO);
			model.addAttribute("threeItemCatCascadeDTO",threeItemCatCascadeDTO);
		}
		//获取品牌
		ExecuteResult<List<ItemBrandDTO>> itemBrandDTOResult = itemBrandExportService.queryItemBrandByIds(itemDTO.getBrand());
		List<ItemBrandDTO> itemBrandDTOList = itemBrandDTOResult.getResult();
		if(itemBrandDTOList != null){
			model.addAttribute("itemBrandDTO",itemBrandDTOList.get(0));
		}
		//获取地域信息
		List<AddressBase> addressBaseList = this.addressBaseService.queryAddressBase("0");
		model.addAttribute("addressBaseList",addressBaseList);
		//获取是否直营店状态
		ExecuteResult<ShopDTO> shResult = shopExportService.findShopInfoById(shopId);
		if(shResult.isSuccess()){
			ShopDTO shDTO = shResult.getResult();
			model.addAttribute("directsale",shDTO.getDirectSale());
		}
		// 查询店铺的运费模版
		ShopFreightTemplateDTO inDTO = new ShopFreightTemplateDTO();
		inDTO.setShopId(shopId);
		inDTO.setDelState("1");
		ExecuteResult<DataGrid<ShopFreightTemplateDTO>> executeResult = shopFreightTemplateService.queryShopFreightTemplateList(inDTO, null);
		if (executeResult.isSuccess()) {
			model.addAttribute("shopFreightTemplateList", executeResult.getResult().getRows());
		}
		if(StringUtils.isNotEmpty(isPlantEdit) && "true".equals(isPlantEdit)){
			addSource = itemDTO.getAddSource();
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(ItemDTO.class);
			filter.getExcludes().add("describeUrl");
			filter.getExcludes().add("ad");
			filter.getExcludes().add("keywords");
			filter.getExcludes().add("packingList");
			filter.getExcludes().add("afterService");
			filter.getExcludes().add("authentication");
			filter.getExcludes().add("specification");
			model.addAttribute("itemDTOJson",JSON.toJSONString(itemDTO,filter));
			model.addAttribute("plstItemId",itemDTO.getPlstItemId());
			//获取店铺分类
			if(itemDTO.getShopCid() != null){
				ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
				shopCategorySellerDTO.setCid(itemDTO.getShopCid());
				ExecuteResult<DataGrid<ShopCategorySellerDTO>> scResult = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO,null);
				List<ShopCategorySellerDTO> ShopCategorySellerList = scResult.getResult().getRows();
				if(ShopCategorySellerList != null && ShopCategorySellerList.size() > 0){
					model.addAttribute("shopCatLevelOneId",ShopCategorySellerList.get(0).getParentCid());
				}
			}
		}else{
			itemDTO.setItemId(null);
			model.addAttribute("itemDTOJson",false);
		}
		//遍历图片获取缺失的图片在前台进行补充，最多允许上传4张
		List<Integer> picList = new ArrayList<Integer>();
		int num = itemDTO.getPicUrls().length;
		for(int k = 1;k<=4 - itemDTO.getPicUrls().length;k++){
			picList.add(num + k);
		}
		
		model.addAttribute("picList",picList);
		model.addAttribute("itemDTO",itemDTO);
		model.addAttribute("operator",operator);
		model.addAttribute("shopCategoryDTOList",shopCategoryDTOList);
		model.addAttribute("addSource",addSource);
		model.addAttribute("doType",doType);
		if(page==null&&rows==null){
			page=1;
			rows=10;
		}
		model.addAttribute("page", page);
		model.addAttribute("rows", rows);
		return "sellcenter/sellgoods/goodsAdd";
	}

	/**
	 * 商品保存
	 * @param itemDTO
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("save")
	@ResponseBody
	public Map<String,Object> save(ItemDTO itemDTO,String itemPics,String allAttrSalesStr,String allAttributesStr,HttpServletRequest request,Model model){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if(StringUtils.isNotEmpty(itemDTO.getTimingList())){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				itemDTO.setTimingListing(sdf.parse(itemDTO.getTimingList()));
			}
			itemDTO.setSellerId(this.getLoginUserId(request));
			//商品图片
			if(StringUtils.isNotEmpty(itemPics)){
				itemDTO.setPicUrls(itemPics.split(","));
			}
			itemDTO.setShopId(this.getShopId(request));
			
			if(null!=this.getShopId(request)){
				ExecuteResult<ShopDTO> shopInfo=shopExportService.findShopInfoById(this.getShopId(request));
				if(shopInfo.getResult()!=null){
					if(shopInfo.getResult().getPlatformId()!=null){
						itemDTO.setPlatformId(shopInfo.getResult().getPlatformId());
					}
				}
			}
			//将商品描述里面的图片路径变为相对路径
			Object imgUrlObj = request.getSession().getServletContext().getAttribute("imageServerAddr");
			if(imgUrlObj != null && StringUtils.isNotEmpty(imgUrlObj.toString())){
				itemDTO.setDescribeUrl(itemDTO.getDescribeUrl().replaceAll(imgUrlObj.toString(),""));
				itemDTO.setSpecification(itemDTO.getSpecification().replaceAll(imgUrlObj.toString(),""));
				itemDTO.setAfterService(itemDTO.getAfterService().replaceAll(imgUrlObj.toString(),""));
			}
			
			ExecuteResult<ItemDTO> result = itemExportService.addItemInfo(itemDTO);
			if (result.isSuccess()) {
				resultMap.put("result", true);
			} else {
				resultMap.put("result", false);
				if (result.getErrorMessages() != null
						&& result.getErrorMessages().size() > 0) {
					resultMap.put("message", result.getErrorMessages().get(0));
				} else {
					resultMap.put("message","操作失败");
				}
			}
		} catch (Exception e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 商品伪删除
	 */
	@RequestMapping("pseudoDelete")
	@ResponseBody
	public Map<String,Object> pseudoDelete(HttpServletRequest request){
		Map<String,Object> retMap = new HashMap<String,Object>();
		String itemId = request.getParameter("itemId");
		ExecuteResult<String> ret = itemExportService.deleteItem(Long.valueOf(itemId));
		if (ret.isSuccess()) {
			retMap.put("messages", "操作成功!");
		} else {
			retMap.put("messages", "操作失败!");
		}
		return retMap;
	}

	/**
	 * 商品编辑
	 * @param itemDTO
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("edit")
	@ResponseBody
	public Map<String,Object> editGoodsInfo(ItemDTO itemDTO,String itemPics,HttpServletRequest request,Model model){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if(StringUtils.isNotEmpty(itemDTO.getTimingList())){
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
				itemDTO.setTimingListing(sdf.parse(itemDTO.getTimingList()));
			}else{
				itemDTO.setTimingListing(null);
				itemExportService.updateTimingListing(itemDTO);
			}
			itemDTO.setSellerId(this.getLoginUserId(request));
			//商品图片
			if(StringUtils.isNotEmpty(itemPics)){
				itemDTO.setPicUrls(itemPics.split(","));
			}
			itemDTO.setShopId(this.getShopId(request));
			//将商品描述里面的图片路径变为相对路径
			Object imgUrlObj = request.getSession().getServletContext().getAttribute("imageServerAddr");
			if(imgUrlObj != null && StringUtils.isNotEmpty(imgUrlObj.toString())){
				itemDTO.setDescribeUrl(itemDTO.getDescribeUrl().replaceAll(imgUrlObj.toString(),""));
				itemDTO.setSpecification(itemDTO.getSpecification().replaceAll(imgUrlObj.toString(),""));
				itemDTO.setAfterService(itemDTO.getAfterService().replaceAll(imgUrlObj.toString(),""));
			}
			ExecuteResult<ItemDTO> result = itemExportService.modifyItemById(itemDTO);
			if (result.isSuccess()) {
				resultMap.put("result", true);
			} else {
				resultMap.put("result", false);
				if (result.getErrorMessages() != null
						&& result.getErrorMessages().size() > 0) {
					resultMap.put("message", result.getErrorMessages().get(0));
				} else {
					resultMap.put("message","操作失败");
				}
			}
		} catch (Exception e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 检查商品编号是否存在
	 * @param productId
	 * @return
	 */
	@RequestMapping("checkProductId")
	@ResponseBody
	public Map<String,Object> checkProductIdExist(String productId){
		//TODO
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try{
			if(StringUtils.isEmpty(productId.trim())){
				resultMap.put("result", false);
				resultMap.put("message","商品编号不能为空");
				return resultMap;
			}
			ExecuteResult<Integer> result = itemExportService.checkProductIdExist(productId);
			if (result.isSuccess()) {
				resultMap.put("result", true);
			} else {
				resultMap.put("result", false);
				if (result.getErrorMessages() != null
						&& result.getErrorMessages().size() > 0) {
					resultMap.put("message", result.getErrorMessages().get(0));
				} else {
					resultMap.put("message","操作失败");
				}
			}
			
		}catch(Exception e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			e.printStackTrace();
			
		}
		return resultMap;
		
	}
	
	/**
	 * 
	 * <p>Description: [校验商品名称是否重复]</p>
	 * itemId为null时是增加时校验，不为null时为修改时校验
	 * Created on 2015年8月18日
	 * @param itemName
	 * @param itemId
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("validateItemName")
	@ResponseBody
	public ExecuteResult<String> validateItemName(String itemName,Long itemId,HttpServletRequest request){
		ExecuteResult<String> executeResult = new ExecuteResult<String>();
		if (StringUtils.isBlank(itemName)) {
			executeResult.addErrorMessage("商品名称不能为空");
			return executeResult;
		}
		Long shopId = this.getShopId(request);
		if (shopId != null) {
			ExecuteResult<List<ItemDTO>> result = itemExportService.queryItemByItemNameAndShopId(itemName, shopId);
			if (result != null && result.getResult() != null
					&& result.getResult().size() > 0) {
				if (itemId != null) {// 排除现在修改的这个商品名称
					for (ItemDTO itemDTO : result.getResult()) {
						if (itemDTO.getItemStatus()!=null && itemDTO.getItemStatus()!=ItemStatusEnum.DELETED.getCode()
								&& itemDTO.getItemId().longValue() != itemId
								.longValue()) {
							executeResult.addErrorMessage("已有该产品输入");
							return executeResult;
						}
					}
				} else {
					for (ItemDTO itemDTO : result.getResult()) {
						if (itemDTO.getItemStatus()!=null && itemDTO.getItemStatus()!=ItemStatusEnum.DELETED.getCode()) {
							executeResult.addErrorMessage("已有该产品输入");
							break;
						}
					}
				}
			}
		}
		return executeResult;
	}
	
	/**
	 * 判断商品是否存在促销活动
	 * @param request
	 * @param itemIdStr
	 * @return
	 */
	@ResponseBody
	@RequestMapping("activityCheck")
	public String activityCheck(HttpServletRequest request,String itemIdStr){
		Map<String, String> map = new HashMap<String, String>();
		Long shopId = WebUtil.getInstance().getShopId(request);
		String itemIdArray[] = itemIdStr.split(",");
		Long[] itemIds = new Long[itemIdArray.length];
		for(int i = 0 ;i<itemIdArray.length;i++){
			itemIds[i] = new Long(itemIdArray[i]);
		}
		if(promotionFrExportService.queryActivityCheck(shopId,itemIds)){
			map.put("status", "10001"); //已经存在商品促销活动
			map.put("message", "您的商品正在参加促销活动，下架后商品将不能继续销售，促销活动会在您将商品重新上架后继续，请确定是否要下架商品？");
			return JSON.toJSONString(map);
		}
		if(promotionMdExportService.queryActivityCheck(shopId,itemIds)){
			map.put("status", "10001"); //已经存在商品促销活动
			map.put("message", "您的商品正在参加促销活动，下架后商品将不能继续销售，促销活动会在您将商品重新上架后继续，请确定是否要下架商品？");
			return JSON.toJSONString(map);
		}
		if(promotionInfoExportService.queryActivityCheck(shopId)){
			map.put("status", "10001"); //已经存在商品促销活动
			map.put("message", "您的商品正在参加促销活动，下架后商品将不能继续销售，促销活动会在您将商品重新上架后继续，请确定是否要下架商品？");
			return JSON.toJSONString(map);
		}
		map.put("status", "10000");
		return JSON.toJSONString(map);
	}
	
	/**
	 * 商品上下架编辑时校验单品是否存在
	 * @param request
	 * @param itemIdStr
	 * @param addSource
	 * @param status
	 * @return
	 */
	@ResponseBody
	@RequestMapping("emboitementCheck")
	public String emboitementCheck(HttpServletRequest request,String itemIdStr,Integer status){
		Map<String, String> map = new HashMap<String, String>();
		Long shopId = WebUtil.getInstance().getShopId(request);
		String itemIdArray[] = itemIdStr.split(",");
		Long[] itemIds = new Long[itemIdArray.length];
		for(int i = 0 ;i<itemIdArray.length;i++){
			itemIds[i] = new Long(itemIdArray[i]);
			//套装商品
			ExecuteResult<ItemBaseDTO> ext=itemExportService.getItemBaseInfoById(itemIds[i]);
			if(ext.getResult().getAddSource()==ItemAddSourceEnum.COMBINATION.getCode()){//套装上架时判断组合单品是否都是上架状态
				if(status==ItemStatusEnum.SALING.getCode()){
					ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
					dto.setPackageItemId(itemIds[i]);
					List<ItemSkuPackageDTO> lists=itemSkuPackageService.getPackages(dto);
					if(!lists.isEmpty()&&lists.size()>0){
						for(ItemSkuPackageDTO oo:lists){
							ExecuteResult<ItemBaseDTO> res=itemExportService.getItemBaseInfoById(oo.getSubItemId());
							if(res.getResult().getItemStatus()!=ItemStatusEnum.SALING.getCode()){
								map.put("status", "10005"); 
								map.put("message", "当前套装内存在非在售的组合单品，该套装商品上架后将不能被买家购买，是否确定上架？");
								return JSON.toJSONString(map);
							  }
						}
					}
				}
				
			}else{//单品商品
				String str="";
				boolean flag=false;
				if(status==ItemStatusEnum.UNSHELVED.getCode()){//单品下架时，判断是否有组合套装
					str="当前商品正在被套装使用，且该商品下架后，其组成的套装商品将不能被买家购买，是否确定下架？";
					flag=true;
				}else if(status==999){//代表是编辑状态,单品编辑时，判断是否有组合套装
					str="当前商品正在被套装使用，请不要修改、增加、删除商品的销售属性，否则其组成的套装商品将无法被买家购买；如需更改销售属性，请在修改单品后，重新编辑该商品组成的所有套装商品。";
					flag=true;
				}
				if(flag){
					ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
					dto.setSubItemId(itemIds[i]);
					List<ItemSkuPackageDTO> lists=itemSkuPackageService.getPackages(dto);
					if(!lists.isEmpty()&&lists.size()>0){
						map.put("status", "10005");
						map.put("message",str);
						return JSON.toJSONString(map);
					}
				}
			}
	   }
		map.put("status", "10006");
		return JSON.toJSONString(map);
}
	/**
	 * 商品上架/下架
	 * @param itemIds
	 * @param status
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("goodsPublish")
	@ResponseBody
	public Map<String,Object> goodsPublish(String itemIdStr,Integer status,HttpServletRequest request,Model model){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//校验店铺是否关闭,如果关闭则不允许执行上/下架操作
		Long shopId = WebUtil.getInstance().getShopId(request);
        ExecuteResult<ShopDTO> results = shopExportService.findShopInfoById(shopId);
        ShopDTO shop = results.getResult();
        if(shop==null || shop.getStatus()==4 || shop.getRunStatus()==2){//如果店铺状态为关闭则不允许进入店铺信息编辑页面、类目编辑页面和品牌编辑页面
        	resultMap.put("message", "店铺处于关闭状态，不允许执行此操作!");
        	return resultMap;
        }
		ItemStatusModifyDTO itemStatusModifyDTO = new ItemStatusModifyDTO();
		//解析商品id
		String[] itemIds = itemIdStr.split(",");
		List<Long> itemIdList = new ArrayList<Long>();
		for(int i = 0;i<itemIds.length;i++){
			ExecuteResult<ItemDTO> itemDTOResult = this.itemExportService.getItemById(new Long(itemIds[i]));
			//店铺从平台上传商品，当平台该商品为“已下架”状态时，店铺不允许对该商品进行上架操作
			if(status == 4){
				//判断是否是平台上传的商品
				if(itemDTOResult.isSuccess()){
					ItemDTO itemDTO = itemDTOResult.getResult();
					if(itemDTO.getPlstItemId() != null && itemDTO.getAddSource()==ItemAddSourceEnum.PLATFORM.getCode()){
						//获取平台商品
						ExecuteResult<ItemDTO> plantItemDTOResult = this.itemExportService.getItemById(itemDTO.getPlstItemId());
						ItemDTO plantItem = plantItemDTOResult.getResult();
						//平台商品为已删除
						if(plantItem==null){
							resultMap.put("message","商品【"+itemDTO.getItemName()+"】在平台商品库中已被删除,不允许执行上架!如有疑问请联系平台运营人员。");
							return resultMap;
						}
						//平台商品为已下架
						if(plantItem.getItemStatus() == 5){
							resultMap.put("message","商品【"+itemDTO.getItemName()+"】在平台商品库中处于下架状态,不允许执行上架!如有疑问请联系平台运营人员。");
							return resultMap;
						}else if(plantItem.getItemStatus() == 30){//平台商品为已删除
							resultMap.put("message","商品【"+itemDTO.getItemName()+"】在平台商品库中已被删除,不允许执行上架!如有疑问请联系平台运营人员。");
							return resultMap;
						}
					}
				}
			}
			//下架操作
			if(status==5){
    			if(itemDTOResult.isSuccess()){
    				ItemDTO itemDTO = itemDTOResult.getResult();
    				if(itemDTO!=null&&null!=itemDTO.getTimingListing()){
    					itemDTO.setTimingListing(null);
        				itemExportService.updateTimingListing(itemDTO);
    				}
    				DataGrid<ShopRenovationDTO> res=shopRenovationExportService.queryShopRenovationByItemId(new Long(itemIds[i]));
        			if(!res.getRows().isEmpty()){
        				resultMap.put("message","商品【"+itemDTO.getItemName()+"】目前在店铺首页显示，请通过店铺装修在店铺首页移除该商品后再进行下架操作");
    					return resultMap;
        			}
        			// 正在参加集采活动的商品不能下架
        			QueryCentralPurchasingDTO inDTO = new QueryCentralPurchasingDTO();
        			inDTO.setItemId(itemDTO.getItemId());
        			ExecuteResult<DataGrid<QueryCentralPurchasingDTO>> result = centralPurchasingExportService.queryCentralPurchasingActivity(inDTO, null);
        			if (result.isSuccess() 
        					&& result.getResult() != null 
        					&& result.getResult().getRows() != null
        					&& result.getResult().getRows().size() > 0) {
        				for (QueryCentralPurchasingDTO centralPurchasingDTO : result.getResult().getRows()) {
        					if(centralPurchasingDTO.getDetailedStatus() != null 
        							&& (centralPurchasingDTO.getDetailedStatus() == CentralPurchasingActivityRealStatusEnum.INACTIVITY.getCode()
        							|| centralPurchasingDTO.getDetailedStatus() == CentralPurchasingActivityRealStatusEnum.INSIGNUP.getCode()
        							|| centralPurchasingDTO.getDetailedStatus() == CentralPurchasingActivityRealStatusEnum.UNSIGNUP.getCode()
        							|| centralPurchasingDTO.getDetailedStatus() == CentralPurchasingActivityRealStatusEnum.UNACTIVITY.getCode())){
        						resultMap.put("message","商品【"+itemDTO.getItemName()+"】目前正在参加集采活动，请先终止活动后再进行下架操作");
            					return resultMap;
        					}
        				}
        			}
    			}
			}
			itemIdList.add(new Long(itemIds[i]));
		}
		itemStatusModifyDTO.setItemIds(itemIdList);
		itemStatusModifyDTO.setItemStatus(status);
		itemStatusModifyDTO.setUserId(this.getLoginUserId(request));
		itemStatusModifyDTO.setOperator(1);
		ExecuteResult<String> result = itemExportService.modifyItemStatusBatch(itemStatusModifyDTO);
		resultMap.put("message", result.isSuccess() ? "操作成功":"操作失败");
		return resultMap;
	}

	/**
	 * 批量获取商品详细信息
	 * @param itemAdvertisements 规则:商品1,广告词1|商品2,广告词2
	 * @param status
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("getItems")
	@ResponseBody
	public Map<String,Object> getItems(String itemIds,HttpServletRequest request,Model model){
		List<ItemQueryOutDTO> itemDTOList = new ArrayList<ItemQueryOutDTO>();
		String[] itemArray = itemIds.split(",");
		List<String> shopCategoryList = new ArrayList<String>();
		for(int i = 0;i<itemArray.length;i++){
			//获取店铺分类
			ExecuteResult<ItemDTO> result = itemExportService.getItemById(new Long(itemArray[i]));
			ItemDTO itemDTO = result.getResult();
			ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
			shopCategorySellerDTO.setCid(itemDTO.getShopCid());
			ExecuteResult<DataGrid<ShopCategorySellerDTO>> er = this.shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO, null);
			if(er.getResult().getRows() != null && er.getResult().getRows().size() > 0){
				shopCategoryList.add(itemArray[i] + ":" + er.getResult().getRows().get(0).getCname());
			}
			//获取商品基础信息
			ItemQueryInDTO itemInDTO = new ItemQueryInDTO();
			itemInDTO.setId(Integer.parseInt(itemArray[i]));
			DataGrid<ItemQueryOutDTO> dg = itemExportService.queryItemList(itemInDTO, null);
			itemDTOList.add(dg.getRows().get(0));
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("result", itemDTOList);
		result.put("shopCategoryList", shopCategoryList);
		return result;
	}


	/**
	 * 设置广告词
	 * @param itemAdvertisements 规则:商品1,广告词1|商品2,广告词2
	 * @param status
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("itemAdvertisementEdit")
	@ResponseBody
	public Map<String,Object> itemAdvertisementEdit(String itemAdvertisements,HttpServletRequest request,Model model){
		List<ItemAdDTO> ads = new ArrayList<ItemAdDTO>() ;
		//解析前台设置的广告词
		String[] itemAdvertisementArray = itemAdvertisements.split("&");
		for(int i = 0;i<itemAdvertisementArray.length;i++){
			String[] items = itemAdvertisementArray[i].split(",");
			String itemId = items[0];
			String ad = items[1];
			ItemAdDTO itemAdDTO = new ItemAdDTO();
			itemAdDTO.setItemId(new Long(itemId));
			itemAdDTO.setAd(ad);
			ads.add(itemAdDTO);
		}
		ExecuteResult<String> result = this.itemExportService.modifyItemAdBatch(ads);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("message", result.isSuccess() ? "操作成功":"操作失败");
		return resultMap;
	}

	/**
	 * 迁移类目
	 * @param itemAdvertisements
	 * @param status
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("itemShopCategoryEdit")
	@ResponseBody
	public Map<String,Object> itemShopCategoryEdit(String itemIdStr,Long shopCategoryId,HttpServletRequest request,Model model){
		//获取商品最新的店铺类目
		List<ItemShopCidDTO> itemShopCidDTOList = new ArrayList<ItemShopCidDTO>();
		String[] itemIds = itemIdStr.split(",");
		for(int i = 0;i<itemIds.length;i++){
			ItemShopCidDTO itemShopCidDTO = new ItemShopCidDTO();
			itemShopCidDTO.setItemId(new Long(itemIds[i]));
			itemShopCidDTO.setShopCid(shopCategoryId);
			itemShopCidDTOList.add(itemShopCidDTO);
		}
		//执行修改
		this.itemExportService.modifyItemShopCidBatch(itemShopCidDTOList);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("message", true ? "操作成功":"操作失败");
		return resultMap;
	}

	/**
	 *
	 * <p>Discription:商品管理-商品库存</p>
	Created on 2015年3月4日
	 * @return
	 * @author:[xueyn]
	 */
	@RequestMapping("stock")
	public String goodsStock(){
		return "sellcenter/sellgoods/goodsStock";
	}

	/**
	 * 获取店铺分类
	 * @return
	 */
	@RequestMapping("getShopCategory")
	@ResponseBody
	public Map<String,Object> getShopCategory(ShopCategorySellerDTO shopCategorySellerDTO){
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO, null);
		List<ShopCategorySellerDTO> list = result.getResult().getRows();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("result", list);
		return resultMap;
	}

	/**
	 * 获取平台分类
	 * @return
	 */
	@RequestMapping("getItemCategorys")
	@ResponseBody
	public Map<String,Object> getItemCategoryList(Long levelOneId,Long levelTwoId,Integer level,HttpServletRequest request){
		List<ItemCatCascadeDTO> resultItemCatCascadeList = new ArrayList<ItemCatCascadeDTO>();
		//获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		//通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> result =  itemCategoryService.queryParentCategoryList(categoryIds);
		List<ItemCatCascadeDTO> itemCatCascadeDTOList = result.getResult();
		switch(level){
			case 1:
				//获取二级类目
				for(ItemCatCascadeDTO itemCatCascadeDTO:itemCatCascadeDTOList){
					if(levelOneId.compareTo(itemCatCascadeDTO.getCid()) == 0){
						resultItemCatCascadeList = itemCatCascadeDTO.getChildCats();
					}
				}
				break;
			case 2:
				//获取二级分类
				List<ItemCatCascadeDTO> itemCatCascadeListTwo = new ArrayList<ItemCatCascadeDTO>();
				for(ItemCatCascadeDTO itemCatCascadeDTO:itemCatCascadeDTOList){
					if(levelOneId.compareTo(itemCatCascadeDTO.getCid()) == 0){
						//获取二级分类
						itemCatCascadeListTwo = itemCatCascadeDTO.getChildCats();
					}
				}
				//获取三级分类
				//遍历二级分类获取三级分类
				for(ItemCatCascadeDTO itemCatCascadeDTOTwo : itemCatCascadeListTwo){
					if(levelTwoId.compareTo(itemCatCascadeDTOTwo.getCid()) == 0){
						resultItemCatCascadeList = itemCatCascadeDTOTwo.getChildCats();
					}
				}
				break;
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("result", resultItemCatCascadeList);
		return resultMap;
	}


	/**
	 * 获取店铺经营品牌
	 * @param cid
	 * @param request
	 * @return
	 */
	@RequestMapping("getShopBrands")
	@ResponseBody
	public Map<String,Object> getShopBrands(ShopBrandDTO shopBrandDTO,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<ItemBrandDTO> itemBrandDTOList = new ArrayList<ItemBrandDTO>();
		//获取店铺id
		shopBrandDTO.setShopId(this.getShopId(request));
		//审核通过
		shopBrandDTO.setStatus(2);
		ExecuteResult<DataGrid<ShopBrandDTO>> executeResult = this.shopBrandExportService.queryShopBrand(shopBrandDTO, null);
		if(executeResult.isSuccess()){
			List<ShopBrandDTO> shopBrandDTOList = executeResult.getResult().getRows();
			for(ShopBrandDTO shopBrandDTOEvery:shopBrandDTOList){
				ExecuteResult<List<ItemBrandDTO>> result = itemBrandExportService.queryItemBrandByIds(shopBrandDTOEvery.getBrandId());
				if(result.isSuccess() && result.getResult() != null && result.getResult().size() > 0)
					itemBrandDTOList.add(result.getResult().get(0));
			}
		}
		resultMap.put("result", itemBrandDTOList);
		return resultMap;
	}

	/**
	 * 获取平台销售属性
	 * @param cid
	 * @param request
	 * @return
	 */
	@RequestMapping("getPlantformCategory")
	@ResponseBody
	public Map<String,Object> getPlantformCategory(String cid,String plantItemId,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(StringUtils.isEmpty(plantItemId)){
			DataGrid<CategoryAttrDTO> dg = this.itemCategoryService.queryCategoryAttrList(new Long(cid), 2);
			resultMap.put("result", dg.getRows());
		}else{
			ExecuteResult<List<ItemAttr>> result = this.itemCategoryService.addCatAttrByKeyValsBak(cid);
					//.queryCatAttrByKeyVals(cid);
			if(result.isSuccess()){
				resultMap.put("result", result.getResult());
			}
		}
		return resultMap;
	}
	/**
	* 获取平台类目属性
	* @param cid
	* @param request
	* @return
	*/
	@RequestMapping("getPlantformAttribute")
	@ResponseBody
	public Map<String,Object> getPlantformAttribute(String cid,String plantItemId,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		if(StringUtils.isEmpty(plantItemId)){
			DataGrid<CategoryAttrDTO> dg = this.itemCategoryService.queryCategoryAttrList(new Long(cid), 2);
			resultMap.put("result", dg.getRows());
		}else{
			ExecuteResult<List<ItemAttr>> result = this.itemCategoryService.queryCatAttrByKeyVals(cid);
					//.queryCatAttrByKeyVals(cid);
			if(result.isSuccess()){
				resultMap.put("result", result.getResult());
			}
		}
		return resultMap;
	}
	/**
	 * 获取店铺销售属性
	 * @return
	 */
	@RequestMapping("getShopSellAttribute")
	@ResponseBody
	public Map<String,Object> getShopSellAttribute(CatAttrSellerDTO catAttrSellerDTO,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		//商家ID
		catAttrSellerDTO.setSellerId(this.getLoginUserId(request));
		//店铺ID
		catAttrSellerDTO.setShopId(this.getShopId(request));
		catAttrSellerDTO.setAttrType(1);
		//查询店铺销售属性、非销售属性
		ExecuteResult<List<ItemAttr>> result = itemAttributeExportService.addItemAttrValueBack(catAttrSellerDTO, 1);
				//.queryCatAttrSellerList(catAttrSellerDTO);
		if(result.isSuccess()){
			resultMap.put("result", result.getResult());
		}
		return resultMap;
	}
	
	/**
	 * 获取商品销售属性、非销售属性
	 * @param itemId 商品id
	 * @return 执行结果Map
	 */
	@RequestMapping("getItemSaleAttrs")
	@ResponseBody
	public Map<String,Object> getItemSaleAttrs(ItemAttrValueItemDTO itemAttrValueItemDTO){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("message", "查询失败");
		if(itemAttrValueItemDTO == null){
			return resultMap;
		}
		ExecuteResult<List<ItemAttr>> result = this.itemAttrValueItemExportService.queryItemAttrValueItem(itemAttrValueItemDTO);
				//.queryItemAttrValueItem(itemAttrValueItemDTO);
		if(!result.isSuccess()){
			return resultMap;
		}
		resultMap.put("message", "查询成功");
		resultMap.put("result", result.getResult());
		return resultMap;
	}

	/**
	 * 添加店铺商品销售属性值
	 * @return
	 */
	@RequestMapping("saveShopSellAttribute")
	@ResponseBody
	public Map<String,Object> saveShopSellAttribute(CatAttrSellerDTO catAttrSellerDTO,Long cid,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("message", "保存失败");
		ItemAttr itemAttr = catAttrSellerDTO.getAttr();
		if(itemAttr == null){
			return resultMap;
		}
		Long itemAttrId = itemAttr.getId();
		//定义添加方式 attrValue:添加销售属性值   attr:添加销售属性
		String saveType = "attrValue";
		if(itemAttrId == null){
			//先保存销售属性
			ExecuteResult<ItemAttr> result = itemAttributeExportService.addItemAttribute(itemAttr);
			if(!result.isSuccess()){
				return resultMap;
			}
			itemAttrId = result.getResult().getId();
			itemAttr.setId(itemAttrId);
			saveType = "attr";
		}
		//保存销售属性
		List<ItemAttrValue> itemAttrValueList = itemAttr.getValues();
		if(itemAttrValueList == null || itemAttrValueList.size() == 0){
			return resultMap;
		}
		//存放保存之后的销售属性，供前台展示使用
		List<ItemAttrValue> itemAttrValueSaveList = new ArrayList<ItemAttrValue>();
		for(ItemAttrValue itemAttrValue : itemAttrValueList){
			if(StringUtils.isEmpty(itemAttrValue.getName())){
				continue;
			}
			itemAttrValue.setAttrId(itemAttrId);
			ExecuteResult<ItemAttrValue> result = this.itemAttributeExportService.addItemAttrValue(itemAttrValue);
			if(!result.isSuccess()){
				return resultMap;
			}
			itemAttrValueSaveList.add(result.getResult());
		}
		itemAttr.setValues(itemAttrValueSaveList);
		resultMap.put("message", "保存成功");
		resultMap.put("itemAttr", itemAttr);
		resultMap.put("saveType", saveType);
		return resultMap;
	}
	
	/**
	 * 编辑 销售属性/销售属性值
	 * @param name 属性/属性值名称
	 * @param id 属性/属性值 id
	 * @param attrType 1:属性 2：属性值  
	 * @return resultMap 执行结果 
	 * 
	 */
	@RequestMapping("modifySaleAttr")
	@ResponseBody
	public Map<String,Object> modifySaleAttr(String name,Long id,String attrType){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		boolean resultBoo = false;
		resultMap.put("result",resultBoo);
		if("1".equals(attrType)){
			//销售属性
			ItemAttr itemAttr = new ItemAttr();
			itemAttr.setId(id);
			itemAttr.setName(name);
			ExecuteResult<ItemAttr> result = itemAttributeExportService.modifyItemAttribute(itemAttr);
			resultBoo = result.isSuccess();
		}else{
			//销售属性值
			ItemAttrValue itemAttrValue = new ItemAttrValue();
			itemAttrValue.setId(id);
			itemAttrValue.setName(name);
			ExecuteResult<ItemAttrValue> result = itemAttributeExportService.modifyItemAttrValue(itemAttrValue);
			resultBoo = result.isSuccess();
		}
		resultMap.put("result",resultBoo);
		return resultMap;
	}
	
	
	/**
	 * 删除 销售属性/销售属性值
	 * @param name 属性/属性值名称
	 * @param id 属性/属性值 id
	 * @param attrType 1:属性 2：属性值  
	 * @return resultMap 执行结果 
	 * 
	 */
	@RequestMapping("deleteSaleAttr")
	@ResponseBody
	public Map<String,Object> deleteSaleAttr(String name,Long id,String attrType){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		boolean resultBoo = false;
		if("1".equals(attrType)){
			//销售属性
			ItemAttr itemAttr = new ItemAttr();
			itemAttr.setId(id);
			itemAttr.setName(name);
			ExecuteResult<ItemAttr> result = itemAttributeExportService.deleteItemAttribute(itemAttr);
			resultBoo = result.isSuccess();
		}else{
			//销售属性值
			ItemAttrValue itemAttrValue = new ItemAttrValue();
			itemAttrValue.setId(id);
			itemAttrValue.setName(name);
			ExecuteResult<ItemAttrValue> result = itemAttributeExportService.deleteItemAttrValue(itemAttrValue);
			resultBoo = result.isSuccess();
		}
		resultMap.put("result",resultBoo);
		return resultMap;
	}
	
	

	/**
	 * 获取店铺销售属性
	 * @return
	 */
	@RequestMapping("getItemCategoryByParent")
	@ResponseBody
	public Map<String,Object> getItemCategoryByParent(Long categoryParentCid,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		DataGrid<ItemCategoryDTO> dataGrid = itemCategoryService.queryItemCategoryList(categoryParentCid);
		List<ItemCategoryDTO> itemCategoryDTOList = dataGrid.getRows();
		resultMap.put("result",itemCategoryDTOList);
		return resultMap;
	}


	/**
	 * 添加店铺商品销售属性值
	 * @return
	 */
	@RequestMapping("getSkuList")
	@ResponseBody
	public Map<String,Object> getSkuList(String skuStr,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List dimvalue = JSON.parseArray(skuStr);
		List<String> skuList = new ArrayList<String>();
		this.skuList(dimvalue, skuList, 0, "");
		if(skuList.size()>Constants.LIMIT_SKU_NUM){
			resultMap.put("flag",true);
			resultMap.put("maxNum",Constants.LIMIT_SKU_NUM);
		}
		resultMap.put("skuList", skuList);
		return resultMap;
	}

	/**
	 * 获取登录用户Id
	 * @param request
	 * @return
	 */
	private Long getLoginUserId(HttpServletRequest request){
		//获取登录用户
		return WebUtil.getInstance().getUserId(request);
	}

	/**
	 * 获取店铺id
	 * @param request
	 * @return
	 */
	private Long getShopId(HttpServletRequest request){
		return WebUtil.getInstance().getShopId(request);
	}

	/**
	 * 获取一级店铺分类
	 * @return
	 */
	private List<ShopCategorySellerDTO> getLevelOneShopCategory(HttpServletRequest request){
		//获取店铺id
		ShopCategorySellerDTO shopCategorySellerDTO_ = new ShopCategorySellerDTO();
		shopCategorySellerDTO_.setShopId(getShopId(request));
		shopCategorySellerDTO_.setLev(1);
		//获取一级店铺分类
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> shopCategoryResult = this.shopCategorySellerExportService.queryShopCategoryList(shopCategorySellerDTO_, null);
		return shopCategoryResult.getResult().getRows();
	}

	/**
	 * sku算法
	 * @param dimvalue
	 * @param result
	 * @param layer
	 * @param curstring
	 */
	private void skuList(List<List<String>> dimvalue, List<String> result, int layer, String curstring) {
		//大于一个集合时：
		if (layer < dimvalue.size() - 1) {
			//大于一个集合时，第一个集合为空
			if (dimvalue.get(layer).size() == 0)
				skuList(dimvalue, result, layer + 1, curstring);
			else {
				for (int i = 0; i < dimvalue.get(layer).size(); i++) {
					StringBuilder s1 = new StringBuilder();
					s1.append(curstring);
					s1.append(dimvalue.get(layer).get(i));
					s1.append(",");
					skuList(dimvalue, result, layer + 1, s1.toString());
				}
			}
		}
		//只有一个集合时：
		else if (layer == dimvalue.size() - 1) {
			//只有一个集合，且集合中没有元素
			if (dimvalue.get(layer).size() == 0)
				result.add(curstring);
			//只有一个集合，且集合中有元素时：其笛卡尔积就是这个集合元素本身
			else {
				for (int i = 0; i < dimvalue.get(layer).size(); i++) {
					result.add(curstring + dimvalue.get(layer).get(i));
				}
			}
		}
	}
	/**
	 * 根据第三级类目获取平台一二级类目
	 * 
	 * @param cid 平台三级类目id
	 */
	@SuppressWarnings("null")
	private Map<String,String> getCategoryName(long cid){
		Map<String,String> resultMap = new HashMap<String,String>();
		 //获取三级类目的父类目信息
        ExecuteResult<List<ItemCatCascadeDTO>> resultCategory = itemCategoryService.queryParentCategoryList(cid);
        //如果获取类目不成功
        //遍历父类类目，获取到一级类目和二级类目、三级类目
        for(ItemCatCascadeDTO itemCatCascadeDTOOne :resultCategory.getResult()){
        	for(ItemCatCascadeDTO itemCatCascadeDTOTwo :itemCatCascadeDTOOne.getChildCats()){
        		for(ItemCatCascadeDTO itemCatCascadeDTOThree :itemCatCascadeDTOTwo.getChildCats()){
        			if(cid==itemCatCascadeDTOThree.getCid()){
						resultMap.put("threeName", itemCatCascadeDTOThree.getCname());
						resultMap.put("oneName", itemCatCascadeDTOOne.getCname());
						resultMap.put("twoName", itemCatCascadeDTOTwo.getCname());
						break ;
					}
        		}
        	}
        	
        }
        return resultMap;
	}
	/**
	 * 查询当前店铺的在售商品
	 * @param request
	 * @param page
	 * @param model
	 * @param itemInDTO
	 * @return
	 */
	@RequestMapping("querySaleGoods")
	public String querySaleGoods(HttpServletRequest request,Integer page,Model model, ItemQueryInDTO itemInDTO){
		 String aux_checkedItemIds=request.getParameter("aux_checkedItemIds");//商品分页选中的商品id
		 String added_checkedItemIds=request.getParameter("added_checkedItemIds");//商品分页选中的商品id
		 String gene_checkedItemIds=request.getParameter("gene_checkedItemIds");//商品分页选中的商品id
		 String basis_checkedItemIds=request.getParameter("basis_checkedItemIds");//商品分页选中的商品id
		 String checkedItemType=request.getParameter("checkedItemType");//商品分页选中的商品id
		// 获取店铺id
		long shopId = WebUtil.getInstance().getShopId(request);
		Long[] shopIds=new Long[1];
		shopIds[0]=shopId;
		itemInDTO.setShopIds(shopIds);
		Pager pager = new Pager();
		if (page == null) {
			page = 1;
		}
		//获取店铺经营的平台分类
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		//通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> ItemCatCascadeDTOResult =  itemCategoryService.queryParentCategoryList(categoryIds);
		//将分类列表转换为map形式
		Map<String,String> categoryMap = new HashMap<String,String>();
		for(ItemCatCascadeDTO itemCatCascadeOne : ItemCatCascadeDTOResult.getResult()){
			for(ItemCatCascadeDTO itemCatCascadeTwo:itemCatCascadeOne.getChildCats()){
				for(ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo.getChildCats()){
					categoryMap.put(itemCatCascadeThree.getCid().toString(),itemCatCascadeOne.getCname() + "/" 
								+ itemCatCascadeTwo.getCname() + "/" + itemCatCascadeThree.getCname());
				}
			}
		}
		pager.setPage(page);
		pager.setSort("created");
		pager.setOrder("desc");
		//默认为在售状态
		itemInDTO.setItemStatus(4);
		itemInDTO.setHasPrice(1);//查询有价格的商品
		//itemInDTO.setAddSource(ItemAddSourceEnum.NORNAL.getCode());
        itemInDTO.setStartTime(null);
        itemInDTO.setEndTime(null);
		DataGrid<ItemQueryOutDTO> listItems=itemExportService.queryItemList(itemInDTO, pager);
		List<ItemQueryOutDTO> itemQueryOutDTOList = listItems.getRows();
		JSONArray array = new JSONArray();
		if(itemQueryOutDTOList != null){
			for(ItemQueryOutDTO itemQueryOutDTO : itemQueryOutDTOList){
				itemQueryOutDTO.setcName(categoryMap.get(itemQueryOutDTO.getCid().toString()));
				itemQueryOutDTO.setAttrSales(JSON.toJSONString(itemQueryOutDTO.getAttrSale()).replace("\"", "'"));
				itemQueryOutDTO.setAttrSaleStr(JSON.toJSONString(itemQueryOutDTO.getSkuInfos()).replace("\"", "'"));
			}
		}
		pager.setTotalCount(listItems.getTotal().intValue());
		pager.setRecords(itemQueryOutDTOList);
		model.addAttribute("pager", pager);
		model.addAttribute("itemInDTO", itemInDTO);
		model.addAttribute("checkedItemType", checkedItemType);
		model.addAttribute("aux_checkedItemIds", aux_checkedItemIds);
		model.addAttribute("added_checkedItemIds", added_checkedItemIds);
		model.addAttribute("gene_checkedItemIds", gene_checkedItemIds);
		model.addAttribute("basis_checkedItemIds", basis_checkedItemIds);
		return "/sellcenter/sellgoods/queryItems";
	}
	/**
	 * 获取套装组合sku
	 * @param skuStr
	 * @return
	 */
	@RequestMapping("getGroupSkuList")
	@ResponseBody
    public Map<String,Object> getGroupSkuList(String skuStr){
		List dimvalue = JSON.parseArray(skuStr);
		List<List<String>> recursiveResult = new ArrayList<List<String>>(); 
	    circulate(dimvalue, recursiveResult);
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	resultMap.put("lists", recursiveResult);
    	return resultMap;
    }
    /**
     * 笛卡尔积算法
     * @param dimValue
     * @param result
     */
    private static void circulate (List<List<String>> dimValue, List<List<String>> result) {  
        int total = 1;  
        for (List<String> list : dimValue) {  
            total *= list.size();  
        }  
        String[] myResult = new String[total];  
        int itemLoopNum = 1;  
        int loopPerItem = 1;  
        int now = 1;  
        for (List<String> list : dimValue) {  
            now *= list.size();  
            int index = 0;  
            int currentSize = list.size();  
            itemLoopNum = total / now;  
            loopPerItem = total / (itemLoopNum * currentSize);  
            int myIndex = 0;  
            for (String string : list) {  
                for (int i = 0; i < loopPerItem; i++) {  
                    if (myIndex == list.size()) {  
                        myIndex = 0;  
                    }  
                    for (int j = 0; j < itemLoopNum; j++) {  
                        myResult[index] = (myResult[index] == null? "" : myResult[index] + ",") + list.get(myIndex);  
                        index++;  
                    }  
                    myIndex++;  
                }  
            }  
        }  
        List<String> stringResult = Arrays.asList(myResult);  
        for (String string : stringResult) {  
            String[] stringArray = string.split(",");  
            result.add(Arrays.asList(stringArray));  
        }  
    } 
    /**
     * 
     * @param itemId
     * @return
     */
    public List<ItemSkuPackageDTO> getSubItemId(Long itemId){
    	ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
    	dto.setPackageItemId(itemId);
    	List<ItemSkuPackageDTO> lists=itemSkuPackageService.getPackages(dto);
    		List<Long> listTemp= new ArrayList<Long>();  
    		 Iterator<ItemSkuPackageDTO> it=lists.iterator(); 
    		 while(it.hasNext()){  
    			 ItemSkuPackageDTO a=it.next(); 
    				 if(listTemp.contains(a.getSubItemId())){  
    		    		   it.remove();  
    		    		  }  
    		    		  else{  
    		    		   listTemp.add(a.getSubItemId());  
    		    		  }  
      }  
    	return lists;
    }
    /**
     * 套装商品编辑查询商品
     * @param itemId
     * @return
     */
    @RequestMapping("getSubPackage")
	@ResponseBody
    public Map<String,Object> getSubPackage(String itemId){
    	Map<String,Object> resultMap = new HashMap<String,Object>();
    	List<ItemSkuPackageDTO> lists=this.getSubItemId(Long.parseLong(itemId));
    	List<ItemDTO> items=new ArrayList<ItemDTO>();
    	List<ItemDTO> itemDtos=new ArrayList<ItemDTO>();
    	Map<String,Object> map=new HashMap<String, Object>();
    	for(ItemSkuPackageDTO itemSkuPackageDTO : lists){
    		if(itemSkuPackageDTO.getAddSource()==ItemAddSourceEnum.NORMAL.getCode()){
    			ExecuteResult<ItemDTO> res=itemExportService.getItemById(itemSkuPackageDTO.getSubItemId());
        		res.getResult().setAttrSaleStr(JSON.toJSONString(res.getResult().getAttrSale()).replace("\"", "'"));
        		res.getResult().setAttributesStr(JSON.toJSONString(res.getResult().getSkuInfos()).replace("\"", "'"));
        		items.add(res.getResult());
    		}else{
    			ExecuteResult<ItemDTO> res=itemExportService.getItemById(itemSkuPackageDTO.getSubItemId());
        		res.getResult().setAttrSaleStr(JSON.toJSONString(res.getResult().getAttrSale()).replace("\"", "'"));
        		res.getResult().setAttributesStr(JSON.toJSONString(res.getResult().getSkuInfos()).replace("\"", "'"));
        		itemDtos.add(res.getResult());
    		}
    		
    	}
    	
    	ExecuteResult<ItemDTO> rt=itemExportService.getItemById(Long.parseLong(itemId));
    	rt.getResult().setAttributesStr(JSON.toJSONString(rt.getResult().getSkuInfos()).replace("\"", "'"));
    	ItemSkuPackageDTO dto=new ItemSkuPackageDTO();
    	dto.setPackageItemId(Long.parseLong(itemId));
    	List<ItemSkuPackageDTO> arr=itemSkuPackageService.getPackages(dto);
    	
    	resultMap.put("result", items);
    	resultMap.put("itemDtos", itemDtos);
    	resultMap.put("itme",rt.getResult());
    	resultMap.put("lists",arr);
    	return resultMap;
    }
       
}
