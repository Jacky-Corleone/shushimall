package com.camelot.mall.sonhome;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.dto.ItemDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.common.CommonService;
import com.camelot.mall.home.HomeService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.tradecenter.dto.TradeOrdersQueryInDTO;
import com.camelot.tradecenter.dto.enums.TradeOrdersStateEnum;
import com.camelot.tradecenter.service.TradeOrderExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserPersonalInfoService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.WebUtil;
/**
 * 
 * <p>Description: 查询子站  页面显示信息</p>
 * Created on 2015年12月22日
 * @author  <a href="mailto:wangpeng34@camelotchina.com">王鹏</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/sonHome")
public class SonHomeController {
	private static final Logger LOG = LoggerFactory.getLogger(SonHomeController.class);
	@Resource
	private NoticeExportService noticeSevice;
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private HomeService homeService;
	@Resource
	private UserExportService userService;
	@Resource
	private TradeOrderExportService tradeOrderService;
	@Resource
	private CommonService commonService;
	@Resource
	private MallRecExportService mallRecService;
	@Resource
	private UserPersonalInfoService userPersionInfoService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private ShopExportService  shopExportService;
	@Resource
	private MallThemeService mallThemeService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private ItemExportService itemService;
	
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request,Model model) throws Exception{
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		String cid=request.getParameter("cid");
		String lev=request.getParameter("lev");
		if (StringUtils.isBlank(cid) ||StringUtils.isBlank(lev)) {
			return "redirect:/";
		}
		mallThemeDTO.setcId(Long.parseLong(cid));
		mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setClev(Integer.parseInt(lev));
		mallThemeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		DataGrid<MallThemeDTO> queryMallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO, "1", null);
		if(queryMallThemeList==null||queryMallThemeList.getRows().size()==0){
			return "redirect:/";
		}
		MallThemeDTO dto=queryMallThemeList.getRows().get(0);
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
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(6);
		MallBannerDTO mallBannerDTO = new MallBannerDTO();
		//状态为上架
		mallBannerDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getStringValue());
		//设置轮播图类型
		//mallBannerDTO.setType(BasicEnum.INT_ENUM_BANNERTYPE_BIGWHEEL.getStringValue());
		mallBannerDTO.setThemeId(dto.getId());
		mallBannerDTO.setBannerType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());//类目
		//轮播图取数逻辑  已发布，并且当前时间在开始时间与结束时间之间
		DataGrid<MallBannerDTO> banners = mallBannerService.queryMallBannerList(mallBannerDTO,"1", page);
		LOG.debug("BUNNER:" + JSON.toJSONString(banners.getRows()));


		//查询本周推荐广告
		page.setRows(6);
		MallAdQueryDTO weekAdDTO = new MallAdQueryDTO();
		weekAdDTO.setStatus(1);
		weekAdDTO.setAdType(6);
		weekAdDTO.setThemeId(dto.getId());
		weekAdDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> weekAdes = this.mallAdvertisService.queryMallAdList(page, weekAdDTO);

		
		//查询轮播下广告位
		page.setRows(6);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(1);
		mallAdQueryDTO.setThemeId(dto.getId());
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		
		
		
		//旗舰店广告
		page.setRows(6);
		MallAdQueryDTO flagshipStore = new MallAdQueryDTO();
		flagshipStore.setStatus(1);
		flagshipStore.setAdType(1);
		flagshipStore.setThemeId(dto.getId());
		flagshipStore.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> shipStore = this.mallAdvertisService.queryMallAdList(page, flagshipStore);
		//查询旗舰店的店铺名称和logo的路径
		JSONArray array = new JSONArray();
		for (MallAdDTO inquiry : shipStore.getRows()) {
			JSONObject obj = JSON.parseObject(JSON.toJSONString(inquiry));
			if(inquiry.getSkuId()!=null){
				ExecuteResult<ShopDTO> shop=shopExportService.findShopInfoById(inquiry.getSkuId());
				if(shop!=null&&shop.getResult()!=null){
					obj.put("shopName",shop.getResult().getShopName());
					obj.put("logoUrl",shop.getResult().getLogoUrl());
				}
			}
			array.add(obj);
		}
		
		//旗舰店底部广告
		page.setRows(10);
		MallAdQueryDTO bottomAdv = new MallAdQueryDTO();
		bottomAdv.setStatus(1);
		bottomAdv.setAdType(10);
		bottomAdv.setThemeId(dto.getId());
		bottomAdv.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> shipStoreAdv = this.mallAdvertisService.queryMallAdList(page, bottomAdv);
		
		
		model.addAttribute("banners", banners.getRows());
		model.addAttribute("advertises", advertises.getRows());
		model.addAttribute("weekAdes", weekAdes.getRows());
		model.addAttribute("shipStores",array);
		model.addAttribute("bottomAdvs",shipStoreAdv.getRows());

		//子站底部广告
		page.setRows(6);
		MallAdQueryDTO bottomAd= new MallAdQueryDTO();
		bottomAd.setStatus(1);
		bottomAd.setAdType(3);
		bottomAd.setThemeId(dto.getId());
		bottomAd.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> bottomAds = this.mallAdvertisService.queryMallAdList(page, bottomAd);
		if(bottomAds!=null&&bottomAds.getRows().size()>0){
			model.addAttribute("bottomAd", bottomAds.getRows().get(0));
		}
		MallRecDTO mallRecDTO = new MallRecDTO();
		mallRecDTO.setStatusDTO(1);
		page.setPage(1);
		page.setRows(100);
		mallRecDTO.setThemeId(dto.getId());
		mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());//商城类目子站楼层
		DataGrid<MallRecDTO> dg = mallRecService.queryMallRecList(mallRecDTO, page);
		
		model.addAttribute("floors", dg.getRows());
		model.addAttribute("mallThemeDTO", dto);
		LOG.info("floors：{}", JSON.toJSONString(dg));
		return "/sonhome/index";
	}
	
	/**
	 * 
	 * <p>Discription:首页登录块查询</p>
	 * Created on 2015年3月11日
	 * @param request
	 * @param model
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/loginFragement")
	public String loginFragement(HttpServletRequest request,Model model){
		String type=request.getParameter("type");
		String utoken = LoginToken.getLoginToken(request);
		RegisterDTO dto = this.userService.getUserByRedis(utoken);
		if( dto != null){
			// TODO GOMA 废弃 通知【UserCenter】开发调整，追加用户头像URL
			// 用户类型（1-普通用户，2-买家，3-卖家, 4-平台）
			UserDTO user = this.userService.queryUserById(dto.getUid());
			@SuppressWarnings("rawtypes")
			Pager page = new Pager();
			page.setPage(1);
			page.setRows(6);

			ShopDTO shopDTO=new ShopDTO();
			shopDTO.setSellerId(user.getUid());
			ExecuteResult<DataGrid<ShopDTO>> shops = this.shopExportService.findShopInfoByCondition(shopDTO, page);


			JSONObject u = JSON.parseObject(JSON.toJSONString(user));
			ExecuteResult<String> er = this.userPersionInfoService.getPersonlInfoPerfectDegree(user.getUid().toString());
			if(er.isSuccess())
				u.put("perfectDegree", er.getResult());
			
			model.addAttribute("user", u);
			ExecuteResult<List<Long>> idsEr = userExportService.queryUserIds(user.getUid());
			
			/** 查询用户待付款、待确认、待评价 订单数量**/
			TradeOrdersQueryInDTO toqi = new TradeOrdersQueryInDTO();
			// 未取消
			//toqi.setCancelFlag(1);
			Long shopId = WebUtil.getInstance().getShopId(request);
			if(type!=null && "buyer".equals(type)){
				if(idsEr.isSuccess()){
					toqi.setBuyerIdList(idsEr.getResult());
				}
				toqi.setUserType(1);
				toqi.setDeleted(1);
				// 待付款
				toqi.setState(TradeOrdersStateEnum.PAYING.getCode());
				ExecuteResult<Long> erPaying = this.tradeOrderService.queryOrderQty(toqi);
				model.addAttribute("paying", erPaying.getResult());

				// 待评价
				toqi.setState(TradeOrdersStateEnum.EVALUATING.getCode());
				ExecuteResult<Long> erEvaluation = this.tradeOrderService.queryOrderQty(toqi);
				model.addAttribute("evaluating", erEvaluation.getResult());
				// 待确认收货
				toqi.setState(TradeOrdersStateEnum.CONFIRMING.getCode());
				ExecuteResult<Long> erConfirming = this.tradeOrderService.queryOrderQty(toqi);
				model.addAttribute("confirming", erConfirming.getResult());

				model.addAttribute("type", "buyer");
			}else{
				if(!shops.getResult().getRows().isEmpty()){
					toqi.setShopId(shopId);
					toqi.setUserType(2);
					toqi.setDeleted(1);
					//代配送
					toqi.setState(TradeOrdersStateEnum.DELIVERING.getCode());
					ExecuteResult<Long> erDelivering = this.tradeOrderService.queryOrderQty(toqi);
					model.addAttribute("delivering", erDelivering.getResult());
					model.addAttribute("type", "seller");
					//用户logoURL
					model.addAttribute("userLogoUrl", shops.getResult().getRows().get(0).getLogoUrl());
				}else{
					model.addAttribute("type", "noseller");
				}
			}
			
		}
		return "/home/login_mini";
	}
	
	/**
	 * 
	 * <p>Discription: 首页公告查询</p>
	 * Created on 2015年12月24日
	 * @param model
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping("/noticeFragement")
	public String noticeFragement(HttpServletRequest request,Model model){
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		String themId=request.getParameter("themId");
		if (StringUtils.isBlank(themId)) {
			return "redirect:/";
		}
		// 首页公告查询已发布
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(5);
		MallThemeDTO them=mallThemeService.getMallThemeById(Long.valueOf(themId));
		MallNoticeDTO dto = new MallNoticeDTO();
		dto.setNoticeStatus(1);
		dto.setPlatformId(Long.valueOf(0));
		dto.setThemeId(Integer.parseInt(themId));
		dto.setThemeType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());//类目子站公告
		LOG.debug("GET NOTICE BY PARAMS:"+JSON.toJSONString(dto));
		DataGrid<MallNoticeDTO> notices = this.noticeSevice.queryNoticeList(page,dto);
		model.addAttribute("notices", notices.getRows());
		model.addAttribute("cid",them.getcId());
		return "/sonhome/notice_mini";
	}
	
	@RequestMapping("/floor")
	public String floor(String num,String fid,Model model,HttpServletRequest request){
		String addresssCode = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		JSONObject floor = this.homeService.getFloor(fid,BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue(),addresssCode);
		String userToken = LoginToken.getLoginToken(request);
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				model.addAttribute("logging_status", "true");
			}else{
				model.addAttribute("logging_status", "false");
			}
		}
		model.addAttribute("floorNum", num);
		model.addAttribute("floor", floor);
		return "/sonhome/floor";
	}
	
	@RequestMapping("/categoryes")
	public String categoryes(Model model, String cid, HttpServletRequest request) {
//		JSONArray categoryes = this.commonService.findCategory();
//		model.addAttribute("categoryes", categoryes);
		String themId = request.getParameter("themId");
		if (StringUtils.isBlank(cid) || StringUtils.isBlank(themId)) {
			return "redirect:/";
		}
		ExecuteResult<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.queryItemByCategoryById(Long.parseLong(cid));
		model.addAttribute("itemCategoryDTO", itemCategoryDTO.getResult());
		JSONArray categoryes = this.commonService.findSonCategory(Long.parseLong(cid), Integer.parseInt(themId));
		model.addAttribute("categoryes", categoryes);
		model.addAttribute("cid", cid);
		model.addAttribute("themId", themId);
		LOG.info("平台所有类目：" + categoryes.toJSONString());
		return "/sonhome/category_all";
	}
	
	@RequestMapping("/topBunner")
	@ResponseBody
	public MallAdDTO queryTopBunner(HttpServletRequest request){
		String themId=request.getParameter("themId");
		if (StringUtils.isBlank(themId)) {
			return null;
		}
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(1);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(3);
		mallAdQueryDTO.setThemeId(Integer.parseInt(themId));
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		
		if( advertises != null && advertises.getRows().size() > 0 ){
			return advertises.getRows().get(0);
		}else{
			return null;
		}
	}
	/**
	 * 
	 * <p>Discription:[二级类目子站类目]</p>
	 * Created on 2015年12月23日
	 * @param model
	 * @param type
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping("/allCategory")
	public String allCategory(Model model,String type,String cid,HttpServletRequest request){
        String themId=request.getParameter("themId");
		if (StringUtils.isBlank(cid)||StringUtils.isBlank(themId)) {
			return "redirect:/";
		}
		ExecuteResult<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.queryItemByCategoryById(Long.parseLong(cid));
		model.addAttribute("itemCategoryDTO", itemCategoryDTO.getResult());
		JSONArray categoryes = this.commonService.findSonCategory(Long.parseLong(cid),Integer.parseInt(themId));
		model.addAttribute("categoryes", categoryes);
		model.addAttribute("type",type);
		LOG.debug("平台所有类目："+categoryes.toJSONString());
		return "/sonhome/left_category";
	}
	@RequestMapping("/guessLove")
	public String guessLove(Model model,HttpServletRequest request){
		String themId=request.getParameter("themId");
		
		if (StringUtils.isBlank(themId)) {
			return null;
		}
		Pager page = new Pager();
		page.setRows(3);
		page.setPage(1);
		
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(5);
		mallAdQueryDTO.setThemeId(Integer.parseInt(themId));
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		JSONArray items = new JSONArray();
		if (advertises != null && advertises.getRows() != null) {
			for (MallAdDTO rec : advertises.getRows()) {
				JSONObject item = JSON.parseObject(JSON.toJSONString(rec));
				ExecuteResult<ItemDTO> erItem = this.itemService.getItemById(rec.getSkuId());
				if( erItem != null && erItem.getResult() != null ){
					item.put("guidePrice", erItem.getResult().getGuidePrice());
				    item.put("hasPrice", erItem.getResult().getHasPrice());
				}
				if(item != null && !item.isEmpty()){
					items.add(item);
				}
			}
		}
		page.setRecords(items);
		page.setTotalCount(advertises.getTotal().intValue());
		
		model.addAttribute("guessLovePage", page);
		
		return "/sonhome/guess_love";
	}
	@RequestMapping("/forward/{cid}/{lev}")
	public String index(@PathVariable Long cid,@PathVariable Integer lev,RedirectAttributes attr,HttpServletRequest request){
		//查询主题
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(1);
		if (cid==null || lev==null) {
			return "redirect:/";
		}
		mallThemeDTO.setcId(cid);
		mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setClev(lev);
		mallThemeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		DataGrid<MallThemeDTO> queryMallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO, "1", page);
		if (queryMallThemeList.getRows()!=null && queryMallThemeList.getRows().size()>0) {
			attr.addAttribute("cid", cid);
			attr.addAttribute("lev", lev);
			return "redirect:/sonHome/index";
		}
		return "redirect:/searchController/searchItemByCategory?cid="+cid;
	}
}