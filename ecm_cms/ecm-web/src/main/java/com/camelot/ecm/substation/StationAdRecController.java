package com.camelot.ecm.substation;

import com.camelot.basecenter.dto.DictionaryDTO;
import com.camelot.basecenter.service.DictionaryService;
import com.camelot.common.Json;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrInDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrQueryDTO;
import com.camelot.sellercenter.mallrecattr.service.MallRecAttrExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.camelot.usercenter.service.UserApplyAuditService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年8月19日
 * @author  <a href="mailto: guojianning@camelotchina.com">Goma 郭建宁</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@SuppressWarnings({"rawtypes","unchecked","unused"})
@Controller
@RequestMapping("${adminPath}/station/mallAdRec")
public class StationAdRecController {
	private static final Logger logger = LoggerFactory.getLogger(StationAdRecController.class);
	@Resource
	private MallRecAttrExportService mallRecAttrService;
	
	/**
	 * 楼层接口
	 */
	@Resource
	private MallRecExportService mallRecExportService;
	/**
	 * 主题
	 */
	@Resource
	private MallThemeService mallThemeService;
		
    @InitBinder
    public void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
    }  
	
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,MallRecAttrQueryDTO dto,Page p, Model model){
		// 查询楼层信息
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(200);
		
		String pageNo=request.getParameter("pageNo");
        String pageSize=request.getParameter("pageSize");
        if(pageNo!=null&&!"".equals(pageNo)){
            p.setPageNo(new Integer(pageNo));
        }else{
            p.setPageNo(1);
        }
        if(pageSize!=null&&!"".equals(pageSize)){
            p.setPageSize(new Integer(pageSize));
        }else{
            p.setPageSize(10);
        }
		// 查询广告推荐位列表
		Pager pager = new Pager();
		pager.setPage(p.getPageNo());
		pager.setRows(p.getPageSize());

		dto.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallRecAttrDTO> dg = mallRecAttrService.queryMallRecAttrList(pager, dto);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page", p);
		
		model.addAttribute("dto", dto);
		
		//查询类目频道
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setStatusGroup(new int[]{1,2});
		DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
		model.addAttribute("themes", themeDataGrid.getRows());
		model.addAttribute("themeId",dto.getThemeId());
		//查询楼层信息
		MallRecDTO mrd = new MallRecDTO();
		if(dto.getThemeId() != null){
			mrd.setThemeId(dto.getThemeId());
		}
		mrd.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		DataGrid<MallRecDTO> dg_rec = mallRecExportService.queryMallRecList(mrd, page);
		model.addAttribute("recs", dg_rec.getRows());
		return new ModelAndView("/substation/malladrec/index");
	}
	@RequestMapping("/toEdit")
	public ModelAndView toEdit(Long id, MallRecAttrQueryDTO statusDTO, Page p) {
		MallRecAttrInDTO dto = new MallRecAttrInDTO();
		if (id != null) {
			MallRecAttrDTO mra = mallRecAttrService.getMallRecAttrById(id);
			BeanUtils.copyProperties(mra, dto);
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("dto", dto);

		Pager page = new Pager();
		page.setPage(1);
		page.setRows(200);
		//查询类目频道
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		mallThemeDTO.setStatusGroup(new int[]{1,2});
		DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
		params.put("themes", themeDataGrid.getRows());
		MallRecDTO mrd = new MallRecDTO();
		DataGrid<MallRecDTO> dg = mallRecExportService.queryMallRecList(mrd, page);
		params.put("recs", dg.getRows());
		params.put("page", p);
		params.put("statusDTO", statusDTO);
		return new ModelAndView("/substation/malladrec/edit", params);
	}
	@RequestMapping("/mallReclist")
	@ResponseBody
	public List<MallRecDTO> mallReclist(Integer themeId){
		//主题相关属性
		MallRecDTO mallRecDTO = new MallRecDTO();
		mallRecDTO.setThemeId(themeId);
		mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
        DataGrid<MallRecDTO>  listMallRecDTO=mallRecExportService.queryMallRecList(mallRecDTO,null);
        
		return listMallRecDTO.getRows();
	}
	
	/**
	 * 
	 * <p>Description: [保存修改]</p>
	 * Created on 2015年9月9日
	 * @param dto
	 * @param page
	 * @param statusRecId 用于数据回显
	 * @param statusRecType 用于数据回显
	 * @param statusTitle 用于数据回显
	 * @param statusTimeFlag 用于数据回显
	 * @param statusStartTime 用于数据回显
	 * @param statusEndTime 用于数据回显
	 * @param statusStatus 用于数据回显
	 * @return
	 * @author:[宋文斌]
	 */
	@RequestMapping("/edit")
	public String edit(MallRecAttrInDTO dto, Page page,String startTime_,String endTime_) {
		ExecuteResult<String> result = null;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startTime = null;
		Date endTime = null;
		try {
			if(!StringUtils.isEmpty(startTime_)){
				startTime = sdf.parse(startTime_);
			}
			if(!StringUtils.isEmpty(endTime_)){
				endTime = sdf.parse(endTime_);
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dto.setStartTime(startTime);
		dto.setEndTime(endTime);
		dto.setType(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
		if (dto.getId() != null) {
			result = mallRecAttrService.modifyMallRecAttr(dto);
		} else {
			result = mallRecAttrService.addMallRecAttr(dto);
		}
//		String baseURL = SysProperties.getAdminPath() + "/mallAdRec/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
//		String paramsURI = this.getParamsURI(statusRecId, statusRecType, statusTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus);
		return "redirect:/admin/station/mallAdRec/index" ;
	}
	@ResponseBody
	@RequestMapping("/publish")
	public Json publish(Long id,String status,Page page){
		Json<Object> json=new Json<Object>();
        try{
        	mallRecAttrService.modifyMallRecAttrStatus( id, status );
            json.setMsg("操作完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}
	
	@RequestMapping("/delRecAttById")
	public String delRecAttById(Long id,Page page){
		ExecuteResult<String> res =mallRecAttrService.deleteMallRecAttrById(id);
		return "redirect:/admin/station/mallAdRec/index" ;
	}
}
