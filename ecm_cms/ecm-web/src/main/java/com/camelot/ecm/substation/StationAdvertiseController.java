package com.camelot.ecm.substation;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.camelot.common.Json;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallBanner.dto.MallBannerInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdInDTO;
import com.camelot.sellercenter.malladvertise.dto.MallAdQueryDTO;
import com.camelot.sellercenter.malladvertise.service.MallAdExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月27日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("${adminPath}/station/mallAdvertise")
public class StationAdvertiseController {

	@Resource
	private MallAdExportService mallAdService;
    @Resource
    private ItemCategoryService itemCategoryService;

    /**
     * 楼层
     */
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
		Page<MallAdDTO> p = new Page<MallAdDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(p.getPageNo());
		pager.setRows(p.getMaxResults());

        //主题相关属性
		dto.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallAdDTO> dg = mallAdService.queryMallAdList(pager,dto);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page",p);
		model.addAttribute("dto", dto);

        //主题
        MallThemeDTO mallThemeDTO = new MallThemeDTO();
        mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setStatusGroup(new int[]{1,2});
        DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
        model.addAttribute("themeList",themeDataGrid.getRows());
		model.addAttribute("themeId",dto.getThemeId());

		return new ModelAndView("/substation/mallad/index");

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
        User user = UserUtils.getUser();
        dto.setAdType(5);
        Page<MallAdDTO> p = new Page<MallAdDTO>(request, response);
        Pager pager = new Pager();
        pager.setPage(p.getPageNo());
        pager.setRows(p.getMaxResults());
        //主题相关属性
        DataGrid<MallAdDTO> dg = mallAdService.queryMallAdList(pager,dto);
        p.setCount(dg.getTotal());
        p.setList(dg.getRows());
        p.setOrderBy(pager.getOrder());
        model.addAttribute("page",p);
        model.addAttribute("dto", dto);

        //主题
        MallThemeDTO mallThemeDTO = new MallThemeDTO();
        mallThemeDTO.setType(dto.getAdType());
        mallThemeDTO.setStatus(1);
        mallThemeDTO.setUserId(user.getId());
        DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
        model.addAttribute("themeList",themeDataGrid.getRows());

        return new ModelAndView("/substation/mallad/indexwc");

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
		return mallAdService.queryMallAdList(pager,dto);
	}
	
	@RequestMapping("/toEdit")
	public String toEdit(Long id,Model model,MallAdInDTO dto){
        User user = UserUtils.getUser();
		if( id != null ){
			MallAdDTO mad = mallAdService.getMallAdById(id);
			BeanUtils.copyProperties(mad, dto);
			dto.setAdURL(mad.getAdURL());
			
			 //主题
	        MallThemeDTO mallThemeDTO = new MallThemeDTO();
	        mallThemeDTO.setType(dto.getAdvType());
			mallThemeDTO.setStatusGroup(new int[]{1,2});
	        DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
	        model.addAttribute("themeList",themeDataGrid.getRows());
		}else{
			//主题
	        MallThemeDTO mallThemeDTO = new MallThemeDTO();
	        mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
			mallThemeDTO.setStatusGroup(new int[]{1,2});
	        DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
			 model.addAttribute("themeList",themeDataGrid.getRows());
		}
        model.addAttribute("dto",dto);

		return "/substation/mallad/edit";
	}
    @RequestMapping("/toEditwc")
    public String toEditWc(Long id,Model model,MallAdInDTO dto){
        User user = UserUtils.getUser();
        if( id != null ){
            MallAdDTO mad = mallAdService.getMallAdById(Long.valueOf(id));
            BeanUtils.copyProperties(mad, dto);
            dto.setAdURL(mad.getAdURL());
            
            //主题
            MallThemeDTO mallThemeDTO = new MallThemeDTO();
            mallThemeDTO.setType(dto.getAdType());
            mallThemeDTO.setStatus(1);
            mallThemeDTO.setUserId(user.getId());
            DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
            model.addAttribute("themeList",themeDataGrid.getRows());
        }
        model.addAttribute("dto",dto);

        return "/substation/mallad/wcnxhedit";
    }

	@RequestMapping("/edit")
	public String edit(MallAdInDTO dto,Page page,HttpServletRequest request){
		dto.setAdvType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		String sellType = request.getParameter("sellType");
		if (null!=sellType && !"".equals(sellType)) {
			if (sellType=="2" || "2".equals(sellType)) {
				dto.setAdType(6);
			}
		}
		if( dto.getId() != null ){
			mallAdService.modifyMallBanner(dto);
		}else{
			dto.setStatus(1);
			mallAdService.addMallAd(dto);
		}
		
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallAdvertise/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize()+"&themeType="+dto.getAdType();
	}
    @RequestMapping("/editwc")
    public String editWc(MallAdInDTO dto,Page page){
//		return dto.getId() != null ? mallAdService.modifyMallBanner(dto) : mallAdService.addMallAd(dto);

        if( dto.getId() != null ){
            mallAdService.modifyMallBanner(dto);
        }else{
            mallAdService.addMallAd(dto);
            if(dto.getId()!=null){
                mallAdService.modifyMallAdStatus( dto.getId(),"0" );
            }
        }

        return "redirect:" + SysProperties.getAdminPath() + "/station/mallAdvertise/indexwc?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize()+"&themeType="+dto.getAdType();
    }
    
    @ResponseBody
	@RequestMapping("/publish")
	public Json publish(Long id,String status,Page page,Integer themeType){
		Json<Object> json=new Json<Object>();
        try{
        	mallAdService.modifyMallAdStatus( id,status );
            json.setMsg("操作完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}
    @RequestMapping("/publishwc")
    public String publishwc(Long id,String status,Page page,Integer themeType){
        mallAdService.modifyMallAdStatus( id,status );
        return "redirect:" + SysProperties.getAdminPath() + "/station/mallAdvertise/indexwc?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize()+"&themeType="+themeType;
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
	 * 删除方法
	 */
	@RequestMapping(value = "delete")
	@ResponseBody
	public Map<String, Object> deleteShopCustomer(Long id) {
		Map<String,Object> retMap = new HashMap<String,Object>();
		if (mallAdService.deleteById(id) > 0) {
			retMap.put("messages", "删除成功!");
		} else {
			retMap.put("messages", "删除失败!");
		}
		return retMap;
	}
	/**
	 * 
	 * <p>Discription:[通过二级类目id查找其三级类目]</p>
	 * Created on 2016年1月2日
	 * @param themId
	 * @return
	 * @author:[王鹏]
	 */
	@RequestMapping(value = "findThreeCate")
	@ResponseBody
	public Json findThreeCate(HttpServletRequest request) {
   	    Json json = new Json();
		String themId=request.getParameter("themId");
		if(StringUtils.isBlank(themId)){
			json.setMsg("频道id不能为空！");
			json.setSuccess(false);
			return json;
		}
		MallThemeDTO dto=mallThemeService.getMallThemeById(Long.parseLong(themId));
		if(dto!=null&&dto.getcId()!=null){
			DataGrid<ItemCategoryDTO> res=itemCategoryService.queryItemCategoryList(dto.getcId());
			json.setObj(res.getRows());
			json.setSuccess(true);
		}
		return json;
	}
}
