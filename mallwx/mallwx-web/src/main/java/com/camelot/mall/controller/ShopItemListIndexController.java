package com.camelot.mall.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.BaseTDKConfigDTO;
import com.camelot.basecenter.dto.PlatformServiceRuleDTO;
import com.camelot.basecenter.service.PlatformServiceRuleExportService;
import com.camelot.goodscenter.dto.FavouriteCountDTO;
import com.camelot.goodscenter.dto.ItemBrandDTO;
import com.camelot.goodscenter.dto.ItemQueryInDTO;
import com.camelot.goodscenter.dto.ItemQueryOutDTO;
import com.camelot.goodscenter.dto.ItemSkuDTO;
import com.camelot.goodscenter.dto.SearchInDTO;
import com.camelot.goodscenter.dto.SearchOutDTO;
import com.camelot.goodscenter.service.ItemBrandExportService;
import com.camelot.goodscenter.service.ItemExportService;
import com.camelot.goodscenter.service.ItemFavouriteExportService;
import com.camelot.goodscenter.service.SearchItemExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.dto.ShopRenovationListDTO;
import com.camelot.mall.service.FavouriteService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.dao.util.RedisDB;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.notice.dto.MallNoticeDTO;
import com.camelot.sellercenter.notice.service.NoticeExportService;
import com.camelot.storecenter.dto.ShopBrandDTO;
import com.camelot.storecenter.dto.ShopCategorySellerDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.dto.ShopFavouriteDTO;
import com.camelot.storecenter.dto.ShopRenovationDTO;
import com.camelot.storecenter.dto.ShopTemplatesDTO;
import com.camelot.storecenter.dto.combin.ShopTemplatesCombinDTO;
import com.camelot.storecenter.service.ShopBrandExportService;
import com.camelot.storecenter.service.ShopCategorySellerExportService;
import com.camelot.storecenter.service.ShopExportService;
import com.camelot.storecenter.service.ShopRenovationExportService;
import com.camelot.storecenter.service.ShopTemplatesExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.userInfo.UserInfoDTO;
import com.camelot.usercenter.dto.userrule.UserPlatformServiceRuleDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserExtendsService;
import com.camelot.usercenter.service.UserPlatformServiceRuleService;
import com.camelot.usercenter.util.LoginToken;
import com.camelot.util.StationUtil;
import com.camelot.util.WebUtil;

