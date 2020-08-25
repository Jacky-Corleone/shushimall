package com.camelot.mall.sellcenter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSON;
import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.goodscenter.dto.*;
import com.camelot.goodscenter.dto.enums.ItemAddSourceEnum;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.TradeInventoryExportService;
import com.camelot.maketcenter.dto.*;
import com.camelot.maketcenter.dto.indto.PromotionFullReducitonInDTO;
import com.camelot.maketcenter.dto.indto.PromotionMarkdownInDTO;
import com.camelot.maketcenter.service.PromotionFrExportService;
import com.camelot.maketcenter.service.PromotionInfoExportService;
import com.camelot.maketcenter.service.PromotionMdExportService;
import com.camelot.maketcenter.service.PromotionService;
import com.camelot.mall.shopcart.Promotion;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enums.PromotionTimeStatusEnum;
import com.camelot.openplatform.common.enums.VipLevelEnum;
import com.camelot.storecenter.dto.ShopCategoryDTO;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.emums.ShopEnums.ShopCategoryStatus;
import com.camelot.storecenter.service.ShopCategoryExportService;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.util.Constants;
import com.camelot.util.WebUtil;

import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * <p>
 * Description: [促销中心]
 * </p>
 * Created on 2015-3-16
 * 
 * @author 解东亮
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sellcenter/salePromotion")
public class SalePromotionController {
	@Resource
	private PromotionService promotionService;
	@Resource
	private PromotionFrExportService promotionFrExportService;
	@Resource
	private PromotionInfoExportService promotionInfoExportService;
	@Resource
	private PromotionMdExportService promotionMdExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private UserExtendsService userExtendsService;
	@Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private ShopCategoryExportService shopCategoryExportService;
	@Resource
	TradeInventoryExportService tradeInventoryExportService;

