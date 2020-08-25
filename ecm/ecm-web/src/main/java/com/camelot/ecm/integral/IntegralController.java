package com.camelot.ecm.integral;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.camelot.maketcenter.dto.IntegralConfigDTO;
import com.camelot.maketcenter.service.IntegralConfigExportService;
import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.util.SysProperties;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
@Controller
@RequestMapping(value = "${adminPath}/integral")
public class IntegralController extends BaseController{
	
	@Resource
	private IntegralConfigExportService integralConfigService;
	@RequestMapping(value = "form")
	public String form(IntegralConfigDTO integralConfigDTO,String isView,Model model,HttpServletRequest request){
		List<IntegralConfigDTO> backIntegral = new ArrayList<IntegralConfigDTO>();
		if (isView!=null) {
			backIntegral = integralConfigService.queryIntegralConfigDTO(integralConfigDTO, null).getResult().getRows();
			if(backIntegral.get(0).getIntegralType()==1){
				List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
				for (int i = 0; i < backIntegral.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("startPrice", backIntegral.get(i).getStartPrice());
					map.put("endPrice", backIntegral.get(i).getEndPrice());
					map.put("getIntegral", backIntegral.get(i).getGetIntegral());
					mapList.add(map);
				}
				 
				model.addAttribute("mapList",mapList);
			}
			model.addAttribute("integralConfigDTO", backIntegral.get(0));
		}else{
		}
		if (backIntegral.size()<1) {
			IntegralConfigDTO configDTO = new IntegralConfigDTO();
			backIntegral.add(configDTO);
		}
		model.addAttribute("isView", isView);
		return "/integral/integralForm";
	}
	
	@RequestMapping(value = "list")
	public String list(IntegralConfigDTO integralConfigDTO,Page page,Model model){
		ExecuteResult<DataGrid<IntegralConfigDTO>> er = integralConfigService.queryOneType(integralConfigDTO, null);
		if(er.isSuccess()){
			DataGrid<IntegralConfigDTO> dg = er.getResult();
			page.setCount(dg.getTotal().intValue());
			page.setList(dg.getRows());
		}
		model.addAttribute("page", page);
		model.addAttribute("integralConfigDTO", integralConfigDTO);
		return "/integral/integralList";
	}
	
