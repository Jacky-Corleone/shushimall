package com.camelot.ecm.sellercenter.mallrec;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

/**
 *  楼层信息控制层
 * 
 * @author - learrings
 * @createDate - 2015-2-28
 * Copyright (c) 2015 北京柯莱特科技有限公司 交付部 
 */
@Controller
@RequestMapping(value = "${adminPath}/mallRec")
public class MallRecController extends BaseController {

	@Autowired
	private MallRecExportService mallRecService;
	@Autowired
	private ItemCategoryService itemCategoryService;
	@Autowired
	private MallThemeService mallThemeService;
    private final static String PUTAWAY="0";
	/**
	 * 跳转楼层编辑页
	 * 
	 * @param mallRecDTO
	 * @return
	 */
	@RequestMapping("/form")
	public String toForm(MallRecDTO mallRecDTO,Model model,String addressType){
		if(addressType!=null&&"1".equals(addressType)){
			model.addAttribute("addressType", addressType);
			
		}
		if(mallRecDTO!=null&&mallRecDTO.getIdDTO()!=null){
			mallRecDTO=mallRecService.getMallRecById(mallRecDTO.getIdDTO());
		}
		if ( mallRecDTO.getThemeId()!=null && mallRecDTO.getThemeId()!=0) {
			MallThemeDTO themeDTO = mallThemeService.getMallThemeById(mallRecDTO.getThemeId());
			model.addAttribute("themeDTO",themeDTO);
		}
		DataGrid<ItemCategoryDTO> ItemCategoryData = itemCategoryService.queryItemCategoryList(0l);
		model.addAttribute("mallRec", mallRecDTO);
		model.addAttribute("itemCateGoryList", ItemCategoryData.getRows());
		return "/sellercenter/mallrec/mallRecForm";
	}
	