	/**
	 * 查询活动列表
	 * @param page
	 * @param promotionInfo
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/index")
	public String toPromotionIndex(Integer page, PromotionInfo promotionInfo,
			HttpServletRequest request, Model model) throws ParseException {
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		promotionInfo.setShopId(this.getShopId(request));
		
		// 促销名称
		if (StringUtils.isNotEmpty(request.getParameter("promotionName"))) {
			promotionInfo
					.setActivityName(request.getParameter("promotionName"));
		}
		// 开始时间
		if (StringUtils.isNotEmpty(request.getParameter("beginDate"))) {
			java.util.Date startTime = formatter.parse(request
					.getParameter("beginDate"));
			promotionInfo.setStartTime(startTime);
		}
		// 结束时间
		if (StringUtils.isNotEmpty(request.getParameter("endDate"))) {
			java.util.Date endTime = formatter.parse(request
					.getParameter("endDate"));
			promotionInfo.setEndTime(endTime);
		}
		// 促销类型1：直降，2：满减
		if (StringUtils.isNotEmpty(request.getParameter("promotionType"))) {
			promotionInfo.setType(Integer.valueOf(request
					.getParameter("promotionType")));
		}
		// 促销状态1：已上线，2：已下线
		if (StringUtils.isNotEmpty(request.getParameter("promotionStatus"))) {
			promotionInfo.setOnlineState(Integer.valueOf(request
					.getParameter("promotionStatus")));
		}

		Pager<PromotionInfo> pager = new Pager<PromotionInfo>();
		if (page == null) {
			page = 1;
		}
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date currentDateString = formatter1
				.parse(formatter1.format(new Date()));

		pager.setPage(page);
		pager.setSort("createTime");
		pager.setOrder("desc");
		ExecuteResult<DataGrid<PromotionInfo>> result = this.promotionInfoExportService
				.queryPromotionInfoList(promotionInfo, pager);
		List<PromotionInfo> promotionInfoList = result.getResult().getRows();
		//获取活动时间
		for (int i = 0; i < promotionInfoList.size(); i++) {
			PromotionInfo promotionInfo_ = promotionInfoList.get(i);
			if (currentDateString.getTime() > promotionInfo_.getEndTime().getTime() ) {
				promotionInfo_.setDateDif("end");
			} else if(currentDateString.getTime() > promotionInfo_.getStartTime().getTime()) {
				promotionInfo_.setDateDif("start");
			}
		}
		pager.setTotalCount(result.getResult().getTotal().intValue());
		pager.setRecords(promotionInfoList);
		model.addAttribute("promotionInfo", promotionInfo);
		model.addAttribute("pager", pager);
		model.addAttribute("date",new Date());
		// 获取店铺一级分类
		// List<ShopCategorySellerDTO> shopCategorySellerDTOList =
		// this.getLevelOneShopCategory(request);
		// model.addAttribute("shopCategorySellerDTOList",
		// shopCategorySellerDTOList);
		return "/sellcenter/salePromotion/promotionIndex";
	}

	/**
	 *  @author XDL 2015/3/7 查询活动订单的明细
	 * @param page
	 * @param promotionInfo
	 * @param proType
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/promotionDetailInfo")
	public String promotionDetailInfo(Integer page, PromotionInfo promotionInfo,String proType ,
			HttpServletRequest request, Model model)
			throws ParseException {
		String meetPrice="";//满足金额
		String discountPrice="";//优惠金额
		String discountPercent="0";//限时折扣
		Integer markdownType = null; // 优惠计算方式
		String isAllItem=request.getParameter("isAllItem");
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date currentDateString = formatter1
				.parse(formatter1.format(new Date()));
		ExecuteResult<DataGrid<PromotionInfo>> result = this.promotionInfoExportService
				.queryPromotionInfoList(promotionInfo, null);
		List<PromotionInfo> promotionInfoList = result.getResult().getRows();
		PromotionInfo promotionInfo_ = new PromotionInfo();
		if(promotionInfoList != null && promotionInfoList.size() > 0){
			promotionInfo_ = promotionInfoList.get(0);
			if (promotionInfo_.getEndTime().getTime() > currentDateString.getTime()) {
				long s = (promotionInfo_.getEndTime().getTime() - currentDateString
						.getTime());
				double betwwenDays =  (s) /(double)((1000 * 60 * 60 * 24) + 0.5);
				if (betwwenDays == 0) {
					promotionInfo_.setDateDif("不足一");
				} else {
					promotionInfo_.setDateDif(String.valueOf(betwwenDays));
				}
			} else {
				promotionInfo_.setDateDif("活动已经结束");
			}
		}

		if (currentDateString.getTime() > promotionInfo_.getEndTime().getTime() ) {
			promotionInfo_.setDateDif("end");
		} else if(currentDateString.getTime() < promotionInfo_.getStartTime().getTime()) {
			promotionInfo_.setDateDif("nostart");
		}else{
			promotionInfo_.setDateDif("starting");
		}
			if("1".equals(proType)){
				//直降活动
				PromotionMarkdown promotionMarkdownIn = new PromotionMarkdown();
				promotionMarkdownIn.setPromotionInfoId(promotionInfo.getId());
				ExecuteResult<DataGrid<PromotionMarkdown>> markResult = promotionMdExportService.queryPromotionMdList(promotionMarkdownIn, null);
				List<PromotionMarkdown> listPromotionMarkdown=new ArrayList<PromotionMarkdown>();
				if(markResult.isSuccess()){
					List<PromotionMarkdown> lists=markResult.getResult().getRows();
					if(null!=lists && lists.size()>0) {
						if("2".equals(isAllItem)){
							for (PromotionMarkdown markdown : lists) {
								ExecuteResult<ItemDTO> itemResult = itemExportService
										.getItemById(markdown.getItemId());
								if (itemResult != null) {
									markdown.setItemName(itemResult.getResult().getItemName());
								}
								listPromotionMarkdown.add(markdown);
							}
						}
						if (lists != null && lists.size() > 0) {
							if(lists.get(0).getDiscountPercent() == null){
								lists.get(0).setDiscountPercent(BigDecimal.ZERO);
							}
							markdownType = lists.get(0).getMarkdownType();
							discountPercent = markdownType == null || markdownType == 1?
									lists.get(0).getDiscountPercent().toString():
									lists.get(0).getPromotionPrice().toString();
							markdownType = lists.get(0).getMarkdownType();
						}
					}
					model.addAttribute("listFull",listPromotionMarkdown);
                    BigDecimal percent=new BigDecimal("0.000");;
                    percent= markdownType == null || markdownType == 1?
                    		new BigDecimal(discountPercent).multiply(new BigDecimal("10")).setScale(2):
                    		new BigDecimal(discountPercent);

                    model.addAttribute("markdownType",markdownType);
					model.addAttribute("discountPercent",percent);
					
				}
			}else{
				//查询满减列表
				PromotionFullReduction PromotionFullReductionIn = new PromotionFullReduction();
				PromotionFullReductionIn.setPromotionInfoId(promotionInfo.getId());
				ExecuteResult<DataGrid<PromotionFullReduction>> pfrResult = promotionFrExportService
							.queryPromotionFrList(PromotionFullReductionIn, null);
				List<PromotionFullReduction> listPromotionFullReduction=new ArrayList<PromotionFullReduction>();//满减
				if(pfrResult.isSuccess()){
					List<PromotionFullReduction> lists=pfrResult.getResult().getRows();
					if(null!=lists && lists.size()>0){
						if("2".equals(isAllItem)){
							for(PromotionFullReduction promotionFull:lists){
								ExecuteResult<ItemDTO> itemResult = itemExportService
										.getItemById(promotionFull.getItemId());
								if(itemResult.getResult()!=null){
									promotionFull.setItemName(itemResult.getResult().getItemName());
								}
								listPromotionFullReduction.add(promotionFull);
							}
						}
						meetPrice=lists.get(0).getMeetPrice().toString();
						discountPrice=lists.get(0).getDiscountPrice().toString();
					}
					model.addAttribute("listFull",listPromotionFullReduction);
					model.addAttribute("meetPrice",meetPrice);
					model.addAttribute("discountPrice",discountPrice);
				}
			 }
          if("1".equals(isAllItem)){
        	 ItemQueryInDTO itemQueryInDTO=new ItemQueryInDTO();
        	 itemQueryInDTO.setShopIds(new Long[]{this.getShopId(request)});
        	 itemQueryInDTO.setItemStatus(4);
        	 Pager<ItemQueryOutDTO> pager = new Pager<ItemQueryOutDTO>();
     		  if(page == null){
     			page = 1;
     		  }
     		pager.setPage(page);
     		DataGrid<ItemQueryOutDTO> dg=itemExportService.queryItemList(itemQueryInDTO, pager);
     		pager.setTotalCount(dg.getTotal().intValue());
    		pager.setRecords(dg.getRows());
     		model.addAttribute("pager",pager);
         }
		model.addAttribute("proType",proType);
		model.addAttribute("promotionInfo", promotionInfo_);
		model.addAttribute("date",new Date());
        //会员等级
        model.addAttribute("vipLevelStr", VipLevelEnum.getNameStrByIdStr(promotionInfo_.getMembershipLevel()));
		return "/sellcenter/salePromotion/promotionDetailInfo";
	}

	/**
	 * 2015/3/20 xdl 删除选中的活动信息
	 * @param promotionId
	 * @param request
	 * @return
	 */
	@RequestMapping("/deletePromotion")
	@ResponseBody
	public Map<String, Object> deletePromotion(String promotionId,
			HttpServletRequest request) {
		// 解析商品id
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ExecuteResult<String> result = promotionService
					.deletePromotion(Long.valueOf(promotionId));
			resultMap.put("message", result.isSuccess() ? "操作成功" : "操作失败");
		} catch (Exception e) {
		}
		return resultMap;
	}

	/**
	 * 添加促销活动时查询商品信息 2015/3/19 xdl
	 * @param itemDTO
	 * @param addSource
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/searchProductInfo")
	public String searchProductInfo(ItemDTO itemDTO, Integer addSource,
			HttpServletRequest request, Model model) throws ParseException {
		Integer operator = 1;
		// 获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategoryDTOList = this
				.getLevelOneShopCategory(request);
		// 获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService
				.queryShopCategoryList(shopId,
						ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for (ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO
				.getResult()) {
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		// 通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> result = itemCategoryService
				.queryParentCategoryList(categoryIds);

		if (itemDTO.getItemId() != null) {
			ExecuteResult<ItemDTO> ItemResult = itemExportService
					.getItemById(itemDTO.getItemId());
			itemDTO = ItemResult.getResult();
			operator = itemDTO.getOperator();
			if (addSource == null || addSource != ItemAddSourceEnum.PLATFORM.getCode()) {
				addSource = itemDTO.getAddSource();
			}
			// 获取商品对应的一级、二级、三级平台分类
			Long oneLevelId = null;
			Long twoLevelId = null;
			List<ItemCatCascadeDTO> ItemCatCascadeLevelTwoList = null;
			List<ItemCatCascadeDTO> ItemCatCascadeLevelThreeList = null;
			labelA: for (ItemCatCascadeDTO itemCatCascadeOne : result
					.getResult()) {
				for (ItemCatCascadeDTO itemCatCascadeTwo : itemCatCascadeOne
						.getChildCats()) {
					for (ItemCatCascadeDTO itemCatCascadeThree : itemCatCascadeTwo
							.getChildCats()) {
						if (itemDTO.getCid().equals(
								itemCatCascadeThree.getCid())) {
							twoLevelId = itemCatCascadeTwo.getCid();
							oneLevelId = itemCatCascadeOne.getCid();
							ItemCatCascadeLevelTwoList = itemCatCascadeOne
									.getChildCats();
							ItemCatCascadeLevelThreeList = itemCatCascadeTwo
									.getChildCats();
							break labelA;
						}
					}
				}
			}
			model.addAttribute("oneLevelId", oneLevelId);
			model.addAttribute("twoLevelId", twoLevelId);
			model.addAttribute("ItemCatCascadeLevelTwoList",
					ItemCatCascadeLevelTwoList);
			model.addAttribute("ItemCatCascadeLevelThreeList",
					ItemCatCascadeLevelThreeList);
			// 获取店铺一级类目
			if (itemDTO.getShopCid() != null) {
				ShopCategorySellerDTO shopCategorySellerDTO = new ShopCategorySellerDTO();
				shopCategorySellerDTO.setCid(itemDTO.getShopCid());
				ExecuteResult<DataGrid<ShopCategorySellerDTO>> scResult = shopCategorySellerExportService
						.queryShopCategoryList(shopCategorySellerDTO, null);
				List<ShopCategorySellerDTO> ShopCategorySellerList = scResult
						.getResult().getRows();
				if (ShopCategorySellerList != null
						&& ShopCategorySellerList.size() > 0) {
					model.addAttribute("shopCatLevelOneId",
							ShopCategorySellerList.get(0).getParentCid());
				}
			}
			// 商品对象转换为json
			model.addAttribute("itemDTOJson", JSON.toJSON(itemDTO));
		}
		List<AddressBase> addressBaseList = this.addressBaseService
				.queryAddressBase("0");
		model.addAttribute("shopCategoryDTOList", shopCategoryDTOList);
		model.addAttribute("itemCatCascadeDTOList", result.getResult());
		// 如果是从平台上传
		if (addSource != null && addSource == ItemAddSourceEnum.PLATFORM.getCode()) {
			model.addAttribute("plstItemId", itemDTO.getItemId());
			itemDTO.setItemId(null);
		}
		model.addAttribute("itemDTO", itemDTO);
		model.addAttribute("addressBaseList", addressBaseList);
		model.addAttribute("operator", operator);
		model.addAttribute("addSource", addSource);
		return "/sellcenter/salePromotion/addPromotionInfo";
	}

	/**
	 * 获取一级店铺分类
	 * 
	 * @return
	 */
	private List<ShopCategorySellerDTO> getLevelOneShopCategory(
			HttpServletRequest request) {
		// 获取店铺id
		ShopCategorySellerDTO shopCategorySellerDTO_ = new ShopCategorySellerDTO();
		shopCategorySellerDTO_.setShopId(getShopId(request));
		shopCategorySellerDTO_.setLev(1);
		// 获取一级店铺分类
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> shopCategoryResult = this.shopCategorySellerExportService
				.queryShopCategoryList(shopCategorySellerDTO_, null);
		return shopCategoryResult.getResult().getRows();
	}

	/**
	 * 2015/3/17 xdl 促销全部上架，下架功能
	 * @param ids
	 * @param status
	 * @param request
	 * @return
	 */
	@RequestMapping("/shelveAll")
	@ResponseBody
	public Map<String, Object> goodsPublish(String ids, Integer status,
			HttpServletRequest request) {
		// 解析商品id
		String[] itemIds = ids.split(",");
		Long[] itemIdArray = new Long[itemIds.length];
		for (int i = 0; i < itemIds.length; i++) {
			itemIdArray[i] = new Long(itemIds[i]);
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			ExecuteResult<String> result = promotionService
					.modifyPromotionOnlineState(status, itemIdArray);
			resultMap.put("message", result.isSuccess() ? "操作成功" : "操作失败");
		} catch (Exception e) {
		}
		return resultMap;
	}

	/**
	 * 获取平台销售属性
	 * 
	 * @param cid
	 * @param request
	 * @return
	 */
	@RequestMapping("getAddressBase")
	@ResponseBody
	public Map<String, Object> getAddressBase(String cid, String plantItemId,
			HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<AddressBase> addressBaseList = this.addressBaseService
				.queryAddressBase("0");

		resultMap.put("result", addressBaseList);

		return resultMap;
	}

	/**
	 *
	 * @param sks
	 * @param sps
	 * @param status
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/submitMeetReduce")
	@ResponseBody
	public Map<String, Object> submitMeetReduce(String sks, String sps,
			Integer status, HttpServletRequest request) throws ParseException {
		// 解析商品id
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String meet_price = request.getParameter("meet_price");
		String discount_price = request.getParameter("discount_price");
		String words = request.getParameter("words");
		String activety_name = request.getParameter("activety_name");
		Long userId = this.getLoginUserId(request);
		String userNmae = this.getLoginUserName(request);
		PromotionInDTO itemInDTO = new PromotionInDTO();
		itemInDTO.setShopId(this.getShopId(request));
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date startTime = formatter.parse(beginDate);
		java.util.Date endTime = formatter.parse(endDate);
		List<PromotionFrDTO> promotionFrList = new ArrayList<PromotionFrDTO>();
		PromotionInfoDTO promotionInfo = new PromotionInfoDTO();
		System.out.println("this can shu is :=" + sps + "sks :" + sks);
		String[] spsIds = sps.split(",");
		String[] sksIds = sks.split(",");
		String mesage = "";
		String label = "";
		for (int i = 0; i < sksIds.length; i++) {
			PromotionFrDTO promotionFullReduction = new PromotionFrDTO();
			promotionFullReduction.setItemId(Long.valueOf(spsIds[i]));
			promotionFullReduction.setSkuId(Long.valueOf(sksIds[i]));
			itemInDTO.setSkuId(Long.valueOf(sksIds[i]));
			itemInDTO.setType(2);
			ExecuteResult<DataGrid<PromotionOutDTO>> dg = promotionService
					.getPromotion(itemInDTO, null);
			int rows = dg.getResult().getRows().size();
			if (rows > 0) {
				for (int j = 0; j < rows; j++) {
					Date endt = dg.getResult().getRows().get(j)
							.getPromotionInfo().getEndTime();
					Date start = dg.getResult().getRows().get(j)
							.getPromotionInfo().getStartTime();
					BigDecimal meetPrice = dg.getResult().getRows().get(j)
							.getPromotionFullReduction().getMeetPrice();
					if (((startTime.compareTo(start) >= 0 && startTime.compareTo(endt) <= 0)
							|| (endTime.compareTo(start) >= 0 && endTime.compareTo(endt) <= 0))
						&& meetPrice.compareTo(new BigDecimal(meet_price)) == 0) {
						label = "exist";
						break;
					} else {
						label = "";
					}
				}
			}
			if (label.equals("exist")) {
				mesage += (sksIds[i] + "已经维护过活动");
			}
			promotionFullReduction.setCreateTime(new Date());
			promotionFullReduction.setDiscountPrice(new BigDecimal(
					discount_price));
			promotionFullReduction.setMeetPrice(new BigDecimal(meet_price));
			promotionFrList.add(promotionFullReduction);
		}
		promotionInfo.setActivityName(activety_name);
		promotionInfo.setEndTime(endTime);
		promotionInfo.setStartTime(startTime);
		promotionInfo.setType(2);
		promotionInfo.setShopId(getShopId(request));
		promotionInfo.setSellerId(userId);
		promotionInfo.setCreateUser(userNmae);
		promotionInfo.setWords(words);
		PromotionFullReducitonInDTO promotionFullReducitonInDTO = new PromotionFullReducitonInDTO();
		promotionFullReducitonInDTO.setPromotionFrDTOList(promotionFrList);
		promotionFullReducitonInDTO.setPromotionInfoDTO(promotionInfo);
		System.out.println("this can shu is :="
				+ JSON.toJSONString(promotionFullReducitonInDTO));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (mesage.equals("")) {
				ExecuteResult<String> result = promotionService
						.addPromotionFullReduciton(promotionFullReducitonInDTO);
				System.out.println("this can shu is :="
						+ JSON.toJSONString(result));
				resultMap.put("message", result.isSuccess() ? "true" : "操作失败");
			} else {
				resultMap.put("message", mesage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 2015/3/17 xdl 直降活动添加功能
	 * @param sks
	 * @param sps
	 * @param activtyPrices
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/submitMarkMarkdown")
	@ResponseBody
	public Map<String, Object> submitMarkMarkdown(String sks, String sps,
			String activtyPrices, HttpServletRequest request)
			throws ParseException {
		// 解析商品id
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String words = request.getParameter("words");
		String activety_name = request.getParameter("activety_name");
//		String max = request.getParameter("max");
//		String min = request.getParameter("min");
//		String area = request.getParameter("area");
//		String sellPrice=request.getParameter("sell_price");
		Long userId = this.getLoginUserId(request);
		String userNmae = this.getLoginUserName(request);
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date startTime = formatter.parse(beginDate);
		java.util.Date endTime = formatter.parse(endDate);
		PromotionInDTO itemInDTO = new PromotionInDTO();
		itemInDTO.setShopId(this.getShopId(request));
		String mesage = "";
		List<PromotionMdDTO> promotionFrList = new ArrayList<PromotionMdDTO>();
		PromotionInfoDTO promotionInfo = new PromotionInfoDTO();
		String[] spsIds = sps.split(",");
		String[] sksIds = sks.split(",");
		String[] activtyPrice = activtyPrices.split(",");
//		String[] maxs = max.split(",");
//		String[] mins = min.split(",");
//		String[] areas = area.split(",");
		// String[] sellPrices=sellPrice.split(",");
		String label = "";
		// String[] minmaxs=minmax.split("-");
		// System.out.println(";"+activtyPrices+":"+minmax);
		for (int i = 0; i < sksIds.length; i++) {
			PromotionMdDTO promotionFullReduction = new PromotionMdDTO();
			promotionFullReduction.setItemId(Long.valueOf(spsIds[i]));
			promotionFullReduction.setSkuId(Long.valueOf(sksIds[i]));
			itemInDTO.setType(1);
			itemInDTO.setSkuId(Long.valueOf(sksIds[i]));
			ExecuteResult<DataGrid<PromotionOutDTO>> dg = promotionService
					.getPromotion(itemInDTO, null);
			int rows = dg.getResult().getRows().size();
			//同一sku在同一时间段只允许有存在于一个活动中
			if (rows > 0) {
				for (int j = 0; j < rows; j++) {
					Date endt = dg.getResult().getRows().get(j)
							.getPromotionInfo().getEndTime();
					Date start = dg.getResult().getRows().get(j)
							.getPromotionInfo().getStartTime();
					if (((startTime.compareTo(start) >= 0 && startTime.compareTo(endt) <= 0)
							|| (endTime.compareTo(start) >= 0 && endTime.compareTo(endt) <= 0))){
						label = "exist";
						break;
					}else {
						label = "";
					}
				}
			}
			if (label.equals("exist")) {
				mesage += (sksIds[i] + "已经维护过活动");
			}
			promotionFullReduction.setCreateTime(new Date());
			System.out.println("actyOrice is :" + activtyPrice[i]);
			promotionFullReduction.setMinNum(Long.valueOf(Constants.MIN_NUM));//最大值
			promotionFullReduction.setMaxNum(Long.valueOf(Constants.MAX_NUM));//最小值
			promotionFullReduction.setAreaId(Constants.AREA_ID);//区域id
			promotionFullReduction.setPromotionPrice(new BigDecimal(
					activtyPrice[i]));
			promotionFrList.add(promotionFullReduction);
		}
		promotionInfo.setActivityName(activety_name);
		promotionInfo.setEndTime(endTime);
		promotionInfo.setStartTime(startTime);
		promotionInfo.setType(1);
		promotionInfo.setShopId(getShopId(request));
		promotionInfo.setSellerId(userId);
		promotionInfo.setCreateUser(userNmae);
		promotionInfo.setWords(words);
		PromotionMarkdownInDTO promotionFullReducitonInDTO = new PromotionMarkdownInDTO();
		promotionFullReducitonInDTO.setPromotionMdDTOList(promotionFrList);
		promotionFullReducitonInDTO.setPromotionInfoDTO(promotionInfo);
		System.out.println("this can shu is :="
				+ JSON.toJSONString(promotionFullReducitonInDTO));
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			if (mesage.equals("")) {
				ExecuteResult<String> result = promotionService
						.addPromotionMarkdown(promotionFullReducitonInDTO);
				System.out.println("this can shu is :="
						+ JSON.toJSONString(result));
				resultMap.put("message", result.isSuccess() ? "true" : "操作失败");
			} else {
				resultMap.put("message", mesage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 2015/3/17 xdl 初始化活动页面
	 * @param page
	 * @param itemInDTO
	 * @param itemDTO
	 * @param shopCategoryLevelOne
	 * @param itemCategoryLevelOne
	 * @param itemCategoryLevelTwo
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("addPromotion")
	public String addPromotion(Integer page, TradeInventoryInDTO itemInDTO,
			ItemDTO itemDTO,String shopCategoryLevelOne,
			String itemCategoryLevelOne,String itemCategoryLevelTwo,HttpServletRequest request,
			Model model) {
		Integer operator = 1;
		// 获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategoryDTOList = this
				.getLevelOneShopCategory(request);
		// 获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService
				.queryShopCategoryList(shopId,
						ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for (ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO
				.getResult()) {
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		// 通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> result = itemCategoryService
				.queryParentCategoryList(categoryIds);
		List<AddressBase> addressBaseList = this.addressBaseService
				.queryAddressBase("0");
		model.addAttribute("shopCategoryDTOList", shopCategoryDTOList);
		model.addAttribute("itemCatCascadeDTOList", result.getResult());
		itemInDTO.setShopId(shopId);
		if (StringUtils.isNotEmpty(request.getParameter("skuId"))) {
			itemInDTO.setSkuId(Long.valueOf((request.getParameter("skuId"))));
		}
		if (StringUtils.isNotEmpty(request.getParameter("spuId"))) {
			itemInDTO.setItemId(Long.valueOf((request.getParameter("spuId"))));
		}
		if (StringUtils.isNotEmpty(request.getParameter("productName"))) {
			itemInDTO.setItemName((request.getParameter("productName")));
		}
//		if (StringUtils.isNotEmpty(request.getParameter("itemStatus"))) {
//			itemInDTO.setItemStatus(Integer.valueOf((request
//					.getParameter("itemStatus"))));
//		}
		if (StringUtils.isNotEmpty(request
				.getParameter("itemCategoryLevelThree"))) {
			itemInDTO.setCid(Long.valueOf((request
					.getParameter("itemCategoryLevelThree"))));
		}
		if (StringUtils
				.isNotEmpty(request.getParameter("shopCategoryLevelTwo"))) {
			itemInDTO.setShopCid(Long.valueOf((request
					.getParameter("shopCategoryLevelTwo"))));
		}
		Pager<TradeInventoryOutDTO> pager = new Pager<TradeInventoryOutDTO>();

		if (page == null) {
			page = 1;
		}
		pager.setPage(page);
		//默认为在售状态
		itemInDTO.setItemStatus(4);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService
				.queryTradeInventoryList(itemInDTO, pager);
		for (int j = 0; j < dg.getResult().getRows().size(); j++) {
			BigDecimal price = dg.getResult().getRows().get(j).getAreaPrices()
					.get(0).getSellPrice();
			dg.getResult().getRows().get(j).setSellPrice(price);
		}
		pager.setTotalCount(dg.getResult().getTotal().intValue());
		pager.setRecords(dg.getResult().getRows());
		model.addAttribute("pager", pager);
		model.addAttribute("pager1", pager);
		model.addAttribute("itemInDTO", itemInDTO);
		model.addAttribute("activeType", "1");
		model.addAttribute("itemDTO", itemDTO);
		model.addAttribute("addressBaseList", addressBaseList);
		model.addAttribute("operator", operator);
		model.addAttribute("shopCategoryLevelOne", shopCategoryLevelOne);
		model.addAttribute("itemCategoryLevelOne", itemCategoryLevelOne);
		model.addAttribute("itemCategoryLevelTwo",itemCategoryLevelTwo);
		return "/sellcenter/salePromotion/addPromotionInfo";
	}


	/**
	 * 2015/3/17 xdl 初始化活动页面
	 * @param page1
	 * @param itemInDTO
	 * @param itemDTO
	 * @param shopCategoryLevelOneD
	 * @param itemCategoryLevelOneD
	 * @param itemCategoryLevelTwoD
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("addPromotion1")
	public String addPromotion1(Integer page1, TradeInventoryInDTO itemInDTO,
			ItemDTO itemDTO,String shopCategoryLevelOneD,
			String itemCategoryLevelOneD,String itemCategoryLevelTwoD,HttpServletRequest request,
			Model model) {
		Integer operator = 1;
		// 获取店铺一级分类
		List<ShopCategorySellerDTO> shopCategoryDTOList = this
				.getLevelOneShopCategory(request);
		// 获取店铺经营的平台分类
		Long shopId = this.getShopId(request);
		ExecuteResult<List<ShopCategoryDTO>> resultShopCategoryDTO = this.shopCategoryExportService
				.queryShopCategoryList(shopId,
						ShopCategoryStatus.PASS.getCode());
		Long[] categoryIds = new Long[resultShopCategoryDTO.getResult().size()];
		int i = 0;
		for (ShopCategoryDTO shopCategoryDTO : resultShopCategoryDTO
				.getResult()) {
			categoryIds[i++] = shopCategoryDTO.getCid();
		}
		// 通过店铺经营分类获取分类列表
		ExecuteResult<List<ItemCatCascadeDTO>> result = itemCategoryService
				.queryParentCategoryList(categoryIds);
		List<AddressBase> addressBaseList = this.addressBaseService
				.queryAddressBase("0");
		model.addAttribute("shopCategoryDTOList", shopCategoryDTOList);
		model.addAttribute("itemCatCascadeDTOList", result.getResult());
		if (StringUtils.isNotEmpty(request.getParameter("skuId1"))) {
			itemInDTO.setSkuId(Long.valueOf((request.getParameter("skuId1"))));
			model.addAttribute("skuId1", request.getParameter("skuId1"));
		}
		if (StringUtils.isNotEmpty(request.getParameter("spuId1"))) {
			itemInDTO.setItemId(Long.valueOf((request.getParameter("spuId1"))));
			model.addAttribute("spuId1", request.getParameter("spuId1"));
		}
		if (StringUtils.isNotEmpty(request.getParameter("productName1"))) {
			itemInDTO.setItemName((request.getParameter("productName1")));
			model.addAttribute("productName1", request.getParameter("productName1"));
		}
		if (StringUtils.isNotEmpty(request
				.getParameter("itemCategoryLevelThree1"))) {
			itemInDTO.setCid(Long.valueOf((request
					.getParameter("itemCategoryLevelThree1"))));
		}
		if (StringUtils.isNotEmpty(request
				.getParameter("shopCategoryLevelTwo1"))) {
			itemInDTO.setShopCid(Long.valueOf((request
					.getParameter("shopCategoryLevelTwo1"))));
		}
		itemInDTO.setShopId(shopId);
		Pager<TradeInventoryOutDTO> pager = new Pager<TradeInventoryOutDTO>();

		if (page1 == null) {
			page1 = 1;
		}
		pager.setPage(page1);
		//默认为在售状态
		itemInDTO.setItemStatus(4);
		ExecuteResult<DataGrid<TradeInventoryOutDTO>> dg = tradeInventoryExportService
				.queryTradeInventoryList(itemInDTO, pager);
		System.out.println("this is enfinfo :" + JSON.toJSONString(dg));
		for (int j = 0; j < dg.getResult().getRows().size(); j++) {
			BigDecimal price = dg.getResult().getRows().get(j).getAreaPrices()
					.get(0).getSellPrice();
			dg.getResult().getRows().get(j).setSellPrice(price);
		}
		pager.setTotalCount(dg.getResult().getTotal().intValue());
		pager.setRecords(dg.getResult().getRows());
		model.addAttribute("pager1", pager);
		model.addAttribute("pager", pager);
		model.addAttribute("itemInDTO1", itemInDTO);
		model.addAttribute("activeType", "2");
		model.addAttribute("itemDTO", itemDTO);
		model.addAttribute("addressBaseList", addressBaseList);
		model.addAttribute("operator", operator);
		model.addAttribute("shopCategoryLevelOneD", shopCategoryLevelOneD);
		model.addAttribute("itemCategoryLevelOneD", itemCategoryLevelOneD);
		model.addAttribute("itemCategoryLevelTwoD",itemCategoryLevelTwoD);
		return "/sellcenter/salePromotion/promotionIndex";
	}

	/**
	 * 获取店铺id
	 * 
	 * @param request
	 * @return
	 */
	private Long getShopId(HttpServletRequest request) {
		// 获取登录用户
		long userId = WebUtil.getInstance().getUserId(request);
		// 获取店铺id
		ExecuteResult<UserInfoDTO> result = userExtendsService
				.findUserInfo(userId);
		UserDTO userDTO = result.getResult().getUserDTO();
		Long shopId = userDTO.getShopId();
		return shopId;
	}

	/**
	 * 获取登录用户Id
	 * 
	 * @param request
	 * @return
	 */
	private Long getLoginUserId(HttpServletRequest request) {
		// 获取登录用户
		Cookie[] cookies = request.getCookies();
		String token = null;
		Long userId = null;
		for (Cookie cookie : cookies) {
			if ("logging_status".equals(cookie.getName())) {
				token = cookie.getValue();
				RegisterDTO registerDTO = userExportService
						.getUserByRedis(token);
				if (registerDTO == null)
					return null;
				userId = registerDTO.getUid();
			}
		}
		return userId;
	}

	/**
	 * 获取登录用户Id
	 * 
	 * @param request
	 * @return
	 */
	private String getLoginUserName(HttpServletRequest request) {
		// 获取登录用户
		Cookie[] cookies = request.getCookies();
		String token = null;
		String userName = null;
		for (Cookie cookie : cookies) {
			if ("logging_status".equals(cookie.getName())) {
				token = cookie.getValue();
				RegisterDTO registerDTO = userExportService
						.getUserByRedis(token);
				if (registerDTO == null)
					return null;
				userName = registerDTO.getLoginname();
			}
		}
		return userName;
	}


	/**
	 * 满减和直降活动活动列表
	 * @param page
	 * @param promotionInfo
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("promotionIndex")
	public String toReDiscountPromotionIndex(Integer page, PromotionInfo promotionInfo,
								   HttpServletRequest request, Model model) throws ParseException {
		String type=request.getParameter("type");
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		// 获取店铺id
		long shopId = WebUtil.getInstance().getShopId(request);
		promotionInfo.setShopId(shopId);

        //促销状态
		if(StringUtils.isNotEmpty(request.getParameter("isEffective")) && !request.getParameter("isEffective").equals("4")){
			promotionInfo.setIsEffective(request.getParameter("isEffective"));
		}else if(StringUtils.isNotEmpty(request.getParameter("isEffective"))){
            promotionInfo.setIsEffective("");
            promotionInfo.setOnlineState(3);
        }

		// 开始时间
		if (StringUtils.isNotEmpty(request.getParameter("beginDate"))) {
			Date startTime = formatter.parse(request
					.getParameter("beginDate"));
			promotionInfo.setStartTime(startTime);
		}
		// 结束时间
		if (StringUtils.isNotEmpty(request.getParameter("endDate"))) {
			java.util.Date endTime = formatter.parse(request
					.getParameter("endDate"));
			promotionInfo.setEndTime(endTime);
		}

		Pager<PromotionInfo> pager = new Pager<PromotionInfo>();
		if (page == null) {
			page = 1;
		}
		SimpleDateFormat formatter1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		Date currentDateString = formatter1
				.parse(formatter1.format(new Date()));
        pager.setPage(page);//
		ExecuteResult<DataGrid<PromotionInfo>> result = this.promotionInfoExportService
				.queryPromotionInfoList(promotionInfo, pager);
		List<PromotionInfo> promotionInfoList = result.getResult().getRows();

          //获取活动时间
		for (int i = 0; i < promotionInfoList.size(); i++) {
			PromotionInfo promotionInfo_ = promotionInfoList.get(i);
			if (currentDateString.getTime() >= promotionInfo_.getEndTime().getTime() ) {
				promotionInfo_.setDateDif("end");
			} else if(currentDateString.getTime() <= promotionInfo_.getStartTime().getTime()) {
				promotionInfo_.setDateDif("nostart");
			}else{
				promotionInfo_.setDateDif("starting");
			}
		}

		pager.setSort("createTime");//根据创建时间
		pager.setOrder("desc");//默认倒叙
		pager.setTotalCount(result.getResult().getTotal().intValue());
		pager.setRecords(promotionInfoList);
		PromotionTimeStatusEnum[] timeStatus=PromotionTimeStatusEnum.values();
		model.addAttribute("timeStatus", timeStatus);
        if(StringUtils.isNotEmpty(request.getParameter("isEffective"))){
            promotionInfo.setIsEffective(request.getParameter("isEffective"));
        }
		model.addAttribute("promotionInfo", promotionInfo);
		model.addAttribute("pager", pager);
		model.addAttribute("type",type);
		model.addAttribute("date", new Date());
		return "/sellcenter/salePromotion/promotionIndex";
	}
	/**
	 * 满减直降参加活动方式
	 * @return
	 */
	@RequestMapping("promotionAddSelect")
	public String promotionAddSelect(HttpServletRequest request, Model model){
		String type=request.getParameter("type");
		model.addAttribute("type",type);
		return "sellcenter/salePromotion/promotionAddSelect";
	}

	/**
	 * 初始化促销活动页面（满减和折扣）和编辑活动
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("toAddPromotion")
	public String toAddPromotion(PromotionInfo promotionInfo, ItemQueryInDTO itemInDTO, Integer page,HttpServletRequest request,Model model) throws ParseException {
        String checkedItemIds=request.getParameter("checkedItemIds");//商品分页选中的商品id
		String promotionId=request.getParameter("promotionId");//促销id
        String itemType=request.getParameter("itemType");//区别
        String isAllItem=request.getParameter("isAllItem");
        SimpleDateFormat formatter1 = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        if(null != request.getParameter("startTime") && !"".equals(request.getParameter("startTime")) && null != request.getParameter("endTime") && !"".equals(request.getParameter("endTime"))){
            promotionInfo.setStartTime(formatter1
                    .parse(request.getParameter("startTime")));
            promotionInfo.setEndTime(formatter1
                    .parse(request.getParameter("endTime")));
        }
		// 解析商品id
        String meetPrice = request.getParameter("meetPrice");//满减金额
        String discountPrice = request.getParameter("discountPrice");//优惠金额
        String discountPercent=request.getParameter("discountPercent");//折扣
        Integer markdownType = null; // 优惠计算方式
        BigDecimal percent=BigDecimal.ZERO;
        if(null!= discountPercent && !"".equals(discountPercent)){
            percent=new BigDecimal(discountPercent);
        }
		StringBuffer itemIds=new StringBuffer();//商品id
        if(null!=checkedItemIds && !"".equals(checkedItemIds)){
            itemIds.append(checkedItemIds);
        }
		// 获取店铺id
		long shopId = WebUtil.getInstance().getShopId(request);
		String type=request.getParameter("type");//活动类型1，满减，2直降活动
		Long[] shopIds=new Long[1];
		shopIds[0]=shopId;
		itemInDTO.setShopIds(shopIds);
		Pager<ItemQueryOutDTO> pager = new Pager<ItemQueryOutDTO>();

		if (page == null) {
			page = 1;
		}
		pager.setPage(page);
		pager.setSort("created");
		pager.setOrder("desc");
		//默认为在售状态
		itemInDTO.setItemStatus(4);
        itemInDTO.setStartTime(null);
        itemInDTO.setEndTime(null);
		DataGrid<ItemQueryOutDTO> listItems=itemExportService.queryItemList(itemInDTO, pager);
		pager.setRecords(listItems.getRows());
		pager.setTotalCount(listItems.getTotal().intValue());
		if(promotionId!=null && !promotionId.equals("") && (null==itemType || itemType.equals(""))){
            promotionInfo=new PromotionInfo();
			promotionInfo.setId(Long.parseLong(promotionId));
			ExecuteResult<DataGrid<PromotionInfo>> result = this.promotionInfoExportService
					.queryPromotionInfoList(promotionInfo, null);
			if(result.getResult().getRows()!=null){
				promotionInfo=result.getResult().getRows().get(0);
				model.addAttribute("promotionInfo",promotionInfo);
				if(type!=null && type.equals("2")){
					//查询满减列表
					PromotionFullReduction PromotionFullReductionIn = new PromotionFullReduction();
					PromotionFullReductionIn.setPromotionInfoId(promotionInfo.getId());
					ExecuteResult<DataGrid<PromotionFullReduction>> pfrResult = promotionFrExportService
							.queryPromotionFrList(PromotionFullReductionIn, null);
					if(pfrResult.isSuccess()){
						List<PromotionFullReduction> lists=pfrResult.getResult().getRows();
						if(null != lists){
							for(PromotionFullReduction proFull:lists){
								itemIds.append(proFull.getItemId()+",");
							}
						}
						if(lists!=null && lists.size()>0){
							meetPrice=lists.get(0).getMeetPrice().toString();
							discountPrice=lists.get(0).getDiscountPrice().toString();
						}
					}
				}else{
					//直降活动
					PromotionMarkdown promotionMarkdownIn = new PromotionMarkdown();
					promotionMarkdownIn.setPromotionInfoId(promotionInfo.getId());
					ExecuteResult<DataGrid<PromotionMarkdown>> markResult = promotionMdExportService.queryPromotionMdList(promotionMarkdownIn, null);
					if(markResult.isSuccess()){
						List<PromotionMarkdown> lists=markResult.getResult().getRows();
						if(null!=lists){
							for(PromotionMarkdown markDown:lists){
								itemIds.append(markDown.getItemId()+",");
							}
						}
						if(lists!=null && lists.size()>0){
							markdownType = lists.get(0).getMarkdownType();
							if (markdownType == null || markdownType == 1) {
								discountPercent=lists.get(0).getDiscountPercent().toString();
							} else {
								discountPercent=lists.get(0).getPromotionPrice().toString();
							}
						}
                        if(null!=discountPercent && !discountPercent.equals("")){
                            percent= markdownType == null || markdownType == 1?
                            		new BigDecimal(discountPercent).multiply(new BigDecimal("10")).setScale(2):
                            		new BigDecimal(discountPercent);
                        }
					}
				}
			}
		}
		if (markdownType == null) {
			String markdownTypeOfString = request.getParameter("markdownType");
			if (markdownTypeOfString != null) {
				markdownType = Integer.parseInt(markdownTypeOfString);
			}
		}
        //满减
        model.addAttribute("itemIds",itemIds);
        model.addAttribute("meetPrice",meetPrice);
        model.addAttribute("discountPrice",discountPrice);
        //直降活动
        model.addAttribute("discountPercent",percent);
        model.addAttribute("markdownType",markdownType);
        //促销基本信息
		model.addAttribute("pager", pager);
		model.addAttribute("itemInDTO",itemInDTO);
		model.addAttribute("type",type);
        model.addAttribute("checkedItemIds",itemIds);
        model.addAttribute("promotionInfo", promotionInfo);
        model.addAttribute("isAllItem",isAllItem);
        //会员等级
        model.addAttribute("vipLevels", VipLevelEnum.values());
		return "/sellcenter/salePromotion/addPromotionReductionInfo";
	}


	/**
	 * 满减和直降活动促销信息保存
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/saveReductionPromotion")
	@ResponseBody
	public Map<String, Object> saveReductionPromotion(HttpServletRequest request) throws ParseException {
		ExecuteResult<String> result = new ExecuteResult<String>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(3);
        String message="";
		try {
            String checkedItemIds=request.getParameter("checkedItemIds");//商品分页选中的商品id
			String promotionId=request.getParameter("promotionId");//促销id
			String type = request.getParameter("type");
			String isAllItem=request.getParameter("isAllItem");
			// 获取店铺id
			long shopId = WebUtil.getInstance().getShopId(request);
			// 解析商品id
			String beginDate = request.getParameter("beginDate");//开始时间
			String discountPercent=request.getParameter("discountPercent");//折扣
			String endDate = request.getParameter("endDate");//结束时间
			String meetPrice = request.getParameter("meetPrice");//满减金额
			String discountPrice = request.getParameter("discountPrice");//优惠金额
			String markdownType = request.getParameter("markdownType");//优惠计算方式
			String words = request.getParameter("words");//广告语
			String activityName = request.getParameter("activityName");//促销名称
			String itemId = request.getParameter("itemIds");//新的商品ids
			String userType=request.getParameter("userType");//用户类型
		/*	String membershipLevel=request.getParameter("membershipLevel");//会员等级
*/            if(null!=checkedItemIds && !"".equals(checkedItemIds)){
                itemId=checkedItemIds;
            }
			Long userId = WebUtil.getInstance().getUserId(request);
			UserDTO user=userExportService.queryUserById(userId);
			String userNmae = this.getLoginUserName(request);
			PromotionInDTO itemInDTO = new PromotionInDTO();
			itemInDTO.setShopId(shopId);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startTime = formatter.parse(beginDate);
			Date endTime = formatter.parse(endDate);
			List<PromotionFrDTO> promotionFrList = new ArrayList<PromotionFrDTO>();//满减
			List<PromotionMdDTO> promotionMarkdownList=new ArrayList<PromotionMdDTO>();
			PromotionInfoDTO promotionInfo = new PromotionInfoDTO();
			String[] itemIds = new String[]{};
			if(itemId != null){
				itemIds=itemId.split(",");
			}
			String state="0";
			if(itemIds!=null&&itemIds.length>0){
				for (int i = 0; i < itemIds.length; i++) {
					if(type!=null && "2".equals(type)){
						PromotionFrDTO promotionFullReduction = new PromotionFrDTO();
						promotionFullReduction.setItemId(Long.valueOf(itemIds[i]));
						promotionFullReduction.setCreateTime(new Date());
						promotionFullReduction.setDiscountPrice(new BigDecimal(discountPrice));
						promotionFullReduction.setMeetPrice(new BigDecimal(meetPrice));
						promotionFrList.add(promotionFullReduction);
					}else{
						PromotionMdDTO promotionMarkdown = new PromotionMdDTO();
						promotionMarkdown.setItemId(Long.valueOf(itemIds[i]));
						promotionMarkdown.setCreateTime(new Date());
						promotionMarkdown.setMarkdownType(Integer.parseInt(markdownType));
	                    BigDecimal discountPercenter=new BigDecimal("0.000");
	                    discountPercenter=new BigDecimal(discountPercent).divide(new BigDecimal("10"));
	                    if ("1".equals(markdownType)) {
	                    	promotionMarkdown.setDiscountPercent(discountPercenter);
	                    } else {
	                    	promotionMarkdown.setPromotionPrice(new BigDecimal(discountPercent));;
	                    	Long[] longItemIds=(Long[]) ConvertUtils.convert(itemIds,Long.class);
	                    	resultMap=checkPromotionPrice(longItemIds,promotionMarkdown.getPromotionPrice(),message,resultMap);
	                        if(!resultMap.isEmpty()){
	                        	return resultMap;
	                        }
	                    }
						state="1";
						promotionMarkdownList.add(promotionMarkdown);
					}
				}
			}else{
				if(type!=null && "2".equals(type)){
					PromotionFrDTO promotionFullReduction = new PromotionFrDTO();
					promotionFullReduction.setCreateTime(new Date());
					promotionFullReduction.setDiscountPrice(new BigDecimal(discountPrice));
					promotionFullReduction.setMeetPrice(new BigDecimal(meetPrice));
					promotionFullReduction.setItemId(0l);
					promotionFrList.add(promotionFullReduction);
				}else{
					PromotionMdDTO promotionMarkdown = new PromotionMdDTO();
					promotionMarkdown.setCreateTime(new Date());
					promotionMarkdown.setItemId(0l);
					promotionMarkdown.setMarkdownType(Integer.parseInt(markdownType));
                    BigDecimal discountPercenter=new BigDecimal("0.000"); 
                    discountPercenter=new BigDecimal(discountPercent).divide(new BigDecimal("10"));
                    if ("1".equals(markdownType)) {
                    	promotionMarkdown.setDiscountPercent(discountPercenter);
                    } else {
                    	promotionMarkdown.setPromotionPrice(new BigDecimal(discountPercent));
                    	ItemQueryInDTO dto=new ItemQueryInDTO();
                    	Long shopIds[]=new Long[]{getShopId(request)};
                    	dto.setShopIds(shopIds);
                    	DataGrid<ItemQueryOutDTO> dg=itemExportService.queryItemList(dto, null);
                    	List<Long> lists=new ArrayList<Long>();
                    	for(ItemQueryOutDTO out:dg.getRows()){
                    		lists.add(out.getItemId());
                    	}
                    	Long[] array = new Long[lists.size()];
                    	array=(Long[]) lists.toArray(array);
                    	resultMap=checkPromotionPrice(array,promotionMarkdown.getPromotionPrice(),message,resultMap);
                        if(!resultMap.isEmpty()){
                        	return resultMap;
                        }
                        
                    }
					state="1";
					promotionMarkdownList.add(promotionMarkdown);
				}
			}
			promotionInfo.setActivityName(activityName);
			promotionInfo.setEndTime(endTime);
			promotionInfo.setStartTime(startTime);
			promotionInfo.setType(Integer.parseInt(type));
			promotionInfo.setShopId(getShopId(request));
			promotionInfo.setSellerId(userId);
			promotionInfo.setCreateUser(userNmae);
			promotionInfo.setWords(words);
			promotionInfo.setOnlineState(1);
			promotionInfo.setIsAllItem(Integer.parseInt(isAllItem));
			if(user.getPlatformId()==null){
				promotionInfo.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_KY_ID);
			}else{
				promotionInfo.setPlatformId(com.camelot.openplatform.common.Constants.PLATFORM_ID);
			}
			
			if(StringUtils.isNotEmpty(userType)){
				promotionInfo.setUserType(Integer.parseInt(userType));
			}
			result=addFullMarkDown(state,promotionFrList,promotionMarkdownList,promotionInfo,promotionId);
			if(result.isSuccess()){
				resultMap.put("result",true);
			}else if(result.getResult()!=null) {
				String strList=result.getResult();
                String[] strs=strList.split(",");
				Long[] strLong=(Long[]) ConvertUtils.convert(strs,Long.class);
				List<ItemDTO> imteDtoList=itemExportService.getItemDTOByItemIds(strLong);
				StringBuffer strListBuffer=new StringBuffer();
				for(ItemDTO item:imteDtoList){
					strListBuffer.append("'"+item.getItemName()+"'、");

				}
				if(StringUtils.isBlank(strList)){
					message="此有效的时间内已经有商品参加了直降活动，不允许再次参加！";
				}else{
					message="此"+strList+"商品在有效的时间内已经参加了直降活动，不允许再次参加！";
				}
				resultMap.put("message",message);
			}else if(result.getResult()==null) {
				resultMap.put("message",result.getErrorMessages());
			}else{
				resultMap.put("result",false);
			}
		} catch (RuntimeException e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	public ExecuteResult<String> addFullMarkDown(String state,List<PromotionFrDTO> promotionFrList,
		List<PromotionMdDTO> promotionMarkdownList,PromotionInfoDTO promotionInfo,String promotionId){
		ExecuteResult<String> result=new ExecuteResult<String>();
		if (state != null && state.equals("0")) {
			//满减
			PromotionFullReducitonInDTO promotionFullReducitonInDTO = new PromotionFullReducitonInDTO();
			promotionFullReducitonInDTO.setPromotionFrDTOList(promotionFrList);
			if(promotionId!=null && !promotionId.equals("")){
				promotionInfo.setId(Long.parseLong(promotionId));
				promotionFullReducitonInDTO.setPromotionInfoDTO(promotionInfo);
				result = promotionService.editPromotionFullReduciton(promotionFullReducitonInDTO);
			}else{
				promotionFullReducitonInDTO.setPromotionInfoDTO(promotionInfo);
				result = promotionService.addPromotionFullReduciton(promotionFullReducitonInDTO);
			}
		} else if (state != null && state.equals("1")) {
			//直降活动
			PromotionMarkdownInDTO promotionMarkdownInDTO = new PromotionMarkdownInDTO();
			promotionMarkdownInDTO.setPromotionMdDTOList(promotionMarkdownList);
			if(promotionId!=null && !promotionId.equals("")){
				promotionInfo.setId(Long.parseLong(promotionId));
				promotionMarkdownInDTO.setPromotionInfoDTO(promotionInfo);
				result = promotionService.editPromotionMarkdown(promotionMarkdownInDTO);
			} else {
				promotionMarkdownInDTO.setPromotionInfoDTO(promotionInfo);
				result = promotionService.addPromotionMarkdown(promotionMarkdownInDTO);
			}
		}
		return result;
	}

	/**
	 * 中止活动（只能查看和删除）
	 * @param request
	 * @return
	 */
	@RequestMapping("endPromotion")
	@ResponseBody
	private Map<String, Object> endPromotion(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String promotionId=request.getParameter("promotionId");
			Long[] itemIdArray=new Long[1];
			itemIdArray[0]=Long.parseLong(promotionId);
			ExecuteResult<String> result = promotionService
					.modifyPromotionOnlineState(Integer.parseInt("3"), itemIdArray);
			if(result.isSuccess()){
				resultMap.put("result",true);
			}else{
				resultMap.put("result",false);
			}
		} catch (Exception e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 批量删除活动
	 * @param request
	 * @return
	 */
	@RequestMapping("deleteAll")
	@ResponseBody
	public Map<String,Object> deleteAll(HttpServletRequest request){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			String promotionId=request.getParameter("ids");
			// 解析商品id
			String[] itemIds = promotionId.split(",");
			Long[] itemIdArray = new Long[itemIds.length];
			for (int i = 0; i < itemIds.length; i++) {
				itemIdArray[i] = new Long(itemIds[i]);
			}
			ExecuteResult<String> result = promotionService
					.deletePromotion(itemIdArray);
			if(result.isSuccess()){
				resultMap.put("result",true);
			}else{
				resultMap.put("result",false);
			}
		} catch (Exception e) {
			resultMap.put("result",false);
			resultMap.put("message",e.getMessage());
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 根据商品id集合查询小于直降金额的商品
	 * @param longItemIds
	 * @param promotionPrice
	 * @param message
	 * @param resultMap
	 * @return
	 */
	private Map<String, Object> checkPromotionPrice(Long[] longItemIds,BigDecimal promotionPrice,String message,Map<String, Object> resultMap){
		if(longItemIds!=null){
			ExecuteResult<List<Long>> res=itemExportService.findItemsByMrkDownPrice(longItemIds,promotionPrice);
			if(res!=null && !res.getResult().isEmpty()){
	         		message="此"+res.getResult().toString().replace("[", "").replace("]", "")+"商品的sku销售价格小于直降金额";
	             	resultMap.put("message",message);
	                return resultMap;
	         }
		}
		return resultMap;
	}

}
