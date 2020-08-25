
package com.camelot.ecm.sellercenter.mallbanner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.common.DateUtil;
import com.camelot.ecm.goodscenter.view.ItemQueryInDTOView;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.enums.MallTypeEnum;
import com.camelot.sellercenter.mallBanner.dto.MallBannerDTO;
import com.camelot.sellercenter.mallBanner.dto.MallBannerInDTO;
import com.camelot.sellercenter.mallBanner.service.MallBannerExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 * 
 * <p>Description: [积分商城轮播图设置]</p>
 * Created on 2015年12月7日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/sellercenter/integralBanner")
public class IntegralBannerController extends BaseController {

	@Autowired
	private MallBannerExportService mallBannerExportService;

	@Resource
    private ItemCategoryService itemCategoryService;
	/**
	 * 
	 * <p>Description: [轮播图列表]</p>
	 * Created on 2015年12月7日
	 * @param mallBanner
	 * @param type 1：mall 2：积分商城
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping(value = {"list", ""})
	public String list(MallBannerDTO mallBanner, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<MallBannerDTO> page = new Page<MallBannerDTO>(request, response);

		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
//		pager.setSort("");
//		pager.setOrder("desc");
		// 积分商城轮播图
		mallBanner.setBannerType(MallTypeEnum.INTEGRALMALL.getId());
		DataGrid<MallBannerDTO> pageGrid = mallBannerExportService.queryMallBannerList(mallBanner, null, pager);

		page.setCount(pageGrid.getTotal());
		page.setList(pageGrid.getRows());
        model.addAttribute("page", page);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model.addAttribute("nowTime",sdf.format(new Date()));
		return "sellercenter/integralBanner/integralBannerList";
	}

	@RequestMapping("form")
	public String form(@ModelAttribute("goods") ItemQueryInDTOView goods,
			MallBannerDTO mallBanner, Model model) {
		DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
		if(mallBanner!=null && mallBanner.getId()!=null){
			mallBanner = mallBannerExportService.getMallBannerById(mallBanner.getId());
            String url = mallBanner.getBannerUrl();
            url = url.replaceAll(SysProperties.getProperty("ngIp"), "");
            mallBanner.setBannerUrl(url);
		}
		//发布开始时间/发布结束时间
		String startTime = "";
		if(mallBanner.getStartTime()!=null && !"".equals(mallBanner.getStartTime())){
			startTime = mallBanner.getStartTime();
		}
		mallBanner.setStartTime(startTime);
		String endTime = "";
		if(mallBanner.getEndTime()!=null && !"".equals(mallBanner.getEndTime())){
			endTime = mallBanner.getEndTime();
		}
		mallBanner.setEndTime(endTime);

		model.addAttribute("mallBanner", mallBanner);
		return "sellercenter/integralBanner/integralBannerForm";
	}

	@RequestMapping("save")
	public String save(MallBannerInDTO mallBanner,String dstype, Model model) {
		ExecuteResult<String> executeResult;
		if("2".equals(dstype)){
			MallBannerDTO banner = mallBannerExportService.getMallBannerById(mallBanner.getId());
			mallBanner.setBannerLink(banner.getBannerLink());
			mallBanner.setBannerType(banner.getBannerType());
			mallBanner.setBannerUrl(banner.getBannerUrl());
			mallBanner.setIntegral(banner.getIntegral());
			mallBanner.setSkuId(banner.getSkuId());
			mallBanner.setSortNumber(banner.getSortNumber());
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			mallBanner.setStartTime(sdf.format(date));
			mallBanner.setStatus(banner.getStatus());
			mallBanner.setTimeFlag(banner.getTimeFlag());
			mallBanner.setTitle(banner.getTitle());
			mallBanner.setEndTime(banner.getEndTime());
			executeResult = mallBannerExportService.modifyMallBanner(mallBanner);
			return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/integralBanner/?repage";
		}
        String url = mallBanner.getBannerUrl();
        url = url.replaceAll(SysProperties.getProperty("ngIp"), "");
        mallBanner.setBannerUrl(url);
        mallBanner.setTimeFlag("0");
        // 积分商城轮播图
        mallBanner.setBannerType(MallTypeEnum.INTEGRALMALL.getId());
		if(mallBanner.getId() == null ){
			executeResult = mallBannerExportService.addMallBanner(mallBanner);
		}else{
			executeResult = mallBannerExportService.modifyMallBanner(mallBanner);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/integralBanner/?repage";
	}

	@RequestMapping("moveUp")
	public String moveUp(Long id, Integer sortNum, RedirectAttributes redirectAttributes) {
		mallBannerExportService.modifyMallBannerSort(id, sortNum);
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/integralBanner/?repage";
	}

	@RequestMapping("moveDown")
	public String moveDown(Long id, Integer sortNum, RedirectAttributes redirectAttributes){
		mallBannerExportService.modifyMallBannerSort(id, sortNum);
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/integralBanner/?repage";
	}

	/**
	 * 下架
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("release")
	public String release(Long id, RedirectAttributes redirectAttributes){
		mallBannerExportService.motifyMallBannerStatus(id, "0");
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/integralBanner/?repage";
	}

	/**
	 * 上架
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("offShelves")
	public String offShelves(Long id, RedirectAttributes redirectAttributes){
		mallBannerExportService.motifyMallBannerStatus(id, "1");
		return "redirect:" + SysProperties.getAdminPath() + "/sellercenter/integralBanner/?repage";
	}

}
