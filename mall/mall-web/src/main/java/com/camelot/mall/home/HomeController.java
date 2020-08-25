package com.camelot.mall.home;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.AddressBaseDTO;
import com.camelot.basecenter.dto.IptableDTO;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.basecenter.service.IptableService;
import com.camelot.goodscenter.dto.ItemShopCartDTO;
import com.camelot.goodscenter.service.ItemPriceExportService;
import com.camelot.mall.Constants;
import com.camelot.mall.common.CommonService;
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

import groovy.lang.MetaClassImpl.Index;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月3日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
public class HomeController {
	
	private Logger LOG = Logger.getLogger(this.getClass());
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
	private IptableService iptableService;
	@Resource 
	private AddressBaseService addressBaseService;
	@Resource
	private ItemPriceExportService itemPriceExportService;
	@Resource
	private MallThemeService mallThemeService;
	
	@RequestMapping("/")
	public ModelAndView index(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		String addressCode = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		if(addressCode==null||addressCode.equals("")){
			addressCode = this.getAddressCode(addressCode,request,response);
		}else{
			MallThemeDTO theme = new MallThemeDTO();
			theme.setCityCode(Long.valueOf(addressCode));
			DataGrid<MallThemeDTO> mtdDg = mallThemeService.queryMallThemeList(theme, "1", new Pager(1, 1));
			if(mtdDg==null||mtdDg.getRows().size()==0){
				return this.index(null, request, response, model);
			}
		}
		ExecuteResult<List<AddressBaseDTO>> abds = addressBaseService.queryNameByCode(addressCode);
		if("0".equals(addressCode)||abds==null||abds.getResult().size()==0){
			return this.index(null, request, response, model);
		}else{
			return new ModelAndView("redirect:/"+abds.getResult().get(0).getNameLetter());
		}
	}
	
	
	@RequestMapping("/{addressLetterName}")
	public ModelAndView index(@PathVariable(value = "addressLetterName") String addressLetterName,HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		Integer themeId = null;
		String addressCode = null;
		if(addressLetterName!=null&&!"".equals(addressLetterName)){
			List<AddressBaseDTO> abds = addressBaseService.queryByNameLetter(addressLetterName);
			addressCode = (abds!=null&&abds.size()>0)?abds.get(0).getCode():null;
			if(addressCode!=null){
				MallThemeDTO theme = new MallThemeDTO();
				theme.setCityCode(Long.valueOf(addressCode));
				DataGrid<MallThemeDTO> mtdDg = mallThemeService.queryMallThemeList(theme, "1", new Pager(1, 1));
				if(mtdDg==null||mtdDg.getRows().size()==0){
					return new ModelAndView("redirect:/");
				}else{
					themeId = mtdDg.getRows().get(0).getId();
					CookieHelper.setCookie(response, Constants.REGION_CODE, addressCode);
					CookieHelper.setCookie(response, Constants.REGION,abds.get(0).getName());
				}
			}else{
				return new ModelAndView("redirect:/");
			}
		}else{
			addressCode = null;
		}
	
//		String addressCode = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
//		if(addressCode==null||addressCode.equals("")){
//			addressCode = this.getAddressCode(addressCode,request,response);
//		}
//		if(addressCode!=null&&!"".equals(addressCode)){
//			MallThemeDTO theme = new MallThemeDTO();
//			theme.setCityCode(Long.valueOf(addressCode));
//			page.setRows(1);
//			DataGrid<MallThemeDTO> dgMallTheme = mallThemeService.queryMallThemeList(theme, "1", page);
//			if(dgMallTheme!=null&&dgMallTheme.getRows()!=null&&dgMallTheme.getRows().size()>0){
//				themeId = dgMallTheme.getRows().get(0).getId();
//			}
//		}
		
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
		
		if(themeId == null) 
			themeId = 0 ;
		MallBannerDTO mallBannerDTO = new MallBannerDTO();
		//状态为上架
		mallBannerDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getStringValue());
		//设置轮播图类型
		mallBannerDTO.setBannerType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());//首页
		//子站id
		if(themeId!=null){
			mallBannerDTO.setThemeId(themeId);
		}
		page.setRows(6);
		//轮播图取数逻辑  已发布，并且当前时间在开始时间与结束时间之间
		DataGrid<MallBannerDTO> banners = mallBannerService.queryMallBannerList(mallBannerDTO,"1", page);
		if(themeId!=null&&banners!=null&&banners.getRows()!=null&&banners.getRows().size()==0){
			mallBannerDTO.setThemeId(0);
			banners = mallBannerService.queryMallBannerList(mallBannerDTO,"1", page);
		}
		LOG.debug("BUNNER:" + JSON.toJSONString(banners.getRows()));


		//查询本周推荐广告