	@RequestMapping(value = "save")
	public String save(IntegralConfigDTO integralConfigDTO,BigDecimal[] startPrice,BigDecimal[] endPrice,BigDecimal[] getIntegral){
		if(1 == integralConfigDTO.getIntegralType()){
			for(int i = 0; i < startPrice.length; i++ ){
				if(null == startPrice[i] || null == endPrice[i] || null == getIntegral[i]){
					continue;
				}
				integralConfigDTO.setUseIntegral(null);
				integralConfigDTO.setExchangeRate(null);
				integralConfigDTO.setStartPrice(startPrice[i]);
				integralConfigDTO.setEndPrice(endPrice[i]);
				integralConfigDTO.setGetIntegral(getIntegral[i]);
				List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
				list.add(integralConfigDTO);
				integralConfigService.addIntegralConfigDTO(list);
			}
		}else if(2 == integralConfigDTO.getIntegralType()){
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setUseIntegral(null);
			integralConfigDTO.setExchangeRate(null);
			integralConfigDTO.setGetIntegral(getIntegral[1]);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.addIntegralConfigDTO(list);
		}else if(3 == integralConfigDTO.getIntegralType()){
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setExchangeRate(null);
			integralConfigDTO.setGetIntegral(null);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.addIntegralConfigDTO(list);
		}else{
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setUseIntegral(null);
			integralConfigDTO.setGetIntegral(null);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.addIntegralConfigDTO(list);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/integral/list";
	}

	@RequestMapping(value = "edit")
	public String edit(IntegralConfigDTO integralConfigDTO,BigDecimal[] startPrice,BigDecimal[] endPrice,BigDecimal[] getIntegral){
		if(1 == integralConfigDTO.getIntegralType()){
			IntegralConfigDTO configDTO = new IntegralConfigDTO();
			configDTO.setIntegralType(integralConfigDTO.getIntegralType());
			configDTO.setPlatformId(integralConfigDTO.getPlatformId());
			ExecuteResult<Integer>  delResult = integralConfigService.deleteByType(configDTO);
			if(delResult.isSuccess()){
				for(int i = 0; i < startPrice.length; i++ ){
					if(null == startPrice[i] || null == endPrice[i] || null == getIntegral[i]){
						continue;
					}
					integralConfigDTO.setUseIntegral(null);
					integralConfigDTO.setExchangeRate(null);
					integralConfigDTO.setStartPrice(startPrice[i]);
					integralConfigDTO.setEndPrice(endPrice[i]);
					integralConfigDTO.setGetIntegral(getIntegral[i]);
					List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
					list.add(integralConfigDTO);
					integralConfigService.addIntegralConfigDTO(list);
				}
			}
		}else if(2 == integralConfigDTO.getIntegralType()){
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setUseIntegral(null);
			integralConfigDTO.setExchangeRate(null);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.updateIntegralConfigDTO(list);
		}else if(3 == integralConfigDTO.getIntegralType()){
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setExchangeRate(null);
			integralConfigDTO.setGetIntegral(null);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.updateIntegralConfigDTO(list);
		}else if(4 == integralConfigDTO.getIntegralType()){
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setUseIntegral(null);
			integralConfigDTO.setGetIntegral(null);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.updateIntegralConfigDTO(list);
		}else if(7 == integralConfigDTO.getIntegralType()){
			integralConfigDTO.setStartPrice(null);
			integralConfigDTO.setEndPrice(null);
			integralConfigDTO.setExchangeRate(null);
			integralConfigDTO.setGetIntegral(getIntegral[1]);
			List<IntegralConfigDTO> list = new ArrayList<IntegralConfigDTO>();
			list.add(integralConfigDTO);
			integralConfigService.updateIntegralConfigDTO(list);
		}
		return "redirect:" + SysProperties.getAdminPath() + "/integral/list";
	}
	
	@RequestMapping(value = "getPlatId")
	@ResponseBody
	public String getPlatId(){
		List<IntegralConfigDTO> list = getIntegralType();
		Map<String, Object> map = new HashMap<String,Object>();
		Set<Integer> typeSet = new HashSet<Integer>();
		for(int i = 0 ;i<list.size();i++){
			Integer type = list.get(i).getIntegralType();
			typeSet.add(type);
		}
		map.put("list", list);
		map.put("type", typeSet);
		return JSON.toJSONString(map);
	}
	/**
	 * <p>Discription:[获取可用积分类型]</p>
	 * Created on 2015-12-14
	 * @return
	 * @author:[范东藏]
	 */
	private List<IntegralConfigDTO> getIntegralType(){
		List<IntegralConfigDTO> result = new ArrayList<IntegralConfigDTO>();
		List<IntegralConfigDTO> dgrows = new ArrayList<IntegralConfigDTO>();
		ExecuteResult<DataGrid<IntegralConfigDTO>> er = integralConfigService.queryOneType(new IntegralConfigDTO(), null);
		if(er.isSuccess()){
			DataGrid<IntegralConfigDTO> dg = er.getResult();
			dgrows = dg.getRows();
		}
		for (int i = 1; i < 6; i++) {
			int j = 0;
			boolean flag = false;
			for (int d = 0; d < dgrows.size(); d++) {
				if(dgrows.get(d).getIntegralType() > 4){
					if(i == dgrows.get(d).getIntegralType()-2&&j == dgrows.get(d).getPlatformId()){
						flag = true;
					}
				} else if(i == dgrows.get(d).getIntegralType()&&j == dgrows.get(d).getPlatformId()){
					flag = true;
				}
			}
			if (!flag) {
				IntegralConfigDTO tmp = new IntegralConfigDTO();
				tmp.setIntegralType(i);
				tmp.setPlatformId(j);
				result.add(tmp);
			}
		}
		return result;
	}

}
