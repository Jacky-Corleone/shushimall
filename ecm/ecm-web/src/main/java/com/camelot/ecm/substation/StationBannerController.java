
package com.camelot.ecm.substation;

import java.util.ArrayList;
import java.util.List;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.common.Json;
import com.camelot.common.enums.SettleEnum.SettleDetailStatusEnum;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
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
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping(value = "${adminPath}/station/mallbanner")
public class StationBannerController extends BaseController {

	@Autowired
	private MallBannerExportService mallBannerExportService;

	/**
	 * 主题
	 */
	@Resource
	private MallThemeService mallThemeService;
	
	/**
	 * 类目
	 */
	@Resource
	private ItemCategoryService itemCategoryService;
	
	/**
	 * 地区 
	 */
	@Resource
	private AddressBaseService addressBaseService;

	@RequestMapping(value = {"list", ""})
	public String list(MallBannerDTO mallBanner,HttpServletRequest request, HttpServletResponse response, Model model,String msg) {
		User user = UserUtils.getUser();

		model.addAttribute("msg", msg);
		Page<MallBannerDTO> page = new Page<MallBannerDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		//主题相关属性
		mallBanner.setBannerType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallBannerDTO> pageGrid = mallBannerExportService.queryMallBannerList(mallBanner,null, pager);

		page.setCount(pageGrid.getTotal());
		page.setList(pageGrid.getRows());
        model.addAttribute("page", page);
        model.addAttribute("title", mallBanner.getTitle());
        model.addAttribute("dto", mallBanner);

		//主题
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setType(mallBanner.getBannerType());
		mallThemeDTO.setStatusGroup(new int[]{1,2});
		DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
		model.addAttribute("themeList",themeDataGrid.getRows());
		model.addAttribute("themeId",mallBanner.getThemeId());
		return "/substation/mallbanner/mallBannerList";
	}

	@RequestMapping("form")
	public String form(MallBannerDTO mallBanner, Model model) {
		if(mallBanner!=null && mallBanner.getId()!=null){
			mallBanner = mallBannerExportService.getMallBannerById(mallBanner.getId());
            String url = mallBanner.getBannerUrl();
            url = url.replaceAll(SysProperties.getProperty("ngIp"), "");
            mallBanner.setBannerUrl(url);
			
			//主题
			MallThemeDTO mallThemeDTO = new MallThemeDTO();
			mallThemeDTO.setType(mallBanner.getBannerType());
			User user = UserUtils.getUser();
			mallThemeDTO.setStatusGroup(new int[]{1,2});
			DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
			model.addAttribute("themeList",themeDataGrid.getRows());
		}else{
			//主题
			MallThemeDTO mallThemeDTO = new MallThemeDTO();
			mallThemeDTO.setType(mallBanner.getBannerType());
			User user = UserUtils.getUser();
			mallThemeDTO.setStatusGroup(new int[]{1,2});
			DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
			model.addAttribute("themeList",themeDataGrid.getRows());
		}
		//发布开始时间/发布结束时间
		String startTime = "";
		if(mallBanner.getStartTime()!=null && !"".equals(mallBanner.getStartTime())){
			startTime = mallBanner.getStartTime().substring(0, 10);
		}
		mallBanner.setStartTime(startTime);
		String endTime = "";
		if(mallBanner.getEndTime()!=null && !"".equals(mallBanner.getEndTime())){
			endTime = mallBanner.getEndTime().substring(0, 10);
		}
		mallBanner.setEndTime(endTime);

		model.addAttribute("mallBanner", mallBanner);

		
		return "/substation/mallbanner/mallBannerForm";
	}

	@RequestMapping("save")
	public String save(MallBannerInDTO mallBanner, Model model) {
		ExecuteResult<String> executeResult;
        String url = mallBanner.getBannerUrl();
        url = url.replaceAll(SysProperties.getProperty("ngIp"), "");
        mallBanner.setBannerUrl(url);
        mallBanner.setBannerType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		if(mallBanner.getId() == null ){
			executeResult = mallBannerExportService.addMallBanner(mallBanner);
		}else{
			executeResult = mallBannerExportService.modifyMallBanner(mallBanner);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallbanner/list?themeType=2";
	}

	@RequestMapping("moveUp")
	public String moveUp(Long id,Integer themeType, Integer sortNum, RedirectAttributes redirectAttributes) {
		mallBannerExportService.modifyMallBannerSort(id, sortNum);
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallbanner/?repage&themeType="+themeType;
	}

	@RequestMapping("moveDown")
	public String moveDown(Long id,Integer themeType, Integer sortNum, RedirectAttributes redirectAttributes){
		mallBannerExportService.modifyMallBannerSort(id, sortNum);
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallbanner/?repage&themeType="+themeType;
	}
	
	@ResponseBody
	@RequestMapping("release")
	public Json release(Long id,String status,Integer themeType, RedirectAttributes redirectAttributes){
		Json<Object> json=new Json<Object>();
        try{
        	mallBannerExportService.motifyMallBannerStatus(id, status);
            json.setMsg("操作完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}


	@RequestMapping("offShelves")
	public String offShelves(Long id,Integer themeType, RedirectAttributes redirectAttributes){
		mallBannerExportService.motifyMallBannerStatus(id, "1");
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallbanner/list?msg=publishSuccess&themeType="+themeType;
	}
	@RequestMapping("delete")
	public String delete(Long id,Integer themeType){
		MallBannerInDTO mallBannerInDTO = new MallBannerInDTO();
		mallBannerInDTO.setId(id.intValue());
		mallBannerInDTO.setStatus("2");
		mallBannerExportService.delete(mallBannerInDTO);
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallbanner/list?msg=delSuccess&themeType="+themeType;
	}

}
