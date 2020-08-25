
package com.camelot.ecm.sellercenter.mallbanner;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.dto.MallBannerInDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * <p>Description: [轮播图设置]</p>
 * Created on 2015年2月27日
 * @author  <a href="mailto: chenghailong@camelotchina.com">程海龙</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/sellercenter/mallbanner")
public class MallBannerController extends BaseController {

    @Resource
  	private MallThemeService mallThemeService;
	@Autowired
	private MallBannerExportService mallBannerExportService;

	@RequestMapping(value = {"list", ""})
	public String list(MallBannerDTO mallBanner,HttpServletRequest request, HttpServletResponse response, Model model,Integer addressType) {

		Page<MallBannerDTO> page = new Page<MallBannerDTO>(request, response);

		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
//		pager.setSort("");
//		pager.setOrder("desc");
		//设置默认首页主题
		mallBanner.setBannerType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		if (addressType==null || "".equals(addressType)) {
			mallBanner.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}else {
			if(mallBanner.getThemeId()==null){
				mallBanner.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
			}
		}
		DataGrid<MallBannerDTO> pageGrid = mallBannerExportService.queryMallBannerList(mallBanner,null, pager);

		page.setCount(pageGrid.getTotal());
		page.setList(pageGrid.getRows());
	
		if(addressType!=null){
			//查询子站
			MallThemeDTO themeDTO = new MallThemeDTO();
			themeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
			Pager p= new Pager();
			p.setPage(1);
			p.setRows(500);
			DataGrid<MallThemeDTO> themeList = mallThemeService.queryMallThemeList(themeDTO, "1",p);
			model.addAttribute("themeList", themeList.getRows());
		}
        model.addAttribute("page", page);
		model.addAttribute("addressType",addressType);
		model.addAttribute("mallBanner",mallBanner);
		
		return "sellercenter/mallbanner/mallBannerList";
	}

	@RequestMapping("form")
	public String form(MallBannerDTO mallBanner, Model model, Integer addressType) {
		if(mallBanner!=null && mallBanner.getId()!=null){
			mallBanner = mallBannerExportService.getMallBannerById(mallBanner.getId());
            String url = mallBanner.getBannerUrl();
            url = url.replaceAll(SysProperties.getProperty("ngIp"), "");
            mallBanner.setBannerUrl(url);

			MallThemeDTO themeDTO = mallThemeService.getMallThemeById(mallBanner.getThemeId());
			model.addAttribute("themeDTO",themeDTO);
		}
		//发布开始时间/发布结束时间
		String startTime = "";
		if(StringUtils.isNotEmpty(mallBanner.getStartTime())){
			startTime = mallBanner.getStartTime();
		}
		mallBanner.setStartTime(startTime);
		String endTime = "";
		if(StringUtils.isNotEmpty(mallBanner.getEndTime())){
			endTime = mallBanner.getEndTime();
		}
		mallBanner.setEndTime(endTime);
		
		model.addAttribute("addressType",addressType);
		model.addAttribute("mallBanner", mallBanner);
		return "sellercenter/mallbanner/mallBannerForm";
	}

	@RequestMapping("save")
	public String save(MallBannerInDTO mallBanner, Model model, Integer addressType) {
		ExecuteResult<String> executeResult;
        String url = mallBanner.getBannerUrl();
        url = url.replaceAll(SysProperties.getProperty("ngIp"), "");
        mallBanner.setBannerUrl(url);
        mallBanner.setTimeFlag("0");
        mallBanner.setBannerType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
        
		if(mallBanner.getId() == null ){
			if (mallBanner.getThemeId() == null) {
				mallBanner.setThemeId(0);
			}
			executeResult = mallBannerExportService.addMallBanner(mallBanner);
		}else{
			executeResult = mallBannerExportService.modifyMallBanner(mallBanner);
		}
		String suffix = addressType==null?"":"?addressType=1";
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallbanner/list" + suffix;
	}

	@RequestMapping("moveUp")
	public String moveUp(Long id, Integer sortNum, RedirectAttributes redirectAttributes) {
		mallBannerExportService.modifyMallBannerSort(id, sortNum);
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallbanner/?repage";
	}

	@RequestMapping("moveDown")
	public String moveDown(Long id, Integer sortNum, RedirectAttributes redirectAttributes){
		mallBannerExportService.modifyMallBannerSort(id, sortNum);
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallbanner/?repage";
	}

	/**
	 * 下架
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("release")
	public String release(Long id, RedirectAttributes redirectAttributes, Integer addressType){
		mallBannerExportService.motifyMallBannerStatus(id, "0");
		String suffix = addressType==null?"":"&addressType=1";
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallbanner/?repage" + suffix;
	}

	/**
	 * 上架
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("offShelves")
	public String offShelves(Long id, RedirectAttributes redirectAttributes, Integer addressType){
		mallBannerExportService.motifyMallBannerStatus(id, "1");
		String suffix = addressType==null?"":"&addressType=1";
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/mallbanner/?repage"+suffix;
	}

}
