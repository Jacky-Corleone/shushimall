package com.camelot.ecm.mallclassify;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.avro.generic.GenericData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.service.MallClassifyService;
import com.camelot.common.DateUtil;
import com.camelot.common.MallType;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

/**
 *
 * <p>
 * Description: [文档分类管理]
 * </p>
 * Created on 2015-2-27
 *
 * @author <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/mallClassify")
public class MallClassifyController extends BaseController {

	@Autowired
	private MallClassifyService mallClassifyService;


	/**
	 * 获取文档分类列表
	 * @param mallClassifyDTO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({ "list", "" })
	public String list(MallClassifyDTO mallClassifyDTO,HttpServletRequest request,
			HttpServletResponse response, Model model){
		Page page = new Page<MallClassifyDTO>(request, response);
		// 将分页page转换为服务所需要的pager
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		pager.setSort("created");
		pager.setOrder("desc");
        Date end=null;
        if(mallClassifyDTO.getEndTime()!=null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            Calendar   calendar   =   new   GregorianCalendar();
            calendar.setTime(mallClassifyDTO.getEndTime());
            calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
            end=calendar.getTime();
            mallClassifyDTO.setEndTime(end);
        }
		DataGrid dg = mallClassifyService.queryMallCassifyList(mallClassifyDTO, pager);
		page.setList(dg.getRows());
		page.setCount(dg.getTotal());
		model.addAttribute("page", page);
		model.addAttribute("mallClassifyDTO",mallClassifyDTO);
		model.addAttribute("typeList", this.getTypeList());
		model.addAttribute("startTime", DateUtil.formatDate(mallClassifyDTO.getStartTime()));
		model.addAttribute("endTime", DateUtil.formatDate(end));
		return "mallclassify/mallClassifyList";
	}

	/**
	 * 跳转到文档分类列表添加/编辑页面
	 * @param mallClassifyDTO
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"form"})
	public String form(MallClassifyDTO mallClassifyDTO,Model model){
		model.addAttribute("mallClassifyDTO", mallClassifyDTO);
		model.addAttribute("typeList", this.getTypeList());
		return "mallclassify/mallClassifyForm";
	}

	/**
	 * 保存
	 * @param mallClassifyDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("save")
	public String save(MallClassifyDTO mallClassifyDTO, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes){
		mallClassifyDTO.setCreated(new Date());
		ExecuteResult<String> result = mallClassifyService.addMallCassify(mallClassifyDTO);
		addMessage(redirectAttributes, result.getResult());
		return "redirect:" + SysProperties.getAdminPath() + "/mallClassify/list?repage";
	}

	/**
	 * 编辑
	 * @param mallNoticeDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("edit")
	public String edit(MallClassifyDTO mallClassifyDTO, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes){
		ExecuteResult<String> result = mallClassifyService.modifyInfoById(mallClassifyDTO);
		addMessage(redirectAttributes, result.getResultMessage());
		return "redirect:" + SysProperties.getAdminPath() + "/mallClassify/list?repage";
	}


	/**
	 *  上下架
	 * @param mallNoticeDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("status")
	public String status(MallClassifyDTO mallClassifyDTO, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes){
		ExecuteResult<String> result = mallClassifyService.modifyStatusById(mallClassifyDTO.getId(), mallClassifyDTO.getStatus());
		return "redirect:" + SysProperties.getAdminPath() + "/mallClassify/list?repage";
	}

	@RequestMapping("getMallClassifyMap")
	@ResponseBody
	public Map<String,Object> getMallClassifyMap(MallClassifyDTO mallClassifyDTO){
		Map<String,Object> mallClassifyMap = new HashMap<String,Object>();
		Pager pager = new Pager();
		pager.setPage(1);
		pager.setRows(10000);
		pager.setPageOffset(1);
		DataGrid dg = mallClassifyService.queryMallCassifyList(mallClassifyDTO, pager);
		List<MallClassifyDTO> mallClassifyDTOList = dg.getRows();
        List<MallClassifyDTO> list=new ArrayList<MallClassifyDTO>();
        if(mallClassifyDTOList!=null&&mallClassifyDTOList.size()>0){
            for(int i=0;i<mallClassifyDTOList.size();i++){
                MallClassifyDTO mdto=mallClassifyDTOList.get(i);
                if(mallClassifyDTO.getStatus()!=null){
                    if(mdto.getStatus()!=null&&mdto.getStatus().intValue()==mallClassifyDTO.getStatus().intValue()){
                        list.add(mdto);
                    }
                }else{
                    list.add(mdto);
                }
            }
        }
		mallClassifyMap.put("data", list);
		return mallClassifyMap;
	}


	/**
	 * 获取文档类型集合
	 * @param response
	 * @return
	 */
	public List<Map<Integer, Object>> getTypeList() {
		List<Map<Integer, Object>> mapList = Lists.newArrayList();
		//获取所有的文档类型
		EnumSet<MallType> currEnumSet = EnumSet.allOf (MallType. class );
		for(MallType mallType : currEnumSet){
			Map<Integer,Object> typeMap = new HashMap<Integer,Object>();
			typeMap.put(mallType.getKey(), mallType.getDesc());
			mapList.add(typeMap);
		}
		return mapList;
	}





}
