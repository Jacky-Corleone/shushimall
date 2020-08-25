package com.camelot.mall.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camelot.CookieHelper;
import com.camelot.mall.Constants;
import com.camelot.mall.service.HomeService;
import com.camelot.mall.service.impl.CommonServiceImpl;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.enums.MallTypeEnum;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.usercenter.dto.RegisterDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.util.LoginToken;

@Controller
public class HomePageController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private CommonServiceImpl commonServiceImpl;
	@Resource
	private MallBannerExportService mallBannerService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private MallRecExportService mallRecService;
	@Resource
	private HomeService homeService;
	@Resource
	private UserExportService userExportService;
	/**
	 * 跳转到主页
	 * @author 马国平 创建时间：2015-5-20 下午1:08:15
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/")
	public String goHome(
			Model model,
			HttpServletRequest request,
			HttpServletResponse response,HttpSession session,
			@CookieValue(value = Constants.USER_TOKEN, required = false, defaultValue = "") String userToken,
			@CookieValue(value = "uid", required = false, defaultValue = "") String uid,
			@RequestParam(value = "remember", required = false, defaultValue = "") String remember,
			@RequestParam(value = "login1st", required = false, defaultValue = "") String login1st,
			@RequestParam(value = "showMenu", required = false, defaultValue = "") String showMenu) {
		try {
			String token = LoginToken.getLoginToken(request);
			String logname = CookieHelper.getCookieVal(request, Constants.USER_TOKEN);
	        if(StringUtils.isBlank(logname)){
	            //model.addAttribute("userId", null);
				//没有检测到登录信息，清理掉遗留的信息
				logger.debug("没有检测到token信息，直接清理掉登录相关的cookie");
				//cleanLogin(request, response, session);
	        }else{
	            RegisterDTO registerDTO = userExportService.getUserByRedis(token);
	            if(registerDTO == null){
					logger.debug("从token没有得到用户信息，清理掉登录相关的cookie");
	                //model.addAttribute("userId", null);
					//cleanLogin(request,response,session);
	            }else{
					logger.debug("从token得到了用户信息，userId:"+registerDTO.getUid());
	                model.addAttribute("userId", registerDTO.getUid());
	                //model.addAttribute("uid", uid);
	    			model.addAttribute("userInfo", registerDTO);
	            }
	        }
			
			JSONArray ja = commonServiceImpl.findCategory();
			model.addAttribute("itemList", ja);
			
			//////控制页面是否默认显示类目菜单
			if(StringUtils.isEmpty(showMenu)){
				model.addAttribute("showMenu", 0);
			}else{
				model.addAttribute("showMenu", 1);
			}
			
			
			//是否显示登录页面
			if (StringUtils.isNotEmpty(login1st)) {
				model.addAttribute("login1st", "1");
			}
			Pager<Object> page = new Pager<Object>();
			page.setPage(1);
			page.setRows(6);
			//轮播图取数逻辑  已发布，并且当前时间在开始时间与结束时间之间
			DataGrid<MallBannerDTO> banners = this.mallBannerService.queryMallBannerList("1", MallTypeEnum.MALL.getId(), page);
			logger.debug("BUNNER:" + JSON.toJSONString(banners.getRows()));

			model.addAttribute("gix", SysProperties.getProperty("ftp_server_dir"));

			//查询轮播下广告位
			page.setRows(6);
			MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
			mallAdQueryDTO.setStatus(1);
			mallAdQueryDTO.setAdType(1);
			DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);

			model.addAttribute("banners", banners.getRows());
			model.addAttribute("advertises", advertises.getRows());

			MallRecDTO mallRecDTO = new MallRecDTO();
			mallRecDTO.setStatusDTO(1);
			
			//小管家只要1,网站首页
			mallRecDTO.setRecTypeDTO(1);
			page.setPage(1);
			page.setRows(100);
			DataGrid<MallRecDTO> dg = mallRecService.queryMallRecList(mallRecDTO, page);
			model.addAttribute("floors", dg.getRows());
			response.addHeader("Cache-control","no-cache");
			return "/home/homepage";
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("首页出异常了");
		}
	}

	/**
	 * 用于测试的方法，以后将会删除
	 * @author 马国平 创建时间：2015-5-29 上午10:30:30
	 * @return
	 */
	@RequestMapping("home/getHomeDatas")
	@ResponseBody
	public String getHomeDatas() {
		JSONArray ja = commonServiceImpl.findCategory();
		return ja.toJSONString();
	}

	@RequestMapping("home/floor")
	public String getEachFoorData(String floorcount, String fid, Model model,HttpServletRequest request) {
		JSONObject floor = this.homeService.getFloor(fid);
		String userToken = LoginToken.getLoginToken(request);
		if(userToken!=null){
			RegisterDTO registerDto = userExportService.getUserByRedis(userToken);
			if(registerDto!=null){
				model.addAttribute("logging_status", "true");
			}else{
				model.addAttribute("logging_status", "false");
			}
		}
		model.addAttribute("floorcount", floorcount);
		model.addAttribute("floor", floor);
		String imgUrlPre = SysProperties.getProperty("ftp_server_dir");
		model.addAttribute("imgUrlPre", imgUrlPre);
		return "/home/floor";
	}

	/**
	 * 微信设置url验证用的方法
	 * @author 马国平 创建时间：2015-6-2 下午2:09:44
	 * @return
	 */
	@RequestMapping("home/validate")
	public String reciveMsg(
			@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("echostr") String echostr,
			Model model) {

		return null;
	}

	@RequestMapping("home/loginStuffing")
	public String loginStuffing(){
		
		return "home/loginWindow";
	}

	//清理掉登录相关的cookie和session
	private void cleanLogin(HttpServletRequest request,
							HttpServletResponse response,
							HttpSession session){
		CookieHelper.delCookie(request, response, Constants.USER_TOKEN);
		CookieHelper.delCookie(request, response, Constants.USER_ID);
		CookieHelper.delCookie(request, response, Constants.USER_NAME);
		CookieHelper.delCookie(request, response, Constants.REQUEST_URL);
		CookieHelper.delCookie(request, response,"logname");
		session.setAttribute("uid",null);
	}
}
