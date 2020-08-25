package com.camelot.ecm.shopCustomer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.ShopAndCustomerDTO;
import com.camelot.storecenter.dto.ShopAndCustomerQueryDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.dto.ShopDTO;
import com.camelot.storecenter.service.ShopAndCustomerService;
import com.camelot.storecenter.service.ShopCustomerServiceService;
import com.camelot.usercenter.service.UserExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;

@Controller
@RequestMapping(value = "${adminPath}/shopCustomer")
public class ShopCustomerController extends BaseController {

	@Resource
	private ShopAndCustomerService shopAndCustomerService;

	@Resource
	private UserExportService userExportService;

	@Resource
	private ShopCustomerServiceService shopCustomerServiceService;

	/**
	 * 展示站点
	 */
	@RequestMapping(value = "listShopCustomer")
	public String listShopCustomer(ShopAndCustomerQueryDTO shopAndCustomerQueryDTO, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ShopCustomerServiceDTO> page = new Page<ShopCustomerServiceDTO>(request, response);
        Pager<ShopCustomerServiceDTO> pager = new Pager<ShopCustomerServiceDTO>();
        pager.setPage(page.getPageNo());
        pager.setRows(page.getPageSize());
        pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		DataGrid<ShopCustomerServiceDTO> dataGrid = shopAndCustomerService.searchShopCustomer(pager, shopAndCustomerQueryDTO);
		page.setCount(dataGrid.getTotal());
		page.setList(dataGrid.getRows());
        model.addAttribute("page",page);
        model.addAttribute("shopAndCustomerQueryDTO",shopAndCustomerQueryDTO);
		return "shopCustomer/shopCustomer";
	}

	/**
	 * 绑定站点
	 */
	@RequestMapping(value = "addShopCustomer")
	@ResponseBody
	public Map<String, Object> createShopCustomer(ShopCustomerServiceDTO shopCustomerServiceDTO) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean ifUnique = addIfUnique(shopCustomerServiceDTO.getStationId());
		//站点唯一
		if(ifUnique == true){
			retMap.put("ifUnique", ifUnique);
			// 添加号码的人;创建时间;是否有效1为有效，0为无效
			//String userId = UserUtils.getUser().getId();
			//UserDTO user = userExportService.queryUserById(Long.valueOf(userId));
			//shopCustomerServiceDTO.setOperator(user.getUname());
			shopCustomerServiceDTO.setCreatedDt(new Date());
			shopCustomerServiceDTO.setDeletedFlag(1);
			ExecuteResult<ShopCustomerServiceDTO> ret = shopCustomerServiceService.createShopCustomerServiceDTO(shopCustomerServiceDTO);
			if (ret.isSuccess()) {
				retMap.put("messages", "保存成功!");
			} else {
				retMap.put("messages", "保存失败!");
			}
		}else{
			retMap.put("ifUnique", ifUnique);
			retMap.put("messages", "该站点已注册!");
		}
		return retMap;
	}

	/**
	 * 删除站点
	 */
	@RequestMapping(value = "deleteShopCustomer")
	@ResponseBody
	public Map<String, Object> deleteShopCustomer(HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String shopCustomerId = request.getParameter("shopCustomerId");
		ExecuteResult<ShopCustomerServiceDTO> ret = shopCustomerServiceService.deleteById(Long.valueOf(shopCustomerId));
		if (ret.isSuccess()) {
			retMap.put("messages", "删除成功!");
		} else {
			retMap.put("messages", "删除失败!");
		}
		return retMap;
	}

	/**
	 * 修改站点
	 */
	@RequestMapping(value= "updateShopCustomer")
	@ResponseBody
	public Map<String, Object> updateShopCustomer(ShopCustomerServiceDTO shopCustomerServiceDTO) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		boolean ifUnique = updateIfUnique(shopCustomerServiceDTO.getShopId(),shopCustomerServiceDTO.getStationId());
		//站点唯一
		if(ifUnique == true){
			retMap.put("ifUnique", ifUnique);
			ExecuteResult<ShopCustomerServiceDTO> ret = shopCustomerServiceService.updateSelective(shopCustomerServiceDTO);
			if (ret.isSuccess()) {
				retMap.put("messages", "修改成功!");
			} else {
				retMap.put("messages", "修改失败!");
			}
		}else{
			retMap.put("ifUnique", ifUnique);
			retMap.put("messages", "该站点已注册!");
		}
		return retMap;
	}

	/**
	 * 绑定站点时判断站点唯一性
	 */
	public boolean addIfUnique(String stationId) {
		boolean ifUnique = true;
		ShopCustomerServiceDTO shopCustomerServiceDTO = new ShopCustomerServiceDTO();
		shopCustomerServiceDTO.setStationId(stationId);
		List<ShopCustomerServiceDTO> retList = shopCustomerServiceService.searchByCondition(shopCustomerServiceDTO);
		if(retList != null && retList.size() > 0){
			ifUnique = false;
		}
		return ifUnique;
	}

	/**
	 * 修改站点时判断站点唯一性(修改站点时可以不改变原来站点)
	 */
	public boolean updateIfUnique(Long shopId,String stationId) {
		boolean ifUnique = true;
		ShopCustomerServiceDTO shopCustomerServiceDTO = new ShopCustomerServiceDTO();
		shopCustomerServiceDTO.setStationId(stationId);
		List<ShopCustomerServiceDTO> retList = shopCustomerServiceService.searchByCondition(shopCustomerServiceDTO);
		if(retList != null && retList.size() > 0){
			for(int i=0;i<retList.size();i++){
				if(!retList.get(i).getShopId().toString().equals(shopId.toString())){
					ifUnique = false;
				}
			}
		}
		return ifUnique;
	}
}
