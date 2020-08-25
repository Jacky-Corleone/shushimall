package com.camelot.ecm.QQCustomer;

import com.camelot.openplatform.common.DataGrid;
import com.camelot.openplatform.common.ExecuteResult;
import com.camelot.openplatform.common.Pager;
import com.camelot.storecenter.dto.QqCustomerDTO;
import com.camelot.storecenter.dto.ShopCustomerServiceDTO;
import com.camelot.storecenter.service.QqCustomerService;
import com.camelot.storecenter.service.ShopExportService;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * QQ客服控制层
 * @author Klaus
 */
@Controller
@RequestMapping("${adminPath}/qqCustomer")
public class QQCustomerController {

	@Resource
	private QqCustomerService qqCustomerService;

	@Resource
	private ShopExportService shopExportService;

	/**
	 * 展示QQ客服列表
	 * @return
	 */
	@RequestMapping("list")
	public String listQQCustomer(QqCustomerDTO qqCustomerDTO, HttpServletRequest request,HttpServletResponse response, Model model) {
		//平台客服
		qqCustomerDTO.setCustomerType(2);
		qqCustomerDTO.setDeletedFlag(0);
		Page<QqCustomerDTO> page = new Page<QqCustomerDTO>(request, response);
		Pager<QqCustomerDTO> pager = new Pager<QqCustomerDTO>();
		pager.setPage(page.getPageNo());
		pager.setRows(page.getPageSize());
		pager.setPageOffset((page.getPageNo() - 1) * page.getPageSize() + 1);
		ExecuteResult<DataGrid<QqCustomerDTO>> result = qqCustomerService.selectListByConditionAll(pager, qqCustomerDTO);
		page.setCount(result.getResult().getTotal());
		page.setList(result.getResult().getRows());
		model.addAttribute("page",page);
		return "QQCustomer/QQCustomer";
	}

	/**
	 * @param customerQqNumber
	 * @param request
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public Map<String, Object> addQQCustomer(String customerQqNumber, HttpServletRequest request) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		//客服类型
		qqCustomerDTO.setCustomerType(2);
		//客服腾讯账号
		qqCustomerDTO.setDeletedFlag(0);
		qqCustomerDTO.setIsDefault(0);
		qqCustomerDTO.setCustomerQqNumber(customerQqNumber);
		qqCustomerDTO.setLastUpdateDate(new Date());
		qqCustomerDTO.setCreateDate(new Date());
		ExecuteResult<String> executeResult = qqCustomerService.addQqCustomer(qqCustomerDTO);
		String message = executeResult.getResultMessage();
		retMap.put("message", message);
		return retMap;
	}

	/**
	 * @param id
	 * @return
	 */
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> deleteCustomer(Long id) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		qqCustomerDTO.setId(id);
		qqCustomerDTO.setDeletedFlag(1);
		String message = "";
		if(	qqCustomerService.updateQqCustomer(qqCustomerDTO) > 0){
			message ="删除成功";
		}else{
			message ="删除失败";
		}
		retMap.put("message", message);
		return retMap;
	}

	/**
	 * @param id
	 * @param customerQqNumber
	 * @return
	 */
	@RequestMapping("modify")
	@ResponseBody
	public Map<String, Object> modifyCustomer(Long id, String customerQqNumber) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Pager pager = new Pager();
		String message = "";
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		qqCustomerDTO.setId(id);
		ExecuteResult<DataGrid<QqCustomerDTO>> updateResult = qqCustomerService.selectListByConditionAll(new Pager(), qqCustomerDTO);
		if(null != updateResult.getResult() ){
			qqCustomerDTO = updateResult.getResult().getRows().get(0);
		    qqCustomerDTO.setCustomerQqNumber(customerQqNumber);
		    qqCustomerDTO.setLastUpdateDate(new Date());
		    
		    if(	qqCustomerService.updateQqCustomer(qqCustomerDTO) > 0){
			     message ="修改成功";
		    }else{
			     message ="修改失败";
		    }
		}else{
			message = "修改失败了";
		}
	
		
		retMap.put("message", message);
		return retMap;
	}

	/**
	 * @param customerQQNumber
	 * @return
	 */
	
	@RequestMapping("verify")
	@ResponseBody
	public Map<String, Object> verifyQQCustomer(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		String customerQqNumber =request.getParameter("customerQqNumber");
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		qqCustomerDTO.setCustomerType(2);
		qqCustomerDTO.setDeletedFlag(0);
		qqCustomerDTO.setCustomerQqNumber(customerQqNumber);
		Pager pager = new Pager();
		ExecuteResult<DataGrid<QqCustomerDTO>> result = qqCustomerService.selectListByConditionAll(pager, qqCustomerDTO);
		if(null == result.getResult() ||result.getResult().getRows().size()==0 ){
		retMap.put("verify", false);
		}else{
	    retMap.put("verify", true);
		}
		return retMap;
	}

	
	@RequestMapping("setDefaultCustomer")
	@ResponseBody
	public Map<String, Object> setDefaultCustomer(HttpServletRequest request,HttpServletResponse response) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Pager pager = new Pager();
		Long Id = Long.valueOf(request.getParameter("Id"));
		QqCustomerDTO qqCustomerDTO = new QqCustomerDTO();
		qqCustomerDTO.setCustomerType(2);
		qqCustomerService.updateMRCustomer(qqCustomerDTO);
		qqCustomerDTO.setId(Id);
		ExecuteResult<DataGrid<QqCustomerDTO>> updateResult = qqCustomerService.selectListByConditionAll(new Pager(), qqCustomerDTO);
			qqCustomerDTO = updateResult.getResult().getRows().get(0);
		    qqCustomerDTO.setIsDefault(1);
		    qqCustomerService.updateQqCustomer(qqCustomerDTO);
		return retMap;
	}

}
