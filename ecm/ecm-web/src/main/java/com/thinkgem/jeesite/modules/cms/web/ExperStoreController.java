/*package com.thinkgem.jeesite.modules.cms.web;

import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.basecenter.domain.AddressBase;
import com.camelot.basecenter.service.AddressBaseService;
import com.camelot.cmscenter.dto.CmsExperStoreDTO;
import com.camelot.cmscenter.service.CmsExperStoreService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/cms/experStore")
public class ExperStoreController {
	
	@Resource
	private CmsExperStoreService cmsExperStoreService;
	
	@Resource
	private AddressBaseService addressBaseService;
	
	@RequestMapping(value = "/list")
	public String list(@ModelAttribute("pager") Pager<?> pager, @Param("cmsExperStoreDTO") CmsExperStoreDTO cmsExperStoreDTO,HttpServletRequest request,HttpServletResponse response,Model model){
		try {
			String name = request.getParameter("name");
			cmsExperStoreDTO.setStoreName(name);
			ExecuteResult<DataGrid<CmsExperStoreDTO>> excuteResult =  cmsExperStoreService.queryExperStoreList(cmsExperStoreDTO,pager);
			//页面分页
			Page<CmsExperStoreDTO> page = new Page<CmsExperStoreDTO>();
			page.setPageNo(pager.getPage());
			page.setPageSize(pager.getRows());
			page.setCount(excuteResult.getResult().getTotal());
			page.setList(excuteResult.getResult().getRows());
			request.setAttribute("page", page);
			request.setAttribute("name", cmsExperStoreDTO.getStoreName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "modules/cms/experstoreList";
	}
	@RequestMapping(value="/delete")
	public String delete(@Param("id") String id,HttpServletRequest request,HttpServletResponse response,Model model){
		CmsExperStoreDTO cmsExperStoreDTO = new CmsExperStoreDTO();
		if(StringUtils.isNotEmpty(id)){
			cmsExperStoreDTO = cmsExperStoreService.queryById(id);
			cmsExperStoreDTO.setUpdateBy(UserUtils.getUser().getId());
			cmsExperStoreService.delete(cmsExperStoreDTO);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/experStore/list";
		}
	@RequestMapping(value="/update")
	public String update(@Param("id") String id,HttpServletRequest request,HttpServletResponse response,Model model){
		CmsExperStoreDTO cmsExperStoreDTO = new CmsExperStoreDTO();
		List<AddressBase> originList = addressBaseService.queryAddressBase("0");
		if(StringUtils.isNotEmpty(id)){
			cmsExperStoreDTO = cmsExperStoreService.queryById(id);
			cmsExperStoreDTO.setAddressList(originList);//获取省级列表
			originList = addressBaseService.queryAddressBase(cmsExperStoreDTO.getProvinceId());
			model.addAttribute("cityList",originList);//获取市级列表
			model.addAttribute("cmsExperStoreDTO", cmsExperStoreDTO);
			String[] coor = cmsExperStoreDTO.getCoordinate().split(",");
			model.addAttribute("coorX", coor[0]);
			model.addAttribute("coorY", coor[1]);
		}
		return "modules/cms/experstoreForm";
		}
	@RequestMapping(value="/add")
	public String add(@Param("cmsExperStoreDTO") CmsExperStoreDTO cmsExperStoreDTO,HttpServletRequest request,HttpServletResponse response,Model model){
		if(null!=cmsExperStoreDTO){
			try {
				if(StringUtils.isNotEmpty(cmsExperStoreDTO.getId())){
					cmsExperStoreDTO.setUpdateBy(UserUtils.getUser().getId());
					cmsExperStoreService.update(cmsExperStoreDTO);
				}else{
					cmsExperStoreDTO.setId(String.valueOf(UUID.randomUUID()));
					cmsExperStoreDTO.setCreateBy(UserUtils.getUser().getId());
					cmsExperStoreDTO.setDelFlag(0);
					cmsExperStoreService.add(cmsExperStoreDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:" + SysProperties.getAdminPath() + "/cms/experStore/list";
	}
	@RequestMapping(value="addOfPage")
	public String addOfPage(@Param("cmsExperStoreDTO") CmsExperStoreDTO cmsExperStoreDTO,HttpServletRequest request,HttpServletResponse response,Model model){
		//1.查询省 list
		//获取籍贯代码
		List<AddressBase> originList = addressBaseService.queryAddressBase("0");
		cmsExperStoreDTO.setAddressList(originList);
		model.addAttribute("cmsExperStoreDTO", cmsExperStoreDTO);
		//2.ajax查询市
		//3.根据选择的省市展现地图显示区域
		return "modules/cms/experstoreForm";
	}
	
	*//**查询省市区*//*
	@ResponseBody
	@RequestMapping(value="/queryAddress")
	public List<AddressBase> queryAddress(@Param("parentCode")String parentCode) {
		List<AddressBase> addressList = addressBaseService.queryAddressBase(parentCode);
		return addressList;
	}
}
*/