//		page.setRows(6);
//		MallAdQueryDTO weekAdDTO = new MallAdQueryDTO();
//		weekAdDTO.setStatus(1);
//		weekAdDTO.setAdType(6);
//		weekAdDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());//首页
//		DataGrid<MallAdDTO> weekAdes = this.mallAdvertisService.queryMallAdList(page, weekAdDTO);

		
		//查询轮播下广告位
		page.setRows(6);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(1);
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());//首页
		if(themeId!=null){
			mallAdQueryDTO.setThemeId(themeId);
		}
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		if(themeId!=null&&advertises!=null&&advertises.getRows()!=null&&advertises.getRows().size()==0){
			mallAdQueryDTO.setThemeId(0);
			advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		}
		model.addAttribute("banners", banners.getRows());
		model.addAttribute("advertises", advertises.getRows());
		//model.addAttribute("weekAdes", weekAdes.getRows());

		MallRecDTO mallRecDTO = new MallRecDTO();
		mallRecDTO.setStatusDTO(1);
		mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());//首页楼层
		page.setPage(1);
		mallRecDTO.setFloorTypeDTO(0);
		page.setRows(100);
		DataGrid<MallRecDTO> dg = null;
		if(themeId!=null){
			mallRecDTO.setThemeId(themeId);
		}
		dg = mallRecService.queryMallRecList(mallRecDTO, page);
		if(dg==null||dg.getRows().size()==0){
			mallRecDTO.setThemeId(0);
			dg = mallRecService.queryMallRecList(mallRecDTO, page);
		}
		mallRecDTO.setFloorTypeDTO(1);
		DataGrid<MallRecDTO> dgTop = null;
		if(themeId!=null){
			mallRecDTO.setThemeId(themeId);
		}
		page.setRows(1);
		dgTop = mallRecService.queryMallRecList(mallRecDTO, page);
		if(dgTop==null||dgTop.getRows().size()==0){
			mallRecDTO.setThemeId(0);
			dgTop = mallRecService.queryMallRecList(mallRecDTO, page);
		}
		model.addAttribute("floors", dg.getRows());
		model.addAttribute("floorTop", dgTop.getRows());
		model.addAttribute("homeStatus", "1");
		return new ModelAndView("/home/index");
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
				if(!shops.getResult().getRows().isEmpty()){
					//用户logoURL
					model.addAttribute("userLogoUrl", shops.getResult().getRows().get(0).getLogoUrl());
				}

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
	 * Created on 2015年3月11日
	 * @param model
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/noticeFragement")
	public String noticeFragement(Model model){
		
		// 首页公告查询已发布
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(5);
		
		MallNoticeDTO dto = new MallNoticeDTO();
		dto.setNoticeStatus(1);
		dto.setPlatformId(Long.valueOf(0));
		LOG.debug("GET NOTICE BY PARAMS:"+JSON.toJSONString(dto));
		dto.setThemeType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());//首页公告
		DataGrid<MallNoticeDTO> notices = this.noticeSevice.queryNoticeList(page,dto);
		model.addAttribute("notices", notices.getRows());
		return "/home/notice_mini";
	}
	
	
	@RequestMapping("/floor")
	public String floor(String addressCode,String num,String fid,Model model,HttpServletRequest request){
		String addresssCode = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		JSONObject floor = this.homeService.getFloor(fid,BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue(),addresssCode);
		String userToken = LoginToken.getLoginToken(request);
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				model.addAttribute("logging_status", "true");
			}else{
				model.addAttribute("logging_status", "false");
			}
		}
		if(null != request.getParameter("type")){
			model.addAttribute("type", "_1");
		}else{
			model.addAttribute("type", "");
		}
		model.addAttribute("floorNum", num);
		model.addAttribute("floor", floor);
		return "/home/floor";
	}
	
	@RequestMapping("/categoryes")
	public String categoryes(Model model){
		JSONArray categoryes = this.commonService.findCategory();
		model.addAttribute("categoryes", categoryes);
  		return "/home/category_all";
	}	
	
	@RequestMapping("/topBunner")
	@ResponseBody
	public MallAdDTO queryTopBunner(Integer addressCode){
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(1);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(3);
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallAdDTO> advertises = null;
		
		MallThemeDTO theme = new MallThemeDTO();
		theme.setCityCode(Long.valueOf(addressCode));
		page.setRows(1);
		DataGrid<MallThemeDTO> dgMallTheme = mallThemeService.queryMallThemeList(theme, "1", page);
		Integer themeId = null;
		if(dgMallTheme!=null&&dgMallTheme.getRows()!=null&&dgMallTheme.getRows().size()>0){
			themeId = dgMallTheme.getRows().get(0).getId();
		}
		if(themeId == null) 
			themeId = 0 ;
		if(themeId!=null){
			mallAdQueryDTO.setThemeId(themeId);
			advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		}
		if(advertises==null||advertises.getRows().size()==0){
			mallAdQueryDTO.setThemeId(0);
			advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		}
		if( advertises != null && advertises.getRows().size() > 0 ){
			return advertises.getRows().get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * <p>Discription: 获取广告</p>
	 * Created on 2016年2月22日
	 * @return
	 * @author:[王宏伟]
	 */
	@ResponseBody
	@RequestMapping("getSalesProcess")
	public String getSalesProcess(Integer adType,Integer rows,String addressCode
			,HttpServletRequest request,HttpServletResponse response){
		Pager page = new Pager();
		page.setPage(1);
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(adType);
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallAdDTO> advertises = null;
		
		MallThemeDTO theme = new MallThemeDTO();
		page.setRows(1);
		Integer themeId = null;
		if(addressCode!=null&&!"".equals(addressCode)){
			theme.setCityCode(Long.valueOf(addressCode));
			DataGrid<MallThemeDTO> dgMallTheme = mallThemeService.queryMallThemeList(theme, "1", page);
			if(dgMallTheme!=null&&dgMallTheme.getRows()!=null&&dgMallTheme.getRows().size()>0){
				themeId = dgMallTheme.getRows().get(0).getId();
			}
		}
		page.setRows(rows);
		if(themeId!=null){
			mallAdQueryDTO.setThemeId(themeId);
			advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		}
		if(advertises==null||advertises.getRows().size()==0){
			mallAdQueryDTO.setThemeId(0);
			advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		}
		if( advertises != null && advertises.getRows().size() > 0 ){
			return JSONObject.toJSONString(advertises.getRows());
		}else{
			return null;
		}
	}
	
	/**
	 * 
	 * <p>Discription: 根据当前地区与skuid获取套装商品价格</p>
	 * Created on 2016年2月27日
	 * @return 价格
	 * @author:[王宏伟]
	 */
	@ResponseBody
	@RequestMapping("getPrice")
	public String getPrice(String addressCode,Long skuId){
		ItemShopCartDTO skuDTO = new ItemShopCartDTO();
		skuDTO.setAreaCode(addressCode);
		skuDTO.setSkuId(skuId);
		BigDecimal bd = itemPriceExportService.getSkuShowPrice(skuDTO);
		if(bd==null){
			return "-1";
		}
		return bd.toString();
	}
	
	/**
	 * 
	 * <p>Discription: 获取IP</p>
	 * Created on 2016年2月24日
	 * @return IP地址
	 * @author:[王宏伟]
	 */
    private String getRemoteHost(final HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip.equals("0:0:0:0:0:0:0:1")){
        	return "127.0.0.1";
        }
        return ip;
    }
    
    /**
	 * 
	 * <p>Discription: 根据IP获取用户所在地址</p>
	 * Created on 2016年2月27日
	 * @author:[王宏伟]
	 */
    private String getAddressCode(String addressCode,HttpServletRequest request,HttpServletResponse response){
		String ip = getRemoteHost(request);
		String[] ips = ip.split("\\.");
		Long number = 0L;
		for(int i = 0;i<ips.length;i++){
			Long num = Long.valueOf(ips[i]);
			for(int j = i+1;j<ips.length;j++){
				num = num*256;
			}
			number += num;
		}
		IptableDTO iptableDTO = new IptableDTO();
		iptableDTO.setNum(number);
		List<IptableDTO> list = iptableService.query(iptableDTO);
		List<AddressBaseDTO> ads = new ArrayList<AddressBaseDTO>();
		if(list!=null&&list.size()>0){
			String name = list.get(0).getCountry();
			if(name!=null&&name.length()>1){
				int idx = name.indexOf('市');
				if(idx != -1){
					name = name.substring(0,idx);
				}
				idx = name.indexOf('省');
				if(idx != -1){
					name = name.substring(idx+1, name.length());
				}
				for(int i = 2;i<=name.length();i++){
					String selectName = name.substring(name.length()-i, name.length());
					//进入select查询如果查询出来的集合大于1则继续查询如果查询出来的储量为0则放弃查询
					ads = addressBaseService.queryAddressListByName("%"+selectName+"%");
					if(ads.size()<2){
						break;
					}
				}
			}
		}
		if(ads.size()==1){
			String[] codes = mallThemeService.queryGroupCityCode();
			for(String code:codes){
				if(code.equals(ads.get(0).getCode())){
					addressCode = code;
					CookieHelper.setCookie(response, Constants.REGION_CODE, code);
					CookieHelper.setCookie(response, Constants.REGION, ads.get(0).getName());
					//CookieHelper.setCookie(response, Constants.REGION, "shanghai");
					break;
				}
			}
		}else{
			CookieHelper.setCookie(response, Constants.REGION_CODE, "0");
			CookieHelper.setCookie(response, Constants.REGION,"全国");
		}
		return addressCode;
    }
    
    public boolean isNumeric(String str){ 
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
}
