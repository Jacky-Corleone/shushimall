package com.camelot.mall.buyercenter.usedmarket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.util.CollectionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemOldDTO;
import com.camelot.goodscenter.dto.ItemPictureDTO;
import com.camelot.goodscenter.dto.indto.ItemOldSeachInDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldOutDTO;
import com.camelot.goodscenter.dto.outdto.ItemOldSeachOutDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemOldExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;

@Controller
@RequestMapping("/usedMarket")
public class UsedMarketController {
	private Logger LOGGER = Logger.getLogger(this.getClass());

	@Resource
    private ItemCategoryService itemCategoryService;
	
	@Resource
	private UserExportService userExportService;

	@Resource
	private ItemOldExportService itemOldExportService;

    @Resource
    private UserExportService userService;
    @Resource
	private AddressBaseService addressBaseService;
    @Resource
    private UserExtendsService userExtendsService;

	/**
	 * 跳转到二手市场商品列表页面
	 * @param request
	 * @param page
	 * @param firstLevelC	选中的第一级类目
	 * @param secondLevlC	选中的第二级类目
	 * @param model
	 * @return
	 */
	@RequestMapping("/usedGoodsList")
	public String goodsList(HttpServletRequest request, ItemOldDTO itemOldDTO , Integer page, Long firstLevelC, Long secondLevelC, Model model) {
		Long userId = WebUtil.getInstance().getUserId(request);
		Integer status = itemOldDTO.getStatus();
		ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(userId);
		Integer[] statuss = null;
		//前台传过来的状态为空， 则查（2：待审核、3：待上架、4：在售、5：已下架、20：驳回）数据
		if(status == null)
		{
			statuss = new Integer[]{2, 3, 4, 5, 20};
		}else
		{
			statuss = new Integer[]{ status };
		}
		itemOldDTO.setStatuss(statuss);
		itemOldDTO.setSellerIds((Long[]) idsEr.getResult().toArray(new Long[idsEr.getResult().size()]));
//		itemOldDTO.setSellerId(userId);
		page = (page == null ? 1 : page);
		//一级类目
		DataGrid<ItemCategoryDTO> result =  itemCategoryService.queryItemCategoryList(0L);
		model.addAttribute("firstItemCategoryDTOList", result.getRows());
		//二级类目
		if(firstLevelC != null)
		{
			DataGrid<ItemCategoryDTO> secondResult =  itemCategoryService.queryItemCategoryList(firstLevelC);
			model.addAttribute("secondItemCategoryDTOList", secondResult.getRows());
		}
		//三级类目
		if(secondLevelC != null)
		{
			DataGrid<ItemCategoryDTO> thirdResult =  itemCategoryService.queryItemCategoryList(secondLevelC);
			model.addAttribute("thirdItemCategoryDTOList", thirdResult.getRows());
		}

		model.addAttribute("firstLevlC", firstLevelC);
		model.addAttribute("secondLevelC", secondLevelC);
		//二手商品列表
		Pager<ItemOldDTO> pager = new Pager<ItemOldDTO>();
		pager.setPage(page);
		ExecuteResult<DataGrid<ItemOldDTO>> oldItemResult = itemOldExportService.queryItemOld(itemOldDTO, pager);
		DataGrid<ItemOldDTO> dg = oldItemResult.getResult();
		if(null!=dg.getRows()&&dg.getTotal()>0){
			for(ItemOldDTO dto :dg.getRows()){
				if(dto.getCid()!=null){
					Map<String,String> categoryMap= this.getCategoryName(dto.getCid());
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
					dto.setcName(cName);
				}
				
			}
		}
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());

		model.addAttribute("itemOldDTO", itemOldDTO);
		model.addAttribute("pager", pager);
		return "/buyercenter/usedmarket/usedGoodsList";
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
	 * 根据上级类目ID 查询下级类目
	 *
	 * @param parentCid 上级类目ID
	 * @return
	 */
	@RequestMapping("/getShopCategory")
	@ResponseBody
	public List<?> getShopCategory(Long parentCode)
	{
		DataGrid<ItemCategoryDTO> result =  itemCategoryService.queryItemCategoryList(parentCode);
		List<ItemCategoryDTO> list = result.getRows();
		LOGGER.debug(JSON.toJSONString(list));
		return list;
	}

