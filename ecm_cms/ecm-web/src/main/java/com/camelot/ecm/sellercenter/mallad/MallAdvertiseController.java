package com.camelot.ecm.sellercenter.mallad;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.common.util.DateUtils;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.enums.MallTypeEnum;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.thinkgem.jeesite.common.persistence.Page;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月27日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("${adminPath}/mallAdvertise")
public class MallAdvertiseController {

	@Resource
	private MallAdExportService mallAdService;
    @Resource
    private ItemCategoryService itemCategoryService;

    //主题
    @Resource
  	private MallThemeService mallThemeService;
    @InitBinder
    public void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
    }  
	/**
	 * 
	 * <p>Discription:广告列表页跳转</p>
	 * Created on 2015年1月26日
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/index")
	public ModelAndView index(MallAdQueryDTO dto, HttpServletRequest request, HttpServletResponse response, Model model) {
		String addressType = request.getParameter("addressType");
		if (addressType==null || "".equals(addressType)) {
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}else {
			dto.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
		}
		Page<MallAdDTO> p = new Page<MallAdDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(p.getPageNo());
		pager.setRows(p.getMaxResults());
		dto.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallAdDTO> dg = mallAdService.queryMallAdList(pager,dto);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page",p);
		model.addAttribute("dto", dto);
		model.addAttribute("addressType", addressType);

		return new ModelAndView("/sellercenter/mallad/index");
		
//        return "/sellercenter/mallad/index";
    }
    /**
     *
     * <p>Discription:广告列表页跳转</p>
     * Created on 2015年1月26日
     * @return
     * @author:[Goma 郭茂茂]
     */
    @RequestMapping("/indexwc")
    public ModelAndView indexwc(MallAdQueryDTO dto, HttpServletRequest request, HttpServletResponse response, Model model) {
    	String addressType = request.getParameter("addressType");
    	if (addressType==null || "".equals(addressType)) {
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}else {
			dto.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
		}
    	dto.setAdType(5);
        Page<MallAdDTO> p = new Page<MallAdDTO>(request, response);
        Pager pager = new Pager();
        pager.setPage(p.getPageNo());
        pager.setRows(p.getMaxResults());
        dto.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
        DataGrid<MallAdDTO> dg = mallAdService.queryMallAdList(pager,dto);
        p.setCount(dg.getTotal());
        p.setList(dg.getRows());
        p.setOrderBy(pager.getOrder());
        model.addAttribute("page",p);
        model.addAttribute("dto", dto);

        return new ModelAndView("/sellercenter/mallad/indexwc");

//        return "/sellercenter/mallad/index";
    }
	/**
	 * 
	 * <p>Discription:广告列表查询</p>
	 * Created on 2015年1月26日
	 * @param pager
	 * @param dto
	 * @return
	 * @author:[Goma 郭茂茂]
	 */
	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid<MallAdDTO> datagrid(Pager pager,MallAdQueryDTO dto){
		dto.setAdvType(MallTypeEnum.MALL.getId());
		return mallAdService.queryMallAdList(pager,dto);
	}
	
	/**
	 * 
	 * <p>Description: [跳转到商城广告修改页]</p>
	 * Created on 2015年9月9日
	 * @param id
	 * @param statusDTO 用于数据回显
	 * @param page 用于数据回显
	 * @param model
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/toEdit")
	public String toEdit(Long id, MallAdQueryDTO statusDTO,MallThemeDTO mallThemeDTO, Page page, Model model,Integer addressType ) {
		MallAdInDTO dto = new MallAdInDTO();
		if( id != null ){
			MallAdDTO mad = mallAdService.getMallAdById(Long.valueOf(id));
			BeanUtils.copyProperties(mad, dto);
			dto.setAdURL(mad.getAdURL());
			if (dto.getAdType() !=null && dto.getAdType()==6) {
				String adSrc = dto.getAdSrc();
				if (adSrc!=null) {
					String[] adImgs = adSrc.split(",");
					if (adImgs.length>0) {
						dto.setAdSrc(adImgs[0]);
						model.addAttribute("adImgs", adImgs);
					}
				}
			}
			if ( dto.getThemeId()!=null && dto.getThemeId()!=0) {
				MallThemeDTO themeDTO = mallThemeService.getMallThemeById(dto.getThemeId());
				model.addAttribute("themeDTO",themeDTO);
			}
		}
        DataGrid<ItemCategoryDTO> items =  itemCategoryService.queryItemCategoryList(0L);
        if(items!=null&&items.getRows()!=null){
            model.addAttribute("itemList",items.getRows());
        }
        model.addAttribute("addressType",addressType);
        model.addAttribute("dto",dto);
        model.addAttribute("statusDTO", statusDTO);
        model.addAttribute("page", page);
        model.addAttribute("mallThemeDTO",mallThemeDTO);
//		return new ModelAndView("/sellercenter/mallad/edit","dto",dto);
		return "/sellercenter/mallad/edit";
	}
	
	/**
	 * 
	 * <p>Description: [跳转到我猜你喜欢修改页]</p>
	 * Created on 2015年9月9日
	 * @param id
	 * @param statusDTO 用于数据回显
	 * @param page 用于数据回显
	 * @param model
	 * @return
	 * @author:[宋文斌]
	 */
    @RequestMapping("/toEditwc")
    public String toEditWc(Long id, MallAdQueryDTO statusDTO, Page page, Model model){
        MallAdInDTO dto = new MallAdInDTO();
        if( id != null ){
            MallAdDTO mad = mallAdService.getMallAdById(Long.valueOf(id));
            BeanUtils.copyProperties(mad, dto);
            dto.setAdURL(mad.getAdURL());
            String adSrc = dto.getAdSrc();
        }
        DataGrid<ItemCategoryDTO> items =  itemCategoryService.queryItemCategoryList(0L);
        if(items!=null&&items.getRows()!=null){
            model.addAttribute("itemList",items.getRows());
        }
        model.addAttribute("dto",dto);
        model.addAttribute("statusDTO",statusDTO);
        model.addAttribute("page",page);
//		return new ModelAndView("/sellercenter/mallad/edit","dto",dto);
        return "/sellercenter/mallad/wcnxhedit";
    }

    /**
     * 
     * <p>Description: [商城广告保存修改]</p>
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
			Date statusEndTime, Integer statusStatus,String startTime_,String endTime_,String addressType) {
//		return dto.getId() != null ? mallAdService.modifyMallBanner(dto) : mallAdService.addMallAd(dto);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		try {
			if (startTime_!=null && endTime_!=null && !"".equals(startTime_) && !"".equals(endTime_)) {
				startTime = sdf.parse(startTime_);
				endTime = sdf.parse(endTime_);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dto.getAdType()!=null && dto.getAdType()!=6 && dto.getAdSrc()!=null && !"".equals(dto.getAdSrc())) {
			String[] adImgs = dto.getAdSrc().split(",");
			if (adImgs.length>0) {
				dto.setAdSrc(adImgs[0]);
			}
		}
		if (dto.getThemeId() == null || "".equals(dto.getThemeId())) {
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}
		dto.setStartTime(startTime);
		dto.setEndTime(endTime);
		dto.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		if( dto.getId() != null ){
			mallAdService.modifyMallBanner(dto);
		}else{
			mallAdService.addMallAd(dto);
		}
		String baseURL = SysProperties.getAdminPath() + "/mallAdvertise/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String url = this.getParamsURI(statusAdType, statusAdTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus,addressType);
		return "redirect:" + baseURL + url;
	}
   
	/**
	 * 
	 * <p>Description: [我猜你喜欢保存修改]</p>
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
	@RequestMapping("/editwc")
    public String editWc(MallAdInDTO dto,Page page,Integer statusAdType,
			String statusAdTitle, Integer statusTimeFlag, Date statusStartTime,
			Date statusEndTime, Integer statusStatus,String addressType){
//		return dto.getId() != null ? mallAdService.modifyMallBanner(dto) : mallAdService.addMallAd(dto);
		dto.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		if (dto.getThemeId() == null || "".equals(dto.getThemeId())) {
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}
        if( dto.getId() != null ){
            mallAdService.modifyMallBanner(dto);
        }else{
            mallAdService.addMallAd(dto);
            if(dto.getId()!=null){
                mallAdService.modifyMallAdStatus( dto.getId(),"0" );
            }
        }
        String baseURL = SysProperties.getAdminPath() + "/mallAdvertise/indexwc?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
        String url = this.getParamsURI(statusAdType, statusAdTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus,addressType);
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
			Date statusStartTime, Date statusEndTime, Integer statusStatus,String addressType) {
		mallAdService.modifyMallAdStatus( id,status );
		String baseURL = SysProperties.getAdminPath() + "/mallAdvertise/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String url = this.getParamsURI(statusAdType, statusAdTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus,addressType);
		return "redirect:" + baseURL + url;
	}
	
	/**
	 * 
	 * <p>Description: [我猜你喜欢上架或下架]</p>
	 * Created on 2015年9月9日
	 * @param id
	 * @param status
	 * @param page
	 * @param statusAdType
	 * @param statusAdTitle
	 * @param statusTimeFlag
	 * @param statusStartTime
	 * @param statusEndTime
	 * @param statusStatus
	 * @return
	 * @author:[宋文斌]
	 */
    @RequestMapping("/publishwc")
    public String publishwc(Long id,String status,Page page,
    		Integer statusAdType, String statusAdTitle, Integer statusTimeFlag,
			Date statusStartTime, Date statusEndTime, Integer statusStatus,String addressType){
        mallAdService.modifyMallAdStatus( id,status );
        String baseURL = SysProperties.getAdminPath() + "/mallAdvertise/indexwc?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String url = this.getParamsURI(statusAdType, statusAdTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus,addressType);
		return "redirect:" + baseURL + url;
    }
	private Pager buildPager(Page page){
		//分页类
		Pager pager = new Pager();
		//设置当前页的起始记录
		pager.setPageOffset(page.getFirstResult());
		//设置每页显示的记录数
		pager.setRows(page.getMaxResults());
		//设置当前页
		pager.setPage(page.getPageNo());
		return pager;
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
				Integer statusStatus,String addressType) {
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
		if (addressType != null) {
			url += "&addressType=" + addressType;
		}
		return url;
	}
}
