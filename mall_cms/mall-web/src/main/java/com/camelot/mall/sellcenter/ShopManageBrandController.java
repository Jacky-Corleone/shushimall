package com.camelot.mall.sellcenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.storecenter.dto.ShopCategoryDTO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.BrandOfShopDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.service.ShopClientService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopModifyInfoExportService;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [店铺经营类目管理]</p>
 * Created on 2015年3月6日
 * @author  <a href="mailto: zhoule@camelotchina.com">周 乐</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/shopManageBrandController")
public class ShopManageBrandController {
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Resource
	private ShopBrandExportService shopBrandExportService;
	
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	
	@Resource 
	private ItemBrandExportService itemBrandExportService;
	
	@Resource
	private ShopClientService shopClientService;
	
	@Resource
	private ShopModifyInfoExportService shopModifyInfoExportService;
	
	@Resource
	private ItemExportService itemExportService;
	
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request, Model model){
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		/**根据品牌id查询品牌信息，并组装品牌logo的图片地址start*/
		ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList = getShopBrandList(shopId, ShopCategoryStatus.PASS.getCode());
		Long[] brandIds = shopClientService.buildBrandIds(shopBrandList);
		ExecuteResult<List<ItemBrandDTO>> brandlist = itemBrandExportService.queryItemBrandByIds(brandIds);
		/**根据品牌id查询品牌信息，并组装品牌logo的图片地址start*/
		
        ExecuteResult<List<ShopCategoryDTO>> applyedCatList = shopCategoryExportService.queryShopCategoryList(shopId, 2);//查询店铺审核通过的三级类目列表
        ExecuteResult<List<ItemCatCascadeDTO>> categorylist = itemCategoryService.queryParentCategoryList(shopClientService.buildShopCategoryIds(applyedCatList.getResult()));

		//被驳回的店铺品牌列表
		ExecuteResult<DataGrid<ShopBrandDTO>> rejectedShopBrandList = getShopBrandList(shopId, ShopCategoryStatus.REJECT.getCode());
		//被驳回的平台品牌列表【对应店铺品牌列表】
		List<ItemBrandDTO> rejectedPlatformBrandList = getPlatformBrandList(shopId, ShopCategoryStatus.REJECT.getCode());
		//组装被驳回的原因
		Map<String, String> rejectedBrandList = buildRejected(rejectedShopBrandList,rejectedPlatformBrandList);
		model.addAttribute("levOneCategoryList", categorylist.getResult());//平台经营的类目
		model.addAttribute("shopBrandList", brandlist.getResult());	//当前店铺经营的所有品牌信息
		model.addAttribute("rejectedBrandList", rejectedBrandList);	//被驳回的品牌信息
		
		return "/sellcenter/shop/shopManageBrandEdit";
	}

	/**
	 * 组装被驳回的原因 
	 * @param rejectedShopBrandList 店铺被驳回品牌列表数据
	 * @param rejectedPlatformBrandList 平台被驳回的品牌列表数据
	 * @return
	 */
	private Map<String, String> buildRejected(
			ExecuteResult<DataGrid<ShopBrandDTO>> rejectedShopBrandList,
			List<ItemBrandDTO> rejectedPlatformBrandList) {
		Map<String, String> result = new HashMap<String, String>();
		if(null==rejectedPlatformBrandList || rejectedPlatformBrandList.size()<=0){
			return result;
		}
		List<ShopBrandDTO> shopRejList = rejectedShopBrandList.getResult().getRows();
		Map<String, String> platformItemMap = new HashMap<String, String>();
        for(int i=0; i<rejectedPlatformBrandList.size(); i++){
			ItemBrandDTO platRej = rejectedPlatformBrandList.get(i);
            platformItemMap.put(platRej.getBrandId() + "", platRej.getBrandName());
		}
        for(int j=0; j<shopRejList.size(); j++){
            ShopBrandDTO shopRej = shopRejList.get(j);
            if(platformItemMap.containsKey(shopRej.getBrandId().toString())){
                result.put(platformItemMap.get(shopRej.getBrandId().toString()), shopRej.getComment());
            }
        }
        return result;
	}

	@RequestMapping("toView")
	public String toView(HttpServletRequest request, Model model){
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		List<ItemBrandDTO> busShopBrandList = getPlatformBrandList(shopId, ShopCategoryStatus.PASS.getCode());
		List<ItemBrandDTO> auditShopBrandList = getPlatformBrandList(shopId, ShopCategoryStatus.APPLY.getCode());
		
		model.addAttribute("busShopBrandList", busShopBrandList);	//当前店铺正在经营的所有品牌信息
		model.addAttribute("auditShopBrandList", auditShopBrandList);	//当前店铺正在审核的所有品牌信息
		
		return "/sellcenter/shop/shopManageBrandView";
	}
	
	/**
	 * 获取平台品牌列表
	 * @param shopId
	 * @param status
	 * @return
	 */
	public List<ItemBrandDTO> getPlatformBrandList(Long shopId, Integer status){
		ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList = getShopBrandList(shopId, status);
		Long[] brandIds = shopClientService.buildBrandIds(shopBrandList);
		ExecuteResult<List<ItemBrandDTO>> brandlist = itemBrandExportService.queryItemBrandByIds(brandIds);
		
		return brandlist.getResult();
	}

	/**
	 * 查询店铺审核通过的正在经营的品牌列表
	 * @param shopId
	 * @param status
	 * @return
	 */
	private ExecuteResult<DataGrid<ShopBrandDTO>> getShopBrandList(Long shopId, Integer status) {
		ShopBrandDTO shopBrandDTO = new ShopBrandDTO();
		shopBrandDTO.setShopId(shopId);
		shopBrandDTO.setStatus(status);
		ExecuteResult<DataGrid<ShopBrandDTO>> shopBrandList = shopBrandExportService.queryShopBrandAll(shopBrandDTO, null);
		return shopBrandList;
	}
	
	/**根据父级id查询类目列表*/
	@RequestMapping("getCategoryByParentId")
	@ResponseBody
	public List<ItemCategoryDTO> getCategoryByParentId(Long parentCode, HttpServletResponse response){
		//查询平台一级类目
		if(null == parentCode){
			return null;
		}else{
			DataGrid<ItemCategoryDTO> categorylist = itemCategoryService.queryItemCategoryList(parentCode);
			return categorylist.getRows();
		}
	}
	
	/**根据三级类目id查询品牌列表*/
	@ResponseBody
	@RequestMapping("/queryBrandByCid")
	public List<ItemBrandDTO> queryBrandByCid(Long cid, HttpServletRequest request) {
		BrandOfShopDTO brandOfShopDTO = new BrandOfShopDTO();
        brandOfShopDTO.setThirdCid(cid);
        DataGrid<ItemBrandDTO> datagrid = itemBrandExportService.queryItemBrandList(brandOfShopDTO);
        //1、查询出三级类目id对应的平台中所有的品牌信息
        List<ItemBrandDTO> platformBrandList = datagrid.getRows();
		
		Long shopId = WebUtil.getInstance().getShopId(request);
		if(null == shopId){//卖家认证申请时，是没有店铺id的
			return platformBrandList;
		}
		//2、查询出店铺正在经营的审核通过的品牌信息
		ExecuteResult<DataGrid<ShopBrandDTO>> result = getShopBrandList(shopId, ShopCategoryStatus.PASS.getCode());
		if(result==null || result.getResult()==null || result.getResult().getRows()==null || result.getResult().getRows().size()<=0){
			return platformBrandList;
		}
		List<ShopBrandDTO> shopBrandList = result.getResult().getRows();
		
		//3、筛选出未申请过的品牌：已经申请过的同一类目下的相同品牌不能再次申请
		List<ItemBrandDTO> noneSeled = new ArrayList<ItemBrandDTO>();
		for(int i=0; null!=platformBrandList && i<platformBrandList.size(); i++){
			ItemBrandDTO platBrand = platformBrandList.get(i);
			boolean exist = false;//该店铺是否正在运营该平台品牌
			for(int j=0; j<shopBrandList.size(); j++){
				ShopBrandDTO shopBrand = shopBrandList.get(j);
				//审核痛的店铺品牌=正在经营的品牌  并且
				if(platBrand.getBrandId().equals(shopBrand.getBrandId()) && cid.equals(shopBrand.getCid())){
					exist = true;
					continue;
				}
			}
			if(!exist){//将店铺未运营的平台品牌放入list中
				noneSeled.add(platBrand);
			}
		}
		return noneSeled;
	}
	
	/**保存品牌信息*/
	@ResponseBody
	@RequestMapping("/saveBrand")
	public Map<String, Object> saveBrand(String addBrandIds, HttpServletRequest request){
		Map<String,Object> result = new HashMap<String,Object>();
		if(StringUtils.isBlank(addBrandIds)){
			result.put("result", "failure");
			return result;
		}
		Long userId = WebUtil.getInstance().getUserId(request);//卖家id
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		shopClientService.addShopBrand(addBrandIds, userId, shopId);
		
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 
	 * <p>Description: [删除店铺经营品牌]</p>
	 * Created on 2015年8月27日
	 * @param cid
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("deleteBrand")
	@ResponseBody
	public Map<String, Object> deleteBrand(Long brandId, HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
		itemQueryInDTO.setShopIds(new Long[] { shopId });
		// 品牌Id集合
		List<Long> brandIds = new ArrayList<Long>();
		brandIds.add(brandId);
		itemQueryInDTO.setBrandIdList(brandIds);
		DataGrid<ItemQueryOutDTO> dataGrid = itemExportService.queryItemList(itemQueryInDTO, null);
		if (dataGrid.getRows() != null && dataGrid.getRows().size() > 0) {
			for(ItemQueryOutDTO itemQueryOutDTO : dataGrid.getRows()){
				if (itemQueryOutDTO.getItemStatus() != ItemStatusEnum.DELETED.getCode()) {
					result.put("result", "failure");
					result.put("msg", "有商品正在使用该品牌，不能删除！");
					return result;
				}
			}
		}
		// 删除店铺经营品牌
		ExecuteResult<String> executeResult = this.shopBrandExportService.updateStatusByShopIdAndBrandId(shopId, brandId);
		if(executeResult.isSuccess()){
			result.put("result", "success");
			result.put("msg", "删除成功");
		}else{
			result.put("result", "failure");
			result.put("msg", "删除失败");
		}
		return result;
	}
}
