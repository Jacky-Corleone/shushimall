package com.camelot.mall.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.camelot.CookieHelper;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.service.MallDocumentService;
import com.camelot.common.util.DateUtils;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.maketcenter.dto.QueryCentralPurchasingDTO;
import com.camelot.maketcenter.dto.emums.CentralPurchasingActivityStatusEnum;
import com.camelot.mall.Constants;
import com.camelot.mall.centralPurchase.CentralPurchaseService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.sellercenter.logo.dto.LogoDTO;
import com.camelot.sellercenter.logo.service.LogoExportService;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.sellercenter.mallword.dto.MallWordDTO;
import com.camelot.sellercenter.mallword.service.MallWordExportService;
import com.camelot.usercenter.dto.UserDTO;
import com.camelot.usercenter.dto.UserMallResourceDTO;
import com.camelot.usercenter.service.UserExportService;
import com.camelot.usercenter.service.UserStorePermissionExportService;
import com.camelot.util.WebUtil;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年3月16日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
public class CommonController {
	private Logger LOG = Logger.getLogger(this.getClass());
	@Resource
	private MallDocumentService mallDocumentService;
	@Resource
	private CommonService commonService;
	@Resource
	private UserStorePermissionExportService userStorePermissionExportService;
	@Resource
	private UserExportService userExportService;
	@Resource
	private LogoExportService logoService;
	@Resource
	private MallWordExportService mallWordService;
	@Resource
	private MallAdExportService mallAdvertisService;
	@Resource
	private MallThemeService mallThemeService;
	@Resource
	private ItemCategoryService itemCategoryService;
	@Resource
	private CentralPurchaseService centralPurchaseService;
	
	
	/**
	 * 
	 * <p>Discription:页尾查询</p>
	 * Created on 2015年3月11日
	 * @param model
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/footer")
	public String footer(Model model){
		MallDocumentDTO dto = new MallDocumentDTO();
		dto.setMallStatus(2);
		dto.setMallType(2);
		@SuppressWarnings("rawtypes")
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(100);
		pager.setSort("mallClassifyId");
		pager.setOrder("asc");
		DataGrid<MallDocumentDTO> dg = this.mallDocumentService.queryMallDocumentList(dto, pager);
		model.addAttribute("documents", dg.getRows());
		LOG.debug("Footer Documents: " + JSON.toJSONString(dg.getRows()));
		dto.setMallType(7);
		DataGrid<MallDocumentDTO> dgg = this.mallDocumentService.queryMallDocumentList(dto, pager);
		model.addAttribute("footDocs", dgg.getRows());
		return "/common/footer_doc";
	}
	
	/**
	 * 
	 * <p>Discription:[卖家中心左侧导航]</p>
	 * Created on 2015年3月13日
	 * @param model
	 * @return
	 * @author:[马桂雷]
	 */
	@RequestMapping("/leftSeller")
	public String leftSeller(Model model, HttpServletRequest request){
		buildMenuByModuleType(model, request, com.camelot.util.Constants.MODULE_TYPE_SELLER);
		return "/common/left_seller";
	}

	private void buildMenuByModuleType(Model model, HttpServletRequest request, Integer moduleType) {
		long uid = WebUtil.getInstance().getUserId(request);
		UserDTO user = userExportService.queryUserById(uid);
		ExecuteResult<List<UserMallResourceDTO>> reslist = new ExecuteResult<List<UserMallResourceDTO>>();
		if (user.getParentId() == null || 1L == user.getParentId()) {// 父账号：查询所有卖家菜单数据
			reslist = userStorePermissionExportService.queryParentResourceList(moduleType, moduleType);
		} else {// 子账号：查询对应的菜单数据
			reslist = userStorePermissionExportService.queryUserMallResourceById(uid, moduleType);
		}
		List<UserMallResourceDTO> results = reslist.getResult();
		results = getFilterMenu(results);
		model.addAttribute("reslist", results);
	}
	
	/**
	 * 
	 * <p>Discription:[买家中心左侧导航]</p>
	 * Created on 2015年3月14日
	 * @param model
	 * @return
	 * @author:[周乐]
	 */
	@RequestMapping("/leftBuyer")
	public String leftBuyer(Model model, HttpServletRequest request){
		buildMenuByModuleType(model, request, com.camelot.util.Constants.MODULE_TYPE_BUYER);
		return "/common/left_buyer";
	}
	
	@RequestMapping("/allCategory")
	public String allCategory(Model model,String type){
		JSONArray categoryes = this.commonService.findCategory();
		model.addAttribute("categoryes", categoryes);
		model.addAttribute("type",type);
		LOG.debug("平台所有类目："+categoryes.toJSONString());
		MallThemeDTO mallThemeDTO = new MallThemeDTO();//查询类目子站信息
		mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		DataGrid<MallThemeDTO> queryMallThemeList = mallThemeService.queryMallThemeList(mallThemeDTO, "1", null);
		List<MallThemeDTO> lists=queryMallThemeList.getRows();
		List<String> categoyNames=new ArrayList<String>();
		if(lists!=null&&lists.size()>0){
			for(MallThemeDTO dto:lists){
				ExecuteResult<ItemCategoryDTO> itemCategoryDTO = itemCategoryService.queryItemByCategoryById(dto.getcId());
				if(itemCategoryDTO.getResult()!=null){
					categoyNames.add(itemCategoryDTO.getResult().getCategoryCName());
				}
			}
		}
		model.addAttribute("categoyNames",categoyNames);
		model.addAttribute("mallThemeList",lists);
		return "/common/left_category";
	}

