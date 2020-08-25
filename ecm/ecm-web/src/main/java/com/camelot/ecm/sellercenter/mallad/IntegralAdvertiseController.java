package com.camelot.ecm.sellercenter.mallad;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.common.util.DateUtils;
import com.camelot.ecm.goodscenter.view.ItemQueryInDTOView;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.enums.MallTypeEnum;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.thinkgem.jeesite.common.persistence.Page;

/**
 * 
 * <p>Description: [积分商城广告位]</p>
 * Created on 2015年12月7日
 * @author  <a href="mailto: songwenbin@camelotchina.com">宋文斌</a>
 * @version 1.0
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping("${adminPath}/sellercenter/integralAdvertise")
public class IntegralAdvertiseController {

	@Resource
	private MallAdExportService mallAdService;
    @Resource
    private ItemCategoryService itemCategoryService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
    }  


    /**
     * 
     * <p>Description: [积分商城广告列表页跳转]</p>
     * Created on 2015年12月7日
     * @param dto
     * @param request
     * @param response
     * @param model
     * @return
     * @author:[宋文斌]
     */
	@RequestMapping("/index")
	public ModelAndView index(MallAdQueryDTO dto, HttpServletRequest request, HttpServletResponse response, Model model) {

		Page<MallAdDTO> p = new Page<MallAdDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(p.getPageNo());
		pager.setRows(p.getMaxResults());
		
		// 积分商城
		dto.setAdvType(MallTypeEnum.INTEGRALMALL.getId());
		DataGrid<MallAdDTO> dg = mallAdService.queryMallAdList(pager,dto);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page",p);
		model.addAttribute("dto", dto);

		return new ModelAndView("/sellercenter/integralAd/index");
		
    }
	
	/**
	 * 
	 * <p>Description: [跳转到积分商城广告修改页]</p>
	 * Created on 2015年9月9日
	 * @param id
	 * @param statusDTO 用于数据回显
	 * @param page 用于数据回显
	 * @param model
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/toEdit")
	public String toEdit(@ModelAttribute("goods") ItemQueryInDTOView goods,
			Long id, MallAdQueryDTO statusDTO, Page page, Model model) {
		DataGrid dataGrid =  itemCategoryService.queryItemCategoryList(0L);
        if(dataGrid!=null){
            model.addAttribute("platformList",dataGrid.getRows());
        }else{
            model.addAttribute("platformList",new ArrayList());
        }
		MallAdInDTO dto = new MallAdInDTO();
		if( id != null ){
			MallAdDTO mad = mallAdService.getMallAdById(Long.valueOf(id));
			BeanUtils.copyProperties(mad, dto);
			dto.setAdURL(mad.getAdURL());
		}
        DataGrid<ItemCategoryDTO> items =  itemCategoryService.queryItemCategoryList(0L);
        if(items!=null&&items.getRows()!=null){
            model.addAttribute("itemList",items.getRows());
        }
        model.addAttribute("dto",dto);
        model.addAttribute("statusDTO", statusDTO);
        model.addAttribute("page", page);
		return "/sellercenter/integralAd/edit";
	}
	/**
	 * 
	 * <p>Description: [删除积分商城广告]</p>
	 * Created on 2015-12-23
	 * @param id
	 * @return
	 * @author:[周志军]
	 */
	@RequestMapping("/toDelete")
	@ResponseBody
	public ExecuteResult<Boolean> toDelete(Long id) {
		ExecuteResult<Boolean> er = mallAdService.deleteMallAd(id);
		return er;
	}
	
    /**
     * 
     * <p>Description: [积分商城广告保存修改]</p>
     * Created on 2015年9月9日
     * @param dto
     * @param page
     * @param statusAdType 用于数据回显
     * @param statusAdTitle 用于数据回显
     * @param statusTimeFlag 用于数据回显
     * @param statusStartTime 用于数据回显
     * @param statusEndTime 用于数据回显
     * @param statusStatus 用于数据回显
     * @return
     * @author:[宋文斌]
     */
	@RequestMapping("/edit")
	public String edit(MallAdInDTO dto, Page page, Integer statusAdType,
			String statusAdTitle, Integer statusTimeFlag, Date statusStartTime,
			Date statusEndTime, Integer statusStatus,String startTime_,String endTime_) {
//		return dto.getId() != null ? mallAdService.modifyMallBanner(dto) : mallAdService.addMallAd(dto);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		try {
			if (dto.getStatus() == 0) {
				startTime = sdf.parse(startTime_);
				endTime = sdf.parse(endTime_);
				Date now = new Date();
				if(now.compareTo(startTime) >= 0 && now.compareTo(endTime) <= 0){
					dto.setStatus(1);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		dto.setStartTime(startTime);
		dto.setEndTime(endTime);
		// 积分商城
		dto.setAdvType(MallTypeEnum.INTEGRALMALL.getId());
		if (dto.getId() != null) {
			mallAdService.modifyMallBanner(dto);
		} else {
			mallAdService.addMallAd(dto);
		}
		String baseURL = SysProperties.getAdminPath() + "/sellercenter/integralAdvertise/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String url = this.getParamsURI(statusAdType, statusAdTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus);
		return "redirect:" + baseURL + url;
	}
   
	/**
	 * 
	 * <p>Description: [商城广告上架或下架]</p>
	 * Created on 2015年9月9日
	 * @param id
	 * @param status
	 * @param page
	 * @param statusAdType 用于数据回显
	 * @param statusAdTitle 用于数据回显
	 * @param statusTimeFlag 用于数据回显
	 * @param statusStartTime 用于数据回显
	 * @param statusEndTime 用于数据回显
	 * @param statusStatus 用于数据回显
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/publish")
	public String publish(Long id, String status, Page page,
			Integer statusAdType, String statusAdTitle, Integer statusTimeFlag,
			Date statusStartTime, Date statusEndTime, Integer statusStatus) {
		mallAdService.modifyMallAdStatus( id,status );
		String baseURL = SysProperties.getAdminPath() + "/sellercenter/integralAdvertise/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String url = this.getParamsURI(statusAdType, statusAdTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus);
		return "redirect:" + baseURL + url;
	}
	
	/**
	 * 
	 * <p>Description: [获取用于回显的URI]</p>
	 * Created on 2015年9月9日
	 * @param statusAdType
	 * @param statusAdTitle
	 * @param statusTimeFlag
	 * @param statusStartTime
	 * @param statusEndTime
	 * @param statusStatus
	 * @return
	 * @author:[宋文斌]
	 */
	private String getParamsURI(Integer statusAdType, String statusAdTitle,
				Integer statusTimeFlag, Date statusStartTime, Date statusEndTime,
				Integer statusStatus) {
		String url = "";
		if (statusAdType != null) {
			url += "&adType=" + statusAdType;
		}
		if (!StringUtils.isBlank(statusAdTitle)) {
			try {
				statusAdTitle = URLEncoder.encode(statusAdTitle, "UTF-8");
				url += "&adTitle=" + statusAdTitle;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (statusTimeFlag != null) {
			url += "&timeFlag=" + statusTimeFlag;
		}
		if (statusStartTime != null) {
			url += "&startTime="
					+ DateUtils.format(statusStartTime, "yyyy-MM-dd");
		}
		if (statusEndTime != null) {
			url += "&endTime=" + DateUtils.format(statusEndTime, "yyyy-MM-dd");
		}
		if (statusStatus != null) {
			url += "&status=" + statusStatus;
		}
		return url;
	}
}
