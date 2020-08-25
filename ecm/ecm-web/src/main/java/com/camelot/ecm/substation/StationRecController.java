package com.camelot.ecm.substation;

import com.camelot.common.Json;
import com.camelot.ecm.sellercenter.mallrec.view.MallRecView;
import com.camelot.goodscenter.dto.ItemCatCascadeDTO;
import com.camelot.goodscenter.dto.ItemCategoryDTO;
import com.camelot.goodscenter.service.ItemCategoryService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.common.enmu.BasicEnum;
import com.camelot.openplatform.util.SysProperties;
import com.camelot.sellercenter.mallRec.dto.MallRecDTO;
import com.camelot.sellercenter.mallRec.service.MallRecExportService;
import com.camelot.sellercenter.malltheme.dto.MallThemeDTO;
import com.camelot.sellercenter.malltheme.service.MallThemeService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  楼层信息控制层
 * 
 * @author - learrings
 * @createDate - 2015-2-28
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping(value = "${adminPath}/station/mallRec")
public class StationRecController extends BaseController {

	@Autowired
	private MallRecExportService mallRecExportService;
	@Autowired
	private ItemCategoryService itemCategoryService;
    private final static String PUTAWAY="0";

	//主题
	@Resource
	private MallThemeService mallThemeService;
	/**
	 * 跳转楼层编辑页
	 * 
	 * @param mallRecDTO
	 * @return
	 */
	@RequestMapping("/form")
	public String toForm(MallRecDTO mallRecDTO,Model model){
		Integer themeType = mallRecDTO.getRecTypeDTO();
		User user = UserUtils.getUser();
		if(mallRecDTO!=null&&mallRecDTO.getIdDTO()!=null){
			mallRecDTO=mallRecExportService.getMallRecById(mallRecDTO.getIdDTO());
		}
		//主题
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setType(themeType);
		mallThemeDTO.setStatusGroup(new int[]{1,2});
		DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
		model.addAttribute("themeList",themeDataGrid.getRows());
		model.addAttribute("mallRec", mallRecDTO);

		return "/substation/mallrec/mallRecForm";
	}
	
	/**
	 * 楼层新增/修改
	 * 
	 * @param mallRecDTO
	 * @return
	 */
	@RequestMapping("/save")
	public String save(MallRecDTO mallRecDTO,RedirectAttributes redirectAttributes){
		
		if(mallRecDTO!=null&&mallRecDTO.getIdDTO()!=null){
			mallRecExportService.modifyMallRec(mallRecDTO);
			addMessage(redirectAttributes, "修改楼层成功");
		}else{
			mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
			mallRecDTO.setStatusDTO(BasicEnum.INT_ENUM_STATIC_SHELVES.getIntVlue());
			mallRecExportService.addMallRec(mallRecDTO);
			addMessage(redirectAttributes, "保存楼层成功");
		}
		return "redirect:" + SysProperties.getAdminPath() + "/station/mallRec/?repage&themeType="+mallRecDTO.getRecTypeDTO();
	}