	/**
	 * 
	 * <p>Discription:商城LOGO查询</p>
	 * Created on 2015年4月28日
	 * @param model
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/logo")
	@ResponseBody
	public LogoDTO mallLogo(){
		ExecuteResult<LogoDTO> er = this.logoService.getMallLogo();
		return er.getResult();
	}
	
	@RequestMapping("/mallWord")
	@ResponseBody
	public List<MallWordDTO> mallWord(){
		MallWordDTO dto = new MallWordDTO();
		@SuppressWarnings("rawtypes")
		Pager page = new Pager();
		page.setRows(15);
		page.setPage(1);
		DataGrid<MallWordDTO> dg = this.mallWordService.datagrid(dto, page);
		return dg.getRows();
	}
	
	@RequestMapping("/guessLove")
	public String guessLove(Model model,HttpServletRequest request,HttpServletResponse response){
		Pager<MallAdDTO> page = new Pager<MallAdDTO>();
		page.setPage(1);

		//获取子站id
		Integer themeId = null;
		String addressCode = CookieHelper.getCookieVal(request, Constants.REGION_CODE);
		if(addressCode!=null&&!"".equals(addressCode)){
			MallThemeDTO theme = new MallThemeDTO();
			theme.setCityCode(Long.valueOf(addressCode));
			page.setRows(1);
			DataGrid<MallThemeDTO> dgMallTheme = mallThemeService.queryMallThemeList(theme, "1", page);
			if(dgMallTheme!=null&&dgMallTheme.getRows()!=null&&dgMallTheme.getRows().size()>0){
				themeId = dgMallTheme.getRows().get(0).getId();
			}
		}
		
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(5);
		page.setRows(6);
		if(themeId!=null){
			mallAdQueryDTO.setThemeId(themeId);
		}
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		if(themeId!=null&&advertises!=null&&advertises.getRows()!=null&&advertises.getRows().size()==0){
			mallAdQueryDTO.setThemeId(0);
			advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		}
		page.setRecords(advertises.getRows());
		page.setTotalCount(advertises.getTotal().intValue());
		model.addAttribute("guessLovePage", page);
		return "/home/guess_love";
	}
	
	@RequestMapping("/recommend")
	public String recommend(Model model){
		Pager page = new Pager();
		page.setRows(5);
		page.setPage(1);
		
		MallAdQueryDTO mallAdQueryDTO = new MallAdQueryDTO();
		mallAdQueryDTO.setStatus(1);
		mallAdQueryDTO.setAdType(5);
		mallAdQueryDTO.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallAdDTO> advertises = this.mallAdvertisService.queryMallAdList(page, mallAdQueryDTO);
		model.addAttribute("jcProducts", advertises.getRows());

		MallAdQueryDTO adQuery = new MallAdQueryDTO();
		adQuery.setStatus(1);
		adQuery.setAdType(1);
		adQuery.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallAdDTO> dg = this.mallAdvertisService.queryMallAdList(page, adQuery);
		model.addAttribute("hotProducts", dg.getRows());
		// 获取集采活动
		QueryCentralPurchasingDTO queryCentralPurchasingDTO = new QueryCentralPurchasingDTO();
		queryCentralPurchasingDTO.setActivityStatus(CentralPurchasingActivityStatusEnum.PUBLISHED.getCode());
		// 科印平台
		queryCentralPurchasingDTO.setPlatformId(0);
		queryCentralPurchasingDTO.setActivityType(1);
		page = centralPurchaseService.getCentralPurchase(queryCentralPurchasingDTO,page);
		model.addAttribute("centralPurchase", page);
		return "/common/recommend";
	}
	
	
	
	/**
	 * 
	 * <p>Description: [获取科印的菜单，去掉filter_menu为1的菜单]</p>
	 * Created on 2015年11月18日
	 * @param results
	 * @return
	 * @author:[宋文斌]
	 */
	private List<UserMallResourceDTO> getFilterMenu(List<UserMallResourceDTO> results) {
		if (null == results || results.size() == 0) {
			return results;
		}
		for (int i = 0; i < results.size(); i++) {
			UserMallResourceDTO dto = results.get(i);
			if (dto.getFilterMenu() != null && dto.getFilterMenu().intValue() == 1) {
				results.remove(dto);
				i--;
			}
			if (null == dto.getUserMallResourceList() || dto.getUserMallResourceList().size() == 0) {
				return results;
			}
			for (int j = 0; j < dto.getUserMallResourceList().size(); j++) {
				UserMallResourceDTO res = dto.getUserMallResourceList().get(j);
				if (res.getFilterMenu() != null && res.getFilterMenu().intValue() == 1) {
					dto.getUserMallResourceList().remove(res);
					j--;
				}
			}
		}
		return results;
	}
	
	/**
	 * 
	 * <p>Description: [获取服务器当前时间]</p>
	 * Created on 2015年12月1日
	 * @param request
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/getServerTime")
	@ResponseBody
	public ExecuteResult<String> getServerTime(HttpServletRequest request) {
		ExecuteResult<String> result = new ExecuteResult<String>();
		result.setResult(DateUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss"));
		return result;
	}
}
