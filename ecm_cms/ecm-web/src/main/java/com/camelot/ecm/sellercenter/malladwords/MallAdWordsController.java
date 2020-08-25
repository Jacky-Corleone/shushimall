package com.camelot.ecm.sellercenter.malladwords;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrInDTO;
import com.camelot.sellercenter.mallrecattr.dto.MallRecAttrQueryDTO;
import com.camelot.sellercenter.mallrecattr.service.MallRecAttrExportService;
import com.thinkgem.jeesite.common.persistence.Page;

/** 
 * <p>Description: [描述该类概要功能介绍]</p>
 * Created on 2015年2月27日
 * @author  <a href="mailto: guomaomao@camelotchina.com">Goma 郭茂茂</a>
 * @version 1.0 
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping("${adminPath}/mallAdWords")
public class MallAdWordsController {
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
	public ModelAndView index(MallRecAttrQueryDTO dto, HttpServletRequest request, HttpServletResponse response, Model model){

		if( dto.getRecType()== null || "".equals(dto.getRecType()) ){
			dto.setRecType(34);
		}
		
		
		// 查询楼层信息
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(200);
		MallRecDTO mrd = new MallRecDTO();
		DataGrid<MallRecDTO> dg_rec = mallRecExportService.queryMallRecList(mrd, page);
		model.addAttribute("recs", dg_rec.getRows());
		
		
		// 查询广告词列表
		Page<MallRecAttrDTO> p = new Page<MallRecAttrDTO>(request, response);
		Pager pager = new Pager();
		pager.setPage(p.getPageNo());
		pager.setRows(p.getMaxResults());
		
		DataGrid<MallRecAttrDTO> dg = mallRecAttrService.queryMallRecAttrList(pager, dto);
		p.setCount(dg.getTotal());
		p.setList(dg.getRows());
		p.setOrderBy(pager.getOrder());
		model.addAttribute("page", p);
		
		model.addAttribute("dto", dto);
		
		return new ModelAndView("/sellercenter/malladwords/index");
//        return "/sellercenter/malladwords/index";
	}
	
	@RequestMapping("/datagrid")
	@ResponseBody
	public DataGrid<MallRecAttrDTO> datagrid(Pager pager,MallRecAttrQueryDTO dto){
		return mallRecAttrService.queryMallRecAttrList(pager, dto);
	}
	@RequestMapping("/toEdit")
	public ModelAndView toEdit(Long id){
		MallRecAttrInDTO dto = new MallRecAttrInDTO();
		 
		if( id != null ){
			MallRecAttrDTO mrad = mallRecAttrService.getMallRecAttrById( id );
			BeanUtils.copyProperties(mrad, dto);
		}
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("dto", dto);
		
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(200);
		MallRecDTO mrd = new MallRecDTO();
		DataGrid<MallRecDTO> dg = mallRecExportService.queryMallRecList(mrd, page);
		params.put("recs", dg.getRows());
		
		return new ModelAndView( "/sellercenter/malladwords/edit", params);
	}
	
	@RequestMapping("/edit")
	public String edit(MallRecAttrInDTO dto,Page page){
//		return dto.getId() != null ? mallRecAttrService.modifyMallRecAttr(dto) : mallRecAttrService.addMallRecAttr(dto);
		if( dto.getId() != null ){
			mallRecAttrService.modifyMallRecAttr(dto);
		}else{
			mallRecAttrService.addMallRecAttr(dto);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/mallAdWords/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
	}
	
	@RequestMapping("/publish")
	public String publish(Long id,String status,Page page){
		mallRecAttrService.modifyMallRecAttrStatus( id,status);
		return "redirect:" + SysProperties.getAdminPath() + "/mallAdWords/index?pageNo="+page.getPageNo()+"&pageSize="+page.getPageSize();
	}
	
	@RequestMapping("/combo")
	@ResponseBody
	public List<MallRecDTO> combo(){
		
		Pager page = new Pager();
		page.setPage(1);
		page.setRows(200);
		
		MallRecDTO dto = new MallRecDTO();
		DataGrid<MallRecDTO> dg = mallRecExportService.queryMallRecList(dto, page);
		return dg.getRows();
	}
}
