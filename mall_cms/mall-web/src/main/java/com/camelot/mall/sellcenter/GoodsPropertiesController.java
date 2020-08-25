package com.camelot.mall.sellcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.CatAttrSellerDTO;
import com.camelot.goodscenter.dto.ItemAttr;
import com.camelot.goodscenter.dto.ItemAttrValue;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/sellcenter/goodsProperties")
public class GoodsPropertiesController {

	private Long shopId;

	@Resource
	private UserExportService userExportService;

	@Resource
	private ShopCategoryExportService shopCategoryExportService;

	@Resource
	private ItemCategoryService itemCategoryService;

	@RequestMapping("assortment")
	public String assortment(HttpServletRequest request, Model model){
		Long userId = WebUtil.getInstance().getUserId(request);
		UserDTO userDTO = userExportService.queryUserById(userId);
		Long parentId = userDTO.getParentId();
		if(parentId!=null && parentId.longValue()>0){
			model.addAttribute("userId",parentId);
		}else{
			model.addAttribute("userId",userId);
		}
		shopId = WebUtil.getInstance().getShopId(request);
		model.addAttribute("shopId",shopId);

		//获取平台分类
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for(ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO.getResult()){
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		ExecuteResult<List<ItemCatCascadeDTO>> firstAssortmentList =  itemCategoryService.queryParentCategoryList(categoryIds);
		model.addAttribute("firstAssortmentList",firstAssortmentList.getResult());
		return "sellcenter/sellgoods/goodsProperties";
	}

	@ResponseBody
	@RequestMapping("goodsProperty")
	public Map<String,Object> goodsProperty(CatAttrSellerDTO catAttrSellerDTO,Model model){
		Map<String,Object> map = new HashMap<String,Object>();
		ExecuteResult<List<ItemAttr>> goodsProperty = itemCategoryService.queryCatAttrSellerList(catAttrSellerDTO);
		map.put("goodsProperty", goodsProperty);
		return map;
	}

	@ResponseBody
	@RequestMapping("saveGoodsProperty")
	public Map<String,Object> saveGoodsProperty(CatAttrSellerDTO catAttrSellerDTO,
			ItemAttr itemAttr,String valueArray){

		Map<String,Object> map = new HashMap<String,Object>();

		//属性类型:1:销售属性;2:非销售属性 必填
		catAttrSellerDTO.setAttrType(1);

		//增加卖家商品类目【属性】
		if(itemAttr.getId()==null || itemAttr.getId().longValue()==0){
			catAttrSellerDTO.setAttr(itemAttr);
			ExecuteResult<ItemAttr> itemAttrResult = itemCategoryService.addItemAttrSeller(catAttrSellerDTO);
			itemAttr = itemAttrResult.getResult();
			//map.put("itemAttr", itemAttr);
		}
		List<ItemAttrValue> values = new ArrayList<ItemAttrValue>();
		//增加卖家商品类目【属性值】
		if(!valueArray.isEmpty()){
			map.put("itemAttrValueIsNotNull", true);
			String[] itemAttrValueArray = valueArray.split(",");
			for(int i=0; i < itemAttrValueArray.length; i++){
				ItemAttrValue itemAttrValue = new ItemAttrValue();
				itemAttrValue.setAttrId(itemAttr.getId());
				itemAttrValue.setName(itemAttrValueArray[i]);
				catAttrSellerDTO.setAttrValue(itemAttrValue);
				ExecuteResult<ItemAttrValue> result = itemCategoryService.addItemAttrValueSeller(catAttrSellerDTO);
				if(result.isSuccess()){
					values.add(result.getResult());
				}
			}
		}else{
			map.put("itemAttrValueIsNotNull", false);
		}
		itemAttr.setValues(values);
		map.put("itemAttr", itemAttr);
		return map;
	}

	/**
	 * 获取平台分类
	 * @return
	 */
	@RequestMapping("getItemCategorys")
	@ResponseBody
	public Map<String,Object> getItemCategoryList(Long levelOneId,Long levelTwoId,Integer level,Long shopId){
		List<ItemCatCascadeDTO> resultItemCatCascadeList = new ArrayList<ItemCatCascadeDTO>();
		//获取店铺经营的平台分类
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = shopCategoryExportService.queryShopCategoryList(shopId,ShopCategoryStatus.PASS.getCode());
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
					if(levelOneId.longValue() == itemCatCascadeDTO.getCid().longValue()){
						resultItemCatCascadeList = itemCatCascadeDTO.getChildCats();
					}
				}
				break;
			case 2:
				//获取二级分类
				List<ItemCatCascadeDTO> itemCatCascadeListTwo = new ArrayList<ItemCatCascadeDTO>();
				for(ItemCatCascadeDTO itemCatCascadeDTO:itemCatCascadeDTOList){
					if(levelOneId.longValue() == itemCatCascadeDTO.getCid().longValue()){
						//获取二级分类
						itemCatCascadeListTwo = itemCatCascadeDTO.getChildCats();
					}
				}
				//获取三级分类
				//遍历二级分类获取三级分类
				for(ItemCatCascadeDTO itemCatCascadeDTOTwo : itemCatCascadeListTwo){
					if(levelTwoId.longValue() == itemCatCascadeDTOTwo.getCid().longValue()){
						resultItemCatCascadeList = itemCatCascadeDTOTwo.getChildCats();
					}
				}
				break;
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("result", resultItemCatCascadeList);
		return resultMap;
	}

}
