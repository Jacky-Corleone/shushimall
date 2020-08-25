package com.camelot.ecm.sellercenter.malladrec;

import com.camelot.common.util.DateUtils;
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
import com.thinkgem.jeesite.common.persistence.Page;

import org.apache.commons.lang3.StringUtils;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
@RequestMapping("${adminPath}/mallAdRec")
public class MallAdRecController {
	@Resource
	private MallRecAttrExportService mallRecAttrService;
	@Resource
	private MallRecExportService mallRecExportService;
	

    @InitBinder
    public void initBinder(WebDataBinder binder) {  
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
        dateFormat.setLenient(false);  
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));  
    }  
	
	
	@RequestMapping("/index")
	public ModelAndView index(HttpServletRequest request,MallRecAttrQueryDTO dto,Page p, Model model){
		String addressType = request.getParameter("addressType");
		// 查询楼层信息
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(200);
		MallRecDTO mrd = new MallRecDTO();
		mrd.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		if (addressType==null || "".equals(addressType)) {
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
			mrd.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}else {
			dto.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
			mrd.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
		}
		DataGrid<MallRecDTO> dg_rec = mallRecExportService.queryMallRecList(mrd, page);
		model.addAttribute("recs", dg_rec.getRows());
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

		dto.setType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		DataGrid<MallRecAttrDTO> dg = mallRecAttrService.queryMallRecAttrList(pager, dto);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page", p);
		
		model.addAttribute("dto", dto);
		model.addAttribute("addressType", addressType);
		
		return new ModelAndView("/sellercenter/malladrec/index");
//        return "/sellercenter/malladrec/index";
	}
	
	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid<MallRecAttrDTO> datagrid(Pager pager,MallRecAttrQueryDTO dto){
		return mallRecAttrService.queryMallRecAttrList(pager, dto);
	}
	
	@RequestMapping("/toEdit")
	public ModelAndView toEdit(Long id, MallRecAttrQueryDTO statusDTO, Page p,Integer addressType) {
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
		MallRecDTO mrd = new MallRecDTO();
		mrd.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		if (addressType==null || "".equals(addressType)) {
			statusDTO.setThemeId(0);
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
			mrd.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}else {
			dto.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
			mrd.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
		}
		DataGrid<MallRecDTO> dg = mallRecExportService.queryMallRecList(mrd, page);
		params.put("recs", dg.getRows());
		params.put("page", p);
		params.put("statusDTO", statusDTO);
		params.put("addressType", addressType);
		return new ModelAndView("/sellercenter/malladrec/edit", params);
		// return "/sellercenter/malladrec/edit";
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
	public String edit(MallRecAttrInDTO dto, Page page, Long statusRecId,
			Integer statusRecType, String statusTitle, Integer statusTimeFlag,
			Date statusStartTime, Date statusEndTime, Integer statusStatus,String startTime_,String endTime_,String addressType) {
//		return dto.getId() != null ? mallRecAttrService.modifyMallRecAttr(dto) : mallRecAttrService.addMallRecAttr(dto);
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
		if (addressType!=null && !"".equals(addressType)) {
			MallRecDTO mallRecById = mallRecExportService.getMallRecById(dto.getRecId());
			dto.setThemeId(mallRecById.getThemeId());
		}else {
			dto.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}
		dto.setStartTime(startTime);
		dto.setEndTime(endTime);
		dto.setType(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		
		if (dto.getId() != null) {
			result = mallRecAttrService.modifyMallRecAttr(dto);
		} else {
			result = mallRecAttrService.addMallRecAttr(dto);
		}
		String baseURL = SysProperties.getAdminPath() + "/mallAdRec/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String paramsURI = this.getParamsURI(statusRecId, statusRecType, statusTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus,addressType);
		return "redirect:" + baseURL + paramsURI;
	}
	
	/**
	 * 
	 * <p>Description: [上架或下架]</p>
	 * Created on 2015年9月9日
	 * @param id
	 * @param status
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
	@RequestMapping("/publish")
	public String publish(Long id,String status,Page page,Long statusRecId,
			Integer statusRecType, String statusTitle, Integer statusTimeFlag,
			Date statusStartTime, Date statusEndTime, Integer statusStatus,String addressType){
		mallRecAttrService.modifyMallRecAttrStatus( id, status );
		String baseURL = SysProperties.getAdminPath() + "/mallAdRec/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
		String paramsURI = this.getParamsURI(statusRecId, statusRecType, statusTitle, statusTimeFlag, statusStartTime, statusEndTime, statusStatus,addressType);
		return "redirect:" + baseURL + paramsURI;
	}
	
	
	@RequestMapping("/delRecAttById")
	public String delRecAttById(Long id,String addressType,Page page){
		System.out.println(id);
		ExecuteResult<String> res =mallRecAttrService.deleteMallRecAttrById(id);
		return "redirect:" + SysProperties.getAdminPath() + "/mallAdRec/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize()+"&addressType="+addressType;
	}
	
	/**
	 * 
	 * <p>Description: [获取用于回显的URI]</p>
	 * Created on 2015年9月9日
	 * @param statusRecId
	 * @param statusRecType
	 * @param statusTitle
	 * @param statusTimeFlag
	 * @param statusStartTime
	 * @param statusEndTime
	 * @param statusStatus
	 * @return &a=xx&b=xx
	 * @author:[宋文斌]
	 */
	private String getParamsURI(Long statusRecId,
			Integer statusRecType, String statusTitle, Integer statusTimeFlag,
			Date statusStartTime, Date statusEndTime, Integer statusStatus,String addressType){
		String url = "";
		if (!StringUtils.isBlank(statusTitle)) {
			try {
				statusTitle = URLEncoder.encode(statusTitle, "UTF-8");
				url += "&title=" + statusTitle;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if (statusRecId != null) {
			url += "&recId=" + statusRecId;
		}
		if (statusRecType != null) {
			url += "&recType=" + statusRecType;
		}
		if (statusTimeFlag != null) {
			url += "&timeFlag=" + statusTimeFlag;
		}
		if (statusStartTime != null) {
			url += "&startTime=" + DateUtils.format(statusStartTime, "yyyy-MM-dd");
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