	/**
	 * 更新商品的状态(上架还是下架,删除)
	 *
	 * @param itemIdStr 商品的ID，或商品ID的集合
	 * @param status 要将商品更新成的状态
	 * @return
	 */
	@RequestMapping("/goodsPublish")
	@ResponseBody
	public Map<String, Object> goodsPublish(HttpServletRequest request, String itemIdStr, Long status)
	{
		Long userId = WebUtil.getInstance().getUserId(request);
		String id=userId+"";
		String message = "操作失败！";
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(itemIdStr))
		{
			LOGGER.info("itemIdStr" + itemIdStr);
			String itemIds[] = itemIdStr.split(",");
			if(itemIds != null && itemIds.length > 0)
			{
				List<Long> itemIdList = new ArrayList<Long>();
				for(String itemId: itemIds)
				{
					itemIdList.add(new Long(itemId.trim()));
				}

				//调service更新
				ExecuteResult<String> result = itemOldExportService.modifyItemOldStatus("", id, status, itemIdList.toArray(new Long[]{}));
				if("success".equals(result.getResultMessage()))
				{
					message = "操作成功";
				}
			}
		}
		map.put("message", message);
		return map;
	}

	@RequestMapping("")
	public String index(Long levOneCid, Long levTwoCid, Long levThreeCid, ItemOldSeachInDTO itemOldSeachInDTO, Integer page, HttpServletRequest request, Model model){
        String token = LoginToken.getLoginToken(request);
        if(StringUtils.isBlank(token)){
            model.addAttribute("userId", "");
        }else{
            RegisterDTO registerDTO = userService.getUserByRedis(token);
            if(registerDTO == null){
                model.addAttribute("userId", "");
            }else{
                model.addAttribute("userId", registerDTO.getUid());
            }
        }
		String keyword=request.getParameter("keyword");
		if(keyword!=null && keyword!=""){
			itemOldSeachInDTO.setKey(keyword);
		}
		//未被审核用户无法发布闲置

		String ifRelease = request.getParameter("ifRelease");
		if(StringUtils.isNotBlank(ifRelease) && ifRelease.equals("0")){
			model.addAttribute("ifRelease", "0");
		}
		//未被审核用户无法一键转卖
		String ifResell = request.getParameter("ifResell");
		if(StringUtils.isNotBlank(ifResell) && ifResell.equals("0")){
			model.addAttribute("ifResell", "0");
		}
		Pager<ItemOldDTO> pager = new Pager<ItemOldDTO>();
		if(page!=null && page>0){
			pager.setPage(page);
		}
		if(null != levThreeCid){
			itemOldSeachInDTO.setCid(levThreeCid);
		}else if(null != levTwoCid){
			itemOldSeachInDTO.setCid(levTwoCid);
		}else if(null != levOneCid){
			itemOldSeachInDTO.setCid(levOneCid);
		}
		
		//获取省市
		List<AddressBase> addressList = addressBaseService.queryAddressBase("0");
		model.addAttribute("addressList", addressList);
		
		//查询20条商品分页数据
		pager.setRows(10);
		ExecuteResult<ItemOldSeachOutDTO> result = itemOldExportService.seachItemOld(itemOldSeachInDTO, pager);
		ItemOldSeachOutDTO itemOld = result.getResult();
		if(null != itemOld){
			DataGrid<ItemOldDTO> dg = itemOld.getItemOldDTOs();
			if(null != dg){
				List<ItemOldDTO> records = dg.getRows();
				pager.setRecords(records);
				Long total = dg.getTotal();
				pager.setTotalCount(total.intValue());
			}
		}
		//将前台选中的一、二级类目id存储到request域中再次返回前台进行高亮显示
		model.addAttribute("levOneCid", levOneCid);
		model.addAttribute("levTwoCid", levTwoCid);
		model.addAttribute("levThreeCid", levThreeCid);
		model.addAttribute("pager", pager);
		model.addAttribute("itemOldSeachInDTO", itemOldSeachInDTO);
		model.addAttribute("flag", "used");
		model.addAttribute("module", "umarket");
		model.addAttribute("keyword",keyword);
		return "/buyercenter/usedmarket/index";
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
	
	
	@RequestMapping("/details")
	public String detail(Long id, HttpServletRequest request, Model model){
		model.addAttribute("item", null);
		model.addAttribute("skuItem", null);
		model.addAttribute("shopInfo", null);
		model.addAttribute("catCascade", null);
		model.addAttribute("skuId", null);
		model.addAttribute("logging_status", "false");

		if(id == null){
			return "/buyercenter/usedmarket/usedProductDetail";
		}

		ExecuteResult<ItemOldOutDTO> result = itemOldExportService.getItemOld(id);
		if(result==null || result.getResult()==null){
			return "/buyercenter/usedmarket/usedProductDetail";
		}
		ItemOldDTO item = result.getResult().getItemOldDTO();
		System.out.println("item======"+JSON.toJSON(item));
		List<ItemPictureDTO> itemPicture = result.getResult().getItemPictureDTO();
		//类目导航
		ExecuteResult<List<ItemCatCascadeDTO>> itemCatCascade = itemCategoryService.queryParentCategoryList(item.getCid());

		//配置商品详情页的SEO
		BaseTDKConfigDTO  baseTDKConfigDTO=new BaseTDKConfigDTO();
		baseTDKConfigDTO.setTitle(item.getItemName()+"【图片 价格  品牌   评价】"+"-舒适100二手市场-舒适100");
		baseTDKConfigDTO.setDescription(item.getItemName()+" 图片、价格、品牌、评论 【舒适100，全国配送，优惠多多】");
		baseTDKConfigDTO.setKeywords(item.getcName()+","+item.getItemName()+",舒适100");
		model.addAttribute("baseTDKConfigDTO3", baseTDKConfigDTO);
		model.addAttribute("shopEvaluationResult", item);
		
		model.addAttribute("item", item);
		model.addAttribute("itemPicture", itemPicture);
		model.addAttribute("catCascade", itemCatCascade.getResult());
		model.addAttribute("favouriteItem", "false");//是否收藏过该商品
		
		ExecuteResult<UserInfoDTO> userInfoResult = userExtendsService.findUserInfo(item.getSellerId());
		String companyName = userInfoResult != null && userInfoResult.isSuccess() && userInfoResult.getResult() != null?
				userInfoResult.getResult().getUserBusinessDTO().getCompanyName():"";
		model.addAttribute("companyName", companyName);


		return "/buyercenter/usedmarket/usedProductDetail";
	}
}
