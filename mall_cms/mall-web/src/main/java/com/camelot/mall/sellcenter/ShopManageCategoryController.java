package com.camelot.mall.sellcenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.enums.ItemStatusEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.service.ShopClientService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.settlecenter.dto.SettleCatExpenseDTO;
import com.camelot.settlecenter.service.SattleCatExpenseExportService;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
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
@RequestMapping("/shopManageCategoryController")
public class ShopManageCategoryController {
	@Resource
	private ItemCategoryService itemCategoryService;
	
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	
	@Resource
	private ShopClientService shopClientService;
	
	@Resource
	private ShopModifyInfoExportService shopModifyInfoExportService;
	
	@Resource
	private SattleCatExpenseExportService sattleCatExpenseExportService;
	
	@Resource
	private ItemExportService itemExportService;
	
	@RequestMapping("toEdit")
	public String toEdit(HttpServletRequest request, Model model){
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		//查询当前店铺的所有经营类目信息
		Long[] passedCids = getShopCids(shopId, ShopCategoryStatus.PASS.getCode());
		//当前店铺正在经营的类目id数组
		Map<String, String> passedCategoryList = buildCategoryListByCids(passedCids);
		//查询店铺类目对应的平台类目列表
		Long[] rejectedCids = getShopCids(shopId, ShopCategoryStatus.REJECT.getCode());
		//被驳回的类目信息列表
		Map<String, String> rejectedCategoryList = buildCategoryListByCids(rejectedCids);
		//平台一级类目
		DataGrid<ItemCategoryDTO> categorylist = itemCategoryService.queryItemCategoryList(0L);
		//查询店铺类目的驳回信息
		ExecuteResult<List<ShopCategoryDTO>> shopRejectList = shopCategoryExportService.queryShopCategoryList(shopId, ShopCategoryStatus.REJECT.getCode());
		//组装驳回原因的map对象
		Map<String, String> rejectComment = buildShopRejectMap(shopRejectList);
		model.addAttribute("levOneCategoryList", categorylist.getRows());//平台一级类目
		model.addAttribute("passedCategoryList", passedCategoryList);	//当前店铺正在经营的类目名称集合
		model.addAttribute("existCids", shopClientService.buildExistsCids(passedCids));
		model.addAttribute("rejectedCategoryList", rejectedCategoryList);	
		model.addAttribute("rejectComment", rejectComment);
		return "/sellcenter/shop/shopManageCategoryEdit";
	}

	/**
	 * 组装驳回原因的map对象
	 * @param shopRejectList
	 * @return
	 */
	private Map<String, String> buildShopRejectMap(ExecuteResult<List<ShopCategoryDTO>> shopRejectList) {
		Map<String, String> rejectMap = new HashMap<String, String>();
		if(null==shopRejectList || shopRejectList.getResult()==null){
			return rejectMap;
		}
		List<ShopCategoryDTO> rejectlist = shopRejectList.getResult();
		for(int i=0; i<rejectlist.size(); i++){
			ShopCategoryDTO record = rejectlist.get(i);
			rejectMap.put(String.valueOf(record.getCid()), record.getComment());
		}
		return rejectMap;
	}

	/**
	 *  根据类目id组装类目map列表信息
	 * @param scids
	 * @return
	 */
	private Map<String, String> buildCategoryListByCids(Long[] scids) {
		ExecuteResult<List<ItemCatCascadeDTO>> cascadeCategoryList = itemCategoryService.queryParentCategoryList(scids);
		Map<String, String> categoryNameList = shopClientService.buildCategoryNameList(cascadeCategoryList.getResult());//组装一级、二级、三级类目的名称
		return categoryNameList;
	}

	/**
	 * 获取店铺的类目id 
	 * @param shopId
	 * @param status
	 * @return
	 */
	private Long[] getShopCids(Long shopId, Integer status) {
		ExecuteResult<List<ShopCategoryDTO>> shopcategorylist = shopCategoryExportService.queryShopCategoryList(shopId, status);
		Long[] scids = shopClientService.buildShopCategoryIds(shopcategorylist.getResult());
		return scids;
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
	
	/**
	 * 根据类目id查询该类目是否存在返点信息
	 * @param request
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("validExistRebate")
	public Map<String, Object> validExistRebate(Long cid, HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "failure");
		result.put("msg", "添加类目信息失败！");
		if(null == cid){
			return result;
		}
		ExecuteResult<List<SettleCatExpenseDTO>> retVal = sattleCatExpenseExportService.queryByIds(new Long[]{cid});
		if(retVal==null || retVal.getResult()==null || retVal.getResult().size()<=0){
			result.put("msg", "该类目不存在返点信息，不允许添加！");
			return result;
		}
		result.put("result", "success");
		return result;
	}
	
	@RequestMapping("toView")
	public String toView(HttpServletRequest request, Model model){
		Long shopId = WebUtil.getInstance().getShopId(request);
		//正在经营的所有类目信息
		Map<String, String> busCategoryNameList = getShopBusinessCategory(shopId, ShopCategoryStatus.PASS.getCode());
		//审核中的所有类目信息
		Map<String, String> auditCategoryNameList = getShopBusinessCategory(shopId, ShopCategoryStatus.APPLY.getCode());
		model.addAttribute("busCategoryNameList", busCategoryNameList);
		model.addAttribute("auditCategoryNameList", auditCategoryNameList);
		
		return "/sellcenter/shop/shopManageCategoryView";
	}

	/**查询店铺正在经营的类目*/
	private Map<String, String> getShopBusinessCategory(Long shopId, Integer status) {
		//查询当前店铺正在经营的所有类目信息
		ExecuteResult<List<ShopCategoryDTO>> shopcategorylist = shopCategoryExportService.queryShopCategoryList(shopId, status);
		Long[] scids = shopClientService.buildShopCategoryIds(shopcategorylist.getResult());
		Map<String, String> busCategory = buildCategoryListByCids(scids);
		
		return busCategory;
	}
	
	/**保存店铺类目信息*/
	@RequestMapping("saveCategory")
	@ResponseBody
	public Map<String, Object> saveCategory(String addCategoryCids, HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isBlank(addCategoryCids)){
			result.put("result", "failure");
			return result;
		}
		Long userId = WebUtil.getInstance().getUserId(request);//卖家id
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		shopClientService.addShopCategory(addCategoryCids, userId, shopId);
		result.put("result", "success");
		return result;
	}
	
	/**
	 * 
	 * <p>Description: [删除店铺经营类目]</p>
	 * Created on 2015年8月26日
	 * @param cid
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("deleteCategory")
	@ResponseBody
	public Map<String, Object> deleteCategory(Long cid, HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		Long shopId = WebUtil.getInstance().getShopId(request);//店铺id
		ItemQueryInDTO itemQueryInDTO = new ItemQueryInDTO();
		itemQueryInDTO.setShopIds(new Long[] { shopId });
		itemQueryInDTO.setCid(cid);
		DataGrid<ItemQueryOutDTO> dataGrid = itemExportService.queryItemList(itemQueryInDTO, null);
		if (dataGrid.getRows() != null && dataGrid.getRows().size() > 0) {
			for(ItemQueryOutDTO itemQueryOutDTO : dataGrid.getRows()){
				if (itemQueryOutDTO.getItemStatus() != ItemStatusEnum.DELETED.getCode()) {
					result.put("result", "failure");
					result.put("msg", "有商品正在使用该类目，不能删除！");
					return result;
				}
			}
		}
		// 删除店铺经营类目
		ExecuteResult<String> executeResult = this.shopCategoryExportService.updateStatusByShopIdAndCid(shopId,cid);
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