/**
 * <p>Description: [店铺商品列表]</p>
 * Created on 2015年3月6日
 * @author  <a href="mailto: zhoule@camelotchina.com">周乐</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("/shopItemListIndexController")
public class ShopItemListIndexController {
	@Resource
	private ItemExportService itemExportService;

	@Resource
	private UserExportService userExportService;

	@Resource
	private ShopCategorySellerExportService shopCategorySellerExportService;

	@Resource
	private ShopExportService shopExportService;

	@Resource
	private UserPlatformServiceRuleService userPlatformServiceRuleService;

	@Resource
	private PlatformServiceRuleExportService platformServiceRuleExportService;

	@Autowired
	private NoticeExportService noticeExportService;

	@Resource
	private ShopTemplatesExportService shopTemplatesExportService;

	@Resource
	private ShopRenovationExportService shopRenovationExportService;

	@Resource
	private ItemFavouriteExportService itemFavouriteExportService;

	@Resource
	private SearchItemExportService searchItemExportService;

	@Resource
	private UserExtendsService userExtendsService;

	@Resource
	private FavouriteService favouriteService;

	@Resource
	private RedisDB redisDB;

	@Resource
	private ShopBrandExportService shopBrandExportService;

	@Resource
	private ItemBrandExportService itemBrandExportService;

	//redis中店铺装修模版信息存储的键的前缀
	public static String REDIS_DECORATE_PREFIX = "SHOPDECORATE_";

	/**
	 * 跳转到店铺产品信息列表页面
	 * @param page
	 * @param shopId
	 * @param itemInDTO
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toListIndex")
	public String toListIndex(Integer page, Long shopId, SearchInDTO itemInDTO,HttpServletRequest request,Model model){
		model.addAttribute("curMenu", "allproduct");//页面标识：用于控制头部样式

		//获取当前店铺的所有商品的第一页的数据
		itemInDTO.setShopId(shopId);
		//初始化数据
		initLeftPage(shopId, itemInDTO, model, request);

		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		if(uid != null && !"".equals(uid)){
			itemInDTO.setBuyerId(Long.valueOf(uid));
		}
		//组装店铺商品列表
		Pager<ItemSkuDTO> pager = buildPager(page, 9, itemInDTO);

		model.addAttribute("pager", pager);
		model.addAttribute("orderSort", itemInDTO.getOrderSort());
		model.addAttribute("keyword", itemInDTO.getKeyword());

		return "/sellcenter/shop/shopProList/" + getTemplateName(shopId, model);
	}
	/**
	 * 加载店铺商品
	 * @param page
	 * @param shopId
	 * @param itemInDTO
	 * @param request
	 * @return
	 */
	@RequestMapping("/toListIndex2")
	@ResponseBody
	public String toListIndex2(Integer page, Long shopId, SearchInDTO itemInDTO,HttpServletRequest request){
		Map<String,Object> modelMap=new HashMap<String, Object>();

		itemInDTO.setShopId(shopId);
		//初始化数据

		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		if(uid != null && !"".equals(uid)){
			itemInDTO.setBuyerId(Long.valueOf(uid));
		}
		//组装店铺商品列表
		Pager<ItemSkuDTO> pager = buildPager(page, 8, itemInDTO);

		//去掉无用的广告
		List<ItemSkuDTO> listIsd=pager.getRecords();
		for(ItemSkuDTO isd : listIsd){
			isd.setDescribeUrl(null);
		}
		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		modelMap.put("gix",imgUrlPre);
		modelMap.put("pager", pager);
		return JSON.toJSONString(modelMap);
	}

	/**
	 * 跳转到店铺首页面
	 * @param page
	 * @param shopId
	 * @param itemInDTO
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/toIndex")
	public String toIndex(Integer page, Long shopId, SearchInDTO itemInDTO,HttpServletRequest request,Model model){
		if(null == shopId){
			shopId = WebUtil.getInstance().getShopId(request);
		}
		//初始化数据
		initLeftPage(shopId, itemInDTO, model, request);
		model.addAttribute("curMenu", "index");//页面标识：用于控制头部样式

		//初始化店铺公告
		Pager<MallNoticeDTO> noticePager = new Pager<MallNoticeDTO>();
		noticePager.setRows(5);

		initNoticeList(noticePager, shopId, model);
		BaseTDKConfigDTO  baseTDKConfigDTO=this.getShopBrands(shopId);
		model.addAttribute("baseTDKConfigDTO2",baseTDKConfigDTO);
		//System.out.println(getTemplateName(shopId, model));
		return "/sellcenter/shop/shopIndex/" + getTemplateName(shopId, model);
	}


	public BaseTDKConfigDTO getShopBrands(Long shopId){

		ExecuteResult<ShopDTO> shopResult = shopExportService.findShopInfoById(shopId);
		ShopDTO shopInfo = shopResult.getResult();
		//根据店铺Id查询店铺名称
		BaseTDKConfigDTO  baseTDKConfigDTO2=new BaseTDKConfigDTO();
		baseTDKConfigDTO2.setTitle(shopInfo.getShopName()+" - 印刷家");

		baseTDKConfigDTO2.setDescription(shopInfo.getShopName()+","+shopInfo.getIntroduce()+"【印刷家，印刷采购好管家，全国配送，优惠多多】");


		ShopBrandDTO shopBrandDTO=new ShopBrandDTO ();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<ItemBrandDTO> itemBrandDTOList = new ArrayList<ItemBrandDTO>();
		//获取店铺id
		shopBrandDTO.setShopId(shopId);
		//审核通过
		shopBrandDTO.setStatus(2);
		ExecuteResult<DataGrid<ShopBrandDTO>> executeResult = this.shopBrandExportService.queryShopBrand(shopBrandDTO, null);
		StringBuffer brandInfo= new StringBuffer();
		if(executeResult.isSuccess()){
			brandInfo.append(shopInfo.getShopName());
			List<ShopBrandDTO> shopBrandDTOList = executeResult.getResult().getRows();
			List<String> list = new ArrayList<String>();
			for(int i=0; i<shopBrandDTOList.size(); i++) {
				String str = shopBrandDTOList.get(i).getBrandId().toString(); //获取传入集合对象的每一个元素
				if (!list.contains(str)) { //查看新集合中是否有指定的元素，如果没有则加入
					list.add(str);
					ExecuteResult<List<ItemBrandDTO>> result = itemBrandExportService.queryItemBrandByIds(shopBrandDTOList.get(i).getBrandId());
					if(result.isSuccess() && result.getResult() != null && result.getResult().size() > 0)
						brandInfo.append(","+result.getResult().get(0).getBrandName());
				}
			}
		}
		baseTDKConfigDTO2.setKeywords(brandInfo.toString());

		return baseTDKConfigDTO2;
	}
	/**
	 * 初始化店铺公告列表
	 * @param shopId
	 * @param model
	 */
	private void initNoticeList(Pager<MallNoticeDTO> noticePager, Long shopId, Model model) {
		MallNoticeDTO noticeDTO = new MallNoticeDTO();
		noticeDTO.setPlatformId(shopId);	//根据店铺id查询
		noticeDTO.setNoticeStatus(1);		//生效的公告
		DataGrid<MallNoticeDTO> datagrid = noticeExportService.queryNoticeList(noticePager, noticeDTO);
		model.addAttribute("noticeList", datagrid.getRows());
	}

	/**
	 * 初始化店铺装修信息并返回店铺模版名称
	 * @param shopId
	 * @param model
	 * @return
	 */
	private String getTemplateName(Long shopId, Model model) {
		ExecuteResult<ShopTemplatesCombinDTO> exeResult = shopRenovationExportService.queryShopRenovationByShopId(shopId);
		ShopTemplatesCombinDTO shopTemplesCombin = exeResult.getResult();
		ShopTemplatesDTO templesDTO = new ShopTemplatesDTO();
		Map<String, ShopRenovationDTO> renovationMap = new HashMap<String, ShopRenovationDTO>();
		String templatesName = "shopTemplate1";
		if(null != shopTemplesCombin){
			templesDTO = shopTemplesCombin.getShopTemplatesDTO();
			renovationMap = shopTemplesCombin.getShopRenovationmap();
			if(null != templesDTO){
				templatesName = templesDTO.getTemplatesName();
			}
		}
		//宋文斌start
		List<ShopRenovationDTO> templatesDTOs = new ArrayList<ShopRenovationDTO>();
		if (renovationMap.get("2a") != null
				&& !StringUtils.isBlank(renovationMap.get("2a").getPictureUrl())) {
			ShopRenovationDTO a2DTO = renovationMap.get("2a");
			a2DTO.setChainUrl(WebUtil.getInstance().addProtocol(a2DTO.getChainUrl(), "http://"));
			templatesDTOs.add(renovationMap.get("2a"));
		}
		if (renovationMap.get("2b") != null
				&& !StringUtils.isBlank(renovationMap.get("2b").getPictureUrl())) {
			ShopRenovationDTO b2DTO = renovationMap.get("2b");
			b2DTO.setChainUrl(WebUtil.getInstance().addProtocol(b2DTO.getChainUrl(), "http://"));
			templatesDTOs.add(renovationMap.get("2b"));
		}
		if (renovationMap.get("2c") != null
				&& !StringUtils.isBlank(renovationMap.get("2c").getPictureUrl())) {
			ShopRenovationDTO c2DTO = renovationMap.get("2c");
			c2DTO.setChainUrl(WebUtil.getInstance().addProtocol(c2DTO.getChainUrl(), "http://"));
			templatesDTOs.add(renovationMap.get("2c"));
		}
		model.addAttribute("banners",templatesDTOs);
		//宋文斌end
		model.addAttribute("shopRenovationMap", renovationMap);
		model.addAttribute("shopTemplates", templesDTO);
		return templatesName;
	}

	/**
	 * 初始化店铺左侧页面数据
	 * @param shopId
	 * @param itemInDTO
	 * @param model
	 * @return
	 */
	private String initLeftPage(Long shopId, SearchInDTO itemInDTO, Model model, HttpServletRequest request) {
		if(shopId==null){//没有店铺id跳转到网站首页
			return "redirect:/";
		}
		//1、店铺信息
		ExecuteResult<ShopDTO> result = shopExportService.findShopInfoById(shopId);
		if(null==result || null==result.getResult()){
			return "redirect:/";
		}
		ShopDTO shopInfo = result.getResult();

		//2、用户信息
		UserDTO user = userExportService.queryUserById(shopInfo.getSellerId());

		//3、获取当前店铺的所有产品分类(自定义的分类)
		List<ShopCategorySellerDTO> listCategory = buildShopCatList(shopId);

		//4、查询认证信息:根据用户id获取平台规则信息，根据规则id获取平台服务规则表中的规则信息（最终拿到图片的url地址）
		List<PlatformServiceRuleDTO> listRule = getPlatformRule(shopInfo.getSellerId());

		//5、获得商铺站点
		String stationId = StationUtil.getStationIdByShopId(shopId);
		String userToken = LoginToken.getLoginToken(request);
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				ShopFavouriteDTO shopfavourite = new ShopFavouriteDTO();
				shopfavourite.setUserId(registerDto.getUid().intValue());
				shopfavourite.setShopId(shopInfo.getShopId().intValue());
				DataGrid<JSONObject> dgShops = this.favouriteService.shops(new Pager(), shopfavourite);
				if(dgShops!=null && dgShops.getTotal()>0){
					model.addAttribute("favouriteShop", "true");
				}else{
					model.addAttribute("favouriteShop", "false");
				}
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("shopInfo", shopInfo);
		model.addAttribute("categorylist", buildCategoryLev(listCategory));
		model.addAttribute("itemInDTO", itemInDTO);
		model.addAttribute("listRule", listRule);
		model.addAttribute("shopId", shopId);
		model.addAttribute("stationId", stationId);
		model.addAttribute("busLicenImgSrc", getBusinessLicenseImgSrc(user));

		return null;
	}

	/**
	 * 获取营业执照图片地址
	 * @param userDTO
	 * @return
	 */
	private String getBusinessLicenseImgSrc(UserDTO userDTO){
		if(null == userDTO){
			return "";
		}
		Long parentId = userDTO.getParentId();
		ExecuteResult<UserInfoDTO> result = new ExecuteResult<UserInfoDTO>();
		//判断是否为子账号
		if(parentId!=null && parentId.longValue() > 0){
			result = userExtendsService.findUserInfo(parentId);
		} else {
			result = userExtendsService.findUserInfo(userDTO.getUid());
		}
		UserInfoDTO userInfoDTO = result.getResult();
		if(userInfoDTO==null){
			userInfoDTO = new UserInfoDTO();
		}
		if(null!=userInfoDTO.getUserBusinessDTO() && StringUtils.isNotBlank(userInfoDTO.getUserBusinessDTO().getBusinessLicencePicSrc())){
			return userInfoDTO.getUserBusinessDTO().getBusinessLicencePicSrc();
		}
		return "";
	}

	/**
	 * 获取认证规则：用于显示店铺头部页面的认证小图标
	 * */
	private List<PlatformServiceRuleDTO> getPlatformRule(Long userId) {
		ExecuteResult<List<UserPlatformServiceRuleDTO>> ruleExeResult = userPlatformServiceRuleService.getUserPlatformRuleList(userId+"");
		Long[] ruleIds = getRuleIds(ruleExeResult.getResult());
		ExecuteResult<List<PlatformServiceRuleDTO>> listRuleResult = new ExecuteResult<List<PlatformServiceRuleDTO>>();
		if(ruleIds!=null && ruleIds.length>0){
			listRuleResult = platformServiceRuleExportService.queryRuleByRuleIds(ruleIds);
		}
		List<PlatformServiceRuleDTO> listRule = listRuleResult.getResult();
		return listRule;
	}

	//获取集合对象中的ruleid
	private Long[] getRuleIds(List<UserPlatformServiceRuleDTO> result) {
		if(result == null){
			return new Long[]{};
		}
		Long[] rids = new Long[result.size()];
		for(int i=0; i<result.size(); i++){
			rids[i] = result.get(i).getRuleId();
		}
		return rids;
	}

	//组装翻页数据
	private Pager<ItemSkuDTO> buildPager(Integer page, Integer pageSize, SearchInDTO itemInDTO) {
		Pager<ItemSkuDTO> pager = new Pager<ItemSkuDTO>();
		if(page == null){
			page = 1;
		}
		if(pageSize!=null){
			pager.setRows(pageSize);
		}
		pager.setPage(page);
		SearchOutDTO searchOutDTO = searchItemExportService.searchItem(itemInDTO, pager);
		DataGrid<ItemSkuDTO> dg = new DataGrid<ItemSkuDTO>();
		int totalCount = 0;
		List<ItemSkuDTO> listRecord = new ArrayList<ItemSkuDTO>();
		if(searchOutDTO != null){
			dg = searchOutDTO.getItemDTOs();
			if(dg != null && dg.getTotal()!=null){
				totalCount = searchOutDTO.getItemDTOs().getTotal().intValue();
				listRecord = searchOutDTO.getItemDTOs().getRows();
			}
		}
		pager.setTotalCount(totalCount);
		pager.setRecords(listRecord);
		return pager;
	}

	//组装当前店铺的类目列表数据
	private List<ShopCategorySellerDTO> buildShopCatList(Long shopId) {
		ShopCategorySellerDTO dto = new ShopCategorySellerDTO();
		dto.setShopId(shopId);
		dto.setStatus(1);
		dto.setHomeShow(1);
		ExecuteResult<DataGrid<ShopCategorySellerDTO>> result = shopCategorySellerExportService.queryShopCategoryList(dto, null);
		List<ShopCategorySellerDTO> list = result.getResult().getRows();
		return list;
	}

	//组装list：将子类目加入到对应的父类目中去
	private List<ShopCategorySellerDTO> buildCategoryLev(List<ShopCategorySellerDTO> list) {
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

	/**
	 * 跳转到店铺模版列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/toTempleList")
	public ModelAndView toTempleList(HttpServletRequest request){
		//查询店铺装修模版
		long shopId = WebUtil.getInstance().getShopId(request);
		List<ShopTemplatesDTO> shopTempleList = getShopTempList(shopId);
		return new ModelAndView("/sellcenter/shop/shopDecorate", "shopTempleList", shopTempleList);
	}

	/**
	 * 组装店铺模版信息
	 * @param shopId
	 * @return
	 */
	private List<ShopTemplatesDTO> getShopTempList(long shopId) {
		ShopTemplatesDTO shopTemplatesDTO = new ShopTemplatesDTO();
		shopTemplatesDTO.setShopId(shopId);
		ExecuteResult<List<ShopTemplatesDTO>> shopTempleList = shopTemplatesExportService.createShopTemplatesList(shopTemplatesDTO);
		return shopTempleList.getResult();
	}

	/**
	 * 跳转到浏览模版页面
	 * @param templateName
	 * @param request
	 * @return
	 */
	@RequestMapping("/templeView")
	public ModelAndView viewTemplate(String templateName, HttpServletRequest request){
		if(StringUtils.isBlank(templateName)){
			return null;
		}
		return new ModelAndView("/sellcenter/shop/templateView/"+templateName, "", null);
	}

	/**
	 * 跳转到店铺装修页面
	 * @param templeId
	 * @param templateName
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/templeEdit")
	public String templeEdit(Long templeId, String templateName, HttpServletRequest request, Model model){
		if(StringUtils.isBlank(templateName)){
			return null;
		}
		long shopId = WebUtil.getInstance().getShopId(request);
		if(null != templeId){
			//更新店铺模版的使用状态
			shopTemplatesExportService.modifyShopTemplatesStatus(templeId, shopId);
		}
		//初始化商家信息、产品分类
		initLeftPage(shopId, new SearchInDTO(), model, request);
		//初始化店铺公告
		Pager<MallNoticeDTO> noticePager = new Pager<MallNoticeDTO>();
		noticePager.setRows(5);
		initNoticeList(noticePager, shopId, model);

		ShopTemplatesCombinDTO shopRenovationMap = getShopRenovationByTempId(templeId);

		model.addAttribute("curMenu", "index");//页面标识：用于控制头部样式
		model.addAttribute("shopRenovationMap", shopRenovationMap.getShopRenovationmap());
		model.addAttribute("shopTemplates", shopRenovationMap.getShopTemplatesDTO());
		model.addAttribute("flag", "decorate");//用于判断是装修还是正常访问店铺首页
		model.addAttribute("shopId", shopId);

		ExecuteResult<ShopTemplatesCombinDTO> exeResult = shopRenovationExportService.queryShopRenovationByShopId(shopId);
		ShopTemplatesCombinDTO shopTemplesCombin = exeResult.getResult();
		ShopTemplatesDTO templesDTO = new ShopTemplatesDTO();
		Map<String, ShopRenovationDTO> renovationMap = new HashMap<String, ShopRenovationDTO>();
		if(null != shopTemplesCombin){
			templesDTO = shopTemplesCombin.getShopTemplatesDTO();
			renovationMap = shopTemplesCombin.getShopRenovationmap();
		}
		//宋文斌start
		List<ShopRenovationDTO> templatesDTOs = new ArrayList<ShopRenovationDTO>();
		if (renovationMap.get("2a") != null
				&& !StringUtils.isBlank(renovationMap.get("2a").getPictureUrl())) {
			templatesDTOs.add(renovationMap.get("2a"));
		}
		if (renovationMap.get("2b") != null
				&& !StringUtils.isBlank(renovationMap.get("2b").getPictureUrl())) {
			templatesDTOs.add(renovationMap.get("2b"));
		}
		if (renovationMap.get("2c") != null
				&& !StringUtils.isBlank(renovationMap.get("2c").getPictureUrl())) {
			templatesDTOs.add(renovationMap.get("2c"));
		}
		model.addAttribute("banners",templatesDTOs);
		//宋文斌end
		model.addAttribute("shopRenovationMap", renovationMap);
		model.addAttribute("shopTemplates", templesDTO);


		return "/sellcenter/shop/shopIndex/"+templateName;
	}

	/**
	 * 跳转到店铺预览页面
	 * @param templeId
	 * @param templateName
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/templePreview")
	public String templePreview(Long templeId, String templateName, HttpServletRequest request, Model model){
		if(StringUtils.isBlank(templateName)){
			return null;
		}
		long shopId = WebUtil.getInstance().getShopId(request);
		//初始化商家信息、产品分类
		initLeftPage(shopId, new SearchInDTO(), model, request);
		//初始化店铺公告
		Pager<MallNoticeDTO> noticePager = new Pager<MallNoticeDTO>();
		noticePager.setRows(5);
		initNoticeList(noticePager, shopId, model);

		ShopTemplatesCombinDTO shopRenovationMap = getShopRenovationByTempId(templeId);

		model.addAttribute("curMenu", "index");//页面标识：用于控制头部样式
		model.addAttribute("shopRenovationMap", shopRenovationMap.getShopRenovationmap());
		model.addAttribute("shopTemplates", shopRenovationMap.getShopTemplatesDTO());
		model.addAttribute("shopId", shopId);

		return "/sellcenter/shop/shopIndex/"+templateName;
	}

	/**
	 * 根据店铺模版id查询店铺的一个模版信息
	 * @param templeId 模版id
	 * @return
	 */
	private ShopTemplatesCombinDTO getShopRenovationByTempId(Long templeId) {
		ShopTemplatesCombinDTO shopTemplates = new ShopTemplatesCombinDTO();
		Object obj = redisDB.getObject(REDIS_DECORATE_PREFIX + templeId);
		if(obj==null){
			ShopRenovationDTO shopRenovationDTO = new ShopRenovationDTO();
			shopRenovationDTO.setTemplatesId(templeId);
			ExecuteResult<ShopTemplatesCombinDTO> result = shopRenovationExportService.queryShopRenovation(shopRenovationDTO);
			if(result != null){
				shopTemplates = result.getResult();
			}
		}else{
			shopTemplates = (ShopTemplatesCombinDTO)obj;
		}
		return shopTemplates;
	}

	/**
	 * 暂存店铺装修信息：放入redis
	 * @param shopRenovationListDTO
	 * @return
	 */
	@RequestMapping("/temporarySave")
	@ResponseBody
	public Map<String, Object> temporarySave(Long templatesId, ShopRenovationListDTO shopRenovationListDTO){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "failure");
		if(shopRenovationListDTO==null || shopRenovationListDTO.getShopRenovationList()==null ||shopRenovationListDTO.getShopRenovationList().size()==0 || templatesId==null){
			return result;
		}
		ShopTemplatesCombinDTO shopTemplatesCombinDTO = new ShopTemplatesCombinDTO();
		Object obj  = redisDB.getObject(REDIS_DECORATE_PREFIX + templatesId);
		if(obj != null){
			shopTemplatesCombinDTO = (ShopTemplatesCombinDTO)obj;
		}else{//第一次放入redis时，将模版id设置进去，避免下次从redis里面获取templateId为空的情况
			ShopTemplatesDTO shopTemplatesDTO = new ShopTemplatesDTO();
			shopTemplatesDTO.setId(templatesId);
			shopTemplatesCombinDTO.setShopTemplatesDTO(shopTemplatesDTO);
		}
		Map<String, ShopRenovationDTO> shopRenovationMap = shopTemplatesCombinDTO.getShopRenovationmap();
		if(shopRenovationMap == null){
			shopRenovationMap = new HashMap<String, ShopRenovationDTO>();
		}
		List<ShopRenovationDTO> listRen = shopRenovationListDTO.getShopRenovationList();
		for(int i=0,lsize=listRen.size(); i<lsize; i++){
			ShopRenovationDTO shopRenovationDTO = listRen.get(i);
			String shopRenovationKey = shopRenovationDTO.getModultType()+shopRenovationDTO.getPosition();
			shopRenovationMap.put(shopRenovationKey, shopRenovationDTO);
		}

		shopTemplatesCombinDTO.setShopRenovationmap(shopRenovationMap);
		String redisKey = REDIS_DECORATE_PREFIX + templatesId;
		redisDB.addObject(redisKey, shopTemplatesCombinDTO);

		result.put("result", "success");

		return result;
	}

	/**
	 * 清空某个位置店铺装修信息：放入redis
	 * @param shopRenovationListDTO
	 * @return
	 */
	@RequestMapping("/temporaryReset")
	@ResponseBody
	public Map<String, Object> temporaryReset(Long templatesId, ShopRenovationListDTO shopRenovationListDTO){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "failure");
		String redisKey = REDIS_DECORATE_PREFIX + templatesId;
		ShopTemplatesCombinDTO shopTemplatesCombinDTO = new ShopTemplatesCombinDTO();
		Object obj =redisDB.getObject(redisKey);	//根据位置信息 获取redis
		if(obj != null){
			shopTemplatesCombinDTO = (ShopTemplatesCombinDTO)obj;
		}else{//第一次放入redis时，将模版id设置进去，避免下次从redis里面获取templateId为空的情况
			ShopTemplatesDTO shopTemplatesDTO = new ShopTemplatesDTO();
			shopTemplatesDTO.setId(templatesId);
			shopTemplatesCombinDTO.setShopTemplatesDTO(shopTemplatesDTO);
		}
		Map<String, ShopRenovationDTO> shopRenovationMap = shopTemplatesCombinDTO.getShopRenovationmap();
		if(shopRenovationMap == null){
			shopRenovationMap = new HashMap<String, ShopRenovationDTO>();
		}

		List<ShopRenovationDTO> listRen = shopRenovationListDTO.getShopRenovationList();
		for(int i=0,lsize=listRen.size(); i<lsize; i++){
			ShopRenovationDTO shopRenovationDTO = listRen.get(i);
			//拼接位置信息 如5f
			String shopRenovationKey = shopRenovationDTO.getModultType()+shopRenovationDTO.getPosition();
			shopRenovationMap.put(shopRenovationKey, shopRenovationDTO);
		}

		shopTemplatesCombinDTO.setShopRenovationmap(shopRenovationMap);
		//把清空的位置的信息 放到redis
		redisDB.addObject(redisKey, shopTemplatesCombinDTO);

		result.put("result", "success");

		return result;
	}



	/**
	 * 保存店铺装修信息
	 * @param shopRenovationDTO
	 */
	private void saveDecorate(ShopRenovationDTO shopRenovationDTO) {
		Long id = shopRenovationDTO.getId();
		if(null == id){
			shopRenovationExportService.addShopRenovation(shopRenovationDTO);
		}else{
			ShopRenovationDTO entity = new ShopRenovationDTO();
			entity.setId(id);
			ExecuteResult<ShopTemplatesCombinDTO> shopTemplates = shopRenovationExportService.queryShopRenovation(entity);
			Map<String, ShopRenovationDTO> retMap = shopTemplates.getResult().getShopRenovationmap();
			if(null!=retMap){
				ShopRenovationDTO dto = retMap.get(shopRenovationDTO.getModultType()+shopRenovationDTO.getPosition());
				if(null != dto){
					dto.setPictureUrl(shopRenovationDTO.getPictureUrl());
					dto.setChainUrl(shopRenovationDTO.getChainUrl());
					dto.setPrice(shopRenovationDTO.getPrice());
					dto.setModified(new Date());
					dto.setSkuId(shopRenovationDTO.getSkuId());
					dto.setModuleName(shopRenovationDTO.getModuleName());
					dto.setHasPrice(shopRenovationDTO.getHasPrice());
					dto.setTemplatesId(shopRenovationDTO.getTemplatesId());
					shopRenovationExportService.modifyShopRenovation(dto);

				}
			}
		}
	}

	/**
	 * 保存店铺模版样式颜色
	 * @param templatesId 店铺模版id
	 * @param color 模版颜色
	 * @return
	 */
	@RequestMapping("/saveShopDecorate")
	@ResponseBody
	public Map<String, Object> saveAll(Long templatesId, String color){
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("result", "success");
		//1、保存颜色信息
		shopTemplatesExportService.modifyShopTemplatesColor(templatesId, color);

		//2、保存店铺装修信息
		String redisKey = REDIS_DECORATE_PREFIX + templatesId;
		Object obj = redisDB.getObject(redisKey);
		if(null == obj){
			return result;
		}
		ShopTemplatesCombinDTO shopTemplatesCombinDTO = (ShopTemplatesCombinDTO)obj;
		Map<String, ShopRenovationDTO> shopRenovationMap = shopTemplatesCombinDTO.getShopRenovationmap();
		if(shopRenovationMap!=null && shopRenovationMap.size()>0){
			for(String key : shopRenovationMap.keySet()){
				ShopRenovationDTO shopRen = shopRenovationMap.get(key);
				shopRen.setTemplatesId(templatesId);
				saveDecorate(shopRen);
			}
		}

		//3、移除redis中的缓存数据
		redisDB.del(REDIS_DECORATE_PREFIX + templatesId);

		return result;
	}

	/**
	 * 查询店铺商品
	 * @param page
	 * @param shopId 店铺id
	 * @param itemInDTO 搜索条件
	 * @param model
	 * @return
	 */
	@RequestMapping("/queryShopProduct")
	public String queryShopProduct(Integer page, Long shopId, ItemQueryInDTO itemInDTO, Model model){
		itemInDTO.setShopIds(new Long[]{shopId});
		itemInDTO.setItemStatus(4);
		Pager<ItemQueryOutDTO> pager = new Pager<ItemQueryOutDTO>();
		if(page == null){
			page = 1;
		}
		pager.setPage(page);
		DataGrid<ItemQueryOutDTO> dg = itemExportService.queryItemList(itemInDTO, pager);
		pager.setTotalCount(dg.getTotal().intValue());
		pager.setRecords(dg.getRows());
		model.addAttribute("pager", pager);

		return "/sellcenter/shop/common/productList";
	}

	/**
	 * 查询我的收藏列表
	 * @param shopId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/loadFavourite")
	@ResponseBody
	public String loadFavourite(Long shopId, HttpServletRequest request, Model model){
		Pager<FavouriteCountDTO> pager = new Pager<FavouriteCountDTO>();
		pager.setRows(6);
		ExecuteResult<DataGrid<FavouriteCountDTO>> result = this.itemFavouriteExportService.queryFavouriteCount(shopId, pager);
		Map<String, Object> retMap = new HashMap<String, Object>();

		retMap.put("result", result.getResult().getRows());
		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		retMap.put("gix",imgUrlPre);
		return JSON.toJSONString(retMap);
	}

	/**
	 * 查询销量最好的前5个商品
	 * @param shopId
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/loadHotProduct")
	@ResponseBody
	public String loadHotSales(Integer page,Long shopId, HttpServletRequest request, Model model){
		SearchInDTO itemInDTO = new SearchInDTO();
		//获取当前店铺的所有商品的第一页的数据
		itemInDTO.setShopId(shopId);
		itemInDTO.setOrderSort(6);//按销量降序
		String uid = CookieHelper.getCookieVal(request, Constants.USER_ID);
		if(uid != null && !"".equals(uid)){
			itemInDTO.setBuyerId(Long.valueOf(uid));
		}
		int pageSize = 8;
		Pager<ItemSkuDTO> pager = buildPager(page, pageSize, itemInDTO);
		Map<String, Object> retMap = new HashMap<String, Object>();
		//去掉无用的广告
		List<ItemSkuDTO> listIsd=pager.getRecords();
		for(ItemSkuDTO isd : listIsd){
			isd.setDescribeUrl(null);
		}
		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		retMap.put("gix",imgUrlPre);
		retMap.put("page",page);
		retMap.put("result", pager.getRecords());
		return JSON.toJSONString(retMap);
	}

	/**
	 * 跳转到公告详情页面
	 * @param request
	 * @param model
	 * @param shopId
	 * @param id
	 * @return
	 */
	@RequestMapping("/toNoticeDetail/{shopId}/{id}")
	public String noticeView(HttpServletRequest request, Model model, @PathVariable("shopId")Long shopId, @PathVariable("id")Long id){
		//初始化商家信息、产品分类
		initLeftPage(shopId, new SearchInDTO(), model, request);

		MallNoticeDTO notice = this.noticeExportService.getNoticeInfo(id);
		model.addAttribute("notice", notice);

		return "/sellcenter/shop/shopNoticeView/" + getTemplateName(shopId, model);
	}

	/**
	 * 跳转到公告列表页面
	 * @param page
	 * @param request
	 * @param model
	 * @param shopId
	 * @return
	 */
	@RequestMapping("/toNoticeList/{shopId}")
	public String noticeList(Integer page, HttpServletRequest request, Model model, @PathVariable("shopId")Long shopId){
		//初始化商家信息、产品分类
		initLeftPage(shopId, new SearchInDTO(), model, request);

		//初始化店铺公告
		Pager<MallNoticeDTO> noticePager = new Pager<MallNoticeDTO>();
		if(null == page){
			page = 1;
		}
		noticePager.setPage(page);
		initNoticeList(noticePager, shopId, model);

		return "/sellcenter/shop/shopNoticeList/" + getTemplateName(shopId, model);
	}
}