	/**
	 * 楼层新增/修改
	 * 
	 * @param mallRecDTO
	 * @return
	 */
	@RequestMapping("/save")
	public String save(MallRecDTO mallRecDTO,String addressType,RedirectAttributes redirectAttributes){
		if (mallRecDTO.getThemeId() == null || "".equals(mallRecDTO.getThemeId())) {
			mallRecDTO.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}
		if (mallRecDTO.getStatusDTO() == null || "".equals(mallRecDTO.getStatusDTO())) {
			mallRecDTO.setStatusDTO(1);
		}
		//首页楼层
		mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
		if(mallRecDTO.getFloorTypeDTO()!=null&&mallRecDTO.getFloorTypeDTO().equals(1)){
			//将所有的热卖单品楼层都下架
			mallRecService.updateStatusByFloorType(mallRecDTO.getThemeId().toString());
		}
		if(mallRecDTO!=null&&mallRecDTO.getIdDTO()!=null){
			mallRecService.modifyMallRec(mallRecDTO);
			addMessage(redirectAttributes, "修改楼层成功");
		}else{
			mallRecService.addMallRec(mallRecDTO);
			addMessage(redirectAttributes, "保存楼层成功");
		}
		return "redirect:" + SysProperties.getAdminPath() +  "/mallRec/list?addressType="+addressType;
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
		String addressType = request.getParameter("addressType");
		if(addressType!=null&&"1".equals(addressType)){
			if (mallRecDTO.getThemeId()==null) {
				mallRecDTO.setThemeId(BasicEnum.THEME_ID_CAR.getIntVlue());
			}
		}else{
			mallRecDTO.setThemeId(BasicEnum.THEME_ID_HOME.getIntVlue());
		}
		if(pageNo!=null){
			pager.setPage(pageNo);
		}
		Page<MallRecView> page = new Page<MallRecView>(request, response);
        if(pager.getPage()<1){
            pager.setPage(1);
        }
        if(pager.getRows()<1){
            pager.setRows(10);
        }
        mallRecDTO.setRecTypeDTO(BasicEnum.INT_ENUM_THEMETYPE_HOME.getIntVlue());
        DataGrid<MallRecDTO>  listMallRecDTO=mallRecService.queryMallRecList(mallRecDTO,pager);
        DataGrid<MallRecView>  MallRecViewData= new DataGrid<MallRecView>();
        MallRecViewData.setTotal(listMallRecDTO.getTotal());
        List<MallRecView> mallRecViewList = new ArrayList<MallRecView>();
        //取得一级目录
        DataGrid<ItemCategoryDTO> ItemCategoryData = itemCategoryService.queryItemCategoryList(0l);
        List<ItemCategoryDTO> itemCategoryList = ItemCategoryData.getRows();
        if(listMallRecDTO.getRows()!=null&&listMallRecDTO.getRows().size()>0){
        	for(MallRecDTO MallRecDTO :listMallRecDTO.getRows()){
        		MallRecView mallRecView = new MallRecView();
        		mallRecView.setMallRec(MallRecDTO);
        		for(ItemCategoryDTO itemCategoryDTO : itemCategoryList){
        			if(itemCategoryDTO.getCategoryCid().equals(MallRecDTO.getCategoryIdDTO())){
        				mallRecView.setcName(itemCategoryDTO.getCategoryCName());
        				break;
        			}
        		}
        		List<ItemCatCascadeDTO> itemCatCascadeDTOList = itemCategoryService.queryParentCategoryList(MallRecDTO.getCategoryIdDTO()).getResult();
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
        if(addressType!=null&&"1".equals(addressType)){
	        //查询子站
	        MallThemeDTO themeDTO = new MallThemeDTO();
	        themeDTO.setStatus(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
	        Pager p= new Pager();
	        p.setPage(1);
	        p.setRows(500);
	        DataGrid<MallThemeDTO> themeList = mallThemeService.queryMallThemeList(themeDTO, "1",p);
	        model.addAttribute("themeList", themeList.getRows());
        }
        model.addAttribute("page", page);
        model.addAttribute("mallRecDTO", mallRecDTO);
        model.addAttribute("addressType", addressType);
		return "/sellercenter/mallrec/mallRecList";
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
	@RequestMapping("/modifyStatus")
	public String modifyStatus(Integer pageNo,Integer floorType, Long idDTO,String statusDTO,String addressType,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response){
		String status=PUTAWAY.equals(statusDTO)?"1":"0";
		if(status!=null&&"1".equals(status)&&floorType!=null&&new Integer(1).equals(floorType)){
			String themeId = request.getParameter("themeId");
			mallRecService.updateStatusByFloorType(themeId);
		}
		ExecuteResult<String> result= mallRecService.modifyMallRecStatus(idDTO,status);
		if (result.isSuccess()&&"1".equals(result.getResult())) {
			addMessage(redirectAttributes, PUTAWAY.equals(statusDTO)?"上架成功":"下架成功");
		} else {
			addMessage(redirectAttributes, PUTAWAY.equals(statusDTO)?"上架失败":"下架失败");
		}
		return "redirect:" + SysProperties.getAdminPath() + "/mallRec/list?pageNo="+pageNo+"&addressType="+addressType;
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
	
	@RequestMapping("/delRecById")
	public String delRecById(Long idDTO,Integer pageNo,String addressType){
		System.out.println("id:"+idDTO);
		ExecuteResult<String> res =mallRecService.deleteMallRecById(idDTO);
		return "redirect:" + SysProperties.getAdminPath() + "/mallRec/list?pageNo="+pageNo+"&addressType="+addressType;
	}
	
	@ResponseBody
	@RequestMapping("/queryRecbyThemeId")
	public List<MallRecDTO> queryRecbyThemeId(Integer themeId){
		MallRecDTO recDTO = new MallRecDTO();
		Pager<MallRecDTO> page = new Pager<MallRecDTO>();
		page.setPage(1);
		page.setRows(999);
		recDTO.setThemeId(themeId);
		recDTO.setStatusDTO(BasicEnum.INT_ENUM_STATIC_ADDED.getIntVlue());
		DataGrid<MallRecDTO> dataGrid = mallRecService.queryMallRecList(recDTO, page);
		return dataGrid.getRows();
	}
}
