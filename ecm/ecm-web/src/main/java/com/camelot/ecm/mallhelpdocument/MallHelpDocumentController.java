package com.camelot.ecm.mallhelpdocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.camelot.common.Json;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.camelot.basecenter.dto.MallClassifyDTO;
import com.camelot.basecenter.dto.MallDocumentDTO;
import com.camelot.basecenter.service.MallClassifyService;
import com.camelot.basecenter.service.MallDocumentService;
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
 * Description: [帮助文档管理]
 * </p>
 * Created on 2015-2-27
 * 
 * @author <a href="mailto: zhangzhiqiang34@camelotchina.com">张志强</a>
 * @version 1.0 Copyright (c) 2015 北京柯莱特科技有限公司 交付部
 */
@Controller
@RequestMapping(value = "${adminPath}/mallHelpDocument")
public class MallHelpDocumentController extends BaseController {
	
	@Autowired
	private MallDocumentService mallDocumentService;
	
	@Autowired
	private MallClassifyService mallClassifyService;

	/**
	 * 获取文档分类列表
	 * @param mallDocumentDTO
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({ "list", "" })
	public String list(MallDocumentDTO mallDocumentDTO,String timeType,String beginTime,String endTime,
			HttpServletRequest request,HttpServletResponse response, Model model){
		Page page = new Page<MallDocumentDTO>(request, response);
		// 将分页page转换为服务所需要的pager
		Pager pager = new Pager();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		pager.setSort("mallCreated");
		pager.setOrder("desc");
		//设置日期查询条件
		if("createTime".equals(timeType)){
			mallDocumentDTO.setMallBeginCreated(DateUtil.parseDate(beginTime));
			mallDocumentDTO.setMallEndCreated(DateUtil.parseDate(endTime));
		}else if("publicTime".equals(timeType)){
			mallDocumentDTO.setMallBeginStartTime(DateUtil.parseDate(beginTime));
			mallDocumentDTO.setMallEndStartTime(DateUtil.parseDate(endTime));
		}
		DataGrid dg = mallDocumentService.queryMallDocumentList(mallDocumentDTO, pager);
		page.setList(dg.getRows());
		page.setCount(dg.getTotal());
		model.addAttribute("page", page);
		model.addAttribute("mallDocumentDTO",mallDocumentDTO);
		model.addAttribute("typeList", this.getTypeList());
		model.addAttribute("beginTime", beginTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("timeType", timeType);
		return "mallhelpdocument/mallHelpDocumentList";
	}
	
	/**
	 * 跳转到文档分类列表添加/编辑页面
	 * @param mallDocumentDTO
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"form"})
	public String form(MallDocumentDTO mallDocumentDTO,Model model){
		List<MallClassifyDTO> mallClassifyDTOList = new ArrayList<MallClassifyDTO>();
		if(mallDocumentDTO.getMallId() != null){
			mallDocumentDTO = mallDocumentService.getMallDocumentById(Long.valueOf(mallDocumentDTO.getMallId().toString()));
			//查询二级分类
			MallClassifyDTO mallClassifyDTO = new MallClassifyDTO();
			mallClassifyDTO.setType(mallDocumentDTO.getMallType());
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(10000);
			pager.setPageOffset(1);
			DataGrid dg = mallClassifyService.queryMallCassifyList(mallClassifyDTO, pager);
			mallClassifyDTOList = dg.getRows();
		}
		model.addAttribute("mallDocumentDTO", mallDocumentDTO);
		model.addAttribute("typeList", this.getTypeList());
		model.addAttribute("mallClassifyDTOList", mallClassifyDTOList);
		return "mallhelpdocument/mallHelpDocumentForm";
	}
	
	/**
	 * 跳转到文档分类查看页面
	 * @param mallDocumentDTO
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:user:view")
	@RequestMapping({"view"})
	public String view(MallDocumentDTO mallDocumentDTO,Model model){
		List<MallClassifyDTO> mallClassifyDTOList = new ArrayList<MallClassifyDTO>();
		if(mallDocumentDTO.getMallId() != null){
			mallDocumentDTO = mallDocumentService.getMallDocumentById(Long.valueOf(mallDocumentDTO.getMallId().toString()));
			//查询二级分类
			MallClassifyDTO mallClassifyDTO = new MallClassifyDTO();
			mallClassifyDTO.setType(mallDocumentDTO.getMallType());
			Pager pager = new Pager();
			pager.setPage(1);
			pager.setRows(10000);
			pager.setPageOffset(1);
			DataGrid dg = mallClassifyService.queryMallCassifyList(mallClassifyDTO, pager);
			mallClassifyDTOList = dg.getRows();
		}
		model.addAttribute("mallDocumentDTO", mallDocumentDTO);
		model.addAttribute("typeList", this.getTypeList());
		model.addAttribute("mallClassifyDTOList", mallClassifyDTOList);
		return "mallhelpdocument/mallHelpDocumentView";
	}
	
	/**
	 * 保存
	 * @param mallDocumentDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("save")
	public String save(MallDocumentDTO mallDocumentDTO, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes){
		mallDocumentDTO.setMallCreated(new Date());
		mallDocumentDTO.setMallStartTime(new Date());
		String path = SysProperties.getProperty("ngIp");
		// 将图片路径保存为相对路径
		mallDocumentDTO.setMallContentUrl(mallDocumentDTO.getMallContentUrl().replaceAll(path,""));
		ExecuteResult<String> result = mallDocumentService.addMallDocument(mallDocumentDTO);
		addMessage(redirectAttributes, result.getResultMessage());
		//20150623  帮助文档发布后 跳转到查询页面 不用显示全部
        //redirectAttributes.addAttribute("mallTitle",mallDocumentDTO.getMallTitle());
        //redirectAttributes.addAttribute("mallId",mallDocumentDTO.getMallId());
        //redirectAttributes.addAttribute("id",mallDocumentDTO.getMallId());
		return "redirect:" + SysProperties.getAdminPath() + "/mallHelpDocument/list?repage";
	}
	
	/**
	 * 编辑
	 * @param mallDocumentDTO
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("edit")
	public String edit(MallDocumentDTO mallDocumentDTO, HttpServletRequest request,
			Model model, RedirectAttributes redirectAttributes){
		String path = SysProperties.getProperty("ngIp");
		// 将图片路径保存为相对路径
		mallDocumentDTO.setMallContentUrl(mallDocumentDTO.getMallContentUrl().replaceAll(path,""));
		ExecuteResult<String> result = mallDocumentService.modifyInfoById(mallDocumentDTO);
		addMessage(redirectAttributes, result.getResultMessage());
		//20150623  帮助文档修改后 跳转到查询页面 不用显示全部
       // redirectAttributes.addAttribute("mallTitle",mallDocumentDTO.getMallTitle());
       // redirectAttributes.addAttribute("mallId",mallDocumentDTO.getMallId());
       // redirectAttributes.addAttribute("id",mallDocumentDTO.getMallId());
		return "redirect:" + SysProperties.getAdminPath() + "/mallHelpDocument/list?repage";
	}
	
	/**
	 *  下架/发布
	 * @param mallDocumentDTO
	 * @param request
	 * @return
	 */
	@RequiresPermissions("sys:user:edit")
	@RequestMapping("status")
    @ResponseBody
	public Json status(MallDocumentDTO mallDocumentDTO, HttpServletRequest request){
        Json json=new Json();
        try{
            ExecuteResult<String> result = mallDocumentService.modifyStatusById(mallDocumentDTO.getMallId(), mallDocumentDTO.getMallStatus());
            if(result.isSuccess()){
                if(mallDocumentDTO.getMallStatus()!=null&&mallDocumentDTO.getMallStatus().intValue()==2){
                    json.setMsg("发布成功");
                }else if(mallDocumentDTO.getMallStatus()!=null&&mallDocumentDTO.getMallStatus().intValue()==1){
                    json.setMsg("下架成功");
                }else{
                    json.setMsg("操作成功");
                }
                json.setSuccess(true);
            }else{
                if(result.getErrorMessages()!=null&&result.getErrorMessages().size()>0){
                    for(int i=0;i<result.getErrorMessages().size();i++){
                        logger.error("帮助文档上下架log:"+result.getErrorMessages().get(i));
                    }
                }
                if(mallDocumentDTO.getMallStatus()!=null&&mallDocumentDTO.getMallStatus().intValue()==2){
                    json.setMsg("发布失败，请稍后再试");
                }else if(mallDocumentDTO.getMallStatus()!=null&&mallDocumentDTO.getMallStatus().intValue()==1){
                    json.setMsg("下架失败，请稍后再试");
                }else{
                    json.setMsg("操作失败，请稍后再试");
                }
                json.setSuccess(false);
            }
        }catch(Exception e){
            logger.error("帮助文档上下架log:"+e.getMessage());
            if(mallDocumentDTO.getMallStatus()!=null&&mallDocumentDTO.getMallStatus().intValue()==2){
                json.setMsg("发布失败，请稍后再试");
            }else if(mallDocumentDTO.getMallStatus()!=null&&mallDocumentDTO.getMallStatus().intValue()==1){
                json.setMsg("下架失败，请稍后再试");
            }else{
                json.setMsg("操作失败，请稍后再试");
            }
            json.setSuccess(false);
        }
		return json;
	}
	
	/**
	 * 获取文档类型集合
	 * @param
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
