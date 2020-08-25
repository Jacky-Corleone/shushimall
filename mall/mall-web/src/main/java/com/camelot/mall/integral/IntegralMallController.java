package com.camelot.mall.integral;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.AddressInfoDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.AddressInfoService;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SkuInfo;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.dto.IntegralItemViewDTO;
import com.camelot.mall.enums.IntegralItemTypeEnum;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.sellercenter.enums.MallTypeEnum;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.service.ShopCategorySellerExportService;

/**
 * 用于显示积分商城首页信息的业务逻辑层
 * 
 * @author 王东晓
 * @createdate 2015-12-07
 *
 */
@Controller
@RequestMapping("/integralMall")
public class IntegralMallController {
	
	private Logger LOG = Logger.getLogger(this.getClass());
	
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private ItemExportService itemExportService;
	@Resource
	private AddressInfoService addressInfoService;
	@Resource
	private AddressBaseService addressBaseService;
	@Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;
	/**
	 * 显示积分商城首页
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping("")
	public String integralMallIndex(Model model, HttpServletRequest request){
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(4);
		//获取积分商城首页轮播图信息
		//轮播图取数逻辑  已发布，并且当前时间在开始时间与结束时间之间
		DataGrid<MallBannerDTO> banners = this.mallBannerService.queryMallBannerList( "1",MallTypeEnum.INTEGRALMALL.getId(), page);
		LOG.debug("BUNNER:" + JSON.toJSONString(banners.getRows()));
		List<MallBannerDTO> bs = banners.getRows();
		model.addAttribute("banners", bs);
		model.addAttribute("bannerSize", bs.size());
		//查询轮播下广告位
		page.setRows(10);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(7);
		mallAdQueryDTO.setAdvType(MallTypeEnum.INTEGRALMALL.getId());
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		if(advertises !=null && advertises.getRows() != null && advertises.getRows().size() != 0 ){
			List<MallAdDTO> mallAdList = advertises.getRows();
			List<IntegralItemViewDTO> integralItemViewDTOList = new LinkedList<IntegralItemViewDTO>();
			IntegralItemViewDTO integralItemViewDTO = null;
			for(MallAdDTO mallAdDTO : mallAdList){
				integralItemViewDTO = new IntegralItemViewDTO();
				integralItemViewDTO.setMallAdDTO(mallAdDTO);
				Long skuId = mallAdDTO.getSkuId();
				ExecuteResult<ItemDTO> itemDTO = this.itemExportService.getItemBySkuId(skuId);
				integralItemViewDTO.setItemDTO(itemDTO.getResult());
				integralItemViewDTOList.add(integralItemViewDTO);
			}
			model.addAttribute("advertises", integralItemViewDTOList);
		}
		
		//积分商城首页商品推荐位
		page.setRows(9);
		mallAdQueryDTO.setAdType(8);
		DataGrid<MallAdDTO> advertisItems = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		if(advertisItems !=null && advertisItems.getRows() != null && advertisItems.getRows().size() != 0 ){
			List<MallAdDTO> mallAdList = advertisItems.getRows();
			List<IntegralItemViewDTO> integralItemViewDTOList = new LinkedList<IntegralItemViewDTO>();
			IntegralItemViewDTO integralItemViewDTO = null;
			for(MallAdDTO mallAdDTO : mallAdList){
				integralItemViewDTO = new IntegralItemViewDTO();
				integralItemViewDTO.setMallAdDTO(mallAdDTO);
				Long skuId = mallAdDTO.getSkuId();
				ExecuteResult<ItemDTO> itemDTO = this.itemExportService.getItemBySkuId(skuId);
				integralItemViewDTO.setItemDTO(itemDTO.getResult());
				integralItemViewDTOList.add(integralItemViewDTO);
			}
			model.addAttribute("advertisItems", integralItemViewDTOList);
		}
		
		//积分商城兑你喜欢
		page.setRows(10);
		mallAdQueryDTO.setAdType(9);
		DataGrid<MallAdDTO> advertisLikes = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		if(advertisLikes !=null && advertisLikes.getRows() != null && advertisLikes.getRows().size() != 0 ){
			List<MallAdDTO> mallAdList = advertisLikes.getRows();
			List<IntegralItemViewDTO> integralItemViewDTOList = new LinkedList<IntegralItemViewDTO>();
			IntegralItemViewDTO integralItemViewDTO = null;
			for(MallAdDTO mallAdDTO : mallAdList){
				integralItemViewDTO = new IntegralItemViewDTO();
				integralItemViewDTO.setMallAdDTO(mallAdDTO);
				Long skuId = mallAdDTO.getSkuId();
				ExecuteResult<ItemDTO> itemDTO = this.itemExportService.getItemBySkuId(skuId);
				integralItemViewDTO.setItemDTO(itemDTO.getResult());
				integralItemViewDTOList.add(integralItemViewDTO);
			}
			model.addAttribute("advertisLikes", integralItemViewDTOList);
		}
		
		return "/integralMall/integralMallIndex";
	}
	@RequestMapping("integralItemDetail")
	public String integralItemDetail(HttpServletRequest request , Model model,Long skuId,Long id,Integer integralType){
		String region = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		
		model.addAttribute("integralType", integralType);
		model.addAttribute("skuId", skuId);
		model.addAttribute("id", id);
		String url = "integralMall/integralItemDetail";
		
		if(skuId == null){
			return url;
		}
		//1、根根据skuId查询商品详情
		ExecuteResult<ItemDTO> itemResult = itemExportService.getItemBySkuId(skuId);
		ItemDTO itemDto = new ItemDTO();
		if(!itemResult.isSuccess()){
			return url;
		}else{
			//取到根据sku取到sku库存，取到sku图片
			itemDto = itemResult.getResult(); 
			List<SkuInfo> skuList = itemDto.getSkuInfos();
			if(skuList != null && !skuList.isEmpty()){
				for(SkuInfo sku : skuList){
					if(sku.getSkuId().toString().equals(skuId.toString())){
						//显示sku图片
						itemDto.setInventory(sku.getSkuInventory());
						String[] picArry = new String[sku.getSkuPics().size()];
						for(int i = 0 ;i<sku.getSkuPics().size(); i++){
							picArry[i] = sku.getSkuPics().get(i).getPicUrl();
						}
						itemDto.setPicUrls(picArry);
					}
				}
			}
		}
		model.addAttribute("item", itemDto);
		ItemDTO item = itemResult.getResult();
		
		List<ShopCategorySellerDTO> categorylist = buildCategoryLev(item.getShopId());//店铺产品分类
		model.addAttribute("categorylist", categorylist);
		
		ItemShopCartDTO dto = new ItemShopCartDTO();
		dto.setAreaCode(region);//省市区编码
		dto.setSkuId(Long.valueOf(skuId));//SKU id
		dto.setQty(1);//数量
		dto.setShopId(item.getShopId());//店铺ID
		dto.setItemId(item.getItemId());//商品ID
		ExecuteResult<ItemShopCartDTO> skuItem = itemExportService.getSkuShopCart(dto); //调商品接口查url
		model.addAttribute("skuItem", skuItem.getResult());
		
		//2、根据广告ID，查询商品积分信息，页面显示
		if(IntegralItemTypeEnum.BANNER.getId().equals(integralType)){
			MallBannerDTO  mallBannerDTO =  this.mallBannerService.getMallBannerById(id);
			Integer integral = mallBannerDTO.getIntegral();
			model.addAttribute("integral",integral);
		}else if(IntegralItemTypeEnum.AD.getId().equals(integralType)){
			MallAdDTO mallAdDTO = this.mallAdvertisService.getMallAdById(id);
			Integer integral = mallAdDTO.getIntegral();
			model.addAttribute("integral", integral);
		}
		return url;
	}
	@RequestMapping("integralOrderDetail")
	public String integralOrderDetail(HttpServletRequest request , Model model,Integer count,Long skuId,Integer integralType,Long id){
		model.addAttribute("id", id);
		model.addAttribute("count", count);
		model.addAttribute("skuId", skuId);
		model.addAttribute("integralType", integralType);
		
		if(IntegralItemTypeEnum.BANNER.getId().equals(integralType)){
			MallBannerDTO  mallBannerDTO =  this.mallBannerService.getMallBannerById(id);
			Integer integral = mallBannerDTO.getIntegral();
			model.addAttribute("integral", integral);
			model.addAttribute("integralSum", integral*count);
		}else if(IntegralItemTypeEnum.AD.getId().equals(integralType)){
			MallAdDTO mallAdDTO = this.mallAdvertisService.getMallAdById(id);
			Integer integral = mallAdDTO.getIntegral();
			model.addAttribute("integral", integral);
			model.addAttribute("integralSum", integral*count);
		}
		//商品信息
		ExecuteResult<ItemDTO> itemResult = this.itemExportService.getItemBySkuId(skuId);
		if(itemResult.isSuccess()){
			model.addAttribute("itemDTO", itemResult.getResult());
		}
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		// 收货地址变更
		String ckAddrId = request.getParameter("address");
		JSONObject json = this.getAddress(uid, ckAddrId);
		JSONObject ckAddress = json.getJSONObject("defAddress");
		model.addAttribute("addresses", json.get("addresses"));
		model.addAttribute("defAddress", ckAddress);
		if(!StringUtils.isEmpty(ckAddrId)){
			return "/integralMall/integralOrderDetail";
		}
		return "/integralMall/integralOrderView";
	}
	
	/**
	 * @desc： 获得用户收货地址及默认选中地址
	 * @param uid
	 * @param ckAddrId
	 * @return
	 */
	private JSONObject getAddress(String uid, String ckAddrId) {
		DataGrid<AddressInfoDTO> dgAddress = this.addressInfoService
				.queryAddressinfo(Long.valueOf(uid));
		JSONArray addresses = new JSONArray();
		JSONObject defAddress = null;
		JSONObject ckAddress = null;

		for (int i = 0; i < dgAddress.getRows().size(); i++) {
			AddressInfoDTO addr = dgAddress.getRows().get(i);
			JSONObject address = JSON.parseObject(JSON.toJSONString(addr));
			ExecuteResult<AddressBaseDTO> erProvice = this.addressBaseService
					.queryNameById(Integer.valueOf(addr.getProvicecode()));
			address.put("provicename", erProvice.getResult().getName());
			ExecuteResult<AddressBaseDTO> erCity = this.addressBaseService
					.queryNameById(Integer.valueOf(addr.getCitycode()));
			address.put("cityname", erCity.getResult().getName());
			ExecuteResult<AddressBaseDTO> erCountry = this.addressBaseService
					.queryNameById(Integer.valueOf(addr.getCountrycode()));
			address.put("countryname", erCountry.getResult().getName());
			if (ckAddrId != null && !"".equals(ckAddrId)
					&& addr.getId().compareTo(Long.valueOf(ckAddrId)) == 0)
				ckAddress = address;
			if (i == 0)
				defAddress = address;
			if (addr.getIsdefault() == 1)
				defAddress = address;
			addresses.add(address);
		}
		if (ckAddress == null)
			ckAddress = defAddress;

		JSONObject json = new JSONObject();
		json.put("defAddress", ckAddress);
		json.put("addresses", addresses);
		return json;
	}
	//组装list：将子类目加入到对应的父类目中去
	private List<ShopCategorySellerDTO> buildCategoryLev(Long shopId) {
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setShopId(shopId);
		dto.setStatus(1);
		dto.setHomeShow(1);
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(dto, null);
		List<ShopCategorySellerDTO> list = result.getResult().getRows();

		List<ShopCategorySellerDTO> buildlist = new ArrayList<ShopCategorySellerDTO>();
		for(int i=0; null!=list && i<list.size(); i++){
			ShopCategorySellerDTO scParent = list.get(i);
			for(int j=0; j<list.size(); j++){
				//将一级类目的所有二级类目加入到一级类目的list中
				ShopCategorySellerDTO scChild = list.get(j);
				if(scParent.getCid() == scChild.getParentCid()){
					scParent.getChildShopCategory().add(scChild);
				}
			}
			//先将一级类目全部加入到buildlist中
			if(scParent.getLev()==1){
				buildlist.add(scParent);
			}
		}
		return buildlist;
	}
}