	/**
	 * 删除楼层
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/delete")
	@ResponseBody
	public Map<String,Object> delete(Long idDTO,Integer themeType,RedirectAttributes redirectAttributes){
		ExecuteResult<String> executeResult= mallRecExportService.deleteMallRecById(idDTO);
		addMessage(redirectAttributes, "删除楼层成功");
		Map<String,Object> map = new HashMap<String,Object>();

		if (executeResult.isSuccess()) {
			map.put("success",true);
			map.put("msg","删除成功！");
		} else {
			map.put("success",false);
			map.put("msg", "删除失败！");
		}
		return map;
	}
	
	/**
	 * 楼层分页查询
	 * 
	 * @param pager
	 * @param mallRecDTO
	 * @return
	 */
	@RequiresPermissions("mallRec:view")
	@RequestMapping({"list", ""})
	public String list(Integer pageNo,Pager pager,MallRecDTO mallRecDTO, HttpServletRequest request, HttpServletResponse response, Model model) {
		User user = UserUtils.getUser();

		if(pageNo!=null){
			pager.setPage(pageNo);
		}
		Page<MallRecView> page = new Page<MallRecView>(request, response);
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
			
        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(10);
        }
		//主题相关属性
		mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_CATEGORY.getIntVlue());
        DataGrid<MallRecDTO>  listMallRecDTO=mallRecExportService.queryMallRecList(mallRecDTO,pager);
        DataGrid<MallRecView>  MallRecViewData= new DataGrid<MallRecView>();
        MallRecViewData.setTotal(listMallRecDTO.getTotal());
        List<MallRecView> mallRecViewList = new ArrayList<MallRecView>();
        
        if(listMallRecDTO.getRows()!=null&&listMallRecDTO.getRows().size()>0){
        	for(MallRecDTO MallRecDTO :listMallRecDTO.getRows()){
        		MallRecView mallRecView = new MallRecView();
        		mallRecView.setMallRec(MallRecDTO);
        		mallRecViewList.add(mallRecView);
        	}
        }
        MallRecViewData.setRows(mallRecViewList);
        if(MallRecViewData!=null){
        	 page.setList(MallRecViewData.getRows());
             page.setCount(MallRecViewData.getTotal());
             page.setPageNo(pager.getPage());
             page.setPageSize(pager.getRows());
        }
        model.addAttribute("page", page);
		model.addAttribute("mallRecDTO", mallRecDTO);

		//主题
		MallThemeDTO mallThemeDTO = new MallThemeDTO();
		mallThemeDTO.setType(mallRecDTO.getRecTypeDTO());
		mallThemeDTO.setStatusGroup(new int[]{1,2});
		DataGrid<MallThemeDTO> themeDataGrid=mallThemeService.queryMallThemeList(mallThemeDTO, null, null);
		model.addAttribute("themeList",themeDataGrid.getRows());
		model.addAttribute("themeId",mallRecDTO.getThemeId());
		return "/substation/mallrec/mallRecList";
	}
    /**
     * <p>Discription:[方法功能中文描述:上下架 
     *  对楼层进行上下架操作的时候： 
     *  1 判断该楼层位是否是上架状态，如果是发布状态的话  直接进行上架操作。
     *  2 否则  根据id获取该楼层的楼层位 和状态 （queryById）
     *  3 根据状态（上架），楼层位  查询该楼层位在该状态的数量（queryCount）
     *  4 如果数量>0  说明该楼层位已经发布了 不能再发布相同的楼层位  
     *  5 否则  直接进行上下架操作
     * ]</p>
     * Created on 2015年1月28日
     * @return
     * @author:[chenx]
     */
	@ResponseBody
	@RequestMapping("/modifyStatus")
	public Json modifyStatus(Integer pageNo,Long idDTO,String statusDTO,Integer themeType,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response){
		Json<Object> json=new Json<Object>();
        try{
        	String status=PUTAWAY.equals(statusDTO)?"1":"0";
    		ExecuteResult<String> result= mallRecExportService.modifyMallRecStatus(idDTO,status);
    		if (result.isSuccess()&&"1".equals(result.getResult())) {
    			addMessage(redirectAttributes, PUTAWAY.equals(statusDTO)?"上架成功":"下架成功");
    		} else {
    			addMessage(redirectAttributes, PUTAWAY.equals(statusDTO)?"上架失败":"下架失败");
    		}
    		json.setMsg("操作完成");
            json.setSuccess(true);
        }catch(Exception e){
            json.setMsg("操作出现意外错误，请稍后再试");
            json.setSuccess(false);
        }
		return json;
	}
	
	private Pager buildPager(Page<MallRecDTO> page){
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
	 * <p>Discription:[三级联动 通过子站主题查询子站对应的楼层信息]</p>
	 * Created on 2015年11月18日
	 * @param themeId
	 * @return
	 * @author: 尹归晋
	 */
	@ResponseBody
	@RequestMapping("/queryRecbyThemeId")
	public List<MallRecDTO> queryRecbyThemeId(Integer themeId){
		if (themeId==null || 0==themeId) {
			return null;
		}
		MallRecDTO recDTO = new MallRecDTO();
		Pager<MallRecDTO> page = new Pager<MallRecDTO>();
		page.setPage(1);
		page.setRows(999);
		User user = UserUtils.getUser();
		recDTO.setThemeId(themeId);
		recDTO.setStatusDTO(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		DataGrid<MallRecDTO> dataGrid = mallRecExportService.queryMallRecList(recDTO, page);
		return dataGrid.getRows();
	}
	
}
